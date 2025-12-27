/*
 * Copyright 2020 by OLTPBenchmark Project
 *
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
 *
 */

package com.oltpbenchmark.benchmarks.auctionmark.exceptions;

import com.oltpbenchmark.api.Procedure.UserAbortException;

public class DuplicateItemIdException extends UserAbortException {
  private static final long serialVersionUID = -667971163586760142L;

  private final String item_id;
  private final String seller_id;
  private final int item_count;

  public DuplicateItemIdException(
      String item_id, String seller_id, int item_count, Exception cause) {
    super(String.format("Duplicate ItemId [%s] for Seller [%s]", item_id, seller_id), cause);

    this.item_id = item_id;
    this.seller_id = seller_id;
    this.item_count = item_count;
  }

  public DuplicateItemIdException(String item_id, String seller_id, int item_count) {
    this(item_id, seller_id, item_count, null);
  }

  public String getItemId() {
    return this.item_id;
  }

  public String getSellerId() {
    return this.seller_id;
  }

  public int getItemCount() {
    return this.item_count;
  }
}
