package com.hl.datax.controller;

import com.hl.datax.config.MyEndPointConfigure;
import com.hl.datax.domain.RepeatTask;
import com.hl.datax.domain.SystemSetting;
import com.hl.datax.repo.RepeatTaskRepository;
import com.hl.datax.service.RepeatTaskService;
import com.hl.datax.service.SystemSettingService;
import com.hl.datax.serviceImpl.RepeatTaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.PreDestroy;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledFuture;

@ServerEndpoint(value = "/websocket/{taskId}", configurator = MyEndPointConfigure.class)
@Component
public class RepeatSocketServer {
  //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
  private static int onlineCount = 0;

  private final RepeatTaskService repeatTaskService;

  private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
  private final SystemSettingService systemSettingService;


  //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
  private static CopyOnWriteArraySet<RepeatSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

  //与某个客户端的连接会话，需要通过它来给客户端发送数据
  private Session session;
  private String sid = "";
  private SystemSetting systemSetting;

  //任务列表
  private Map<Integer, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();


  public RepeatSocketServer(RepeatTaskService repeatTaskService, ThreadPoolTaskScheduler threadPoolTaskScheduler, SystemSettingService systemSettingService) {
    this.repeatTaskService = repeatTaskService;
    this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    this.systemSettingService = systemSettingService;

    this.systemSetting = systemSettingService.find(1);
  }

  /**
   * 在bean销毁之前关闭所有任务，并修改数据库
   */
  @PreDestroy
  public void preDestory() {
    System.out.println("销毁所有任务");
    //定制所有任务
    scheduledFutureMap.values().forEach(p -> p.cancel(true));
    //修改运行状态
    scheduledFutureMap.keySet().forEach(i -> {
      RepeatTask t = this.repeatTaskService.findRepeatTaskById(i);
      t.setIsRun(0);
      repeatTaskService.save(t);
    });
    scheduledFutureMap.clear();
  }

  /**
   * 连接建立成功调用的方法
   */
  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    this.sid = session.getRequestURI().getPath().replace("/websocket/", "");
    if (!scheduledFutureMap.containsKey(Integer.parseInt(this.sid))) {
      webSocketSet.add(this);     //加入set中
    }
  }

  /**
   * 连接关闭调用的方法
   * 连接关闭时关闭任务，从任务列表中删除任务并修改数据库
   */
  @OnClose
  public void onClose() {
    //webSocketSet.remove(this);  //从set中删除
  }

  /**
   * 收到客户端消息后调用的方法
   *
   * @param message 客户端发送过来的消息
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    System.out.println("来自客户端的消息:" + message);
    String[] protocol = message.split("\\|");//拆分协议，第一部分为当前连接的id，第二部分是task的id
    RepeatTask task = repeatTaskService.findRepeatTaskById(Integer.parseInt(protocol[1]));

    //停止任务
    if ("1".equals(protocol[2])) {
      task.setIsRun(0);
      //获取对应的定时任务，停止在从任务列表中删除
      ScheduledFuture<?> future = scheduledFutureMap.get(task.getId());
      if (future != null) {
        future.cancel(true);
        scheduledFutureMap.remove(task.getId());
      }
    } else {
      task.setIsRun(1);
      ScheduledFuture<?> schedule;
      if (scheduledFutureMap.containsKey(task.getId())) {
        schedule = scheduledFutureMap.get(task.getId());
        if (schedule.isCancelled()) {
          scheduledFutureMap.remove(task.getId());
        }
      } else {
        schedule = threadPoolTaskScheduler.schedule(
            () -> this.execOnceCmd(String.format("python %s\\datax.py %s", systemSetting.getBinDir(), task.getFileName()), task.getId()),
            new CronTrigger(task.getCron()));
        scheduledFutureMap.put(task.getId(), schedule);
      }


    }
    repeatTaskService.save(task);

  }

  /**
   * 开始执行一个命令
   *
   * @param cmd    命令
   * @param taskId 任务id
   */
  private void execOnceCmd(String cmd, int taskId) {
    Runtime runtime = Runtime.getRuntime();
    Process p = null;
    StringBuilder sb = new StringBuilder();
    Optional<RepeatSocketServer> first = webSocketSet.stream().filter(e -> e.sid == String.valueOf(taskId)).findFirst();
    Session sessionTmp = this.session;
    if (first.isPresent()) {
      sessionTmp = first.get().session;
    }
    final Session session = sessionTmp;
    try {
      p = runtime.exec(cmd);
      final InputStream is1 = p.getInputStream();
      final InputStream is2 = p.getErrorStream();
      new Thread(() -> {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
        RepeatTask t = this.repeatTaskService.findRepeatTaskById(taskId);
        try {
          String line1;
          while ((line1 = br1.readLine()) != null) {
//            sb.append(line1).append("\n");
            //发送输出信息
            if (session.isOpen()) {
              session.getBasicRemote().sendText(String.format("%s|[INFO]:|%s", taskId, line1));
            }
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
        RepeatTask t = this.repeatTaskService.findRepeatTaskById(taskId);
        try {
          String line2;
          while ((line2 = br2.readLine()) != null) {
            //发送错误信息
            if (session.isOpen()) {
              session.getBasicRemote().sendText(String.format("%s|[ERROR]:|%s", taskId, line2));
            }
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
      //等待任务运行
      p.waitFor();
      //任务销毁
      p.destroy();
      //scheduledFutureMap.remove(taskId);
    } catch (Exception e) {
      try {
        p.getInputStream().close();
        p.getErrorStream().close();
        p.getOutputStream().close();
      } catch (IOException ex) {
        ex.printStackTrace();

      }

    }
  }
}