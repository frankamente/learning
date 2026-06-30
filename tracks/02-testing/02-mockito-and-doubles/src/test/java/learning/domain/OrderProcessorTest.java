package learning.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    // TODO: Declare mocks for dependencies:
    // @Mock
    // private PaymentGateway paymentGateway;
    // ...

    // TODO: Declare SUT:
    // @InjectMocks
    // private OrderProcessor orderProcessor;

    @Test
    void shouldFailImmediatelyWhenInventoryIsOutOfStock() {
        // TODO:
        // - Stub inventoryService.reserve to throw OutOfStockException.
        // - Assert that calling orderProcessor.process throws the exception.
        // - Verify that paymentGateway was never called and orderRepository was never saved.
    }

    @Test
    void shouldCompleteAndSaveOrderWhenPaymentSucceeds() {
        // TODO:
        // - Stub paymentGateway to return PaymentResult.SUCCESS.
        // - Assert that process returns true.
        // - Verify that the order transitioned to COMPLETED.
        // - Verify orderRepository.save was called.
    }

    @Test
    void shouldReleaseInventoryAndCancelOrderWhenPaymentFails() {
        // TODO:
        // - Stub paymentGateway to return PaymentResult.FAILURE.
        // - Assert that process returns false.
        // - Verify inventoryService.release was called.
        // - Verify that the order transitioned to CANCELLED.
        // - Verify orderRepository.save was called.
    }

    @Test
    void shouldProcessOrderSuccessfullyUsingFakeRepository() {
        // TODO:
        // - Create an instance of FakeOrderRepository.
        // - Manually construct OrderProcessor passing the payment mock, inventory mock, and the fake repository.
        // - Run a successful payment scenario.
        // - Assert that the fake repository actually contains the saved order (no Mockito verification).
    }

    // Fake Repository class for learning taxonomy
    static class FakeOrderRepository implements OrderRepository {
        private final List<Order> savedOrders = new ArrayList<>();

        @Override
        public void save(Order order) {
            savedOrders.add(order);
        }

        public List<Order> getSavedOrders() {
            return savedOrders;
        }
    }
}
