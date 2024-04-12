package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    //这个虽然是插入，但是这个里面并没有公共字段所以没必要去使用@AutoFill了
    void insertDishFlavor(List<DishFlavor> dishFlavors);

    void deleteFlavorByDishId(List<Long> idList);

    @Select("select * from dish_flavor where dish_id = #{id};")
    List<DishFlavor> selectFlavorByDishId(Long id);
}
