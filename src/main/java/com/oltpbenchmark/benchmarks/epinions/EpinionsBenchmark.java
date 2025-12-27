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

package com.oltpbenchmark.benchmarks.epinions;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.epinions.procedures.GetAverageRatingByTrustedUser;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.SQLUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EpinionsBenchmark extends BenchmarkModule {

  private static final Logger LOG = LoggerFactory.getLogger(EpinionsBenchmark.class);

  public EpinionsBenchmark(WorkloadConfiguration workConf) {
    super(workConf);
  }

  @Override
  protected Package getProcedurePackageImpl() {
    return GetAverageRatingByTrustedUser.class.getPackage();
  }

  @Override
  protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
    List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();
    DatabaseType databaseType = this.getWorkloadConfiguration().getDatabaseType();

    try {

      // LOADING FROM THE DATABASE IMPORTANT INFORMATION
      // LIST OF USERS

      Table t = this.getCatalog().getTable("USERACCT");

      ArrayList<String> user_ids = new ArrayList<>();
      ArrayList<String> item_ids = new ArrayList<>();
      String userCount = SQLUtil.selectColValues(databaseType, t, "u_id");

      try (Connection metaConn = this.makeConnection()) {
        try (Statement stmt = metaConn.createStatement()) {
          try (ResultSet res = stmt.executeQuery(userCount)) {
            while (res.next()) {
              user_ids.add(res.getString(1));
            }
          }
          if (LOG.isDebugEnabled()) {
            LOG.debug("Loaded: {} User ids", user_ids.size());
          }
          // LIST OF ITEMS AND
          t = this.getCatalog().getTable("ITEM");

          String itemCount = SQLUtil.selectColValues(databaseType, t, "i_id");
          try (ResultSet res = stmt.executeQuery(itemCount)) {
            while (res.next()) {
              item_ids.add(res.getString(1));
            }
          }
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("Loaded: {} Item ids", item_ids.size());
        }
      }

      // Now create the workers.
      for (int i = 0; i < workConf.getTerminals(); ++i) {
        workers.add(new EpinionsWorker(this, i, user_ids, item_ids));
      }

    } catch (SQLException e) {
      LOG.error(e.getMessage(), e);
    }
    return workers;
  }

  @Override
  protected Loader<EpinionsBenchmark> makeLoaderImpl() {
    return new EpinionsLoader(this);
  }
}
