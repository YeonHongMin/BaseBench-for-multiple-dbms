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

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegionGenerator implements Iterable<List<Object>> {
  private static final int COMMENT_AVERAGE_LENGTH = 72;

  private final Distributions distributions;
  private final TextPool textPool;

  public RegionGenerator() {
    this(Distributions.getDefaultDistributions(), TextPool.getDefaultTestPool());
  }

  public RegionGenerator(Distributions distributions, TextPool textPool) {
    this.distributions = requireNonNull(distributions, "distributions is null");
    this.textPool = requireNonNull(textPool, "textPool is null");
  }

  @Override
  public Iterator<List<Object>> iterator() {
    return new RegionGeneratorIterator(distributions.getRegions(), textPool);
  }

  private static class RegionGeneratorIterator implements Iterator<List<Object>> {
    private final Distribution regions;
    private final TPCHRandomText commentRandom;

    private int index;

    private RegionGeneratorIterator(Distribution regions, TextPool textPool) {
      this.regions = regions;
      this.commentRandom = new TPCHRandomText(1500869201L, textPool, COMMENT_AVERAGE_LENGTH);
    }

    @Override
    public boolean hasNext() {
      return index < regions.size();
    }

    @Override
    public List<Object> next() {
      List<Object> region = new ArrayList<>();
      region.add((long) index);
      region.add(regions.getValue(index));
      region.add(commentRandom.nextValue());

      commentRandom.rowFinished();
      index++;

      return region;
    }
  }
}
