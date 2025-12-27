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

import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.catalog.*;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.types.SortDirectionType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SQLUtil {
  private static final Logger LOG = LoggerFactory.getLogger(SQLUtil.class);

  private static final DateFormat[] timestamp_formats =
      new DateFormat[] {
        new SimpleDateFormat("yyyy-MM-dd"),
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"),
      };

  /**
   * 주어진 객체에서 long 값을 반환합니다. 다양한 DBMS의 서로 다른 경우를 처리합니다.
   *
   * @param obj
   * @return
   */
  public static Long getLong(Object obj) {
    if (obj == null) {
      return (null);
    }

    if (obj instanceof Long) {
      return (Long) obj;
    } else if (obj instanceof Integer) {
      return ((Integer) obj).longValue();
    } else if (obj instanceof BigDecimal) {
      return ((BigDecimal) obj).longValue();
    }

    LOG.warn("BAD BAD BAD: returning null because getLong does not support {}", obj.getClass());

    return (null);
  }

  public static Integer getInteger(Object obj) {
    if (obj == null) {
      return (null);
    }

    if (obj instanceof Integer) {
      return (Integer) obj;
    } else if (obj instanceof Long) {
      return ((Long) obj).intValue();
    } else if (obj instanceof BigDecimal) {
      return ((BigDecimal) obj).intValue();
    }

    LOG.warn("BAD BAD BAD: returning null because getInteger does not support {}", obj.getClass());

    return (null);
  }

  /**
   * 주어진 객체에서 double 값을 반환합니다. 다양한 DBMS의 서로 다른 경우를 처리합니다.
   *
   * @param obj
   * @return
   */
  public static Double getDouble(Object obj) {
    if (obj == null) {
      return (null);
    }

    if (obj instanceof Double) {
      return (Double) obj;
    } else if (obj instanceof Float) {
      return ((Float) obj).doubleValue();
    } else if (obj instanceof BigDecimal) {
      return ((BigDecimal) obj).doubleValue();
    }

    LOG.warn("BAD BAD BAD: returning null because getDouble does not support {}", obj.getClass());

    return (null);
  }

  public static String clobToString(Object obj) {
    try {
      Clob clob = (Clob) obj;
      return clob.getSubString(1, (int) clob.length());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getString(Object obj) {
    if (obj == null) {
      return (null);
    }

    if (obj instanceof String) {
      return (String) obj;
    } else if (obj instanceof BigDecimal bigDecimal) {
      return bigDecimal.toString();
    }

    LOG.warn("BAD BAD BAD: returning null because getString does not support {}", obj.getClass());

    return (null);
  }

  /**
   * Oracle DB 지원: Oracle DDL(예: auctionmark CONFIG_PROFILE 테이블)에서 도입된 TIMESTAMP 필드는
   * OJDBC 전용 {@code oracle.sql.TIMESTAMP} 객체를 생성합니다.
   * {@link #getTimestamp(Object)}는 {@code oracle.sql.TIMESTAMP}를
   * {@code java.sql.TIMESTAMP}로 변환할 수 있어야 합니다.
   *
   * <p>주요 문제는 {@code oracle.sql.TIMESTAMP}가 JDBC에서 사용할 수 없으므로,
   * 일반적으로 타입을 가져오고 해결하려고 하면 다른 데이터베이스 프로파일이 깨질 수 있다는 것입니다.
   * 이는 OJDBC 전용 클래스와 메서드를 리플렉션으로 로드하여 해결할 수 있습니다.
   */
  private static final Class<?> ORACLE_TIMESTAMP;

  private static final Method TIMESTAMP_VALUE_METHOD;

  static {
    Method timestampValueMethod;
    Class<?> oracleTimestamp;
    try {
      // If oracle.sql.TIMESTAMP can be loaded
      oracleTimestamp = Class.forName("oracle.sql.TIMESTAMP");
      // Then java.sql.Timestamp oracle.sql.TIMESTAMP.timestampValue() can be loaded
      timestampValueMethod = oracleTimestamp.getDeclaredMethod("timestampValue");
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      oracleTimestamp = null;
      timestampValueMethod = null;
    }
    // If loading is successful then both variables won't be null.
    TIMESTAMP_VALUE_METHOD = timestampValueMethod;
    ORACLE_TIMESTAMP = oracleTimestamp;
  }

  /**
   * 주어진 객체에서 Timestamp를 반환합니다. 다양한 DBMS의 서로 다른 경우를 처리합니다.
   *
   * @param obj
   * @return
   */
  public static Timestamp getTimestamp(Object obj) {
    if (obj == null) {
      return (null);
    }

    if (obj instanceof Timestamp) {
      return (Timestamp) obj;
    } else if (obj instanceof Date) {
      return new Timestamp(((Date) obj).getTime());
    } else if (ORACLE_TIMESTAMP != null && ORACLE_TIMESTAMP.isInstance(obj)) {
      try {
        // https://docs.oracle.com/en/database/oracle/oracle-database/21/jajdb/oracle/sql/TIMESTAMP.html#timestampValue__
        return (Timestamp) TIMESTAMP_VALUE_METHOD.invoke(ORACLE_TIMESTAMP.cast(obj));
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    Long timestamp = SQLUtil.getLong(obj);
    return (timestamp != null ? new Timestamp(timestamp) : null);
  }

  /**
   * 주어진 Column에 대한 내부 시퀀스 이름을 반환합니다.
   *
   * @param conn
   * @param dbType
   * @param catalog_col
   * @return
   */
  public static String getSequenceName(Connection conn, DatabaseType dbType, Column catalog_col)
      throws SQLException {
    Table catalog_tbl = catalog_col.getTable();

    String seqName = null;
    String sql = null;
    if (dbType == DatabaseType.POSTGRES) {
      sql =
          String.format(
              "SELECT pg_get_serial_sequence('%s', '%s')",
              catalog_tbl.getName(), catalog_col.getName());
    } else if (dbType == DatabaseType.SQLSERVER || dbType == DatabaseType.SQLAZURE) {
      // NOTE: This likely only handles certain syntaxes for defaults.
      sql =
          String.format(
              """
SELECT REPLACE(REPLACE([definition], '(NEXT VALUE FOR [', ''), '])', '') AS seq
FROM sys.default_constraints dc
JOIN sys.columns c ON c.default_object_id=dc.object_id
JOIN sys.tables t ON c.object_id=t.object_id
WHERE t.name='%s' AND c.name='%s'
""",
              catalog_tbl.getName(), catalog_col.getName());
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Unexpected request for sequence name on {} using {}", catalog_col, dbType);
      }
    }

    if (sql != null) {
      try (Statement stmt = conn.createStatement()) {
        try (ResultSet result = stmt.executeQuery(sql)) {
          if (result.next()) {
            seqName = result.getString(1);
          }
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug(String.format("%s => [%s]", sql, seqName));
        }
      }
    }

    return seqName;
  }

  /**
   * IDENTITY 열이 있는 주어진 테이블을 명시적 값으로 삽입할 수 있도록 표시합니다.
   *
   * @param conn
   * @param dbType
   * @param catalog_tbl
   * @param on
   */
  public static void setIdentityInsert(
      Connection conn, DatabaseType dbType, Table catalog_tbl, boolean on) throws SQLException {
    String sql = null;
    if (dbType == DatabaseType.SQLSERVER || dbType == DatabaseType.SQLAZURE) {
      sql = "SET IDENTITY_INSERT " + catalog_tbl.getName() + " " + (on ? "ON" : "OFF");
    }

    if (sql != null) {
      try (Statement stmt = conn.createStatement()) {
        boolean result = stmt.execute(sql);
        if (LOG.isDebugEnabled()) {
          LOG.debug(String.format("%s => [%s]", sql, result));
          SQLWarning warnings = stmt.getWarnings();
          if (warnings != null) {
            LOG.debug(warnings.toString());
          }
        }
      }
    }
  }

  public static Object[] getRowAsArray(ResultSet rs) throws SQLException {
    ResultSetMetaData rs_md = rs.getMetaData();
    int num_cols = rs_md.getColumnCount();
    Object[] data = new Object[num_cols];
    for (int i = 0; i < num_cols; i++) {
      data[i] = rs.getObject(i + 1);
    }
    return data;
  }

  public static List<Object[]> toList(ResultSet rs) throws SQLException {
    ResultSetMetaData rs_md = rs.getMetaData();
    int num_cols = rs_md.getColumnCount();

    List<Object[]> results = new ArrayList<>();
    while (rs.next()) {
      Object[] row = new Object[num_cols];
      for (int i = 0; i < num_cols; i++) {
        row[i] = rs.getObject(i + 1);
      }
      results.add(row);
    }

    return results;
  }

  /**
   * 주어진 값의 문자열 표현을 sqlType에 따라 적절한 객체로 변환합니다.
   *
   * @param sqlType
   * @param value
   * @return
   * @see java.sql.Types
   */
  public static Object castValue(int sqlType, String value) {
    Object ret = null;
    switch (sqlType) {
      case Types.CHAR:
      case Types.VARCHAR:
      case Types.NCHAR:
      case Types.NVARCHAR:
        {
          ret = value;
          break;
        }
      case Types.TINYINT:
      case Types.SMALLINT:
        {
          ret = Short.parseShort(value);
          break;
        }
      case Types.INTEGER:
        {
          ret = Integer.parseInt(value);
          break;
        }
      case Types.BIGINT:
        {
          ret = Long.parseLong(value);
          break;
        }
      case Types.BOOLEAN:
        {
          ret = Boolean.parseBoolean(value);
          break;
        }
      case Types.DECIMAL:
      case Types.REAL:
      case Types.NUMERIC:
      case Types.DOUBLE:
        {
          ret = Double.parseDouble(value);
          break;
        }
      case Types.FLOAT:
        {
          ret = Float.parseFloat(value);
          break;
        }
      case Types.DATE:
      case Types.TIME:
      case Types.TIMESTAMP:
        {
          for (DateFormat f : timestamp_formats) {
            try {
              ret = f.parse(value);
            } catch (ParseException ex) {
              // Ignore...
            }
            if (ret != null) {
              break;
            }
          }
          if (ret == null) {
            throw new RuntimeException("Failed to parse timestamp '" + value + "'");
          }
          break;
        }
      default:
        LOG.warn("Unexpected SQL Type '{}' for value '{}'", sqlType, value);
    }
    return (ret);
  }

  /**
   * 주어진 sqlType 식별자가 String 데이터 타입인 경우 true를 반환합니다.
   *
   * @param sqlType
   * @return
   * @see java.sql.Types
   */
  public static boolean isStringType(int sqlType) {
    switch (sqlType) {
      case Types.CHAR:
      case Types.VARCHAR:
      case Types.NCHAR:
      case Types.NVARCHAR:
        {
          return (true);
        }
      default:
        return (false);
    }
  }

  /**
   * 주어진 sqlType 식별자가 Integer 데이터 타입인 경우 true를 반환합니다.
   *
   * @param sqlType
   * @return
   * @see java.sql.Types
   */
  public static boolean isIntegerType(int sqlType) {
    switch (sqlType) {
      case Types.TINYINT:
      case Types.SMALLINT:
      case Types.INTEGER:
      case Types.BIGINT:
        {
          return (true);
        }
      default:
        return (false);
    }
  }

  /**
   * 레코드 수를 계산하기 위한 COUNT(*) SQL을 반환합니다.
   *
   * @param dbType
   * @param catalog_tbl
   * @return select count 실행을 위한 SQL
   */
  public static String getCountSQL(DatabaseType dbType, Table catalog_tbl) {
    return SQLUtil.getCountSQL(dbType, catalog_tbl, "*");
  }

  /**
   * 레코드 수를 계산하기 위한 COUNT() SQL을 반환합니다. col 매개변수를 카운트할 열로 사용합니다.
   *
   * @param dbType
   * @param catalog_tbl
   * @param col
   * @return select count 실행을 위한 SQL
   */
  public static String getCountSQL(DatabaseType dbType, Table catalog_tbl, String col) {
    String tableName =
        (dbType.shouldEscapeNames() ? catalog_tbl.getEscapedName() : catalog_tbl.getName());
    return String.format("SELECT COUNT(%s) FROM %s", col, tableName.trim());
  }

  /**
   * 이 테이블에 대한 'INSERT' SQL 문자열을 배치 크기 1로 자동 생성합니다.
   *
   * @param catalog_tbl
   * @param db_type
   * @param exclude_columns
   * @return
   */
  public static String getInsertSQL(
      Table catalog_tbl, DatabaseType db_type, int... exclude_columns) {
    return SQLUtil.getInsertSQL(catalog_tbl, db_type, 1, exclude_columns);
  }

  /**
   * 이 테이블에 대한 'INSERT' SQL 문자열을 자동 생성합니다.
   *
   * @param catalog_tbl
   * @param db_type
   * @param batchSize insert에 포함되어야 하는 매개변수 세트의 수
   * @param exclude_columns
   * @return
   */
  public static String getInsertSQL(
      Table catalog_tbl, DatabaseType db_type, int batchSize, int... exclude_columns) {
    boolean show_cols = db_type.shouldIncludeColumnNames();
    boolean escape_names = db_type.shouldEscapeNames();

    StringBuilder sb = new StringBuilder();
    if (db_type.equals(DatabaseType.PHOENIX)) {
      sb.append("UPSERT");
    } else {
      sb.append("INSERT");
    }
    sb.append(" INTO ").append(escape_names ? catalog_tbl.getEscapedName() : catalog_tbl.getName());

    StringBuilder values = new StringBuilder();
    boolean first;

    // Column Names
    // XXX: Disabled because of case issues with HSQLDB
    if (show_cols) {
      sb.append(" (");
    }
    first = true;

    // These are the column offset that we want to exclude
    Set<Integer> excluded = new HashSet<>();
    for (int ex : exclude_columns) {
      excluded.add(ex);
    }

    for (Column catalog_col : catalog_tbl.getColumns()) {
      if (excluded.contains(catalog_col.getIndex())) {
        continue;
      }
      if (!first) {
        if (show_cols) {
          sb.append(", ");
        }
        values.append(", ");
      }
      if (show_cols) {
        sb.append(escape_names ? catalog_col.getEscapedName() : catalog_col.getName());
      }
      values.append("?");
      first = false;
    }
    if (show_cols) {
      sb.append(")");
    }

    // Values
    sb.append(" VALUES ");
    for (int i = 0; i < batchSize; i++) {
      sb.append("(").append(values.toString()).append(")");
    }

    return (sb.toString());
  }

  public static String getMaxColSQL(DatabaseType dbType, Table catalog_tbl, String col) {
    String tableName =
        (dbType.shouldEscapeNames() ? catalog_tbl.getEscapedName() : catalog_tbl.getName());
    return String.format("SELECT MAX(%s) FROM %s", col, tableName);
  }

  public static String selectColValues(DatabaseType databaseType, Table catalog_tbl, String col) {
    String tableName =
        (databaseType.shouldEscapeNames() ? catalog_tbl.getEscapedName() : catalog_tbl.getName());
    return String.format("SELECT %s FROM %s", col, tableName);
  }

  /** 데이터베이스에서 카탈로그를 추출합니다. */
  public static AbstractCatalog getCatalog(
      BenchmarkModule benchmarkModule, DatabaseType databaseType, Connection connection)
      throws SQLException {
    switch (databaseType) {
      case NOISEPAGE: // fall-through
      case HSQLDB:
        return getCatalogHSQLDB(benchmarkModule);
      default:
        return getCatalogDirect(databaseType, connection);
    }
  }

  /**
   * 모든 카탈로그 정보를 추출하기 위해 HSQLDB의 인메모리 인스턴스를 생성합니다.
   *
   * <p>아직 모든 SQL 표준을 지원하지 않을 수 있는 데이터베이스를 지원합니다.
   *
   * @return
   */
  private static AbstractCatalog getCatalogHSQLDB(BenchmarkModule benchmarkModule) {
    return new HSQLDBCatalog(benchmarkModule);
  }

  /** 데이터베이스에서 직접 카탈로그 정보를 추출합니다. */
  private static AbstractCatalog getCatalogDirect(DatabaseType databaseType, Connection connection)
      throws SQLException {
    DatabaseMetaData md = connection.getMetaData();

    String separator = md.getIdentifierQuoteString();
    String catalog = connection.getCatalog();
    String schema = connection.getSchema();

    Map<String, Table> tables = new HashMap<>();

    List<String> excludedColumns = new ArrayList<>();

    if (databaseType.equals(DatabaseType.COCKROACHDB)) {
      // cockroachdb has a hidden column called "ROWID" that should not be directly used via the
      // catalog
      excludedColumns.add("ROWID");
    }

    try (ResultSet table_rs = md.getTables(catalog, schema, null, new String[] {"TABLE"})) {
      while (table_rs.next()) {

        String table_type = table_rs.getString("TABLE_TYPE");
        if (!table_type.equalsIgnoreCase("TABLE")) {
          continue;
        }

        String table_name = table_rs.getString("TABLE_NAME");
        Table catalog_tbl = new Table(table_name, separator);

        try (ResultSet col_rs = md.getColumns(catalog, schema, table_name, null)) {
          while (col_rs.next()) {
            String col_name = col_rs.getString("COLUMN_NAME");

            if (excludedColumns.contains(col_name.toUpperCase())) {
              LOG.debug(
                  "found excluded column [{}] for in database type [{}].  Skipping...",
                  col_name,
                  databaseType);
              continue;
            }

            int col_type = col_rs.getInt("DATA_TYPE");
            Integer col_size = col_rs.getInt("COLUMN_SIZE");
            boolean col_nullable = col_rs.getString("IS_NULLABLE").equalsIgnoreCase("YES");

            Column catalog_col =
                new Column(col_name, separator, catalog_tbl, col_type, col_size, col_nullable);

            catalog_tbl.addColumn(catalog_col);
          }
        }

        try (ResultSet idx_rs = md.getIndexInfo(catalog, schema, table_name, false, false)) {
          while (idx_rs.next()) {
            int idx_type = idx_rs.getShort("TYPE");
            if (idx_type == DatabaseMetaData.tableIndexStatistic) {
              continue;
            }
            boolean idx_unique = (!idx_rs.getBoolean("NON_UNIQUE"));
            String idx_name = idx_rs.getString("INDEX_NAME");
            int idx_col_pos = idx_rs.getInt("ORDINAL_POSITION") - 1;
            String idx_col_name = idx_rs.getString("COLUMN_NAME");
            String sort = idx_rs.getString("ASC_OR_DESC");
            SortDirectionType idx_direction;
            if (sort != null) {
              idx_direction =
                  sort.equalsIgnoreCase("A") ? SortDirectionType.ASC : SortDirectionType.DESC;
            } else {
              idx_direction = null;
            }

            Index catalog_idx = catalog_tbl.getIndex(idx_name);
            if (catalog_idx == null) {
              catalog_idx = new Index(idx_name, separator, catalog_tbl, idx_type, idx_unique);
              catalog_tbl.addIndex(catalog_idx);
            }

            catalog_idx.addColumn(idx_col_name, idx_direction, idx_col_pos);
          }
        }

        tables.put(table_name, catalog_tbl);
      }
    }

    for (Table table : tables.values()) {
      try (ResultSet fk_rs = md.getImportedKeys(catalog, schema, table.getName())) {
        while (fk_rs.next()) {
          String colName = fk_rs.getString("FKCOLUMN_NAME");

          String fk_tableName = fk_rs.getString("PKTABLE_NAME");
          String fk_colName = fk_rs.getString("PKCOLUMN_NAME");

          Table fk_table = tables.get(fk_tableName);
          Column fk_col = fk_table.getColumnByName(fk_colName);

          Column catalog_col = table.getColumnByName(colName);
          catalog_col.setForeignKey(fk_col);
        }
      }
    }

    return new Catalog(tables);
  }

  public static boolean isDuplicateKeyException(Exception ex) {
    // MYSQL
    if (ex instanceof SQLIntegrityConstraintViolationException) {
      return (true);
    } else if (ex instanceof SQLException) {
      SQLException sqlEx = (SQLException) ex;
      String sqlState = sqlEx.getSQLState();
      String sqlMessage = sqlEx.getMessage();

      // POSTGRES
      if (sqlState != null && sqlState.contains("23505")) {
        return (true);
      }
      // SQLSERVER
      else if (sqlState != null && sqlState.equals("23000") && sqlEx.getErrorCode() == 2627) {
        return (true);
      }
      // SQLITE
      else if (sqlEx.getErrorCode() == 19
          && sqlMessage != null
          && sqlMessage.contains("SQLITE_CONSTRAINT_UNIQUE")) {
        return (true);
      }
    }
    return (false);
  }

  /**
   * SqlException이 연결 오류에 관한 것인지 확인합니다.
   *
   * @param ex SqlException
   * @return boolean
   */
  public static boolean isConnectionErrorException(SQLException ex) {
    if (ex instanceof SQLNonTransientConnectionException
        || ex instanceof SQLTransientConnectionException
        || ex.getMessage().equals("Connection reset")
        || ex.getMessage().equals("The connection is closed.")
        || ex.getMessage().equals("Read timed out.")
        || ex.getMessage().contains("The connection has been closed.")
        || ex.getMessage().contains("Can not read response from server.")
        || ex.getMessage().endsWith("connection was unexpectedly lost.")
        || ex.getMessage().endsWith("Command could not be timed out. Reason: Socket closed")
        || ex.getMessage().endsWith("No operations allowed after connection closed")
        || ex.getMessage().contains("Communications link failure")) {
      return true;
    }

    return false;
  }

  /**
   * 연결이 정상인지 확인합니다.
   *
   * @param conn
   * @param checkValid 연결에 대해 isValid() 검사를 수행할지 여부. 참고: 실제로 noop 쿼리를 실행할 수 있으므로 비용이 클 수 있습니다.
   * @return boolean
   * @throws SQLException
   */
  public static boolean isConnectionOK(Connection conn, boolean checkValid) throws SQLException {
    boolean ret = conn != null && !conn.isClosed();
    if (checkValid) {
      // isValid() can be expensive, so only do it if we have to and timeout after 1 second
      ret = ret && conn.isValid(1);
    }
    return ret;
  }

  /**
   * isValid() 검사를 수행하지 않고 연결이 정상인지 확인합니다.
   *
   * @param conn
   * @return boolean
   * @throws SQLException
   */
  public static boolean isConnectionOK(Connection conn) throws SQLException {
    return isConnectionOK(conn, false);
  }
}
