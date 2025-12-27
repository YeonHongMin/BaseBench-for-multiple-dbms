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

package com.oltpbenchmark.benchmarks.twitter;

import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.LoaderThread;
import com.oltpbenchmark.benchmarks.twitter.util.NameHistogram;
import com.oltpbenchmark.benchmarks.twitter.util.TweetHistogram;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.distributions.ScrambledZipfianGenerator;
import com.oltpbenchmark.distributions.ZipfianGenerator;
import com.oltpbenchmark.util.RandomDistribution.FlatHistogram;
import com.oltpbenchmark.util.SQLUtil;
import com.oltpbenchmark.util.TextGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public final class TwitterLoader extends Loader<TwitterBenchmark> {
  private final int num_users;
  private final long num_tweets;
  private final int num_follows;

  public TwitterLoader(TwitterBenchmark benchmark) {
    super(benchmark);
    this.num_users = (int) Math.round(TwitterConstants.NUM_USERS * this.scaleFactor);
    this.num_tweets = (int) Math.round(TwitterConstants.NUM_TWEETS * this.scaleFactor);
    this.num_follows = (int) Math.round(TwitterConstants.MAX_FOLLOW_PER_USER * this.scaleFactor);
    if (LOG.isDebugEnabled()) {
      LOG.debug("# of USERS:  {}", this.num_users);
      LOG.debug("# of TWEETS: {}", this.num_tweets);
      LOG.debug("# of FOLLOWS: {}", this.num_follows);
    }
  }

  @Override
  public List<LoaderThread> createLoaderThreads() {
    List<LoaderThread> threads = new ArrayList<>();
    final int numLoaders = this.benchmark.getWorkloadConfiguration().getLoaderThreads();
    // 먼저 USERS를 로드합니다
    final int itemsPerThread = Math.max(this.num_users / numLoaders, 1);
    final int numUserThreads = (int) Math.ceil((double) this.num_users / itemsPerThread);
    // 그 다음 FOLLOWS와 TWEETS를 로드합니다
    final long tweetsPerThread = Math.max(this.num_tweets / numLoaders, 1);
    final int numTweetThreads = (int) Math.ceil((double) this.num_tweets / tweetsPerThread);

    final CountDownLatch userLatch = new CountDownLatch(numUserThreads);

    // USERS
    for (int i = 0; i < numUserThreads; i++) {
      final int lo = i * itemsPerThread + 1;
      final int hi = Math.min(this.num_users, (i + 1) * itemsPerThread);

      threads.add(
          new LoaderThread(this.benchmark) {
            @Override
            public void load(Connection conn) throws SQLException {
              loadUsers(conn, lo, hi);
            }

            @Override
            public void afterLoad() {
              userLatch.countDown();
            }
          });
    }

    // FOLLOW_DATA는 USERS에 의존합니다
    for (int i = 0; i < numUserThreads; i++) {
      final int lo = i * itemsPerThread + 1;
      final int hi = Math.min(this.num_users, (i + 1) * itemsPerThread);

      threads.add(
          new LoaderThread(this.benchmark) {
            @Override
            public void load(Connection conn) throws SQLException {

              loadFollowData(conn, lo, hi);
            }

            @Override
            public void beforeLoad() {
              try {
                userLatch.await();
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            }
          });
    }

    // TWEETS는 USERS에 의존합니다
    for (int i = 0; i < numTweetThreads; i++) {
      final long lo = i * tweetsPerThread + 1;
      final long hi = Math.min(this.num_tweets, (i + 1) * tweetsPerThread);

      threads.add(
          new LoaderThread(this.benchmark) {
            @Override
            public void load(Connection conn) throws SQLException {

              loadTweets(conn, lo, hi);
            }

            @Override
            public void beforeLoad() {
              try {
                userLatch.await();
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            }
          });
    }

    return threads;
  }

  /**
   * @throws SQLException
   * @author Djellel num_users명의 사용자를 로드합니다.
   */
  protected void loadUsers(Connection conn, int lo, int hi) throws SQLException {
    Table catalog_tbl = benchmark.getCatalog().getTable(TwitterConstants.TABLENAME_USER);

    String sql = SQLUtil.getInsertSQL(catalog_tbl, this.getDatabaseType());

    int total = 0;

    try (PreparedStatement userInsert = conn.prepareStatement(sql)) {
      int batchSize = 0;

      NameHistogram name_h = new NameHistogram();
      FlatHistogram<Integer> name_len_rng = new FlatHistogram<>(this.rng(), name_h);

      for (int i = lo; i <= hi; i++) {
        // 이 사용자에 대한 무작위 사용자 이름 생성
        int name_length = name_len_rng.nextValue();
        String name = TextGenerator.randomStr(this.rng(), name_length);

        userInsert.setInt(1, i); // ID
        userInsert.setString(2, name); // NAME
        userInsert.setString(3, name + "@tweeter.com"); // EMAIL
        userInsert.setNull(4, java.sql.Types.INTEGER);
        userInsert.setNull(5, java.sql.Types.INTEGER);
        userInsert.setNull(6, java.sql.Types.INTEGER);
        userInsert.addBatch();

        batchSize++;
        total++;
        if ((batchSize % workConf.getBatchSize()) == 0) {
          userInsert.executeBatch();

          userInsert.clearBatch();
          batchSize = 0;
          if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Users %d / %d", total, this.num_users));
          }
        }
      }
      if (batchSize > 0) {
        userInsert.executeBatch();
        userInsert.clearBatch();
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug(String.format("Users Loaded [%d]", total));
    }
  }

  /**
   * @throws SQLException
   * @author Djellel 여기서 무엇이 일어나나요?: 트윗 수는 num_tweets로 고정되어 있으며, 분포를 사용하여 트윗을 작성한 사용자를 단순히 선택합니다
   */
  protected void loadTweets(Connection conn, long lo, long hi) throws SQLException {
    Table catalog_tbl = benchmark.getCatalog().getTable(TwitterConstants.TABLENAME_TWEETS);

    String sql = SQLUtil.getInsertSQL(catalog_tbl, this.getDatabaseType());

    int total = 0;

    try (PreparedStatement tweetInsert = conn.prepareStatement(sql)) {

      int batchSize = 0;
      ScrambledZipfianGenerator zy = new ScrambledZipfianGenerator(1, this.num_users);

      TweetHistogram tweet_h = new TweetHistogram();
      FlatHistogram<Integer> tweet_len_rng = new FlatHistogram<>(this.rng(), tweet_h);

      for (long i = lo; i <= hi; i++) {
        int uid = zy.nextInt();
        tweetInsert.setLong(1, i);
        tweetInsert.setInt(2, uid);
        tweetInsert.setString(3, TextGenerator.randomStr(this.rng(), tweet_len_rng.nextValue()));
        tweetInsert.setNull(4, java.sql.Types.DATE);
        tweetInsert.addBatch();
        batchSize++;
        total++;

        if ((batchSize % workConf.getBatchSize()) == 0) {
          tweetInsert.executeBatch();
          tweetInsert.clearBatch();
          batchSize = 0;
          if (LOG.isDebugEnabled()) {
            LOG.debug("tweet % {}/{}", total, this.num_tweets);
          }
        }
      }
      if (batchSize > 0) {
        tweetInsert.executeBatch();
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("[Tweets Loaded] {}", this.num_tweets);
    }
  }

  /**
   * @throws SQLException
   * @author Djellel 여기서 무엇이 일어나나요?: 각 사용자(팔로워)에 대해 팔로우하는 사용자 수(팔로위 목록)를 선택한 다음 해당 목록을 채우기 위해 사용자를
   *     선택합니다. 선택은 분포를 기반으로 합니다. 참고: 상관관계를 피하기 위해 두 가지 다른 분포를 사용합니다: ZipfianGenerator (가장 많이 팔로우되는
   *     사용자 설명) ScrambledZipfianGenerator (활발한 트위터 사용자 설명)
   */
  protected void loadFollowData(Connection conn, int lo, int hi) throws SQLException {

    int total = 1;

    Table followsTable = benchmark.getCatalog().getTable(TwitterConstants.TABLENAME_FOLLOWS);
    Table followersTable = benchmark.getCatalog().getTable(TwitterConstants.TABLENAME_FOLLOWERS);

    String followsTableSql = SQLUtil.getInsertSQL(followsTable, this.getDatabaseType());
    String followersTableSql = SQLUtil.getInsertSQL(followersTable, this.getDatabaseType());

    try (PreparedStatement followsInsert = conn.prepareStatement(followsTableSql);
        PreparedStatement followersInsert = conn.prepareStatement(followersTableSql)) {

      int batchSize = 0;

      ZipfianGenerator zipfFollowee = new ZipfianGenerator(rng(), 1, this.num_users, 1.75);
      ZipfianGenerator zipfFollows = new ZipfianGenerator(rng(), this.num_follows, 1.75);
      List<Integer> followees = new ArrayList<>();
      for (int follower = lo; follower <= hi; follower++) {
        followees.clear();
        int time = zipfFollows.nextInt();
        if (time == 0) {
          time = 1; // 최소한 이 팔로워는 1명의 사용자를 팔로우합니다
        }
        for (int f = 0; f < time; ) {
          int followee = zipfFollowee.nextInt();
          if (follower != followee && !followees.contains(followee)) {
            followsInsert.setInt(1, follower);
            followsInsert.setInt(2, followee);
            followsInsert.addBatch();

            followersInsert.setInt(1, followee);
            followersInsert.setInt(2, follower);
            followersInsert.addBatch();

            followees.add(followee);

            total++;
            batchSize++;

            if ((batchSize % workConf.getBatchSize()) == 0) {
              followsInsert.executeBatch();
              followersInsert.executeBatch();
              followsInsert.clearBatch();
              followersInsert.clearBatch();
              batchSize = 0;
              if (LOG.isDebugEnabled()) {
                LOG.debug(
                    "Follows  % {}", (int) (((double) follower / (double) this.num_users) * 100));
              }
            }
          }

          f++;
        }
      }
      if (batchSize > 0) {
        followsInsert.executeBatch();
        followersInsert.executeBatch();
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("[Follows Loaded] {}", total);
    }
  }
}
