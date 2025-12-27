/*
 *  Copyright (C) 2013 by H-Store Project
 *  Brown University
 *  Massachusetts Institute of Technology
 *  Yale University
 *
 *  이 소프트웨어 및 관련 문서 파일(이하 "소프트웨어")의 복사본을 얻는 모든 사람에게
 *  무료로 소프트웨어를 다루는 권한이 부여됩니다. 여기에는 제한 없이 사용, 복사, 수정,
 *  병합, 게시, 배포, 재라이센스 및/또는 소프트웨어의 복사본을 판매할 권리가 포함되며,
 *  소프트웨어를 제공받은 사람이 다음 조건에 따라 이를 수행할 수 있습니다:
 *
 *  위의 저작권 고지와 이 권한 고지는 소프트웨어의 모든 복사본 또는 중요한 부분에
 *  포함되어야 합니다.
 *
 *  소프트웨어는 "있는 그대로" 제공되며, 명시적이거나 묵시적인 어떠한 종류의 보증도
 *  없습니다. 상품성, 특정 목적에의 적합성 및 비침해에 대한 보증을 포함하되 이에
 *  국한되지 않습니다. 계약, 불법 행위 또는 기타 행위로 인해 발생하는 어떠한 청구,
 *  손해 또는 기타 책임에 대해서도 저작자는 책임을 지지 않습니다. 소프트웨어 또는
 *  소프트웨어의 사용 또는 기타 거래와 관련하여 발생하는 경우에도 마찬가지입니다.
 */
package com.oltpbenchmark.benchmarks.smallbank.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.smallbank.SmallBankConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 송금 프로시저
 *
 * @author pavlo
 */
public class SendPayment extends Procedure {

  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE custid = ?");

  public final SQLStmt GetCheckingBalance =
      new SQLStmt("SELECT bal FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE custid = ?");

  public final SQLStmt UpdateCheckingBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + "   SET bal = bal + ? "
              + " WHERE custid = ?");

  public void run(Connection conn, long sendAcct, long destAcct, double amount)
      throws SQLException {
    // 계정 정보 가져오기
    try (PreparedStatement stmt0 = this.getPreparedStatement(conn, GetAccount, sendAcct)) {
      try (ResultSet r0 = stmt0.executeQuery()) {
        if (!r0.next()) {
          String msg = "Invalid account '" + sendAcct + "'";
          throw new UserAbortException(msg);
        }
      }
    }

    try (PreparedStatement stmt1 = this.getPreparedStatement(conn, GetAccount, destAcct)) {
      try (ResultSet r1 = stmt1.executeQuery()) {
        if (!r1.next()) {
          String msg = "Invalid account '" + destAcct + "'";
          throw new UserAbortException(msg);
        }
      }
    }

    // 송신자의 계정 잔액 가져오기
    double balance;

    try (PreparedStatement balStmt0 =
        this.getPreparedStatement(conn, GetCheckingBalance, sendAcct)) {
      try (ResultSet balRes0 = balStmt0.executeQuery()) {
        if (!balRes0.next()) {
          String msg =
              String.format(
                  "No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, sendAcct);
          throw new UserAbortException(msg);
        }
        balance = balRes0.getDouble(1);
      }
    }

    // 충분한 돈이 있는지 확인합니다.
    if (balance < amount) {
      String msg =
          String.format(
              "Insufficient %s funds for customer #%d",
              SmallBankConstants.TABLENAME_CHECKING, sendAcct);
      throw new UserAbortException(msg);
    }

    // 차변
    try (PreparedStatement updateStmt =
        this.getPreparedStatement(conn, UpdateCheckingBalance, amount * -1d, sendAcct)) {
      updateStmt.executeUpdate();
    }
    // 대변
    try (PreparedStatement updateStmt =
        this.getPreparedStatement(conn, UpdateCheckingBalance, amount, destAcct)) {
      updateStmt.executeUpdate();
    }
  }
}
