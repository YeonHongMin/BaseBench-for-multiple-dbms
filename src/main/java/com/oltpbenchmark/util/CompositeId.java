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

package com.oltpbenchmark.util;

import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;

/**
 * 비트 시프트를 이용해 여러 값을 하나의 long으로 패킹합니다.
 *
 * 작성자: pavlo
 */
public abstract class CompositeId {

  private static final String PAD_STRING = "0";
  public static final int INT_MAX_DIGITS = 10;
  public static final int LONG_MAX_DIGITS = 19;

  protected final String encode(int[] offset_bits) {
    int encodedStringSize = IntStream.of(offset_bits).sum();
    StringBuilder compositeBuilder = new StringBuilder(encodedStringSize);

    String[] decodedValues = this.toArray();
    for (int i = 0; i < decodedValues.length; i++) {
      String value = decodedValues[i];
      int valueLength = offset_bits[i];
      String encodedValue = StringUtils.leftPad(value, valueLength, PAD_STRING);
      compositeBuilder.append(encodedValue);
    }

    return compositeBuilder.toString();
  }

  protected final String[] decode(String composite_id, int[] offset_bits) {
    String[] decodedValues = new String[offset_bits.length];

    int start = 0;
    for (int i = 0; i < decodedValues.length; i++) {
      int valueLength = offset_bits[i];
      int end = start + valueLength;
      decodedValues[i] = StringUtils.substring(composite_id, start, end);
      start = end;
    }
    return decodedValues;
  }

  public abstract String encode();

  public abstract void decode(String composite_id);

  public abstract String[] toArray();
}

