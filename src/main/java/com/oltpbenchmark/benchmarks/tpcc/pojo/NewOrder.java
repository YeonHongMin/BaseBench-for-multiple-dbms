/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한과 조건을 준수해 주세요.
 *
 */

package com.oltpbenchmark.benchmarks.tpcc.pojo;

import java.io.Serializable;

public class NewOrder implements Serializable {
  private static final long serialVersionUID = 1L;

  public int no_w_id;
  public int no_d_id;
  public int no_o_id;

  @Override
  public String toString() {
    return ("\n***************** NewOrder ********************"
        + "\n*      no_w_id = "
        + no_w_id
        + "\n*      no_d_id = "
        + no_d_id
        + "\n*      no_o_id = "
        + no_o_id
        + "\n**********************************************");
  }
}
