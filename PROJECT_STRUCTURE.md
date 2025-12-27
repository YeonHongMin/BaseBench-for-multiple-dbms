# BenchBase 프로젝트 구조

## 개요

BenchBase는 JDBC를 통한 다중 DBMS SQL 벤치마킹 프레임워크입니다. 원래 OLTPBenchmark의 현대화 버전으로, Java 19+와 Maven을 기반으로 구축되었습니다.

## 프로젝트 루트 구조

```
BaseBench-for-multiple-dbms/
├── benchbase.jar                    # 빌드된 실행 가능한 JAR 파일
├── benchbase-tibero/               # Tibero DBMS 전용 빌드
│   ├── benchbase.jar
│   ├── config/
│   ├── lib/                        # Tibero JDBC 드라이버 등
│   ├── scripts/
│   └── README.md
├── config/                         # 설정 파일들
├── data/                           # 데이터 저장 디렉터리
├── docker/                         # Docker 지원 파일들
├── lib/                            # 공통 라이브러리들
├── results/                        # 벤치마크 결과 저장 디렉터리
├── scripts/                        # 유틸리티 스크립트들
├── src/                            # 소스 코드
├── target/                         # Maven 빌드 결과물
├── pom.xml                         # Maven 프로젝트 설정
├── mvnw / mvnw.cmd                 # Maven Wrapper
├── README.md                       # 프로젝트 설명
├── CONTRIBUTORS.md                 # 기여자 목록
├── CONTRIBUTING.md                 # 기여 가이드
├── LICENSE                         # 라이선스 파일
├── TASKS.md                        # 작업 목록
├── TRD.md                          # 기술 요구 사항 문서
└── *.sh                            # DBMS별 빠른 실행 스크립트들
```

## 소스 코드 구조 (src/)

### 메인 소스 (src/main/java/com/oltpbenchmark/)

```
src/main/java/com/oltpbenchmark/
├── api/                            # 핵심 API 인터페이스 및 클래스들
│   ├── BenchmarkModule.java       # 벤치마크 모듈 인터페이스
│   ├── Loader.java                # 데이터 로더
│   ├── Worker.java                # 워커 스레드
│   ├── Procedure.java             # 저장 프로시저 인터페이스
│   ├── SQLStmt.java               # SQL 문장 클래스
│   ├── collectors/                # 성능 수집기들
│   ├── dialects/                  # SQL 방언 지원
│   └── templates/                 # 템플릿 시스템
├── benchmarks/                    # 벤치마크 구현체들
│   ├── auctionmark/               # AuctionMark 벤치마크
│   ├── chbenchmark/               # CH-benCHmark (TPC-H + TPC-C)
│   ├── epinions/                  # Epinions.com 벤치마크
│   ├── hyadapt/                   # HYADAPT 벤치마크
│   ├── noop/                      # NoOp (부하 테스트용)
│   ├── otmetrics/                 # OT-Metrics 벤치마크
│   ├── resourcestresser/          # 리소스 스트레서
│   ├── seats/                     # SEATS 항공 예약 시스템
│   ├── sibench/                   # SIBench 벤치마크
│   ├── smallbank/                 # SmallBank 벤치마크
│   ├── tatp/                      # TATP (Telecom Application)
│   ├── templated/                 # 템플릿 기반 커스텀 벤치마크
│   ├── tpcc/                      # TPC-C (OLTP 표준)
│   ├── tpcds/                     # TPC-DS (의사결정 지원)
│   ├── tpch/                      # TPC-H (데이터 웨어하우스)
│   ├── twitter/                   # Twitter 벤치마크
│   ├── voter/                     # Voter 투표 시스템
│   ├── wikipedia/                 # Wikipedia 벤치마크
│   ├── ycsb/                      # Yahoo! Cloud Serving Benchmark
│   └── README.md                  # 벤치마크 설명
├── catalog/                       # 데이터베이스 카탈로그 시스템
│   ├── AbstractCatalog.java       # 추상 카탈로그
│   ├── Catalog.java               # 메인 카탈로그 인터페이스
│   ├── HSQLDBCatalog.java         # HSQLDB 기반 카탈로그
│   ├── Table.java                 # 테이블 메타데이터
│   ├── Column.java                # 컬럼 메타데이터
│   └── Index.java                 # 인덱스 메타데이터
├── distributions/                 # 난수 분포 생성기들
│   ├── CounterGenerator.java      # 카운터 생성기
│   ├── CyclicCounterGenerator.java # 순환 카운터
│   ├── Generator.java             # 기본 생성기 인터페이스
│   ├── IntegerGenerator.java      # 정수 생성기
│   ├── ScrambledZipfianGenerator.java # 스크램블드 집피안
│   ├── UniformGenerator.java      # 균등 분포 생성기
│   ├── Utils.java                 # 유틸리티 함수들
│   └── ZipfianGenerator.java      # 집피안 분포 생성기
├── jdbc/                          # JDBC 관련 유틸리티
│   └── AutoIncrementPreparedStatement.java
├── types/                         # 열거형 및 타입 정의들
│   ├── DatabaseType.java          # 지원 DBMS 타입들
│   ├── SortDirectionType.java     # 정렬 방향
│   ├── State.java                 # 워크로드 상태
│   └── TransactionStatus.java     # 트랜잭션 상태
├── util/                          # 유틸리티 클래스들
│   ├── ClassUtil.java             # 클래스 리플렉션 유틸리티
│   ├── CollectionUtil.java        # 컬렉션 조작 유틸리티
│   ├── ConnectionPoolManager.java # 커넥션 풀 관리자
│   ├── FileUtil.java              # 파일 조작 유틸리티
│   ├── Histogram.java             # 히스토그램 클래스
│   ├── JSONUtil.java              # JSON 처리 유틸리티
│   ├── RandomDistribution.java    # 랜덤 분포 클래스
│   ├── RandomGenerator.java       # 랜덤 생성기
│   ├── SQLUtil.java               # SQL 유틸리티
│   ├── StringUtil.java            # 문자열 처리 유틸리티
│   ├── TextGenerator.java         # 텍스트 생성기
│   ├── TimeUtil.java              # 시간 처리 유틸리티
│   └── ThreadUtil.java            # 스레드 유틸리티
├── BenchmarkState.java            # 벤치마크 상태 관리
├── DBWorkload.java                # 메인 워크로드 실행 클래스
├── DistributionStatistics.java    # 분포 통계
├── LatencyRecord.java             # 지연 시간 기록
├── Phase.java                     # 실행 단계 관리
├── Results.java                   # 결과 저장 및 보고
├── SubmittedProcedure.java        # 제출된 프로시저
├── ThreadBench.java               # 스레드 벤치마크
├── WorkloadConfiguration.java     # 워크로드 설정
└── WorkloadState.java             # 워크로드 상태
```

### 테스트 소스 (src/test/java/com/oltpbenchmark/)

```
src/test/java/com/oltpbenchmark/
├── catalog/                       # 카탈로그 테스트
├── util/                          # 유틸리티 테스트
└── benchmarks/                    # 각 벤치마크별 테스트
    ├── twitter/
    ├── wikipedia/
    └── ycsb/
```

### 리소스 파일 (src/main/resources/)

```
src/main/resources/
├── benchmarks/                    # 각 벤치마크의 SQL 스크립트와 설정
│   ├── auctionmark/
│   ├── chbenchmark/
│   ├── epinions/
│   ├── seats/
│   ├── tatp/
│   ├── tpcc/
│   ├── tpch/
│   ├── twitter/
│   ├── voter/
│   ├── wikipedia/
│   ├── ycsb/
│   └── gather_schema_stats_oracle.sql
├── config/                        # 설정 파일들
│   └── tibero/
├── dialect.xsd                    # SQL 방언 스키마
├── templates.xsd                  # 템플릿 스키마
├── log4j.properties               # Log4j 설정
└── logging.properties             # Java 로깅 설정
```

## 빌드 및 배포 구조

### Maven 프로파일 지원 DBMS

BenchBase는 다양한 DBMS를 위한 Maven 프로파일을 지원합니다:

- **PostgreSQL**: `postgres` 프로파일
- **MySQL/MariaDB**: `mysql`/`mariadb` 프로파일
- **Oracle**: `oracle` 프로파일
- **SQL Server**: `sqlserver` 프로파일
- **DB2**: `db2` 프로파일
- **Tibero**: `tibero` 프로파일 (v0.1 추가)
- **SQLite**: `sqlite` 프로파일
- **CockroachDB**: `cockroachdb` 프로파일
- **Phoenix**: `phoenix` 프로파일
- **Spanner**: `spanner` 프로파일

### 빌드 결과물 (target/)

```
target/
├── benchbase-<profile>.tgz        # 압축된 배포판
├── benchbase-<profile>.zip        # ZIP 배포판
├── classes/                       # 컴파일된 클래스 파일들
├── generated-sources/             # 생성된 소스 파일들
├── generated-test-sources/        # 생성된 테스트 소스들
└── test-classes/                  # 컴파일된 테스트 클래스들
```

## 주요 아키텍처 컴포넌트

### 1. 벤치마크 모듈 시스템
- 각 벤치마크는 `BenchmarkModule` 인터페이스를 구현
- 데이터 로딩, 워크로드 실행, 결과 수집을 담당

### 2. 워커/로더 시스템
- `Worker`: 실제 트랜잭션 실행을 담당하는 스레드
- `Loader`: 초기 데이터 로딩을 담당
- 멀티스레드 아키텍처로 높은 처리량 지원

### 3. 커넥션 풀 관리
- HikariCP 기반의 커넥션 풀링
- DBMS별 최적화 설정 자동 적용
- 동적 풀 크기 조정 (터미널 수 기반)

### 4. 분포 생성기 시스템
- 다양한 난수 분포 지원 (균등, 집피안, 스크램블드 집피안 등)
- 벤치마크 워크로드의 현실성 향상

### 5. 카탈로그 시스템
- 데이터베이스 스키마 메타데이터 관리
- HSQLDB 기반의 인메모리 카탈로그
- 크로스-DBMS 호환성 지원

### 6. 결과 수집 및 모니터링
- 지연 시간, 처리량 측정
- JSON/히스토그램 형식 결과 출력
- 실시간 모니터링 지원

## 실행 스크립트들

프로젝트 루트에는 각 DBMS별로 빠른 실행을 위한 스크립트들이 제공됩니다:

### 빌드 스크립트 (테이블 생성 + 데이터 로드)
- `start_build_pg.sh` - PostgreSQL
- `start_build_mysql.sh` - MySQL
- `start_build_oracle.sh` - Oracle
- `start_build_mssql.sh` - SQL Server
- `start_build_db2.sh` - DB2
- `start_build_tibero.sh` - Tibero

### 실행 스크립트 (워크로드 실행)
- `start_exec_*.sh` - 각 DBMS별 워크로드 실행

### 정리 스크립트 (테이블 삭제)
- `drop_build_*.sh` - 각 DBMS별 테이블 정리

## Docker 지원

```
docker/benchbase/
├── devcontainer/                  # 개발 환경
│   ├── build-in-container.sh
│   └── Dockerfile
└── fullimage/                     # 전체 이미지
    ├── build-full-image.sh
    ├── run-full-image.sh
    └── Dockerfile
```

## 익명화 스크립트

```
scripts/anonymization/
├── src/
│   ├── anonymizer.py             # 메인 익명화 스크립트
│   ├── configuration/            # 설정 관리
│   ├── modules/                  # 익명화 모듈들
│   └── test.py                   # 테스트 스크립트
```

## 주요 특징

1. **멀티 DBMS 지원**: 10개 이상의 DBMS 지원
2. **확장성**: 새로운 벤치마크와 DBMS 쉽게 추가 가능
3. **현실성**: 실제 워크로드를 모방하는 다양한 분포 지원
4. **성능 모니터링**: 상세한 성능 메트릭 수집
5. **컨테이너화**: Docker를 통한 쉬운 배포
6. **모던화**: Java 19+, Maven, HikariCP 등 최신 기술 사용

이 구조는 BenchBase가 다양한 데이터베이스 시스템에 대한 포괄적인 벤치마킹을 수행할 수 있도록 설계되었습니다.
