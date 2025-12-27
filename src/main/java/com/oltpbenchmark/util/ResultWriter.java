/*
 * 저작권 2020 OLTPBenchmark 프로젝트
 *
 * Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 * 라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 * 라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 * 이 소프트웨어는 "있는 그대로" 배포되며,
 * 명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 * 라이선스에서 허용하는 권한과 제한 사항은
 * 라이선스의 본문을 참조하십시오.
 *
 */

package com.oltpbenchmark.util;

import com.oltpbenchmark.DistributionStatistics;
import com.oltpbenchmark.LatencyRecord;
import com.oltpbenchmark.Results;
import com.oltpbenchmark.ThreadBench;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.collectors.DBParameterCollector;
import com.oltpbenchmark.api.collectors.DBParameterCollectorGen;
import com.oltpbenchmark.types.DatabaseType;
import java.io.PrintStream;
import java.util.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

public class ResultWriter {

  public static final double MILLISECONDS_FACTOR = 1e3;

  private static final String[] IGNORE_CONF = {"type", "driver", "url", "username", "password"};

  private static final String[] BENCHMARK_KEY_FIELD = {"isolation", "scalefactor", "terminals"};

  private final XMLConfiguration expConf;
  private final DBParameterCollector collector;
  private final Results results;
  private final DatabaseType dbType;
  private final String benchType;

  public ResultWriter(Results r, XMLConfiguration conf, CommandLine argsLine) {
    this.expConf = conf;
    this.results = r;
    this.dbType = DatabaseType.valueOf(expConf.getString("type").toUpperCase());
    this.benchType = argsLine.getOptionValue("b");

    String dbUrl = expConf.getString("url");
    String username = expConf.getString("username");
    String password = expConf.getString("password");

    this.collector = DBParameterCollectorGen.getCollector(dbType, dbUrl, username, password);
  }

  public void writeParams(PrintStream os) {
    String dbConf = collector.collectParameters();
    os.print(dbConf);
  }

  public void writeMetrics(PrintStream os) {
    os.print(collector.collectMetrics());
  }

  public boolean hasMetrics() {
    return collector.hasMetrics();
  }

  public void writeConfig(PrintStream os) throws ConfigurationException {

    XMLConfiguration outputConf = (XMLConfiguration) expConf.clone();
    for (String key : IGNORE_CONF) {
      outputConf.clearProperty(key);
    }

    FileHandler handler = new FileHandler(outputConf);
    handler.save(os);
  }

  public void writeSummary(PrintStream os) {
    Map<String, Object> summaryMap = new LinkedHashMap<>();
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    Date now = new Date();
    summaryMap.put("Start timestamp (milliseconds)", results.getStartTimestampMs());
    summaryMap.put("Current Timestamp (milliseconds)", now.getTime());
    summaryMap.put("Elapsed Time (nanoseconds)", results.getNanoseconds());
    summaryMap.put("DBMS Type", dbType);
    summaryMap.put("DBMS Version", collector.collectVersion());
    summaryMap.put("Benchmark Type", benchType);
    summaryMap.put("Final State", results.getState());
    summaryMap.put("Measured Requests", results.getMeasuredRequests());
    for (String field : BENCHMARK_KEY_FIELD) {
      summaryMap.put(field, expConf.getString(field));
    }
    summaryMap.put("Latency Distribution", results.getDistributionStatistics().toMap());
    summaryMap.put("Throughput (requests/second)", results.requestsPerSecondThroughput());
    summaryMap.put("Goodput (requests/second)", results.requestsPerSecondGoodput());
    os.println(JSONUtil.format(JSONUtil.toJSONString(summaryMap)));
  }

  public void writeResults(int windowSizeSeconds, PrintStream out) {
    writeResults(windowSizeSeconds, out, TransactionType.INVALID);
  }

  public void writeResults(int windowSizeSeconds, PrintStream out, TransactionType txType) {
    String[] header = {
      "Time (seconds)",
      "Throughput (requests/second)",
      "Average Latency (millisecond)",
      "Minimum Latency (millisecond)",
      "25th Percentile Latency (millisecond)",
      "Median Latency (millisecond)",
      "75th Percentile Latency (millisecond)",
      "90th Percentile Latency (millisecond)",
      "95th Percentile Latency (millisecond)",
      "99th Percentile Latency (millisecond)",
      "Maximum Latency (millisecond)",
      "tp (req/s) scaled"
    };
    out.println(StringUtil.join(",", header));
    int i = 0;
    for (DistributionStatistics s :
        new ThreadBench.TimeBucketIterable(
            results.getLatencySamples(), windowSizeSeconds, txType)) {
      out.printf(
          "%d,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f\n",
          i * windowSizeSeconds,
          (double) s.getCount() / windowSizeSeconds,
          s.getAverage() / MILLISECONDS_FACTOR,
          s.getMinimum() / MILLISECONDS_FACTOR,
          s.get25thPercentile() / MILLISECONDS_FACTOR,
          s.getMedian() / MILLISECONDS_FACTOR,
          s.get75thPercentile() / MILLISECONDS_FACTOR,
          s.get90thPercentile() / MILLISECONDS_FACTOR,
          s.get95thPercentile() / MILLISECONDS_FACTOR,
          s.get99thPercentile() / MILLISECONDS_FACTOR,
          s.getMaximum() / MILLISECONDS_FACTOR,
          MILLISECONDS_FACTOR / s.getAverage());
      i += 1;
    }
  }

  public void writeSamples(PrintStream out) {
    writeSamples(1, out, TransactionType.INVALID);
  }

  public void writeSamples(int windowSizeSeconds, PrintStream out, TransactionType txType) {
    String[] header = {
      "Time (seconds)",
      "Requests",
      "Throughput (requests/second)",
      "Minimum Latency (microseconds)",
      "25th Percentile Latency (microseconds)",
      "Median Latency (microseconds)",
      "Average Latency (microseconds)",
      "75th Percentile Latency (microseconds)",
      "90th Percentile Latency (microseconds)",
      "95th Percentile Latency (microseconds)",
      "99th Percentile Latency (microseconds)",
      "Maximum Latency (microseconds)"
    };
    out.println(StringUtil.join(",", header));
    int i = 0;
    for (DistributionStatistics s :
        new ThreadBench.TimeBucketIterable(
            results.getLatencySamples(), windowSizeSeconds, txType)) {
      out.printf(
          "%d,%d,%.3f,%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
          i * windowSizeSeconds,
          s.getCount(),
          (double) s.getCount() / windowSizeSeconds,
          (int) s.getMinimum(),
          (int) s.get25thPercentile(),
          (int) s.getMedian(),
          (int) s.getAverage(),
          (int) s.get75thPercentile(),
          (int) s.get90thPercentile(),
          (int) s.get95thPercentile(),
          (int) s.get99thPercentile(),
          (int) s.getMaximum());
      i += 1;
    }
  }

  public void writeRaw(List<TransactionType> activeTXTypes, PrintStream out) {

    // nanoTime은 기준 오프셋을 보장하지 않기 때문에 보정이 필요합니다.
    // 현재시간(currentTime)을 기준으로 1970-01-01부터 밀리초 단위로 맞춥니다.
    double x = ((double) System.nanoTime() / (double) 1000000000);
    double y = ((double) System.currentTimeMillis() / (double) 1000);
    double offset = x - y;

    // long startNs = latencySamples.get(0).startNs;
    String[] header = {
      "Transaction Type Index",
      "Transaction Name",
      "Start Time (microseconds)",
      "Latency (microseconds)",
      "Worker Id (start number)",
      "Phase Id (index in config file)"
    };
    out.println(StringUtil.join(",", header));
    for (LatencyRecord.Sample s : results.getLatencySamples()) {
      double startUs = ((double) s.getStartNanosecond() / (double) 1000000000);
      String[] row = {
        Integer.toString(s.getTransactionType()),
        // 중요!
        // TxnType 오프셋은 1부터 시작합니다!
        activeTXTypes.get(s.getTransactionType() - 1).getName(),
        String.format("%10.6f", startUs - offset),
        Integer.toString(s.getLatencyMicrosecond()),
        Integer.toString(s.getWorkerId()),
        Integer.toString(s.getPhaseId()),
      };
      out.println(StringUtil.join(",", row));
    }
  }
}
