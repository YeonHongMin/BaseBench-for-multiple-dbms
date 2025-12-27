/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한 및 조건을 준수해 주세요.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.distributions;

import java.util.concurrent.atomic.AtomicInteger;

<<<<<<< HEAD
<<<<<<< HEAD
/** Generates a sequence of integers 0, 1, ... */
public final class CounterGenerator extends IntegerGenerator {
  final AtomicInteger counter;

  /** Create a counter that starts at countstart */
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
/** 0, 1, ... 순서로 정수를 증가시키는 시퀀스를 생성합니다. */
public final class CounterGenerator extends IntegerGenerator {
  final AtomicInteger counter;

  /** countstart부터 시작하는 카운터를 생성합니다. */
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public CounterGenerator(int countstart) {
    counter = new AtomicInteger(countstart);
    setLastInt(counter.get() - 1);
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /**
   * If the generator returns numeric (integer) values, return the next value as an int. Default is
   * to return -1, which is appropriate for generators that do not return numeric values.
   */
=======
  /** 숫자(generator)이 int 값을 반환하는 경우 다음값을 int로 돌려줍니다. 기본은 -1이며, 숫자를 반환하지 않는 생성기에 적절합니다. */
>>>>>>> master
=======
  /** 숫자(generator)이 int 값을 반환하는 경우 다음값을 int로 돌려줍니다. 기본은 -1이며, 숫자를 반환하지 않는 생성기에 적절합니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public int nextInt() {
    int ret = counter.getAndIncrement();
    setLastInt(ret);
    return ret;
  }

  @Override
  public int lastInt() {
    return counter.get() - 1;
  }

  @Override
  public double mean() {
    throw new UnsupportedOperationException("Can't compute mean of non-stationary distribution!");
  }

  public void reset() {
<<<<<<< HEAD
<<<<<<< HEAD
    // TODO Auto-generated method stub
=======
    // TODO: 필요한 경우, 카운터 재설정 기능을 구현합니다.
>>>>>>> master
=======
    // TODO: 필요한 경우, 카운터 재설정 기능을 구현합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    counter.set(0);
  }
}
