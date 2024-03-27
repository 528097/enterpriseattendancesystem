package com.example.enterpriseattendancesystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.example.enterpriseattendancesystem.request.AttendanceRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-22
 */
public interface IAttendanceService extends IService<Attendance> {

    IPage<Attendance> findById(int pageNum, int pageSize);

    String deleteById(Long id);

    String saveStart();

    Object update(Long id, AttendanceRequest request);

    String saveEnd();

    Employee userInfo();

    IPage<Attendance> conditionSearch(AttendanceRequest request,int pageNum, int pageSize);
}
