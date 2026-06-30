package learning.domain.discount;

import learning.Money;

public final class DiscountService {
    public Money applyDiscount(Money original, DiscountPolicy policy) {
        return policy.apply(original);
    }
}
