package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.LinkOption;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */

    @ApiOperation(value = "登录方法")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 修改密码
     * @param editDTO
     * @return
     */
    @ApiOperation(value = "修改密码")
    @PutMapping("editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO editDTO){
        Result result =  employeeService.editPassword(editDTO);
        return result;
    }

    /**
     * 增加新用户
     * @param employeeDTO
     * @return
     */
    @ApiOperation(value = "增加新员工")
    @PostMapping
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO){
        Result  result = employeeService.addEmployee(employeeDTO);
        return result;
    }

    @GetMapping("{id}")
    @ApiOperation(value = "根据ID查询员工信息")
    public Result<Employee> selectEmployeeById(@PathVariable Long id){
        Result<Employee> result = employeeService.selectEmployeeById(id);
        return result;
    }

    @PutMapping
    @ApiOperation(value = "编辑员工信息")
    public Result editEmployeeInfo(@RequestBody EmployeeDTO employeeDTO){
        Result result = employeeService.editEmployeeInfo(employeeDTO);
        return result;
    }

    @GetMapping("page")
    @ApiOperation(value = "分页查询")
    public Result<PageResult> findEmployeeToPage(EmployeePageQueryDTO employeePageQueryDTO){
        Result<PageResult> result = employeeService.findEmployeeToPage(employeePageQueryDTO);
        return result;
    }

    @PostMapping("status/{status}")
    @ApiOperation("禁用和启用员工账户")
    public Result switchStatus(@PathVariable Integer status, Integer id){
        Result result = employeeService.switchStatus(status,id);
        return result;
    }

}
