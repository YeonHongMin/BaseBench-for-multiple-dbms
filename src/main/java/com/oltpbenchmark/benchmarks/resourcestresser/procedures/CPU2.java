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
import java.sql.ResultSet;
import java.sql.SQLException;

public class CPU2 extends Procedure {

  public final SQLStmt cpuSelect;

  {
    String complexClause = "passwd";
    for (int i = 1; i <= ResourceStresserWorker.CPU2_nestedLevel; ++i) {
      complexClause = "md5(concat(" + complexClause + ",?))";
    }
    cpuSelect =
        new SQLStmt(
            "SELECT count(*) FROM (SELECT "
                + complexClause
                + " FROM "
                + ResourceStresserConstants.TABLENAME_CPUTABLE
                + " WHERE empid >= 0 AND empid < 100) T2");
  }

  public void run(Connection conn, int howManyPerTransaction, int sleepLength, int nestedLevel)
      throws SQLException {

    for (int tranIdx = 0; tranIdx < howManyPerTransaction; ++tranIdx) {
      double randNoise = ResourceStresserWorker.gen.nextDouble();

      try (PreparedStatement stmt = this.getPreparedStatement(conn, cpuSelect)) {
        for (int i = 1; i <= nestedLevel; ++i) {
          stmt.setString(i, Double.toString(randNoise));
        }

        // TODO: Is this the right place to sleep?  With rs open???
        try (ResultSet rs = stmt.executeQuery()) {
          assert rs != null;
          try {
            Thread.sleep(sleepLength);
          } catch (InterruptedException e) {
            throw new SQLException("Unexpected interupt while sleeping!");
          }
        }
      }
    }
  }
}
