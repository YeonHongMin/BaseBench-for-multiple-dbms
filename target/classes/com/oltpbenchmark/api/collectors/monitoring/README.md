<<<<<<< HEAD
# Monitoring in BenchBase

Monitoring in BenchBase can be enabled using the
```text
 -im,--interval-monitor <arg>   Monitoring Interval in milliseconds
 -mt,--monitor-type <arg>       Type of Monitoring (throughput/advanced)
```
command line option when executing BenchBase, where the monitoring interval describes the sleeping period of the thread between recording monitoring information.
We currently support two types of monitoring:

1. Basic throughput monitoring to track the progress while executing a benchmark (`-mt=throughput`), and
2. monitoring of query and system properties via system tables for both SQLServer and Postgres (`-mt=advanced`).
    Support for other engines can also be added.

The former is the default setting unless the monitoring type is explicitly set to advanced which will trigger system monitoring if the database type is supported.

Throughput monitoring logs updated throughput values directly to the system output, while advanced monitoring creates csv files recording their findings in folder `results/monitor/`.
Advanced monitoring collects data for a variety of events, such as one-off information about a query (for example query plans, query text, etc.), repeated information about a query (elapsed time per query execution, worker time, execution count etc.), and repeated system information (cache hits, number of transactions etc.).
Which events are collected depends on the database system and is customized in corresponding drivers.
The code for the drivers can be found in package [`src.main.java.com.oltpbenchmark.api.collectors.monitoring`](./../monitoring/).

For advanced monitoring to function with SQLServer, the user needs to have access to the system tables, for Postgres, `pg_stat_statements` needs to be enabled.
Queries will fail gracefully, i.e., without interrupting the benchmark execution but instead logging an error.
Note that in either database system, frequent (additional) queries against the DBMS may distort the benchmarking results.
That is, a high additional query load via frequent pulling of data from the DBMS will incur system load and can potentially block the execution of the actual benchmark queries.
=======
# BenchBase의 모니터링

BenchBase의 모니터링은 BenchBase 실행 시 다음 명령줄 옵션을 사용하여 활성화할 수 있습니다:
```text
 -im,--interval-monitor <arg>   모니터링 간격(밀리초)
 -mt,--monitor-type <arg>       모니터링 유형 (throughput/advanced)
```
여기서 모니터링 간격은 모니터링 정보를 기록하는 스레드의 대기 시간을 나타냅니다.
현재 두 가지 유형의 모니터링을 지원합니다:

1. 벤치마크 실행 중 진행 상황을 추적하는 기본 처리량 모니터링 (`-mt=throughput`), 그리고
2. SQLServer와 Postgres 모두에 대해 시스템 테이블을 통한 쿼리 및 시스템 속성 모니터링 (`-mt=advanced`).
    다른 엔진에 대한 지원도 추가할 수 있습니다.

전자는 모니터링 유형이 명시적으로 advanced로 설정되지 않는 한 기본 설정이며, advanced로 설정하면 데이터베이스 유형이 지원되는 경우 시스템 모니터링이 트리거됩니다.

처리량 모니터링은 업데이트된 처리량 값을 시스템 출력에 직접 기록하는 반면, 고급 모니터링은 `results/monitor/` 폴더에 결과를 기록하는 CSV 파일을 생성합니다.
고급 모니터링은 다양한 이벤트에 대한 데이터를 수집합니다. 예를 들어 쿼리에 대한 일회성 정보(쿼리 계획, 쿼리 텍스트 등), 쿼리에 대한 반복 정보(쿼리 실행당 경과 시간, 작업자 시간, 실행 횟수 등), 반복 시스템 정보(캐시 히트, 트랜잭션 수 등)가 있습니다.
수집되는 이벤트는 데이터베이스 시스템에 따라 다르며 해당 드라이버에서 사용자 정의됩니다.
드라이버 코드는 [`src.main.java.com.oltpbenchmark.api.collectors.monitoring`](./../monitoring/) 패키지에서 찾을 수 있습니다.

SQLServer에서 고급 모니터링이 작동하려면 사용자가 시스템 테이블에 액세스할 수 있어야 하며, Postgres의 경우 `pg_stat_statements`가 활성화되어 있어야 합니다.
쿼리는 우아하게 실패합니다. 즉, 벤치마크 실행을 중단하지 않고 대신 오류를 기록합니다.
두 데이터베이스 시스템 모두에서 DBMS에 대한 빈번한 (추가) 쿼리가 벤치마킹 결과를 왜곡할 수 있습니다.
즉, DBMS에서 데이터를 자주 가져오는 높은 추가 쿼리 부하는 시스템 부하를 발생시키고 실제 벤치마크 쿼리 실행을 차단할 수 있습니다.
>>>>>>> master
