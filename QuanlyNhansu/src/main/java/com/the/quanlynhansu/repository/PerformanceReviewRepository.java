package com.the.quanlynhansu.repository;

import com.the.quanlynhansu.entity.PerformanceReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReviewEntity, Long> {
}
