package com.hl.datax.service;

import com.hl.datax.domain.RepeatTask;

import java.util.List;

public interface RepeatTaskService {
  List<RepeatTask> findAllByUserId(Integer userId);

  RepeatTask save(RepeatTask task);

  void delete(Integer id);
  RepeatTask findRepeatTaskById(Integer id);

  List<RepeatTask> findAllTaskByIsRun();
}
