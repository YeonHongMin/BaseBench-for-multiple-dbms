package com.oltpbenchmark.api.collectors.monitoring;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link DatabaseMonitor.RepeatedSystemEvent}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableRepeatedSystemEvent.builder()}.
 */
@Generated(from = "DatabaseMonitor.RepeatedSystemEvent", generator = "Immutables")
@SuppressWarnings({"all"})
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
public final class ImmutableRepeatedSystemEvent
    implements DatabaseMonitor.RepeatedSystemEvent {
  private final Instant instant;
  private final Map<String, String> propertyValues;

  private ImmutableRepeatedSystemEvent(Instant instant, Map<String, String> propertyValues) {
    this.instant = instant;
    this.propertyValues = propertyValues;
  }

  /**
   *The timestamp at which this event was observed. 
   */
  @Override
  public Instant getInstant() {
    return instant;
  }

  /**
   *Mapping of observed properties to their corresponding values. 
   */
  @Override
  public Map<String, String> getPropertyValues() {
    return propertyValues;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link DatabaseMonitor.RepeatedSystemEvent#getInstant() instant} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for instant
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableRepeatedSystemEvent withInstant(Instant value) {
    if (this.instant == value) return this;
    Instant newValue = Objects.requireNonNull(value, "instant");
    return new ImmutableRepeatedSystemEvent(newValue, this.propertyValues);
  }

  /**
   * Copy the current immutable object by replacing the {@link DatabaseMonitor.RepeatedSystemEvent#getPropertyValues() propertyValues} map with the specified map.
   * Nulls are not permitted as keys or values.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param entries The entries to be added to the propertyValues map
   * @return A modified copy of {@code this} object
   */
  public final ImmutableRepeatedSystemEvent withPropertyValues(Map<String, ? extends String> entries) {
    if (this.propertyValues == entries) return this;
    Map<String, String> newValue = createUnmodifiableMap(true, false, entries);
    return new ImmutableRepeatedSystemEvent(this.instant, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableRepeatedSystemEvent} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableRepeatedSystemEvent
        && equalTo(0, (ImmutableRepeatedSystemEvent) another);
  }

  private boolean equalTo(int synthetic, ImmutableRepeatedSystemEvent another) {
    return instant.equals(another.instant)
        && propertyValues.equals(another.propertyValues);
  }

  /**
   * Computes a hash code from attributes: {@code instant}, {@code propertyValues}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + instant.hashCode();
    h += (h << 5) + propertyValues.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code RepeatedSystemEvent} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "RepeatedSystemEvent{"
        + "instant=" + instant
        + ", propertyValues=" + propertyValues
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link DatabaseMonitor.RepeatedSystemEvent} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable RepeatedSystemEvent instance
   */
  public static ImmutableRepeatedSystemEvent copyOf(DatabaseMonitor.RepeatedSystemEvent instance) {
    if (instance instanceof ImmutableRepeatedSystemEvent) {
      return (ImmutableRepeatedSystemEvent) instance;
    }
    return ImmutableRepeatedSystemEvent.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableRepeatedSystemEvent ImmutableRepeatedSystemEvent}.
   * <pre>
   * ImmutableRepeatedSystemEvent.builder()
   *    .instant(java.time.Instant) // required {@link DatabaseMonitor.RepeatedSystemEvent#getInstant() instant}
   *    .putPropertyValues|putAllPropertyValues(String =&gt; String) // {@link DatabaseMonitor.RepeatedSystemEvent#getPropertyValues() propertyValues} mappings
   *    .build();
   * </pre>
   * @return A new ImmutableRepeatedSystemEvent builder
   */
  public static ImmutableRepeatedSystemEvent.Builder builder() {
    return new ImmutableRepeatedSystemEvent.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableRepeatedSystemEvent ImmutableRepeatedSystemEvent}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "DatabaseMonitor.RepeatedSystemEvent", generator = "Immutables")
  public static final class Builder {
    private static final long INIT_BIT_INSTANT = 0x1L;
    private long initBits = 0x1L;

    private Instant instant;
    private Map<String, String> propertyValues = new LinkedHashMap<String, String>();

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code RepeatedSystemEvent} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * Collection elements and entries will be added, not replaced.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(DatabaseMonitor.RepeatedSystemEvent instance) {
      Objects.requireNonNull(instance, "instance");
      this.instant(instance.getInstant());
      putAllPropertyValues(instance.getPropertyValues());
      return this;
    }

    /**
     * Initializes the value for the {@link DatabaseMonitor.RepeatedSystemEvent#getInstant() instant} attribute.
     * @param instant The value for instant 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder instant(Instant instant) {
      this.instant = Objects.requireNonNull(instant, "instant");
      initBits &= ~INIT_BIT_INSTANT;
      return this;
    }

    /**
     * Put one entry to the {@link DatabaseMonitor.RepeatedSystemEvent#getPropertyValues() propertyValues} map.
     * @param key The key in the propertyValues map
     * @param value The associated value in the propertyValues map
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder putPropertyValues(String key, String value) {
      this.propertyValues.put(
          Objects.requireNonNull(key, "propertyValues key"),
          Objects.requireNonNull(value, value == null ? "propertyValues value for key: " + key : null));
      return this;
    }

    /**
     * Put one entry to the {@link DatabaseMonitor.RepeatedSystemEvent#getPropertyValues() propertyValues} map. Nulls are not permitted
     * @param entry The key and value entry
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder putPropertyValues(Map.Entry<String, ? extends String> entry) {
      String k = entry.getKey();
      String v = entry.getValue();
      this.propertyValues.put(
          Objects.requireNonNull(k, "propertyValues key"),
          Objects.requireNonNull(v, v == null ? "propertyValues value for key: " + k : null));
      return this;
    }

    /**
     * Sets or replaces all mappings from the specified map as entries for the {@link DatabaseMonitor.RepeatedSystemEvent#getPropertyValues() propertyValues} map. Nulls are not permitted
     * @param entries The entries that will be added to the propertyValues map
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder propertyValues(Map<String, ? extends String> entries) {
      this.propertyValues.clear();
      return putAllPropertyValues(entries);
    }

    /**
     * Put all mappings from the specified map as entries to {@link DatabaseMonitor.RepeatedSystemEvent#getPropertyValues() propertyValues} map. Nulls are not permitted
     * @param entries The entries that will be added to the propertyValues map
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder putAllPropertyValues(Map<String, ? extends String> entries) {
      for (Map.Entry<String, ? extends String> e : entries.entrySet()) {
        String k = e.getKey();
        String v = e.getValue();
        this.propertyValues.put(
            Objects.requireNonNull(k, "propertyValues key"),
            Objects.requireNonNull(v, v == null ? "propertyValues value for key: " + k : null));
      }
      return this;
    }

    /**
     * Builds a new {@link ImmutableRepeatedSystemEvent ImmutableRepeatedSystemEvent}.
     * @return An immutable instance of RepeatedSystemEvent
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableRepeatedSystemEvent build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableRepeatedSystemEvent(instant, createUnmodifiableMap(false, false, propertyValues));
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_INSTANT) != 0) attributes.add("instant");
      return "Cannot build RepeatedSystemEvent, some of required attributes are not set " + attributes;
    }
  }

  private static <K, V> Map<K, V> createUnmodifiableMap(boolean checkNulls, boolean skipNulls, Map<? extends K, ? extends V> map) {
    switch (map.size()) {
    case 0: return Collections.emptyMap();
    case 1: {
      Map.Entry<? extends K, ? extends V> e = map.entrySet().iterator().next();
      K k = e.getKey();
      V v = e.getValue();
      if (checkNulls) {
        Objects.requireNonNull(k, "key");
        Objects.requireNonNull(v, v == null ? "value for key: " + k : null);
      }
      if (skipNulls && (k == null || v == null)) {
        return Collections.emptyMap();
      }
      return Collections.singletonMap(k, v);
    }
    default: {
      Map<K, V> linkedMap = new LinkedHashMap<>(map.size() * 4 / 3 + 1);
      if (skipNulls || checkNulls) {
        for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
          K k = e.getKey();
          V v = e.getValue();
          if (skipNulls) {
            if (k == null || v == null) continue;
          } else if (checkNulls) {
            Objects.requireNonNull(k, "key");
            Objects.requireNonNull(v, v == null ? "value for key: " + k : null);
          }
          linkedMap.put(k, v);
        }
      } else {
        linkedMap.putAll(map);
      }
      return Collections.unmodifiableMap(linkedMap);
    }
    }
  }
}
