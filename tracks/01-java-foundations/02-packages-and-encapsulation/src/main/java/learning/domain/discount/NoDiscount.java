package learning.domain.discount;

import learning.Money;

record NoDiscount() implements DiscountPolicy {
    @Override
    public Money apply(Money original) {
        return original;
    }
}
