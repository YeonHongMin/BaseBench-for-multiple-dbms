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
import com.oltpbenchmark.benchmarks.auctionmark.util.ItemStatus;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LoadConfig extends Procedure {

  // -----------------------------------------------------------------
  // STATEMENTS
  // -----------------------------------------------------------------

  public final SQLStmt getConfigProfile =
      new SQLStmt("SELECT * FROM " + AuctionMarkConstants.TABLENAME_CONFIG_PROFILE);

  public final SQLStmt getCategoryCounts =
      new SQLStmt(
          "SELECT i_c_id, COUNT(i_id) FROM "
              + AuctionMarkConstants.TABLENAME_ITEM
              + " GROUP BY i_c_id");

  public final SQLStmt getAttributes =
      new SQLStmt("SELECT gag_id FROM " + AuctionMarkConstants.TABLENAME_GLOBAL_ATTRIBUTE_GROUP);

  public final SQLStmt getPendingComments =
      new SQLStmt(
          "SELECT ic_id, ic_i_id, ic_u_id, ic_buyer_id "
              + "  FROM "
              + AuctionMarkConstants.TABLENAME_ITEM_COMMENT
              + " WHERE ic_response IS NULL");

  public final SQLStmt getPastItems =
      new SQLStmt(
          "SELECT i_id, i_current_price, i_end_date, i_num_bids, i_status "
              + "  FROM "
              + AuctionMarkConstants.TABLENAME_ITEM
              + ", "
              + AuctionMarkConstants.TABLENAME_CONFIG_PROFILE
              + " WHERE i_status = ? AND i_end_date <= cfp_loader_start "
              + " ORDER BY i_end_date ASC "
              + " LIMIT "
              + AuctionMarkConstants.ITEM_LOADCONFIG_LIMIT);

  public final SQLStmt getFutureItems =
      new SQLStmt(
          "SELECT i_id, i_current_price, i_end_date, i_num_bids, i_status "
              + "  FROM "
              + AuctionMarkConstants.TABLENAME_ITEM
              + ", "
              + AuctionMarkConstants.TABLENAME_CONFIG_PROFILE
              + " WHERE i_status = ? AND i_end_date > cfp_loader_start "
              + " ORDER BY i_end_date ASC "
              + " LIMIT "
              + AuctionMarkConstants.ITEM_LOADCONFIG_LIMIT);

  // -----------------------------------------------------------------
  // RUN
  // -----------------------------------------------------------------

  public Config run(Connection conn) throws SQLException {

    List<Object[]> configProfile;
    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getConfigProfile)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        configProfile = SQLUtil.toList(resultSet);
        // Oracle DB DDL contains some CLOB fields (for LoadConfig procedures).
        // These CLOB needs to be converted to String while the connection is alive.

        // This CLOB conversion for Oracle needs to be done here, otherwise the conversion will be
        // attempted
        // by SQLUtil.getString(Object) after the connection closes, which will result in
        // java.sql.SQLRecoverableException: Closed Connection.
        if (getDbType() == DatabaseType.ORACLE) {
          for (Object[] configProfileInstance : configProfile) {
            configProfileInstance[3] = SQLUtil.clobToString(configProfileInstance[3]);
          }
        }
      }
    }

    List<Object[]> categoryCounts;
    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getCategoryCounts)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        categoryCounts = SQLUtil.toList(resultSet);
      }
    }

    List<Object[]> attributes;
    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getAttributes)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        attributes = SQLUtil.toList(resultSet);
      }
    }

    List<Object[]> pendingComments;
    try (PreparedStatement preparedStatement =
        this.getPreparedStatement(conn, getPendingComments)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        pendingComments = SQLUtil.toList(resultSet);
      }
    }

    List<Object[]> openItems;
    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getFutureItems)) {
      preparedStatement.setLong(1, ItemStatus.OPEN.ordinal());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        openItems = SQLUtil.toList(resultSet);
      }
    }

    List<Object[]> waiting;
    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getPastItems)) {
      preparedStatement.setLong(1, ItemStatus.WAITING_FOR_PURCHASE.ordinal());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        waiting = SQLUtil.toList(resultSet);
      }
    }

    List<Object[]> closedItems;
    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getPastItems)) {
      preparedStatement.setLong(1, ItemStatus.CLOSED.ordinal());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        closedItems = SQLUtil.toList(resultSet);
      }
    }

    return new Config(
        configProfile,
        categoryCounts,
        attributes,
        pendingComments,
        openItems,
        waiting,
        closedItems);
  }
}
