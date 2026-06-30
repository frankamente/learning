# Lessons Learned & Concepts Summary

This document serves as a personal knowledge base of engineering concepts, pitfalls, and best practices learned during this roadmap.

---

## 1. Value Objects & Immutability

### Key Concept
* A **Value Object** is a domain object whose identity is defined by its properties, not a unique ID (unlike Entities). For example, `Money(5, "USD")` is structurally identical to another `Money(5, "USD")`.
* Value Objects must be **immutable**. Their state cannot change after instantiation.

### Java Implementation Notes
* **Records**: Java Records (`public record Money(...)`) are ideal for Value Objects because their fields are automatically `private final`, and they implement `equals()`, `hashCode()`, and `toString()` based on structural equality.
* **Non-mutating methods**: Methods like `add(Money other)` must return a **new instance** of the object rather than modifying the current instance:
  ```java
  public Money add(Money moneyToAdd) {
      return new Money(this.amount.add(moneyToAdd.amount), this.currency);
  }
  ```
* **Invariants at Construction**: Validations should happen inside the constructor (using a **Compact Constructor**) so that it is impossible to instantiate an invalid object in the system.

---

## 2. Java Records: Compact Constructors

### Key Concept
* A **Compact Constructor** (`public RecordName { ... }`) does not declare parameters. It implicitly receives the record's components and runs *before* they are assigned to their final fields.

### Example
```java
public record Money(BigDecimal amount, String currency) {
    public Money {
        // Validations run here. If validation fails, object creation is prevented.
        java.util.Objects.requireNonNull(amount, "Amount cannot be null");
        
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }
}
```

---

## 3. The `BigDecimal` Comparison Trap in Java

### The Pitfall
* The `.equals()` method in `BigDecimal` compares **both numerical value and scale** (number of decimal places).
* For example:
  ```java
  BigDecimal five = new BigDecimal("5");
  BigDecimal fivePointZero = new BigDecimal("5.0");
  
  boolean isEquals = five.equals(fivePointZero); // FALSE!
  ```

### The Solution
* Always use `.compareTo()` to compare `BigDecimal` values:
  ```java
  boolean isSameValue = five.compareTo(fivePointZero) == 0; // TRUE!
  ```
* When comparing to check if a value is positive/negative:
  ```java
  boolean isPositive = amount.compareTo(BigDecimal.ZERO) >= 0;
  ```

### Testing Assertions (AssertJ)
* Use `isEqualByComparingTo()` instead of `isEqualTo()` for asserting `BigDecimal` values:
  ```java
  assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(6));
  ```

---

## 4. Map Lookups Optimization & Defensive Copies

### Key Concept: Single vs. Double Hash Lookup
* When using a `Map`, calling `.containsKey(key)` followed by `.get(key)` or `.remove(key)` performs **two lookups** in the hash table.
* In Java, you should do a **single lookup** by calling `.get(key)` or `.remove(key)` directly:
  * For retrieval, handle the potential `null` result using `Optional.ofNullable()`:
    ```java
    // 1 lookup (O(1)) instead of 2
    return Optional.ofNullable(map.get(id));
    ```
  * For deletion, calling `remove(id)` directly is completely safe even if the key does not exist (it simply returns `null` and does not throw any exception).

### Key Concept: Encapsulation & Defensive Copies
* When returning a collection that is part of a class's internal state (e.g. `findAll()`), returning it directly allows external clients to modify the internal state of the class (e.g. `repo.findAll().clear()` would modify the map if returned directly).
* To prevent this, always return an **unmodifiable copy** of the collection. In Java 10+, `List.copyOf(collection)` is the idiomatic way:
  ```java
  public List<T> findAll() {
      // Returns a safe, unmodifiable shallow copy of the values
      return List.copyOf(inMemoryCollection.values());
  }
  ```

---

## 5. Generic Type Bounds

### Key Concept
* **Bounded Type Parameters** allow you to restrict the types that can be used as type arguments in a parameterized class or method.
* In a repository, to ensure that any type `T` has an identifier of type `ID`, we bind `T` to implement a contract like `Entity<ID>`:
  ```java
  public class InMemoryRepository<T extends Entity<ID>, ID> {
      public void save(T entity) {
          ID id = entity.id(); // Safe to call because T is bounded by Entity<ID>
          inMemoryCollection.put(id, entity);
      }
  }
  ```

---

## 6. Domain Exceptions, Optional Pipelines & Result Pattern

### Key Concept: Custom Domain Exceptions
* Instead of throwing generic exceptions (like `IllegalArgumentException`), model domain failures explicitly using custom unchecked exceptions:
  * `EntityNotFoundException` represents a query finding nothing.
  * `DuplicateKeyException` represents a state violation on strict insertion.

### Key Concept: Optional Pipelines (Functional Style)
* Avoid using manual `if (opt.isPresent())` block checks. Instead, chain operations using monadic methods:
  * `.orElseThrow()`: Cleanly resolves a value or throws an exception.
  * `.map()` / `.flatMap()`: Transforms a present value or propagates an empty `Optional`.
  ```java
  // Clean Optional pipeline
  return repository.findById(productId)
          .map(product -> discountService.applyDiscount(product.price(), policy));
  ```

### Key Concept: The Result Type Pattern
* In functional programming, expected business errors (like duplicate records) are often represented as **data** rather than using JVM exception throwing (which is expensive and represents exceptional, unexpected failures).
* We model this in Java using a `sealed interface Result<T>` permitting `Success<T>` and `Failure<T>`:
  ```java
  public sealed interface Result<T> permits Success, Failure {}
  
  public record Success<T>(T value) implements Result<T> {}
  public record Failure<T>(String errorMessage, Throwable cause) implements Result<T> {}
  ```
* In clients, we process the result cleanly using a `switch` expression:
  ```java
  return switch (result) {
      case Success<Product> s -> ...;
      case Failure<Product> f -> ...;
  };
  ```

### Key Concept: Unnamed Variables (Java 22+)
* Java 22 standardized the use of the underscore `_` to denote unused variables. This is particularly useful in lambda expressions where a parameter must be declared by the API but is not needed:
  ```java
  // Using '_' for unused lambda parameter
  optionalProduct.ifPresentOrElse(
      (_) -> { throw new DuplicateKeyException("Already exists"); },
      () -> save(product)
  );
  ```


