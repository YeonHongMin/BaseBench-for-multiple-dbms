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

package com.oltpbenchmark.benchmarks.sibench;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.sibench.procedures.MinRecord;
import com.oltpbenchmark.benchmarks.sibench.procedures.UpdateRecord;
import com.oltpbenchmark.types.TransactionStatus;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

public final class SIWorker extends Worker<SIBenchmark> {

  private static Random updateRecordIdGenerator = null;
  private final int recordCount;

  public SIWorker(SIBenchmark benchmarkModule, int id, int init_record_count) {
    super(benchmarkModule, id);
    synchronized (SIWorker.class) {
      // We must know where to start inserting
      if (updateRecordIdGenerator == null) {
        updateRecordIdGenerator = benchmarkModule.rng();
      }
    }
    this.recordCount = init_record_count;
  }

  @Override
  protected TransactionStatus executeWork(Connection conn, TransactionType nextTrans)
      throws UserAbortException, SQLException {
    Class<? extends Procedure> procClass = nextTrans.getProcedureClass();

    if (procClass.equals(MinRecord.class)) {
      minRecord(conn);
    } else if (procClass.equals(UpdateRecord.class)) {
      updateRecord(conn);
    }
    return (TransactionStatus.SUCCESS);
  }

  private void minRecord(Connection conn) throws SQLException {
    MinRecord proc = this.getProcedure(MinRecord.class);

    proc.run(conn);
  }

  private void updateRecord(Connection conn) throws SQLException {
    UpdateRecord proc = this.getProcedure(UpdateRecord.class);

    int id = updateRecordIdGenerator.nextInt(this.recordCount);
    proc.run(conn, id);
  }
}
