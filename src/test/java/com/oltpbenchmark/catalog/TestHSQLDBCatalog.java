/*
 *  Copyright 2021 by OLTPBenchmark Project
 *
 *  이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 *  라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 *  라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 *  명시적/묵시적 보증 없이 배포됩니다.
 *  라이선스에서 허용된 제한과 조건을 준수해 주세요.
 */

package com.oltpbenchmark.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oltpbenchmark.api.MockBenchmark;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.SQLUtil;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;

public class TestHSQLDBCatalog {

  private MockBenchmark benchmark;
  private HSQLDBCatalog catalog;

  @Before
  public void setUp() throws Exception {
    this.benchmark = new MockBenchmark();
    this.catalog = new HSQLDBCatalog(benchmark);
    assertNotNull(this.catalog);
  }

  /** 원본 테이블 이름 가져오기 테스트 */
  @Test
  public void testGetOriginalTableNames() {
    // 이 맵의 키와 값이 대소문자를 구분하지 않는 경우에만 같아야 합니다
    Map<String, String> origTableNames = this.catalog.getOriginalTableNames();
    assertNotNull(origTableNames);
    assertFalse(origTableNames.isEmpty());

    for (Entry<String, String> e : origTableNames.entrySet()) {
      assertFalse(e.toString(), e.getKey().equals(e.getValue()));
      assertTrue(e.toString(), e.getKey().equalsIgnoreCase(e.getValue()));
    }
  }

  /** 초기화 테스트 */
  @Test
  public void testInit() throws Exception {
    // 테스트 파일에서 CREATE TABLE의 개수를 세어봅니다
    String ddlPath = this.benchmark.getDatabaseDDLPath(DatabaseType.HSQLDB);
    try (InputStream stream = this.getClass().getResourceAsStream(ddlPath)) {
      assertNotNull(stream);
      String contents = new String(stream.readAllBytes());
      assertFalse(contents.isEmpty());
      int offset = 0;
      int num_tables = 0;
      while (offset < contents.length()) {
        offset = contents.indexOf("CREATE TABLE", offset);
        if (offset == -1) break;
        num_tables++;
        offset++;
      }
      assertEquals(num_tables, 3);

      // CatalogUtil이 동일한 수의 테이블을 반환하는지 확인합니다
      assertEquals(num_tables, this.catalog.getTables().size());

      // 맵 이름이 테이블 이름과 일치하는지 확인합니다
      for (String table_name :
          this.catalog.getTables().stream().map(AbstractCatalogObject::getName).toList()) {
        Table catalog_tbl = this.catalog.getTable(table_name);
        assertNotNull(catalog_tbl);
        assertEquals(table_name, catalog_tbl.getName());
      }
    }
  }

  /** 외래 키 테스트 */
  @Test
  public void testForeignKeys() {
    // C 테이블은 두 개의 외래 키를 가져야 합니다
    Table catalog_tbl = this.catalog.getTable("C");
    int found = 0;
    assertNotNull(catalog_tbl);
    for (Column catalog_col : catalog_tbl.getColumns()) {
      assertNotNull(catalog_col);
      Column fkey_col = catalog_col.getForeignKey();
      if (fkey_col != null) {
        assertNotEquals(fkey_col.getTable(), catalog_tbl);
        found++;
      }
    }
    assertEquals(2, found);
  }

  /** 인덱스 테스트 */
  @Test
  public void testIndexes() {
    for (Table catalog_tbl : this.catalog.getTables()) {
      assertNotNull(catalog_tbl);

      for (Index catalog_idx : catalog_tbl.getIndexes()) {
        assertNotNull(catalog_idx);
        assertEquals(catalog_tbl, catalog_idx.getTable());
        assertTrue(catalog_idx.getColumns().size() > 0);

        for (int i = 0; i < catalog_idx.getColumns().size(); i++) {
          assertNotNull(catalog_idx.getColumns().get(i).getName());
          assertNotNull(catalog_idx.getColumns().get(i).getDir());
        }
      }
    }
  }

  /** 정수 컬럼 테스트 */
  @Test
  public void testIntegerColumns() {
    // 이름에 'IATTR'이 포함된 컬럼은 정수 타입입니다
    // 따라서 우리의 작은 검사기가 제대로 작동하는지 확인해야 합니다
    for (Table catalog_tbl : this.catalog.getTables()) {
      assertNotNull(catalog_tbl);
      for (Column catalog_col : catalog_tbl.getColumns()) {
        assertNotNull(catalog_col);
        if (catalog_col.getName().contains("_IATTR")) {
          boolean actual = SQLUtil.isIntegerType(catalog_col.getType());
          assertTrue(catalog_col.getName() + " -> " + catalog_col.getType(), actual);
        }
      }
    }
  }
}
