package learning.client;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import learning.Money;
import learning.domain.discount.DiscountPolicy;
import learning.domain.discount.DiscountFactory;
import learning.domain.discount.DiscountService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void shouldThrowExceptionOnCurrencyMismatch() {
        Money original = new Money(BigDecimal.valueOf(100), "EUR");
        Money discount = new Money(BigDecimal.TWO, "USD"); // Mismatch
        DiscountPolicy policy = DiscountFactory.absolute(discount);

        assertThatThrownBy(() -> discountService.applyDiscount(original, policy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(Money.CURRENCY_NOT_THE_SAME_MESSAGE);
    }

    @Test
    void shouldCapAtZeroWhenAbsoluteDiscountExceedsPrice() {
        Money original = new Money(BigDecimal.valueOf(10), "EUR");
        Money discount = new Money(BigDecimal.valueOf(15), "EUR"); // Exceeds
        DiscountPolicy policy = DiscountFactory.absolute(discount);

        Money result = discountService.applyDiscount(original, policy);

        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
