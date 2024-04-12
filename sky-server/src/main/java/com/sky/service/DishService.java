package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;

import java.util.List;

public interface DishService {
    Result addDish(DishDTO dishDTO);

    Result<PageResult> PageDishList(DishPageQueryDTO dishPageQueryDTO);

    Result deleteBatch(List<Long> idList);
}
