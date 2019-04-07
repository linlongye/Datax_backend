package com.hl.datax.utils;

import com.google.common.base.Strings;
import com.hl.datax.domain.SystemSetting;
import com.hl.datax.domain.TempTask;
import com.hl.datax.repo.SystemSettingRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class FileTool {

  /**
   * 保存配置信息到文件
   *
   * @param content 配置信息
   */
  public static String saveFile(String content, SystemSettingRepository settingRepository) throws IOException {
    SystemSetting systemSetting = settingRepository.findAll().get(0);
    String fileName = String.format("%s/%s.json", systemSetting.getJobDir(), UUID.randomUUID().toString());
    File file = new File(fileName);
    if (!file.exists()) {
      file.createNewFile();
    }
    FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
    BufferedWriter bw = new BufferedWriter(fileWriter);
    bw.write(content);
    bw.flush();
    bw.close();
    fileWriter.close();
    return fileName;
  }

  public static boolean deleteFile(String fileName) {
    if (Strings.isNullOrEmpty(fileName)) {
      return true;
    }
    File file = new File(fileName);
    if (file.exists()) {
      return file.delete();
    }
    return false;
  }
}
