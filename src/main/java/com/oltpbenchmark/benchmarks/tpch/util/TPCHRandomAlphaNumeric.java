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

import com.oltpbenchmark.util.RowRandomInt;

public class TPCHRandomAlphaNumeric extends RowRandomInt {
  private static final char[] ALPHA_NUMERIC =
      "0123456789abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ,".toCharArray();

  private static final double LOW_LENGTH_MULTIPLIER = 0.4;
  private static final double HIGH_LENGTH_MULTIPLIER = 1.6;

  private static final int USAGE_PER_ROW = 9;

  private final int minLength;
  private final int maxLength;

  public TPCHRandomAlphaNumeric(long seed, int averageLength) {
    this(seed, averageLength, 1);
  }

  public TPCHRandomAlphaNumeric(long seed, int averageLength, int seedsPerRow) {
    super(seed, USAGE_PER_ROW * seedsPerRow);
    this.minLength = (int) (averageLength * LOW_LENGTH_MULTIPLIER);
    this.maxLength = (int) (averageLength * HIGH_LENGTH_MULTIPLIER);
  }

  public String nextValue() {
    char[] buffer = new char[nextInt(minLength, maxLength)];

    long charIndex = 0;
    for (int i = 0; i < buffer.length; i++) {
      if (i % 5 == 0) {
        charIndex = nextInt(0, Integer.MAX_VALUE);
      }
      buffer[i] = ALPHA_NUMERIC[(int) (charIndex & 0x3f)];
      charIndex >>= 6;
    }

    return new String(buffer);
  }
}
