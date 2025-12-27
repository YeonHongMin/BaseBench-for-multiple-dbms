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

//
// 이 파일은 JavaTM Architecture for XML Binding(JAXB) Reference
// Implementation, vJAXB 2.1.10에 의해 생성되었습니다.
// 자세한 내용은 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>를 참조하십시오.
// 이 파일에 대한 수정 사항은 소스 스키마를 다시 컴파일하면 손실됩니다.
// 생성일: 2011.12.28 오후 11:42:38 EST
//

package com.oltpbenchmark.api.dialects;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * 이 객체는 com.oltpbenchmark.api.dialects 패키지에서 생성된 각 Java 콘텐츠 인터페이스 및
 * Java 요소 인터페이스에 대한 팩토리 메서드를 포함합니다.
 *
 * <p>ObjectFactory를 사용하면 XML 콘텐츠에 대한 Java 표현의 새 인스턴스를 프로그래밍 방식으로
 * 생성할 수 있습니다. XML 콘텐츠의 Java 표현은 스키마 타입 정의, 요소 선언 및 모델 그룹의
 * 바인딩을 나타내는 스키마 파생 인터페이스 및 클래스로 구성될 수 있습니다. 각각에 대한 팩토리
 * 메서드는 이 클래스에서 제공됩니다.
 */
@XmlRegistry
public class ObjectFactory {

  private static final QName _Dialects_QNAME = new QName("", "dialects");

  /**
   * 패키지 com.oltpbenchmark.api.dialects에 대한 스키마 파생 클래스의 새 인스턴스를 생성하는 데
   * 사용할 수 있는 새 ObjectFactory를 생성합니다.
   */
  public ObjectFactory() {}

  /** {@link DialectType }의 인스턴스를 생성합니다. */
  public DialectType createDialectType() {
    return new DialectType();
  }

  /** {@link StatementType }의 인스턴스를 생성합니다. */
  public StatementType createStatementType() {
    return new StatementType();
  }

  /** {@link ProcedureType }의 인스턴스를 생성합니다. */
  public ProcedureType createProcedureType() {
    return new ProcedureType();
  }

  /** {@link DialectsType }의 인스턴스를 생성합니다. */
  public DialectsType createDialectsType() {
    return new DialectsType();
  }

  /** {@link JAXBElement }{@code <}{@link DialectsType }{@code >}의 인스턴스를 생성합니다. */
  @XmlElementDecl(namespace = "", name = "dialects")
  public JAXBElement<DialectsType> createDialects(DialectsType value) {
    return new JAXBElement<>(_Dialects_QNAME, DialectsType.class, null, value);
  }
}
