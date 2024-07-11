package com.the.quanlynhansu.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String employeeId;
    List<RoleDTO> roleDTOS;


}