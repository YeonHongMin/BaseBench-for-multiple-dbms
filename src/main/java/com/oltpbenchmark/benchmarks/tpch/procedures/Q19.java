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

public class Q19 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               SUM(l_extendedprice* (1 - l_discount)) AS revenue
            FROM
               lineitem,
               part
            WHERE
               (
                  p_partkey = l_partkey
                  AND p_brand = ?
                  AND p_container IN ('SM CASE', 'SM BOX', 'SM PACK', 'SM PKG')
                  AND l_quantity >= ?
                  AND l_quantity <= ? + 10
                  AND p_size BETWEEN 1 AND 5
                  AND l_shipmode IN ('AIR', 'AIR REG')
                  AND l_shipinstruct = 'DELIVER IN PERSON'
               )
               OR
               (
                  p_partkey = l_partkey
                  AND p_brand = ?
                  AND p_container IN ('MED BAG', 'MED BOX', 'MED PKG', 'MED PACK')
                  AND l_quantity >= ?
                  AND l_quantity <= ? + 10
                  AND p_size BETWEEN 1 AND 10
                  AND l_shipmode IN ('AIR', 'AIR REG')
                  AND l_shipinstruct = 'DELIVER IN PERSON'
               )
               OR
               (
                  p_partkey = l_partkey
                  AND p_brand = ?
                  AND p_container IN ('LG CASE', 'LG BOX', 'LG PACK', 'LG PKG')
                  AND l_quantity >= ?
                  AND l_quantity <= ? + 10
                  AND p_size BETWEEN 1 AND 15
                  AND l_shipmode IN ('AIR', 'AIR REG')
                  AND l_shipinstruct = 'DELIVER IN PERSON'
               )
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
    // QUANTITY1은 [1..10] 범위 내에서 무작위로 선택됩니다.
    int quantity1 = rand.number(1, 10);

    // QUANTITY2는 [10..20] 범위 내에서 무작위로 선택됩니다.
    int quantity2 = rand.number(10, 20);

    // QUANTITY3는 [20..30] 범위 내에서 무작위로 선택됩니다.
    int quantity3 = rand.number(20, 30);

    // BRAND1, BRAND2, BRAND3 = 'Brand#MN' 여기서 각 MN은 [1 .. 5] 범위 내에서 무작위로
    // 독립적으로 선택된 두 숫자를 나타내는 두 문자 문자열입니다.
    String brand1 = TPCHUtil.randomBrand(rand);
    String brand2 = TPCHUtil.randomBrand(rand);
    String brand3 = TPCHUtil.randomBrand(rand);

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setString(1, brand1);
    stmt.setInt(2, quantity1);
    stmt.setInt(3, quantity1);
    stmt.setString(4, brand2);
    stmt.setInt(5, quantity2);
    stmt.setInt(6, quantity2);
    stmt.setString(7, brand3);
    stmt.setInt(8, quantity3);
    stmt.setInt(9, quantity3);
    return stmt;
  }
}
