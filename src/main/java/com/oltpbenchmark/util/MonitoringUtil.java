/*
<<<<<<< HEAD
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
=======
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
>>>>>>> master
 *
 */

package com.oltpbenchmark.util;

import java.util.regex.Pattern;

<<<<<<< HEAD
=======
/** SQL 쿼리 모니터링을 위한 유틸리티 클래스입니다. */
>>>>>>> master
public abstract class MonitoringUtil {
  private static final Pattern MONITORING_PATTERN =
      Pattern.compile("/[*] MONITOR-(?<queryId>\\S+) [*]/");
  private static final String MONITORING_MARKER = "/* MONITOR-$queryId */";
  private static final String MONITORING_PREFIX = "/* MONITOR-";
  private static final String MONITORING_QUERYID = "$queryId";

<<<<<<< HEAD
  /** Universal monitoring prefix. */
=======
  /** 범용 모니터링 접두사 패턴을 반환합니다. */
>>>>>>> master
  public static Pattern getMonitoringPattern() {
    return MonitoringUtil.MONITORING_PATTERN;
  }

<<<<<<< HEAD
  /** Get monitoring marker. */
=======
  /** 모니터링 마커를 반환합니다. */
>>>>>>> master
  public static String getMonitoringMarker() {
    return MonitoringUtil.MONITORING_MARKER;
  }

<<<<<<< HEAD
  /** Get monitoring identifier. */
=======
  /** 모니터링 식별자를 반환합니다. */
>>>>>>> master
  public static String getMonitoringPrefix() {
    return MonitoringUtil.MONITORING_PREFIX;
  }

<<<<<<< HEAD
  /** Query identifier in monitoring prefix. */
=======
  /** 모니터링 접두사에 사용되는 쿼리 식별자를 반환합니다. */
>>>>>>> master
  public static String getMonitoringQueryId() {
    return MonitoringUtil.MONITORING_QUERYID;
  }
}
