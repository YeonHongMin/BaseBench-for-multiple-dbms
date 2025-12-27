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
import com.oltpbenchmark.benchmarks.tpch.TPCHConstants;
import com.oltpbenchmark.benchmarks.tpch.TPCHUtil;
import com.oltpbenchmark.util.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Q21 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               s_name,
               COUNT(*) AS numwait
            FROM
               supplier,
               lineitem l1,
               orders,
               nation
            WHERE
               s_suppkey = l1.l_suppkey
               AND o_orderkey = l1.l_orderkey
               AND o_orderstatus = 'F'
               AND l1.l_receiptdate > l1.l_commitdate
               AND EXISTS
               (
                  SELECT
                     *
                  FROM
                     lineitem l2
                  WHERE
                     l2.l_orderkey = l1.l_orderkey
                     AND l2.l_suppkey <> l1.l_suppkey
               )
               AND NOT EXISTS
               (
                  SELECT
                     *
                  FROM
                     lineitem l3
                  WHERE
                     l3.l_orderkey = l1.l_orderkey
                     AND l3.l_suppkey <> l1.l_suppkey
                     AND l3.l_receiptdate > l3.l_commitdate
               )
               AND s_nationkey = n_nationkey
               AND n_name = ?
            GROUP BY
               s_name
            ORDER BY
               numwait DESC,
               s_name LIMIT 100
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
<<<<<<< HEAD
<<<<<<< HEAD
    // NATION is randomly selected within the list of values defined for N_NAME in Clause 4.2.3
=======
    // NATION은 절 4.2.3에서 N_NAME에 대해 정의된 값 목록 내에서 무작위로 선택됩니다.
>>>>>>> master
=======
    // NATION is randomly selected within the list of values defined for N_NAME in Clause 4.2.3
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    String nation = TPCHUtil.choice(TPCHConstants.N_NAME, rand);

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setString(1, nation);
    return stmt;
  }
}
