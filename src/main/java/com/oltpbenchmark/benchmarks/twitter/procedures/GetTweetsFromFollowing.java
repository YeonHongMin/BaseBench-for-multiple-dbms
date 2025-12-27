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

package com.oltpbenchmark.benchmarks.twitter.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.twitter.TwitterConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetTweetsFromFollowing extends Procedure {

  public final SQLStmt getFollowing =
      new SQLStmt(
          "SELECT f2 FROM "
              + TwitterConstants.TABLENAME_FOLLOWS
              + " WHERE f1 = ? LIMIT "
              + TwitterConstants.LIMIT_FOLLOWERS);

<<<<<<< HEAD
<<<<<<< HEAD
  /** NOTE: The ?? is substituted into a string of repeated ?'s */
=======
  /** 참고: ??는 반복되는 ?'s 문자열로 대체됩니다 */
>>>>>>> master
=======
  /** NOTE: The ?? is substituted into a string of repeated ?'s */
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  public final SQLStmt getTweets =
      new SQLStmt(
          "SELECT * FROM " + TwitterConstants.TABLENAME_TWEETS + " WHERE uid IN (??)",
          TwitterConstants.LIMIT_FOLLOWERS);

  public void run(Connection conn, int uid) throws SQLException {
    try (PreparedStatement getFollowingStatement = this.getPreparedStatement(conn, getFollowing)) {
      getFollowingStatement.setLong(1, uid);
      try (ResultSet followingResult = getFollowingStatement.executeQuery()) {

        try (PreparedStatement stmt = this.getPreparedStatement(conn, getTweets)) {
          int ctr = 0;
          long last = -1;
          while (followingResult.next() && ctr++ < TwitterConstants.LIMIT_FOLLOWERS) {
            last = followingResult.getLong(1);
            stmt.setLong(ctr, last);
          }
          if (ctr > 0) {
            while (ctr++ < TwitterConstants.LIMIT_FOLLOWERS) {
              stmt.setLong(ctr, last);
            }
            try (ResultSet getTweetsResult = stmt.executeQuery()) {
              assert getTweetsResult != null;
            }
          } else {
<<<<<<< HEAD
<<<<<<< HEAD
            // LOG.debug("No followers for user: "+uid); // so what .. ?
=======
            // LOG.debug("사용자에 대한 팔로워가 없습니다: "+uid); // 그래서 뭐..?
>>>>>>> master
=======
            // LOG.debug("No followers for user: "+uid); // so what .. ?
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
          }
        }
      }
    }
  }
}
