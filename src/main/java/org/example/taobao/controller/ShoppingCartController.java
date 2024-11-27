package org.example.taobao.controller;

import org.example.taobao.dto.InsertShoppingCartDto;
import org.example.taobao.service.ShoppingCartService;
import org.example.taobao.vo.Result;
import org.example.taobao.vo.SkuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 关岁安
 */

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("insertShoppingCart")
    public Result insertShoppingCart(@RequestBody InsertShoppingCartDto insertShoppingCartDto){
        return shoppingCartService.insertShoppingCart(insertShoppingCartDto);
    }


    @GetMapping("gainAllShoppingCartByUserId")
    private Result gainAllShoppingCartByUserId(@RequestParam Integer userId){
        return shoppingCartService.gainAllShoppingCartByUserId(userId);
    }


}
