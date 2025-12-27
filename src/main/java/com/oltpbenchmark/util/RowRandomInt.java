/*
<<<<<<< HEAD
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
 */
package com.oltpbenchmark.util;

=======
 * 저작권 2020 Trino
 *
 * Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 * 라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 * 이 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 * 라이선스에서 허용하는 권한과 제한 사항은
 * 라이선스의 본문을 참조하십시오.
 *
 */
package com.oltpbenchmark.util;

/** 선형 합동 방식으로 난수를 생성하는 도우미 클래스입니다. */
>>>>>>> master
public class RowRandomInt {
  private static final long MULTIPLIER = 16807;
  private static final long MODULUS = 2147483647;
  private static final long DEFAULT_SEED = 19620718;

  private final int seedsPerRow;

  private long seed;
  private int usage;

<<<<<<< HEAD
  /**
   * Creates a new random number generator with the specified seed and specified number of random
   * values per row.
   */
=======
  /** 지정된 시드와 행당 난수 개수를 사용하는 난수 생성기를 생성합니다. */
>>>>>>> master
  public RowRandomInt(long seed, int seedsPerRow) {
    this.seed = seed;
    this.seedsPerRow = seedsPerRow;
  }

<<<<<<< HEAD
  /**
   * Creates a new random generator with specified column number and specified number of random
   * values per row. Seed is base + colnum * Constant.
   */
=======
  /** 열 번호와 행당 난수 개수를 지정하여 생성기를 초기화합니다. 시드는 base + colnum * Constant입니다. */
>>>>>>> master
  public RowRandomInt(int globalColumnNumber, int seedsPerRow) {
    this(globalColumnNumber, DEFAULT_SEED, seedsPerRow);
  }

<<<<<<< HEAD
  /**
   * Creates a new random generator with specified column number and specified number of random
   * values per row. Seed is base + colnum * Constant.
   */
=======
  /** 열 번호, 시드, 행당 난수 개수를 지정하여 생성기를 초기화합니다. 시드는 base + colnum * Constant입니다. */
>>>>>>> master
  public RowRandomInt(int globalColumnNumber, long seed, int seedsPerRow) {
    this.seed = seed + globalColumnNumber * (MODULUS / 799);
    this.seedsPerRow = seedsPerRow;
  }

<<<<<<< HEAD
  /** Get a random value between lowValue (inclusive) and highValue (inclusive). */
  public int nextInt(int lowValue, int highValue) {
    nextRand();

    // This will result in overflow when high is max int and low is 0,
    // which is a bug since you will get a value outside of the
    // specified range. There is code that relies on this bug.
=======
  /** lowValue 이상 highValue 이하 범위에서 난수를 반환합니다. */
  public int nextInt(int lowValue, int highValue) {
    nextRand();

    // high가 최대 int이고 low가 0이면 오버플로우가 발생합니다.
    // 그 결과 지정된 범위를 벗어난 값이 나올 수 있는 버그가 있지만,
    // 일부 코드가 이 동작에 의존합니다.
>>>>>>> master
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

<<<<<<< HEAD
  /**
   * Advances the random number generator to the start of the sequence for the next row. Each row
   * uses a specified number of random values, so the random number generator can be quickly
   * advanced for partitioned data sets.
   */
=======
  /** 다음 행 시퀀스의 시작 위치로 난수 생성기를 이동합니다. 각 행은 지정된 개수의 난수를 사용하므로 파티셔닝된 데이터를 빠르게 처리할 수 있습니다. */
>>>>>>> master
  public void rowFinished() {
    advanceSeed(seedsPerRow - usage);
    usage = 0;
  }

<<<<<<< HEAD
  /**
   * Advance the specified number of rows. Advancing to a specific row is needed for partitioned
   * data sets.
   */
  public void advanceRows(long rowCount) {
    // finish the current row
=======
  /** 지정된 행 수만큼 난수 생성기를 전진시킵니다. 파티셔닝된 데이터 처리 시 특정 행으로 이동하기 위해 필요합니다. */
  public void advanceRows(long rowCount) {
    // 현재 행을 마무리합니다.
>>>>>>> master
    if (usage != 0) {
      rowFinished();
    }

<<<<<<< HEAD
    // advance the seed
=======
    // 시드를 앞으로 이동합니다.
>>>>>>> master
    advanceSeed(seedsPerRow * rowCount);
  }

  private void advanceSeed(long count) {
    long multiplier = MULTIPLIER;
    while (count > 0) {
      if (count % 2 != 0) {
        seed = (multiplier * seed) % MODULUS;
      }
<<<<<<< HEAD
      // integer division, truncates
=======
      // 정수 나눗셈은 소수점 이하를 버립니다.
>>>>>>> master
      count = count / 2;
      multiplier = (multiplier * multiplier) % MODULUS;
    }
  }
}
