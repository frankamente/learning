package learning.domain;

import learning.Money;
import java.math.BigDecimal;

public final class OrderItemBuilder {
    private String productName = "Keyboard";
    private Money price = new Money(BigDecimal.valueOf(20), "EUR");
    private int quantity = 1;

    private OrderItemBuilder() {}

    public static OrderItemBuilder anOrderItem() {
        return new OrderItemBuilder();
    }

    public OrderItemBuilder withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public OrderItemBuilder withPrice(Money price) {
        this.price = price;
        return this;
    }

    public OrderItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderItem build() {
        return new OrderItem(productName, price, quantity);
    }
}
