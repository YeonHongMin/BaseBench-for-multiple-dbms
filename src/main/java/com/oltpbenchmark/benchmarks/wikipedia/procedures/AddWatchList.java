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
import com.oltpbenchmark.util.SQLUtil;
import com.oltpbenchmark.util.TimeUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddWatchList extends Procedure {
  private static final Logger LOG = LoggerFactory.getLogger(AddWatchList.class);

  // -----------------------------------------------------------------
  // 문장들
  // -----------------------------------------------------------------

  public SQLStmt insertWatchList =
      new SQLStmt(
          "INSERT INTO "
              + WikipediaConstants.TABLENAME_WATCHLIST
              + " ("
              + "wl_user, wl_namespace, wl_title, wl_notificationtimestamp"
              + ") VALUES ("
              + "?,?,?,NULL"
              + ")");

  public SQLStmt setUserTouched =
      new SQLStmt(
          "UPDATE "
              + WikipediaConstants.TABLENAME_USER
              + "   SET user_touched = ? WHERE user_id = ?");

  // -----------------------------------------------------------------
  // 실행
  // -----------------------------------------------------------------

  public void run(Connection conn, int userId, int nameSpace, String pageTitle)
      throws SQLException {
    if (userId > 0) {

      try (PreparedStatement ps = this.getPreparedStatement(conn, insertWatchList)) {
        ps.setInt(1, userId);
        ps.setInt(2, nameSpace);
        ps.setString(3, pageTitle);
        ps.executeUpdate();
      } catch (SQLException e) {
        if (SQLUtil.isDuplicateKeyException(e)) {
          LOG.debug(e.toString());
          throw new UserAbortException(
              "User " + userId + " already has " + pageTitle + " in their watchlist");
        } else {
          throw e;
        }
      }

      if (nameSpace == 0) {

        // 일반 페이지인 경우, 해당 토론 페이지에 대한 관심 목록 라인도 추가합니다
        try (PreparedStatement ps = this.getPreparedStatement(conn, insertWatchList)) {
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
