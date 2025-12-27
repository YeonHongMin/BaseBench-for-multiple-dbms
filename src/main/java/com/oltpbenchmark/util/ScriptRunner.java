/*
 * 저작권 2020 OLTPBenchmark 프로젝트
 *
 * Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 * 라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 * 이 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 * 라이선스에서 허용하는 권한과 제한 사항은
 * 라이선스의 본문을 참조하십시오.
 *
 */

/*
 * iBATIS Apache 프로젝트의 com.ibatis.common.jdbc.ScriptRunner 클래스를 약간 수정한 버전입니다.
 * Resource 클래스와 일부 생성자 의존성을 제거했습니다.
 */
package com.oltpbenchmark.util;

import java.io.*;
import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 데이터베이스 스크립트를 실행하는 도구입니다. http://pastebin.com/f10584951 */
public class ScriptRunner {
  private static final Logger LOG = LoggerFactory.getLogger(ScriptRunner.class);

  private static final String DEFAULT_DELIMITER = ";";

  private final Connection connection;
  private final boolean stopOnError;
  private final boolean autoCommit;

  /** 기본 생성자 */
  public ScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError) {
    this.connection = connection;
    this.autoCommit = autoCommit;
    this.stopOnError = stopOnError;
  }

  public void runExternalScript(String path) throws IOException, SQLException {

    LOG.debug("trying to find external file by path {}", path);

    try (FileReader reader = new FileReader(path)) {

      runScript(reader);
    }
  }

  public void runScript(String path) throws IOException, SQLException {

    LOG.debug("trying to find file by resource stream path {}", path);

    try (InputStream in = this.getClass().getResourceAsStream(path);
        Reader reader = new InputStreamReader(in)) {

      runScript(reader);
    }
  }

  private void runScript(Reader reader) throws IOException, SQLException {
    boolean originalAutoCommit = connection.getAutoCommit();

    try {
      if (originalAutoCommit != this.autoCommit) {
        connection.setAutoCommit(this.autoCommit);
      }
      runScript(connection, reader);
    } finally {
      connection.setAutoCommit(originalAutoCommit);
    }
  }

  /**
   * 전달된 커넥션을 사용해 Reader에서 읽은 SQL 스크립트를 실행합니다.
   *
   * @param conn - 스크립트 실행에 사용할 커넥션
   * @param reader - 스크립트 소스
   * @throws SQLException SQL 오류 발생 시
   * @throws IOException Reader에서 읽는 중 오류가 발생한 경우
   */
  private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
    StringBuffer command = null;
    try (LineNumberReader lineReader = new LineNumberReader(reader)) {
      String line = null;
      while ((line = lineReader.readLine()) != null) {
        if (LOG.isDebugEnabled()) {
          LOG.debug(line);
        }
        if (command == null) {
          command = new StringBuffer();
        }
        String trimmedLine = line.trim();
        line = line.replaceAll("\\-\\-.*$", ""); // 행 내 주석 제거

        if (trimmedLine.startsWith("--") || trimmedLine.startsWith("//")) {
          LOG.debug(trimmedLine);
        } else if (trimmedLine.length() < 1) {
          // 아무 작업도 하지 않습니다
        } else if (trimmedLine.endsWith(getDelimiter())) {
          command.append(line, 0, line.lastIndexOf(getDelimiter()));
          command.append(" ");

          try (Statement statement = conn.createStatement()) {

            boolean hasResults = false;
            final String sql = command.toString().trim();
            if (stopOnError) {
              hasResults = statement.execute(sql);
            } else {
              try {
                statement.execute(sql);
              } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
              }
            }

            if (autoCommit && !conn.getAutoCommit()) {
              conn.commit();
            }

            // 임시 처리
            if (hasResults && !sql.toUpperCase().startsWith("CREATE")) {
              try (ResultSet rs = statement.getResultSet()) {
                if (hasResults && rs != null) {
                  ResultSetMetaData md = rs.getMetaData();
                  int cols = md.getColumnCount();
                  for (int i = 0; i < cols; i++) {
                    String name = md.getColumnLabel(i);
                    LOG.debug(name);
                  }

                  while (rs.next()) {
                    for (int i = 0; i < cols; i++) {
                      String value = rs.getString(i);
                      LOG.debug(value);
                    }
                  }
                }
              }
            }

            command = null;
          } finally {

            Thread.yield();
          }
        } else {
          command.append(line);
          command.append(" ");
        }
      }
      if (!autoCommit) {
        conn.commit();
      }
    } finally {
      if (!autoCommit) {
        conn.rollback();
      }
    }
  }

  private String getDelimiter() {
    return DEFAULT_DELIMITER;
  }
}

