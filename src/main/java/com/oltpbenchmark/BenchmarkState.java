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

import com.oltpbenchmark.types.State;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BenchmarkState {

  private static final Logger LOG = LoggerFactory.getLogger(BenchmarkState.class);

  private final long testStartNs;
  private final CountDownLatch startBarrier;
  private final AtomicInteger notDoneCount;
  private volatile State state = State.WARMUP;

  /**
   * @param numThreads 테스트에 참여하는 스레드 수: 마스터 스레드를 포함합니다.
   */
  public BenchmarkState(int numThreads) {
    startBarrier = new CountDownLatch(numThreads);
    notDoneCount = new AtomicInteger(numThreads);

    testStartNs = System.nanoTime();
  }

  // this로 보호됨

  public long getTestStartNs() {
    return testStartNs;
  }

  public State getState() {
    synchronized (this) {
      return state;
    }
  }

  /** 모든 스레드가 이를 호출할 때까지 대기합니다. 모든 스레드가 진입하면 반환됩니다. */
  public void blockForStart() {

    startBarrier.countDown();
    try {
      startBarrier.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void startMeasure() {
    state = State.MEASURE;
  }

  public void startColdQuery() {
    state = State.COLD_QUERY;
  }

  public void startHotQuery() {
    state = State.MEASURE;
  }

  public void signalLatencyComplete() {
    state = State.LATENCY_COMPLETE;
  }

  public void ackLatencyComplete() {
    state = State.MEASURE;
  }

  public void signalError() {
    // 스레드가 종료되었으므로 카운트를 감소시키고 오류 상태로 설정
    notDoneCount.decrementAndGet();
    state = State.ERROR;
  }

  public void startCoolDown() {
    state = State.DONE;

    // 마스터 스레드도 완료 신호를 보내야 합니다
    signalDone();
  }

  /** 이 스레드가 완료 상태에 진입했음을 알립니다. */
  public int signalDone() {

    int current = notDoneCount.decrementAndGet();

    if (LOG.isDebugEnabled()) {
      LOG.debug(String.format("%d workers are not done. Waiting until they finish", current));
    }
    if (current == 0) {
      // 우리가 완료를 마지막으로 인지한 스레드입니다: 차단된 모든
      // 워커를 깨웁니다
      this.state = State.EXIT;
    }
    return current;
  }
}
