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

import static com.oltpbenchmark.benchmarks.tpch.util.DistributionLoader.loadDistribution;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Map;

public class Distributions {
  private static final Distributions DEFAULT_DISTRIBUTIONS = loadDefaults();

  private static Distributions loadDefaults() {
    try (InputStream resource =
        Distributions.class.getResourceAsStream("/benchmarks/tpch/dists.dss")) {
      BufferedReader distReader = new BufferedReader(new InputStreamReader(resource, UTF_8));
      return new Distributions(loadDistribution(distReader.lines()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static Distributions getDefaultDistributions() {
    return DEFAULT_DISTRIBUTIONS;
  }

  private final Distribution grammars;
  private final Distribution nounPhrase;
  private final Distribution verbPhrase;
  private final Distribution prepositions;
  private final Distribution nouns;
  private final Distribution verbs;
  private final Distribution articles;
  private final Distribution adjectives;
  private final Distribution adverbs;
  private final Distribution auxiliaries;
  private final Distribution terminators;
  private final Distribution orderPriorities;
  private final Distribution shipInstructions;
  private final Distribution shipModes;
  private final Distribution returnFlags;
  private final Distribution partContainers;
  private final Distribution partColors;
  private final Distribution partTypes;
  private final Distribution marketSegments;
  private final Distribution nations;
  private final Distribution regions;

  public Distributions(Map<String, Distribution> distributions) {
    this.grammars = getDistribution(distributions, "grammar");
    this.nounPhrase = getDistribution(distributions, "np");
    this.verbPhrase = getDistribution(distributions, "vp");
    this.prepositions = getDistribution(distributions, "prepositions");
    this.nouns = getDistribution(distributions, "nouns");
    this.verbs = getDistribution(distributions, "verbs");
    this.articles = getDistribution(distributions, "articles");
    this.adjectives = getDistribution(distributions, "adjectives");
    this.adverbs = getDistribution(distributions, "adverbs");
    this.auxiliaries = getDistribution(distributions, "auxillaries");
    this.terminators = getDistribution(distributions, "terminators");
    this.orderPriorities = getDistribution(distributions, "o_oprio");
    this.shipInstructions = getDistribution(distributions, "instruct");
    this.shipModes = getDistribution(distributions, "smode");
    this.returnFlags = getDistribution(distributions, "rflag");
    this.partContainers = getDistribution(distributions, "p_cntr");
    this.partColors = getDistribution(distributions, "colors");
    this.partTypes = getDistribution(distributions, "p_types");
    this.marketSegments = getDistribution(distributions, "msegmnt");
    this.nations = getDistribution(distributions, "nations");
    this.regions = getDistribution(distributions, "regions");
  }

  public Distribution getAdjectives() {
    return adjectives;
  }

  public Distribution getAdverbs() {
    return adverbs;
  }

  public Distribution getArticles() {
    return articles;
  }

  public Distribution getAuxiliaries() {
    return auxiliaries;
  }

  public Distribution getGrammars() {
    return grammars;
  }

  public Distribution getMarketSegments() {
    return marketSegments;
  }

  public Distribution getNations() {
    return nations;
  }

  public Distribution getNounPhrase() {
    return nounPhrase;
  }

  public Distribution getNouns() {
    return nouns;
  }

  public Distribution getOrderPriorities() {
    return orderPriorities;
  }

  public Distribution getPartColors() {
    return partColors;
  }

  public Distribution getPartContainers() {
    return partContainers;
  }

  public Distribution getPartTypes() {
    return partTypes;
  }

  public Distribution getPrepositions() {
    return prepositions;
  }

  public Distribution getRegions() {
    return regions;
  }

  public Distribution getReturnFlags() {
    return returnFlags;
  }

  public Distribution getShipInstructions() {
    return shipInstructions;
  }

  public Distribution getShipModes() {
    return shipModes;
  }

  public Distribution getTerminators() {
    return terminators;
  }

  public Distribution getVerbPhrase() {
    return verbPhrase;
  }

  public Distribution getVerbs() {
    return verbs;
  }

  private static Distribution getDistribution(
      Map<String, Distribution> distributions, String name) {
    Distribution distribution = distributions.get(name);
    return distribution;
  }
}
