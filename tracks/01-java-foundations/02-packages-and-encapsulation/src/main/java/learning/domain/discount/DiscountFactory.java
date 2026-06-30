package learning.domain.discount;

import java.math.BigDecimal;
import learning.Money;

public final class DiscountFactory {
    private DiscountFactory() {}

    public static DiscountPolicy noDiscount() {
        return new NoDiscount();
    }

    public static DiscountPolicy percentage(BigDecimal percentage) {
        return new PercentageDiscount(percentage);
    }

    public static DiscountPolicy absolute(Money amount) {
        return new AbsoluteDiscount(amount);
    }
}
