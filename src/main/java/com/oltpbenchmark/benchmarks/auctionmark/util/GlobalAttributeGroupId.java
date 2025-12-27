/*
 * Copyright 2020 by OLTPBenchmark Project
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
 *
 */

package com.oltpbenchmark.benchmarks.auctionmark.util;

import com.oltpbenchmark.util.CompositeId;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.IntStream;

public final class GlobalAttributeGroupId extends CompositeId
    implements Comparable<GlobalAttributeGroupId> {

  private static final int[] COMPOSITE_BITS = {
    INT_MAX_DIGITS, // CATEGORY
    INT_MAX_DIGITS, // ID
    INT_MAX_DIGITS // COUNT
  };

  public static final int ID_LENGTH = IntStream.of(COMPOSITE_BITS).sum();

  private int category_id;
  private int id;
  private int count;

  public GlobalAttributeGroupId(int category_id, int id, int count) {
    this.category_id = category_id;
    this.id = id;
    this.count = count;
  }

  public GlobalAttributeGroupId(String composite_id) {
    this.decode(composite_id);
  }

  @Override
  public String encode() {
    return (this.encode(COMPOSITE_BITS));
  }

  @Override
  public void decode(String composite_id) {
    String[] values = super.decode(composite_id, COMPOSITE_BITS);
    this.category_id = Integer.parseInt(values[0]);
    this.id = Integer.parseInt(values[1]);
    this.count = Integer.parseInt(values[2]);
  }

  @Override
  public String[] toArray() {
    return (new String[] {
      Integer.toString(this.category_id), Integer.toString(this.id), Integer.toString(this.count)
    });
  }

  public int getCategoryId() {
    return (this.category_id);
  }

  protected int getId() {
    return (this.id);
  }

  public int getCount() {
    return (this.count);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GlobalAttributeGroupId that = (GlobalAttributeGroupId) o;
    return category_id == that.category_id && id == that.id && count == that.count;
  }

  @Override
  public int hashCode() {
    return Objects.hash(category_id, id, count);
  }

  @Override
  public int compareTo(GlobalAttributeGroupId o) {
    return Comparator.comparingInt(GlobalAttributeGroupId::getCategoryId)
        .thenComparingInt(GlobalAttributeGroupId::getId)
        .thenComparingInt(GlobalAttributeGroupId::getCount)
        .compare(this, o);
  }
}
