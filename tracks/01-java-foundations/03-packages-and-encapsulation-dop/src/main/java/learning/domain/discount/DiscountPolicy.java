package learning.domain.discount;

public sealed interface DiscountPolicy permits PercentageDiscount, AbsoluteDiscount, NoDiscount {
}
