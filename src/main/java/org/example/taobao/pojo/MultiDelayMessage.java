package org.example.taobao.pojo;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 1. 由于定时器的原因，每次给延迟队列添加一个延迟时间很长的消息体的时候会导致cpu每次都需要给消息体计算，所以就会导致cpu开销很大
 * 2. 由于大多数用户在生成订单之后就会在短时间内进行支付，所以就可以将这个30分钟拆分成很多的部分，如由10s（后面的时间会越来越长）构成的数组，来进行检测，如果在数组前几段时间中就进行支付，
 * 那么没有必要将这个数据一直存在消息队列中（核心减负思路，减少消息体在消息队列中的时间）
 * 3. 当每经历一个数组就将数组的长度（也可以说是元素减少），当数组长度为0（或者说为空的时候）的时候就说明这个时间到头了，那么就说明用户超过付款时间了
 *
 * @author 关岁安
 * @param <T>
 */
@Data
@NoArgsConstructor
public class MultiDelayMessage<T> implements Serializable {

    //内容的消息体
    private T data;

    //用于计时的数组，单位为毫秒
    private List<Long> delayMillis;

    public MultiDelayMessage(T data,List<Long> delayMillis){
        this.data = data;
        this.delayMillis = delayMillis;
    }

    public static <T> MultiDelayMessage<T> of(T data,Long ... delayMillis){
        return new MultiDelayMessage<>(data, CollUtil.newArrayList(delayMillis));
    }

    //将数组中对应的延迟时间移除
    public Long removeNextDelay(){
        return delayMillis.remove(0);
    }

    //判断用户是否超过付款时间
    public boolean hasNextDelay(){
        return !delayMillis.isEmpty();
    }



}
