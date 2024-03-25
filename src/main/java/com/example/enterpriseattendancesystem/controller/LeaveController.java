package com.example.enterpriseattendancesystem.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.example.enterpriseattendancesystem.entity.Leave;
import com.example.enterpriseattendancesystem.service.ILeaveService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 员工请假记录表 前端控制器
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-23
 */
@RestController
@RequestMapping("/leave")
public class LeaveController {
    private ILeaveService leaveService;

    public LeaveController(ILeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/{id}")
    @RequiresPermissions("attendance:leave")
    public String createLeave(
            @PathVariable Long id,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String leaveReason) {
        return leaveService.saveLeave(id, startDate, endDate, leaveReason);
    }
    @PostMapping("/approve/{id}")
    @RequiresPermissions("attendance:leave:approve")
    public String approveLeave(
            @PathVariable Long id,
            @RequestParam String status) {
        return leaveService.saveLeaveApproval(id, status);
    }
    @PostMapping("/list")
    @RequiresPermissions("attendance:leave:list")
    public IPage<Leave> leaveList(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        IPage<Leave> page = leaveService.findAll(status, pageNum, pageSize);
        return page;
    }


}
