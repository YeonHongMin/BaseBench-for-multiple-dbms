/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
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
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한 및 조건을 준수해 주세요.
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.types;

public enum TransactionStatus {
<<<<<<< HEAD
<<<<<<< HEAD
  /** Unknown status */
  UNKNOWN,
  /** The transaction executed successfully and committed without any errors. */
  SUCCESS,
  /**
   * The transaction executed successfully but then was aborted due to the valid user control code.
   * This is not an error.
   */
  USER_ABORTED,
  /** The transaction did not execute due to internal benchmark state. It should be retried */
  RETRY,

  /**
   * The transaction did not execute due to internal benchmark state. The Worker should retry but
   * select a new random transaction to execute.
   */
  RETRY_DIFFERENT,

  /** Transaction encountered an error and was not retried */
=======
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  /** 상태를 알 수 없음 */
  UNKNOWN,
  /** 트랜잭션이 오류 없이 정상 실행되어 커밋됨 */
  SUCCESS,
  /** 트랜잭션은 정상 실행되었지만 유효한 사용자 제어 코드에 따라 중단되었습니다. 이는 오류로 간주되지 않습니다. */
  USER_ABORTED,
  /** 내부 벤치마크 상태로 실행되지 않았으며, 재시도해야 합니다. */
  RETRY,

  /** 내부 벤치마크 상태로 실행되지 않았으며 워커는 재시도하되 새로 무작위 트랜잭션을 선택해 실행해야 합니다. */
  RETRY_DIFFERENT,

  /** 트랜잭션이 오류를 겪었고 재시도되지 않았음 */
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
  ERROR
}
