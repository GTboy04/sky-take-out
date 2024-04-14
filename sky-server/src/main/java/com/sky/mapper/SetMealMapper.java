package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;

import java.util.List;

public interface SetMealMapper {
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeals);
}
