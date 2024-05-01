package com.example.enterpriseattendancesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.enterpriseattendancesystem.Util.JwtUtils;
import com.example.enterpriseattendancesystem.entity.Attendance;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.example.enterpriseattendancesystem.mapper.AttendanceMapper;
import com.example.enterpriseattendancesystem.mapper.EmployeeMapper;
import com.example.enterpriseattendancesystem.request.AttendanceRequest;
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
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // 使用Token获取用户信息
        String email = JwtUtils.getClaimByToken(getToken()).getSubject();
        Employee auditor = employeeServiceimpl.findByEmail(email);
        LocalDate todayLocalDate = LocalDate.now();
        //获取上班标准时间
        QueryWrapper<Attendance> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name","上班时间和下班时间").eq("version", 0);
        Attendance attendance2 = attendanceMapper.selectOne(queryWrapper1);
        // 将LocalDate转换为Date类型
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",auditor.getUsername()).eq("date", todayLocalDate);;
        Attendance attendance1 = attendanceMapper.selectOne(queryWrapper);
        if (attendance1 != null) {
            return "你今天已经打卡过了";
        }
        Attendance attendance =new Attendance();
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

        // 获取当前时间
        LocalTime now1 = LocalTime.now();
        // 判断并设置状态
        if(now1.isBefore(attendance2.getStartTime())){
            attendance.setStatus("正常");
        } else {
            attendance.setStatus("迟到");
        }
        //获取员工姓名

        //存储打卡信息
        attendanceMapper.insert(attendance);

        //返回打卡信息
        return "打卡成功时间:" + now + "姓名:"+ auditor.getUsername() + "状态:"+ attendance.getStatus();
    }

    @Override
    public Object update(Long id, AttendanceRequest request) {
        String email = JwtUtils.getClaimByToken((String) SecurityUtils.getSubject().getPrincipal()).getSubject();
        Employee auditor = employeeServiceimpl.findByEmail(email);
        if (auditor.getUsername().equals(request.getName())){
            return "你不能修改自己的考勤记录";
        }
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",request.getId());
        Attendance attendance = attendanceMapper.selectOne(queryWrapper);
        attendance.setStatus(request.getStatus());
        attendance.setDate(request.getDate());
        attendance.setEndTime(request.getEndTime());
        attendance.setStartTime(request.getStartTime());
        // 然后，调用MyBatis-Plus提供的updateById方法，传入新的考勤记录
        boolean isUpdated = attendanceMapper.update(attendance,queryWrapper) > 0;
        // 如果更新成功，返回新的更新后的考勤记录
        if (isUpdated) {
            return "更新成功:"+attendanceMapper.selectOne(queryWrapper);
        } else {
            return "更新失败";
        }
    }

    @Override
    public String saveEnd() {
        // 创建新的QueryWrapper，设置查询条件为员工ID
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        //获取下班时间
        QueryWrapper<Attendance> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name","上班时间和下班时间").eq("version", 0);
        Attendance attendance2 = attendanceMapper.selectOne(queryWrapper1);
        // 使用Token获取用户信息
        String email = JwtUtils.getClaimByToken(getToken()).getSubject();
        Employee auditor = employeeServiceimpl.findByEmail(email);
        // 获取今天的日期（LocalDate格式）
        LocalDate todayLocalDate = LocalDate.now();
        // 将LocalDate转换为Date类型
        queryWrapper.eq("name",auditor.getUsername()).eq("date", todayLocalDate);;
        Attendance attendance = attendanceMapper.selectOne(queryWrapper);
        if (attendance == null) {
            return "没有找到你今日的打卡记录";
        }
        if (attendance.getEndTime()!=null) {
            return "你今天已经下班打卡过了,时间:"+attendance.getEndTime();
        }
        // 获取当前时间
        Date now = new Date();
        // 转换格式
        ZonedDateTime zdt = now.toInstant().atZone(ZoneId.systemDefault());
        LocalTime endTime = zdt.toLocalTime();
        // 设定下班时间
        attendance.setEndTime(endTime);
        String status = attendance.getStatus();
        // 获取当前时间
        LocalTime now1 = LocalTime.now();
        // 判断并设置状态
        if(now1.isAfter(attendance2.getEndTime())){
            attendance.setStatus(status + ",正常");
        } else {
            attendance.setStatus(status + ",早退");
        }
        // 更新打卡信息
        attendanceMapper.update(attendance,queryWrapper);
        // 返回打卡信息
        return "下班成功时间: " + now + " 姓名: "+ auditor.getUsername() + " 状态: " + attendance.getStatus();
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
    @Override
    public Employee userInfo() {
        String email = JwtUtils.getClaimByToken(getToken()).getSubject();
        Employee employee = employeeServiceimpl.findByEmail(email);
        return employee;
    }

    @Override
    public IPage<Attendance> conditionSearch(AttendanceRequest request,int pageNum, int pageSize) {
        // 创建分页对象
        Page<Attendance> page = new Page<>(pageNum, pageSize);

        // 创建QueryWrapper对象，设置查询条件
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();

        // 添加逻辑未删除的条件
        queryWrapper.eq("version", 1);

        // 根据请求参数添加条件
        if (request.getId() != null ) {
            queryWrapper.eq("id", request.getId());
        }
        if (request.getName() != null && !request.getName().isEmpty()) {
            queryWrapper.like("name", request.getName()); // 如果是模糊查询可以使用like
        }
        if (request.getEmployeeId() != null ) {
            queryWrapper.eq("employee_id", request.getEmployeeId());
        }
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            queryWrapper.like("status", request.getStatus());
        }
        if (request.getDate() != null) {
            queryWrapper.eq("date", request.getDate());
        }

        // 使用MyBatis-Plus提供的分页查询方法，传入Page对象和QueryWrapper对象
        IPage<Attendance> attendancePage = attendanceMapper.selectPage(page, queryWrapper);

        // 返回查询结果
        return attendancePage;
    }

    @Override
    public Map<String, Object> getStatistics(Integer year, Integer month, Integer day) {
        // 创建返回的结果集
        Map<String, Object> statistics = new HashMap<>();
        // 创建QueryWrapper对象, 设置查询条件
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status", "COUNT(*) as count").groupBy("status");

        // 根据年、月、日参数添加日期范围条件
        if (year != null) {
            LocalDate start = LocalDate.of(year, 1, 1);
            LocalDate end = LocalDate.of(year, 12, 31);
            if (month != null) {
                start = LocalDate.of(year, month, 1);
                end = start.with(TemporalAdjusters.lastDayOfMonth());
                if (day != null) {
                    start = LocalDate.of(year, month, day);
                    end = start;
                }
            }
            queryWrapper.between("date", start, end);
        }

        // 执行查询操作
        List<Map<String, Object>> results = attendanceMapper.selectMaps(queryWrapper);
        // 将查询结果转换为前端需要的格式
        for (Map<String, Object> result : results) {
            String status = (String) result.get("status");
            Long count = (Long) result.get("count");
            statistics.put(status, count);
        }
        return statistics;
    }

    @Override
    public String setStartEndWorkTime(LocalTime startTime,LocalTime endTime) {
        //获取下班时间
        QueryWrapper<Attendance> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name","上班时间和下班时间").eq("version", 0);
        Attendance attendance2 = attendanceMapper.selectOne(queryWrapper1);

        //设置新的上班时间
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",attendance2.getId());
        if (startTime != null) {
            attendance2.setStartTime(startTime);
        }
        if (endTime != null) {
            attendance2.setEndTime(endTime);
        }
        attendanceMapper.update(attendance2,queryWrapper);
        return "上班时间设置成功为：" + startTime + "下班时间设置成功为：" + endTime;
    }

    @Override
    public String getStartEndWorkTime() {
        QueryWrapper<Attendance> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name","上班时间和下班时间").eq("version", 0);
        Attendance attendance2 = attendanceMapper.selectOne(queryWrapper1);
        return "上班时间为：" + attendance2.getStartTime() + "下班时间为：" + attendance2.getEndTime();
    }


}
