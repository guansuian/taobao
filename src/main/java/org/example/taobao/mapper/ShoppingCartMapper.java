package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.taobao.dao.InsertShoppingCartDao;
import org.example.taobao.pojo.ShoppingCartSku;
import org.example.taobao.vo.ShoppingCartVo;

import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface ShoppingCartMapper {

    @Insert("insert into shopping_cart (sku_id,shopping_id,user_id,number,spu_id,price,head,spu_name) values (#{skuId},#{shoppingId},#{userId},#{number},#{spuId},#{price},#{head},#{spuName})")
    void insertShoppingCart(InsertShoppingCartDao insertShoppingCartDao);

    @Select("select sku_id as skuId, spu_id as spuId , shopping_id as shoppingId , user_id as userId ,number,price, head ,spu_name as spuName from shopping_cart where user_id = #{userId}")
    List<ShoppingCartVo> gainShoppingCartListBuUserId(Integer userId);


    @Select("select distinct shopping_id from shopping_cart where user_id = #{userId}")
    List<Integer> gainShoppingIdList(Integer userId);


    @Select("select id ,sku_id as skuId,shopping_id as shoppingId,user_id as userId,number ,spu_id as spuId,price,head from shopping_cart where sku_id = #{skuId} limit 1")
    ShoppingCartSku gainShoppingCartSku(Long skuId);


    @Update("update shopping_cart set number = number + #{number} where sku_id = #{skuId}")
    void updateSkuNumber(ShoppingCartSku shoppingCartSku);
}
