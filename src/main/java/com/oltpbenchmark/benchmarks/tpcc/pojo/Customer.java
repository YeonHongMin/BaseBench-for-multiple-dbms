/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한과 조건을 준수해 주세요.
 *
 */

package com.oltpbenchmark.benchmarks.tpcc.pojo;

import java.sql.Timestamp;
import java.util.Objects;

public class Customer {

  public int c_id;
  public int c_d_id;
  public int c_w_id;
  public int c_payment_cnt;
  public int c_delivery_cnt;
  public Timestamp c_since;
  public float c_discount;
  public float c_credit_lim;
  public float c_balance;
  public float c_ytd_payment;
  public String c_credit;
  public String c_last;
  public String c_first;
  public String c_street_1;
  public String c_street_2;
  public String c_city;
  public String c_state;
  public String c_zip;
  public String c_phone;
  public String c_middle;
  public String c_data;

  @Override
  public String toString() {
    return ("\n***************** Customer ********************"
        + "\n*           c_id = "
        + c_id
        + "\n*         c_d_id = "
        + c_d_id
        + "\n*         c_w_id = "
        + c_w_id
        + "\n*     c_discount = "
        + c_discount
        + "\n*       c_credit = "
        + c_credit
        + "\n*         c_last = "
        + c_last
        + "\n*        c_first = "
        + c_first
        + "\n*   c_credit_lim = "
        + c_credit_lim
        + "\n*      c_balance = "
        + c_balance
        + "\n*  c_ytd_payment = "
        + c_ytd_payment
        + "\n*  c_payment_cnt = "
        + c_payment_cnt
        + "\n* c_delivery_cnt = "
        + c_delivery_cnt
        + "\n*     c_street_1 = "
        + c_street_1
        + "\n*     c_street_2 = "
        + c_street_2
        + "\n*         c_city = "
        + c_city
        + "\n*        c_state = "
        + c_state
        + "\n*          c_zip = "
        + c_zip
        + "\n*        c_phone = "
        + c_phone
        + "\n*        c_since = "
        + c_since
        + "\n*       c_middle = "
        + c_middle
        + "\n*         c_data = "
        + c_data
        + "\n**********************************************");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Customer customer = (Customer) o;
    return c_id == customer.c_id
        && c_d_id == customer.c_d_id
        && c_w_id == customer.c_w_id
        && c_payment_cnt == customer.c_payment_cnt
        && c_delivery_cnt == customer.c_delivery_cnt
        && Float.compare(customer.c_discount, c_discount) == 0
        && Float.compare(customer.c_credit_lim, c_credit_lim) == 0
        && Float.compare(customer.c_balance, c_balance) == 0
        && Float.compare(customer.c_ytd_payment, c_ytd_payment) == 0
        && Objects.equals(c_since, customer.c_since)
        && Objects.equals(c_credit, customer.c_credit)
        && Objects.equals(c_last, customer.c_last)
        && Objects.equals(c_first, customer.c_first)
        && Objects.equals(c_street_1, customer.c_street_1)
        && Objects.equals(c_street_2, customer.c_street_2)
        && Objects.equals(c_city, customer.c_city)
        && Objects.equals(c_state, customer.c_state)
        && Objects.equals(c_zip, customer.c_zip)
        && Objects.equals(c_phone, customer.c_phone)
        && Objects.equals(c_middle, customer.c_middle)
        && Objects.equals(c_data, customer.c_data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        c_id,
        c_d_id,
        c_w_id,
        c_payment_cnt,
        c_delivery_cnt,
        c_since,
        c_discount,
        c_credit_lim,
        c_balance,
        c_ytd_payment,
        c_credit,
        c_last,
        c_first,
        c_street_1,
        c_street_2,
        c_city,
        c_state,
        c_zip,
        c_phone,
        c_middle,
        c_data);
  }
}
