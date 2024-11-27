package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.taobao.dto.PageCategoryDto;
import org.example.taobao.vo.GoodsVo;
import org.example.taobao.vo.SimplyGoodsVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface SpuMapper {

    @Insert("insert into spu (id,name,title,category_id,status,create_time,shop_id,synthesis,is_mao) values (#{id}, #{name},#{title},#{categoryId},1,#{createTime},#{shopId},1,0)")
    void insertSpu(Long id, String name,String title, Integer categoryId, LocalDateTime createTime, Integer shopId);

    @Insert("insert into spu (id,name,title,category_id,status,create_time,shop_id,synthesis,is_mao) values (#{id}, #{name},#{title},#{categoryId},1,#{createTime},#{shopId},1,1)")
    void insertIsSpu(Long id, String name,String title, Integer categoryId, LocalDateTime createTime, Integer shopId);

    @Update("update spu set head = #{url} where id = #{spuId}")
    void uploadSpuMainImg(String url,Integer spuId);

    @Update("update spu set one_price = #{onePrice} where id=#{id}")
    void uploadOnePrice(String onePrice,Long id);

    String gainShoppingNameBySpuId(Long spuId);

    @Select("select id ,name ,title,head,one_price as onePrice from spu where is_mao = 0")
    List<SimplyGoodsVo> gainSimplyList();

    @Select("select id,name,title,description,category_id as categoryId,create_time as createTime, shop_id as shopId, head,one_price as onePrice,synthesis , is_mao as isMao from spu where id = #{id}")
    GoodsVo gainGoods(Integer id);

    @Select("select name from spu where id = #{spuId}")
    String gainSpuName(Long spuId);

    @Select("select head from spu where id = #{spuId}")
    String gainSouHead(Long spuId);

    @Select("select is_mao  from spu where id = #{spuId}")
    Integer gainIsMao(Long spuId);

    @Select("select id,name,title,head,one_price as onePrice from spu where is_mao = 1 and category_id = #{categoryId}")
    List<SimplyGoodsVo> gainSeckillerGoods(PageCategoryDto pageCategoryDto);
}
