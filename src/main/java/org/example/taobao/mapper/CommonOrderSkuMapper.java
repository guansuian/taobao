package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.taobao.dao.CommonOrderSkuDao;
import org.example.taobao.dto.CommonOrderSkuDto;
import org.example.taobao.pojo.AddCommonOrderSkuInventory;
import org.example.taobao.vo.CommonOrderSkuVo;


import java.util.List;

/**
 * @author 关岁安
 */
@Mapper
public interface CommonOrderSkuMapper {


    void insertShoppingSku(CommonOrderSkuDao commonOrderSkuDao);


    @Select("select sku_id as skuId, number from common_order_sku where order_id = #{orderId}")
    List<AddCommonOrderSkuInventory> gainCommonSkuList(Long orderId);


    @Select("select sku_id as skuId,spu_id as spuId,number,price,spu_name as spuName,spu_img as spuImg from common_order_sku where order_id = #{orderId}")
    List<CommonOrderSkuVo> gainCommonOrderSkuVo(Long orderId);
}
