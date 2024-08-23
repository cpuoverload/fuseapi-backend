package com.cpuoverload.controller;

import com.cpuoverload.config.RequiresLogin;
import com.cpuoverload.model.dto.user.*;
import com.cpuoverload.model.entity.User;
import com.cpuoverload.model.vo.UserVo;
import com.cpuoverload.service.UserService;
import com.cpuoverload.utils.ApiResponse;
import com.cpuoverload.utils.BusinessException;
import com.cpuoverload.utils.Error;
import com.cpuoverload.utils.IdRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<Long> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        if (username == null || password == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        Long userId = userService.register(username, password);
        return ApiResponse.success(userId);
    }

    @PostMapping("/login")
    public ApiResponse<UserVo> login(@RequestBody RegisterRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if (username == null || password == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        UserVo userVo = userService.login(username, password, request);
        return ApiResponse.success(userVo);
    }

    @PostMapping("/logout")
    public ApiResponse<Boolean> logout(HttpServletRequest request) {
        userService.logout(request);
        return ApiResponse.success(true);
    }

    @GetMapping("/getMyInfo")
    @RequiresLogin
    public ApiResponse<UserVo> getMyInfo(HttpServletRequest request) {
        UserVo userVo = userService.getMyInfo(request);
        return ApiResponse.success(userVo);
    }

    @PostMapping("/updateMyInfo")
    @RequiresLogin
    public ApiResponse<Boolean> updateMyInfo(@RequestBody UpdateMyInfoRequest updateMyInfoRequest,
                                             HttpServletRequest request) {
        if (updateMyInfoRequest == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(updateMyInfoRequest, user);
        user.setId(userService.getLoginUserId(request));
        boolean success = userService.updateMyInfo(user);
        if (!success) {
            throw new BusinessException(Error.UPDATE_ERROR);
        }
        return ApiResponse.success(true);
    }

    @GetMapping("/get/{id}")
    // 管理员
    public ApiResponse<UserVo> getUserById(@PathVariable Long id) {
        if (id == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        UserVo userVo = userService.getUserById(id);
        return ApiResponse.success(userVo);
    }

    @PostMapping("/list")
    // 管理员
    public ApiResponse<List<UserVo>> listUser(@RequestBody ListRequest listRequest) {
        if (listRequest == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        List<UserVo> userVoList = userService.listUser(listRequest);
        return ApiResponse.success(userVoList);
    }

    @PostMapping("/add")
    // 管理员
    public ApiResponse<Long> addUser(@RequestBody AddRequest addRequest) {
        if (addRequest == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(addRequest, user);
        Long userId = userService.addUser(user);
        return ApiResponse.success(userId);
    }

    @PostMapping("/update")
    // 管理员
    public ApiResponse<Boolean> updateUser(@RequestBody UpdateRequest updateRequest) {
        if (updateRequest == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        Long userId = updateRequest.getId();
        if (userId == null) {
            throw new BusinessException(Error.PARAMS_ERROR, "userId 为 null");
        }
        User user = new User();
        BeanUtils.copyProperties(updateRequest, user);
        boolean success = userService.updateUser(user);
        if (!success) {
            throw new BusinessException(Error.UPDATE_ERROR);
        }
        return ApiResponse.success(true);
    }

    @PostMapping("/delete")
    // 管理员
    public ApiResponse<Boolean> deleteUser(@RequestBody IdRequest idRequest) {
        if (idRequest == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        boolean success = userService.deleteUser(idRequest.getId());
        if (!success) {
            throw new BusinessException(Error.DELETE_ERROR);
        }
        return ApiResponse.success(true);
    }
}
