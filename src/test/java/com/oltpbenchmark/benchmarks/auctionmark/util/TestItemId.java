/*
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
 */

package com.oltpbenchmark.benchmarks.auctionmark.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Random;
import org.junit.Test;

public class TestItemId {

  private static final Random rand = new Random(1);

  private final int num_items = 10;
  private final int num_users = 3;

  /** testItemId */
  @Test
  public void testItemId() {
    for (int i = 0; i < num_users; i++) {
      UserId user_id = new UserId(rand.nextInt(Integer.MAX_VALUE), rand.nextInt(Integer.MAX_VALUE));
      for (int item_ctr = 0; item_ctr < num_items; item_ctr++) {
        ItemId customer_id = new ItemId(user_id, item_ctr);
        assertNotNull(customer_id);
        assertEquals(user_id, customer_id.getSellerId());
        assertEquals(item_ctr, customer_id.getItemCtr());
      } // FOR
    } // FOR
  }

  /** testItemIdEncode */
  @Test
  public void testItemIdEncode() {
    for (int i = 0; i < num_users; i++) {
      UserId user_id = new UserId(rand.nextInt(Integer.MAX_VALUE), rand.nextInt(Integer.MAX_VALUE));
      for (int item_ctr = 0; item_ctr < num_items; item_ctr++) {
        String encoded = new ItemId(user_id, item_ctr).encode();

        ItemId customer_id = new ItemId(encoded);
        assertNotNull(customer_id);
        assertEquals(user_id, customer_id.getSellerId());
        assertEquals(item_ctr, customer_id.getItemCtr());
      } // FOR
    } // FOR
  }
}
