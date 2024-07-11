package com.the.quanlynhansu.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "employee")
public class EmployeeEntity extends AbstractEntity {
    @Column(name = "full_name")
    private String fullname;

    private String email;
    private String phone;
    private String address;
    private String degree;

    @Column(name = "contract_term")
    private String contractTerm;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "date_hired")
    private LocalDateTime dateHired;

    @ManyToOne
    @JoinColumn(name = "position_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PositionEntity position;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEntity user;
}