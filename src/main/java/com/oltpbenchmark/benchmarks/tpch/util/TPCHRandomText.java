/*
 * Copyright 2020 Trino
 *
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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
<<<<<<< HEAD
=======
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
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 */
package com.oltpbenchmark.benchmarks.tpch.util;

import com.oltpbenchmark.util.RowRandomInt;

public class TPCHRandomText extends RowRandomInt {
  private static final double LOW_LENGTH_MULTIPLIER = 0.4;
  private static final double HIGH_LENGTH_MULTIPLIER = 1.6;

  private final TextPool textPool;
  private final int minLength;
  private final int maxLength;

  public TPCHRandomText(long seed, TextPool textPool, double averageTextLength) {
    this(seed, textPool, averageTextLength, 1);
  }

  public TPCHRandomText(long seed, TextPool textPool, double averageTextLength, int seedsPerRow) {
    super(seed, seedsPerRow * 2);
    this.textPool = textPool;
    this.minLength = (int) (averageTextLength * LOW_LENGTH_MULTIPLIER);
    this.maxLength = (int) (averageTextLength * HIGH_LENGTH_MULTIPLIER);
  }

  public String nextValue() {
    int offset = nextInt(0, textPool.size() - maxLength);
    int length = nextInt(minLength, maxLength);
    return textPool.getText(offset, offset + length);
  }
}
