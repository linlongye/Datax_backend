package com.hl.datax.controller;

import com.hl.datax.domain.ResponseMessage;
import com.hl.datax.domain.User;
import com.hl.datax.service.UserService;
import com.hl.datax.utils.LoggerTool;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
  private final static String CONTROLLER_NAME = "UserController";
  private final static String LOGIN = "login";
  private final static String ADD = "addUser";
  private final static String DELETE = "deleteUser";
  private final static String UPDATE = "updateUser";
  private final static String FIND_ALL = "findAll";
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("login")
  public ResponseMessage login(@RequestBody User user) {
    LoggerTool.info(CONTROLLER_NAME, LOGIN, user);
    ResponseMessage res = new ResponseMessage(false);
    try {
      if (userService.login(user)) {
        res.setSuccess(true);
        res.setMsg(userService.findByName(user.getUserName()));
      } else {
        res.setMsg("登录失败，用户名或密码错误");
      }
    } catch (Exception e) {
      res.setMsg(e);
      LoggerTool.error(CONTROLLER_NAME, LOGIN, e);
    }
    return res;
  }


  @PostMapping("add")
  public ResponseMessage addUser(@RequestBody User user) {
    LoggerTool.info(CONTROLLER_NAME, ADD, user);
    ResponseMessage res = new ResponseMessage(false);
    try {
      if (userService.isUserExitByName(user.getUserName())) {
        res.setMsg("该用户名已被注册");
      } else {
        User newUser = userService.add(user);
        res.setSuccess(true);
        res.setMsg(newUser);
      }
    } catch (Exception e) {
      res.setMsg(e);
      LoggerTool.error(CONTROLLER_NAME, ADD, e);
    }
    return res;
  }


  @GetMapping("/delete/{name}")
  public ResponseMessage deleteUser(@PathVariable("name") String name) {
    LoggerTool.info(CONTROLLER_NAME, DELETE, name);
    ResponseMessage res = new ResponseMessage(false);
    try {
      userService.deleteByName(name);
      res.setSuccess(true);
      res.setMsg("删除成功");
    } catch (Exception e) {
      res.setMsg("删除失败," + e.toString());
      LoggerTool.error(CONTROLLER_NAME, DELETE, e);
    }
    return res;
  }

  @PostMapping("/update")
  public ResponseMessage updateUser(@RequestBody User user) {
    LoggerTool.info(CONTROLLER_NAME, UPDATE, user);
    ResponseMessage res = new ResponseMessage(false);
    try {
      userService.update(user);
      res.setSuccess(true);
      res.setMsg("更新成功");
    } catch (Exception e) {
      res.setMsg("更新失败，" + e.toString());
      LoggerTool.error(CONTROLLER_NAME, UPDATE, e);
    }
    return res;
  }

  @GetMapping("/all")
  public ResponseMessage findAll() {
    LoggerTool.info(CONTROLLER_NAME, FIND_ALL, null);
    ResponseMessage res = new ResponseMessage(false);
    try {
      res.setSuccess(true);
      res.setMsg(userService.findAll());
    } catch (Exception e) {
      res.setMsg("数据获取失败，" + e.toString());
      LoggerTool.error(CONTROLLER_NAME, UPDATE, e);
    }
    return res;
  }
}
