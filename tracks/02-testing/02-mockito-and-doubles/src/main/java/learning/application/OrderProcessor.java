package learning.application;

import learning.domain.Order;
import learning.domain.PaymentGateway;
import learning.domain.InventoryService;
import learning.domain.OrderRepository;

public class OrderProcessor {
    private final PaymentGateway paymentGateway;
    private final InventoryService inventoryService;
    private final OrderRepository orderRepository;

    public OrderProcessor(
            PaymentGateway paymentGateway,
            InventoryService inventoryService,
            OrderRepository orderRepository
    ) {
        this.paymentGateway = paymentGateway;
        this.inventoryService = inventoryService;
        this.orderRepository = orderRepository;
    }

    public boolean process(Order order) {
        // TODO: Implement according to business rules:
        // 1. Reserve inventory.
        // 2. Charge payment.
        // 3. Handle outcomes:
        //    - Payment success -> complete order, save to repo, return true.
        //    - Payment failure -> release inventory, cancel order, save to repo, return false.
        return false;
    }
}
