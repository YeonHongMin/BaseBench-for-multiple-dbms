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

import com.oltpbenchmark.api.AbstractTestWorker;
import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.benchmarks.auctionmark.util.UserId;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;

public class TestAuctionMarkWorker extends AbstractTestWorker<AuctionMarkBenchmark> {

  @Override
  public List<Class<? extends Procedure>> procedures() {
    return TestAuctionMarkBenchmark.PROCEDURE_CLASSES;
  }

  @Override
  public Class<AuctionMarkBenchmark> benchmarkClass() {
    return AuctionMarkBenchmark.class;
  }

  @Override
  protected void postCreateDatabaseSetup() throws IOException {
    super.postCreateDatabaseSetup();
    AuctionMarkProfile.clearCachedProfile();
    AuctionMarkConstants.CLOSE_AUCTIONS_ENABLE = false;
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** testUniqueSellers */
=======
  /** 고유 판매자 테스트 */
>>>>>>> master
=======
  /** testUniqueSellers */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @Test
  public void testUniqueSellers() throws Exception {

    Set<UserId> all_users = new HashSet<UserId>();
    Set<UserId> worker_users = new TreeSet<UserId>();
    Integer last_num_users = null;
    for (var w : this.workers) {
      AuctionMarkWorker worker = (AuctionMarkWorker) w;
      assertNotNull(w);

<<<<<<< HEAD
<<<<<<< HEAD
      // Get the uninitialized profile
=======
      // 초기화되지 않은 프로필을 가져옵니다
>>>>>>> master
=======
      // Get the uninitialized profile
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      AuctionMarkProfile profile = worker.getProfile();
      assertNotNull(profile);
      assertTrue(profile.users_per_itemCount.isEmpty());

<<<<<<< HEAD
<<<<<<< HEAD
      // Then try to initialize it
=======
      // 그런 다음 초기화를 시도합니다
>>>>>>> master
=======
      // Then try to initialize it
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      profile.loadProfile(worker);
      assertFalse(profile.users_per_itemCount.isEmpty());
      int num_users = profile.users_per_itemCount.getSampleCount();
      if (last_num_users != null) assertEquals(last_num_users.intValue(), num_users);
      else {
        System.err.println("Number of Users: " + num_users);
      }

      worker_users.clear();
      for (int i = 0; i < num_users; i++) {
        UserId user_id = profile.getRandomSellerId(worker.getId());
        assertNotNull(user_id);
        assertFalse(
            worker.getId() + " -> " + user_id.toString() + " / " + user_id.encode(),
            all_users.contains(user_id));
        worker_users.add(user_id);
      } // FOR
      assertFalse(worker_users.isEmpty());
      all_users.addAll(worker_users);
      last_num_users = num_users;
    } // FOR
  }
}
