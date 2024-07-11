package com.the.quanlynhansu.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentDTO {
    private Long id;
    private String name;
    private String code;
    private List<Long> employeeIds; // Sử dụng List<Long> để lưu các id của nhân viên
    private List<EmployeeDTO> employees; // Nếu cần, bạn có thể sử dụng List<EmployeeDTO> để lưu thông tin đầy đủ của nhân viên
}
