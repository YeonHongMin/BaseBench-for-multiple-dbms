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

import com.oltpbenchmark.benchmarks.seats.SEATSConstants;
import java.sql.Timestamp;
import java.util.Calendar;
import org.junit.Before;
import org.junit.Test;

public class TestFlightId {

  private final long[] base_ids = {111, 222, 333};
  private final long[] depart_airport_ids = {444, 555, 666};
  private final long[] arrive_airport_ids = {777, 888, 999};
  private final int[] flight_offset_days = {1, 2, 4, 8};
  private final Timestamp[] flight_dates = new Timestamp[this.flight_offset_days.length];
  private Timestamp start_date;

  @Before
  public void setUp() throws Exception {
    this.start_date = new Timestamp(Calendar.getInstance().getTime().getTime());
    for (int i = 0; i < this.flight_dates.length; i++) {
      int day = this.flight_offset_days[i];
      this.flight_dates[i] =
          new Timestamp(this.start_date.getTime() + (day * SEATSConstants.MILLISECONDS_PER_DAY));
    } // FOR
  }

  /** FlightId 테스트 */
  @Test
  public void testFlightId() {
    for (long base_id : this.base_ids) {
      for (long depart_airport_id : this.depart_airport_ids) {
        for (long arrive_airport_id : this.arrive_airport_ids) {
          for (Timestamp flight_date : this.flight_dates) {
            FlightId flight_id =
                new FlightId(
                    base_id, depart_airport_id, arrive_airport_id, this.start_date, flight_date);
            assertNotNull(flight_id);
            assertEquals(base_id, flight_id.getAirlineId());
            assertEquals(depart_airport_id, flight_id.getDepartAirportId());
            assertEquals(arrive_airport_id, flight_id.getArriveAirportId());
            assertEquals(flight_date, flight_id.getDepartDateAsTimestamp(this.start_date));
          } // FOR (time_code)
        } // FOR (arrive_airport_id)
      } // FOR (depart_airport_id)
    } // FOR (base_ids)
  }

  /** FlightId 인코딩 테스트 */
  @Test
  public void testFlightIdEncode() {
    for (long base_id : this.base_ids) {
      for (long depart_airport_id : this.depart_airport_ids) {
        for (long arrive_airport_id : this.arrive_airport_ids) {
          for (Timestamp flight_date : this.flight_dates) {
            String encoded =
                new FlightId(
                        base_id, depart_airport_id, arrive_airport_id, this.start_date, flight_date)
                    .encode();

            FlightId flight_id = new FlightId(encoded);
            assertNotNull(flight_id);
            assertEquals(base_id, flight_id.getAirlineId());
            assertEquals(depart_airport_id, flight_id.getDepartAirportId());
            assertEquals(arrive_airport_id, flight_id.getArriveAirportId());
            assertEquals(flight_date, flight_id.getDepartDateAsTimestamp(this.start_date));
          } // FOR (time_code)
        } // FOR (arrive_airport_id)
      } // FOR (depart_airport_id)
    } // FOR (base_ids)
  }

  /** FlightId 디코딩 테스트 */
  @Test
  public void testFlightIdDecode() {
    for (long base_id : this.base_ids) {
      for (long depart_airport_id : this.depart_airport_ids) {
        for (long arrive_airport_id : this.arrive_airport_ids) {
          for (Timestamp flight_date : this.flight_dates) {
            String[] values = {
              String.valueOf(base_id),
              String.valueOf(depart_airport_id),
              String.valueOf(arrive_airport_id),
              String.valueOf(FlightId.calculateFlightDate(this.start_date, flight_date))
            };
            String encoded =
                new FlightId(
                        base_id, depart_airport_id, arrive_airport_id, this.start_date, flight_date)
                    .encode();

            String[] new_values = new FlightId(encoded).toArray();
            assertEquals(values.length, new_values.length);
            for (int i = 0; i < new_values.length; i++) {
              assertEquals(values[i], new_values[i]);
            } // FOR
          } // FOR (time_code)
        } // FOR (arrive_airport_id)
      } // FOR (depart_airport_id)
    } // FOR (base_ids)
  }
}
