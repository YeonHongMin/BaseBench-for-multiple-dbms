/*
 * Copyright 2020 by OLTPBenchmark Project
 *
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
 *
 */

package com.oltpbenchmark.benchmarks.auctionmark.util;

import com.oltpbenchmark.util.CompositeId;
import java.util.Comparator;
import java.util.Objects;

/**
 * Composite Item Id First 48-bits are the seller's USER.U_ID Last 16-bits are the item counter for
 * this particular user
 *
 * @author pavlo
 */
public final class ItemId extends CompositeId implements Comparable<ItemId> {

  private static final int[] COMPOSITE_BITS = {
    UserId.ID_LENGTH, // SELLER_ID
    INT_MAX_DIGITS // ITEM_CTR
  };

  private UserId seller_id;
  private int item_ctr;

  public ItemId(UserId seller_id, int item_ctr) {
    this.seller_id = seller_id;
    this.item_ctr = item_ctr;
  }

  public ItemId(String composite_id) {
    this.decode(composite_id);
  }

  @Override
  public String encode() {
    return (this.encode(COMPOSITE_BITS));
  }

  @Override
  public void decode(String composite_id) {
    String[] values = super.decode(composite_id, COMPOSITE_BITS);
    this.seller_id = new UserId(values[0]);
    this.item_ctr = Integer.parseInt(values[1]);
  }

  @Override
  public String[] toArray() {
    return (new String[] {this.seller_id.encode(), Integer.toString(this.item_ctr)});
  }

  /**
   * Return the user id portion of this ItemId
   *
   * @return the user_id
   */
  public UserId getSellerId() {
    return (this.seller_id);
  }

  /**
   * Return the item counter id for this user in the ItemId
   *
   * @return the item_ctr
   */
  public int getItemCtr() {
    return (this.item_ctr);
  }

  @Override
  public String toString() {
    return (String.format(
        "ItemId<item_ctr=%d, seller_id=%s, encoded=%s>",
        this.item_ctr, this.seller_id, this.encode()));
  }

  public static String toString(String itemId) {
    return new ItemId(itemId).toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemId itemId = (ItemId) o;
    return item_ctr == itemId.item_ctr && Objects.equals(seller_id, itemId.seller_id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seller_id, item_ctr);
  }

  @Override
  public int compareTo(ItemId o) {
    return Comparator.comparing(ItemId::getSellerId)
        .thenComparingInt(ItemId::getItemCtr)
        .compare(this, o);
  }
}
