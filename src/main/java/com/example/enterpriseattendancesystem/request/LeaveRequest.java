package com.example.enterpriseattendancesystem.request;

import lombok.Data;

@Data
public class LeaveRequest {
    private String leaveReason;
    private String startDate;
    private String endDate;
    private Integer employeeId;
    private String auditor;
    private String name;
    private String status;
}
