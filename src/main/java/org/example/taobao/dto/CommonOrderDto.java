package org.example.taobao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonOrderDto {

    private Integer userId;
    private Integer totalPrice;
    private Integer shoppingId;
    private String shoppingName;
    private List<CommonOrderSkuDto> commonOrderSkuDtoList;
}
