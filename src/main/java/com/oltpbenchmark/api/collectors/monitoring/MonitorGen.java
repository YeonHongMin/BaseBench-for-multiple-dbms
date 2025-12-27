package com.oltpbenchmark.api.collectors.monitoring;

import com.oltpbenchmark.BenchmarkState;
import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.util.MonitorInfo;
import java.util.List;

<<<<<<< HEAD
<<<<<<< HEAD
/**
 * Monitor generator that picks the appropriate monitoring implemnetation based on the database
 * type.
 */
=======
/** DB 유형에 따라 적절한 모니터 구현체를 선택하는 모니터 생성기입니다. */
>>>>>>> master
=======
/** DB 유형에 따라 적절한 모니터 구현체를 선택하는 모니터 생성기입니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
public class MonitorGen {
  public static Monitor getMonitor(
      MonitorInfo monitorInfo,
      BenchmarkState testState,
      List<? extends Worker<? extends BenchmarkModule>> workers,
      WorkloadConfiguration conf) {
    switch (monitorInfo.getMonitoringType()) {
      case ADVANCED:
        {
          switch (conf.getDatabaseType()) {
            case SQLSERVER:
              return new SQLServerMonitor(monitorInfo, testState, workers, conf);
            case POSTGRES:
              return new PostgreSQLMonitor(monitorInfo, testState, workers, conf);
            default:
              return new Monitor(monitorInfo, testState, workers);
          }
        }
      default:
        return new Monitor(monitorInfo, testState, workers);
    }
  }
}
