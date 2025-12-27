/*
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *  Copyright 2015 by OLTPBenchmark Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
<<<<<<< HEAD
=======
 *  저작권 2015 OLTPBenchmark 프로젝트
 *
 *  Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 *  라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 *  라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 *  이 소프트웨어는 "있는 그대로" 배포되며,
 *  명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 *  라이선스에서 허용하는 권한과 제한 사항은
 *  라이선스의 본문을 참조하십시오.
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 */

package com.oltpbenchmark.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.oltpbenchmark.api.Procedure.UserAbortException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

public abstract class AbstractTestWorker<T extends BenchmarkModule> extends AbstractTestCase<T> {

  protected static final int NUM_TERMINALS = 1;

  protected List<Worker<? extends BenchmarkModule>> workers;

  public AbstractTestWorker() {
    super(true, true);
  }

  public AbstractTestWorker(String ddlOverridePath) {
    super(true, true, ddlOverridePath);
  }

  public AbstractTestWorker(String ddlOverridePath, String sessionSetupFile) {
    super(true, true, ddlOverridePath, sessionSetupFile);
  }

  @Override
  public List<String> ignorableTables() {
    return null;
  }

  @Override
  protected void postCreateDatabaseSetup() throws IOException {
    this.workers = this.benchmark.makeWorkers();
    assertNotNull(this.workers);
    assertEquals(NUM_TERMINALS, this.workers.size());
  }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  /** testGetProcedure */
  @Test
  public void testGetProcedure() {
    // Make sure that we can get a Procedure handle for each TransactionType
<<<<<<< HEAD
=======
  /** 프로시저 가져오기 테스트 */
  @Test
  public void testGetProcedure() {
    // 각 TransactionType에 대해 Procedure 핸들을 가져올 수 있는지 확인합니다
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    Worker<?> w = workers.get(0);
    assertNotNull(w);
    for (Class<? extends Procedure> procClass : this.procedures()) {
      assertNotNull(procClass);
      Procedure proc = w.getProcedure(procClass);
      assertNotNull("Failed to get procedure " + procClass.getSimpleName(), proc);
      assertEquals(procClass, proc.getClass());
    }
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /* testExecuteWork
   * Similar to Worker.run()
=======
  /* 작업 실행 테스트
   * Worker.run()과 유사합니다
>>>>>>> master
=======
  /* testExecuteWork
   * Similar to Worker.run()
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   */
  @Test
  public void testExecuteWork() throws Exception {
    Worker<?> w = workers.get(0);
    assertNotNull(w);
    w.setupSession();
    w.initialize();
    assertFalse(this.conn.isReadOnly());
    for (TransactionType txnType : this.workConf.getTransTypes()) {
      if (txnType.isSupplemental()) {
        continue;
      }

      StopWatch sw = new StopWatch(txnType.toString());

      try {
        LOG.info("starting execution of [{}]", txnType);
        sw.start();
        w.executeWork(this.conn, txnType);
        sw.stop();

      } catch (UserAbortException ex) {
<<<<<<< HEAD
<<<<<<< HEAD
        // These are expected, so they can be ignored
        // Anything else is a serious error
=======
        // 이것들은 예상되는 것이므로 무시할 수 있습니다
        // 그 외의 것은 심각한 오류입니다
>>>>>>> master
=======
        // These are expected, so they can be ignored
        // Anything else is a serious error
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      } catch (Throwable ex) {
        throw new RuntimeException("Failed to execute " + txnType, ex);
      } finally {
        LOG.info(
            "completed execution of [{}] in {} ms",
            txnType.toString(),
            sw.getTime(TimeUnit.MILLISECONDS));
      }
    }
  }
}
