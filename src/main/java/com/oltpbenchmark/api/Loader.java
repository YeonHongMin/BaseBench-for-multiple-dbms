/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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
<<<<<<< HEAD
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
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.api;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.catalog.AbstractCatalog;
import com.oltpbenchmark.catalog.Column;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.Histogram;
import com.oltpbenchmark.util.SQLUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pavlo
 */
public abstract class Loader<T extends BenchmarkModule> {
  protected static final Logger LOG = LoggerFactory.getLogger(Loader.class);

  protected final T benchmark;

  protected final WorkloadConfiguration workConf;
  protected final double scaleFactor;
  private final Histogram<String> tableSizes = new Histogram<>(true);

  public Loader(T benchmark) {
    this.benchmark = benchmark;
    this.workConf = benchmark.getWorkloadConfiguration();
    this.scaleFactor = workConf.getScaleFactor();
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * Each Loader will generate a list of Runnable objects that will perform the loading operation
   * for the benchmark. The number of threads that will be launched at the same time depends on the
   * number of cores that are available. But they are guaranteed to execute in the order specified
   * in the list. You will have to use your own protections if there are dependencies between
   * threads (i.e., if one table needs to be loaded before another).
   *
   * <p>Each LoaderThread will be given a Connection handle to the DBMS when it is invoked.
   *
   * <p>If the benchmark does <b>not</b> support multi-threaded loading yet, then this method should
   * return null.
   *
   * @return The list of LoaderThreads the framework will launch.
<<<<<<< HEAD
=======
   * 각 Loader는 벤치마크에 대한 로딩 작업을 수행할 Runnable 객체 목록을 생성합니다. 동시에 시작되는 스레드 수는 사용 가능한 코어 수에 따라 달라집니다. 하지만
   * 목록에 지정된 순서대로 실행되는 것이 보장됩니다. 스레드 간에 종속성이 있는 경우(예: 한 테이블이 다른 테이블보다 먼저 로드되어야 하는 경우) 자체 보호 장치를 사용해야
   * 합니다.
   *
   * <p>각 LoaderThread는 호출될 때 DBMS에 대한 Connection 핸들을 받습니다.
   *
   * <p>벤치마크가 아직 다중 스레드 로딩을 지원하지 않는 경우, 이 메서드는 null을 반환해야 합니다.
   *
   * @return 프레임워크가 시작할 LoaderThread 목록
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   */
  public abstract List<LoaderThread> createLoaderThreads() throws SQLException;

  public void addToTableCount(String tableName, int delta) {
    this.tableSizes.put(tableName, delta);
  }

  public Histogram<String> getTableCounts() {
    return (this.tableSizes);
  }

  public DatabaseType getDatabaseType() {
    return (this.workConf.getDatabaseType());
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Get the pre-seeded Random generator for this Loader invocation
=======
   * 이 Loader 호출에 대한 사전 시드된 Random 생성기를 가져옵니다
>>>>>>> master
=======
   * Get the pre-seeded Random generator for this Loader invocation
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  public Random rng() {
    return (this.benchmark.rng());
  }

  /**
   * Method that can be overriden to specifically unload the tables of the database. In the default
   * implementation it checks for tables from the catalog to delete them using SQL. Any subclass can
   * inject custom behavior here.
   *
   * @param catalog The catalog containing all loaded tables
   * @throws SQLException
   */
  public void unload(Connection conn, AbstractCatalog catalog) throws SQLException {

    boolean shouldEscapeNames = this.getDatabaseType().shouldEscapeNames();

    try (Statement st = conn.createStatement()) {
      for (Table catalog_tbl : catalog.getTables()) {
        String tableName = shouldEscapeNames ? catalog_tbl.getEscapedName() : catalog_tbl.getName();
        LOG.debug(String.format("Deleting data from table [%s]", tableName));

        String sql = "DELETE FROM " + tableName;
        st.execute(sql);
      }
    }
  }

  protected void updateAutoIncrement(Connection conn, Column catalog_col, int value)
      throws SQLException {
    String sql = null;
    String seqName = SQLUtil.getSequenceName(conn, getDatabaseType(), catalog_col);
    DatabaseType dbType = getDatabaseType();
    if (seqName != null) {
      if (dbType == DatabaseType.POSTGRES) {
        sql = String.format("SELECT setval('%s', %d)", seqName.toLowerCase(), value);
      } else if (dbType == DatabaseType.SQLSERVER || dbType == DatabaseType.SQLAZURE) {
        sql = String.format("ALTER SEQUENCE [%s] RESTART WITH %d", seqName, value);
      }
    }

    if (sql != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug(
            String.format(
                "Updating %s auto-increment counter %s with value '%d'",
                catalog_col.getName(), seqName, value));
      }
      try (Statement stmt = conn.createStatement()) {
        boolean result = stmt.execute(sql);
        if (LOG.isDebugEnabled()) {
          LOG.debug(String.format("%s => [%s]", sql, result));
        }
      }
    }
  }
}
