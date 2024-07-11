package com.the.quanlynhansu.service;

import com.the.quanlynhansu.model.dto.AttendanceDTO;

import java.util.List;

public interface AttendanceSevice {
    AttendanceDTO checkIn(AttendanceDTO attendanceDTO);
    AttendanceDTO checkOut(Long id);
    List<AttendanceDTO> getMonthlyAttendance(Long employeeId, int month);
}
