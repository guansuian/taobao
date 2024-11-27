package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.taobao.pojo.SkuAttribute;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface AttributeMapper {


    @Insert("insert into attribute (id,name,attr_type,category_id,create_time,spu_id) values (#{id},#{name},0,#{categoryId},#{createTime},#{spuId})")
    void insertBasicAttribute(Long id, String name, Integer categoryId, LocalDateTime createTime,Long spuId);

    @Insert("insert into attribute(id,name,options,attr_type,category_id,create_time,spu_id) values (#{id},#{name},#{options},1,#{categoryId},#{createTime},#{spuId})")
    void insertSaleAttribute(Long id,String name,String options,Integer categoryId,LocalDateTime createTime,Long spuId);

    @Select("select id as attributeId,name,options,category_id as categoryId from attribute where spu_id=#{spuId} and attr_type = 1")
    List<SkuAttribute> gainSkuAttribute(Integer spuId);

    @Select("select name from attribute where id = #{attrId}")
    String gainSkuAttributeName(Integer attrId);
}
