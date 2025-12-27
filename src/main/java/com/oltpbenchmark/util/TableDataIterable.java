/*
<<<<<<< HEAD
<<<<<<< HEAD
 * Copyright 2020 by OLTPBenchmark Project
 *
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
 * 저작권 2020 OLTPBenchmark 프로젝트
 *
 * Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 * 라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 * 이 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 * 라이선스에서 허용하는 권한과 제한 사항은
 * 라이선스의 본문을 참조하십시오.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.util;

import com.oltpbenchmark.catalog.Column;
import com.oltpbenchmark.catalog.Table;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

<<<<<<< HEAD
<<<<<<< HEAD
/**
 * @author pavlo
 */
=======
/** 작성자: pavlo */
>>>>>>> master
=======
/** 작성자: pavlo */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
public class TableDataIterable implements Iterable<Object[]>, AutoCloseable {
  private final String filePath;
  private final CSVReader reader;
  private final boolean auto_generate_first_column;

  private final int[] types;
  private final boolean[] fkeys;
  private final boolean[] nullable;
  private int line_ctr = 0;

  private InputStream in = null;

  /**
<<<<<<< HEAD
<<<<<<< HEAD
   * Constructor
   *
   * @param filePath
   * @param has_header whether we expect the data file to include a header in the first row
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * 생성자
   *
   * @param filePath
   * @param has_header 데이터 파일의 첫 줄에 헤더가 포함되어 있다고 예상하는지 여부
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * @param auto_generate_first_column TODO
   * @throws Exception
   */
  public TableDataIterable(
      Table catalog_tbl, String filePath, boolean has_header, boolean auto_generate_first_column)
      throws Exception {
    this.filePath = filePath;
    this.auto_generate_first_column = auto_generate_first_column;

    this.types = new int[catalog_tbl.getColumnCount()];
    this.fkeys = new boolean[catalog_tbl.getColumnCount()];
    this.nullable = new boolean[catalog_tbl.getColumnCount()];
    for (int i = 0; i < this.types.length; i++) {
      Column catalog_col = catalog_tbl.getColumn(i);
      this.types[i] = catalog_col.getType();
      this.fkeys[i] = (catalog_col.getForeignKey() != null);
      this.nullable[i] = catalog_col.isNullable();
    }

    in = this.getClass().getResourceAsStream(filePath);
    this.reader = new CSVReader(new InputStreamReader(in));

<<<<<<< HEAD
<<<<<<< HEAD
    // Throw away the first row if there is a header
=======
    // 헤더가 있으면 첫 행을 건너뜁니다.
>>>>>>> master
=======
    // 헤더가 있으면 첫 행을 건너뜁니다.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    if (has_header) {
      this.reader.readNext();
      this.line_ctr++;
    }
  }

  public Iterator<Object[]> iterator() {
    return (new TableIterator());
  }

  @Override
  public void close() throws IOException {
    if (this.in != null) {
      in.close();
    }

    if (this.reader != null) {
      reader.close();
    }
  }

  public class TableIterator implements Iterator<Object[]> {
    String[] next = null;

    private void getNext() {
      if (next == null) {
        try {
          next = reader.readNext();
        } catch (Exception ex) {
          throw new RuntimeException("Unable to retrieve tuples from '" + filePath + "'", ex);
        }
      }
    }

    @Override
    public boolean hasNext() {
      this.getNext();
      return (next != null);
    }

    @Override
    public Object[] next() {
      this.getNext();
      if (next == null) {
        return (next);
      }
      String[] row;
      synchronized (this) {
        row = this.next;
        this.next = null;
      }
      Object[] tuple = new Object[types.length];
      int row_idx = 0;
      for (int col_idx = 0; col_idx < types.length; col_idx++) {
<<<<<<< HEAD
<<<<<<< HEAD
        // Auto-generate first column
        if (col_idx == 0 && auto_generate_first_column) {
          tuple[col_idx] = (long) line_ctr;
        } else if (row_idx >= row.length) {
          // Null values.
          tuple[col_idx] = null;
        } else if (fkeys[col_idx]) {
          // Foreign keys.
          tuple[col_idx] = row[row_idx++];
        }
        // Default: Cast the string into the proper type
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
        // 첫 번째 열을 자동 생성합니다.
        if (col_idx == 0 && auto_generate_first_column) {
          tuple[col_idx] = (long) line_ctr;
        } else if (row_idx >= row.length) {
          // 널 값입니다.
          tuple[col_idx] = null;
        } else if (fkeys[col_idx]) {
          // 외래 키입니다.
          tuple[col_idx] = row[row_idx++];
        }
        // 기본: 문자열을 적절한 타입으로 변환합니다.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
        else {
          if (row[row_idx].isEmpty() && nullable[col_idx]) {
            tuple[col_idx] = null;
          } else {
            try {
              tuple[col_idx] = SQLUtil.castValue(types[col_idx], row[row_idx]);
            } catch (Throwable ex) {
              throw new RuntimeException(
                  String.format(
                      "Line %d: Invalid data '%s' for column #%d",
                      TableDataIterable.this.line_ctr, row[row_idx], col_idx));
            }
          }
          row_idx++;
        }
      }
      TableDataIterable.this.line_ctr++;
      return (tuple);
    }

    @Override
    public void remove() {
      throw new RuntimeException("Unimplemented operation");
    }
  }
}
