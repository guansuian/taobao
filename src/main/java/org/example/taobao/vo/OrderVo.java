package org.example.taobao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.taobao.pojo.Order;

import java.time.LocalDateTime;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo {
    private Order order;
    private String head;
}
