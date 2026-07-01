# Issue 010: Spring Dependency Injection and Application Context

## Context
Spring is fundamentally a dependency injection (DI) container. Before utilizing web controllers or databases, we must master how Spring manages objects (called **Beans**) inside the **Application Context**.

## Goal
Implement a small Spring Boot application demonstrating Constructor Injection, Bean Scopes, and Profiles, backed by context-verification integration tests.

## Requirements

### Project Setup
Create project `01-dependency-injection` using Maven with:
- Spring Boot Starter Parent (using version `4.1.0` or similar modern 4.x).
- Dependencies: `spring-boot-starter` (core Spring Boot), `spring-boot-starter-test`.
- JUnit 6.0.3 and AssertJ 3.27.7 configurations matching our global standard.

### Core Components
1. **Repository Interface & Implementations**:
   - `MessageRepository` interface.
   - `InMemoryMessageRepository` annotated with `@Repository`.
2. **Service Interface & Profile-driven Implementations**:
   - `NotificationSender` interface.
   - `EmailNotificationSender` active under profile `"email"`.
   - `SmsNotificationSender` active under profile `"sms"`.
3. **Application Service / Orchestrator**:
   - `NotificationService` that coordinates messaging.
   - Must use **Constructor Injection** to inject the `MessageRepository` and the active `NotificationSender`.
   - Annotated with `@Service`.

### Bean Scopes
- Create a Prototype scoped component `RequestIdGenerator` (annotated with `@Scope("prototype")`).
- Inject it into two separate Singleton beans (`NotificationService` and `InMemoryMessageRepository`).
- Write a test verifying that each singleton bean receives a **distinct instance** of `RequestIdGenerator` (demonstrating Prototype behavior).

### Context & Integration Tests
Write JUnit 6 tests using `@SpringBootTest` to verify:
- The context boots correctly.
- When profile `"email"` is active, `NotificationSender` is an instance of `EmailNotificationSender`.
- When profile `"sms"` is active, `NotificationSender` is an instance of `SmsNotificationSender`.
- Prototype scope behavior is correct (two different singletons get distinct instances of the prototype bean).

## Definition of Done (DoD)
- [ ] Maven Spring Boot parent configuration complete in `pom.xml`.
- [ ] Core interfaces, profile-driven components, and constructor injection implemented.
- [ ] Scopes component `RequestIdGenerator` created.
- [ ] `@SpringBootTest` suite verifying profile activation and bean resolution passes.
- [ ] Prototype scope validation test passing.
