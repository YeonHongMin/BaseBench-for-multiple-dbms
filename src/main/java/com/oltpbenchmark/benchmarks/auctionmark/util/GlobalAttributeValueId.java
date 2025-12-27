/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
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
<<<<<<< HEAD
=======
 * Apache License, Version 2.0 (이하 "라이센스")에 따라 라이센스가 부여됩니다.
 * 이 파일은 라이센스에 따라 사용할 수 있으며, 라이센스에 따라 사용하지 않는 한
 * 사용할 수 없습니다. 라이센스 사본은 다음에서 얻을 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 적용 가능한 법률에 의해 요구되거나 서면으로 합의되지 않는 한, 라이센스에 따라
 * 배포되는 소프트웨어는 "있는 그대로" 배포되며, 명시적이거나 묵시적인 어떠한 종류의
 * 보증이나 조건도 없습니다. 라이센스에 따른 권한 및 제한 사항에 대한 자세한 내용은
 * 라이센스를 참조하십시오.
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.benchmarks.auctionmark.util;

import com.oltpbenchmark.util.CompositeId;
import java.util.Comparator;
import java.util.Objects;

public class GlobalAttributeValueId extends CompositeId
    implements Comparable<GlobalAttributeValueId> {

  private static final int[] COMPOSITE_BITS = {
    GlobalAttributeGroupId.ID_LENGTH, // GROUP_ATTRIBUTE_ID
    INT_MAX_DIGITS // ID
  };

  private String group_attribute_id;
  private int id;

  public GlobalAttributeValueId(String group_attribute_id, int id) {
    this.group_attribute_id = group_attribute_id;
    this.id = id;
  }

  public GlobalAttributeValueId(GlobalAttributeGroupId group_attribute_id, int id) {
    this(group_attribute_id.encode(), id);
  }

  @Override
  public String encode() {
    return (super.encode(COMPOSITE_BITS));
  }

  @Override
  public void decode(String composite_id) {
    String[] values = super.decode(composite_id, COMPOSITE_BITS);
    this.group_attribute_id = values[0];
    this.id = Integer.parseInt(values[1]);
  }

  @Override
  public String[] toArray() {
    return (new String[] {this.group_attribute_id, Integer.toString(this.id)});
  }

  public GlobalAttributeGroupId getGlobalAttributeGroup() {
    return new GlobalAttributeGroupId(this.group_attribute_id);
  }

  protected int getId() {
    return (this.id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GlobalAttributeValueId that = (GlobalAttributeValueId) o;
    return id == that.id && Objects.equals(group_attribute_id, that.group_attribute_id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(group_attribute_id, id);
  }

  @Override
  public int compareTo(GlobalAttributeValueId o) {
    return Comparator.comparing(GlobalAttributeValueId::getGlobalAttributeGroup)
        .thenComparingInt(GlobalAttributeValueId::getId)
        .compare(this, o);
  }
}
