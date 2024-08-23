package com.cpuoverload.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpuoverload.model.entity.User;

/**
 * @author passion
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-08-20 21:03:39
 */
public interface UserService extends IService<User> {

    Long register(String username, String password);
}
