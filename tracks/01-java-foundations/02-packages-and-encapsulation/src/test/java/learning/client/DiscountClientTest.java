package learning.client;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import learning.Money;
import learning.domain.discount.DiscountPolicy;
import learning.domain.discount.DiscountFactory;
import learning.domain.discount.DiscountService;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountClientTest {
    private final DiscountService discountService = new DiscountService();

    @Test
    void shouldApplyNoDiscount() {
        Money original = new Money(BigDecimal.valueOf(100), "EUR");
        DiscountPolicy policy = DiscountFactory.noDiscount();

        Money result = discountService.applyDiscount(original, policy);

        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldApplyPercentageDiscount() {
        Money original = new Money(BigDecimal.valueOf(100), "EUR");
        DiscountPolicy policy = DiscountFactory.percentage(BigDecimal.TEN);

        Money result = discountService.applyDiscount(original, policy);

        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    void shouldApplyAbsoluteDiscount() {
        Money original = new Money(BigDecimal.valueOf(100), "EUR");
        Money discount = new Money(BigDecimal.TWO, "EUR");
        DiscountPolicy policy = DiscountFactory.absolute(discount);

        Money result = discountService.applyDiscount(original, policy);

        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(98));
    }
}
