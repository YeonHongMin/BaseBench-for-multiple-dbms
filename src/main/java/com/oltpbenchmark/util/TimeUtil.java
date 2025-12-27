/*
<<<<<<< HEAD
<<<<<<< HEAD
 * Copyright 2020 by OLTPBenchmark Project
 *
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
 * 저작권 2020 OLTPBenchmark 프로젝트
 *
 * Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 * 라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 * 이 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 * 라이선스에서 허용하는 권한과 제한 사항은
 * 라이선스의 본문을 참조하십시오.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public abstract class TimeUtil {

  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
  public static final SimpleDateFormat DATE_FORMAT_14 = new SimpleDateFormat("yyyyMMddHHmmss");

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * TODO(djellel)
=======
   * 현재 시간을 14자리 문자열 형식(yyyyMMddHHmmss)으로 반환합니다.
>>>>>>> master
=======
   * TODO(djellel)
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  public static String getCurrentTimeString14() {
    return TimeUtil.DATE_FORMAT_14.format(new java.util.Date());
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * TODO(djellel)
=======
   * 현재 시간을 문자열 형식(yyyy-MM-dd_HH-mm-ss)으로 반환합니다.
>>>>>>> master
=======
   * TODO(djellel)
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @return
   */
  public static String getCurrentTimeString() {
    return TimeUtil.DATE_FORMAT.format(new java.util.Date());
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** Get a timestamp of the current time */
=======
  /** 현재 시간의 타임스탬프를 가져옵니다. */
>>>>>>> master
=======
  /** Get a timestamp of the current time */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public static Timestamp getCurrentTime() {
    return new Timestamp(System.currentTimeMillis());
  }
}
