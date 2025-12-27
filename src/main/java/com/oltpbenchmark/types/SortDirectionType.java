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
 *
 */

/* This file is part of VoltDB.
 * Copyright (C) 2008-2010 VoltDB L.L.C.
 *
 * VoltDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VoltDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VoltDB.  If not, see <http://www.gnu.org/licenses/>.
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
 *
 */

/* 이 파일은 VoltDB의 일부입니다.
 * Copyright (C) 2008-2010 VoltDB L.L.C.
 *
 * VoltDB는 자유 소프트웨어로서 다음 조건에 따라 재배포하거나 수정할 수 있습니다:
 * Free Software Foundation이 발표한 GNU General Public License 버전 3 또는 그 이후 버전.
 *
 * VoltDB는 유용하게 사용되기를 바라며 배포되지만, 어떠한 보증도 없이,
 * 상품성이나 특정 목적에의 적합성에 대한 묵시적 보증조차 제공되지 않습니다.
 * 자세한 내용은 GNU General Public License를 참조하십시오.
 *
 * VoltDB와 함께 GNU General Public License 사본을 받아야 합니다.
 * 받지 못했다면 <http://www.gnu.org/licenses/>를 확인하십시오.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 */

package com.oltpbenchmark.types;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

<<<<<<< HEAD
<<<<<<< HEAD
/** */
=======
/** 정렬 방향을 나타내는 열거형입니다. */
>>>>>>> master
=======
/** 정렬 방향을 나타내는 열거형입니다. */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
public enum SortDirectionType {
  INVALID(0),
  ASC(1),
  DESC(2);

  SortDirectionType(int val) {}

  public int getValue() {
    return this.ordinal();
  }

  protected static final Map<Integer, SortDirectionType> idx_lookup = new HashMap<>();
  protected static final Map<String, SortDirectionType> name_lookup = new HashMap<>();

  static {
    for (SortDirectionType vt : EnumSet.allOf(SortDirectionType.class)) {
      SortDirectionType.idx_lookup.put(vt.ordinal(), vt);
      SortDirectionType.name_lookup.put(vt.name().toLowerCase().intern(), vt);
    }
  }

  public static SortDirectionType get(Integer idx) {

    SortDirectionType ret = SortDirectionType.idx_lookup.get(idx);
    return (ret == null ? SortDirectionType.INVALID : ret);
  }

  public static SortDirectionType get(String name) {
    SortDirectionType ret = SortDirectionType.name_lookup.get(name.toLowerCase().intern());
    return (ret == null ? SortDirectionType.INVALID : ret);
  }
}
