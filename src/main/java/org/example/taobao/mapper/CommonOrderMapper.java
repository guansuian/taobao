package org.example.taobao.mapper;

import org.apache.ibatis.annotations.*;
import org.example.taobao.dao.CommonOrderDao;
import org.example.taobao.dto.CommonOrderDto;
import org.example.taobao.pojo.CommonOrder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface CommonOrderMapper {
    void insertCommonOrder(CommonOrderDao commonOrderDao);

    @Select("select id as orderId, user_id as userId ,order_create_date as orderCreateDate,status ,total_price as totalPrice,shopping_id as shoppingId,pay_no as  payNo,pay_time as payTime ,father_order_id as fatherOrderId from common_Order where id = #{orderId}")
    CommonOrder gainCommonOrderList(Long orderId);


    CommonOrder gainCommonOrderByOrderId(Long orderId);


    @Update("update common_order set status = 2 where id = #{orderId}")
    void updateCommonOrderStatus(Long orderId);


    @Update("update common_order set status = 5 , refund_result = #{refundResult} where id = #{orderId}")
    void updateCancelCommonOrder(Long orderId,String refundResult);

    @Delete("DELETE FROM common_order WHERE id = #{orderId}")
    void deleteCancelCommonOrder(Long orderId);

    @Update("update common_order set status = 2,pay_no = #{alipayTradeNo} ,pay_time = #{currentTime} where father_order_id = #{orderId}")
    void updateCommonOrderStatusByFatherId(Long orderId, LocalDateTime currentTime, String alipayTradeNo);


    @Update("update common_order set status = 2,pay_no = #{alipayTradeNo} ,pay_time = #{currentTime} where id = #{orderId}")
    void updateCommonOrderStatusBuOrderId(Long orderId, LocalDateTime currentTime, String alipayTradeNo);


    @Select("select father_order_id from common_order where id = #{orderId}")
    Long gainCommonFatherOrderId(Long orderId);


    List<CommonOrder> gainCommonOrderListByUserId(Integer userId,Integer status);

    List<CommonOrder> gainShopKeeperRefundBuShopppingId(Integer shoppingId);



}
