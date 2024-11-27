package org.example.taobao.service.impl;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.taobao.Content.MqContent;
import org.example.taobao.dao.CommonOrderDao;
import org.example.taobao.dao.CommonOrderSkuDao;
import org.example.taobao.dto.CommonOrderDto;
import org.example.taobao.dto.CommonOrderSkuDto;
import org.example.taobao.dto.CommonPayOrderDto;
import org.example.taobao.dto.RefundDto;
import org.example.taobao.mapper.*;
import org.example.taobao.pojo.*;
import org.example.taobao.service.CommonOrderService;
import org.example.taobao.utils.RedisWorker;
import org.example.taobao.vo.CommonOrderSkuVo;
import org.example.taobao.vo.CommonOrderVo;
import org.example.taobao.vo.Result;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 关岁安
 */

@Service
public class CommonOrderServiceImpl implements CommonOrderService {

    @Autowired
    private SkuMapper skuMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisWorker redisWorker;

    @Autowired
    private CommonOrderMapper commonOrderMapper;

    @Autowired
    private CommonOrderSkuMapper commonOrderSkuMapper;

    @Autowired
    private CommonFatherOrderMapper commonFatherOrderMapper;

    @Autowired
    private SkuAttributeValueMapper skuAttributeValueMapper;

    @Autowired
    private AttributeMapper attributeMapper;

    /**
     * 如果生成订单成功了 应该返回一个字符串集合 这集合集合中含有对应订单id
     * @param commonOrderDtoList
     * @return
     */




    @Override
    @Transactional()
    public Result insertCommonOrderList(List<CommonOrderDto> commonOrderDtoList) {

        List<Long> orderIdList = new ArrayList<>();
        Integer totalPrice = 0;
        Long fatherOrderId = redisWorker.nextId("fatherOrder");
        String fatherOrderIdStr = String.valueOf(fatherOrderId);

        for (CommonOrderDto commonOrderDto : commonOrderDtoList) {
            totalPrice += commonOrderDto.getTotalPrice();
            for (CommonOrderSkuDto commonOrderSkuDto : commonOrderDto.getCommonOrderSkuDtoList()) {

                Sku sku = skuMapper.checkSku(commonOrderSkuDto.getSkuId());
                if(sku.getInventory() < 1){
                    System.out.println("已经查过库存了");
                    return Result.error(commonOrderSkuDto.getSpuName() + "超过库存了");
                }
            }
        }

        for (CommonOrderDto commonOrderDto : commonOrderDtoList) {
            for (CommonOrderSkuDto commonOrderSkuDto : commonOrderDto.getCommonOrderSkuDtoList()) {
                System.out.println(commonOrderSkuDto.getNumber());
                int tmp = skuMapper.reduceInventory2(commonOrderSkuDto.getSkuId(),commonOrderSkuDto.getNumber());
                // 如果 tmp == 0，表示没有进行库存修改，则回滚之前的操作
                if (tmp == 0) {
                    System.out.println("已经查过库存了1");
                    throw new RuntimeException(commonOrderSkuDto.getSpuName() + " 超过库存了");
                }
            }
        }

        for (CommonOrderDto commonOrderDto : commonOrderDtoList) {
            Long id  = redisWorker.nextId("commonOrder");
            LocalDateTime currentTime = LocalDateTime.now();
            orderIdList.add(id);
            CommonOrderDao commonOrderDao = new CommonOrderDao(
                    fatherOrderId,
                    currentTime,
                    id,
                    commonOrderDto.getUserId(),
                    commonOrderDto.getTotalPrice(),
                    commonOrderDto.getShoppingId(),
                    commonOrderDto.getShoppingName()
            );

                commonOrderMapper.insertCommonOrder(commonOrderDao);

            for (CommonOrderSkuDto commonOrderSkuDto : commonOrderDto.getCommonOrderSkuDtoList()) {
                CommonOrderSkuDao commonOrderSkuDao = new CommonOrderSkuDao(
                        commonOrderSkuDto.getSkuId(),
                        commonOrderSkuDto.getSpuId(),
                        commonOrderSkuDto.getNumber(),
                        commonOrderSkuDto.getPrice(),
                        commonOrderSkuDto.getSpuName(),
                        commonOrderSkuDto.getSpuImg(),
                        id
                );
                commonOrderSkuMapper.insertShoppingSku(commonOrderSkuDao);

            }
        }
        commonFatherOrderMapper.insertCommonFatherOrder(totalPrice,fatherOrderId);
//        MultiDelayMessage<Long> msg = MultiDelayMessage.of(fatherOrderId, 10000L,10000L,10000L,15000L,15000L,60000L,3*60000L,5*60000L,300000L,600000L);
        for (Long orderId : orderIdList) {
            MultiDelayMessage<Long> msg = MultiDelayMessage.of(orderId, 10000L,10000L,10000L,15000L,15000L,60000L,3*60000L,5*60000L,300000L,600000L);
            rabbitTemplate.convertAndSend(
                    MqContent.DELAY_EXCHANGE, MqContent.DELAY_ORDER_ROUTING_KEY, msg,
                    new DelayMessageProcessor(msg.removeNextDelay().intValue())
            );
        }
        return Result.success(fatherOrderIdStr);
    }

    @Override
    public Result gainCommonPayOrderList(CommonPayOrderDto commonPayOrderDto) {
        return  gainCommonOrderList(commonPayOrderDto,1);
    }

    @Override
    public Result refundCommonOrder( RefundDto refundDto) {
        Long orderIdLong = Long.parseLong(refundDto.getOrderId());
        System.out.println(refundDto.getRefundResult());
        commonOrderMapper.updateCancelCommonOrder(orderIdLong,refundDto.getRefundResult());
        return Result.success("已经通知商家进行退款");
    }

    @Override
    public Result gainRefundCommonOrderList(Integer shoppingId) {
        return gainCommonOrderList(shoppingId,2);
    }

    @Override
    public Result cancelRefundCommonOrder(String orderId) {
        Long orderIdLong = Long.parseLong(orderId);
         commonOrderMapper.updateCommonOrderStatus(orderIdLong);
         return Result.success("修改成功");
    }

    @Override
    public Result deleteCommonOrder(String orderId) {
        Long orderIdLong = Long.parseLong(orderId);
        List<AddCommonOrderSkuInventory> addCommonOrderSkuInventories = commonOrderSkuMapper.gainCommonSkuList(orderIdLong);
        for (AddCommonOrderSkuInventory addCommonOrderSkuInventory : addCommonOrderSkuInventories) {
            skuMapper.addInventory(addCommonOrderSkuInventory.getSkuId(),addCommonOrderSkuInventory.getNumber());
        }
        commonOrderMapper.deleteCancelCommonOrder(orderIdLong);
        return Result.success("成功取消订单");
    }


    Result gainCommonOrderList(Object o,int type){
        List<CommonOrder> commonOrderList = new ArrayList<>();
        List<CommonOrderVo> returnList = new ArrayList<>();
        if(type == 1){
            //如果是1的话，那么就代表着获取的已经进行支付的CommonOrderList
            CommonPayOrderDto commonPayOrderDto = (CommonPayOrderDto) o;
            //如果是1获取已经支付的普通订单
            commonOrderList = commonOrderMapper.gainCommonOrderListByUserId(commonPayOrderDto.getUserId(),commonPayOrderDto.getStatus());
        }else if(type == 2){
            Integer shoppingId = (Integer) o;
            commonOrderList = commonOrderMapper.gainShopKeeperRefundBuShopppingId(shoppingId);
        }
        for (CommonOrder commonOrder : commonOrderList) {
            List<CommonOrderSkuVo> commonOrderSkuVos = commonOrderSkuMapper.gainCommonOrderSkuVo(commonOrder.getOrderId());
            for (CommonOrderSkuVo commonOrderSkuVo : commonOrderSkuVos) {
                String orderId = String.valueOf(commonOrder.getOrderId());
                commonOrderSkuVo.setOrderId(orderId);
                List<SkuAttributeValue> skuAttributeValueList = skuAttributeValueMapper.gainSkuAttribute(commonOrderSkuVo.getSkuId());
                for (SkuAttributeValue skuAttributeValue : skuAttributeValueList) {
                    Integer attributeId = skuAttributeValue.getAttrId();
                    String name = attributeMapper.gainSkuAttributeName(attributeId);
                    skuAttributeValue.setAttrName(name);
                }
                commonOrderSkuVo.setSkuAttributeValues(skuAttributeValueList);
            }
            CommonOrderVo commonOrderVo = new CommonOrderVo(
                    commonOrder.getRefundResult(),
                    String.valueOf(commonOrder.getOrderId()),
                    commonOrder.getUserId(),
                    commonOrder.getOrderCreateDate(),
                    commonOrder.getStatus(),
                    commonOrder.getTotalPrice(),
                    commonOrder.getShoppingId(),
                    commonOrder.getPayNo(),
                    commonOrder.getPayTime(),
                    commonOrder.getShoppingName(),
                    String.valueOf(commonOrder.getFatherOrderId()),
                    commonOrderSkuVos
            );
            returnList.add(commonOrderVo);
        }
        for (CommonOrderVo commonOrderVo : returnList) {
            System.out.println(commonOrderVo);
        }
        return Result.success(returnList);

    }




}
