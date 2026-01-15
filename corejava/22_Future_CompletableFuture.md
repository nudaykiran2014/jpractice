# Java Future & CompletableFuture - Complete Guide

## Table of Contents
1. [What is Future?](#1-what-is-future)
2. [Future Interface](#2-future-interface)
3. [CompletableFuture Basics](#3-completablefuture-basics)
4. [Creating CompletableFutures](#4-creating-completablefutures)
5. [Transforming Results](#5-transforming-results)
6. [Combining Futures](#6-combining-futures)
7. [Exception Handling](#7-exception-handling)
8. [Timeouts (Java 9+)](#8-timeouts-java-9)
9. [Common Patterns](#9-common-patterns)
10. [Interview Questions](#10-interview-questions)

---

# 1. What is Future?

## 🧒 Kid-Friendly Explanation

**Future = A receipt for your order**

```
You: "I want a pizza" 🍕
Restaurant: "Here's ticket #42, we'll call you"
(You can do other things while waiting!)
Later: "Order #42 ready!"
You: Pick up pizza using ticket
```

```java
Future<Pizza> ticket = restaurant.orderPizza();  // Returns immediately
// Do other things...
Pizza pizza = ticket.get();  // Wait and get result
```

---

# 2. Future Interface

```java
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit) throws InterruptedException, 
                                              ExecutionException, 
                                              TimeoutException;
}
```

## Basic Usage

```java
ExecutorService executor = Executors.newSingleThreadExecutor();

Future<Integer> future = executor.submit(() -> {
    Thread.sleep(2000);
    return 42;
});

// Do other work while task runs...

// Get result (blocks if not ready)
try {
    Integer result = future.get();  // Blocks until complete
    System.out.println("Result: " + result);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
} catch (ExecutionException e) {
    System.out.println("Task failed: " + e.getCause());
}

executor.shutdown();
```

## Future Methods

```java
Future<String> future = executor.submit(task);

// Check status (non-blocking)
future.isDone();       // Completed (success, failure, or cancelled)?
future.isCancelled();  // Was cancelled?

// Get result
future.get();                         // Block forever
future.get(5, TimeUnit.SECONDS);      // Block with timeout

// Cancel
future.cancel(false);  // Cancel if not started
future.cancel(true);   // Interrupt if running
```

## Limitations of Future

| Problem | Description |
|---------|-------------|
| No chaining | Can't compose multiple futures |
| Blocking get() | Only way to get result |
| No exception handling | Must catch ExecutionException |
| No combining | Can't combine multiple results |
| No completion callback | Can't react when done |

**CompletableFuture solves all these!**

---

# 3. CompletableFuture Basics

## What is CompletableFuture?

- Implements `Future` AND `CompletionStage`
- Non-blocking, composable async operations
- Built-in exception handling
- Can be manually completed

## Creating Simple CompletableFuture

```java
// Already completed with value
CompletableFuture<String> completed = CompletableFuture.completedFuture("Hello");

// Failed future
CompletableFuture<String> failed = CompletableFuture.failedFuture(new RuntimeException("Oops"));

// Empty (to complete manually)
CompletableFuture<String> manual = new CompletableFuture<>();
manual.complete("Done");  // Complete manually
```

---

# 4. Creating CompletableFutures

## supplyAsync() - Run with Return Value

```java
// Uses ForkJoinPool.commonPool()
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    // Runs in background thread
    return "Result";
});

// With custom executor
ExecutorService executor = Executors.newFixedThreadPool(4);
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
    return "Result";
}, executor);
```

## runAsync() - Run Without Return Value

```java
CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
    System.out.println("Running in: " + Thread.currentThread().getName());
});

// With custom executor
CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
    doSomething();
}, executor);
```

## Getting Results

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");

// Blocking
String result = future.get();                    // Blocks, checked exceptions
String result2 = future.join();                  // Blocks, unchecked exception

// Non-blocking
String now = future.getNow("default");           // Default if not done

// With timeout (Java 9+)
String result3 = future.orTimeout(5, TimeUnit.SECONDS)
                       .join();
```

---

# 5. Transforming Results

## thenApply() - Transform Result (like map)

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> "hello")
    .thenApply(s -> s.toUpperCase())      // "HELLO"
    .thenApply(s -> s + " WORLD");        // "HELLO WORLD"

String result = future.join();  // "HELLO WORLD"
```

## thenAccept() - Consume Result (no return)

```java
CompletableFuture.supplyAsync(() -> "Hello")
    .thenAccept(s -> System.out.println("Result: " + s));
// Prints: Result: Hello
```

## thenRun() - Run After Completion (ignore result)

```java
CompletableFuture.supplyAsync(() -> "Hello")
    .thenRun(() -> System.out.println("Done!"));
// Prints: Done! (doesn't use the result)
```

## thenCompose() - Chain Async Operations (like flatMap)

```java
// When transformation returns another CompletableFuture
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> 42)
    .thenCompose(id -> fetchUserById(id));  // Returns CompletableFuture<String>

// Compare:
// thenApply:   T -> U           (sync transformation)
// thenCompose: T -> CompletableFuture<U>  (async transformation)
```

## Async Variants

```java
// Sync - runs in same thread or completing thread
future.thenApply(s -> s.toUpperCase());

// Async - runs in common pool
future.thenApplyAsync(s -> s.toUpperCase());

// Async with custom executor
future.thenApplyAsync(s -> s.toUpperCase(), myExecutor);
```

---

# 6. Combining Futures

## thenCombine() - Combine Two Futures

```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");

CompletableFuture<String> combined = future1.thenCombine(future2, 
    (s1, s2) -> s1 + " " + s2);

String result = combined.join();  // "Hello World"
```

## thenAcceptBoth() - Consume Two Results

```java
future1.thenAcceptBoth(future2, (s1, s2) -> {
    System.out.println(s1 + " " + s2);
});
```

## runAfterBoth() - Run After Both Complete

```java
future1.runAfterBoth(future2, () -> {
    System.out.println("Both completed!");
});
```

## allOf() - Wait for All Futures

```java
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "C");

// Wait for all
CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2, f3);
all.join();

// Get all results
List<String> results = Stream.of(f1, f2, f3)
    .map(CompletableFuture::join)
    .toList();
// ["A", "B", "C"]
```

## anyOf() - First to Complete

```java
CompletableFuture<Object> fastest = CompletableFuture.anyOf(f1, f2, f3);
Object firstResult = fastest.join();  // Whichever finishes first
```

## applyToEither() - Use First Available

```java
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
    sleep(2000);
    return "Slow";
});
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
    sleep(1000);
    return "Fast";
});

String result = f1.applyToEither(f2, s -> s.toUpperCase()).join();
// "FAST" (whichever completes first)
```

---

# 7. Exception Handling

## exceptionally() - Handle Exceptions

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        if (Math.random() > 0.5) throw new RuntimeException("Oops!");
        return "Success";
    })
    .exceptionally(ex -> {
        System.out.println("Error: " + ex.getMessage());
        return "Default";  // Recovery value
    });
```

## handle() - Handle Success or Failure

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        if (Math.random() > 0.5) throw new RuntimeException("Oops!");
        return "Success";
    })
    .handle((result, ex) -> {
        if (ex != null) {
            return "Error: " + ex.getMessage();
        }
        return "Result: " + result;
    });
```

## whenComplete() - React to Completion (doesn't change result)

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> "Hello")
    .whenComplete((result, ex) -> {
        if (ex != null) {
            System.out.println("Failed: " + ex);
        } else {
            System.out.println("Completed: " + result);
        }
    });
// Returns same result/exception
```

## Comparison

| Method | Handles Success | Handles Error | Returns |
|--------|-----------------|---------------|---------|
| `exceptionally` | ❌ | ✅ | Recovery value |
| `handle` | ✅ | ✅ | Transformed value |
| `whenComplete` | ✅ | ✅ | Same value/error |

## Exception Propagation

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        throw new RuntimeException("Step 1 failed");
    })
    .thenApply(s -> s.toUpperCase())    // Skipped!
    .thenApply(s -> s + "!")            // Skipped!
    .exceptionally(ex -> "Recovered");  // Catches exception
```

---

# 8. Timeouts (Java 9+)

## orTimeout() - Fail on Timeout

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        sleep(10000);  // Takes 10 seconds
        return "Done";
    })
    .orTimeout(2, TimeUnit.SECONDS);  // Timeout after 2 seconds

try {
    future.join();
} catch (CompletionException e) {
    // TimeoutException wrapped in CompletionException
}
```

## completeOnTimeout() - Default on Timeout

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        sleep(10000);
        return "Done";
    })
    .completeOnTimeout("Timeout!", 2, TimeUnit.SECONDS);

String result = future.join();  // "Timeout!" (after 2 seconds)
```

---

# 9. Common Patterns

## Parallel API Calls

```java
public UserProfile getUserProfile(long userId) {
    CompletableFuture<User> userFuture = CompletableFuture
        .supplyAsync(() -> userService.getUser(userId));
    
    CompletableFuture<List<Order>> ordersFuture = CompletableFuture
        .supplyAsync(() -> orderService.getOrders(userId));
    
    CompletableFuture<List<Address>> addressFuture = CompletableFuture
        .supplyAsync(() -> addressService.getAddresses(userId));
    
    return userFuture.thenCombine(ordersFuture, (user, orders) -> {
        return new UserProfile(user, orders, null);
    }).thenCombine(addressFuture, (profile, addresses) -> {
        profile.setAddresses(addresses);
        return profile;
    }).join();
}
```

## Retry Pattern

```java
public <T> CompletableFuture<T> retry(Supplier<T> task, int maxRetries) {
    CompletableFuture<T> future = CompletableFuture.supplyAsync(task);
    
    for (int i = 0; i < maxRetries; i++) {
        future = future.exceptionally(ex -> {
            System.out.println("Retrying...");
            return task.get();
        });
    }
    
    return future;
}
```

## Batch Processing

```java
public List<Result> processBatch(List<Long> ids) {
    List<CompletableFuture<Result>> futures = ids.stream()
        .map(id -> CompletableFuture.supplyAsync(() -> process(id)))
        .toList();
    
    return futures.stream()
        .map(CompletableFuture::join)
        .toList();
}
```

## First Successful (Ignore Failures)

```java
public CompletableFuture<String> getFirstSuccess(List<Supplier<String>> tasks) {
    CompletableFuture<String> result = new CompletableFuture<>();
    AtomicInteger remaining = new AtomicInteger(tasks.size());
    
    for (Supplier<String> task : tasks) {
        CompletableFuture.supplyAsync(task)
            .thenAccept(result::complete)  // First success wins
            .exceptionally(ex -> {
                if (remaining.decrementAndGet() == 0) {
                    result.completeExceptionally(new RuntimeException("All failed"));
                }
                return null;
            });
    }
    
    return result;
}
```

---

# 10. Interview Questions

## Q1: Difference between Future and CompletableFuture?

**Answer:**
| Future | CompletableFuture |
|--------|-------------------|
| Blocking get() only | Non-blocking callbacks |
| No chaining | Fluent chaining |
| No exception handling | Built-in exception handling |
| No combining | allOf(), anyOf(), combine() |
| Cannot complete manually | Can complete manually |

---

## Q2: Difference between thenApply and thenCompose?

**Answer:**
- **thenApply**: Sync transformation `T -> U`
- **thenCompose**: Async transformation `T -> CompletableFuture<U>`

```java
// thenApply for simple transformation
future.thenApply(s -> s.toUpperCase());

// thenCompose when transformation is async
future.thenCompose(id -> fetchFromDatabase(id));
```

Like `map` vs `flatMap` in streams!

---

## Q3: How to handle exceptions in CompletableFuture?

**Answer:**
```java
// exceptionally - recover from error
.exceptionally(ex -> "default")

// handle - handle both success and failure
.handle((result, ex) -> ex != null ? "error" : result)

// whenComplete - side effects only
.whenComplete((result, ex) -> log(result, ex))
```

---

## Q4: What is the default executor for CompletableFuture?

**Answer:**
`ForkJoinPool.commonPool()` is used by default for async methods. You can provide a custom executor:
```java
CompletableFuture.supplyAsync(task, myExecutor);
```

---

## Q5: How to wait for multiple futures?

**Answer:**
```java
// Wait for ALL
CompletableFuture.allOf(f1, f2, f3).join();

// Get first completed
CompletableFuture.anyOf(f1, f2, f3).join();
```

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│            COMPLETABLEFUTURE CHEAT SHEET                    │
├─────────────────────────────────────────────────────────────┤
│ CREATE                                                      │
│   supplyAsync(() -> value)     // With result               │
│   runAsync(() -> { })          // No result                 │
│   completedFuture(value)       // Already complete          │
├─────────────────────────────────────────────────────────────┤
│ TRANSFORM                                                   │
│   thenApply(x -> y)           // Transform result           │
│   thenAccept(x -> { })        // Consume result             │
│   thenRun(() -> { })          // Run after, ignore result   │
│   thenCompose(x -> cf)        // Chain async operation      │
├─────────────────────────────────────────────────────────────┤
│ COMBINE                                                     │
│   thenCombine(cf, (a,b) -> c) // Combine two results        │
│   allOf(cf1, cf2, ...)        // Wait for all               │
│   anyOf(cf1, cf2, ...)        // First to complete          │
├─────────────────────────────────────────────────────────────┤
│ EXCEPTIONS                                                  │
│   exceptionally(ex -> value)  // Handle error               │
│   handle((r, ex) -> value)    // Handle both                │
│   whenComplete((r, ex) -> {}) // Side effect                │
├─────────────────────────────────────────────────────────────┤
│ GET RESULT                                                  │
│   join()                      // Block (unchecked ex)       │
│   get()                       // Block (checked ex)         │
│   getNow(default)             // Non-blocking               │
└─────────────────────────────────────────────────────────────┘
```
