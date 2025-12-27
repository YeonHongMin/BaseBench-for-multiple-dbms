/*
<<<<<<< HEAD
 * Copyright 2020 by OLTPBenchmark Project
 *
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
>>>>>>> master
 *
 */

package com.oltpbenchmark.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

<<<<<<< HEAD
/**
 * @author pavlo
 */
=======
/** 작성자: pavlo */
>>>>>>> master
public abstract class FileUtil {

  private static final Pattern EXT_SPLIT = Pattern.compile("\\.");

  /**
<<<<<<< HEAD
   * Join path components
=======
   * 경로 구성 요소를 연결합니다.
>>>>>>> master
   *
   * @param args
   * @return
   */
  public static String joinPath(String... args) {
    StringBuilder result = new StringBuilder();
    boolean first = true;
    for (String a : args) {
      if (a != null && a.length() > 0) {
        if (!first) {
          result.append("/");
        }
        result.append(a);
        first = false;
      }
    }
    return result.toString();
  }

  /**
<<<<<<< HEAD
   * Given a basename for a file, find the next possible filename if this file already exists. For
   * example, if the file test.res already exists, create a file called, test.1.res
=======
   * 기본 파일명이 이미 존재하면 다음 가능한 파일명을 생성합니다. 예: test.res가 존재하면 test.1.res를 만듭니다.
>>>>>>> master
   *
   * @param basename
   * @return
   */
  public static String getNextFilename(String basename) {

    if (!exists(basename)) return basename;

    File f = new File(basename);
    if (f != null && f.isFile()) {
      String parts[] = EXT_SPLIT.split(basename);

<<<<<<< HEAD
      // Check how many files already exist
=======
      // 이미 존재하는 파일 수를 확인합니다.
>>>>>>> master
      int counter = 1;
      String nextName = parts[0] + "." + counter + "." + parts[1];
      while (exists(nextName)) {
        ++counter;
        nextName = parts[0] + "." + counter + "." + parts[1];
      }
      return nextName;
    }

<<<<<<< HEAD
    // Should we throw instead??
=======
    // 대신 예외를 던져야 할까요?
>>>>>>> master
    return null;
  }

  public static boolean exists(String path) {
    return (new File(path).exists());
  }

  public static String checkPath(String path, String name) throws FileNotFoundException {
    if (path != null) path = path.trim();
    if (path == null || path.isEmpty()) return null;

    if (!FileUtil.exists(path)) {
      throw new FileNotFoundException(name + " not found:" + path);
    }
    return path;
  }

  /**
<<<<<<< HEAD
   * Create any directory in the list paths if it doesn't exist
=======
   * 지정된 경로들 각각에 대해 디렉터리가 없으면 생성합니다.
>>>>>>> master
   *
   * @param paths
   */
  public static void makeDirIfNotExists(String... paths) {
    for (String p : paths) {
      if (p == null) {
        continue;
      }
      File f = new File(p);
      if (!f.exists()) {
        f.mkdirs();
      }
    }
  }

  public static void writeStringToFile(File file, String content) throws IOException {
    try (FileWriter writer = new FileWriter(file)) {
      writer.write(content);
      writer.flush();
    }
  }
}
