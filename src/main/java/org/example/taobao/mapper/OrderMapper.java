package org.example.taobao.mapper;

import org.apache.ibatis.annotations.*;
import org.example.taobao.dao.InsertOrderDao;
import org.example.taobao.pojo.Order;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface OrderMapper {
    @Insert("insert into `order` (id,user_id,spu_id,sku_id,order_create_date,number,spu_name,total_price,status) values (#{id},#{userId},#{spuId},#{skuId},#{currentTime},#{number},#{spuName},#{totalPrice},1)")
    void insertOrder(Long id,Integer userId, Long spuId, Long skuId, LocalDateTime currentTime, Integer number, String spuName, Integer totalPrice);


    void insertSeckillerOrder(InsertOrderDao message);

    void insertExOrderSeckillerOrder(InsertOrderDao message);

    @Select("select id,user_id as userId,order_create_date as orderCreateTime,number,status,spu_id as spuId,spu_name as spuName,total_price as totalPrice,pay_no as PayNo,pay_time as payTime ,sku_id as skuId from `order` where id = #{id}")
    Order gainOrder(Long id);



    @Update("update `order` set pay_no = #{payId},pay_time = #{localDateTime} , status = 2 where id = #{orderId}")
    void updateOrderHasPay(Long orderId, LocalDateTime localDateTime, String payId);

    @Update("update `order` set status = 2 where id = #{orderId}")
    void updateOrderStatus(Long orderId);


    @Delete("DELETE FROM `order` WHERE id = #{orderId}")
    void deleteOrder(Long orderId);

    @Select("select id,user_id as userId,order_create_date as orderCreateTime,number,status,spu_id as spuId,spu_name as spuName,total_price as totalPrice,pay_no as PayNo,pay_time as payTime ,sku_id as skuId from `order` where user_id = #{userId} ")
    List<Order> gainOrderList(Integer userId);

    @Select("select id,user_id as userId,order_create_date as orderCreateTime,number,status,spu_id as spuId,spu_name as spuName,total_price as totalPrice,pay_no as PayNo,pay_time as payTime ,sku_id as skuId from `order` where user_id = #{userId} and status=1 LIMIT #{pageSize} OFFSET #{offset}")
    List<Order> gainNoOrderList(Integer userId,Integer pageSize,Integer offset);

    @Select("select count(*) from `order` where user_id = #{userId} and status = 1")
    Integer gainNoPayOrderNumber(Integer userId);

    @Select("select count(*) from `order` where user_id = #{userId} and status = 2")
    Integer gainHasPayOrderNumber(Integer userId);
    @Select("select id,user_id as userId,order_create_date as orderCreateTime,number,status,spu_id as spuId,spu_name as spuName,total_price as totalPrice,pay_no as PayNo,pay_time as payTime ,sku_id as skuId from `order` where user_id = #{userId} and status=2 LIMIT #{pageSize} OFFSET #{offset}")
    List<Order> gainHasPayOrder(Integer userId, Integer pageSize, Integer offset);



}
