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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
<<<<<<< HEAD
<<<<<<< HEAD
 * Table Catalog Object
=======
 * 테이블 정의와 컬럼/인덱스를 담는 카탈로그 객체입니다.
>>>>>>> master
=======
 * 테이블 정의와 컬럼/인덱스를 담는 카탈로그 객체입니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 * @author Carlo A. Curino (carlo@curino.us)
 * @author pavlo
 * @author Djellel
 */
public class Table extends AbstractCatalogObject {
  private static final long serialVersionUID = 1L;

  private final ArrayList<Column> columns = new ArrayList<>();
  private final ArrayList<Index> indexes = new ArrayList<>();

  public Table(String name, String separator) {
    super(name, separator);
  }

  public void addColumn(Column col) {
    this.columns.add(col);
  }

  public int getColumnCount() {
    return this.columns.size();
  }

  public List<Column> getColumns() {
    return Collections.unmodifiableList(this.columns);
  }

  public Column getColumn(int index) {
    return this.columns.get(index);
  }

  public int[] getColumnTypes() {
    int[] types = new int[this.getColumnCount()];
    for (Column catalog_col : this.getColumns()) {
      types[catalog_col.getIndex()] = catalog_col.getType();
    }
    return (types);
  }

  public Column getColumnByName(String colname) {
    int idx = getColumnIndex(colname);
    return (idx >= 0 ? this.columns.get(idx) : null);
  }

  public int getColumnIndex(Column catalog_col) {
    return (this.getColumnIndex(catalog_col.getName()));
  }

  public int getColumnIndex(String columnName) {
    for (int i = 0, cnt = getColumnCount(); i < cnt; i++) {
      if (this.columns.get(i).getName().equalsIgnoreCase(columnName)) {
        return (i);
      }
    }
    return -1;
  }

  public void addIndex(Index index) {
    this.indexes.add(index);
  }

  public Index getIndex(String indexName) {
    for (Index catalog_idx : this.indexes) {
      if (catalog_idx.getName().equalsIgnoreCase(indexName)) {
        return (catalog_idx);
      }
    }
    return (null);
  }

  public List<Index> getIndexes() {
    return this.indexes;
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
    Table table = (Table) o;
    return Objects.equals(columns, table.columns) && Objects.equals(indexes, table.indexes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), columns, indexes);
  }
}
