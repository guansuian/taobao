package org.example.taobao.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.taobao.dto.CommonOrderSkuDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 关岁安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonOrderDao {
    private Long fatherOrderId;
    private LocalDateTime currentTime;
    private Long id;
    private Integer userId;
    private Integer totalPrice;
    private Integer shoppingId;
    private String shoppingName;

}
