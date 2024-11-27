package org.example.taobao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonOrderSkuDto {
    private Long skuId;
    private Long spuId;
    private Integer number;
    private Integer price;
    private String spuName;
    private String spuImg;
}
