package com.hl.datax.controller;

import com.hl.datax.domain.ResponseMessage;
import com.hl.datax.domain.TempTask;
import com.hl.datax.service.TempTaskService;
import com.hl.datax.utils.LoggerTool;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/temp")
public class TempTaskController {

  private final static String CONTROLLER_NAME = "TempTaskController";
  private final static String GET_ALL = "getAll";
  private final static String FIND_BY_USER_ID = "findByUserId";
  private final static String ADD = "add";
  private final static String UPDATE = "update";
  private final static String DELETE = "delete";

  private final TempTaskService tempTaskService;

  public TempTaskController(TempTaskService tempTaskService) {
    this.tempTaskService = tempTaskService;
  }


  @GetMapping("/all")
  public ResponseMessage getAll() {
    LoggerTool.info(CONTROLLER_NAME, GET_ALL, null);
    ResponseMessage res = new ResponseMessage(false);
    try {
      List<TempTask> all = tempTaskService.findAll();
      res.setSuccess(true);
      res.setMsg(all);
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, GET_ALL, e);
      res.setMsg(e);
    }
    return res;
  }

  @GetMapping("/find/{userid}")
  public ResponseMessage findByUserId(@PathVariable("userid") Integer userId) {
    LoggerTool.info(CONTROLLER_NAME, FIND_BY_USER_ID, null);
    ResponseMessage res = new ResponseMessage(false);
    try {
      List<TempTask> byUserId = tempTaskService.findByUserId(userId);
      res.setSuccess(true);
      res.setMsg(byUserId);
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, FIND_BY_USER_ID, e);
      res.setMsg(e);
    }
    return res;
  }

  @PostMapping("/add")
  public ResponseMessage add(@RequestBody TempTask tempTask) {
    LoggerTool.info(CONTROLLER_NAME, UPDATE, tempTask);
    ResponseMessage res = new ResponseMessage(false);
    try {
      TempTask t = tempTaskService.save(tempTask);
      res.setSuccess(true);
      res.setMsg(t);
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, UPDATE, e);
      res.setMsg(e);
    }
    return res;
  }

  @PostMapping("/update")
  public ResponseMessage update(@RequestBody TempTask tempTask) {
    LoggerTool.info(CONTROLLER_NAME, ADD, tempTask);
    ResponseMessage res = new ResponseMessage(false);
    try {
      if (tempTask.getUserId() == null) {
        res.setMsg("用户id不能为空");
      } else {
        TempTask t = tempTaskService.save(tempTask);
        res.setSuccess(true);
        res.setMsg(t);
      }
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, ADD, e);
      res.setMsg(e);
    }
    return res;
  }


  @GetMapping("/delete/{id}")
  public ResponseMessage delete(@PathVariable("id") Integer id) {
    LoggerTool.info(CONTROLLER_NAME, DELETE, id);
    ResponseMessage res = new ResponseMessage(false);
    try {
      tempTaskService.delete(id);
      res.setSuccess(true);
      res.setMsg("删除成功");
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, DELETE, e);
      res.setMsg(e);
    }
    return res;
  }
}
