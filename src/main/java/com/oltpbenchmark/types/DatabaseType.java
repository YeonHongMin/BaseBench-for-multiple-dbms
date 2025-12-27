/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
<<<<<<< HEAD
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 있지 않는 한,
 * 이 소프트웨어는 "있는 그대로" 제공되며 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한 및 조건을 반드시 따르십시오.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.types;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
<<<<<<< HEAD
<<<<<<< HEAD
 * List of the database management systems that we support in the framework.
=======
 * 프레임워크가 지원하는 데이터베이스 관리 시스템 목록입니다.
>>>>>>> master
=======
 * 프레임워크가 지원하는 데이터베이스 관리 시스템 목록입니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 * @author pavlo
 */
public enum DatabaseType {
  AMAZONRDS(true, false),
  CASSANDRA(true, true),
  COCKROACHDB(false, false, true),
  DB2(true, false),
  H2(true, false),
  HSQLDB(false, false),
  POSTGRES(false, false, true, true),
  MARIADB(true, false),
  MONETDB(false, false),
  MYROCKS(true, false),
  MYSQL(true, false),
  NOISEPAGE(false, false),
  NUODB(true, false),
  ORACLE(true, false),
  SINGLESTORE(true, false),
  SPANNER(false, true),
  SQLAZURE(true, true, true),
  SQLITE(true, false),
  SQLSERVER(true, true, true, true),
<<<<<<< HEAD
<<<<<<< HEAD
=======
  TIBERO(true, false),
>>>>>>> master
=======
  TIBERO(true, false),
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  TIMESTEN(true, false),
  PHOENIX(true, true);

  DatabaseType(
      boolean escapeNames,
      boolean includeColNames,
      boolean loadNeedsUpdateColumnSequence,
      boolean needsMonitoringPrefix) {
    this.escapeNames = escapeNames;
    this.includeColNames = includeColNames;
    this.loadNeedsUpdateColumnSequence = loadNeedsUpdateColumnSequence;
    this.needsMonitoringPrefix = needsMonitoringPrefix;
  }

  DatabaseType(
      boolean escapeNames, boolean includeColNames, boolean loadNeedsUpdateColumnSequence) {
    this(escapeNames, includeColNames, loadNeedsUpdateColumnSequence, false);
  }

  DatabaseType(boolean escapeNames, boolean includeColNames) {
    this(escapeNames, includeColNames, false, false);
  }

<<<<<<< HEAD
<<<<<<< HEAD
  /** If this flag is set to true, then the framework will escape names in the INSERT queries */
  private final boolean escapeNames;

  /**
   * If this flag is set to true, then the framework will include the column names when generating
   * INSERT queries for loading data.
   */
  private final boolean includeColNames;

  /**
   * If this flag is set to true, the framework will attempt to update the column sequence after
   * loading data.
   */
  private final boolean loadNeedsUpdateColumnSequence;

  /** If this flag is set to true, the framework will add a monitoring prefix to each query. */
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  /** 이 플래그가 true이면 프레임워크가 INSERT 쿼리에서 이름을 이스케이프합니다. */
  private final boolean escapeNames;

  /** 이 플래그가 true이면 데이터를 로드하기 위한 INSERT 쿼리 생성 시 컬럼 이름을 포함합니다. */
  private final boolean includeColNames;

  /** 이 플래그가 true이면 데이터를 로드한 뒤 컬럼 순서를 갱신하도록 시도합니다. */
  private final boolean loadNeedsUpdateColumnSequence;

  /** 이 플래그가 true이면 프레임워크가 각 쿼리에 모니터링 접두사를 추가합니다. */
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  private final boolean needsMonitoringPrefix;

  // ---------------------------------------------------------------
  // ACCESSORS
  // ----------------------------------------------------------------

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * @return True if the framework should escape the names of columns/tables when generating SQL to
   *     load in data for the target database type.
=======
   * @return 대상 DB 유형으로 데이터를 로드할 SQL을 생성할 때 컬럼/테이블 이름을 이스케이프해야 하면 true
>>>>>>> master
=======
   * @return 대상 DB 유형으로 데이터를 로드할 SQL을 생성할 때 컬럼/테이블 이름을 이스케이프해야 하면 true
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   */
  public boolean shouldEscapeNames() {
    return (this.escapeNames);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * @return True if the framework should include the names of columns when generating SQL to load
   *     in data for the target database type.
=======
   * @return 대상 DB 유형으로 데이터를 로드할 SQL을 생성할 때 컬럼 이름을 포함해야 하면 true
>>>>>>> master
=======
   * @return 대상 DB 유형으로 데이터를 로드할 SQL을 생성할 때 컬럼 이름을 포함해야 하면 true
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   */
  public boolean shouldIncludeColumnNames() {
    return (this.includeColNames);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * @return True if the framework should attempt to update the column sequence after loading data.
=======
   * @return 데이터를 로드한 후 컬럼 순서를 갱신해야 하면 true
>>>>>>> master
=======
   * @return 데이터를 로드한 후 컬럼 순서를 갱신해야 하면 true
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   */
  public boolean shouldUpdateColumnSequenceAfterLoad() {
    return (this.loadNeedsUpdateColumnSequence);
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * @return True if the framework should add a monitoring prefix to each query.
=======
   * @return 각 쿼리에 모니터링 접두사를 추가해야 하면 true
>>>>>>> master
=======
   * @return 각 쿼리에 모니터링 접두사를 추가해야 하면 true
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   */
  public boolean shouldCreateMonitoringPrefix() {
    return (this.needsMonitoringPrefix);
  }

  // ----------------------------------------------------------------
  // STATIC METHODS + MEMBERS
  // ----------------------------------------------------------------

  protected static final Map<Integer, DatabaseType> idx_lookup = new HashMap<>();
  protected static final Map<String, DatabaseType> name_lookup = new HashMap<>();

  static {
    for (DatabaseType vt : EnumSet.allOf(DatabaseType.class)) {
      DatabaseType.idx_lookup.put(vt.ordinal(), vt);
      DatabaseType.name_lookup.put(vt.name().toUpperCase(), vt);
    }
  }

  public static DatabaseType get(String name) {
    return (DatabaseType.name_lookup.get(name.toUpperCase()));
  }
}
