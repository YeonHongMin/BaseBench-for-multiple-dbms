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
>>>>>>> master
 */

package com.oltpbenchmark.benchmarks.seats.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import org.junit.Before;
import org.junit.Test;

public class TestReturnFlight {

  private final int customer_base_id = 1000;
  private final long depart_airport_id = 9999;
  private final int[] return_days = {1, 5, 14};

  private Timestamp flight_date;
  private CustomerId customer_id;

  @Before
  public void setUp() throws Exception {
    this.customer_id = new CustomerId(this.customer_base_id, this.depart_airport_id);
    assertNotNull(this.customer_id);
    this.flight_date = new Timestamp(System.currentTimeMillis());
    assertNotNull(this.flight_date);
  }

<<<<<<< HEAD
  /** testReturnFlight */
=======
  /** ReturnFlight 테스트 */
>>>>>>> master
  @Test
  public void testReturnFlight() {
    for (int return_day : this.return_days) {
      ReturnFlight return_flight =
          new ReturnFlight(this.customer_id, this.depart_airport_id, this.flight_date, return_day);
      assertNotNull(return_flight);
      assertEquals(this.customer_id, return_flight.getCustomerId());
      assertEquals(this.depart_airport_id, return_flight.getReturnAirportId());
      assertTrue(this.flight_date.getTime() < return_flight.getReturnDate().getTime());
    } // FOR
  }

<<<<<<< HEAD
  /** testCalculateReturnDate */
=======
  /** 반환 날짜 계산 테스트 */
>>>>>>> master
  @Test
  public void testCalculateReturnDate() {
    for (int return_day : this.return_days) {
      Timestamp return_flight_date = ReturnFlight.calculateReturnDate(this.flight_date, return_day);
      assertNotNull(return_flight_date);
      assertTrue(this.flight_date.getTime() < return_flight_date.getTime());
    } // FOR
  }
}
