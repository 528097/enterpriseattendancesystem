package com.example.enterpriseattendancesystem.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.example.enterpriseattendancesystem.service.IAttendanceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-22
 */
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);
    @Autowired
    private IAttendanceService attendanceService;

    @PostMapping("/get")
    @RequiresPermissions("attendance:get")
    public IPage<Attendance> getAttendance(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        // 调用 Service 层的方法获取数据
        IPage<Attendance> page = attendanceService.findById(pageNum, pageSize);
        return page;
    }

    @PostMapping("/create/{id}")
    @RequiresPermissions("attendance:create")
    public String createAttendance(@PathVariable Long id) {
        return attendanceService.saveStart(id);
    }

    @PostMapping("/update/{id}")
    @RequiresPermissions("attendance:update")
    public Object updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
        return attendanceService.update(id, attendance);
    }
    @PostMapping("/delete/{id}")
    @RequiresPermissions("attendance:delete")
    public void deleteAttendance(@PathVariable Long id) {
        logger.info("Attempting to delete attendance with ID: {}", id);
        attendanceService.deleteById(id);
        logger.info("Deleted attendance with ID: {}", id);
    }

    @PostMapping("/end/{id}")
    @RequiresPermissions("attendance:end")
    public String endAttendance(@PathVariable Long id) {
        return attendanceService.saveEnd(id);
    }


}
