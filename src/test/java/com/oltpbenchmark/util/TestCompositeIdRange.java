/*
<<<<<<< HEAD
<<<<<<< HEAD
 *  Copyright 2021 by OLTPBenchmark Project
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
=======
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Apache License, Version 2.0 (the "License")에 따라 라이선스가 부여됩니다.
 * 이 파일을 사용하려면 라이선스와 일치해야 합니다.
 * 다음에서 라이선스 사본을 얻을 수 있습니다:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 동의하지 않는 한,
 * 라이선스에 따라 배포되는 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적이거나 암묵적인 어떠한 종류의 보증이나 조건도 없습니다.
 * 라이선스에서 허용되는 특정 언어에 대한 권한과
 * 제한 사항을 참조하십시오.
 *
>>>>>>> master
=======
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
 *
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 */

package com.oltpbenchmark.util;

import static org.junit.Assert.assertEquals;

import java.util.Objects;
import org.junit.Test;

public class TestCompositeIdRange {

  public static class PackedLong extends CompositeId {
    private static final int[] COMPOSITE_BITS = {
      INT_MAX_DIGITS, // FIELD1
      LONG_MAX_DIGITS, // FIELD2
    };

    protected int field1;
    protected long field2;

    public PackedLong() {}

    public PackedLong(int field1, long field2) {
      this.field1 = field1;
      this.field2 = field2;
    }

    @Override
    public String encode() {
      return (this.encode(COMPOSITE_BITS));
    }

    @Override
    public void decode(String composite_id) {
      String[] values = super.decode(composite_id, COMPOSITE_BITS);
      this.field1 = Integer.parseInt(values[0]);
      this.field2 = Long.parseLong(values[1]);
    }

    @Override
    public String[] toArray() {
      return (new String[] {Integer.toString(this.field1), Long.toString(this.field2)});
    }

    public int getField1() {
      return this.field1;
    }

    public long getField2() {
      return this.field2;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      PackedLong that = (PackedLong) o;
      return field1 == that.field1 && field2 == that.field2;
    }

    @Override
    public int hashCode() {
      return Objects.hash(field1, field2);
    }
  }

  @Test
  public void testPackOK() {
    PackedLong packedLong = new PackedLong(1, 2);
    String encodedLong = packedLong.encode();
    PackedLong packedLong2 = new PackedLong();
    packedLong2.decode(encodedLong);
    assertEquals(packedLong.getField1(), packedLong2.getField1());
    assertEquals(packedLong.getField2(), packedLong2.getField2());
  }
}
