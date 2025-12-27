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

package com.oltpbenchmark.benchmarks.auctionmark.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.auctionmark.AuctionMarkConstants;
import com.oltpbenchmark.benchmarks.auctionmark.util.ItemStatus;
import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Remove ITEM entries created after the loader started
 *
 * @author pavlo
 */
public class ResetDatabase extends Procedure {
  private static final Logger LOG = LoggerFactory.getLogger(ResetDatabase.class);

  // -----------------------------------------------------------------
  // STATEMENTS
  // -----------------------------------------------------------------

  public final SQLStmt getLoaderStop =
      new SQLStmt("SELECT cfp_loader_stop FROM " + AuctionMarkConstants.TABLENAME_CONFIG_PROFILE);

  public final SQLStmt resetItems =
      new SQLStmt(
          "UPDATE "
              + AuctionMarkConstants.TABLENAME_ITEM
              + "   SET i_status = ?, i_updated = ?"
              + " WHERE i_status != ?"
              + "   AND i_updated > ? ");

  public final SQLStmt deleteItemPurchases =
      new SQLStmt(
          "DELETE FROM " + AuctionMarkConstants.TABLENAME_ITEM_PURCHASE + " WHERE ip_date > ?");

  public void run(Connection conn) throws SQLException {
    int updated;

    // We have to get the loaderStopTimestamp from the CONFIG_PROFILE
    // We will then reset any changes that were made after this timestamp
    Timestamp loaderStop;

    try (PreparedStatement preparedStatement = this.getPreparedStatement(conn, getLoaderStop)) {
      try (ResultSet rs = preparedStatement.executeQuery()) {
        rs.next();

        loaderStop = rs.getTimestamp(1);
      }
    }

    // Reset ITEM information
    try (PreparedStatement stmt =
        this.getPreparedStatement(
            conn,
            resetItems,
            ItemStatus.OPEN.ordinal(),
            loaderStop,
            ItemStatus.OPEN.ordinal(),
            loaderStop)) {
      updated = stmt.executeUpdate();
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug(AuctionMarkConstants.TABLENAME_ITEM + " Reset: {}", updated);
    }

    // Reset ITEM_PURCHASE
    try (PreparedStatement stmt =
        this.getPreparedStatement(conn, deleteItemPurchases, loaderStop)) {
      updated = stmt.executeUpdate();
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug(AuctionMarkConstants.TABLENAME_ITEM_PURCHASE + " Reset: {}", updated);
    }
  }
}
