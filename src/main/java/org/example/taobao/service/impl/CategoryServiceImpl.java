package org.example.taobao.service.impl;

import org.example.taobao.mapper.CategoryMapper;
import org.example.taobao.service.CategoryService;
import org.example.taobao.vo.CategoryUnitVo;
import org.example.taobao.vo.CategoryVo;
import org.example.taobao.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 关岁安
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result<String> insertCategory(String parentCategory, List<String> sonCategory) {
        LocalDateTime currentTime = LocalDateTime.now();
        Integer id = categoryMapper.selectExistsCategoryName(parentCategory);
        if(id == null){
           //如果没有查到了
            categoryMapper.insertParentCategory(parentCategory,currentTime);
            Integer parentId = categoryMapper.selectExistsCategoryName(parentCategory);
            for(String name:sonCategory){
               if(categoryMapper.selectExistsCategoryName(name) == null){
                   categoryMapper.insertSonCategory(name,parentId,currentTime);
               }else {
                    return Result.success("该父分类下的子分类已存在 请不要重复进行插入");
               }
            }
        }else{
           //查到了
            for(String name:sonCategory){
                if(categoryMapper.selectExistsCategoryName(name) == null){
                    categoryMapper.insertSonCategory(name,id,currentTime);
                }else {
                    return Result.success("该父分类下的子分类已存在 请不要重复进行插入");
                }
            }
        }
        return Result.success("创建成功");
    }

    @Override
    public Result gainAllCategory() {
        List<CategoryVo> parentCategoryList = categoryMapper.gainParentCategoryList();
        List<CategoryUnitVo> categoryAllUnitVos = new ArrayList<>();
        for(CategoryVo categoryVo:parentCategoryList){
            Integer id = categoryVo.getId();
            List<CategoryVo> sonCategoryList = categoryMapper.gainSonCategoryList(id);
            CategoryUnitVo categoryUnitVo = new CategoryUnitVo(categoryVo,sonCategoryList);
            categoryAllUnitVos.add(categoryUnitVo);
        }
        return Result.success(categoryAllUnitVos);
    }
}
