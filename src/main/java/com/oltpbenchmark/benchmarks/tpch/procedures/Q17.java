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

package com.oltpbenchmark.benchmarks.tpch.procedures;

import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.tpch.TPCHConstants;
import com.oltpbenchmark.benchmarks.tpch.TPCHUtil;
import com.oltpbenchmark.util.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Q17 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          """
            SELECT
               SUM(l_extendedprice) / 7.0 AS avg_yearly
            FROM
               lineitem,
               part
            WHERE
               p_partkey = l_partkey
               AND p_brand = ?
               AND p_container = ?
               AND l_quantity < (
               SELECT
                  0.2 * AVG(l_quantity)
               FROM
                  lineitem
               WHERE
                  l_partkey = p_partkey )
            """);

  @Override
  protected PreparedStatement getStatement(
      Connection conn, RandomGenerator rand, double scaleFactor) throws SQLException {
    String brand = TPCHUtil.randomBrand(rand);

<<<<<<< HEAD
    // CONTAINER is randomly selected within the list of 2-syllable strings defined for Containers
    // in Clause
    // 4.2.2.13
=======
    // CONTAINER는 절 4.2.2.13에서 Containers에 대해 정의된 2음절 문자열 목록 내에서 무작위로 선택됩니다.
>>>>>>> master
    String containerS1 = TPCHUtil.choice(TPCHConstants.CONTAINERS_S1, rand);
    String containerS2 = TPCHUtil.choice(TPCHConstants.CONTAINERS_S2, rand);
    String container = String.format("%s %s", containerS1, containerS2);

    PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
    stmt.setString(1, brand);
    stmt.setString(2, container);
    return stmt;
  }
}
