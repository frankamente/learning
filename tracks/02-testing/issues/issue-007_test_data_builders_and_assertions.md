# Issue 007: Test Data Builders and AssertJ Assertions

## Context
When writing unit tests for complex domain models (like aggregates), setting up test data often requires writing bloated constructor calls. This creates "test clutter," making tests hard to read and fragile when constructors change.

The **Test Data Builder** pattern solves this by providing a fluent API to build domain objects with sensible default values, only overriding what is relevant for the specific test case.

## Goal
Implement a domain aggregate representing a customer order and test it thoroughly using:
1. **Test Data Builders** for all domain classes.
2. **Behavior-focused test names** (e.g., `shouldApplyTenPercentDiscountWhenOrderTotalIsOverOneHundred`).
3. **Fluent AssertJ assertions** (e.g., `assertThat(order.total()).isEqualByComparingTo(...)`).

## Domain Model & Rules
1. **Customer**: Has an `id`, `name`, `email`, and `address`.
2. **OrderItem**: Contains a `productName`, a `price` (`Money`), and a `quantity` (integer > 0).
3. **Order**:
   - Contains a `Customer`, a `List<OrderItem>`, and a status (`DRAFT`, `COMPLETED`, `CANCELLED`).
   - Rule 1: **Total Price Calculation**: The total is the sum of `price * quantity` of all items.
   - Rule 2: **Automatic Discount**: If the total price exceeds **100 EUR**, a **10% discount** is applied to the final order price.
   - Rule 3: **Invariants**: An order cannot be transitioned to `COMPLETED` if it contains no items, or if the customer has an invalid email.

## Requirements

### Test Data Builders
Implement builder classes with static factory methods (e.g., `aCustomer()`, `anOrderItem()`, `anOrder()`) and default values:
- `CustomerBuilder`: defaults to valid customer name, email "john.doe@example.com", etc.
- `OrderItemBuilder`: defaults to a product "Keyboard", price 20 EUR, quantity 1.
- `OrderBuilder`: defaults to a valid customer, draft status, and empty items.

### Example Builder Usage in Tests:
```java
Order order = anOrder()
    .withCustomer(aCustomer().withEmail("invalid-email"))
    .with(anOrderItem().withPrice(new Money(BigDecimal.valueOf(120), "EUR")))
    .build();
```

## Definition of Done (DoD)
- [ ] `Customer`, `OrderItem`, and `Order` models implemented with invariants.
- [ ] Fluently chainable `CustomerBuilder`, `OrderItemBuilder`, and `OrderBuilder` classes implemented.
- [ ] Unit tests verifying:
  - Price calculation without discount.
  - Price calculation with 10% discount when over 100 EUR.
  - Verification that completing an empty order throws `IllegalStateException`.
  - Verification that completing an order with an invalid customer email throws `IllegalStateException`.
- [ ] All tests use descriptive, behavior-based names and AssertJ assertions.
