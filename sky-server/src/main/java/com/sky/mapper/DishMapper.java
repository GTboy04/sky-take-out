package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Gt_boy
 * @description:
 * @date 2024/4/11 19:49
 */

@Mapper
public interface DishMapper {

    @AutoFill(OperationType.INSERT)
    void insertDish(Dish dish);

    List<DishVO> selectPage(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> idList);

    List<Integer> getStatusById(List<Long> idList);
}
