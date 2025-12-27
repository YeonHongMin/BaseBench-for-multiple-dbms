/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
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
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 약관을 준수하지 않으면 사용이 제한됩니다.
 * 라이선스 전문은 아래 링크에서 확인하십시오.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 별도 계약이나 법률적 요구가 없다면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.benchmarks.tpcds;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.types.TransactionStatus;
import java.sql.Connection;

public final class TPCDSWorker extends Worker<TPCDSBenchmark> {
  public TPCDSWorker(TPCDSBenchmark benchmarkModule, int id) {
    super(benchmarkModule, id);
  }

  protected TransactionStatus executeWork(Connection conn, TransactionType txnType)
      throws Procedure.UserAbortException {
    return null;
  }
}
