package com.the.quanlynhansu.service.impl;

import com.the.quanlynhansu.entity.RoleEntity;
import com.the.quanlynhansu.entity.UserEntity;
import com.the.quanlynhansu.model.dto.RoleDTO;
import com.the.quanlynhansu.model.dto.UserDTO;
import com.the.quanlynhansu.model.request.UserRequest;
import com.the.quanlynhansu.model.response.BaseResponse;
import com.the.quanlynhansu.repository.RoleRepository;
import com.the.quanlynhansu.repository.UserRepository;
import com.the.quanlynhansu.service.IUserservice;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserservice {
 private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

     @Autowired
     private UserRepository userRepository;
     @Autowired
      private RoleRepository roleRepository;
     @Autowired
     private ModelMapper modelMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
        List<RoleDTO> roleDTOS= roleRepository.getRoleByUsername(username).stream().map(roleEntity -> modelMapper.map(roleEntity, RoleDTO.class)).toList();
        userDTO.setRoleDTOS(roleDTOS);
        return userDTO;
    }

    @Override
    public BaseResponse<Page<UserDTO>> getAllUser(UserRequest userRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<UserEntity> userEntities = userRepository.findFirstByUserId(userRequest,pageable);
        List<UserDTO> userDTOList = userEntities.getContent().stream().map(userEntity -> {
            UserDTO userDTO = modelMapper.map(userEntity,UserDTO.class);
            userDTO.setId(userEntity.getId());
            userDTO.setUsername(userEntity.getUsername());
            userDTO.setPassword(userEntity.getPassword());
            userDTO.setEmail(userEntity.getEmail());
            return userDTO;
        }).collect(Collectors.toList());
        Page<UserDTO> userDTOPage =  new PageImpl<>(userDTOList, pageable, userEntities.getTotalElements());
        BaseResponse<Page<UserDTO>> baseResponse = new BaseResponse<>();
        baseResponse.setCode(HttpStatus.OK.value());
        baseResponse.setMessage("Thành công");
        baseResponse.setData(userDTOPage);
        return baseResponse;
    }

    @Override
    public BaseResponse<?> addUser(UserEntity userEntity) {
        BaseResponse<?> baseResponse = new BaseResponse<>();

        // kiểm tra tài khoản đã tồn tại chưa
        if (userRepository.findByUsername(userEntity.getUsername()) != null){
            baseResponse.setCode(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage("Tài khoản đã tồn tại");
            return baseResponse;
        }
        //Mã hóa mật khẩu
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        //Gán mặc định là quyền nhân viên
        RoleEntity role = new RoleEntity();
        role.setName("ROLE_USER");
        userEntity.setRoles(Collections.singleton(role));


        baseResponse.setCode(HttpStatus.OK.value());
        baseResponse.setMessage("Đăng kí thành công");
        userRepository.save(userEntity);
        return baseResponse;
    }

    @Override
    public BaseResponse updateUser(long id, UserEntity userEntity) {
       BaseResponse<?> baseResponse = new BaseResponse<>();
        Optional<UserEntity> user = userRepository.findById(id);
        user.get().setUsername(userEntity.getUsername());
        user.get().setPassword(userEntity.getPassword());
        user.get().setEmail(userEntity.getEmail());
        return baseResponse;
    }

    @Override
    public BaseResponse<?> deleteUser(Long id) {
        BaseResponse<?> baseResponse = new BaseResponse<>();
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()){
            baseResponse.setCode(HttpStatus.NOT_FOUND.value());
            baseResponse.setMessage("tài khoản không tồn tại");
            return baseResponse;
        }
        UserEntity userEntity = user.get();
        userEntity.setDeleted(true);
        userRepository.save(userEntity);

        baseResponse.setCode(HttpStatus.OK.value());
        baseResponse.setMessage("Xóa thành công");
        return baseResponse;
    }
}
