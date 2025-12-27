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

import java.text.SimpleDateFormat;

public final class TPCCConfig {

  public static final String[] nameTokens = {
    "BAR", "OUGHT", "ABLE", "PRI", "PRES", "ESE", "ANTI", "CALLY", "ATION", "EING"
  };

  public static final String terminalPrefix = "Term-";

  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static final int configWhseCount = 1;
  public static final int configItemCount = 100000; // TPC-C 표준 = 100,000
  public static final int configDistPerWhse = 10; // TPC-C 표준 = 10
  public static final int configCustPerDist = 3000; // TPC-C 표준 = 3,000

  /** 새 주문 트랜잭션을 롤백할 때 사용하는 잘못된 아이템 ID입니다. */
  public static final int INVALID_ITEM_ID = -12345;
}
