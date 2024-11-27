package org.example.taobao.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 关岁安
 */
@Data
public class CategoryUnitVo {
    private CategoryVo parentCategory;
    private List<CategoryVo> sonCategory;

    public CategoryUnitVo(CategoryVo parentCategory,List<CategoryVo> sonCategory){
        this.parentCategory = parentCategory;
        this.sonCategory = sonCategory;
    }
}
