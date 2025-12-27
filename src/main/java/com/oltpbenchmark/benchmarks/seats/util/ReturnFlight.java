/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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
<<<<<<< HEAD
=======
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
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.benchmarks.seats.util;

import com.oltpbenchmark.benchmarks.seats.SEATSConstants;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;

public class ReturnFlight implements Comparable<ReturnFlight> {

  private final CustomerId customer_id;
  private final long return_airport_id;
  private final Timestamp return_date;

  public ReturnFlight(
      CustomerId customer_id, long return_airport_id, Timestamp flight_date, int return_days) {
    this.customer_id = customer_id;
    this.return_airport_id = return_airport_id;
    this.return_date = ReturnFlight.calculateReturnDate(flight_date, return_days);
  }

  /**
   * @param flight_date
   * @param return_days
   * @return
   */
  protected static Timestamp calculateReturnDate(Timestamp flight_date, int return_days) {

<<<<<<< HEAD
<<<<<<< HEAD
    // Round this to the start of the day
=======
    // 이것을 하루의 시작으로 반올림합니다.
>>>>>>> master
=======
    // Round this to the start of the day
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(
        flight_date.getTime() + (return_days * SEATSConstants.MILLISECONDS_PER_DAY));

    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);

    cal.clear();
    cal.set(year, month, day);
    return (new Timestamp(cal.getTime().getTime()));
  }

  /**
   * @return the customer_id
   */
  public CustomerId getCustomerId() {
    return customer_id;
  }

  /**
   * @return the return_airport_id
   */
  public long getReturnAirportId() {
    return return_airport_id;
  }

  /**
   * @return the return_time
   */
  public Timestamp getReturnDate() {
    return return_date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReturnFlight that = (ReturnFlight) o;
    return return_airport_id == that.return_airport_id
        && Objects.equals(customer_id, that.customer_id)
        && Objects.equals(return_date, that.return_date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customer_id, return_airport_id, return_date);
  }

  @Override
  public int compareTo(ReturnFlight o) {
    if (this.customer_id.equals(o.customer_id)
        && this.return_airport_id == o.return_airport_id
        && this.return_date.equals(o.return_date)) {
      return (0);
    }
<<<<<<< HEAD
<<<<<<< HEAD
    // Otherwise order by time
=======
    // 그렇지 않으면 시간순으로 정렬합니다.
>>>>>>> master
=======
    // Otherwise order by time
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    return (this.return_date.compareTo(o.return_date));
  }

  @Override
  public String toString() {
    return String.format(
        "ReturnFlight{%s,airport=%s,date=%s}",
        this.customer_id, this.return_airport_id, this.return_date);
  }
}
