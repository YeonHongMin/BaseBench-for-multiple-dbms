/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한과 조건을 준수해 주세요.
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

  /** [lower, upper] 범위를 포합해서 나타냅니다. */
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
      // 예외가 발생해도 안전하게 처리합니다.
      LOG.error("잘못된 transactionType으로 실행되었습니다?!", ex);
      throw new RuntimeException("Bad transaction type = " + nextTransaction);
    }
    return (TransactionStatus.SUCCESS);
  }

  @Override
  protected long getPreExecutionWaitInMillis(TransactionType type) {
    // TPC-C 5.2.5.2: 각 트랜잭션 타입에 대한 키 입력 시간을 반영합니다.
    return type.getPreExecutionWait();
  }

  @Override
  protected long getPostExecutionWaitInMillis(TransactionType type) {
    // TPC-C 5.2.5.4: 각 트랜잭션 타입에 대한 사고 시간을 반영합니다.
    long mean = type.getPostExecutionWait();

    float c = this.getBenchmark().rng().nextFloat();
    long thinkTime = (long) (-1 * Math.log(c) * mean);
    if (thinkTime > 10 * mean) {
      thinkTime = 10 * mean;
    }

    return thinkTime;
  }
}
