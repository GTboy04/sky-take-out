package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Result editPassword(PasswordEditDTO editDTO);

    Result addEmployee(EmployeeDTO employeeDTO);

    Result<Employee> selectEmployeeById(Long id);

    Result editEmployeeInfo(EmployeeDTO employeeDTO);

    Result<PageResult> findEmployeeToPage(EmployeePageQueryDTO employeePageQueryDTO);

    Result switchStatus(Integer status, Integer id);
}
