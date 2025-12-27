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

package com.oltpbenchmark.benchmarks.auctionmark.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.auctionmark.AuctionMarkConstants;
import com.oltpbenchmark.benchmarks.auctionmark.util.AuctionMarkUtil;
import java.sql.*;

/**
 * UpdateItem
 *
 * @author pavlo
 * @author visawee
 */
public class UpdateItem extends Procedure {

  // -----------------------------------------------------------------
  // STATEMENTS
  // -----------------------------------------------------------------

  public final SQLStmt updateItem =
      new SQLStmt(
          "UPDATE "
              + AuctionMarkConstants.TABLENAME_ITEM
              + "   SET i_description = ?, "
              + "       i_updated = ? "
              + " WHERE i_id = ? AND i_u_id = ? "
          // "   AND i_status = " + ItemStatus.OPEN.ordinal()
          );

  public final SQLStmt deleteItemAttribute =
      new SQLStmt(
          "DELETE FROM "
              + AuctionMarkConstants.TABLENAME_ITEM_ATTRIBUTE
              + " WHERE ia_id = ? AND ia_i_id = ? AND ia_u_id = ?");

  public final SQLStmt getMaxItemAttributeId =
      new SQLStmt(
          "SELECT MAX(ia_id) FROM "
              + AuctionMarkConstants.TABLENAME_ITEM_ATTRIBUTE
              + " WHERE ia_i_id = ? AND ia_u_id = ?");

  public final SQLStmt insertItemAttribute =
      new SQLStmt(
          "INSERT INTO "
              + AuctionMarkConstants.TABLENAME_ITEM_ATTRIBUTE
              + " ("
              + "ia_id,"
              + "ia_i_id,"
              + "ia_u_id,"
              + "ia_gav_id,"
              + "ia_gag_id"
              + ") VALUES (?, ?, ?, ?, ?)");

  // -----------------------------------------------------------------
  // RUN METHOD
  // -----------------------------------------------------------------

  /**
   * The buyer modifies an existing auction that is still available. The transaction will just
   * update the description of the auction. A small percentage of the transactions will be for
   * auctions that are uneditable (1.0%?); when this occurs, the transaction will abort.
   */
  public boolean run(
      Connection conn,
      Timestamp[] benchmarkTimes,
      String item_id,
      String seller_id,
      String description,
      boolean delete_attribute,
      String[] add_attribute)
      throws SQLException {
    final Timestamp currentTime = AuctionMarkUtil.getProcTimestamp(benchmarkTimes);

    try (PreparedStatement stmt =
        this.getPreparedStatement(conn, updateItem, description, currentTime, item_id, seller_id)) {
      int updated = stmt.executeUpdate();
      if (updated == 0) {
        throw new UserAbortException("Unable to update closed auction");
      }
    }

    // DELETE ITEM_ATTRIBUTE
    if (delete_attribute) {
      // Only delete the first (if it even exists)
      String ia_id = AuctionMarkUtil.getUniqueElementId(item_id, 0);
      try (PreparedStatement stmt =
          this.getPreparedStatement(conn, deleteItemAttribute, ia_id, item_id, seller_id)) {
        stmt.executeUpdate();
      }
    }
    // ADD ITEM_ATTRIBUTE
    if (add_attribute.length > 0 && !add_attribute[0].equals("-1")) {

      String gag_id = add_attribute[0];
      String gav_id = add_attribute[1];
      String ia_id = "-1";

      try (PreparedStatement stmt =
          this.getPreparedStatement(conn, getMaxItemAttributeId, item_id, seller_id)) {
        try (ResultSet results = stmt.executeQuery()) {
          if (results.next()) {
            ia_id = results.getString(0);
          } else {
            ia_id = AuctionMarkUtil.getUniqueElementId(item_id, 0);
          }
        }
      }

      try (PreparedStatement stmt =
          this.getPreparedStatement(
              conn, insertItemAttribute, ia_id, item_id, seller_id, gag_id, gav_id)) {
        stmt.executeUpdate();
      }
    }

    return (true);
  }
}
