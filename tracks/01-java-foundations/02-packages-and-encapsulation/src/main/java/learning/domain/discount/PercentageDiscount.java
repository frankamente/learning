package learning.domain.discount;

import learning.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

record PercentageDiscount(BigDecimal percentage) implements DiscountPolicy {
    @Override
    public Money apply(Money original) {
        BigDecimal amount = original.amount();
        Money moneyToSubtract = new Money(amount.multiply(percentage.divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP)), original.currency());
        return original.subtract(moneyToSubtract);
    }
}
