package learning.domain;

import learning.Money;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final String id;
    private final Customer customer;
    private final List<OrderItem> items = new ArrayList<>();
    private OrderStatus status;

    public Order(String id, Customer customer) {
        this.id = java.util.Objects.requireNonNull(id, "Order ID cannot be null");
        this.customer = java.util.Objects.requireNonNull(customer, "Customer cannot be null");
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
        // TODO: Sum all items' subtotals.
        // If total price exceeds 100 EUR, apply a 10% discount.
        return null;
    }

    public void complete() {
        // TODO: Enforce invariants before completing:
        // 1. Order cannot be completed if it contains no items.
        // 2. Customer must have a valid email.
        // If valid, change status to COMPLETED.
    }
}
