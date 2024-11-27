package org.example.taobao.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.example.taobao.dto.*;
import org.example.taobao.mapper.AttributeMapper;
import org.example.taobao.mapper.SkuAttributeValueMapper;
import org.example.taobao.mapper.SkuMapper;
import org.example.taobao.mapper.SpuMapper;
import org.example.taobao.pojo.Sku;
import org.example.taobao.pojo.SkuAttributeValue;
import org.example.taobao.pojo.SkuSonAttribute;
import org.example.taobao.service.SkuService;
import org.example.taobao.pojo.SkuAttribute;
import org.example.taobao.vo.OrderShowVo;
import org.example.taobao.vo.SkuAttributeVo;
import org.example.taobao.vo.SkuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.example.taobao.utils.IdGenerationTest.generateId;
import static org.example.taobao.utils.RedisConstants.CACHE_CHECK_INVENTORY_SKU_ID;

/**
 * @author 关岁安
 */
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SkuAttributeValueMapper skuAttributeValueMapper;

    @Autowired
    private AttributeMapper attributeMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 1.先插销售属性
     * @param skuDto
     */
    @Override
    public void insertSku(SkuDto skuDto) {
        //雪花算法生成随机id

        Integer shoppingId = skuDto.getShoppingId();
        Long spuId = skuDto.getSpuId();


        //在这里要获取到spu的属性，如果spu是isMao=0就说明是秒杀商品，直接存redis
        Integer isMao = spuMapper.gainIsMao(spuId);

        Integer categoryId = skuDto.getCategoryId();

        ResultDto[] resultDtoList = skuDto.getResultDtoList();
        SaleUnit[] saleUnits = skuDto.getSaleUnits();

        //先给每一个销售属性添加唯一的id
        for (SaleUnit saleUnit : saleUnits) {
            Long saleAttributeId = generateId();
            saleUnit.setId(saleAttributeId);
            String parentSaleName = saleUnit.getParentSaleName();
            SonSaleAttribute[] sonSaleAttributeList = saleUnit.getSonSaleAttributeList();
            StringBuilder option = new StringBuilder();
            for (SonSaleAttribute sonSaleAttribute : sonSaleAttributeList) {
                option.append(sonSaleAttribute.getName()).append(",");
            }
            LocalDateTime currentTime = LocalDateTime.now();
            attributeMapper.insertSaleAttribute(saleAttributeId,parentSaleName, option.toString(),categoryId,currentTime,spuId);
        }

        for(int index = 0; index < resultDtoList.length ;index++){
            Long skuId = generateId();
            Integer inventory = resultDtoList[index].getInventory();
            SonSaleAttribute[] sonSaleAttributeList = resultDtoList[index].getSonSaleAttributeList();
            Integer price = 0;
            for (int i = 0; i < sonSaleAttributeList.length; i++) {
                price += sonSaleAttributeList[i].getPrice();
            }

            if(isMao == 1){
                Sku sku =new Sku(1,skuId,price,shoppingId,inventory);
                String key = "seckill:inventory:" + skuId;
                stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(sku));
            }

            skuMapper.insertSku(skuId,spuId,price,shoppingId,inventory);
            for (int i = 0; i < sonSaleAttributeList.length; i++) {
                Long skuAttributeId = generateId();

                Long attributeId = saleUnits[i].getId();
                String parentSaleName = saleUnits[i].getParentSaleName();
                System.out.println(parentSaleName);

                Integer salePrice = sonSaleAttributeList[i].getPrice();
                String attributeValue = sonSaleAttributeList[i].getName();

                String url = sonSaleAttributeList[i].getHead();
                if(url == null){
                    url = "无";
                }
                skuAttributeValueMapper.insertSkuAttribute(skuAttributeId,skuId,attributeId,attributeValue,salePrice,url);
            }
        }



    }

    @Override
    public List<SkuAttributeVo> gainSkuAttribute(Integer spuId) {
        //首先查到这个商品的全部的销售属性
        List<SkuAttribute> skuAttributes = attributeMapper.gainSkuAttribute(spuId);
        //然后新建一个用于返回给前端渲染界面的集合
        List<SkuAttributeVo> skuAttributeVos = new ArrayList<>();
        for (SkuAttribute skuAttribute : skuAttributes) {

            List<String> list  = extractItem(skuAttribute.getOptions());
            Integer fatherAttributeId = skuAttribute.getAttributeId();
            List<SkuSonAttribute> skuSonAttributes = new ArrayList<>();

            for (String value : list) {
                String url = skuAttributeValueMapper.gainUrl(value,fatherAttributeId);
                SkuSonAttribute skuSonAttribute = new SkuSonAttribute(url,value);
                skuSonAttributes.add(skuSonAttribute);
            }

            SkuAttributeVo skuAttributeVo = new SkuAttributeVo(skuAttribute.getAttributeId(),skuAttribute.getName(),skuAttribute.getCategoryId(),skuSonAttributes);
            skuAttributeVos.add(skuAttributeVo);
        }
        System.out.println(skuAttributeVos);
        return skuAttributeVos;
    }

    @Override
    public List<SkuVo> gainSku(Integer spuId) {
        List<SkuVo> skuAttributeVoList = skuMapper.gainSkuList(spuId);
        for (SkuVo skuVo : skuAttributeVoList) {
            Long skuId = skuVo.getSkuId();
            List<SkuAttributeValue> skuAttributeList = skuAttributeValueMapper.gainSkuAttribute(skuId);
            skuVo.setSkuAttributeValues(skuAttributeList);
        }
        return skuAttributeVoList;
    }

    @Override
    public List<OrderShowVo> gainOrderShowVoList(List<OrderShowDto> orderShowDto) {
        List<OrderShowVo> list = new ArrayList<>();
        for (OrderShowDto showDto : orderShowDto) {
            Long skuId = showDto.getSkuId();
            Long spuId = showDto.getSpuId();
            Integer isMao = spuMapper.gainIsMao(spuId);
            System.out.println(isMao);

            Integer orderNumber = showDto.getOrderNumber();
            String head = showDto.getHead();

            List<SkuAttributeValue> skuAttributeValueList =skuAttributeValueMapper.gainSkuAttribute(skuId);
            for (SkuAttributeValue skuAttributeValue : skuAttributeValueList) {
                Integer attributeId = skuAttributeValue.getAttrId();
                String name = attributeMapper.gainSkuAttributeName(attributeId);
                skuAttributeValue.setAttrName(name);
            }
            String spuName = spuMapper.gainSpuName(spuId);
            Integer originalPrice = skuMapper.gainSkuPrice(skuId);
            String shopName = spuMapper.gainShoppingNameBySpuId(spuId);
            OrderShowVo orderShowVo = new OrderShowVo(spuName,originalPrice,head,orderNumber,shopName,spuId,skuId,skuAttributeValueList,isMao);
            list.add(orderShowVo);
        }
        return list;
    }

    @Override
    public Integer checkSkuInventory(Long skuId) {
        String key = "sku:inventory:" + skuId;
        //1.从redis查询店铺缓存
        String inventoryJson =  stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(StrUtil.isNotBlank(inventoryJson)){
            //3.存在就直接返回
            Sku sku = JSONUtil.toBean(inventoryJson,Sku.class);
            return sku.getInventory();
        }else{
             Sku sku = skuMapper.checkSku(skuId);
             if(sku == null){
                 return null;
             }
            stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(sku));
            System.out.println("重新设置了SKU的库存");
             return sku.getInventory();
        }
    }


    private List<String> extractItem(String input){
        // 创建一个列表来存储提取的颜色
        List<String> colorsList = new ArrayList<>();

        // 拆分输入字符串，使用逗号作为分隔符
        String[] parts = input.split(",");

        // 遍历每个部分并添加到列表中
        for (String part : parts) {
            String trimmedPart = part.trim();
            if (!trimmedPart.isEmpty()) {
                colorsList.add(trimmedPart);
            }
        }
        System.out.println(colorsList);
        // 将列表转换为数组并返回
        return colorsList;
    }
}
