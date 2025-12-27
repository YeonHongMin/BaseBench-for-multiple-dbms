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
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestCustomerId {

  private final long[] base_ids = {66666, 77777, 88888};
  private final long[] airport_ids = {123, 1234, 12345};

  /** testCustomerId */
  @Test
  public void testCustomerId() {
    for (long base_id : this.base_ids) {
      for (long airport_id : this.airport_ids) {
        CustomerId customer_id = new CustomerId((int) base_id, airport_id);
        assertNotNull(customer_id);
        assertEquals(base_id, customer_id.getId());
        assertEquals(airport_id, customer_id.getDepartAirportId());
      } // FOR
    } // FOR
  }

  /** testCustomerIdEncode */
  @Test
  public void testCustomerIdEncode() {
    for (long base_id : this.base_ids) {
      for (long airport_id : this.airport_ids) {
        String encoded = new CustomerId((int) base_id, airport_id).encode();
        //                System.err.println("base_id=" + base_id);
        //                System.err.println("airport_id=" + airport_id);
        //                System.err.println("encodd=" + encoded);
        //                System.exit(1);

        CustomerId customer_id = new CustomerId(encoded);
        assertNotNull(customer_id);
        assertEquals(base_id, customer_id.getId());
        assertEquals(airport_id, customer_id.getDepartAirportId());
      } // FOR
    } // FOR
  }

  /** testCustomerIdDecode */
  @Test
  public void testCustomerIdDecode() {
    for (long base_id : this.base_ids) {
      for (long airport_id : this.airport_ids) {
        String[] values = {String.valueOf(base_id), String.valueOf(airport_id)};
        String encoded = new CustomerId((int) base_id, airport_id).encode();

        String[] new_values = new CustomerId(encoded).toArray();
        assertEquals(values.length, new_values.length);
        for (int i = 0; i < new_values.length; i++) {
          assertEquals(values[i], new_values[i]);
        } // FOR
      } // FOR
    } // FOR
  }
}
