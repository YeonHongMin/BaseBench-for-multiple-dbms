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

/** long 기반 선형 합동 난수 생성기입니다. */
>>>>>>> master
public class RowRandomLong {
  private static final long MULTIPLIER = 6364136223846793005L;
  private static final long INCREMENT = 1;

  private final int seedsPerRow;

  private long seed;
  private int usage;

<<<<<<< HEAD
  /**
   * Creates a new random number generator with the specified seed and specified number of random
   * values per row.
   */
=======
  /** 지정된 시드와 행당 난수 개수로 새로운 생성기를 초기화합니다. */
>>>>>>> master
  public RowRandomLong(long seed, int seedsPerRow) {
    this.seed = seed;
    this.seedsPerRow = seedsPerRow;
  }

<<<<<<< HEAD
  /** Get a random value between lowValue (inclusive) and highValue (inclusive). */
=======
  /** lowValue 이상 highValue 이하 범위에서 난수 값을 반환합니다. */
>>>>>>> master
  protected long nextLong(long lowValue, long highValue) {
    nextRand();

    long valueInRange = Math.abs(seed) % (highValue - lowValue + 1);

    return lowValue + valueInRange;
  }

  protected long nextRand() {
    seed = (seed * MULTIPLIER) + INCREMENT;
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
  /** 다음 행 시퀀스를 시작하도록 난수 생성기를 이동합니다. 각 행은 지정된 개수의 난수를 사용하며, 파티셔닝된 데이터를 빠르게 처리할 수 있습니다. */
>>>>>>> master
  public void rowFinished() {
    advanceSeed32(seedsPerRow - usage);
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
  /** 지정된 행 수만큼 난수를 전진시킵니다. 파티셔닝된 데이터에서는 특정 행으로 이동할 때 필요합니다. */
  public void advanceRows(long rowCount) {
    // 현재 행을 마무리합니다.
>>>>>>> master
    if (usage != 0) {
      rowFinished();
    }

<<<<<<< HEAD
    // advance the seed
=======
    // 시드를 이동합니다.
>>>>>>> master
    advanceSeed32(seedsPerRow * rowCount);
  }

  private static final long MULTIPLIER_32 = 16807;
  private static final long MODULUS_32 = 2147483647;

  private void advanceSeed32(long count) {
    long multiplier = MULTIPLIER_32;
    while (count > 0) {
<<<<<<< HEAD
      // testing for oddness, this seems portable
      if (count % 2 != 0) {
        seed = (multiplier * seed) % MODULUS_32;
      }
      // integer division, truncates
=======
      // 홀수 여부를 확인하며, 이 방식은 이식성이 있습니다.
      if (count % 2 != 0) {
        seed = (multiplier * seed) % MODULUS_32;
      }
      // 정수 나눗셈은 소수점 이하를 버립니다.
>>>>>>> master
      count = count / 2;
      multiplier = (multiplier * multiplier) % MODULUS_32;
    }
  }
}
