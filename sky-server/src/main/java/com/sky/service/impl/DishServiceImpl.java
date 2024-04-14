package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    /*
     * 其实我感觉加了事务之后不用再去进行判断影响数据行数了，因为如果影响行数为0，自然会进行回归
     * 当一个service中的业务逻辑设计到多张表的时候需要加上@Transactional注解，开始事务管理
     * 当有一张表操作失败则不能提交事务
     * */
    @Transactional
    @Override
    //这里的方法名没有起好，没有体现我真正的用意：添加菜品和口味
    public Result addDish(DishDTO dishDTO) {
        log.info("添加菜品前端传入的DishDTO{}", dishDTO);

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
        log.info("把DishDTO封装成Dish{}", dish);
        //调用mapper层向Dish表单中添加数据
        dishMapper.insertDish(dish);
        //添加成功之后，主键回显菜品的主键回到了dish对象中，我们获取主键信息放入DishFlavor中
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if (dishFlavors != null && !dishFlavors.isEmpty()) {
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            //这个地方的命令不规范，如果要进行批量插入的话建议是以Batch为后缀
            dishFlavorMapper.insertDishFlavor(dishFlavors);
            return Result.success();
        }

        return Result.error(MessageConstant.UNKNOWN_ERROR);


    }

    @Override
    public Result<PageResult> PageDishList(DishPageQueryDTO dishPageQueryDTO) {
        log.info("DisPageQueryDTO{}", dishPageQueryDTO);
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        List<DishVO> dishVOS = dishMapper.selectPage(dishPageQueryDTO);
        PageInfo<DishVO> dishVOPageInfo = new PageInfo<>(dishVOS);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(dishVOPageInfo.getTotal());
        pageResult.setRecords(dishVOPageInfo.getList());

        return Result.success(pageResult);


    }


    @Transactional
    @Override
    public Result deleteBatch(List<Long> idList) {

        /*
         * 删除并没有我想的那么简单，就是直接通过菜品的id去删除
         * 我这个想法其实是错误的，如果要想去删除这个菜品需要考虑的有很多
         * 1. 菜品的状态是起售还是停售，如果是停售的话才能去删除，起售的话就不能删除
         * 2. 菜品有没有关联别的套餐，如果关联套餐的话，删除了之后，会破坏数据库的完成性
         *    就好比数据库的外键空能一样，我们数据库中是没有添加外键的，但是我们要在java代码中体现出
         *    表之间的连接关系
         * 3. 通过上述条件删除之后，我们还需要去删除我们菜品对应的口味数据
         * 4. 涉及到多张表的业务包加@Transactional
         * */

        log.info("idList{}", idList);

        List<Integer> status = dishMapper.getStatusById(idList);
        for (Integer i : status) {
            if (i == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        List<Long> setmealId = setMealDishMapper.getSetMealIdByDishId(idList);
        if (setmealId != null && !setmealId.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //判断结束可以进行删除了
        dishMapper.deleteBatch(idList);
        dishFlavorMapper.deleteFlavorByDishId(idList);

        return Result.success();

    }

    @Transactional
    @Override
    public Result updateDishWithFlavor(DishDTO dishDTO) {

        log.info("更新菜品信息：{}", dishDTO);

        //1.更新Dish表中的数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);


        //2.更新DishFlavor中的表，DishDTO中的Flavor数据是集合List，想要对其修改其实不太好直接修改
        //可以通过先删除后添加的方式进行修改
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if (dishFlavors != null && !dishFlavors.isEmpty()) {
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });

            dishFlavorMapper.deleteFlavorByDishId(Collections.singletonList(dish.getId()));
            dishFlavorMapper.insertDishFlavor(dishFlavors);
        }
        return Result.success();
    }

    /**
     * 查询Dish表和Dish_Flavor表
     *
     * @param id
     * @return
     */
    @Override
    public Result<DishVO> selectDishById(Long id) {
        DishVO dishVO = new DishVO();
        Dish dish = dishMapper.getDishById(id);
        BeanUtils.copyProperties(dish, dishVO);

        List<DishFlavor> dishFlavors = dishFlavorMapper.selectFlavorByDishId(id);

        dishVO.setFlavors(dishFlavors);

        return Result.success(dishVO);
    }

    @Transactional //涉及到多张表
    @Override
    public Result SwitchDishAndSetMealStatus(Integer status, Long dishId) {
        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setStatus(status);
        log.info("菜品{}",dish);
        //起售与停售菜品
        dishMapper.update(dish);


        //只有当菜品停售的时候才需要我去停售套餐，而菜品起售的时候不需要任何的操作
        // 通过dishId去查SetmealDish中的SetmealId，然后去修改套餐的status

        if (status.equals(StatusConstant.DISABLE)) {
            List<Long> SetmealId = setMealDishMapper.findSetmealIdByDishId(dishId);
            //当去数据库中插找数据的时候，要进行判断
            if (SetmealId != null&&!SetmealId.isEmpty()) {
                for (Long l : SetmealId) {
                    Setmeal setmeal = new Setmeal();
                    setmeal.setId(l);
                    setmeal.setStatus(status);
                    setMealMapper.update(setmeal);
                }
                return Result.success();
            }
        }
        return Result.success();
    }
}
