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

package com.oltpbenchmark.benchmarks.seats;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.seats.procedures.LoadConfig;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.util.RandomGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class SEATSBenchmark extends BenchmarkModule {

  private final RandomGenerator rng = new RandomGenerator((int) System.currentTimeMillis());

  public SEATSBenchmark(WorkloadConfiguration workConf) {
    super(workConf);
    this.registerSupplementalProcedure(LoadConfig.class);
  }

  public String getDataDir() {
    return "/benchmarks/" + getBenchmarkName();
  }

  public RandomGenerator getRandomGenerator() {
    return (this.rng);
  }

  @Override
  protected Package getProcedurePackageImpl() {
    return (LoadConfig.class.getPackage());
  }

  @Override
  protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
    List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();
    for (int i = 0; i < this.workConf.getTerminals(); ++i) {
      workers.add(new SEATSWorker(this, i));
    }
    return (workers);
  }

  @Override
  protected Loader<SEATSBenchmark> makeLoaderImpl() {
    return new SEATSLoader(this);
  }

  /**
   * Return the path of the CSV file that has data for the given Table catalog handle
   *
   * @param data_dir
   * @param catalog_tbl
   * @return
   */
  public static String getTableDataFilePath(String data_dir, Table catalog_tbl) {
    return String.format(
        "%s%stable.%s.csv", data_dir, File.separator, catalog_tbl.getName().toLowerCase());
  }
}
