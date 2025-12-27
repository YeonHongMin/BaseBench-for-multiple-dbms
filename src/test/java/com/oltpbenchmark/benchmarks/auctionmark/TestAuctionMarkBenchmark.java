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

package com.oltpbenchmark.benchmarks.auctionmark;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oltpbenchmark.api.AbstractTestBenchmarkModule;
import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.benchmarks.auctionmark.procedures.*;
import com.oltpbenchmark.benchmarks.auctionmark.util.CategoryParser;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class TestAuctionMarkBenchmark extends AbstractTestBenchmarkModule<AuctionMarkBenchmark> {

  public static final List<Class<? extends Procedure>> PROCEDURE_CLASSES =
      List.of(
          GetItem.class,
          GetUserInfo.class,
          NewBid.class,
          NewComment.class,
          NewCommentResponse.class,
          NewFeedback.class,
          NewItem.class,
          NewPurchase.class,
          UpdateItem.class);

  @Override
  public List<Class<? extends Procedure>> procedures() {
    return PROCEDURE_CLASSES;
  }

  @Override
  public Class<AuctionMarkBenchmark> benchmarkClass() {
    return AuctionMarkBenchmark.class;
  }

  @Override
  protected void postCreateDatabaseSetup() throws IOException {
    super.postCreateDatabaseSetup();
    AuctionMarkProfile.clearCachedProfile();
  }

<<<<<<< HEAD
  /** testCategoryParser */
=======
  /** 카테고리 파서 테스트 */
>>>>>>> master
  @Test
  public void testCategoryParser() throws Exception {
    CategoryParser categoryParser = new CategoryParser();
    assertNotNull(categoryParser.getCategoryMap());
    assertTrue(categoryParser.getCategoryMap().size() > 0);
  }

<<<<<<< HEAD
  /** testSupplementalClasses */
  @Test
  public void testSupplementalClasses() throws Exception {
    // Check to make sure that we have something...
=======
  /** 보조 클래스 테스트 */
  @Test
  public void testSupplementalClasses() throws Exception {
    // 무언가가 있는지 확인합니다...
>>>>>>> master
    Map<TransactionType, Procedure> procs = this.benchmark.getProcedures();
    assertNotNull(procs);
  }
}
