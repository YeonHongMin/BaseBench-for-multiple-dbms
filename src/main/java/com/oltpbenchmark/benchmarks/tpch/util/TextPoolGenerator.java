/*
 * Copyright 2020 Trino
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
 */
package com.oltpbenchmark.benchmarks.tpch.util;

import static java.util.Objects.requireNonNull;

import com.oltpbenchmark.util.RowRandomInt;
import com.oltpbenchmark.util.StringUtil;
import java.util.List;

public class TextPoolGenerator {
  private static final int MAX_SENTENCE_LENGTH = 256;

  private final int size;
  private final TextGenerationProgressMonitor monitor;

  private final ParsedDistribution grammars;
  private final ParsedDistribution nounPhrases;
  private final ParsedDistribution verbPhrases;
  private final IndexedDistribution prepositions;
  private final IndexedDistribution terminators;
  private final IndexedDistribution adverbs;
  private final IndexedDistribution verbs;
  private final IndexedDistribution auxiliaries;
  private final IndexedDistribution articles;
  private final IndexedDistribution adjectives;
  private final IndexedDistribution nouns;

  public TextPoolGenerator(int size, Distributions distributions) {
    this(
        size,
        distributions,
        new TextGenerationProgressMonitor() {
          @Override
          public void updateProgress(double progress) {}
        });
  }

  public TextPoolGenerator(
      int size, Distributions distributions, TextGenerationProgressMonitor monitor) {
    this.size = size;
    requireNonNull(distributions, "distributions is null");
    this.monitor = requireNonNull(monitor, "monitor is null");

    this.grammars = new ParsedDistribution(distributions.getGrammars());
    this.nounPhrases = new ParsedDistribution(distributions.getNounPhrase());
    this.verbPhrases = new ParsedDistribution(distributions.getVerbPhrase());

    prepositions = new IndexedDistribution(distributions.getPrepositions());
    terminators = new IndexedDistribution(distributions.getTerminators());
    adverbs = new IndexedDistribution(distributions.getAdverbs());
    verbs = new IndexedDistribution(distributions.getVerbs());
    auxiliaries = new IndexedDistribution(distributions.getAuxiliaries());
    articles = new IndexedDistribution(distributions.getArticles());
    adjectives = new IndexedDistribution(distributions.getAdjectives());
    nouns = new IndexedDistribution(distributions.getNouns());
  }

  public String generate() {
    StringBuilder output = new StringBuilder(size + MAX_SENTENCE_LENGTH);

    RowRandomInt randomInt = new RowRandomInt(933588178L, Integer.MAX_VALUE);

    while (output.length() < size) {
      generateSentence(output, randomInt);
      monitor.updateProgress(Math.min(1.0 * output.length() / size, 1.0));
    }
    output.setLength(size);
    return output.toString();
  }

  private void generateSentence(StringBuilder builder, RowRandomInt random) {
    int index = grammars.getRandomIndex(random);
    for (char token : grammars.getTokens(index)) {
      switch (token) {
        case 'V':
          generateVerbPhrase(builder, random);
          break;
        case 'N':
          generateNounPhrase(builder, random);
          break;
        case 'P':
          String preposition = prepositions.randomValue(random);
          builder.append(preposition);
          builder.append(" the ");
          generateNounPhrase(builder, random);
          break;
        case 'T':
<<<<<<< HEAD
<<<<<<< HEAD
          // trim trailing space
          // terminators should abut previous word
=======
          // 후행 공백 제거
          // 종결자는 이전 단어에 인접해야 합니다.
>>>>>>> master
=======
          // trim trailing space
          // terminators should abut previous word
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
          builder.setLength(builder.length() - 1);
          String terminator = terminators.randomValue(random);
          builder.append(terminator);
          break;
        default:
          throw new IllegalStateException("Unknown token '" + token + "'");
      }
      if (builder.charAt(builder.length() - 1) != ' ') {
        builder.append(' ');
      }
    }
  }

  private void generateVerbPhrase(StringBuilder builder, RowRandomInt random) {
    int index = verbPhrases.getRandomIndex(random);
    for (char token : verbPhrases.getTokens(index)) {
<<<<<<< HEAD
<<<<<<< HEAD
      // pick a random word
=======
      // 무작위 단어 선택
>>>>>>> master
=======
      // pick a random word
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      switch (token) {
        case 'D':
          builder.append(adverbs.randomValue(random));
          break;
        case 'V':
          builder.append(verbs.randomValue(random));
          break;
        case 'X':
          builder.append(auxiliaries.randomValue(random));
          break;
        default:
          throw new IllegalStateException("Unknown token '" + token + "'");
      }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      // string may end with a comma or such
      builder.append(nounPhrases.getBonusText(index));

      // add a space
<<<<<<< HEAD
=======
      // 문자열은 쉼표 등으로 끝날 수 있습니다.
      builder.append(nounPhrases.getBonusText(index));

      // 공백 추가
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      builder.append(" ");
    }
  }

  private void generateNounPhrase(StringBuilder builder, RowRandomInt random) {
    int index = nounPhrases.getRandomIndex(random);
    for (char token : nounPhrases.getTokens(index)) {
<<<<<<< HEAD
<<<<<<< HEAD
      // pick a random word
=======
      // 무작위 단어 선택
>>>>>>> master
=======
      // pick a random word
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      switch (token) {
        case 'A':
          builder.append(articles.randomValue(random));
          break;
        case 'J':
          builder.append(adjectives.randomValue(random));
          break;
        case 'D':
          builder.append(adverbs.randomValue(random));
          break;
        case 'N':
          builder.append(nouns.randomValue(random));
          break;
        default:
          throw new IllegalStateException("Unknown token '" + token + "'");
      }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      // string may end with a comma or such
      builder.append(nounPhrases.getBonusText(index));

      // add a space
<<<<<<< HEAD
=======
      // 문자열은 쉼표 등으로 끝날 수 있습니다.
      builder.append(nounPhrases.getBonusText(index));

      // 공백 추가
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
      builder.append(" ");
    }
  }

  public interface TextGenerationProgressMonitor {
    void updateProgress(double progress);
  }

  private static class IndexedDistribution {
    private final String[] randomTable;

    private IndexedDistribution(Distribution distribution) {
      randomTable = new String[distribution.getWeight(distribution.size() - 1)];
      int valueIndex = 0;
      for (int i = 0; i < randomTable.length; i++) {
        if (i >= distribution.getWeight(valueIndex)) {
          valueIndex++;
        }
        randomTable[i] = distribution.getValue(valueIndex);
      }
    }

    public String randomValue(RowRandomInt random) {
      int randomIndex = random.nextInt(0, randomTable.length - 1);
      return randomTable[randomIndex];
    }
  }

  private static class ParsedDistribution {
    private final char[][] parsedDistribution;
    private final String[] bonusText;

    private final int[] randomTable;

    private ParsedDistribution(Distribution distribution) {
      parsedDistribution = new char[distribution.size()][];
      bonusText = new String[distribution.size()];
      for (int i = 0; i < distribution.size(); i++) {
        List<String> tokens =
            StringUtil.splitToList(StringUtil.WHITESPACE, distribution.getValue(i));

        parsedDistribution[i] = new char[tokens.size()];
        for (int j = 0; j < parsedDistribution[i].length; j++) {
          String token = tokens.get(j);
          parsedDistribution[i][j] = token.charAt(0);
          bonusText[i] = token.substring(1);
        }
      }

      randomTable = new int[distribution.getWeight(distribution.size() - 1)];
      int valueIndex = 0;
      for (int i = 0; i < randomTable.length; i++) {
        if (i >= distribution.getWeight(valueIndex)) {
          valueIndex++;
        }
        randomTable[i] = valueIndex;
      }
    }

    public int getRandomIndex(RowRandomInt random) {
      int randomIndex = random.nextInt(0, randomTable.length - 1);
      return randomTable[randomIndex];
    }

    public char[] getTokens(int index) {
      return parsedDistribution[index];
    }

    public String getBonusText(int index) {
      return bonusText[index];
    }
  }
}
