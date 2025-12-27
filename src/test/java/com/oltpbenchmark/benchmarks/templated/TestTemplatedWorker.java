<<<<<<< HEAD
<<<<<<< HEAD
=======
/*
 *  저작권 2015 OLTPBenchmark 프로젝트
 *
 *  Apache License, Version 2.0(이하 "라이선스")에 따라 사용이 허가됩니다.
 *  라이선스를 준수하지 않고는 이 파일을 사용할 수 없습니다.
 *  라이선스 사본은 다음에서 확인할 수 있습니다.
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  관련 법률에서 요구하거나 서면으로 합의하지 않는 한,
 *  이 소프트웨어는 "있는 그대로" 배포되며,
 *  명시적이거나 묵시적인 어떠한 보증도 제공하지 않습니다.
 *  라이선스에서 허용하는 권한과 제한 사항은
 *  라이선스의 본문을 참조하십시오.
 *
 */

>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
package com.oltpbenchmark.benchmarks.templated;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.oltpbenchmark.DBWorkload;
import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.AbstractTestWorker;
import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.benchmarks.tpcc.TPCCBenchmark;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTemplatedWorker extends AbstractTestWorker<TemplatedBenchmark> {
  private static final Logger LOG = LoggerFactory.getLogger(TestTemplatedWorker.class);

  public static final String DDL_OVERRIDE_PATH =
      Paths.get("src", "main", "resources", "benchmarks", "tpcc", "ddl-generic.sql")
          .toAbsolutePath()
          .toString();
  public static final String SAMPLE_TEMPLATED_LOADING_CONFIG =
      Paths.get("config", "sqlite", "sample_tpcc_config.xml").toAbsolutePath().toString();
  public static final String SAMPLE_TEMPLATED_CONFIG =
      Paths.get("config", "sqlite", "sample_templated_config.xml").toAbsolutePath().toString();
  public static final String TEMPLATES_CONFIG =
      Paths.get("data", "templated", "example.xml").toAbsolutePath().toString();

  TPCCBenchmark tpccBenchmark = null;

  public TestTemplatedWorker() {
<<<<<<< HEAD
<<<<<<< HEAD
    // Technically we aren't creating this schema with the
    // TemplatedBenchmark, but specifying the DDL that we are using (see
    // below) allows some other checks to pass.
=======
    // 기술적으로는 TemplatedBenchmark로 이 스키마를 생성하는 것이 아니지만,
    // 사용 중인 DDL을 지정하면(아래 참조) 다른 일부 검사가 통과할 수 있습니다.
>>>>>>> master
=======
    // Technically we aren't creating this schema with the
    // TemplatedBenchmark, but specifying the DDL that we are using (see
    // below) allows some other checks to pass.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    super(DDL_OVERRIDE_PATH);
  }

  public static void setWorkloadConfigXml(WorkloadConfiguration workConf) {
<<<<<<< HEAD
<<<<<<< HEAD
    // Load the configuration file so we can parse the query_template_file value.
=======
    // query_template_file 값을 파싱할 수 있도록 설정 파일을 로드합니다.
>>>>>>> master
=======
    // Load the configuration file so we can parse the query_template_file value.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    try {
      XMLConfiguration xmlConf = DBWorkload.buildConfiguration(SAMPLE_TEMPLATED_CONFIG);
      workConf.setXmlConfig(xmlConf);
    } catch (ConfigurationException ex) {
      LOG.error("Error loading configuration: " + SAMPLE_TEMPLATED_CONFIG, ex);
    }
  }

  @Override
  protected void customWorkloadConfiguration(WorkloadConfiguration workConf) {
    setWorkloadConfigXml(workConf);
  }

  @Override
  public List<Class<? extends Procedure>> procedures() {
<<<<<<< HEAD
<<<<<<< HEAD
    // Note: the first time this is called is before the benchmark is
    // initialized, so it should return nothing.
    // It's only populated after the config is loaded for the benchmark.
=======
    // 참고: 처음 호출될 때는 벤치마크가 초기화되기 전이므로 아무것도 반환하지 않아야 합니다.
    // 벤치마크의 설정이 로드된 후에만 채워집니다.
>>>>>>> master
=======
    // Note: the first time this is called is before the benchmark is
    // initialized, so it should return nothing.
    // It's only populated after the config is loaded for the benchmark.
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    List<Class<? extends Procedure>> procedures = new ArrayList<>();
    if (this.benchmark != null) {
      procedures = this.benchmark.getProcedureClasses();
      if (!procedures.isEmpty() && this.workConf.getTransTypes().isEmpty()) {
        workConf.setTransTypes(proceduresToTransactionTypes(procedures));
      }
    }
    return procedures;
  }

  @Override
  public Class<TemplatedBenchmark> benchmarkClass() {
    return TemplatedBenchmark.class;
  }

  private void setupTpccBenchmarkHelper() throws SQLException {
    if (this.tpccBenchmark != null) {
      return;
    }

<<<<<<< HEAD
<<<<<<< HEAD
    // Create a second benchmark to re/ab/use for loading the database (tpcc in this case).
=======
    // 데이터베이스 로드를 위해 두 번째 벤치마크를 생성합니다(이 경우 tpcc).
>>>>>>> master
=======
    // Create a second benchmark to re/ab/use for loading the database (tpcc in this case).
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
    WorkloadConfiguration tpccWorkConf = new WorkloadConfiguration();
    tpccWorkConf.setDatabaseType(this.workConf.getDatabaseType());
    tpccWorkConf.setUrl(this.workConf.getUrl());
    tpccWorkConf.setScaleFactor(this.workConf.getScaleFactor());
    tpccWorkConf.setTerminals(this.workConf.getTerminals());
    tpccWorkConf.setBatchSize(this.workConf.getBatchSize());
    // tpccWorkConf.setBenchmarkName(BenchmarkModule.convertBenchmarkClassToBenchmarkName(TPCCBenchmark.class));
    tpccWorkConf.setBenchmarkName(
        TPCCBenchmark.class.getSimpleName().toLowerCase().replace("benchmark", ""));

    this.tpccBenchmark = new TPCCBenchmark(this.workConf);
    conn = this.tpccBenchmark.makeConnection();
    assertNotNull(conn);
    this.tpccBenchmark.refreshCatalog();
    catalog = this.tpccBenchmark.getCatalog();
    assertNotNull(catalog);
  }

  protected void createDatabase() {
    try {
      this.setupTpccBenchmarkHelper();
      this.tpccBenchmark.createDatabase();
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      cleanupServer();
      fail("createDatabase() failed");
    }
  }

  protected void loadDatabase() {
    try {
      this.setupTpccBenchmarkHelper();
      this.tpccBenchmark.loadDatabase();
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      cleanupServer();
      fail("loadDatabase() failed");
    }
  }
}
