package com.the.quanlynhansu.controller.resource;

import com.the.quanlynhansu.model.dto.PerformanceReviewDTO;
import com.the.quanlynhansu.model.response.BaseResponse;
import com.the.quanlynhansu.service.PerformanceReviewSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance-reviews")
public class PerformanceReviewController {

    @Autowired
    private PerformanceReviewSevice performanceReviewService;

    @PostMapping
    public ResponseEntity<PerformanceReviewDTO> createPerformanceReview(@RequestBody PerformanceReviewDTO performanceReviewDTO) {
        PerformanceReviewDTO createdPerformanceReview = performanceReviewService.createPerformanceReview(performanceReviewDTO);
        return ResponseEntity.ok(createdPerformanceReview);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceReviewDTO> getPerformanceReviewById(@PathVariable Long id) {
        PerformanceReviewDTO performanceReview = performanceReviewService.getPerformanceReviewById(id);
        return ResponseEntity.ok(performanceReview);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<PerformanceReviewDTO>>> getAllPerformanceReviews(
            @RequestParam int page,
            @RequestParam int size) {
        // Tạo một PerformanceReviewDTO rỗng để truyền vào service
        PerformanceReviewDTO performanceReviewDTO = new PerformanceReviewDTO();

        // Gọi phương thức getAll từ service và nhận về kết quả
        BaseResponse<Page<PerformanceReviewDTO>> response = performanceReviewService.getAll(performanceReviewDTO, page, size);

        // Trả về response entity với mã trạng thái OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

