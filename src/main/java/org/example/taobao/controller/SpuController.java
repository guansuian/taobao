package org.example.taobao.controller;

import io.opentracing.Span;
import org.example.taobao.dto.OnePriceDto;
import org.example.taobao.dto.PageCategoryDto;
import org.example.taobao.dto.PageDto;
import org.example.taobao.dto.SpuDto;
import org.example.taobao.service.SpuService;
import org.example.taobao.vo.GoodsVo;
import org.example.taobao.vo.PageVo;
import org.example.taobao.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * @author 关岁安
 */
@RestController
@RequestMapping("/spu")
public class SpuController {

    @Autowired
    private SpuService spuService;

    @PostMapping("uploadSpu")
    public Result uploadSpu(@RequestBody SpuDto spuDto){
        Long spuId = spuService.uploadSpu(spuDto);
        return Result.success(spuId);
    }
    @PostMapping("uploadSpuImg")
    public Result uploadSpuImg(MultipartFile file,Integer spuId) throws Exception {
        spuService.uploadSpuImg(file,spuId);
        return Result.success("已经成功上传图片");
    }

    @PostMapping("uploadSpuMainImg")
    public void uploadSpuMainImg(MultipartFile file,Integer spuId) throws Exception {
        spuService.uploadSpuMainImg(file,spuId);
    }


    @PostMapping("uploadOnePrice")
    public void uploadOnePrice(@RequestBody OnePriceDto onePriceDto){
        spuService.uploadOnePrice(onePriceDto);
    }


    @PostMapping("gainAllSimplyGoods")
    public Result gainAllSimplyGoods(@RequestBody PageDto pageDto){
        System.out.println(pageDto);
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();
        PageVo pageVo = spuService.gainSimplyGoodsVoList(page,pageSize);
        return Result.success(pageVo);
    }

    @PostMapping("gainSeckillerCategoryGoods")
    public Result gainCategoryGoods(@RequestBody PageCategoryDto pageCategoryDto){
        PageVo pageVo = spuService.gainCategoryGoods(pageCategoryDto);
        return Result.success(pageVo);
    }

    @GetMapping("gainGoodsById")
    public Result gainGoodsById(@RequestParam("id") Integer id){
        System.out.println(id);
        GoodsVo goodsVo = spuService.gainGoodsById(id);
        System.out.println(goodsVo);
        return Result.success(goodsVo);
    }

    @GetMapping("gainSpuImgs")
    public Result gainSpuImgs(@RequestParam("goodsId") Integer id){
        System.out.println(id);
        List<String> headList = spuService.gainSpuImgs(id);
        return Result.success(headList);
    }

}
