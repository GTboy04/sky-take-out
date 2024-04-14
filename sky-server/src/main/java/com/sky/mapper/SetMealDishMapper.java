package com.sky.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    List<Long> getSetMealIdByDishId(List<Long> idList);

    @Select("select setmeal_id from setmeal_dish where dish_id = #{dishId}")
    Long findSetmealIdByDishId(Long dishId);
}
