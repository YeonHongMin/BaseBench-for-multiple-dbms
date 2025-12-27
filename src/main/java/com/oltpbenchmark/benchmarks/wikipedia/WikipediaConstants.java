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

package com.oltpbenchmark.benchmarks.wikipedia;

public abstract class WikipediaConstants {

  /** The percentage of page updates that are made by anonymous users [0%-100%] */
  public static final int ANONYMOUS_PAGE_UPDATE_PROB = 26;

  /** */
  public static final int ANONYMOUS_USER_ID = 0;

  public static final double USER_ID_SIGMA = 1.0001d;

  /** Length of the tokens */
  public static final int TOKEN_LENGTH = 32;

  /** Number of baseline pages */
  public static final int PAGES = 1000;

  /** Number of baseline Users */
  public static final int USERS = 2000;

  // ----------------------------------------------------------------
  // DISTRIBUTION CONSTANTS
  // ----------------------------------------------------------------

  public static final double NUM_WATCHES_PER_USER_SIGMA = 1.75d;

  public static final int MAX_WATCHES_PER_USER = 1000;

  public static final double WATCHLIST_PAGE_SIGMA = 1.0001d;

  public static final double REVISION_USER_SIGMA = 1.0001d;

  // ----------------------------------------------------------------
  // DATA SET INFORMATION
  // ----------------------------------------------------------------

  /** Table Names */
  public static final String TABLENAME_IPBLOCKS = "ipblocks";

  public static final String TABLENAME_LOGGING = "logging";
  public static final String TABLENAME_PAGE = "page";
  public static final String TABLENAME_PAGE_BACKUP = "page_backup";
  public static final String TABLENAME_PAGE_RESTRICTIONS = "page_restrictions";
  public static final String TABLENAME_RECENTCHANGES = "recentchanges";
  public static final String TABLENAME_REVISION = "revision";
  public static final String TABLENAME_TEXT = "text";
  public static final String TABLENAME_USER = "useracct";
  public static final String TABLENAME_USER_GROUPS = "user_groups";
  public static final String TABLENAME_VALUE_BACKUP = "value_backup";
  public static final String TABLENAME_WATCHLIST = "watchlist";
}
