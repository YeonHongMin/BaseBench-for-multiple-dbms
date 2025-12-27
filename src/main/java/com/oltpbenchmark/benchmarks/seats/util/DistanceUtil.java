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

package com.oltpbenchmark.benchmarks.seats.util;

import com.oltpbenchmark.util.Pair;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
/** Based on code found here: http://www.zipcodeworld.com/samples/distance.java.html */
public abstract class DistanceUtil {

  /**
   * Calculate the distance between two points
<<<<<<< HEAD
=======
/** 여기서 찾은 코드를 기반으로 합니다: http://www.zipcodeworld.com/samples/distance.java.html */
public abstract class DistanceUtil {

  /**
   * 두 점 사이의 거리를 계산합니다.
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param lat0
   * @param lon0
   * @param lat1
   * @param lon1
   * @return
   */
  public static double distance(double lat0, double lon0, double lat1, double lon1) {
    double theta = lon0 - lon1;
    double dist =
        Math.sin(deg2rad(lat0)) * Math.sin(deg2rad(lat1))
            + Math.cos(deg2rad(lat0)) * Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(theta));
    dist = Math.acos(dist);
    dist = rad2deg(dist);
    return (dist * 60 * 1.1515);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Pair<Latitude, Longitude>
=======
   * Pair<위도, 경도>
>>>>>>> master
=======
   * Pair<Latitude, Longitude>
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param loc0
   * @param loc1
   * @return
   */
  public static double distance(Pair<Double, Double> loc0, Pair<Double, Double> loc1) {
    return (DistanceUtil.distance(loc0.first, loc0.second, loc1.first, loc1.second));
  }

  private static double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  private static double rad2deg(double rad) {
    return (rad * 180.0 / Math.PI);
  }
}
