package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    @GetMapping("page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> DishPage(DishPageQueryDTO dishPageQueryDTO){
        Result<PageResult> result = dishService.PageDishList(dishPageQueryDTO);
        return result;
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result deleteDish(@RequestParam List<Long> ids){
        log.info("传入idList：{}",ids);
        //自己处理也可以交给这个处理也是可以交给spring帮我们去处理的
        Result result = dishService.deleteBatch(ids);
        return result;
    }

    @GetMapping("{id}")
    public Result<DishVO> getDishById(@PathVariable Long id){
        return dishService.selectDishById(id);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDishWithFlavor(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        Result result = dishService.updateDishWithFlavor(dishDTO);
        return result;
    }

    @PostMapping("status/{status}")
    @ApiOperation("菜品起售、停售")
    public Result switchStatus(@PathVariable Integer status, @RequestParam("id") Long dishId){
        log.info("菜品的起售与停售{},{}",status,dishId);
        return dishService.SwitchDishAndSetMealStatus(status,dishId);
    }
}
