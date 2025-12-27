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

package com.oltpbenchmark.benchmarks.chbenchmark.queries;

import com.oltpbenchmark.api.SQLStmt;

public class Q1 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          "SELECT ol_number, "
              + "sum(ol_quantity) AS sum_qty, "
              + "sum(ol_amount) AS sum_amount, "
              + "avg(ol_quantity) AS avg_qty, "
              + "avg(ol_amount) AS avg_amount, "
              + "count(*) AS count_order "
              + "FROM order_line "
              + "WHERE ol_delivery_d > '2007-01-02 00:00:00.000000' "
              + "GROUP BY ol_number "
              + "ORDER BY ol_number");

  protected SQLStmt get_query() {
    return query_stmt;
  }
}
