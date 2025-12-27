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

import jakarta.xml.bind.annotation.*;

/**
 * valueType 복합 유형을 위한 Java 클래스입니다.
 *
 * <p>아래 스키마 조각은 이 클래스에 포함될 내용을 명세합니다.
 *
 * <pre>
 * &lt;complexType name="valueType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *       &lt;attribute name="dist" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="min" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="seed" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "valueType",
    propOrder = {"value"})
public class ValueType {

  @XmlValue protected String value;

  @XmlAttribute(name = "dist")
  protected String dist;

  @XmlAttribute(name = "min")
  protected String min;

  @XmlAttribute(name = "max")
  protected String max;

  @XmlAttribute(name = "seed")
  protected String seed;

  /**
   * value 속성을 반환합니다.
   *
   * @return {@link String} 타입의 객체를 반환합니다.
   */
  public String getValue() {
    return value;
  }

  /**
   * value 속성을 설정합니다.
   *
   * @param value 허용되는 객체는 {@link String}입니다.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * dist 속성을 반환합니다.
   *
   * @return {@link String} 타입의 객체를 반환합니다.
   */
  public String getDist() {
    return dist;
  }

  /**
   * dist 속성을 설정합니다.
   *
   * @param value 허용되는 객체는 {@link String}입니다.
   */
  public void setDist(String value) {
    this.dist = value;
  }

  /**
   * min 속성을 반환합니다.
   *
   * @return {@link String} 타입의 객체를 반환합니다.
   */
  public String getMin() {
    return min;
  }

  /**
   * min 속성을 설정합니다.
   *
   * @param value 허용되는 객체는 {@link String}입니다.
   */
  public void setMin(String value) {
    this.min = value;
  }

  /**
   * max 속성을 반환합니다.
   *
   * @return {@link String} 타입의 객체를 반환합니다.
   */
  public String getMax() {
    return max;
  }

  /**
   * max 속성을 설정합니다.
   *
   * @param value 허용되는 객체는 {@link String}입니다.
   */
  public void setMax(String value) {
    this.max = value;
  }

  /**
   * seed 속성을 반환합니다.
   *
   * @return {@link String} 타입의 객체를 반환합니다.
   */
  public String getSeed() {
    return seed;
  }

  /**
   * seed 속성을 설정합니다.
   *
   * @param value 허용되는 객체는 {@link String}입니다.
   */
  public void setSeed(String value) {
    this.seed = value;
  }
}
