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
public class CommonOrderSkuVo {
    private String orderId;
    private Long skuId;
    private Long spuId;
    private Integer number;
    private Integer price;
    private String spuName;
    private String spuImg;
    private List<SkuAttributeValue> skuAttributeValues;
}
