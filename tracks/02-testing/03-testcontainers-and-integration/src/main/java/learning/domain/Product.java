package learning.domain;

import java.util.Objects;

public record Product(String id, String name, Money price) {
    public Product {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(price, "Price cannot be null");
    }
}
