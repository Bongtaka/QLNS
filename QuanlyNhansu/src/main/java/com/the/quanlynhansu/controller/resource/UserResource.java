package com.the.quanlynhansu.controller.resource;

import com.the.quanlynhansu.entity.UserEntity;
import com.the.quanlynhansu.model.dto.UserDTO;
import com.the.quanlynhansu.model.request.UserRequest;
import com.the.quanlynhansu.model.response.BaseResponse;
import com.the.quanlynhansu.service.IUserservice;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/account")
public class UserResource {
    @Autowired
    private IUserservice iUserService;
    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserEntity userEntity) {

        return ResponseEntity.status(HttpStatus.CREATED).body(iUserService.addUser(userEntity));
    }

    @PostMapping("/ListUser")
    public ResponseEntity<BaseResponse<Page<UserDTO>>> ListUser(@RequestBody UserRequest userRequest,
                                                                @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                @RequestParam(name = "size", required = false, defaultValue = "10") int size
                                                                ){
        return ResponseEntity.ok(iUserService.getAllUser(userRequest,page,size));

    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UserEntity userEntity) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(iUserService.updateUser(id, userEntity));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse<?>> deleteUser(@PathVariable("userId") Long userId) {
        BaseResponse<?> response = iUserService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}

