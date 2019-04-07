package com.hl.datax.repo;

import com.hl.datax.domain.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, Integer> {
  SystemSetting findSystemSettingById(Integer id);
}
