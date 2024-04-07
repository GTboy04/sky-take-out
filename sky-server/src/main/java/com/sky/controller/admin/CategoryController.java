package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gt_boy
 * @description: TODO
 * @date 2024/4/7 20:38
 */
@RestController
@RequestMapping("admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "添加分类")
    @PostMapping
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
        Result result = categoryService.addCategory(categoryDTO);
        return result;
    }

    @ApiOperation("根据分类类型查询对应类型商品")
    @GetMapping("list")
    public Result<List<Category>> selectByType(Integer type) {
        Result<List<Category>> result = categoryService.selectByType(type);
        return result;
    }

    @ApiOperation("修改分类")
    @PutMapping
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
        Result result = categoryService.updateCategory(categoryDTO);
        return result;
    }

    @ApiOperation("根据id删除分类")
    @DeleteMapping
    public Result deleteById(Long id) {
        Result result = categoryService.deleteById(id);
        return result;
    }

    @ApiOperation("启用和禁用分裂")
    @PostMapping("status/{status}")
    public Result switchStatus(@PathVariable Integer status, Long id){
        Result result = categoryService.updateStatus(status,id);
        return result;
    }

    @ApiOperation("分页查询")
    @GetMapping("page")
    public Result<PageResult> findPage(CategoryPageQueryDTO categoryPageQueryDTO){
        Result<PageResult> result = categoryService.findPage(categoryPageQueryDTO);
        return result;
    }

}
