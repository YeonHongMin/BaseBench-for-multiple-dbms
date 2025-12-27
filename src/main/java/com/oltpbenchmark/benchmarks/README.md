# 벤치마크 구현

각 디렉터리는 다음과 같은 레이아웃을 가지고 있습니다:

* `{Prefix}Benchmark.java` - 벤치마크의 모든 클래스 경로를 설정하는 BenchmarkModule 구현입니다.
* `{Prefix}Loader.java` - 데이터베이스를 채우는 역할을 담당하는 Loader 구현입니다.
* `{Prefix}Worker.java` - 이 벤치마크의 Worker입니다. 추상 Worker 드라이버 코드로부터 무작위 TransactionType을 제공받은 후 적절한 프로시저를 호출합니다.
* `procedures` - 벤치마크의 모든 트랜잭션 타입에 대한 구현입니다.

벤치마크별 `dialects` 및 `ddls` 파일은 이제 `src/main/resources` 디렉터리에 저장됩니다.
