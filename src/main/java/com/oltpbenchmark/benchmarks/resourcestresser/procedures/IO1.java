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

public class IO1 extends Procedure {
  private static final Logger LOG = LoggerFactory.getLogger(IO1.class);

  public final SQLStmt ioUpdate;

  {
    String sql =
        "UPDATE "
            + ResourceStresserConstants.TABLENAME_IOTABLE
            + " SET %s WHERE empid >= ? AND empid < ?";
    String setClause = "";
    for (int col = 1; col <= ResourceStresserWorker.IO1_howManyColsPerRow; ++col) {
      setClause = setClause + (col > 1 ? "," : "") + " data" + col + "=?";
    }
    this.ioUpdate = new SQLStmt(String.format(sql, setClause));
  }

  public void run(
      Connection conn,
      int myId,
      int howManyColsPerRow,
      int howManyUpdatesPerTransaction,
      int howManyRowsPerUpdate,
      int keyRange)
      throws SQLException {

    // int keyRange = 20; //1024000 / 200; // FIXME
    int startingKey = myId * keyRange;

    for (int up = 0; up < howManyUpdatesPerTransaction; ++up) {
      int leftKey =
          ResourceStresserWorker.gen.nextInt(Math.max(1, keyRange - howManyRowsPerUpdate))
              + startingKey;
      int rightKey = leftKey + howManyRowsPerUpdate;

      try (PreparedStatement stmt = this.getPreparedStatement(conn, ioUpdate)) {

        for (int col = 1; col <= howManyColsPerRow; ++col) {
          double value =
              ResourceStresserWorker.gen.nextDouble() + ResourceStresserWorker.gen.nextDouble();
          stmt.setString(col, Double.toString(value));
        }
        stmt.setInt(howManyColsPerRow + 1, leftKey);
        stmt.setInt(howManyColsPerRow + 2, rightKey);
        int result = stmt.executeUpdate();
        if (result != howManyRowsPerUpdate) {
          LOG.warn("supposedtochange={} but result={}", howManyRowsPerUpdate, result);
        }
      }
    }
  }
}
