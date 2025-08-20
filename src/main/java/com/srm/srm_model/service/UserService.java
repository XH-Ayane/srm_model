package com.srm.srm_model.service;

import com.srm.srm_model.entity.User;

import java.util.List;

public interface UserService {

    // 根据ID查询用户
    User getUserById(Long id);

    // 根据用户名来查询用户
    User getUserByUsername(String username);

    // 查询所有用户
    List<User> getAllUsers();

    // 保存用户
    User saveUser(User user);

    // 更新用户
    User updateUser(User user);

    // 删除用户
    boolean deleteUser(Long id);

    // 用户登录
    User login(String username, String password);
}
