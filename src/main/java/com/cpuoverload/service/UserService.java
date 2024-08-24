package com.cpuoverload.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpuoverload.model.dto.user.ListRequest;
import com.cpuoverload.model.entity.User;
import com.cpuoverload.model.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author passion
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-08-20 21:03:39
 */
public interface UserService extends IService<User> {

    Long getLoginUserId(HttpServletRequest request);

    Long register(String username, String password);

    UserVo login(String username, String password, HttpServletRequest request);

    boolean logout(HttpServletRequest request);

    UserVo getMyInfo(HttpServletRequest request);

    boolean updateMyInfo(User user);

    UserVo getUserById(Long id);

    List<UserVo> listUser(ListRequest listRequest);

    Long addUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(Long id);
}
