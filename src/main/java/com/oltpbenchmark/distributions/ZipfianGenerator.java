/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한과 조건을 준수해 주세요.
 *
 */

package com.oltpbenchmark.distributions;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
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
 */
public final class ZipfianGenerator extends IntegerGenerator {
  public static final double ZIPFIAN_CONSTANT = 0.99;

  private static final Logger LOG = LoggerFactory.getLogger(ZipfianGenerator.class);

  final Random rng;

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
   */
  boolean allowitemcountdecrease = false;

  /**
   * 지정한 아이템 개수를 사용하는 zipfian 생성기를 만듭니다.
   *
   * @param rng 랜덤 생성기
   * @param _items 아이템 개수
   */
  public ZipfianGenerator(Random rng, long _items) {
    this(rng, 0, _items - 1, ZIPFIAN_CONSTANT);
  }

  /**
   * min과 max 사이 범위에서 zipfian 생성기를 만듭니다.
   *
   * @param _min 생성할 가장 작은 정수
   * @param _max 생성할 가장 큰 정수
   */
  public ZipfianGenerator(Random rng, long _min, long _max) {
    this(rng, _min, _max, ZIPFIAN_CONSTANT);
  }

  /**
   * 지정한 zipfian 상수를 사용하는 생성기를 만듭니다.
   *
   * @param _items 전체 아이템 수
   * @param _zipfianconstant 사용할 zipfian 상수
   */
  public ZipfianGenerator(Random rng, long _items, double _zipfianconstant) {
    this(rng, 0, _items - 1, _zipfianconstant);
  }

  /**
   * min~max 범위에서 지정한 zipfian 상수로 생성기를 만듭니다.
   *
   * @param min 생성할 가장 작은 정수
   * @param max 생성할 가장 큰 정수
   * @param _zipfianconstant 사용할 zipfian 상수
   */
  public ZipfianGenerator(Random rng, long min, long max, double _zipfianconstant) {
    this(rng, min, max, _zipfianconstant, zetastatic(max - min + 1, _zipfianconstant));
  }

  /**
   * 미리 계산한 zeta 값을 이용해 min~max 범위에서 zipfian 생성기를 만듭니다.
   *
   * @param min 생성할 수 있는 가장 작은 정수
   * @param max 생성할 수 있는 가장 큰 정수
   * @param _zipfianconstant 사용할 zipfian 상수
   * @param _zetan 미리 계산된 zeta 값
   */
  public ZipfianGenerator(Random rng, long min, long max, double _zipfianconstant, double _zetan) {
    this.rng = rng;
    items = max - min + 1;
    base = min;
    zipfianconstant = _zipfianconstant;

    theta = zipfianconstant;

    zeta2theta = zeta(2, theta);

    alpha = 1.0 / (1.0 - theta);
    // zetan은 items와 theta로부터 계산합니다.
    zetan = _zetan;
    countforzeta = items;
    eta = (1 - Math.pow(2.0 / items, 1 - theta)) / (1 - zeta2theta / zetan);

    // 디버그 출력: "XXXX 3 XXXX"
    nextInt();
    // 디버그 출력: "XXXX 4 XXXX"
  }

  /**
   * n개의 아이템 분포를 위해 zipfian 상수 theta로 zeta 값을 처음부터 계산합니다. n 값을 기억해서 아이템 수가 바뀌면 zeta를 다시 계산할 수 있습니다.
   *
   * @param n zeta를 계산할 아이템 수
   * @param theta 사용할 zipfian 상수
   */
  double zeta(long n, double theta) {
    countforzeta = n;
    return zetastatic(n, theta);
  }

  /**
   * n개의 아이템과 theta zipfian 상수로 zeta를 처음부터 계산하는 정적 버전입니다. 이 함수는 n 값을 기억하지 않습니다.
   *
   * @param n zeta를 계산할 아이템 수
   * @param theta 사용할 zipfian 상수
   */
  static double zetastatic(long n, double theta) {
    return zetastatic(0, n, theta, 0);
  }

  /**
   * 이전에 st개의 아이템으로 계산한 zeta에 이어서, 현재 n개의 아이템을 위한 zeta를 점증적으로 계산합니다. theta zipfian 상수를 사용하며, 새로운 n
   * 값을 기억해서 itemcount 변화 시 다시 계산할지 판단할 수 있습니다.
   *
   * @param st 마지막으로 initialsum을 계산할 때 쓰인 아이템 수
   * @param n 현재의 아이템 수
   * @param theta 사용할 zipfian 상수
   * @param initialsum 이전에 계산된 zeta 값
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

    // 디버그 출력: countforzeta 값을 확인합니다.

    return sum;
  }

  /**
   * 다음 아이템을 생성합니다. 이 분포는 낮은 정수 쪽으로 치우치며, 0이 가장 인기 있고 1이 두 번째입니다.
   *
   * @param itemcount 분포에서 고려하는 아이템 개수
   * @return 다음 항목
   */
  public int nextInt(int itemcount) {
    return (int) nextLong(itemcount);
  }

  /**
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
   * Zipfian 분포로 치우친 다음 값을 반환합니다. 0번 아이템이 가장 인기 있고, 1번이 다음으로 인기 있으며, min이 0이 아니라면 그 대신 min부터 순서대로 인기
   * 아이템이 결정됩니다. 인기 아이템을 전체 공간에 흩어지게 하려면 {@link ScrambledZipfianGenerator}를 사용하세요.
   */
  @Override
  public int nextInt() {
    return (int) nextLong(items);
  }

  /** Zipfian 분포에 따라 다음 long 값을 반환합니다. 인기 아이템이 앞쪽에 몰려 있는 구조입니다. */
  public long nextLong() {
    return nextLong(items);
  }

  /**
   * @todo ZipfianGenerator.mean()을 구현해야 합니다.
   */
  @Override
  public double mean() {
    throw new UnsupportedOperationException("@todo ZipfianGenerator.mean() 구현 필요");
  }
}
