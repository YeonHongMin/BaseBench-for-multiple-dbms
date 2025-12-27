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

public class Q6 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               SUM(l_extendedprice * l_discount) AS revenue
            FROM
               lineitem
            WHERE
               l_shipdate >= DATE ?
               AND l_shipdate < DATE ? + INTERVAL '1' YEAR
               AND l_discount BETWEEN ? - 0.01 AND ? + 0.01
               AND l_quantity < ?
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
<<<<<<< HEAD
    // DATE is the first of January of a randomly selected year within [1993 .. 1997]
    int year = rand.number(1993, 1997);
    String date = String.format("%d-01-01", year);

    // DISCOUNT is randomly selected within [0.02 .. 0.09]
    String discount = String.format("0.0%d", rand.number(2, 9));

    // QUANTITY is randomly selected within [24 .. 25]
=======
    // DATE는 [1993 .. 1997] 범위 내에서 무작위로 선택된 연도의 1월 1일입니다.
    int year = rand.number(1993, 1997);
    String date = String.format("%d-01-01", year);

    // DISCOUNT는 [0.02 .. 0.09] 범위 내에서 무작위로 선택됩니다.
    String discount = String.format("0.0%d", rand.number(2, 9));

    // QUANTITY는 [24 .. 25] 범위 내에서 무작위로 선택됩니다.
>>>>>>> master
    int quantity = rand.number(24, 25);

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setDate(1, Date.valueOf(date));
    stmt.setDate(2, Date.valueOf(date));
    stmt.setString(3, discount);
    stmt.setString(4, discount);
    stmt.setInt(5, quantity);
    return stmt;
  }
}
