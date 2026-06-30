# Issue 002: Implement Value Objects with Invariants

## Context
Value Objects are immutable domain objects whose identity is determined by their properties rather than a unique identifier. They are essential for modeling domain concepts cleanly and enforcing invariants at the boundary of construction.

## Goal
Implement a `Money` value object as a Java **Record** that encapsulates:
- An `amount` (e.g., `BigDecimal`).
- A `currency` (e.g., a custom enum or ISO 4217 currency code validation).

## Invariants to Enforce
1. **Non-nullability**: Neither `amount` nor `currency` can be null.
2. **Precision**: The amount must have a fixed/validated scale (e.g., 2 decimal places).
3. **Currency ISO validation**: Currency codes must be exactly 3 uppercase letters (e.g., USD, EUR, GBP).
4. **Behavior**:
   - `add(Money other)`: Returns a new `Money` instance representing the sum. Throws an exception if currencies do not match.
   - `subtract(Money other)`: Returns a new `Money` instance representing the difference. Throws an exception if currencies do not match.
   - Zero or positive value validation (optional, depending on the domain use-case, but let's allow negative balances, just validate non-null values).

## Definition of Done (DoD)
- [ ] A compact constructor in the `Money` record that validates all invariants.
- [ ] A complete suite of unit tests verifying:
  - Successful creation under valid conditions.
  - Failures (throwing `NullPointerException` or `IllegalArgumentException`) when invariants are violated.
  - Adding/subtracting money with the same currency works.
  - Adding/subtracting money with different currencies throws `IllegalArgumentException`.
  - Immutability checks (records are naturally immutable, but ensuring the scale is normalized/copied if necessary).
