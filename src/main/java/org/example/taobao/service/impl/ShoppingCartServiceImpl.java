package org.example.taobao.service.impl;

import org.example.taobao.dao.InsertOrderDao;
import org.example.taobao.dao.InsertShoppingCartDao;
import org.example.taobao.dto.InsertShoppingCartDto;
import org.example.taobao.mapper.*;
import org.example.taobao.pojo.ShoppingCartSku;
import org.example.taobao.pojo.Sku;
import org.example.taobao.pojo.SkuAttributeValue;
import org.example.taobao.service.ShoppingCartService;
import org.example.taobao.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 关岁安
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ShoppingMapper shoppingMapper;

    @Autowired
    private SkuAttributeValueMapper skuAttributeValueMapper;

    @Autowired
    private AttributeMapper attributeMapper;

    @Override
    public Result insertShoppingCart(InsertShoppingCartDto insertShoppingCartDto) {

        ShoppingCartSku shoppingCartSku = shoppingCartMapper.gainShoppingCartSku(insertShoppingCartDto.getSkuVo().getSkuId());

        if(shoppingCartSku == null){
            Long spuId = skuMapper.gainSpuIdBySkuId(insertShoppingCartDto.getSkuVo().getSkuId());
            String head = spuMapper.gainSouHead(spuId);
            String spuName = spuMapper.gainSpuName(spuId);
            InsertShoppingCartDao insertShoppingCartDao = new InsertShoppingCartDao(
                    insertShoppingCartDto.getSkuVo().getSkuId(),
                    spuId,
                    insertShoppingCartDto.getSkuVo().getShoppingId(),
                    insertShoppingCartDto.getUserId(),
                    insertShoppingCartDto.getNumber(),
                    insertShoppingCartDto.getSkuVo().getPrice(),
                    head,
                    spuName
            );
            shoppingCartMapper.insertShoppingCart(insertShoppingCartDao);
        }else{
            shoppingCartMapper.updateSkuNumber(shoppingCartSku);
        }
        return Result.success("将新增加的购物车插入成功");
    }

    @Override
    public Result gainAllShoppingCartByUserId(Integer userId) {
        //这个集合是返回给前端的集合
        List<ShopAndShoppingCartVo> shopAndShoppingCartListVo = new ArrayList<>();

        //这个集合是从数据库中取到的集合 里面包含了所有的购物车资料
        List<ShoppingCartVo> shoppingCartVos = shoppingCartMapper.gainShoppingCartListBuUserId(userId);

        //这个集合是购物车数据库中取到的集合 是用来确定父关系的
        List<Integer> shoppingIdList = shoppingCartMapper.gainShoppingIdList(userId);
        for (Integer shoppingId : shoppingIdList) {

            //这个集合是为了从shoppingCartVos全部集合中取到和shoppingId一致的子元素
            List<ShoppingCartVo> list = new ArrayList<>();

            String shoppingName = shoppingMapper.gainShopName(shoppingId);
            for (ShoppingCartVo shoppingCartVo : shoppingCartVos) {
                if(Objects.equals(shoppingCartVo.getShoppingId(), shoppingId)){
                    List<SkuAttributeValue> skuAttributeValueList = skuAttributeValueMapper.gainSkuAttribute(shoppingCartVo.getSkuId());
                    for (SkuAttributeValue skuAttributeValue : skuAttributeValueList) {
                        Integer attributeId = skuAttributeValue.getAttrId();
                        String name = attributeMapper.gainSkuAttributeName(attributeId);
                        skuAttributeValue.setAttrName(name);
                    }
                    shoppingCartVo.setSkuAttributeValueList(skuAttributeValueList);
                    list.add(shoppingCartVo);
                }
            }
            ShopAndShoppingCartVo unitVo = new ShopAndShoppingCartVo(shoppingName,shoppingId,list);
            shopAndShoppingCartListVo.add(unitVo);
        }
        return Result.success(shopAndShoppingCartListVo);
    }
}
