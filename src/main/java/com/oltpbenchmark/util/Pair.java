/*
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
 *
 */

/* This file is part of VoltDB.
 * Copyright (C) 2008-2010 VoltDB L.L.C.
 *
 * VoltDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VoltDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VoltDB.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.oltpbenchmark.util;

import java.util.Objects;

/**
 * 제네릭 타입 두 개를 하나의 쌍으로 표현합니다. 동등성, 해시 코드를 지원하며, C++ STL의 pair를 기반으로 합니다.
 */
public class Pair<T, U> implements Comparable<Pair<T, U>> {

  public final T first;
  public final U second;
  private final transient Integer hash;

  public Pair(T first, U second, boolean precomputeHash) {
    this.first = first;
    this.second = second;
    hash = (precomputeHash ? this.computeHashCode() : null);
  }

  public Pair(T first, U second) {
    this(first, second, true);
  }

  private int computeHashCode() {
    return (first == null ? 0 : first.hashCode() * 31) + (second == null ? 0 : second.hashCode());
  }

  public int hashCode() {
    if (hash != null) {
      return (hash);
    }
    return (this.computeHashCode());
  }

  public String toString() {
    return String.format("<%s, %s>", first, second);
  }

  @Override
  public int compareTo(Pair<T, U> other) {
    return (other.hash - this.hash);
  }

  @SuppressWarnings("unchecked")
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || !(o instanceof Pair)) {
      return false;
    }

    Pair<T, U> other = (Pair<T, U>) o;

    return (Objects.equals(first, other.first)) && (Objects.equals(second, other.second));
  }

  /** Java의 제네릭 타입 추론을 이용해 Pair를 생성하는 편의 메서드입니다. */
  public static <T, U> Pair<T, U> of(T x, U y) {
    return new Pair<>(x, y);
  }
}

