package com.sky.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "分页查询传参数")
public class CategoryPageQueryDTO implements Serializable {

    //页码
    private Integer page;

    //每页记录数
    private Integer pageSize;

    //分类名称
    private String name;

    //分类类型 1菜品分类  2套餐分类
    private Integer type;

}
