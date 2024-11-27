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
public class ShoppingCartSku {
    private Integer id;
    private Long skuId;
    private Integer shoppingId;
    private Integer userId;
    private Integer number;
    private Long spuId;
    private Integer price;
    private String head;
    private String spuName;
}
