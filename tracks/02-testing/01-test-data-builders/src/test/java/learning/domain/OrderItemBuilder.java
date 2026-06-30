package learning.domain;

import learning.Money;
import java.math.BigDecimal;

public final class OrderItemBuilder {
    // TODO: Define fields with default values:
    // - productName = "Keyboard"
    // - price = new Money(BigDecimal.valueOf(20), "EUR")
    // - quantity = 1

    private OrderItemBuilder() {}

    public static OrderItemBuilder anOrderItem() {
        return new OrderItemBuilder();
    }

    // TODO: Write chainable withProductName, withPrice, and withQuantity methods.

    public OrderItem build() {
        // TODO: Return a new OrderItem using the builder's fields
        return null;
    }
}
