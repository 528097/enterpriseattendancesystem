package com.example.enterpriseattendancesystem.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.example.enterpriseattendancesystem.entity.Leave;
import com.example.enterpriseattendancesystem.request.LeaveRequest;
import com.example.enterpriseattendancesystem.service.ILeaveService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
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
@CrossOrigin(origins = "http://localhost:8080")
@Controller
@RequestMapping("/leave")
public class LeaveController {
    private ILeaveService leaveService;

    public LeaveController(ILeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/apply")
    @ResponseBody
    @RequiresPermissions("leave:apply")
    public String createLeave(@RequestBody LeaveRequest leaveRequest) {
        return leaveService.saveLeave(leaveRequest);
    }
    @PostMapping("/approve/{id}")
    @ResponseBody
    @RequiresPermissions("leave:approve")
    public String approveLeave(
            @PathVariable Long id,
            @RequestParam String status) {
        return leaveService.saveLeaveApproval(id, status);
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    @RequiresPermissions("leave:delete")
    public String deleteAttendance(@PathVariable Long id) {
        return leaveService.deleteLeave(id);
    }
    @PostMapping("/list")
    @ResponseBody
    @RequiresPermissions("leave:list")
    public IPage<Leave> leaveList(
            @RequestBody LeaveRequest request,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        IPage<Leave> page = leaveService.findAll(request, pageNum, pageSize);
        return page;
    }


}
