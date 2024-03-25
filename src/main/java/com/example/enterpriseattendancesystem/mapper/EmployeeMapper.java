package com.example.enterpriseattendancesystem.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-20
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {


}
