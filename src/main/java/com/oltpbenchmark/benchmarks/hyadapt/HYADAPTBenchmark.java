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

package com.oltpbenchmark.benchmarks.hyadapt;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.hyadapt.procedures.ReadRecord1;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.util.SQLUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HYADAPTBenchmark extends BenchmarkModule {
  private static final Logger LOG = LoggerFactory.getLogger(HYADAPTBenchmark.class);

  public HYADAPTBenchmark(WorkloadConfiguration workConf) {
    super(workConf);
  }

  @Override
  protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
    List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();

    // LOADING FROM THE DATABASE IMPORTANT INFORMATION
    // LIST OF USERS

    Table t = this.getCatalog().getTable("HTABLE");

    String userCount = SQLUtil.getCountSQL(this.workConf.getDatabaseType(), t);
    int init_record_count = 0;
    try (Connection metaConn = this.makeConnection()) {

      try (Statement stmt = metaConn.createStatement()) {
        try (ResultSet res = stmt.executeQuery(userCount)) {
          while (res.next()) {
            init_record_count = res.getInt(1);
          }
        }
      }
      //
      for (int i = 0; i < workConf.getTerminals(); ++i) {
        //                Connection conn = this.makeConnection();
        //                conn.setAutoCommit(false);
        workers.add(new HYADAPTWorker(this, i, init_record_count + 1));
      }

      LOG.info("Init Record Count :: {}", init_record_count);
    } catch (SQLException e) {
      LOG.error(e.getMessage(), e);
    }

    return workers;
  }

  @Override
  protected Loader<HYADAPTBenchmark> makeLoaderImpl() {
    return new HYADAPTLoader(this);
  }

  @Override
  protected Package getProcedurePackageImpl() {
    // TODO Auto-generated method stub
    return ReadRecord1.class.getPackage();
  }
}
