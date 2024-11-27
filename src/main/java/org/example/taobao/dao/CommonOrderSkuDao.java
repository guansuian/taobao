package org.example.taobao.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonOrderSkuDao {
    private Long skuId;
    private Long spuId;
    private Integer number;
    private Integer price;
    private String spuName;
    private String spuImg;
    private Long orderId;
}
