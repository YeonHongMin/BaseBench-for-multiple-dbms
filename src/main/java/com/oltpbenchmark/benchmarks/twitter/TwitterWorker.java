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

import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionGenerator;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.twitter.procedures.*;
import com.oltpbenchmark.benchmarks.twitter.util.TweetHistogram;
import com.oltpbenchmark.benchmarks.twitter.util.TwitterOperation;
import com.oltpbenchmark.types.TransactionStatus;
import com.oltpbenchmark.util.RandomDistribution.FlatHistogram;
import com.oltpbenchmark.util.TextGenerator;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;

public final class TwitterWorker extends Worker<TwitterBenchmark> {
  private final TransactionGenerator<TwitterOperation> generator;

  private final FlatHistogram<Integer> tweet_len_rng;
  private final int num_users;

  public TwitterWorker(
      TwitterBenchmark benchmarkModule, int id, TransactionGenerator<TwitterOperation> generator) {
    super(benchmarkModule, id);
    this.generator = generator;
    this.num_users =
        (int)
            Math.round(
                TwitterConstants.NUM_USERS * this.getWorkloadConfiguration().getScaleFactor());

    TweetHistogram tweet_h = new TweetHistogram();
    this.tweet_len_rng = new FlatHistogram<>(this.rng(), tweet_h);
  }

  @Override
  protected TransactionStatus executeWork(Connection conn, TransactionType nextTrans)
      throws UserAbortException, SQLException {
    TwitterOperation t = generator.nextTransaction();
    // 0은 유효하지 않은 ID이므로, 여기서 무작위 값을 최소 1로 고정합니다
    t.uid = this.rng().nextInt(this.num_users - 1) + 1;

    if (nextTrans.getProcedureClass().equals(GetTweet.class)) {
      doSelect1Tweet(conn, t.tweetid);
    } else if (nextTrans.getProcedureClass().equals(GetTweetsFromFollowing.class)) {
      doSelectTweetsFromPplIFollow(conn, t.uid);
    } else if (nextTrans.getProcedureClass().equals(GetFollowers.class)) {
      doSelectNamesOfPplThatFollowMe(conn, t.uid);
    } else if (nextTrans.getProcedureClass().equals(GetUserTweets.class)) {
      doSelectTweetsForUid(conn, t.uid);
    } else if (nextTrans.getProcedureClass().equals(InsertTweet.class)) {
      int len = this.tweet_len_rng.nextValue();
      String text = TextGenerator.randomStr(this.rng(), len);
      doInsertTweet(conn, t.uid, text);
    }
    return (TransactionStatus.SUCCESS);
  }

  public void doSelect1Tweet(Connection conn, int tweet_id) throws SQLException {
    GetTweet proc = this.getProcedure(GetTweet.class);

    proc.run(conn, tweet_id);
  }

  public void doSelectTweetsFromPplIFollow(Connection conn, int uid) throws SQLException {
    GetTweetsFromFollowing proc = this.getProcedure(GetTweetsFromFollowing.class);

    proc.run(conn, uid);
  }

  public void doSelectNamesOfPplThatFollowMe(Connection conn, int uid) throws SQLException {
    GetFollowers proc = this.getProcedure(GetFollowers.class);

    proc.run(conn, uid);
  }

  public void doSelectTweetsForUid(Connection conn, int uid) throws SQLException {
    GetUserTweets proc = this.getProcedure(GetUserTweets.class);

    proc.run(conn, uid);
  }

  public void doInsertTweet(Connection conn, int uid, String text) throws SQLException {
    InsertTweet proc = this.getProcedure(InsertTweet.class);

    Time time = new Time(System.currentTimeMillis());
    proc.run(conn, uid, text, time);
  }
}
