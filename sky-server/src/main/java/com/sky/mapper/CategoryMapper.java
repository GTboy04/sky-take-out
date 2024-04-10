package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @AutoFill(OperationType.INSERT)
    int insert(Category category);

    List<Category> selectByType(Integer type);

    int update(Category category);

    int deleteById(Long id);

    List<Category> queryList(CategoryPageQueryDTO category);
}
