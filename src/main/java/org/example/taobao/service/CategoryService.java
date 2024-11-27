package org.example.taobao.service;

import org.example.taobao.vo.CategoryUnitVo;
import org.example.taobao.vo.Result;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 关岁安
 */
public interface CategoryService {
    Result<String> insertCategory(String parentCategory, List<String> sonCategory);

    Result<CategoryUnitVo> gainAllCategory();

}
