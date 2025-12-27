/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Apache License, Version 2.0 (이하 "라이선스")에 따라 라이선스됩니다.
 * 라이선스를 준수하지 않는 한 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 얻을 수 있습니다:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 적용 가능한 법률에 의해 요구되거나 서면으로 합의하지 않는 한,
 * 라이선스에 따라 배포된 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 종류의 보증이나 조건도 없습니다.
 * 권한 및 제한에 대한 자세한 내용은 라이선스를 참조하세요.
 *
 */

package com.oltpbenchmark;

import com.oltpbenchmark.LatencyRecord.Sample;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.types.State;
import com.oltpbenchmark.util.Histogram;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Results {

  private final State state;
  private final long startTimestampMs;
  private final long nanoseconds;
  private final int measuredRequests;
  private final DistributionStatistics distributionStatistics;
  private final List<LatencyRecord.Sample> latencySamples;
  private final Histogram<TransactionType> unknown = new Histogram<>(false);
  private final Histogram<TransactionType> success = new Histogram<>(true);
  private final Histogram<TransactionType> abort = new Histogram<>(false);
  private final Histogram<TransactionType> retry = new Histogram<>(false);
  private final Histogram<TransactionType> error = new Histogram<>(false);
  private final Histogram<TransactionType> retryDifferent = new Histogram<>(false);
  private final Map<TransactionType, Histogram<String>> abortMessages = new HashMap<>();

  public Results(
      State state,
      long startTimestampMs,
      long elapsedNanoseconds,
      int measuredRequests,
      DistributionStatistics distributionStatistics,
      final List<LatencyRecord.Sample> latencySamples) {
    this.startTimestampMs = startTimestampMs;
    this.nanoseconds = elapsedNanoseconds;
    this.measuredRequests = measuredRequests;
    this.distributionStatistics = distributionStatistics;
    this.state = state;

    if (distributionStatistics == null) {
      this.latencySamples = null;
    } else {
      // 방어적 복사
      this.latencySamples = List.copyOf(latencySamples);
    }
  }

  public State getState() {
    return state;
  }

  public DistributionStatistics getDistributionStatistics() {
    return distributionStatistics;
  }

  public Histogram<TransactionType> getSuccess() {
    return success;
  }

  public Histogram<TransactionType> getUnknown() {
    return unknown;
  }

  public Histogram<TransactionType> getAbort() {
    return abort;
  }

  public Histogram<TransactionType> getRetry() {
    return retry;
  }

  public Histogram<TransactionType> getError() {
    return error;
  }

  public Histogram<TransactionType> getRetryDifferent() {
    return retryDifferent;
  }

  public Map<TransactionType, Histogram<String>> getAbortMessages() {
    return abortMessages;
  }

  public double requestsPerSecondThroughput() {
    return (double) measuredRequests / (double) nanoseconds * 1e9;
  }

  public double requestsPerSecondGoodput() {
    return (double) success.getSampleCount() / (double) nanoseconds * 1e9;
  }

  public List<Sample> getLatencySamples() {
    return latencySamples;
  }

  public long getStartTimestampMs() {
    return startTimestampMs;
  }

  public long getNanoseconds() {
    return nanoseconds;
  }

  public int getMeasuredRequests() {
    return measuredRequests;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Results(state=");
    sb.append(state);
    sb.append(", nanoSeconds=");
    sb.append(nanoseconds);
    sb.append(", measuredRequests=");
    sb.append(measuredRequests);
    sb.append(") = ");
    sb.append(requestsPerSecondThroughput());
    sb.append(" requests/sec (throughput)");
    sb.append(", ");
    sb.append(requestsPerSecondGoodput());
    sb.append(" requests/sec (goodput)");
    return sb.toString();
  }
}
