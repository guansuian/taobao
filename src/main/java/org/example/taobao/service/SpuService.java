package org.example.taobao.service;

import org.example.taobao.dto.OnePriceDto;
import org.example.taobao.dto.PageCategoryDto;
import org.example.taobao.dto.SpuDto;
import org.example.taobao.vo.GoodsVo;
import org.example.taobao.vo.PageVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 关岁安
 */
public interface SpuService {

    Long uploadSpu(SpuDto spuDto);

    void uploadSpuImg(MultipartFile file,Integer spuId) throws Exception;

    void uploadSpuMainImg(MultipartFile file,Integer spuId) throws Exception;

    void uploadOnePrice(OnePriceDto onePriceDto);

    PageVo gainSimplyGoodsVoList(Integer page, Integer pageSize);

    GoodsVo gainGoodsById(Integer id);

    List<String> gainSpuImgs(Integer id);

    PageVo gainCategoryGoods(PageCategoryDto pageCategoryDto);
}
