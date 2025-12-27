/*
 * Copyright 2020 Trino
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
 */
package com.oltpbenchmark.benchmarks.tpch.util;

import com.oltpbenchmark.util.RowRandomInt;
import com.oltpbenchmark.util.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TPCHRandomStringSequence extends RowRandomInt {
  private final int count;
  private final Distribution distribution;

  public TPCHRandomStringSequence(long seed, int count, Distribution distribution) {
    this(seed, count, distribution, 1);
  }

  public TPCHRandomStringSequence(
      long seed, int count, Distribution distribution, int seedsPerRow) {
    super(seed, distribution.size() * seedsPerRow);
    this.count = count;
    this.distribution = distribution;
  }

  public String nextValue() {
    List<String> values = new ArrayList<>(distribution.getValues());

    // 문자열의 처음 'count' 요소를 무작위화
    for (int currentPosition = 0; currentPosition < count; currentPosition++) {
      int swapPosition = nextInt(currentPosition, values.size() - 1);
      Collections.swap(values, currentPosition, swapPosition);
    }

    // 무작위 단어 결합
    String result = StringUtil.join(" ", values.subList(0, count));
    return result;
  }
}
