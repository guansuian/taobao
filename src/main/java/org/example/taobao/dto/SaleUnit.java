package org.example.taobao.dto;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class SaleUnit {
    private Long id;
    private String parentSaleName;
    private SonSaleAttribute[] sonSaleAttributeList;
}
