package com.the.quanlynhansu.service;

import com.the.quanlynhansu.model.dto.LeaveRequestDTO;
import com.the.quanlynhansu.model.response.BaseResponse;

import java.util.List;

public interface LeaveRequestService {
    LeaveRequestDTO createLeaveRequest(LeaveRequestDTO leaveRequestDTO);
    LeaveRequestDTO updateLeaveRequestStatus(Long id, String status);
    List<LeaveRequestDTO> getLeaveRequestsByEmployee(Long employeeId);
    BaseResponse<?> deleteLeaveRequest(Long id);
}
