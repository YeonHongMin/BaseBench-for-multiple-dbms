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
package com.oltpbenchmark.api.collectors;

import com.oltpbenchmark.util.JSONUtil;
import java.sql.*;
import java.util.*;

public class PostgresCollector extends DBCollector {

  private static final String VERSION_SQL = "SELECT version();";

  private static final String PARAMETERS_SQL = "SHOW ALL;";

  private static final String[] PG_STAT_VIEWS = {
    "pg_stat_archiver", "pg_stat_bgwriter", "pg_stat_database",
    "pg_stat_database_conflicts", "pg_stat_user_tables", "pg_statio_user_tables",
    "pg_stat_user_indexes", "pg_statio_user_indexes"
  };

  private final Map<String, List<Map<String, String>>> pgMetrics = new HashMap<>();

  public PostgresCollector(String oriDBUrl, String username, String password) {

    try (Connection conn = DriverManager.getConnection(oriDBUrl, username, password)) {
      try (Statement s = conn.createStatement()) {

        // DBMS 踰꾩쟾???섏쭛?⑸땲??
        try (ResultSet out = s.executeQuery(VERSION_SQL)) {
          if (out.next()) {
            this.version = out.getString(1);
          }
        }

        // DBMS ?뚮씪誘명꽣瑜??섏쭛?⑸땲??
        try (ResultSet out = s.executeQuery(PARAMETERS_SQL)) {
          while (out.next()) {
            dbParameters.put(out.getString("name"), out.getString("setting"));
          }
        }

        // DBMS ?대? 硫뷀듃由?쓣 ?섏쭛?⑸땲??
        for (String viewName : PG_STAT_VIEWS) {
          try (ResultSet out = s.executeQuery("SELECT * FROM " + viewName)) {
            pgMetrics.put(viewName, getMetrics(out));
          } catch (SQLException ex) {
            LOG.error("Error while collecting DB metric view: {}", ex.getMessage());
          }
        }
      }
    } catch (SQLException e) {
      LOG.error("Error while collecting DB parameters: {}", e.getMessage());
    }
  }

  @Override
  public boolean hasMetrics() {
    return (!pgMetrics.isEmpty());
  }

  @Override
  public String collectMetrics() {
    return JSONUtil.format(JSONUtil.toJSONString(pgMetrics));
  }

  private List<Map<String, String>> getMetrics(ResultSet out) throws SQLException {
    ResultSetMetaData metadata = out.getMetaData();
    int numColumns = metadata.getColumnCount();
    String[] columnNames = new String[numColumns];
    for (int i = 0; i < numColumns; ++i) {
      columnNames[i] = metadata.getColumnName(i + 1).toLowerCase();
    }

    List<Map<String, String>> metrics = new ArrayList<>();
    while (out.next()) {
      Map<String, String> metricMap = new TreeMap<>();
      for (int i = 0; i < numColumns; ++i) {
        metricMap.put(columnNames[i], out.getString(i + 1));
      }
      metrics.add(metricMap);
    }
    return metrics;
  }
}

