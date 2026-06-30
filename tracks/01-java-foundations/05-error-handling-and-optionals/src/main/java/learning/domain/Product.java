package learning.domain;

import learning.Money;

public record Product(String id, String name, Money price) implements Entity<String> {
}
