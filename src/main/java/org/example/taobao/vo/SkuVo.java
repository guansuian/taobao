package org.example.taobao.vo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.taobao.pojo.SkuAttributeValue;

import java.util.List;


/**
 * @author 关岁安
 */
@Data
public class SkuVo {
    private Long skuId;
    private Integer price;
    private Integer shoppingId;
    private Integer inventory;
    private List<SkuAttributeValue> skuAttributeValues;
}
