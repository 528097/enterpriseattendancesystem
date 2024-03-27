package com.example.enterpriseattendancesystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.baomidou.mybatisplus.extension.service.IService;

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

    Object update(Long id, Attendance attendance);

    String saveEnd(Long id);
}
