/*
 * Copyright 2020 by OLTPBenchmark Project
 *
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
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한과 조건을 준수해 주세요.
>>>>>>> master
 *
 */

package com.oltpbenchmark.distributions;

import java.util.Random;

<<<<<<< HEAD
/** Utility functions. */
=======
/** 분포 생성 시 사용하는 유틸 함수 모음입니다. */
>>>>>>> master
public class Utils {
  private static final Random rand = new Random();
  private static final ThreadLocal<Random> rng = new ThreadLocal<>();

  public static Random random() {
    Random ret = rng.get();
    if (ret == null) {
      ret = new Random(rand.nextLong());
      rng.set(ret);
    }
    return ret;
  }

  public static final long FNV_offset_basis_64 = 0xCBF29CE484222325L;
  public static final long FNV_prime_64 = 1099511628211L;

  /**
<<<<<<< HEAD
   * 64 bit FNV hash. Produces more "random" hashes than (say) String.hashCode().
   *
   * @param val The value to hash.
   * @return The hash value
   */
  public static long FNVhash64(long val) {
    // from http://en.wikipedia.org/wiki/Fowler_Noll_Vo_hash
=======
   * 64비트 FNV 해시를 계산합니다. String.hashCode()보다 더 "무작위"성 높은 값을 만들어낸다고 알려졌습니다.
   *
   * @param val 해시할 값
   * @return 계산된 해시값
   */
  public static long FNVhash64(long val) {
    // http://en.wikipedia.org/wiki/Fowler_Noll_Vo_hash 참조
>>>>>>> master
    long hashval = FNV_offset_basis_64;

    for (int i = 0; i < 8; i++) {
      long octet = val & 0x00ff;
      val = val >> 8;

      hashval = hashval ^ octet;
      hashval = hashval * FNV_prime_64;
<<<<<<< HEAD
      // hashval = hashval ^ octet;
=======
      // 이전에는 hashval = hashval ^ octet; 방식으로 계산했습니다.
>>>>>>> master
    }
    return Math.abs(hashval);
  }
}
