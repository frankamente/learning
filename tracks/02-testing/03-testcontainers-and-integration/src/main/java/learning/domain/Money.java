package learning.domain;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {
    public Money {
        java.util.Objects.requireNonNull(amount, "Amount cannot be null");
        java.util.Objects.requireNonNull(currency, "Currency cannot be null");
    }
}
