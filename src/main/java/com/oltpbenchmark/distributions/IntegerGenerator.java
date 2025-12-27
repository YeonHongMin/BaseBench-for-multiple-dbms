/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Apache License, Version 2.0 (the "License")에 따라 라이선스가 부여됩니다.
 * 이 파일을 사용하려면 라이선스와 일치해야 합니다.
 * 다음에서 라이선스 사본을 얻을 수 있습니다:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 동의하지 않는 한,
 * 라이선스에 따라 배포되는 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적이거나 암묵적인 어떠한 종류의 보증이나 조건도 없습니다.
 * 라이선스에서 허용되는 특정 언어에 대한 권한과
 * 제한 사항을 참조하십시오.
 *
 */

package com.oltpbenchmark.distributions;

/**
 * int와 문자열을 모두 생성할 수 있는 생성기
 *
 * @author cooperb
 */
public abstract class IntegerGenerator extends Generator {
  int lastint;

  /**
   * 생성된 마지막 값을 설정합니다. IntegerGenerator 하위 클래스는 이 호출을 사용하여
   * 마지막 문자열 값을 적절히 설정해야 합니다. 그렇지 않으면 lastString()과 lastInt() 호출이 작동하지 않습니다.
   */
  protected void setLastInt(int last) {
    lastint = last;
  }

  /**
   * 다음 값을 int로 반환합니다. 이 메서드를 재정의할 때 setLastString()을 적절히 호출해야 합니다.
   * 그렇지 않으면 lastString() 호출이 작동하지 않습니다.
   */
  public abstract int nextInt();

  /** 분포에서 다음 문자열을 생성합니다. */
  public String nextString() {
    return "" + nextInt();
  }

  /**
   * 분포에서 이전에 생성된 문자열을 반환합니다. 예를 들어 마지막 nextString() 호출에서 반환된 값입니다.
   * lastString()을 호출해도 분포가 진행되거나 부작용을 일으키지 않아야 합니다.
   * nextString()이 아직 호출되지 않았다면 lastString()은 적절한 값을 반환해야 합니다.
   */
  @Override
  public String lastString() {
    return "" + lastInt();
  }

  /**
   * 분포에서 이전에 생성된 int를 반환합니다. 이 호출은 IntegerGenerator 하위 클래스에 고유하며,
   * IntegerGenerator 하위 클래스가 nextInt()에 대해 항상 int를 반환한다고 가정합니다 (예: 임의의 문자열 아님).
   */
  public int lastInt() {
    return lastint;
  }

  /** 이 생성기가 반환할 값들의 기대값(평균)을 반환합니다. */
  public abstract double mean();
}
