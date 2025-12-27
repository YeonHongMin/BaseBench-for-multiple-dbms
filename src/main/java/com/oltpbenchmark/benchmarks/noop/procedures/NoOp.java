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

package com.oltpbenchmark.benchmarks.noop.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 실제 NoOp 구현
 *
 * @author pavlo
 * @author eric-haibin-lin
 */
public class NoOp extends Procedure {
  private static final Logger LOG = LoggerFactory.getLogger(NoOp.class);

  // 쿼리는 세미콜론만 포함합니다
  // 이것만으로도 DBMS가 파싱하고 무언가를 수행해야 합니다
  public final SQLStmt noopStmt = new SQLStmt(";");

  public void run(Connection conn) {
    try (PreparedStatement stmt = this.getPreparedStatement(conn, noopStmt)) {
      // 중요:
      // 결과를 반환하지 않는 쿼리를 실행할 때 일부 DBMS는 여기서 예외를 발생시킵니다.
      // 따라서 예외를 잡아서 무시합니다. 새로운 DBMS를 이 벤치마크에 포팅하는 경우,
      // 여기서 이 예외를 비활성화하고 실제로 올바르게 작동하는지 확인해야 합니다.

      if (stmt.execute()) {
        ResultSet r = stmt.getResultSet();
        while (r.next()) {
          // 아무것도 하지 않음
        }
        r.close();
      }

    } catch (Exception ex) {
      // 이 오류는 "쿼리에서 결과가 반환되지 않았습니다."와 같은 메시지일 수 있습니다.
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exception for NoOp query. This may be expected!", ex);
      }
    }
  }
}
