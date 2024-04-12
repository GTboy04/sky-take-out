package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Gt_boy
 * @description:
 * @date 2024/4/11 19:49
 */

@Mapper
public interface DishMapper {

    @AutoFill(OperationType.INSERT)
    void insertDish(Dish dish);
}
