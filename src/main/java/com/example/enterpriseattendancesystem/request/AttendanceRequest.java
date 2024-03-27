package com.example.enterpriseattendancesystem.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AttendanceRequest {

    private Long id;

    private Integer employeeId;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String status;

    private Integer version;

    private String name;

}
