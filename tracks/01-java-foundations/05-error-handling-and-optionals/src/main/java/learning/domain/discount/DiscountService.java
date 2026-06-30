package learning.domain.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import learning.Money;

public final class DiscountService {
    public Money applyDiscount(Money original, DiscountPolicy policy) {
        return switch (policy) {
            case NoDiscount n -> original;
            case PercentageDiscount p -> {
                BigDecimal discountRate = p.percentage().divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP);
                BigDecimal discountAmount = original.amount().multiply(discountRate);
                BigDecimal finalAmount = original.amount().subtract(discountAmount).max(BigDecimal.ZERO);
                yield new Money(finalAmount, original.currency());
            }
            case AbsoluteDiscount a -> {
                Money subtracted = original.subtract(a.amount());
                BigDecimal finalAmount = subtracted.amount().max(BigDecimal.ZERO);
                yield new Money(finalAmount, original.currency());
            }
        };
    }
}
