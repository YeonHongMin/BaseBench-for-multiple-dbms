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
/** Thread-safe cyclic counter generator. */
=======
/** 스레드 안전한 순환 카운터 생성기입니다. */
>>>>>>> master
=======
/** 스레드 안전한 순환 카운터 생성기입니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
public class CyclicCounterGenerator extends IntegerGenerator {

  private final int maxVal;
  private final AtomicInteger counter;

  public CyclicCounterGenerator(int maxVal) {
    this.maxVal = maxVal;
    this.counter = new AtomicInteger(0);
  }

  protected void setLastInt(int last) {
    throw new UnsupportedOperationException("Cyclic counter cannot be set to a value");
  }

  @Override
  public int nextInt() {
    return counter.accumulateAndGet(1, (index, inc) -> (++index >= maxVal ? 0 : index));
  }

  @Override
  public String nextString() {
    return "" + nextInt();
  }

  @Override
  public String lastString() {
    return "" + lastInt();
  }

  @Override
  public int lastInt() {
    return counter.get();
  }

  @Override
  public double mean() {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
