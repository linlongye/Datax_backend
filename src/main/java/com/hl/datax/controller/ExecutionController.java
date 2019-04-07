package com.hl.datax.controller;

import com.google.common.base.Strings;
import com.hl.datax.domain.ResponseMessage;
import com.hl.datax.domain.TempTask;
import com.hl.datax.service.SystemSettingService;
import com.hl.datax.utils.LoggerTool;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("exec")
public class ExecutionController {
  private final static String CONTROLLER_NAME = "ExecutionController";
  private final static String EXEC_ONCE = "execOnce";
  private final static String GET_TEMPLATE_JSON = "getTemplateJson";

  private static String binDir = "";
  private final SystemSettingService systemSettingService;

  public ExecutionController(SystemSettingService systemSettingService) {
    this.systemSettingService = systemSettingService;
    binDir = systemSettingService.find(1).getBinDir();
  }

  @PostMapping("/once")
  private ResponseMessage execOnce(@RequestBody TempTask tempTask) {
    LoggerTool.info(CONTROLLER_NAME, EXEC_ONCE, tempTask);
    ResponseMessage res = new ResponseMessage(false);
    try {
      if (tempTask != null && !Strings.isNullOrEmpty(tempTask.getFileName())) {
        String cmd = String.format("python %s\\datax.py %s", binDir, tempTask.getFileName());
        String resultStr = null;
        resultStr = execOnceCmd(cmd);
        res.setSuccess(true);
        res.setMsg(resultStr);
      } else {
        res.setMsg("当前任务不存在配置文件");
      }
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, EXEC_ONCE, e);
      res.setMsg(e);
    }

    return res;
  }

  @GetMapping("/getTemplateJson/{input}/{output}")
  public ResponseMessage getTemplateJson(@PathVariable("input") String inputType, @PathVariable("output") String outputType) {
    LoggerTool.info(CONTROLLER_NAME, GET_TEMPLATE_JSON, null);
    ResponseMessage res = new ResponseMessage(false);
    try {
      if (Strings.isNullOrEmpty(inputType) || Strings.isNullOrEmpty(outputType)) {
        res.setMsg("输入数据库类型和输出数据库类型不能为空");
      } else {
        String shell = String.format("python %s\\datax.py -r %s -w %s", binDir, inputType, outputType);
        String result = execCmd(shell);
        if (Strings.isNullOrEmpty(result)) {
          res.setMsg("执行失败");
        } else {
          res.setSuccess(true);
          res.setMsg(result);
        }
      }
    } catch (Exception e) {
      LoggerTool.error(CONTROLLER_NAME, GET_TEMPLATE_JSON, e);
      res.setMsg(e);
    }
    return res;
  }

  private String execCmd(String cmd) throws InterruptedException, IOException {
    Process exec = Runtime.getRuntime().exec(cmd);
    // 方法阻塞, 等待命令执行完成（成功会返回0）

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
    String line;
    StringBuilder sb = new StringBuilder();
    boolean startRead = false;
    while ((line = bufferedReader.readLine()) != null) {
      if (line.trim().startsWith("{")) {
        startRead = true;
      }
      if (startRead) {
        sb.append(line).append("\n");
      }

    }
    exec.waitFor();
    return sb.toString();
  }


  private  String execOnceCmd(String cmd) {
    Runtime runtime = Runtime.getRuntime();
    StringBuilder sb = new StringBuilder();
    Process p = null;
    try {
      p = runtime.exec(cmd);
      final InputStream is1 = p.getInputStream();
      final InputStream is2 = p.getErrorStream();
      new Thread(() -> {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
        try {
          String line1;
          while ((line1 = br1.readLine()) != null) {
            sb.append(line1).append("\n");
          }
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          try {
            is1.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }).start();
      new Thread(() -> {
        BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
        try {
          String line2;
          while ((line2 = br2.readLine()) != null) {
            sb.append(line2).append("\\n");
          }
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          try {
            is2.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }).start();
      p.waitFor();
      p.destroy();
    } catch (Exception e) {
      try {
        p.getInputStream().close();
        p.getErrorStream().close();
        p.getOutputStream().close();
      } catch (IOException ex) {
        ex.printStackTrace();

      }

    }
    return sb.toString();
  }

}
