package com.the.quanlynhansu.model.request;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class EmployeeRequest {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String degree;
    private Long departmentId;
    private Long positionId;
    private Long userId;
    private LocalDateTime dateOfBirth;
    private LocalDateTime dateHired;
    private String contractTerm;
    private String condition;


}
