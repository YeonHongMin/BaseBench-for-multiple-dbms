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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
<<<<<<< HEAD
 * A generator of a zipfian distribution. It produces a sequence of items, such that some items are
 * more popular than others, according to a zipfian distribution. When you construct an instance of
 * this class, you specify the number of items in the set to draw from, either by specifying an
 * itemcount (so that the sequence is of items from 0 to itemcount-1) or by specifying a min and a
 * max (so that the sequence is of items from min to max inclusive). After you construct the
 * instance, you can change the number of items by calling nextInt(itemcount) or
 * nextLong(itemcount).
 *
 * <p>Note that the popular items will be clustered together, e.g. item 0 is the most popular, item
 * 1 the second most popular, and so on (or min is the most popular, min+1 the next most popular,
 * etc.) If you don't want this clustering, and instead want the popular items scattered throughout
 * the item space, then use ScrambledZipfianGenerator instead.
 *
 * <p>Be aware: initializing this generator may take a long time if there are lots of items to
 * choose from (e.g. over a minute for 100 million objects). This is because certain mathematical
 * values need to be computed to properly generate a zipfian skew, and one of those values (zeta) is
 * a sum sequence from 1 to n, where n is the itemcount. Note that if you increase the number of
 * items in the set, we can compute a new zeta incrementally, so it should be fast unless you have
 * added millions of items. However, if you decrease the number of items, we recompute zeta from
 * scratch, so this can take a long time.
 *
 * <p>The algorithm used here is from "Quickly Generating Billion-Record Synthetic Databases", Jim
 * Gray et al, SIGMOD 1994.
=======
 * zipfian 분포 생성기입니다. 특정 아이템이 다른 아이템에 비해 더 많이 선택되며, itemcount를 통해 0부터 itemcount-1까지 혹은 min~max 사이에서
 * 아이템을 순차적으로 생성합니다. 생성 후 nextInt(itemcount) 또는 nextLong(itemcount)를 호출해 아이템 수를 변경할 수도 있습니다.
 *
 * <p>인기 아이템들은 모여서 나타나기 때문에(예: 0이 가장 인기 있고, 1이 두 번째 등) 헤드가 몰려 있는 것이 싫다면 {@link
 * ScrambledZipfianGenerator}를 사용하세요.
 *
 * <p>참고: 아이템 수가 많은 경우 생성 초기화에 시간이 오래 걸릴 수 있습니다(예: 1억 개면 1분 이상). zipfian 치우침을 만들기 위해 수학적 상수들이 필요하고,
 * 일정 항목(zeta)은 1부터 n까지의 합이어서 시간이 걸립니다. 아이템 수를 증가시키면 zeta를 점증적으로 계산하므로 빠르지만, 감소시키면 다시 처음부터 계산하므로 시간이
 * 오래 걸릴 수 있습니다.
 *
 * <p>이 알고리즘은 Jim Gray 외, SIGMOD 1994 논문 "Quickly Generating Billion-Record Synthetic Databases"에
 * 기반합니다.
>>>>>>> master
 */
public final class ZipfianGenerator extends IntegerGenerator {
  public static final double ZIPFIAN_CONSTANT = 0.99;

  private static final Logger LOG = LoggerFactory.getLogger(ZipfianGenerator.class);

  final Random rng;

<<<<<<< HEAD
  /** Number of items. */
  long items;

  /** Min item to generate. */
  long base;

  /** The zipfian constant to use. */
  double zipfianconstant;

  /** Computed parameters for generating the distribution. */
  double alpha, zetan, eta, theta, zeta2theta;

  /** The number of items used to compute zetan the last time. */
  long countforzeta;

  /**
   * Flag to prevent problems. If you increase the number of items the zipfian generator is allowed
   * to choose from, this code will incrementally compute a new zeta value for the larger itemcount.
   * However, if you decrease the number of items, the code computes zeta from scratch; this is
   * expensive for large itemsets. Usually this is not intentional; e.g. one thread thinks the
   * number of items is 1001 and calls "nextLong()" with that item count; then another thread who
   * thinks the number of items is 1000 calls nextLong() with itemcount=1000 triggering the
   * expensive recomputation. (It is expensive for 100 million items, not really for 1000 items.)
   * Why did the second thread think there were only 1000 items? maybe it read the item count before
   * the first thread incremented it. So this flag allows you to say if you really do want that
   * recomputation. If true, then the code will recompute zeta if the itemcount goes down. If false,
   * the code will assume itemcount only goes up, and never recompute.
=======
  /** 생성할 아이템 수입니다. */
  long items;

  /** 생성 가능한 가장 작은 아이템 값입니다. */
  long base;

  /** 사용할 zipfian 상수입니다. */
  double zipfianconstant;

  /** 분포 생성을 위해 계산한 파라미터들입니다. */
  double alpha, zetan, eta, theta, zeta2theta;

  /** 마지막으로 zetan을 계산할 때 사용한 아이템 수입니다. */
  long countforzeta;

  /**
   * 값을 감소시키는 상황에서 zeta를 다시 계산할지 여부를 제어하는 플래그입니다. 아이템 수를 늘리면 점증적으로 zeta를 계산하지만, 줄일 경우 처음부터 다시 계산하므로
   * 비용이 큽니다. 일반적으로 이 상황은 의도치 않게 발생하며, 예를 들어 하나의 스레드는 1001개의 아이템을 예상하며 nextLong()을 호출하고, 다른 스레드는
   * 1000개라고 생각하면서 nextLong(1000)을 호출해 느린 재계산을 유발합니다. (1억 개에서는 매우 느리지만 1000개 정도면 크게 느리지 않습니다.) 두 번째
   * 스레드가 왜 1000만 보았을까요? 첫 번째 스레드가 아이템 수를 올리기 전에 읽었을 수도 있습니다. 이 플래그로 정말로 재계산을 원한다면 true로 설정하고, 그렇지
   * 않다면 false로 설정하여 itemcount가 감소할 때도 재계산하지 않도록 할 수 있습니다.
>>>>>>> master
   */
  boolean allowitemcountdecrease = false;

  /**
<<<<<<< HEAD
   * Create a zipfian generator for the specified number of items.
   *
   * @param rng
   * @param _items
=======
   * 지정한 아이템 개수를 사용하는 zipfian 생성기를 만듭니다.
   *
   * @param rng 랜덤 생성기
   * @param _items 아이템 개수
>>>>>>> master
   */
  public ZipfianGenerator(Random rng, long _items) {
    this(rng, 0, _items - 1, ZIPFIAN_CONSTANT);
  }

  /**
<<<<<<< HEAD
   * Create a zipfian generator for items between min and max.
   *
   * @param _min The smallest integer to generate in the sequence.
   * @param _max The largest integer to generate in the sequence.
=======
   * min과 max 사이 범위에서 zipfian 생성기를 만듭니다.
   *
   * @param _min 생성할 가장 작은 정수
   * @param _max 생성할 가장 큰 정수
>>>>>>> master
   */
  public ZipfianGenerator(Random rng, long _min, long _max) {
    this(rng, _min, _max, ZIPFIAN_CONSTANT);
  }

  /**
<<<<<<< HEAD
   * Create a zipfian generator for the specified number of items using the specified zipfian
   * constant.
   *
   * @param _items The number of items in the distribution.
   * @param _zipfianconstant The zipfian constant to use.
=======
   * 지정한 zipfian 상수를 사용하는 생성기를 만듭니다.
   *
   * @param _items 전체 아이템 수
   * @param _zipfianconstant 사용할 zipfian 상수
>>>>>>> master
   */
  public ZipfianGenerator(Random rng, long _items, double _zipfianconstant) {
    this(rng, 0, _items - 1, _zipfianconstant);
  }

  /**
<<<<<<< HEAD
   * Create a zipfian generator for items between min and max (inclusive) for the specified zipfian
   * constant.
   *
   * @param min The smallest integer to generate in the sequence.
   * @param max The largest integer to generate in the sequence.
   * @param _zipfianconstant The zipfian constant to use.
=======
   * min~max 범위에서 지정한 zipfian 상수로 생성기를 만듭니다.
   *
   * @param min 생성할 가장 작은 정수
   * @param max 생성할 가장 큰 정수
   * @param _zipfianconstant 사용할 zipfian 상수
>>>>>>> master
   */
  public ZipfianGenerator(Random rng, long min, long max, double _zipfianconstant) {
    this(rng, min, max, _zipfianconstant, zetastatic(max - min + 1, _zipfianconstant));
  }

  /**
<<<<<<< HEAD
   * Create a zipfian generator for items between min and max (inclusive) for the specified zipfian
   * constant, using the precomputed value of zeta.
   *
   * @param min The smallest integer to generate in the sequence.
   * @param max The largest integer to generate in the sequence.
   * @param _zipfianconstant The zipfian constant to use.
   * @param _zetan The precomputed zeta constant.
=======
   * 미리 계산한 zeta 값을 이용해 min~max 범위에서 zipfian 생성기를 만듭니다.
   *
   * @param min 생성할 수 있는 가장 작은 정수
   * @param max 생성할 수 있는 가장 큰 정수
   * @param _zipfianconstant 사용할 zipfian 상수
   * @param _zetan 미리 계산된 zeta 값
>>>>>>> master
   */
  public ZipfianGenerator(Random rng, long min, long max, double _zipfianconstant, double _zetan) {
    this.rng = rng;
    items = max - min + 1;
    base = min;
    zipfianconstant = _zipfianconstant;

    theta = zipfianconstant;

    zeta2theta = zeta(2, theta);

    alpha = 1.0 / (1.0 - theta);
<<<<<<< HEAD
    // zetan=zeta(items,theta);
=======
    // zetan은 items와 theta로부터 계산합니다.
>>>>>>> master
    zetan = _zetan;
    countforzeta = items;
    eta = (1 - Math.pow(2.0 / items, 1 - theta)) / (1 - zeta2theta / zetan);

<<<<<<< HEAD
    // System.out.println("XXXX 3 XXXX");
    nextInt();
    // System.out.println("XXXX 4 XXXX");
  }

  /**
   * Compute the zeta constant needed for the distribution. Do this from scratch for a distribution
   * with n items, using the zipfian constant theta. Remember the value of n, so if we change the
   * itemcount, we can recompute zeta.
   *
   * @param n The number of items to compute zeta over.
   * @param theta The zipfian constant.
=======
    // 디버그 출력: "XXXX 3 XXXX"
    nextInt();
    // 디버그 출력: "XXXX 4 XXXX"
  }

  /**
   * n개의 아이템 분포를 위해 zipfian 상수 theta로 zeta 값을 처음부터 계산합니다. n 값을 기억해서 아이템 수가 바뀌면 zeta를 다시 계산할 수 있습니다.
   *
   * @param n zeta를 계산할 아이템 수
   * @param theta 사용할 zipfian 상수
>>>>>>> master
   */
  double zeta(long n, double theta) {
    countforzeta = n;
    return zetastatic(n, theta);
  }

  /**
<<<<<<< HEAD
   * Compute the zeta constant needed for the distribution. Do this from scratch for a distribution
   * with n items, using the zipfian constant theta. This is a static version of the function which
   * will not remember n.
   *
   * @param n The number of items to compute zeta over.
   * @param theta The zipfian constant.
=======
   * n개의 아이템과 theta zipfian 상수로 zeta를 처음부터 계산하는 정적 버전입니다. 이 함수는 n 값을 기억하지 않습니다.
   *
   * @param n zeta를 계산할 아이템 수
   * @param theta 사용할 zipfian 상수
>>>>>>> master
   */
  static double zetastatic(long n, double theta) {
    return zetastatic(0, n, theta, 0);
  }

  /**
<<<<<<< HEAD
   * Compute the zeta constant needed for the distribution. Do this incrementally for a distribution
   * that has n items now but used to have st items. Use the zipfian constant theta. Remember the
   * new value of n so that if we change the itemcount, we'll know to recompute zeta.
   *
   * @param st The number of items used to compute the last initialsum
   * @param n The number of items to compute zeta over.
   * @param theta The zipfian constant.
   * @param initialsum The value of zeta we are computing incrementally from.
=======
   * 이전에 st개의 아이템으로 계산한 zeta에 이어서, 현재 n개의 아이템을 위한 zeta를 점증적으로 계산합니다. theta zipfian 상수를 사용하며, 새로운 n
   * 값을 기억해서 itemcount 변화 시 다시 계산할지 판단할 수 있습니다.
   *
   * @param st 마지막으로 initialsum을 계산할 때 쓰인 아이템 수
   * @param n 현재의 아이템 수
   * @param theta 사용할 zipfian 상수
   * @param initialsum 이전에 계산된 zeta 값
>>>>>>> master
   */
  double zeta(long st, long n, double theta, double initialsum) {
    countforzeta = n;
    return zetastatic(st, n, theta, initialsum);
  }

  /**
   * Compute the zeta constant needed for the distribution. Do this incrementally for a distribution
   * that has n items now but used to have st items. Use the zipfian constant theta. Remember the
   * new value of n so that if we change the itemcount, we'll know to recompute zeta.
   *
   * @param st The number of items used to compute the last initialsum
   * @param n The number of items to compute zeta over.
   * @param theta The zipfian constant.
   * @param initialsum The value of zeta we are computing incrementally from.
   */
  static double zetastatic(long st, long n, double theta, double initialsum) {
    double sum = initialsum;
    for (long i = st; i < n; i++) {

      sum += 1 / (Math.pow(i + 1, theta));
    }

<<<<<<< HEAD
    // System.out.println("countforzeta="+countforzeta);
=======
    // 디버그 출력: countforzeta 값을 확인합니다.
>>>>>>> master

    return sum;
  }

  /**
<<<<<<< HEAD
   * Generate the next item. this distribution will be skewed toward lower integers; e.g. 0 will be
   * the most popular, 1 the next most popular, etc.
   *
   * @param itemcount The number of items in the distribution.
   * @return The next item in the sequence.
=======
   * 다음 아이템을 생성합니다. 이 분포는 낮은 정수 쪽으로 치우치며, 0이 가장 인기 있고 1이 두 번째입니다.
   *
   * @param itemcount 분포에서 고려하는 아이템 개수
   * @return 다음 항목
>>>>>>> master
   */
  public int nextInt(int itemcount) {
    return (int) nextLong(itemcount);
  }

  /**
<<<<<<< HEAD
   * Generate the next item as a long.
   *
   * @param itemcount The number of items in the distribution.
   * @return The next item in the sequence.
   */
  public long nextLong(long itemcount) {
    // from "Quickly Generating Billion-Record Synthetic Databases", Jim Gray et al, SIGMOD 1994

    if (itemcount != countforzeta) {

      // have to recompute zetan and eta, since they depend on itemcount
      synchronized (this) {
        if (itemcount > countforzeta) {
          // System.err.println("WARNING: Incrementally recomputing Zipfian distribtion.
          // (itemcount="+itemcount+" countforzeta="+countforzeta+")");

          // we have added more items. can compute zetan incrementally, which is cheaper
          zetan = zeta(countforzeta, itemcount, theta, zetan);
          eta = (1 - Math.pow(2.0 / items, 1 - theta)) / (1 - zeta2theta / zetan);
        } else if ((itemcount < countforzeta) && (allowitemcountdecrease)) {
          // have to start over with zetan
          // note : for large itemsets, this is very slow. so don't do it!

          // TODO: can also have a negative incremental computation, e.g. if you decrease the number
          // of items, then just subtract
          // the zeta sequence terms for the items that went away. This would be faster than
          // recomputing from scratch when the number of items
          // decreases
=======
   * 다음 아이템을 long으로 생성합니다.
   *
   * @param itemcount 분포에 포함되는 아이템 수
   * @return 다음 항목
   */
  public long nextLong(long itemcount) {
    // Jim Gray 외의 SIGMOD 1994 논문 "Quickly Generating Billion-Record Synthetic Databases" 출처

    if (itemcount != countforzeta) {

      // itemcount에 따라 zetan과 eta를 다시 계산해야 합니다.
      synchronized (this) {
        if (itemcount > countforzeta) {
          // (예시) 경고 메시지를 stderr로 출력할 수 있습니다.
          // System.err.println("WARNING: Incrementally recomputing Zipfian distribtion.
          // (itemcount="+itemcount+" countforzeta="+countforzeta+")");

          // 아이템이 늘었으므로 점증적으로 zetan을 계산할 수 있습니다(더 저렴함).
          zetan = zeta(countforzeta, itemcount, theta, zetan);
          eta = (1 - Math.pow(2.0 / items, 1 - theta)) / (1 - zeta2theta / zetan);
        } else if ((itemcount < countforzeta) && (allowitemcountdecrease)) {
          // zetan을 처음부터 다시 계산해야 합니다.
          // 참고: 아이템 세트가 크면 매우 느립니다. 가능한 피해야 합니다.

          // TODO: 아이템 수를 줄이면 빠르게 대응하기 위해 감소분 zeta 항을 빼는 방식 등 음의 점증 계산도 고려할 수 있습니다.
          //       이렇게 하면 아이템 수가 줄어들 때 전체를 다시 계산하지 않아도 됩니다.
>>>>>>> master

          LOG.warn(
              "WARNING: Recomputing Zipfian distribtion. This is slow and should be avoided. (itemcount={} countforzeta={})",
              itemcount,
              countforzeta);

          zetan = zeta(itemcount, theta);
          eta = (1 - Math.pow(2.0 / items, 1 - theta)) / (1 - zeta2theta / zetan);
        }
      }
    }

    double u = this.rng.nextDouble();
    double uz = u * zetan;

    if (uz < 1.0) {
      return base;
    }

    if (uz < 1.0 + Math.pow(0.5, theta)) {
      return base + 1;
    }

    long ret = base + (long) ((itemcount) * Math.pow(eta * u - eta + 1, alpha));
    setLastInt((int) ret);
    return ret;
  }

  /**
<<<<<<< HEAD
   * Return the next value, skewed by the Zipfian distribution. The 0th item will be the most
   * popular, followed by the 1st, followed by the 2nd, etc. (Or, if min != 0, the min-th item is
   * the most popular, the min+1th item the next most popular, etc.) If you want the popular items
   * scattered throughout the item space, use ScrambledZipfianGenerator instead.
=======
   * Zipfian 분포로 치우친 다음 값을 반환합니다. 0번 아이템이 가장 인기 있고, 1번이 다음으로 인기 있으며, min이 0이 아니라면 그 대신 min부터 순서대로 인기
   * 아이템이 결정됩니다. 인기 아이템을 전체 공간에 흩어지게 하려면 {@link ScrambledZipfianGenerator}를 사용하세요.
>>>>>>> master
   */
  @Override
  public int nextInt() {
    return (int) nextLong(items);
  }

<<<<<<< HEAD
  /**
   * Return the next value, skewed by the Zipfian distribution. The 0th item will be the most
   * popular, followed by the 1st, followed by the 2nd, etc. (Or, if min != 0, the min-th item is
   * the most popular, the min+1th item the next most popular, etc.) If you want the popular items
   * scattered throughout the item space, use ScrambledZipfianGenerator instead.
   */
=======
  /** Zipfian 분포에 따라 다음 long 값을 반환합니다. 인기 아이템이 앞쪽에 몰려 있는 구조입니다. */
>>>>>>> master
  public long nextLong() {
    return nextLong(items);
  }

  /**
<<<<<<< HEAD
   * @todo Implement ZipfianGenerator.mean()
   */
  @Override
  public double mean() {
    throw new UnsupportedOperationException("@todo implement ZipfianGenerator.mean()");
=======
   * @todo ZipfianGenerator.mean()을 구현해야 합니다.
   */
  @Override
  public double mean() {
    throw new UnsupportedOperationException("@todo ZipfianGenerator.mean() 구현 필요");
>>>>>>> master
  }
}
