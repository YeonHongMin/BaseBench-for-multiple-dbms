<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한 및 조건을 준수해 주세요.
 *
 */

<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
package com.oltpbenchmark.catalog;

import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.types.SortDirectionType;
import com.oltpbenchmark.util.Pair;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

public final class HSQLDBCatalog implements AbstractCatalog {

  private static final String DB_CONNECTION = "jdbc:hsqldb:mem:";
  private static final String DB_JDBC = "org.hsqldb.jdbcDriver";
  private static final DatabaseType DB_TYPE = DatabaseType.HSQLDB;

  private final BenchmarkModule benchmarkModule;

<<<<<<< HEAD
<<<<<<< HEAD
  private final Map<String, Table> tables = new HashMap<>(); // original table name -> table
  private final Map<String, String>
      originalTableNames; // HSQLDB uppercase table name -> original table name

  private static final Random rand = new Random();

  /** Connection to the HSQLDB instance. */
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  private final Map<String, Table> tables = new HashMap<>(); // 원본 테이블 이름 -> 테이블
  private final Map<String, String> originalTableNames; // HSQLDB가 대문자로 변환한 테이블 이름 -> 원본 테이블 명

  private static final Random rand = new Random();

  /** HSQLDB 인스턴스와의 연결입니다. */
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  private final Connection conn;

  public HSQLDBCatalog(BenchmarkModule benchmarkModule) {
    this.benchmarkModule = benchmarkModule;
    String dbName =
        String.format("catalog-%s-%d.db", this.benchmarkModule.getBenchmarkName(), rand.nextInt());

    Connection conn;
    try {
      Class.forName(DB_JDBC);
      conn =
          DriverManager.getConnection(DB_CONNECTION + dbName + ";sql.syntax_mys=true", null, null);
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
    this.conn = conn;

    this.originalTableNames = this.getOriginalTableNames();
    try {
      this.init();
    } catch (SQLException | IOException e) {
      throw new RuntimeException(
          String.format(
              "Failed to initialize %s database catalog.", this.benchmarkModule.getBenchmarkName()),
          e);
    }
  }

  @Override
  public void close() throws SQLException {
    this.conn.close();
  }

  @Override
  public Collection<Table> getTables() {
    return tables.values();
  }

  @Override
  public Table getTable(String tableName) {
    return tables.get(originalTableNames.get(tableName.toUpperCase()));
  }

  private void init() throws SQLException, IOException {
<<<<<<< HEAD
<<<<<<< HEAD
    // Load the database DDL.
    this.benchmarkModule.createDatabase(DB_TYPE, this.conn);

    // TableName -> ColumnName -> <FKeyTable, FKeyColumn>
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    // 데이터베이스 DDL을 로드합니다.
    this.benchmarkModule.createDatabase(DB_TYPE, this.conn);

    // TableName -> ColumnName -> <외래키 테이블, 외래키 컬럼>
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    Map<String, Map<String, Pair<String, String>>> foreignKeys = new HashMap<>();

    DatabaseMetaData md = conn.getMetaData();
    ResultSet tableRS = md.getTables(null, null, null, new String[] {"TABLE"});
    while (tableRS.next()) {
      String internalTableName = tableRS.getString(3);
      String upperTableName = internalTableName.toUpperCase();
      String originalTableName = originalTableNames.get(upperTableName);

      String tableType = tableRS.getString(4);
      if (!tableType.equalsIgnoreCase("TABLE")) continue;

      Table catalogTable = new Table(originalTableName, "");

<<<<<<< HEAD
<<<<<<< HEAD
      // COLUMNS
=======
      // 컬럼
>>>>>>> master
=======
      // 컬럼
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      try (ResultSet colRS = md.getColumns(null, null, internalTableName, null)) {
        while (colRS.next()) {
          String colName = colRS.getString(4);
          int colType = colRS.getInt(5);
          @SuppressWarnings("unused")
          String colTypeName = colRS.getString(6);
          Integer colSize = colRS.getInt(7);
          boolean colNullable = colRS.getString(18).equalsIgnoreCase("YES");

          Column catalogCol = new Column(colName, "", catalogTable, colType, colSize, colNullable);
<<<<<<< HEAD
<<<<<<< HEAD
          // TODO(WAN): The following block of code was relevant for programmatic CreateDialect
          // support.
          //            i.e., using the HSQLDB catalog instance to automatically create dialects for
          // other DBMSs.
          //            Since we don't add new database support often, and can hand-write most of
          // that,
          //            it is probably worth the tradeoff to have this functionality removed.
          /*
          {
              String colDefaultValue = colRS.getString(13);
              // TODO(WAN): Inherited FIXME autoinc should use colRS.getString(22).toUpperCase().equals("YES")
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
          // TODO(WAN): 아래 코드 블록은 프로그래밍 방식 CreateDialect 지원에 관련된 것이었습니다.
          //            예를 들어 HSQLDB 카탈로그를 이용해 다른 DBMS용 방언을 자동 생성하는 기능입니다.
          //            새 DBMS 지원을 자주 추가하지 않고 대부분 수작업으로 작성하므로,
          //            이 기능을 제거하는 것이 비용 대비 가치가 있어 보입니다.
          /*
          {
              String colDefaultValue = colRS.getString(13);
              // TODO(WAN): 상속된 FIXME autoinc는 colRS.getString(22).toUpperCase().equals("YES")를 사용해야 합니다.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
              boolean colAutoInc = false;
              catalogCol.setDefaultValue(colDefaultValue);
              catalogCol.setAutoInc(colAutoInc);
              catalogCol.setNullable(colNullable);
<<<<<<< HEAD
<<<<<<< HEAD
              // TODO(WAN): Inherited FIXME setSigned
=======
              // TODO(WAN): 상속된 FIXME setSigned
>>>>>>> master
=======
              // TODO(WAN): 상속된 FIXME setSigned
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
          }
          */

          catalogTable.addColumn(catalogCol);
        }
      }

<<<<<<< HEAD
<<<<<<< HEAD
      // TODO(WAN): It looks like the primaryKeyColumns were only used in CreateDialect.
      /*
      {
          // PRIMARY KEYS
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      // TODO(WAN): primaryKeyColumns는 CreateDialect에서만 사용된 것으로 보입니다.
      /*
      {
          // 기본 키
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
          try (ResultSet pkeyRS = md.getPrimaryKeys(null, null, internalTableName)) {
              SortedMap<Integer, String> pkeyCols = new TreeMap<>();
              while (pkeyRS.next()) {
                  String colName = pkeyRS.getString(4);
                  int colIdx = pkeyRS.getShort(5);
<<<<<<< HEAD
<<<<<<< HEAD
                  // TODO(WAN): Is this hack still necessary?
                  //            Previously, the index hack is around SQLite not returning the KEY_SEQ.
=======
                  // TODO(WAN): 이 꼼수가 여전히 필요한가요?
                  //            이전에는 SQLite가 KEY_SEQ를 반환하지 않아 인덱스 꼼수를 사용했습니다.
>>>>>>> master
=======
                  // TODO(WAN): 이 꼼수가 여전히 필요한가요?
                  //            이전에는 SQLite가 KEY_SEQ를 반환하지 않아 인덱스 꼼수를 사용했습니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
                  if (colIdx == 0) colIdx = pkeyCols.size();
                  pkeyCols.put(colIdx, colName);
              }
          }
          catalogTable.setPrimaryKeyColumns(pkeyCols.values());
      }
      */

<<<<<<< HEAD
<<<<<<< HEAD
      // INDEXES
=======
      // 인덱스
>>>>>>> master
=======
      // 인덱스
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      try (ResultSet idxRS = md.getIndexInfo(null, null, internalTableName, false, false)) {
        while (idxRS.next()) {
          int idxType = idxRS.getShort(7);
          if (idxType == DatabaseMetaData.tableIndexStatistic) {
            continue;
          }
          boolean idxUnique = !idxRS.getBoolean(4);
          String idxName = idxRS.getString(6);
          int idxColPos = idxRS.getInt(8) - 1;
          String idxColName = idxRS.getString(9);
          String sort = idxRS.getString(10);
          SortDirectionType idxDirection;
          if (sort != null) {
            idxDirection =
                sort.equalsIgnoreCase("A") ? SortDirectionType.ASC : SortDirectionType.DESC;
          } else {
            idxDirection = null;
          }

          Index catalogIdx = catalogTable.getIndex(idxName);
          if (catalogIdx == null) {
            catalogIdx = new Index(idxName, "", catalogTable, idxType, idxUnique);
            catalogTable.addIndex(catalogIdx);
          }
          catalogIdx.addColumn(idxColName, idxDirection, idxColPos);
        }
      }

<<<<<<< HEAD
<<<<<<< HEAD
      // FOREIGN KEYS
=======
      // 외래키
>>>>>>> master
=======
      // 외래키
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      try (ResultSet fkRS = md.getImportedKeys(null, null, internalTableName)) {
        foreignKeys.put(originalTableName, new HashMap<>());
        while (fkRS.next()) {
          String colName = fkRS.getString(8);
          String fkTableName = originalTableNames.get(fkRS.getString(3).toUpperCase());
          String fkColName = fkRS.getString(4);
          foreignKeys.get(originalTableName).put(colName, Pair.of(fkTableName, fkColName));
        }
      }

<<<<<<< HEAD
<<<<<<< HEAD
      // Register the table to the catalog.
=======
      // 카탈로그에 테이블을 등록합니다.
>>>>>>> master
=======
      // 카탈로그에 테이블을 등록합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      this.tables.put(originalTableName, catalogTable);
    }

    for (Table catalogTable : this.tables.values()) {
      Map<String, Pair<String, String>> fk = foreignKeys.get(catalogTable.getName());
      fk.forEach(
          (colName, fkey) -> {
            Column catalogCol = catalogTable.getColumnByName(colName);

            Table fkeyTable = this.tables.get(fkey.first);
            if (fkeyTable == null) {
              throw new RuntimeException("Unexpected foreign key parent table " + fkey);
            }

            Column fkeyCol = fkeyTable.getColumnByName(fkey.second);
            if (fkeyCol == null) {
              throw new RuntimeException("Unexpected foreign key parent column " + fkey);
            }

            catalogCol.setForeignKey(fkeyCol);
          });
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * HACK: HSQLDB will always uppercase table names. The original table names are extracted from the
   * DDL.
   *
   * @return A map from the original table names to the uppercase HSQLDB table names.
   */
  Map<String, String> getOriginalTableNames() {
    // Get the contents of the HSQLDB DDL for the current benchmark.
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * HACK: HSQLDB는 테이블 이름을 항상 대문자로 변경합니다. 원본 테이블 이름은 DDL에서 추출합니다.
   *
   * @return 원래 테이블 이름에서 HSQLDB 대문자 이름으로 대응되는 맵
   */
  Map<String, String> getOriginalTableNames() {
    // 현재 벤치마크의 HSQLDB DDL 내용을 가져옵니다.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    String ddlContents;
    try {
      String ddlPath = this.benchmarkModule.getWorkloadConfiguration().getDDLPath();
      URL ddlURL;
      if (ddlPath == null) {
        ddlPath = this.benchmarkModule.getDatabaseDDLPath(DatabaseType.HSQLDB);
        ddlURL = Objects.requireNonNull(this.getClass().getResource(ddlPath));
      } else {
        ddlURL = Path.of(ddlPath).toUri().toURL();
      }
      ddlContents = IOUtils.toString(ddlURL, Charset.defaultCharset());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    // Extract and map the original table names to their uppercase versions.
=======
    // 원본 테이블 이름과 대문자 버전을 추출해 매핑합니다.
>>>>>>> master
=======
    // 원본 테이블 이름과 대문자 버전을 추출해 매핑합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    Map<String, String> originalTableNames = new HashMap<>();
    Pattern p = Pattern.compile("CREATE[\\s]+TABLE[\\s]+(.*?)[\\s]+", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(ddlContents);
    while (m.find()) {
      String tableName = m.group(1).trim();
      originalTableNames.put(tableName.toUpperCase(), tableName);
    }
    return originalTableNames;
  }
}
