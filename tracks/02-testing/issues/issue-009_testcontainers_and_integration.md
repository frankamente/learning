# Issue 009: Integration Testing and Testcontainers (PostgreSQL)

## Context
Unit tests with mocks are great for business logic, but they cannot verify that our SQL queries, database constraints, or transaction boundaries are correct. For this, we need **Integration Tests** running against a real database.

Using a shared, persistent database for tests is an antipatrimony (leads to state pollution, flaky tests, and setup headaches). Instead, we use **Testcontainers** to spin up a lightweight, isolated PostgreSQL database inside a Docker container automatically for the lifecycle of our tests.

## Goal
Implement a `JdbcProductRepository` using raw JDBC and write integration tests verifying its behavior against a running PostgreSQL Testcontainer.

## Requirements

### Database Schema
Create a file `src/test/resources/schema.sql` defining:
```sql
CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL
);
```

### JDBC Repository
Implement `JdbcProductRepository` implementing `ProductRepository`:
```java
public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
}
```
Use raw JDBC (`Connection`, `PreparedStatement`, `ResultSet`) to query and update the database.

### Integration Test Configuration
Configure a JUnit 6 test class `JdbcProductRepositoryIT` (using `IT` suffix for Integration Tests) that:
1. Starts a `PostgreSQLContainer<?>` automatically before tests.
2. Retrieves dynamic connection properties (JDBC URL, username, password) from the running container.
3. Initializes the database schema using the `schema.sql` file.
4. Truncates/cleans the `products` table before each test case to ensure test isolation.
5. Verifies:
   - Saving and retrieving a product.
   - Finding a non-existent product returns `Optional.empty()`.
   - Saving a product with a duplicate ID throws an exception (e.g. `SQLException` or a mapped domain exception).

### Singleton Container Pattern
Implement the singleton container pattern (sharing the same container instance across all tests) to ensure we only boot Docker once, speeding up the test suite.

## Definition of Done (DoD)
- [ ] `JdbcProductRepository` fully implemented with raw JDBC.
- [ ] `schema.sql` database schema initialization script written.
- [ ] `JdbcProductRepositoryIT` integration test suite implemented using Testcontainers.
- [ ] Testcontainers singleton pattern utilized for fast test startup.
- [ ] Table truncation active in `@BeforeEach` to guarantee test isolation.
- [ ] All integration tests passing.
