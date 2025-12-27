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

package com.oltpbenchmark.benchmarks.voter;

import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.LoaderThread;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.util.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class VoterLoader extends Loader<VoterBenchmark> {

  public VoterLoader(VoterBenchmark benchmark) {
    super(benchmark);
  }

  @Override
  public List<LoaderThread> createLoaderThreads() {
    List<LoaderThread> threads = new ArrayList<>();

<<<<<<< HEAD
    // CONTESTANTS
=======
    // 후보자
>>>>>>> master
    threads.add(
        new LoaderThread(this.benchmark) {
          @Override
          public void load(Connection conn) throws SQLException {
            loadContestants(conn);
          }
        });

<<<<<<< HEAD
    // LOCATIONS
=======
    // 위치
>>>>>>> master
    threads.add(
        new LoaderThread(this.benchmark) {
          @Override
          public void load(Connection conn) throws SQLException {
            loadLocations(conn);
          }
        });

    return threads;
  }

  private void loadContestants(Connection conn) throws SQLException {
    Table catalog_tbl = benchmark.getCatalog().getTable(VoterConstants.TABLENAME_CONTESTANTS);
    try (PreparedStatement ps =
        conn.prepareStatement(SQLUtil.getInsertSQL(catalog_tbl, this.getDatabaseType()))) {

      for (int i = 0; i < this.benchmark.numContestants; i++) {
        ps.setInt(1, i + 1);
        ps.setString(2, VoterConstants.CONTESTANT_NAMES[i]);
        ps.addBatch();
      }
      ps.executeBatch();
    }
  }

  private void loadLocations(Connection conn) throws SQLException {
    Table catalog_tbl = benchmark.getCatalog().getTable(VoterConstants.TABLENAME_LOCATIONS);
    try (PreparedStatement ps =
        conn.prepareStatement(SQLUtil.getInsertSQL(catalog_tbl, this.getDatabaseType()))) {

      for (int i = 0; i < VoterConstants.AREA_CODES.length; i++) {
        ps.setShort(1, VoterConstants.AREA_CODES[i]);
        ps.setString(2, VoterConstants.STATE_CODES[i]);
        ps.addBatch();
      }
      ps.executeBatch();
    }
  }
}
