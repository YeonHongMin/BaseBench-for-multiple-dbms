# 기술 요구사항 문서 (TRD)
## BenchBase - 다중 DBMS SQL 벤치마킹 프레임워크

**문서 버전:** 1.0  
**작성일:** 2024  
**프로젝트:** BenchBase (이전 OLTPBench)

---

## 목차

1. [개요](#1-개요)
2. [시스템 아키텍처](#2-시스템-아키텍처)
3. [기술 스택](#3-기술-스택)
4. [주요 컴포넌트](#4-주요-컴포넌트)
5. [데이터베이스 지원](#5-데이터베이스-지원)
6. [벤치마크 목록](#6-벤치마크-목록)
7. [기능 요구사항](#7-기능-요구사항)
8. [비기능 요구사항](#8-비기능-요구사항)
9. [설정 및 구성](#9-설정-및-구성)
10. [API 및 인터페이스](#10-api-및-인터페이스)
11. [데이터 수집 및 모니터링](#11-데이터-수집-및-모니터링)
12. [확장성 및 확장](#12-확장성-및-확장)
13. [배포 및 빌드](#13-배포-및-빌드)
14. [의존성 관리](#14-의존성-관리)

---

## 1. 개요

### 1.1 프로젝트 소개

BenchBase는 JDBC를 통한 다중 DBMS SQL 벤치마킹 프레임워크입니다. 이전 OLTPBench의 공식 현대화 버전으로, 다양한 관계형 데이터베이스 관리 시스템(DBMS)에 대해 표준화된 벤치마크 테스트를 수행할 수 있는 확장 가능한 플랫폼을 제공합니다.

### 1.2 목적

- 다양한 DBMS에 대한 일관된 벤치마킹 환경 제공
- 멀티스레드 로드 생성 및 성능 측정
- 트랜잭션 유형별 지연 시간 및 처리량 분석
- 확장 가능한 벤치마크 프레임워크 제공

### 1.3 주요 특징

- **멀티스레드 로드 생성**: 가변 속도 및 가변 혼합 로드 생성
- **다중 DBMS 지원**: JDBC를 지원하는 모든 관계형 데이터베이스 지원
- **표준 벤치마크**: TPC-C, TPC-H 등 산업 표준 벤치마크 포함
- **데이터 수집**: 트랜잭션 유형별 지연 시간 및 처리량 로그
- **연결 풀링**: HikariCP를 통한 효율적인 데이터베이스 연결 관리
- **모니터링**: 고급 모니터링 및 처리량 모니터링 지원

---

## 2. 시스템 아키텍처

### 2.1 전체 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│                      DBWorkload (Main Entry)                │
│  - 명령줄 인자 파싱                                          │
│  - 설정 파일 로드                                            │
│  - 벤치마크 모듈 초기화                                      │
└──────────────────────┬──────────────────────────────────────┘
                       │
        ┌──────────────┴──────────────┐
        │                             │
┌───────▼────────┐          ┌────────▼────────┐
│ BenchmarkModule│          │ WorkloadConfig   │
│  - createDB()  │          │  - DB 설정       │
│  - loadDB()    │          │  - Phase 설정    │
│  - makeWorkers()│          │  - 트랜잭션 타입 │
└───────┬────────┘          └─────────────────┘
        │
        ├─────────────────┬──────────────────┐
        │                 │                  │
┌───────▼──────┐  ┌───────▼──────┐  ┌───────▼──────┐
│   Loader     │  │    Worker    │  │   Procedure   │
│  - 데이터 로드│  │  - 트랜잭션  │  │  - SQL 실행   │
└──────────────┘  │    실행      │  └──────────────┘
                  └───────┬──────┘
                          │
                  ┌───────▼──────┐
                  │  ThreadBench │
                  │  - 멀티스레드 │
                  │  - 성능 측정  │
                  └───────┬──────┘
                          │
                  ┌───────▼──────┐
                  │    Results   │
                  │  - 결과 수집  │
                  │  - 리포트 생성│
                  └──────────────┘
```

### 2.2 핵심 컴포넌트 흐름

1. **초기화 단계**
   - `DBWorkload.main()` 진입점
   - XML 설정 파일 파싱
   - 벤치마크 모듈 인스턴스 생성

2. **데이터베이스 생성 단계**
   - `BenchmarkModule.createDatabase()` 호출
   - DDL 스크립트 실행
   - 스키마 생성

3. **데이터 로드 단계**
   - `BenchmarkModule.loadDatabase()` 호출
   - `Loader` 인스턴스 생성
   - 멀티스레드 데이터 로드

4. **워크로드 실행 단계**
   - `Worker` 인스턴스 생성
   - `ThreadBench`를 통한 멀티스레드 실행
   - 트랜잭션 실행 및 성능 측정

5. **결과 수집 단계**
   - `Results` 객체에 성능 데이터 수집
   - CSV, JSON 형식으로 결과 출력

### 2.3 스레드 모델

- **Worker Threads**: 각 터미널(클라이언트)에 대해 하나의 Worker 스레드 생성
- **Loader Threads**: 데이터 로딩을 위한 병렬 스레드 (설정 가능)
- **Monitor Thread**: 처리량 및 성능 모니터링을 위한 별도 스레드

---

## 3. 기술 스택

### 3.1 프로그래밍 언어 및 플랫폼

- **Java**: 버전 19 이상
- **JDK**: Java 19+ (소스 및 타겟)
- **빌드 도구**: Maven 3.8 이상
- **패키징**: JAR, TGZ, ZIP

### 3.2 핵심 라이브러리

#### 데이터베이스 연결
- **HikariCP**: 5.1.0 - 연결 풀 관리
- **JDBC Drivers**: 프로파일별 데이터베이스 드라이버

#### 설정 관리
- **Apache Commons Configuration2**: 2.11.0 - XML 설정 파일 파싱
- **Commons CLI**: 1.9.0 - 명령줄 인자 처리

#### 로깅
- **SLF4J API**: 2.0.16 - 로깅 인터페이스
- **SLF4J Reload4j**: 2.0.16 - 로깅 구현체

#### 유틸리티
- **Apache Commons Lang3**: 3.17.0 - 문자열 및 객체 유틸리티
- **Apache Commons IO**: 2.18.0 - 파일 I/O
- **Apache Commons Text**: 1.13.0 - 텍스트 처리
- **Apache Commons Collections4**: 4.4 - 컬렉션 유틸리티
- **OpenCSV**: 5.9 - CSV 파일 처리
- **JSON**: 20240303 - JSON 처리

#### XML 처리
- **Jakarta XML Bind API**: 4.0.2
- **JAXB Runtime**: 4.0.5

#### 코드 생성
- **Janino**: 3.1.12 - 동적 코드 컴파일 (템플릿 벤치마크용)
- **Commons JXPath**: 1.3 - XPath 표현식 처리

#### 불변 객체
- **Immutables**: 2.10.1 - 불변 값 객체 생성

### 3.3 테스트 프레임워크

- **JUnit**: 4.13.2
- **HSQLDB**: 2.7.4 (테스트용 인메모리 데이터베이스)

---

## 4. 주요 컴포넌트

### 4.1 DBWorkload

**위치**: `com.oltpbenchmark.DBWorkload`

**역할**: 메인 진입점 및 오케스트레이터

**주요 기능**:
- 명령줄 인자 파싱 및 검증
- XML 설정 파일 로드 및 파싱
- 벤치마크 모듈 초기화 및 실행 순서 관리
- 데이터베이스 생성/클리어/로드/실행 단계 제어
- 결과 수집 및 출력

**주요 메서드**:
- `main(String[] args)`: 진입점
- `buildConfiguration(String filename)`: XML 설정 파일 로드
- `runCreator(BenchmarkModule bench)`: 데이터베이스 스키마 생성
- `runLoader(BenchmarkModule bench)`: 데이터 로드
- `runWorkload(List<BenchmarkModule> benchList, MonitorInfo monitorInfo)`: 워크로드 실행
- `writeOutputs(Results r, ...)`: 결과 파일 출력

### 4.2 BenchmarkModule

**위치**: `com.oltpbenchmark.api.BenchmarkModule`

**역할**: 모든 벤치마크 구현의 기본 추상 클래스

**주요 기능**:
- 데이터베이스 연결 관리 (직접 연결 또는 연결 풀)
- 데이터베이스 스키마 생성
- 데이터 로더 생성 및 실행
- Worker 인스턴스 생성
- 트랜잭션 타입 초기화
- SQL 방언(Dialect) 관리

**주요 메서드**:
- `makeConnection()`: 데이터베이스 연결 획득
- `createDatabase()`: 스키마 생성
- `loadDatabase()`: 데이터 로드
- `makeWorkers()`: Worker 인스턴스 생성
- `getProcedures()`: Procedure 인스턴스 맵 반환
- `initTransactionType(...)`: 트랜잭션 타입 초기화

**연결 풀 관리**:
- `initializeConnectionPool()`: 연결 풀 초기화
- `closeConnectionPool()`: 연결 풀 종료
- HikariCP를 통한 효율적인 연결 관리

### 4.3 Worker

**위치**: `com.oltpbenchmark.api.Worker`

**역할**: 트랜잭션 실행을 담당하는 워커 스레드

**주요 기능**:
- 벤치마크 상태 관리
- 트랜잭션 타입 선택 및 실행
- 재시도 로직 처리
- 연결 관리 (트랜잭션당 새 연결 또는 재사용)
- 지연 시간 기록

**주요 메서드**:
- `run()`: Worker 스레드 실행 루프
- `doWork(DatabaseType databaseType, TransactionType transactionType)`: 트랜잭션 실행
- `executeWork(...)`: 실제 Procedure 실행

### 4.4 Loader

**위치**: `com.oltpbenchmark.api.Loader`

**역할**: 벤치마크 데이터를 데이터베이스에 로드

**주요 기능**:
- 테이블별 데이터 생성 및 삽입
- 병렬 로딩 지원 (LoaderThread)
- 배치 삽입 최적화
- 진행 상황 추적

**주요 메서드**:
- `load()`: 데이터 로드 실행
- `createLoaderThreads()`: 로더 스레드 생성
- `unload()`: 데이터 삭제

### 4.5 Procedure

**위치**: `com.oltpbenchmark.api.Procedure`

**역할**: 개별 트랜잭션 타입의 SQL 실행 로직

**주요 기능**:
- 데이터베이스별 SQL 방언 로드
- PreparedStatement 실행
- 파라미터 바인딩
- 결과 처리

**주요 메서드**:
- `run(Connection conn, ...)`: 트랜잭션 실행
- `loadSQLDialect(StatementDialects dialects)`: SQL 방언 로드

### 4.6 ThreadBench

**위치**: `com.oltpbenchmark.ThreadBench`

**역할**: 멀티스레드 벤치마크 실행 및 성능 측정

**주요 기능**:
- Worker 스레드 생성 및 관리
- 속도 제한(Rate Limiting) 적용
- 워밍업 및 측정 단계 관리
- 성능 데이터 수집

**주요 메서드**:
- `runRateLimitedBenchmark(...)`: 속도 제한 벤치마크 실행
- `createWorkerThreads()`: Worker 스레드 생성
- `finalizeWorkers(...)`: Worker 스레드 종료 및 결과 수집

### 4.7 WorkloadConfiguration

**위치**: `com.oltpbenchmark.WorkloadConfiguration`

**역할**: 벤치마크 실행을 위한 모든 설정 정보 저장

**주요 설정 항목**:
- 데이터베이스 연결 정보 (URL, 사용자명, 비밀번호, 드라이버)
- 트랜잭션 타입 및 가중치
- Phase 설정 (시간, 워밍업, 속도 제한)
- 연결 풀 설정
- 격리 수준 (Isolation Level)
- 스케일 팩터

### 4.8 Results

**위치**: `com.oltpbenchmark.Results`

**역할**: 벤치마크 실행 결과 수집 및 저장

**주요 데이터**:
- 완료된 트랜잭션 수
- 중단된 트랜잭션 수
- 재시도된 트랜잭션 수
- 오류 발생 수
- 트랜잭션별 지연 시간 히스토그램
- 처리량 통계

### 4.9 ConnectionPoolManager

**위치**: `com.oltpbenchmark.util.ConnectionPoolManager`

**역할**: HikariCP를 사용한 연결 풀 관리

**주요 기능**:
- 연결 풀 생성 및 구성
- 연결 획득 및 반환
- 연결 풀 통계 수집
- 연결 풀 종료 및 리소스 정리

**설정 가능 항목**:
- 최소/최대 풀 크기
- 연결 타임아웃
- 유휴 타임아웃
- 최대 수명
- 트랜잭션 격리 수준

---

## 5. 데이터베이스 지원

### 5.1 지원 DBMS 목록

프로젝트는 Maven 프로파일을 통해 다양한 DBMS를 지원합니다:

1. **PostgreSQL** (`postgres`)
   - 드라이버: `org.postgresql:postgresql:42.7.4`

2. **MySQL** (`mysql`)
   - 드라이버: `mysql:mysql-connector-java:8.0.30`

3. **MariaDB** (`mariadb`)
   - 드라이버: `org.mariadb.jdbc:mariadb-java-client:3.5.1`

4. **Oracle** (`oracle`)
   - 드라이버: `com.oracle.database.jdbc:ojdbc11:23.7.0.25.01`

5. **SQL Server** (`sqlserver`)
   - 드라이버: `com.microsoft.sqlserver:mssql-jdbc:11.2.3.jre17`

6. **SQLite** (`sqlite`)
   - 드라이버: `org.xerial:sqlite-jdbc:3.47.1.0`

7. **CockroachDB** (`cockroachdb`)
   - 드라이버: PostgreSQL 드라이버 사용

8. **Google Spanner** (`spanner`)
   - 드라이버: `com.google.cloud:google-cloud-spanner-jdbc:2.24.1`

9. **Apache Phoenix** (`phoenix`)
   - 드라이버: `org.apache.phoenix:phoenix-client-hbase-2.4:5.1.3`

10. **Tibero** (`tibero`) - v0.1에서 추가
    - 드라이버: `lib/tibero7-jdbc.jar` (로컬 JAR)
    - Oracle 호환 DBMS

11. **DB2** (`db2`)
    - 드라이버: `lib/db2jcc4.jar` (로컬 JAR)

### 5.2 데이터베이스 타입 추상화

**위치**: `com.oltpbenchmark.types.DatabaseType`

**기능**:
- 데이터베이스별 특성 관리
- SQL 방언 처리
- 카탈로그 지원 여부 확인
- 모니터링 접두사 지원 여부 확인

### 5.3 SQL 방언(Dialect) 관리

**위치**: `com.oltpbenchmark.api.StatementDialects`

**기능**:
- 데이터베이스별 SQL 문 변형 관리
- XML 기반 방언 정의 파일 로드
- Procedure별 적절한 SQL 문 선택

**방언 파일 구조**:
- `dialect.xsd`: 스키마 정의
- `config/{dbms}/dialects.xml`: 데이터베이스별 방언 정의

---

## 6. 벤치마크 목록

### 6.1 표준 벤치마크

1. **TPC-C** (`tpcc`)
   - OLTP 벤치마크
   - 5가지 트랜잭션 타입
   - 웨어하우스 기반 스케일링

2. **TPC-H** (`tpch`)
   - 의사결정 지원 벤치마크
   - 22개의 분석 쿼리
   - 스케일 팩터 기반 데이터 크기

3. **TPC-DS** (`tpcds`)
   - 의사결정 지원 벤치마크 (설정 파일 대기 중)

### 6.2 애플리케이션별 벤치마크

4. **Wikipedia** (`wikipedia`)
   - 위키피디아 워크로드 시뮬레이션

5. **Twitter** (`twitter`)
   - 소셜 미디어 워크로드

6. **Epinions** (`epinions`)
   - 온라인 리뷰 시스템

7. **SEATS** (`seats`)
   - 항공 예약 시스템

8. **AuctionMark** (`auctionmark`)
   - 온라인 경매 시스템

9. **CH-benCHmark** (`chbenchmark`)
   - 복합 워크로드 (TPC-C + TPC-H)

### 6.3 특수 목적 벤치마크

10. **YCSB** (`ycsb`)
    - Yahoo! Cloud Serving Benchmark

11. **TATP** (`tatp`)
    - 통신 애플리케이션 트랜잭션 처리

12. **SmallBank** (`smallbank`)
    - 간단한 뱅킹 워크로드

13. **SIBench** (`sibench`)
    - 격리 수준 테스트

14. **Resource Stresser** (`resourcestresser`)
    - 시스템 리소스 스트레스 테스트

15. **Voter** (`voter`)
    - 투표 시스템 워크로드

16. **NoOp** (`noop`)
    - 최소 오버헤드 벤치마크

17. **OT-Metrics** (`otmetrics`)
    - 운영 메트릭 수집

18. **hyadapt** (`hyadapt`)
    - 하이브리드 워크로드 (설정 파일 대기 중)

19. **Templated** (`templated`)
    - 사용자 정의 SQL 템플릿 기반 벤치마크

### 6.4 벤치마크 구조

각 벤치마크는 다음 구조를 따릅니다:

```
benchmarks/{benchmark_name}/
├── {Prefix}Benchmark.java      # 벤치마크 모듈 구현
├── {Prefix}Loader.java          # 데이터 로더 구현
├── {Prefix}Worker.java          # 워커 구현
├── {Prefix}Constants.java       # 상수 정의 (선택)
└── procedures/                  # 트랜잭션 프로시저
    ├── Procedure1.java
    ├── Procedure2.java
    └── ...
```

---

## 7. 기능 요구사항

### 7.1 벤치마크 실행 기능

#### FR-1: 데이터베이스 스키마 생성
- **설명**: 벤치마크에 필요한 테이블, 인덱스 등 데이터베이스 객체 생성
- **입력**: DDL 스크립트 경로
- **출력**: 생성된 데이터베이스 스키마
- **우선순위**: 필수

#### FR-2: 데이터 로드
- **설명**: 벤치마크 실행을 위한 초기 데이터 로드
- **입력**: 스케일 팩터, 데이터 디렉토리
- **출력**: 로드된 데이터 레코드 수
- **우선순위**: 필수
- **성능 요구사항**: 병렬 로딩 지원

#### FR-3: 워크로드 실행
- **설명**: 벤치마크 트랜잭션 실행 및 성능 측정
- **입력**: Phase 설정, 트랜잭션 가중치, 속도 제한
- **출력**: 성능 결과 (처리량, 지연 시간)
- **우선순위**: 필수

#### FR-4: 데이터베이스 클리어
- **설명**: 벤치마크 데이터 삭제
- **입력**: 없음
- **출력**: 빈 데이터베이스
- **우선순위**: 선택

### 7.2 성능 측정 기능

#### FR-5: 트랜잭션 지연 시간 측정
- **설명**: 각 트랜잭션의 실행 시간 측정
- **측정 항목**: 
  - 최소/최대/평균 지연 시간
  - 백분위수 (50th, 95th, 99th)
  - 히스토그램
- **우선순위**: 필수

#### FR-6: 처리량 측정
- **설명**: 단위 시간당 트랜잭션 처리 수 측정
- **측정 항목**: TPS (Transactions Per Second)
- **우선순위**: 필수

#### FR-7: 트랜잭션 상태 추적
- **설명**: 트랜잭션 실행 결과 상태 분류
- **상태 유형**:
  - 완료 (Success)
  - 중단 (Abort)
  - 재시도 (Retry)
  - 오류 (Error)
  - 알 수 없음 (Unknown)
- **우선순위**: 필수

### 7.3 속도 제어 기능

#### FR-8: 속도 제한 (Rate Limiting)
- **설명**: 초당 트랜잭션 수 제한
- **옵션**:
  - 숫자: 초당 트랜잭션 수
  - "unlimited": 제한 없음
  - "disabled": 비활성화
- **우선순위**: 필수

#### FR-9: 도착 패턴 제어
- **설명**: 트랜잭션 도착 패턴 시뮬레이션
- **패턴**:
  - REGULAR: 정규 분포
  - POISSON: 포아송 분포
- **우선순위**: 선택

### 7.4 연결 관리 기능

#### FR-10: 연결 풀링
- **설명**: HikariCP를 통한 효율적인 연결 관리
- **설정 항목**:
  - 최소/최대 풀 크기
  - 연결 타임아웃
  - 유휴 타임아웃
  - 최대 수명
- **우선순위**: 선택 (기본값: 비활성화)

#### FR-11: 연결 재사용
- **설명**: 트랜잭션 간 연결 재사용 또는 새 연결 생성
- **옵션**:
  - 트랜잭션당 새 연결
  - 세션당 하나의 연결 재사용
- **우선순위**: 선택

#### FR-12: 연결 실패 복구
- **설명**: 연결 실패 시 자동 재연결
- **우선순위**: 선택

### 7.5 결과 출력 기능

#### FR-13: CSV 결과 출력
- **설명**: 성능 결과를 CSV 형식으로 출력
- **파일 유형**:
  - Raw 데이터
  - 샘플 데이터
  - 집계 결과
  - 트랜잭션 타입별 결과
- **우선순위**: 필수

#### FR-14: JSON 결과 출력
- **설명**: 성능 결과를 JSON 형식으로 출력
- **파일 유형**:
  - 요약 (Summary)
  - 파라미터 (Parameters)
  - 메트릭 (Metrics)
  - 히스토그램
- **우선순위**: 선택

#### FR-15: XML 설정 출력
- **설명**: 실행된 벤치마크 설정을 XML로 출력
- **우선순위**: 선택

### 7.6 모니터링 기능

#### FR-16: 처리량 모니터링
- **설명**: 실시간 처리량 모니터링
- **인터벌**: 설정 가능 (밀리초)
- **우선순위**: 선택

#### FR-17: 고급 모니터링
- **설명**: 데이터베이스별 고급 메트릭 수집
- **지원 DBMS**: PostgreSQL, MySQL, CockroachDB
- **우선순위**: 선택

---

## 8. 비기능 요구사항

### 8.1 성능 요구사항

#### NFR-1: 멀티스레드 성능
- **요구사항**: 동시에 수백 개의 Worker 스레드 실행 지원
- **측정 기준**: CPU 코어 수에 비례한 확장성

#### NFR-2: 메모리 효율성
- **요구사항**: 대규모 데이터셋 로딩 시 메모리 효율적 처리
- **측정 기준**: 배치 크기 조정을 통한 메모리 사용량 제어

#### NFR-3: 연결 풀 성능
- **요구사항**: 연결 풀링 시 오버헤드 최소화
- **측정 기준**: 연결 획득 시간 < 1ms

### 8.2 확장성 요구사항

#### NFR-4: 벤치마크 확장성
- **요구사항**: 새로운 벤치마크 추가 용이성
- **측정 기준**: 최소한의 코드 변경으로 새 벤치마크 추가 가능

#### NFR-5: DBMS 확장성
- **요구사항**: 새로운 DBMS 추가 용이성
- **측정 기준**: 프로파일 및 드라이버 추가만으로 새 DBMS 지원

### 8.3 안정성 요구사항

#### NFR-6: 오류 처리
- **요구사항**: 데이터베이스 오류 시 적절한 처리 및 복구
- **측정 기준**: 재시도 로직 및 오류 로깅

#### NFR-7: 트랜잭션 격리
- **요구사항**: 다양한 격리 수준 지원
- **지원 수준**: READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE

### 8.4 호환성 요구사항

#### NFR-8: JDBC 호환성
- **요구사항**: JDBC 4.0 이상 표준 준수
- **측정 기준**: JDBC 호환 데이터베이스와의 연동

#### NFR-9: 플랫폼 호환성
- **요구사항**: Windows, Linux, macOS 지원
- **측정 기준**: 크로스 플랫폼 빌드 및 실행

### 8.5 사용성 요구사항

#### NFR-10: 설정 파일 가독성
- **요구사항**: XML 설정 파일의 명확성 및 문서화
- **측정 기준**: 예제 설정 파일 제공

#### NFR-11: 로깅
- **요구사항**: 상세한 로깅 및 디버깅 정보 제공
- **측정 기준**: SLF4J를 통한 구조화된 로깅

---

## 9. 설정 및 구성

### 9.1 명령줄 옵션

```bash
java -jar benchbase.jar [OPTIONS]
```

**필수 옵션**:
- `-b, --bench <arg>`: 벤치마크 클래스 이름 (예: tpcc, tpch)
- `-c, --config <arg>`: 워크로드 설정 파일 경로

**선택 옵션**:
- `--create <true|false>`: 데이터베이스 스키마 생성
- `--clear <true|false>`: 데이터베이스 데이터 삭제
- `--load <true|false>`: 데이터 로드
- `--execute <true|false>`: 벤치마크 실행
- `-d, --directory <arg>`: 결과 파일 출력 디렉토리 (기본값: results)
- `-s, --sample <arg>`: 샘플링 윈도우 크기 (기본값: 5)
- `-im, --interval-monitor <arg>`: 모니터링 인터벌 (밀리초)
- `-mt, --monitor-type <arg>`: 모니터링 타입 (throughput/advanced)
- `-jh, --json-histograms <arg>`: JSON 히스토그램 출력 파일
- `--dialects-export <arg>`: SQL 방언 내보내기
- `-h, --help`: 도움말 출력

### 9.2 XML 설정 파일 구조

```xml
<?xml version="1.0"?>
<parameters>
    <!-- 데이터베이스 연결 설정 -->
    <type>postgres</type>
    <driver>org.postgresql.Driver</driver>
    <url>jdbc:postgresql://localhost:5432/benchbase</url>
    <username>postgres</username>
    <password>password</password>
    
    <!-- 연결 풀 설정 (선택) -->
    <connectionPool>
        <enabled>true</enabled>
        <minSize>5</minSize>
        <maxSize>20</maxSize>
        <connectionTimeout>30000</connectionTimeout>
        <idleTimeout>600000</idleTimeout>
        <maxLifetime>1800000</maxLifetime>
    </connectionPool>
    
    <!-- 벤치마크 설정 -->
    <scalefactor>1.0</scalefactor>
    <terminals>10</terminals>
    <loaderThreads>4</loaderThreads>
    <isolation>TRANSACTION_SERIALIZABLE</isolation>
    <randomSeed>12345</randomSeed>
    <batchsize>128</batchsize>
    
    <!-- 트랜잭션 타입 정의 -->
    <transactiontypes>
        <transactiontype>
            <name>NewOrder</name>
            <id>1</id>
        </transactiontype>
        <!-- ... -->
    </transactiontypes>
    
    <!-- 워크로드 Phase 정의 -->
    <works>
        <work>
            <time>60</time>
            <warmup>10</warmup>
            <rate>100</rate>
            <weights>45,43,4,4,4</weights>
            <active_terminals>10</active_terminals>
        </work>
        <!-- ... -->
    </works>
</parameters>
```

### 9.3 Phase 설정

**속성**:
- `time`: 실행 시간 (초)
- `warmup`: 워밍업 시간 (초)
- `rate`: 초당 트랜잭션 수 (또는 "unlimited", "disabled")
- `weights`: 트랜잭션 타입별 가중치 (쉼표로 구분)
- `active_terminals`: 활성 터미널 수
- `serial`: 순차 실행 모드 (true/false)
- `arrival`: 도착 패턴 ("regular" 또는 "poisson")

---

## 10. API 및 인터페이스

### 10.1 BenchmarkModule API

#### 추상 메서드 (구현 필수)

```java
protected abstract List<Worker<? extends BenchmarkModule>> makeWorkersImpl() throws IOException;
```
- Worker 인스턴스 리스트 생성

```java
protected abstract Loader<? extends BenchmarkModule> makeLoaderImpl();
```
- Loader 인스턴스 생성

```java
protected abstract Package getProcedurePackageImpl();
```
- Procedure 패키지 반환

#### 공개 메서드

```java
public final Connection makeConnection() throws SQLException;
```
- 데이터베이스 연결 획득

```java
public final void createDatabase() throws SQLException, IOException;
```
- 데이터베이스 스키마 생성

```java
public final Loader<? extends BenchmarkModule> loadDatabase() 
    throws IOException, SQLException, InterruptedException;
```
- 데이터 로드

```java
public final TransactionType initTransactionType(
    String procName, int id, long preExecutionWait, long postExecutionWait);
```
- 트랜잭션 타입 초기화

### 10.2 Worker API

#### 추상 메서드 (구현 필수)

```java
protected abstract void execute(Connection conn, TransactionType txnType) throws UserAbortException, SQLException;
```
- 트랜잭션 실행 로직

### 10.3 Loader API

#### 추상 메서드 (구현 필수)

```java
public abstract void load(Connection conn) throws SQLException;
```
- 데이터 로드 로직

```java
public abstract void unload(Connection conn, AbstractCatalog catalog) throws SQLException;
```
- 데이터 삭제 로직

### 10.4 Procedure API

#### 추상 메서드 (구현 필수)

```java
public abstract void run(Connection conn, Random gen, 
    int terminalWarehouseID, int numWarehouses, 
    int terminalDistrictID, Worker<? extends BenchmarkModule> w) throws SQLException;
```
- 트랜잭션 실행 로직

---

## 11. 데이터 수집 및 모니터링

### 11.1 성능 메트릭 수집

#### 지연 시간 메트릭
- **위치**: `com.oltpbenchmark.LatencyRecord`
- **수집 항목**:
  - 트랜잭션 시작/종료 시간
  - 실행 시간 (나노초 단위)
  - 트랜잭션 타입별 집계

#### 처리량 메트릭
- **위치**: `com.oltpbenchmark.ThreadBench`
- **수집 항목**:
  - 초당 트랜잭션 수 (TPS)
  - 시간대별 처리량 변화

### 11.2 히스토그램 생성

**위치**: `com.oltpbenchmark.util.Histogram`

**기능**:
- 지연 시간 분포 히스토그램
- 백분위수 계산 (50th, 95th, 99th)
- JSON 형식 내보내기

### 11.3 데이터베이스 메트릭 수집

**위치**: `com.oltpbenchmark.api.collectors`

**지원 DBMS**:
- PostgreSQL: `PostgresCollector`
- MySQL: `MySQLCollector`
- CockroachDB: `CockroachCollector`

**수집 항목**:
- 연결 수
- 쿼리 통계
- 캐시 히트율
- 락 통계

### 11.4 결과 파일 형식

#### CSV 파일
- **Raw 데이터**: `{benchmark}_{timestamp}.raw.csv`
- **샘플 데이터**: `{benchmark}_{timestamp}.samples.csv`
- **결과 데이터**: `{benchmark}_{timestamp}.results.csv`
- **트랜잭션별 결과**: `{benchmark}_{timestamp}.results.{txnType}.csv`

#### JSON 파일
- **요약**: `{benchmark}_{timestamp}.summary.json`
- **파라미터**: `{benchmark}_{timestamp}.params.json`
- **메트릭**: `{benchmark}_{timestamp}.metrics.json`
- **히스토그램**: 사용자 지정 파일명

#### XML 파일
- **설정**: `{benchmark}_{timestamp}.config.xml`

---

## 12. 확장성 및 확장

### 12.1 새 벤치마크 추가

**단계**:
1. `benchmarks/{name}/` 디렉토리 생성
2. `{Prefix}Benchmark.java` 구현
3. `{Prefix}Loader.java` 구현
4. `{Prefix}Worker.java` 구현
5. `procedures/` 디렉토리에 Procedure 클래스 구현
6. `config/plugin.xml`에 벤치마크 등록
7. DDL 스크립트 작성 (`src/main/resources/benchmarks/{name}/ddl-*.sql`)

**예제 구조**:
```java
public class MyBenchmark extends BenchmarkModule {
    public MyBenchmark(WorkloadConfiguration workConf) {
        super(workConf);
    }
    
    @Override
    protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
        // Worker 생성 로직
    }
    
    @Override
    protected Loader<? extends BenchmarkModule> makeLoaderImpl() {
        // Loader 생성 로직
    }
    
    @Override
    protected Package getProcedurePackageImpl() {
        return MyBenchmark.class.getPackage();
    }
}
```

### 12.2 새 DBMS 추가

**단계**:
1. `pom.xml`에 새 프로파일 추가
2. 해당 DBMS의 JDBC 드라이버 의존성 추가
3. `com.oltpbenchmark.types.DatabaseType`에 새 타입 추가
4. SQL 방언 파일 작성 (`config/{dbms}/dialects.xml`)
5. DDL 스크립트 작성 (필요시)

**예제 프로파일**:
```xml
<profile>
    <id>mydbms</id>
    <properties>
        <classifier>mydbms</classifier>
    </properties>
    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>mydbms-jdbc</artifactId>
            <version>1.0.0</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</profile>
```

### 12.3 새 트랜잭션 타입 추가

**단계**:
1. `procedures/` 디렉토리에 새 Procedure 클래스 생성
2. `run()` 메서드 구현
3. SQL 문을 `StatementDialects`에 추가
4. 설정 파일에 트랜잭션 타입 등록

---

## 13. 배포 및 빌드

### 13.1 빌드 요구사항

- **Java**: JDK 19 이상
- **Maven**: 3.8 이상
- **운영 체제**: Windows, Linux, macOS

### 13.2 빌드 명령

```bash
# 특정 DBMS 프로파일로 빌드
./mvnw clean package -P postgres

# 여러 프로파일로 빌드
./mvnw clean package -P postgres,mysql

# 테스트 포함 빌드
./mvnw clean package test -P postgres
```

### 13.3 배포 패키지

빌드 후 `target/` 디렉토리에 다음 파일 생성:
- `benchbase-{profile}.tgz`: 압축된 tar.gz 아카이브
- `benchbase-{profile}.zip`: ZIP 아카이브

**패키지 구조**:
```
benchbase-{profile}/
├── benchbase.jar          # 실행 가능한 JAR
├── lib/                   # 의존성 라이브러리
├── config/                # 설정 파일
├── data/                  # 데이터 파일
└── scripts/               # 유틸리티 스크립트
```

### 13.4 Maven으로 직접 실행

```bash
mvn clean compile exec:java -P postgres \
  -Dexec.args="-b tpcc -c config/postgres/sample_tpcc_config.xml \
  --create=true --load=true --execute=true"
```

### 13.5 Docker 지원

**개발 이미지**:
```bash
./docker/benchbase/build-dev-image.sh
./docker/benchbase/run-dev-image.sh
```

**전체 이미지**:
```bash
BENCHBASE_PROFILES='postgres mysql' ./docker/benchbase/build-full-image.sh
BENCHBASE_PROFILE='postgres' ./docker/benchbase/run-full-image.sh --help
```

---

## 14. 의존성 관리

### 14.1 핵심 의존성

| 그룹 ID | 아티팩트 ID | 버전 | 용도 |
|---------|------------|------|------|
| org.slf4j | slf4j-api | 2.0.16 | 로깅 인터페이스 |
| org.slf4j | slf4j-reload4j | 2.0.16 | 로깅 구현 |
| org.apache.commons | commons-configuration2 | 2.11.0 | 설정 관리 |
| commons-cli | commons-cli | 1.9.0 | CLI 파싱 |
| com.zaxxer | HikariCP | 5.1.0 | 연결 풀 |
| org.json | json | 20240303 | JSON 처리 |
| com.opencsv | opencsv | 5.9 | CSV 처리 |

### 14.2 데이터베이스 드라이버 (프로파일별)

| DBMS | 그룹 ID | 아티팩트 ID | 버전 |
|------|---------|------------|------|
| PostgreSQL | org.postgresql | postgresql | 42.7.4 |
| MySQL | mysql | mysql-connector-java | 8.0.30 |
| Oracle | com.oracle.database.jdbc | ojdbc11 | 23.7.0.25.01 |
| MariaDB | org.mariadb.jdbc | mariadb-java-client | 3.5.1 |
| SQL Server | com.microsoft.sqlserver | mssql-jdbc | 11.2.3.jre17 |
| SQLite | org.xerial | sqlite-jdbc | 3.47.1.0 |
| Spanner | com.google.cloud | google-cloud-spanner-jdbc | 2.24.1 |
| Phoenix | org.apache.phoenix | phoenix-client-hbase-2.4 | 5.1.3 |
| Tibero | - | tibero7-jdbc.jar (로컬) | 7.x |
| DB2 | - | db2jcc4.jar (로컬) | 4.x |

### 14.3 테스트 의존성

- **JUnit**: 4.13.2
- **HSQLDB**: 2.7.4 (테스트용)

### 14.4 코드 생성 의존성

- **Immutables**: 2.10.1 (불변 객체 생성)
- **Janino**: 3.1.12 (동적 컴파일)
- **JAXB**: 4.0.5 (XML 바인딩)

---

## 부록

### A. 용어 정의

- **Benchmark**: 벤치마크 테스트 스위트
- **Worker**: 트랜잭션을 실행하는 스레드
- **Loader**: 데이터를 데이터베이스에 로드하는 컴포넌트
- **Procedure**: 개별 트랜잭션 타입의 실행 로직
- **Phase**: 벤치마크 실행 단계 (워밍업, 측정 등)
- **Terminal**: 클라이언트 세션을 나타내는 가상 터미널
- **Dialect**: 데이터베이스별 SQL 문 변형

### B. 참고 자료

- [BenchBase GitHub 저장소](https://github.com/cmu-db/benchbase)
- [OLTPBench 논문](http://www.vldb.org/pvldb/vol7/p277-difallah.pdf)
- [TPC-C 스펙](http://www.tpc.org/tpcc/)
- [TPC-H 스펙](http://www.tpc.org/tpch/)

### C. 변경 이력

| 버전 | 날짜 | 변경 내용 |
|------|------|----------|
| 1.0 | 2024 | 초기 TRD 문서 작성 |
| 1.1 | 2024-12-27 | v0.1 변경 사항 반영: Java 19 지원, Tibero/DB2 추가, Oracle DDL 개선 |

---

**문서 끝**

