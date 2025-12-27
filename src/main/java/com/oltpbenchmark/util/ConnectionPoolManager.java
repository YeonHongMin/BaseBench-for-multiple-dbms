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

package com.oltpbenchmark.util;

import com.oltpbenchmark.WorkloadConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HikariCP를 사용한 커넥션 풀 관리자를 제공합니다.
 * 데이터베이스 연결에 효율적인 풀링을 제공합니다.
 */
public class ConnectionPoolManager {
  private static final Logger LOG = LoggerFactory.getLogger(ConnectionPoolManager.class);

  private final HikariDataSource dataSource;
  private final WorkloadConfiguration workConf;

  /**
   * 주어진 구성으로 ConnectionPoolManager를 생성합니다.
   *
   * @param workConf 데이터베이스 연결 설정을 포함하는 워크로드 구성
   */
  public ConnectionPoolManager(WorkloadConfiguration workConf) {
    this.workConf = workConf;
    this.dataSource = createDataSource();
    LOG.info(
        "ConnectionPoolManager initialized with pool size: min={}, max={}",
        workConf.getPoolMinSize(),
        workConf.getPoolMaxSize());
  }

  /**
   * HikariCP 데이터 소스를 생성하고 설정합니다.
   *
   * @return 구성된 HikariDataSource
   */
  private HikariDataSource createDataSource() {
    HikariConfig config = new HikariConfig();

    // 기본 연결 설정
    config.setJdbcUrl(workConf.getUrl());
    if (workConf.getUsername() != null && !workConf.getUsername().isEmpty()) {
      config.setUsername(workConf.getUsername());
      config.setPassword(workConf.getPassword());
    }

    // 드라이버 클래스(선택 사항; HikariCP가 자동으로 감지할 수 있음)
    if (workConf.getDriverClass() != null && !workConf.getDriverClass().isEmpty()) {
      config.setDriverClassName(workConf.getDriverClass());
    }

    // 풀 크기 구성
    config.setMinimumIdle(workConf.getPoolMinSize());
    config.setMaximumPoolSize(workConf.getPoolMaxSize());

    // 연결 타임아웃 설정
    config.setConnectionTimeout(workConf.getPoolConnectionTimeout());
    config.setIdleTimeout(workConf.getPoolIdleTimeout());
    config.setMaxLifetime(workConf.getPoolMaxLifetime());

    // 모니터링 용도 풀 이름 설정
    config.setPoolName("BenchBase-HikariPool");

    // 커넥션 검증 활성화
    config.setValidationTimeout(5000); // 5초

    // 벤치마크 트랜잭션에서는 자동 커밋을 비활성화합니다.
    config.setAutoCommit(false);

    // 트랜잭션 격리 수준 설정
    config.setTransactionIsolation(getIsolationLevelName(workConf.getIsolationMode()));

    // 추가 성능 최적화 설정
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

    LOG.info(
        "Creating HikariCP DataSource: url={}, poolSize={}-{}",
        workConf.getUrl(),
        workConf.getPoolMinSize(),
        workConf.getPoolMaxSize());

    return new HikariDataSource(config);
  }

  /**
   * 풀에서 커넥션을 가져옵니다.
   *
   * @return 풀에서 가져온 데이터베이스 커넥션
   * @throws SQLException 커넥션을 확보할 수 없는 경우
   */
  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  /**
   * 현재 풀에서 활성화된 커넥션 수를 반환합니다.
   *
   * @return 활성 커넥션 수
   */
  public int getActiveConnections() {
    return dataSource.getHikariPoolMXBean().getActiveConnections();
  }

  /**
   * 현재 풀에 대기 중인 유휴 커넥션 수를 반환합니다.
   *
   * @return 유휴 커넥션 수
   */
  public int getIdleConnections() {
    return dataSource.getHikariPoolMXBean().getIdleConnections();
  }

  /**
   * 풀에 존재하는 전체 커넥션 수를 반환합니다.
   *
   * @return 전체 커넥션 수
   */
  public int getTotalConnections() {
    return dataSource.getHikariPoolMXBean().getTotalConnections();
  }

  /**
   * 커넥션을 기다리는 스레드 수를 반환합니다.
   *
   * @return 커넥션을 기다리는 스레드 수
   */
  public int getThreadsAwaitingConnection() {
    return dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection();
  }

  /**
   * 모든 커넥션을 닫고 풀을 종료합니다.
   */
  public void close() {
    if (dataSource != null && !dataSource.isClosed()) {
      LOG.info(
          "Closing ConnectionPoolManager. Stats: active={}, idle={}, total={}, waiting={}",
          getActiveConnections(),
          getIdleConnections(),
          getTotalConnections(),
          getThreadsAwaitingConnection());
      dataSource.close();
      LOG.info("ConnectionPoolManager closed successfully.");
    }
  }

  /**
   * 풀 종료 여부를 확인합니다.
   *
   * @return 풀이 종료된 경우 true
   */
  public boolean isClosed() {
    return dataSource == null || dataSource.isClosed();
  }

  /**
   * JDBC 격리 수준 상수를 HikariCP 설정에 사용할 명칭으로 변환합니다.
   *
   * @param isolationLevel JDBC 격리 수준 상수
   * @return HikariCP 설정에 사용할 격리 수준 명칭
   */
  private String getIsolationLevelName(int isolationLevel) {
    return switch (isolationLevel) {
      case Connection.TRANSACTION_NONE -> "TRANSACTION_NONE";
      case Connection.TRANSACTION_READ_UNCOMMITTED -> "TRANSACTION_READ_UNCOMMITTED";
      case Connection.TRANSACTION_READ_COMMITTED -> "TRANSACTION_READ_COMMITTED";
      case Connection.TRANSACTION_REPEATABLE_READ -> "TRANSACTION_REPEATABLE_READ";
      case Connection.TRANSACTION_SERIALIZABLE -> "TRANSACTION_SERIALIZABLE";
      default -> "TRANSACTION_SERIALIZABLE";
    };
  }

  /**
   * 풀 통계를 포맷된 문자열로 반환합니다.
   *
   * @return 풀 통계 문자열
   */
  public String getPoolStats() {
    if (dataSource == null || dataSource.isClosed()) {
      return "Pool is closed";
    }
    return String.format(
        "Pool Stats: active=%d, idle=%d, total=%d, waiting=%d",
        getActiveConnections(),
        getIdleConnections(),
        getTotalConnections(),
        getThreadsAwaitingConnection());
  }
}

