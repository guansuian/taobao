package org.example.taobao;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import org.example.taobao.dto.PageCategoryDto;
import org.example.taobao.mapper.OrderMapper;
import org.example.taobao.mapper.SkuAttributeValueMapper;
import org.example.taobao.mapper.SkuMapper;
import org.example.taobao.mapper.SpuMapper;
import org.example.taobao.pojo.Order;
import org.example.taobao.utils.RedisWorker;
import org.example.taobao.vo.SimplyGoodsVo;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
public class test {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    RedisWorker redisWorker;

    @Test
    void gainSeckillerGoods(){
        PageCategoryDto pageCategoryDto = new PageCategoryDto(1,10,11);
        List<SimplyGoodsVo> simplyGoodsVos = spuMapper.gainSeckillerGoods(pageCategoryDto);
        System.out.println(simplyGoodsVos);
    }


    @Test
    void gainId(){
        System.out.println(redisWorker.nextId("order"));
    }



    @Test
    void gainIsmMao(){
        Integer isMao = spuMapper.gainIsMao(865920058L);
        System.out.println(isMao);
    }





    @Test
    void testSendDelayMessage(){
        rabbitTemplate.convertAndSend("delay.direct", "hi", "你好世界", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDelay(10000);
                return message;
            }
        });
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println(currentTime);
        System.out.println("发送消息成功");
//        log.info("消息发送成功");
    }


    @Test
    void testPublisherConfirm(){
        Message message = MessageBuilder.withBody("你好".getBytes(StandardCharsets.UTF_8))
                        .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT).build();
        for(int i = 1;i<=1000000;i++){
            rabbitTemplate.convertAndSend("taobao.test",message);
        }
    }


    @Test
    public void test() {
        String queueName = "taobao.test";
        String msg = "你好世界，我是关岁安";
        rabbitTemplate.convertAndSend(queueName,msg);
    }

    @Test
    public void test1(){
        skuMapper.reduceInventory(255761452L);
    }

//    @Test
//    public void test2(){
//        LocalDateTime currentTime = LocalDateTime.now();
//        orderMapper.insertOrder(1,2,3,4,currentTime,10,"零食好忙",1000);
//    }
//
//    @Test
//    public void test3(){
//        Order order = orderMapper.gainOrder(487853472);
//        System.out.println(order);
//    }
}