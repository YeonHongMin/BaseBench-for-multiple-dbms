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

package com.oltpbenchmark.benchmarks.auctionmark.util;

import com.oltpbenchmark.benchmarks.auctionmark.AuctionMarkConstants;
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AuctionMarkUtil {
  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(AuctionMarkUtil.class);

  @SuppressWarnings("unused")
  private static final long ITEM_ID_MASK = 0xFFFFFFFFFFFFFFL; // 56 bits (ITEM_ID)

  /**
   * @param item_id
   * @param idx
   * @return
   */
  public static String getUniqueElementId(String item_id, int idx) {
    ItemId itemId = new ItemId(item_id);
    UserId sellerId = itemId.getSellerId();

    return new ItemId(sellerId, idx).encode();
  }

  /**
   * @param benchmarkTimes
   * @return
   */
  public static Timestamp getProcTimestamp(Timestamp[] benchmarkTimes) {

    Timestamp tmp = new Timestamp(System.currentTimeMillis());
    long timestamp = getScaledTimestamp(benchmarkTimes[0], benchmarkTimes[1], tmp);
    tmp.setTime(timestamp);

    return (tmp);
  }

  /**
   * @param benchmarkStart
   * @param clientStart
   * @param current
   * @return
   */
  public static long getScaledTimestamp(
      Timestamp benchmarkStart, Timestamp clientStart, Timestamp current) {
    // First get the offset between the benchmarkStart and the clientStart
    // We then subtract that value from the current time. This gives us the total elapsed
    // time from the current time to the time that the benchmark start (with the gap
    // from when the benchmark was loading data cut out)
    long base = benchmarkStart.getTime();
    long offset = current.getTime() - (clientStart.getTime() - base);
    long elapsed = (offset - base) * AuctionMarkConstants.TIME_SCALE_FACTOR;
    return (base + elapsed);
  }
}
