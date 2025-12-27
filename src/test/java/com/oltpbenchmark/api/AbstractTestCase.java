/*
<<<<<<< HEAD
 *  Copyright 2015 by OLTPBenchmark Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
=======
 *  저작권 2015 OLTPBenchmark 프로젝트
 *
 *  Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 *  라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 *  라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 *  이 소프트웨어는 "있는 그대로" 배포되며,
 *  명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 *  라이선스에서 허용하는 권한과 제한 사항은
 *  라이선스의 본문을 참조하십시오.
>>>>>>> master
 */

package com.oltpbenchmark.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.catalog.AbstractCatalog;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.ClassUtil;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.hsqldb.Database;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTestCase<T extends BenchmarkModule> {

  protected final Logger LOG = LoggerFactory.getLogger(getClass());

  // -----------------------------------------------------------------

<<<<<<< HEAD
  /**
   * This is the database type that we will use in our unit tests. This should always be one of the
   * embedded java databases
   */
=======
  /** 단위 테스트에서 사용할 데이터베이스 타입입니다. 항상 임베디드 Java 데이터베이스 중 하나여야 합니다. */
>>>>>>> master
  private static final DatabaseType DB_TYPE = DatabaseType.HSQLDB;

  // -----------------------------------------------------------------

  protected static final double DB_SCALE_FACTOR = 0.01;

  private Server server = null;

  protected WorkloadConfiguration workConf;
  protected T benchmark;
  protected AbstractCatalog catalog;
  protected Connection conn;

  protected final boolean createDatabase;
  protected final boolean loadDatabase;
  protected final String ddlOverridePath;
  protected final String sessionSetupFile;

  private static final AtomicInteger portCounter = new AtomicInteger(9001);
  private static final int MAX_PORT_NUMBER = 65535;

  public AbstractTestCase(boolean createDatabase, boolean loadDatabase) {
    this.benchmark = null;
    this.createDatabase = createDatabase;
    this.loadDatabase = loadDatabase;
    this.ddlOverridePath = null;
    this.sessionSetupFile = null;
  }

  public AbstractTestCase(boolean createDatabase, boolean loadDatabase, String ddlOverridePath) {
    this.benchmark = null;
    this.createDatabase = createDatabase;
    this.loadDatabase = loadDatabase;
    this.ddlOverridePath = ddlOverridePath;
    this.sessionSetupFile = null;
  }

  public AbstractTestCase(
      boolean createDatabase,
      boolean loadDatabase,
      String ddlOverridePath,
      String sessionSetupFile) {
    this.benchmark = null;
    this.createDatabase = createDatabase;
    this.loadDatabase = loadDatabase;
    this.ddlOverridePath = ddlOverridePath;
    this.sessionSetupFile = sessionSetupFile;
  }

  public abstract List<Class<? extends Procedure>> procedures();

  public abstract Class<T> benchmarkClass();

  public abstract List<String> ignorableTables();

  @Rule public TestName name = new TestName();

  @Before
  public final void setUp() throws Exception {
    HsqlProperties props = new HsqlProperties();
    // props.setProperty("server.remote_open", true);

    int port = findAvailablePort();

    LOG.info("starting HSQLDB server for test [{}] on port [{}]", name.getMethodName(), port);

    server = new Server();
    server.setProperties(props);
    server.setDatabasePath(0, "mem:benchbase;sql.syntax_mys=true");
    server.setDatabaseName(0, "benchbase");
    server.setAddress("localhost");
    server.setPort(port);
    server.setSilent(true);
    server.setLogWriter(null);
    server.start();

    this.workConf = new WorkloadConfiguration();

    String DB_CONNECTION =
        String.format("jdbc:hsqldb:hsql://localhost:%d/benchbase", server.getPort());

    this.workConf.setTransTypes(proceduresToTransactionTypes(procedures()));
    this.workConf.setDatabaseType(DB_TYPE);
    this.workConf.setUrl(DB_CONNECTION);
    this.workConf.setScaleFactor(DB_SCALE_FACTOR);
    this.workConf.setTerminals(1);
    this.workConf.setBatchSize(128);
    this.workConf.setBenchmarkName(
        BenchmarkModule.convertBenchmarkClassToBenchmarkName(benchmarkClass()));
    this.workConf.setDDLPath(this.ddlOverridePath);
    this.workConf.setSessionSetupFile(this.sessionSetupFile);

    customWorkloadConfiguration(this.workConf);

    this.benchmark =
        ClassUtil.newInstance(
            benchmarkClass(),
            new Object[] {this.workConf},
            new Class<?>[] {WorkloadConfiguration.class});
    assertNotNull(this.benchmark);

<<<<<<< HEAD
    // HACK: calling this a second time is a cheap no-op for most benchmark
    // tests, but actually ensures that the procedures list is populated
    // for the TestTemplatedWorker test which doesn't know its procedures
    // until after the benchmark is initialized and the config is loaded.
=======
    // HACK: 대부분의 벤치마크 테스트에서는 두 번째로 호출하는 것이 저렴한 no-op이지만,
    // 실제로는 벤치마크가 초기화되고 설정이 로드된 후에야 프로시저를 알 수 있는
    // TestTemplatedWorker 테스트를 위해 프로시저 목록이 채워지도록 보장합니다.
>>>>>>> master
    var proceedures = this.procedures();
    assertNotNull(proceedures);
    if (!(this instanceof TestDDLOverride)) {
      assertFalse(proceedures.isEmpty());
    }

    this.conn = this.benchmark.makeConnection();
    assertNotNull(this.conn);

    this.benchmark.refreshCatalog();
    this.catalog = this.benchmark.getCatalog();
    assertNotNull(this.catalog);

    if (createDatabase) {
      this.createDatabase();
    }

    if (loadDatabase) {
      this.loadDatabase();
    }

    try {
      postCreateDatabaseSetup();
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      cleanupServer();
      fail("postCreateDatabaseSetup() failed");
    }
  }

  private int findAvailablePort() throws IOException {
    while (true) {
      int port = portCounter.incrementAndGet();

      if (port > MAX_PORT_NUMBER) {
        throw new IOException("No available port found up to " + MAX_PORT_NUMBER);
      }

      try (ServerSocket testSocket = new ServerSocket(port)) {
        assert testSocket != null;
        return port;
      } catch (BindException e) {
<<<<<<< HEAD
        // This port is already in use. Continue to next port.
=======
        // 이 포트는 이미 사용 중입니다. 다음 포트로 계속 진행합니다.
>>>>>>> master
        LOG.warn("Port {} is already in use. Trying next port.", port);
      }
    }
  }

  protected TransactionTypes proceduresToTransactionTypes(
      List<Class<? extends Procedure>> procedures) {
    TransactionTypes txnTypes = new TransactionTypes(new ArrayList<>());

    int id = 0;
    for (Class<? extends Procedure> procedureClass : procedures) {
      TransactionType tt = new TransactionType(procedureClass, id++, false, 0, 0);
      txnTypes.add(tt);
    }

    return txnTypes;
  }

  protected void createDatabase() {
    try {
      this.benchmark.createDatabase();
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      cleanupServer();
      fail("createDatabase() failed");
    }
  }

  protected void loadDatabase() {
    try {
      this.benchmark.loadDatabase();
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      cleanupServer();
      fail("loadDatabase() failed");
    }
  }

  protected void customWorkloadConfiguration(WorkloadConfiguration workConf) {}

  protected void postCreateDatabaseSetup() throws IOException {}

  @After
  public final void tearDown() throws Exception {

    if (this.conn != null) {
      this.conn.close();
    }

    cleanupServer();
  }

  protected void cleanupServer() {
    if (server != null) {

      LOG.trace("shutting down catalogs...");
      server.shutdownCatalogs(Database.CLOSEMODE_NORMAL);

      LOG.trace("stopping server...");
      server.stop();

      while (server.getState() != ServerConstants.SERVER_STATE_SHUTDOWN) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException ignore) {
        }
      }

      LOG.trace("shutting down server...");
      server.shutdown();
    }
  }
}
