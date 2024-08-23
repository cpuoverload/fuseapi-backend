package com.cpuoverload.service.impl;


import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpuoverload.constant.Constant;
import com.cpuoverload.mapper.UserMapper;
import com.cpuoverload.model.dto.user.ListRequest;
import com.cpuoverload.model.entity.User;
import com.cpuoverload.model.vo.UserVo;
import com.cpuoverload.service.UserService;
import com.cpuoverload.utils.BusinessException;
import com.cpuoverload.utils.Error;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author passion
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-08-20 21:03:39
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public Long register(String username, String password) {
        // 1. 校验参数
        validate(username, password);
        // 2. 判断 username 是否重复
        synchronized (username.intern()) {
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
            user.setSecretKey(DigestUtil.sha256Hex(Constant.RANDOM_STRING + System.currentTimeMillis()));
            boolean success = this.save(user);
            // 3. 返回 userId
            if (!success) {
                throw new BusinessException(Error.SYSTEM_ERROR, "注册失败");
            }
            return user.getId();
        }
    }

    @Override
    public UserVo login(String username, String password, HttpServletRequest request) {
        // 1. 校验参数
        validate(username, password);
        // 2. 判断用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(Error.USER_NOT_FOUND);
        }
        // 3. 判断密码是否正确
        boolean isPwdCorrect = DigestUtil.bcryptCheck(password, user.getPassword());
        if (!isPwdCorrect) {
            throw new BusinessException(Error.PASSWORD_ERROR);
        }
        // 4. 保存用户登录状态到 session
        UserVo userVo = entityToVo(user);
        request.getSession().setAttribute(Constant.LOGIN_USER, userVo);
        // 5. 返回用户信息
        return userVo;
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        request.getSession().removeAttribute(Constant.LOGIN_USER);
        return true;
    }

    @Override
    public UserVo getMyInfo(HttpServletRequest request) {
        User user = this.getById(getLoginUserId(request));
        return entityToVo(user);
    }

    @Override
    public boolean updateMyInfo(User user) {
        String nickname = user.getNickname();
        String password = user.getPassword();
        if (nickname == null && password == null) {
            throw new BusinessException(Error.PARAMS_ERROR, "没有要更新的内容");
        }
        if (nickname != null && nickname.length() < 2) {
            throw new BusinessException(Error.PARAMS_ERROR, "昵称长度少于 2 位");
        }
        if (password != null && password.length() < 8) {
            throw new BusinessException(Error.PARAMS_ERROR, "密码长度少于 8 位");
        } else {
            user.setPassword(DigestUtil.bcrypt(password));
        }
        return this.updateById(user);
    }

    @Override
    public boolean updateUser(User user) {
        String nickname = user.getNickname();
        String password = user.getPassword();
        String role = user.getRole();
        if (nickname == null && password == null && role == null) {
            throw new BusinessException(Error.PARAMS_ERROR, "没有要更新的内容");
        }
        if (nickname != null && nickname.length() < 2) {
            throw new BusinessException(Error.PARAMS_ERROR, "昵称长度少于 2 位");
        }
        if (password != null && password.length() < 8) {
            throw new BusinessException(Error.PARAMS_ERROR, "密码长度少于 8 位");
        } else {
            user.setPassword(DigestUtil.bcrypt(password));
        }
        if (role != null && !role.equals("user") && !role.equals("admin")) {
            throw new BusinessException(Error.PARAMS_ERROR, "角色不合法");
        }
        return this.updateById(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        return this.removeById(id);
    }

    /**
     * 校验参数
     * @param username
     * @param password
     */
    public void validate(String username, String password) {
        if (username == null || password == null) {
            throw new BusinessException(Error.PARAMS_ERROR);
        }
        if (username.length() < 6) {
            throw new BusinessException(Error.PARAMS_ERROR, "用户名长度少于 6 位");
        }
        if (password.length() < 8) {
            throw new BusinessException(Error.PARAMS_ERROR, "密码长度少于 8 位");
        }
    }

    /**
     * entity 转换为 vo
     * @param user
     * @return
     */
    public UserVo entityToVo(User user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    public Long getLoginUserId(HttpServletRequest request) {
        UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.LOGIN_USER);
        return userVo.getId();
    }

    @Override
    public UserVo getUserById(Long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(Error.USER_NOT_FOUND);
        }
        return entityToVo(user);
    }

    @Override
    public List<UserVo> listUser(ListRequest listRequest) {
        IPage<User> page = new Page<>(listRequest.getCurrent(), listRequest.getPageSize());

        // 由于不是所有字段都是精确查询，有的字段需要模糊查询，有的字段需要排序，所以不能简单地写成 new QueryWrapper(entity)
        // sortField 是动态传入的列名，无法使用 LambdaQueryWrapper
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(listRequest.getId() != null, "id", listRequest.getId())
                .like(StringUtils.isNotBlank(listRequest.getUsername()), "username", listRequest.getUsername())
                .like(StringUtils.isNotBlank(listRequest.getNickname()), "nickname", listRequest.getNickname())
                .eq(listRequest.getRole() != null, "role", listRequest.getRole())
                .orderBy(listRequest.getSortField() != null, listRequest.getIsAscend(), listRequest.getSortField());

        IPage<User> userPage = this.page(page, queryWrapper);
        List<UserVo> userVoList = userPage.getRecords().stream().map(this::entityToVo).collect(Collectors.toList());
        return userVoList;
    }

    @Override
    public Long addUser(User user) {
        boolean success = this.save(user);
        if (!success) {
            throw new BusinessException(Error.SYSTEM_ERROR, "添加用户失败");
        }
        return user.getId();
    }
}


