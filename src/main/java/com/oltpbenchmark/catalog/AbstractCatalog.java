<<<<<<< HEAD
=======
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

>>>>>>> master
package com.oltpbenchmark.catalog;

import java.sql.SQLException;
import java.util.Collection;

/**
<<<<<<< HEAD
 * An abstraction over a database's catalog.
 *
 * <p>Concretely, this abstraction supports two types of catalogs: - "real" catalogs, which query
 * the table directly; - catalogs backed by an in-memory instance of HSQLDB, in case the DBMS is
 * unable to support certain SQL queries.
=======
 * 데이터베이스 카탈로그를 추상화합니다.
 *
 * <p>실제 테이블을 직접 조회하는 "실제" 카탈로그와, DBMS가 특정 SQL을 지원하지 못할 때 메모리 기반 HSQLDB로 대체되는 카탈로그 두 종류를 지원합니다.
>>>>>>> master
 */
public interface AbstractCatalog {
  Collection<Table> getTables();

  Table getTable(String tableName);

  void close() throws SQLException;
}
