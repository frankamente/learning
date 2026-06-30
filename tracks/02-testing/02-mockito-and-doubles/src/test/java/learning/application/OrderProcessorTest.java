package learning.application;

import learning.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static learning.domain.OrderBuilder.anOrder;
import static learning.domain.OrderItemBuilder.anOrderItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    @Mock
    private InventoryService inventoryService;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderProcessor orderProcessor;

    @Test
    void shouldFailImmediatelyWhenInventoryIsOutOfStock() {
        Order order = anOrder().build();
        willThrow(OutOfStockException.class).given(inventoryService).reserve(order);

        assertThatThrownBy(() -> orderProcessor.process(order))
                .isInstanceOf(OutOfStockException.class);

        then(paymentGateway).shouldHaveNoInteractions();
        then(orderRepository).should(never()).save(any());
    }

    @Test
    void shouldCompleteAndSaveOrderWhenPaymentSucceeds() {
        Order order = anOrder().with(anOrderItem()).build();
        given(paymentGateway.charge(order)).willReturn(PaymentResult.SUCCESS);

        boolean result = orderProcessor.process(order);

        assertThat(result).isTrue();
        assertThat(order.status()).isEqualByComparingTo(OrderStatus.COMPLETED);
        then(orderRepository).should().save(order);
    }

    @Test
    void shouldReleaseInventoryAndCancelOrderWhenPaymentFails() {
        Order order = anOrder().with(anOrderItem()).build();
        given(paymentGateway.charge(order)).willReturn(PaymentResult.FAILURE);

        boolean result = orderProcessor.process(order);

        assertThat(result).isFalse();
        assertThat(order.status()).isEqualByComparingTo(OrderStatus.CANCELLED);
        then(orderRepository).should().save(order);
        then(inventoryService).should().release(order);
    }

    @Test
    void shouldProcessOrderSuccessfullyUsingFakeRepository() {
        FakeOrderRepository fakeOrderRepository = new FakeOrderRepository();
        OrderProcessor orderProcessorWithFakeRepository = new OrderProcessor(paymentGateway, inventoryService, fakeOrderRepository);
        Order order = anOrder().with(anOrderItem()).build();

        given(paymentGateway.charge(order)).willReturn(PaymentResult.SUCCESS);

        boolean result = orderProcessorWithFakeRepository.process(order);

        assertThat(result).isTrue();
        assertThat(order.status()).isEqualByComparingTo(OrderStatus.COMPLETED);
        assertThat(fakeOrderRepository.getSavedOrders()).contains(order);
    }
}
