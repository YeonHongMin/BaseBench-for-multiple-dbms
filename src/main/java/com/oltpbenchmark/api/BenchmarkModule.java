/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
<<<<<<< HEAD
=======
 * Apache License, Version 2.0 (이하 "라이센스")에 따라 라이센스가 부여됩니다.
 * 이 파일은 라이센스에 따라 사용할 수 있으며, 라이센스에 따라 사용하지 않는 한
 * 사용할 수 없습니다. 라이센스 사본은 다음에서 얻을 수 있습니다.
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * 적용 가능한 법률에 의해 요구되거나 서면으로 합의되지 않는 한, 라이센스에 따라
 * 배포되는 소프트웨어는 "있는 그대로" 배포되며, 명시적이거나 묵시적인 어떠한 종류의
 * 보증이나 조건도 없습니다. 라이센스에 따른 권한 및 제한 사항에 대한 자세한 내용은
 * 라이센스를 참조하십시오.
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.api;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.catalog.AbstractCatalog;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.ClassUtil;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import com.oltpbenchmark.util.ConnectionPoolManager;
>>>>>>> master
=======
import com.oltpbenchmark.util.ConnectionPoolManager;
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
import com.oltpbenchmark.util.FileUtil;
import com.oltpbenchmark.util.SQLUtil;
import com.oltpbenchmark.util.ScriptRunner;
import com.oltpbenchmark.util.ThreadUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
/** Base class for all benchmark implementations */
public abstract class BenchmarkModule {
  private static final Logger LOG = LoggerFactory.getLogger(BenchmarkModule.class);

  /** The workload configuration for this benchmark invocation */
  protected final WorkloadConfiguration workConf;

  /** Class loader variable for this benchmark */
  protected ClassLoader classLoader;

  /** These are the variations of the Procedure's Statement SQL */
  protected final StatementDialects dialects;

  /** Supplemental Procedures */
  private final Set<Class<? extends Procedure>> supplementalProcedures = new HashSet<>();

  /** A single Random object that should be re-used by all a benchmark's components */
<<<<<<< HEAD
=======
/** 모든 벤치마크 구현을 위한 기본 클래스 */
public abstract class BenchmarkModule {
  private static final Logger LOG = LoggerFactory.getLogger(BenchmarkModule.class);

  /** 이 벤치마크 호출에 대한 워크로드 구성 */
  protected final WorkloadConfiguration workConf;

  /** 이 벤치마크에 대한 클래스 로더 변수 */
  protected ClassLoader classLoader;

  /** Procedure의 Statement SQL 변형들 */
  protected final StatementDialects dialects;

  /** 보조 프로시저 */
  private final Set<Class<? extends Procedure>> supplementalProcedures = new HashSet<>();

  /** 벤치마크의 모든 구성 요소에서 재사용해야 하는 단일 Random 객체 */
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  private static final ThreadLocal<Random> rng = new ThreadLocal<>();

  private AbstractCatalog catalog = null;

<<<<<<< HEAD
<<<<<<< HEAD
  /**
   * Constructor!
=======
  /** 연결 풀 관리자 (선택 사항, 연결 풀링이 활성화된 경우 사용) */
  private ConnectionPoolManager connectionPoolManager = null;

  /**
   * 생성자!
>>>>>>> master
=======
  /** Connection Pool Manager (optional, used when connection pooling is enabled) */
  private ConnectionPoolManager connectionPoolManager = null;

  /**
   * Constructor!
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param workConf
   */
  public BenchmarkModule(WorkloadConfiguration workConf) {
    this.workConf = workConf;
    this.dialects = new StatementDialects(workConf);
    // setClassLoader();
    this.classLoader = Thread.currentThread().getContextClassLoader();
  }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  /**
   * Instantiates the classLoader variable, needs to be overwritten if benchmark uses a custom
   * implementation.
   */
<<<<<<< HEAD
=======
  /** classLoader 변수를 인스턴스화합니다. 벤치마크가 사용자 정의 구현을 사용하는 경우 재정의해야 합니다. */
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  protected void setClassLoader() {
    this.classLoader = Thread.currentThread().getContextClassLoader();
  }

  // --------------------------------------------------------------------------
<<<<<<< HEAD
<<<<<<< HEAD
  // DATABASE CONNECTION
  // --------------------------------------------------------------------------

  public final Connection makeConnection() throws SQLException {

=======
  // 데이터베이스 연결
  // --------------------------------------------------------------------------

  /** 구성에서 활성화된 경우 연결 풀을 초기화합니다. 연결을 만들기 전에 호출해야 합니다. */
=======
  // DATABASE CONNECTION
  // --------------------------------------------------------------------------

  /**
   * Initialize the connection pool if enabled in configuration. Should be called before making
   * connections.
   */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public final void initializeConnectionPool() {
    if (workConf.isConnectionPoolEnabled() && connectionPoolManager == null) {
      connectionPoolManager = new ConnectionPoolManager(workConf);
      LOG.info("Connection pool initialized for benchmark: {}", getBenchmarkName());
    }
  }

  /**
<<<<<<< HEAD
   * 데이터베이스 연결을 가져옵니다. 활성화된 경우 연결 풀을 사용하고, 그렇지 않으면 직접 연결을 생성합니다.
   *
   * @return 데이터베이스 연결
   * @throws SQLException 연결을 설정할 수 없는 경우
   */
  public final Connection makeConnection() throws SQLException {
    // 활성화된 경우 연결 풀 사용
=======
   * Get a database connection. Uses connection pool if enabled, otherwise creates direct
   * connection.
   *
   * @return A database connection
   * @throws SQLException If connection cannot be established
   */
  public final Connection makeConnection() throws SQLException {
    // Use connection pool if enabled
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    if (workConf.isConnectionPoolEnabled()) {
      if (connectionPoolManager == null) {
        initializeConnectionPool();
      }
      return connectionPoolManager.getConnection();
    }

<<<<<<< HEAD
    // 직접 연결로 폴백 (원래 동작)
>>>>>>> master
=======
    // Fall back to direct connection (original behavior)
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    if (StringUtils.isEmpty(workConf.getUsername())) {
      return DriverManager.getConnection(workConf.getUrl());
    } else {
      return DriverManager.getConnection(
          workConf.getUrl(), workConf.getUsername(), workConf.getPassword());
    }
  }

<<<<<<< HEAD
<<<<<<< HEAD
=======
  /**
   * 연결 풀링이 활성화된 경우 연결 풀 관리자를 가져옵니다.
   *
   * @return ConnectionPoolManager 또는 풀링이 비활성화된 경우 null
=======
  /**
   * Get the connection pool manager if connection pooling is enabled.
   *
   * @return ConnectionPoolManager or null if pooling is disabled
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   */
  public final ConnectionPoolManager getConnectionPoolManager() {
    return connectionPoolManager;
  }

<<<<<<< HEAD
  /** 연결 풀을 닫고 모든 리소스를 해제합니다. 벤치마크가 완료되면 호출해야 합니다. */
=======
  /**
   * Close the connection pool and release all resources. Should be called when the benchmark is
   * complete.
   */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public final void closeConnectionPool() {
    if (connectionPoolManager != null) {
      LOG.info("Closing connection pool for benchmark: {}", getBenchmarkName());
      LOG.info("Final pool stats: {}", connectionPoolManager.getPoolStats());
      connectionPoolManager.close();
      connectionPoolManager = null;
    }
  }

<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  private String afterLoadScriptPath = null;

  public final void setAfterLoadScriptPath(String scriptPath) throws FileNotFoundException {
    if (scriptPath != null) scriptPath = scriptPath.trim();
    try {
      this.afterLoadScriptPath = FileUtil.checkPath(scriptPath, "afterload");
      return;
    } catch (FileNotFoundException ex) {
      this.afterLoadScriptPath = null;
    }

    if (this.afterLoadScriptPath == null && scriptPath != null && !scriptPath.isEmpty()) {
      if (this.getClass().getResourceAsStream(scriptPath) == null) {
        throw new FileNotFoundException(
            "Couldn't find " + scriptPath + " as local file or resource.");
      }
      this.afterLoadScriptPath = scriptPath;
    }
  }

  public String getAfterLoadScriptPath() {
    return this.afterLoadScriptPath;
  }

  // --------------------------------------------------------------------------
  // IMPLEMENTING CLASS INTERFACE
  // --------------------------------------------------------------------------

  /**
   * @return
   * @throws IOException
   */
  protected abstract List<Worker<? extends BenchmarkModule>> makeWorkersImpl() throws IOException;

  /**
   * Each BenchmarkModule needs to implement this method to load a sample dataset into the database.
   * The Connection handle will already be configured for you, and the base class will commit+close
   * it once this method returns
   *
   * @return TODO
   */
  protected abstract Loader<? extends BenchmarkModule> makeLoaderImpl();

  protected abstract Package getProcedurePackageImpl();

  // --------------------------------------------------------------------------
  // PUBLIC INTERFACE
  // --------------------------------------------------------------------------

  /**
   * Return the Random generator that should be used by all this benchmark's components. We are
   * using ThreadLocal to make this support multiple threads better. This will set the seed if one
   * is specified in the workload config file
   */
  public Random rng() {
    Random ret = rng.get();
    if (ret == null) {
      if (this.workConf.getRandomSeed() != -1) {
        ret = new Random(this.workConf.getRandomSeed());
      } else {
        ret = new Random();
      }
      rng.set(ret);
    }
    return ret;
  }

  private String convertBenchmarkClassToBenchmarkName() {
    return convertBenchmarkClassToBenchmarkName(this.getClass());
  }

  protected static <T> String convertBenchmarkClassToBenchmarkName(Class<T> clazz) {
    assert (clazz != null);
    String name = clazz.getSimpleName().toLowerCase();
    // Special case for "CHBenCHmark"
    if (!name.equals("chbenchmark") && name.endsWith("benchmark")) {
      name = name.replace("benchmark", "");
    }
    return (name);
  }

  /**
   * Return the URL handle to the DDL used to load the benchmark's database schema.
   *
   * @param db_type
   */
  public String getDatabaseDDLPath(DatabaseType db_type) {

    // The order matters!
    List<String> names = new ArrayList<>();
    if (db_type != null) {
      DatabaseType ddl_db_type = db_type;
      // HACK: Use MySQL if we're given MariaDB
      if (ddl_db_type == DatabaseType.MARIADB) ddl_db_type = DatabaseType.MYSQL;
      names.add("ddl-" + ddl_db_type.name().toLowerCase() + ".sql");
    }
    names.add("ddl-generic.sql");

    for (String fileName : names) {
      final String benchmarkName = getBenchmarkName();
      final String path = "/benchmarks/" + benchmarkName + "/" + fileName;

      try (InputStream stream = this.getClass().getResourceAsStream(path)) {
        if (stream != null) {
          return path;
        }

      } catch (IOException e) {
        LOG.error(e.getMessage(), e);
      }
    }

    return null;
  }

  public final List<Worker<? extends BenchmarkModule>> makeWorkers() throws IOException {
    return (this.makeWorkersImpl());
  }

  public final void refreshCatalog() throws SQLException {
    if (this.catalog != null) {
      try {
        this.catalog.close();
      } catch (SQLException throwables) {
        LOG.error(throwables.getMessage(), throwables);
      }
    }
    try (Connection conn = this.makeConnection()) {
      this.catalog =
          SQLUtil.getCatalog(this, this.getWorkloadConfiguration().getDatabaseType(), conn);
    }
  }

  /**
   * Create the Benchmark Database This is the main method used to create all the database objects
   * (e.g., table, indexes, etc) needed for this benchmark
   */
  public final void createDatabase() throws SQLException, IOException {
    try (Connection conn = this.makeConnection()) {
      this.createDatabase(this.workConf.getDatabaseType(), conn);
    }
  }

  /**
   * Create the Benchmark Database This is the main method used to create all the database objects
   * (e.g., table, indexes, etc) needed for this benchmark
   */
  public final void createDatabase(DatabaseType dbType, Connection conn)
      throws SQLException, IOException {

<<<<<<< HEAD
<<<<<<< HEAD
    ScriptRunner runner = new ScriptRunner(conn, true, true);
=======
    // stopOnError=false to continue on DROP TABLE errors (table may not exist)
    ScriptRunner runner = new ScriptRunner(conn, true, false);
>>>>>>> master
=======
    // stopOnError=false to continue on DROP TABLE errors (table may not exist)
    ScriptRunner runner = new ScriptRunner(conn, true, false);
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d

    if (workConf.getDDLPath() != null) {
      String ddlPath = workConf.getDDLPath();
      LOG.warn("Overriding default DDL script path");
      LOG.debug("Executing script [{}] for database type [{}]", ddlPath, dbType);
      runner.runExternalScript(ddlPath);
    } else {
      String ddlPath = this.getDatabaseDDLPath(dbType);
      LOG.debug("Executing script [{}] for database type [{}]", ddlPath, dbType);
      runner.runScript(ddlPath);
    }
  }

  public final void runScript(String scriptPath) throws SQLException, IOException {
    try (Connection conn = this.makeConnection()) {
      DatabaseType dbType = this.workConf.getDatabaseType();
      ScriptRunner runner = new ScriptRunner(conn, true, true);
      LOG.debug(
          "Checking for script [{}] on local filesystem for database type [{}]",
          scriptPath,
          dbType);
      if (FileUtil.exists(scriptPath)) {
        LOG.debug(
            "Executing script [{}] from local filesystem for database type [{}]",
            scriptPath,
            dbType);
        runner.runExternalScript(scriptPath);
      } else {
        LOG.debug(
            "Executing script [{}] from resource stream for database type [{}]",
            scriptPath,
            dbType);
        runner.runScript(scriptPath);
      }
    }
  }

  /** Invoke this benchmark's database loader */
  public final Loader<? extends BenchmarkModule> loadDatabase()
      throws IOException, SQLException, InterruptedException {
    Loader<? extends BenchmarkModule> loader;

    loader = this.makeLoaderImpl();
    if (loader != null) {

      try {
        List<LoaderThread> loaderThreads = loader.createLoaderThreads();
        int maxConcurrent = workConf.getLoaderThreads();

        ThreadUtil.runLoaderThreads(loaderThreads, maxConcurrent);

        if (!loader.getTableCounts().isEmpty()) {
          LOG.debug("Table Counts:\n{}", loader.getTableCounts());
        }
      } finally {
        if (LOG.isDebugEnabled()) {
          LOG.debug(
              String.format(
                  "Finished loading the %s database", this.getBenchmarkName().toUpperCase()));
        }
      }
    }

    if (this.afterLoadScriptPath != null) {
      LOG.debug(
          "Running script {} after load for {} benchmark...",
          this.afterLoadScriptPath,
          this.workConf.getBenchmarkName().toUpperCase());
      runScript(this.afterLoadScriptPath);
      LOG.debug(
          "Finished running script {} after load for {} benchmark...",
          this.afterLoadScriptPath,
          this.workConf.getBenchmarkName().toUpperCase());
    }

    return loader;
  }

  public final void clearDatabase() throws SQLException {

    try (Connection conn = this.makeConnection()) {
      Loader<? extends BenchmarkModule> loader = this.makeLoaderImpl();
      if (loader != null) {
        conn.setAutoCommit(false);
        loader.unload(conn, this.catalog);
        conn.commit();
      }
    }
  }

  // --------------------------------------------------------------------------
  // UTILITY METHODS
  // --------------------------------------------------------------------------

  /** Return the unique identifier for this benchmark */
  public final String getBenchmarkName() {
    String workConfName = this.workConf.getBenchmarkName();
    return workConfName != null ? workConfName : convertBenchmarkClassToBenchmarkName();
  }

  /** Return the database's catalog */
  public final AbstractCatalog getCatalog() {

    if (catalog == null) {
      throw new RuntimeException("getCatalog() has been called before refreshCatalog()");
    }

    return this.catalog;
  }

  /** Return the StatementDialects loaded for this benchmark */
  public final StatementDialects getStatementDialects() {
    return (this.dialects);
  }

  @Override
  public final String toString() {
    return getBenchmarkName();
  }

  /**
   * Initialize a TransactionType handle for the get procedure name and id This should only be
   * invoked a start-up time
   *
   * @param procName
   * @param id
   * @return
   */
  @SuppressWarnings("unchecked")
  public final TransactionType initTransactionType(
      String procName, int id, long preExecutionWait, long postExecutionWait) {
    if (id == TransactionType.INVALID_ID) {
      throw new RuntimeException(
          String.format(
              "Procedure %s.%s cannot use the reserved id '%d' for %s",
              getBenchmarkName(),
              procName,
              id,
              TransactionType.INVALID.getClass().getSimpleName()));
    }

    Package pkg = this.getProcedurePackageImpl();

    String fullName = pkg.getName() + "." + procName;
    Class<? extends Procedure> procClass =
        (Class<? extends Procedure>) ClassUtil.getClass(this.classLoader, fullName);

    return new TransactionType(procClass, id, false, preExecutionWait, postExecutionWait);
  }

  public final WorkloadConfiguration getWorkloadConfiguration() {
    return (this.workConf);
  }

  /**
   * Return a mapping from TransactionTypes to Procedure invocations
   *
   * @return
   */
  public Map<TransactionType, Procedure> getProcedures() {
    Map<TransactionType, Procedure> proc_xref = new HashMap<>();
    TransactionTypes txns = this.workConf.getTransTypes();

    if (txns != null) {
      for (Class<? extends Procedure> procClass : this.supplementalProcedures) {
        TransactionType txn = txns.getType(procClass);
        if (txn == null) {
          txn = new TransactionType(procClass, procClass.hashCode(), true, 0, 0);
          txns.add(txn);
        }
      }

      for (TransactionType txn : txns) {
        Procedure proc =
            ClassUtil.newInstance(txn.getProcedureClass(), new Object[0], new Class<?>[0]);
        proc.initialize(this.workConf.getDatabaseType());
        proc_xref.put(txn, proc);
        proc.loadSQLDialect(this.dialects);
        if (this.workConf.getAdvancedMonitoringEnabled()) {
          proc.enabledAdvancedMonitoring();
        }
      }
    }
    if (proc_xref.isEmpty()) {
      LOG.warn("No procedures defined for {}", this);
    }
    return (proc_xref);
  }

  /**
   * @param procClass
   */
  public final void registerSupplementalProcedure(Class<? extends Procedure> procClass) {
    this.supplementalProcedures.add(procClass);
  }
}
