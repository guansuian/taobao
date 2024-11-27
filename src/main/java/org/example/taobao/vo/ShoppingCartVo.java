package org.example.taobao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.taobao.pojo.SkuAttributeValue;

import java.util.List;

/**
 * @author 关岁安
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartVo {
    private String spuName;
    private Long skuId;
    private Long spuId;
    private Integer shoppingId;
    private Integer userId;
    private Integer number;
    private Integer price;
    private String head;
    private List<SkuAttributeValue> skuAttributeValueList;

}
