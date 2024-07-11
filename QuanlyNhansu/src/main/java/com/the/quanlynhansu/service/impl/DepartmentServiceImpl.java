package com.the.quanlynhansu.service.impl;

import com.the.quanlynhansu.entity.DepartmentEntity;
import com.the.quanlynhansu.entity.EmployeeEntity;
import com.the.quanlynhansu.model.dto.DepartmentDTO;
import com.the.quanlynhansu.model.dto.EmployeeDTO;
import com.the.quanlynhansu.model.request.DepartmentRequest;
import com.the.quanlynhansu.model.response.BaseResponse;
import com.the.quanlynhansu.repository.DepartmentRepository;
import com.the.quanlynhansu.repository.EmployeeRepository;
import com.the.quanlynhansu.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public BaseResponse<Page<DepartmentDTO>> getAllDeparment(DepartmentRequest departmentRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DepartmentEntity> departmentEntities = departmentRepository.findFirstByDeparment(departmentRequest, pageable);

        List<DepartmentDTO> departmentDTOS = departmentEntities.getContent().stream().map(departmentEntity -> {
            DepartmentDTO dto = new DepartmentDTO();
            dto.setId(departmentEntity.getId());
            dto.setName(departmentEntity.getName());

            // Lấy danh sách employeeId từ DepartmentEntity
            List<Long> employeeIds = departmentEntity.getEmployees().stream()
                    .map(EmployeeEntity::getId)
                    .collect(Collectors.toList());
            dto.setEmployeeIds(employeeIds);

            // Map danh sách nhân viên từ DepartmentEntity sang EmployeeDTO
            List<EmployeeDTO> employeeDTOS = departmentEntity.getEmployees().stream()
                    .map(employeeEntity -> {
                        EmployeeDTO employeeDTO = new EmployeeDTO();
                        employeeDTO.setId(employeeEntity.getId());
                        employeeDTO.setFullname(employeeEntity.getFullname());
                        employeeDTO.setEmail(employeeEntity.getEmail());
                        employeeDTO.setPhone(employeeEntity.getPhone());
                        employeeDTO.setAddress(employeeEntity.getAddress());
                        employeeDTO.setDegree(employeeEntity.getDegree());
                        employeeDTO.setContractTerm(employeeEntity.getContractTerm());
                        return employeeDTO;
                    })
                    .collect(Collectors.toList());

            dto.setEmployees(employeeDTOS);
            return dto;
        }).collect(Collectors.toList());

        Page<DepartmentDTO> dtoPage = new PageImpl<>(departmentDTOS, pageable, departmentEntities.getTotalElements());

        BaseResponse<Page<DepartmentDTO>> baseResponse = new BaseResponse<>();
        baseResponse.setCode(HttpStatus.OK.value());
        baseResponse.setMessage("Thành công");
        baseResponse.setData(dtoPage);
        return baseResponse;
    }

    @Override
    public BaseResponse<?> createDeparment(DepartmentDTO departmentDTO) {
        BaseResponse<?> response = new BaseResponse<>();
        DepartmentEntity department = new DepartmentEntity();
        department.setName(departmentDTO.getName());
        department.setCode(departmentDTO.getCode());

        // Mapping danh sách nhân viên từ DTO sang Entity
        List<EmployeeEntity> employees = departmentDTO.getEmployees().stream()
                .map(dto -> {
                    EmployeeEntity employee = new EmployeeEntity();
                    employee.setFullname(dto.getFullname());
                    employee.setEmail(dto.getEmail());
                    employee.setPhone(dto.getPhone());
                    employee.setAddress(dto.getAddress());
                    employee.setDegree(dto.getDegree());
                    employee.setDepartment(department); // Liên kết nhân viên với phòng ban
                    return employee;
                })
                .collect(Collectors.toList());

        department.setEmployees(employees); // Thiết lập danh sách nhân viên cho phòng ban

        DepartmentEntity departmentEntity = departmentRepository.save(department);

        if (departmentEntity == null) {
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Không thể thêm phòng ban");
            return response;
        }

        response.setCode(HttpStatus.OK.value());
        response.setMessage("Thêm phòng ban thành công");
        return response;
    }

    @Override
    public BaseResponse<DepartmentDTO> updatedeparment(Long departmentId, DepartmentDTO dto) {
        BaseResponse<DepartmentDTO> response = new BaseResponse<>();
        Optional<DepartmentEntity> optionalDepartment = departmentRepository.findById(departmentId);
        if (optionalDepartment.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Không tìm thấy phòng ban");
            return response;
        }

        DepartmentEntity departmentEntity = optionalDepartment.get();
        departmentEntity.setName(dto.getName());
        departmentEntity.setCode(dto.getCode());

        // Mapping danh sách nhân viên từ DTO sang Entity
        List<EmployeeEntity> employees = dto.getEmployees().stream()
                .map(empDto -> {
                    Optional<EmployeeEntity> optionalEmployee = employeeRepository.findById(empDto.getId());
                    if (optionalEmployee.isPresent()) {
                        EmployeeEntity employee = optionalEmployee.get();
                        employee.setFullname(empDto.getFullname());
                        employee.setEmail(empDto.getEmail());
                        employee.setPhone(empDto.getPhone());
                        employee.setAddress(empDto.getAddress());
                        employee.setDegree(empDto.getDegree());
                        employee.setDepartment(departmentEntity); // Liên kết nhân viên với phòng ban
                        return employee;
                    } else {
                        // Nếu không tìm thấy nhân viên, tạo mới và liên kết với phòng ban
                        EmployeeEntity newEmployee = new EmployeeEntity();
                        newEmployee.setFullname(empDto.getFullname());
                        newEmployee.setEmail(empDto.getEmail());
                        newEmployee.setPhone(empDto.getPhone());
                        newEmployee.setAddress(empDto.getAddress());
                        newEmployee.setDegree(empDto.getDegree());
                        newEmployee.setDepartment(departmentEntity);
                        return newEmployee;
                    }
                })
                .collect(Collectors.toList());

        departmentEntity.setEmployees(employees); // Thiết lập danh sách nhân viên mới cho phòng ban

        departmentRepository.save(departmentEntity);

        response.setCode(HttpStatus.OK.value());
        response.setMessage("Sửa thành công");
        response.setData(dto);
        return response;
    }

    @Override
    public BaseResponse<?> deleteDepartment(Long departmentId) {
        BaseResponse<?> response = new BaseResponse<>();

        // Kiểm tra sự tồn tại của phòng ban
        Optional<DepartmentEntity> department = departmentRepository.findById(departmentId);
        if (department.isEmpty()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Không tìm thấy phòng ban");
            return response;
        }

        // Lấy thông tin phòng ban
        DepartmentEntity departmentEntity = department.get();

        // Xóa phòng ban
        departmentRepository.delete(departmentEntity);

        // Kiểm tra xem việc xóa có thành công hay không
        if (departmentRepository.existsById(departmentId)) {
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Không thể xóa phòng ban");
            return response;
        }

        // Thiết lập phản hồi thành công
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Xóa phòng ban thành công");
        return response;
    }
}
