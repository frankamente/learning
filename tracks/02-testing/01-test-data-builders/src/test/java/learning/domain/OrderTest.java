package learning.domain;

import learning.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static learning.domain.CustomerBuilder.aCustomer;
import static learning.domain.OrderBuilder.anOrder;
import static learning.domain.OrderItemBuilder.anOrderItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void shouldCalculateTotalWithoutDiscountForOrdersUnderOneHundred() {
        Order order = anOrder()
                .with(anOrderItem().withPrice(new Money(BigDecimal.valueOf(40), "EUR")).withQuantity(2))
                .build();

        Money total = order.total();

        assertThat(total.amount()).isEqualByComparingTo(BigDecimal.valueOf(80));
    }

    @Test
    void shouldCalculateTotalWithTenPercentDiscountForOrdersOverOneHundred() {
        Order order = anOrder()
                .with(anOrderItem().withPrice(new Money(BigDecimal.valueOf(60), "EUR")).withQuantity(2))
                .build();

        Money total = order.total();

        // 120 EUR - 10% (12 EUR) = 108 EUR
        assertThat(total.amount()).isEqualByComparingTo(BigDecimal.valueOf(108));
    }

    @Test
    void shouldReturnZeroEurTotalWhenOrderIsEmpty() {
        Order order = anOrder().build();

        Money total = order.total();

        assertThat(total.amount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(total.currency()).isEqualTo("EUR");
    }

    @Test
    void shouldFailToCompleteEmptyOrder() {
        Order order = anOrder().build();

        assertThatThrownBy(() -> order.complete())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("empty");
    }

    @Test
    void shouldFailToCompleteOrderWhenCustomerEmailIsInvalid() {
        Order order = anOrder()
                .withCustomer(aCustomer().withEmail("invalidemail"))
                .with(anOrderItem())
                .build();

        assertThatThrownBy(() -> order.complete())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email");
    }

    @Test
    void shouldCompleteOrderSuccessfullyWhenValid() {
        Order order = anOrder()
                .with(anOrderItem())
                .build();

        order.complete();

        assertThat(order.status()).isEqualTo(OrderStatus.COMPLETED);
    }
}
