package com.example.enterpriseattendancesystem.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.enterpriseattendancesystem.Util.JwtUtils;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.example.enterpriseattendancesystem.entity.Leave;
import com.example.enterpriseattendancesystem.request.AttendanceRequest;
import com.example.enterpriseattendancesystem.request.LeaveRequest;
import com.example.enterpriseattendancesystem.response.LoginResponse;
import com.example.enterpriseattendancesystem.service.IEmployeeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-20
 */
@CrossOrigin(origins = "http://localhost:8080")
@Controller
@RequestMapping("/employee")
public class EmployeeController {
    private final IEmployeeService employeeService;


    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping("/register")
    public ResponseEntity<?> register(@RequestBody Employee employee) {
        boolean result = employeeService.register(employee);
        if (result) {
            return ResponseEntity.ok("注册成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("注册失败");
        }
    }
    @RequestMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        LoginResponse response = employeeService.login(email, password);
        if (response.isSuccess()) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", response.getToken());
            return new ResponseEntity<>("登录成功", headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/list")
    @ResponseBody
    @RequiresPermissions("employee:list")
    public IPage<Employee> employeeList(
            @RequestBody Employee employee,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        IPage<Employee> page = employeeService.findPage(employee, pageNum, pageSize);
        return page;
    }
    @PostMapping("/add")
    @ResponseBody
    @RequiresPermissions("employee:add")
    public String addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    @RequiresPermissions("employee:update")
    public String updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id,employee);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    @RequiresPermissions("employee:delete")
    public String deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteEmployee(id);
    }
}

