package com.the.quanlynhansu.service.impl;


import com.the.quanlynhansu.entity.DepartmentEntity;
import com.the.quanlynhansu.entity.EmployeeEntity;
import com.the.quanlynhansu.entity.PositionEntity;
import com.the.quanlynhansu.entity.UserEntity;
import com.the.quanlynhansu.model.dto.EmployeeDTO;
import com.the.quanlynhansu.model.request.EmployeeRequest;
import com.the.quanlynhansu.model.response.BaseResponse;
import com.the.quanlynhansu.repository.DepartmentRepository;
import com.the.quanlynhansu.repository.EmployeeRepository;
import com.the.quanlynhansu.repository.PositionRepository;
import com.the.quanlynhansu.repository.UserRepository;
import com.the.quanlynhansu.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private final PositionRepository positionRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final DepartmentRepository departmentRepository;
    @Autowired
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(PositionRepository positionRepository, UserRepository userRepository, DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.positionRepository = positionRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    public BaseResponse<Page<EmployeeDTO>> getAllEmployee(EmployeeRequest employeeRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeEntity> employeeEntities = employeeRepository.findFirstByEmployee(employeeRequest, pageable);
        List<EmployeeDTO> employeeDTOS = employeeEntities.getContent().stream().map(entity -> {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(entity.getId());
            employeeDTO.setFullname(entity.getFullname());
            employeeDTO.setAddress(entity.getAddress());
            employeeDTO.setEmail(entity.getEmail());
            employeeDTO.setDegree(entity.getDegree());
            employeeDTO.setPhone(entity.getPhone());
            employeeDTO.setDateOfBirth(LocalDate.from(entity.getDateOfBirth()));
            employeeDTO.setDateHired(LocalDate.from(entity.getDateHired()));
            employeeDTO.setContractTerm(entity.getContractTerm());
            return employeeDTO;
        }).collect(Collectors.toList());

        Page<EmployeeDTO> dtoPage = new PageImpl<>(employeeDTOS, pageable, employeeEntities.getTotalElements());
        BaseResponse<Page<EmployeeDTO>> baseResponse = new BaseResponse<>();
        baseResponse.setCode(HttpStatus.OK.value());
        baseResponse.setMessage("Thành công");
        baseResponse.setData(dtoPage);
        return baseResponse;
    }

    @Override
    public BaseResponse<?> createEmployee(EmployeeDTO employeeDTO) {
        BaseResponse<?> baseResponse = new BaseResponse<>();

        // Kiểm tra xem vị trí tồn tại hay không
        Optional<PositionEntity> position = positionRepository.findById(employeeDTO.getPositionId());
        if (position.isEmpty()) {
            baseResponse.setCode(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage("Không tồn tại chức vụ");
            return baseResponse;
        }

        // Kiểm tra xem bộ phận tồn tại hay không
        Optional<DepartmentEntity> departmentEntity = departmentRepository.findById(employeeDTO.getDepartmentId());
        if (departmentEntity.isEmpty()) {
            baseResponse.setCode(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage("Không tồn tại bộ phận");
            return baseResponse;
        }

        Optional<UserEntity> user = userRepository.findById(employeeDTO.getUserId());
        if (user.isEmpty()) {
            baseResponse.setCode(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage("Không tồn tại bộ phận");
            return baseResponse;
        }
        // Tạo đối tượng EmployeeEntity từ EmployeeDTO
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setPosition(position.get());
        employeeEntity.setDepartment(departmentEntity.get());
        employeeEntity.setDegree(employeeDTO.getDegree());
        employeeEntity.setAddress(employeeDTO.getAddress());
        employeeEntity.setEmail(employeeDTO.getEmail());
        employeeEntity.setFullname(employeeDTO.getFullname());
        employeeEntity.setContractTerm(employeeDTO.getContractTerm());
        employeeEntity.setDateHired(employeeDTO.getDateHired().atStartOfDay());
        employeeEntity.setPhone(employeeDTO.getPhone());
        employeeEntity.setUser(user.get());

        // Lưu đối tượng nhân viên vào cơ sở dữ liệu
        EmployeeEntity savedEntity = employeeRepository.save(employeeEntity);
        if (savedEntity == null) {
            baseResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessage("Không thể tạo mới nhân viên");
            return baseResponse;
        }

        baseResponse.setCode(HttpStatus.OK.value());
        baseResponse.setMessage("Thêm thành công");
        return baseResponse;
    }

    @Override
    public BaseResponse<?> deleteEmployee(Long id) {
        BaseResponse response = new BaseResponse<>();
        try {
            Optional<EmployeeEntity> employeeOptional = employeeRepository.findById(id);
            if (employeeOptional.isPresent()) {
                employeeRepository.delete(employeeOptional.get());
                response.setCode(HttpStatus.OK.value());
                response.setMessage("Xóa nhân viên thành công");
            } else {
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Không tìm thấy nhân viên với id: " + id);
            }
        } catch (Exception e) {
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Có lỗi xảy ra trong quá trình xóa nhân viên");
        }
        return response;
    }


    @Override
    public BaseResponse<EmployeeDTO> updateEmploy(long employeeId, EmployeeDTO employeeDTO) {
        BaseResponse<EmployeeDTO> response = new BaseResponse<>();
        Optional<EmployeeEntity> employee = employeeRepository.findById(employeeId);
        if (employee.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(" Không tìm thấy nhân viên");
            return response;
        }
        EmployeeEntity entity = employee.get();
        entity.setFullname(employeeDTO.getFullname());
        entity.setEmail(employeeDTO.getEmail());
        entity.setPhone(employeeDTO.getPhone());
        entity.setAddress(employeeDTO.getAddress());
        Optional<PositionEntity> position = positionRepository.findById(employeeDTO.getPositionId());
        if (position.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(" Không tồn tại chức vụ");
            return response;
        }
        Optional<DepartmentEntity> departmentEntity = departmentRepository.findById(employeeDTO.getPositionId());
        if (position.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(" Không tồn tại chức vụ");
            return response;
        }
        entity.setPosition(position.get());
        entity.setDepartment(departmentEntity.get());

        employeeRepository.save(entity);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Đã cập nhập thành công");
        response.setData(employeeDTO);
        return response;
    }
}
