package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;

import java.util.List;


public interface CategoryService {
    Result addCategory(CategoryDTO categoryDTO);

    Result<List<Category>> selectByType(Integer type);

    Result updateCategory(CategoryDTO categoryDTO);

    Result deleteById(Long id);

    Result updateStatus(Integer status, Long id);

    Result<PageResult> findPage(CategoryPageQueryDTO categoryPageQueryDTO);
}
