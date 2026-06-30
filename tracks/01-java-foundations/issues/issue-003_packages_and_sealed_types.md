# Issue 003: Packages, Encapsulation, and Sealed Types

## Context
Encapsulation and package boundary design are vital in professional systems to prevent internal implementation details from leaking to clients. Modern Java (21+) introduces sealed classes/interfaces and switch pattern matching, which allow us to define closed, exhaustive type hierarchies.

## Goal
Implement a discount calculation feature for our domain that:
1. **Encapsulates implementation details** using Java package visibility (keeping policies package-private, exposing only a public interface or factory).
2. **Defines a closed hierarchy** of discount policies using a `sealed interface DiscountPolicy`.
3. **Applies discounts** using modern Java `switch` expressions with pattern matching, leveraging compiler-enforced exhaustiveness.

## Requirements

### Package Structure
- Create a package `learning.domain.discount`.
- Inside `learning.domain.discount`:
  - `public sealed interface DiscountPolicy` which allows only specific implementations:
    - `PercentageDiscount` (e.g., 10% off).
    - `AbsoluteDiscount` (e.g., $5 off).
    - `NoDiscount` (no discount applied).
  - Make the implementations package-private (no `public` modifier) so they cannot be accessed or instantiated outside the `learning.domain.discount` package.
  - Provide a public factory class or static factory methods to construct policies.

### Switch Pattern Matching
- Implement a method that calculates the discounted price:
  ```java
  public Money applyDiscount(Money original, DiscountPolicy policy) {
      // Use switch pattern matching on 'policy'
  }
  ```
- Verify that the switch expression is exhaustive (meaning you don't need a `default` case because the compiler knows all possible subclasses of the sealed interface).

## Definition of Done (DoD)
- [ ] Sealed interface `DiscountPolicy` restricts subclasses correctly.
- [ ] Implementations are package-private, hiding details from external packages.
- [ ] `switch` expression pattern matching computes discounts without a `default` block.
- [ ] Unit tests in a different package (e.g., `learning.client` or package root) assert that:
  - Valid discounts are applied correctly.
  - Implementations cannot be directly imported or instantiated outside their package (encapsulation check).
