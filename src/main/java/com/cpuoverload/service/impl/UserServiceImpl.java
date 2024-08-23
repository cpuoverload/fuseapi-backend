package com.cpuoverload.service.impl;


import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpuoverload.mapper.UserMapper;
import com.cpuoverload.model.entity.User;
import com.cpuoverload.service.UserService;
import com.cpuoverload.utils.BusinessException;
import com.cpuoverload.utils.Error;
import org.springframework.stereotype.Service;

/**
 * @author passion
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-08-20 21:03:39
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final String randomString = "sjfdkoqef";

    @Override
    public Long register(String username, String password) {
        // 1. 校验参数
        if (username == null || password == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        if (username.length() < 6) {
            throw new BusinessException(Error.PARAMS_ERROR, "用户名长度少于 6 位");
        }
        if (password.length() < 8) {
            throw new BusinessException(Error.PARAMS_ERROR, "密码长度少于 8 位");
        }
        synchronized (username.intern()) {
            // 2. 判断 username 是否重复
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, username);
            long count = this.count(queryWrapper);
            // 2.1 若重复，注册失败
            if (count > 0) {
                throw new BusinessException(Error.DUPLICATED_USERNAME_ERROR);
            }
            // 2.2 若不重复，插入记录
            User user = new User();
            user.setUsername(username);
            // 密码加密
            user.setPassword(DigestUtil.bcrypt(password));
            // 分配 AK/SK
            user.setAccessKey(UUID.randomUUID().toString(true));
            user.setSecretKey(DigestUtil.sha256Hex(randomString + System.currentTimeMillis()));
            boolean success = this.save(user);
            // 3. 返回 userId
            if (!success) {
                throw new BusinessException(Error.SYSTEM_ERROR, "注册失败");
            }
            return user.getId();
        }
    }
}


