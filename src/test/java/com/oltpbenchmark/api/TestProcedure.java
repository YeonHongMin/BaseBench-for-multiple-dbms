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
 */

package com.oltpbenchmark.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.oltpbenchmark.benchmarks.tatp.procedures.DeleteCallForwarding;
import com.oltpbenchmark.types.DatabaseType;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class TestProcedure {

  @Before
  public void setUp() throws Exception {}

  /** 프로시저 이름 가져오기 테스트 */
  @Test
  public void testGetProcedureName() throws Exception {
    DeleteCallForwarding proc = new DeleteCallForwarding();
    assertEquals(DeleteCallForwarding.class.getSimpleName(), proc.getProcedureName());
  }

  /** 문장 가져오기 테스트 */
  @Test
  public void testGetStatements() throws Exception {
    Map<String, SQLStmt> stmts = Procedure.getStatements(new DeleteCallForwarding());
    assertNotNull(stmts);
    assertEquals(2, stmts.size());
    //        System.err.println(stmts);
  }

  /** 생성자를 사용한 문장 가져오기 테스트 */
  @Test
  public void testGetStatementsConstructor() throws Exception {
    Procedure proc = new DeleteCallForwarding();
    proc.initialize(DatabaseType.POSTGRES);

    // 프로시저 핸들이 정적 메서드에서 반환하는 것과
    // 동일한 SQLStmts를 가지고 있는지 확인합니다
    Map<String, SQLStmt> expected = Procedure.getStatements(proc);
    assertNotNull(expected);
    //        System.err.println("EXPECTED:" + expected);

    Map<String, SQLStmt> actual = proc.getStatements();
    assertNotNull(actual);
    //        System.err.println("ACTUAL:" + actual);

    assertEquals(expected.size(), actual.size());
    assertEquals(expected, actual);
  }
}
