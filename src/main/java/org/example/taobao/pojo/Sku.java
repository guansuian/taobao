package org.example.taobao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sku {
    private Integer status;
    private Long skuId;
    private Integer price;
    private Integer shoppingId;
    private Integer inventory;
}
