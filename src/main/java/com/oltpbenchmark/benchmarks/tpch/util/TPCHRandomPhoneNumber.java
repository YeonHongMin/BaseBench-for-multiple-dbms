/*
 * Copyright 2020 Trino
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
 */
package com.oltpbenchmark.benchmarks.tpch.util;

import static java.util.Locale.ENGLISH;

import com.oltpbenchmark.util.RowRandomInt;

public class TPCHRandomPhoneNumber extends RowRandomInt {
  // 전화번호의 국가 코드로 제한됨
  private static final int NATIONS_MAX = 90;

  public TPCHRandomPhoneNumber(long seed) {
    this(seed, 1);
  }

  public TPCHRandomPhoneNumber(long seed, int seedsPerRow) {
    super(seed, 3 * seedsPerRow);
  }

  public String nextValue(long nationKey) {
    return String.format(
        ENGLISH,
        "%02d-%03d-%03d-%04d",
        (10 + (nationKey % NATIONS_MAX)),
        nextInt(100, 999),
        nextInt(100, 999),
        nextInt(1000, 9999));
  }
}
