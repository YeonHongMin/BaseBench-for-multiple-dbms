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
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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
<<<<<<< HEAD
=======
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

/* 이 파일은 VoltDB의 일부입니다.
 * Copyright (C) 2008-2010 VoltDB L.L.C.
 *
 * VoltDB는 자유 소프트웨어입니다: 자유 소프트웨어 재단에서 발행한
 * GNU 일반 공중 사용 허가서(GPL) 버전 3 또는 그 이후 버전의 조건에 따라
 * 재배포하거나 수정할 수 있습니다.
 *
 * VoltDB는 유용할 것으로 기대되지만, 상품성이나 특정 목적에의 적합성에 대한
 * 묵시적 보증을 포함하여 어떠한 보증도 없이 배포됩니다.
 * 자세한 내용은 GNU 일반 공중 사용 허가서를 참조하십시오.
 *
 * GNU 일반 공중 사용 허가서의 사본은 VoltDB와 함께 제공되어야 합니다.
 * 그렇지 않은 경우, <http://www.gnu.org/licenses/>를 참조하십시오.
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 */

package com.oltpbenchmark.util;

import java.util.Objects;

<<<<<<< HEAD
<<<<<<< HEAD
/**
 * Class representing a pair of generic-ized types. Supports equality, hashing and all that other
 * nice Java stuff. Based on STL's pair class in C++.
 */
=======
/** 제네릭 타입 두 개를 하나의 쌍으로 표현합니다. 동등성, 해시 코드를 지원하며, C++ STL의 pair를 기반으로 합니다. */
>>>>>>> master
=======
/** 제네릭 타입 두 개를 하나의 쌍으로 표현합니다. 동등성, 해시 코드를 지원하며, C++ STL의 pair를 기반으로 합니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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

<<<<<<< HEAD
<<<<<<< HEAD
  /** Convenience class method for constructing pairs using Java's generic type inference. */
=======
  /** Java의 제네릭 타입 추론을 이용해 Pair를 생성하는 편의 메서드입니다. */
>>>>>>> master
=======
  /** Java의 제네릭 타입 추론을 이용해 Pair를 생성하는 편의 메서드입니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public static <T, U> Pair<T, U> of(T x, U y) {
    return new Pair<>(x, y);
  }
}
