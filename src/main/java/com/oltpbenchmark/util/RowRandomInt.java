/*
 * Copyright 2020 Trino
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.oltpbenchmark.util;

/** 선형 합동 방식으로 난수를 생성하는 도우미 클래스입니다. */
public class RowRandomInt {
  private static final long MULTIPLIER = 16807;
  private static final long MODULUS = 2147483647;
  private static final long DEFAULT_SEED = 19620718;

  private final int seedsPerRow;

  private long seed;
  private int usage;

  /** 지정된 시드와 행당 난수 개수를 사용하는 난수 생성기를 생성합니다. */
  public RowRandomInt(long seed, int seedsPerRow) {
    this.seed = seed;
    this.seedsPerRow = seedsPerRow;
  }

  /** 열 번호와 행당 난수 개수를 지정하여 생성기를 초기화합니다. 시드는 base + colnum * Constant입니다. */
  public RowRandomInt(int globalColumnNumber, int seedsPerRow) {
    this(globalColumnNumber, DEFAULT_SEED, seedsPerRow);
  }

  /** 열 번호, 시드, 행당 난수 개수를 지정하여 생성기를 초기화합니다. 시드는 base + colnum * Constant입니다. */
  public RowRandomInt(int globalColumnNumber, long seed, int seedsPerRow) {
    this.seed = seed + globalColumnNumber * (MODULUS / 799);
    this.seedsPerRow = seedsPerRow;
  }

  /** lowValue 이상 highValue 이하 범위에서 난수를 반환합니다. */
  public int nextInt(int lowValue, int highValue) {
    nextRand();

    // high가 최대 int이고 low가 0이면 오버플로우가 발생합니다.
    // 그 결과 지정된 범위를 벗어난 값이 나올 수 있는 버그가 있지만,
    // 일부 코드가 이 동작에 의존합니다.
    int intRange = highValue - lowValue + 1;
    double doubleRange = (double) intRange;
    int valueInRange = (int) ((1.0 * seed / MODULUS) * doubleRange);

    return lowValue + valueInRange;
  }

  public long nextRand() {
    seed = (seed * MULTIPLIER) % MODULUS;
    usage++;
    return seed;
  }

  /** 다음 행 시퀀스의 시작 위치로 난수 생성기를 이동합니다. 각 행은 지정된 개수의 난수를 사용하므로 파티셔닝된 데이터를 빠르게 처리할 수 있습니다. */
  public void rowFinished() {
    advanceSeed(seedsPerRow - usage);
    usage = 0;
  }

  /** 지정된 행 수만큼 난수 생성기를 전진시킵니다. 파티셔닝된 데이터 처리 시 특정 행으로 이동하기 위해 필요합니다. */
  public void advanceRows(long rowCount) {
    // 현재 행을 마무리합니다.
    if (usage != 0) {
      rowFinished();
    }

    // 시드를 앞으로 이동합니다.
    advanceSeed(seedsPerRow * rowCount);
  }

  private void advanceSeed(long count) {
    long multiplier = MULTIPLIER;
    while (count > 0) {
      if (count % 2 != 0) {
        seed = (multiplier * seed) % MODULUS;
      }
      // 정수 나눗셈은 소수점 이하를 버립니다.
      count = count / 2;
      multiplier = (multiplier * multiplier) % MODULUS;
    }
  }
}
