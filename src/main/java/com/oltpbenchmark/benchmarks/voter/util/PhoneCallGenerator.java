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

package com.oltpbenchmark.benchmarks.voter.util;

import com.oltpbenchmark.benchmarks.voter.VoterConstants;
import java.util.Random;

public class PhoneCallGenerator {

  private final Random rand;
  private long nextVoteId;
  private final int contestantCount;
  private final int[] votingMap = new int[VoterConstants.AREA_CODES.length];

  public static class PhoneCall {
    public final long voteId;
    public final int contestantNumber;
    public final long phoneNumber;

    protected PhoneCall(long voteId, int contestantNumber, long phoneNumber) {
      this.voteId = voteId;
      this.contestantNumber = contestantNumber;
      this.phoneNumber = phoneNumber;
    }
  }

  public PhoneCallGenerator(Random rng, int clientId, int contestantCount) {
    this.rand = rng;
    this.nextVoteId = clientId * 10000000L;
    this.contestantCount = contestantCount;

<<<<<<< HEAD
<<<<<<< HEAD
    // This is a just a small fudge to make the geographical voting map more interesting for the
    // benchmark!
=======
    // 이것은 벤치마크를 위해 지리적 투표 맵을 더 흥미롭게 만들기 위한 작은 조작입니다!
>>>>>>> master
=======
    // This is a just a small fudge to make the geographical voting map more interesting for the
    // benchmark!
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    for (int i = 0; i < votingMap.length; i++) {
      votingMap[i] = 1;
      if (rand.nextInt(100) >= 30) {
        votingMap[i] = (int) (Math.abs(Math.sin(i) * contestantCount) % contestantCount) + 1;
      }
    }
  }

  /**
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
   * Receives/generates a simulated voting call
   *
   * @return Call details (calling number and contestant to whom the vote is given)
   */
  public PhoneCall receive() {

    // (including invalid votes to demonstrate transaction validating in the database)

    // Pick a random area code for the originating phone call
    int areaCodeIndex = rand.nextInt(VoterConstants.AREA_CODES.length);

    // Pick a contestant number
<<<<<<< HEAD
=======
   * 시뮬레이션된 투표 전화를 수신/생성합니다
   *
   * @return 전화 세부 정보 (발신 번호 및 투표가 주어진 후보자)
   */
  public PhoneCall receive() {

    // (데이터베이스에서 트랜잭션 검증을 시연하기 위해 유효하지 않은 투표 포함)

    // 발신 전화에 대한 무작위 지역 코드 선택
    int areaCodeIndex = rand.nextInt(VoterConstants.AREA_CODES.length);

    // 후보자 번호 선택
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    int contestantNumber = votingMap[areaCodeIndex];
    if (rand.nextBoolean()) {
      contestantNumber = rand.nextInt(contestantCount) + 1;
    }

<<<<<<< HEAD
<<<<<<< HEAD
    //  introduce an invalid contestant every 100 call or so to simulate fraud
    //  and invalid entries (something the transaction validates against)
=======
    // 사기 및 유효하지 않은 항목을 시뮬레이션하기 위해 약 100통의 전화마다 유효하지 않은 후보자 도입
    // (트랜잭션이 검증하는 항목)
>>>>>>> master
=======
    //  introduce an invalid contestant every 100 call or so to simulate fraud
    //  and invalid entries (something the transaction validates against)
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    if (rand.nextInt(100) == 0) {
      contestantNumber = 999;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    // Build the phone number
    long phoneNumber =
        VoterConstants.AREA_CODES[areaCodeIndex] * 10000000L + rand.nextInt(10000000);

    // This needs to be globally unique

    // Return the generated phone number
<<<<<<< HEAD
=======
    // 전화번호 구성
    long phoneNumber =
        VoterConstants.AREA_CODES[areaCodeIndex] * 10000000L + rand.nextInt(10000000);

    // 이것은 전역적으로 고유해야 합니다

    // 생성된 전화번호 반환
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    return new PhoneCall(this.nextVoteId++, contestantNumber, phoneNumber);
  }
}
