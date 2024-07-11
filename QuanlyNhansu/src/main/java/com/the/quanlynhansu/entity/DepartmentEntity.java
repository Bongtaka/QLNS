package com.the.quanlynhansu.entity;


import com.the.quanlynhansu.model.dto.EmployeeDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "department")
public class DepartmentEntity extends AbstractEntity {
    private String code;
    private String name;


    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmployeeEntity> employees;



}
