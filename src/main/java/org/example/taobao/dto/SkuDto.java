package org.example.taobao.dto;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class SkuDto {

    private Integer categoryId;

    private Integer shoppingId;

    private Long spuId;

    private ResultDto[] resultDtoList;

    private SaleUnit[] saleUnits;

}
