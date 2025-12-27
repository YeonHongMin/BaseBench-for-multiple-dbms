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

package com.oltpbenchmark.benchmarks.wikipedia;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.wikipedia.procedures.*;
import com.oltpbenchmark.benchmarks.wikipedia.util.Article;
import com.oltpbenchmark.benchmarks.wikipedia.util.WikipediaUtil;
import com.oltpbenchmark.types.TransactionStatus;
import com.oltpbenchmark.util.RandomDistribution.Flat;
import com.oltpbenchmark.util.RandomDistribution.Zipf;
import com.oltpbenchmark.util.TextGenerator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WikipediaWorker extends Worker<WikipediaBenchmark> {
  private static final Logger LOG = LoggerFactory.getLogger(WikipediaWorker.class);

  private Set<Integer> addedWatchlistPages = new HashSet<>();

  public WikipediaWorker(WikipediaBenchmark benchmarkModule, int id) {
    super(benchmarkModule, id);
  }

  private String generateUserIP() {
    return String.format(
        "%d.%d.%d.%d",
        this.rng().nextInt(255) + 1,
        this.rng().nextInt(256),
        this.rng().nextInt(256),
        this.rng().nextInt(256));
  }

  @Override
  protected TransactionStatus executeWork(Connection conn, TransactionType nextTransaction)
      throws UserAbortException, SQLException {
    Flat z_users = new Flat(this.rng(), 1, this.getBenchmark().num_users);
    Zipf z_pages =
        new Zipf(this.rng(), 1, this.getBenchmark().num_pages, WikipediaConstants.USER_ID_SIGMA);

    Class<? extends Procedure> procClass = nextTransaction.getProcedureClass();
    boolean needUser =
        (procClass.equals(AddWatchList.class)
            || procClass.equals(RemoveWatchList.class)
            || procClass.equals(GetPageAuthenticated.class));

    int userId;

    do {
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      // Check whether this should be an anonymous update
      if (this.rng().nextInt(100) < WikipediaConstants.ANONYMOUS_PAGE_UPDATE_PROB) {
        userId = WikipediaConstants.ANONYMOUS_USER_ID;
      }
      // Otherwise figure out what user is updating this page
      else {
        userId = z_users.nextInt();
      }
      // Repeat if we need a user but we generated Anonymous
    } while (needUser && userId == WikipediaConstants.ANONYMOUS_USER_ID);

    // Figure out what page they're going to update
    int page_id = z_pages.nextInt();
    if (procClass.equals(AddWatchList.class)) {
      // This while loop gets stuck in an infinite loop for small scale factors.
      // So we're just going to let it throw whatever it wants in the set from now on
<<<<<<< HEAD
=======
      // 익명 업데이트여야 하는지 확인합니다
      if (this.rng().nextInt(100) < WikipediaConstants.ANONYMOUS_PAGE_UPDATE_PROB) {
        userId = WikipediaConstants.ANONYMOUS_USER_ID;
      }
      // 그렇지 않으면 이 페이지를 업데이트하는 사용자가 누구인지 파악합니다
      else {
        userId = z_users.nextInt();
      }
      // 사용자가 필요하지만 익명을 생성한 경우 반복합니다
    } while (needUser && userId == WikipediaConstants.ANONYMOUS_USER_ID);

    // 업데이트할 페이지를 파악합니다
    int page_id = z_pages.nextInt();
    if (procClass.equals(AddWatchList.class)) {
      // 이 while 루프는 작은 스케일 팩터에서 무한 루프에 빠집니다.
      // 이제부터는 집합에 무엇을 넣든 그냥 던지도록 하겠습니다
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      // while (addedWatchlistPages.contains(page_id)) {
      //    page_id = z_pages.nextInt();
      // }
      addedWatchlistPages.add(page_id);
    }

    String pageTitle = WikipediaUtil.generatePageTitle(this.rng(), page_id);
    int nameSpace = WikipediaUtil.generatePageNamespace(this.rng(), page_id);

<<<<<<< HEAD
<<<<<<< HEAD
    // AddWatchList
=======
    // 관심 목록 추가
>>>>>>> master
=======
    // AddWatchList
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    try {
      if (procClass.equals(AddWatchList.class)) {

        this.addToWatchlist(conn, userId, nameSpace, pageTitle);
      }
<<<<<<< HEAD
<<<<<<< HEAD
      // RemoveWatchList
=======
      // 관심 목록 제거
>>>>>>> master
=======
      // RemoveWatchList
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      else if (procClass.equals(RemoveWatchList.class)) {

        this.removeFromWatchlist(conn, userId, nameSpace, pageTitle);
      }
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      // UpdatePage
      else if (procClass.equals(UpdatePage.class)) {
        this.updatePage(conn, this.generateUserIP(), userId, nameSpace, pageTitle);
      }
      // GetPageAnonymous
      else if (procClass.equals(GetPageAnonymous.class)) {
        this.getPageAnonymous(conn, true, this.generateUserIP(), nameSpace, pageTitle);
      }
      // GetPageAuthenticated
<<<<<<< HEAD
=======
      // 페이지 업데이트
      else if (procClass.equals(UpdatePage.class)) {
        this.updatePage(conn, this.generateUserIP(), userId, nameSpace, pageTitle);
      }
      // 익명 페이지 가져오기
      else if (procClass.equals(GetPageAnonymous.class)) {
        this.getPageAnonymous(conn, true, this.generateUserIP(), nameSpace, pageTitle);
      }
      // 인증된 페이지 가져오기
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      else if (procClass.equals(GetPageAuthenticated.class)) {

        this.getPageAuthenticated(conn, true, this.generateUserIP(), userId, nameSpace, pageTitle);
      }
    } catch (SQLException esql) {
      LOG.error(
          "Caught SQL Exception in WikipediaWorker for procedure{}:{}",
          procClass.getName(),
          esql,
          esql);
      throw esql;
    }
    return (TransactionStatus.SUCCESS);
  }

  /**
   * Implements wikipedia selection of last version of an article (with and without the user being
   * logged in)
   */
  public Article getPageAnonymous(
      Connection conn, boolean forSelect, String userIp, int nameSpace, String pageTitle)
      throws SQLException {
    GetPageAnonymous proc = this.getProcedure(GetPageAnonymous.class);

    return proc.run(conn, forSelect, userIp, nameSpace, pageTitle);
  }

  public Article getPageAuthenticated(
      Connection conn,
      boolean forSelect,
      String userIp,
      int userId,
      int nameSpace,
      String pageTitle)
      throws SQLException {
    GetPageAuthenticated proc = this.getProcedure(GetPageAuthenticated.class);

    return proc.run(conn, forSelect, userIp, userId, nameSpace, pageTitle);
  }

  public void addToWatchlist(Connection conn, int userId, int nameSpace, String pageTitle)
      throws SQLException {
    AddWatchList proc = this.getProcedure(AddWatchList.class);

    proc.run(conn, userId, nameSpace, pageTitle);
  }

  public void removeFromWatchlist(Connection conn, int userId, int nameSpace, String pageTitle)
      throws SQLException {
    RemoveWatchList proc = this.getProcedure(RemoveWatchList.class);

    proc.run(conn, userId, nameSpace, pageTitle);
  }

  public void updatePage(
      Connection conn, String userIp, int userId, int nameSpace, String pageTitle)
      throws SQLException {
    Article a = this.getPageAnonymous(conn, false, userIp, nameSpace, pageTitle);

    // TODO: If the Article is null, then we want to insert a new page.
    // But we don't support that right now.
    if (a == null) {
      return;
    }

    WikipediaBenchmark b = this.getBenchmark();
    int revCommentLen = b.commentLength.nextValue();
    String revComment = TextGenerator.randomStr(this.rng(), revCommentLen + 1);
    int revMinorEdit = b.minorEdit.nextValue();

<<<<<<< HEAD
<<<<<<< HEAD
    // Permute the original text of the article
    // Important: We have to make sure that we fill in the entire array
=======
    // 기사의 원본 텍스트를 순열합니다
    // 중요: 전체 배열을 채우도록 해야 합니다
>>>>>>> master
=======
    // Permute the original text of the article
    // Important: We have to make sure that we fill in the entire array
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    char[] newText = b.generateRevisionText(a.oldText.toCharArray());

    if (LOG.isTraceEnabled()) {
      LOG.trace("UPDATING: Page: id:{} ns:{} title{}", a.pageId, nameSpace, pageTitle);
    }
    UpdatePage proc = this.getProcedure(UpdatePage.class);

    proc.run(
        conn,
        a.textId,
        a.pageId,
        pageTitle,
        new String(newText),
        nameSpace,
        userId,
        userIp,
        a.userText,
        a.revisionId,
        revComment,
        revMinorEdit);
  }
}
