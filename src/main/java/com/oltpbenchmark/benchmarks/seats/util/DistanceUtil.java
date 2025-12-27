/*
 * Copyright 2020 by OLTPBenchmark Project
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

package com.oltpbenchmark.benchmarks.seats.util;

import com.oltpbenchmark.util.Pair;

/** 여기서 찾은 코드를 기반으로 합니다: http://www.zipcodeworld.com/samples/distance.java.html */
public abstract class DistanceUtil {

  /**
   * 두 점 사이의 거리를 계산합니다.
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
   * Pair<위도, 경도>
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
