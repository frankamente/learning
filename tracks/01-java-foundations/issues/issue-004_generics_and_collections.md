# Issue 004: Generics, Custom Repository, and Collections

## Context
Understanding Java's type system (Generics) and the Collections framework is critical for writing reusable, type-safe data access layers. Java 21 also introduced `SequencedCollections`, which provides a uniform contract for collections with a defined encounter order.

## Goal
Implement a generic, in-memory storage component (`InMemoryRepository<T, ID>`) that stores domain entities in insertion order and provides query capabilities using Java Streams.

## Requirements

### Generics & Interfaces
- Define an interface `Entity<ID>` with a method `ID id()`.
- Create a generic class `InMemoryRepository<T extends Entity<ID>, ID>` that acts as an in-memory database.

### Collections (SequencedCollections)
- Use a sequenced collection internally (e.g. `LinkedHashMap` or similar) to ensure entities are queried and returned in the exact order they were inserted.
- Implement methods:
  - `void save(T entity)`: Stores the entity. If an entity with the same ID already exists, it should be updated in-place without changing its insertion order.
  - `Optional<T> findById(ID id)`: Resolves an entity by ID.
  - `List<T> findAll()`: Returns all stored entities in insertion order.
  - `void deleteById(ID id)`: Deletes an entity.

### Streams & Query Operations
- Implement a method `List<T> find(Predicate<T> filter)` that returns filtered entities.
- Implement an aggregation method (e.g., grouping entities by some property or mapping them to a map of IDs to entities using `Collectors.toMap`).

## Definition of Done (DoD)
- [ ] A generic `InMemoryRepository<T, ID>` implementing insertion order.
- [ ] Proper use of `Optional` to handle missing entities.
- [ ] Comprehensive unit tests verifying:
  - Saving, updating, and retrieving entities.
  - Correct order of retrieval (`findAll()`).
  - Filtering entities using streams.
  - Handled edge cases (e.g., searching for non-existing IDs returning `Optional.empty()`).
