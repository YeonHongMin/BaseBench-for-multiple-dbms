/*
<<<<<<< HEAD
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
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.util;

import java.util.*;

/**
<<<<<<< HEAD
<<<<<<< HEAD
 * A class that generates random numbers that follow some distribution.
 *
 * <p>Copied from <a href="https://issues.apache.org/jira/browse/HADOOP-3315">hadoop-3315 tfile</a>.
 * Remove after tfile is committed and use the tfile version of this class instead.
 */
public class RandomDistribution {
  /** Interface for discrete (integer) random distributions. */
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 * 특정 분포를 따르는 난수를 생성하는 클래스입니다.
 *
 * <p><a href="https://issues.apache.org/jira/browse/HADOOP-3315">hadoop-3315 tfile</a>에서 복사하여 가져온
 * 코드이며, tfile이 커밋되면 이를 대체할 수 있습니다.
 */
public class RandomDistribution {
  /** 정수 기반 이산 분포를 위한 인터페이스입니다. */
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public abstract static class DiscreteRNG extends Random {
    private static final long serialVersionUID = 1L;
    protected final long min;
    protected final long max;
    protected final Random random;
    protected final double mean;
    protected final long range_size;
    private Histogram<Long> history;

    public DiscreteRNG(Random random, long min, long max) {
      if (min >= max) {
        throw new IllegalArgumentException("Invalid range [" + min + " >= " + max + "]");
      }
      this.random = random;
      this.min = min;
      this.max = max;
      this.range_size = (max - min) + 1;
      this.mean = this.range_size / 2.0;
    }

    protected abstract long nextLongImpl();

<<<<<<< HEAD
<<<<<<< HEAD
    /** Enable keeping track of the values that the RNG generates */
=======
    /** RNG에서 생성된 값을 기록하도록 설정합니다. */
>>>>>>> master
=======
    /** RNG에서 생성된 값을 기록하도록 설정합니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    public void enableHistory() {

      this.history = new Histogram<>();
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Return the histogram of the values that have been generated
=======
     * 지금까지 생성된 값의 히스토그램을 반환합니다.
>>>>>>> master
=======
     * 지금까지 생성된 값의 히스토그램을 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
     *
     * @return
     */
    public Histogram<Long> getHistory() {

      return (this.history);
    }

    public long getRange() {
      return this.range_size;
    }

    public long getMin() {
      return this.min;
    }

    public long getMax() {
      return this.max;
    }

    public Random getRandom() {
      return (this.random);
    }

    public double calculateMean(int num_samples) {
      long total = 0l;
      for (int i = 0; i < num_samples; i++) {
        total += this.nextLong();
<<<<<<< HEAD
<<<<<<< HEAD
      } // FOR
=======
      } // 반복 종료
>>>>>>> master
=======
      } // 반복 종료
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      return (total / (double) num_samples);
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Get the next random number as an int
     *
     * @return the next random number.
=======
     * 다음 난수를 int로 반환합니다.
     *
     * @return 다음 난수
>>>>>>> master
=======
     * 다음 난수를 int로 반환합니다.
     *
     * @return 다음 난수
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
     */
    @Override
    public final int nextInt() {
      long val = (int) this.nextLongImpl();
      if (this.history != null) {
        this.history.put(val);
      }
      return ((int) val);
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Get the next random number as a long
     *
     * @return the next random number.
=======
     * 다음 난수를 long으로 반환합니다.
     *
     * @return 다음 난수
>>>>>>> master
=======
     * 다음 난수를 long으로 반환합니다.
     *
     * @return 다음 난수
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
     */
    @Override
    public final long nextLong() {
      long val = this.nextLongImpl();
      if (this.history != null) {
        this.history.put(val);
      }
      return (val);
    }

    @Override
    public String toString() {
      return String.format(
          "%s[min=%d, max=%d]", this.getClass().getSimpleName(), this.min, this.max);
    }

    public static long nextLong(Random rng, long n) {
<<<<<<< HEAD
<<<<<<< HEAD
      // error checking and 2^x checking removed for simplicity.
=======
      // 단순화를 위해 에러 검사와 2^x 검사를 생략했습니다.
>>>>>>> master
=======
      // 단순화를 위해 에러 검사와 2^x 검사를 생략했습니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      long bits, val;
      do {
        bits = (rng.nextLong() << 1) >>> 1;
        val = bits % n;
      } while (bits - val + (n - 1) < 0L);
      return val;
    }
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** P(i)=1/(max-min) */
=======
  /** P(i)=1/(max-min) (균등 분포) */
>>>>>>> master
=======
  /** P(i)=1/(max-min) (균등 분포) */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public static class Flat extends DiscreteRNG {
    private static final long serialVersionUID = 1L;

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Generate random integers from min (inclusive) to max (exclusive) following even distribution.
     *
     * @param random The basic random number generator.
     * @param min Minimum integer
     * @param max maximum integer (exclusive).
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
     * 최소값(min) 이상, 최대값(max) 미만の 범위에서 균등 분포로 난수를 생성합니다.
     *
     * @param random 기본 난수 생성기
     * @param min 최소 정수
     * @param max 최대 정수 (배타적)
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
     */
    public Flat(Random random, long min, long max) {
      super(random, min, max);
    }

    /**
     * @see DiscreteRNG#nextInt()
     */
    @Override
    protected long nextLongImpl() {
<<<<<<< HEAD
<<<<<<< HEAD
      // error checking and 2^x checking removed for simplicity.
=======
      // 단순화를 위해 에러 검사와 2^x 검사를 생략했습니다.
>>>>>>> master
=======
      // 단순화를 위해 에러 검사와 2^x 검사를 생략했습니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      long bits, val;
      do {
        bits = (random.nextLong() << 1) >>> 1;
        val = bits % (this.range_size - 1);
      } while (bits - val + (this.range_size - 1) < 0L);
      val += this.min;

      return val;
    }
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** P(i)=1/(max-min) */
=======
  /** P(i)=1/(max-min) (히스토그램 기반 균등 분포) */
>>>>>>> master
=======
  /** P(i)=1/(max-min) (히스토그램 기반 균등 분포) */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public static class FlatHistogram<T extends Comparable<T>> extends DiscreteRNG {
    private static final long serialVersionUID = 1L;
    private final Flat inner;
    private final TreeMap<Long, T> value_rle = new TreeMap<>();
    private Histogram<T> history;

<<<<<<< HEAD
<<<<<<< HEAD
    /** Generate a run-length of the values of the histogram */
=======
    /** 히스토그램 값의 런 길이를 생성합니다. */
>>>>>>> master
=======
    /** 히스토그램 값의 런 길이를 생성합니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    public FlatHistogram(Random random, Histogram<T> histogram) {
      super(random, 0, histogram.getSampleCount());
      this.inner = new Flat(random, 0, histogram.getSampleCount());

      long total = 0;
      for (T k : histogram.values()) {
        long v = histogram.get(k);
        total += v;
        this.value_rle.put(total, k);
      }
    }

    @Override
    public void enableHistory() {
      this.history = new Histogram<>();
    }

    public Histogram<T> getHistogramHistory() {
      if (this.history != null) {
        return (this.history);
      }
      return (null);
    }

    public T nextValue() {
      int idx = this.inner.nextInt();
      Long total = this.value_rle.tailMap((long) idx).firstKey();
      T val = this.value_rle.get(total);
      if (this.history != null) {
        this.history.put(val);
      }
      return (val);
      //            assert(false) : "Went beyond our expected total '" + idx + "'";
      //            return (null);
    }

    /**
     * @see DiscreteRNG#nextLong()
     */
    @Override
    protected long nextLongImpl() {
      Object val = this.nextValue();
      if (val instanceof Integer) {
        return ((Integer) val);
      }
      return ((Long) val);
    }
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** Simple generic class overload to avoid some cast warnings below. */
  public static class IntegerFlatHistogram extends FlatHistogram<Integer> {
    // Required serialization field.
=======
  /** 캐스트 경고를 피하기 위한 간단한 제네릭 확장입니다. */
  public static class IntegerFlatHistogram extends FlatHistogram<Integer> {
    // 직렬화를 위한 필수 필드입니다.
>>>>>>> master
=======
  /** 캐스트 경고를 피하기 위한 간단한 제네릭 확장입니다. */
  public static class IntegerFlatHistogram extends FlatHistogram<Integer> {
    // 직렬화를 위한 필수 필드입니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    private static final long serialVersionUID = 1L;

    public IntegerFlatHistogram(Random random, Histogram<Integer> histogram) {
      super(random, histogram);
    }
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** Gaussian Distribution */
=======
  /** 가우시안 분포 */
>>>>>>> master
=======
  /** 가우시안 분포 */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public static class Gaussian extends DiscreteRNG {
    private static final long serialVersionUID = 1L;

    public Gaussian(Random random, long min, long max) {
      super(random, min, max);
    }

    @Override
    protected long nextLongImpl() {
      int value = -1;
      while (value < 0 || value >= this.range_size) {
        double gaussian = (this.random.nextGaussian() + 2.0) / 4.0;
        value = (int) Math.round(gaussian * this.range_size);
      }
      return (value + this.min);
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Zipf distribution. The ratio of the probabilities of integer i and j is defined as follows:
=======
   * Zipf 분포입니다. 정수 i와 j의 확률 비율은 다음과 같이 정의됩니다:
>>>>>>> master
=======
   * Zipf 분포입니다. 정수 i와 j의 확률 비율은 다음과 같이 정의됩니다:
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * <p>P(i)/P(j)=((j-min+1)/(i-min+1))^sigma.
   */
  public static class Zipf extends DiscreteRNG {
    private static final long serialVersionUID = 1L;
    private static final double DEFAULT_EPSILON = 0.001;
    private final ArrayList<Long> k;
    private final ArrayList<Double> v;

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Constructor
     *
     * @param r The random number generator.
     * @param min minimum integer (inclusvie)
     * @param max maximum integer (exclusive)
     * @param sigma parameter sigma. (sigma > 1.0)
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
     * 생성자
     *
     * @param r 난수 생성기
     * @param min 최소 정수 (포함)
     * @param max 최대 정수 (배타적)
     * @param sigma sigma 파라미터 (sigma > 1.0)
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
     */
    public Zipf(Random r, long min, long max, double sigma) {
      this(r, min, max, sigma, DEFAULT_EPSILON);
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Constructor.
     *
     * @param r The random number generator.
     * @param min minimum integer (inclusvie)
     * @param max maximum integer (exclusive)
     * @param sigma parameter sigma. (sigma > 1.0)
     * @param epsilon Allowable error percentage (0 < epsilon < 1.0).
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
     * 생성자
     *
     * @param r 난수 생성기
     * @param min 최소 정수 (포함)
     * @param max 최대 정수 (배타적)
     * @param sigma sigma 파라미터 (sigma > 1.0)
     * @param epsilon 허용 오차 비율 (0 < epsilon < 1.0)
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
     */
    public Zipf(Random r, long min, long max, double sigma, double epsilon) {
      super(r, min, max);
      if ((max <= min) || (sigma <= 1) || (epsilon <= 0) || (epsilon >= 0.5)) {
        throw new IllegalArgumentException(
            "Invalid arguments [min="
                + min
                + ", max="
                + max
                + ", sigma="
                + sigma
                + ", epsilon="
                + epsilon
                + "]");
      }
      k = new ArrayList<>();
      v = new ArrayList<>();

      double sum = 0;
      long last = -1;
      for (long i = min; i < max; ++i) {
        sum += Math.exp(-sigma * Math.log(i - min + 1));
        if ((last == -1) || i * (1 - epsilon) > last) {
          k.add(i);
          v.add(sum);
          last = i;
        }
      }

      if (last != max - 1) {
        k.add(max - 1);
        v.add(sum);
      }

      v.set(v.size() - 1, 1.0);

      for (int i = v.size() - 2; i >= 0; --i) {
        v.set(i, v.get(i) / sum);
      }
    }

    /**
     * @see DiscreteRNG#nextInt()
     */
    @Override
    protected long nextLongImpl() {
      double d = random.nextDouble();
      int idx = Collections.binarySearch(v, d);

      if (idx > 0) {
        ++idx;
      } else {
        idx = -(idx + 1);
      }

      if (idx >= v.size()) {
        idx = v.size() - 1;
      }

      if (idx == 0) {
        return k.get(0);
      }

      long ceiling = k.get(idx);
      long lower = k.get(idx - 1);

      return ceiling - DiscreteRNG.nextLong(random, ceiling - lower);
    }
  }
}
