package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface SpuImgMapper {

   @Insert("insert into spu_img (spu_id,url) values (#{spuId},#{url})")
    void uploadSpuImg(String url,Integer spuId);

   @Select("select url from spu_img where spu_id = #{spuId}")
   List<String> gainSpuImgs(Integer spuId);

}
