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

import static org.junit.Assert.*;

import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.util.Histogram;
import com.oltpbenchmark.util.SQLUtil;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTestLoader<T extends BenchmarkModule> extends AbstractTestCase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractTestLoader.class);

  public AbstractTestLoader() {
    super(true, false);
  }

  @Override
  public List<String> ignorableTables() {
    return null;
  }

  /** 로드 테스트 */
  @Test
  public void testLoad() throws Exception {

    this.benchmark.loadDatabase();

    validateLoad();
  }

  /** 로드 후 스크립트를 사용한 로드 테스트 */
  @Test
  public void testLoadWithAfterLoad() throws Exception {
    this.benchmark.setAfterLoadScriptPath("/after-load.sql");

    this.benchmark.loadDatabase();

    // after-load로 'extra'라는 테이블이 추가되며, 하나의 항목이 0입니다
    try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM extra");
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        assertEquals(
            "Table 'extra' from after-load.sql has value different than 0", rs.getInt(1), 0);
      }
    } catch (Exception e) {
      fail("Table 'extra' from after-load.sql was not created");
    }

    validateLoad();
  }

  /** 외부 로드 후 스크립트를 사용한 로드 테스트 */
  @Test
  public void testLoadWithExternalAfterLoad() throws Exception {
    String afterLoadScriptPath =
        Paths.get("src", "test", "java", "com", "oltpbenchmark", "api", "after-load-external.sql")
            .toAbsolutePath()
            .toString();

    this.benchmark.setAfterLoadScriptPath(afterLoadScriptPath);

    this.benchmark.loadDatabase();

    // after-load로 'extra_external'이라는 테이블이 추가되며, 하나의 항목이 1입니다
    try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM extra_external");
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        assertEquals(
            "Table 'extra_external' from " + afterLoadScriptPath + " has value different than 1",
            rs.getInt(1),
            1);
      }
    } catch (Exception e) {
      fail("Table 'extra_external' from " + afterLoadScriptPath + " was not created");
    }

    validateLoad();
  }

  private void validateLoad() throws SQLException {
    assertFalse(
        "Failed to get table names for " + benchmark.getBenchmarkName().toUpperCase(),
        this.catalog.getTables().isEmpty());

    LOG.debug("Computing the size of the tables");
    Histogram<String> tableSizes = new Histogram<String>(true);

    for (Table table : this.catalog.getTables()) {
      String tableName = table.getName();
      Table catalog_tbl = this.catalog.getTable(tableName);

      String sql = SQLUtil.getCountSQL(this.workConf.getDatabaseType(), catalog_tbl);

      try (Statement stmt = conn.createStatement();
          ResultSet result = stmt.executeQuery(sql); ) {

        assertNotNull(result);

        boolean adv = result.next();
        assertTrue(sql, adv);

        int count = result.getInt(1);
        LOG.debug(sql + " => " + count);
        tableSizes.put(tableName, count);
      }
    }

    LOG.debug("=== TABLE SIZES ===\n" + tableSizes);
    assertFalse(
        "Unable to compute the tables size for " + benchmark.getBenchmarkName().toUpperCase(),
        tableSizes.isEmpty());

    for (String tableName : tableSizes.values()) {
      long count = tableSizes.get(tableName);

      if (ignorableTables() != null
          && ignorableTables().stream().anyMatch(tableName::equalsIgnoreCase)) {
        continue;
      }

      assert (count > 0) : "No tuples were inserted for table " + tableName;
    }
  }

  public Loader<? extends BenchmarkModule> testLoadWithReturn() throws Exception {
    Loader<? extends BenchmarkModule> loader = this.benchmark.loadDatabase();

    validateLoad();

    return loader;
  }
}
