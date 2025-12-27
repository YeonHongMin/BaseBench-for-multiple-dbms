/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Apache License, Version 2.0 (이하 "라이선스")에 따라 라이선스됩니다.
 * 라이선스를 준수하지 않는 한 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 얻을 수 있습니다:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * 적용 가능한 법률에 의해 요구되거나 서면으로 합의하지 않는 한,
 * 라이선스에 따라 배포된 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 종류의 보증이나 조건도 없습니다.
 * 권한 및 제한에 대한 자세한 내용은 라이선스를 참조하세요.
 *
 */

package com.oltpbenchmark;

import com.oltpbenchmark.LatencyRecord.Sample;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.api.collectors.monitoring.Monitor;
import com.oltpbenchmark.api.collectors.monitoring.MonitorGen;
import com.oltpbenchmark.types.State;
import com.oltpbenchmark.util.MonitorInfo;
import com.oltpbenchmark.util.StringUtil;
import java.util.*;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadBench implements Thread.UncaughtExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ThreadBench.class);
  // 모니터링 스레드가 메인 스레드에 다시 합류할 때까지 대기할 시간(밀리초)을 결정합니다.
  private static final int MONITOR_REJOIN_TIME = 60000;

  private final BenchmarkState testState;
  private final List<? extends Worker<? extends BenchmarkModule>> workers;
  private final ArrayList<Thread> workerThreads;
  private final List<WorkloadConfiguration> workConfs;
  private final ArrayList<LatencyRecord.Sample> samples = new ArrayList<>();
  private final MonitorInfo monitorInfo;

  private Monitor monitor = null;

  private ThreadBench(
      List<? extends Worker<? extends BenchmarkModule>> workers,
      List<WorkloadConfiguration> workConfs,
      MonitorInfo monitorInfo) {
    this.workers = workers;
    this.workConfs = workConfs;
    this.workerThreads = new ArrayList<>(workers.size());
    this.monitorInfo = monitorInfo;
    this.testState = new BenchmarkState(workers.size() + 1);
  }

  public static Results runRateLimitedBenchmark(
      List<Worker<? extends BenchmarkModule>> workers,
      List<WorkloadConfiguration> workConfs,
      MonitorInfo monitorInfo) {
    ThreadBench bench = new ThreadBench(workers, workConfs, monitorInfo);
    return bench.runRateLimitedMultiPhase();
  }

  private void createWorkerThreads() {

    for (Worker<?> worker : workers) {
      worker.initializeState();
      Thread thread = new Thread(worker);
      thread.setUncaughtExceptionHandler(this);
      thread.start();
      this.workerThreads.add(thread);
    }
  }

  private void interruptWorkers() {
    for (Worker<?> worker : workers) {
      worker.cancelStatement();
    }
  }

  private int finalizeWorkers(ArrayList<Thread> workerThreads) throws InterruptedException {

    int requests = 0;

    new WatchDogThread().start();

    for (int i = 0; i < workerThreads.size(); ++i) {

      // FIXME 이것이 최선의 해결책인지 확실하지 않습니다... 영원히 멈추지 않도록 보장하지만
      // 문제를 무시할 수 있습니다
      workerThreads.get(i).join(60000); // 스레드가 종료될 때까지 60초 대기
      // 그렇지 않으면 멈춥니다

      /*
       * // CARLO: 멈춰있는 스레드를 종료하기 위해 이것을 하고 싶을 수도 있습니다... if
       * (workerThreads.get(i).isAlive()) { workerThreads.get(i).kill(); try {
       * workerThreads.get(i).join(); } catch (InterruptedException e) { } }
       */

      requests += workers.get(i).getRequests();

      LOG.debug("threadbench calling teardown");

      workers.get(i).tearDown();
    }

    return requests;
  }

  private Results runRateLimitedMultiPhase() {
    boolean errorsThrown = false;
    List<WorkloadState> workStates = new ArrayList<>();

    for (WorkloadConfiguration workState : this.workConfs) {
      workState.initializeState(testState);
      workStates.add(workState.getWorkloadState());
    }

    this.createWorkerThreads();

    // long measureStart = start;
    Phase phase = null;

    // 가장 긴 수면 간격을 결정하는 데 사용됩니다
    double lowestRate = Double.MAX_VALUE;

    for (WorkloadState workState : workStates) {
      workState.switchToNextPhase();
      phase = workState.getCurrentPhase();
      LOG.info(phase.currentPhaseString());
      if (phase.getRate() < lowestRate) {
        lowestRate = phase.getRate();
      }
    }

    // 실행이 순차적이면 testState를 콜드 쿼리로 변경합니다. 순차 실행에는
    // 워밍업 단계가 없지만 콜드 쿼리와 측정 쿼리를 순차적으로 실행하기 때문입니다.
    if (phase != null && phase.isLatencyRun()) {
      synchronized (testState) {
        testState.startColdQuery();
      }
    }

    long startTs = System.currentTimeMillis();
    long start = System.nanoTime();
    long warmupStart = System.nanoTime();
    long warmup = warmupStart;
    long measureEnd = -1;

    long intervalNs = getInterval(lowestRate, phase.getArrival());

    long nextInterval = start + intervalNs;
    int nextToAdd = 1;
    int rateFactor;

    boolean resetQueues = true;

    long delta = phase.getTime() * 1000000000L;
    boolean lastEntry = false;

    // 모니터 초기화
    if (this.monitorInfo.getMonitoringInterval() > 0) {
      this.monitor =
          MonitorGen.getMonitor(
              this.monitorInfo, this.testState, this.workers, this.workConfs.get(0));
      this.monitor.start();
    }

    // 워커가 작업을 시작할 수 있도록 허용합니다.
    testState.blockForStart();

    // 메인 루프
    while (true) {
      // 새로운 작업 게시... 새로운 워크로드 부분이 있는 경우 큐 재설정...

      for (WorkloadState workState : workStates) {
        if (workState.getCurrentPhase() != null) {
          rateFactor = (int) (workState.getCurrentPhase().getRate() / lowestRate);
        } else {
          rateFactor = 1;
        }
        workState.addToQueue(nextToAdd * rateFactor, resetQueues);
      }
      resetQueues = false;

      // 간격이 만료될 때까지 대기합니다. "대기하지 않음"일 수 있습니다
      long now = System.nanoTime();
      if (phase != null) {
        warmup = warmupStart + phase.getWarmupTime() * 1000000000L;
      }
      long diff = nextInterval - now;
      while (diff > 0) { // 조기 깨어날 수 있으므로 여러 번 잠들어 이를 피합니다
        long ms = diff / 1000000;
        diff = diff % 1000000;
        try {
          Thread.sleep(ms, (int) diff);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        now = System.nanoTime();
        diff = nextInterval - now;
      }

      boolean phaseComplete = false;
      if (phase != null) {
        if (phase.isLatencyRun())
        // 지연 시간 실행(각 쿼리를 순차적으로 실행)은 완료를 표시하는 자체 상태를 가집니다
        {
          phaseComplete = testState.getState() == State.LATENCY_COMPLETE;
        } else {
          phaseComplete = testState.getState() == State.MEASURE && (start + delta <= now);
        }
      }

      // 이 단계가 완료되면 다음 단계로 이동하거나 오류가 발생한 경우 진입합니다
      boolean errorThrown = testState.getState() == State.ERROR;
      errorsThrown = errorsThrown || errorThrown;
      if ((phaseComplete || errorThrown) && !lastEntry) {
        // 테스트의 각 단계 후 여기에 진입합니다
        // 새 단계가 이전 단계의 큐에 영향을 받지 않도록 큐를 재설정합니다
        resetQueues = true;

        // 새 단계 가져오기
        synchronized (testState) {
          if (phase.isLatencyRun()) {
            testState.ackLatencyComplete();
          }
          for (WorkloadState workState : workStates) {
            synchronized (workState) {
              workState.switchToNextPhase();
              lowestRate = Integer.MAX_VALUE;
              phase = workState.getCurrentPhase();
              interruptWorkers();
              if (phase == null && !lastEntry) {
                // 마지막 단계
                lastEntry = true;
                testState.startCoolDown();
                measureEnd = now;
                LOG.info(
                    "{} :: Waiting for all terminals to finish ..", StringUtil.bold("TERMINATE"));
              } else if (phase != null) {
                // 순차 실행 매개변수 재설정
                if (phase.isLatencyRun()) {
                  phase.resetSerial();
                  testState.startColdQuery();
                }
                LOG.info(phase.currentPhaseString());
                if (phase.getRate() < lowestRate) {
                  lowestRate = phase.getRate();
                }
              }
            }
          }
          if (phase != null) {
            // 깨어남 속도에 따라 확인 빈도 업데이트
            // intervalNs = (long) (1000000000. / (double)
            // lowestRate + 0.5);
            delta += phase.getTime() * 1000000000L;
          }
        }
      }

      // 다음 간격 계산
      // 및 전달할 메시지 수
      if (phase != null) {
        intervalNs = 0;
        nextToAdd = 0;
        do {
          intervalNs += getInterval(lowestRate, phase.getArrival());
          nextToAdd++;
        } while ((-diff) > intervalNs && !lastEntry);
        nextInterval += intervalNs;
      }

      // 테스트 상태를 적절히 업데이트합니다
      State state = testState.getState();
      if (state == State.WARMUP && now >= warmup) {
        synchronized (testState) {
          if (phase != null && phase.isLatencyRun()) {
            testState.startColdQuery();
          } else {
            testState.startMeasure();
          }
          interruptWorkers();
        }
        start = now;
        LOG.info("{} :: Warmup complete, starting measurements.", StringUtil.bold("MEASURE"));
        // measureEnd = measureStart + measureSeconds * 1000000000L;

        // 순차 실행의 경우, 모든 쿼리를 정확히 한 번씩 실행하려고 합니다.
        // 따라서 일부 쿼리가 워밍업 단계 중에 시작된 경우 재시작해야 합니다.
        // 순차 실행을 하지 않는 경우, 이 함수는 효과가 없으므로
        // 안전하게 호출할 수 있습니다.
        phase.resetSerial();
      } else if (state == State.EXIT) {
        // 모든 스레드가 완료를 인지했습니다. 즉, 모든 측정된
        // 요청이 확실히 완료되었습니다.
        // 종료할 시간입니다.
        break;
      }
    }

    // 모든 워커를 정리하는 것과 별도로 모니터링 스레드를 중지하여
    // 이러한 스레드(가능한 SQLExceptions 포함)의 오류는 무시할 수 있지만 다른 것은 무시할 수 없습니다.
    try {
      if (this.monitor != null) {
        this.monitor.interrupt();
        this.monitor.join(MONITOR_REJOIN_TIME);
        this.monitor.tearDown();
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }

    try {
      int requests = finalizeWorkers(this.workerThreads);

      // 가능한 가장 비효율적인 방법으로 모든 지연 시간을 결합합니다: 정렬!
      for (Worker<?> w : workers) {
        for (LatencyRecord.Sample sample : w.getLatencyRecords()) {
          samples.add(sample);
        }
      }
      Collections.sort(samples);

      // 모든 지연 시간에 대한 통계 계산
      int[] latencies = new int[samples.size()];
      for (int i = 0; i < samples.size(); ++i) {
        latencies[i] = samples.get(i).getLatencyMicrosecond();
      }
      DistributionStatistics stats = DistributionStatistics.computeStatistics(latencies);

      Results results =
          new Results(
              // 실행 중 오류가 발생한 경우, 결과를 출력한 *후*에
              // 0이 아닌 값으로 종료할 수 있도록 최종 Results 상태에 해당 사실을 전파합니다.
              errorsThrown ? State.ERROR : testState.getState(),
              startTs,
              measureEnd - start,
              requests,
              stats,
              samples);

      // 트랜잭션 히스토그램 계산
      Set<TransactionType> txnTypes = new HashSet<>();
      for (WorkloadConfiguration workConf : workConfs) {
        txnTypes.addAll(workConf.getTransTypes());
      }
      txnTypes.remove(TransactionType.INVALID);

      results.getUnknown().putAll(txnTypes, 0);
      results.getSuccess().putAll(txnTypes, 0);
      results.getRetry().putAll(txnTypes, 0);
      results.getAbort().putAll(txnTypes, 0);
      results.getError().putAll(txnTypes, 0);
      results.getRetryDifferent().putAll(txnTypes, 0);

      for (Worker<?> w : workers) {
        results.getUnknown().putHistogram(w.getTransactionUnknownHistogram());
        results.getSuccess().putHistogram(w.getTransactionSuccessHistogram());
        results.getRetry().putHistogram(w.getTransactionRetryHistogram());
        results.getAbort().putHistogram(w.getTransactionAbortHistogram());
        results.getError().putHistogram(w.getTransactionErrorHistogram());
        results.getRetryDifferent().putHistogram(w.getTransactionRetryDifferentHistogram());
      }

      return (results);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private long getInterval(double lowestRate, Phase.Arrival arrival) {
    // TODO 자동 생성된 메서드 스텁
    if (arrival == Phase.Arrival.POISSON) {
      return (long) ((-Math.log(1 - Math.random()) / lowestRate) * 1000000000.);
    } else {
      return (long) (1000000000. / lowestRate + 0.5);
    }
  }

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    // 워커 스레드 중 하나가 종료된 경우를 처리합니다
    LOG.error(e.getMessage(), e);
    // 실험을 계속하지 않습니다. 대신 테스트에 남아있는 나머지
    // 단계를 우회하고 오류 상태를 신호합니다.
    // 실험을 완료하기 위한 나머지 워크플로우는 동일하게 유지되며,
    // 부분 메트릭이 보고됩니다(즉, 실패가 발생할 때까지).
    synchronized (testState) {
      for (WorkloadConfiguration workConf : this.workConfs) {
        synchronized (workConf.getWorkloadState()) {
          WorkloadState workState = workConf.getWorkloadState();
          Phase phase = workState.getCurrentPhase();
          while (phase != null) {
            workState.switchToNextPhase();
            phase = workState.getCurrentPhase();
          }
        }
      }
      testState.signalError();
    }
  }

  public static final class TimeBucketIterable implements Iterable<DistributionStatistics> {
    private final Iterable<Sample> samples;
    private final int windowSizeSeconds;
    private final TransactionType transactionType;

    /**
     * @param samples
     * @param windowSizeSeconds
     * @param transactionType Allows to filter transactions by type
     */
    public TimeBucketIterable(
        Iterable<Sample> samples, int windowSizeSeconds, TransactionType transactionType) {
      this.samples = samples;
      this.windowSizeSeconds = windowSizeSeconds;
      this.transactionType = transactionType;
    }

    @Override
    public Iterator<DistributionStatistics> iterator() {
      return new TimeBucketIterator(samples.iterator(), windowSizeSeconds, transactionType);
    }
  }

  private static final class TimeBucketIterator implements Iterator<DistributionStatistics> {
    private final Iterator<Sample> samples;
    private final int windowSizeSeconds;
    private final TransactionType txType;

    private Sample sample;
    private long nextStartNanosecond;

    private DistributionStatistics next;

    /**
     * @param samples
     * @param windowSizeSeconds
     * @param txType Allows to filter transactions by type
     */
    public TimeBucketIterator(
        Iterator<LatencyRecord.Sample> samples, int windowSizeSeconds, TransactionType txType) {
      this.samples = samples;
      this.windowSizeSeconds = windowSizeSeconds;
      this.txType = txType;

      if (samples.hasNext()) {
        sample = samples.next();
        // TODO: To be totally correct, we would want this to be the
        // timestamp of the start
        // of the measurement interval. In most cases this won't matter.
        nextStartNanosecond = sample.getStartNanosecond();
        calculateNext();
      }
    }

    private void calculateNext() {

      // Collect all samples in the time window
      ArrayList<Integer> latencies = new ArrayList<>();
      long endNanoseconds = nextStartNanosecond + (windowSizeSeconds * 1000000000L);
      while (sample != null && sample.getStartNanosecond() < endNanoseconds) {

        // Check if a TX Type filter is set, in the default case,
        // INVALID TXType means all should be reported, if a filter is
        // set, only this specific transaction
        if (txType.equals(TransactionType.INVALID)
            || txType.getId() == sample.getTransactionType()) {
          latencies.add(sample.getLatencyMicrosecond());
        }

        if (samples.hasNext()) {
          sample = samples.next();
        } else {
          sample = null;
        }
      }

      // Set up the next time window

      nextStartNanosecond = endNanoseconds;

      int[] l = new int[latencies.size()];
      for (int i = 0; i < l.length; ++i) {
        l[i] = latencies.get(i);
      }

      next = DistributionStatistics.computeStatistics(l);
    }

    @Override
    public boolean hasNext() {
      return next != null;
    }

    @Override
    public DistributionStatistics next() {
      if (next == null) {
        throw new NoSuchElementException();
      }
      DistributionStatistics out = next;
      next = null;
      if (sample != null) {
        calculateNext();
      }
      return out;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("unsupported");
    }
  }

  private class WatchDogThread extends Thread {
    {
      this.setDaemon(true);
    }

    @Override
    public void run() {
      Map<String, Object> m = new ListOrderedMap<>();
      LOG.info("Starting WatchDogThread");
      while (true) {
        try {
          Thread.sleep(20000);
        } catch (InterruptedException ex) {
          return;
        }

        m.clear();
        for (Thread t : workerThreads) {
          m.put(t.getName(), t.isAlive());
        }
        LOG.info("Worker Thread Status:\n{}", StringUtil.formatMaps(m));
      }
    }
  }
}
