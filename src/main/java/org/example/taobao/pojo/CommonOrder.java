package org.example.taobao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonOrder {
    private String refundResult;
    private String shoppingName;
    private Long orderId;
    private Integer userId;
    private LocalDateTime orderCreateDate;
    private Integer status;
    private Integer totalPrice;
    private Integer shoppingId;
    private String payNo;
    private LocalDateTime payTime;
    private Long fatherOrderId;
}