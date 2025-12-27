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

package com.oltpbenchmark.catalog;

import com.oltpbenchmark.types.SortDirectionType;
import java.io.Serializable;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/** 인덱스 정보를 담는 카탈로그 객체입니다. */
public class Index extends AbstractCatalogObject {
  private static final long serialVersionUID = 1L;

  private final Table table;
  private final TreeMap<Integer, IndexColumn> columns = new TreeMap<>();
  private final int type;
  private final boolean unique;

  static class IndexColumn implements Serializable {
    static final long serialVersionUID = 0;

    private final String name;
    private final SortDirectionType dir;

    IndexColumn(String name, SortDirectionType dir) {
      this.name = name;
      this.dir = dir;
    }

    public String getName() {
      return name;
    }

    public SortDirectionType getDir() {
      return dir;
    }
  }

  public Index(String name, String separator, Table table, int type, boolean unique) {
    super(name, separator);
    this.table = table;
    this.type = type;
    this.unique = unique;
  }

  public void addColumn(String colName, SortDirectionType colOrder, int colPosition) {
    this.columns.put(colPosition, new IndexColumn(colName, colOrder));
  }

  public Table getTable() {
    return table;
  }

  public SortedMap<Integer, IndexColumn> getColumns() {
    return columns;
  }

  public int getType() {
    return type;
  }

  public boolean isUnique() {
    return unique;
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
    Index index = (Index) o;
    return type == index.type
        && unique == index.unique
        && Objects.equals(table, index.table)
        && Objects.equals(columns, index.columns);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), table.name, columns, type, unique);
  }
}
