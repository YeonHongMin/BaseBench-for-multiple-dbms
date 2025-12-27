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

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.tpcc.procedures.NewOrder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TPCCBenchmark extends BenchmarkModule {
  private static final Logger LOG = LoggerFactory.getLogger(TPCCBenchmark.class);

  public TPCCBenchmark(WorkloadConfiguration workConf) {
    super(workConf);
  }

  @Override
  protected Package getProcedurePackageImpl() {
    return (NewOrder.class.getPackage());
  }

  @Override
  protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
    List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();

    try {
      List<TPCCWorker> terminals = createTerminals();
      workers.addAll(terminals);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }

    return workers;
  }

  @Override
  protected Loader<TPCCBenchmark> makeLoaderImpl() {
    return new TPCCLoader(this);
  }

  protected List<TPCCWorker> createTerminals() throws SQLException {

    TPCCWorker[] terminals = new TPCCWorker[workConf.getTerminals()];

    int numWarehouses = (int) workConf.getScaleFactor();
    if (numWarehouses <= 0) {
      numWarehouses = 1;
    }

    int numTerminals = workConf.getTerminals();

    // 터미널을 창고별로 최대한 균등하게 분배합니다.
    // 예: 7개 창고에 10개 터미널이 있을 경우
    // 1, 1, 2, 1, 2, 1, 2 순서로 분배됩니다.
    final double terminalsPerWarehouse = (double) numTerminals / numWarehouses;
    int workerId = 0;

    for (int w = 0; w < numWarehouses; w++) {
      // 현재 창고에 할당할 터미널 개수를 계산합니다.
      int lowerTerminalId = (int) (w * terminalsPerWarehouse);
      int upperTerminalId = (int) ((w + 1) * terminalsPerWarehouse);
      // 이중 반올림 오류를 방지합니다.
      int w_id = w + 1;
      if (w_id == numWarehouses) {
        upperTerminalId = numTerminals;
      }
      int numWarehouseTerminals = upperTerminalId - lowerTerminalId;

      if (LOG.isDebugEnabled()) {
        LOG.debug(
            String.format(
                "w_id %d = %d terminals [lower=%d / upper%d]",
                w_id, numWarehouseTerminals, lowerTerminalId, upperTerminalId));
      }

      final double districtsPerTerminal =
          TPCCConfig.configDistPerWhse / (double) numWarehouseTerminals;
      for (int terminalId = 0; terminalId < numWarehouseTerminals; terminalId++) {
        int lowerDistrictId = (int) (terminalId * districtsPerTerminal);
        int upperDistrictId = (int) ((terminalId + 1) * districtsPerTerminal);
        if (terminalId + 1 == numWarehouseTerminals) {
          upperDistrictId = TPCCConfig.configDistPerWhse;
        }
        lowerDistrictId += 1;

        TPCCWorker terminal =
            new TPCCWorker(this, workerId++, w_id, lowerDistrictId, upperDistrictId, numWarehouses);
        terminals[lowerTerminalId + terminalId] = terminal;
      }
    }

    return Arrays.asList(terminals);
  }
}
