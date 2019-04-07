package com.hl.datax.repo;

import com.hl.datax.domain.RepeatTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepeatTaskRepository extends JpaRepository<RepeatTask, Integer> {
  List<RepeatTask> findAllByUserId(Integer userId);

  RepeatTask findRepeatTaskById(Integer id);

  /**
   * 查找所有正在运行或者没有运行的任务，0代表没有运行，1代码正在运行
   *
   * @param isRun
   * @return
   */
  List<RepeatTask> findAllByIsRun(Integer isRun);
}
