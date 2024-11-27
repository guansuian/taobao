package org.example.taobao.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.taobao.Content.MqContent;
import org.example.taobao.dao.InsertOrderDao;
import org.example.taobao.dto.DeleteAndReturnInventoryDto;
import org.example.taobao.dto.OrderDto;
import org.example.taobao.dto.OrderPageDto;
import org.example.taobao.mapper.OrderMapper;
import org.example.taobao.mapper.SkuMapper;
import org.example.taobao.mapper.SpuMapper;
import org.example.taobao.pojo.DelayMessageProcessor;
import org.example.taobao.pojo.MultiDelayMessage;
import org.example.taobao.pojo.Order;
import org.example.taobao.pojo.Sku;
import org.example.taobao.service.OrderService;
import org.example.taobao.utils.RedisWorker;
import org.example.taobao.vo.OrderVo;
import org.example.taobao.vo.PageVo;
import org.example.taobao.vo.Result;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.util.*;

import static org.example.taobao.utils.IdGenerationTest.generateId;

/**
 * @author 关岁安
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SpuMapper spuMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Resource
    private RedisTemplate<String,Object> stringRedisTemplate;

    @Autowired
    private RedisWorker redisWorker;


    /**
     * 查询没有秒杀标记的商品
     *
     * @param list
     * @return
     */
    @Override
    public Result insertOrder(List<OrderDto> list) throws JsonProcessingException {
        for (OrderDto orderDto : list) {
            Integer userId = orderDto.getUserId();
            Long spuId = orderDto.getSpuId();
            LocalDateTime currentTime = LocalDateTime.now();
            Integer number = orderDto.getNumber();
            String spuName = orderDto.getSpuName();
            Integer totalPrice = orderDto.getTotalPrice();
            //1.查对应的商品
            Long skuId = orderDto.getSkuId();
            //2.获取到对应的库存
            Sku sku = skuMapper.checkSku(skuId);
            Integer inventory = sku.getInventory();
            if(inventory < 1){
                System.out.println("库存不足1");
                return Result.error("库存不足:" + skuId);
            }
           return insertOrderPart(userId,spuId,currentTime,number,spuName,totalPrice,skuId);
        }
        System.out.println("我想看看受否已经跳出循环");
        return Result.error("我想看看是不是没有返回这个值");
    }

    @Transactional
    public Result insertOrderPart(Integer userId,Long spuId,LocalDateTime currentTime,Integer number,String spuName,Integer totalPrice,Long skuId) throws JsonProcessingException {
        //3.减少库存

        int tmp = skuMapper.reduceInventory(skuId);
        System.out.println(tmp);
        if(tmp <=0){
            System.out.println("库存不足2");
            return Result.error("库存不足");
        }

        Long id = generateId();
        //4.将订单插入订单表
        orderMapper.insertOrder(id,userId,spuId,skuId,currentTime,number,spuName,totalPrice);
        System.out.println("已经订单插入数据库了");
        //5.发送延迟消息
        try {
//            MultiDelayMessage<Integer> msg = MultiDelayMessage.of(id, 10000L,10000L,10000L,15000L,15000L,60000L,3*60000L,5*60000L,300000L);
            MultiDelayMessage<Long> msg = MultiDelayMessage.of(id, 10000L,10000L,10000L,15000L,15000L,60000L,3*60000L,5*60000L,300000L,600000L);
            rabbitTemplate.convertAndSend(
                    MqContent.DELAY_EXCHANGE, MqContent.DELAY_ORDER_ROUTING_KEY, msg,
                    new DelayMessageProcessor(msg.removeNextDelay().intValue())
            );
        } catch (AmqpException e) {
            throw new RuntimeException(e);
        }
        return Result.success(id);
    }

    /**
     * 秒杀商品使用消息队列来实现
     * @param list
     * @return
     */

    @Override
    public Result insertInstantOrder(List<OrderDto> list) {
        for (OrderDto orderDto : list) {
            Integer number = orderDto.getNumber();
            Long skuId = orderDto.getSkuId();
            Long result = subSpecsStock(skuId,number);
            if(result == -1){
                System.out.println("库存不足");
                return Result.error("库存不足");
            }else{
                Long id = redisWorker.nextId("order");
                // 将 Long 转换为字符串
                String idString = String.valueOf(id);
                LocalDateTime currentTime = LocalDateTime.now();
                InsertOrderDao insertOrderDao = new InsertOrderDao(id,orderDto.getUserId(),orderDto.getSkuId(),
                        orderDto.getSpuId(),currentTime,orderDto.getNumber(),orderDto.getSpuName(),orderDto.getTotalPrice());
                rabbitTemplate.convertAndSend("taobao.test",insertOrderDao);
                return Result.success(idString);
            }
        }
        return null;
    }

    /**
     *调用lua脚本来实现减库存
     */
    public Long subSpecsStock(Long id, Integer sub) {
        //使用lua脚本保证原子性
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText("if redis.call('exists', KEYS[1]) == 1 then\n" +
                "    local data = cjson.decode(redis.call('get', KEYS[1]))\n" +
                "    data.inventory = tonumber(data.inventory) - tonumber(ARGV[1])\n" +
                "    if data.inventory >= 0 then\n" +
                "    redis.call('set', KEYS[1], cjson.encode(data))\n" +
                "        return data.inventory\n" +
                "    else\n" +
                "        return -1\n" +
                "    end\n" +
                "end\n" +
                "return -1");
        redisScript.setResultType(Long.class);
        Long result = (Long) stringRedisTemplate.execute(redisScript, new ArrayList<String>() {{
            add("seckill:inventory:" + id);
        }}, String.valueOf(sub));
        return result;
    }



    @Override
    public Result gainOrderVoListByUserId(Integer userId) {
        List<Order> orders = orderMapper.gainOrderList(userId);
        List<OrderVo> orderVos = new ArrayList<>();
        for (Order order : orders) {
            Long spuId = order.getSpuId();
            String head = spuMapper.gainSouHead(spuId);
            OrderVo orderVo = new OrderVo(order,head);
            orderVos.add(orderVo);
        }
        return Result.success(orderVos);
    }

    @Override
    public Result gainHasNoPayOrder(Integer userId, Integer page, Integer pageSize) {
        //获取所有的记录数
        Long sumOrderNumber = Long.valueOf(orderMapper.gainNoPayOrderNumber(userId));
        //获取到偏移量
        Integer offset = (page-1) * pageSize;
        System.out.println("偏移量为：" + offset);
        List<Order> orderList = orderMapper.gainNoOrderList(userId,pageSize,offset);
        System.out.println("获取的订单为：" + orderList);
        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            Long spuId = order.getSpuId();
            String head = spuMapper.gainSouHead(spuId);
            OrderVo orderVo = new OrderVo(order,head);
            orderVoList.add(orderVo);
        }
        PageVo pageVo = new PageVo(sumOrderNumber,orderVoList);
        return Result.success(pageVo);
    }

    @Override
    public Result gainHasPayOrder(OrderPageDto orderPageDto) {
        Integer userId = orderPageDto.getUserId();
        Integer page = orderPageDto.getPage();
        Integer pageSize = orderPageDto.getPageSize();
        //获取所有的记录
        Long sumOrderNumber = Long.valueOf(orderMapper.gainHasPayOrderNumber(userId));
        Integer offset = (page - 1)*pageSize;
        System.out.println("在获取已经支付的操作中：" + offset);
        List<Order> orderList = orderMapper.gainHasPayOrder(userId,pageSize,offset);
        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            Long spuId = order.getSpuId();
            String head = spuMapper.gainSouHead(spuId);
            OrderVo orderVo = new OrderVo(order,head);
            orderVoList.add(orderVo);
        }
        PageVo pageVo = new PageVo(sumOrderNumber,orderVoList);
        return Result.success(pageVo);
    }


    /**
     * 简单解释一下这个lua脚本
     * 由于脚本相当以一个文件 所以可以将这个脚本设置为一个静态不可改变的值
     * 为了可以调用的这个脚本 所以就需要给内存添加一个地方给其使用
     * DefaultRedisScript<Long>中的Long就是返回值类型
     */
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("buyInstant.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }



    @Override
    public Result deleteAndReturnInventory(DeleteAndReturnInventoryDto deleteAndReturnInventoryDto) {
        Long orderId = deleteAndReturnInventoryDto.getOrderId();
        Long skuId = deleteAndReturnInventoryDto.getSkuId();
        Order order = orderMapper.gainOrder(orderId);
        try {
            deleteOrderAndAddInventory(orderId,skuId,order.getNumber());
            return Result.success("删除和回滚成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error("删除或者是回滚出现了问题");
        }
    }



    @Transactional
    protected void deleteOrderAndAddInventory(Long orderId, Long skuId,Integer number){
        orderMapper.deleteOrder(orderId);
        skuMapper.addInventory(skuId,number);
    }




}


