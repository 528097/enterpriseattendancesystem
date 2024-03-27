package com.example.enterpriseattendancesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.enterpriseattendancesystem.Util.JwtUtils;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.example.enterpriseattendancesystem.mapper.AttendanceMapper;
import com.example.enterpriseattendancesystem.mapper.EmployeeMapper;
import com.example.enterpriseattendancesystem.service.IAttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-22
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {
    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeServiceImpl employeeServiceimpl;

    @Override
    public IPage<Attendance> findById(int pageNum, int pageSize) {
        String email = JwtUtils.getClaimByToken(getToken()).getSubject();
        Employee auditor = employeeServiceimpl.findByEmail(email);
        // 创建 Page 对象，设置当前页码和每页显示的数量
        Page<Attendance> page = new Page<>(pageNum, pageSize);
        // 创建 QueryWrapper 对象，设置查询条件为员工 ID
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", auditor.getId()).eq("version",1);
        // 使用 MyBatis-Plus 提供的分页查询方法，传入 Page 对象和 QueryWrapper 对象
        IPage<Attendance> attendancePage = attendanceMapper.selectPage(page, queryWrapper);
        return attendancePage;
    }

    @Override
    public String deleteById(Long id) {
        // 执行查询操作
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Attendance attendance = attendanceMapper.selectOne(queryWrapper);
        if (attendance != null) {
            // 更新version字段为0
            attendance.setVersion(0);
            // 执行更新操作
            attendanceMapper.update(attendance,queryWrapper);
            return "删除成功";
        } else {
            // 可以添加日志，表明ID不存在
            // logger.error("Attendance with id " + id + " does not exist.");
            return "删除失败";
        }
    }

    @Override
    public String saveStart() {
        //创建新的打卡记录
        Attendance attendance = new Attendance();
        // 使用Token获取用户信息
        String email = JwtUtils.getClaimByToken(getToken()).getSubject();
        Employee auditor = employeeServiceimpl.findByEmail(email);
        attendance.setEmployeeId(auditor.getId());
        attendance.setName(auditor.getUsername());
        // 获取当前时间
        Date now = new Date();
        //转换格式
        ZonedDateTime zdt = now.toInstant().atZone(ZoneId.systemDefault());
        LocalTime startTime = zdt.toLocalTime();
        attendance.setStartTime(startTime);
        //新增代码段：将Date类型的now转为LocalDate类型，并赋值给attendance
        LocalDate date = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        attendance.setDate(date);

        //判断并设置状态
        if(new Time(now.getTime()).before(Time.valueOf("10:00:00"))){
            attendance.setStatus("正常");
        } else {
            attendance.setStatus("迟到");
        }
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",auditor.getId());
        //获取员工姓名
        Employee employee = employeeMapper.selectOne(queryWrapper);
        String employeeName = employee.getUsername();

        //存储打卡信息
        attendanceMapper.insert(attendance);

        //返回打卡信息
        return "打卡成功时间:" + now + "姓名:"+ employeeName + "状态:"+ attendance.getStatus();
    }

    @Override
    public Object update(Long id, Attendance attendance) {
        String email = JwtUtils.getClaimByToken((String) SecurityUtils.getSubject().getPrincipal()).getSubject();
        Employee auditor = employeeServiceimpl.findByEmail(email);
        if (auditor.getUsername().equals(attendance.getName())){
            return "你不能修改自己的考勤记录";
        }
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",attendance.getId());
        // 然后，调用MyBatis-Plus提供的updateById方法，传入新的考勤记录
        boolean isUpdated = attendanceMapper.update(attendance,queryWrapper) > 0;
        // 如果更新成功，返回新的更新后的考勤记录
        if (isUpdated) {
            return attendanceMapper.selectOne(queryWrapper);
        } else {
            return null;
        }
    }

    @Override
    public String saveEnd(Long id) {
        // 创建新的QueryWrapper，设置查询条件为员工ID
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", id).orderByDesc("date");
        // 查询出最新的打卡记录
        Attendance attendance = attendanceMapper.selectOne(queryWrapper);
        // 获取当前时间
        Date now = new Date();
        // 转换格式
        ZonedDateTime zdt = now.toInstant().atZone(ZoneId.systemDefault());
        LocalTime endTime = zdt.toLocalTime();
        // 设定下班时间
        attendance.setEndTime(endTime);
        // 判断并设置状态
        if (new Time(now.getTime()).after(Time.valueOf("18:00:00"))) {
            attendance.setStatus("正常");
        } else {
            attendance.setStatus("早退");
        }
        // 更新打卡信息
        attendanceMapper.updateById(attendance);
        // 返回打卡信息
        return "打卡成功时间: " + now + " 姓名: "+ attendance.getEmployeeId() + " 状态: " + attendance.getStatus();
    }
    public String getToken() {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader("Authorization");
        // 假设Token是Bearer类型的，需要去掉前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }

}
