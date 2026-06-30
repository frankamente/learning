# Track: Testing

## Goal
Use tests as a design tool, not only as correctness checks.

## Current Baseline
- **Java**: 25
- **Build Tool**: Maven 3.9+
- **Testing Framework**: JUnit Jupiter & AssertJ 3.27.7

## Modules
1. **Module 1: Test Data Builders & Assertions**
   - Descriptive behavior-focused test naming.
   - AssertJ assertions (fluent assertions, customized error messages).
   - Test Data Builders pattern for clean fixture management.
2. **Module 2: Mockito & Test Doubles**
   - Mocks, stubs, spies, and fakes.
   - Naming conventions for mocks (e.g. avoiding generic names like `mockService`).
   - Mockito annotations and lifecycle.
3. **Module 3: Integration Tests & Testcontainers**
   - Database testing with Testcontainers (PostgreSQL).
   - Spring Boot slice tests later (when starting Spring).

## Active Issues
- [x] **Issue 007**: Test Data Builders, assertions and clean domain test design. See [issue-007_test_data_builders_and_assertions.md](issues/issue-007_test_data_builders_and_assertions.md)
- [x] **Issue 008**: Mockito, Test Doubles, and BDD Mocking. See [issue-008_mockito_and_test_doubles.md](issues/issue-008_mockito_and_test_doubles.md)
- [x] **Issue 009**: Integration Testing and Testcontainers (PostgreSQL). See [issue-009_testcontainers_and_integration.md](issues/issue-009_testcontainers_and_integration.md)
