package com.example.enterpriseattendancesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.enterpriseattendancesystem.Util.JwtUtils;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.example.enterpriseattendancesystem.mapper.EmployeeMapper;
import com.example.enterpriseattendancesystem.response.LoginResponse;
import com.example.enterpriseattendancesystem.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-20
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    private final EmployeeMapper employeeMapper;
    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper=employeeMapper;
    }

    @Override
    public boolean register(Employee employee) {
        // 检查用户是否已存在
        if (existsByEmail(employee.getEmail())) {
            return false;
        }
        // 插入新用户
        int result = employeeMapper.insert(employee);
        return result > 0;
    }

    @Override
    public LoginResponse login(String email, String password) {
        Employee employee = this.findByEmail(email);
        if (employee != null && employee.getPassword().equals(password)) {
            String token = JwtUtils.generateToken(email); // 生成JWT
            return new LoginResponse(true, token, "登录成功");
        }
        return new LoginResponse(false, null, "登录失败: 用户名或密码错误");
    }

    @Override
    public Employee findByEmail(String email) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Employee employee = employeeMapper.selectOne(queryWrapper);
        return employee;
    }

    @Override
    public boolean existsByEmail(String email) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Integer count = employeeMapper.selectCount(queryWrapper);
        return count != null && count > 0;
    }
}




