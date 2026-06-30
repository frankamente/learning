# Issue 001: Setup Maven Project with Java 25

## Context
Before we can write any Java code or tests, we must establish a solid project structure using Maven. We target **Java 25** to use the latest platform features and compiler capabilities.

## Goal
Configure a single Maven module under `tracks/01-java-foundations/01-setup-and-value-objects/` that correctly sets up:
- Java 25 compiler configuration.
- UTF-8 source encoding.
- JUnit Jupiter (JUnit 5) dependency.
- A basic unit test that runs successfully to verify the toolchain configuration.

## Definition of Done (DoD)
- [ ] Maven `pom.xml` configured with `<maven.compiler.release>25</maven.compiler.release>`.
- [ ] Maven test plugin configured to execute JUnit Jupiter tests.
- [ ] A unit test class `VerifyTest` with at least one passing test case.
- [ ] Running `mvn clean test` completes successfully.
