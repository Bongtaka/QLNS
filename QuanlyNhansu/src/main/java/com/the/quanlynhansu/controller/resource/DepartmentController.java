package com.the.quanlynhansu.controller.resource;

import com.the.quanlynhansu.model.dto.DepartmentDTO;
import com.the.quanlynhansu.model.dto.EmployeeDTO;
import com.the.quanlynhansu.model.request.DepartmentRequest;
import com.the.quanlynhansu.model.request.EmployeeRequest;
import com.the.quanlynhansu.model.response.BaseResponse;
import com.the.quanlynhansu.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // API để lấy danh sách phòng ban
    @PostMapping
    public ResponseEntity<BaseResponse<?>> getAllDepartments(@RequestBody DepartmentRequest departmentRequest,
                                                                  @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                  @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(departmentService.getAllDeparment(departmentRequest, page, size));
    }


    // API để tạo mới phòng ban
    @PostMapping("creat/{departmentId}")
    public ResponseEntity<BaseResponse<?>> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        BaseResponse<?> response = departmentService.createDeparment(departmentDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // API để cập nhật phòng ban
    @PutMapping("update/{departmentId}")
    public ResponseEntity<BaseResponse<DepartmentDTO>> updateDepartment(@PathVariable Long departmentId,
                                                                        @RequestBody DepartmentDTO departmentDTO) {
        BaseResponse<DepartmentDTO> response = departmentService.updatedeparment(departmentId, departmentDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // API để xóa phòng ban
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<BaseResponse<?>> deleteDepartment(@PathVariable Long departmentId) {
        BaseResponse<?> response = departmentService.deleteDepartment(departmentId);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
