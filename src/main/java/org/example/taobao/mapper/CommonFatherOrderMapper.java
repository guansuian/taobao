package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.taobao.pojo.CommonFatherOrder;

/**
 * @author 关岁安
 */
@Mapper
public interface CommonFatherOrderMapper {


    @Insert("insert into common_father_order (common_father_order_id,total_price) values (#{fatherOrderId},#{totalPrice})")
    void insertCommonFatherOrder(Integer totalPrice, Long fatherOrderId) ;

    @Select("select total_price as totalPrice , common_father_order_id as commonFatherOrderId from common_father_order where common_father_order_id = #{id}")
    CommonFatherOrder gainTotalPrice(Long id);

    @Delete("delete from common_father_order WHERE common_father_order_id = #{orderId}")
    void deleteCommonFatherOrder(Long orderId);
}
