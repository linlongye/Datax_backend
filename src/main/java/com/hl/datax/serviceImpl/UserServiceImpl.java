package com.hl.datax.serviceImpl;

import com.hl.datax.domain.User;
import com.hl.datax.repo.UserRepository;
import com.hl.datax.service.UserService;
import com.hl.datax.utils.PasswordTool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordTool passwordTool;

  public UserServiceImpl(UserRepository userRepository, PasswordTool passwordTool) {
    this.userRepository = userRepository;
    this.passwordTool = passwordTool;
  }

  @Override
  public boolean login(User user) {
    boolean res = false;
    User findUser = userRepository.findUserByUserNameAndPassword(user.getUserName(), PasswordTool.encrypt(user.getPassword(), passwordTool.getKeyt()));
    if (findUser != null && Objects.equals(PasswordTool.encrypt(user.getPassword(), passwordTool.getKeyt()), findUser.getPassword())) {
      res = true;
    }
    return res;
  }

  @Override
  public User add(User user) {
    //加密用户密码
    user.setPassword(PasswordTool.encrypt(user.getPassword(), passwordTool.getKeyt()));
    userRepository.save(user);
    return user;
  }

  @Override
  public boolean isUserExitByName(String userName) {
    return userRepository.findUserByUserName(userName) != null;
  }

  @Override
  public void update(User user) {
    ifUserIdNull(user);
    user.setPassword(PasswordTool.encrypt(user.getPassword(), passwordTool.getKeyt()));
    userRepository.save(user);
  }

  @Override
  public boolean delete(Integer id) {
    if (id == null) {
      return false;
    }
    userRepository.deleteById(id);
    return true;
  }

  @Override
  public boolean deleteByName(String name) {
    if (name == null) {
      return false;
    }
    userRepository.deleteByUserName(name);
    return true;
  }

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User findByName(String userName) {
    return userRepository.findUserByUserName(userName);
  }

  /**
   * 如果传入的User对象的id值为空时，从数据库查找对应的id
   * 仅对update使用
   *
   * @param user
   */
  private void ifUserIdNull(User user) {
    if (user.getId() == null) {
      User findUser = userRepository.findUserByUserName(user.getUserName());
      if (findUser != null) {
        user.setId(findUser.getId());
      }
    }
  }
}
