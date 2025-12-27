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
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한과 조건을 준수해 주세요.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.benchmarks.tpcc;

import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.tpcc.procedures.TPCCProcedure;
import com.oltpbenchmark.types.TransactionStatus;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TPCCWorker extends Worker<TPCCBenchmark> {

  private static final Logger LOG = LoggerFactory.getLogger(TPCCWorker.class);

  private final int terminalWarehouseID;

<<<<<<< HEAD
<<<<<<< HEAD
  /** Forms a range [lower, upper] (inclusive). */
=======
  /** [lower, upper] 범위를 포합해서 나타냅니다. */
>>>>>>> master
=======
  /** [lower, upper] 범위를 포합해서 나타냅니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  private final int terminalDistrictLowerID;

  private final int terminalDistrictUpperID;
  private final Random gen = new Random();

  private final int numWarehouses;

  public TPCCWorker(
      TPCCBenchmark benchmarkModule,
      int id,
      int terminalWarehouseID,
      int terminalDistrictLowerID,
      int terminalDistrictUpperID,
      int numWarehouses) {
    super(benchmarkModule, id);

    this.terminalWarehouseID = terminalWarehouseID;
    this.terminalDistrictLowerID = terminalDistrictLowerID;
    this.terminalDistrictUpperID = terminalDistrictUpperID;

    this.numWarehouses = numWarehouses;
  }

  /** Executes a single TPCC transaction of type transactionType. */
  @Override
  protected TransactionStatus executeWork(Connection conn, TransactionType nextTransaction)
      throws UserAbortException, SQLException {
    try {
      TPCCProcedure proc = (TPCCProcedure) this.getProcedure(nextTransaction.getProcedureClass());
      proc.run(
          conn,
          gen,
          terminalWarehouseID,
          numWarehouses,
          terminalDistrictLowerID,
          terminalDistrictUpperID,
          this);
    } catch (ClassCastException ex) {
<<<<<<< HEAD
<<<<<<< HEAD
      // fail gracefully
      LOG.error("We have been invoked with an INVALID transactionType?!", ex);
=======
      // 예외가 발생해도 안전하게 처리합니다.
      LOG.error("잘못된 transactionType으로 실행되었습니다?!", ex);
>>>>>>> master
=======
      // 예외가 발생해도 안전하게 처리합니다.
      LOG.error("잘못된 transactionType으로 실행되었습니다?!", ex);
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      throw new RuntimeException("Bad transaction type = " + nextTransaction);
    }
    return (TransactionStatus.SUCCESS);
  }

  @Override
  protected long getPreExecutionWaitInMillis(TransactionType type) {
<<<<<<< HEAD
<<<<<<< HEAD
    // TPC-C 5.2.5.2: For keying times for each type of transaction.
=======
    // TPC-C 5.2.5.2: 각 트랜잭션 타입에 대한 키 입력 시간을 반영합니다.
>>>>>>> master
=======
    // TPC-C 5.2.5.2: 각 트랜잭션 타입에 대한 키 입력 시간을 반영합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    return type.getPreExecutionWait();
  }

  @Override
  protected long getPostExecutionWaitInMillis(TransactionType type) {
<<<<<<< HEAD
<<<<<<< HEAD
    // TPC-C 5.2.5.4: For think times for each type of transaction.
=======
    // TPC-C 5.2.5.4: 각 트랜잭션 타입에 대한 사고 시간을 반영합니다.
>>>>>>> master
=======
    // TPC-C 5.2.5.4: 각 트랜잭션 타입에 대한 사고 시간을 반영합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    long mean = type.getPostExecutionWait();

    float c = this.getBenchmark().rng().nextFloat();
    long thinkTime = (long) (-1 * Math.log(c) * mean);
    if (thinkTime > 10 * mean) {
      thinkTime = 10 * mean;
    }

    return thinkTime;
  }
}
