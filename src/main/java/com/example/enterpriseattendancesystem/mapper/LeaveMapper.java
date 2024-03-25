package com.example.enterpriseattendancesystem.mapper;

import com.example.enterpriseattendancesystem.entity.Leave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 员工请假记录表 Mapper 接口
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-23
 */
@Mapper
public interface LeaveMapper extends BaseMapper<Leave> {

}
