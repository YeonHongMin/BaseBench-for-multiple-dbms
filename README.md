# BenchBase

BaseBench의 한국어 번역 버전입니다.

**버전: 0.1**

[![BenchBase (Java with Maven)](https://github.com/cmu-db/benchbase/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/cmu-db/benchbase/actions/workflows/maven.yml)

BenchBase(이전 [OLTPBench](https://github.com/oltpbenchmark/oltpbench/))는 JDBC를 통한 다중 DBMS SQL 벤치마킹 프레임워크입니다.

**목차**

- [빠른 시작](#빠른-시작)
- [설명](#설명)
- [사용 가이드](#사용-가이드)
- [기여하기](#기여하기)
- [알려진 문제](#알려진-문제)
- [크레딧](#크레딧)
- [이 저장소 인용하기](#이-저장소-인용하기)
- [변경 사항 (v0.1)](#변경-사항-v01)

---

## 빠른 시작

`postgres` 프로파일을 사용하여 BenchBase를 클론하고 빌드하려면:

```bash
git clone --depth 1 https://github.com/cmu-db/benchbase.git
cd benchbase
./mvnw clean package -P postgres
```

이렇게 하면 `target` 폴더에 아티팩트가 생성되며, 압축을 풀 수 있습니다:

```bash
cd target
tar xvzf benchbase-postgres.tgz
cd benchbase-postgres
```

이 폴더 내에서 BenchBase를 실행할 수 있습니다. 예를 들어 `tpcc` 벤치마크를 실행하려면:

```bash
java -jar benchbase.jar -b tpcc -c config/postgres/sample_tpcc_config.xml --create=true --load=true --execute=true
```

전체 옵션 목록을 표시하려면:

```bash
java -jar benchbase.jar -h
```

---

## 설명

벤치마킹은 매우 유용하지만 끝없이 고통스럽습니다. 이 벤치마크 스위트는 여러 박사/박사후/교수들이 모여 각자의 워크로드/프레임워크/경험/노력을 결합한 결과입니다. 우리는 이것이 다른 사람들의 시간을 절약하고, 오픈소스 방식으로 성장할 수 있는 확장 가능한 플랫폼을 제공할 수 있기를 희망합니다.

BenchBase는 멀티스레드 로드 생성기입니다. 이 프레임워크는 JDBC를 지원하는 모든 관계형 데이터베이스에 대해 가변 속도, 가변 혼합 로드를 생성할 수 있도록 설계되었습니다. 또한 프레임워크는 데이터 수집 기능을 제공합니다(예: 트랜잭션 유형별 지연 시간 및 처리량 로그).

BenchBase 프레임워크에는 다음과 같은 벤치마크가 포함되어 있습니다:

* [AuctionMark](https://github.com/cmu-db/benchbase/wiki/AuctionMark)
* [CH-benCHmark](https://github.com/cmu-db/benchbase/wiki/CH-benCHmark)
* [Epinions.com](https://github.com/cmu-db/benchbase/wiki/epinions)
* hyadapt -- 설정 파일 대기 중
* [NoOp](https://github.com/cmu-db/benchbase/wiki/NoOp)
* [OT-Metrics](https://github.com/cmu-db/benchbase/wiki/OT-Metrics)
* [Resource Stresser](https://github.com/cmu-db/benchbase/wiki/Resource-Stresser)
* [SEATS](https://github.com/cmu-db/benchbase/wiki/Seats)
* [SIBench](https://github.com/cmu-db/benchbase/wiki/SIBench)
* [SmallBank](https://github.com/cmu-db/benchbase/wiki/SmallBank)
* [TATP](https://github.com/cmu-db/benchbase/wiki/TATP)
* [TPC-C](https://github.com/cmu-db/benchbase/wiki/TPC-C)
* [TPC-H](https://github.com/cmu-db/benchbase/wiki/TPC-H)
* TPC-DS -- 설정 파일 대기 중
* [Twitter](https://github.com/cmu-db/benchbase/wiki/Twitter)
* [Voter](https://github.com/cmu-db/benchbase/wiki/Voter)
* [Wikipedia](https://github.com/cmu-db/benchbase/wiki/Wikipedia)
* [YCSB](https://github.com/cmu-db/benchbase/wiki/YCSB)

이 프레임워크는 쉬운 확장을 허용하도록 설계되었습니다. 우리는 기여자가 새로운 벤치마크를 포함하는 데 사용할 수 있는 스텁 코드를 제공하며, 모든 시스템 기능(로깅, 제어된 속도, 제어된 혼합 등)을 활용할 수 있습니다.

---

## 사용 가이드

### 시스템 요구사항

- **Java**: JDK 19 이상
- **Maven**: 3.8 이상 (Maven Wrapper 포함)

### 지원 DBMS

| DBMS | 프로파일명 | JDBC 드라이버 | 비고 |
|------|----------|--------------|------|
| PostgreSQL | `postgres` | postgresql-42.7.2 | |
| MySQL | `mysql` | mysql-connector-j-8.4.0 | |
| MariaDB | `mariadb` | mariadb-java-client | |
| Oracle | `oracle` | ojdbc11-23.2.0.0 | v0.1에서 DDL 수정 |
| SQL Server | `sqlserver` | mssql-jdbc-12.8.1 | |
| DB2 | `db2` | db2jcc4 | |
| Tibero | `tibero` | tibero7-jdbc | v0.1에서 추가 |
| SQLite | `sqlite` | sqlite-jdbc | |
| CockroachDB | `cockroachdb` | postgresql | |
| Phoenix | `phoenix` | phoenix-client | |
| Spanner | `spanner` | google-cloud-spanner-jdbc | |

### 빌드 방법
프로파일 이름(`-P`)으로 지정된 데이터베이스에 대한 배포판을 빌드하려면 다음 명령을 실행하세요.

```bash
./mvnw clean package -P <profile name>
```

다음 파일들이 `./target` 폴더에 배치됩니다:

* `benchbase-<profile name>.tgz`
* `benchbase-<profile name>.zip`

### 실행 방법
배포판을 빌드하고 압축을 풀면 다른 실행 가능한 jar 파일처럼 `benchbase`를 실행할 수 있습니다. 다음 예제는 확장된 `.zip` 또는 `.tgz` 배포판의 루트에서 실행하는 것을 가정합니다. 배포 구조 외부에서 `benchbase`를 실행하려고 하면 `java.lang.NoClassDefFoundError`를 포함한 다양한 오류가 발생할 수 있습니다.

도움말 내용을 표시하려면:
```bash
java -jar benchbase.jar -h
```

`tpcc` 벤치마크를 실행하려면:
```bash
java -jar benchbase.jar -b tpcc -c config/postgres/sample_tpcc_config.xml --create=true --load=true --execute=true
```

여러 스키마를 생성하고 로드해야 하는 `chbenchmark`와 같은 복합 벤치마크의 경우 쉼표로 구분된 목록을 제공할 수 있습니다:
```bash
java -jar benchbase.jar -b tpcc,chbenchmark -c config/postgres/sample_chbenchmark_config.xml --create=true --load=true --execute=true
```

다음 옵션이 제공됩니다:

```text
usage: benchbase
 -b,--bench <arg>               [required] Benchmark class. Currently
                                supported: [tpcc, tpch, tatp, wikipedia,
                                resourcestresser, twitter, epinions, ycsb,
                                seats, auctionmark, chbenchmark, voter,
                                sibench, noop, smallbank, hyadapt,
                                otmetrics, templated]
 -c,--config <arg>              [required] Workload configuration file
    --clear <arg>               Clear all records in the database for this
                                benchmark
    --create <arg>              Initialize the database for this benchmark
 -d,--directory <arg>           Base directory for the result files,
                                default is current directory
    --dialects-export <arg>     Export benchmark SQL to a dialects file
    --execute <arg>             Execute the benchmark workload
 -h,--help                      Print this help
 -im,--interval-monitor <arg>   Throughput Monitoring Interval in
                                milliseconds
 -jh,--json-histograms <arg>    Export histograms to JSON file
    --load <arg>                Load data using the benchmark's data
                                loader
 -s,--sample <arg>              Sampling window
```

### Maven으로 실행하는 방법

benchbase를 실행하기 전에 먼저 빌드, 패키징 및 압축 해제하는 대신, Maven을 사용하여 소스 코드에 대해 벤치마크를 직접 실행할 수 있습니다. 프로젝트를 클론한 후 Maven `exec:java` 목표를 사용하여 루트 프로젝트 디렉토리에서 모든 벤치마크를 실행할 수 있습니다. 예를 들어, 다음 명령은 `postgres`에 대해 `tpcc` 벤치마크를 실행합니다:

```
mvn clean compile exec:java -P postgres -Dexec.args="-b tpcc -c config/postgres/sample_tpcc_config.xml --create=true --load=true --execute=true"
```

이는 위의 단계와 동일하지만 먼저 패키징한 다음 배포판을 압축 해제할 필요가 없습니다.

### 로깅 활성화 방법

로깅을 활성화하려면(예: PostgreSQL JDBC 드라이버의 경우), 시작할 때 다음 JVM 속성을 추가하세요:

```
-Djava.util.logging.config.file=src/main/resources/logging.properties
```

로깅 수준을 수정하려면 [`logging.properties`](src/main/resources/logging.properties) 및/또는 [`log4j.properties`](src/main/resources/log4j.properties)를 업데이트할 수 있습니다.

### 릴리스 방법

```
./mvnw -B release:prepare
./mvnw -B release:perform
```

### Docker 사용 방법

- 소스에서 빌드하는 데 도움이 되는 개발 이미지를 빌드하거나 가져오기:

  ```sh
  ./docker/benchbase/build-dev-image.sh
  ./docker/benchbase/run-dev-image.sh
  ```

  또는

  ```sh
  docker run -it --rm --pull \
    -v /path/to/benchbase-source:/benchbase \
    -v $HOME/.m2:/home/containeruser/.m2 \
    benchbase.azure.cr.io/benchbase-dev
  ```

- 전체 이미지 빌드:

  ```sh
  # 모든 프로파일이 포함된 이미지 빌드
  ./docker/benchbase/build-full-image.sh

  # 또는 일부만 빌드하려는 경우
  BENCHBASE_PROFILES='postgres mysql' ./docker/benchbase/build-full-image.sh
  ```

- 주어진 프로파일에 대해 이미지 실행:

  ```sh
  BENCHBASE_PROFILE='postgres' ./docker/benchbase/run-full-image.sh --help # 또는 이전과 같이 다른 benchbase 인수
  ```

  또는

  ```sh
  docker run -it --rm --env BENCHBASE_PROFILE='postgres' \
    -v results:/benchbase/results benchbase.azurecr.io/benchbase --help # 또는 이전과 같이 다른 benchbase 인수
  ```

> 자세한 내용은 [docker/benchbase/README.md](./docker/benchbase/)를 참조하세요.

[Github Codespaces](https://github.com/features/codespaces) 및 [VSCode devcontainer](https://code.visualstudio.com/docs/remote/containers) 지원도 사용할 수 있습니다.

### 새 데이터베이스 지원 추가 방법

예제는 기존 MySQL 및 PostgreSQL 코드를 참조하세요.

---

## 기여하기

모든 기여를 환영합니다! [pull request](https://github.com/cmu-db/benchbase/pulls)를 열어주세요. 일반적인 기여에는 다음이 포함될 수 있습니다:

- 새 DBMS 지원 추가.
- 기존 벤치마크에 대한 더 많은 테스트 추가.
- 버그나 알려진 문제 수정.

추가 참고사항은 [CONTRIBUTING.md](./CONTRIBUTING.md)를 참조하세요.

## 알려진 문제

모든 문제에 대해 [GitHub의 이슈 트래커](https://github.com/cmu-db/benchbase/issues)를 사용하세요.

## 크레딧

BenchBase는 원본 OLTPBench의 공식 현대화 버전입니다.

원본 OLTPBench 코드는 원본 논문 [OLTP-Bench: An Extensible Testbed for Benchmarking Relational Databases](http://www.vldb.org/pvldb/vol7/p277-difallah.pdf)의 저자인 D. E. Difallah, A. Pavlo, C. Curino, P. Cudré-Mauroux가 대부분 작성했습니다. VLDB 2014. 아래 인용 가이드를 참조하세요.

현대화의 상당 부분은 [Tim Veil @ Cockroach Labs](https://github.com/timveil-cockroach)가 기여했으며, 다음을 포함하되 이에 국한되지 않습니다:

* Java ~~17~~ ~~21~~ 19 이상으로 빌드되었으며 이를 위해 설계되었습니다.
* Ant에서 Maven으로 마이그레이션.
  * Maven 구조에 맞게 프로젝트 재구성.
  * 정적 `lib` 디렉토리 및 종속성 제거.
  * 필수 종속성 업데이트 및 사용되지 않거나 원하지 않는 종속성 제거.
  * 모든 비 `.java` 파일을 표준 Maven `resources` 디렉토리로 이동.
  * [Maven Wrapper](https://maven.apache.org/wrapper)와 함께 제공.
* 패키징 및 버전 관리 개선.
    * Calendar Versioning(https://calver.org/)으로 이동.
    * 프로젝트가 이제 실행 가능한 `.jar`가 포함된 `.tgz` 또는 `.zip`으로 배포됩니다.
    * 모든 코드가 디렉토리 대신 `.jar` 내부에서 `resources`를 읽도록 업데이트되었습니다.
* Log4J에 대한 직접 종속성에서 SLF4J로 이동.
* 명확성과 일관성을 위해 많은 파일 재구성 및 이름 변경.
* "정적 분석"을 기반으로 한 수많은 수정 적용.
    * JDK 마이그레이션(박싱, 언박싱 등).
    * 모든 `java.lang.AutoCloseable` 인스턴스에 대해 `try-with-resources` 구현.
    * 적절한 로깅을 위해 `printStackTrace()` 또는 `System.out.println` 호출 제거.
* 코드 재포맷 및 import 정리.
* 모든 `assert` 호출 제거.
* 다양한 형태의 데드 코드 및 오래된 구성 제거.
* `Loader` 작업 중 `commit()` 호출 제거.
* `Connection` 객체의 `Worker` 및 `Loader` 사용 리팩토링 및 트랜잭션 처리 정리.
* Maven 종속성을 최신 상태로 유지하기 위해 [Dependabot](https://dependabot.com/) 도입.
* 대부분을 제거하여 출력 플래그 단순화, 일반적으로 보고 기능을 기본적으로 활성화된 상태로 유지.
* 구성된 벤치마크 데이터베이스에서 직접 채울 수 있는 대체 `Catalog` 제공. 이전 카탈로그는 `HSQLDB`를 통해 프록시되었습니다. 이는 카탈로그 지원이 불완전할 수 있는 DBMS에 대한 옵션으로 남아 있습니다.

## 이 저장소 인용하기

학술 논문에서 이 저장소를 사용하는 경우 다음을 인용하세요:

> D. E. Difallah, A. Pavlo, C. Curino, and P. Cudré-Mauroux, "OLTP-Bench: An Extensible Testbed for Benchmarking Relational Databases," PVLDB, vol. 7, iss. 4, pp. 277-288, 2013.

편의를 위해 BibTeX가 아래에 제공됩니다.

```bibtex
@article{DifallahPCC13,
  author = {Djellel Eddine Difallah and Andrew Pavlo and Carlo Curino and Philippe Cudr{\'e}-Mauroux},
  title = {OLTP-Bench: An Extensible Testbed for Benchmarking Relational Databases},
  journal = {PVLDB},
  volume = {7},
  number = {4},
  year = {2013},
  pages = {277--288},
  url = {http://www.vldb.org/pvldb/vol7/p277-difallah.pdf},
}
```

---

## 변경 사항 (v0.1)

### HikariCP 커넥션 풀 최적화

원본 BenchBase에 다음과 같은 HikariCP 커넥션 풀 최적화가 추가되었습니다.

#### 1. 동적 풀 크기 조정 (터미널 수 기반)

터미널 수를 기반으로 커넥션 풀 크기가 자동으로 계산됩니다.

- **최소 풀 크기**: `max(터미널 수 / 2, 5)`
- **최대 풀 크기**: `max(터미널 수 × 1.5, 터미널 수 + 10)`

예시: 터미널 20개 설정 시 → 풀 크기 10-30으로 자동 설정

#### 2. DBMS별 최적화 설정

각 데이터베이스 유형에 맞는 최적화 설정이 자동으로 적용됩니다.

| DBMS | 최적화 설정 |
|------|-----------|
| **MySQL/MariaDB** | `useServerPrepStmts`, `rewriteBatchedStatements`, `useLocalSessionState`, `cacheResultSetMetadata` 등 |
| **PostgreSQL** | `prepareThreshold`, `preparedStatementCacheQueries`, `preparedStatementCacheSizeMiB` |
| **Oracle** | `implicitStatementCacheSize`, `defaultRowPrefetch` |
| **SQL Server** | `sendStringParametersAsUnicode`, `selectMethod` |
| **DB2** | `fullyMaterializeLobData`, `progressiveStreaming` |
| **SQLite** | `journal_mode=WAL`, `synchronous=NORMAL` |
| **H2/HSQLDB** | `LOG=0`, `LOCK_MODE=0` |

#### 3. 커넥션 풀 설정 방법

XML 설정 파일에서 다음과 같이 커넥션 풀을 구성할 수 있습니다:

```xml
<connectionPool>
    <!-- 커넥션 풀 활성화 -->
    <enabled>true</enabled>

    <!-- 동적 크기 조정 (기본값: true) -->
    <!-- 명시적으로 minSize/maxSize를 지정하면 자동으로 false가 됩니다 -->
    <dynamicSizing>true</dynamicSizing>

    <!-- 명시적 풀 크기 설정 (선택사항) -->
    <!-- <minSize>10</minSize> -->
    <!-- <maxSize>50</maxSize> -->

    <!-- 타임아웃 설정 (밀리초) -->
    <connectionTimeout>30000</connectionTimeout>  <!-- 30초 -->
    <idleTimeout>600000</idleTimeout>             <!-- 10분 -->
    <maxLifetime>1800000</maxLifetime>            <!-- 30분 -->
</connectionPool>
```

#### 4. 수정된 파일

- `WorkloadConfiguration.java`: 동적 풀 크기 조정 필드 및 메서드 추가
- `ConnectionPoolManager.java`: DBMS별 최적화 설정 적용 메서드 추가
- `DBWorkload.java`: XML 설정 파싱 및 동적 풀 크기 계산 로직 추가

### Tibero 데이터베이스 지원 추가

Tibero DBMS에 대한 지원이 추가되었습니다. Tibero는 Oracle 호환 DBMS로, Oracle과 유사한 SQL 구문을 사용합니다.

#### Tibero 빌드 방법

```bash
# Tibero 프로필로 빌드
./mvnw clean package -P tibero

# 또는 환경 변수 설정 후 빌드
export BENCHBASE_PROFILE=tibero
./mvnw clean package
```

#### Tibero 설정 예시

```xml
<?xml version="1.0"?>
<parameters>
    <type>TIBERO</type>
    <driver>com.tmax.tibero.jdbc.TbDriver</driver>
    <url>jdbc:tibero:thin:@localhost:8629:tibero</url>
    <username>benchbase</username>
    <password>password</password>
    <isolation>TRANSACTION_READ_COMMITTED</isolation>

    <!-- 커넥션 풀 설정 -->
    <connectionPool>
        <enabled>true</enabled>
        <dynamicSizing>true</dynamicSizing>
    </connectionPool>

    <scalefactor>1</scalefactor>
    <terminals>4</terminals>
    <!-- ... -->
</parameters>
```

#### Tibero 관련 추가 파일

- `DatabaseType.java`: TIBERO 타입 추가
- `pom.xml`: Tibero JDBC 프로필 추가 (`lib/tibero7-jdbc.jar` 사용)
- `ddl-tibero.sql`: TPC-C용 Tibero DDL 스크립트
- `dialect-tibero.xml`: Tibero SQL 방언 설정
- `sample_tpcc_config.xml`: Tibero용 TPC-C 샘플 설정

### Oracle DDL 스크립트 개선

Oracle 데이터베이스에서 TPC-C 벤치마크 실행 시 발생하던 테이블 DROP 관련 오류가 수정되었습니다.

#### 문제점

기존 DDL 스크립트에서 `DROP TABLE ... CASCADE CONSTRAINTS` 구문 사용 시:
- 테이블이 존재하지 않으면 ORA-00942 에러 발생
- 테이블이 잠겨 있으면 ORA-00054 에러 발생
- 이로 인해 후속 CREATE TABLE 문이 실행되지 않는 문제 발생

#### 해결 방법

PL/SQL 예외 처리 블록을 사용하여 테이블이 없을 때 발생하는 ORA-00942 에러를 무시하도록 수정:

```sql
-- 기존 방식 (오류 발생 가능)
DROP TABLE ORDER_LINE CASCADE CONSTRAINTS;

-- 수정된 방식 (오류 무시)
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE ORDER_LINE CASCADE CONSTRAINTS PURGE';
  EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
```

#### 수정된 파일

- `src/main/resources/benchmarks/tpcc/ddl-oracle.sql`

### 빠른 실행 스크립트

각 DBMS별로 빠르게 벤치마크를 실행할 수 있는 스크립트가 제공됩니다.

#### 빌드 스크립트 (테이블 생성 및 데이터 로드)

| DBMS | 스크립트 |
|------|---------|
| PostgreSQL | `./start_build_pg.sh` |
| MySQL | `./start_build_mysql.sh` |
| Oracle | `./start_build_oracle.sh` |
| SQL Server | `./start_build_mssql.sh` |
| DB2 | `./start_build_db2.sh` |
| Tibero | `./start_build_tibero.sh` |

#### 실행 스크립트 (워크로드 실행)

| DBMS | 스크립트 |
|------|---------|
| PostgreSQL | `./start_exec_pg.sh` |
| MySQL | `./start_exec_mysql.sh` |
| Oracle | `./start_exec_oracle.sh` |
| SQL Server | `./start_exec_mssql.sh` |
| DB2 | `./start_exec_db2.sh` |
| Tibero | `./start_exec_tibero.sh` |

#### 테이블 삭제 스크립트

| DBMS | 스크립트 |
|------|---------|
| PostgreSQL | `./drop_build_pg.sh` |
| MySQL | `./drop_build_mysql.sh` |
| Oracle | `./drop_build_oracle.sh` |
| SQL Server | `./drop_build_mssql.sh` |
| DB2 | `./drop_build_db2.sh` |
| Tibero | `./drop_build_tibero.sh` |
