package org.example.taobao.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertOrderDao implements Serializable {
    @Serial
    private static final long serialVersionUID = 3541386546686337632L;
    private Long id;
    private Integer userId;
    private Long skuId;
    private Long spuId;
    private LocalDateTime currentTime;
    private Integer number;
    private String spuName;
    private Integer totalPrice;
}
