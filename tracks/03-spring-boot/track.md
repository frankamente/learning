# Track 3: Spring Boot and Modern Spring

## Objective
Learn Spring Boot as a professional backend platform, understanding its core machinery (Application Context, Dependency Injection, Auto-configuration) and writing modern, high-quality test-driven services.

## Roadmap

### 1. Spring Core Foundations
- **Module 1: Dependency Injection & Application Context**
  - Bean lifecycle, scopes (Singleton vs. Prototype), constructor injection, and profiles.
  - Test verifying context configurations.
- **Module 2: Spring Boot Auto-configuration & Configuration Properties**
  - Custom auto-configurations, conditional beans (`@ConditionalOnProperty`, etc.), and `@ConfigurationProperties` binding with validation.

### 2. Web Services and REST APIs
- **Module 3: Spring MVC & Web Layer Design**
  - RestControllers, request/response validation, global error mapping (RFC 9457 Problem Details).
  - WebMvcTest slice testing.
- **Module 4: Modern REST Client & Resilience**
  - Modern `RestClient` and declarative interface HTTP clients. Timeouts, retries, and error boundaries.

### 3. Security and Identity Management
- **Module 5: Spring Security & IAM Integration**
  - Filter chain, security contexts, form + LDAP concurrent login, Social login OAuth2/OIDC, and Keycloak integrations.

## Active Issues
- [ ] **Issue 010**: Spring Dependency Injection and Application Context. See [issue-010_spring_dependency_injection_and_context.md](issues/issue-010_spring_dependency_injection_and_context.md)
