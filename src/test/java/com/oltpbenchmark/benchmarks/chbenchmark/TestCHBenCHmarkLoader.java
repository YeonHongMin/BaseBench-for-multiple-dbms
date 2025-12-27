/*
 * Copyright 2015 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 별도 합의가 없다면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다. 라이선스가 허용하는 범위 내에서만 사용하세요.
 *
 */

package com.oltpbenchmark.benchmarks.chbenchmark;

import com.oltpbenchmark.api.AbstractTestLoader;
import com.oltpbenchmark.api.Procedure;
import java.util.List;
import org.junit.Ignore;

@Ignore("테스트 케이스가 개발 중입니다")
public class TestCHBenCHmarkLoader extends AbstractTestLoader<CHBenCHmark> {

  @Override
  public List<Class<? extends Procedure>> procedures() {
    return TestCHBenCHmark.PROCEDURE_CLASSES;
  }

  @Override
  public Class<CHBenCHmark> benchmarkClass() {
    return CHBenCHmark.class;
  }
}
