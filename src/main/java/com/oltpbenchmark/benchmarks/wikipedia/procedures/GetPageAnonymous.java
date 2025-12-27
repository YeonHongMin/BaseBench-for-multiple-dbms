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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetPageAnonymous extends Procedure {
  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(GetPageAnonymous.class);

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
  // XXX 번역하기 어려움
  public SQLStmt selectIpBlocks =
      new SQLStmt(
          "SELECT * FROM " + WikipediaConstants.TABLENAME_IPBLOCKS + " WHERE ipb_address = ?");
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
          "SELECT old_text, old_flags FROM "
              + WikipediaConstants.TABLENAME_TEXT
              + " WHERE old_id = ? LIMIT 1");

  // -----------------------------------------------------------------
  // 실행
  // -----------------------------------------------------------------

  public Article run(
      Connection conn, boolean forSelect, String userIp, int pageNamespace, String pageTitle)
      throws UserAbortException, SQLException {
    int param = 1;

    int pageId;
    try (PreparedStatement st = this.getPreparedStatement(conn, selectPage)) {
      st.setInt(param++, pageNamespace);
      st.setString(param++, pageTitle);
      try (ResultSet rs = st.executeQuery()) {
        if (!rs.next()) {
          String msg =
              String.format("Invalid Page: Namespace:%d / Title:\"%s\"", pageNamespace, pageTitle);
          throw new UserAbortException(msg);
        }
        pageId = rs.getInt(1);
      }
    }

    try (PreparedStatement st = this.getPreparedStatement(conn, selectPageRestriction)) {
      st.setInt(1, pageId);
      try (ResultSet rs = st.executeQuery()) {
        while (rs.next()) {
          rs.getBytes(1);
        }
      }
    }
    // IP 주소 또는 사용자 이름으로 사용자 차단을 확인합니다

    try (PreparedStatement st = this.getPreparedStatement(conn, selectIpBlocks)) {
      st.setString(1, userIp);
      try (ResultSet rs = st.executeQuery()) {
        while (rs.next()) {
          rs.getBytes(11);
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
          String msg =
              String.format(
                  "Invalid Page: Namespace:%d / Title:\"%s\" / PageId:%d",
                  pageNamespace, pageTitle, pageId);
          throw new UserAbortException(msg);
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
          String msg =
              "No such text: "
                  + textId
                  + " for page_id:"
                  + pageId
                  + " page_namespace: "
                  + pageNamespace
                  + " page_title:"
                  + pageTitle;
          throw new UserAbortException(msg);
        }

        if (!forSelect) {
          a = new Article(userIp, pageId, rs.getString("old_text"), textId, revisionId);
        }
      }
    }
    return a;
  }
}
