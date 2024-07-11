package com.the.quanlynhansu.repository;

import com.the.quanlynhansu.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {
    List<AttendanceEntity> findByEmployeeIdAndCheckInTimeBetween(Long employeeId, LocalDateTime start, LocalDateTime end);
}
