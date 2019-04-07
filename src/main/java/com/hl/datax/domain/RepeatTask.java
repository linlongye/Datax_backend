package com.hl.datax.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "repeattask")
public class RepeatTask implements Serializable {

  @Id
  @GeneratedValue
  @JsonProperty("id")
  private Integer id;

  /**
   * 任务名称
   */
  @JsonProperty("name")
  private String name;

  /**
   * 配置的json字符串
   */
  @JsonProperty("jsonstr")
  private String jsonStr;

  /**
   * 备注
   */
  @JsonProperty("remark")
  private String remark;

  /**
   * 用户id
   */
  @JsonProperty("userid")
  private Integer userId;

  @JsonProperty("filename")
  private String fileName;

  /**
   * 定时任务的cron表达式
   */
  @JsonProperty("cron")
  private String cron;

  /**
   * 任务是否已经运行，0代表没有运行，1代表正在运行
   */
  @JsonProperty("isrun")
  private Integer isRun;

  /**
   * 执行的结果，不执行序列化
   */
  @JsonProperty("result")
  private String result;
}
