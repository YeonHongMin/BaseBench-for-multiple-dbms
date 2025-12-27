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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdatePage extends Procedure {
  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(UpdatePage.class);

  // -----------------------------------------------------------------
  // 문장들
  // -----------------------------------------------------------------
  public SQLStmt insertText =
      new SQLStmt(
          "INSERT INTO "
              + WikipediaConstants.TABLENAME_TEXT
              + " ("
              + "old_page,old_text,old_flags"
              + ") VALUES ("
              + "?,?,?"
              + ")");
  public SQLStmt insertRevision =
      new SQLStmt(
          "INSERT INTO "
              + WikipediaConstants.TABLENAME_REVISION
              + " ("
              + "rev_page, "
              + "rev_text_id, "
              + "rev_comment, "
              + "rev_minor_edit, "
              + "rev_user, "
              + "rev_user_text, "
              + "rev_timestamp, "
              + "rev_deleted, "
              + "rev_len, "
              + "rev_parent_id"
              + ") VALUES ("
              + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
              + ")");
  public SQLStmt updatePage =
      new SQLStmt(
          "UPDATE "
              + WikipediaConstants.TABLENAME_PAGE
              + "   SET page_latest = ?, page_touched = ?, page_is_new = 0, page_is_redirect = 0, page_len = ? "
              + " WHERE page_id = ?");
  public SQLStmt insertRecentChanges =
      new SQLStmt(
          "INSERT INTO "
              + WikipediaConstants.TABLENAME_RECENTCHANGES
              + " ("
              + "rc_timestamp, "
              + "rc_cur_time, "
              + "rc_namespace, "
              + "rc_title, "
              + "rc_type, "
              + "rc_minor, "
              + "rc_cur_id, "
              + "rc_user, "
              + "rc_user_text, "
              + "rc_comment, "
              + "rc_this_oldid, "
              + "rc_last_oldid, "
              + "rc_bot, "
              + "rc_moved_to_ns, "
              + "rc_moved_to_title, "
              + "rc_ip, "
              + "rc_old_len, "
              + "rc_new_len "
              + ") VALUES ("
              + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
              + ")");
  public SQLStmt selectWatchList =
      new SQLStmt(
          "SELECT wl_user FROM "
              + WikipediaConstants.TABLENAME_WATCHLIST
              + " WHERE wl_title = ?"
              + "   AND wl_namespace = ?"
              + "   AND wl_user != ?"
              + "   AND wl_notificationtimestamp IS NULL");
  public SQLStmt updateWatchList =
      new SQLStmt(
          "UPDATE "
              + WikipediaConstants.TABLENAME_WATCHLIST
              + "   SET wl_notificationtimestamp = ? "
              + " WHERE wl_title = ?"
              + "   AND wl_namespace = ?"
              + "   AND wl_user = ?");
  public SQLStmt selectUser =
      new SQLStmt("SELECT * FROM " + WikipediaConstants.TABLENAME_USER + " WHERE user_id = ?");
  public SQLStmt insertLogging =
      new SQLStmt(
          "INSERT INTO "
              + WikipediaConstants.TABLENAME_LOGGING
              + " ("
              + "log_type, log_action, log_timestamp, log_user, log_user_text, "
              + "log_namespace, log_title, log_page, log_comment, log_params"
              + ") VALUES ("
              + "'patrol','patrol',?,?,?,?,?,?,'',?"
              + ")");
  public SQLStmt updateUserEdit =
      new SQLStmt(
          "UPDATE "
              + WikipediaConstants.TABLENAME_USER
              + "   SET user_editcount=user_editcount+1"
              + " WHERE user_id = ?");
  public SQLStmt updateUserTouched =
      new SQLStmt(
          "UPDATE "
              + WikipediaConstants.TABLENAME_USER
              + "   SET user_touched = ?"
              + " WHERE user_id = ?");

  // -----------------------------------------------------------------
  // 실행
  // -----------------------------------------------------------------

  public void run(
      Connection conn,
      long textId,
      int pageId,
      String pageTitle,
      String pageText,
      int pageNamespace,
      int userId,
      String userIp,
      String userText,
      long revisionId,
      String revComment,
      int revMinorEdit)
      throws SQLException {

    final String timestamp = TimeUtil.getCurrentTimeString14();

    // 새 텍스트 삽입
    long nextTextId;
    long nextRevId;

    try (PreparedStatement ps =
        this.getPreparedStatementReturnKeys(conn, insertText, new int[] {1})) {
      int param = 1;
      ps.setInt(param++, pageId);
      ps.setString(param++, pageText);
      ps.setString(param++, "utf-8"); // 이것은 오류입니다
      execute(conn, ps);

      try (ResultSet rs = ps.getGeneratedKeys()) {
        rs.next();
        nextTextId = rs.getLong(1);
      }
    }

    // 새 리비전 삽입

    try (PreparedStatement ps =
        this.getPreparedStatementReturnKeys(conn, insertRevision, new int[] {1})) {
      int param = 1;
      ps.setInt(param++, pageId); // rev_page
      ps.setLong(param++, nextTextId); // rev_text_id
      ps.setString(
          param++, revComment.substring(0, Math.min(revComment.length(), 255 - 1))); // rev_comment
      ps.setInt(param++, revMinorEdit); // rev_minor_edit // this is an error
      ps.setInt(param++, userId); // rev_user
      ps.setString(param++, userText); // rev_user_text
      ps.setString(param++, timestamp); // rev_timestamp
      ps.setInt(param++, 0); // rev_deleted // this is an error
      ps.setInt(param++, pageText.length()); // rev_len
      ps.setLong(param++, revisionId); // rev_parent_id // this is an error
      execute(conn, ps);

      try (ResultSet rs = ps.getGeneratedKeys()) {
        rs.next();
        nextRevId = rs.getLong(1);
      }
    }

    // 쿼리에서 AND page_latest = "+a.revisionId+"를 제거합니다, 왜냐하면
    // 데이터에 문제가 생기기 때문이고, page_id는 PK이므로
    try (PreparedStatement ps = this.getPreparedStatement(conn, updatePage)) {
      int param = 1;
      ps.setLong(param++, nextRevId);
      ps.setString(param++, timestamp);
      ps.setInt(param++, pageText.length());
      ps.setInt(param++, pageId);
      execute(conn, ps);
    }

    try (PreparedStatement ps = this.getPreparedStatement(conn, insertRecentChanges)) {
      int param = 1;
      ps.setString(param++, timestamp); // rc_timestamp
      ps.setString(param++, timestamp); // rc_cur_time
      ps.setInt(param++, pageNamespace); // rc_namespace
      ps.setString(param++, pageTitle); // rc_title
      ps.setInt(param++, 0); // rc_type
      ps.setInt(param++, 0); // rc_minor
      ps.setInt(param++, pageId); // rc_cur_id
      ps.setInt(param++, userId); // rc_user
      ps.setString(param++, userText); // rc_user_text
      ps.setString(param++, revComment); // rc_comment
      ps.setLong(param++, nextTextId); // rc_this_oldid
      ps.setLong(param++, textId); // rc_last_oldid
      ps.setInt(param++, 0); // rc_bot
      ps.setInt(param++, 0); // rc_moved_to_ns
      ps.setString(param++, ""); // rc_moved_to_title
      ps.setString(param++, userIp); // rc_ip
      ps.setInt(param++, pageText.length()); // rc_old_len
      ps.setInt(param++, pageText.length()); // rc_new_len
      execute(conn, ps);
    }

    // 제거됨
    // sql="INSERT INTO `cu_changes` () VALUES ();";
    // st.addBatch(sql);

    // 관심 있는 사용자 선택
    ArrayList<Integer> wlUser = new ArrayList<>();
    try (PreparedStatement ps = this.getPreparedStatement(conn, selectWatchList)) {
      int param = 1;
      ps.setString(param++, pageTitle);
      ps.setInt(param++, pageNamespace);
      ps.setInt(param++, userId);
      try (ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
          wlUser.add(rs.getInt(1));
        }
      }
    }

    // =====================================================================
    // 관심 목록 업데이트: txn3 (항상 그런 것은 아니며, 누군가 페이지를 보고 있는 경우에만,
    // txn2의 일부일 수 있음)
    // =====================================================================
    if (!wlUser.isEmpty()) {

      try (PreparedStatement ps = this.getPreparedStatement(conn, updateWatchList)) {
        int param = 1;
        ps.setString(param++, timestamp);
        ps.setString(param++, pageTitle);
        ps.setInt(param++, pageNamespace);
        for (Integer otherUserId : wlUser) {
          ps.setInt(param, otherUserId);
          ps.addBatch();
        }
        executeBatch(conn, ps);
      }

      // =====================================================================
      // 사용자 및 로깅 정보 업데이트: txn4 (아직 txn2의 일부일 수 있음)
      // =====================================================================

      // 이것은 페이지가 관심 목록에 있는 경우에만 한 번 실행되는 것 같습니다

      try (PreparedStatement ps = this.getPreparedStatement(conn, selectUser)) {
        int param = 1;
        for (Integer otherUserId : wlUser) {
          ps.setInt(param, otherUserId);
          try (ResultSet rs = ps.executeQuery()) {
            rs.next();
          }
        }
      }
    }

    // 이것은 항상 실행되며, 때로는 별도의 트랜잭션으로, 때로는 이전 것과 함께 실행됩니다

    try (PreparedStatement ps = this.getPreparedStatement(conn, insertLogging)) {
      int param = 1;
      ps.setString(param++, timestamp);
      ps.setInt(param++, userId);
      ps.setString(param++, pageTitle);
      ps.setInt(param++, pageNamespace);
      ps.setString(param++, userText);
      ps.setInt(param++, pageId);
      ps.setString(param++, String.format("%d\n%d\n%d", nextRevId, revisionId, 1));
      execute(conn, ps);
    }

    try (PreparedStatement ps = this.getPreparedStatement(conn, updateUserEdit)) {
      int param = 1;
      ps.setInt(param++, userId);
      execute(conn, ps);
    }

    try (PreparedStatement ps = this.getPreparedStatement(conn, updateUserTouched)) {
      int param = 1;
      ps.setString(param++, timestamp);
      ps.setInt(param++, userId);
      execute(conn, ps);
    }
  }

  public void execute(Connection conn, PreparedStatement p) throws SQLException {

    p.execute();
  }

  public void executeBatch(Connection conn, PreparedStatement p) throws SQLException {

    p.executeBatch();
  }
}
