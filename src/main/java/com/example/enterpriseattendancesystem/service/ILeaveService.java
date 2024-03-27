package com.example.enterpriseattendancesystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.example.enterpriseattendancesystem.entity.Leave;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.enterpriseattendancesystem.request.LeaveRequest;

import java.util.Date;

/**
 * <p>
 * 员工请假记录表 服务类
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-23
 */
public interface ILeaveService extends IService<Leave> {

    String saveLeave(LeaveRequest leaveRequest);

    String saveLeaveApproval(Long id , String status);

    IPage<Leave> findAll(LeaveRequest request, int pageNum, int pageSize);

    String deleteLeave(Long id);
}
