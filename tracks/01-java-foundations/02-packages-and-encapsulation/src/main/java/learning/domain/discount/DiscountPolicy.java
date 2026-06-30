package learning.domain.discount;

import learning.Money;

public sealed interface DiscountPolicy permits PercentageDiscount, AbsoluteDiscount, NoDiscount {

    Money apply(Money original);
}
