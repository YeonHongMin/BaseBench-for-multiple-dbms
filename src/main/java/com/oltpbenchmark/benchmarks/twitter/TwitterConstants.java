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

public abstract class TwitterConstants {

  public static final String TABLENAME_USER = "user_profiles";
  public static final String TABLENAME_TWEETS = "tweets";
  public static final String TABLENAME_FOLLOWS = "follows";
  public static final String TABLENAME_FOLLOWERS = "followers";
  public static final String TABLENAME_ADDED_TWEETS = "added_tweets";

  /** 사용자 기준 수 */
  public static final int NUM_USERS = 500;

  /** 트윗 기준 수 */
  public static final int NUM_TWEETS = 20000;

  /** 사용자당 최대 팔로우 기준 */
  public static final int MAX_FOLLOW_PER_USER = 50;

  /** 메시지 길이 (포함) */
  public static final int MAX_TWEET_LENGTH = 140;

  /** 이름 길이 (포함) */
  public static final int MIN_NAME_LENGTH = 3;

  public static final int MAX_NAME_LENGTH = 20;
  // TODO: 다음 매개변수를 WorkLoadConfiguration의 매개변수로 만들기
  public static int LIMIT_TWEETS = 100;
  public static int LIMIT_TWEETS_FOR_UID = 10;
  public static int LIMIT_FOLLOWERS = 20;
}
