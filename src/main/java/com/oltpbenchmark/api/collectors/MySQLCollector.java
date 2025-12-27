/*
<<<<<<< HEAD
 * Copyright 2020 by OLTPBenchmark Project
 *
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
 *
 */

=======
 * 저작권 2020 OLTPBenchmark 프로젝트
 *
 * Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 * 라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 * 이 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 * 라이선스에서 허용하는 권한과 제한 사항은
 * 라이선스의 본문을 참조하십시오.
 *
 */
>>>>>>> master
package com.oltpbenchmark.api.collectors;

import java.sql.*;

public class MySQLCollector extends DBCollector {

  private static final String VERSION_SQL = "SELECT @@GLOBAL.version;";

  private static final String PARAMETERS_SQL = "SHOW VARIABLES;";

  private static final String METRICS_SQL = "SHOW STATUS";

  public MySQLCollector(String oriDBUrl, String username, String password) {
    try (Connection conn = DriverManager.getConnection(oriDBUrl, username, password)) {
      try (Statement s = conn.createStatement()) {

<<<<<<< HEAD
        // Collect DBMS version
=======
        // DBMS 踰꾩쟾???섏쭛?⑸땲??
>>>>>>> master
        try (ResultSet out = s.executeQuery(VERSION_SQL)) {
          if (out.next()) {
            this.version = out.getString(1);
          }
        }

<<<<<<< HEAD
        // Collect DBMS parameters
=======
        // DBMS ?뚮씪誘명꽣瑜??섏쭛?⑸땲??
>>>>>>> master
        try (ResultSet out = s.executeQuery(PARAMETERS_SQL)) {
          while (out.next()) {
            dbParameters.put(out.getString(1).toLowerCase(), out.getString(2));
          }
        }

<<<<<<< HEAD
        // Collect DBMS internal metrics
=======
        // DBMS ?대? 硫뷀듃由?쓣 ?섏쭛?⑸땲??
>>>>>>> master
        try (ResultSet out = s.executeQuery(METRICS_SQL)) {
          while (out.next()) {
            dbMetrics.put(out.getString(1).toLowerCase(), out.getString(2));
          }
        }
      }
    } catch (SQLException e) {
      LOG.error("Error while collecting DB parameters: {}", e.getMessage());
    }
  }
}
