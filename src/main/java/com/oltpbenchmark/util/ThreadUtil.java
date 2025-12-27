/*
 * 저작권 2020 OLTPBenchmark 프로젝트
 *
 * Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 * 라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 * 이 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 * 라이선스에서 허용하는 권한과 제한 사항은
 * 라이선스의 본문을 참조하십시오.
 *
 */

/*
 *  Copyright (C) 2012 by H-Store Project
 *  Brown University
 *  Massachusetts Institute of Technology
 *  Yale University
 *
 *  http://hstore.cs.brown.edu/
 *
 *  본 소프트웨어 및 관련 문서 파일(이하 "소프트웨어")의 사본을 취득한 모든 사람에게
 *  무료로 사용, 복사, 수정, 병합, 출판, 배포, 서브라이선스 및/또는 판매할 수 있는 권한을 부여하며,
 *  소프트웨어를 제공받은 사람이 다음 조건을 준수하는 경우에 한하여
 *  위와 같이 할 수 있도록 허용합니다:
 *
 *  위의 저작권 고지와 본 허가 고지는 소프트웨어의 모든 사본 또는
 *  중요한 부분에 포함되어야 합니다.
 *
 *  소프트웨어는 "있는 그대로" 제공되며, 상품성, 특정 목적에의 적합성 및
 *  비침해에 대한 보증을 포함하되 이에 국한되지 않는 어떠한 종류의 명시적이거나
 *  묵시적인 보증 없이 제공됩니다. 어떠한 경우에도 저작자는 계약, 불법 행위 또는
 *  기타의 경우에 있어 소프트웨어 또는 소프트웨어의 사용 또는 기타 거래로 인해
 *  발생하는 청구, 손해 또는 기타 책임에 대해 책임을 지지 않습니다.
 */
package com.oltpbenchmark.util;

import com.oltpbenchmark.api.LoaderThread;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ThreadUtil {
  private static final Logger LOG = LoggerFactory.getLogger(ThreadUtil.class);

  public static int availableProcessors() {
    return Math.max(1, Runtime.getRuntime().availableProcessors());
  }

  /**
   * 주어진 스레드 목록에 대해 모두 실행(한 번에 최대 max_concurrent까지)하고 완료되면 반환합니다. max_concurrent가 null인 경우 모든 스레드가
   * 동시에 시작됩니다.
   *
   * @param loaderThreads
   * @param maxConcurrent
   * @throws Exception
   */
  public static void runLoaderThreads(
      final Collection<LoaderThread> loaderThreads, int maxConcurrent) throws InterruptedException {

    final int loaderThreadSize = loaderThreads.size();

    int poolSize = Math.max(1, Math.min(maxConcurrent, loaderThreadSize));

    int threadOverflow = (loaderThreadSize > poolSize ? loaderThreadSize - poolSize : 0);

    if (LOG.isInfoEnabled()) {
      LOG.info(
          "Creating a Thread Pool with a size of {} to run {} Loader Threads.  {} threads will be queued.",
          poolSize,
          loaderThreadSize,
          threadOverflow);
    }

    ExecutorService service = Executors.newFixedThreadPool(poolSize, factory);

    final long start = System.currentTimeMillis();

    final CountDownLatch latch = new CountDownLatch(loaderThreadSize);

    try {
      for (LoaderThread loaderThread : loaderThreads) {
        service.execute(new LatchRunnable(loaderThread, latch));
      }

      LOG.trace("All Loader Threads executed; waiting on latches...");
      latch.await();

    } finally {

      LOG.trace("Attempting to shutdown the pool...");

      service.shutdown();

      boolean cleanTermination = service.awaitTermination(5, TimeUnit.MINUTES);

      if (cleanTermination) {
        LOG.trace("Pool shut down cleanly!");
      } else {
        LOG.warn(
            "Pool shut down after termination timeout expired.  Likely caused by an unhandled exception in a Loader Thread causing latch count down.  Will force shutdown now.");

        List<Runnable> notStarted = service.shutdownNow();

        LOG.warn("{} Loader Threads were terminated before starting.", notStarted.size());
      }

      if (LOG.isInfoEnabled()) {
        final long stop = System.currentTimeMillis();
        LOG.info(
            String.format(
                "Finished executing %d Loader Threads [time=%.02fs]",
                loaderThreadSize, (stop - start) / 1000d));
      }
    }
  }

  private static final ThreadFactory factory =
      new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
          Thread t = new Thread(r);
          t.setDaemon(true);
          return (t);
        }
      };

  private static class LatchRunnable implements Runnable {
    private final LoaderThread loaderThread;
    private final CountDownLatch latch;

    public LatchRunnable(LoaderThread loaderThread, CountDownLatch latch) {
      this.loaderThread = loaderThread;
      this.latch = latch;
    }

    @Override
    public void run() {
      try {
        this.loaderThread.run();
      } catch (Exception e) {
        LOG.error(
            String.format(
                "Exception in Loader Thread with message: [%s]; will count down latch with count %d and then exit :(",
                e.getMessage(), this.latch.getCount()),
            e);
        System.exit(1);
      } finally {
        this.latch.countDown();
      }
    }
  }
}
