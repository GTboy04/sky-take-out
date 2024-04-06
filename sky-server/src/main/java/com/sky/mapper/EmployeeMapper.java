package com.sky.mapper;

import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Select("select * from employee where id = #{empId}")
    Employee selectById(Long empId);

    void update(Employee employee);


    void insertEmployee(Employee employee);

    List<Employee> selectPage(EmployeePageQueryDTO employeePageQueryDTO);
}
