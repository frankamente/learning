# Issue 008: Mockito, Test Doubles, and BDD Mocking

## Context
When unit testing services that coordinate multiple dependencies, we must isolate the system under test (SUT) from external subsystems (e.g. networks, databases, payment processors). We do this using **Test Doubles**.

Mockito is Java's standard tool for creating *mocks* and *stubs*, but it can easily be abused (e.g., generic names like `mockService`, over-verifying internal details, or hardcoding execution order).

## Goal
Implement an `OrderProcessor` service and test it using:
1. **Mockito BDD style**: Use `given(...)` instead of `when(...)`, and `then(...).should(...)` instead of `verify(...)`.
2. **Proper double taxonomy**: Differentiate and implement **Mocks**, **Stubs**, and **Fakes** in your tests.
3. **Role-based naming**: Avoid generic `mock` prefixes for dependencies.

## Domain Model & Rules
We need an `OrderProcessor` that processes an `Order`:
```java
public class OrderProcessor {
    public OrderProcessor(
        PaymentGateway paymentGateway,
        InventoryService inventoryService,
        OrderRepository orderRepository
    ) { ... }
}
```

### Business Rules for `process(Order order)`:
1. **Check & Reserve Inventory**: Reserve the items via `InventoryService`.
   - If reservation fails (throws `OutofStockException`), order processing terminates immediately, leaving the order status unchanged (no payment, no repository save).
2. **Process Payment**: If reserved, execute payment via `PaymentGateway`.
   - Payment returns a `PaymentResult` (either `SUCCESS` or `FAILURE`).
3. **Handle Outcomes**:
   - **On Payment Success**: Transition order to `COMPLETED`, save to repository, and return `true`.
   - **On Payment Failure**: Release inventory reservation, transition order to `CANCELLED`, save to repository, and return `false`.

## Requirements

### Test Setup
- Configure your test class using JUnit Jupiter and the Mockito Extension (`@ExtendWith(MockitoExtension.class)`).
- Inject dependencies using Mockito annotations (`@Mock`, `@InjectMocks`).
- Differentiate:
  - **Stubbing**: Use stubbing (`given(...)`) to configure queries (like checking inventory or processing payment).
  - **Verifying (Mocking)**: Use BDD verification (`then().should()`) to verify commands (like making sure the repository saves the order).
  - **Faking**: Implement a simple, fast in-memory `FakeOrderRepository` for a specific test case to verify how it differs from a mock.

## Definition of Done (DoD)
- [ ] `OrderProcessor` logic implemented coordinating dependencies.
- [ ] Unit tests covering:
  - Immediate failure on stock shortage.
  - Success path: payment succeeds, order completes, saved to repo.
  - Failure path: payment fails, inventory released, order cancelled, saved to repo.
- [ ] BDD-style mocking and verification used exclusively.
- [ ] No generic `mockX` variable names used.
- [ ] A dedicated test case showcasing the usage of a `FakeOrderRepository` instead of the Mockito mock.
