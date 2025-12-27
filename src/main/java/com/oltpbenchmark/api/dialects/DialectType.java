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

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * dialectType 복합 타입에 대한 Java 클래스입니다.
 *
 * <p>다음 스키마 조각은 이 클래스에 포함될 것으로 예상되는 내용을 지정합니다.
 *
 * <pre>
 * &lt;complexType name="dialectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="procedure" type="{}procedureType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "dialectType",
    propOrder = {"procedure"})
public class DialectType {

  @XmlElement(required = true)
  protected List<ProcedureType> procedure;

  @XmlAttribute(required = true)
  protected String type;

  /**
   * procedure 속성의 값을 가져옵니다.
   *
   * <p>이 접근자 메서드는 스냅샷이 아닌 라이브 목록에 대한 참조를 반환합니다. 따라서 반환된 목록에
   * 수행하는 모든 수정 사항은 JAXB 객체 내부에 반영됩니다. 이것이 procedure 속성에 대해
   * <CODE>set</CODE> 메서드가 없는 이유입니다.
   *
   * <p>예를 들어, 새 항목을 추가하려면 다음과 같이 수행합니다:
   *
   * <pre>
   *    getProcedure().add(newItem);
   * </pre>
   *
   * <p>다음 타입의 객체가 목록에 허용됩니다: {@link ProcedureType }
   */
  public List<ProcedureType> getProcedure() {
    if (procedure == null) {
      procedure = new ArrayList<>();
    }
    return this.procedure;
  }

  /**
   * type 속성의 값을 가져옵니다.
   *
   * @return 가능한 객체는 {@link String }입니다.
   */
  public String getType() {
    return type;
  }

  /**
   * type 속성의 값을 설정합니다.
   *
   * @param value 허용되는 객체는 {@link String }입니다.
   */
  public void setType(String value) {
    this.type = value;
  }
}
