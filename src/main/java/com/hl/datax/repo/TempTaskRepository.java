package com.hl.datax.repo;

import com.hl.datax.domain.TempTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempTaskRepository extends JpaRepository<TempTask, Integer> {
  List<TempTask> findTempTaskByUserId(Integer id);
}
