package com.hl.datax.serviceImpl;

import com.hl.datax.domain.RepeatTask;
import com.hl.datax.repo.RepeatTaskRepository;
import com.hl.datax.repo.SystemSettingRepository;
import com.hl.datax.service.RepeatTaskService;
import com.hl.datax.utils.FileTool;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RepeatTaskServiceImpl implements RepeatTaskService {

  private final SystemSettingRepository settingRepository;
  private final RepeatTaskRepository repeatTaskRepository;

  public RepeatTaskServiceImpl(RepeatTaskRepository repeatTaskRepository, SystemSettingRepository settingRepository) {
    this.repeatTaskRepository = repeatTaskRepository;
    this.settingRepository = settingRepository;
  }

  @Override
  public List<RepeatTask> findAllByUserId(Integer userId) {
    return repeatTaskRepository.findAllByUserId(userId);
  }

  @Override
  public RepeatTask save(RepeatTask tempTask) {
    if (tempTask == null) {
      return null;
    }
    if (tempTask.getId() != null) {
      FileTool.deleteFile(tempTask.getFileName());
    }
    String fileName = null;
    try {
      fileName = FileTool.saveFile(tempTask.getJsonStr(), settingRepository);
    } catch (IOException e) {
      e.printStackTrace();
    }
    tempTask.setFileName(fileName);
    return repeatTaskRepository.save(tempTask);
  }

  @Override
  public void delete(Integer id) {
    repeatTaskRepository.deleteById(id);
  }

  @Override
  public RepeatTask findRepeatTaskById(Integer id) {
    return repeatTaskRepository.findRepeatTaskById(id);
  }

  @Override
  public List<RepeatTask> findAllTaskByIsRun() {
    return repeatTaskRepository.findAllByIsRun(1);
  }


}
