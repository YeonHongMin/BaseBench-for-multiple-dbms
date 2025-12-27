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

package com.oltpbenchmark.benchmarks.auctionmark.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.auctionmark.AuctionMarkConstants;
import com.oltpbenchmark.benchmarks.auctionmark.util.AuctionMarkUtil;
import com.oltpbenchmark.util.SQLUtil;
import java.sql.*;

/**
 * NewComment
 *
 * @author visawee
 */
public class NewComment extends Procedure {

  // -----------------------------------------------------------------
  // 문장
  // -----------------------------------------------------------------

  public final SQLStmt getItemComments =
      new SQLStmt(
          "SELECT i_num_comments "
              + "  FROM "
              + AuctionMarkConstants.TABLENAME_ITEM
              + " WHERE i_id = ? AND i_u_id = ?");

  public final SQLStmt updateItemComments =
      new SQLStmt(
          "UPDATE "
              + AuctionMarkConstants.TABLENAME_ITEM
              + "   SET i_num_comments = i_num_comments + 1 "
              + " WHERE i_id = ? AND i_u_id = ?");

  public final SQLStmt insertItemComment =
      new SQLStmt(
          "INSERT INTO "
              + AuctionMarkConstants.TABLENAME_ITEM_COMMENT
              + "("
              + "ic_id,"
              + "ic_i_id,"
              + "ic_u_id,"
              + "ic_buyer_id,"
              + "ic_question, "
              + "ic_created,"
              + "ic_updated "
              + ") VALUES (?,?,?,?,?,?,?)");

  public final SQLStmt updateUser =
      new SQLStmt(
          "UPDATE "
              + AuctionMarkConstants.TABLENAME_USERACCT
              + " "
              + "SET u_comments = u_comments + 1, "
              + "    u_updated = ? "
              + " WHERE u_id = ?");

  // -----------------------------------------------------------------
  // RUN METHOD
  // -----------------------------------------------------------------

  public Object[] run(
      Connection conn,
      Timestamp[] benchmarkTimes,
      String item_id,
      String seller_id,
      String buyer_id,
      String question)
      throws SQLException {
    final Timestamp currentTime = AuctionMarkUtil.getProcTimestamp(benchmarkTimes);

    // comment_id 설정
    long ic_id = 0;
    try (PreparedStatement stmt =
        this.getPreparedStatement(conn, getItemComments, item_id, seller_id)) {
      try (ResultSet results = stmt.executeQuery()) {
        if (results.next()) {
          ic_id = results.getLong(1) + 1;
        }
      }
    }

    try (PreparedStatement preparedStatement =
        this.getPreparedStatement(
            conn,
            insertItemComment,
            ic_id,
            item_id,
            seller_id,
            buyer_id,
            question,
            currentTime,
            currentTime)) {
      preparedStatement.executeUpdate();
    } catch (SQLException ex) {
      if (SQLUtil.isDuplicateKeyException(ex)) {
        throw new UserAbortException(
            "item comment id "
                + ic_id
                + " already exists for item "
                + item_id
                + " and seller "
                + seller_id);
      } else {
        throw ex;
      }
    }

    try (PreparedStatement preparedStatement =
        this.getPreparedStatement(conn, updateItemComments, item_id, seller_id)) {
      preparedStatement.executeUpdate();
    }

    try (PreparedStatement preparedStatement =
        this.getPreparedStatement(conn, updateUser, currentTime, seller_id)) {
      preparedStatement.executeUpdate();
    }

    // 새로운 ic_id 반환
    return new Object[] {ic_id, item_id, seller_id};
  }
}
