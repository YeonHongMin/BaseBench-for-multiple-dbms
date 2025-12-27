<<<<<<< HEAD
<<<<<<< HEAD
=======
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

>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
package com.oltpbenchmark.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.util.SQLUtil;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class TestDDLOverride extends AbstractTestCase<MockBenchmark> {

  public TestDDLOverride() {
    super(
        false,
        false,
        Paths.get("src", "test", "resources", "benchmarks", "mockbenchmark", "ddl-hsqldb.sql")
            .toAbsolutePath()
            .toString());
  }

  @Override
  public List<Class<? extends Procedure>> procedures() {
    return new ArrayList<>();
  }

  @Override
  public Class<MockBenchmark> benchmarkClass() {
    return MockBenchmark.class;
  }

  @Override
  public List<String> ignorableTables() {
    return null;
  }

<<<<<<< HEAD
<<<<<<< HEAD
=======
  /** DDL 오버라이드를 사용한 생성 테스트 */
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @Test
  public void testCreateWithDdlOverride() throws Exception {
    this.benchmark.createDatabase();

    assertFalse(
        "Failed to get table names for " + benchmark.getBenchmarkName().toUpperCase(),
        this.catalog.getTables().isEmpty());
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
        assertEquals(0, count);
      }
    }
  }
}
