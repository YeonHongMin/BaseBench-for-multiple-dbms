/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
<<<<<<< HEAD
=======
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
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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

public class InsertCallForwarding extends Procedure {

  public final SQLStmt getSubscriber =
      new SQLStmt("SELECT s_id FROM " + TATPConstants.TABLENAME_SUBSCRIBER + " WHERE sub_nbr = ?");

  public final SQLStmt getSpecialFacility =
      new SQLStmt(
          "SELECT sf_type FROM " + TATPConstants.TABLENAME_SPECIAL_FACILITY + " WHERE s_id = ?");

  public final SQLStmt insertCallForwarding =
      new SQLStmt(
          "INSERT INTO " + TATPConstants.TABLENAME_CALL_FORWARDING + " VALUES (?, ?, ?, ?, ?)");

  public long run(
      Connection conn, String sub_nbr, byte sf_type, byte start_time, byte end_time, String numberx)
      throws SQLException {
    long s_id = -1;

    try (PreparedStatement stmt = this.getPreparedStatement(conn, getSubscriber)) {
      stmt.setString(1, sub_nbr);
      try (ResultSet results = stmt.executeQuery()) {
        if (results.next()) {
          s_id = results.getLong(1);
        }
      }
    }

    try (PreparedStatement stmt = this.getPreparedStatement(conn, getSpecialFacility)) {
      stmt.setLong(1, s_id);
      try (ResultSet results = stmt.executeQuery()) {
        assert results != null;
      }
    }

    // Inserting a new CALL_FORWARDING record only succeeds 30% of the time

    int rows_updated = -1;

    try (PreparedStatement stmt = this.getPreparedStatement(conn, insertCallForwarding)) {
      stmt.setLong(1, s_id);
      stmt.setByte(2, sf_type);
      stmt.setByte(3, start_time);
      stmt.setByte(4, end_time);
      stmt.setString(5, numberx);

      try {
        rows_updated = stmt.executeUpdate();
      } catch (SQLException ex) {
        throw new UserAbortException(
            "Failed to insert a row in " + TATPConstants.TABLENAME_CALL_FORWARDING);
      }
    }
    return (rows_updated);
  }
}
