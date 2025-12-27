/*
<<<<<<< HEAD
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
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 */

package com.oltpbenchmark.benchmarks.auctionmark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oltpbenchmark.api.AbstractTestLoader;
import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.auctionmark.util.ItemInfo;
import com.oltpbenchmark.util.Histogram;
import com.oltpbenchmark.util.RandomGenerator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class TestAuctionMarkLoader extends AbstractTestLoader<AuctionMarkBenchmark> {

  @Override
  public List<Class<? extends Procedure>> procedures() {
    return TestAuctionMarkBenchmark.PROCEDURE_CLASSES;
  }

  @Override
  public Class<AuctionMarkBenchmark> benchmarkClass() {
    return AuctionMarkBenchmark.class;
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** testSaveLoadProfile */
=======
  /** 프로필 저장/로드 테스트 */
>>>>>>> master
=======
  /** testSaveLoadProfile */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @Test
  public void testSaveLoadProfile() throws Exception {
    AuctionMarkProfile.clearCachedProfile();
    AuctionMarkLoader loader = (AuctionMarkLoader) super.testLoadWithReturn();

    AuctionMarkProfile orig = loader.profile;
    assertNotNull(orig);
    assertFalse(orig.users_per_itemCount.isEmpty());

    AuctionMarkProfile copy = new AuctionMarkProfile(this.benchmark, new RandomGenerator(0));
    assertTrue(copy.users_per_itemCount.isEmpty());

    List<Worker<?>> workers = this.benchmark.makeWorkers();
    AuctionMarkWorker worker = (AuctionMarkWorker) workers.get(0);
    copy.loadProfile(worker);

    assertEquals(orig.scale_factor, copy.scale_factor, 0.001f);
    assertEquals(orig.getLoaderStartTime().toString(), copy.getLoaderStartTime().toString());
    assertEquals(orig.getLoaderStopTime().toString(), copy.getLoaderStopTime().toString());
    assertEquals(orig.users_per_itemCount, copy.users_per_itemCount);
  }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  /** testLoadProfilePerClient */
  @Test
  public void testLoadProfilePerClient() throws Exception {
    // We don't have to reload our cached profile here
    // We just want to make sure that each client's profile contains a unique
    // set of ItemInfo records that are not found in any other profile's lists
<<<<<<< HEAD
=======
  /** 클라이언트별 프로필 로드 테스트 */
  @Test
  public void testLoadProfilePerClient() throws Exception {
    // 여기서는 캐시된 프로필을 다시 로드할 필요가 없습니다
    // 각 클라이언트의 프로필이 고유한 ItemInfo 레코드 집합을 포함하고 있으며
    // 다른 프로필의 목록에는 없는지 확인하기만 하면 됩니다
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    int num_clients = 9;
    this.workConf.setTerminals(num_clients);
    AuctionMarkLoader loader = (AuctionMarkLoader) super.testLoadWithReturn();
    assertNotNull(loader);

    Set<ItemInfo> allItemInfos = new HashSet<>();
    Set<ItemInfo> clientItemInfos = new HashSet<>();
    Histogram<Integer> clientItemCtr = new Histogram<>();
    List<Worker<?>> workers = this.benchmark.makeWorkers();
    assertEquals(num_clients, workers.size());
    for (int i = 0; i < num_clients; i++) {
      AuctionMarkWorker worker = (AuctionMarkWorker) workers.get(i);
      assertNotNull(worker);
      worker.initialize(); // Initializes the profile we need

      clientItemInfos.clear();
      for (LinkedList<ItemInfo> items : worker.profile.allItemSets) {
        assertNotNull(items);
        for (ItemInfo itemInfo : items) {
<<<<<<< HEAD
<<<<<<< HEAD
          // Make sure we haven't seen it another list for this client
          assertFalse(itemInfo.toString(), clientItemInfos.contains(itemInfo));
          // Nor that we have seen it in any other client
=======
          // 이 클라이언트의 다른 목록에서 본 적이 없는지 확인합니다
          assertFalse(itemInfo.toString(), clientItemInfos.contains(itemInfo));
          // 다른 클라이언트에서 본 적도 없는지 확인합니다
>>>>>>> master
=======
          // Make sure we haven't seen it another list for this client
          assertFalse(itemInfo.toString(), clientItemInfos.contains(itemInfo));
          // Nor that we have seen it in any other client
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
          assertFalse(itemInfo.toString(), allItemInfos.contains(itemInfo));
        } // FOR
        clientItemInfos.addAll(items);
      } // FOR
      clientItemCtr.put(i, clientItemInfos.size());
      allItemInfos.addAll(clientItemInfos);
    } // FOR
  }
}
