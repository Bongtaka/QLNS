package com.the.quanlynhansu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "attendance")

public class AttendanceEntity extends AbstractEntity {
    private String workday;
    private LocalDateTime checkInTime;  // Sửa lại tên biến thành checkInTime
    private LocalDateTime checkOutTime; // Sửa lại tên biến thành checkOutTime
    private boolean isOvertime;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private EmployeeEntity employee; // Sửa lại tên biến thành employee
}
