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

package com.oltpbenchmark.benchmarks.twitter.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.twitter.TwitterConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetFollowers extends Procedure {

  public final SQLStmt getFollowers =
      new SQLStmt(
          "SELECT f2 FROM "
              + TwitterConstants.TABLENAME_FOLLOWERS
              + " WHERE f1 = ? LIMIT "
              + TwitterConstants.LIMIT_FOLLOWERS);

  /** 참고: ??는 반복되는 ?'s 문자열로 대체됩니다 */
  public final SQLStmt getFollowerNames =
      new SQLStmt(
          "SELECT uid, name FROM " + TwitterConstants.TABLENAME_USER + " WHERE uid IN (??)",
          TwitterConstants.LIMIT_FOLLOWERS);

  public void run(Connection conn, long uid) throws SQLException {
    try (PreparedStatement stmt = this.getPreparedStatement(conn, getFollowers)) {
      stmt.setLong(1, uid);
      try (ResultSet rs = stmt.executeQuery()) {

        try (PreparedStatement getFollowerNamesstmt =
            this.getPreparedStatement(conn, getFollowerNames)) {
          int ctr = 0;
          long last = -1;
          while (rs.next() && ctr++ < TwitterConstants.LIMIT_FOLLOWERS) {
            last = rs.getLong(1);
            getFollowerNamesstmt.setLong(ctr, last);
          }
          if (ctr > 0) {
            while (ctr++ < TwitterConstants.LIMIT_FOLLOWERS) {
              getFollowerNamesstmt.setLong(ctr, last);
            }
            try (ResultSet getFollowerNamesrs = getFollowerNamesstmt.executeQuery()) {
              assert getFollowerNamesrs != null;
            }
          }
        }
      }
    }
    // LOG.warn("사용자에 대한 팔로워가 없습니다: "+uid); //... 그래서 뭐?
  }
}
