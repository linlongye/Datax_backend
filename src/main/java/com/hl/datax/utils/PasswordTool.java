package com.hl.datax.utils;

import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@Configuration
public class PasswordTool {
  //密钥
  private String keyt="1234567890";

  public String getKeyt() {
    return keyt;
  }

  public void setKeyt(String keyt) {
    this.keyt = keyt;
  }

  /**
   * 加密
   *
   * @param content  需要加密的内容
   * @param password 加密密码
   * @return
   */
  public static String encrypt(String content, String password) {
    try {
      KeyGenerator kgen = KeyGenerator.getInstance("AES");
      /*
       * 这个是由于linux和window的内核不同造成的！
       * SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
       * secureRandom.setSeed(PASSWORD.getBytes());
       * 然后初始化，就能解决这个问题！
       */
      SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
      secureRandom.setSeed(password.getBytes());
      kgen.init(128, secureRandom);
      SecretKey secretKey = kgen.generateKey();
      byte[] enCodeFormat = secretKey.getEncoded();
      SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
      Cipher cipher = Cipher.getInstance("AES");// 创建密码器
      byte[] byteContent = content.getBytes("utf-8");
      cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
      byte[] result = cipher.doFinal(byteContent);
      return parseByte2HexStr(result); // 加密
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * 将二进制转换成16进制
   *
   * @param buf
   * @return
   */
  public static String parseByte2HexStr(byte buf[]) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
      String hex = Integer.toHexString(buf[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sb.append(hex.toUpperCase());
    }
    return sb.toString();
  }
}

