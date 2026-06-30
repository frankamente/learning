# Issue 006: Concurrency, Virtual Threads, and Thread Pinning

## Context
Java 21 introduced **Virtual Threads**, lightweight threads mapped to carrier platform threads. Unlike platform threads, virtual threads do not block the underlying OS thread during blocking I/O or sleep operations. However, they can get "pinned" (stuck) to the carrier thread if they execute inside a `synchronized` block.

## Goal
Implement a `ConcurrentTaskProcessor` and a suite of experiments that demonstrate:
1. **Throughput Differences**: Compare executing 100 blocking tasks (simulated with `Thread.sleep`) using a platform thread pool vs. a virtual thread executor.
2. **Virtual Thread Pinning**: Demonstrate thread pinning by running tasks inside a `synchronized` block, and show how to fix it by refactoring to Java's `ReentrantLock`.

## Requirements

### Task Processor
- Create a class `ConcurrentTaskProcessor` that:
  - Takes a list of `Runnable` or `Callable<T>` tasks.
  - Provides a method `List<T> executeWithPlatformPool(List<Callable<T>> tasks, int poolSize)` using `Executors.newFixedThreadPool`.
  - Provides a method `List<T> executeWithVirtualThreads(List<Callable<T>> tasks)` using `Executors.newVirtualThreadPerTaskExecutor`.

### Pinning Experiment
- Create a class `PinningDemo` with:
  - A method `void executePinnedTask()` that runs a virtual thread containing a `synchronized` block with a sleep operation.
  - A method `void executeUnpinnedTask()` that runs the same operation but refactored to use `java.util.concurrent.locks.ReentrantLock`.

## Definition of Done (DoD)
- [ ] `ConcurrentTaskProcessor` successfully switches between platform and virtual thread executors.
- [ ] `PinningDemo` implements both the synchronized (pinned) and ReentrantLock (unpinned) variations.
- [ ] Unit tests verifying:
  - Virtual thread execution of multiple blocking tasks finishes in a fraction of the time compared to a small platform thread pool.
  - Clean resource release using try-with-resources on `ExecutorService` (which implements `AutoCloseable` in Java 19+).
