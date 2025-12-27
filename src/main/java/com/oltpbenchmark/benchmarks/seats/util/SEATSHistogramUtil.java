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

import com.oltpbenchmark.benchmarks.seats.SEATSConstants;
import com.oltpbenchmark.util.Histogram;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SEATSHistogramUtil {
  private static final Logger LOG = LoggerFactory.getLogger(SEATSHistogramUtil.class);

  private static final Map<String, Histogram<String>> cached_Histograms = new HashMap<>();

  private static Map<String, Histogram<String>> cached_AirportFlights;

  private static String getHistogramFilePath(String data_dir, String name) {
    return data_dir + File.separator + "histogram." + name.toLowerCase();
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Returns the Flights Per Airport Histogram
=======
   * 공항별 항공편 히스토그램을 반환합니다.
>>>>>>> master
=======
   * Returns the Flights Per Airport Histogram
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param data_path
   * @return
   * @throws Exception
   */
  public static synchronized Map<String, Histogram<String>> loadAirportFlights(String data_path)
      throws Exception {
    if (cached_AirportFlights != null) {
      return (cached_AirportFlights);
    }

    String filePath = getHistogramFilePath(data_path, SEATSConstants.HISTOGRAM_FLIGHTS_PER_AIRPORT);
    Histogram<String> h = new Histogram<>();
    h.load(filePath);

    Map<String, Histogram<String>> m = new TreeMap<>();
    Pattern pattern = Pattern.compile("-");
    Collection<String> values = h.values();
    for (String value : values) {
      String[] split = pattern.split(value);
      Histogram<String> src_h = m.get(split[0]);
      if (src_h == null) {
        src_h = new Histogram<>();
        m.put(split[0], src_h);
      }
      src_h.put(split[1], h.get(value));
    }

    cached_AirportFlights = m;
    return (m);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Construct a histogram from an airline-benchmark data file
=======
   * 항공사 벤치마크 데이터 파일에서 히스토그램을 구성합니다.
>>>>>>> master
=======
   * Construct a histogram from an airline-benchmark data file
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   *
   * @param name
   * @param data_path
   * @param has_header
   * @return
   * @throws Exception
   */
  public static synchronized Histogram<String> loadHistogram(
      String name, String data_path, boolean has_header) throws Exception {
    String filePath = getHistogramFilePath(data_path, name);
    Histogram<String> histogram = cached_Histograms.get(filePath);
    if (histogram == null) {
      histogram = new Histogram<>();
      histogram.load(filePath);
      cached_Histograms.put(filePath, histogram);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug(String.format("Histogram %s\n%s", name, histogram));
    }

    return (histogram);
  }
}
