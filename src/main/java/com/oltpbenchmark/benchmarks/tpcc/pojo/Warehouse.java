/*
 * Copyright 2020 by OLTPBenchmark Project
 *
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
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한과 조건을 준수해 주세요.
>>>>>>> master
 *
 */

package com.oltpbenchmark.benchmarks.tpcc.pojo;

import java.io.Serializable;

public class Warehouse implements Serializable {
  static final long serialVersionUID = 0;

<<<<<<< HEAD
  public int w_id; // PRIMARY KEY
=======
  public int w_id; // 기본 키
>>>>>>> master
  public float w_ytd;
  public double w_tax;
  public String w_name;
  public String w_street_1;
  public String w_street_2;
  public String w_city;
  public String w_state;
  public String w_zip;

  @Override
  public String toString() {
    return ("\n***************** Warehouse ********************"
        + "\n*       w_id = "
        + w_id
        + "\n*      w_ytd = "
        + w_ytd
        + "\n*      w_tax = "
        + w_tax
        + "\n*     w_name = "
        + w_name
        + "\n* w_street_1 = "
        + w_street_1
        + "\n* w_street_2 = "
        + w_street_2
        + "\n*     w_city = "
        + w_city
        + "\n*    w_state = "
        + w_state
        + "\n*      w_zip = "
        + w_zip
        + "\n**********************************************");
  }
}
