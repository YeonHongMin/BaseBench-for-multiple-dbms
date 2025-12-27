/*
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
 *
 */

package com.oltpbenchmark.util;

import java.util.regex.Pattern;

public abstract class MonitoringUtil {
  private static final Pattern MONITORING_PATTERN =
      Pattern.compile("/[*] MONITOR-(?<queryId>\\S+) [*]/");
  private static final String MONITORING_MARKER = "/* MONITOR-$queryId */";
  private static final String MONITORING_PREFIX = "/* MONITOR-";
  private static final String MONITORING_QUERYID = "$queryId";

  /** 공통 모니터링 접두사입니다. */
  public static Pattern getMonitoringPattern() {
    return MonitoringUtil.MONITORING_PATTERN;
  }

  /** 모니터링 마커를 가져옵니다. */
  public static String getMonitoringMarker() {
    return MonitoringUtil.MONITORING_MARKER;
  }

  /** 모니터링 식별자를 가져옵니다. */
  public static String getMonitoringPrefix() {
    return MonitoringUtil.MONITORING_PREFIX;
  }

  /** 모니터링 접두사 안의 쿼리 식별자입니다. */
  public static String getMonitoringQueryId() {
    return MonitoringUtil.MONITORING_QUERYID;
  }
}

