package org.example.taobao.Content;

/**
 * @author 关岁安
 */
public interface MqContent {
    //延迟消息的交换机
    String DELAY_EXCHANGE = "delay.direct";
    //延迟消息的队列
    String DELAY_ORDER_QUEUE = "delay.queue";

    //延迟消息的路由
    String DELAY_ORDER_ROUTING_KEY = "hi";

    // 延迟消息的时长
    Long[] DELAY_TIMES = {10000L, 10000L, 10000L, 15000L, 15000L};
}
