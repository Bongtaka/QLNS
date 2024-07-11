package com.the.quanlynhansu.model.request;

import com.the.quanlynhansu.model.dto.EmployeeDTO;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentRequest {
    private Long id;
    private String name;
    private String code;
    private long idEmployee;
    private List<EmployeeDTO> employees;
}
