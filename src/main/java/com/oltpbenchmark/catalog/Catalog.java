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
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한 및 조건을 준수해 주세요.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.catalog;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

<<<<<<< HEAD
<<<<<<< HEAD
/**
 * @author pavlo
 */
=======
/** 등록된 테이블 맵을 기반으로 카탈로그를 제공하는 구현체입니다. */
>>>>>>> master
=======
/** 등록된 테이블 맵을 기반으로 카탈로그를 제공하는 구현체입니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
public final class Catalog implements AbstractCatalog {

  private final Map<String, Table> tables;

  public Catalog(Map<String, Table> tables) {
    this.tables = tables;
  }

  @Override
  public Collection<Table> getTables() {
    return (this.tables.values());
  }

  @Override
  public Table getTable(String tableName) {
    for (Table table : tables.values()) {
      if (table.getName().equalsIgnoreCase(tableName)) {
        return table;
      }
    }
    throw new IllegalArgumentException(String.format("no table found with name [%s]", tableName));
  }

  @Override
  public void close() throws SQLException {
    // No-op.
  }
}
