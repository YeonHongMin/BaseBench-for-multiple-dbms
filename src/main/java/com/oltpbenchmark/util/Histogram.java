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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
<<<<<<< HEAD
<<<<<<< HEAD
 * A very nice and simple generic Histogram
 *
 * @author svelagap
 * @author pavlo
=======
 * 매우 단순하면서도 범용적인 히스토그램 구현체입니다.
 *
 * <p>작성자: svelagap 작성자: pavlo
>>>>>>> master
=======
 * 매우 단순하면서도 범용적인 히스토그램 구현체입니다.
 *
 * <p>작성자: svelagap 작성자: pavlo
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 */
public class Histogram<X extends Comparable<X>> implements JSONSerializable {
  private static final Logger LOG = LoggerFactory.getLogger(Histogram.class);

  private static final long serialVersionUID = 0L;

  private static final String MARKER = "*";
  private static final Integer MAX_CHARS = 80;
  private static final Integer MAX_VALUE_LENGTH = 80;

  public enum Members {
    VALUE_TYPE,
    HISTOGRAM,
    NUM_SAMPLES,
    KEEP_ZERO_ENTRIES,
  }

  protected final TreeMap<X, Integer> histogram = new TreeMap<>();
  protected int num_samples = 0;
  private transient boolean dirty = false;

<<<<<<< HEAD
<<<<<<< HEAD
  /** */
  protected transient Map<Object, String> debug_names;

  /**
   * The Min/Max values are the smallest/greatest values we have seen based on some natural ordering
   */
  // Note: marked as transient to avoid serialization warnings due to missing
  // explicit Serializable interface on base types like X == Integer
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  /** 디버그 이름 매핑 */
  protected transient Map<Object, String> debug_names;

  /** 최솟값/최댓값은 자연 순서에 따라 지금까지 관측한 가장 작은/큰 값입니다. */
  // 참고: 직렬화 경고를 피하려고 transient로 표시합니다.
  // X == Integer처럼 기본 타입은 Serializable을 명시하지 않습니다.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  protected transient Comparable<X> min_value;

  protected transient Comparable<X> max_value;

<<<<<<< HEAD
<<<<<<< HEAD
  /**
   * The Min/Max counts are the values that have the smallest/greatest number of occurences in the
   * histogram
   */
=======
  /** 최솟값/최댓값 카운트는 히스토그램에서 가장 적게/많이 발생한 항목의 수입니다. */
>>>>>>> master
=======
  /** 최솟값/최댓값 카운트는 히스토그램에서 가장 적게/많이 발생한 항목의 수입니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  protected int min_count = 0;

  protected ArrayList<X> min_count_values;
  protected int max_count = 0;
  protected ArrayList<X> max_count_values;

<<<<<<< HEAD
<<<<<<< HEAD
  /** A switchable flag that determines whether non-zero entries are kept or removed */
  protected boolean keep_zero_entries = false;

  /** Constructor */
  public Histogram() {
    // Nothing...
  }

  /**
   * Constructor
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  /** 0이 아닌 항목을 유지할지 제거할지를 결정하는 플래그입니다. */
  protected boolean keep_zero_entries = false;

  /** 기본 생성자 */
  public Histogram() {
    // 특별한 초기화 없음
  }

  /**
   * keepZeroEntries 설정으로 히스토그램을 생성합니다.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param keepZeroEntries
   */
  public Histogram(boolean keepZeroEntries) {
    this.keep_zero_entries = keepZeroEntries;
  }

  public boolean hasDebugLabels() {
    return (this.debug_names != null && !this.debug_names.isEmpty());
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Set whether this histogram is allowed to retain zero count entries If the flag switches from
   * true to false, then all zero count entries will be removed Default is false
=======
   * 히스토그램이 0 카운트 항목을 유지할지 여부를 설정합니다. true에서 false로 전환되면 모든 0 카운트 항목을 제거합니다. 기본값은 false입니다.
>>>>>>> master
=======
   * 히스토그램이 0 카운트 항목을 유지할지 여부를 설정합니다. true에서 false로 전환되면 모든 0 카운트 항목을 제거합니다. 기본값은 false입니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param flag
   */
  public void setKeepZeroEntries(boolean flag) {
<<<<<<< HEAD
<<<<<<< HEAD
    // When this option is disabled, we need to remove all of the zeroed entries
=======
    // 이 옵션이 꺼지면 0으로 표시된 항목을 모두 제거해야 합니다.
>>>>>>> master
=======
    // 이 옵션이 꺼지면 0으로 표시된 항목을 모두 제거해야 합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    if (!flag && this.keep_zero_entries) {
      synchronized (this) {
        Iterator<X> it = this.histogram.keySet().iterator();
        int ctr = 0;
        while (it.hasNext()) {
          X key = it.next();
          if (this.histogram.get(key) == 0) {
            it.remove();
            ctr++;
            this.dirty = true;
          }
        }
        if (ctr > 0) {
          LOG.debug("Removed {} zero entries from histogram", ctr);
        }
      }
    }
    this.keep_zero_entries = flag;
  }

  public boolean isZeroEntriesEnabled() {
    return this.keep_zero_entries;
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * The main method that updates a value in the histogram with a given sample count This should be
   * called by one of the public interface methods that are synchronized This method is not
   * synchronized on purpose for performance
=======
   * 주어진 샘플 수로 히스토그램에 값을 업데이트합니다. 동기화된 퍼블릭 인터페이스 메서드에서 호출되며 성능을 위해 이 메서드는 의도적으로 동기화되지 않습니다.
>>>>>>> master
=======
   * 주어진 샘플 수로 히스토그램에 값을 업데이트합니다. 동기화된 퍼블릭 인터페이스 메서드에서 호출되며 성능을 위해 이 메서드는 의도적으로 동기화되지 않습니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param value
   * @param count
   */
  private void _put(X value, int count) {
    if (value == null) {
      return;
    }
    this.num_samples += count;

<<<<<<< HEAD
<<<<<<< HEAD
    // If we already have this value in our histogram, then add the new count
    // to its existing total
=======
    // 이미 히스토그램에 값이 있으면 기존 합계에 새 카운트를 더합니다.
>>>>>>> master
=======
    // 이미 히스토그램에 값이 있으면 기존 합계에 새 카운트를 더합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    if (this.histogram.containsKey(value)) {
      count += this.histogram.get(value);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    // If the new count is zero, then completely remove it if we're not allowed to have zero entries
=======
    // 새 카운트가 0이고 0 항목을 유지하지 않으면 완전히 제거합니다.
>>>>>>> master
=======
    // 새 카운트가 0이고 0 항목을 유지하지 않으면 완전히 제거합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    if (count == 0 && !this.keep_zero_entries) {
      this.histogram.remove(value);
    } else {
      this.histogram.put(value, count);
    }
    this.dirty = true;
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /**
   * Recalculate the min/max count value sets Since this is expensive, this should only be done
   * whenever that information is needed
   */
=======
  /** 최솟값/최댓값 관련 정보를 다시 계산합니다. 비용이 크므로 해당 정보가 필요할 때만 수행합니다. */
>>>>>>> master
=======
  /** 최솟값/최댓값 관련 정보를 다시 계산합니다. 비용이 크므로 해당 정보가 필요할 때만 수행합니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  private synchronized void calculateInternalValues() {
    if (!this.dirty) {
      return;
    }

<<<<<<< HEAD
<<<<<<< HEAD
    // New Min/Max Counts
    // The reason we have to loop through and check every time is that our
    // value may be the current min/max count and thus it may or may not still
    // be after the count is changed
=======
    // 새로운 최솟값/최댓값 카운트를 계산합니다.
    // 매 반복마다 확인해야 하는 이유는 값이 현재 최솟값/최댓값일 수 있고,
    // 카운트 변경 여부에 따라 계속 유지되지 않을 수 있기 때문입니다.
>>>>>>> master
=======
    // 새로운 최솟값/최댓값 카운트를 계산합니다.
    // 매 반복마다 확인해야 하는 이유는 값이 현재 최솟값/최댓값일 수 있고,
    // 카운트 변경 여부에 따라 계속 유지되지 않을 수 있기 때문입니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    this.max_count = 0;
    this.min_count = Integer.MAX_VALUE;
    this.min_value = null;
    this.max_value = null;

    if (this.min_count_values == null) {
      this.min_count_values = new ArrayList<>();
    }
    if (this.max_count_values == null) {
      this.max_count_values = new ArrayList<>();
    }

    for (Entry<X, Integer> e : this.histogram.entrySet()) {
      X value = e.getKey();
      int cnt = e.getValue();

<<<<<<< HEAD
<<<<<<< HEAD
      // Is this value the new min/max values?
=======
      // 이 값이 새로운 최솟값/최댓값인지 확인합니다.
>>>>>>> master
=======
      // 이 값이 새로운 최솟값/최댓값인지 확인합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      if (this.min_value == null || this.min_value.compareTo(value) > 0) {
        this.min_value = value;
      }

      if (this.max_value == null || this.max_value.compareTo(value) < 0) {
        this.max_value = value;
      }

      if (cnt <= this.min_count) {
        if (cnt < this.min_count) {
          this.min_count_values.clear();
        }
        this.min_count_values.add(value);
        this.min_count = cnt;
      }

      if (cnt >= this.max_count) {
        if (cnt > this.max_count) {
          this.max_count_values.clear();
        }
        this.max_count_values.add(value);
        this.max_count = cnt;
      }
    }
    this.dirty = false;
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Get the number of samples entered into the histogram using the put methods
=======
   * put 메서드로 기록된 샘플 수를 반환합니다.
>>>>>>> master
=======
   * put 메서드로 기록된 샘플 수를 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  public int getSampleCount() {
    return (this.num_samples);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Get the number of unique values entered into the histogram
=======
   * 히스토그램에 기록된 고유 값의 수를 반환합니다.
>>>>>>> master
=======
   * 히스토그램에 기록된 고유 값의 수를 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  public int getValueCount() {
    return (this.histogram.values().size());
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Get the smallest value entered into the histogram This assumes that the values implement the
   * Comparable interface
=======
   * 히스토그램에 기록된 가장 작은 값을 반환합니다. 값은 Comparable 인터페이스를 구현한다고 가정합니다.
>>>>>>> master
=======
   * 히스토그램에 기록된 가장 작은 값을 반환합니다. 값은 Comparable 인터페이스를 구현한다고 가정합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public X getMinValue() {
    this.calculateInternalValues();
    return ((X) this.min_value);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Get the largest value entered into the histogram This assumes that the values implement the
   * Comparable interface
=======
   * 히스토그램에 기록된 가장 큰 값을 반환합니다. 값은 Comparable 인터페이스를 구현한다고 가정합니다.
>>>>>>> master
=======
   * 히스토그램에 기록된 가장 큰 값을 반환합니다. 값은 Comparable 인터페이스를 구현한다고 가정합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public X getMaxValue() {
    this.calculateInternalValues();
    return ((X) this.max_value);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return the number of samples for the value with the smallest number of samples in the histogram
=======
   * 히스토그램에서 가장 적은 샘플 수를 가진 값의 샘플 수를 반환합니다.
>>>>>>> master
=======
   * 히스토그램에서 가장 적은 샘플 수를 가진 값의 샘플 수를 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  public int getMinCount() {
    this.calculateInternalValues();
    return (this.min_count);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return the set values with the smallest number of samples
=======
   * 샘플 수가 가장 작은 값들의 집합을 반환합니다.
>>>>>>> master
=======
   * 샘플 수가 가장 작은 값들의 집합을 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  public Collection<X> getMinCountValues() {
    this.calculateInternalValues();
    return (this.min_count_values);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return the number of samples for the value with the greatest number of samples in the histogram
=======
   * 히스토그램에서 가장 많은 샘플 수를 가진 값의 샘플 수를 반환합니다.
>>>>>>> master
=======
   * 히스토그램에서 가장 많은 샘플 수를 가진 값의 샘플 수를 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  public int getMaxCount() {
    this.calculateInternalValues();
    return (this.max_count);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return the set values with the greatest number of samples
=======
   * 샘플 수가 가장 많은 값들의 집합을 반환합니다.
>>>>>>> master
=======
   * 샘플 수가 가장 많은 값들의 집합을 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  public Collection<X> getMaxCountValues() {
    this.calculateInternalValues();
    return (this.max_count_values);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Return all the values stored in the histogram
=======
   * 히스토그램에 저장된 모든 값을 반환합니다.
>>>>>>> master
=======
   * 히스토그램에 저장된 모든 값을 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  public Collection<X> values() {
    return (Collections.unmodifiableCollection(this.histogram.keySet()));
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** Reset the histogram's internal data */
=======
  /** 히스토그램의 내부 데이터를 초기화합니다. */
>>>>>>> master
=======
  /** 히스토그램의 내부 데이터를 초기화합니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public synchronized void clear() {
    this.histogram.clear();
    this.num_samples = 0;
    this.min_count = 0;
    if (this.min_count_values != null) {
      this.min_count_values.clear();
    }
    this.min_value = null;
    this.max_count = 0;
    if (this.max_count_values != null) {
      this.max_count_values.clear();
    }
    this.max_value = null;

    this.dirty = true;
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /**
   * Clear all the values stored in the histogram. The keys are only kept if KeepZeroEntries is
   * enabled, otherwise it does the same thing as clear()
   */
=======
  /** 히스토그램에 저장된 모든 값을 지웁니다. KeepZeroEntries가 활성화되면 키만 유지하고, 그렇지 않으면 clear()와 동일하게 동작합니다. */
>>>>>>> master
=======
  /** 히스토그램에 저장된 모든 값을 지웁니다. KeepZeroEntries가 활성화되면 키만 유지하고, 그렇지 않으면 clear()와 동일하게 동작합니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public synchronized void clearValues() {
    if (this.keep_zero_entries) {
      for (Entry<X, Integer> e : this.histogram.entrySet()) {
        this.histogram.put(e.getKey(), 0);
<<<<<<< HEAD
<<<<<<< HEAD
      } // FOR
=======
      } // 반복 종료
>>>>>>> master
=======
      } // 반복 종료
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      this.num_samples = 0;
      this.min_count = 0;
      if (this.min_count_values != null) this.min_count_values.clear();
      this.min_value = null;
      this.max_count = 0;
      if (this.max_count_values != null) this.max_count_values.clear();
      this.max_value = null;
    } else {
      this.clear();
    }
    this.dirty = true;
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
=======
   * 히스토그램이 비어 있는지 확인합니다.
   *
>>>>>>> master
=======
   * 히스토그램이 비어 있는지 확인합니다.
   *
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * @return
   */
  public boolean isEmpty() {
    return (this.histogram.isEmpty());
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Increments the number of occurrences of this particular value i
   *
   * @param value the value to be added to the histogram
=======
   * 특정 값의 발생 횟수를 i만큼 증가시킵니다.
   *
   * @param value 히스토그램에 추가할 값
>>>>>>> master
=======
   * 특정 값의 발생 횟수를 i만큼 증가시킵니다.
   *
   * @param value 히스토그램에 추가할 값
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   */
  public synchronized void put(X value, int i) {
    this._put(value, i);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Set the number of occurrences of this particular value i
   *
   * @param value the value to be added to the histogram
=======
   * 특정 값의 발생 횟수를 설정합니다.
   *
   * @param value 히스토그램에 추가할 값
>>>>>>> master
=======
   * 특정 값의 발생 횟수를 설정합니다.
   *
   * @param value 히스토그램에 추가할 값
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   */
  public synchronized void set(X value, int i) {
    Integer orig = this.get(value);
    if (orig != null && orig != i) {
      i = (orig > i ? -1 * (orig - i) : i - orig);
    }
    this._put(value, i);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Increments the number of occurrences of this particular value i
   *
   * @param value the value to be added to the histogram
=======
   * 특정 값의 발생 횟수를 1만큼 증가시킵니다.
   *
   * @param value 히스토그램에 추가할 값
>>>>>>> master
=======
   * 특정 값의 발생 횟수를 1만큼 증가시킵니다.
   *
   * @param value 히스토그램에 추가할 값
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   */
  public synchronized void put(X value) {
    this._put(value, 1);
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** Increment all values in the histogram by one */
=======
  /** 히스토그램의 모든 값을 1씩 증가시킵니다. */
>>>>>>> master
=======
  /** 히스토그램의 모든 값을 1씩 증가시킵니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public void putAll() {
    this.putAll(this.histogram.keySet(), 1);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Increment multiple values by one
=======
   * 여러 값의 발생 횟수를 1씩 증가시킵니다.
>>>>>>> master
=======
   * 여러 값의 발생 횟수를 1씩 증가시킵니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param values
   */
  public void putAll(Collection<X> values) {
    this.putAll(values, 1);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Increment multiple values by the given count
=======
   * 여러 값의 발생 횟수를 주어진 count만큼 증가시킵니다.
>>>>>>> master
=======
   * 여러 값의 발생 횟수를 주어진 count만큼 증가시킵니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param values
   * @param count
   */
  public synchronized void putAll(Collection<X> values, int count) {
    for (X v : values) {
      this._put(v, count);
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Add all the entries from the provided Histogram into this objects totals
=======
   * 다른 히스토그램의 모든 항목을 현재 히스토그램에 합산합니다.
>>>>>>> master
=======
   * 다른 히스토그램의 모든 항목을 현재 히스토그램에 합산합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param other
   */
  public synchronized void putHistogram(Histogram<X> other) {
    for (Entry<X, Integer> e : other.histogram.entrySet()) {
      if (e.getValue() > 0) {
        this._put(e.getKey(), e.getValue());
      }
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Remove the entire count for the given value
=======
   * 주어진 값의 전체 카운트를 제거합니다.
>>>>>>> master
=======
   * 주어진 값의 전체 카운트를 제거합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param value
   */
  public synchronized void removeAll(X value) {
    Integer cnt = this.histogram.get(value);
    if (cnt != null && cnt > 0) {
      this._put(value, cnt * -1);
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Returns the current count for the given value If the value was never entered into the
   * histogram, then the count will be null
=======
   * 지정된 값의 현재 카운트를 반환합니다. 값이 히스토그램에 존재하지 않으면 null을 반환합니다.
>>>>>>> master
=======
   * 지정된 값의 현재 카운트를 반환합니다. 값이 히스토그램에 존재하지 않으면 null을 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param value
   * @return
   */
  public Integer get(X value) {
    return (histogram.get(value));
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Returns the current count for the given value. If that value was nevered entered in the
   * histogram, then the value returned will be value_if_null
=======
   * 지정된 값의 카운트를 반환하며, 값이 없으면 value_if_null을 반환합니다.
>>>>>>> master
=======
   * 지정된 값의 카운트를 반환하며, 값이 없으면 value_if_null을 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param value
   * @param value_if_null
   * @return
   */
  public int get(X value, int value_if_null) {
    Integer count = histogram.get(value);
    return (count == null ? value_if_null : count);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Returns true if this histogram contains the specified key.
=======
   * 히스토그램에 지정된 키가 있는지 확인합니다.
>>>>>>> master
=======
   * 히스토그램에 지정된 키가 있는지 확인합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param value
   * @return
   */
  public boolean contains(X value) {
    return (this.histogram.containsKey(value));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Histogram<?> histogram1 = (Histogram<?>) o;
    return Objects.equals(histogram, histogram1.histogram);
  }

  @Override
  public int hashCode() {
    return Objects.hash(histogram);
  }

  // ----------------------------------------------------------------------------
<<<<<<< HEAD
<<<<<<< HEAD
  // DEBUG METHODS
  // ----------------------------------------------------------------------------

  /** Histogram Pretty Print */
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  // 디버그 메서드
  // ----------------------------------------------------------------------------

  /** 히스토그램을 보기 좋게 문자열로 반환합니다. */
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public String toString() {
    return (this.toString(MAX_CHARS, MAX_VALUE_LENGTH));
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Histogram Pretty Print
=======
   * 히스토그램을 보기 좋게 문자열로 반환합니다.
>>>>>>> master
=======
   * 히스토그램을 보기 좋게 문자열로 반환합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param max_chars
   * @param max_length
   * @return
   */
  public synchronized String toString(Integer max_chars, Integer max_length) {
    StringBuilder s = new StringBuilder();
    if (max_length == null) {
      max_length = MAX_VALUE_LENGTH;
    }

    this.calculateInternalValues();

<<<<<<< HEAD
<<<<<<< HEAD
    // Figure out the max size of the counts
=======
    // 카운트 문자열의 최대 길이를 계산합니다.
>>>>>>> master
=======
    // 카운트 문자열의 최대 길이를 계산합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    int max_ctr_length = 4;
    for (Integer ctr : this.histogram.values()) {
      max_ctr_length = Math.max(max_ctr_length, ctr.toString().length());
    }

<<<<<<< HEAD
<<<<<<< HEAD
    // Don't let anything go longer than MAX_VALUE_LENGTH chars
=======
    // MAX_VALUE_LENGTH를 넘기지 않도록 합니다.
>>>>>>> master
=======
    // MAX_VALUE_LENGTH를 넘기지 않도록 합니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    String f = "%-" + max_length + "s [%" + max_ctr_length + "d] ";
    boolean first = true;
    boolean has_labels = this.hasDebugLabels();
    for (Object value : this.histogram.keySet()) {
      if (!first) {
        s.append("\n");
      }
      String str = null;
      if (has_labels) {
        str = this.debug_names.get(value);
      }
      if (str == null) {
        str = (value != null ? value.toString() : "null");
      }
      int value_str_len = str.length();
      if (value_str_len > max_length) {
        str = str.substring(0, max_length - 3) + "...";
      }

      int cnt = (value != null ? this.histogram.get(value) : 0);
      int chars = (int) ((cnt / (double) this.max_count) * max_chars);
      s.append(String.format(f, str, cnt));
      for (int i = 0; i < chars; i++) {
        s.append(MARKER);
      }
      first = false;
    }
    if (this.histogram.isEmpty()) {
      s.append("<EMPTY>");
    }
    return (s.toString());
  }

  // ----------------------------------------------------------------------------
<<<<<<< HEAD
<<<<<<< HEAD
  // SERIALIZATION METHODS
=======
  // 직렬화 메서드
>>>>>>> master
=======
  // 직렬화 메서드
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  // ----------------------------------------------------------------------------

  @Override
  public void load(String input_path) throws IOException {
    JSONUtil.load(this, input_path);
  }

  @Override
  public void save(String output_path) throws IOException {
    JSONUtil.save(this, output_path);
  }

  @Override
  public String toJSONString() {
    return (JSONUtil.toJSONString(this));
  }

  @Override
  public void toJSON(JSONStringer stringer) throws JSONException {
    Class<?> value_type = null;
    for (Members element : Histogram.Members.values()) {
      if (element == Histogram.Members.VALUE_TYPE) {
        continue;
      }
      try {
        Field field = Histogram.class.getDeclaredField(element.toString().toLowerCase());
        if (element == Members.HISTOGRAM) {
          stringer.key(Members.HISTOGRAM.name()).object();
          for (Object value : this.histogram.keySet()) {
            if (value != null && value_type == null) {
              value_type = value.getClass();
            }
            stringer.key(value.toString()).value(this.histogram.get(value));
          }
          stringer.endObject();
        } else if (element == Members.KEEP_ZERO_ENTRIES) {
          if (this.keep_zero_entries) {
            stringer.key(element.name()).value(this.keep_zero_entries);
          }
        } else {
          stringer.key(element.name()).value(field.get(this));
        }
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        System.exit(1);
      }
    }
    if (value_type != null) {
      stringer.key(Histogram.Members.VALUE_TYPE.name()).value(value_type.getCanonicalName());
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void fromJSON(JSONObject object) throws JSONException {
    if (object.has(Members.KEEP_ZERO_ENTRIES.name())) {
      this.setKeepZeroEntries(object.getBoolean(Members.KEEP_ZERO_ENTRIES.name()));
    }
    Class<?> value_type = null;
    if (object.has(Members.VALUE_TYPE.name())) {
      String className = object.getString(Members.VALUE_TYPE.name());
      value_type = ClassUtil.getClass(className);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    // This code sucks ass...
=======
    // 이 코드는 다소 난잡합니다...
>>>>>>> master
=======
    // 이 코드는 다소 난잡합니다...
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    for (Members element : Histogram.Members.values()) {
      if (element == Members.KEEP_ZERO_ENTRIES || element == Members.VALUE_TYPE) {
        continue;
      }
      try {
        String field_name = element.toString().toLowerCase();
        Field field = Histogram.class.getDeclaredField(field_name);
        if (element == Members.HISTOGRAM) {
          JSONObject jsonObject = object.getJSONObject(Members.HISTOGRAM.name());
          Iterator<String> keys = jsonObject.keys();
          while (keys.hasNext()) {
            String key_name = keys.next();

            X key_value = (X) JSONUtil.getPrimitiveValue(key_name, value_type);
            int count = jsonObject.getInt(key_name);
            this.histogram.put(key_value, count);
          }
        } else {
          field.set(this, object.getInt(element.name()));
        }
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        System.exit(1);
      }
    }

    this.dirty = true;
    this.calculateInternalValues();
  }
}
