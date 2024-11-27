package org.example.taobao.pojo;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class SkuAttribution {
    private Integer id;
    private Integer skuId;
    private Integer attrId;
    private String attrValue;
    private Integer price;
    private String url;
}
