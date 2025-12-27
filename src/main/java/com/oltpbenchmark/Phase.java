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
 * Apache License, Version 2.0 (이하 "라이선스")에 따라 라이선스됩니다.
 * 라이선스를 준수하지 않는 한 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 얻을 수 있습니다:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 적용 가능한 법률에 의해 요구되거나 서면으로 합의하지 않는 한,
 * 라이선스에 따라 배포된 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 종류의 보증이나 조건도 없습니다.
 * 권한 및 제한에 대한 자세한 내용은 라이선스를 참조하세요.
>>>>>>> master
 *
 */

package com.oltpbenchmark;

import com.oltpbenchmark.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Phase {
  public enum Arrival {
    REGULAR,
    POISSON,
  }

  private final Random gen = new Random();
  private final String benchmarkName;
  private final int id;
  private final int time;
  private final int warmupTime;
  private final double rate;
  private final Arrival arrival;

  private final boolean rateLimited;
  private final boolean disabled;
  private final boolean serial;
  private final boolean timed;
  private final List<Double> weights;
  private final int weightCount;
  private final int activeTerminals;
  private int nextSerial;

  Phase(
      String benchmarkName,
      int id,
      int t,
      int wt,
      double r,
      List<Double> weights,
      boolean rateLimited,
      boolean disabled,
      boolean serial,
      boolean timed,
      int activeTerminals,
      Arrival a) {
    this.benchmarkName = benchmarkName;
    this.id = id;
    this.time = t;
    this.warmupTime = wt;
    this.rate = r;
    this.weights = weights;
    this.weightCount = this.weights.size();
    this.rateLimited = rateLimited;
    this.disabled = disabled;
    this.serial = serial;
    this.timed = timed;
    this.nextSerial = 1;
    this.activeTerminals = activeTerminals;
    this.arrival = a;
  }

  public boolean isRateLimited() {
    return rateLimited;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public boolean isSerial() {
    return serial;
  }

  public boolean isTimed() {
    return timed;
  }

  public boolean isLatencyRun() {
    return !timed && serial;
  }

  public boolean isThroughputRun() {
    return !isLatencyRun();
  }

  public void resetSerial() {
    this.nextSerial = 1;
  }

  public int getActiveTerminals() {
    return activeTerminals;
  }

  public int getWeightCount() {
    return (this.weightCount);
  }

  public int getId() {
    return id;
  }

  public int getTime() {
    return time;
  }

  public int getWarmupTime() {
    return warmupTime;
  }

  public double getRate() {
    return rate;
  }

  public Arrival getArrival() {
    return arrival;
  }

  public List<Double> getWeights() {
    return (this.weights);
  }

  /**
<<<<<<< HEAD
   * Computes the sum of weights. Usually needs to add up to 100%
   *
   * @return The total weight
=======
   * 가중치의 합을 계산합니다. 일반적으로 100%가 되어야 합니다.
   *
   * @return 총 가중치
>>>>>>> master
   */
  public double totalWeight() {
    double total = 0.0;
    for (Double d : weights) {
      total += d;
    }
    return total;
  }

  /**
<<<<<<< HEAD
   * This simply computes the next transaction by randomly selecting one based on the weights of
   * this phase.
   *
   * @return
=======
   * 이 단계의 가중치를 기반으로 무작위로 선택하여 다음 트랜잭션을 계산합니다.
   *
   * @return 트랜잭션 ID
>>>>>>> master
   */
  public int chooseTransaction() {
    return chooseTransaction(false);
  }

  public int chooseTransaction(boolean isColdQuery) {
    if (isDisabled()) {
      return -1;
    }

    if (isSerial()) {
      int ret;
      synchronized (this) {
        ret = this.nextSerial;

<<<<<<< HEAD
        // Serial runs should not execute queries with non-positive
        // weights.
=======
        // 순차 실행은 양수가 아닌 가중치를 가진 쿼리를 실행하지 않아야 합니다.
>>>>>>> master
        while (ret <= this.weightCount && weights.get(ret - 1) <= 0.0) {
          ret = ++this.nextSerial;
        }

<<<<<<< HEAD
        // If it's a cold execution, then we don't want to advance yet,
        // since the hot run needs to execute the same query.
        if (!isColdQuery) {

          // throughput) run, so we loop through the list multiple
          // times. Note that we do the modulus before the increment
          // so that we end up in the range [1,num_weights]
=======
        // 콜드 실행인 경우, 아직 진행하지 않습니다.
        // 핫 실행이 동일한 쿼리를 실행해야 하기 때문입니다.
        if (!isColdQuery) {

          // 처리량) 실행이므로 목록을 여러 번 반복합니다.
          // 증가 전에 모듈로를 수행하여 [1,num_weights] 범위에 있도록 합니다.
>>>>>>> master
          if (isTimed()) {

            this.nextSerial %= this.weightCount;
          }

          ++this.nextSerial;
        }
      }
      return ret;
    } else {
      int randomPercentage = gen.nextInt((int) totalWeight()) + 1;
      double weight = 0.0;
      for (int i = 0; i < this.weightCount; i++) {
        weight += weights.get(i);
        if (randomPercentage <= weight) {
          return i + 1;
        }
      }
    }

    return -1;
  }

<<<<<<< HEAD
  /** Returns a string for logging purposes when entering the phase */
=======
  /** 단계 진입 시 로깅 목적으로 문자열을 반환합니다 */
>>>>>>> master
  public String currentPhaseString() {
    List<String> inner = new ArrayList<>();
    inner.add("[Workload=" + benchmarkName.toUpperCase() + "]");
    if (isDisabled()) {
      inner.add("[Disabled=true]");
    } else {
      if (isLatencyRun()) {
        inner.add("[Serial=true]");
        inner.add("[Time=n/a]");
      } else {
        inner.add("[Serial=" + isSerial() + "]");
        inner.add("[Time=" + time + "]");
      }
      inner.add("[WarmupTime=" + warmupTime + "]");
      inner.add("[Rate=" + (isRateLimited() ? rate : "unlimited") + "]");
      inner.add("[Arrival=" + arrival + "]");
      inner.add("[Ratios=" + getWeights() + "]");
      inner.add("[ActiveWorkers=" + getActiveTerminals() + "]");
    }

    return StringUtil.bold("PHASE START") + " :: " + StringUtil.join(" ", inner);
  }
}
