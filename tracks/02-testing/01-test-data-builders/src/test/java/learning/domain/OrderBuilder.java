package learning.domain;

import java.util.ArrayList;
import java.util.List;

public final class OrderBuilder {
    private String id = "o1";
    private Customer customer = CustomerBuilder.aCustomer().build();
    private final List<OrderItem> items = new ArrayList<>();

    private OrderBuilder() {
    }

    public static OrderBuilder anOrder() {
        return new OrderBuilder();
    }

    public OrderBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public OrderBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public OrderBuilder withCustomer(CustomerBuilder customerBuilder) {
        this.customer = customerBuilder.build();
        return this;
    }

    public OrderBuilder with(OrderItemBuilder orderItemBuilder) {
        this.items.add(orderItemBuilder.build());
        return this;
    }

    public Order build() {
        Order order = new Order(id, customer);
        items.forEach(order::addItem);
        return order;
    }
}
