package learning.domain;

import learning.Money;
import java.math.BigDecimal;

public final class OrderItemBuilder {

    private String productName;
    private Money price;
    private int quantity;

    private OrderItemBuilder() {
        productName = "Keyboard";
        price = new Money(BigDecimal.valueOf(20), "EUR");
        quantity = 1;
    }

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
