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

import java.util.ArrayList;
import java.util.Iterator;

/** (시작 시간, 지연 시간) 쌍의 레코드를 효율적으로 저장합니다. */
public class LatencyRecord implements Iterable<LatencyRecord.Sample> {
  /** 한 번에 50만 개의 샘플을 위한 공간 할당 */
  static final int ALLOC_SIZE = 500000;

  /**
   * 마이크로초 형식의 (시작 시간, 지연 시간, 트랜잭션 타입, 워커 ID, 단계 ID) 5중 구조를 포함합니다. 시작 시간은 startNs부터 시작하여 증분으로 인코딩하여
   * "압축"됩니다. 32비트 정수는 2146초 또는 35분 간격에 대해 충분한 해상도를 제공합니다.
   */
  private final ArrayList<Sample[]> values = new ArrayList<>();

  private int nextIndex;

  private final long startNanosecond;
  private long lastNanosecond;

  public LatencyRecord(long startNanosecond) {
    this.startNanosecond = startNanosecond;
    this.lastNanosecond = startNanosecond;
    allocateChunk();
  }

  public void addLatency(
      int transType, long startNanosecond, long endNanosecond, int workerId, int phaseId) {

    if (nextIndex == ALLOC_SIZE) {
      allocateChunk();
    }
    Sample[] chunk = values.get(values.size() - 1);

    long startOffsetNanosecond = (startNanosecond - lastNanosecond + 500);

    int latencyMicroseconds = (int) ((endNanosecond - startNanosecond + 500) / 1000);

    chunk[nextIndex] =
        new Sample(transType, startOffsetNanosecond, latencyMicroseconds, workerId, phaseId);
    ++nextIndex;

    lastNanosecond += startOffsetNanosecond;
  }

  private void allocateChunk() {
    values.add(new Sample[ALLOC_SIZE]);
    nextIndex = 0;
  }

  /** 기록된 샘플 수를 반환합니다. */
  public int size() {
    // 전체 청크에 저장된 샘플
    int samples = (values.size() - 1) * ALLOC_SIZE;

    // 마지막 불완전한 청크에 저장된 샘플
    samples += nextIndex;
    return samples;
  }

  /** 단일 샘플의 시작 시간과 지연 시간을 저장합니다. 불변입니다. */
  public static final class Sample implements Comparable<Sample> {
    private final int transactionType;
    private long startNanosecond;
    private final int latencyMicrosecond;
    private final int workerId;
    private final int phaseId;

    public Sample(
        int transactionType,
        long startNanosecond,
        int latencyMicrosecond,
        int workerId,
        int phaseId) {
      this.transactionType = transactionType;
      this.startNanosecond = startNanosecond;
      this.latencyMicrosecond = latencyMicrosecond;
      this.workerId = workerId;
      this.phaseId = phaseId;
    }

    public int getTransactionType() {
      return transactionType;
    }

    public long getStartNanosecond() {
      return startNanosecond;
    }

    public int getLatencyMicrosecond() {
      return latencyMicrosecond;
    }

    public int getWorkerId() {
      return workerId;
    }

    public int getPhaseId() {
      return phaseId;
    }

    @Override
    public int compareTo(Sample other) {
      long diff = this.startNanosecond - other.startNanosecond;

      // long에서 int로의 오버플로우를 피하기 위한 명시적 비교
      if (diff > 0) {
        return 1;
      } else if (diff < 0) {
        return -1;
      } else {

        return 0;
      }
    }
  }

  private final class LatencyRecordIterator implements Iterator<Sample> {
    private int chunkIndex = 0;
    private int subIndex = 0;
    private long lastIteratorNanosecond = startNanosecond;

    @Override
    public boolean hasNext() {
      if (chunkIndex < values.size() - 1) {
        return true;
      }
      return subIndex < nextIndex;
    }

    @Override
    public Sample next() {
      Sample[] chunk = values.get(chunkIndex);
      Sample s = chunk[subIndex];

      // 청크 내에서 반복하고 다음 청크로 넘어갑니다
      ++subIndex;

      if (subIndex == ALLOC_SIZE) {
        chunkIndex += 1;
        subIndex = 0;
      }

      // 이전에는 s.startNs가 이전 값으로부터의 오프셋이었습니다.
      // 이제 절대값으로 만듭니다.
      s.startNanosecond += lastIteratorNanosecond;
      lastIteratorNanosecond = s.startNanosecond;

      return s;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove is not supported");
    }
  }

  public Iterator<Sample> iterator() {
    return new LatencyRecordIterator();
  }
}
