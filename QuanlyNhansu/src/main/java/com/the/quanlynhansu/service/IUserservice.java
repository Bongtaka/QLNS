package com.the.quanlynhansu.service;

import com.the.quanlynhansu.entity.UserEntity;
import com.the.quanlynhansu.model.dto.UserDTO;
import com.the.quanlynhansu.model.request.UserRequest;
import com.the.quanlynhansu.model.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface IUserservice {
    UserDTO findUserByUsername(String username);
    BaseResponse<Page<UserDTO>>getAllUser(UserRequest userRequest, int page, int size);
    BaseResponse<?>addUser(UserEntity userEntity);

    BaseResponse updateUser(long id, UserEntity userEntity);
    BaseResponse<?> deleteUser(Long id);
}
