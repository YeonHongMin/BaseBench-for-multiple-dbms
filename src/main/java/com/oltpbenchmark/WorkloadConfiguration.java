/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
=======
 * Apache License, Version 2.0 (이하 "라이선스")에 따라 라이선스됩니다.
 * 라이선스를 준수하지 않는 한 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 얻을 수 있습니다:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * 적용 가능한 법률에 의해 요구되거나 서면으로 합의하지 않는 한,
 * 라이선스에 따라 배포된 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 종류의 보증이나 조건도 없습니다.
 * 권한 및 제한에 대한 자세한 내용은 라이선스를 참조하세요.
>>>>>>> master
 *
 */

package com.oltpbenchmark;

import com.oltpbenchmark.api.TransactionTypes;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.FileUtil;
import com.oltpbenchmark.util.ThreadUtil;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration2.XMLConfiguration;

public class WorkloadConfiguration {

  private final List<Phase> phases = new ArrayList<>();
  private DatabaseType databaseType;
  private String benchmarkName;
  private String url;
  private String username;
  private String password;
  private String driverClass;
  private int batchSize;
  private String sessionSetupFile;
  private int maxRetries;
  private int randomSeed = -1;
  private double scaleFactor = 1.0;
  private double selectivity = -1.0;
  private int terminals;
  private int loaderThreads = ThreadUtil.availableProcessors();
  private XMLConfiguration xmlConfig = null;
  private WorkloadState workloadState;
  private TransactionTypes transTypes = null;
  private int isolationMode = Connection.TRANSACTION_SERIALIZABLE;
  private String dataDir = null;
  private String ddlPath = null;
  private boolean advancedMonitoringEnabled = false;

<<<<<<< HEAD
  /**
   * If true, establish a new connection for each transaction, otherwise use one persistent
   * connection per client session. This is useful to measure the connection overhead.
   */
  private boolean newConnectionPerTxn = false;

  /**
   * If true, attempt to catch connection closed exceptions and reconnect. This allows the benchmark
   * to recover like a typical application would in the case of a replicated cluster
   * primary-secondary failover.
=======
  // 연결 풀 설정
  private boolean connectionPoolEnabled = false;
  private boolean dynamicPoolSizingEnabled = true; // 터미널 수 기반 동적 풀 크기 조정
  private int poolMinSize = 5;
  private int poolMaxSize = 20;
  private long poolConnectionTimeout = 30000; // 30초
  private long poolIdleTimeout = 600000; // 10분
  private long poolMaxLifetime = 1800000; // 30분

  /** true이면 각 트랜잭션마다 새 연결을 설정하고, 그렇지 않으면 클라이언트 세션당 하나의 지속적인 연결을 사용합니다. 연결 오버헤드를 측정하는 데 유용합니다. */
  private boolean newConnectionPerTxn = false;

  /**
   * true이면 연결 종료 예외를 포착하고 재연결을 시도합니다. 이를 통해 벤치마크가 복제된 클러스터의 주-보조 장애 조치(failover) 상황에서 일반 애플리케이션처럼
   * 복구할 수 있습니다.
>>>>>>> master
   */
  private boolean reconnectOnConnectionFailure = false;

  public String getBenchmarkName() {
    return benchmarkName;
  }

  public void setBenchmarkName(String benchmarkName) {
    this.benchmarkName = benchmarkName;
  }

  public WorkloadState getWorkloadState() {
    return workloadState;
  }

  public DatabaseType getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(DatabaseType databaseType) {
    this.databaseType = databaseType;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDriverClass() {
    return driverClass;
  }

  public void setDriverClass(String driverClass) {
    this.driverClass = driverClass;
  }

  public int getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public String getSessionSetupFile() {
    return sessionSetupFile;
  }

  public void setSessionSetupFile(String sessionSetupFile) throws FileNotFoundException {
    this.sessionSetupFile = FileUtil.checkPath(sessionSetupFile, "sessionsetupfile");
  }

  public int getMaxRetries() {
    return maxRetries;
  }

  public void setMaxRetries(int maxRetries) {
    this.maxRetries = maxRetries;
  }

  public void setAdvancedMonitoringEnabled(boolean advancedMonitoringEnabled) {
    this.advancedMonitoringEnabled = true;
  }

  public boolean getAdvancedMonitoringEnabled() {
    return this.advancedMonitoringEnabled;
  }

  /**
<<<<<<< HEAD
   * @return @see newConnectionPerTxn member docs for behavior.
=======
   * @return 동작에 대한 자세한 내용은 newConnectionPerTxn 멤버 문서를 참조하세요.
>>>>>>> master
   */
  public boolean getNewConnectionPerTxn() {
    return newConnectionPerTxn;
  }

  /**
<<<<<<< HEAD
   * Used by the configuration loader at startup. Changing it any other time is probably
   * dangeroues. @see newConnectionPerTxn member docs for behavior.
   *
   * @param newConnectionPerTxn
=======
   * 시작 시 설정 로더에 의해 사용됩니다. 다른 시간에 변경하는 것은 위험할 수 있습니다. 동작에 대한 자세한 내용은 newConnectionPerTxn 멤버 문서를
   * 참조하세요.
   *
   * @param newConnectionPerTxn 트랜잭션당 새 연결 여부
>>>>>>> master
   */
  public void setNewConnectionPerTxn(boolean newConnectionPerTxn) {
    this.newConnectionPerTxn = newConnectionPerTxn;
  }

  /**
<<<<<<< HEAD
   * @return @see reconnectOnConnectionFailure member docs for behavior.
=======
   * @return 동작에 대한 자세한 내용은 reconnectOnConnectionFailure 멤버 문서를 참조하세요.
>>>>>>> master
   */
  public boolean getReconnectOnConnectionFailure() {
    return reconnectOnConnectionFailure;
  }

  /**
<<<<<<< HEAD
   * Used by the configuration loader at startup. Changing it any other time is probably
   * dangeroues. @see reconnectOnConnectionFailure member docs for behavior.
   *
   * @param reconnectOnConnectionFailure
=======
   * 시작 시 설정 로더에 의해 사용됩니다. 다른 시간에 변경하는 것은 위험할 수 있습니다. 동작에 대한 자세한 내용은 reconnectOnConnectionFailure 멤버
   * 문서를 참조하세요.
   *
   * @param reconnectOnConnectionFailure 연결 실패 시 재연결 여부
>>>>>>> master
   */
  public void setReconnectOnConnectionFailure(boolean reconnectOnConnectionFailure) {
    this.reconnectOnConnectionFailure = reconnectOnConnectionFailure;
  }

<<<<<<< HEAD
  /** Initiate a new benchmark and workload state */
=======
  /** 새로운 벤치마크 및 워크로드 상태를 시작합니다 */
>>>>>>> master
  public void initializeState(BenchmarkState benchmarkState) {
    this.workloadState = new WorkloadState(benchmarkState, phases, terminals);
  }

  public void addPhase(
      int id,
      int time,
      int warmup,
      double rate,
      List<Double> weights,
      boolean rateLimited,
      boolean disabled,
      boolean serial,
      boolean timed,
      int active_terminals,
      Phase.Arrival arrival) {
    phases.add(
        new Phase(
            benchmarkName,
            id,
            time,
            warmup,
            rate,
            weights,
            rateLimited,
            disabled,
            serial,
            timed,
            active_terminals,
            arrival));
  }

  /**
<<<<<<< HEAD
   * The number of loader threads that the framework is allowed to use.
   *
   * @return
=======
   * 프레임워크가 사용할 수 있는 로더 스레드 수입니다.
   *
   * @return 로더 스레드 수
>>>>>>> master
   */
  public int getLoaderThreads() {
    return this.loaderThreads;
  }

  public void setLoaderThreads(int loaderThreads) {
    this.loaderThreads = loaderThreads;
  }

  public double getSelectivity() {
    return this.selectivity;
  }

  public void setSelectivity(double selectivity) {
    this.selectivity = selectivity;
  }

  /**
<<<<<<< HEAD
   * The random seed for this benchmark
   *
   * @return
=======
   * 이 벤치마크의 랜덤 시드입니다
   *
   * @return 랜덤 시드
>>>>>>> master
   */
  public int getRandomSeed() {
    return this.randomSeed;
  }

  /**
<<<<<<< HEAD
   * Set the random seed for this benchmark
   *
   * @param randomSeed
=======
   * 이 벤치마크의 랜덤 시드를 설정합니다
   *
   * @param randomSeed 랜덤 시드
>>>>>>> master
   */
  public void setRandomSeed(int randomSeed) {
    this.randomSeed = randomSeed;
  }

  /**
<<<<<<< HEAD
   * Return the scale factor of the database size
   *
   * @return
=======
   * 데이터베이스 크기의 스케일 팩터를 반환합니다
   *
   * @return 스케일 팩터
>>>>>>> master
   */
  public double getScaleFactor() {
    return this.scaleFactor;
  }

  /**
<<<<<<< HEAD
   * Set the scale factor for the database A value of 1 means the default size. A value greater than
   * 1 means the database is larger A value less than 1 means the database is smaller
   *
   * @param scaleFactor
=======
   * 데이터베이스의 스케일 팩터를 설정합니다. 값 1은 기본 크기를 의미합니다. 1보다 큰 값은 데이터베이스가 더 크다는 것을 의미하고, 1보다 작은 값은 데이터베이스가 더
   * 작다는 것을 의미합니다
   *
   * @param scaleFactor 스케일 팩터
>>>>>>> master
   */
  public void setScaleFactor(double scaleFactor) {
    this.scaleFactor = scaleFactor;
  }

  /**
<<<<<<< HEAD
   * Return the number of phases specified in the config file
   *
   * @return
=======
   * 설정 파일에 지정된 단계 수를 반환합니다
   *
   * @return 단계 수
>>>>>>> master
   */
  public int getNumberOfPhases() {
    return phases.size();
  }

<<<<<<< HEAD
  /**
   * Return the directory in which we can find the data files (for example, CSV files) for loading
   * the database.
   */
=======
  /** 데이터베이스를 로드하기 위한 데이터 파일(예: CSV 파일)을 찾을 수 있는 디렉토리를 반환합니다. */
>>>>>>> master
  public String getDataDir() {
    return this.dataDir;
  }

<<<<<<< HEAD
  /**
   * Set the directory in which we can find the data files (for example, CSV files) for loading the
   * database.
   */
=======
  /** 데이터베이스를 로드하기 위한 데이터 파일(예: CSV 파일)을 찾을 수 있는 디렉토리를 설정합니다. */
>>>>>>> master
  public void setDataDir(String dir) {
    this.dataDir = dir;
  }

<<<<<<< HEAD
  /** Return the path in which we can find the ddl script. */
=======
  /** DDL 스크립트를 찾을 수 있는 경로를 반환합니다. */
>>>>>>> master
  public String getDDLPath() {
    return this.ddlPath;
  }

<<<<<<< HEAD
  /** Set the path in which we can find the ddl script. */
=======
  /** DDL 스크립트를 찾을 수 있는 경로를 설정합니다. */
>>>>>>> master
  public void setDDLPath(String ddlPath) throws FileNotFoundException {
    this.ddlPath = FileUtil.checkPath(ddlPath, "ddlpath");
  }

<<<<<<< HEAD
  /** A utility method that init the phaseIterator and dialectMap */
=======
  /** phaseIterator와 dialectMap을 초기화하는 유틸리티 메서드입니다 */
>>>>>>> master
  public void init() {
    try {
      Class.forName(this.driverClass);
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("Failed to initialize JDBC driver '" + this.driverClass + "'", ex);
    }
  }

  public int getTerminals() {
    return terminals;
  }

  public void setTerminals(int terminals) {
    this.terminals = terminals;
  }

  public TransactionTypes getTransTypes() {
    return transTypes;
  }

  public void setTransTypes(TransactionTypes transTypes) {
    this.transTypes = transTypes;
  }

  public List<Phase> getPhases() {
    return phases;
  }

  public XMLConfiguration getXmlConfig() {
    return xmlConfig;
  }

  public void setXmlConfig(XMLConfiguration xmlConfig) {
    this.xmlConfig = xmlConfig;
  }

  public int getIsolationMode() {
    return isolationMode;
  }

  public void setIsolationMode(String mode) {
    switch (mode) {
      case "TRANSACTION_SERIALIZABLE":
        this.isolationMode = Connection.TRANSACTION_SERIALIZABLE;
        break;
      case "TRANSACTION_READ_COMMITTED":
        this.isolationMode = Connection.TRANSACTION_READ_COMMITTED;
        break;
      case "TRANSACTION_REPEATABLE_READ":
        this.isolationMode = Connection.TRANSACTION_REPEATABLE_READ;
        break;
      case "TRANSACTION_READ_UNCOMMITTED":
        this.isolationMode = Connection.TRANSACTION_READ_UNCOMMITTED;
        break;
      case "TRANSACTION_NONE":
        this.isolationMode = Connection.TRANSACTION_NONE;
    }
  }

  public String getIsolationString() {
    if (this.isolationMode == Connection.TRANSACTION_SERIALIZABLE) {
      return "TRANSACTION_SERIALIZABLE";
    } else if (this.isolationMode == Connection.TRANSACTION_READ_COMMITTED) {
      return "TRANSACTION_READ_COMMITTED";
    } else if (this.isolationMode == Connection.TRANSACTION_REPEATABLE_READ) {
      return "TRANSACTION_REPEATABLE_READ";
    } else if (this.isolationMode == Connection.TRANSACTION_READ_UNCOMMITTED) {
      return "TRANSACTION_READ_UNCOMMITTED";
    } else if (this.isolationMode == Connection.TRANSACTION_NONE) {
      return "TRANSACTION_NONE";
    } else {
      return "TRANSACTION_SERIALIZABLE";
    }
  }

<<<<<<< HEAD
=======
  // 연결 풀 Getter 및 Setter

  /**
   * @return 연결 풀링이 활성화되어 있으면 true
   */
  public boolean isConnectionPoolEnabled() {
    return connectionPoolEnabled;
  }

  /**
   * 연결 풀링을 활성화하거나 비활성화합니다.
   *
   * @param connectionPoolEnabled 연결 풀링을 활성화하려면 true
   */
  public void setConnectionPoolEnabled(boolean connectionPoolEnabled) {
    this.connectionPoolEnabled = connectionPoolEnabled;
  }

  /**
   * @return 풀의 최소 유휴 연결 수
   */
  public int getPoolMinSize() {
    return poolMinSize;
  }

  /**
   * 풀의 최소 유휴 연결 수를 설정합니다.
   *
   * @param poolMinSize 최소 풀 크기
   */
  public void setPoolMinSize(int poolMinSize) {
    this.poolMinSize = poolMinSize;
  }

  /**
   * @return 풀의 최대 연결 수
   */
  public int getPoolMaxSize() {
    return poolMaxSize;
  }

  /**
   * 풀의 최대 연결 수를 설정합니다.
   *
   * @param poolMaxSize 최대 풀 크기
   */
  public void setPoolMaxSize(int poolMaxSize) {
    this.poolMaxSize = poolMaxSize;
  }

  /**
   * @return 풀에서 연결을 기다리는 최대 시간(밀리초)
   */
  public long getPoolConnectionTimeout() {
    return poolConnectionTimeout;
  }

  /**
   * 풀에서 연결을 기다리는 최대 시간을 설정합니다.
   *
   * @param poolConnectionTimeout 밀리초 단위 타임아웃
   */
  public void setPoolConnectionTimeout(long poolConnectionTimeout) {
    this.poolConnectionTimeout = poolConnectionTimeout;
  }

  /**
   * @return 풀에서 연결이 유휴 상태로 있을 수 있는 최대 시간(밀리초)
   */
  public long getPoolIdleTimeout() {
    return poolIdleTimeout;
  }

  /**
   * 풀에서 연결이 유휴 상태로 있을 수 있는 최대 시간을 설정합니다.
   *
   * @param poolIdleTimeout 밀리초 단위 유휴 타임아웃
   */
  public void setPoolIdleTimeout(long poolIdleTimeout) {
    this.poolIdleTimeout = poolIdleTimeout;
  }

  /**
   * @return 풀에서 연결의 최대 수명(밀리초)
   */
  public long getPoolMaxLifetime() {
    return poolMaxLifetime;
  }

  /**
   * 풀에서 연결의 최대 수명을 설정합니다.
   *
   * @param poolMaxLifetime 밀리초 단위 최대 수명
   */
  public void setPoolMaxLifetime(long poolMaxLifetime) {
    this.poolMaxLifetime = poolMaxLifetime;
  }

  /**
   * @return 동적 풀 크기 조정이 활성화되어 있으면 true
   */
  public boolean isDynamicPoolSizingEnabled() {
    return dynamicPoolSizingEnabled;
  }

  /**
   * 동적 풀 크기 조정을 활성화하거나 비활성화합니다. 활성화 시 터미널 수를 기반으로 풀 크기가 자동 계산됩니다.
   *
   * @param dynamicPoolSizingEnabled 동적 풀 크기 조정을 활성화하려면 true
   */
  public void setDynamicPoolSizingEnabled(boolean dynamicPoolSizingEnabled) {
    this.dynamicPoolSizingEnabled = dynamicPoolSizingEnabled;
  }

  /** 터미널 수를 기반으로 최적화된 풀 크기를 계산합니다. 동적 풀 크기 조정이 활성화된 경우에만 동작합니다. */
  public void calculateOptimalPoolSize() {
    if (dynamicPoolSizingEnabled && terminals > 0) {
      // 최소 풀 크기: 터미널 수의 절반 또는 최소 5
      this.poolMinSize = Math.max(terminals / 2, 5);
      // 최대 풀 크기: 터미널 수의 1.5배 또는 최대 터미널 수 + 10
      this.poolMaxSize = Math.max((int) (terminals * 1.5), terminals + 10);
      // 최소값 보장
      this.poolMaxSize = Math.max(this.poolMaxSize, this.poolMinSize + 5);
    }
  }

>>>>>>> master
  @Override
  public String toString() {
    return "WorkloadConfiguration{"
        + "phases="
        + phases
        + ", databaseType="
        + databaseType
        + ", benchmarkName='"
        + benchmarkName
        + '\''
        + ", url='"
        + url
        + '\''
        + ", username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", driverClass='"
        + driverClass
        + '\''
        + ", reconnectOnFailure="
        + reconnectOnConnectionFailure
        + ", newConnectionPerTxn="
        + newConnectionPerTxn
<<<<<<< HEAD
=======
        + ", connectionPoolEnabled="
        + connectionPoolEnabled
        + ", poolMinSize="
        + poolMinSize
        + ", poolMaxSize="
        + poolMaxSize
>>>>>>> master
        + ", batchSize="
        + batchSize
        + ", ddlpath="
        + ddlPath
        + ", sessionSetupFile="
        + sessionSetupFile
        + ", maxRetries="
        + maxRetries
        + ", scaleFactor="
        + scaleFactor
        + ", selectivity="
        + selectivity
        + ", terminals="
        + terminals
        + ", loaderThreads="
        + loaderThreads
        + ", workloadState="
        + workloadState
        + ", transTypes="
        + transTypes
        + ", isolationMode="
        + isolationMode
        + ", dataDir='"
        + dataDir
        + '\''
        + '}';
  }
}
