/*
 * Copyright 2020 Trino
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
 */
package com.oltpbenchmark.benchmarks.tpch.util;

import static java.util.Objects.requireNonNull;

import com.oltpbenchmark.util.RowRandomInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Distribution {
  @SuppressWarnings("unused") // never read
  private final String name;

  private final List<String> values;
  private final int[] weights;
  private final String[] distribution;
  private final int maxWeight;

  public Distribution(String name, Map<String, Integer> distribution) {
    this.name = requireNonNull(name, "name is null");
    requireNonNull(distribution, "distribution is null");

    List<String> values = new ArrayList<>();
    this.weights = new int[distribution.size()];

    int runningWeight = 0;
    int index = 0;
    boolean isValidDistribution = true;
    for (Entry<String, Integer> entry : distribution.entrySet()) {
      values.add(entry.getKey());

      runningWeight += entry.getValue();
      weights[index] = runningWeight;

      isValidDistribution &= entry.getValue() > 0;

      index++;
    }
    this.values = values;

<<<<<<< HEAD
    // "nations" is hack and not a valid distribution so we need to skip it
=======
    // "nations"는 해킹이며 유효한 분포가 아니므로 건너뛰어야 합니다.
>>>>>>> master
    if (isValidDistribution) {
      this.maxWeight = weights[weights.length - 1];
      this.distribution = new String[maxWeight];

      index = 0;
      for (String value : this.values) {
        int count = distribution.get(value);
        for (int i = 0; i < count; i++) {
          this.distribution[index] = value;
          index++;
        }
      }
    } else {
      this.maxWeight = -1;
      this.distribution = null;
    }
  }

  public String getValue(int index) {
    return values.get(index);
  }

  public List<String> getValues() {
    return values;
  }

  public int getWeight(int index) {
    return weights[index];
  }

  public int size() {
    return values.size();
  }

  public String randomValue(RowRandomInt randomInt) {
    int randomValue = randomInt.nextInt(0, maxWeight - 1);
    return distribution[randomValue];
  }
}
