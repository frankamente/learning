package learning.domain;

import learning.domain.Money;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {
    private final String id;
    private final Customer customer;
    private final List<OrderItem> items = new ArrayList<>();
    private OrderStatus status;

    public Order(String id, Customer customer) {
        this.id = Objects.requireNonNull(id, "Order ID cannot be null");
        this.customer = Objects.requireNonNull(customer, "Customer cannot be null");
        this.status = OrderStatus.DRAFT;
    }

    public String id() {
        return id;
    }

    public Customer customer() {
        return customer;
    }

    public List<OrderItem> items() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus status() {
        return status;
    }

    public void addItem(OrderItem item) {
        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Cannot add items to a " + status + " order");
        }
        items.add(item);
    }

    public Money total() {
        if (items.isEmpty()) {
            return new Money(BigDecimal.ZERO, "EUR");
        }
        Money total = new Money(BigDecimal.ZERO, items.getFirst().price().currency());
        for (OrderItem item : items()) {
            total = total.add(item.subtotal());
        }

        if (total.amount().compareTo(BigDecimal.valueOf(100)) > 0) {
            total = total.multiply(new BigDecimal("0.90"));
        }
        return total;
    }

    public void complete() {
        if (items().isEmpty()) {
            throw new IllegalStateException("empty");
        }
        if (!customer().hasValidEmail()) {
            throw new IllegalStateException("email");
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if (this.status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed order");
        }
        this.status = OrderStatus.CANCELLED;
    }
}
