package org.example.taobao.service;

import org.example.taobao.vo.Result;
import org.example.taobao.vo.ShoppingVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author 关岁安
 */
public interface ShoppingService {

    Result registerShopping(MultipartFile file, String userId, String address, String username, String shoppingName, String type,String content) ;

    Result gainShops(Integer id,Integer page,Integer pageSize);

    Result gainNoShopsList(Integer page,Integer pageSize);

    void changeShoppingHead(String url ,Integer id);

    Result gainShoppingBasicINformationBySpuId(Long spuId);
}
