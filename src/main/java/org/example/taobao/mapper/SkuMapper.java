package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.taobao.dao.InsertOrderDao;
import org.example.taobao.pojo.AddCommonOrderSkuInventory;
import org.example.taobao.pojo.Sku;
import org.example.taobao.vo.SkuVo;

import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface SkuMapper {

    @Insert("insert into sku(id,spu_id,price,shopping_id,status,inventory) values (#{id},#{spuId},#{price},#{shoppingId},1,#{inventory})")
    void insertSku(Long id,Long spuId,Integer price,Integer shoppingId,Integer inventory);

    @Select("select id as skuId,price ,shopping_id as shoppingId,inventory from sku where spu_id = #{spuId}")
    List<SkuVo> gainSkuList(Integer spuId);

    @Select("select price from sku where id = #{skuId}")
    Integer gainSkuPrice(Long skuId);

    @Select("select id,spu_id as spuId ,price,shopping_id as shoppingId , status ,inventory from sku where id = #{skuId}")
    Sku checkSku(Long shuId);

    @Update("update sku set inventory = inventory - 1 where id = #{skuId} and inventory > 0")
    int reduceInventory(Long skuId);

    @Update("update sku set inventory = inventory - #{number} where id = #{skuId} and inventory - #{number} + 1 > 0")
    int reduceInventory2(Long skuId,Integer number);


    @Update("update sku set inventory = inventory + #{number} where id = #{skuId}")
    int addInventory(Long skuId,Integer number);

    @Update("update sku set inventory = inventory - #{number} where id = #{skuId} and inventory > 0")
    int reduceSecSkuInventory(InsertOrderDao insertOrderDao);

    @Update("update sku set inventory = inventory + #{number} where id = #{skuId} and inventory > 0")
    void addInventoryInEx(InsertOrderDao message);


    @Select ("select spu_id from sku where id = #{skuId}")
    Long gainSpuIdBySkuId(Long skuId);

    @Update("update sku set inventory = inventory + #{number} where id = #{skuId} ")
    void addInventoryAndUpdateStatus(AddCommonOrderSkuInventory addCommonOrderSkuInventory);
}
