package org.example.taobao.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 关岁安
 */
@Mapper
public interface SpuAttributeValueMapper {

    @Insert("insert into spu_attribute_value (spu_id,attr_id,attr_value) values (#{spuId},#{attrId},#{attrValue})")
    void insertBasicAttributeValue(Long spuId,Long attrId,String attrValue);

}
