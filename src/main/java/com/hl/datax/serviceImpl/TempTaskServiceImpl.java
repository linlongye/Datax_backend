package com.hl.datax.serviceImpl;

import com.hl.datax.domain.TempTask;
import com.hl.datax.repo.SystemSettingRepository;
import com.hl.datax.repo.TempTaskRepository;
import com.hl.datax.service.TempTaskService;
import com.hl.datax.utils.FileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TempTaskServiceImpl implements TempTaskService {

  private final TempTaskRepository tempTaskRepository;
  @Autowired
  private SystemSettingRepository settingRepository;

  public TempTaskServiceImpl(TempTaskRepository tempTaskRepository) {
    this.tempTaskRepository = tempTaskRepository;
  }

  @Override
  public List<TempTask> findAll() {
    return tempTaskRepository.findAll();
  }

  @Override
  public TempTask save(TempTask tempTask) {
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
    return tempTaskRepository.save(tempTask);
  }

  @Override
  public List<TempTask> findByUserId(Integer id) {
    return tempTaskRepository.findTempTaskByUserId(id);
  }

  @Override
  public void delete(Integer id) {
    tempTaskRepository.deleteById(id);
  }


}
