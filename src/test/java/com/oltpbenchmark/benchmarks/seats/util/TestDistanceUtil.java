/*
<<<<<<< HEAD
 *  Copyright 2015 by OLTPBenchmark Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
=======
 * Copyright 2015 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 별도 합의가 없다면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다. 라이선스가 허용하는 범위 내에서만 사용하세요.
 *
>>>>>>> master
 */

package com.oltpbenchmark.benchmarks.seats.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestDistanceUtil {

<<<<<<< HEAD
  /** testDistance */
  @Test
  public void testDistance() throws Exception {
    // { latitude, longitude }
=======
  /** 거리 테스트 */
  @Test
  public void testDistance() throws Exception {
    // { 위도, 경도 }
>>>>>>> master
    double[][] locations = {
      {39.175278, -76.668333}, // Baltimore-Washington, USA (BWI)
      {-22.808889, -43.243611}, // Rio de Janeiro, Brazil (GIG)
      {40.633333, -73.783333}, // New York, USA (JFK)
      {-33.946111, 151.177222}, // Syndey, Austrailia (SYD)
    };
<<<<<<< HEAD
    // expected distance in miles
=======
    // 마일 단위 예상 거리
>>>>>>> master
    double[] expected = {
      4796, // BWI->GIG
      183, // BWI->JFK
      9787, // BWI->SYD
      4802, // GIG->JFK
      8402, // GIG->SYD
      9950, // JFK->SYD
    };

    int e = 0;
    for (int i = 0; i < locations.length - 1; i++) {
      double[] loc0 = locations[i];
      for (int j = i + 1; j < locations.length; j++) {
        double[] loc1 = locations[j];
        double distance = Math.round(DistanceUtil.distance(loc0[0], loc0[1], loc1[0], loc1[1]));
        assertEquals(expected[e++], distance, 0.0001f);
      } // FOR
    } // FOR
  }
}
