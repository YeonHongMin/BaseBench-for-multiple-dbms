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

package com.oltpbenchmark.benchmarks.tatp;

public abstract class TATPConstants {

  public static final long DEFAULT_NUM_SUBSCRIBERS = 100000L;

  public static final int SUB_NBR_PADDING_SIZE = 15;

  // ----------------------------------------------------------------
  // STORED PROCEDURE EXECUTION FREQUENCIES (0-100)
  // ----------------------------------------------------------------
  public static final int FREQUENCY_DELETE_CALL_FORWARDING = 2; // Multi
  public static final int FREQUENCY_GET_ACCESS_DATA = 35; // Single
  public static final int FREQUENCY_GET_NEW_DESTINATION = 10; // Single
  public static final int FREQUENCY_GET_SUBSCRIBER_DATA = 35; // Single
  public static final int FREQUENCY_INSERT_CALL_FORWARDING = 2; // Multi
  public static final int FREQUENCY_UPDATE_LOCATION = 14; // Multi
  public static final int FREQUENCY_UPDATE_SUBSCRIBER_DATA = 2; // Single

  // ----------------------------------------------------------------
  // TABLE NAMES
  // ----------------------------------------------------------------
  public static final String TABLENAME_SUBSCRIBER = "subscriber";
  public static final String TABLENAME_ACCESS_INFO = "access_info";
  public static final String TABLENAME_SPECIAL_FACILITY = "special_facility";
  public static final String TABLENAME_CALL_FORWARDING = "call_forwarding";

  public static final String[] TABLENAMES = {
    TABLENAME_SUBSCRIBER,
    TABLENAME_ACCESS_INFO,
    TABLENAME_SPECIAL_FACILITY,
    TABLENAME_CALL_FORWARDING
  };
}
