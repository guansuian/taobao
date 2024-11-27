package org.example.taobao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.taobao.vo.SkuVo;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertShoppingCartDto {
    private SkuVo skuVo;
    private Integer number;
    private Integer userId;
}
