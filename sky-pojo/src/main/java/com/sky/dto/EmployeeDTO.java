package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "员工传入数据")
public class EmployeeDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("昵称")
    private String name;
    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("省份证号码")
    private String idNumber;

}
