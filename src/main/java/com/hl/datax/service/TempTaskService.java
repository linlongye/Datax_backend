package com.hl.datax.service;

import com.hl.datax.domain.TempTask;

import java.util.List;

public interface TempTaskService {
  List<TempTask> findAll();

  TempTask save(TempTask tempTask);

  List<TempTask> findByUserId(Integer id);

  void delete(Integer id);
}
