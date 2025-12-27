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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Q16 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               p_brand,
               p_type,
               p_size,
               COUNT(DISTINCT ps_suppkey) AS supplier_cnt
            FROM
               partsupp,
               part
            WHERE
               p_partkey = ps_partkey
               AND p_brand <> ?
               AND p_type NOT LIKE ?
               AND p_size IN (?, ?, ?, ?, ?, ?, ?, ?)
               AND ps_suppkey NOT IN
               (
                  SELECT
                     s_suppkey
                  FROM
                     supplier
                  WHERE
                     s_comment LIKE '%Customer%Complaints%'
               )
            GROUP BY
               p_brand,
               p_type,
               p_size
            ORDER BY
               supplier_cnt DESC,
               p_brand,
               p_type,
               p_size
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
    String brand = TPCHUtil.randomBrand(rand);

    // TYPE는 절 4.2.2.13에서 Types에 대해 정의된 3음절 문자열 목록 내에서 무작위로
    // 선택된 문자열의 처음 2음절로 구성됩니다.
    String syllable1 = TPCHUtil.choice(TPCHConstants.TYPE_S1, rand);
    String syllable2 = TPCHUtil.choice(TPCHConstants.TYPE_S2, rand);
    String type = String.format("%s %s", syllable1, syllable2) + "%";

    // SIZE_n은 n이 [1,8]일 때 [1 .. 50] 범위 내에서 8개의 서로 다른 값 집합으로 무작위로 선택됩니다.

    int[] sizes = new int[8];
    Set<Integer> seen = new HashSet<>(8);

    for (int i = 0; i < 8; i++) {
      int num = rand.number(1, 50);

      while (seen.contains(num)) {
        num = rand.number(1, 50);
      }

      sizes[i] = num;
      seen.add(num);
    }

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setString(1, brand);
    stmt.setString(2, type);
    for (int i = 0; i < 8; i++) {
      stmt.setInt(3 + i, sizes[i]);
    }
    return stmt;
  }
}
