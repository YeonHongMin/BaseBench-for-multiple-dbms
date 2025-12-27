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

/*
<<<<<<< HEAD
<<<<<<< HEAD
 *   TPC-H implementation
=======
 *   TPC-H 구현
>>>>>>> master
=======
 *   TPC-H implementation
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 *   Ben Reilly (bd.reilly@gmail.com)
 *   Ippokratis Pandis (ipandis@us.ibm.com)
 *
 */

package com.oltpbenchmark.benchmarks.tpch;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.tpch.procedures.Q1;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TPCHBenchmark extends BenchmarkModule {
  private static final Logger LOG = LoggerFactory.getLogger(TPCHBenchmark.class);

  public TPCHBenchmark(WorkloadConfiguration workConf) {
    super(workConf);
  }

  @Override
  protected Package getProcedurePackageImpl() {
    return (Q1.class.getPackage());
  }

  @Override
  protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
    List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();

    int numTerminals = workConf.getTerminals();
<<<<<<< HEAD
<<<<<<< HEAD
    LOG.info(String.format("Creating %d workers for TPC-H", numTerminals));
=======
    LOG.info(String.format("TPC-H를 위한 %d개의 워커 생성", numTerminals));
>>>>>>> master
=======
    LOG.info(String.format("Creating %d workers for TPC-H", numTerminals));
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    for (int i = 0; i < numTerminals; i++) {
      workers.add(new TPCHWorker(this, i));
    }
    return workers;
  }

  @Override
  protected Loader<TPCHBenchmark> makeLoaderImpl() {
    return new TPCHLoader(this);
  }
}
