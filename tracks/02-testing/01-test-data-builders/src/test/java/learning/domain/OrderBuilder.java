package learning.domain;

import java.util.ArrayList;
import java.util.List;

public final class OrderBuilder {
    private String id = "o1";
    private Customer customer = CustomerBuilder.aCustomer().build();
    private List<OrderItem> items = new ArrayList<>();

    private OrderBuilder() {}

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

    // TODO: Write a withCustomer(CustomerBuilder customerBuilder) method for convenience
    // This allows doing: anOrder().withCustomer(aCustomer().withName("Alice"))

    // TODO: Write a method to add items to the order builder
    // This allows doing: anOrder().with(anOrderItem().withPrice(10 EUR))

    public Order build() {
        Order order = new Order(id, customer);
        // TODO: Add all items from the list to the order object
        return order;
    }
}
