/*
<<<<<<< HEAD
 * Copyright 2020 by OLTPBenchmark Project
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
=======
 * 저작권 2020 OLTPBenchmark 프로젝트
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
>>>>>>> master
 *
 */

package com.oltpbenchmark.util;

import java.util.Random;

public class RandomGenerator extends Random {
  static final long serialVersionUID = 0;

  /**
<<<<<<< HEAD
   * Constructor
   *
   * @param seed
=======
   * 생성자
   *
   * @param seed 시드 값
>>>>>>> master
   */
  public RandomGenerator(int seed) {
    super(seed);
  }

  /**
<<<<<<< HEAD
   * Returns a random int value between minimum and maximum (inclusive)
   *
   * @param minimum
   * @param maximum
   * @returns a int in the range [minimum, maximum]. Note that this is inclusive.
=======
   * 최소값과 최대값(포함) 사이에서 랜덤 int 값을 반환합니다.
   *
   * @param minimum
   * @param maximum
   * @return 범위 [minimum, maximum]의 난수
>>>>>>> master
   */
  public int number(int minimum, int maximum) {

    int range_size = maximum - minimum + 1;
    int value = this.nextInt(range_size);
    value += minimum;

    return value;
  }

  /**
<<<<<<< HEAD
   * Returns a random long value between minimum and maximum (inclusive)
=======
   * 최소값과 최대값(포함) 사이에서 랜덤 long 값을 반환합니다.
>>>>>>> master
   *
   * @param minimum
   * @param maximum
   * @return
   */
  public long number(long minimum, long maximum) {

    long range_size = (maximum - minimum) + 1;

<<<<<<< HEAD
    // error checking and 2^x checking removed for simplicity.
=======
    // 단순화를 위해 에러 검사와 2^x 검사를 생략했습니다.
>>>>>>> master
    long bits, val;
    do {
      bits = (this.nextLong() << 1) >>> 1;
      val = bits % range_size;
    } while (bits - val + range_size < 0L);
    val += minimum;

    return val;
  }

  /**
<<<<<<< HEAD
=======
   * 지정된 소수 자릿수 범위 내에서 난수를 생성합니다.
   *
>>>>>>> master
   * @param decimal_places
   * @param minimum
   * @param maximum
   * @return
   */
  public double fixedPoint(int decimal_places, double minimum, double maximum) {

    int multiplier = 1;
    for (int i = 0; i < decimal_places; ++i) {
      multiplier *= 10;
    }

    int int_min = (int) (minimum * multiplier + 0.5);
    int int_max = (int) (maximum * multiplier + 0.5);

    return (double) this.number(int_min, int_max) / (double) multiplier;
  }

<<<<<<< HEAD
  /**
   * @returns a random alphabetic string with length in range [minimum_length, maximum_length].
   */
=======
  /** 길이가 [minimum_length, maximum_length]인 알파벳 문자열을 반환합니다. */
>>>>>>> master
  public String astring(int minimum_length, int maximum_length) {
    return randomString(minimum_length, maximum_length, 'a', 26);
  }

<<<<<<< HEAD
  /**
   * @returns a random numeric string with length in range [minimum_length, maximum_length].
   */
=======
  /** 길이가 [minimum_length, maximum_length]인 숫자 문자열을 반환합니다. */
>>>>>>> master
  public String nstring(int minimum_length, int maximum_length) {
    return randomString(minimum_length, maximum_length, '0', 10);
  }

  /**
<<<<<<< HEAD
=======
   * 지정된 범위와 기본 문자에 따라 랜덤 문자열을 만듭니다.
   *
>>>>>>> master
   * @param minimum_length
   * @param maximum_length
   * @param base
   * @param numCharacters
   * @return
   */
  private String randomString(
      int minimum_length, int maximum_length, char base, int numCharacters) {
    int length = number(minimum_length, maximum_length);
    byte baseByte = (byte) base;
    byte[] bytes = new byte[length];
    for (int i = 0; i < length; ++i) {
      bytes[i] = (byte) (baseByte + number(0, numCharacters - 1));
    }
    return new String(bytes);
  }
}
