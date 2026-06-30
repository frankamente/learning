package learning.domain.discount;

import learning.Money;

record AbsoluteDiscount(Money amount) implements DiscountPolicy {
    @Override
    public Money apply(Money original) {
        return original.subtract(amount);
    }
}
