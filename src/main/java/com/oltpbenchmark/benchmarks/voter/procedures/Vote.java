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

/* 이 파일은 VoltDB의 일부입니다.
 * Copyright (C) 2008-2012 VoltDB Inc.
 *
 * 다음 조건에 따라, 이 소프트웨어 및 관련 문서 파일(이하 "소프트웨어")의
 * 사본을 얻는 모든 사람에게 무료로, 제한 없이 소프트웨어를 다루는 권한이
 * 부여됩니다. 여기에는 사용, 복사, 수정, 병합, 게시, 배포, 재라이센스 및/또는
 * 소프트웨어의 사본을 판매하는 권리와 소프트웨어가 제공된 사람들이 그러한
 * 권한을 행사할 수 있도록 하는 권리가 제한 없이 포함됩니다:
 *
 * 위의 저작권 고지와 이 권한 고지는 소프트웨어의 모든 사본 또는 중요한 부분에
 * 포함되어야 합니다.
 *
 * 소프트웨어는 "있는 그대로" 제공되며, 상품성, 특정 목적에의 적합성 및
 * 비침해에 대한 보증을 포함하되 이에 국한되지 않는 어떠한 종류의 명시적이거나
 * 묵시적인 보증 없이 제공됩니다. 어떠한 경우에도 저자는 계약, 불법 행위 또는
 * 기타 행위에 의해 발생하는 소프트웨어 또는 소프트웨어의 사용 또는 기타 거래와
 * 관련하여 발생하는 모든 청구, 손해 또는 기타 책임에 대해 책임을 지지 않습니다.
 */

//
// 투표를 수락하고 비즈니스 로직을 적용합니다: 투표가 유효한 후보자에 대한 것인지,
// 그리고 투표자(발신자의 전화번호)가 허용된 투표 수를 초과하지 않았는지 확인합니다.
//

package com.oltpbenchmark.benchmarks.voter.procedures;

import static com.oltpbenchmark.benchmarks.voter.VoterConstants.*;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Vote extends Procedure {

  // 가능한 반환 코드
  public static final long VOTE_SUCCESSFUL = 0;
  public static final long ERR_INVALID_CONTESTANT = 1;
  public static final long ERR_VOTER_OVER_VOTE_LIMIT = 2;

  // 투표가 유효한 후보자에 대한 것인지 확인합니다
  public final SQLStmt checkContestantStmt =
      new SQLStmt(
          "SELECT contestant_number FROM "
              + TABLENAME_CONTESTANTS
              + " WHERE contestant_number = ?");

  // 투표자가 허용된 투표 수를 초과했는지 확인합니다
  public final SQLStmt checkVoterStmt =
      new SQLStmt("SELECT COUNT(*) FROM " + TABLENAME_VOTES + " WHERE phone_number = ?");

  // 지역 코드를 확인하여 해당 주를 조회합니다
  public final SQLStmt checkStateStmt =
      new SQLStmt("SELECT state FROM " + TABLENAME_LOCATIONS + " WHERE area_code = ?");

  // 투표를 기록합니다
  public final SQLStmt insertVoteStmt =
      new SQLStmt(
          "INSERT INTO "
              + TABLENAME_VOTES
              + " (vote_id, phone_number, state, contestant_number, created) "
              + "VALUES (?, ?, ?, ?, NOW())");

  public long run(
      Connection conn,
      long voteId,
      long phoneNumber,
      int contestantNumber,
      long maxVotesPerPhoneNumber)
      throws SQLException {

    try (PreparedStatement ps = getPreparedStatement(conn, checkContestantStmt)) {
      ps.setInt(1, contestantNumber);
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) {
          return ERR_INVALID_CONTESTANT;
        }
      }
    }

    try (PreparedStatement ps = getPreparedStatement(conn, checkVoterStmt)) {
      ps.setLong(1, phoneNumber);
      try (ResultSet rs = ps.executeQuery()) {
        boolean hasVoterEnt = rs.next();
        if (hasVoterEnt && rs.getLong(1) >= maxVotesPerPhoneNumber) {
          return ERR_VOTER_OVER_VOTE_LIMIT;
        }
      }
    }

    String state = null;

    try (PreparedStatement ps = getPreparedStatement(conn, checkStateStmt)) {
      ps.setShort(1, (short) (phoneNumber / 10000000L));
      try (ResultSet rs = ps.executeQuery()) {
        // 일부 샘플 클라이언트 라이브러리는 대부분 유효하지 않은 전화번호를 생성하는
        // 레거시 무작위 전화 생성 방식을 사용합니다. 리팩토링까지는 이러한 모든 투표를
        // "XX" 가짜 주로 재할당합니다(이러한 투표는 Live Statistics 대시보드에 표시되지 않지만,
        // 유효하지 않은 것으로 처리되는 대신 합법적인 것으로 추적됩니다. 이는 오래된 클라이언트가
        // 대부분 잘못 처리하여 모든 트랜잭션이 거부되는 것을 방지하기 위함입니다).
        state = rs.next() ? rs.getString(1) : "XX";
      }
    }

    try (PreparedStatement ps = getPreparedStatement(conn, insertVoteStmt)) {
      ps.setLong(1, voteId);
      ps.setLong(2, phoneNumber);
      ps.setString(3, state);
      ps.setInt(4, contestantNumber);
      ps.execute();
    }

    // 반환 값을 0으로 설정: 성공적인 투표
    return VOTE_SUCCESSFUL;
  }
}
