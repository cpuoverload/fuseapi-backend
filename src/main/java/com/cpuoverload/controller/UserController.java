package com.cpuoverload.controller;

import com.cpuoverload.utils.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/name")
    public ApiResponse<String> getName() {
        return ApiResponse.success("John Doe");
    }
}
