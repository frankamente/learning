package learning.domain;

import learning.Money;
import java.math.BigDecimal;

public record OrderItem(String productName, Money price, int quantity) {
    public OrderItem {
        java.util.Objects.requireNonNull(productName, "Product name cannot be null");
        java.util.Objects.requireNonNull(price, "Price cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    public Money subtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
