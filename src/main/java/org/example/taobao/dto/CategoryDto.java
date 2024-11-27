package org.example.taobao.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 关岁安
 */
@Data
public class CategoryDto {

    private String parentCategory;

    private List<String> sonCategory;

}
