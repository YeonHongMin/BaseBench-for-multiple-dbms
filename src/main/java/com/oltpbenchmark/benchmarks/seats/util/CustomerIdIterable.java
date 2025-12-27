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

package com.oltpbenchmark.benchmarks.seats.util;

import com.oltpbenchmark.util.Histogram;
import java.util.Iterator;
import org.apache.commons.collections4.set.ListOrderedSet;

public class CustomerIdIterable implements Iterable<CustomerId> {
  private final Histogram<Long> airport_max_customer_id;
  private final ListOrderedSet<Long> airport_ids = new ListOrderedSet<>();
  private Long last_airport_id = null;
  private int last_id = -1;
  private long last_max_id = -1;

  public CustomerIdIterable(Histogram<Long> airport_max_customer_id) {
    this.airport_max_customer_id = airport_max_customer_id;
    this.airport_ids.addAll(airport_max_customer_id.values());
  }

  @Override
  public Iterator<CustomerId> iterator() {
    return new Iterator<CustomerId>() {
      @Override
      public boolean hasNext() {
        return (!CustomerIdIterable.this.airport_ids.isEmpty()
            || (last_id != -1 && last_id < last_max_id));
      }

      @Override
      public CustomerId next() {
        if (last_airport_id == null) {
          last_airport_id = airport_ids.remove(0);
          last_id = 0;
          last_max_id = airport_max_customer_id.get(last_airport_id);
        }
        CustomerId next_id = new CustomerId(last_id, last_airport_id);
        if (++last_id == last_max_id) {
          last_airport_id = null;
        }
        return next_id;
      }

      @Override
      public void remove() {
        // Not implemented
      }
    };
  }
}
