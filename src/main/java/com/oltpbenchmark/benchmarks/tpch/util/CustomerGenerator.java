/*
 * Copyright 2020 Trino
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
 */
package com.oltpbenchmark.benchmarks.tpch.util;

import static com.oltpbenchmark.benchmarks.tpch.util.GenerateUtils.calculateRowCount;
import static com.oltpbenchmark.benchmarks.tpch.util.GenerateUtils.calculateStartIndex;
import static java.util.Locale.ENGLISH;
import static java.util.Objects.requireNonNull;

import com.oltpbenchmark.util.RowRandomBoundedInt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomerGenerator implements Iterable<List<Object>> {
  public static final int SCALE_BASE = 150_000;
  private static final int ACCOUNT_BALANCE_MIN = -99999;
  private static final int ACCOUNT_BALANCE_MAX = 999999;
  private static final int ADDRESS_AVERAGE_LENGTH = 25;
  private static final int COMMENT_AVERAGE_LENGTH = 73;

  private final double scaleFactor;
  private final int part;
  private final int partCount;

  private final Distributions distributions;
  private final TextPool textPool;

  public CustomerGenerator(double scaleFactor, int part, int partCount) {
    this(
        scaleFactor,
        part,
        partCount,
        Distributions.getDefaultDistributions(),
        TextPool.getDefaultTestPool());
  }

  public CustomerGenerator(
      double scaleFactor, int part, int partCount, Distributions distributions, TextPool textPool) {
    this.scaleFactor = scaleFactor;
    this.part = part;
    this.partCount = partCount;

    this.distributions = requireNonNull(distributions, "distributions is null");
    this.textPool = requireNonNull(textPool, "textPool is null");
  }

  @Override
  public Iterator<List<Object>> iterator() {
    return new CustomerGeneratorIterator(
        distributions,
        textPool,
        calculateStartIndex(SCALE_BASE, scaleFactor, part, partCount),
        calculateRowCount(SCALE_BASE, scaleFactor, part, partCount));
  }

  private static class CustomerGeneratorIterator implements Iterator<List<Object>> {
    private final TPCHRandomAlphaNumeric addressRandom =
        new TPCHRandomAlphaNumeric(881155353L, ADDRESS_AVERAGE_LENGTH);
    private final RowRandomBoundedInt nationKeyRandom;
    private final TPCHRandomPhoneNumber phoneRandom = new TPCHRandomPhoneNumber(1521138112L);
    private final RowRandomBoundedInt accountBalanceRandom =
        new RowRandomBoundedInt(298370230L, ACCOUNT_BALANCE_MIN, ACCOUNT_BALANCE_MAX);
    private final TPCHRandomString marketSegmentRandom;
    private final TPCHRandomText commentRandom;

    private final long startIndex;
    private final long rowCount;

    private long index;

    private CustomerGeneratorIterator(
        Distributions distributions, TextPool textPool, long startIndex, long rowCount) {
      this.startIndex = startIndex;
      this.rowCount = rowCount;

      nationKeyRandom =
          new RowRandomBoundedInt(1489529863L, 0, distributions.getNations().size() - 1);
      marketSegmentRandom = new TPCHRandomString(1140279430L, distributions.getMarketSegments());
      commentRandom = new TPCHRandomText(1335826707L, textPool, COMMENT_AVERAGE_LENGTH);

      addressRandom.advanceRows(startIndex);
      nationKeyRandom.advanceRows(startIndex);
      phoneRandom.advanceRows(startIndex);
      accountBalanceRandom.advanceRows(startIndex);
      marketSegmentRandom.advanceRows(startIndex);
      commentRandom.advanceRows(startIndex);
    }

    @Override
    public boolean hasNext() {
      return index < rowCount;
    }

    @Override
    public List<Object> next() {
      List<Object> customer = makeCustomer(startIndex + index + 1);

      addressRandom.rowFinished();
      nationKeyRandom.rowFinished();
      phoneRandom.rowFinished();
      accountBalanceRandom.rowFinished();
      marketSegmentRandom.rowFinished();
      commentRandom.rowFinished();

      index++;

      return customer;
    }

    private List<Object> makeCustomer(long customerKey) {
      long nationKey = nationKeyRandom.nextValue();

      List<Object> customer = new ArrayList<>();

      customer.add(customerKey);
      customer.add(String.format(ENGLISH, "Customer#%09d", customerKey));
      customer.add(addressRandom.nextValue());
      customer.add(nationKey);
      customer.add(phoneRandom.nextValue(nationKey));
      customer.add((double) accountBalanceRandom.nextValue() / 100.);
      customer.add(marketSegmentRandom.nextValue());
      customer.add(commentRandom.nextValue());

      return customer;
    }
  }
}
