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
public class CommonFatherOrder {
    private Integer totalPrice;
    private Long commonFatherOrderId;
}
