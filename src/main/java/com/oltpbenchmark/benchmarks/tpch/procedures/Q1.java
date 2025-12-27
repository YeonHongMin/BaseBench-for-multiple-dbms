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

package com.oltpbenchmark.benchmarks.tpch.procedures;

import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.util.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Q1 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
              SELECT
                 l_returnflag,
                 l_linestatus,
                 SUM(l_quantity) AS sum_qty,
                 SUM(l_extendedprice) AS sum_base_price,
                 SUM(l_extendedprice * (1 - l_discount)) AS sum_disc_price,
                 SUM(l_extendedprice * (1 - l_discount) * (1 + l_tax)) AS sum_charge,
                 AVG(l_quantity) AS avg_qty,
                 AVG(l_extendedprice) AS avg_price,
                 AVG(l_discount) AS avg_disc,
                 COUNT(*) AS count_order
              FROM
                 lineitem
              WHERE
                 l_shipdate <= DATE '1998-12-01' - INTERVAL ? DAY
              GROUP BY
                 l_returnflag,
                 l_linestatus
              ORDER BY
                 l_returnflag,
                 l_linestatus
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
    String delta = String.valueOf(rand.number(60, 120));

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setString(1, delta);
    return stmt;
  }
}
