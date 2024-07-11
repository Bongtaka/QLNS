package com.the.quanlynhansu.service.impl;
import com.the.quanlynhansu.entity.EmployeeEntity;
import com.the.quanlynhansu.entity.LeaveRequestEntity;
import com.the.quanlynhansu.entity.UserEntity;
import com.the.quanlynhansu.model.dto.LeaveRequestDTO;
import com.the.quanlynhansu.model.response.BaseResponse;
import com.the.quanlynhansu.repository.EmployeeRepository;
import com.the.quanlynhansu.repository.LeaveRequestRepository;
import com.the.quanlynhansu.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
        EmployeeEntity employee = employeeRepository.findById(leaveRequestDTO.getEmployeeId()).orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        LeaveRequestEntity leaveRequest = new LeaveRequestEntity();
        leaveRequest.setStartDate(leaveRequestDTO.getStartDate());
        leaveRequest.setEndDate(leaveRequestDTO.getEndDate());
        leaveRequest.setReason(leaveRequestDTO.getReason());
        leaveRequest.setStatus("pending"); // Mặc định là "pending"
        leaveRequest.setEmployee(employee);

        LeaveRequestEntity savedLeaveRequest = leaveRequestRepository.save(leaveRequest);

        return convertToDTO(savedLeaveRequest);
    }

    @Override
    public LeaveRequestDTO updateLeaveRequestStatus(Long id, String status) {
        LeaveRequestEntity leaveRequest = leaveRequestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Leave request not found"));
        leaveRequest.setStatus(status);
        LeaveRequestEntity updatedLeaveRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(updatedLeaveRequest);
    }

    @Override
    public List<LeaveRequestDTO> getLeaveRequestsByEmployee(Long employeeId) {
        Optional<LeaveRequestEntity> leaveRequests = leaveRequestRepository.findById(employeeId);
        return leaveRequests.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public BaseResponse<?> deleteLeaveRequest(Long id) {
        // Tạo một đối tượng BaseResponse để chứa kết quả trả về
        BaseResponse<?> baseResponse = new BaseResponse<>();

        // Tìm kiếm yêu cầu nghỉ phép bằng id
        Optional<LeaveRequestEntity> leaveRequest = leaveRequestRepository.findById(id);

        // Kiểm tra xem yêu cầu nghỉ phép có tồn tại hay không
        if (leaveRequest.isEmpty()) {
            // Nếu không tồn tại, trả về mã lỗi 404 và thông báo lỗi
            baseResponse.setCode(HttpStatus.NOT_FOUND.value());
            baseResponse.setMessage("Yêu cầu nghỉ phép không tồn tại");
            return baseResponse;
        }

        // Nếu tồn tại, lấy đối tượng LeaveRequestEntity
        LeaveRequestEntity leaveRequestEntity = leaveRequest.get();

        // Đánh dấu yêu cầu nghỉ phép này là đã bị xóa (bằng cách đặt thuộc tính deleted thành true)
        leaveRequestEntity.setDeleted(true);

        // Lưu lại yêu cầu nghỉ phép đã được đánh dấu là đã xóa vào cơ sở dữ liệu
        leaveRequestRepository.save(leaveRequestEntity);

        // Thiết lập mã trạng thái và thông báo thành công cho BaseResponse
        baseResponse.setCode(HttpStatus.OK.value());
        baseResponse.setMessage("Xóa thành công");
        return baseResponse;
    }


    private LeaveRequestDTO convertToDTO(LeaveRequestEntity leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leaveRequest.getId());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setReason(leaveRequest.getReason());
        dto.setStatus(leaveRequest.getStatus());
        dto.setEmployeeId(leaveRequest.getEmployee().getId());
        return dto;
    }
}

