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

public class Item {

<<<<<<< HEAD
<<<<<<< HEAD
  public int i_id; // PRIMARY KEY
=======
  public int i_id; // 기본 키
>>>>>>> master
=======
  public int i_id; // 기본 키
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public int i_im_id;
  public double i_price;
  public String i_name;
  public String i_data;

  @Override
  public String toString() {
    return ("\n***************** Item ********************"
        + "\n*    i_id = "
        + i_id
        + "\n*  i_name = "
        + i_name
        + "\n* i_price = "
        + i_price
        + "\n*  i_data = "
        + i_data
        + "\n* i_im_id = "
        + i_im_id
        + "\n**********************************************");
  }
}
