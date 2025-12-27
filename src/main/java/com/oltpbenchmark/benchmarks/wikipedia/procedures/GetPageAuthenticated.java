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
import com.oltpbenchmark.benchmarks.wikipedia.util.Article;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetPageAuthenticated extends Procedure {

  // -----------------------------------------------------------------
  // 문장들
  // -----------------------------------------------------------------

  public SQLStmt selectPage =
      new SQLStmt(
          "SELECT * FROM "
              + WikipediaConstants.TABLENAME_PAGE
              + " WHERE page_namespace = ? AND page_title = ? LIMIT 1");
  public SQLStmt selectPageRestriction =
      new SQLStmt(
          "SELECT * FROM " + WikipediaConstants.TABLENAME_PAGE_RESTRICTIONS + " WHERE pr_page = ?");
  public SQLStmt selectIpBlocks =
      new SQLStmt("SELECT * FROM " + WikipediaConstants.TABLENAME_IPBLOCKS + " WHERE ipb_user = ?");
  public SQLStmt selectPageRevision =
      new SQLStmt(
          "SELECT * "
              + "  FROM "
              + WikipediaConstants.TABLENAME_PAGE
              + ", "
              + WikipediaConstants.TABLENAME_REVISION
              + " WHERE page_id = rev_page "
              + "   AND rev_page = ? "
              + "   AND page_id = ? "
              + "   AND rev_id = page_latest LIMIT 1");
  public SQLStmt selectText =
      new SQLStmt(
          "SELECT old_text,old_flags FROM "
              + WikipediaConstants.TABLENAME_TEXT
              + " WHERE old_id = ? LIMIT 1");
  public SQLStmt selectUser =
      new SQLStmt(
          "SELECT * FROM " + WikipediaConstants.TABLENAME_USER + " WHERE user_id = ? LIMIT 1");
  public SQLStmt selectGroup =
      new SQLStmt(
          "SELECT ug_group FROM "
              + WikipediaConstants.TABLENAME_USER_GROUPS
              + " WHERE ug_user = ?");

  // -----------------------------------------------------------------
  // 실행
  // -----------------------------------------------------------------

  public Article run(
      Connection conn,
      boolean forSelect,
      String userIp,
      int userId,
      int nameSpace,
      String pageTitle)
      throws SQLException {
    // =======================================================
    // 기본 데이터 로딩: txn1
    // =======================================================
    // 사용자가 로그인한 경우 사용자 데이터를 검색합니다

    // FIXME USER_ID로 너무 자주 선택함
    String userText = userIp;
    try (PreparedStatement st = this.getPreparedStatement(conn, selectUser)) {
      if (userId > 0) {
        st.setInt(1, userId);
        try (ResultSet rs = st.executeQuery()) {
          if (rs.next()) {
            userText = rs.getString("user_name");
          } else {
            throw new UserAbortException("Invalid UserId: " + userId);
          }
        }
        // 사용자가 속할 수 있는 모든 그룹을 가져옵니다 (접근 제어 정보)
        try (PreparedStatement selectGroupsStatement =
            this.getPreparedStatement(conn, selectGroup)) {
          selectGroupsStatement.setInt(1, userId);
          try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
              String userGroupName = rs.getString(1);
              assert userGroupName != null;
            }
          }
        }
      }
    }

    int pageId;
    try (PreparedStatement st = this.getPreparedStatement(conn, selectPage)) {
      st.setInt(1, nameSpace);
      st.setString(2, pageTitle);
      try (ResultSet rs = st.executeQuery()) {

        if (!rs.next()) {
          throw new UserAbortException(
              "INVALID page namespace/title:" + nameSpace + "/" + pageTitle);
        }
        pageId = rs.getInt("page_id");
      }
    }

    try (PreparedStatement st = this.getPreparedStatement(conn, selectPageRestriction)) {
      st.setInt(1, pageId);
      try (ResultSet rs = st.executeQuery()) {
        while (rs.next()) {
          byte[] pr_type = rs.getBytes(1);
          assert pr_type != null;
        }
      }
    }

    // IP 주소 또는 사용자 이름으로 사용자 차단을 확인합니다
    try (PreparedStatement st = this.getPreparedStatement(conn, selectIpBlocks)) {
      st.setInt(1, userId);
      try (ResultSet rs = st.executeQuery()) {
        while (rs.next()) {
          byte[] ipb_expiry = rs.getBytes(11);
          assert ipb_expiry != null;
        }
      }
    }

    long revisionId;
    long textId;

    try (PreparedStatement st = this.getPreparedStatement(conn, selectPageRevision)) {
      st.setInt(1, pageId);
      st.setInt(2, pageId);
      try (ResultSet rs = st.executeQuery()) {
        if (!rs.next()) {
          throw new UserAbortException(
              "no such revision: page_id:"
                  + pageId
                  + " page_namespace: "
                  + nameSpace
                  + " page_title:"
                  + pageTitle);
        }

        revisionId = rs.getLong("rev_id");
        textId = rs.getLong("rev_text_id");
      }
    }

    // 참고: 다음은 위키피디아의 변형입니다... 원본에는 old_page 열이 포함되지 않았습니다!
    // sql =
    // "SELECT old_text,old_flags FROM `text` WHERE old_id = '"+textId+"' AND old_page =
    // '"+pageId+"' LIMIT 1";

    Article a = null;
    try (PreparedStatement st = this.getPreparedStatement(conn, selectText)) {
      st.setLong(1, textId);
      try (ResultSet rs = st.executeQuery()) {
        if (!rs.next()) {
          throw new UserAbortException(
              "no such text: "
                  + textId
                  + " for page_id:"
                  + pageId
                  + " page_namespace: "
                  + nameSpace
                  + " page_title:"
                  + pageTitle);
        }

        if (!forSelect) {
          a = new Article(userText, pageId, rs.getString("old_text"), textId, revisionId);
        }
      }
    }

    return a;
  }
}
