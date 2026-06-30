# Track: Java Foundations

## Goal
Become fluent enough in Java to reason about backend code without hiding behind frameworks.

## Current Baseline
- **Java**: 25 (OpenJDK 25)
- **Build Tool**: Maven 3.9+ with Compiler Release 25
- **Testing Framework**: JUnit Jupiter (JUnit 5)

## Modules
1. **Module 1: Setup & Value Objects / Invariants**
   - Maven setup with Java 25.
   - Core concepts of Value Objects (immutability, validation on construction, defensive copying).
   - Java Records and how they support value semantics.
2. **Module 2: Packages and Encapsulation**
   - Packages and visibility rules (package-private by default, module-info later if needed).
   - Cohesion and coupling.
   - Sealed classes and interfaces with switch pattern matching.
3. **Module 3: Generics and Collections**
   - Java Generics (wildcards, bounds).
   - Collections hierarchy (List, Set, Map, SequencedCollections).
   - Stream API pipelines and Java 21+ enhancements.
4. **Module 4: Error Handling & Optionals**
   - Checked vs. Unchecked Exceptions.
   - Designing custom exceptions and domain-specific result types.
   - Proper use of Java's `Optional`.
5. **Module 5: Concurrency and Virtual Threads**
   - Basic concurrency concepts (threads, synchronization, volatile, locks).
   - ExecutorService.
   - Virtual Threads (Java 21+) and lightweight execution.

## Active Issues
- [x] **Issue 001**: Setup Maven project with Java 25 and write validation test. See [issue-001_setup_maven_java25.md](issues/issue-001_setup_maven_java25.md)
- [x] **Issue 002**: Implement value objects with invariants and validation tests. See [issue-002_value_objects_invariants.md](issues/issue-002_value_objects_invariants.md)
- [x] **Issue 003**: Packages, Encapsulation, and Sealed Types. See [issue-003_packages_and_sealed_types.md](issues/issue-003_packages_and_sealed_types.md)
- [x] **Issue 004**: Generics, Custom Repository, and Collections. See [issue-004_generics_and_collections.md](issues/issue-004_generics_and_collections.md)
- [ ] **Issue 005**: Domain Exceptions, Result Types, and Optional Pipelines. See [issue-005_error_handling_and_optionals.md](issues/issue-005_error_handling_and_optionals.md)
