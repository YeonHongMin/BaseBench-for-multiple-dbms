/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
=======
 * Apache License, Version 2.0 (이하 "라이센스")에 따라 라이센스가 부여됩니다.
 * 이 파일은 라이센스에 따라 사용할 수 있으며, 라이센스에 따라 사용하지 않는 한
 * 사용할 수 없습니다. 라이센스 사본은 다음에서 얻을 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 적용 가능한 법률에 의해 요구되거나 서면으로 합의되지 않는 한, 라이센스에 따라
 * 배포되는 소프트웨어는 "있는 그대로" 배포되며, 명시적이거나 묵시적인 어떠한 종류의
 * 보증이나 조건도 없습니다. 라이센스에 따른 권한 및 제한 사항에 대한 자세한 내용은
 * 라이센스를 참조하십시오.
>>>>>>> master
 *
 */

package com.oltpbenchmark.api;

import com.oltpbenchmark.jdbc.AutoIncrementPreparedStatement;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.MonitoringUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Procedure {
  private static final Logger LOG = LoggerFactory.getLogger(Procedure.class);

  private final String procName;
  private DatabaseType dbType;

  public DatabaseType getDbType() {
    return dbType;
  }

  private Map<String, SQLStmt> name_stmt_xref;

<<<<<<< HEAD
  /** Constructor */
=======
  /** 생성자 */
>>>>>>> master
  protected Procedure() {
    this.procName = this.getClass().getSimpleName();
  }

  /**
<<<<<<< HEAD
   * Initialize all of the SQLStmt handles. This must be called separately from the constructor,
   * otherwise we can't get access to all of our SQLStmts.
=======
   * 모든 SQLStmt 핸들을 초기화합니다. 생성자와 별도로 호출해야 하며, 그렇지 않으면 모든 SQLStmt에 액세스할 수 없습니다.
>>>>>>> master
   *
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  protected final <T extends Procedure> T initialize(DatabaseType dbType) {
    this.dbType = dbType;
    this.name_stmt_xref = Procedure.getStatements(this);

    if (LOG.isDebugEnabled()) {
      LOG.debug(
          String.format(
              "Initialized %s with %d SQLStmts: %s",
              this, this.name_stmt_xref.size(), this.name_stmt_xref.keySet()));
    }
    return ((T) this);
  }

<<<<<<< HEAD
  /** Return the name of this Procedure */
=======
  /** 이 Procedure의 이름을 반환합니다 */
>>>>>>> master
  protected final String getProcedureName() {
    return (this.procName);
  }

  /**
<<<<<<< HEAD
   * Return a PreparedStatement for the given SQLStmt handle The underlying Procedure API will make
   * sure that the proper SQL for the target DBMS is used for this SQLStmt. This will automatically
   * call setObject for all the parameters you pass in
=======
   * 주어진 SQLStmt 핸들에 대한 PreparedStatement를 반환합니다. 기본 Procedure API는 이 SQLStmt에 대해 대상 DBMS에 적합한 SQL이
   * 사용되도록 보장합니다. 전달한 모든 매개변수에 대해 자동으로 setObject를 호출합니다.
>>>>>>> master
   *
   * @param conn
   * @param stmt
   * @param params
   * @return
   * @throws SQLException
   */
  public final PreparedStatement getPreparedStatement(
      Connection conn, SQLStmt stmt, Object... params) throws SQLException {
    PreparedStatement pStmt = this.getPreparedStatementReturnKeys(conn, stmt, null);
    for (int i = 0; i < params.length; i++) {
      pStmt.setObject(i + 1, params[i]);
    }
    return (pStmt);
  }

  /**
<<<<<<< HEAD
   * Return a PreparedStatement for the given SQLStmt handle The underlying Procedure API will make
   * sure that the proper SQL for the target DBMS is used for this SQLStmt.
=======
   * 주어진 SQLStmt 핸들에 대한 PreparedStatement를 반환합니다. 기본 Procedure API는 이 SQLStmt에 대해 대상 DBMS에 적합한 SQL이
   * 사용되도록 보장합니다.
>>>>>>> master
   *
   * @param conn
   * @param stmt
   * @param is
   * @return
   * @throws SQLException
   */
  public final PreparedStatement getPreparedStatementReturnKeys(
      Connection conn, SQLStmt stmt, int[] is) throws SQLException {

    PreparedStatement pStmt = null;

<<<<<<< HEAD
    // HACK: If the target system is Postgres, wrap the PreparedStatement in a special
    //       one that fakes the getGeneratedKeys().
=======
    // 해킹: 대상 시스템이 Postgres인 경우, getGeneratedKeys()를 가짜로 만드는
    //       특수한 것으로 PreparedStatement를 래핑합니다.
>>>>>>> master
    if (is != null
        && (this.dbType == DatabaseType.POSTGRES
            || this.dbType == DatabaseType.COCKROACHDB
            || this.dbType == DatabaseType.SQLSERVER
            || this.dbType == DatabaseType.SQLAZURE)) {
      pStmt = new AutoIncrementPreparedStatement(this.dbType, conn.prepareStatement(stmt.getSQL()));
    }
    // Everyone else can use the regular getGeneratedKeys() method
    else if (is != null) {
      pStmt = conn.prepareStatement(stmt.getSQL(), is);
    }
    // They don't care about keys
    else {
      pStmt = conn.prepareStatement(stmt.getSQL());
    }

    return (pStmt);
  }

  /**
   * Fetch the SQL from the dialect map
   *
   * @param dialects
   */
  protected final void loadSQLDialect(StatementDialects dialects) {
    Collection<String> stmtNames = dialects.getStatementNames(this.procName);
    if (stmtNames == null) {
      return;
    }
    for (String stmtName : stmtNames) {
      String sql = dialects.getSQL(this.procName, stmtName);

      SQLStmt stmt = this.name_stmt_xref.get(stmtName);
      if (LOG.isDebugEnabled()) {
        LOG.debug(
            String.format(
                "Setting %s SQL dialect for %s.%s",
                dialects.getDatabaseType(), this.procName, stmtName));
      }
      if (stmt == null) {
        throw new RuntimeException(
            String.format(
                "Dialect file contains an unknown statement: Procedure %s, Statement %s",
                this.procName, stmtName));
      }
      stmt.setSQL(sql);
    }
  }

  /** Enable monitoring for this procedure by adding a monitoring prefixes. */
  protected final void enabledAdvancedMonitoring() {
    for (String stmtName : this.getStatements().keySet()) {
      SQLStmt stmt = this.name_stmt_xref.get(stmtName);
      LOG.debug("Enabling advanced monitoring for query {}.", stmtName);
      // Create monitoring prefix.
      String prefix = MonitoringUtil.getMonitoringMarker();
      prefix = prefix.replace(MonitoringUtil.getMonitoringQueryId(), stmtName);
      // Update SQL string.
      stmt.setSQL(prefix + stmt.getSQL());
    }
  }

  /**
   * Hook for testing
   *
   * @return
   */
  protected final Map<String, SQLStmt> getStatements() {
    return (Collections.unmodifiableMap(this.name_stmt_xref));
  }

  protected static Map<String, SQLStmt> getStatements(Procedure proc) {
    Class<? extends Procedure> c = proc.getClass();
    Map<String, SQLStmt> stmts = new HashMap<>();
    for (Field f : c.getDeclaredFields()) {
      int modifiers = f.getModifiers();
      if (!Modifier.isTransient(modifiers)
          && Modifier.isPublic(modifiers)
          && !Modifier.isStatic(modifiers)) {
        try {
          Object o = f.get(proc);
          if (o instanceof SQLStmt) {
            stmts.put(f.getName(), (SQLStmt) o);
          }
        } catch (Exception ex) {
          throw new RuntimeException("Failed to retrieve " + f + " from " + c.getSimpleName(), ex);
        }
      }
    }
    return (stmts);
  }

  @Override
  public String toString() {
    return (this.procName);
  }

  /**
   * Thrown from a Procedure to indicate to the Worker that the procedure should be aborted and
   * rolled back.
   */
  public static class UserAbortException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    /**
     * Default Constructor
     *
     * @param msg
     * @param ex
     */
    public UserAbortException(String msg, Throwable ex) {
      super(msg, ex);
    }

    /** Constructs a new UserAbortException with the specified detail message. */
    public UserAbortException(String msg) {
      this(msg, null);
    }
  }
}
