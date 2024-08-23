package com.cpuoverload.controller;

import com.cpuoverload.model.dto.user.UserRegisterRequest;
import com.cpuoverload.service.UserService;
import com.cpuoverload.utils.ApiResponse;
import com.cpuoverload.utils.BusinessException;
import com.cpuoverload.utils.Error;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        if (username == null || password == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        Long userId = userService.register(username, password);
        return ApiResponse.success(userId);
    }
}
