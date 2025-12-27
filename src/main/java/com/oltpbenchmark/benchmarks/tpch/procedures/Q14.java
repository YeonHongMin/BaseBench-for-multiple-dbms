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

package com.oltpbenchmark.benchmarks.tpch.procedures;

import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.util.RandomGenerator;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Q14 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               100.00 * SUM(
               CASE
                  WHEN
                     p_type LIKE 'PROMO%'
                  THEN
                     l_extendedprice * (1 - l_discount)
                  ELSE
                     0
               END
            ) / SUM(l_extendedprice * (1 - l_discount)) AS promo_revenue
            FROM
               lineitem, part
            WHERE
               l_partkey = p_partkey
               AND l_shipdate >= DATE ?
               AND l_shipdate < DATE ? + INTERVAL '1' MONTH
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
<<<<<<< HEAD
    // DATE is the first day of a month randomly selected from a random year within [1993 .. 1997]
=======
    // DATE는 [1993 .. 1997] 범위 내의 무작위 연도에서 무작위로 선택된 월의 첫 번째 날입니다.
>>>>>>> master
    int year = rand.number(1993, 1997);
    int month = rand.number(1, 12);
    String date = String.format("%d-%02d-01", year, month);

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setDate(1, Date.valueOf(date));
    stmt.setDate(2, Date.valueOf(date));
    return stmt;
  }
}
