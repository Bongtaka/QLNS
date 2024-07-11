package com.the.quanlynhansu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "trainingsession")
public class TrainingSessionEntity extends AbstractEntity {
    private String name;
    private LocalDateTime date;
    private String location;

}
