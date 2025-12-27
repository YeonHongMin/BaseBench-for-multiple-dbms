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

package com.oltpbenchmark.benchmarks.otmetrics;

import java.time.LocalDateTime;
import java.time.Month;

public abstract class OTMetricsConstants {

<<<<<<< HEAD
<<<<<<< HEAD
  /** Table Names */
=======
  /** 테이블 이름 */
>>>>>>> master
=======
  /** Table Names */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public static final String TABLENAME_SOURCES = "sources";

  public static final String TABLENAME_SESSIONS = "sessions";
  public static final String TABLENAME_TYPES = "types";
  public static final String TABLENAME_OBSERVATIONS = "observations";

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  /**
   * Number of records per table. All of the tables in this benchmark will scale as you change the
   * benchmark scalefactor
   */
  public static final int NUM_SOURCES = 100;

  public static final int NUM_SESSIONS = 1000;
  public static final int NUM_TYPES = 500; // FIXED SIZE
  public static final int NUM_OBSERVATIONS = 10000;

  /** All objects in the database will be created starting after this date */
<<<<<<< HEAD
=======
  /** 테이블당 레코드 수. 이 벤치마크의 모든 테이블은 벤치마크 스케일 팩터를 변경하면 그에 따라 스케일링됩니다. */
  public static final int NUM_SOURCES = 100;

  public static final int NUM_SESSIONS = 1000;
  public static final int NUM_TYPES = 500; // 고정 크기
  public static final int NUM_OBSERVATIONS = 10000;

  /** 데이터베이스의 모든 객체는 이 날짜 이후부터 생성됩니다 */
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public static final LocalDateTime START_DATE = LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0);
}
