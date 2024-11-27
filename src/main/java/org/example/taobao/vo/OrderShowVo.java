package org.example.taobao.vo;

import lombok.Data;
import org.example.taobao.pojo.SkuAttributeValue;

import java.util.List;

/**
 * @author 关岁安
 */
@Data
public class OrderShowVo {
    private String spuName;
    private Integer originalPrice;

    private String head;
    private Integer orderNumber;
    private String shoppingName;
    private Long spuId;
    private Long skuId;
    private List<SkuAttributeValue> skuAttributeValues;
    private Integer isMao;

    public OrderShowVo(String spuName, Integer originalPrice, String head, Integer orderNumber, String shoppingName, Long spuId, Long skuId, List<SkuAttributeValue> skuAttributeValues,Integer isMao) {
        this.spuName = spuName;
        this.originalPrice = originalPrice;
        this.head = head;
        this.orderNumber = orderNumber;
        this.shoppingName = shoppingName;
        this.spuId = spuId;
        this.skuId = skuId;
        this.skuAttributeValues = skuAttributeValues;
        this.isMao = isMao;
    }
}
