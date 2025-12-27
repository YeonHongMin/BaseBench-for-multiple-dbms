/*
 * 저작권 2020 Trino
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
 *
 */
package com.oltpbenchmark.util;

/** 주어진 범위에서 랜덤 long 값을 생성하는 클래스입니다. */
public class RowRandomBoundedLong {
  private final RowRandomLong randomLong;
  private final RowRandomInt randomInt;

  private final long lowValue;
  private final long highValue;

  public RowRandomBoundedLong(long seed, boolean use64Bits, long lowValue, long highValue) {
    this(seed, use64Bits, lowValue, highValue, 1);
  }

  public RowRandomBoundedLong(
      long seed, boolean use64Bits, long lowValue, long highValue, int seedsPerRow) {
    if (use64Bits) {
      this.randomLong = new RowRandomLong(seed, seedsPerRow);
      this.randomInt = null;
    } else {
      this.randomLong = null;
      this.randomInt = new RowRandomInt(seed, seedsPerRow);
    }

    this.lowValue = lowValue;
    this.highValue = highValue;
  }

  public long nextValue() {
    if (randomLong != null) {
      return randomLong.nextLong(lowValue, highValue);
    }
    return randomInt.nextInt((int) lowValue, (int) highValue);
  }

  public void rowFinished() {
    if (randomLong != null) {
      randomLong.rowFinished();
    } else {
      randomInt.rowFinished();
    }
  }

  public void advanceRows(long rowCount) {
    if (randomLong != null) {
      randomLong.advanceRows(rowCount);
    } else {
      randomInt.advanceRows(rowCount);
    }
  }
}
