/*
 * Copyright 2020 by OLTPBenchmark Project
 *
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

public class Q5 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               n_name,
               SUM(l_extendedprice * (1 - l_discount)) AS revenue
            FROM
               customer,
               orders,
               lineitem,
               supplier,
               nation,
               region
            WHERE
               c_custkey = o_custkey
               AND l_orderkey = o_orderkey
               AND l_suppkey = s_suppkey
               AND c_nationkey = s_nationkey
               AND s_nationkey = n_nationkey
               AND n_regionkey = r_regionkey
               AND r_name = ?
               AND o_orderdate >= DATE ?
               AND o_orderdate < DATE ? + INTERVAL '1' YEAR
            GROUP BY
               n_name
            ORDER BY
               revenue DESC
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
    String region = TPCHUtil.choice(TPCHConstants.R_NAME, rand);

    int year = rand.number(1993, 1997);
    String date = String.format("%d-01-01", year);

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setString(1, region);
    stmt.setDate(2, Date.valueOf(date));
    stmt.setDate(3, Date.valueOf(date));
    return stmt;
  }
}
