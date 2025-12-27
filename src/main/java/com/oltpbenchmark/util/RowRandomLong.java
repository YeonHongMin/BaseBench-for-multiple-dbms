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

/** long 기반 선형 합동 난수 생성기입니다. */
public class RowRandomLong {
  private static final long MULTIPLIER = 6364136223846793005L;
  private static final long INCREMENT = 1;

  private final int seedsPerRow;

  private long seed;
  private int usage;

  /**
   * 지정된 시드와 행당 난수 개수로 새로운 생성기를 초기화합니다.
   */
  public RowRandomLong(long seed, int seedsPerRow) {
    this.seed = seed;
    this.seedsPerRow = seedsPerRow;
  }

  /** lowValue 이상 highValue 이하 범위에서 난수 값을 반환합니다. */
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

  /**
   * 다음 행 시퀀스를 시작하도록 난수 생성기를 이동합니다.
   * 각 행은 지정된 개수의 난수를 사용하며, 파티셔닝된 데이터를 빠르게 처리할 수 있습니다.
   */
  public void rowFinished() {
    advanceSeed32(seedsPerRow - usage);
    usage = 0;
  }

  /**
   * 지정된 행 수만큼 난수를 전진시킵니다. 파티셔닝된 데이터에서는 특정 행으로 이동할 때 필요합니다.
   */
  public void advanceRows(long rowCount) {
    // 현재 행을 마무리합니다.
    if (usage != 0) {
      rowFinished();
    }

    // 시드를 이동합니다.
    advanceSeed32(seedsPerRow * rowCount);
  }

  private static final long MULTIPLIER_32 = 16807;
  private static final long MODULUS_32 = 2147483647;

  private void advanceSeed32(long count) {
    long multiplier = MULTIPLIER_32;
    while (count > 0) {
      // 홀수 여부를 확인하며, 이 방식은 이식성이 있습니다.
      if (count % 2 != 0) {
        seed = (multiplier * seed) % MODULUS_32;
      }
      // 정수 나눗셈은 소수점 이하를 버립니다.
      count = count / 2;
      multiplier = (multiplier * multiplier) % MODULUS_32;
    }
  }
}
