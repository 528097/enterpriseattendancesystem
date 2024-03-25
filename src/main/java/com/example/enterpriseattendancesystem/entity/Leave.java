package com.example.enterpriseattendancesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 员工请假记录表
 * </p>
 *
 * @author 谢妍雨
 * @since 2024-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请假记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工ID
     */
    private Integer employeeId;

    /**
     * 请假开始时间
     */
    private LocalDateTime startTime;

    /**
     * 请假结束时间
     */
    private LocalDateTime endTime;

    /**
     * 请假原因
     */
    private String leaveReason;

    /**
     * 审核状态
     */
    private String status;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 版本控制
     */
    private String version;

    /**
     * 员工姓名
     */
    private String name;


}
