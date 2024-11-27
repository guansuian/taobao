package org.example.taobao.dto;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class OrderShowDto {
    private Long spuId;
    private Long skuId;
    private String head;
    private Integer orderNumber;
}
