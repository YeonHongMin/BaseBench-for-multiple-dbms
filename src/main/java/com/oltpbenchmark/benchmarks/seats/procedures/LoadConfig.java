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

package com.oltpbenchmark.benchmarks.seats.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.seats.SEATSConstants;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LoadConfig extends Procedure {

  // -----------------------------------------------------------------
  // 문장
  // -----------------------------------------------------------------

  public final SQLStmt getConfigProfile =
      new SQLStmt("SELECT * FROM " + SEATSConstants.TABLENAME_CONFIG_PROFILE);

  public final SQLStmt getConfigHistogram =
      new SQLStmt("SELECT * FROM " + SEATSConstants.TABLENAME_CONFIG_HISTOGRAMS);

  public final SQLStmt getCountryCodes =
      new SQLStmt("SELECT CO_ID, CO_CODE_3 FROM " + SEATSConstants.TABLENAME_COUNTRY);

  public final SQLStmt getAirportCodes =
      new SQLStmt("SELECT AP_ID, AP_CODE FROM " + SEATSConstants.TABLENAME_AIRPORT);

  public final SQLStmt getAirlineCodes =
      new SQLStmt(
          "SELECT AL_ID, AL_IATA_CODE FROM "
              + SEATSConstants.TABLENAME_AIRLINE
              + " WHERE AL_IATA_CODE != ''");

  public final SQLStmt getFlights =
      new SQLStmt(
          "SELECT f_id FROM "
              + SEATSConstants.TABLENAME_FLIGHT
              + " ORDER BY F_DEPART_TIME DESC "
              + " LIMIT "
              + SEATSConstants.CACHE_LIMIT_FLIGHT_IDS);

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
            configProfileInstance[1] = SQLUtil.clobToString(configProfileInstance[1]);
          }
        }
      }
    }

    List<Object[]> histogram;
    try (PreparedStatement preparedStatement =
        this.getPreparedStatement(conn, getConfigHistogram)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        histogram = SQLUtil.toList(resultSet);
        // Oracle DB DDL contains some CLOB fields (for LoadConfig procedures).
        // These CLOB needs to be converted to String while the connection is alive.

        // This CLOB conversion for Oracle needs to be done here, otherwise the conversion will be
        // attempted
        // by SQLUtil.getString(Object) after the connection closes, which will result in
        // java.sql.SQLRecoverableException: Closed Connection.
        if (getDbType() == DatabaseType.ORACLE) {
          for (Object[] histogramInstance : histogram) {
            histogramInstance[1] = SQLUtil.clobToString(histogramInstance[1]);
          }
        }
      }
    }

    List<Object[]> countryCodes;
    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getCountryCodes)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        countryCodes = SQLUtil.toList(resultSet);
      }
    }

    List<Object[]> airportCodes;
    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getAirportCodes)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        airportCodes = SQLUtil.toList(resultSet);
      }
    }

    List<Object[]> airlineCodes;
    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getAirlineCodes)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        airlineCodes = SQLUtil.toList(resultSet);
      }
    }

    List<Object[]> flights;
    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getFlights)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        flights = SQLUtil.toList(resultSet);
      }
    }

    return new Config(configProfile, histogram, countryCodes, airportCodes, airlineCodes, flights);
  }
}
