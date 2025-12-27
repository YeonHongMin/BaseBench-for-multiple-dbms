/*
 *  Copyright (C) 2013 by H-Store Project
 *  Brown University
 *  Massachusetts Institute of Technology
 *  Yale University
 *
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
<<<<<<< HEAD
=======
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
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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
<<<<<<< HEAD
<<<<<<< HEAD
 * Amalgamate Procedure Original version by Mohammad Alomari and Michael Cahill
=======
 * 통합 프로시저 원본 버전: Mohammad Alomari 및 Michael Cahill
>>>>>>> master
=======
 * Amalgamate Procedure Original version by Mohammad Alomari and Michael Cahill
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 * @author pavlo
 */
public class Amalgamate extends Procedure {

  // 2013-05-05
<<<<<<< HEAD
<<<<<<< HEAD
  // In the original version of the benchmark, this is suppose to be a look up
  // on the customer's name. We don't have fast implementation of replicated
  // secondary indexes, so we'll just ignore that part for now.
=======
  // 벤치마크의 원본 버전에서는 이것이 고객 이름에 대한 조회여야 합니다.
  // 복제된 보조 인덱스의 빠른 구현이 없으므로 지금은 해당 부분을 무시합니다.
>>>>>>> master
=======
  // In the original version of the benchmark, this is suppose to be a look up
  // on the customer's name. We don't have fast implementation of replicated
  // secondary indexes, so we'll just ignore that part for now.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE custid = ?");

  public final SQLStmt GetSavingsBalance =
      new SQLStmt("SELECT bal FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ?");

  public final SQLStmt GetCheckingBalance =
      new SQLStmt("SELECT bal FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE custid = ?");

  public final SQLStmt UpdateSavingsBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_SAVINGS
              + "   SET bal = bal - ? "
              + " WHERE custid = ?");

  public final SQLStmt UpdateCheckingBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + "   SET bal = bal + ? "
              + " WHERE custid = ?");

  public final SQLStmt ZeroCheckingBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + "   SET bal = 0.0 "
              + " WHERE custid = ?");

  public void run(Connection conn, long custId0, long custId1) throws SQLException {
<<<<<<< HEAD
<<<<<<< HEAD
    // Get Account Information
=======
    // 계정 정보 가져오기
>>>>>>> master
=======
    // Get Account Information
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    try (PreparedStatement stmt0 = this.getPreparedStatement(conn, GetAccount, custId0)) {
      try (ResultSet r0 = stmt0.executeQuery()) {
        if (!r0.next()) {
          String msg = "Invalid account '" + custId0 + "'";
          throw new UserAbortException(msg);
        }
      }
    }

    try (PreparedStatement stmt1 = this.getPreparedStatement(conn, GetAccount, custId1)) {
      try (ResultSet r1 = stmt1.executeQuery()) {
        if (!r1.next()) {
          String msg = "Invalid account '" + custId1 + "'";
          throw new UserAbortException(msg);
        }
      }
    }

<<<<<<< HEAD
<<<<<<< HEAD
    // Get Balance Information
=======
    // 잔액 정보 가져오기
>>>>>>> master
=======
    // Get Balance Information
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    double savingsBalance;
    try (PreparedStatement balStmt0 = this.getPreparedStatement(conn, GetSavingsBalance, custId0)) {
      try (ResultSet balRes0 = balStmt0.executeQuery()) {
        if (!balRes0.next()) {
          String msg =
              String.format(
                  "No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId0);
          throw new UserAbortException(msg);
        }
        savingsBalance = balRes0.getDouble(1);
      }
    }

    double checkingBalance;
    try (PreparedStatement balStmt1 =
        this.getPreparedStatement(conn, GetCheckingBalance, custId1)) {
      try (ResultSet balRes1 = balStmt1.executeQuery()) {
        if (!balRes1.next()) {
          String msg =
              String.format(
                  "No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId1);
          throw new UserAbortException(msg);
        }

        checkingBalance = balRes1.getDouble(1);
      }
    }

    double total = checkingBalance + savingsBalance;
    // assert(total >= 0);

<<<<<<< HEAD
<<<<<<< HEAD
    // Update Balance Information
=======
    // 잔액 정보 업데이트
>>>>>>> master
=======
    // Update Balance Information
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    try (PreparedStatement updateStmt0 =
        this.getPreparedStatement(conn, ZeroCheckingBalance, custId0)) {
      updateStmt0.executeUpdate();
    }

    try (PreparedStatement updateStmt1 =
        this.getPreparedStatement(conn, UpdateSavingsBalance, total, custId1)) {
      updateStmt1.executeUpdate();
    }
  }
}
