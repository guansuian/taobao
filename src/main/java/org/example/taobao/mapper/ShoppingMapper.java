package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.taobao.vo.ShoppingVo;
import org.ini4j.Ini;

import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface ShoppingMapper {

    @Insert("INSERT INTO shop (shopkeeper_id,name,type,head,address,username,pass,content) VALUES (#{shopkeeperId},#{name},#{type},#{head},#{address},#{username},'no',#{content})")
    void registerShopping(Integer shopkeeperId,String name,String type ,String head,String address,String username,String content);

    @Select("SELECT * FROM shop WHERE shopkeeper_id = #{id}")
    List<ShoppingVo> gainShops(Integer id);


    ShoppingVo gainShopVoByShopId(Long spuId);

    @Select("SELECT * FROM shop WHERE pass = 'no'")
    List<ShoppingVo> gainNoShopsList();

    List<ShoppingVo> gainNoPassShop(Integer id);

    List<ShoppingVo> gainYesPassShop(Integer id);

    @Update("update shop set head = #{head} , pass = 'no' where id = #{id}")
    void changeShoppingHead(String head,Integer id);


    @Select("select name from shop where id = #{shoppingId}")
    String gainShopName(Integer shoppingId);
}
