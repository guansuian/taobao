package org.example.taobao.controller;

import org.example.taobao.dto.PageDto;
import org.example.taobao.service.ShoppingService;
import org.example.taobao.utils.AliOssUtil;
import org.example.taobao.vo.Result;
import org.ini4j.Ini;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author 关岁安
 */
@RestController
@RequestMapping("/shopping")
public class ShoppingController {


    @Autowired
    private ShoppingService shoppingService;

    @PostMapping("registerShopping")
    public Result registerShopping(MultipartFile file, String userId, String address, String username, String shoppingName, String type,String content){
        return shoppingService.registerShopping(file,userId,address,username,shoppingName, type,content);
    }

    @PostMapping("gainShops")
    public Result gainShops(@RequestParam String userId, @RequestParam Integer page, @RequestParam Integer pageSize) {
        Integer id = Integer.valueOf(userId);
        return shoppingService.gainShops(id,page,pageSize);
    }

    @PostMapping("gainNoShopsList")
    public Result gainNoShopsList(@RequestBody PageDto pageDto){
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();
        System.out.println(page);
        System.out.println(pageSize);
        return shoppingService.gainNoShopsList(page, pageSize);
    }

    @PostMapping("changeShoppingHead")
    public Result changeShoppingHead(MultipartFile file,String shoppingId) throws Exception {
        Integer id = Integer.parseInt(shoppingId);
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
        String url = AliOssUtil.upLoadFile(fileName,file.getInputStream());
        shoppingService.changeShoppingHead(url,id);
        System.out.println(id);
        return Result.success(url);
    }

    @GetMapping("gainShoppingBasicInformationBySpuId")
    private Result gainShoppingBasicInformationBySpuId(@RequestParam Long spuId){
        return  shoppingService.gainShoppingBasicINformationBySpuId(spuId);
    }


}
