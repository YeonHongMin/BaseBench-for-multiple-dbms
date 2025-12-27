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
import com.oltpbenchmark.benchmarks.tpch.TPCHConstants;
import com.oltpbenchmark.benchmarks.tpch.TPCHUtil;
import com.oltpbenchmark.util.RandomGenerator;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Q12 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
                l_shipmode,
                SUM(
                    CASE
                        WHEN
                            o_orderpriority = '1-URGENT' OR o_orderpriority = '2-HIGH'
                        THEN
                            1
                        ELSE
                            0
                    END
                ) AS high_line_count,
                SUM(
                    CASE
                        WHEN
                            o_orderpriority <> '1-URGENT' AND o_orderpriority <> '2-HIGH'
                        THEN
                            1
                        ELSE
                            0
                    END
                ) AS low_line_count
            FROM
                orders,
                lineitem
            WHERE
                o_orderkey = l_orderkey
                AND l_shipmode IN (?, ?)
                AND l_commitdate < l_receiptdate
                AND l_shipdate < l_commitdate
                AND l_receiptdate >= DATE ?
                AND l_receiptdate < DATE ? + INTERVAL '1' YEAR
            GROUP BY
                l_shipmode
            ORDER BY
                l_shipmode
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
<<<<<<< HEAD
    // SHIPMODE1 is randomly selected within the list of values defined for Modes in Clause 4.2.2.13
    String shipMode1 = TPCHUtil.choice(TPCHConstants.MODES, rand);

    // SHIPMODE2 is randomly selected within the list of values defined for Modes in Clause 4.2.2.13
    // and must be
    // different from the value selected for SHIPMODE1 in item 1
=======
    // SHIPMODE1은 절 4.2.2.13에서 Modes에 대해 정의된 값 목록 내에서 무작위로 선택됩니다.
    String shipMode1 = TPCHUtil.choice(TPCHConstants.MODES, rand);

    // SHIPMODE2는 절 4.2.2.13에서 Modes에 대해 정의된 값 목록 내에서 무작위로 선택되며
    // 항목 1에서 SHIPMODE1에 대해 선택된 값과 달라야 합니다.
>>>>>>> master
    String shipMode2 = shipMode1;
    while (shipMode1.equals(shipMode2)) {
      shipMode2 = TPCHUtil.choice(TPCHConstants.MODES, rand);
    }

<<<<<<< HEAD
    // DATE is the first of January of a randomly selected year within [1993 .. 1997]
=======
    // DATE는 [1993 .. 1997] 범위 내에서 무작위로 선택된 연도의 1월 1일입니다.
>>>>>>> master
    int year = rand.number(1993, 1997);
    String date = String.format("%d-01-01", year);

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setString(1, shipMode1);
    stmt.setString(2, shipMode2);
    stmt.setDate(3, Date.valueOf(date));
    stmt.setDate(4, Date.valueOf(date));
    return stmt;
  }
}
