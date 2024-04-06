package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        /*通过用户名查询数据库之后并没有插到数据，就会返回null*/
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 修改密码
     * @param editDTO
     * @return
     */
    @Override
    @ApiOperation("修改密码的操作")
    public Result editPassword(PasswordEditDTO editDTO) {
        String password = editDTO.getOldPassword();
        Employee employee = employeeMapper.selectById(editDTO.getEmpId());
        //从数据库中获得的密码应该是通过加密的
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(employee.getPassword())){
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_EDIT_FAILED);
        }
        password = DigestUtils.md5DigestAsHex(editDTO.getNewPassword().getBytes());
        //验证密码通过之后，修改旧的密码
        employee.setPassword(password);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
        return Result.success();
    }

    @Override
    @ApiOperation("添加新员工的方法")
    public Result addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.insertEmployee(employee);

        return Result.success();
    }

    @Override
    public Result<Employee> selectEmployeeById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        employee.setPassword("");

        return Result.success(employee);
    }

    @Override
    public Result editEmployeeInfo(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setUpdateUser(BaseContext.getCurrentId());
        employee.setUpdateTime(LocalDateTime.now());
        employeeMapper.update(employee);
        return Result.success();
    }

    @Override
    public Result<PageResult> findEmployeeToPage(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        List<Employee> employeeList = employeeMapper.selectPage(employeePageQueryDTO);
        PageInfo<Employee> employeePageInfo = new PageInfo<>(employeeList);
        PageResult pageResult = new PageResult(employeePageInfo.getTotal(), employeePageInfo.getList());
        return Result.success(pageResult);
    }

    @Override
    public Result switchStatus(Integer status, Integer id) {
        Employee employee = Employee.builder()
                .id(Long.valueOf(id))
                .status(status)
                .build();

//        TODO：我感觉这里应该还要再做判断
        employeeMapper.update(employee);
        return Result.success();

    }

}
