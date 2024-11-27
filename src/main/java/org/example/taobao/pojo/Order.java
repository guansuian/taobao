package org.example.taobao.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 关岁安
 */
@Data
public class Order {
    private Long id;
    private Integer userId;
    private LocalDateTime orderCreateTime;
    private Integer number;
    private Integer status;
    private Long spuId;
    private String spuName;
    private Integer totalPrice;
    private String payNo;
    private LocalDateTime payTime;
    private Long skuId;
}
