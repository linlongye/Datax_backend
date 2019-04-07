package com.hl.datax.service;

import com.hl.datax.domain.SystemSetting;

public interface SystemSettingService {
  SystemSetting save(SystemSetting systemSetting);

  SystemSetting find(Integer id);
}
