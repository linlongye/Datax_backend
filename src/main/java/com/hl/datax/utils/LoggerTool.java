package com.hl.datax.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class LoggerTool {
  private static final Logger logger = LoggerFactory.getLogger(LoggerTool.class);

  /**
   * 打印info日志信息
   *
   * @param controllerName
   * @param methodName
   * @param o
   */
  public static void info(String controllerName, String methodName, Object o) {
    if (o == null) {
      logger.info(String.format("控制器:%s,方法%s", controllerName, methodName));
    } else {
      logger.info(String.format("控制器:%s,方法%s,参数%s", controllerName, methodName, o));
    }
  }

  public static void error(String controllerName, String methodName, Object o) {
    logger.error(String.format("控制器:%s,方法%s,错误堆栈%s", controllerName, methodName, o));
  }
}
