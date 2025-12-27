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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Q22 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               cntrycode,
               COUNT(*) AS numcust,
               SUM(c_acctbal) AS totacctbal
            FROM
               (
                  SELECT
                     SUBSTRING(c_phone FROM 1 FOR 2) AS cntrycode,
                     c_acctbal
                  FROM
                     customer
                  WHERE
                     SUBSTRING(c_phone FROM 1 FOR 2) IN (?, ?, ?, ?, ?, ?, ?)
                     AND c_acctbal >
                     (
                         SELECT
                            AVG(c_acctbal)
                         FROM
                            customer
                         WHERE
                            c_acctbal > 0.00
                            AND SUBSTRING(c_phone FROM 1 FOR 2) IN (?, ?, ?, ?, ?, ?, ?)
                     )
                     AND NOT EXISTS
                     (
                         SELECT
                            *
                         FROM
                            orders
                         WHERE
                            o_custkey = c_custkey
                     )
               )
               AS custsale
            GROUP BY
               cntrycode
            ORDER BY
               cntrycode
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
<<<<<<< HEAD
    // I1 - I7 are randomly selected without repetition from the possible values

    // We are given
    //      Let i be an index into the list of strings Nations
    //          (i.e., ALGERIA is 0, ARGENTINA is 1, etc., see Clause 4.2.3),
    //      Let country_code be the sub-string representation of the number (i + 10)
    // There are 25 nations, hence country_code ranges from [10, 34]
=======
    // I1 - I7는 가능한 값에서 반복 없이 무작위로 선택됩니다.

    // 주어진 조건:
    //      i를 문자열 Nations 목록의 인덱스로 둡니다.
    //          (즉, ALGERIA는 0, ARGENTINA는 1 등, 절 4.2.3 참조),
    //      country_code를 숫자 (i + 10)의 부분 문자열 표현으로 둡니다.
    // 국가는 25개이므로 country_code는 [10, 34] 범위입니다.
>>>>>>> master

    Set<Integer> seen = new HashSet<>(7);
    int[] codes = new int[7];
    for (int i = 0; i < 7; i++) {
      int num = rand.number(10, 34);

      while (seen.contains(num)) {
        num = rand.number(10, 34);
      }

      codes[i] = num;
      seen.add(num);
    }

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    for (int i = 0; i < 7; i++) {
      stmt.setString(1 + i, String.valueOf(codes[i]));
    }
    for (int i = 0; i < 7; i++) {
      stmt.setString(8 + i, String.valueOf(codes[i]));
    }
    return stmt;
  }
}
