/*
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
>>>>>>> master
 *
 */

package com.oltpbenchmark.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

<<<<<<< HEAD
/**
 * @author pavlo
 */
=======
/** 작성자: pavlo */
>>>>>>> master
public abstract class ClassUtil {
  private static final Logger LOG = LoggerFactory.getLogger(ClassUtil.class);

  private static final Map<Class<?>, List<Class<?>>> CACHE_getSuperClasses = new HashMap<>();
  private static final Map<Class<?>, Set<Class<?>>> CACHE_getInterfaceClasses = new HashMap<>();

  /**
<<<<<<< HEAD
   * Get the generic types for the given field
=======
   * 지정된 필드의 제네릭 타입 매개변수를 가져옵니다.
>>>>>>> master
   *
   * @param field
   * @return
   */
  public static List<Class<?>> getGenericTypes(Field field) {
    ArrayList<Class<?>> generic_classes = new ArrayList<>();
    Type gtype = field.getGenericType();
    if (gtype instanceof ParameterizedType) {
      ParameterizedType ptype = (ParameterizedType) gtype;
      getGenericTypesImpl(ptype, generic_classes);
    }
    return (generic_classes);
  }

  private static void getGenericTypesImpl(ParameterizedType ptype, List<Class<?>> classes) {
<<<<<<< HEAD
    // list the actual type arguments
=======
    // 실제 타입 인자를 나열합니다.
>>>>>>> master
    for (Type t : ptype.getActualTypeArguments()) {
      if (t instanceof Class) {
        //                System.err.println("C: " + t);
        classes.add((Class<?>) t);
      } else if (t instanceof ParameterizedType) {
        ParameterizedType next = (ParameterizedType) t;
        //                System.err.println("PT: " + next);
        classes.add((Class<?>) next.getRawType());
        getGenericTypesImpl(next, classes);
      }
    }
  }

  /**
<<<<<<< HEAD
   * Return an ordered list of all the sub-classes for a given class Useful when dealing with
   * generics
=======
   * 지정된 클래스의 서브클래스를 계층 순서대로 반환합니다. 제네릭 처리 시 유용합니다.
>>>>>>> master
   *
   * @param element_class
   * @return
   */
  public static List<Class<?>> getSuperClasses(Class<?> element_class) {
    List<Class<?>> ret = ClassUtil.CACHE_getSuperClasses.get(element_class);
    if (ret == null) {
      ret = new ArrayList<>();
      while (element_class != null) {
        ret.add(element_class);
        element_class = element_class.getSuperclass();
      }
      ret = Collections.unmodifiableList(ret);
      ClassUtil.CACHE_getSuperClasses.put(element_class, ret);
    }
    return (ret);
  }

  /**
<<<<<<< HEAD
   * Get a set of all of the interfaces that the element_class implements
=======
   * element_class가 구현한 모든 인터페이스의 집합을 반환합니다.
>>>>>>> master
   *
   * @param element_class
   * @return
   */
  public static Collection<Class<?>> getInterfaces(Class<?> element_class) {
    Set<Class<?>> ret = ClassUtil.CACHE_getInterfaceClasses.get(element_class);
    if (ret == null) {
      ret = new HashSet<Class<?>>(ClassUtils.getAllInterfaces(element_class));
      if (element_class.isInterface()) {
        ret.add(element_class);
      }
      ret = Collections.unmodifiableSet(ret);
      ClassUtil.CACHE_getInterfaceClasses.put(element_class, ret);
    }
    return (ret);
  }

  @SuppressWarnings("unchecked")
  public static <T> T newInstance(String class_name, Object[] params, Class<?>[] classes) {
    return ((T) ClassUtil.newInstance(ClassUtil.getClass(class_name), params, classes));
  }

  public static <T> T newInstance(Class<T> target_class, Object[] params, Class<?>[] classes) {
    Constructor<T> constructor = ClassUtil.getConstructor(target_class, classes);
    T ret = null;
    try {
      ret = constructor.newInstance(params);
    } catch (Exception ex) {
      throw new RuntimeException(
          "Failed to create new instance of " + target_class.getSimpleName(), ex);
    }
    return (ret);
  }

  /**
<<<<<<< HEAD
=======
   * 매개변수 배열에 대응하는 생성자를 target_class에서 검색합니다.
   *
>>>>>>> master
   * @param <T>
   * @param target_class
   * @param params
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> Constructor<T> getConstructor(Class<T> target_class, Class<?>... params) {
    NoSuchMethodException error = null;
    try {
      return (target_class.getConstructor(params));
    } catch (NoSuchMethodException ex) {
<<<<<<< HEAD
      // The first time we get this it can be ignored
      // We'll try to be nice and find a match for them
=======
      // 처음에는 무시해도 됩니다.
      // 가능한 후보를 찾아보기 위해 노력합니다.
>>>>>>> master
      error = ex;
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("TARGET CLASS:  {}", target_class);
      LOG.debug("TARGET PARAMS: {}", Arrays.toString(params));
    }

    @SuppressWarnings("rawtypes")
    List<Class<?>>[] paramSuper = (List<Class<?>>[]) new List[params.length];
    for (int i = 0; i < params.length; i++) {
      paramSuper[i] = ClassUtil.getSuperClasses(params[i]);
      if (LOG.isDebugEnabled()) {
        LOG.debug("  SUPER[{}] => {}", params[i].getSimpleName(), paramSuper[i]);
      }
    }

    for (Constructor<?> c : target_class.getConstructors()) {
      Class<?>[] cTypes = c.getParameterTypes();
      if (LOG.isDebugEnabled()) {
        LOG.debug("CANDIDATE: {}", c);
        LOG.debug("CANDIDATE PARAMS: {}", Arrays.toString(cTypes));
      }
      if (params.length != cTypes.length) {
        continue;
      }

      for (int i = 0; i < params.length; i++) {
        List<Class<?>> cSuper = ClassUtil.getSuperClasses(cTypes[i]);
        if (LOG.isDebugEnabled()) {
          LOG.debug("  SUPER[{}] => {}", cTypes[i].getSimpleName(), cSuper);
        }
        if (!CollectionUtils.intersection(paramSuper[i], cSuper).isEmpty()) {
          return ((Constructor<T>) c);
        }
      }
    }
    throw new RuntimeException(
        "Failed to retrieve constructor for " + target_class.getSimpleName(), error);
  }

  /**
<<<<<<< HEAD
=======
   * 현재 스레드 컨텍스트 ClassLoader를 사용하여 클래스 이름을 찾아 반환합니다.
   *
>>>>>>> master
   * @param class_name
   * @return
   */
  public static Class<?> getClass(String class_name) {
    return getClass(Thread.currentThread().getContextClassLoader(), class_name);
  }

  /**
<<<<<<< HEAD
=======
   * 지정된 ClassLoader를 통해 클래스 이름에 맞는 Class 객체를 반환합니다.
   *
>>>>>>> master
   * @param loader
   * @param class_name
   * @return
   */
  public static Class<?> getClass(ClassLoader loader, String class_name) {
    Class<?> target_class = null;
    try {
      target_class = ClassUtils.getClass(loader, class_name);
    } catch (Exception ex) {
      throw new RuntimeException("Failed to retrieve class for " + class_name, ex);
    }
    return target_class;
  }
}
