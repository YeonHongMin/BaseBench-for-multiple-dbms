/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
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
 *
 */

package com.oltpbenchmark.benchmarks.twitter.util;

import com.oltpbenchmark.util.Histogram;

/**
<<<<<<< HEAD
 * A histogram of Twitter username length. This is derived from
=======
 * 트위터 사용자 이름 길이의 히스토그램. 이것은 다음에서 파생되었습니다:
>>>>>>> master
 * http://simplymeasured.com/blog/2010/06/lakers-vs-celtics-social-media-breakdown-nba/
 *
 * @author pavlo
 */
public final class NameHistogram extends Histogram<Integer> {
  private static final long serialVersionUID = 0L;

  {
    this.put(1, 2);
    this.put(2, 12);
    this.put(3, 209);
    this.put(4, 2027);
    this.put(5, 7987);
    this.put(6, 22236);
    this.put(7, 38682);
    this.put(8, 54809);
    this.put(9, 65614);
    this.put(10, 70547);
    this.put(11, 69153);
    this.put(12, 63777);
    this.put(13, 56049);
    this.put(14, 47905);
    this.put(15, 48166);
  }
}
