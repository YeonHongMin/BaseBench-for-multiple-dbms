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

public class Q20 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               s_name,
               s_address
            FROM
               supplier,
               nation
            WHERE
               s_suppkey IN
               (
                  SELECT
                     ps_suppkey
                  FROM
                     partsupp
                  WHERE
                     ps_partkey IN
                     (
                        SELECT
                           p_partkey
                        FROM
                           part
                        WHERE
                           p_name LIKE ?
                     )
                     AND ps_availqty > (
                     SELECT
                        0.5 * SUM(l_quantity)
                     FROM
                        lineitem
                     WHERE
                        l_partkey = ps_partkey
                        AND l_suppkey = ps_suppkey
                        AND l_shipdate >= DATE ?
                        AND l_shipdate < DATE ? + INTERVAL '1' YEAR )
               )
               AND s_nationkey = n_nationkey
               AND n_name = ?
            ORDER BY
               s_name
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
    // COLOR는 P_NAME 생성에 대해 정의된 값 목록 내에서 무작위로 선택됩니다.
    String color = TPCHUtil.choice(TPCHConstants.P_NAME_GENERATOR, rand) + "%";

    // DATE는 1993..1997 범위 내에서 무작위로 선택된 연도의 1월 1일입니다.
    int year = rand.number(1993, 1997);
    String date = String.format("%d-01-01", year);

    // NATION은 절 4.2.3에서 N_NAME에 대해 정의된 값 목록 내에서 무작위로 선택됩니다.
    String nation = TPCHUtil.choice(TPCHConstants.N_NAME, rand);

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setString(1, color);
    stmt.setDate(2, Date.valueOf(date));
    stmt.setDate(3, Date.valueOf(date));
    stmt.setString(4, nation);
    return stmt;
  }
}
