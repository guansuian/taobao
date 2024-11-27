package org.example.taobao.controller;

import org.example.taobao.dto.CategoryDto;
import org.example.taobao.service.CategoryService;
import org.example.taobao.vo.CategoryUnitVo;
import org.example.taobao.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 关岁安
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping("insertCategory")
    public Result<String> insertCategory(@RequestBody CategoryDto categoryDto){
        String parentCategory = categoryDto.getParentCategory();
        List<String> sonCategory = categoryDto.getSonCategory();
        System.out.println(sonCategory);
        return categoryService.insertCategory(parentCategory,sonCategory);
    }

    @PostMapping("gainAllCategory")
    public Result gainAllCategory(){
        return categoryService.gainAllCategory();
    }



}
