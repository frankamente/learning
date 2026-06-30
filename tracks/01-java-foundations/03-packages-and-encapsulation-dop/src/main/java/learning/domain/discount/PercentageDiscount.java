package learning.domain.discount;

import java.math.BigDecimal;

record PercentageDiscount(BigDecimal percentage) implements DiscountPolicy {
}
