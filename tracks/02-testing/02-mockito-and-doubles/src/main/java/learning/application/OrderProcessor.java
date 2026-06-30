package learning.application;

import learning.domain.*;

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
        inventoryService.reserve(order);
        PaymentResult charge = paymentGateway.charge(order);
        return switch (charge) {
            case SUCCESS -> {
                order.complete();
                orderRepository.save(order);
                yield true;
            }
            case FAILURE -> {
                inventoryService.release(order);
                order.cancel();
                orderRepository.save(order);
                yield false;
            }
        };
    }
}
