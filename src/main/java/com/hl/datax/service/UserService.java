package com.hl.datax.service;

import com.hl.datax.domain.User;

import java.util.List;

public interface UserService {

  /**
   * 登录
   */
  boolean login(User user);

  /**
   * 添加用户
   *
   * @param user {@link User}
   * @return {@link User}
   */
  User add(User user);

  /**
   * 根据用户名查找才用户名是否已被注册
   *
   * @param userName 用户名
   * @return 是否注册
   */
  boolean isUserExitByName(String userName);

  /**
   * 更新用户
   *
   * @param user
   */
  void update(User user);

  /**
   * 删除用户
   *
   * @param id
   * @return
   */
  boolean delete(Integer id);

  boolean deleteByName(String name);

  List<User> findAll();

  User findByName(String userName);
}
