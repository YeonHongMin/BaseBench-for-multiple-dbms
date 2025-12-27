/*
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
 */

package com.oltpbenchmark.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;
import org.junit.Test;

/**
 * @author pavlo
 */
public class TestClassUtil {

  private final Class<?> target_class = ArrayList.class;

  public static class MockObject1 {
    public MockObject1(MockObject1 x) {}
  }

  public static class MockObject2 {
    public MockObject2(MockObject2 x) {}
  }

  public static class MockObject3 extends MockObject2 {
    public MockObject3(MockObject2 x) {
      super(x);
    }
  }

<<<<<<< HEAD
  /** testGetConstructor */
=======
  /** 생성자 테스트 */
>>>>>>> master
  @Test
  public void testGetConstructor() throws Exception {
    Class<?>[] targets = {
      MockObject1.class, MockObject2.class,
    };
    Class<?>[] params = {MockObject1.class};

    for (Class<?> targetClass : targets) {
      Constructor<?> c = ClassUtil.getConstructor(targetClass, params);
      assertNotNull(c);
    } // FOR
  }

<<<<<<< HEAD
  /** testGetSuperClasses */
=======
  /** 슈퍼 클래스 테스트 */
>>>>>>> master
  @Test
  public void testGetSuperClasses() {
    Class<?>[] expected = {
      target_class, AbstractList.class, AbstractCollection.class, Object.class,
    };
    List<Class<?>> results = ClassUtil.getSuperClasses(target_class);
    // System.err.println(target_class + " => " + results);
    assert (!results.isEmpty());
    assertEquals(expected.length, results.size());

    for (Class<?> e : expected) {
      assert (results.contains(e));
    } // FOR
  }

<<<<<<< HEAD
  /** testGetSuperClassesCatalogType */
=======
  /** 카탈로그 타입 슈퍼 클래스 테스트 */
>>>>>>> master
  @Test
  public void testGetSuperClassesCatalogType() {
    Class<?>[] expected = {
      MockObject3.class, MockObject2.class, Object.class,
    };
    List<Class<?>> results = ClassUtil.getSuperClasses(MockObject3.class);
    assert (!results.isEmpty());
    assertEquals(expected.length, results.size());

    for (Class<?> e : expected) {
      assert (results.contains(e));
    } // FOR
  }

<<<<<<< HEAD
  /** GetInterfaces */
=======
  /** 인터페이스 가져오기 */
>>>>>>> master
  @Test
  public void testGetInterfaces() {
    Class<?>[] expected = {
      Serializable.class,
      Cloneable.class,
      Iterable.class,
      Collection.class,
      List.class,
      RandomAccess.class,
      // New in Java 21:
      SequencedCollection.class,
    };
    Collection<Class<?>> results = ClassUtil.getInterfaces(target_class);
    // System.err.println(target_class + " => " + results);
    assert (!results.isEmpty());
    assertEquals(expected.length, results.size());

    for (Class<?> e : expected) {
      assert (results.contains(e));
    } // FOR
  }
}
