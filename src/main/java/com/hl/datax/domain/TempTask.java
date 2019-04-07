package com.hl.datax.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 临时任务，仅执行一次的任务
 */

@Entity
@Table(name = "temptask")
@Data
@ToString
public class TempTask implements Serializable {
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

}
