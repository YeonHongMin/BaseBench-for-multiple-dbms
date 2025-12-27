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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Q15 extends GenericQuery {

  public final SQLStmt createview_stmt =
      new SQLStmt(
          "CREATE view revenue0 (supplier_no, total_revenue) AS "
              + "SELECT "
              + "mod((s_w_id * s_i_id),10000) as supplier_no, "
              + "sum(ol_amount) as total_revenue "
              + "FROM "
              + "order_line, stock "
              + "WHERE "
              + "ol_i_id = s_i_id "
              + "AND ol_supply_w_id = s_w_id "
              + "AND ol_delivery_d >= '2007-01-02 00:00:00.000000' "
              + "GROUP BY "
              + "supplier_no");

  public final SQLStmt query_stmt =
      new SQLStmt(
          "SELECT su_suppkey, "
              + "su_name, "
              + "su_address, "
              + "su_phone, "
              + "total_revenue "
              + "FROM supplier, revenue0 "
              + "WHERE su_suppkey = supplier_no "
              + "AND total_revenue = (select max(total_revenue) from revenue0) "
              + "ORDER BY su_suppkey");

  public final SQLStmt dropview_stmt = new SQLStmt("DROP VIEW revenue0");

  protected SQLStmt get_query() {
    return query_stmt;
  }

  public void run(Connection conn) throws SQLException {
    // With this query, we have to set up a view before we execute the
    // query, then drop it once we're done.
    try (Statement stmt = conn.createStatement()) {
      try {
        stmt.executeUpdate(createview_stmt.getSQL());
        super.run(conn);
      } finally {
        stmt.executeUpdate(dropview_stmt.getSQL());
      }
    }
  }
}
