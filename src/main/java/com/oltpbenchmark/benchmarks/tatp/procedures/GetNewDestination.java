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

package com.oltpbenchmark.benchmarks.tatp.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.tatp.TATPConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetNewDestination extends Procedure {

  public final SQLStmt getNewDestination =
      new SQLStmt(
          "SELECT cf.numberx "
              + "  FROM "
              + TATPConstants.TABLENAME_SPECIAL_FACILITY
              + " sf, "
              + "       "
              + TATPConstants.TABLENAME_CALL_FORWARDING
              + " cf "
              + " WHERE sf.s_id = ? "
              + "   AND sf.sf_type = ? "
              + "   AND sf.is_active = 1 "
              + "   AND cf.s_id = sf.s_id "
              + "   AND cf.sf_type = sf.sf_type "
              + "   AND cf.start_time <= ? "
              + "   AND cf.end_time > ?");

  public void run(Connection conn, long s_id, byte sf_type, byte start_time, byte end_time)
      throws SQLException {
    try (PreparedStatement stmt = this.getPreparedStatement(conn, getNewDestination)) {
      stmt.setLong(1, s_id);
      stmt.setByte(2, sf_type);
      stmt.setByte(3, start_time);
      stmt.setByte(4, end_time);
      try (ResultSet results = stmt.executeQuery()) {
        assert results != null;
      }
    }
  }
}
