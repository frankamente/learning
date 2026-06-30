package learning.domain;

import java.util.ArrayList;
import java.util.List;

public class FakeOrderRepository implements OrderRepository {
    private final List<Order> savedOrders = new ArrayList<>();

    @Override
    public void save(Order order) {
        savedOrders.add(order);
    }

    public List<Order> getSavedOrders() {
        return savedOrders;
    }
}
