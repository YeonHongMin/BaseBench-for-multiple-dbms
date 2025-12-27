<<<<<<< HEAD
=======
/*
 *  저작권 2015 OLTPBenchmark 프로젝트
 *
 *  Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 *  라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 *  라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 *  이 소프트웨어는 "있는 그대로" 배포되며,
 *  명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 *  라이선스에서 허용하는 권한과 제한 사항은
 *  라이선스의 본문을 참조하십시오.
 *
 */

>>>>>>> master
package com.oltpbenchmark.benchmarks.twitter;

import com.oltpbenchmark.api.AbstractTestWorker;
import com.oltpbenchmark.api.Procedure;
import java.util.List;
import org.junit.Ignore;

<<<<<<< HEAD
@Ignore("the testcase is under development")
=======
@Ignore("테스트 케이스가 개발 중입니다")
>>>>>>> master
public class TestTwitterWorker extends AbstractTestWorker<TwitterBenchmark> {

  @Override
  public List<Class<? extends Procedure>> procedures() {
    return TestTwitterBenchmark.PROCEDURE_CLASSES;
  }

  @Override
  public Class<TwitterBenchmark> benchmarkClass() {
    return TwitterBenchmark.class;
  }
}
