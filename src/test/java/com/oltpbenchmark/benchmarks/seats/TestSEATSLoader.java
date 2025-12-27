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

package com.oltpbenchmark.benchmarks.seats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.oltpbenchmark.api.AbstractTestLoader;
import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Worker;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class TestSEATSLoader extends AbstractTestLoader<SEATSBenchmark> {

  @Override
  public List<Class<? extends Procedure>> procedures() {
    return TestSEATSBenchmark.PROCEDURE_CLASSES;
  }

  @Override
  public Class<SEATSBenchmark> benchmarkClass() {
    return SEATSBenchmark.class;
  }

  @Override
  protected void postCreateDatabaseSetup() throws IOException {
    super.postCreateDatabaseSetup();
    SEATSProfile.clearCachedProfile();
  }

<<<<<<< HEAD
  /** testSaveLoadProfile */
=======
  /** 프로필 저장/로드 테스트 */
>>>>>>> master
  @Test
  public void testSaveLoadProfile() throws Exception {
    this.benchmark.createDatabase();
    SEATSLoader loader = (SEATSLoader) this.benchmark.loadDatabase();
    assertNotNull(loader);

    SEATSProfile orig = loader.profile;
    assertNotNull(orig);

<<<<<<< HEAD
    // Make sure there is something in our profile after loading the database
=======
    // 데이터베이스를 로드한 후 프로필에 무언가 있는지 확인합니다
>>>>>>> master
    assertFalse("Empty Profile: airport_max_customer_id", orig.airport_max_customer_id.isEmpty());

    SEATSProfile copy = new SEATSProfile(this.benchmark, benchmark.getRandomGenerator());
    assert (copy.airport_histograms.isEmpty());

    List<Worker<?>> workers = this.benchmark.makeWorkers();
    SEATSWorker worker = (SEATSWorker) workers.get(0);
    copy.loadProfile(worker);

    assertEquals(orig.scale_factor, copy.scale_factor, 0.001f);
    assertEquals(orig.airport_max_customer_id, copy.airport_max_customer_id);
    assertEquals(orig.flight_start_date.toString(), copy.flight_start_date.toString());
    assertEquals(orig.flight_upcoming_date.toString(), copy.flight_upcoming_date.toString());
    assertEquals(orig.flight_past_days, copy.flight_past_days);
    assertEquals(orig.flight_future_days, copy.flight_future_days);
    assertEquals(orig.flight_upcoming_offset, copy.flight_upcoming_offset);
    assertEquals(orig.reservation_upcoming_offset, copy.reservation_upcoming_offset);
    assertEquals(orig.num_reservations, copy.num_reservations);
    assertEquals(orig.histograms, copy.histograms);
    assertEquals(orig.airport_histograms, copy.airport_histograms);
<<<<<<< HEAD
    // TODO(WAN): This was commented out before and so it is still commented out now, but we should
    // probably dig
    //  into why it is not the same.
=======
    // TODO(WAN): 이것은 이전에 주석 처리되었고 지금도 여전히 주석 처리되어 있지만,
    // 왜 동일하지 않은지 조사해야 할 것 같습니다.
>>>>>>> master
    // assertEquals(orig.code_id_xref, copy.code_id_xref);
  }
}
