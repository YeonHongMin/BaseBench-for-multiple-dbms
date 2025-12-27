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

package com.oltpbenchmark.benchmarks.resourcestresser;

import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.resourcestresser.procedures.*;
import com.oltpbenchmark.types.TransactionStatus;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

public final class ResourceStresserWorker extends Worker<ResourceStresserBenchmark> {
  public static final int CONTENTION1_howManyKeys = 10;
  public static final int CONTENTION1_howManyUpdates = 20;
  public static final int CONTENTION1_sleepLength = 1;

  public static final int CONTENTION2_howManyKeys = 10;
  public static final int CONTENTION2_howManyUpdates = 5;
  public static final int CONTENTION2_sleepLength = 2;

  public static final int IO1_howManyColsPerRow = 16;
  public static final int IO1_howManyRowsPerUpdate = 10;
  public static final int IO1_howManyUpdatePerTransaction = 10;

  public static final int IO2_howManyUpdatePerTransaction = 50;
  public static final boolean IO2_makeSureWorketSetFitsInMemory = true;

  public static final int CPU1_howManyPerTrasaction = 10;
  public static final int CPU1_sleep = 1;
  public static final int CPU1_nestedLevel = 5;

  public static final int CPU2_howManyPerTrasaction = 5;
  public static final int CPU2_sleep = 2;
  public static final int CPU2_nestedLevel = 5;

  public static final Random gen = new Random(1); // I change the random seed every time!

  private final int keyRange;
  private final int numKeys;

  public ResourceStresserWorker(
      ResourceStresserBenchmark benchmarkModule, int id, int numKeys, int keyRange) {
    super(benchmarkModule, id);
    this.numKeys = numKeys;
    this.keyRange = keyRange;
  }

  @Override
  protected TransactionStatus executeWork(Connection conn, TransactionType nextTrans)
      throws UserAbortException, SQLException {
    if (nextTrans.getProcedureClass().equals(CPU1.class)) {
      cpu1Transaction(conn, CPU1_howManyPerTrasaction, CPU1_sleep, CPU1_nestedLevel);
    } else if (nextTrans.getProcedureClass().equals(CPU2.class)) {
      cpu2Transaction(conn, CPU2_howManyPerTrasaction, CPU2_sleep, CPU2_nestedLevel);
    } else if (nextTrans.getProcedureClass().equals(IO1.class)) {
      io1Transaction(
          conn,
          IO1_howManyColsPerRow,
          IO1_howManyRowsPerUpdate,
          IO1_howManyUpdatePerTransaction,
          keyRange);
    } else if (nextTrans.getProcedureClass().equals(IO2.class)) {
      io2Transaction(
          conn, IO2_howManyUpdatePerTransaction, IO2_makeSureWorketSetFitsInMemory, keyRange);
    } else if (nextTrans.getProcedureClass().equals(Contention1.class)) {
      contention1Transaction(conn, CONTENTION1_howManyUpdates, CONTENTION1_sleepLength);
    } else if (nextTrans.getProcedureClass().equals(Contention2.class)) {
      contention2Transaction(
          conn, CONTENTION2_howManyKeys, CONTENTION2_howManyUpdates, CONTENTION2_sleepLength);
    }
    return (TransactionStatus.SUCCESS);
  }

  private void contention1Transaction(Connection conn, int howManyUpdates, int sleepLength)
      throws SQLException {
    Contention1 proc = this.getProcedure(Contention1.class);

    proc.run(conn, howManyUpdates, sleepLength, this.numKeys);
  }

  private void contention2Transaction(
      Connection conn, int howManyKeys, int howManyUpdates, int sleepLength) throws SQLException {
    Contention2 proc = this.getProcedure(Contention2.class);

    proc.run(conn, howManyKeys, howManyUpdates, sleepLength, this.numKeys);
  }

  private void io1Transaction(
      Connection conn,
      int howManyColsPerRow,
      int howManyUpdatesPerTransaction,
      int howManyRowsPerUpdate,
      int keyRange)
      throws SQLException {
    IO1 proc = this.getProcedure(IO1.class);

    proc.run(
        conn,
        this.getId(),
        howManyColsPerRow,
        howManyUpdatesPerTransaction,
        howManyRowsPerUpdate,
        keyRange);
  }

  private void io2Transaction(
      Connection conn,
      int howManyUpdatesPerTransaction,
      boolean makeSureWorkerSetFitsInMemory,
      int keyRange)
      throws SQLException {
    IO2 proc = this.getProcedure(IO2.class);

    proc.run(
        conn, this.getId(), howManyUpdatesPerTransaction, makeSureWorkerSetFitsInMemory, keyRange);
  }

  private void cpu1Transaction(
      Connection conn, int howManyPerTransaction, int sleepLength, int nestedLevel)
      throws SQLException {
    CPU1 proc = this.getProcedure(CPU1.class);

    proc.run(conn, howManyPerTransaction, sleepLength, nestedLevel);
  }

  private void cpu2Transaction(
      Connection conn, int howManyPerTransaction, int sleepLength, int nestedLevel)
      throws SQLException {
    CPU2 proc = this.getProcedure(CPU2.class);

    proc.run(conn, howManyPerTransaction, sleepLength, nestedLevel);
  }
}
