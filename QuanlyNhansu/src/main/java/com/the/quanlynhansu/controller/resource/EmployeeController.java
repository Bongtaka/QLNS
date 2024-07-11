package com.the.quanlynhansu.controller.resource;


import com.the.quanlynhansu.model.dto.EmployeeDTO;
import com.the.quanlynhansu.model.request.EmployeeRequest;
import com.the.quanlynhansu.model.response.BaseResponse;
import com.the.quanlynhansu.repository.EmployeeRepository;
import com.the.quanlynhansu.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/admin")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Logging trong controller
    @PostMapping
    public ResponseEntity<BaseResponse<Page<EmployeeDTO>>> getAll(@RequestBody EmployeeRequest employeeRequest,
                                                                  @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                  @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.getAllEmployee(employeeRequest, page, size));
    }
    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponse<?>> deleteEmployee(@PathVariable("id") Long id){
        BaseResponse<?> response = employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

