package org.example.taobao.service;

import org.example.taobao.dto.OrderShowDto;
import org.example.taobao.dto.SkuDto;
import org.example.taobao.vo.OrderShowVo;
import org.example.taobao.vo.SkuAttributeVo;
import org.example.taobao.vo.SkuVo;

import java.util.List;

/**
 * @author 关岁安
 */
public interface SkuService {

    void insertSku(SkuDto skuDto);

    List<SkuAttributeVo> gainSkuAttribute(Integer spuId);

    List<SkuVo> gainSku(Integer spuId);


    List<OrderShowVo> gainOrderShowVoList(List<OrderShowDto> orderShowDto);

    Integer checkSkuInventory(Long skuId);


}
