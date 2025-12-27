/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
<<<<<<< HEAD
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
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한과 조건을 준수해 주세요.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.benchmarks.tpcc.pojo;

import java.io.Serializable;

public class Stock implements Serializable {
  static final long serialVersionUID = 0;

<<<<<<< HEAD
<<<<<<< HEAD
  public int s_i_id; // PRIMARY KEY 2
  public int s_w_id; // PRIMARY KEY 1
=======
  public int s_i_id; // 기본 키 2
  public int s_w_id; // 기본 키 1
>>>>>>> master
=======
  public int s_i_id; // 기본 키 2
  public int s_w_id; // 기본 키 1
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public int s_order_cnt;
  public int s_remote_cnt;
  public int s_quantity;
  public float s_ytd;
  public String s_data;
  public String s_dist_01;
  public String s_dist_02;
  public String s_dist_03;
  public String s_dist_04;
  public String s_dist_05;
  public String s_dist_06;
  public String s_dist_07;
  public String s_dist_08;
  public String s_dist_09;
  public String s_dist_10;

  @Override
  public String toString() {
    return ("\n***************** Stock ********************"
        + "\n*       s_i_id = "
        + s_i_id
        + "\n*       s_w_id = "
        + s_w_id
        + "\n*   s_quantity = "
        + s_quantity
        + "\n*        s_ytd = "
        + s_ytd
        + "\n*  s_order_cnt = "
        + s_order_cnt
        + "\n* s_remote_cnt = "
        + s_remote_cnt
        + "\n*       s_data = "
        + s_data
        + "\n*    s_dist_01 = "
        + s_dist_01
        + "\n*    s_dist_02 = "
        + s_dist_02
        + "\n*    s_dist_03 = "
        + s_dist_03
        + "\n*    s_dist_04 = "
        + s_dist_04
        + "\n*    s_dist_05 = "
        + s_dist_05
        + "\n*    s_dist_06 = "
        + s_dist_06
        + "\n*    s_dist_07 = "
        + s_dist_07
        + "\n*    s_dist_08 = "
        + s_dist_08
        + "\n*    s_dist_09 = "
        + s_dist_09
        + "\n*    s_dist_10 = "
        + s_dist_10
        + "\n**********************************************");
  }
}
