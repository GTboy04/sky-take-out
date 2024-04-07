package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Gt_boy
 * @description: TODO
 * @date 2024/4/7 20:46
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result addCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .status(StatusConstant.ENABLE)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .createUser(BaseContext.getCurrentId())
                .updateUser(BaseContext.getCurrentId())
                .build();

        BeanUtils.copyProperties(categoryDTO, category);
        log.info("category:{}", category);
        int i = categoryMapper.insert(category);
        if (i > 0) {
            return Result.success();
        }

        return Result.error("分类插入失败");
    }

    @Override
    public Result<List<Category>> selectByType(Integer type) {
        List<Category> categories = categoryMapper.selectByType(type);

        if (categories != null) {
            return Result.success(categories);
        }

        return Result.error("获取分类商品失败");
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @Override
    public Result updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        int i = categoryMapper.update(category);
        if (i > 0) {
            return Result.success();
        }

        return Result.error(MessageConstant.UNKNOWN_ERROR);

    }

    /**
     * 根据id去删除
     *
     * @param id
     * @return
     */
    @Override
    public Result deleteById(Long id) {

        int i = categoryMapper.deleteById(id);
        if (i > 0) {
            return Result.success();
        }

        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    /**
     * 启用与禁用
     *
     * @param status
     * @param id
     * @return
     */
    @Override
    public Result updateStatus(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();

        int update = categoryMapper.update(category);
        if (update > 0) {
            return Result.success();
        }

        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    @Override
    public Result<PageResult> findPage(CategoryPageQueryDTO categoryPageQueryDTO) {

        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
/*        Category category = Category.builder()
                .name(categoryPageQueryDTO.getName())
                .type(categoryPageQueryDTO.getType())
                .build();*/

        PageInfo<Category> categoryPageInfo = new PageInfo<>();
        List<Category> categories = categoryMapper.queryList(categoryPageQueryDTO);
        categoryPageInfo.setList(categories);
        PageResult pageResult = new PageResult(categoryPageInfo.getTotal(),categoryPageInfo.getList());
        return Result.success(pageResult);
    }
}
