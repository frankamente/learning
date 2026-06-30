package learning.domain;

public record Product(String id, String name, Money price) {
    public Product {
        java.util.Objects.requireNonNull(id, "ID cannot be null");
        java.util.Objects.requireNonNull(name, "Name cannot be null");
        java.util.Objects.requireNonNull(price, "Price cannot be null");
    }
}
