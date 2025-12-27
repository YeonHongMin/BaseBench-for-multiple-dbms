<<<<<<< HEAD
=======
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

>>>>>>> master
package com.oltpbenchmark.distributions;

import java.util.concurrent.ThreadLocalRandom;

public class UniformGenerator extends IntegerGenerator {
  int min;
  int max;

  /**
<<<<<<< HEAD
   * Create a uniformly distributed random number generator for items.
   *
   * @param items Number of items.
=======
   * 아이템 수만큼 균등 분포로 무작위 정수를 생성하는 생성기를 만듭니다.
   *
   * @param items 아이템 개수
>>>>>>> master
   */
  public UniformGenerator(int items) {
    this(0, items - 1);
  }

  /**
<<<<<<< HEAD
   * Create a uniformly distributed random number generator for items between min and max
   * (inclusive).
   *
   * @param min Smallest integer to generate in the sequence.
   * @param max Largest integer to generate in the sequence.
=======
   * 최소값과 최대값(포함) 사이에서 균등하게 무작위 정수를 반환합니다.
   *
   * @param min 생성할 수 있는 가장 작은 정수
   * @param max 생성할 수 있는 가장 큰 정수
>>>>>>> master
   */
  public UniformGenerator(int min, int max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public int nextInt() {
    return ThreadLocalRandom.current().nextInt(this.min, this.max + 1);
  }

  @Override
  public double mean() {
    return (this.max + this.min) / 2.0;
  }
}
