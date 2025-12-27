package com.oltpbenchmark.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link MonitorInfo}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableMonitorInfo.builder()}.
 */
@Generated(from = "MonitorInfo", generator = "Immutables")
@SuppressWarnings({"all"})
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
public final class ImmutableMonitorInfo implements MonitorInfo {
  private final int monitoringInterval;
  private final MonitorInfo.MonitoringType monitoringType;

  private ImmutableMonitorInfo(ImmutableMonitorInfo.Builder builder) {
    if (builder.monitoringIntervalIsSet()) {
      initShim.monitoringInterval(builder.monitoringInterval);
    }
    if (builder.monitoringType != null) {
      initShim.monitoringType(builder.monitoringType);
    }
    this.monitoringInterval = initShim.getMonitoringInterval();
    this.monitoringType = initShim.getMonitoringType();
    this.initShim = null;
  }

  private ImmutableMonitorInfo(int monitoringInterval, MonitorInfo.MonitoringType monitoringType) {
    this.monitoringInterval = monitoringInterval;
    this.monitoringType = monitoringType;
    this.initShim = null;
  }

  private static final byte STAGE_INITIALIZING = -1;
  private static final byte STAGE_UNINITIALIZED = 0;
  private static final byte STAGE_INITIALIZED = 1;
  private transient volatile InitShim initShim = new InitShim();

  @Generated(from = "MonitorInfo", generator = "Immutables")
  private final class InitShim {
    private byte monitoringIntervalBuildStage = STAGE_UNINITIALIZED;
    private int monitoringInterval;

    int getMonitoringInterval() {
      if (monitoringIntervalBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (monitoringIntervalBuildStage == STAGE_UNINITIALIZED) {
        monitoringIntervalBuildStage = STAGE_INITIALIZING;
        this.monitoringInterval = getMonitoringIntervalInitialize();
        monitoringIntervalBuildStage = STAGE_INITIALIZED;
      }
      return this.monitoringInterval;
    }

    void monitoringInterval(int monitoringInterval) {
      this.monitoringInterval = monitoringInterval;
      monitoringIntervalBuildStage = STAGE_INITIALIZED;
    }

    private byte monitoringTypeBuildStage = STAGE_UNINITIALIZED;
    private MonitorInfo.MonitoringType monitoringType;

    MonitorInfo.MonitoringType getMonitoringType() {
      if (monitoringTypeBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (monitoringTypeBuildStage == STAGE_UNINITIALIZED) {
        monitoringTypeBuildStage = STAGE_INITIALIZING;
        this.monitoringType = Objects.requireNonNull(getMonitoringTypeInitialize(), "monitoringType");
        monitoringTypeBuildStage = STAGE_INITIALIZED;
      }
      return this.monitoringType;
    }

    void monitoringType(MonitorInfo.MonitoringType monitoringType) {
      this.monitoringType = monitoringType;
      monitoringTypeBuildStage = STAGE_INITIALIZED;
    }

    private String formatInitCycleMessage() {
      List<String> attributes = new ArrayList<>();
      if (monitoringIntervalBuildStage == STAGE_INITIALIZING) attributes.add("monitoringInterval");
      if (monitoringTypeBuildStage == STAGE_INITIALIZING) attributes.add("monitoringType");
      return "Cannot build MonitorInfo, attribute initializers form cycle " + attributes;
    }
  }

  private int getMonitoringIntervalInitialize() {
    return MonitorInfo.super.getMonitoringInterval();
  }

  private MonitorInfo.MonitoringType getMonitoringTypeInitialize() {
    return MonitorInfo.super.getMonitoringType();
  }

  /**
   *모니터링 간격입니다. 
   */
  @Override
  public int getMonitoringInterval() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.getMonitoringInterval()
        : this.monitoringInterval;
  }

  /**
   *모니터링 유형입니다. 
   */
  @Override
  public MonitorInfo.MonitoringType getMonitoringType() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.getMonitoringType()
        : this.monitoringType;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link MonitorInfo#getMonitoringInterval() monitoringInterval} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for monitoringInterval
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableMonitorInfo withMonitoringInterval(int value) {
    if (this.monitoringInterval == value) return this;
    return new ImmutableMonitorInfo(value, this.monitoringType);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link MonitorInfo#getMonitoringType() monitoringType} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for monitoringType
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableMonitorInfo withMonitoringType(MonitorInfo.MonitoringType value) {
    MonitorInfo.MonitoringType newValue = Objects.requireNonNull(value, "monitoringType");
    if (this.monitoringType == newValue) return this;
    return new ImmutableMonitorInfo(this.monitoringInterval, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableMonitorInfo} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableMonitorInfo
        && equalTo(0, (ImmutableMonitorInfo) another);
  }

  private boolean equalTo(int synthetic, ImmutableMonitorInfo another) {
    return monitoringInterval == another.monitoringInterval
        && monitoringType.equals(another.monitoringType);
  }

  /**
   * Computes a hash code from attributes: {@code monitoringInterval}, {@code monitoringType}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + monitoringInterval;
    h += (h << 5) + monitoringType.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code MonitorInfo} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "MonitorInfo{"
        + "monitoringInterval=" + monitoringInterval
        + ", monitoringType=" + monitoringType
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link MonitorInfo} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable MonitorInfo instance
   */
  public static ImmutableMonitorInfo copyOf(MonitorInfo instance) {
    if (instance instanceof ImmutableMonitorInfo) {
      return (ImmutableMonitorInfo) instance;
    }
    return ImmutableMonitorInfo.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableMonitorInfo ImmutableMonitorInfo}.
   * <pre>
   * ImmutableMonitorInfo.builder()
   *    .monitoringInterval(int) // optional {@link MonitorInfo#getMonitoringInterval() monitoringInterval}
   *    .monitoringType(com.oltpbenchmark.util.MonitorInfo.MonitoringType) // optional {@link MonitorInfo#getMonitoringType() monitoringType}
   *    .build();
   * </pre>
   * @return A new ImmutableMonitorInfo builder
   */
  public static ImmutableMonitorInfo.Builder builder() {
    return new ImmutableMonitorInfo.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableMonitorInfo ImmutableMonitorInfo}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "MonitorInfo", generator = "Immutables")
  public static final class Builder {
    private static final long OPT_BIT_MONITORING_INTERVAL = 0x1L;
    private long optBits;

    private int monitoringInterval;
    private MonitorInfo.MonitoringType monitoringType;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code MonitorInfo} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(MonitorInfo instance) {
      Objects.requireNonNull(instance, "instance");
      this.monitoringInterval(instance.getMonitoringInterval());
      this.monitoringType(instance.getMonitoringType());
      return this;
    }

    /**
     * Initializes the value for the {@link MonitorInfo#getMonitoringInterval() monitoringInterval} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link MonitorInfo#getMonitoringInterval() monitoringInterval}.</em>
     * @param monitoringInterval The value for monitoringInterval 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder monitoringInterval(int monitoringInterval) {
      this.monitoringInterval = monitoringInterval;
      optBits |= OPT_BIT_MONITORING_INTERVAL;
      return this;
    }

    /**
     * Initializes the value for the {@link MonitorInfo#getMonitoringType() monitoringType} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link MonitorInfo#getMonitoringType() monitoringType}.</em>
     * @param monitoringType The value for monitoringType 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder monitoringType(MonitorInfo.MonitoringType monitoringType) {
      this.monitoringType = Objects.requireNonNull(monitoringType, "monitoringType");
      return this;
    }

    /**
     * Builds a new {@link ImmutableMonitorInfo ImmutableMonitorInfo}.
     * @return An immutable instance of MonitorInfo
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableMonitorInfo build() {
      return new ImmutableMonitorInfo(this);
    }

    private boolean monitoringIntervalIsSet() {
      return (optBits & OPT_BIT_MONITORING_INTERVAL) != 0;
    }
  }
}
