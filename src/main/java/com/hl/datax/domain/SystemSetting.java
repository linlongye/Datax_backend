package com.hl.datax.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "systemsetting")
public class SystemSetting implements Serializable {
  @Id
  @GeneratedValue
  @JsonProperty("id")
  private int id;

  /**
   * datax.py的保存目录
   */
  @JsonProperty("bindir")
  private String binDir;


  /**
   * 保存job的json文件保存目录
   */
  @JsonProperty("jobdir")
  private String jobDir;
}
