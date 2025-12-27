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

package com.oltpbenchmark.benchmarks.tpch;

public class TPCHConstants {

  public static final String TABLENAME_REGION = "region";
  public static final String TABLENAME_NATION = "nation";
  public static final String TABLENAME_SUPPLIER = "supplier";
  public static final String TABLENAME_CUSTOMER = "customer";
  public static final String TABLENAME_PART = "part";
  public static final String TABLENAME_ORDER = "orders";
  public static final String TABLENAME_PARTSUPP = "partsupp";
  public static final String TABLENAME_LINEITEM = "lineitem";

  // 4.2.2.13 컨테이너 음절 1
  public static final String[] CONTAINERS_S1 = {"SM", "LG", "MED", "JUMBO", "WRAP"};

  // 4.2.2.13 컨테이너 음절 2
  public static final String[] CONTAINERS_S2 = {
    "CASE", "BOX", "BAG", "JAR", "PKG", "PACK", "CAN", "DRUM"
  };

  // 4.2.2.13 모드
  public static final String[] MODES = {"REG AIR", "AIR", "RAIL", "SHIP", "TRUCK", "MAIL", "FOB"};

  // 4.2.2.13 세그먼트
  public static final String[] SEGMENTS = {
    "AUTOMOBILE", "BUILDING", "FURNITURE", "MACHINERY", "HOUSEHOLD"
  };

  // 4.2.2.13 타입 음절 1
  public static final String[] TYPE_S1 = {
    "STANDARD", "SMALL", "MEDIUM", "LARGE", "ECONOMY", "PROMO"
  };

  // 4.2.2.13 타입 음절 2
  public static final String[] TYPE_S2 = {"ANODIZED", "BURNISHED", "PLATED", "POLISHED", "BRUSHED"};

  // 4.2.2.13 타입 음절 3
  public static final String[] TYPE_S3 = {"TIN", "NICKEL", "BRASS", "STEEL", "COPPER"};

  // 4.2.3 N_NAME
  public static final String[] N_NAME = {
    "ALGERIA",
    "ARGENTINA",
    "BRAZIL",
    "CANADA",
    "EGYPT",
    "ETHIOPIA",
    "FRANCE",
    "GERMANY",
    "INDIA",
    "INDONESIA",
    "IRAN",
    "IRAQ",
    "JAPAN",
    "JORDAN",
    "KENYA",
    "MOROCCO",
    "MOZAMBIQUE",
    "PERU",
    "CHINA",
    "ROMANIA",
    "SAUDI ARABIA",
    "VIETNAM",
    "RUSSIA",
    "UNITED KINGDOM",
    "UNITED STATES"
  };

  // 4.2.3 이 중 다섯 개를 연결하여 생성된 P_NAME
  public static final String[] P_NAME_GENERATOR = {
    "almond",
    "antique",
    "aquamarine",
    "azure",
    "beige",
    "bisque",
    "black",
    "blanched",
    "blue",
    "blush",
    "brown",
    "burlywood",
    "burnished",
    "chartreuse",
    "chiffon",
    "chocolate",
    "coral",
    "cornflower",
    "cornsilk",
    "cream",
    "cyan",
    "dark",
    "deep",
    "dim",
    "dodger",
    "drab",
    "firebrick",
    "floral",
    "forest",
    "frosted",
    "gainsboro",
    "ghost",
    "goldenrod",
    "green",
    "grey",
    "honeydew",
    "hot",
    "indian",
    "ivory",
    "khaki",
    "lace",
    "lavender",
    "lawn",
    "lemon",
    "light",
    "lime",
    "linen",
    "magenta",
    "maroon",
    "medium",
    "metallic",
    "midnight",
    "mint",
    "misty",
    "moccasin",
    "navajo",
    "navy",
    "olive",
    "orange",
    "orchid",
    "pale",
    "papaya",
    "peach",
    "peru",
    "pink",
    "plum",
    "powder",
    "puff",
    "purple",
    "red",
    "rose",
    "rosy",
    "royal",
    "saddle",
    "salmon",
    "sandy",
    "seashell",
    "sienna",
    "sky",
    "slate",
    "smoke",
    "snow",
    "spring",
    "steel",
    "tan",
    "thistle",
    "tomato",
    "turquoise",
    "violet",
    "wheat",
    "white",
    "yellow"
  };

  // 4.2.3 R_NAME
  public static final String[] R_NAME = {"AFRICA", "AMERICA", "ASIA", "EUROPE", "MIDDLE EAST"};
}
