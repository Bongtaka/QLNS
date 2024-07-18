package com.the.quanlynhansu.service;

import com.the.quanlynhansu.model.dto.PerformanceReviewDTO;
import com.the.quanlynhansu.model.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface PerformanceReviewSevice {

    BaseResponse<Page<PerformanceReviewDTO>> getAll(PerformanceReviewDTO performanceReviewDTO, int page, int size);
    PerformanceReviewDTO createPerformanceReview(PerformanceReviewDTO performanceReviewDTO);
    PerformanceReviewDTO getPerformanceReviewById(Long id);

}
