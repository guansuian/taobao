package org.example.taobao.dto;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class ResultDto {
    private Integer inventory;
    private SonSaleAttribute[] sonSaleAttributeList;

}
