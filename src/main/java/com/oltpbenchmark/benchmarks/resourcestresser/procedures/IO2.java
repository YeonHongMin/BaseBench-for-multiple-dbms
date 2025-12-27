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

package com.oltpbenchmark.benchmarks.resourcestresser.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.resourcestresser.ResourceStresserConstants;
import com.oltpbenchmark.benchmarks.resourcestresser.ResourceStresserWorker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * io2Transaction deals with a table that has much smaller rows. It runs a given number of updates,
 * where each update only changes one row.
 */
public class IO2 extends Procedure {
  private static final Logger LOG = LoggerFactory.getLogger(IO2.class);

  public final SQLStmt ioUpdate =
      new SQLStmt(
          "UPDATE "
              + ResourceStresserConstants.TABLENAME_IOTABLESMALLROW
              + " SET flag1 = ? WHERE empid = ?");

  public void run(
      Connection conn,
      int myId,
      int howManyUpdatesPerTransaction,
      boolean makeSureWorkerSetFitsInMemory,
      int keyRange)
      throws SQLException {

    // int keyRange = (makeSureWorkerSetFitsInMemory ? 16777216 / 160 : 167772160 / 160); // FIXME
    int startingKey = myId * keyRange;
    // int lastKey = (myId + 1) * keyRange - 1;

    for (int up = 0; up < howManyUpdatesPerTransaction; ++up) {
      int key = ResourceStresserWorker.gen.nextInt(keyRange) + startingKey;
      int value = ResourceStresserWorker.gen.nextInt();
      try (PreparedStatement stmt = this.getPreparedStatement(conn, ioUpdate)) {
        stmt.setInt(1, value);
        stmt.setInt(2, key);

        int result = stmt.executeUpdate();
        if (result != 1) {
          LOG.warn("supposedtochange=" + 1 + " but rc={}", result);
        }
      }
    }
  }
}
