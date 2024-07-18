package com.the.quanlynhansu.service.impl;


import com.the.quanlynhansu.entity.EmployeeEntity;
import com.the.quanlynhansu.entity.PerformanceReviewEntity;
import com.the.quanlynhansu.model.dto.PerformanceReviewDTO;
import com.the.quanlynhansu.model.response.BaseResponse;
import com.the.quanlynhansu.repository.EmployeeRepository;
import com.the.quanlynhansu.repository.PerformanceReviewRepository;
import com.the.quanlynhansu.service.PerformanceReviewSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PerformanceReviewImpl implements PerformanceReviewSevice {

    @Autowired
    public PerformanceReviewRepository performanceReviewRepository;

    @Autowired
    public EmployeeRepository employeeRepository;
    @Override
    public BaseResponse<Page<PerformanceReviewDTO>> getAll(PerformanceReviewDTO performanceReviewDTO, int page, int size) {
        // Tạo đối tượng Pageable để phân trang
        Pageable pageable = PageRequest.of(page, size);

        // Truy vấn các đánh giá hiệu suất từ repository với phân trang
        Page<PerformanceReviewEntity> performanceReviewEntities = performanceReviewRepository.findAll(pageable);

        // Chuyển đổi các thực thể (entities) sang DTOs
        Page<PerformanceReviewDTO> performanceReviewDTOS = performanceReviewEntities.map(this::convertToDTO);

        // Tạo đối tượng BaseResponse để chứa kết quả trả về
        BaseResponse<Page<PerformanceReviewDTO>> baseResponse = new BaseResponse<>();
        baseResponse.setCode(HttpStatus.OK.value());
        baseResponse.setMessage("Lấy danh sách đánh giá hiệu suất thành công");
        baseResponse.setData(performanceReviewDTOS);

        return baseResponse;
    }

    @Override
    public PerformanceReviewDTO createPerformanceReview(PerformanceReviewDTO performanceReviewDTO) {
        EmployeeEntity employee = employeeRepository.findById(performanceReviewDTO.getEmployeeId()).orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        PerformanceReviewEntity performanceReview = new PerformanceReviewEntity();
        performanceReview.setDate(performanceReviewDTO.getDate());
        performanceReview.setComments(performanceReviewDTO.getComments());
        performanceReview.setEmployee(employee);

        PerformanceReviewEntity savedPerformanceReview = performanceReviewRepository.save(performanceReview);

        return convertToDTO(savedPerformanceReview);
    }

    @Override
    public PerformanceReviewDTO getPerformanceReviewById(Long id) {
        PerformanceReviewEntity performanceReview = performanceReviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Performance review not found"));
        return convertToDTO(performanceReview);
    }

    private PerformanceReviewDTO convertToDTO(PerformanceReviewEntity performanceReview) {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setDate(performanceReview.getDate());
        dto.setComments(performanceReview.getComments());
        dto.setEmployeeId(performanceReview.getEmployee().getId());
        return dto;
    }

}
