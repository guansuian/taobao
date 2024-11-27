package org.example.taobao.controller;

import org.example.taobao.dto.OrderShowDto;
import org.example.taobao.dto.SkuDto;
import org.example.taobao.service.SkuService;
import org.example.taobao.utils.UploadImgToAlibaba;
import org.example.taobao.vo.OrderShowVo;
import org.example.taobao.vo.Result;
import org.example.taobao.vo.SkuAttributeVo;
import org.example.taobao.vo.SkuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 关岁安
 */
@RestController
@RequestMapping("/sku")
public class SkuController {

    @Autowired
    private SkuService skuService;

    @PostMapping( value = "uploadSkuImg")
    public Result uploadSkuImg(MultipartFile file) throws Exception {
        String url =  UploadImgToAlibaba.uploadImgToAlibaba(file);
        return Result.success(url);
    }

    @PostMapping(value = "uploadSku")
    public Result uploadSku(@RequestBody SkuDto skuDto){
        skuService.insertSku(skuDto);
        return  Result.success("后端可以接收到");
    }

    @GetMapping(value = "gainSkuAttribute")
    public Result gainSpuAttribute(@RequestParam("goodsId") Integer id){
        List<SkuAttributeVo> skuAttributes = skuService.gainSkuAttribute(id);
        return Result.success(skuAttributes);
    }

    @GetMapping("gainSku")
    public Result gainSku(@RequestParam("goodsId") Integer spuId){
         List<SkuVo> list = skuService.gainSku(spuId);
         return Result.success(list);
    }

    /**
 * 关于如何将商品渲染到页面并点击容器的时候会有不同的价格进行展示
 * 1，首先就是要获取到其销售的属性值，其中就包括father和son的销售属性
 * father是attributeId的形式 son是String的形式
 *
 */

    @PostMapping("gainSkuListBySkuId")
    public Result gainSkuListBySkuId(@RequestBody List<OrderShowDto> orderShowDto){
        List<OrderShowVo> showVos = skuService.gainOrderShowVoList(orderShowDto);
        return Result.success(showVos);
    }

    @GetMapping("checkSkuInventory")
    public Result checkSkuInventory(@RequestParam("skuId") Long skuId){
        Integer inventory = skuService.checkSkuInventory(skuId);
        return Result.success(inventory);
    }

//[ResultDto(inventory=11, sonSaleAttributeList=[SonSaleAttribute(name=红色, head=https://ling-an-gansuian.oss-cn-beijing.aliyuncs.com/91cd5de5-02c0-4a3d-b31b-afb8f0ae785d.png, price=0), SonSaleAttribute(name=大, head=, price=0)]), ResultDto(inventory=12, sonSaleAttributeList=[SonSaleAttribute(name=红色, head=https://ling-an-gansuian.oss-cn-beijing.aliyuncs.com/91cd5de5-02c0-4a3d-b31b-afb8f0ae785d.png, price=0), SonSaleAttribute(name=小, head=, price=0)]), ResultDto(inventory=13, sonSaleAttributeList=[SonSaleAttribute(name=蓝色, head=https://ling-an-gansuian.oss-cn-beijing.aliyuncs.com/a3e14437-ca3e-4832-a9e2-d89905e2c3ff.png, price=0), SonSaleAttribute(name=大, head=, price=0)]), ResultDto(inventory=14, sonSaleAttributeList=[SonSaleAttribute(name=蓝色, head=https://ling-an-gansuian.oss-cn-beijing.aliyuncs.com/a3e14437-ca3e-4832-a9e2-d89905e2c3ff.png, price=0), SonSaleAttribute(name=小, head=, price=0)])]
//[SaleUnit(parentSaleName=颜色, sonSaleAttributeList=[SonSaleAttribute(name=红色, head=https://ling-an-gansuian.oss-cn-beijing.aliyuncs.com/91cd5de5-02c0-4a3d-b31b-afb8f0ae785d.png, price=0), SonSaleAttribute(name=蓝色, head=https://ling-an-gansuian.oss-cn-beijing.aliyuncs.com/a3e14437-ca3e-4832-a9e2-d89905e2c3ff.png, price=0)]), SaleUnit(parentSaleName=尺寸, sonSaleAttributeList=[SonSaleAttribute(name=大, head=, price=0), SonSaleAttribute(name=小, head=, price=0)])]

}
