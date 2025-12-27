/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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
<<<<<<< HEAD
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
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.api;

import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
<<<<<<< HEAD
<<<<<<< HEAD
 * A LoaderThread is responsible for loading some portion of a benchmark's database. Note that each
 * LoaderThread has its own database Connection handle.
=======
 * LoaderThread는 벤치마크 데이터베이스의 일부를 로드하는 역할을 합니다. 각 LoaderThread는 자체 데이터베이스 Connection 핸들을 가지고 있습니다.
>>>>>>> master
=======
 * A LoaderThread is responsible for loading some portion of a benchmark's database. Note that each
 * LoaderThread has its own database Connection handle.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 */
public abstract class LoaderThread implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(LoaderThread.class);

  private final BenchmarkModule benchmarkModule;

  public LoaderThread(BenchmarkModule benchmarkModule) {
    this.benchmarkModule = benchmarkModule;
  }

  @Override
  public final void run() {
    beforeLoad();
    try (Connection conn = benchmarkModule.makeConnection()) {
      load(conn);
    } catch (SQLException ex) {
      SQLException next_ex = ex.getNextException();
      String msg =
          String.format(
              "Unexpected error when loading %s database",
              benchmarkModule.getBenchmarkName().toUpperCase());
      LOG.error(msg, next_ex);
      throw new RuntimeException(ex);
    } finally {
      afterLoad();
    }
  }

  /**
   * This is the method that each LoaderThread has to implement
   *
   * @param conn
   * @throws SQLException
   */
  public abstract void load(Connection conn) throws SQLException;

  public void beforeLoad() {
    // useful for implementing waits for countdown latches, this ensures we open the connection
    // right before its used to avoid stale connections
  }

  public void afterLoad() {
    // useful for counting down latches
  }
}
