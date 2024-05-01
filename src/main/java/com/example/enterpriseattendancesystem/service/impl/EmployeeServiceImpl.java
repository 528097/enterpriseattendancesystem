package com.example.enterpriseattendancesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.enterpriseattendancesystem.Util.JwtUtils;
import com.example.enterpriseattendancesystem.entity.Attendance;
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

    @Override
    public IPage<Employee> findPage(Employee employee, int pageNum, int pageSize) {
        Page<Employee> page = new Page<>(pageNum, pageSize);

        // 创建QueryWrapper对象，设置查询条件
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();

        // 添加逻辑未删除的条件
        queryWrapper.eq("version", 1);
        if (employee.getUsername() != null && !employee.getUsername().equals("")) {
            queryWrapper.like("username",employee.getUsername());
        }
        if (employee.getPosition() != null && !employee.getPosition().equals("")) {
            queryWrapper.like("position",employee.getPosition());
        }
        if (employee.getEmail() != null && !employee.getEmail().equals("")) {
            queryWrapper.like("email",employee.getEmail());
        }
        if (employee.getGender() != null && !employee.getGender().equals("")) {
            queryWrapper.eq("gender",employee.getGender());
        }
        if (employee.getId() != null) {
            queryWrapper.like("id",employee.getId());
        }
        IPage<Employee> employeeIPage = employeeMapper.selectPage(page, queryWrapper);

        // 返回查询结果
        return employeeIPage;
    }

    @Override
    public String addEmployee(Employee employee) {
        if (employee.getUsername() == null ) {
            return "用户名不能为空";
        }
        if (employee.getPassword() == null ) {
            return "密码不能为空";
        }
        if (employee.getGender() == null ) {
            return "性别不能为空";
        }
        if (employee.getEmail() == null ) {
            return "邮箱不能为空";
        }
        if (employee.getPosition() == null ) {
            return "职位不能为空";
        }
        employeeMapper.insert(employee);
        return "添加用户成功";
    }

    @Override
    public String updateEmployee(Long id,Employee employee) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Employee employee1 = employeeMapper.selectOne(queryWrapper);
        employee1.setEmail(employee.getEmail());
        employee1.setGender(employee.getGender());
        employee1.setUsername(employee.getUsername());
        employee1.setPassword(employee.getPassword());
        employee1.setPosition(employee.getPosition());
        boolean isUpdated = employeeMapper.update(employee1,queryWrapper) > 0;
        // 如果更新成功，返回新的更新后的考勤记录
        if (isUpdated) {
            return "更新成功:"+employeeMapper.selectOne(queryWrapper);
        } else {
            return "更新失败";
        }
    }

    @Override
    public String deleteEmployee(Long id) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Employee employee1 = employeeMapper.selectOne(queryWrapper);
        employee1.setVersion(0);
        boolean isUpdated = employeeMapper.update(employee1,queryWrapper) > 0;
        // 如果更新成功，返回新的更新后的考勤记录
        if (isUpdated) {
            return "删除成功:"+employeeMapper.selectOne(queryWrapper);
        } else {
            return "删除失败";
        }
    }

}




