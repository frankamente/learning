package learning.domain.discount;

import java.math.BigDecimal;

public record PercentageDiscount(BigDecimal percentage) implements DiscountPolicy {
}
