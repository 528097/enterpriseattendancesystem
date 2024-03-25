package com.example.enterpriseattendancesystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.example.enterpriseattendancesystem.entity.Leave;
import com.baomidou.mybatisplus.extension.service.IService;

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

    String saveLeave(Long id, String startDate, String endDate, String leaveReason);

    String saveLeaveApproval(Long id , String status);

    IPage<Leave> findAll(String status, int pageNum, int pageSize);
}
