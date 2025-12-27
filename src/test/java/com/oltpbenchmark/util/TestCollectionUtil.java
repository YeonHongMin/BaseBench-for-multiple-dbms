/*
<<<<<<< HEAD
<<<<<<< HEAD
 *  Copyright 2015 by OLTPBenchmark Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
=======
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Apache License, Version 2.0 (the "License")에 따라 라이선스가 부여됩니다.
 * 이 파일을 사용하려면 라이선스와 일치해야 합니다.
 * 다음에서 라이선스 사본을 얻을 수 있습니다:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 동의하지 않는 한,
 * 라이선스에 따라 배포되는 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적이거나 암묵적인 어떠한 종류의 보증이나 조건도 없습니다.
 * 라이선스에서 허용되는 특정 언어에 대한 권한과
 * 제한 사항을 참조하십시오.
 *
>>>>>>> master
=======
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
 *
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 */

package com.oltpbenchmark.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.*;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.junit.Test;

/**
 * @author pavlo
 */
public class TestCollectionUtil {

  private final Random rand = new Random();

<<<<<<< HEAD
<<<<<<< HEAD
  /** testIterableEnumeration */
=======
  /** 반복 가능한 열거 테스트 */
>>>>>>> master
=======
  /** testIterableEnumeration */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @Test
  public void testIterableEnumeration() {
    final int size = 10;
    Enumeration<Integer> e =
        new Enumeration<Integer>() {
          int ctr = 0;

          @Override
          public Integer nextElement() {
            return (ctr++);
          }

          @Override
          public boolean hasMoreElements() {
            return (ctr < size);
          }
        };

    List<Integer> found = new ArrayList<Integer>();
    for (Integer i : CollectionUtil.iterable(e.asIterator())) found.add(i);
    assertEquals(size, found.size());
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** testAddAll */
=======
  /** 모두 추가 테스트 */
>>>>>>> master
=======
  /** testAddAll */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @Test
  public void testAddAll() {
    int cnt = rand.nextInt(50) + 1;
    List<Integer> l = new ArrayList<Integer>();
    Integer[] a = new Integer[cnt];
    for (int i = 0; i < cnt; i++) {
      int next = rand.nextInt();
      l.add(next);
      a[i] = next;
    } // FOR

    Collection<Integer> c = CollectionUtil.addAll(new HashSet<Integer>(), l.iterator());
    assertEquals(l.size(), c.size());
    assert (c.containsAll(l));

    c = CollectionUtil.addAll(new HashSet<Integer>(), a);
    assertEquals(l.size(), c.size());
    assert (c.containsAll(l));
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** testGetGreatest */
=======
  /** 최대값 가져오기 테스트 */
>>>>>>> master
=======
  /** testGetGreatest */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @Test
  public void testGetGreatest() {
    Map<String, Integer> map = new HashMap<String, Integer>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 4);
    map.put("d", 3);
    String key = CollectionUtil.getGreatest(map);
    assertEquals("c", key);
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** testGetFirst */
=======
  /** 첫 번째 항목 가져오기 테스트 */
>>>>>>> master
=======
  /** testGetFirst */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @Test
  public void testGetFirst() {
    List<String> list = new ArrayList<String>();
    list.add("a");
    list.add("b");
    list.add("c");
    String key = CollectionUtil.first(list);
    assertEquals("a", key);
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** testPop */
=======
  /** 팝 테스트 */
>>>>>>> master
=======
  /** testPop */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @SuppressWarnings("unchecked")
  @Test
  public void testPop() {
    String[] expected = new String[11];
    RandomGenerator rng = new RandomGenerator(0);
    for (int i = 0; i < expected.length; i++) {
      expected[i] = rng.astring(1, 32);
    } // FOR

    @SuppressWarnings("rawtypes")
    Collection<String>[] collections =
        new Collection[] {
          CollectionUtil.addAll(new ListOrderedSet<String>(), expected),
          CollectionUtil.addAll(new HashSet<String>(), expected),
          CollectionUtil.addAll(new ArrayList<String>(), expected),
        };
    for (Collection<String> c : collections) {
      assertNotNull(c);
      assertEquals(c.getClass().getSimpleName(), expected.length, c.size());
      String pop = CollectionUtil.pop(c);
      assertNotNull(c.getClass().getSimpleName(), pop);
      assertEquals(c.getClass().getSimpleName(), expected.length - 1, c.size());
      assertFalse(c.getClass().getSimpleName(), c.contains(pop));

      if (c instanceof List || c instanceof ListOrderedSet) {
        assertEquals(c.getClass().getSimpleName(), expected[0], pop);
      }
    } // FOR
  }
}
