package com.hl.datax.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExecTool {
  public static String execOnceCmd(String cmd) {
    Runtime runtime = Runtime.getRuntime();
    StringBuilder sb = new StringBuilder();
    Process p = null;
    try {
      p = runtime.exec(cmd);
      final InputStream is1 = p.getInputStream();
      final InputStream is2 = p.getErrorStream();
      new Thread(() -> {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
        try {
          String line1;
          while ((line1 = br1.readLine()) != null) {
            sb.append(line1).append("\n");
          }
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          try {
            is1.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }).start();
      new Thread(() -> {
        BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
        try {
          String line2;
          while ((line2 = br2.readLine()) != null) {
            sb.append(line2).append("\\n");
          }
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          try {
            is2.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }).start();
      p.waitFor();
      p.destroy();
    } catch (Exception e) {
      try {
        p.getInputStream().close();
        p.getErrorStream().close();
        p.getOutputStream().close();
      } catch (IOException ex) {
        ex.printStackTrace();

      }

    }
    return sb.toString();
  }
}
