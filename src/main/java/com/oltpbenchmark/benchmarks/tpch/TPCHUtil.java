/*
 * Copyright 2020 by OLTPBenchmark Project
 *
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
 *
 */

package com.oltpbenchmark.benchmarks.tpch;

import com.oltpbenchmark.util.RandomGenerator;

public class TPCHUtil {

  /**
   * 배열의 무작위 요소를 반환합니다.
   *
   * @param array
   * @param rand
   * @param <T>
   * @return 배열의 무작위 요소
   */
  public static <T> T choice(T[] array, RandomGenerator rand) {
    return array[rand.number(1, array.length) - 1];
  }

  /**
   * 국가가 주어지면 지역 키를 반환합니다.
   *
   * @param nation N_NAME
   * @return 지역 키
   */
  public static int getRegionKeyFromNation(String nation) {
    switch (nation) {
      case "ALGERIA":
      case "ETHIOPIA":
      case "KENYA":
      case "MOROCCO":
      case "MOZAMBIQUE":
        return 0;
      case "ARGENTINA":
      case "BRAZIL":
      case "CANADA":
      case "PERU":
      case "UNITED STATES":
        return 1;
      case "INDIA":
      case "INDONESIA":
      case "JAPAN":
      case "CHINA":
      case "VIETNAM":
        return 2;
      case "FRANCE":
      case "GERMANY":
      case "ROMANIA":
      case "RUSSIA":
      case "UNITED KINGDOM":
        return 3;
      case "EGYPT":
      case "IRAN":
      case "IRAQ":
      case "JORDAN":
      case "SAUDI ARABIA":
        return 4;
      default:
        throw new IllegalArgumentException(String.format("Invalid nation %s", nation));
    }
  }

  /**
   * 지역 키가 주어지면 지역을 반환합니다.
   *
   * @param regionKey 지역 키
   * @return 지역
   */
  public static String getRegionFromRegionKey(int regionKey) {
    switch (regionKey) {
      case 0:
        return "AFRICA";
      case 1:
        return "AMERICA";
      case 2:
        return "ASIA";
      case 3:
        return "EUROPE";
      case 4:
        return "MIDDLE EAST";
      default:
        throw new IllegalArgumentException(String.format("Invalid region key %s", regionKey));
    }
  }

  /**
   * 'Brand#MN' 형식의 무작위 브랜드 문자열을 생성합니다. 여기서 M과 N은 [1 .. 5] 범위 내에서
   * 무작위로 독립적으로 선택된 두 숫자를 나타내는 두 개의 단일 문자 문자열입니다.
   *
   * @param rand 사용할 난수 생성기
   * @return TPCH 사양을 준수하는 무작위 브랜드
   */
  public static String randomBrand(RandomGenerator rand) {
    int M = rand.number(1, 5);
    int N = rand.number(1, 5);

    return String.format("Brand#%d%d", M, N);
  }
}
