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

package com.oltpbenchmark.benchmarks.smallbank;

public abstract class SmallBankConstants {

  // ----------------------------------------------------------------
<<<<<<< HEAD
<<<<<<< HEAD
  // TABLE NAMES
=======
  // 테이블 이름
>>>>>>> master
=======
  // TABLE NAMES
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  // ----------------------------------------------------------------
  public static final String TABLENAME_ACCOUNTS = "accounts";
  public static final String TABLENAME_SAVINGS = "savings";
  public static final String TABLENAME_CHECKING = "checking";

  // ----------------------------------------------------------------
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  // ACCOUNT INFORMATION
  // ----------------------------------------------------------------

  // Default number of customers in bank
<<<<<<< HEAD
=======
  // 계정 정보
  // ----------------------------------------------------------------

  // 은행의 기본 고객 수
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public static final int NUM_ACCOUNTS = 1000000;

  public static final boolean HOTSPOT_USE_FIXED_SIZE = false;
  public static final double HOTSPOT_PERCENTAGE = 25; // [0% - 100%]
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public static final int HOTSPOT_FIXED_SIZE = 100; // fixed number of tuples

  // ----------------------------------------------------------------
  // ADDITIONAL CONFIGURATION SETTINGS
  // ----------------------------------------------------------------

  // Initial balance amount
  // We'll just make it really big so that they never run out of money
<<<<<<< HEAD
=======
  public static final int HOTSPOT_FIXED_SIZE = 100; // 고정된 튜플 수

  // ----------------------------------------------------------------
  // 추가 구성 설정
  // ----------------------------------------------------------------

  // 초기 잔액
  // 돈이 떨어지지 않도록 매우 크게 설정합니다.
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public static final int MIN_BALANCE = 10000;
  public static final int MAX_BALANCE = 50000;

  // ----------------------------------------------------------------
<<<<<<< HEAD
<<<<<<< HEAD
  // PROCEDURE PARAMETERS
  // These amounts are from the original code
=======
  // 프로시저 매개변수
  // 이 금액은 원본 코드에서 가져온 것입니다.
>>>>>>> master
=======
  // PROCEDURE PARAMETERS
  // These amounts are from the original code
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  // ----------------------------------------------------------------
  public static final double PARAM_SEND_PAYMENT_AMOUNT = 5.0d;
  public static final double PARAM_DEPOSIT_CHECKING_AMOUNT = 1.3d;
  public static final double PARAM_TRANSACT_SAVINGS_AMOUNT = 20.20d;
  public static final double PARAM_WRITE_CHECK_AMOUNT = 5.0d;
}
