/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
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
 *
 */

package com.oltpbenchmark.benchmarks.seats;

import java.util.regex.Pattern;

public abstract class SEATSConstants {

  // ----------------------------------------------------------------
<<<<<<< HEAD
  // STORED PROCEDURE EXECUTION FREQUENCIES (0% - 100%)
=======
  // 저장 프로시저 실행 빈도 (0% - 100%)
>>>>>>> master
  // ----------------------------------------------------------------

  public static final int FREQUENCY_DELETE_RESERVATION = 10;
  public static final int FREQUENCY_FIND_FLIGHTS = 10;
  public static final int FREQUENCY_FIND_OPEN_SEATS = 35;
  public static final int FREQUENCY_NEW_RESERVATION = 20;
  public static final int FREQUENCY_UPDATE_CUSTOMER = 10;
  public static final int FREQUENCY_UPDATE_RESERVATION = 15;

  // ----------------------------------------------------------------
<<<<<<< HEAD
  // FLIGHT CONSTANTS
  // ----------------------------------------------------------------

  /**
   * The different distances that we can look-up for nearby airports This is similar to the customer
   * selecting a dropdown when looking for flights
   */
  // public static final int DISTANCES[] = { 5 }; // , 10, 25, 50, 100 };

  // Zhenwu made the changes. The original code is above
  public static final int[] DISTANCES = {5, 10, 25, 50, 100};

  /** The number of days in the past and future that we will generate flight information for */
=======
  // 항공편 상수
  // ----------------------------------------------------------------

  /** 인근 공항을 조회할 수 있는 다양한 거리입니다. 이것은 고객이 항공편을 찾을 때 드롭다운을 선택하는 것과 유사합니다. */
  // public static final int DISTANCES[] = { 5 }; // , 10, 25, 50, 100 };

  // Zhenwu가 변경했습니다. 원본 코드는 위에 있습니다.
  public static final int[] DISTANCES = {5, 10, 25, 50, 100};

  /** 항공편 정보를 생성할 과거 및 미래 일수 */
>>>>>>> master
  public static final int FLIGHTS_DAYS_PAST = 1;

  public static final int FLIGHTS_DAYS_FUTURE = 50;

  /**
<<<<<<< HEAD
   * Average # of flights per day NUM_FLIGHTS_PER_DAY = 15000 Source:
=======
   * 일일 평균 항공편 수 NUM_FLIGHTS_PER_DAY = 15000 출처:
>>>>>>> master
   * http://www.transtats.bts.gov/DL_SelectFields.asp?Table_ID=236&DB_Short_Name=On-Time
   */
  public static final int FLIGHTS_PER_DAY_MIN = 1125;

  public static final int FLIGHTS_PER_DAY_MAX = 1875;

<<<<<<< HEAD
  /**
   * Number of seats available per flight If you change this then you must also change FindOpenSeats
   */
  public static final int FLIGHTS_NUM_SEATS = 150;

  /** How many First Class seats are on a given flight These reservations are more expensive */
  public static final int FLIGHTS_FIRST_CLASS_OFFSET = 10;

  /** The rate in which a flight can travel between two airports (miles per hour) */
  public static final double FLIGHT_TRAVEL_RATE = 570.0; // Boeing 747

  // ----------------------------------------------------------------
  // CUSTOMER CONSTANTS
  // ----------------------------------------------------------------

  /** Default number of customers in the database */
  public static final int CUSTOMERS_COUNT = 100000;

  /** Max Number of FREQUENT_FLYER records per CUSTOMER */
=======
  /** 항공편당 사용 가능한 좌석 수 이 값을 변경하면 FindOpenSeats도 변경해야 합니다. */
  public static final int FLIGHTS_NUM_SEATS = 150;

  /** 주어진 항공편의 퍼스트 클래스 좌석 수 이 예약은 더 비쌉니다. */
  public static final int FLIGHTS_FIRST_CLASS_OFFSET = 10;

  /** 두 공항 간 항공편이 이동할 수 있는 속도 (시간당 마일) */
  public static final double FLIGHT_TRAVEL_RATE = 570.0; // Boeing 747

  // ----------------------------------------------------------------
  // 고객 상수
  // ----------------------------------------------------------------

  /** 데이터베이스의 기본 고객 수 */
  public static final int CUSTOMERS_COUNT = 100000;

  /** CUSTOMER당 FREQUENT_FLYER 레코드의 최대 수 */
>>>>>>> master
  public static final int CUSTOMER_NUM_FREQUENTFLYERS_MIN = 0;

  public static final int CUSTOMER_NUM_FREQUENTFLYERS_MAX = 10;
  public static final double CUSTOMER_NUM_FREQUENTFLYERS_SIGMA = 2.0;

<<<<<<< HEAD
  /**
   * The maximum number of days that we allow a customer to wait before needing a reservation on a
   * return to their original departure airport
   */
=======
  /** 고객이 원래 출발 공항으로 돌아오기 위해 예약이 필요하기 전에 대기할 수 있는 최대 일수 */
>>>>>>> master
  public static final int CUSTOMER_RETURN_FLIGHT_DAYS_MIN = 1;

  public static final int CUSTOMER_RETURN_FLIGHT_DAYS_MAX = 14;

  // ----------------------------------------------------------------
<<<<<<< HEAD
  // RESERVATION CONSTANTS
=======
  // 예약 상수
>>>>>>> master
  // ----------------------------------------------------------------

  public static final int RESERVATION_PRICE_MIN = 100;
  public static final int RESERVATION_PRICE_MAX = 1000;

  public static final int MAX_OPEN_SEATS_PER_TXN = 100;

  // ----------------------------------------------------------------
<<<<<<< HEAD
  // PROBABILITIES
  // ----------------------------------------------------------------

  /** Probability that a customer books a non-roundtrip flight (0% - 100%) */
  public static final int PROB_SINGLE_FLIGHT_RESERVATION = 10;

  /**
   * Probability that a customer will invoke DeleteReservation using the string version of their
   * Customer Id (0% - 100%)
   */
  public static final int PROB_DELETE_WITH_CUSTOMER_ID_STR = 20;

  /**
   * Probability that a customer will invoke UpdateCustomer using the string version of their
   * Customer Id (0% - 100%)
   */
  public static final int PROB_UPDATE_WITH_CUSTOMER_ID_STR = 20;

  /**
   * Probability that a customer will invoke DeleteReservation using the string version of their
   * FrequentFlyer Id (0% - 100%)
   */
  public static final int PROB_DELETE_WITH_FREQUENTFLYER_ID_STR = 20;

  /** Probability that is a seat is initially occupied (0% - 100%) */
  public static final int PROB_SEAT_OCCUPIED = 1; // 25;

  /** Probability that UpdateCustomer should update FrequentFlyer records */
  public static final int PROB_UPDATE_FREQUENT_FLYER = 25;

  /** Probability that a new Reservation will be added to the DeleteReservation queue */
  public static final int PROB_DELETE_RESERVATION = 50;

  /** Probability that a new Reservation will be added to the UpdateReservation queue */
  public static final int PROB_UPDATE_RESERVATION = 50;

  /** Probability that a deleted Reservation will be requeued for another NewReservation call */
  public static final int PROB_REQUEUE_DELETED_RESERVATION = 90;

  /** Probability that FindFlights will use the distance search */
  public static final int PROB_FIND_FLIGHTS_NEARBY_AIRPORT = 25;

  /** Probability that FindFlights will use two random airports as its input */
  public static final int PROB_FIND_FLIGHTS_RANDOM_AIRPORTS = 10;

  // ----------------------------------------------------------------
  // TIME CONSTANTS
  // ----------------------------------------------------------------

  /** Number of microseconds in a day */
  public static final long MILLISECONDS_PER_MINUTE = 60000L; // 60sec * 1,000

  /** Number of microseconds in a day */
  public static final long MILLISECONDS_PER_DAY = 86400000L; // 60sec * 60min * 24hr * 1,000

  /** The format of the time codes used in HISTOGRAM_FLIGHTS_PER_DEPART_TIMES */
  public static final Pattern TIMECODE_PATTERN = Pattern.compile("([\\d]{2,2}):([\\d]{2,2})");

  // ----------------------------------------------------------------
  // CACHE SIZES
  // ----------------------------------------------------------------

  /** The number of FlightIds we want to keep cached locally at a client */
=======
  // 확률
  // ----------------------------------------------------------------

  /** 고객이 왕복 항공편이 아닌 항공편을 예약할 확률 (0% - 100%) */
  public static final int PROB_SINGLE_FLIGHT_RESERVATION = 10;

  /** 고객이 Customer Id의 문자열 버전을 사용하여 DeleteReservation을 호출할 확률 (0% - 100%) */
  public static final int PROB_DELETE_WITH_CUSTOMER_ID_STR = 20;

  /** 고객이 Customer Id의 문자열 버전을 사용하여 UpdateCustomer를 호출할 확률 (0% - 100%) */
  public static final int PROB_UPDATE_WITH_CUSTOMER_ID_STR = 20;

  /** 고객이 FrequentFlyer Id의 문자열 버전을 사용하여 DeleteReservation을 호출할 확률 (0% - 100%) */
  public static final int PROB_DELETE_WITH_FREQUENTFLYER_ID_STR = 20;

  /** 좌석이 처음에 점유될 확률 (0% - 100%) */
  public static final int PROB_SEAT_OCCUPIED = 1; // 25;

  /** UpdateCustomer가 FrequentFlyer 레코드를 업데이트해야 할 확률 */
  public static final int PROB_UPDATE_FREQUENT_FLYER = 25;

  /** 새 예약이 DeleteReservation 큐에 추가될 확률 */
  public static final int PROB_DELETE_RESERVATION = 50;

  /** 새 예약이 UpdateReservation 큐에 추가될 확률 */
  public static final int PROB_UPDATE_RESERVATION = 50;

  /** 삭제된 예약이 다른 NewReservation 호출을 위해 다시 큐에 추가될 확률 */
  public static final int PROB_REQUEUE_DELETED_RESERVATION = 90;

  /** FindFlights가 거리 검색을 사용할 확률 */
  public static final int PROB_FIND_FLIGHTS_NEARBY_AIRPORT = 25;

  /** FindFlights가 입력으로 두 개의 무작위 공항을 사용할 확률 */
  public static final int PROB_FIND_FLIGHTS_RANDOM_AIRPORTS = 10;

  // ----------------------------------------------------------------
  // 시간 상수
  // ----------------------------------------------------------------

  /** 분당 밀리초 수 */
  public static final long MILLISECONDS_PER_MINUTE = 60000L; // 60sec * 1,000

  /** 일당 밀리초 수 */
  public static final long MILLISECONDS_PER_DAY = 86400000L; // 60sec * 60min * 24hr * 1,000

  /** HISTOGRAM_FLIGHTS_PER_DEPART_TIMES에 사용되는 시간 코드 형식 */
  public static final Pattern TIMECODE_PATTERN = Pattern.compile("([\\d]{2,2}):([\\d]{2,2})");

  // ----------------------------------------------------------------
  // 캐시 크기
  // ----------------------------------------------------------------

  /** 클라이언트에서 로컬로 캐시하려는 FlightIds 수 */
>>>>>>> master
  public static final int CACHE_LIMIT_FLIGHT_IDS = 10000;

  public static final int CACHE_LIMIT_PENDING_INSERTS = 10000;
  public static final int CACHE_LIMIT_PENDING_UPDATES = 5000;
  public static final int CACHE_LIMIT_PENDING_DELETES = 5000;

  // ----------------------------------------------------------------
<<<<<<< HEAD
  // DATA SET INFORMATION
  // ----------------------------------------------------------------

  /** Table Names */
=======
  // 데이터 세트 정보
  // ----------------------------------------------------------------

  /** 테이블 이름 */
>>>>>>> master
  public static final String TABLENAME_COUNTRY = "country";

  public static final String TABLENAME_AIRLINE = "airline";
  public static final String TABLENAME_CUSTOMER = "customer";
  public static final String TABLENAME_FREQUENT_FLYER = "frequent_flyer";
  public static final String TABLENAME_AIRPORT = "airport";
  public static final String TABLENAME_AIRPORT_DISTANCE = "airport_distance";
  public static final String TABLENAME_FLIGHT = "flight";
  public static final String TABLENAME_RESERVATION = "reservation";

  public static final String TABLENAME_CONFIG_PROFILE = "config_profile";
  public static final String TABLENAME_CONFIG_HISTOGRAMS = "config_histograms";

<<<<<<< HEAD
  /** Histogram Data Set Names */
=======
  /** 히스토그램 데이터 세트 이름 */
>>>>>>> master
  public static final String HISTOGRAM_FLIGHTS_PER_AIRPORT = "flights_per_airport";

  public static final String HISTOGRAM_FLIGHTS_PER_DEPART_TIMES = "flights_per_time";

<<<<<<< HEAD
  /** Tables that are loaded from data files */
=======
  /** 데이터 파일에서 로드되는 테이블 */
>>>>>>> master
  public static final String[] TABLES_DATAFILES = {
    SEATSConstants.TABLENAME_COUNTRY,
    SEATSConstants.TABLENAME_AIRPORT,
    SEATSConstants.TABLENAME_AIRLINE,
  };

<<<<<<< HEAD
  /** Tables generated from random data IMPORTANT: FLIGHT must come before FREQUENT_FLYER */
=======
  /** 무작위 데이터에서 생성된 테이블 중요: FLIGHT는 FREQUENT_FLYER보다 먼저 와야 합니다. */
>>>>>>> master
  public static final String[] TABLES_SCALING = {
    SEATSConstants.TABLENAME_CUSTOMER,
    SEATSConstants.TABLENAME_AIRPORT_DISTANCE,
    SEATSConstants.TABLENAME_FLIGHT,
    SEATSConstants.TABLENAME_FREQUENT_FLYER,
    SEATSConstants.TABLENAME_RESERVATION,
  };

<<<<<<< HEAD
  /** Configuration Tables */
=======
  /** 구성 테이블 */
>>>>>>> master
  public static final String[] TABLES_CONFIG = {
    SEATSConstants.TABLENAME_CONFIG_PROFILE, SEATSConstants.TABLENAME_CONFIG_HISTOGRAMS,
  };

<<<<<<< HEAD
  /** Histograms generated from data files */
=======
  /** 데이터 파일에서 생성된 히스토그램 */
>>>>>>> master
  public static final String[] HISTOGRAM_DATA_FILES = {
    SEATSConstants.HISTOGRAM_FLIGHTS_PER_AIRPORT, SEATSConstants.HISTOGRAM_FLIGHTS_PER_DEPART_TIMES,
  };

  /**
<<<<<<< HEAD
   * Tuple Code to Tuple Id Mapping For some tables, we want to store a unique code that can be used
   * to map to the id of a tuple. Any table that has a foreign key reference to this table will use
   * the unique code in the input data tables instead of the id. Thus, we need to keep a table of
   * how to map these codes to the ids when loading.
=======
   * 튜플 코드를 튜플 ID로 매핑 일부 테이블의 경우 튜플의 ID에 매핑하는 데 사용할 수 있는 고유 코드를 저장하려고 합니다. 이 테이블에 대한 외래 키 참조가 있는 모든
   * 테이블은 ID 대신 입력 데이터 테이블의 고유 코드를 사용합니다. 따라서 로드할 때 이러한 코드를 ID에 매핑하는 방법을 저장하는 테이블이 필요합니다.
>>>>>>> master
   */
  public static final String AIRPORT_ID = "ap_id";

  public static final String AIRLINE_ID = "al_id";
  public static final String COUNTRY_ID = "co_id";
  public static final String AIRLINE_IATA_CODE = "al_iata_code";
  public static final String AIRPORT_CODE = "ap_code";
  public static final String COUNTRY_CODE = "co_code_3";

  public static final String[][] CODE_TO_ID_COLUMNS = {
    {TABLENAME_COUNTRY, COUNTRY_CODE, COUNTRY_ID},
    {TABLENAME_AIRPORT, AIRPORT_CODE, AIRPORT_ID},
    {TABLENAME_AIRLINE, AIRLINE_IATA_CODE, AIRLINE_ID},
  };
}
