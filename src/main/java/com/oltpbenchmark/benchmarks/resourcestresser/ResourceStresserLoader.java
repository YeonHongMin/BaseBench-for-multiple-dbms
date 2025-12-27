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

package com.oltpbenchmark.benchmarks.resourcestresser;

import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.LoaderThread;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.util.SQLUtil;
import com.oltpbenchmark.util.TextGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ResourceStresserLoader extends Loader<ResourceStresserBenchmark> {

  private final int numEmployees;

  public ResourceStresserLoader(ResourceStresserBenchmark benchmark) {
    super(benchmark);
    this.numEmployees = (int) (this.scaleFactor * ResourceStresserConstants.RECORD_COUNT);
    if (LOG.isDebugEnabled()) {
      LOG.debug("# of EMPLOYEES:  {}", this.numEmployees);
    }
  }

  @Override
  public List<LoaderThread> createLoaderThreads() {
    List<LoaderThread> threads = new ArrayList<>();
    threads.add(
        new LoaderThread(this.benchmark) {
          @Override
          public void load(Connection conn) throws SQLException {
            loadTable(conn, ResourceStresserConstants.TABLENAME_CPUTABLE);
          }
        });
    threads.add(
        new LoaderThread(this.benchmark) {
          @Override
          public void load(Connection conn) throws SQLException {
            loadTable(conn, ResourceStresserConstants.TABLENAME_IOTABLE);
          }
        });
    threads.add(
        new LoaderThread(this.benchmark) {
          @Override
          public void load(Connection conn) throws SQLException {
            loadTable(conn, ResourceStresserConstants.TABLENAME_IOTABLESMALLROW);
          }
        });
    threads.add(
        new LoaderThread(this.benchmark) {
          @Override
          public void load(Connection conn) throws SQLException {
            loadTable(conn, ResourceStresserConstants.TABLENAME_LOCKTABLE);
          }
        });
    return (threads);
  }

  private void loadTable(Connection conn, String tableName) throws SQLException {
    Table catalog_tbl = this.benchmark.getCatalog().getTable(tableName);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Start loading {}", tableName);
    }
    String sql = SQLUtil.getInsertSQL(catalog_tbl, this.getDatabaseType());
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      int batch = 0;
      int i;
      for (i = 0; i < this.numEmployees; ++i) {
        stmt.setInt(1, i);
        if (tableName.equals(ResourceStresserConstants.TABLENAME_CPUTABLE)) {
          stmt.setString(
              2, TextGenerator.randomStr(rng(), ResourceStresserConstants.STRING_LENGTH));
        } else if (tableName.equals(ResourceStresserConstants.TABLENAME_IOTABLE)) {
          for (int j = 2; j <= catalog_tbl.getColumnCount(); ++j) {
            stmt.setString(
                j, TextGenerator.randomStr(rng(), ResourceStresserConstants.STRING_LENGTH));
          }
        } else {
          stmt.setInt(2, rng().nextInt());
        }

        stmt.addBatch();
        if (++batch >= workConf.getBatchSize()) {
          int[] result = stmt.executeBatch();
          assert result != null;

          batch = 0;
          if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Records Loaded %d / %d", i + 1, this.numEmployees));
          }
        }
      }
      if (batch > 0) {
        stmt.executeBatch();
        if (LOG.isDebugEnabled()) {
          LOG.debug(String.format("Records Loaded %d / %d", i, this.numEmployees));
        }
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Finished loading {}", tableName);
    }
  }
}
