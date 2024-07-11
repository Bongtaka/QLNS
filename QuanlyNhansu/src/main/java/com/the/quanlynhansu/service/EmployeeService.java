package com.the.quanlynhansu.service;

import com.the.quanlynhansu.entity.EmployeeEntity;
import com.the.quanlynhansu.model.dto.EmployeeDTO;
import com.the.quanlynhansu.model.request.EmployeeRequest;
import com.the.quanlynhansu.model.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface EmployeeService {


    BaseResponse<Page<EmployeeDTO>> getAllEmployee(EmployeeRequest employeeRequest, int page, int size);

    BaseResponse<?> createEmployee(EmployeeDTO employeeDTO);

    BaseResponse<?> deleteEmployee(Long id);

    BaseResponse<EmployeeDTO> updateEmploy(long employeeId, EmployeeDTO employeeDTO);
}
