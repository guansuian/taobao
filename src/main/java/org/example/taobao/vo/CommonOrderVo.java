package org.example.taobao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonOrderVo {
    private String refundResult;
    private String orderId;
    private Integer userId;
    private LocalDateTime orderCreateTime;
    private Integer status;
    private Integer totalPrice;
    private Integer shoppingId;
    private String payNo;
    private LocalDateTime payTime;
    private String shoppingName;
    private String fatherOrderId;
    private List<CommonOrderSkuVo> commonOrderSkuVos;
}
