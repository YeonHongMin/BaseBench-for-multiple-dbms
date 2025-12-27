/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한 및 조건을 준수해 주세요.
 *
 */

package com.oltpbenchmark.distributions;

/** 특정 분포(Uniform, Zipfian, Sequential 등)를 따르는 문자열 시퀀스를 생성하는 표현입니다. */
public abstract class Generator {
  /** 분포에서 다음 문자열을 생성합니다. */
  public abstract String nextString();

  /**
   * 분포가 마지막으로 생성한 문자열(마지막 nextString() 호출 값)을 반환합니다. lastString()을 호출해도 분포 상태는 변화하거나 부작용을 일으키지
   * 않습니다. nextString()을 아직 호출하지 않았다면 적절한 값을 반환해야 합니다.
   */
  public abstract String lastString();
}
