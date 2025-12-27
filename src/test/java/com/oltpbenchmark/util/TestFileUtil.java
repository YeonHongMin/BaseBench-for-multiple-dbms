/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Apache License, Version 2.0 (the "License")에 따라 라이선스가 부여됩니다.
 * 이 파일을 사용하려면 라이선스와 일치해야 합니다.
 * 다음에서 라이선스 사본을 얻을 수 있습니다:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 동의하지 않는 한,
 * 라이선스에 따라 배포되는 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적이거나 암묵적인 어떠한 종류의 보증이나 조건도 없습니다.
 * 라이선스에서 허용되는 특정 언어에 대한 권한과
 * 제한 사항을 참조하십시오.
 *
 */

package com.oltpbenchmark.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestFileUtil {

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  void touch(String name) {
    try {
      File f = new File(name);
      if (!f.exists()) new FileOutputStream(f).close();
      f.setLastModified(TimeUtil.getCurrentTime().getTime());
    } catch (IOException e) {
    }
  }

  void rm(String name) {
    File file = new File(name);
    file.delete();
  }

  @Test
  public void testIncrementFileNames() {

    String basename = "base.res";
    assertEquals("base.res", FileUtil.getNextFilename(basename));
    touch("base.res");
    assertEquals("base.1.res", FileUtil.getNextFilename(basename));
    assertEquals("base.1.res", FileUtil.getNextFilename(basename));
    touch("base.1.res");
    assertEquals("base.2.res", FileUtil.getNextFilename(basename));

    rm("base.res");
    rm("base.1.res");
    rm("base.2.res");
  }
}
