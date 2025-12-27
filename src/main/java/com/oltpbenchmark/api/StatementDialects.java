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

package com.oltpbenchmark.api;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.dialects.*;
import com.oltpbenchmark.types.DatabaseType;
import jakarta.xml.bind.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.Map.Entry;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author pavlo
 */
public final class StatementDialects {
  private static final Logger LOG = LoggerFactory.getLogger(StatementDialects.class);

  private static final DatabaseType DEFAULT_DB_TYPE = DatabaseType.MYSQL;

  private final WorkloadConfiguration workloadConfiguration;

  /** ProcName -> StmtName -> SQL */
  private final Map<String, Map<String, String>> dialectsMap = new HashMap<>();

  /**
   * 생성자
   *
   * @param workloadConfiguration
   */
  public StatementDialects(WorkloadConfiguration workloadConfiguration) {
    this.workloadConfiguration = workloadConfiguration;

    try {
      this.load();
    } catch (JAXBException | SAXException e) {
      throw new RuntimeException(String.format("Error loading dialect: %s", e.getMessage()), e);
    }
  }

  /**
   * 이 벤치마크에 사용되는 SQL Dialect XML 파일에 대한 File 핸들을 반환합니다.
   *
   * @return
   */
  public String getSQLDialectPath(DatabaseType databaseType) {
    String fileName = null;

    if (databaseType != null) {
      fileName = "dialect-" + databaseType.name().toLowerCase() + ".xml";
    }

    if (fileName != null) {

      final String path =
          "/benchmarks/" + workloadConfiguration.getBenchmarkName() + "/" + fileName;

      try (InputStream stream = this.getClass().getResourceAsStream(path)) {

        if (stream != null) {
          return path;
        }

      } catch (IOException e) {
        LOG.error(e.getMessage(), e);
      }

      LOG.debug("No dialect file in {}", path);
    }

    return (null);
  }

  /**
   * Load in the assigned XML file and populate the internal dialects map
   *
   * @return
   */
  protected boolean load() throws JAXBException, SAXException {
    final DatabaseType dbType = workloadConfiguration.getDatabaseType();

    final String sqlDialectPath = getSQLDialectPath(dbType);

    if (sqlDialectPath == null) {
      LOG.debug("SKIP - No SQL dialect file was given.");
      return (false);
    }

    final String xmlContext = this.getClass().getPackage().getName() + ".dialects";

    // COPIED FROM VoltDB's VoltCompiler.java
    JAXBContext jc = JAXBContext.newInstance(xmlContext);
    // This schema shot the sheriff.
    SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema =
        sf.newSchema(new StreamSource(this.getClass().getResourceAsStream("/dialect.xsd")));
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    // But did not shoot unmarshaller!
    unmarshaller.setSchema(schema);

    StreamSource streamSource =
        new StreamSource(this.getClass().getResourceAsStream(sqlDialectPath));
    JAXBElement<DialectsType> result = unmarshaller.unmarshal(streamSource, DialectsType.class);
    DialectsType dialects = result.getValue();

    if (LOG.isDebugEnabled()) {
      LOG.debug("Loading the SQL dialect file for path {}", sqlDialectPath);
    }

    for (DialectType dialect : dialects.getDialect()) {

      if (!dialect.getType().equalsIgnoreCase(dbType.name())) {
        continue;
      }

      // For each Procedure in the XML file, go through its list of Statements
      // and populate our dialects map with the mapped SQL
      for (ProcedureType procedure : dialect.getProcedure()) {
        String procName = procedure.getName();

        // Loop through all of the Statements listed for this Procedure
        Map<String, String> procDialects = this.dialectsMap.get(procName);
        for (StatementType statement : procedure.getStatement()) {
          String stmtName = statement.getName();
          String stmtSQL = statement.getValue().trim();
          if (procDialects == null) {
            procDialects = new HashMap<>();
            this.dialectsMap.put(procName, procDialects);
          }
          procDialects.put(stmtName, stmtSQL);
          LOG.debug(String.format("%s.%s.%s\n%s\n", dbType, procName, stmtName, stmtSQL));
        }
      }
    }
    if (this.dialectsMap.isEmpty()) {
      LOG.warn(
          String.format(
              "No SQL dialect provided for %s. Using default %s", dbType, DEFAULT_DB_TYPE));
      return (false);
    }

    return (true);
  }

  /**
   * Export the original SQL for all of the SQLStmt in the given list of Procedures
   *
   * @param dbType
   * @param procedures
   * @return A well-formed XML export of the SQL for the given Procedures
   */
  public String export(DatabaseType dbType, Collection<Procedure> procedures) {

    Marshaller marshaller = null;
    JAXBContext jc = null;

    final String xmlContext = this.getClass().getPackage().getName() + ".dialects";

    try {
      jc = JAXBContext.newInstance(xmlContext);
      marshaller = jc.createMarshaller();

      SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema =
          sf.newSchema(new StreamSource(this.getClass().getResourceAsStream("/dialect.xsd")));
      marshaller.setSchema(schema);
    } catch (Exception ex) {
      throw new RuntimeException("Unable to initialize serializer", ex);
    }

    List<Procedure> sorted = new ArrayList<>(procedures);
    sorted.sort(
        new Comparator<Procedure>() {
          @Override
          public int compare(Procedure o1, Procedure o2) {
            return (o1.getProcedureName().compareTo(o2.getProcedureName()));
          }
        });

    ObjectFactory factory = new ObjectFactory();
    DialectType dType = factory.createDialectType();
    dType.setType(dbType.name());
    for (Procedure proc : sorted) {
      if (proc.getStatements().isEmpty()) {
        continue;
      }

      ProcedureType pType = factory.createProcedureType();
      pType.setName(proc.getProcedureName());
      for (Entry<String, SQLStmt> e : proc.getStatements().entrySet()) {
        StatementType sType = factory.createStatementType();
        sType.setName(e.getKey());
        sType.setValue(e.getValue().getOriginalSQL());
        pType.getStatement().add(sType);
      }
      dType.getProcedure().add(pType);
    }
    DialectsType dialects = factory.createDialectsType();
    dialects.getDialect().add(dType);

    StringWriter st = new StringWriter();
    try {
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(factory.createDialects(dialects), st);
    } catch (JAXBException ex) {
      throw new RuntimeException("Failed to generate XML", ex);
    }

    return (st.toString());
  }

  /**
   * Return the DatabaseType loaded from the XML file
   *
   * @return
   */
  public DatabaseType getDatabaseType() {
    return workloadConfiguration.getDatabaseType();
  }

  /**
   * Return the list of Statement names that we have dialect information for the given Procedure
   * name. If there are SQL dialects for the given Procedure, then the result will be null.
   *
   * @param procName
   * @return
   */
  protected Collection<String> getStatementNames(String procName) {
    Map<String, String> procDialects = this.dialectsMap.get(procName);
    return (procDialects != null ? procDialects.keySet() : null);
  }

  /**
   * Return the SQL dialect for the given Statement in the Procedure
   *
   * @param procName
   * @param stmtName
   * @return
   */
  public String getSQL(String procName, String stmtName) {
    Map<String, String> procDialects = this.dialectsMap.get(procName);
    if (procDialects != null) {
      return (procDialects.get(stmtName));
    }
    return (null);
  }

  /**
   * @return The list of Procedure names that we have dialect information for.
   */
  protected Collection<String> getProcedureNames() {
    return (this.dialectsMap.keySet());
  }
}
