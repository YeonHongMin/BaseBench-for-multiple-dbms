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

package com.oltpbenchmark.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL 문을 위한 래퍼 클래스
 *
 * @author pavlo
 */
public final class SQLStmt {
  private static final Logger LOG = LoggerFactory.getLogger(SQLStmt.class);

  private static final Pattern SUBSTITUTION_PATTERN = Pattern.compile("\\?\\?");

  private String orig_sql;
  private String sql;

  /** 이 Statement의 SQL에서 발견되는 각 고유한 '??'를 이 배열에 지정된 수의 '?'로 대체합니다. */
  private final int[] substitutions;

  /**
   * 생성자
   *
   * @param sql
   * @param substitutions
   */
  public SQLStmt(String sql, int... substitutions) {
    this.substitutions = substitutions;
    this.setSQL(sql);
  }

  /**
   * 마법의 SQL 설정자! 패턴 "??"의 각 발생은 반복된 ?의 문자열로 대체됩니다.
   *
   * @param sql
   */
  public final void setSQL(String sql) {
    this.orig_sql = sql.trim();
    for (int ctr : this.substitutions) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < ctr; i++) {
        sb.append(i > 0 ? ", " : "").append("?");
      }
      Matcher m = SUBSTITUTION_PATTERN.matcher(sql);
      String replace = sb.toString();
      sql = m.replaceFirst(replace);
    }
    this.sql = sql;
    if (LOG.isDebugEnabled()) {
      LOG.debug("Initialized SQL:\n{}", this.sql);
    }
  }

  public final String getSQL() {
    return (this.sql);
  }

  protected final String getOriginalSQL() {
    return (this.orig_sql);
  }

  @Override
  public String toString() {
    return "SQLStmt{" + this.sql + "}";
  }
}
