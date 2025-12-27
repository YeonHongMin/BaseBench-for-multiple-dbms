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

/**
 * 이 클래스는 속도 제한 벤치마크를 실행할 때 시스템에 제출된 프로시저를 추적하는 데 사용됩니다.
 *
 * @author breilly
 */
public class SubmittedProcedure {
  private final int type;
  private final long startTime;

  SubmittedProcedure(int type) {
    this.type = type;
    this.startTime = System.nanoTime();
  }

  public int getType() {
    return type;
  }

  public long getStartTime() {
    return startTime;
  }
}
