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

/**
 * zipfian 분포를 따르는 생성기입니다. 일부 아이템이 더 인기 있게 등장하며, 지정한 범위 내에서 서로 다른 빈도로 값을 반환합니다. 생성 시 전체 아이템 개수를
 * itemcount(0..itemcount-1)를 통해 지정하거나, 최소/최대값(min, max)을 전달할 수 있습니다. 생성 이후에는 nextInt(itemcount) 또는
 * nextLong(itemcount)를 호출해서 아이템 수를 조정할 수 있습니다.
 *
 * <p>{@link ZipfianGenerator}과 달리, 이 클래스는 인기 아이템들이 아이템 공간 전반에 흩어지도록 합니다. 인기 아이템이 몰려 있는 것을 원하지 않는다면
 * 이 클래스를 대신 사용하세요.
 */
public class ScrambledZipfianGenerator extends IntegerGenerator {
  public static final double ZETAN = 26.46902820178302;
  public static final double USED_ZIPFIAN_CONSTANT = 0.99;
  public static final long ITEM_COUNT = 10000000000L;

  ZipfianGenerator gen;
  long _min, _max, _itemcount;

  /**
   * Create a zipfian generator for the specified number of items.
   *
   * @param _items The number of items in the distribution.
   */
  public ScrambledZipfianGenerator(long _items) {
    this(0, _items - 1);
  }

  /**
   * Create a zipfian generator for items between min and max.
   *
   * @param _min The smallest integer to generate in the sequence.
   * @param _max The largest integer to generate in the sequence.
   */
  public ScrambledZipfianGenerator(long _min, long _max) {
    this(_min, _max, ZipfianGenerator.ZIPFIAN_CONSTANT);
  }

  /**
   * Create a zipfian generator for items between min and max (inclusive) for the specified zipfian
   * constant. If you use a zipfian constant other than 0.99, this will take a long time to complete
   * because we need to recompute zeta.
   *
   * @param min The smallest integer to generate in the sequence.
   * @param max The largest integer to generate in the sequence.
   * @param _zipfianconstant The zipfian constant to use.
   */
  public ScrambledZipfianGenerator(long min, long max, double _zipfianconstant) {
    _min = min;
    _max = max;
    _itemcount = _max - _min + 1;
    if (_zipfianconstant == USED_ZIPFIAN_CONSTANT) {
      gen = new ZipfianGenerator(Utils.random(), 0, ITEM_COUNT, _zipfianconstant, ZETAN);
    } else {
      gen = new ZipfianGenerator(Utils.random(), 0, ITEM_COUNT, _zipfianconstant);
    }
  }

  /** 시퀀스의 다음 int 값을 반환합니다. */
  @Override
  public int nextInt() {
    return (int) nextLong();
  }

  /** 시퀀스의 다음 long 값을 반환합니다. */
  public long nextLong() {
    long ret = gen.nextLong();
    ret = _min + Utils.FNVhash64(ret) % _itemcount;
    setLastInt((int) ret);
    return ret;
  }

  /** 값들이 (가능하면 균등하게) 섞여 있기 때문에 평균값은 범위의 중앙입니다. */
  @Override
  public double mean() {
    return ((double) (_min + _max)) / 2.0;
  }
}
