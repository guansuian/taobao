package org.example.taobao.dto;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class OrderDto {
    private Long spuId;
    private String spuName;
    private Integer userId;
    private Integer number;
    private Integer totalPrice;
    private Long skuId;
}
