package learning.domain.discount;

import learning.Money;

public record AbsoluteDiscount(Money amount) implements DiscountPolicy {
}
