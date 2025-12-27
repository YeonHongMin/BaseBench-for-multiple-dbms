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

package com.oltpbenchmark.benchmarks.twitter;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.TransactionGenerator;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.twitter.procedures.GetFollowers;
import com.oltpbenchmark.benchmarks.twitter.util.TraceTransactionGenerator;
import com.oltpbenchmark.benchmarks.twitter.util.TwitterOperation;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

public final class TwitterBenchmark extends BenchmarkModule {

  private final TwitterConfiguration twitterConf;

  public TwitterBenchmark(WorkloadConfiguration workConf) {
    super(workConf);
    this.twitterConf = new TwitterConfiguration(workConf);
  }

  @Override
  protected Package getProcedurePackageImpl() {
    return GetFollowers.class.getPackage();
  }

  @Override
  protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() throws IOException {
    List<String> tweetIds =
        FileUtils.readLines(new File(twitterConf.getTracefile()), Charset.defaultCharset());
    List<String> userIds =
        FileUtils.readLines(new File(twitterConf.getTracefile2()), Charset.defaultCharset());

    if (tweetIds.size() != userIds.size()) {
      throw new RuntimeException(
          String.format(
              "there was a problem reading files, sizes don't match.  tweets %d, userids %d",
              tweetIds.size(), userIds.size()));
    }

    List<TwitterOperation> trace = new ArrayList<>();
    for (int i = 0; i < tweetIds.size(); i++) {
      trace.add(
          new TwitterOperation(
              Integer.parseInt(tweetIds.get(i)), Integer.parseInt(userIds.get(i))));
    }

    List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();
    for (int i = 0; i < workConf.getTerminals(); ++i) {
      TransactionGenerator<TwitterOperation> generator = new TraceTransactionGenerator(trace);
      workers.add(new TwitterWorker(this, i, generator));
    }
    return workers;
  }

  @Override
  protected Loader<TwitterBenchmark> makeLoaderImpl() {
    return new TwitterLoader(this);
  }
}
