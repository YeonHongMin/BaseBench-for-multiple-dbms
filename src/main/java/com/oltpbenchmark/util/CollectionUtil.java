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
import org.apache.commons.collections4.set.ListOrderedSet;

<<<<<<< HEAD
<<<<<<< HEAD
/**
 * @author pavlo
 */
public abstract class CollectionUtil {

  /**
   * Put all the values of an Iterator into a List
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
/** 작성자: pavlo */
public abstract class CollectionUtil {

  /**
   * Iterator에 있는 모든 값을 List로 옮깁니다.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <T>
   * @param it
   * @return
   */
  public static <T> List<T> list(Iterator<T> it) {
    List<T> list = new ArrayList<>();
    CollectionUtil.addAll(list, it);
    return (list);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Add all the items in the array to a Collection
=======
   * 배열의 모든 항목을 Collection에 추가합니다.
>>>>>>> master
=======
   * 배열의 모든 항목을 Collection에 추가합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <T>
   * @param data
   * @param items
   */
  @SuppressWarnings("unchecked")
  public static <T> Collection<T> addAll(Collection<T> data, T... items) {
    data.addAll(Arrays.asList(items));
    return (data);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Add all of the items from the Iterator into the given collection
=======
   * Iterator에서 가져온 모든 항목을 대상 컬렉션에 추가합니다.
>>>>>>> master
=======
   * Iterator에서 가져온 모든 항목을 대상 컬렉션에 추가합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <T>
   * @param data
   * @param items
   */
  public static <T> Collection<T> addAll(Collection<T> data, Iterator<T> items) {
    while (items.hasNext()) {
      data.add(items.next());
    }
    return (data);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
=======
   * 주어진 맵에서 가장 큰 값에 대응하는 키를 반환합니다.
   *
>>>>>>> master
=======
   * 주어진 맵에서 가장 큰 값에 대응하는 키를 반환합니다.
   *
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * @param <T>
   * @param <U>
   * @param map
   * @return
   */
  public static <T, U extends Comparable<U>> T getGreatest(Map<T, U> map) {
    T max_key = null;
    U max_value = null;
    for (Map.Entry<T, U> e : map.entrySet()) {
      T key = e.getKey();
      U value = e.getValue();
      if (max_value == null || value.compareTo(max_value) > 0) {
        max_value = value;
        max_key = key;
      }
<<<<<<< HEAD
<<<<<<< HEAD
    } // FOR
=======
    } // 반복 종료
>>>>>>> master
=======
    } // 반복 종료
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    return (max_key);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return the first item in a Iterable
=======
   * Iterable에서 첫 번째 항목을 반환합니다.
>>>>>>> master
=======
   * Iterable에서 첫 번째 항목을 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <T>
   * @param items
   * @return
   */
  public static <T> T first(Iterable<T> items) {
    return (CollectionUtil.get(items, 0));
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return the ith element of a set. Super lame
=======
   * Set에서 i번째 요소를 반환합니다. 다소 불편한 방식입니다.
>>>>>>> master
=======
   * Set에서 i번째 요소를 반환합니다. 다소 불편한 방식입니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <T>
   * @param items
   * @param idx
   * @return
   */
  public static <T> T get(Iterable<T> items, int idx) {
    if (items instanceof AbstractList<?>) {
      return ((AbstractList<T>) items).get(idx);
    } else if (items instanceof ListOrderedSet<?>) {
      return ((ListOrderedSet<T>) items).get(idx);
    }
    int ctr = 0;
    for (T t : items) {
      if (ctr++ == idx) {
        return (t);
      }
    }
    return (null);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return the last item in an Iterable
=======
   * Iterable에서 마지막 항목을 반환합니다.
>>>>>>> master
=======
   * Iterable에서 마지막 항목을 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <T>
   * @param items
   * @return
   */
  public static <T> T last(Iterable<T> items) {
    T last = null;
    if (items instanceof AbstractList<?>) {
      AbstractList<T> list = (AbstractList<T>) items;
      last = (list.isEmpty() ? null : list.get(list.size() - 1));
    } else {
      for (T t : items) {
        last = t;
      }
    }
    return (last);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return the last item in an array
=======
   * 배열에서 마지막 항목을 반환합니다.
>>>>>>> master
=======
   * 배열에서 마지막 항목을 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <T>
   * @param items
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T last(T... items) {
    if (items != null && items.length > 0) {
      return (items[items.length - 1]);
    }
    return (null);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Wrap an Iterable around an Iterator
=======
   * Iterator를 감싼 Iterable을 생성합니다.
>>>>>>> master
=======
   * Iterator를 감싼 Iterable을 생성합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <T>
   * @param it
   * @return
   */
  public static <T> Iterable<T> iterable(final Iterator<T> it) {
    return (new Iterable<T>() {
      @Override
      public Iterator<T> iterator() {
        return (it);
      }
    });
  }

  public static <T> T pop(Collection<T> items) {
    T t = CollectionUtil.first(items);
    if (t != null) {
      items.remove(t);
    }
    return (t);
  }
}
