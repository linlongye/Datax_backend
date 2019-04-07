package com.hl.datax.controller;

import com.hl.datax.domain.ResponseMessage;
import com.hl.datax.domain.SystemSetting;
import com.hl.datax.service.SystemSettingService;
import com.hl.datax.utils.LoggerTool;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/setting")
public class SystemSettingController {
  private final static String CONTROLLER_NAME = "SystemSettingController";

  private final SystemSettingService systemSettingService;


  public SystemSettingController(SystemSettingService systemSettingService) {
    this.systemSettingService = systemSettingService;
  }


  @GetMapping("/find/{id}")
  public ResponseMessage find(@PathVariable("id") Integer id) {
    LoggerTool.info(CONTROLLER_NAME, "find", id);
    ResponseMessage res = new ResponseMessage(false);
    try {
      SystemSetting systemSetting = systemSettingService.find(id);
      res.setSuccess(true);
      res.setMsg(systemSetting);
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, "find", e);
      res.setMsg(e);
    }
    return res;
  }

  @PostMapping("/save")
  public ResponseMessage save(@RequestBody SystemSetting systemSetting) {
    LoggerTool.info(CONTROLLER_NAME, "save", systemSetting);
    ResponseMessage res = new ResponseMessage(false);
    try {
      SystemSetting save = systemSettingService.save(systemSetting);
      res.setSuccess(true);
      res.setMsg(save);
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, "save", e);
      res.setMsg(e);
    }
    return res;
  }
}
