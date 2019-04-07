package com.hl.datax.serviceImpl;

import com.hl.datax.domain.SystemSetting;
import com.hl.datax.repo.SystemSettingRepository;
import com.hl.datax.service.SystemSettingService;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingServiceImpl implements SystemSettingService {

  private final SystemSettingRepository systemSettingRepository;

  public SystemSettingServiceImpl(SystemSettingRepository systemSettingRepository) {
    this.systemSettingRepository = systemSettingRepository;
  }

  @Override
  public SystemSetting save(SystemSetting systemSetting) {
    return systemSettingRepository.save(systemSetting);
  }

  @Override
  public SystemSetting find(Integer id) {
    return systemSettingRepository.findAll().get(0);
  }


}
