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

package com.oltpbenchmark.benchmarks.smallbank;

import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.LoaderThread;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.util.RandomDistribution.DiscreteRNG;
import com.oltpbenchmark.util.RandomDistribution.Gaussian;
import com.oltpbenchmark.util.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * SmallBankBenchmark 로더
 *
 * @author pavlo
 */
public final class SmallBankLoader extends Loader<SmallBankBenchmark> {
  private final Table catalogAccts;
  private final Table catalogSavings;
  private final Table catalogChecking;

  private final String sqlAccts;
  private final String sqlSavings;
  private final String sqlChecking;

  private final long numAccounts;
  private final int custNameLength;

  public SmallBankLoader(SmallBankBenchmark benchmark) {
    super(benchmark);

    this.catalogAccts = this.benchmark.getCatalog().getTable(SmallBankConstants.TABLENAME_ACCOUNTS);
    this.catalogSavings =
        this.benchmark.getCatalog().getTable(SmallBankConstants.TABLENAME_SAVINGS);
    this.catalogChecking =
        this.benchmark.getCatalog().getTable(SmallBankConstants.TABLENAME_CHECKING);

    this.sqlAccts = SQLUtil.getInsertSQL(this.catalogAccts, this.getDatabaseType());
    this.sqlSavings = SQLUtil.getInsertSQL(this.catalogSavings, this.getDatabaseType());
    this.sqlChecking = SQLUtil.getInsertSQL(this.catalogChecking, this.getDatabaseType());

    this.numAccounts = benchmark.numAccounts;
    this.custNameLength = SmallBankBenchmark.getCustomerNameLength(this.catalogAccts);
  }

  @Override
  public List<LoaderThread> createLoaderThreads() throws SQLException {
    List<LoaderThread> threads = new ArrayList<>();
    int batchSize = 100000;
    long start = 0;
    while (start < this.numAccounts) {
      long stop = Math.min(start + batchSize, this.numAccounts);
      threads.add(new Generator(start, stop));
      start = stop;
    }
    return (threads);
  }

  /** 계정 범위를 생성할 수 있는 스레드 */
  private class Generator extends LoaderThread {
    private final long start;
    private final long stop;
    private final DiscreteRNG randBalance;

    PreparedStatement stmtAccts;
    PreparedStatement stmtSavings;
    PreparedStatement stmtChecking;

    public Generator(long start, long stop) {
      super(benchmark);
      this.start = start;
      this.stop = stop;
      this.randBalance =
          new Gaussian(
              benchmark.rng(), SmallBankConstants.MIN_BALANCE, SmallBankConstants.MAX_BALANCE);
    }

    @Override
    public void load(Connection conn) {
      try {
        this.stmtAccts = conn.prepareStatement(SmallBankLoader.this.sqlAccts);
        this.stmtSavings = conn.prepareStatement(SmallBankLoader.this.sqlSavings);
        this.stmtChecking = conn.prepareStatement(SmallBankLoader.this.sqlChecking);

        final String acctNameFormat = "%0" + custNameLength + "d";
        int batchSize = 0;
        for (long acctId = this.start; acctId < this.stop; acctId++) {
          // 계정
          String acctName = String.format(acctNameFormat, acctId);
          stmtAccts.setLong(1, acctId);
          stmtAccts.setString(2, acctName);
          stmtAccts.addBatch();

          // 당좌 계정
          stmtChecking.setLong(1, acctId);
          stmtChecking.setInt(2, this.randBalance.nextInt());
          stmtChecking.addBatch();

          // 저축 계정
          stmtSavings.setLong(1, acctId);
          stmtSavings.setInt(2, this.randBalance.nextInt());
          stmtSavings.addBatch();

          if (++batchSize >= workConf.getBatchSize()) {
            this.loadTables(conn);
            batchSize = 0;
          }
        }
        if (batchSize > 0) {
          this.loadTables(conn);
        }
      } catch (SQLException ex) {
        LOG.error("Failed to load data", ex);
        throw new RuntimeException(ex);
      }
    }

    private void loadTables(Connection conn) throws SQLException {
      this.stmtAccts.executeBatch();
      this.stmtSavings.executeBatch();
      this.stmtChecking.executeBatch();
    }
  }
}
