package org.example.taobao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageCategoryDto {
    private Integer page;
    private Integer pageSize;
    private Integer categoryId;
}
