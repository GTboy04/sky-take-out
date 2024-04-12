package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gt_boy
 * @description:
 * @date 2024/4/11 12:12
 */

@RestController
@RequestMapping("admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO){
        Result result = dishService.addDish(dishDTO);
        return result;
    }

    /**
     * 彩品分页查询
     * @param dishPageQueryDTO
     * @return
     */
/*    @GetMapping("page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> DishPage(DishPageQueryDTO dishPageQueryDTO){
        Result<PageResult> result = dishService.PageDishList(dishPageQueryDTO);
        return result;
    }*/

}
