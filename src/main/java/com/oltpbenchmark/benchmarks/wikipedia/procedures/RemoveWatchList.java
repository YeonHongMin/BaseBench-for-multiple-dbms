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

package com.oltpbenchmark.benchmarks.wikipedia.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.wikipedia.WikipediaConstants;
import com.oltpbenchmark.util.TimeUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveWatchList extends Procedure {

  public SQLStmt removeWatchList =
      new SQLStmt(
          "DELETE FROM "
              + WikipediaConstants.TABLENAME_WATCHLIST
              + " WHERE wl_user = ? AND wl_namespace = ? AND wl_title = ?");
  public SQLStmt setUserTouched =
      new SQLStmt(
          "UPDATE "
              + WikipediaConstants.TABLENAME_USER
              + "   SET user_touched = ? "
              + " WHERE user_id =  ? ");

  public void run(Connection conn, int userId, int nameSpace, String pageTitle)
      throws SQLException {

    if (userId > 0) {
      try (PreparedStatement ps = this.getPreparedStatement(conn, removeWatchList)) {
        ps.setInt(1, userId);
        ps.setInt(2, nameSpace);
        ps.setString(3, pageTitle);
        ps.executeUpdate();
      }

      if (nameSpace == 0) {
        // 일반 페이지인 경우, 해당 토론 페이지에 대한 관심 목록 라인을 제거합니다
        try (PreparedStatement ps = this.getPreparedStatement(conn, removeWatchList)) {
          ps.setInt(1, userId);
          ps.setInt(2, 1);
          ps.setString(3, pageTitle);
          ps.executeUpdate();
        }
      }

      try (PreparedStatement ps = this.getPreparedStatement(conn, setUserTouched)) {
        ps.setString(1, TimeUtil.getCurrentTimeString14());
        ps.setInt(2, userId);
        ps.executeUpdate();
      }
    }
  }
}
