# Issue 005: Domain Exceptions, Result Types, and Optional Pipelines

## Context
Professional error handling goes beyond throwing generic exceptions. It involves modeling business failures explicitly. Modern Java encourages the use of `Optional` pipelines for query results and sealed types for expressing either/or results (the "Result Type" pattern).

## Goal
Implement a robust domain validation and error handling system for our `InMemoryRepository` and a client service that:
1. **Differentiates business errors** using custom unchecked exceptions.
2. **Utilizes Optional pipelines** instead of manual `if-isPresent` checks.
3. **Explores the Result Type pattern** using sealed records to represent successes and failures explicitly without relying on exception throwing for expected business control flows.

## Requirements

### Part 1: Custom Exceptions & Strict Repository Checks
- Refactor the repository:
  - If saving an entity with an ID that already exists, but we want to prevent overwrites under certain strict conditions, throw a custom `DuplicateKeyException`.
  - Add a method `T getOrThrow(ID id)` that retrieves the entity or throws a custom `EntityNotFoundException` containing the missing ID.

### Part 2: Functional Optional Pipelines
- Write a client service method `findDiscountedPriceForProduct(String productId, DiscountPolicy policy)` that:
  - Finds the product in the repository.
  - Applies a discount.
  - Returns `Optional<Money>`.
  - **Constraint**: Implement this pipeline entirely using functional methods (`flatMap`, `map`, `or`, `orElseThrow`) without a single `if` statement or manual `isPresent()` check.

### Part 3: The Result Type Pattern
- Implement a generic `Result<T>` sealed type hierarchy:
  - `sealed interface Result<T> permits Success, Failure`
  - `record Success<T>(T value) implements Result<T>`
  - `record Failure<T>(String errorMessage, Throwable cause) implements Result<T>`
- Write a repository method `Result<T> trySave(T entity)` that returns `Success` or `Failure` instead of throwing exceptions.

## Definition of Done (DoD)
- [ ] Custom domain exceptions created and thrown appropriately.
- [ ] Client service method implemented using pure `Optional` pipelines.
- [ ] Sealed `Result<T>` hierarchy implemented and used to represent save operations.
- [ ] Unit tests verifying:
  - Correct exceptions are thrown with descriptive messages.
  - `Optional` pipeline yields correct output for present and empty cases.
  - Sealed `Result` pattern correctly resolves using switch expressions.
