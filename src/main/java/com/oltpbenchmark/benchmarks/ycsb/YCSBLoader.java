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

package com.oltpbenchmark.benchmarks.ycsb;

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

class YCSBLoader extends Loader<YCSBBenchmark> {
  private final int num_record;

  public YCSBLoader(YCSBBenchmark benchmark) {
    super(benchmark);
    this.num_record = (int) Math.round(YCSBConstants.RECORD_COUNT * this.scaleFactor);
    if (LOG.isDebugEnabled()) {
      LOG.debug("# of RECORDS:  {}", this.num_record);
    }
  }

  @Override
  public List<LoaderThread> createLoaderThreads() {
    List<LoaderThread> threads = new ArrayList<>();
    int count = 0;
    while (count < this.num_record) {
      final int start = count;
      final int stop = Math.min(start + YCSBConstants.THREAD_BATCH_SIZE, this.num_record);
      threads.add(
          new LoaderThread(this.benchmark) {
            @Override
            public void load(Connection conn) throws SQLException {
              if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("YCSBLoadThread[%d, %d]", start, stop));
              }
              loadRecords(conn, start, stop);
            }
          });
      count = stop;
    }
    return (threads);
  }

  private void loadRecords(Connection conn, int start, int stop) throws SQLException {
    Table catalog_tbl = benchmark.getCatalog().getTable("USERTABLE");

    String sql = SQLUtil.getInsertSQL(catalog_tbl, this.getDatabaseType());
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      long total = 0;
      int batch = 0;
      for (int i = start; i < stop; i++) {
        stmt.setInt(1, i);
        for (int j = 0; j < YCSBConstants.NUM_FIELDS; j++) {
          stmt.setString(j + 2, TextGenerator.randomStr(rng(), benchmark.fieldSize));
        }
        stmt.addBatch();
        total++;
        if (++batch >= workConf.getBatchSize()) {
          stmt.executeBatch();

          batch = 0;
          if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Records Loaded %d / %d", total, this.num_record));
          }
        }
      }
      if (batch > 0) {
        stmt.executeBatch();
        if (LOG.isDebugEnabled()) {
          LOG.debug(String.format("Records Loaded %d / %d", total, this.num_record));
        }
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Finished loading {}", catalog_tbl.getName());
    }
  }
}
