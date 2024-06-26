package com.example.enterpriseattendancesystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.enterpriseattendancesystem.response.LoginResponse;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-20
 */
public interface IEmployeeService extends IService<Employee> {

    boolean  register(Employee employee);

    LoginResponse login(String email, String password);

    Employee findByEmail(String email);

    boolean existsByEmail(String email);

    IPage<Employee> findPage(Employee employee, int pageNum, int pageSize);

    String addEmployee(Employee employee);

    String updateEmployee(Long id,Employee employee);

    String deleteEmployee(Long id);
}
