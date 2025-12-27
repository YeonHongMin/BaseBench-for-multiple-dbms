/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * 이 파일은 Apache License, Version 2.0("라이선스")에 따라 배포됩니다.
 * 라이선스 조건을 준수하지 않으면 이 파일을 사용할 수 없습니다.
 * 라이선스 전문은 다음 주소에서 확인할 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 관련법이나 서면 합의가 없으면 이 소프트웨어는 "있는 그대로" 제공되며,
 * 명시적/묵시적 보증 없이 배포됩니다.
 * 라이선스에서 허용된 제한 및 조건을 준수해 주세요.
 *
 */

//
// 이 파일은 JavaTM XML 바인딩 아키텍처(JAXB) 참조 구현 v2.3.0.1에 의해 생성되었습니다.
// <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a>를 참고하십시오.
// 원본 스키마를 다시 컴파일하면 여기 수정한 내용은 모두 사라집니다.
// 생성 일자: 2023.11.16 UTC 오전 08:29:59
//

package com.oltpbenchmark.api.templates;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * main.java.com.oltpbenchmark.api.templates 패키지에서 생성된 Java 콘텐츠/엘리먼트 인터페이스를 위한 팩터리 메서드를 담고 있습니다.
 *
 * <p>ObjectFactory는 XML 콘텐츠에 대응하는 Java 표현을 프로그래밍 방식으로 생성할 수 있게 합니다. XML 표현에는 스키마 타입 정의, 엘리먼트 선언, 모델
 * 그룹에 대응하는 인터페이스와 클래스가 포함됩니다. 이 클래스는 그런 항목들을 생성하는 메서드를 제공합니다.
 */
@XmlRegistry
public class ObjectFactory {

  private static final QName _Templates_QNAME = new QName("", "templates");

  /**
   * main.java.com.oltpbenchmark.api.templates 패키지의 스키마 유도 클래스 인스턴스를 만들기 위한 ObjectFactory를 생성합니다.
   */
  public ObjectFactory() {}

  /** {@link TemplatesType} 인스턴스를 생성합니다. */
  public TemplatesType createTemplatesType() {
    return new TemplatesType();
  }

  /** {@link TemplateType} 인스턴스를 생성합니다. */
  public TemplateType createTemplateType() {
    return new TemplateType();
  }

  /** {@link TypesType} 인스턴스를 생성합니다. */
  public TypesType createTypesType() {
    return new TypesType();
  }

  /** {@link ValuesType} 인스턴스를 생성합니다. */
  public ValuesType createValuesType() {
    return new ValuesType();
  }

  /** {@link ValueType} 인스턴스를 생성합니다. */
  public ValueType createValueType() {
    return new ValueType();
  }

  /**
   * {@link JAXBElement }{@code <}{@link TemplatesType }{@code >} 인스턴스를 생성합니다.
   *
   * @param value XML 엘리먼트 값을 나타내는 Java 객체입니다.
   * @return 새로운 {@link JAXBElement }{@code <}{@link TemplatesType }{@code >} 인스턴스
   */
  @XmlElementDecl(namespace = "", name = "templates")
  public JAXBElement<TemplatesType> createTemplates(TemplatesType value) {
    return new JAXBElement<TemplatesType>(_Templates_QNAME, TemplatesType.class, null, value);
  }
}
