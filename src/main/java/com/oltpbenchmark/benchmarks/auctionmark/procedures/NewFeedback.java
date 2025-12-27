/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
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
 *
 */

package com.oltpbenchmark.benchmarks.auctionmark.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.auctionmark.AuctionMarkConstants;
import com.oltpbenchmark.benchmarks.auctionmark.util.AuctionMarkUtil;
import java.sql.*;

/**
 * NewFeedback
 *
 * @author pavlo
 */
public class NewFeedback extends Procedure {

  // -----------------------------------------------------------------
  // STATEMENTS
  // -----------------------------------------------------------------

  public final SQLStmt checkUserFeedback =
      new SQLStmt(
          "SELECT uf_i_id, uf_i_u_id, uf_from_id "
              + "  FROM "
              + AuctionMarkConstants.TABLENAME_USERACCT_FEEDBACK
              + " "
              + " WHERE uf_u_id = ? AND uf_i_id = ? AND uf_i_u_id = ? AND uf_from_id = ?");

  public final SQLStmt insertFeedback =
      new SQLStmt(
          "INSERT INTO "
              + AuctionMarkConstants.TABLENAME_USERACCT_FEEDBACK
              + "( "
              + "uf_u_id, "
              + "uf_i_id,"
              + "uf_i_u_id,"
              + "uf_from_id,"
              + "uf_rating,"
              + "uf_date,"
              + "uf_sattr0"
              + ") VALUES ("
              + "?,"
              + // UF_U_ID
              "?,"
              + // UF_I_ID
              "?,"
              + // UF_I_U_ID
              "?,"
              + // UF_FROM_ID
              "?,"
              + // UF_RATING
              "?,"
              + // UF_DATE
              "?"
              + // UF_SATTR0
              ")");

  public final SQLStmt updateUser =
      new SQLStmt(
          "UPDATE "
              + AuctionMarkConstants.TABLENAME_USERACCT
              + " "
              + "SET u_rating = u_rating + ?, "
              + "    u_updated = ? "
              + " WHERE u_id = ?");

  // -----------------------------------------------------------------
  // RUN METHOD
  // -----------------------------------------------------------------

  public void run(
      Connection conn,
      Timestamp[] benchmarkTimes,
      String user_id,
      String i_id,
      String seller_id,
      String from_id,
      long rating,
      String comment)
      throws SQLException {
    final Timestamp currentTime = AuctionMarkUtil.getProcTimestamp(benchmarkTimes);

    // Check to make sure they're not trying to add feedback
    // twice for the same ITEM
    try (PreparedStatement stmt =
        this.getPreparedStatement(conn, checkUserFeedback, user_id, i_id, seller_id, from_id)) {
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          throw new UserAbortException("Trying to add feedback for item " + i_id + " twice");
        }
      }
    }

    try (PreparedStatement stmt =
        this.getPreparedStatement(
            conn,
            insertFeedback,
            user_id,
            i_id,
            seller_id,
            from_id,
            rating,
            currentTime,
            comment)) {
      stmt.executeUpdate();
    }
    try (PreparedStatement preparedStatement =
        this.getPreparedStatement(conn, updateUser, rating, currentTime, user_id)) {
      preparedStatement.executeUpdate();
    }
  }
}
