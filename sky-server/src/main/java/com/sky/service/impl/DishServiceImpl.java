package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.Result;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Gt_boy
 * @description:
 * @date 2024/4/11 19:48
 */

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    //自动填充组件
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /*
    * 其实我感觉加了事务之后不用再去进行判断影响数据行数了，因为如果影响行数为0，自然会进行回归
    * 当一个service中的业务逻辑设计到多张表的时候需要加上@Transactional注解，开始事务管理
    * 当有一张表操作失败则不能提交事务
    * */
    @Transactional
    @Override
    //这里的方法名没有起好，没有体现我真正的用意：添加菜品和口味
    public Result addDish(DishDTO dishDTO) {
        log.info("添加菜品前端传入的DishDTO{}",dishDTO);

        //这里给创建dish对象的时候，可以使用spring提供的工具BeanUtils进行属性复制

        Dish dish = Dish.builder()
                .categoryId(dishDTO.getCategoryId())
                .description(dishDTO.getDescription())
                .id(dishDTO.getId())
                .image(dishDTO.getImage())
                .name(dishDTO.getName())
                .price(dishDTO.getPrice())
                .status(StatusConstant.ENABLE)
                .build();
        log.info("把DishDTO封装成Dish{}",dish);
        //调用mapper层向Dish表单中添加数据
        dishMapper.insertDish(dish);
            //添加成功之后，主键回显菜品的主键回到了dish对象中，我们获取主键信息放入DishFlavor中
            List<DishFlavor> dishFlavors = dishDTO.getFlavors();
            if (dishFlavors != null && !dishFlavors.isEmpty()){
                dishFlavors.forEach(dishFlavor -> {
                    dishFlavor.setDishId(dish.getId());
                });
                //这个地方的命令不规范，如果要进行批量插入的话建议是以Batch为后缀
                dishFlavorMapper.insertDishFlavor(dishFlavors);
                return Result.success();
            }

            return Result.error(MessageConstant.UNKNOWN_ERROR);






    }
}
