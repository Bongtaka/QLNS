package com.the.quanlynhansu.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceDTO {
    private Long id;
    private String workday;
    private LocalDateTime checkInTime;  // Sửa lại tên biến thành checkInTime
    private LocalDateTime checkOutTime; // Sửa lại tên biến thành checkOutTime
    private boolean isOvertime;
    private long employeeId;
}
