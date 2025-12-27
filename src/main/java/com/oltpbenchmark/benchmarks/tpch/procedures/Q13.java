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
import com.oltpbenchmark.benchmarks.tpch.TPCHUtil;
import com.oltpbenchmark.util.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Q13 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               c_count,
               COUNT(*) AS custdist
            FROM
               (
                  SELECT
                     c_custkey,
                     COUNT(o_orderkey) AS c_count
                  FROM
                     customer
                     LEFT OUTER JOIN
                        orders
                        ON c_custkey = o_custkey
                        AND o_comment NOT LIKE ?
                  GROUP BY
                     c_custkey
               )
               AS c_orders
            GROUP BY
               c_count
            ORDER BY
               custdist DESC,
               c_count DESC
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
    // WORD1은 4가지 가능한 값 중에서 무작위로 선택됩니다: special, pending, unusual, express
    String word1 = TPCHUtil.choice(new String[] {"special", "pending", "unusual", "express"}, rand);

    // WORD2는 4가지 가능한 값 중에서 무작위로 선택됩니다: packages, requests, accounts, deposits
    String word2 =
        TPCHUtil.choice(new String[] {"packages", "requests", "accounts", "deposits"}, rand);

    String filter = "%" + word1 + "%" + word2 + "%";

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setString(1, filter);
    return stmt;
  }
}
