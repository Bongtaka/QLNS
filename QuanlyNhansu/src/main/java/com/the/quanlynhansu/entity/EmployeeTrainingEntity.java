package com.the.quanlynhansu.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "employeetraining")
public class EmployeeTrainingEntity extends AbstractEntity {

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "session_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private TrainingSessionEntity trainingSessionEntity;
}
