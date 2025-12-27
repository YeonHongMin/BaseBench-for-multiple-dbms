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

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.smallbank.procedures.*;
import com.oltpbenchmark.types.TransactionStatus;
import com.oltpbenchmark.util.RandomDistribution.DiscreteRNG;
import com.oltpbenchmark.util.RandomDistribution.Flat;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SmallBank 벤치마크 작업 드라이버
 *
 * @author pavlo
 */
public final class SmallBankWorker extends Worker<SmallBankBenchmark> {
  private static final Logger LOG = LoggerFactory.getLogger(SmallBankWorker.class);

  private final Amalgamate procAmalgamate;
  private final Balance procBalance;
  private final DepositChecking procDepositChecking;
  private final SendPayment procSendPayment;
  private final TransactSavings procTransactSavings;
  private final WriteCheck procWriteCheck;

  private final DiscreteRNG rng;
  private final long numAccounts;
  private final int custNameLength;
  private final String custNameFormat;
  private final long[] custIdsBuffer = {-1L, -1L};

  public SmallBankWorker(SmallBankBenchmark benchmarkModule, int id) {
    super(benchmarkModule, id);

    // 이것은 트랜잭션을 실행할 때마다 해시맵 조회를 호출하는 것을 피하기 위한
    // 작은 속도 향상입니다. 코어가 많지 않은 클라이언트 머신에서 이렇게 하는 것이 중요합니다.
    this.procAmalgamate = this.getProcedure(Amalgamate.class);
    this.procBalance = this.getProcedure(Balance.class);
    this.procDepositChecking = this.getProcedure(DepositChecking.class);
    this.procSendPayment = this.getProcedure(SendPayment.class);
    this.procTransactSavings = this.getProcedure(TransactSavings.class);
    this.procWriteCheck = this.getProcedure(WriteCheck.class);

    this.numAccounts = benchmarkModule.numAccounts;
    this.custNameLength =
        SmallBankBenchmark.getCustomerNameLength(
            benchmarkModule.getCatalog().getTable(SmallBankConstants.TABLENAME_ACCOUNTS));
    this.custNameFormat = "%0" + this.custNameLength + "d";
    this.rng = new Flat(rng(), 0, this.numAccounts);
  }

  protected void generateCustIds(boolean needsTwoAccts) {
    for (int i = 0; i < this.custIdsBuffer.length; i++) {
      this.custIdsBuffer[i] = this.rng.nextLong();

      // 절대 같을 수 없습니다!
      if (i > 0 && this.custIdsBuffer[i - 1] == this.custIdsBuffer[i]) {
        i--;
        continue;
      }

      // 하나의 acctId만 필요한 경우 여기서 중단합니다.
      if (i == 0 && !needsTwoAccts) {
        break;
      }
      // 두 개의 acctId가 필요한 경우 두 번째 것을 생성해야 합니다.
      if (i == 0) {
        continue;
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug(String.format("Accounts: %s", Arrays.toString(this.custIdsBuffer)));
    }
  }

  @Override
  protected TransactionStatus executeWork(Connection conn, TransactionType txnType)
      throws UserAbortException, SQLException {
    Class<? extends Procedure> procClass = txnType.getProcedureClass();

    // 통합
    if (procClass.equals(Amalgamate.class)) {
      this.generateCustIds(true);
      this.procAmalgamate.run(conn, this.custIdsBuffer[0], this.custIdsBuffer[1]);

      // 잔액
    } else if (procClass.equals(Balance.class)) {
      this.generateCustIds(false);
      String custName = String.format(this.custNameFormat, this.custIdsBuffer[0]);
      this.procBalance.run(conn, custName);

      // 당좌 예금
    } else if (procClass.equals(DepositChecking.class)) {
      this.generateCustIds(false);
      String custName = String.format(this.custNameFormat, this.custIdsBuffer[0]);
      this.procDepositChecking.run(
          conn, custName, SmallBankConstants.PARAM_DEPOSIT_CHECKING_AMOUNT);

      // 송금
    } else if (procClass.equals(SendPayment.class)) {
      this.generateCustIds(true);
      this.procSendPayment.run(
          conn,
          this.custIdsBuffer[0],
          this.custIdsBuffer[1],
          SmallBankConstants.PARAM_SEND_PAYMENT_AMOUNT);

      // 저축 거래
    } else if (procClass.equals(TransactSavings.class)) {
      this.generateCustIds(false);
      String custName = String.format(this.custNameFormat, this.custIdsBuffer[0]);
      this.procTransactSavings.run(
          conn, custName, SmallBankConstants.PARAM_TRANSACT_SAVINGS_AMOUNT);

      // 수표 작성
    } else if (procClass.equals(WriteCheck.class)) {
      this.generateCustIds(false);
      String custName = String.format(this.custNameFormat, this.custIdsBuffer[0]);
      this.procWriteCheck.run(conn, custName, SmallBankConstants.PARAM_WRITE_CHECK_AMOUNT);
    }

    return TransactionStatus.SUCCESS;
  }
}
