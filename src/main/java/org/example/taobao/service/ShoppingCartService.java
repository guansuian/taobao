package org.example.taobao.service;

import org.example.taobao.dto.InsertShoppingCartDto;
import org.example.taobao.vo.Result;
import org.example.taobao.vo.SkuVo;

/**
 * @author 关岁安
 */
public interface ShoppingCartService {
    Result insertShoppingCart(InsertShoppingCartDto insertShoppingCartDto);

    Result gainAllShoppingCartByUserId(Integer userId);
}
