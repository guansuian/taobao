package org.example.taobao.pojo;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommonOrderSkuInventory {
    private Long skuId;
    private Integer number;
}
