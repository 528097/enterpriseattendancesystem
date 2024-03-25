package com.example.enterpriseattendancesystem.controller;


import com.example.enterpriseattendancesystem.Util.JwtUtils;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.example.enterpriseattendancesystem.response.LoginResponse;
import com.example.enterpriseattendancesystem.service.IEmployeeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-20
 */
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.getMessage());
        }
    }
}

