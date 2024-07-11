package com.the.quanlynhansu.service;

import com.the.quanlynhansu.entity.DepartmentEntity;
import com.the.quanlynhansu.model.dto.DepartmentDTO;
import com.the.quanlynhansu.model.dto.EmployeeDTO;
import com.the.quanlynhansu.model.request.DepartmentRequest;
import com.the.quanlynhansu.model.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface DepartmentService {

    BaseResponse<Page<DepartmentDTO>> getAllDeparment(DepartmentRequest departmentRequest, int page, int size);

    BaseResponse<?> createDeparment(DepartmentDTO departmentDTO);
    BaseResponse<DepartmentDTO> updatedeparment(Long departmentId, DepartmentDTO dto);
    BaseResponse<?> deleteDepartment(Long departmentId);
}
