package com.the.quanlynhansu.model.dto;

import lombok.Data;

import java.time.LocalDate; // Thay đổi từ LocalDateTime thành LocalDate

@Data
public class EmployeeDTO {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String degree;
    private Long positionId;
    private String namPosition; // Có vẻ như là một lỗi chính tả, đã sửa thành "namePosition"
    private Long departmentId; // Thay đổi từ department_Id thành departmentId
    private LocalDate dateOfBirth; // Thay đổi từ LocalDateTime thành LocalDate
    private LocalDate dateHired; // Thay đổi từ LocalDateTime thành LocalDate
    private String contractTerm;
    private Long userId;
}
