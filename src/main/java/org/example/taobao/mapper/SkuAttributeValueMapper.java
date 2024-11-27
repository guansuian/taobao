package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.taobao.pojo.SkuAttribute;
import org.example.taobao.pojo.SkuAttributeValue;
import org.example.taobao.pojo.SkuSonAttribute;

import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface SkuAttributeValueMapper {

    @Insert("insert into sku_attribute_value(id,sku_id,attr_id,attr_value,price,url) values (#{id} ,#{skuId},#{attrId},#{attrValue},#{price},#{url})")
    void insertSkuAttribute(Long id,Long skuId,Long attrId,String attrValue,Integer price, String url);

    @Select("SELECT url FROM sku_attribute_value WHERE attr_value = #{value} AND attr_id = #{fatherAttributeId} LIMIT 1")
    String gainUrl(String value, Integer fatherAttributeId);

    @Select("select attr_id as attrId , attr_value as attrValue from sku_attribute_value where sku_id = #{skuId}")
    List<SkuAttributeValue> gainSkuAttribute(Long skuId);
}
