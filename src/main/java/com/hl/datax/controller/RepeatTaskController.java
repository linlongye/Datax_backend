package com.hl.datax.controller;

import com.hl.datax.domain.RepeatTask;
import com.hl.datax.domain.ResponseMessage;
import com.hl.datax.service.RepeatTaskService;
import com.hl.datax.utils.LoggerTool;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("repeat")
public class RepeatTaskController {
  private final static String CONTROLLER_NAME = "RepeatTaskController";
  private final static String FIND_ALL = "findAll";
  private final static String SAVE = "save";
  private final RepeatTaskService repeatTaskService;

  public RepeatTaskController(RepeatTaskService repeatTaskService) {
    this.repeatTaskService = repeatTaskService;
  }

  @GetMapping("/all/{userid}")
  public ResponseMessage findAll(@PathVariable("userid") Integer userid) {
    LoggerTool.info(CONTROLLER_NAME, FIND_ALL, null);
    ResponseMessage res = new ResponseMessage(false);
    try {
      res.setSuccess(true);
      res.setMsg(repeatTaskService.findAllByUserId(userid));
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, FIND_ALL, e);
      res.setMsg(e);
    }
    return res;
  }

  @PostMapping("/save")
  public ResponseMessage save(@RequestBody RepeatTask task) {
    LoggerTool.info(CONTROLLER_NAME, SAVE, task);
    ResponseMessage res = new ResponseMessage(false);
    try {
      res.setSuccess(true);
      res.setMsg(repeatTaskService.save(task));
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, SAVE, e);
      res.setMsg(e);
    }
    return res;
  }


  @GetMapping("/delete/{id}")
  public ResponseMessage save(@PathVariable("id") Integer id) {
    LoggerTool.info(CONTROLLER_NAME, SAVE, id);
    ResponseMessage res = new ResponseMessage(false);
    try {
      repeatTaskService.delete(id);
      res.setSuccess(true);
      res.setMsg("删除成功");
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, SAVE, e);
      res.setMsg(e);
    }
    return res;
  }
}
