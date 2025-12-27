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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

<<<<<<< HEAD
<<<<<<< HEAD
/**
 * @author pavlo
 */
=======
/** 작성자: pavlo */
>>>>>>> master
=======
/** 작성자: pavlo */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
public abstract class JSONUtil {
  private static final Logger LOG = LoggerFactory.getLogger(JSONUtil.class.getName());

  private static final String JSON_CLASS_SUFFIX = "_class";
  private static final Map<Class<?>, Field[]> SERIALIZABLE_FIELDS = new HashMap<>();

  /**
<<<<<<< HEAD
<<<<<<< HEAD
=======
   * 주어진 클래스에서 직렬화 가능한 필드 배열을 반환합니다.
   *
>>>>>>> master
=======
   * 주어진 클래스에서 직렬화 가능한 필드 배열을 반환합니다.
   *
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * @param clazz
   * @return
   */
  public static Field[] getSerializableFields(Class<?> clazz, String... fieldsToExclude) {
    Field[] ret = SERIALIZABLE_FIELDS.get(clazz);
    if (ret == null) {
      Collection<String> exclude = CollectionUtil.addAll(new HashSet<>(), fieldsToExclude);
      synchronized (SERIALIZABLE_FIELDS) {
        ret = SERIALIZABLE_FIELDS.get(clazz);
        if (ret == null) {
          List<Field> fields = new ArrayList<>();
          for (Field f : clazz.getFields()) {
            int modifiers = f.getModifiers();
            if (!Modifier.isTransient(modifiers)
                && Modifier.isPublic(modifiers)
                && !Modifier.isStatic(modifiers)
                && !exclude.contains(f.getName())) {
              fields.add(f);
            }
          }
          ret = fields.toArray(new Field[0]);
          SERIALIZABLE_FIELDS.put(clazz, ret);
        }
      }
    }
    return (ret);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * JSON Pretty Print
=======
   * JSON 문자열을 보기 좋은(Pretty) 포맷으로 변환합니다.
>>>>>>> master
=======
   * JSON 문자열을 보기 좋은(Pretty) 포맷으로 변환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param json
   * @return
   * @throws JSONException
   */
  public static String format(String json) {
    try {
      return (JSONUtil.format(
          new JSONObject(json) {
            /**
<<<<<<< HEAD
<<<<<<< HEAD
             * changes the value of JSONObject.map to a LinkedHashMap in order to maintain order of
             * keys. See Also: https://stackoverflow.com/a/62476486
=======
             * JSONObject.map의 값을 LinkedHashMap으로 바꿔 키 순서를 유지합니다. 참고:
             * https://stackoverflow.com/a/62476486
>>>>>>> master
=======
             * JSONObject.map의 값을 LinkedHashMap으로 바꿔 키 순서를 유지합니다. 참고:
             * https://stackoverflow.com/a/62476486
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
             */
            @Override
            public JSONObject put(String key, Object value) throws JSONException {
              try {
                Field map = JSONObject.class.getDeclaredField("map");
                map.setAccessible(true);
                Object mapValue = map.get(this);
                if (!(mapValue instanceof LinkedHashMap)) {
                  map.set(this, new LinkedHashMap<>());
                }
              } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
              }
              return super.put(key, value);
            }
          }));
    } catch (RuntimeException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static String format(JSONObject o) {
    try {
      return o.toString(1);
    } catch (JSONException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
=======
   * 객체를 JSON 문자열로 직렬화합니다.
   *
>>>>>>> master
=======
   * 객체를 JSON 문자열로 직렬화합니다.
   *
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * @param object
   * @return
   */
  public static String toJSONString(Object object) {
    JSONStringer stringer = new JSONStringer();
    try {
      if (object instanceof JSONSerializable) {
        stringer.object();
        ((JSONSerializable) object).toJSON(stringer);
        stringer.endObject();
      } else if (object != null) {
        Class<?> clazz = object.getClass();
        //                stringer.key(clazz.getSimpleName());
        JSONUtil.writeFieldValue(stringer, clazz, object);
      }
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
    return (stringer.toString());
  }

  public static <T extends JSONSerializable> T fromJSONString(T t, String json) {
    try {
      JSONObject json_object = new JSONObject(json);
      t.fromJSON(json_object);
    } catch (JSONException ex) {
      throw new RuntimeException("Failed to deserialize object " + t, ex);
    }
    return (t);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Write the contents of a JSONSerializable object out to a file on the local disk
=======
   * JSONSerializable 객체의 내용을 로컬 파일로 씁니다.
>>>>>>> master
=======
   * JSONSerializable 객체의 내용을 로컬 파일로 씁니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <T>
   * @param object
   * @param output_path
   * @throws IOException
   */
  public static <T extends JSONSerializable> void save(T object, String output_path)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug(
          "Writing out contents of {} to '{}'", object.getClass().getSimpleName(), output_path);
    }
    File f = new File(output_path);
    try {
      FileUtil.makeDirIfNotExists(f.getParent());
      String json = object.toJSONString();
      FileUtil.writeStringToFile(f, format(json));
    } catch (Exception ex) {
      LOG.error("Failed to serialize the {} file '{}'", object.getClass().getSimpleName(), f, ex);
      throw new IOException(ex);
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Load in a JSONSerialable stored in a file
=======
   * 파일에 저장된 JSONSerializable 객체를 로드합니다.
>>>>>>> master
=======
   * 파일에 저장된 JSONSerializable 객체를 로드합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param object
   * @param input_path
   * @throws Exception
   */
  public static <T extends JSONSerializable> void load(T object, String input_path)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug(
          "Loading in serialized {} from '{}'", object.getClass().getSimpleName(), input_path);
    }

    String contents;

    try (InputStream in = JSONUtil.class.getResourceAsStream(input_path)) {
      contents = IOUtils.toString(in, Charset.defaultCharset());
    }

    try {
      object.fromJSON(new JSONObject(contents));
    } catch (Exception ex) {
      LOG.error(
          "Failed to deserialize the {} from file '{}'",
          object.getClass().getSimpleName(),
          input_path,
          ex);
      throw new IOException(ex);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("The loading of the {} is complete", object.getClass().getSimpleName());
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * For a given list of Fields, write out the contents of the corresponding field to the JSONObject
   * The each of the JSONObject's elements will be the upper case version of the Field's name
=======
   * 지정된 필드 목록의 값을 JSONObject에 기록합니다. JSONObject의 각 요소 이름은 필드 이름의 대문자 버전입니다.
>>>>>>> master
=======
   * 지정된 필드 목록의 값을 JSONObject에 기록합니다. JSONObject의 각 요소 이름은 필드 이름의 대문자 버전입니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <T>
   * @param stringer
   * @param object
   * @param base_class
   * @param fields
   * @throws JSONException
   */
  public static <T> void fieldsToJSON(
      JSONStringer stringer, T object, Class<? extends T> base_class, Field[] fields)
      throws JSONException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Serializing out {} elements for {}", fields.length, base_class.getSimpleName());
    }
    for (Field f : fields) {
      String json_key = f.getName().toUpperCase();
      stringer.key(json_key);

      try {
        Class<?> f_class = f.getType();
        Object f_value = f.get(object);

<<<<<<< HEAD
<<<<<<< HEAD
        // Null
        if (f_value == null) {
          writeFieldValue(stringer, f_class, f_value);
          // Maps
        } else if (f_value instanceof Map) {
          writeFieldValue(stringer, f_class, f_value);
          // Everything else
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
        // 값이 null인 경우
        if (f_value == null) {
          writeFieldValue(stringer, f_class, f_value);
          // Map
        } else if (f_value instanceof Map) {
          writeFieldValue(stringer, f_class, f_value);
          // 그 외 모든 경우
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
        } else {
          writeFieldValue(stringer, f_class, f_value);
        }
      } catch (Exception ex) {
        throw new JSONException(ex);
      }
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
=======
   * 필드 값을 JSON에 기록합니다.
   *
>>>>>>> master
=======
   * 필드 값을 JSON에 기록합니다.
   *
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * @param stringer
   * @param field_class
   * @param field_value
   * @throws JSONException
   */
  public static void writeFieldValue(
      JSONStringer stringer, Class<?> field_class, Object field_value) throws JSONException {
<<<<<<< HEAD
<<<<<<< HEAD
    // Null
=======
    // 값이 null인 경우
>>>>>>> master
=======
    // 값이 null인 경우
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    if (field_value == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("writeNullFieldValue({}, {})", field_class, field_value);
      }
      stringer.value(null);

<<<<<<< HEAD
<<<<<<< HEAD
      // Collections
=======
      // 컬렉션
>>>>>>> master
=======
      // 컬렉션
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    } else if (ClassUtil.getInterfaces(field_class).contains(Collection.class)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("writeCollectionFieldValue({}, {})", field_class, field_value);
      }
      stringer.array();
      for (Object value : (Collection<?>) field_value) {
        if (value == null) {
          stringer.value(null);
        } else {
          writeFieldValue(stringer, value.getClass(), value);
        }
      }
      stringer.endArray();

<<<<<<< HEAD
<<<<<<< HEAD
      // Maps
=======
      // Map
>>>>>>> master
=======
      // Map
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    } else if (field_value instanceof Map) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("writeMapFieldValue({}, {})", field_class, field_value);
      }
      stringer.object();
      for (Entry<?, ?> e : ((Map<?, ?>) field_value).entrySet()) {
<<<<<<< HEAD
<<<<<<< HEAD
        // We can handle null keys
        String key_value = null;
        if (e.getKey() != null) {
          // deserialize it on the other side
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
        // null 키도 처리할 수 있습니다.
        String key_value = null;
        if (e.getKey() != null) {
          // 반대편에서도 역직렬화합니다.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
          Class<?> key_class = e.getKey().getClass();
          key_value = makePrimitiveValue(key_class, e.getKey()).toString();
        }
        stringer.key(key_value);

<<<<<<< HEAD
<<<<<<< HEAD
        // We can also handle null values. Where is your god now???
=======
        // null 값도 처리합니다. 문제없습니다.
>>>>>>> master
=======
        // null 값도 처리합니다. 문제없습니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
        if (e.getValue() == null) {
          stringer.value(null);
        } else {
          writeFieldValue(stringer, e.getValue().getClass(), e.getValue());
        }
      }
      stringer.endObject();

<<<<<<< HEAD
<<<<<<< HEAD
      // Primitive
=======
      // 원시 타입
>>>>>>> master
=======
      // 원시 타입
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("writePrimitiveFieldValue({}, {})", field_class, field_value);
      }
      stringer.value(makePrimitiveValue(field_class, field_value));
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Read data from the given JSONObject and populate the given Map
=======
   * 주어진 JSONObject에서 데이터를 읽어 Map에 채웁니다.
>>>>>>> master
=======
   * 주어진 JSONObject에서 데이터를 읽어 Map에 채웁니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param json_object
   * @param map
   * @param inner_classes
   * @throws Exception
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  protected static void readMapField(
      final JSONObject json_object, final Map map, final Stack<Class> inner_classes)
      throws Exception {
    Class<?> key_class = inner_classes.pop();
    Class<?> val_class = inner_classes.pop();
    Collection<Class<?>> val_interfaces = ClassUtil.getInterfaces(val_class);

    for (String json_key : CollectionUtil.iterable(json_object.keys())) {
      final Stack<Class> next_inner_classes = new Stack<>();
      next_inner_classes.addAll(inner_classes);

<<<<<<< HEAD
<<<<<<< HEAD
      // KEY
      Object key = JSONUtil.getPrimitiveValue(json_key, key_class);

      // VALUE
      Object object = null;
      if (json_object.isNull(json_key)) {
        // Nothing...
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      // 키
      Object key = JSONUtil.getPrimitiveValue(json_key, key_class);

      // 값
      Object object = null;
      if (json_object.isNull(json_key)) {
        // 특별한 작업 없음
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      } else if (val_interfaces.contains(List.class)) {
        object = new ArrayList();
        readCollectionField(
            json_object.getJSONArray(json_key), (Collection) object, next_inner_classes);
      } else if (val_interfaces.contains(Set.class)) {
        object = new HashSet();
        readCollectionField(
            json_object.getJSONArray(json_key), (Collection) object, next_inner_classes);
      } else if (val_interfaces.contains(Map.class)) {
        object = new HashMap();
        readMapField(json_object.getJSONObject(json_key), (Map) object, next_inner_classes);
      } else {
        String json_string = json_object.getString(json_key);

        object = JSONUtil.getPrimitiveValue(json_string, val_class);
      }
      map.put(key, object);
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Read data from the given JSONArray and populate the given Collection
=======
   * 주어진 JSONArray에서 데이터를 읽어 Collection을 채웁니다.
>>>>>>> master
=======
   * 주어진 JSONArray에서 데이터를 읽어 Collection을 채웁니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param json_array
   * @param collection
   * @param inner_classes
   * @throws Exception
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  protected static void readCollectionField(
      final JSONArray json_array, final Collection collection, final Stack<Class> inner_classes)
      throws Exception {
<<<<<<< HEAD
<<<<<<< HEAD
    // We need to figure out what the inner type of the collection is
    // If it's a Collection or a Map, then we need to instantiate it before
    // we can call readFieldValue() again for it.
=======
    // 컬렉션의 내부 타입을 알아내야 합니다.
    // 내부 타입이 컬렉션 또는 Map이면 readFieldValue()를 다시 호출하기 전에 인스턴스를 생성해야 합니다.
>>>>>>> master
=======
    // 컬렉션의 내부 타입을 알아내야 합니다.
    // 내부 타입이 컬렉션 또는 Map이면 readFieldValue()를 다시 호출하기 전에 인스턴스를 생성해야 합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    Class inner_class = inner_classes.pop();
    Collection<Class<?>> inner_interfaces = ClassUtil.getInterfaces(inner_class);

    for (int i = 0, cnt = json_array.length(); i < cnt; i++) {
      final Stack<Class> next_inner_classes = new Stack<>();
      next_inner_classes.addAll(inner_classes);

      Object value = null;

<<<<<<< HEAD
<<<<<<< HEAD
      // Null
      if (json_array.isNull(i)) {
        value = null;
        // Lists
      } else if (inner_interfaces.contains(List.class)) {
        value = new ArrayList();
        readCollectionField(json_array.getJSONArray(i), (Collection) value, next_inner_classes);
        // Sets
      } else if (inner_interfaces.contains(Set.class)) {
        value = new HashSet();
        readCollectionField(json_array.getJSONArray(i), (Collection) value, next_inner_classes);
        // Maps
      } else if (inner_interfaces.contains(Map.class)) {
        value = new HashMap();
        readMapField(json_array.getJSONObject(i), (Map) value, next_inner_classes);
        // Values
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      // 값이 null인 경우
      if (json_array.isNull(i)) {
        value = null;
        // List
      } else if (inner_interfaces.contains(List.class)) {
        value = new ArrayList();
        readCollectionField(json_array.getJSONArray(i), (Collection) value, next_inner_classes);
        // Set
      } else if (inner_interfaces.contains(Set.class)) {
        value = new HashSet();
        readCollectionField(json_array.getJSONArray(i), (Collection) value, next_inner_classes);
        // Map
      } else if (inner_interfaces.contains(Map.class)) {
        value = new HashMap();
        readMapField(json_array.getJSONObject(i), (Map) value, next_inner_classes);
        // 일반 값
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      } else {
        String json_string = json_array.getString(i);
        value = JSONUtil.getPrimitiveValue(json_string, inner_class);
      }
      collection.add(value);
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
=======
   * JSONObject에서 지정된 필드 값을 읽어 객체에 설정합니다.
   *
>>>>>>> master
=======
   * JSONObject에서 지정된 필드 값을 읽어 객체에 설정합니다.
   *
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * @param json_object
   * @param json_key
   * @param field_handle
   * @param object
   * @throws Exception
   */
  @SuppressWarnings("rawtypes")
  public static void readFieldValue(
      final JSONObject json_object, final String json_key, Field field_handle, Object object)
      throws Exception {

    Class<?> field_class = field_handle.getType();
    Object field_object = field_handle.get(object);
    // String field_name = field_handle.getName();

<<<<<<< HEAD
<<<<<<< HEAD
    // Null
=======
    // 값이 null인 경우
>>>>>>> master
=======
    // 값이 null인 경우
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    if (json_object.isNull(json_key)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Field {} is null", json_key);
      }
      field_handle.set(object, null);

<<<<<<< HEAD
<<<<<<< HEAD
      // Collections
=======
      // 컬렉션
>>>>>>> master
=======
      // 컬렉션
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    } else if (ClassUtil.getInterfaces(field_class).contains(Collection.class)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Field {} is a collection", json_key);
      }

      Stack<Class> inner_classes = new Stack<>();
      inner_classes.addAll(ClassUtil.getGenericTypes(field_handle));
      Collections.reverse(inner_classes);

      JSONArray json_inner = json_object.getJSONArray(json_key);
      if (json_inner == null) {
        throw new JSONException("No array exists for '" + json_key + "'");
      }
      readCollectionField(json_inner, (Collection) field_object, inner_classes);

<<<<<<< HEAD
<<<<<<< HEAD
      // Maps
=======
      // Map
>>>>>>> master
=======
      // Map
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    } else if (field_object instanceof Map) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Field {} is a map", json_key);
      }

      Stack<Class> inner_classes = new Stack<>();
      inner_classes.addAll(ClassUtil.getGenericTypes(field_handle));
      Collections.reverse(inner_classes);

      JSONObject json_inner = json_object.getJSONObject(json_key);
      if (json_inner == null) {
        throw new JSONException("No object exists for '" + json_key + "'");
      }
      readMapField(json_inner, (Map) field_object, inner_classes);

<<<<<<< HEAD
<<<<<<< HEAD
      // Everything else...
=======
      // 그 외 모든 경우...
>>>>>>> master
=======
      // 그 외 모든 경우...
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    } else {
      Class explicit_field_class = JSONUtil.getClassForField(json_object, json_key);
      if (explicit_field_class != null) {
        field_class = explicit_field_class;
        if (LOG.isDebugEnabled()) {
          LOG.debug("Found explict field class {} for {}", field_class.getSimpleName(), json_key);
        }
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("Field {} is primitive type {}", json_key, field_class.getSimpleName());
      }
      Object value = JSONUtil.getPrimitiveValue(json_object.getString(json_key), field_class);
      field_handle.set(object, value);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Set field {} to '{}'", json_key, value);
      }
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * For the given list of Fields, load in the values from the JSON object into the current object
   * If ignore_missing is false, then JSONUtil will not throw an error if a field is missing
=======
   * 지정된 필드 리스트에 대해 JSONObject에서 값을 읽어 현재 객체에 설정합니다. ignore_missing이 false이면 필드가 없을 때 오류를 던지지 않습니다.
>>>>>>> master
=======
   * 지정된 필드 리스트에 대해 JSONObject에서 값을 읽어 현재 객체에 설정합니다. ignore_missing이 false이면 필드가 없을 때 오류를 던지지 않습니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param <E>
   * @param <T>
   * @param json_object
   * @param object
   * @param base_class
   * @param ignore_missing
   * @param fields
   * @throws JSONException
   */
  public static <E extends Enum<?>, T> void fieldsFromJSON(
      JSONObject json_object,
      T object,
      Class<? extends T> base_class,
      boolean ignore_missing,
      Field... fields)
      throws JSONException {
    for (Field field_handle : fields) {
      String json_key = field_handle.getName().toUpperCase();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Retreiving value for field '{}'", json_key);
      }

      if (!json_object.has(json_key)) {
        String msg =
            "JSONObject for "
                + base_class.getSimpleName()
                + " does not have key '"
                + json_key
                + "': "
                + CollectionUtil.list(json_object.keys());
        if (ignore_missing) {
          LOG.warn(msg);
          continue;
        } else {
          throw new JSONException(msg);
        }
      }

      try {
        readFieldValue(json_object, json_key, field_handle, object);
      } catch (Exception ex) {
        // System.err.println(field_class + ": " + ClassUtil.getSuperClasses(field_class));
        LOG.error(
            "Unable to deserialize field '{}' from {}", json_key, base_class.getSimpleName(), ex);
        throw new JSONException(ex);
      }
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return the class of a field if it was stored in the JSONObject along with the value If there is
   * no class information, then this will return null
=======
   * JSONObject에 필드와 함께 저장된 클래스 정보를 반환합니다. 클래스 정보가 없으면 null을 반환합니다.
>>>>>>> master
=======
   * JSONObject에 필드와 함께 저장된 클래스 정보를 반환합니다. 클래스 정보가 없으면 null을 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param json_object
   * @param json_key
   * @return
   * @throws JSONException
   */
  private static Class<?> getClassForField(JSONObject json_object, String json_key)
      throws JSONException {
    Class<?> field_class = null;
<<<<<<< HEAD
<<<<<<< HEAD
    // Check whether we also stored the class
=======
    // 클래스 정보도 저장했는지 확인합니다.
>>>>>>> master
=======
    // 클래스 정보도 저장했는지 확인합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    if (json_object.has(json_key + JSON_CLASS_SUFFIX)) {
      try {
        field_class = ClassUtil.getClass(json_object.getString(json_key + JSON_CLASS_SUFFIX));
      } catch (Exception ex) {
        LOG.error("Failed to include class for field '{}'", json_key, ex);
        throw new JSONException(ex);
      }
    }
    return (field_class);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return the proper serialization string for the given value
=======
   * 주어진 값을 직렬화 가능한 형태로 반환합니다.
>>>>>>> master
=======
   * 주어진 값을 직렬화 가능한 형태로 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param field_class
   * @param field_value
   * @return
   */
  private static Object makePrimitiveValue(Class<?> field_class, Object field_value) {
    Object value = null;

    if (field_class.equals(Class.class)) {
      value = ((Class<?>) field_value).getName();
      // JSONSerializable
    } else if (ClassUtil.getInterfaces(field_class).contains(JSONSerializable.class)) {
<<<<<<< HEAD
<<<<<<< HEAD
      // Just return the value back. The JSON library will take care of it
      //            System.err.println(field_class + ": " + field_value);
      value = field_value;
      // Everything else
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      // 그대로 반환합니다. JSON 라이브러리가 처리합니다.
      //            System.err.println(field_class + ": " + field_value);
      value = field_value;
      // 그 외 모든 경우
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    } else {
      value = field_value; // .toString();
    }
    return (value);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * For the given JSON string, figure out what kind of object it is and return it
=======
   * 주어진 JSON 문자열에서 타입을 파악하여 적절한 객체를 반환합니다.
>>>>>>> master
=======
   * 주어진 JSON 문자열에서 타입을 파악하여 적절한 객체를 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param json_value
   * @param field_class
   * @return
   */
  public static Object getPrimitiveValue(String json_value, Class<?> field_class) {
    Object value = null;

    if (field_class.equals(Class.class)) {
      value = ClassUtil.getClass(json_value);
      if (value == null) {
        throw new JSONException("Failed to get class from '" + json_value + "'");
      }
<<<<<<< HEAD
<<<<<<< HEAD
      // Enum
=======
      // Enum 타입
>>>>>>> master
=======
      // Enum 타입
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    } else if (field_class.isEnum()) {
      for (Object o : field_class.getEnumConstants()) {
        Enum<?> e = (Enum<?>) o;
        if (json_value.equals(e.name())) {
          return (e);
        }
      }
      throw new JSONException(
          "Invalid enum value '"
              + json_value
              + "': "
              + Arrays.toString(field_class.getEnumConstants()));
      // JSONSerializable
    } else if (ClassUtil.getInterfaces(field_class).contains(JSONSerializable.class)) {
      value = ClassUtil.newInstance(field_class, null, null);
      ((JSONSerializable) value).fromJSON(new JSONObject(json_value));
      // Boolean
    } else if (field_class.equals(Boolean.class) || field_class.equals(boolean.class)) {
<<<<<<< HEAD
<<<<<<< HEAD
      // We have to use field_class.equals() because the value may be null
=======
      // 값이 null일 수 있어서 field_class.equals()를 사용합니다.
>>>>>>> master
=======
      // 값이 null일 수 있어서 field_class.equals()를 사용합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      value = Boolean.parseBoolean(json_value);
      // Short
    } else if (field_class.equals(Short.class) || field_class.equals(short.class)) {
      value = Short.parseShort(json_value);
      // Integer
    } else if (field_class.equals(Integer.class) || field_class.equals(int.class)) {
      value = Integer.parseInt(json_value);
      // Long
    } else if (field_class.equals(Long.class) || field_class.equals(long.class)) {
      value = Long.parseLong(json_value);
      // Float
    } else if (field_class.equals(Float.class) || field_class.equals(float.class)) {
      value = Float.parseFloat(json_value);
      // Double
    } else if (field_class.equals(Double.class) || field_class.equals(double.class)) {
      value = Double.parseDouble(json_value);
      // String
    } else if (field_class.equals(String.class)) {
      value = json_value;
    }
    return (value);
  }
}
