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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Q3 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               l_orderkey,
               SUM(l_extendedprice * (1 - l_discount)) AS revenue,
               o_orderdate,
               o_shippriority
            FROM
               customer,
               orders,
               lineitem
            WHERE
               c_mktsegment = ?
               AND c_custkey = o_custkey
               AND l_orderkey = o_orderkey
               AND o_orderdate < DATE ?
               AND l_shipdate > DATE ?
            GROUP BY
               l_orderkey,
               o_orderdate,
               o_shippriority
            ORDER BY
               revenue DESC,
               o_orderdate LIMIT 10
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
    String segment = TPCHUtil.choice(TPCHConstants.SEGMENTS, rand);

<<<<<<< HEAD
<<<<<<< HEAD
    // date must be randomly selected between [1995-03-01, 1995-03-31]
=======
    // 날짜는 [1995-03-01, 1995-03-31] 사이에서 무작위로 선택되어야 합니다.
>>>>>>> master
=======
    // date must be randomly selected between [1995-03-01, 1995-03-31]
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    int day = rand.number(1, 31);
    String date = String.format("1995-03-%02d", day);

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setString(1, segment);
    stmt.setDate(2, Date.valueOf(date));
    stmt.setDate(3, Date.valueOf(date));
    return stmt;
  }
}
