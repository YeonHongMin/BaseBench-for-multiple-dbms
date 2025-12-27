/*
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
 */

package com.oltpbenchmark.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oltpbenchmark.catalog.AbstractCatalog;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.ClassUtil;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;

public abstract class AbstractTestBenchmarkModule<T extends BenchmarkModule>
    extends AbstractTestCase<T> {

  public AbstractTestBenchmarkModule() {
    super(false, false);
  }

  @Override
  public List<String> ignorableTables() {
    return null;
  }

  /** 데이터베이스 DDL 경로 가져오기 테스트 */
  @Test
  public void testGetDatabaseDDLPath() throws Exception {
    String ddlPath = this.benchmark.getDatabaseDDLPath(this.workConf.getDatabaseType());
    assertNotNull(ddlPath);
    try (InputStream stream = this.getClass().getResourceAsStream(ddlPath)) {
      assertNotNull(stream);
    }
  }

  /** 데이터베이스 생성 테스트 */
  @Test
  public void testCreateDatabase() throws Exception {
    this.benchmark.createDatabase();

    // 일부 테이블이 반환되는지 확인합니다
    this.benchmark.refreshCatalog();
    AbstractCatalog catalog = this.benchmark.getCatalog();
    assertNotNull(catalog);
    assertFalse(catalog.getTables().isEmpty());

    // 빈 테이블이 없는지 확인합니다
    for (Table catalog_tbl : catalog.getTables()) {
      assert (catalog_tbl.getColumnCount() > 0) : "Missing columns for " + catalog_tbl;
    }
  }

  /** 트랜잭션 타입 가져오기 테스트 */
  @Test
  public void testGetTransactionType() {
    int id = 1;
    for (Class<? extends Procedure> procClass : procedures()) {
      assertNotNull(procClass);
      String procName = procClass.getSimpleName();
      TransactionType txnType = this.benchmark.initTransactionType(procName, id++, 0, 0);
      assertNotNull(txnType);
      assertEquals(procClass, txnType.getProcedureClass());
    }
  }

  /** SQL 방언 경로 가져오기 테스트 */
  @Test
  public void testGetSQLDialectPath() throws Exception {
    for (DatabaseType dbType : DatabaseType.values()) {
      String xmlFilePath = this.benchmark.getStatementDialects().getSQLDialectPath(dbType);
      if (xmlFilePath != null) {
        URL xmlUrl = this.getClass().getResource(xmlFilePath);
        assertNotNull(xmlUrl);
        File xmlFile = new File(xmlUrl.toURI());
        assertTrue(xmlFile.getAbsolutePath(), xmlFile.exists());
      }
    }
  }

  /** SQL 방언 로드 테스트 */
  @Test
  public void testLoadSQLDialect() throws Exception {
    for (DatabaseType dbType : DatabaseType.values()) {
      this.workConf.setDatabaseType(dbType);

      // 로드할 수 있는지 확인합니다
      StatementDialects dialects = new StatementDialects(this.workConf);
      if (dialects.load()) {

        for (String procName : dialects.getProcedureNames()) {
          for (String stmtName : dialects.getStatementNames(procName)) {
            String sql = dialects.getSQL(procName, stmtName);
            assertNotNull(sql);
            assertFalse(sql.isEmpty());
          }
        }
      }
    }
  }

  /** SQL 방언 덤프 테스트 */
  @Test
  public void testDumpSQLDialect() throws Exception {
    for (DatabaseType dbType : DatabaseType.values()) {
      this.workConf.setDatabaseType(dbType);

      StatementDialects dialects = new StatementDialects(this.workConf);
      if (dialects.load()) {
        String dump = dialects.export(dbType, this.benchmark.getProcedures().values());
        assertNotNull(dump);
        assertFalse(dump.isEmpty());
        Set<String> benchmarkProcedureNames =
            this.benchmark.getProcedures().values().stream()
                .map(Procedure::getProcedureName)
                .collect(Collectors.toSet());
        for (String procName : dialects.getProcedureNames()) {
          if (benchmarkProcedureNames.contains(procName)) {
            assertTrue(procName, dump.contains(procName));
            for (String stmtName : dialects.getStatementNames(procName)) {
              assertTrue(procName + "." + stmtName, dump.contains(stmtName));
            }
          }
        }
      }
    }
  }

  /** SQL 방언 설정 테스트 */
  @Test
  public void testSetSQLDialect() throws Exception {
    for (DatabaseType dbType : DatabaseType.values()) {
      this.workConf.setDatabaseType(dbType);

      StatementDialects dialects = new StatementDialects(this.workConf);
      if (dialects.load()) {

        for (Procedure proc : this.benchmark.getProcedures().values()) {
          if (dialects.getProcedureNames().contains(proc.getProcedureName())) {
            // BenchmarkModule::getProcedureName에서 방언이 로드되므로 새로운 proc이 필요합니다
            Procedure testProc =
                ClassUtil.newInstance(proc.getClass().getName(), new Object[0], new Class<?>[0]);
            assertNotNull(testProc);
            testProc.initialize(dbType);
            testProc.loadSQLDialect(dialects);

            Collection<String> dialectStatementNames =
                dialects.getStatementNames(testProc.getProcedureName());

            for (String statementName : dialectStatementNames) {
              SQLStmt stmt = testProc.getStatements().get(statementName);
              assertNotNull(stmt);
              String dialectSQL = dialects.getSQL(testProc.getProcedureName(), statementName);
              assertEquals(dialectSQL, stmt.getOriginalSQL());
            }
          }
        }
      }
    }
  }
}
