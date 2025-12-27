/*
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *  Copyright 2015 by OLTPBenchmark Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
<<<<<<< HEAD
=======
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
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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

<<<<<<< HEAD
<<<<<<< HEAD
  /** testGetProcedureName */
=======
  /** 프로시저 이름 가져오기 테스트 */
>>>>>>> master
=======
  /** testGetProcedureName */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @Test
  public void testGetProcedureName() throws Exception {
    DeleteCallForwarding proc = new DeleteCallForwarding();
    assertEquals(DeleteCallForwarding.class.getSimpleName(), proc.getProcedureName());
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** testGetStatements */
=======
  /** 문장 가져오기 테스트 */
>>>>>>> master
=======
  /** testGetStatements */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @Test
  public void testGetStatements() throws Exception {
    Map<String, SQLStmt> stmts = Procedure.getStatements(new DeleteCallForwarding());
    assertNotNull(stmts);
    assertEquals(2, stmts.size());
    //        System.err.println(stmts);
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** testGetStatementsConstructor */
=======
  /** 생성자를 사용한 문장 가져오기 테스트 */
>>>>>>> master
=======
  /** testGetStatementsConstructor */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  @Test
  public void testGetStatementsConstructor() throws Exception {
    Procedure proc = new DeleteCallForwarding();
    proc.initialize(DatabaseType.POSTGRES);

<<<<<<< HEAD
<<<<<<< HEAD
    // Make sure that procedure handle has the same
    // SQLStmts as what we get back from the static method
=======
    // 프로시저 핸들이 정적 메서드에서 반환하는 것과
    // 동일한 SQLStmts를 가지고 있는지 확인합니다
>>>>>>> master
=======
    // Make sure that procedure handle has the same
    // SQLStmts as what we get back from the static method
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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
