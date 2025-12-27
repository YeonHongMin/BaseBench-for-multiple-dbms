/*
 * 저작권 2020 OLTPBenchmark 프로젝트
 *
 * Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 * 라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 * 이 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 * 라이선스에서 허용하는 권한과 제한 사항은
 * 라이선스의 본문을 참조하십시오.
 *
 */

/*
 *  Copyright (C) 2012 by H-Store Project
 *  Brown University
 *  Massachusetts Institute of Technology
 *  Yale University
 *
 *  http://hstore.cs.brown.edu/
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

package com.oltpbenchmark.util;

import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LatchedExceptionHandler implements Thread.UncaughtExceptionHandler {
  private final Logger LOG = LoggerFactory.getLogger(getClass());

  private final CountDownLatch latch;

  public LatchedExceptionHandler(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    LOG.error(
        String.format(
            "Uncaught Exception in thread with message: [%s]; will count down latch with count %d",
            e.getMessage(), this.latch.getCount()),
        e);
    while (this.latch.getCount() > 0) {
      this.latch.countDown();
    }
  }
}
