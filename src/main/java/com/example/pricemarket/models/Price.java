package com.example.pricemarket.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Price {
    private Instant timestamp;
    private BigDecimal value;

    public Price(BigDecimal value) {
        this.timestamp = Instant.now();
        this.value = value;
    }

    public Price() {
        this.timestamp = Instant.now();
    }

    // For testing only.
    public Price(Instant timestamp, BigDecimal value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Price{"
                + " timeStamp=" + timestamp
                + ", value=" + value
                + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimestamp(), getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price = (Price) o;
        return Objects.equals(getTimestamp(), price.getTimestamp())
                && Objects.equals(getValue(), price.getValue());
    }

}
