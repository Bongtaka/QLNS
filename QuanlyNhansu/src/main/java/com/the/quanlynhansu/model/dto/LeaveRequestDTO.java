package com.the.quanlynhansu.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LeaveRequestDTO {
        private Long id;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String reason;
        private String status;
        private long employeeId;
    }


