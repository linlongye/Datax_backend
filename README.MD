Datax控制台后台系统
修改datax源码中com.alibaba.datax.common.util.RetryUtil.java99行中 将默认的重置次数7此改为1次
```java
private static class Retry {

    public <T> T doRetry(Callable<T> callable, int retryTimes, long sleepTimeInMilliSecond, boolean exponential, List<Class<?>> retryExceptionClasss)
        throws Exception {
      //by hl
      //修改为一次，
      retryTimes = 1;
      if (null == callable) {
        throw new IllegalArgumentException("系统编程错误, 入参callable不能为空 ! ");
      }

      if (retryTimes < 1) {
        throw new IllegalArgumentException(String.format(
            "系统编程错误, 入参retrytime[%d]不能小于1 !", retryTimes));
      }

      Exception saveException = null;
      for (int i = 0; i < retryTimes; i++) {
        try {
          return call(callable);
        } catch (Exception e) {
          saveException = e;
          if (i == 0) {
            LOG.error(String.format("Exception when calling callable, 异常Msg:%s", saveException.getMessage()), saveException);
          }

          if (null != retryExceptionClasss && !retryExceptionClasss.isEmpty()) {
            boolean needRetry = false;
            for (Class<?> eachExceptionClass : retryExceptionClasss) {
              if (eachExceptionClass == e.getClass()) {
                needRetry = true;
                break;
              }
            }
            if (!needRetry) {
              throw saveException;
            }
          }

          if (i + 1 < retryTimes && sleepTimeInMilliSecond > 0) {
            long startTime = System.currentTimeMillis();

            long timeToSleep;
            if (exponential) {
              timeToSleep = sleepTimeInMilliSecond * (long) Math.pow(2, i);
              if (timeToSleep >= MAX_SLEEP_MILLISECOND) {
                timeToSleep = MAX_SLEEP_MILLISECOND;
              }
            } else {
              timeToSleep = sleepTimeInMilliSecond;
              if (timeToSleep >= MAX_SLEEP_MILLISECOND) {
                timeToSleep = MAX_SLEEP_MILLISECOND;
              }
            }

            try {
              Thread.sleep(timeToSleep);
            } catch (InterruptedException ignored) {
            }

            long realTimeSleep = System.currentTimeMillis() - startTime;

            LOG.error(String.format("Exception when calling callable, 即将尝试执行第%s次重试.本次重试计划等待[%s]ms,实际等待[%s]ms, 异常Msg:[%s]",
                i + 1, timeToSleep, realTimeSleep, e.getMessage()));

          }
        }
      }
      throw saveException;
    }
```