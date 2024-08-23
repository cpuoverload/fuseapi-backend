package com.cpuoverload.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 5200123986732432651L;
    private String username;
    private String password;
}
