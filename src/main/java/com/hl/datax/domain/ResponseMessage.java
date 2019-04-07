package com.hl.datax.domain;

import lombok.Data;

@Data
public class ResponseMessage {
  private Boolean success;
  private Object msg;

  public ResponseMessage(Boolean success) {
    this.success = success;
  }
}
