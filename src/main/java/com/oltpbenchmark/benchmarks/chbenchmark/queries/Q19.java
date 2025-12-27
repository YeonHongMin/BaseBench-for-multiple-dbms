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

package com.oltpbenchmark.benchmarks.chbenchmark.queries;

import com.oltpbenchmark.api.SQLStmt;

public class Q19 extends GenericQuery {

  public final SQLStmt query_stmt =
      new SQLStmt(
          "SELECT sum(ol_amount) AS revenue "
              + "FROM order_line, "
              + "item "
              + "WHERE (ol_i_id = i_id "
              + "AND i_data LIKE '%a' "
              + "AND ol_quantity >= 1 "
              + "AND ol_quantity <= 10 "
              + "AND i_price BETWEEN 1 AND 400000 "
              + "AND ol_w_id IN (1, "
              + "2, "
              + "3)) "
              + "OR (ol_i_id = i_id "
              + "AND i_data LIKE '%b' "
              + "AND ol_quantity >= 1 "
              + "AND ol_quantity <= 10 "
              + "AND i_price BETWEEN 1 AND 400000 "
              + "AND ol_w_id IN (1, "
              + "2, "
              + "4)) "
              + "OR (ol_i_id = i_id "
              + "AND i_data LIKE '%c' "
              + "AND ol_quantity >= 1 "
              + "AND ol_quantity <= 10 "
              + "AND i_price BETWEEN 1 AND 400000 "
              + "AND ol_w_id IN (1, "
              + "5, "
              + "3))");

  protected SQLStmt get_query() {
    return query_stmt;
  }
}
