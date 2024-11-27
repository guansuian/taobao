package org.example.taobao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 关岁安
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageDto {
    private Integer userId;
    private Integer page;
    private Integer pageSize;
}
