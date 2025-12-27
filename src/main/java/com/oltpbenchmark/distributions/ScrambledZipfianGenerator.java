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
 * <p>Unlike @ZipfianGenerator, this class scatters the "popular" items across the itemspace. Use
 * this, instead of @ZipfianGenerator, if you don't want the head of the distribution (the popular
 * items) clustered together.
=======
 * zipfian 분포를 따르는 생성기입니다. 일부 아이템이 더 인기 있게 등장하며, 지정한 범위 내에서 서로 다른 빈도로 값을 반환합니다. 생성 시 전체 아이템 개수를
 * itemcount(0..itemcount-1)를 통해 지정하거나, 최소/최대값(min, max)을 전달할 수 있습니다. 생성 이후에는 nextInt(itemcount) 또는
 * nextLong(itemcount)를 호출해서 아이템 수를 조정할 수 있습니다.
 *
 * <p>{@link ZipfianGenerator}과 달리, 이 클래스는 인기 아이템들이 아이템 공간 전반에 흩어지도록 합니다. 인기 아이템이 몰려 있는 것을 원하지 않는다면
 * 이 클래스를 대신 사용하세요.
>>>>>>> master
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

<<<<<<< HEAD
  /** Return the next int in the sequence. */
=======
  /** 시퀀스의 다음 int 값을 반환합니다. */
>>>>>>> master
  @Override
  public int nextInt() {
    return (int) nextLong();
  }

<<<<<<< HEAD
  /** Return the next long in the sequence. */
=======
  /** 시퀀스의 다음 long 값을 반환합니다. */
>>>>>>> master
  public long nextLong() {
    long ret = gen.nextLong();
    ret = _min + Utils.FNVhash64(ret) % _itemcount;
    setLastInt((int) ret);
    return ret;
  }

<<<<<<< HEAD
  /**
   * since the values are scrambled (hopefully uniformly), the mean is simply the middle of the
   * range.
   */
=======
  /** 값들이 (가능하면 균등하게) 섞여 있기 때문에 평균값은 범위의 중앙입니다. */
>>>>>>> master
  @Override
  public double mean() {
    return ((double) (_min + _max)) / 2.0;
  }
}
