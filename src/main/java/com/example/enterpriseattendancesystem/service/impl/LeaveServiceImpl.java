package com.example.enterpriseattendancesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.enterpriseattendancesystem.Util.JwtUtils;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.example.enterpriseattendancesystem.entity.Leave;
import com.example.enterpriseattendancesystem.mapper.EmployeeMapper;
import com.example.enterpriseattendancesystem.mapper.LeaveMapper;
import com.example.enterpriseattendancesystem.request.LeaveRequest;
import com.example.enterpriseattendancesystem.service.ILeaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.utility.DateUtil;
import org.apache.catalina.security.SecurityUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * <p>
 * 员工请假记录表 服务实现类
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-23
 */
@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements ILeaveService {
    @Autowired
    private LeaveMapper leaveMapper;
    @Autowired
    private EmployeeServiceImpl employeeServiceimpl;
    @Autowired
    private AttendanceServiceImpl attendanceServiceimpl;

    @Override
    public String saveLeave(LeaveRequest leaveRequest) {
        Employee employee = attendanceServiceimpl.userInfo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(leaveRequest.getStartDate(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(leaveRequest.getEndDate(), formatter);
        // 创建新的请假记录
        Leave leave = new Leave();
        leave.setName(employee.getUsername());
        leave.setEmployeeId(employee.getId());
        leave.setStartTime(startTime);
        leave.setEndTime(endTime);
        leave.setLeaveReason(leaveRequest.getLeaveReason());
        leave.setStatus("未审核");
        // 获取当前日期
        LocalDateTime currentTime = LocalDateTime.now();
        // 确保起始日期在结束日期之前并确保请假的开始日期不在今天之前
        if (startTime.isBefore(endTime) && !startTime.toLocalDate().isBefore(currentTime.toLocalDate())) {
            // 保存请假记录
            leaveMapper.insert(leave);

            // 计算时长(以小时为单位)
            long duration = ChronoUnit.HOURS.between(startTime, endTime);

            return "请假申请提交成功，时长：" + duration + "小时,请等待审核结果";
        } else {
            return "请假申请提交失败，确保起始日期在结束日期之前并且开始日期不在今天之前";
        }
    }

    @Override
    public String saveLeaveApproval(Long id , String status) {
        String email = JwtUtils.getClaimByToken((String) SecurityUtils.getSubject().getPrincipal()).getSubject();

        Employee auditor = employeeServiceimpl.findByEmail(email);
        // 获取请假记录
        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Leave leave = leaveMapper.selectOne(queryWrapper);

        if (leave != null) {
            if (leave.getName().equals(auditor.getUsername())) {
                return "你不能给自己审核请假";
            }
            // 设置审核人和审核状态
            leave.setAuditor(auditor.getUsername());
            leave.setStatus(status);
            // 更新请假记录
            leaveMapper.update(leave, queryWrapper);
            return "修改审核内容成功";
        } else {
            return "请假记录为空，修改失败";
        }
    }

    @Override
    public IPage<Leave> findAll(LeaveRequest request, int pageNum, int pageSize) {
        // 创建 Page 对象，设置当前页码和每页显示的数量
        Page<Leave> page = new Page<>(pageNum, pageSize);
        // 创建 QueryWrapper 对象，设置查询条件为员工 ID
        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("version",1);
        if (StringUtils.isNotEmpty(request.getStatus())) {
            // 如果状态非空，则添加状态查询条件
            queryWrapper.eq("status", request.getStatus());
        }
        if (StringUtils.isNotEmpty(request.getAuditor())) {
            // 如果审核人非空，则添加状态查询条件
            queryWrapper.eq("auditor", request.getAuditor());
        }
        if (StringUtils.isNotEmpty(request.getName())) {
            // 如果姓名非空，则添加状态查询条件
            queryWrapper.like("name", request.getName());
        }
        if (request.getEmployeeId() != null) {
            // 如果员工id非空，则添加状态查询条件
            queryWrapper.eq("employee_id", request.getEmployeeId());
        }
        // 使用 MyBatis-Plus 提供的分页查询方法，传入 Page 对象和 QueryWrapper 对象
        IPage<Leave> leavePage = leaveMapper.selectPage(page, queryWrapper);
        return leavePage;
    }

    @Override
    public String deleteLeave(Long id) {
        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Leave leave = leaveMapper.selectOne(queryWrapper);
        if (leave != null) {
            // 更新version字段为0
            leave.setVersion(0);
            // 执行更新操作
            leaveMapper.update(leave,queryWrapper);
            return "删除成功";
        } else {
            // 可以添加日志，表明ID不存在
            // logger.error("Attendance with id " + id + " does not exist.");
            return "删除失败";
        }
    }
}
