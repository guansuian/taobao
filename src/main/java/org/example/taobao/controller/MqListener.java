package org.example.taobao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import org.example.taobao.Content.MqContent;
import org.example.taobao.dao.InsertOrderDao;
import org.example.taobao.mapper.*;
import org.example.taobao.pojo.*;
import org.example.taobao.utils.RedisWorker;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 关岁安
 */


@Component
public class MqListener {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisWorker redisWorker;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CommonFatherOrderMapper commonFatherOrderMapper;

    @Autowired
    private CommonOrderSkuMapper commonOrderSkuMapper;

    @Autowired
    private CommonOrderMapper commonOrderMapper;


    /**
     * 普通订单的延迟消息队列
     * @param msg
     * @throws JsonProcessingException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "delay.queue" ,durable = "true"),
            exchange = @Exchange(value = "delay.direct",delayed = "true"),
            key="hi"
    ))
    public void listenDelayQueue(MultiDelayMessage<Long> msg) throws JsonProcessingException {
        System.out.println(msg);
        //1.查询订单状态
        CommonOrder commonOrder = commonOrderMapper.gainCommonOrderByOrderId(msg.getData());
        if(commonOrder == null){
            System.out.println("买家已经买了1");
            return;
        }
        if(commonOrder.getPayNo() != null){
            System.out.println("买家已经买了");
            //如果从支付宝那里获取到的订单信息为已经支付
            commonOrderMapper.updateCommonOrderStatus(commonOrder.getOrderId());
            return;
        }
        //如果没有超时
        if(msg.hasNextDelay()){
            Long nextDelay = msg.removeNextDelay();
            rabbitTemplate.convertAndSend(MqContent.DELAY_EXCHANGE,MqContent.DELAY_ORDER_ROUTING_KEY,msg,
                    new DelayMessageProcessor(nextDelay.intValue()));
            return;
        }
        //获取到所有的skuID和number
        System.out.println("已经超时了");
        List<AddCommonOrderSkuInventory> addCommonOrderSkuInventories = commonOrderSkuMapper.gainCommonSkuList(msg.getData());
        //如果超时了就修改订单状态然后，回滚库存
        deleteAndAddInventory(msg.getData(),addCommonOrderSkuInventories);
    }
    @Transactional
    public void deleteAndAddInventory(Long orderId,List<AddCommonOrderSkuInventory> list){
        Long fatherCommonOrderId = commonOrderMapper.gainCommonFatherOrderId(orderId);
        commonFatherOrderMapper.deleteCommonFatherOrder(fatherCommonOrderId);
        //1.删除小定单
        commonOrderMapper.deleteCancelCommonOrder(orderId);
        //2.回滚库存
        for (AddCommonOrderSkuInventory addCommonOrderSkuInventory : list) {
            skuMapper.addInventoryAndUpdateStatus(addCommonOrderSkuInventory);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "delay.queue1" ,durable = "true"),
            exchange = @Exchange(value = "delay.direct1",delayed = "true"),
            key="hi"
    ))
    public void listenDelayQueue1(MultiDelayMessage<Long> msg) throws JsonProcessingException {
        System.out.println(msg);
        //1.查询订单状态
        Order order = orderMapper.gainOrder(msg.getData());
        if(order == null || order.getStatus() == 2){
            System.out.println("买家已经买了1");
            return;
        }

        if(order.getPayNo() != null){
            System.out.println("买家已经买了");
            //如果从支付宝那里获取到的订单信息为已经支付
            orderMapper.updateOrderStatus(order.getId());
            return;
        }

        //如果没有超时
        if(msg.hasNextDelay()){
            Long nextDelay = msg.removeNextDelay();
            rabbitTemplate.convertAndSend("delay.direct1","hi",msg,
                    new DelayMessageProcessor(nextDelay.intValue()));
            return;
        }
        System.out.println("已经超时了");
        //如果超时了就删减订单然后，回滚库存,redis这边也需要回滚库存
        String key = "seckill:inventory:" + order.getSkuId();
        String value = redisTemplate.opsForValue().get(key);
        System.out.println("key:" + key);
        System.out.println("value:" + value);
        if (value != null) {
            Sku inventory = objectMapper.readValue(value, Sku.class);
            //回滚库存
            inventory.setInventory(inventory.getInventory() + order.getNumber());
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(inventory));
        }
        deleteOrderAndAddInventory(msg.getData(),order.getSkuId(),order.getNumber());
    }

    @Transactional
    protected void deleteOrderAndAddInventory(Long orderId, Long skuId,Integer number){
        orderMapper.deleteOrder(orderId);
        skuMapper.addInventory(skuId,number);
    }

    @RabbitListener(queues = "taobao.test")
    public void receiveMessage(InsertOrderDao message) throws JsonProcessingException {
        System.out.println(message);
        try {
            insertOrderAndReduceInventory(message);
//            MultiDelayMessage<Long> msg = MultiDelayMessage.of(message.getId(), 10000L,10000L,10000L,15000L,15000L,60000L,3*60000L,5*60000L,300000L,600000L);
            MultiDelayMessage<Long> msg = MultiDelayMessage.of(message.getId(), 10000L,10000L,10000L,15000L,15000L,60000L,3*60000L,5*60000L,300000L,600000L);
            rabbitTemplate.convertAndSend(
                    "delay.direct1", "hi", msg,
                    new DelayMessageProcessor(msg.removeNextDelay().intValue())
            );
        } catch (Exception e) {
            //这里需要生成一个异常订单
            insertExOrderAndAddInventory(message);
            throw new RuntimeException(e);
        }

        //发送延迟消息队列
//        MultiDelayMessage<Long> msg = MultiDelayMessage.of(message.getId(), 10000L,10000L,10000L,15000L,15000L,60000L,3*60000L,5*60000L,300000L,600000L);

//        System.out.println("Received message: " + message);
    }
    @Transactional
    protected void insertOrderAndReduceInventory(InsertOrderDao message){
        orderMapper.insertSeckillerOrder(message);
        int updateNumber = skuMapper.reduceSecSkuInventory(message);
        if(updateNumber == 0){
            System.out.println("没有将这个进行减少 说名和redis没有保证一致性");
        }
    }

    @Transactional
    protected void insertExOrderAndAddInventory(InsertOrderDao message) throws JsonProcessingException {
        Long id = redisWorker.nextId("order");
        message.setId(id);
        skuMapper.addInventoryInEx(message);
        orderMapper.insertExOrderSeckillerOrder(message);
        //在这里要设置redis的值回滚
        String key = "seckill:inventory:" + message.getSkuId();
        String value =  redisTemplate.opsForValue().get(key);
        if (value != null) {
            Sku inventory = objectMapper.readValue(value, Sku.class);
            //回滚库存
            inventory.setInventory(inventory.getInventory() + message.getNumber());
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(inventory));
        }

    }



}
