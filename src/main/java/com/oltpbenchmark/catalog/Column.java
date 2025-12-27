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

import java.util.Objects;

/**
<<<<<<< HEAD
<<<<<<< HEAD
 * Column Catalog Object
=======
 * 테이블 칼럼을 나타내는 카탈로그 객체입니다.
>>>>>>> master
=======
 * 테이블 칼럼을 나타내는 카탈로그 객체입니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 * @author pavlo
 */
public class Column extends AbstractCatalogObject {
  private static final long serialVersionUID = 1L;

  private final Table table;
  private final int type;
  private final Integer size;
  private final boolean nullable;

  private Column foreignKey = null;

  public Column(
      String name, String separator, Table table, int type, Integer size, boolean nullable) {
    super(name, separator);
    this.table = table;
    this.type = type;
    this.size = size;
    this.nullable = nullable;
  }

  public Table getTable() {
    return table;
  }

  public int getType() {
    return type;
  }

  public Integer getSize() {
    return size;
  }

  public boolean isNullable() {
    return nullable;
  }

  public Column getForeignKey() {
    return foreignKey;
  }

  public void setForeignKey(Column foreignKey) {
    this.foreignKey = foreignKey;
  }

  public int getIndex() {
    return this.table.getColumnIndex(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Column column = (Column) o;
    return type == column.type
        && nullable == column.nullable
        && Objects.equals(table, column.table)
        && Objects.equals(size, column.size)
        && Objects.equals(foreignKey, column.foreignKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), table.name, type, size, nullable, foreignKey);
  }
}
