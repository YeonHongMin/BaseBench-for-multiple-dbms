/*
<<<<<<< HEAD
<<<<<<< HEAD
 *  Copyright 2016 by OLTPBenchmark Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 * Copyright 2016 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 별도 합의가 없다면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다. 라이선스가 허용하는 범위 내에서만 사용하세요.
 *
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 */

package com.oltpbenchmark.benchmarks.noop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oltpbenchmark.api.AbstractTestWorker;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.util.SQLUtil;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import org.junit.Test;

public class TestNoOpWorker extends AbstractTestWorker<NoOpBenchmark> {

  @Override
  public List<Class<? extends Procedure>> procedures() {
    return TestNoOpBenchmark.PROCEDURE_CLASSES;
  }

  @Override
  public Class<NoOpBenchmark> benchmarkClass() {
    return NoOpBenchmark.class;
  }

  @Test
  public void testNoSessionSetupFile() throws Exception {
<<<<<<< HEAD
<<<<<<< HEAD
    // Check that there is no session setup file assigned to the worker's config
=======
    // 워커의 설정에 세션 설정 파일이 할당되지 않았는지 확인합니다
>>>>>>> master
=======
    // Check that there is no session setup file assigned to the worker's config
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    assertNull("Session setup file should be null", this.workConf.getSessionSetupFile());

    List<Worker<? extends BenchmarkModule>> workers = this.benchmark.makeWorkers();
    Worker<?> worker = workers.get(0);
    assertNull(
        "Session setup file should be null",
        worker.getWorkloadConfiguration().getSessionSetupFile());

<<<<<<< HEAD
<<<<<<< HEAD
    // Make sure there are no rows in the table
=======
    // 테이블에 행이 없는지 확인합니다
>>>>>>> master
=======
    // Make sure there are no rows in the table
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    this.testExecuteWork();

    Table catalog_tbl = this.catalog.getTable("FAKE2");
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
