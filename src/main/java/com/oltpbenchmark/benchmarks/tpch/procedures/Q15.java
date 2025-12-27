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
import java.sql.Statement;

public class Q15 extends GenericQuery {

  public final SQLStmt createview_stmt =
      new SQLStmt(
          """
            CREATE view revenue0 (supplier_no, total_revenue) AS
            SELECT
               l_suppkey,
               SUM(l_extendedprice * (1 - l_discount))
            FROM
               lineitem
            WHERE
               l_shipdate >= DATE ?
               AND l_shipdate < DATE ? + INTERVAL '3' MONTH
            GROUP BY
               l_suppkey
            """);

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               s_suppkey,
               s_name,
               s_address,
               s_phone,
               total_revenue
            FROM
               supplier,
               revenue0
            WHERE
               s_suppkey = supplier_no
               AND total_revenue = (
                  SELECT
                     MAX(total_revenue)
                  FROM
                     revenue0
               )
            ORDER BY
               s_suppkey
            """);

  public final SQLStmt dropview_stmt =
      new SQLStmt(
          """
            DROP VIEW revenue0
            """);

  @Override
  public void run(Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
<<<<<<< HEAD
    // With this query, we have to set up a view before we execute the
    // query, then drop it once we're done.
    try (Statement stmt = conn.createStatement()) {
      try {
        // DATE is the first day of a randomly selected month between
        // the first month of 1993 and the 10th month of 1997
=======
    // 이 쿼리의 경우 쿼리를 실행하기 전에 뷰를 설정한 다음
    // 완료되면 삭제해야 합니다.
    try (Statement stmt = conn.createStatement()) {
      try {
        // DATE는 1993년 1월과 1997년 10월 사이에서 무작위로 선택된 월의 첫 번째 날입니다.
>>>>>>> master
        int year = rand.number(1993, 1997);
        int month = rand.number(1, year == 1997 ? 10 : 12);
        String date = String.format("%d-%02d-01", year, month);

        String sql = createview_stmt.getSQL();
        sql = sql.replace("?", String.format("'%s'", date));
        stmt.execute(sql);
        super.run(conn, rand, scaleFactor);
      } finally {
        String sql = dropview_stmt.getSQL();
        stmt.execute(sql);
      }
    }
  }

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
    return this.getPreparedStatement(conn, query_stmt);
  }
}
