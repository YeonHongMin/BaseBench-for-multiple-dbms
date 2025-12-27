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

public class OrderLine {

  public int ol_w_id;
  public int ol_d_id;
  public int ol_o_id;
  public int ol_number;
  public int ol_i_id;
  public int ol_supply_w_id;
  public int ol_quantity;
  public Timestamp ol_delivery_d;
  public float ol_amount;
  public String ol_dist_info;

  @Override
  public String toString() {
    return ("\n***************** OrderLine ********************"
        + "\n*        ol_w_id = "
        + ol_w_id
        + "\n*        ol_d_id = "
        + ol_d_id
        + "\n*        ol_o_id = "
        + ol_o_id
        + "\n*      ol_number = "
        + ol_number
        + "\n*        ol_i_id = "
        + ol_i_id
        + "\n*  ol_delivery_d = "
        + ol_delivery_d
        + "\n*      ol_amount = "
        + ol_amount
        + "\n* ol_supply_w_id = "
        + ol_supply_w_id
        + "\n*    ol_quantity = "
        + ol_quantity
        + "\n*   ol_dist_info = "
        + ol_dist_info
        + "\n**********************************************");
  }
}
