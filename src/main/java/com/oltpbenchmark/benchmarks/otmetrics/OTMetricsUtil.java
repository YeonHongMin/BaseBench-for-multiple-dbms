/*
 * Copyright 2022 by OLTPBenchmark Project
 *
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
 *
 */

package com.oltpbenchmark.benchmarks.otmetrics;

import java.time.LocalDateTime;

public class OTMetricsUtil {

  /**
   * 주어진 source_id에 대해 데이터베이스의 모든 session/observation에 대한 시작 타임스탬프를 반환합니다.
   *
   * @param source_id
   * @return
   */
  public static LocalDateTime getCreateDateTime(int source_id) {
    return OTMetricsConstants.START_DATE.plusHours(source_id);
  }

  /**
   * 주어진 source_id와 세션 내의 timetick에 대해 관찰 데이터의 타임스탬프를 반환합니다.
   *
   * @param source_id
   * @param timetick
   * @return
   */
  public static LocalDateTime getObservationDateTime(int source_id, int timetick) {
    LocalDateTime base = getCreateDateTime(source_id);
    return base.plusMinutes(timetick * 20);
  }
}
