package org.example.taobao.dao;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertShoppingCartDao {
    private Long skuId;
    private Long spuId;
    private Integer shoppingId;
    private Integer userId;
    private Integer number;
    private Integer price;
    private String head;
    private String spuName;
}
