package com.oltpbenchmark.api.collectors.monitoring;

import com.oltpbenchmark.BenchmarkState;
import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.util.MonitorInfo;
import com.oltpbenchmark.util.MonitoringUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQLServer에 특화된 모니터 구현체입니다. 시스템 테이블에서 쿼리/시스템 정보를 추출합니다.
 * 참고: "VIEW SERVER PERFORMANCE STATE" 권한이 필요합니다.
 */
public class SQLServerMonitor extends DatabaseMonitor {

  private final String DM_EXEC_QUERY_STATS =
      """
          SELECT q.text AS query_text, st.plan_handle, pl.query_plan,
          q.text AS query_text, st.plan_handle, pl.query_plan,
          st.execution_count, st.min_worker_time, st.max_worker_time,
          st.total_worker_time, st.min_physical_reads, st.max_physical_reads,
          st.total_physical_reads, st.min_elapsed_time, st.max_elapsed_time,
          st.total_elapsed_time, st.total_rows, st.min_rows, st.max_rows,
          st.min_spills, st.max_spills, st.total_spills,
          st.min_logical_writes, st.max_logical_writes, st.total_logical_writes,
          st.min_logical_reads, st.max_logical_reads, st.total_logical_reads,
          st.min_used_grant_kb, st.max_used_grant_kb, st.total_used_grant_kb,
          st.min_used_threads, st.max_used_threads, st.total_used_threads
          FROM sys.dm_exec_query_stats st
          CROSS APPLY sys.dm_exec_sql_text(st.plan_handle) q
          CROSS APPLY sys.dm_exec_query_plan(st.plan_handle) pl
      """;
  private String DM_OS_PERFORMANCE_STATS =
      """
          SELECT cntr_value, counter_name
          FROM sys.dm_os_performance_counters
          WHERE instance_name='%s';
      """;
  private final String CLEAN_CACHE = "DBCC FREEPROCCACHE;";
  private final Pattern DATABASE_URL_PATTERN = Pattern.compile("database=(?<instanceName>\\S+);");

  private final List<String> singleQueryProperties;
  private final List<String> repeatedQueryProperties;
  private final List<String> repeatedSystemProperties;

  private final Set<String> cached_plans;

  public SQLServerMonitor(
      MonitorInfo monitorInfo,
      BenchmarkState testState,
      List<? extends Worker<? extends BenchmarkModule>> workers,
      WorkloadConfiguration conf) {
    super(monitorInfo, testState, workers, conf);

    // URL에서 데이터베이스 인스턴스 이름을 추출합니다.
    Matcher m = DATABASE_URL_PATTERN.matcher(conf.getUrl());
    if (m.find()) {
      DM_OS_PERFORMANCE_STATS = DM_OS_PERFORMANCE_STATS.formatted(m.group("instanceName"));
    }

    this.cached_plans = new HashSet<String>();

    this.singleQueryProperties =
        new ArrayList<String>() {
          {
            add("query_plan");
            add("plan_handle");
          }
        };

    this.repeatedQueryProperties =
        new ArrayList<String>() {
          {
            add("execution_count");
            add("min_worker_time");
            add("max_worker_time");
            add("total_worker_time");
            add("min_physical_reads");
            add("max_physical_reads");
            add("total_physical_reads");
            add("min_elapsed_time");
            add("max_elapsed_time");
            add("total_elapsed_time");
            add("min_rows");
            add("max_rows");
            add("total_rows");
            add("min_spills");
            add("max_spills");
            add("total_spills");
            add("min_logical_writes");
            add("max_logical_writes");
            add("total_logical_writes");
            add("min_logical_reads");
            add("max_logical_reads");
            add("total_logical_reads");
            add("min_used_grant_kb");
            add("max_used_grant_kb");
            add("total_used_grant_kb");
            add("min_used_threads");
            add("max_used_threads");
            add("total_used_threads");
            add("plan_handle");
          }
        };

    this.repeatedSystemProperties =
        new ArrayList<String>() {
          {
            add("Data File(s) Size (KB)");
            add("Transactions/sec");
            add("Write Transactions/sec");
            add("Cache Hit Ratio");
            add("Cache Entries Count");
          }
        };
  }

  /**
   * 앞서 정의한 쿼리와 속성을 이용해 단일 및 반복 쿼리 이벤트를 추출합니다.
   */
  private void extractQueryMetrics(Instant instant) {
    ImmutableSingleQueryEvent.Builder singleQueryEventBuilder = ImmutableSingleQueryEvent.builder();
    ImmutableRepeatedQueryEvent.Builder repeatedQueryEventBuilder =
        ImmutableRepeatedQueryEvent.builder();

    try (PreparedStatement stmt = conn.prepareStatement(DM_EXEC_QUERY_STATS)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        // SQL Server 방언 XML에서 주석으로 모니터링이 활성화된 쿼리만 저장합니다.
        String query_text = rs.getString("query_text");
        if (!query_text.contains(MonitoringUtil.getMonitoringPrefix())) {
          continue;
        }

        // 쿼리 텍스트 내 주석에서 식별자를 가져옵니다.
        Matcher m = MonitoringUtil.getMonitoringPattern().matcher(query_text);
        if (m.find()) {
          String identifier = m.group("queryId");
          query_text = m.replaceAll("");
          // 플랜을 식별하기 위해 plan_handle을 가져옵니다.
          String plan_handle = rs.getString("plan_handle");

          // 플랜이 처음 실행될 때 발생할 수 있는 단일 쿼리 정보를 처리합니다.
          Map<String, String> propertyValues;
          if (!cached_plans.contains(plan_handle)) {
            cached_plans.add(plan_handle);

            singleQueryEventBuilder.queryId(identifier);
            propertyValues = new HashMap<String, String>();
            propertyValues.put("query_text", query_text);
            // 단일 이벤트 정보를 추가합니다.
            for (String property : this.singleQueryProperties) {
              String value = rs.getString(property);
              if (value != null) {
                propertyValues.put(property, value);
              }
            }
            singleQueryEventBuilder.propertyValues(propertyValues);
            this.singleQueryEvents.add(singleQueryEventBuilder.build());
          }

          // 반복 쿼리 이벤트를 처리합니다.
          repeatedQueryEventBuilder.queryId(identifier).instant(instant);
          propertyValues = new HashMap<String, String>();
          for (String property : this.repeatedQueryProperties) {
            String value = rs.getString(property);
            if (value != null) {
              propertyValues.put(property, value);
            }
          }
          repeatedQueryEventBuilder.propertyValues(propertyValues);
          this.repeatedQueryEvents.add(repeatedQueryEventBuilder.build());
        }
      }
    } catch (SQLException sqlError) {
      LOG.error("Error when extracting per query measurements.");
      LOG.error(sqlError.getMessage());
    }
  }

  /**
   * 앞서 정의한 쿼리와 속성을 이용해 시스템 이벤트를 추출합니다. 벤치마크 실행을 방해하지 않도록 예외는 우아하게 처리합니다.
   */
  private void extractPerformanceMetrics(Instant instant) {
    ImmutableRepeatedSystemEvent.Builder repeatedSystemEventBuilder =
        ImmutableRepeatedSystemEvent.builder();
    repeatedSystemEventBuilder.instant(instant);

    // OS 성능 관련 이벤트를 추출합니다.
    Map<String, String> propertyValues = new HashMap<String, String>();
    try (PreparedStatement stmt = conn.prepareStatement(DM_OS_PERFORMANCE_STATS)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        // 속성 값을 추가합니다.
        String counter_name = rs.getString("counter_name").trim();
        if (this.repeatedSystemProperties.contains(counter_name)) {
          propertyValues.put(counter_name, rs.getString("cntr_value"));
        }
      }
    } catch (SQLException sqlError) {
      LOG.error("Error when extracting OS metrics from SQL Server.");
      LOG.error(sqlError.getMessage());
    }
    repeatedSystemEventBuilder.propertyValues(propertyValues);
    this.repeatedSystemEvents.add(repeatedSystemEventBuilder.build());
  }

  @Override
  protected String getCleanupStmt() {
    return CLEAN_CACHE;
  }

  @Override
  protected void runExtraction() {
    Instant time = Instant.now();

    extractQueryMetrics(time);
    extractPerformanceMetrics(time);
  }

  @Override
  protected void writeSystemMetrics() {
    this.writeRepeatedSystemEventsToCSV();
  }
}
