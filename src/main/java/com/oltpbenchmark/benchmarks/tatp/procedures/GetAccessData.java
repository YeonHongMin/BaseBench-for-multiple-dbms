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

package com.oltpbenchmark.benchmarks.tatp.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.tatp.TATPConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetAccessData extends Procedure {

  public final SQLStmt getAccessInfo =
      new SQLStmt(
          "SELECT data1, data2, data3, data4 FROM "
              + TATPConstants.TABLENAME_ACCESS_INFO
              + " WHERE s_id = ? AND ai_type = ?");

  public void run(Connection conn, long s_id, byte ai_type) throws SQLException {
    try (PreparedStatement stmt = this.getPreparedStatement(conn, getAccessInfo)) {
      stmt.setLong(1, s_id);
      stmt.setByte(2, ai_type);
      try (ResultSet results = stmt.executeQuery()) {
        assert results != null;
      }
    }
  }
}
