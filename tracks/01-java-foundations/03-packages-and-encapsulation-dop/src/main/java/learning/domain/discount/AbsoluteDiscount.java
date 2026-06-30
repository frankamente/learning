package learning.domain.discount;

import learning.Money;

record AbsoluteDiscount(Money amount) implements DiscountPolicy {
}
