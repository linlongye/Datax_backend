package com.hl.datax.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user")
@ToString
public class User implements Serializable {

  @JsonProperty("id")
  @Id
  @GeneratedValue
  private Integer id;

  @Column(nullable = false, unique = true)
  @JsonProperty("username")
  private String userName;

  @Column(nullable = false)
  @JsonProperty("password")
  private String password;

  @Column(name = "role")
  @JsonProperty("role")
  private Integer role;

}
