/*
 * Copyright 2015 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 별도 합의가 없다면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다. 라이선스가 허용하는 범위 내에서만 사용하세요.
 *
 */

package com.oltpbenchmark.benchmarks.seats.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.oltpbenchmark.util.Histogram;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class TestCustomerIdIterable {

  final Random rand = new Random();
  final Histogram<Long> airport_max_customer_id = new Histogram<Long>();
  CustomerIdIterable customer_id_iterable;

  @Before
  public void setUp() throws Exception {
    for (long airport = 0; airport <= 285; airport++) {
      this.airport_max_customer_id.put(airport, rand.nextInt(100));
    } // FOR
    this.customer_id_iterable = new CustomerIdIterable(this.airport_max_customer_id);
  }

  /** 반복자 테스트 */
  @Test
  public void testIterator() throws Exception {
    Set<String> seen_ids = new HashSet<>();
    Histogram<Long> airport_ids = new Histogram<>();
    for (CustomerId c_id : this.customer_id_iterable) {
      assertNotNull(c_id);
      String encoded = c_id.encode();
      assertFalse(seen_ids.contains(encoded));
      seen_ids.add(encoded);
      airport_ids.put(c_id.getDepartAirportId());
    } // FOR
    assertEquals(this.airport_max_customer_id.getSampleCount(), seen_ids.size());
    assertEquals(this.airport_max_customer_id, airport_ids);
  }
}
