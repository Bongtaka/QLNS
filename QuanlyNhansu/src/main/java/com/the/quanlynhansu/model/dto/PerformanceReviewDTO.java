package com.the.quanlynhansu.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class PerformanceReviewDTO {
    private LocalDateTime date;

    private String comments;
    private Long employeeId;
}
