package org.example.taobao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.taobao.dao.InsertShoppingCartDao;

import java.util.List;

/**
 * @author 关岁安
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopAndShoppingCartVo {
    private String shoppingName;
    private Integer shoppingId;
    private List<ShoppingCartVo> shoppingCartSku;
}
