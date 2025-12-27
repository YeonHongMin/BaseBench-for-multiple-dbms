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

package com.oltpbenchmark.benchmarks.ycsb;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.ycsb.procedures.*;
import com.oltpbenchmark.distributions.CounterGenerator;
import com.oltpbenchmark.distributions.UniformGenerator;
import com.oltpbenchmark.distributions.ZipfianGenerator;
import com.oltpbenchmark.types.TransactionStatus;
import com.oltpbenchmark.util.TextGenerator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * YCSBWorker 구현체 누가 실제로 이걸 썼는지 기억이 안 나지만 2016년에 고쳤습니다...
 *
 * @author pavlo
 */
class YCSBWorker extends Worker<YCSBBenchmark> {

  private final ZipfianGenerator readRecord;
  private static CounterGenerator insertRecord;
  private final UniformGenerator randScan;

  private final char[] data;
  private final String[] params = new String[YCSBConstants.NUM_FIELDS];
  private final String[] results = new String[YCSBConstants.NUM_FIELDS];

  private final UpdateRecord procUpdateRecord;
  private final ScanRecord procScanRecord;
  private final ReadRecord procReadRecord;
  private final ReadModifyWriteRecord procReadModifyWriteRecord;
  private final InsertRecord procInsertRecord;
  private final DeleteRecord procDeleteRecord;

  public YCSBWorker(YCSBBenchmark benchmarkModule, int id, int init_record_count) {
    super(benchmarkModule, id);
    this.data = new char[benchmarkModule.fieldSize];
    this.readRecord =
        new ZipfianGenerator(
            rng(), init_record_count, benchmarkModule.skewFactor); // pool for read keys
    this.randScan = new UniformGenerator(1, YCSBConstants.MAX_SCAN);

    synchronized (YCSBWorker.class) {
      // 삽입을 시작할 위치를 알아야 합니다
      if (insertRecord == null) {
        insertRecord = new CounterGenerator(init_record_count);
      }
    }

    // 이것은 트랜잭션을 실행할 때마다 해시맵 조회를 호출하는 것을 피하기 위한
    // 사소한 속도 향상입니다. 코어가 많지 않은 클라이언트 머신에서는 중요합니다
    this.procUpdateRecord = this.getProcedure(UpdateRecord.class);
    this.procScanRecord = this.getProcedure(ScanRecord.class);
    this.procReadRecord = this.getProcedure(ReadRecord.class);
    this.procReadModifyWriteRecord = this.getProcedure(ReadModifyWriteRecord.class);
    this.procInsertRecord = this.getProcedure(InsertRecord.class);
    this.procDeleteRecord = this.getProcedure(DeleteRecord.class);
  }

  @Override
  protected TransactionStatus executeWork(Connection conn, TransactionType nextTrans)
      throws UserAbortException, SQLException {
    Class<? extends Procedure> procClass = nextTrans.getProcedureClass();

    if (procClass.equals(DeleteRecord.class)) {
      deleteRecord(conn);
    } else if (procClass.equals(InsertRecord.class)) {
      insertRecord(conn);
    } else if (procClass.equals(ReadModifyWriteRecord.class)) {
      readModifyWriteRecord(conn);
    } else if (procClass.equals(ReadRecord.class)) {
      readRecord(conn);
    } else if (procClass.equals(ScanRecord.class)) {
      scanRecord(conn);
    } else if (procClass.equals(UpdateRecord.class)) {
      updateRecord(conn);
    }
    return (TransactionStatus.SUCCESS);
  }

  private void updateRecord(Connection conn) throws SQLException {
    int keyname = readRecord.nextInt();
    this.buildParameters();
    this.procUpdateRecord.run(conn, keyname, this.params);
  }

  private void scanRecord(Connection conn) throws SQLException {
    int keyname = readRecord.nextInt();
    int count = randScan.nextInt();
    this.procScanRecord.run(conn, keyname, count, new ArrayList<>());
  }

  private void readRecord(Connection conn) throws SQLException {
    int keyname = readRecord.nextInt();
    this.procReadRecord.run(conn, keyname, this.results);
  }

  private void readModifyWriteRecord(Connection conn) throws SQLException {
    int keyname = readRecord.nextInt();
    this.buildParameters();
    this.procReadModifyWriteRecord.run(conn, keyname, this.params, this.results);
  }

  private void insertRecord(Connection conn) throws SQLException {
    int keyname = insertRecord.nextInt();
    this.buildParameters();
    this.procInsertRecord.run(conn, keyname, this.params);
  }

  private void deleteRecord(Connection conn) throws SQLException {
    int keyname = readRecord.nextInt();
    this.procDeleteRecord.run(conn, keyname);
  }

  private void buildParameters() {
    for (int i = 0; i < this.params.length; i++) {
      this.params[i] = new String(TextGenerator.randomFastChars(rng(), this.data));
    }
  }
}
