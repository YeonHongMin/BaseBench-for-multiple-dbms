/*
 * Copyright 2020 by OLTPBenchmark Project
 *
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
 *
 */

package com.oltpbenchmark.benchmarks.ycsb;

public abstract class YCSBConstants {

  public static final int RECORD_COUNT = 1000;

  public static final int NUM_FIELDS = 10;

  /**
   * USERTABLE에서 각 필드의 최대 크기입니다. 참고: 코드에서 이 값을 증가시키면 모든 DDL 파일을 업데이트해야 합니다.
   */
  public static final int MAX_FIELD_SIZE = 100; // chars

  /** 각 스레드가 로드할 레코드 수입니다. */
  public static final int THREAD_BATCH_SIZE = 50000;

  public static final int MAX_SCAN = 1000;

  public static final String TABLE_NAME = "usertable";
}
