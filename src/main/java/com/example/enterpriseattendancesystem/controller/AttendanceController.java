package com.example.enterpriseattendancesystem.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.example.enterpriseattendancesystem.request.AttendanceRequest;
import com.example.enterpriseattendancesystem.service.IAttendanceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-22
 */
@CrossOrigin(origins = "http://localhost:8080")
@Controller
@RequestMapping("/api/attendance")
public class AttendanceController {
    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);
    @Autowired
    private IAttendanceService attendanceService;

    @PostMapping("/setStartEndWorkTime")
    @ResponseBody
    @RequiresPermissions("attendance:setStartEndWorkTime")
    public String setStartWorkTime(
            @RequestBody AttendanceRequest request) {
        return attendanceService.setStartEndWorkTime(request.getStartTime(), request.getEndTime());
    }
    @PostMapping("/getStartEndWorkTime")
    @ResponseBody
    @RequiresPermissions("attendance:getStartEndWorkTime")
    public String getStartWorkTime() {
        return attendanceService.getStartEndWorkTime();
    }


    @PostMapping("/get")
    @ResponseBody
    @RequiresPermissions("attendance:get")
    public IPage<Attendance> getAttendance(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        // 调用 Service 层的方法获取数据
        IPage<Attendance> page = attendanceService.findById(pageNum, pageSize);
        return page;
    }
    @PostMapping("/get/want")
    @ResponseBody
    @RequiresPermissions("attendance:get/want")
    public IPage<Attendance> getAttendanceWant(
            @RequestBody AttendanceRequest request,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        // 调用 Service 层的方法获取数据
        IPage<Attendance> page = attendanceService.conditionSearch(request,pageNum, pageSize);
        return page;
    }

    @PostMapping("/create")
    @ResponseBody
    @RequiresPermissions("attendance:create")
    public String createAttendance() {
        return attendanceService.saveStart();
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    @RequiresPermissions("attendance:update")
    public Object updateAttendance(@PathVariable Long id, @RequestBody AttendanceRequest request) {
        return attendanceService.update(id, request);
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    @RequiresPermissions("attendance:delete")
    public String deleteAttendance(@PathVariable Long id) {
        return attendanceService.deleteById(id);
    }

    @PostMapping("/end")
    @ResponseBody
    @RequiresPermissions("attendance:end")
    public String endAttendance() {
        return attendanceService.saveEnd();
    }

    @PostMapping("/userinfo")
    @ResponseBody
    @RequiresPermissions("attendance:userinfo")
    public Employee userInfo() {
        return attendanceService.userInfo();
    }

    @PostMapping("/statistics")
    @ResponseBody
    @RequiresPermissions("attendance:statistics")
    public Map<String, Object> getAttendanceStatistics(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day) {
        return attendanceService.getStatistics(year, month, day);
    }
}
