/*
 * Copyright 2020 Trino
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
 */
package com.oltpbenchmark.benchmarks.tpch.util;

import static com.oltpbenchmark.benchmarks.tpch.util.Distributions.getDefaultDistributions;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.Objects.requireNonNull;

import com.oltpbenchmark.util.RowRandomInt;

public class TextPool {
  private static final int DEFAULT_TEXT_POOL_SIZE = 300 * 1024 * 1024;
  private static final int MAX_SENTENCE_LENGTH = 256;

  private static final TextPool DEFAULT_TEXT_POOL =
      new TextPool(DEFAULT_TEXT_POOL_SIZE, getDefaultDistributions());

  public static TextPool getDefaultTestPool() {
    return DEFAULT_TEXT_POOL;
  }

  private final byte[] textPool;
  private final int textPoolSize;

  public TextPool(int size, Distributions distributions) {
    this(size, distributions, progress -> {});
  }

  public TextPool(int size, Distributions distributions, TextGenerationProgressMonitor monitor) {
    requireNonNull(distributions, "distributions is null");
    requireNonNull(monitor, "monitor is null");

    ByteArrayBuilder output = new ByteArrayBuilder(size + MAX_SENTENCE_LENGTH);

    RowRandomInt randomInt = new RowRandomInt(933588178L, Integer.MAX_VALUE);

    while (output.getLength() < size) {
      generateSentence(distributions, output, randomInt);
      monitor.updateProgress(Math.min(1.0 * output.getLength() / size, 1.0));
    }
    output.erase(output.getLength() - size);
    textPool = output.getBytes();
    textPoolSize = output.getLength();
  }

  public int size() {
    return textPoolSize;
  }

  public String getText(int begin, int end) {
    if (end > textPoolSize) {
      throw new IndexOutOfBoundsException(
          format("Index %d is beyond end of text pool (size = %d)", end, textPoolSize));
    }
    return new String(textPool, begin, end - begin, US_ASCII);
  }

  private static void generateSentence(
      Distributions distributions, ByteArrayBuilder builder, RowRandomInt random) {
    String syntax = distributions.getGrammars().randomValue(random);

    int maxLength = syntax.length();
    for (int i = 0; i < maxLength; i += 2) {
      switch (syntax.charAt(i)) {
        case 'V':
          generateVerbPhrase(distributions, builder, random);
          break;
        case 'N':
          generateNounPhrase(distributions, builder, random);
          break;
        case 'P':
          String preposition = distributions.getPrepositions().randomValue(random);
          builder.append(preposition);
          builder.append(" the ");
          generateNounPhrase(distributions, builder, random);
          break;
        case 'T':
          // 후행 공백 제거
          // 종결자는 이전 단어에 인접해야 합니다.
          builder.erase(1);
          String terminator = distributions.getTerminators().randomValue(random);
          builder.append(terminator);
          break;
        default:
          throw new IllegalArgumentException("Unknown token '" + syntax.charAt(i) + "'");
      }
      if (builder.getLastChar() != ' ') {
        builder.append(" ");
      }
    }
  }

  private static void generateVerbPhrase(
      Distributions distributions, ByteArrayBuilder builder, RowRandomInt random) {
    String syntax = distributions.getVerbPhrase().randomValue(random);
    int maxLength = syntax.length();
    for (int i = 0; i < maxLength; i += 2) {
      Distribution source;
      switch (syntax.charAt(i)) {
        case 'D':
          source = distributions.getAdverbs();
          break;
        case 'V':
          source = distributions.getVerbs();
          break;
        case 'X':
          source = distributions.getAuxiliaries();
          break;
        default:
          throw new IllegalArgumentException("Unknown token '" + syntax.charAt(i) + "'");
      }

      // 무작위 단어 선택
      String word = source.randomValue(random);
      builder.append(word);

      // 공백 추가
      builder.append(" ");
    }
  }

  private static void generateNounPhrase(
      Distributions distributions, ByteArrayBuilder builder, RowRandomInt random) {
    String syntax = distributions.getNounPhrase().randomValue(random);
    int maxLength = syntax.length();
    for (int i = 0; i < maxLength; i++) {
      Distribution source;
      switch (syntax.charAt(i)) {
        case 'A':
          source = distributions.getArticles();
          break;
        case 'J':
          source = distributions.getAdjectives();
          break;
        case 'D':
          source = distributions.getAdverbs();
          break;
        case 'N':
          source = distributions.getNouns();
          break;
        case ',':
          builder.erase(1);
          builder.append(", ");
          continue;
        case ' ':
          continue;
        default:
          throw new IllegalArgumentException("Unknown token '" + syntax.charAt(i) + "'");
      }
      // 무작위 단어 선택
      String word = source.randomValue(random);
      builder.append(word);

      // 공백 추가
      builder.append(" ");
    }
  }

  public interface TextGenerationProgressMonitor {
    void updateProgress(double progress);
  }

  private static class ByteArrayBuilder {
    private int length;
    private final byte[] bytes;

    public ByteArrayBuilder(int size) {
      this.bytes = new byte[size];
    }

    @SuppressWarnings("deprecation")
    public void append(String string) {
      // 데이터가 ASCII이므로 안전합니다.
      string.getBytes(0, string.length(), bytes, length);
      length += string.length();
    }

    public void erase(int count) {
      length -= count;
    }

    public int getLength() {
      return length;
    }

    public byte[] getBytes() {
      return bytes;
    }

    public char getLastChar() {
      return (char) bytes[length - 1];
    }
  }
}
