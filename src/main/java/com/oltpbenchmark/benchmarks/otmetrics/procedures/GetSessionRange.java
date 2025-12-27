/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Apache License, Version 2.0 (이하 "라이센스")에 따라 라이센스가 부여됩니다.
 * 이 파일은 라이센스에 따라 사용할 수 있으며, 라이센스에 따라 사용하지 않는 한
 * 사용할 수 없습니다. 라이센스 사본은 다음에서 얻을 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 적용 가능한 법률에 의해 요구되거나 서면으로 합의되지 않는 한, 라이센스에 따라
 * 배포되는 소프트웨어는 "있는 그대로" 배포되며, 명시적이거나 묵시적인 어떠한 종류의
 * 보증이나 조건도 없습니다. 라이센스에 따른 권한 및 제한 사항에 대한 자세한 내용은
 * 라이센스를 참조하십시오.
 *
 */

package com.oltpbenchmark.benchmarks.otmetrics.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetSessionRange extends Procedure {

  /**
   * 모든 JDBC 드라이버가 배열을 사용하여 가변 길이 매개변수를 채우는 것을 지원하지 않기 때문에
   * 이 방법을 사용합니다.
   */
  private final String baseSQL =
      "SELECT * FROM observations"
          + " WHERE source_id = ?"
          + "   AND session_id >= ?"
          + "   AND session_id <= ?"
          + "   AND type_id IN (%s)"
          + " ORDER BY created_time";

  public final SQLStmt RangeQuery1 = new SQLStmt(String.format(baseSQL, "?"));
  public final SQLStmt RangeQuery2 = new SQLStmt(String.format(baseSQL, "?, ?"));
  public final SQLStmt RangeQuery3 = new SQLStmt(String.format(baseSQL, "?, ?, ?"));

  public List<Object[]> run(
      Connection conn, int source_id, int session_low, int session_high, int type_ids[])
      throws SQLException {
    final List<Object[]> finalResults = new ArrayList<>();

    PreparedStatement stmt;
    switch (type_ids.length) {
      case 1:
        stmt =
            this.getPreparedStatement(
                conn, RangeQuery1, source_id, session_low, session_high, type_ids[0]);
        break;
      case 2:
        stmt =
            this.getPreparedStatement(
                conn, RangeQuery2, source_id, session_low, session_high, type_ids[0], type_ids[1]);
        break;
      case 3:
        stmt =
            this.getPreparedStatement(
                conn,
                RangeQuery3,
                source_id,
                session_low,
                session_high,
                type_ids[0],
                type_ids[1],
                type_ids[2]);
        break;
      default:
        throw new RuntimeException("Unexpected type_id array length of " + type_ids.length);
    } // SWITCH
    assert (stmt != null);

    // 실행!
    try (ResultSet results = stmt.executeQuery()) {
      while (results.next()) {
        int cols = results.getMetaData().getColumnCount();
        Object[] arr = new Object[cols];
        for (int i = 0; i < cols; i++) {
          arr[i] = results.getObject(i + 1).toString();
        }
        finalResults.add(arr);
      }
    }
    stmt.close();

    return (finalResults);
  }
}
