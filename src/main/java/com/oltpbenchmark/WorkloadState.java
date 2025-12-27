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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 이 클래스는 단일 워크로드의 워커들 간에 상태를 공유하는 데 사용됩니다. 워커는 이를 사용하여 작업을 요청하고 전역 BenchmarkState에 대한 인터페이스로 사용합니다.
 *
 * @author alendit
 */
public class WorkloadState {
  private static final int RATE_QUEUE_LIMIT = 10000;
  private static final Logger LOG = LoggerFactory.getLogger(WorkloadState.class);

  private final BenchmarkState benchmarkState;
  private final LinkedList<SubmittedProcedure> workQueue = new LinkedList<>();
  private final int num_terminals;
  private final Iterator<Phase> phaseIterator;

  private int workersWaiting = 0;

  @SuppressWarnings("unused") // 읽히지 않음
  private int workersWorking = 0;

  private int workerNeedSleep;

  private Phase currentPhase = null;

  public WorkloadState(BenchmarkState benchmarkState, List<Phase> works, int num_terminals) {
    this.benchmarkState = benchmarkState;
    this.num_terminals = num_terminals;
    this.workerNeedSleep = num_terminals;

    phaseIterator = works.iterator();
  }

  /** 작업 요청을 추가합니다. */
  public void addToQueue(int amount, boolean resetQueues) {
    int workAdded = 0;

    synchronized (this) {
      if (resetQueues) {
        workQueue.clear();
      }

      // 단계가 활성화되고 속도 제한이 있는 경우에만 작업 큐를 사용합니다.
      if (currentPhase == null
          || currentPhase.isDisabled()
          || !currentPhase.isRateLimited()
          || currentPhase.isSerial()) {
        return;
      }

      // 지정된 수의 프로시저를 큐 끝에 추가합니다.
      // 현재 속도를 따라갈 수 없으면 트랜잭션을 잘라냅니다
      for (int i = 0; i < amount && workQueue.size() <= RATE_QUEUE_LIMIT; ++i) {
        workQueue.add(new SubmittedProcedure(currentPhase.chooseTransaction()));
        workAdded++;
      }

      // 새로운 작업을 처리하기 위해 잠자는 워커를 깨웁니다.
      int numToWake = Math.min(workAdded, workersWaiting);
      while (numToWake-- > 0) {
        this.notify();
      }
    }
  }

  public void signalDone() {
    int current = this.benchmarkState.signalDone();
    if (current == 0) {
      synchronized (this) {
        if (workersWaiting > 0) {
          this.notifyAll();
        }
      }
    }
  }

  /** 작업을 기다릴 때 ThreadPoolThreads에 의해 호출됩니다. */
  public SubmittedProcedure fetchWork() {
    synchronized (this) {
      if (currentPhase != null && currentPhase.isSerial()) {
        ++workersWaiting;
        while (getGlobalState() == State.LATENCY_COMPLETE) {
          try {
            this.wait();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
        --workersWaiting;

        if (getGlobalState() == State.EXIT || getGlobalState() == State.DONE) {
          return null;
        }

        ++workersWorking;
        return new SubmittedProcedure(
            currentPhase.chooseTransaction(getGlobalState() == State.COLD_QUERY));
      }
    }

    // 무제한 속도 단계는 작업 큐를 사용하지 않습니다.
    if (currentPhase != null && !currentPhase.isRateLimited()) {
      synchronized (this) {
        ++workersWorking;
      }
      return new SubmittedProcedure(
          currentPhase.chooseTransaction(getGlobalState() == State.COLD_QUERY));
    }

    synchronized (this) {
      // 작업이 사용 가능할 때까지 대기합니다.
      if (workQueue.peek() == null) {
        workersWaiting += 1;
        while (workQueue.peek() == null) {
          if (this.benchmarkState.getState() == State.EXIT
              || this.benchmarkState.getState() == State.DONE) {
            return null;
          }

          try {
            this.wait();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
        workersWaiting -= 1;
      }

      ++workersWorking;

      return workQueue.remove();
    }
  }

  public void finishedWork() {
    synchronized (this) {
      --workersWorking;
    }
  }

  public Phase getNextPhase() {
    if (phaseIterator.hasNext()) {
      return phaseIterator.next();
    }
    return null;
  }

  public Phase getCurrentPhase() {
    synchronized (benchmarkState) {
      return currentPhase;
    }
  }

  /*
   * 워커가 이 단계에서 깨어 있어야 하는지 물어보기 위해 호출됩니다
   */
  public void stayAwake() {
    synchronized (this) {
      while (workerNeedSleep > 0) {
        workerNeedSleep--;
        try {
          this.wait();
        } catch (InterruptedException e) {
          LOG.error(e.getMessage(), e);
        }
      }
    }
  }

  public void switchToNextPhase() {
    synchronized (this) {
      this.currentPhase = this.getNextPhase();

      // 이전 단계의 작업을 지웁니다.
      workQueue.clear();

      // 얼마나 많은 워커가 잠들어야 하는지 결정한 다음 그렇게 합니다.
      if (this.currentPhase == null)
      // 벤치마크 종료---모든 사람을 깨워 종료할 수 있도록 합니다
      {
        workerNeedSleep = 0;
      } else {
        this.currentPhase.resetSerial();
        if (this.currentPhase.isDisabled())
        // 단계 비활성화---모든 사람이 잠들어야 합니다
        {
          workerNeedSleep = this.num_terminals;
        } else
        // 단계 실행 중---적절한 수의 터미널을 활성화합니다
        {
          workerNeedSleep = this.num_terminals - this.currentPhase.getActiveTerminals();
        }
      }

      this.notifyAll();
    }
  }

  /** 시작 전 차단을 전역 상태 핸들러에 위임합니다 */
  public void blockForStart() {
    benchmarkState.blockForStart();
  }

  /**
   * 전역 상태 쿼리를 벤치마크 상태 핸들러에 위임합니다
   *
   * @return 전역 상태
   */
  public State getGlobalState() {
    return benchmarkState.getState();
  }

  public void signalLatencyComplete() {

    benchmarkState.signalLatencyComplete();
  }

  public void startColdQuery() {

    benchmarkState.startColdQuery();
  }

  public void startHotQuery() {

    benchmarkState.startHotQuery();
  }

  public long getTestStartNs() {
    return benchmarkState.getTestStartNs();
  }
}
