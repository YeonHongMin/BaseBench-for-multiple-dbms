package com.oltpbenchmark.api.collectors.monitoring;

import com.oltpbenchmark.BenchmarkState;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.util.MonitorInfo;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 벤치마크 실행 중 워커의 처리량을 주기적으로 출력하는 일반적인 모니터 클래스입니다.
 */
public class Monitor extends Thread {
  protected static final Logger LOG = LoggerFactory.getLogger(DatabaseMonitor.class);

  protected final MonitorInfo monitorInfo;
  protected final BenchmarkState testState;
  protected final List<? extends Worker<? extends BenchmarkModule>> workers;

  {
    this.setDaemon(true);
  }

  /**
   * 모니터가 각 반복 사이에 대기하는 시간(밀리초)입니다.
   */
  Monitor(
      MonitorInfo monitorInfo,
      BenchmarkState testState,
      List<? extends Worker<? extends BenchmarkModule>> workers) {
    this.monitorInfo = monitorInfo;
    this.testState = testState;
    this.workers = workers;
  }

  @Override
  public void run() {
    int interval = this.monitorInfo.getMonitoringInterval();

    LOG.info("Starting MonitorThread Interval [{}ms]", interval);
    while (!Thread.currentThread().isInterrupted()) {
      // 마지막 처리량을 계산합니다.
      long measuredRequests = 0;
      synchronized (this.testState) {
        for (Worker<?> w : this.workers) {
          measuredRequests += w.getAndResetIntervalRequests();
        }
      }
      double seconds = interval / 1000d;
      double tps = (double) measuredRequests / seconds;
      LOG.info("Throughput: {} txn/sec", tps);

      try {
        Thread.sleep(interval);
      } catch (InterruptedException ex) {
      // 인터럽트 플래그를 복원합니다.
        Thread.currentThread().interrupt();
      }
    }
  }

  /** 테스트 종료 시 필요한 정리 작업을 수행합니다. */
  public void tearDown() {
    // 별도로 수행할 작업이 없습니다.
  }
}
