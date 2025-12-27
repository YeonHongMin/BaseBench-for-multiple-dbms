# BenchBase 작업 목록 (TASKS)

**생성일**: 2024  
**프로젝트**: BenchBase - 다중 DBMS SQL 벤치마킹 프레임워크

---

## 목차

1. [코드 완성 작업](#1-코드-완성-작업)
2. [기능 개선 작업](#2-기능-개선-작업)
3. [버그 수정 작업](#3-버그-수정-작업)
4. [테스트 개선 작업](#4-테스트-개선-작업)
5. [설정 파일 완성 작업](#5-설정-파일-완성-작업)
6. [문서화 작업](#6-문서화-작업)
7. [인프라 개선 작업](#7-인프라-개선-작업)
8. [벤치마크 확장 작업](#8-벤치마크-확장-작업)
9. [데이터베이스 지원 확장 작업](#9-데이터베이스-지원-확장-작업)
10. [성능 최적화 작업](#10-성능-최적화-작업)

---

## 1. 코드 완성 작업

### TASK-001: ZipfianGenerator.mean() 메서드 구현
- **우선순위**: 중간
- **카테고리**: 코드 완성
- **위치**: `src/main/java/com/oltpbenchmark/distributions/ZipfianGenerator.java:311-316`
- **설명**: Zipfian 분포의 평균값을 계산하는 메서드 구현 필요
- **현재 상태**: `UnsupportedOperationException` 발생
- **요구사항**:
  - Zipfian 분포의 수학적 평균 공식 구현
  - 단위 테스트 작성
  - 문서화 추가

### TASK-002: Worker.indicatesReadOnly() 메서드 완성
- **우선순위**: 낮음
- **카테고리**: 코드 완성
- **위치**: `src/main/java/com/oltpbenchmark/api/Worker.java:652-681`
- **설명**: MySQL 및 PostgreSQL의 읽기 전용 연결 감지 로직 구현
- **현재 상태**: TODO 주석만 존재, SQL Server만 구현됨
- **요구사항**:
  - MySQL 읽기 전용 연결 감지 로직 추가
  - PostgreSQL 읽기 전용 연결 감지 로직 추가
  - 각 DBMS별 SQL 상태 코드 문서화
  - 단위 테스트 작성

### TASK-003: ThreadBench.getInterval() 메서드 개선
- **우선순위**: 낮음
- **카테고리**: 코드 완성
- **위치**: `src/main/java/com/oltpbenchmark/ThreadBench.java:383-390`
- **설명**: "TODO Auto-generated method stub" 주석 제거 및 메서드 개선
- **현재 상태**: 기능은 동작하지만 주석이 남아있음
- **요구사항**:
  - 불필요한 주석 제거
  - 메서드 문서화 개선
  - 코드 리뷰

### TASK-004: AuctionMarkProfile 필드 문서화
- **우선순위**: 낮음
- **카테고리**: 코드 완성
- **위치**: `src/main/java/com/oltpbenchmark/benchmarks/auctionmark/AuctionMarkProfile.java:156-160`
- **설명**: TODO 주석이 있는 필드들에 대한 문서화 추가
- **현재 상태**: `seller_item_cnt`, `pending_commentResponses` 필드에 TODO 주석
- **요구사항**:
  - 필드 용도 설명 추가
  - JavaDoc 주석 작성

---

## 2. 기능 개선 작업

### TASK-005: Templated 벤치마크에 init/load 단계 지원 추가
- **우선순위**: 높음
- **카테고리**: 기능 개선
- **위치**: `src/main/java/com/oltpbenchmark/benchmarks/templated/`
- **설명**: Templated 벤치마크에 데이터베이스 초기화 및 데이터 로딩 기능 추가
- **현재 상태**: init/load 단계 미지원
- **요구사항**:
  - TemplatedBenchmark에 createDatabase() 구현
  - TemplatedLoader 구현
  - 설정 파일에 DDL 경로 지정 기능 추가
  - 문서화 업데이트
  - 단위 테스트 작성

### TASK-006: SQLite SLEEP 함수 및 락 작업 지원
- **우선순위**: 중간
- **카테고리**: 기능 개선
- **위치**: `src/main/resources/benchmarks/resourcestresser/dialect-sqlite.xml:16-25`
- **설명**: SQLite에서 SLEEP 함수 및 명시적 락 작업 지원 추가
- **현재 상태**: FIXME 주석으로 표시된 제한사항
- **요구사항**:
  - SQLite 사용자 정의 함수로 SLEEP 구현
  - 또는 대체 방법 제시
  - 락 작업 대체 방법 구현
  - 문서화 추가

### TASK-007: 데이터 익명화(Anonymization) 기능 완성
- **우선순위**: 중간
- **카테고리**: 기능 개선
- **위치**: `scripts/anonymization/src/anonymizer.py:55`
- **설명**: 민감 정보 익명화 기능 완성
- **현재 상태**: TODO 주석만 존재
- **요구사항**:
  - 차등 프라이버시 기반 익명화 알고리즘 구현
  - 설정 파일 파싱 및 적용
  - 템플릿 쿼리 파일 자동 수정 기능
  - 문서화 및 예제 추가

### TASK-008: 연결 풀 모니터링 및 통계 개선
- **우선순위**: 낮음
- **카테고리**: 기능 개선
- **위치**: `src/main/java/com/oltpbenchmark/util/ConnectionPoolManager.java`
- **설명**: 연결 풀의 상세한 통계 및 모니터링 기능 추가
- **현재 상태**: 기본 통계만 제공
- **요구사항**:
  - 연결 획득/반환 시간 통계
  - 활성/유휴 연결 수 추적
  - 연결 풀 히트율 계산
  - 결과 파일에 연결 풀 통계 포함

---

## 3. 버그 수정 작업

### TASK-009: MariaDB DDL 처리 HACK 제거
- **우선순위**: 낮음
- **카테고리**: 버그 수정
- **위치**: `src/main/java/com/oltpbenchmark/api/BenchmarkModule.java:238`
- **설명**: MariaDB를 MySQL로 처리하는 HACK 제거 및 적절한 처리 방법 구현
- **현재 상태**: MariaDB를 MySQL로 강제 변환
- **요구사항**:
  - MariaDB 전용 DDL 파일 생성 또는
  - MariaDB와 MySQL의 차이점을 고려한 처리 로직 구현
  - 테스트 케이스 추가

### TASK-010: Docker 테스트 병렬 실행 버그 수정
- **우선순위**: 낮음
- **카테고리**: 버그 수정
- **위치**: `docker/benchbase/devcontainer/build-in-container.sh:114`
- **설명**: Docker 컨테이너 내 테스트 병렬 실행 시 발생하는 버그 수정
- **현재 상태**: FIXME 주석으로 표시됨, 병렬 실행 비활성화됨
- **요구사항**:
  - 버그 원인 분석
  - 병렬 실행 지원 또는 적절한 해결책 제시
  - CI/CD 파이프라인 개선

---

## 4. 테스트 개선 작업

### TASK-011: SEATS 벤치마크 테스트 개선
- **우선순위**: 낮음
- **카테고리**: 테스트 개선
- **위치**: `src/test/java/com/oltpbenchmark/benchmarks/seats/TestSEATSLoader.java:79`
- **설명**: 주석 처리된 테스트 코드 검토 및 개선
- **현재 상태**: 일부 테스트가 주석 처리됨
- **요구사항**:
  - 주석 처리된 테스트 검토
  - 필요시 테스트 재활성화 또는 수정
  - 테스트 커버리지 향상

### TASK-012: Templated 벤치마크 단위 테스트 추가
- **우선순위**: 중간
- **카테고리**: 테스트 개선
- **위치**: `src/test/java/com/oltpbenchmark/benchmarks/templated/`
- **설명**: Templated 벤치마크에 대한 전용 단위 테스트 추가
- **현재 상태**: TPC-C 로더를 재사용하는 임시 방법 사용
- **요구사항**:
  - TestTemplatedBenchmark.java 작성
  - 독립적인 테스트 데이터셋 생성
  - 다양한 템플릿 시나리오 테스트

### TASK-013: 연결 풀 관련 테스트 추가
- **우선순위**: 중간
- **카테고리**: 테스트 개선
- **위치**: `src/test/java/com/oltpbenchmark/util/`
- **설명**: ConnectionPoolManager에 대한 단위 테스트 및 통합 테스트 추가
- **현재 상태**: 테스트 부족
- **요구사항**:
  - 연결 풀 생성/종료 테스트
  - 연결 획득/반환 테스트
  - 풀 크기 제한 테스트
  - 타임아웃 테스트
  - 동시성 테스트

---

## 5. 설정 파일 완성 작업

### TASK-014: hyadapt 벤치마크 설정 파일 추가
- **우선순위**: 중간
- **카테고리**: 설정 파일 완성
- **위치**: `config/` 디렉토리
- **설명**: hyadapt 벤치마크를 위한 설정 파일 생성
- **현재 상태**: README에 "설정 파일 대기 중"으로 표시됨
- **요구사항**:
  - 각 DBMS별 샘플 설정 파일 생성
  - plugin.xml에 등록 확인
  - 문서화 추가

### TASK-015: TPC-DS 벤치마크 설정 파일 추가
- **우선순위**: 중간
- **카테고리**: 설정 파일 완성
- **위치**: `config/` 디렉토리
- **설명**: TPC-DS 벤치마크를 위한 설정 파일 생성
- **현재 상태**: README에 "설정 파일 대기 중"으로 표시됨
- **요구사항**:
  - 각 DBMS별 샘플 설정 파일 생성
  - plugin.xml에 등록 확인
  - 문서화 추가

### TASK-016: Wikipedia 벤치마크 AUTO_INCREMENT 처리 완성
- **우선순위**: 낮음
- **카테고리**: 설정 파일 완성
- **위치**: `src/main/resources/benchmarks/wikipedia/ddl-*.sql`
- **설명**: 여러 DDL 파일에 TODO로 표시된 AUTO_INCREMENT 처리 완성
- **현재 상태**: ipb_id, log_id, page_id, rev_id, old_id에 TODO 주석
- **요구사항**:
  - 각 DBMS별 적절한 AUTO_INCREMENT/SEQUENCE 구문 적용
  - 테스트 및 검증

### TASK-017: Twitter 벤치마크 AUTO_INCREMENT 처리 완성
- **우선순위**: 낮음
- **카테고리**: 설정 파일 완성
- **위치**: `src/main/resources/benchmarks/twitter/ddl-*.sql`
- **설명**: Twitter 벤치마크 DDL 파일의 AUTO_INCREMENT 처리 완성
- **현재 상태**: id 필드에 TODO 주석
- **요구사항**:
  - 각 DBMS별 적절한 AUTO_INCREMENT/SEQUENCE 구문 적용
  - 테스트 및 검증

### TASK-018: TPC-C 벤치마크 ON UPDATE CURRENT_TIMESTAMP 처리
- **우선순위**: 낮음
- **카테고리**: 설정 파일 완성
- **위치**: `src/main/resources/benchmarks/tpcc/ddl-*.sql`
- **설명**: 여러 DDL 파일에 TODO로 표시된 ON UPDATE CURRENT_TIMESTAMP 처리
- **현재 상태**: C_SINCE, O_ENTRY_D, H_DATE 필드에 TODO 주석
- **요구사항**:
  - 각 DBMS별 적절한 트리거 또는 대체 방법 구현
  - 테스트 및 검증

---

## 6. 문서화 작업

### TASK-019: PMD 코드 품질 체크 활성화
- **우선순위**: 낮음
- **카테고리**: 문서화/코드 품질
- **위치**: `pom.xml:408-422`
- **설명**: 주석 처리된 PMD 플러그인 활성화
- **현재 상태**: TODO 주석으로 표시됨
- **요구사항**:
  - PMD 플러그인 활성화
  - 규칙 설정
  - CI/CD 파이프라인에 통합
  - 문서화 추가

### TASK-020: JavaDoc 문서화 린팅 활성화
- **우선순위**: 낮음
- **카테고리**: 문서화
- **위치**: `pom.xml:393-395`
- **설명**: 주석 처리된 JavaDoc 린팅 활성화
- **현재 상태**: TODO 주석으로 표시됨
- **요구사항**:
  - -Xdoclint 옵션 활성화
  - 누락된 JavaDoc 추가
  - 문서화 표준 정의

### TASK-021: API 문서 자동 생성 및 배포
- **우선순위**: 낮음
- **카테고리**: 문서화
- **위치**: 프로젝트 루트
- **설명**: JavaDoc을 기반으로 한 API 문서 자동 생성 및 배포
- **현재 상태**: 없음
- **요구사항**:
  - Maven JavaDoc 플러그인 설정
  - GitHub Pages 또는 다른 호스팅 설정
  - CI/CD 파이프라인에 통합

### TASK-022: 벤치마크별 상세 문서 작성
- **우선순위**: 중간
- **카테고리**: 문서화
- **위치**: `docs/benchmarks/` (신규 생성)
- **설명**: 각 벤치마크에 대한 상세한 사용 가이드 작성
- **현재 상태**: 위키 링크만 존재
- **요구사항**:
  - 각 벤치마크별 문서 작성
  - 설정 예제 포함
  - 성능 튜닝 가이드 포함
  - 트러블슈팅 가이드 포함

---

## 7. 인프라 개선 작업

### TASK-023: Docker 멀티스테이지 빌드 개선
- **우선순위**: 낮음
- **카테고리**: 인프라 개선
- **위치**: `docker/benchbase/devcontainer/build-in-container.sh:5`
- **설명**: Docker 빌드 캐싱을 위한 멀티스테이지 빌드로 전환
- **현재 상태**: TODO 주석으로 표시됨
- **요구사항**:
  - 멀티스테이지 Dockerfile 작성
  - 빌드 시간 단축
  - 캐시 효율성 개선

### TASK-024: Docker 전체 이미지 멀티스테이지 빌드
- **우선순위**: 낮음
- **카테고리**: 인프라 개선
- **위치**: `docker/benchbase/fullimage/Dockerfile:1`
- **설명**: 전체 이미지를 devcontainer에서 빌드하도록 개선
- **현재 상태**: TODO 주석으로 표시됨
- **요구사항**:
  - 멀티스테이지 빌드 구조 설계
  - 이미지 크기 최적화
  - 빌드 시간 단축

### TASK-025: CI/CD 파이프라인 개선
- **우선순위**: 중간
- **카테고리**: 인프라 개선
- **위치**: `.github/workflows/`
- **설명**: GitHub Actions 워크플로우 개선
- **현재 상태**: 기본적인 Maven 빌드만 수행
- **요구사항**:
  - 모든 DBMS 프로파일 빌드 테스트
  - 통합 테스트 자동화
  - 성능 벤치마크 자동 실행
  - 결과 리포트 자동 생성

### TASK-026: 의존성 보안 업데이트 자동화
- **우선순위**: 중간
- **카테고리**: 인프라 개선
- **위치**: 프로젝트 전체
- **설명**: Dependabot을 통한 의존성 업데이트 자동화 개선
- **현재 상태**: 기본 설정만 존재
- **요구사항**:
  - 보안 취약점 자동 스캔
  - 업데이트 PR 자동 생성
  - 테스트 자동 실행
  - 변경 로그 자동 생성

---

## 8. 벤치마크 확장 작업

### TASK-027: 새 벤치마크 추가 가이드 작성
- **우선순위**: 중간
- **카테고리**: 벤치마크 확장
- **위치**: `docs/` 디렉토리
- **설명**: 새 벤치마크를 추가하는 방법에 대한 상세 가이드 작성
- **현재 상태**: 기본적인 README만 존재
- **요구사항**:
  - 단계별 튜토리얼 작성
  - 코드 템플릿 제공
  - 베스트 프랙티스 문서화
  - 예제 포함

### TASK-028: 벤치마크 성능 비교 도구 개발
- **우선순위**: 낮음
- **카테고리**: 벤치마크 확장
- **위치**: `scripts/` 디렉토리
- **설명**: 여러 벤치마크 실행 결과를 비교하는 도구 개발
- **현재 상태**: 없음
- **요구사항**:
  - 결과 파일 파싱
  - 시각화 (그래프, 차트)
  - 리포트 생성
  - 웹 인터페이스 (선택사항)

---

## 9. 데이터베이스 지원 확장 작업

### TASK-029: 새 DBMS 추가 가이드 작성
- **우선순위**: 중간
- **카테고리**: 데이터베이스 지원 확장
- **위치**: `docs/` 디렉토리
- **설명**: 새 DBMS를 추가하는 방법에 대한 상세 가이드 작성
- **현재 상태**: CONTRIBUTING.md에 간단한 언급만 존재
- **요구사항**:
  - 단계별 튜토리얼 작성
  - SQL 방언 작성 가이드
  - DDL 작성 가이드
  - 테스트 방법 문서화

### TASK-030: DB2 프로파일 추가
- **우선순위**: 낮음
- **카테고리**: 데이터베이스 지원 확장
- **위치**: `pom.xml`, `config/` 디렉토리
- **설명**: DB2 데이터베이스에 대한 Maven 프로파일 추가
- **현재 상태**: 일부 벤치마크에 DB2 DDL이 존재하지만 프로파일 없음
- **요구사항**:
  - Maven 프로파일 추가
  - JDBC 드라이버 의존성 추가
  - 샘플 설정 파일 생성
  - 테스트 추가

### TASK-031: Tibero 프로파일 추가
- **우선순위**: 낮음
- **카테고리**: 데이터베이스 지원 확장
- **위치**: `pom.xml`, `config/` 디렉토리
- **설명**: Tibero 데이터베이스에 대한 Maven 프로파일 추가
- **현재 상태**: 일부 설정 파일이 존재하지만 프로파일 없음
- **요구사항**:
  - Maven 프로파일 추가
  - JDBC 드라이버 의존성 추가
  - 샘플 설정 파일 생성
  - 테스트 추가

---

## 10. 성능 최적화 작업

### TASK-032: 데이터 로딩 성능 최적화
- **우선순위**: 중간
- **카테고리**: 성능 최적화
- **위치**: `src/main/java/com/oltpbenchmark/api/Loader.java`
- **설명**: 대용량 데이터 로딩 시 성능 개선
- **현재 상태**: 기본적인 배치 삽입만 지원
- **요구사항**:
  - 병렬 로딩 최적화
  - 배치 크기 자동 조정
  - 메모리 사용량 최적화
  - 진행 상황 모니터링 개선

### TASK-033: Worker 스레드 오버헤드 최소화
- **우선순위**: 낮음
- **카테고리**: 성능 최적화
- **위치**: `src/main/java/com/oltpbenchmark/api/Worker.java`
- **설명**: Worker 스레드의 오버헤드를 최소화하여 더 정확한 성능 측정
- **현재 상태**: 기본 구현
- **요구사항**:
  - 불필요한 객체 생성 최소화
  - 메모리 할당 최적화
  - CPU 사용량 분석
  - 프로파일링 및 개선

### TASK-034: 결과 수집 및 기록 성능 개선
- **우선순위**: 낮음
- **카테고리**: 성능 최적화
- **위치**: `src/main/java/com/oltpbenchmark/ThreadBench.java`
- **설명**: 대규모 벤치마크 실행 시 결과 수집 성능 개선
- **현재 상태**: 기본 구현
- **요구사항**:
  - 비동기 결과 기록
  - 메모리 효율적인 히스토그램 구현
  - 스트리밍 결과 출력
  - 압축 옵션 추가

---

## 우선순위 요약

### 높은 우선순위
- TASK-005: Templated 벤치마크에 init/load 단계 지원 추가

### 중간 우선순위
- TASK-001: ZipfianGenerator.mean() 메서드 구현
- TASK-006: SQLite SLEEP 함수 및 락 작업 지원
- TASK-007: 데이터 익명화 기능 완성
- TASK-012: Templated 벤치마크 단위 테스트 추가
- TASK-013: 연결 풀 관련 테스트 추가
- TASK-014: hyadapt 벤치마크 설정 파일 추가
- TASK-015: TPC-DS 벤치마크 설정 파일 추가
- TASK-022: 벤치마크별 상세 문서 작성
- TASK-025: CI/CD 파이프라인 개선
- TASK-027: 새 벤치마크 추가 가이드 작성
- TASK-029: 새 DBMS 추가 가이드 작성
- TASK-032: 데이터 로딩 성능 최적화

### 낮은 우선순위
- TASK-002: Worker.indicatesReadOnly() 메서드 완성
- TASK-003: ThreadBench.getInterval() 메서드 개선
- TASK-004: AuctionMarkProfile 필드 문서화
- TASK-008: 연결 풀 모니터링 및 통계 개선
- TASK-009: MariaDB DDL 처리 HACK 제거
- TASK-010: Docker 테스트 병렬 실행 버그 수정
- TASK-011: SEATS 벤치마크 테스트 개선
- TASK-016: Wikipedia 벤치마크 AUTO_INCREMENT 처리 완성
- TASK-017: Twitter 벤치마크 AUTO_INCREMENT 처리 완성
- TASK-018: TPC-C 벤치마크 ON UPDATE CURRENT_TIMESTAMP 처리
- TASK-019: PMD 코드 품질 체크 활성화
- TASK-020: JavaDoc 문서화 린팅 활성화
- TASK-021: API 문서 자동 생성 및 배포
- TASK-023: Docker 멀티스테이지 빌드 개선
- TASK-024: Docker 전체 이미지 멀티스테이지 빌드
- TASK-026: 의존성 보안 업데이트 자동화
- TASK-028: 벤치마크 성능 비교 도구 개발
- TASK-030: DB2 프로파일 추가
- TASK-031: Tibero 프로파일 추가
- TASK-033: Worker 스레드 오버헤드 최소화
- TASK-034: 결과 수집 및 기록 성능 개선

---

## 작업 통계

- **총 작업 수**: 34개
- **높은 우선순위**: 1개
- **중간 우선순위**: 12개
- **낮은 우선순위**: 21개

---

## 작업 상태 추적

각 작업은 다음 상태를 가질 수 있습니다:
- **TODO**: 아직 시작하지 않음
- **IN_PROGRESS**: 진행 중
- **REVIEW**: 코드 리뷰 대기 중
- **DONE**: 완료됨
- **BLOCKED**: 차단됨 (다른 작업에 의존)
- **CANCELLED**: 취소됨

---

**문서 끝**

