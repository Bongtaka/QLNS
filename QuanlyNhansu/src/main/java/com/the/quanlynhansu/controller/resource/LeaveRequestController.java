package com.the.quanlynhansu.controller.resource;
import com.the.quanlynhansu.model.dto.LeaveRequestDTO;
import com.the.quanlynhansu.model.response.BaseResponse;
import com.the.quanlynhansu.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaverequests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<LeaveRequestDTO> createLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        LeaveRequestDTO createdLeaveRequest = leaveRequestService.createLeaveRequest(leaveRequestDTO);
        return ResponseEntity.ok(createdLeaveRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequestDTO> updateLeaveRequestStatus(@PathVariable Long id, @RequestParam String status) {
        LeaveRequestDTO updatedLeaveRequest = leaveRequestService.updateLeaveRequestStatus(id, status);
        return ResponseEntity.ok(updatedLeaveRequest);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequestDTO>> getLeaveRequestsByEmployee(@PathVariable Long employeeId) {
        List<LeaveRequestDTO> leaveRequests = leaveRequestService.getLeaveRequestsByEmployee(employeeId);
        return ResponseEntity.ok(leaveRequests);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponse<?>> deleteLeaveRequest(@PathVariable("id") Long id){
        BaseResponse<?> response = leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
