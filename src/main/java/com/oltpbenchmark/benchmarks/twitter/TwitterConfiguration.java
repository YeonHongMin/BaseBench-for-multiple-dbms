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

import com.oltpbenchmark.WorkloadConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;

public class TwitterConfiguration {

  private final XMLConfiguration xmlConfig;

  public TwitterConfiguration(WorkloadConfiguration workConf) {
    this.xmlConfig = workConf.getXmlConfig();
  }

  public String getTracefile() {
    return xmlConfig.getString("tracefile", null);
  }

  public String getTracefile2() {
    return xmlConfig.getString("tracefile2", null);
  }
}
