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

package com.oltpbenchmark.benchmarks.wikipedia.util;

import com.oltpbenchmark.benchmarks.wikipedia.data.PageHistograms;
import com.oltpbenchmark.util.RandomDistribution.FlatHistogram;
import com.oltpbenchmark.util.TextGenerator;
import java.util.Random;

public abstract class WikipediaUtil {

  public static String generatePageTitle(Random rand, int page_id) {
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    // Yo we need to do this to ensure that for a given page_id, we always get back the same title.
    // This is a hack for now (as it will break the <randomSeed> option in the config file.
    // But from what I can tell it works.
    rand.setSeed(page_id);
    FlatHistogram<Integer> h_titleLength = new FlatHistogram<>(rand, PageHistograms.TITLE_LENGTH);
    // HACK: Always append the page id to the title
    // so that it's guaranteed to be unique.
    // Otherwise we can get collisions with larger scale factors.
<<<<<<< HEAD
=======
    // 주어진 page_id에 대해 항상 동일한 제목을 얻도록 하기 위해 이렇게 해야 합니다.
    // 이것은 지금은 해킹입니다 (설정 파일의 <randomSeed> 옵션을 깨뜨릴 것입니다.
    // 하지만 제 생각에는 작동합니다.
    rand.setSeed(page_id);
    FlatHistogram<Integer> h_titleLength = new FlatHistogram<>(rand, PageHistograms.TITLE_LENGTH);
    // HACK: 제목에 항상 page_id를 추가하여 고유성을 보장합니다.
    // 그렇지 않으면 더 큰 스케일 팩터에서 충돌이 발생할 수 있습니다.
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    int titleLength = h_titleLength.nextValue();
    return TextGenerator.randomStr(rand, titleLength) + " [" + page_id + "]";
  }

  public static int generatePageNamespace(Random rand, int page_id) {
    FlatHistogram<Integer> h_namespace = new FlatHistogram<>(rand, PageHistograms.NAMESPACE);
    return h_namespace.nextInt();
  }
}
