package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.taobao.vo.CategoryVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface CategoryMapper {

    @Insert("insert into category (name,parent_id,create_time) values (#{parentCategory},0,#{currentTime})")
    void insertParentCategory(String parentCategory, LocalDateTime currentTime);

    @Insert("insert into category (name,parent_id,create_time) values (#{name},#{id},#{currentTime})")
    void insertSonCategory(String name,Integer id,LocalDateTime currentTime);

    @Select("select id from category where name = #{parentCategory} " +
            "LIMIT 1")
    Integer selectExistsCategoryName(String parentCategory);

    @Select("select id,name from category where parent_id = 0 ")
    List<CategoryVo> gainParentCategoryList();

    @Select("select id,name from category where parent_id = #{parentId}")
    List<CategoryVo> gainSonCategoryList(Integer parentId);



}
