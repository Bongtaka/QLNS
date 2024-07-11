package com.the.quanlynhansu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "jobposition")
public class PositionEntity extends AbstractEntity {
    private String name;


}
