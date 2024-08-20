package com.cpuoverload.utils;

import lombok.Getter;

@Getter
public enum Error {

    PARAMS_ERROR(10001, "请求参数错误"),
    UNLOGIN_ERROR(10002, "未登录"),
    FORBBIDEN_ERROR(10003, "无权限"),
    SYSTEM_ERROR(10004, "系统异常");

    private final int code;
    private final String message;

    Error(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
