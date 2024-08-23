package com.cpuoverload.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddRequest implements Serializable {

    private static final long serialVersionUID = -3890744654110351049L;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 角色
     */
    private String role;
}
