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

package com.oltpbenchmark.benchmarks.wikipedia;

import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.LoaderThread;
import com.oltpbenchmark.benchmarks.wikipedia.data.PageHistograms;
import com.oltpbenchmark.benchmarks.wikipedia.data.TextHistograms;
import com.oltpbenchmark.benchmarks.wikipedia.data.UserHistograms;
import com.oltpbenchmark.benchmarks.wikipedia.util.WikipediaUtil;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.types.DatabaseType;
import com.oltpbenchmark.util.RandomDistribution.FlatHistogram;
import com.oltpbenchmark.util.RandomDistribution.Zipf;
import com.oltpbenchmark.util.SQLUtil;
import com.oltpbenchmark.util.StringUtil;
import com.oltpbenchmark.util.TextGenerator;
import com.oltpbenchmark.util.TimeUtil;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * 합성 위키피디아 데이터 로더
 *
 * @author pavlo
 * @author djellel
 */
public final class WikipediaLoader extends Loader<WikipediaBenchmark> {

  /** UserId -> # of Revisions */
  private final int[] user_revision_ctr;

  /** PageId -> Last Revision Id */
  private final int[] page_last_rev_id;

  /** PageId -> Last Revision Length */
  private final int[] page_last_rev_length;

  /**
   * 생성자
   *
   * @param benchmark
   */
  public WikipediaLoader(WikipediaBenchmark benchmark) {
    super(benchmark);

    this.user_revision_ctr = new int[this.benchmark.num_users];
    Arrays.fill(this.user_revision_ctr, 0);

    this.page_last_rev_id = new int[this.benchmark.num_pages];
    Arrays.fill(this.page_last_rev_id, -1);
    this.page_last_rev_length = new int[this.benchmark.num_pages];
    Arrays.fill(this.page_last_rev_length, -1);

    if (LOG.isDebugEnabled()) {
      LOG.debug("# of USERS:  {}", this.benchmark.num_users);
      LOG.debug("# of PAGES: {}", this.benchmark.num_pages);
    }
  }

  @Override
  public List<LoaderThread> createLoaderThreads() {
    List<LoaderThread> threads = new ArrayList<>();
    final int numLoaders = this.benchmark.getWorkloadConfiguration().getLoaderThreads();
    final int numItems = this.benchmark.num_pages + this.benchmark.num_users;
    final int itemsPerThread = Math.max(numItems / numLoaders, 1);
    final int numUserThreads = (int) Math.ceil((double) this.benchmark.num_users / itemsPerThread);
    final int numPageThreads = (int) Math.ceil((double) this.benchmark.num_pages / itemsPerThread);

    final CountDownLatch userPageLatch = new CountDownLatch(numUserThreads + numPageThreads);

    final CountDownLatch anonUserLatch = new CountDownLatch(1);
    threads.add(
        new LoaderThread(this.benchmark) {
          @Override
          public void load(Connection conn) throws SQLException {
            Table catalog_tbl = benchmark.getCatalog().getTable(WikipediaConstants.TABLENAME_USER);

            SQLUtil.setIdentityInsert(conn, getDatabaseType(), catalog_tbl, true);

            String sql =
                SQLUtil.getInsertSQL(
                    catalog_tbl, benchmark.getWorkloadConfiguration().getDatabaseType());
            // Oracle DB에서 NULL로 처리되는 빈 문자열
            String dummyString = getDatabaseType() == DatabaseType.ORACLE ? " " : "";

            // 익명 사용자 로드
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
              int param = 1;
              stmt.setInt(param++, WikipediaConstants.ANONYMOUS_USER_ID); // user_id
              stmt.setString(param++, "Anonymous"); // user_name
              stmt.setString(param++, dummyString); // user_real_name
              stmt.setString(param++, dummyString); // user_password
              stmt.setString(param++, dummyString); // user_newpassword
              stmt.setNull(param++, JDBCType.VARCHAR.getVendorTypeNumber()); // user_newpass_time
              stmt.setString(param++, dummyString); // user_email
              stmt.setString(param++, dummyString); // user_options
              stmt.setString(param++, dummyString); // user_touched
              stmt.setString(param++, dummyString); // user_token
              stmt.setNull(
                  param++, JDBCType.VARCHAR.getVendorTypeNumber()); // user_email_authenticated
              stmt.setNull(param++, JDBCType.VARCHAR.getVendorTypeNumber()); // user_email_token
              stmt.setNull(
                  param++, JDBCType.VARCHAR.getVendorTypeNumber()); // user_email_token_expires
              stmt.setNull(param++, JDBCType.VARCHAR.getVendorTypeNumber()); // user_registration
              stmt.setInt(param, 0); // user_editcount

              stmt.executeUpdate();
            }

            SQLUtil.setIdentityInsert(conn, getDatabaseType(), catalog_tbl, false);
          }

          @Override
          public void afterLoad() {
            anonUserLatch.countDown();
          }
        });

    // USERS
    for (int i = 0; i < numUserThreads; i++) {
      // load USERS[lo, hi]
      final int lo = i * itemsPerThread + 1;
      final int hi = Math.min(this.benchmark.num_users, (i + 1) * itemsPerThread);

      threads.add(
          new LoaderThread(this.benchmark) {
            @Override
            public void load(Connection conn) throws SQLException {
              loadUsers(conn, lo, hi);
            }

            @Override
            public void afterLoad() {
              userPageLatch.countDown();
            }

            @Override
            public void beforeLoad() {
              try {
                anonUserLatch.await();
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            }
          });
    }

    // PAGES
    for (int i = 0; i < numPageThreads; i++) {
      // load PAGES[lo, hi]
      final int lo = i * itemsPerThread + 1;
      final int hi = Math.min(this.benchmark.num_pages, (i + 1) * itemsPerThread);

      threads.add(
          new LoaderThread(this.benchmark) {
            @Override
            public void load(Connection conn) throws SQLException {
              loadPages(conn, lo, hi);
            }

            @Override
            public void afterLoad() {
              userPageLatch.countDown();
            }

            @Override
            public void beforeLoad() {
              try {
                anonUserLatch.await();
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            }
          });
    }

    // WATCHLIST and REVISIONS depends on USERS and PAGES

    // WATCHLIST
    threads.add(
        new LoaderThread(this.benchmark) {
          @Override
          public void load(Connection conn) throws SQLException {
            loadWatchlist(conn);
          }

          @Override
          public void beforeLoad() {
            try {
              userPageLatch.await();
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
        });

    // REVISIONS
    threads.add(
        new LoaderThread(this.benchmark) {
          @Override
          public void load(Connection conn) throws SQLException {
            loadRevision(conn);
          }

          @Override
          public void beforeLoad() {
            try {
              userPageLatch.await();
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
        });

    return threads;
  }

  /** 사용자 계정 */
  private void loadUsers(Connection conn, int lo, int hi) throws SQLException {
    Table catalog_tbl = benchmark.getCatalog().getTable(WikipediaConstants.TABLENAME_USER);

    SQLUtil.setIdentityInsert(conn, getDatabaseType(), catalog_tbl, true);
    String sql = SQLUtil.getInsertSQL(catalog_tbl, this.getDatabaseType());

    try (PreparedStatement userInsert = conn.prepareStatement(sql)) {
      FlatHistogram<Integer> h_nameLength = new FlatHistogram<>(rng(), UserHistograms.NAME_LENGTH);
      FlatHistogram<Integer> h_realNameLength =
          new FlatHistogram<>(rng(), UserHistograms.REAL_NAME_LENGTH);
      FlatHistogram<Integer> h_revCount = new FlatHistogram<>(rng(), UserHistograms.REVISION_COUNT);

      int[] types = catalog_tbl.getColumnTypes();
      int batchSize = 0;
      int lastPercent = -1;
      for (int i = lo; i <= hi; i++) {
        // 이름 앞에 UserId를 붙입니다. 이렇게 하면 모든 사용자 이름이 고유할 가능성이 높아집니다
        // 보장은 아니지만 충분합니다...
        String name = i + TextGenerator.randomStr(rng(), h_nameLength.nextValue());
        String realName = TextGenerator.randomStr(rng(), h_realNameLength.nextValue());
        int revCount = h_revCount.nextValue();
        String password = StringUtil.repeat("*", rng().nextInt(32) + 1);

        char[] eChars = TextGenerator.randomChars(rng(), rng().nextInt(32) + 5);
        eChars[4 + rng().nextInt(eChars.length - 4)] = '@';
        String email = new String(eChars);

        String token = TextGenerator.randomStr(rng(), WikipediaConstants.TOKEN_LENGTH);
        String userOptions = "fake_longoptionslist";
        String newPassTime = TimeUtil.getCurrentTimeString14();
        String touched = TimeUtil.getCurrentTimeString14();

        int param = 1;
        userInsert.setInt(param++, i); // user_id
        userInsert.setString(param++, name); // user_name
        userInsert.setString(param++, realName); // user_real_name
        userInsert.setString(param++, password); // user_password
        userInsert.setString(param++, password); // user_newpassword
        userInsert.setString(param++, newPassTime); // user_newpass_time
        userInsert.setString(param++, email); // user_email
        userInsert.setString(param++, userOptions); // user_options
        userInsert.setString(param++, touched); // user_touched
        userInsert.setString(param++, token); // user_token
        userInsert.setNull(param++, types[param - 2]); // user_email_authenticated
        userInsert.setNull(param++, types[param - 2]); // user_email_token
        userInsert.setNull(param++, types[param - 2]); // user_email_token_expires
        userInsert.setNull(param++, types[param - 2]); // user_registration
        userInsert.setInt(param++, revCount); // user_editcount
        userInsert.addBatch();

        if (++batchSize % workConf.getBatchSize() == 0) {
          userInsert.executeBatch();
          userInsert.clearBatch();
          this.addToTableCount(catalog_tbl.getName(), batchSize);
          batchSize = 0;
          if (LOG.isDebugEnabled()) {
            int percent = (int) (((double) i / (double) this.benchmark.num_users) * 100);
            if (percent != lastPercent) {
              LOG.debug("USERACCT ({}%)", percent);
            }
            lastPercent = percent;
          }
        }
      }
      if (batchSize > 0) {
        this.addToTableCount(catalog_tbl.getName(), batchSize);
        userInsert.executeBatch();
        userInsert.clearBatch();
      }
    }
    SQLUtil.setIdentityInsert(conn, getDatabaseType(), catalog_tbl, false);
    DatabaseType dbType = this.getDatabaseType();
    if (dbType.shouldUpdateColumnSequenceAfterLoad()) {
      this.updateAutoIncrement(conn, catalog_tbl.getColumn(0), this.benchmark.num_users);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Users  % {}", this.benchmark.num_users);
    }
  }

  /** 페이지 */
  private void loadPages(Connection conn, int lo, int hi) throws SQLException {
    Table catalog_tbl = benchmark.getCatalog().getTable(WikipediaConstants.TABLENAME_PAGE);

    SQLUtil.setIdentityInsert(conn, getDatabaseType(), catalog_tbl, true);
    String sql = SQLUtil.getInsertSQL(catalog_tbl, this.getDatabaseType());
    try (PreparedStatement pageInsert = conn.prepareStatement(sql)) {
      FlatHistogram<String> h_restrictions =
          new FlatHistogram<>(rng(), PageHistograms.RESTRICTIONS);

      int batchSize = 0;
      int lastPercent = -1;

      for (int i = lo; i <= hi; i++) {
        String title = WikipediaUtil.generatePageTitle(rng(), i);
        int namespace = WikipediaUtil.generatePageNamespace(rng(), i);
        String restrictions = h_restrictions.nextValue();
        double pageRandom = rng().nextDouble();
        String pageTouched = TimeUtil.getCurrentTimeString14();

        int param = 1;
        pageInsert.setInt(param++, i); // page_id
        pageInsert.setInt(param++, namespace); // page_namespace
        pageInsert.setString(param++, title); // page_title
        pageInsert.setString(param++, restrictions); // page_restrictions
        pageInsert.setInt(param++, 0); // page_counter
        pageInsert.setInt(param++, 0); // page_is_redirect
        pageInsert.setInt(param++, 0); // page_is_new
        pageInsert.setDouble(param++, pageRandom); // page_random
        pageInsert.setString(param++, pageTouched); // page_touched
        pageInsert.setInt(param++, 0); // page_latest
        pageInsert.setInt(param++, 0); // page_len
        pageInsert.addBatch();

        if (++batchSize % workConf.getBatchSize() == 0) {
          pageInsert.executeBatch();
          pageInsert.clearBatch();
          this.addToTableCount(catalog_tbl.getName(), batchSize);
          batchSize = 0;
          if (LOG.isDebugEnabled()) {
            int percent = (int) (((double) i / (double) this.benchmark.num_pages) * 100);
            if (percent != lastPercent) {
              LOG.debug("PAGE ({}%)", percent);
            }
            lastPercent = percent;
          }
        }
      }
      if (batchSize > 0) {
        pageInsert.executeBatch();
        pageInsert.clearBatch();
        this.addToTableCount(catalog_tbl.getName(), batchSize);
      }
    }
    SQLUtil.setIdentityInsert(conn, getDatabaseType(), catalog_tbl, false);
    DatabaseType dbType = this.getDatabaseType();
    if (dbType.shouldUpdateColumnSequenceAfterLoad()) {
      this.updateAutoIncrement(conn, catalog_tbl.getColumn(0), this.benchmark.num_pages);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Users  % {}", this.benchmark.num_pages);
    }
  }

  /** 관심 목록 */
  private void loadWatchlist(Connection conn) throws SQLException {
    Table catalog_tbl = benchmark.getCatalog().getTable(WikipediaConstants.TABLENAME_WATCHLIST);

    String sql = SQLUtil.getInsertSQL(catalog_tbl, this.getDatabaseType());
    try (PreparedStatement watchInsert = conn.prepareStatement(sql)) {
      int max_watches_per_user =
          Math.min(this.benchmark.num_pages, WikipediaConstants.MAX_WATCHES_PER_USER);
      Zipf h_numWatches =
          new Zipf(rng(), 0, max_watches_per_user, WikipediaConstants.NUM_WATCHES_PER_USER_SIGMA);
      Zipf h_pageId =
          new Zipf(rng(), 1, this.benchmark.num_pages, WikipediaConstants.WATCHLIST_PAGE_SIGMA);

      // 작은 튜플을 가진 테이블에 대해 큰 최대 배치 크기를 사용합니다
      int maxBatchSize = workConf.getBatchSize() * 5;

      int batchSize = 0;
      int lastPercent = -1;
      Set<Integer> userPages = new HashSet<>();

      for (int user_id = 1; user_id <= this.benchmark.num_users; user_id++) {
        int num_watches = h_numWatches.nextInt();
        if (LOG.isTraceEnabled()) {
          LOG.trace("{} => {}", user_id, num_watches);
        }
        if (num_watches == 0) {
          continue;
        }

        userPages.clear();
        for (int i = 0; i < num_watches; i++) {
          int pageId = -1;
          // HACK: 작은 데이터베이스 크기로 테스트할 때의 해결책
          if (num_watches == max_watches_per_user) {
            pageId = i + 1;
          } else {
            pageId = h_pageId.nextInt();
            while (userPages.contains(pageId)) {
              pageId = h_pageId.nextInt();
            }
          }

          userPages.add(pageId);

          Integer namespace = WikipediaUtil.generatePageNamespace(rng(), pageId);
          String title = WikipediaUtil.generatePageTitle(rng(), pageId);

          int param = 1;
          watchInsert.setInt(param++, user_id); // wl_user
          watchInsert.setInt(param++, namespace); // wl_namespace
          watchInsert.setString(param++, title); // wl_title
          watchInsert.setNull(param++, java.sql.Types.VARCHAR); // wl_notificationtimestamp
          watchInsert.addBatch();
          batchSize++;
        }

        if (batchSize >= maxBatchSize) {
          watchInsert.executeBatch();
          watchInsert.clearBatch();
          this.addToTableCount(catalog_tbl.getName(), batchSize);
          batchSize = 0;
          if (LOG.isDebugEnabled()) {
            int percent = (int) (((double) user_id / (double) this.benchmark.num_users) * 100);
            if (percent != lastPercent) {
              LOG.debug("WATCHLIST ({}%)", percent);
            }
            lastPercent = percent;
          }
        }
      }

      if (batchSize > 0) {
        watchInsert.executeBatch();
        watchInsert.clearBatch();
        this.addToTableCount(catalog_tbl.getName(), batchSize);
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Watchlist Loaded");
    }
  }

  /** 리비전 */
  private void loadRevision(Connection conn) throws SQLException {

    // TEXT
    Table textTable = benchmark.getCatalog().getTable(WikipediaConstants.TABLENAME_TEXT);
    String textSQL = SQLUtil.getInsertSQL(textTable, this.getDatabaseType());

    // REVISION
    Table revTable = benchmark.getCatalog().getTable(WikipediaConstants.TABLENAME_REVISION);
    String revSQL = SQLUtil.getInsertSQL(revTable, this.getDatabaseType());

    int batchSize = 1;
    Zipf h_users =
        new Zipf(rng(), 1, this.benchmark.num_users, WikipediaConstants.REVISION_USER_SIGMA);
    FlatHistogram<Integer> h_textLength = new FlatHistogram<>(rng(), TextHistograms.TEXT_LENGTH);
    FlatHistogram<Integer> h_commentLength = this.benchmark.commentLength;
    FlatHistogram<Integer> h_minorEdit = this.benchmark.minorEdit;
    FlatHistogram<Integer> h_nameLength = new FlatHistogram<>(rng(), UserHistograms.NAME_LENGTH);
    FlatHistogram<Integer> h_numRevisions =
        new FlatHistogram<>(rng(), PageHistograms.REVISIONS_PER_PAGE);

    final int rev_comment_max = revTable.getColumnByName("rev_comment").getSize();
    int rev_id = 1;
    int lastPercent = -1;

    try (PreparedStatement textInsert = conn.prepareStatement(textSQL);
        PreparedStatement revisionInsert = conn.prepareStatement(revSQL)) {

      for (int page_id = 1; page_id <= this.benchmark.num_pages; page_id++) {
        // 페이지당 최소 하나의 리비전이 있어야 합니다
        int num_revised = h_numRevisions.nextValue();
        LOG.debug(String.format("페이지 %d의 리비전 수: %d", page_id, num_revised));

        // 새 리비전이 무엇일지 생성합니다
        int old_text_length = h_textLength.nextValue();
        char[] old_text = TextGenerator.randomChars(rng(), old_text_length);

        for (int i = 0; i < num_revised; i++) {
          // 리비전을 수행하는 사용자와 수정된 페이지를 생성합니다
          // 항상 그들의 카운터를 업데이트하는지 확인합니다
          int user_id = h_users.nextInt();
          this.user_revision_ctr[user_id - 1]++;

          // 새 리비전이 무엇일지 생성합니다
          if (i > 0) {
            old_text = this.benchmark.generateRevisionText(old_text);
            old_text_length = old_text.length;
          }

          int rev_comment_len = Math.min(rev_comment_max, h_commentLength.nextValue() + 1); // HACK
          String rev_comment = TextGenerator.randomStr(rng(), rev_comment_len);

          // REV_USER_TEXT 필드는 일반적으로 사용자 이름이지만, 지금은 그냥 무의미한 텍스트를 넣겠습니다
          String user_text = TextGenerator.randomStr(rng(), h_nameLength.nextValue() + 1);

          // Insert the text
          int col = 1;
          textInsert.setInt(col++, rev_id); // old_id
          textInsert.setString(col++, new String(old_text)); // old_text
          textInsert.setString(col++, "utf-8"); // old_flags
          textInsert.setInt(col++, page_id); // old_page
          textInsert.addBatch();

          // Insert the revision
          col = 1;
          revisionInsert.setInt(col++, rev_id); // rev_id
          revisionInsert.setInt(col++, page_id); // rev_page
          revisionInsert.setInt(col++, rev_id); // rev_text_id
          revisionInsert.setString(col++, rev_comment); // rev_comment
          revisionInsert.setInt(col++, user_id); // rev_user
          revisionInsert.setString(col++, user_text); // rev_user_text
          revisionInsert.setString(col++, TimeUtil.getCurrentTimeString14()); // rev_timestamp
          revisionInsert.setInt(col++, h_minorEdit.nextValue()); // rev_minor_edit
          revisionInsert.setInt(col++, 0); // rev_deleted
          revisionInsert.setInt(col++, 0); // rev_len
          revisionInsert.setInt(col++, 0); // rev_parent_id
          revisionInsert.addBatch();

          // 마지막 리비전 정보 업데이트
          this.page_last_rev_id[page_id - 1] = rev_id;
          this.page_last_rev_length[page_id - 1] = old_text_length;
          rev_id++;
          batchSize++;
        }
        if (batchSize > workConf.getBatchSize()) {
          textInsert.executeBatch();
          revisionInsert.executeBatch();
          this.addToTableCount(textTable.getName(), batchSize);
          this.addToTableCount(revTable.getName(), batchSize);
          batchSize = 0;

          if (LOG.isDebugEnabled()) {
            int percent = (int) (((double) page_id / (double) this.benchmark.num_pages) * 100);
            if (percent != lastPercent) {
              LOG.debug("REVISIONS ({}%)", percent);
            }
            lastPercent = percent;
          }
        }
      }
      if (batchSize > 0) {
        textInsert.executeBatch();
        revisionInsert.executeBatch();
        this.addToTableCount(textTable.getName(), batchSize);
        this.addToTableCount(revTable.getName(), batchSize);
      }
    }

    DatabaseType dbType = this.getDatabaseType();
    if (dbType.shouldUpdateColumnSequenceAfterLoad()) {
      this.updateAutoIncrement(conn, textTable.getColumn(0), rev_id);
      this.updateAutoIncrement(conn, revTable.getColumn(0), rev_id);
    }

    // 사용자 업데이트
    revTable = benchmark.getCatalog().getTable(WikipediaConstants.TABLENAME_USER);
    String revTableName =
        (this.getDatabaseType().shouldEscapeNames())
            ? revTable.getEscapedName()
            : revTable.getName();

    String updateUserSql =
        "UPDATE "
            + revTableName
            + "   SET user_editcount = ?, "
            + "       user_touched = ? "
            + " WHERE user_id = ?";
    try (PreparedStatement userUpdate = conn.prepareStatement(updateUserSql)) {
      batchSize = 0;
      for (int i = 0; i < this.benchmark.num_users; i++) {
        int col = 1;
        userUpdate.setInt(col++, this.user_revision_ctr[i]);
        userUpdate.setString(col++, TimeUtil.getCurrentTimeString14());
        userUpdate.setInt(col++, i + 1); // id는 1부터 시작
        userUpdate.addBatch();
        if ((++batchSize % workConf.getBatchSize()) == 0) {
          userUpdate.executeBatch();
          userUpdate.clearBatch();
          batchSize = 0;
        }
      }
      if (batchSize > 0) {
        userUpdate.executeBatch();
        userUpdate.clearBatch();
      }
    }

    // 페이지 업데이트
    revTable = benchmark.getCatalog().getTable(WikipediaConstants.TABLENAME_PAGE);

    revTableName =
        (this.getDatabaseType().shouldEscapeNames())
            ? revTable.getEscapedName()
            : revTable.getName();

    String updatePageSql =
        "UPDATE "
            + revTableName
            + "   SET page_latest = ?, "
            + "       page_touched = ?, "
            + "       page_is_new = 0, "
            + "       page_is_redirect = 0, "
            + "       page_len = ? "
            + " WHERE page_id = ?";
    try (PreparedStatement pageUpdate = conn.prepareStatement(updatePageSql)) {
      batchSize = 0;
      for (int i = 0; i < this.benchmark.num_pages; i++) {
        if (this.page_last_rev_id[i] == -1) {
          continue;
        }

        int col = 1;
        pageUpdate.setInt(col++, this.page_last_rev_id[i]);
        pageUpdate.setString(col++, TimeUtil.getCurrentTimeString14());
        pageUpdate.setInt(col++, this.page_last_rev_length[i]);
        pageUpdate.setInt(col++, i + 1); // id는 1부터 시작
        pageUpdate.addBatch();
        if ((++batchSize % workConf.getBatchSize()) == 0) {
          pageUpdate.executeBatch();
          pageUpdate.clearBatch();
          batchSize = 0;
        }
      }
      if (batchSize > 0) {
        pageUpdate.executeBatch();
        pageUpdate.clearBatch();
      }
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Revision loaded");
    }
  }
}
