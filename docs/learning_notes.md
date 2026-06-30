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

### Record Equals & BigDecimal Scale Trap
* Because Java Records automatically generate `.equals()` by delegating to each field's `.equals()`, wrapping a `BigDecimal` inside a record (like `Money`) makes the record's equals method susceptible to the scale trap.
* `new Money(new BigDecimal("5"), "USD").equals(new Money(new BigDecimal("5.0"), "USD"))` will return `false`.
* **The Solution**: In the record's compact constructor, normalize the scale of the incoming `BigDecimal` so all instances share the same scale:
  ```java
  public record Money(BigDecimal amount, String currency) {
      public Money {
          // Normalize scale to 2 and round (or truncate) to avoid the equals() scale trap
          amount = amount.setScale(2, java.math.RoundingMode.HALF_UP);
      }
  }
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

---

## 7. Concurrency & Virtual Threads (Java 25 Baseline)

### Key Concept: Platform vs. Virtual Threads
* **Platform Threads**: Managed by the OS, mapping 1:1 to OS threads. They are heavy, memory-intensive (~1MB stack), and expensive to context-switch.
* **Virtual Threads**: Managed by the JVM, mapping M:N to a pool of platform "carrier" threads. They are lightweight (~hundreds of bytes stack) and can be created in millions.
* **Unmounting**: When a virtual thread performs a blocking operation (like `Thread.sleep()` or blocking socket read), the JVM scheduler *unmounts* it from its carrier thread, leaving the carrier thread free to run other virtual threads.

### JEP 491: Synchronized Unmounting (Java 24+)
* In early virtual thread previews (Java 19-21), blocking inside a `synchronized` block or method would **pin** the virtual thread to its carrier thread, blocking the carrier thread as well. Developers had to refactor all `synchronized` blocks to `ReentrantLock` to avoid this.
* **In Java 24+ (JEP 491)**, the JVM was updated to transition synchronized blocks to support unmounting. A virtual thread now successfully unmounts inside `synchronized` blocks when performing blocking operations:
  ```java
  // In Java 25, this NO LONGER pins the carrier thread during sleep!
  synchronized (new Object()) {
      Thread.sleep(100); 
  }
  ```

### Key Concept: Lock Contention
* While `ReentrantLock` allows unmounting, using a shared instance of a lock (like a class-level final field) across multiple concurrent tasks will **serialize** execution due to mutual exclusion:
  ```java
  private final ReentrantLock lock = new ReentrantLock();
  
  public void executeLockTask() {
      lock.lock(); // Only one thread runs this at a time; others wait in series!
      try {
          Thread.sleep(100);
      } finally {
          lock.unlock();
      }
  }
  ```
* For concurrent operations that do not require mutual exclusion, do not lock on a shared instance.

---

## 8. Thread Safety: Why Do We Use Locks? (Safety vs. Speed)

### Key Concept: Locks are for Safety, not Speed
* **Locks NEVER make code faster**. In fact, they introduce overhead and serialization (making threads wait in line). If we could write code without locks, it would run at maximum speed.
* We use locks for **Thread Safety (Correctness)**: protecting **shared mutable state** (variables that are read/written concurrently by multiple threads) from corruption or race conditions.

### The Bank Account Analogy (Race Condition)
Suppose a bank account has a balance of **$100**. Two clients concurrently try to withdraw **$80** at the exact same millisecond:
1. **Thread A (ATM 1)**: Reads balance ($100). Thinks: "Sufficient funds, proceed!"
2. **Thread B (ATM 2)**: Reads balance ($100) concurrently. Thinks: "Sufficient funds, proceed!"
3. **Thread A**: Subtracts $80. New balance = $20. Disburses cash.
4. **Thread B**: Subtracts $80. New balance = -$60 (Overdrawn). Disburses cash.
* Without synchronization, the bank loses $60 because two threads operated simultaneously.

### The Solution: Locking for Mutual Exclusion
* By wrapping the withdrawal logic in a lock (e.g. `ReentrantLock` or `synchronized`), we force Thread B to wait outside until Thread A finishes.
* Thread B will then read the updated balance of $20 and reject the withdrawal. The code runs **slower** (Thread B had to wait), but it is **correct**.

* **With Virtual Threads**: When a virtual thread blocks waiting to acquire a lock or during a sleep/network call, it **unmounts** from its platform carrier thread. The JVM scheduler immediately repurposes that carrier thread to run another virtual thread. Once the lock is released or the waiting is over, the virtual thread is remounted onto an available carrier thread to resume execution.

---

## 9. Testing: Test Data Builders and Behavior-Focused Assertions

### Key Concept: Test Clutter
* Setting up test fixtures via standard constructors (e.g. `new Order("o1", new Customer("c1", "Name", ...))`) results in verbose setup blocks.
* If a domain constructor changes later, dozens of tests break, making the test suite fragile.

### Key Concept: Test Data Builder Pattern
* Provides a fluent API to build domain models with sensible default values, only overriding what is relevant to the test case:
  ```java
  // Clear, readable setup
  Order order = anOrder()
      .withCustomer(aCustomer().withEmail("invalid-email"))
      .with(anOrderItem().withPrice(new Money(BigDecimal.valueOf(120), "EUR")))
      .build();
  ```
* **Chaining Builders**: Having a builder accept other builder instances (e.g., `OrderBuilder.withCustomer(CustomerBuilder)`) enhances expression and avoids intermediate `.build()` calls in the test itself.

### Key Concept: Behavior-Focused Test Naming & Assertions
* **Naming**: Name tests based on the expected behavior rather than the class name or simple action (e.g., `shouldApplyTenPercentDiscountWhenOrderTotalIsOverOneHundred` vs. `testOrderTotal`).
* **AssertJ Fluent API**: Use expressive, human-readable assertion methods instead of raw boolean checks:
  * `assertThat(result.amount()).isEqualByComparingTo(expected)` (proper BigDecimal handling).
  * `assertThatThrownBy(() -> action()).isInstanceOf(IllegalStateException.class).hasMessageContaining("empty")`.

---

## 10. JUnit 6 (Unified Platform & Engine)

### Key Concept: Unified Versioning
* In JUnit 5, components were split into different versions (JUnit Platform, Jupiter, Vintage), which often made dependency management complex.
* **JUnit 6** (released in late 2025) consolidates all these packages under a **single unified version (6.x.x)**.
* **Java Baseline**: JUnit 6 requires **Java 17 or higher** (fully compatible with our Java 25 workspace).
* **Improved Parameterized Testing**: Migrated from `univocity-parsers` to `FastCSV` for CSV-driven tests.
* **Coroutines Support**: Native integration with Kotlin `suspend` functions without needing extra coroutine libraries.

---

## 11. BDDMockito and Switch Expressions

### Key Concept: BDDMockito (Given / When / Then)
* Standard Mockito uses `when(mock.call()).thenReturn(val)` and `verify(mock).call()`. This mixes vocabulary and breaks the BDD flow.
* **BDDMockito** (`org.mockito.BDDMockito.*`) provides aliases to align with the **Given / When / Then** structure:
  * **Given (Stubbing)**: `given(mock.call()).willReturn(val)` or `willThrow(exception).given(mock).reserve(...)`.
  * **Then (Verification)**: `then(mock).should().call()` or `then(mock).shouldHaveNoInteractions()`.
  ```java
  // Given
  given(paymentGateway.charge(order)).willReturn(PaymentResult.SUCCESS);

  // When
  boolean result = orderProcessor.process(order);

  // Then
  assertThat(result).isTrue();
  then(orderRepository).should().save(order);
  ```

### Key Concept: Switch Expressions (Java 14+)
* Standard `switch` statements can lead to bugs (missing `break`, leading to fall-through) and require fallback return statements outside the block.
* **Switch Expressions** yield a value directly using the `yield` keyword or arrow syntax `->` and are compile-time checked for exhaustiveness (no `default` case is needed for enums/sealed types if all cases are covered):
  ```java
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
  ```

---

## 12. Integration Testing and Testcontainers (PostgreSQL)

### Unit vs. Integration Testing
* **Unit Testing**: Focuses on a single class/method in isolation, mocking all external dependencies. Fast and cheap, but cannot verify database behavior, SQL queries, or integrations.
* **Integration Testing**: Exercises the system with real external dependencies (databases, queue brokers, web services) to verify actual runtime interactions.

### Testcontainers
* Testcontainers is a Java library that allows launching real Docker containers (PostgreSQL, Kafka, Redis, etc.) programmatically during test execution.
* Ensures **isolated, reproducible database states** for every developer and CI runner, preventing database pollution.

### Best Practices for DB Integration Tests
1. **Singleton Container Pattern**: Spawning a Docker container is resource-heavy (takes 3-8 seconds). To speed up testing, define the container as a `static` field in a base class or test class so it boots once per test execution rather than once per test class.
2. **State Cleanliness (Truncation)**: Run `TRUNCATE TABLE tablename` in `@BeforeEach` to clean the database before each test. This ensures test isolation without restarting the container.
3. **Dynamic Configuration**: Do not hardcode ports. Retrieve the dynamic JDBC URL, username, and password from the running container via `container.getJdbcUrl()`, `container.getUsername()`, etc.

### JDBC Best Practices
* **Use try-with-resources for `ResultSet`**: Like `PreparedStatement` and `Connection`, the `ResultSet` must be closed to prevent cursor leaks in PostgreSQL.
* **Access columns by name**: Prefer `resultSet.getString("name")` over `resultSet.getString(2)`. Reading columns by name is resistant to column order shifts in SQL `SELECT` queries.
* **executeUpdate vs executeQuery**: Use `executeUpdate()` for statements that mutate data (INSERT, UPDATE, DELETE) and `executeQuery()` only for reading data (SELECT).








