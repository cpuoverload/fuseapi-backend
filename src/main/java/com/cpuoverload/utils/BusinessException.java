package com.cpuoverload.utils;

public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Error error) {
        super(error.getMessage());
        this.code = error.getCode();
    }

    public BusinessException(Error error, String message) {
        super(message);
        this.code = error.getCode();
    }

    public int getCode() {
        return code;
    }
}
