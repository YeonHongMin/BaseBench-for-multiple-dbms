package com.oltpbenchmark.api.collectors.monitoring;

import com.oltpbenchmark.BenchmarkState;
import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.util.FileUtil;
import com.oltpbenchmark.util.MonitorInfo;
import com.oltpbenchmark.util.StringUtil;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.immutables.value.Value;

/** DBMS 전반에서 사용하는 기능을 통합한 일반적인 데이터베이스 모니터입니다. */
public abstract class DatabaseMonitor extends Monitor {
  protected enum DatabaseState {
    READY,
    INVALID,
    TEST
  };

  protected final String OUTPUT_DIR = "results/monitor";
  protected final String CSV_DELIMITER = ",";
  protected final String SINGLE_QUERY_EVENT_CSV = "single_query_event";
  protected final String REP_QUERY_EVENT_CSV = "repeated_query_event";
  protected final String REP_SYSTEM_EVENT_CSV = "system_query_event";
  protected final int FILE_FLUSH_COUNT = 1000; // 1000ms마다 메트릭 파일에 기록을 플러시합니다.

  protected DatabaseState currentState = DatabaseState.INVALID;
  protected int ticks = 1;

  protected WorkloadConfiguration conf;
  protected Connection conn;
  protected List<SingleQueryEvent> singleQueryEvents;
  protected List<RepeatedQueryEvent> repeatedQueryEvents;
  protected List<RepeatedSystemEvent> repeatedSystemEvents;

  /**
   * 벤치마크 환경과 동일한 연결 정보로 DBMS에 접속합니다.
   *
   * @param conf
   * @return
   * @throws SQLException
   */
  private final Connection makeConnection() throws SQLException {
    if (StringUtils.isEmpty(conf.getUsername())) {
      return DriverManager.getConnection(conf.getUrl());
    } else {
      return DriverManager.getConnection(conf.getUrl(), conf.getUsername(), conf.getPassword());
    }
  }

  public DatabaseMonitor(
      MonitorInfo monitorInfo,
      BenchmarkState testState,
      List<? extends Worker<? extends BenchmarkModule>> workers,
      WorkloadConfiguration workloadConf) {
    super(monitorInfo, testState, workers);

    try {
      this.conf = workloadConf;
      this.conn = makeConnection();
    } catch (SQLException e) {
      this.conn = null;
      LOG.error("Could not initialize connection to create DatabaseMonitor.");
      LOG.error(e.getMessage());
    }

    FileUtil.makeDirIfNotExists(OUTPUT_DIR);

    // 이벤트 기록용 리스트를 초기화합니다.
    this.singleQueryEvents = new ArrayList<>();
    this.repeatedQueryEvents = new ArrayList<>();
    this.repeatedSystemEvents = new ArrayList<>();

    LOG.info("Initialized DatabaseMonitor.");
  }

  protected void writeSingleQueryEventsToCSV() {
    String filePath = getFilePath(SINGLE_QUERY_EVENT_CSV, this.ticks);
    try {
      if (this.singleQueryEvents.size() == 0) {
        LOG.warn("No query events have been recorded, file not written.");
        return;
      }

      if (Files.deleteIfExists(Paths.get(filePath))) {
        LOG.warn("File at " + filePath + " deleted before writing query events to file.");
      }
      PrintStream out = new PrintStream(filePath);
      out.println(
          "QueryId,"
              + StringUtil.join(",", this.singleQueryEvents.get(0).getPropertyValues().keySet()));
      for (SingleQueryEvent event : this.singleQueryEvents) {
        out.println(
            event.getQueryId()
                + ","
                + StringUtil.join(",", this.singleQueryEvents.get(0).getPropertyValues().values()));
      }
      out.close();
      this.singleQueryEvents = new ArrayList<>();
      LOG.info("Query events written to " + filePath);
    } catch (IOException e) {
      LOG.error("Error when writing query events to file.");
      LOG.error(e.getMessage());
    }
  }

  protected void writeRepeatedQueryEventsToCSV() {
    String filePath = getFilePath(REP_QUERY_EVENT_CSV, this.ticks);
    try {
      if (this.repeatedQueryEvents.size() == 0) {
        LOG.warn("No repeated query events have been recorded, file not written.");
        return;
      }

      if (Files.deleteIfExists(Paths.get(filePath))) {
        LOG.warn("File at " + filePath + " deleted before writing repeated query events to file.");
      }
      PrintStream out = new PrintStream(filePath);
      out.println(
          "QueryId,Instant,"
              + StringUtil.join(",", this.repeatedQueryEvents.get(0).getPropertyValues().keySet()));
      for (RepeatedQueryEvent event : this.repeatedQueryEvents) {
        out.println(
            event.getQueryId()
                + ","
                + event.getInstant().toString()
                + ","
                + StringUtil.join(
                    ",", this.repeatedQueryEvents.get(0).getPropertyValues().values()));
      }
      out.close();
      this.repeatedQueryEvents = new ArrayList<>();
      LOG.info("Repeated query events written to " + filePath);
    } catch (IOException e) {
      LOG.error("Error when writing repeated query events to file.");
      LOG.error(e.getMessage());
    }
  }

  protected void writeRepeatedSystemEventsToCSV() {
    String filePath = getFilePath(REP_SYSTEM_EVENT_CSV, this.ticks);
    try {
      if (this.repeatedSystemEvents.size() == 0) {
        LOG.warn("No repeated system events have been recorded, file not written.");
        return;
      }

      if (Files.deleteIfExists(Paths.get(filePath))) {
        LOG.warn("File at " + filePath + " deleted before writing repeated system events to file.");
      }
      PrintStream out = new PrintStream(filePath);
      out.println(
          "Instant,"
              + StringUtil.join(
                  ",", this.repeatedSystemEvents.get(0).getPropertyValues().keySet()));
      for (RepeatedSystemEvent event : this.repeatedSystemEvents) {
        out.println(
            event.getInstant().toString()
                + ","
                + StringUtil.join(
                    ",", this.repeatedSystemEvents.get(0).getPropertyValues().values()));
      }
      out.close();
      this.repeatedSystemEvents = new ArrayList<>();
      LOG.info("Repeated system events written to " + filePath);
    } catch (IOException e) {
      LOG.error("Error when writing repeated system events to file.");
      LOG.error(e.getMessage());
    }
  }

  protected String getFilePath(String filename, int fileCounter) {
    return FileUtil.joinPath(OUTPUT_DIR, filename + "_" + fileCounter + ".csv");
  }

  protected void cleanupCache() {
    try (PreparedStatement stmt = conn.prepareStatement(this.getCleanupStmt())) {
      stmt.execute();
    } catch (SQLException sqlError) {
      LOG.error("Error when cleaning up cached plans.");
      LOG.error(sqlError.getMessage());
    }
  }

  protected void writeQueryMetrics() {
    this.writeSingleQueryEventsToCSV();
    this.writeRepeatedQueryEventsToCSV();
  }

  protected void writeToCSV() {
    this.writeQueryMetrics();
    this.writeSystemMetrics();
  }

  @Value.Immutable
  public interface SingleQueryEvent {

    /** 쿼리를 식별하는 문자열입니다. */
    String getQueryId();

    /** 관측한 속성과 그 값의 매핑입니다. */
    Map<String, String> getPropertyValues();
  }

  @Value.Immutable
  public interface RepeatedQueryEvent {

    /** 쿼리를 식별하는 문자열입니다. */
    String getQueryId();

    /** 이 이벤트가 관측된 시점의 타임스탬프입니다. */
    Instant getInstant();

    /** 관측한 속성과 그 값의 매핑입니다. */
    Map<String, String> getPropertyValues();
  }

  @Value.Immutable
  public interface RepeatedSystemEvent {

    /** 이 시스템 이벤트가 관측된 시점의 타임스탬프입니다. */
    Instant getInstant();

    /** 관측한 속성과 그 값의 매핑입니다. */
    Map<String, String> getPropertyValues();
  }

  protected abstract String getCleanupStmt();

  /** 원하는 쿼리 및 성능 지표의 추출을 실행합니다. */
  protected abstract void runExtraction();

  /** 시스템 관련 메트릭을 기록합니다. */
  protected abstract void writeSystemMetrics();

  /**
   * 모니터를 실행합니다. 캐시를 정리하고 초기 추출을 수행한 뒤 주기(interval)만큼 대기합니다.
   * 각 주기마다 메트릭을 추출하고, 필요 시 파일에 기록한 뒤 실행이 끝나면 로그를 모으고 캐시를 다시 정리합니다.
   */
  @Override
  public void run() {
    int interval = this.monitorInfo.getMonitoringInterval();

    LOG.info("Starting Monitor Interval [{}ms]", interval);
      // 초기 설정 동안 최소한 하나의 이벤트는 기록되도록 합니다.
    if (this.conn != null) {
      cleanupCache();
      runExtraction();
    }
    // 주기적으로 시스템 테이블 통계를 수집합니다.
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(interval);
      } catch (InterruptedException ex) {
      // 인터럽트 플래그를 복원합니다.
        Thread.currentThread().interrupt();
      }
      if (this.conn != null) {
        runExtraction();
      }
      if (ticks % FILE_FLUSH_COUNT == 0) {
        writeToCSV();
      }
      ticks++;
    }

    if (this.conn != null) {
      cleanupCache();
    }

    writeToCSV();
  }

  /** 테스트 종료 시 필요한 정리 작업을 수행합니다. */
  @Override
  public void tearDown() {
    if (this.conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        LOG.error("Connection could not be closed.", e);
      }
      this.conn = null;
    }
  }
}
