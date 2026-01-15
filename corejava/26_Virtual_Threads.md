# Java Virtual Threads (Java 21) - Complete Guide

## Table of Contents
1. [What are Virtual Threads?](#1-what-are-virtual-threads)
2. [Creating Virtual Threads](#2-creating-virtual-threads)
3. [Virtual Thread Executor](#3-virtual-thread-executor)
4. [Platform vs Virtual Threads](#4-platform-vs-virtual-threads)
5. [How Virtual Threads Work](#5-how-virtual-threads-work)
6. [Best Practices](#6-best-practices)
7. [Limitations & Pitfalls](#7-limitations--pitfalls)
8. [Migration Guide](#8-migration-guide)
9. [Interview Questions](#9-interview-questions)

---

# 1. What are Virtual Threads?

## 🧒 Kid-Friendly Explanation

**Platform Threads = Heavy trucks 🚛**
- Expensive to create
- Use lots of fuel (memory ~1MB each)
- Limited number on road (thousands)

**Virtual Threads = Lightweight bicycles 🚲**
- Cheap to create
- Use little fuel (few KB each)
- Millions can exist!

## The Problem with Platform Threads

```java
// Traditional approach - limited by OS threads
ExecutorService executor = Executors.newFixedThreadPool(200);
// Can't create millions of threads - OS limit!

// Each blocked thread wastes resources
thread.sleep(1000);  // Thread doing nothing but consuming ~1MB memory
```

## The Solution: Virtual Threads

```java
// Create millions of virtual threads!
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 1_000_000; i++) {
        executor.submit(() -> {
            Thread.sleep(1000);  // Doesn't waste OS thread!
            return result;
        });
    }
}
```

---

# 2. Creating Virtual Threads

## Method 1: Thread.startVirtualThread()

```java
Thread vThread = Thread.startVirtualThread(() -> {
    System.out.println("Running in: " + Thread.currentThread());
});

vThread.join();  // Wait for completion
```

## Method 2: Thread.ofVirtual() Builder

```java
// Basic
Thread vThread = Thread.ofVirtual()
    .start(() -> System.out.println("Virtual thread!"));

// With name
Thread vThread = Thread.ofVirtual()
    .name("my-virtual-thread")
    .start(() -> doWork());

// With name prefix (for multiple)
Thread.Builder builder = Thread.ofVirtual().name("worker-", 0);
Thread t1 = builder.start(() -> task1());  // worker-0
Thread t2 = builder.start(() -> task2());  // worker-1
Thread t3 = builder.start(() -> task3());  // worker-2

// Unstarted (start later)
Thread vThread = Thread.ofVirtual()
    .name("deferred")
    .unstarted(() -> doWork());
vThread.start();  // Start when ready
```

## Method 3: Factory

```java
ThreadFactory factory = Thread.ofVirtual()
    .name("virtual-worker-", 0)
    .factory();

Thread t1 = factory.newThread(() -> task1());
Thread t2 = factory.newThread(() -> task2());
t1.start();
t2.start();
```

## Check if Virtual

```java
Thread current = Thread.currentThread();

if (current.isVirtual()) {
    System.out.println("Running in virtual thread!");
} else {
    System.out.println("Running in platform thread");
}
```

---

# 3. Virtual Thread Executor

## newVirtualThreadPerTaskExecutor()

Creates a new virtual thread for EVERY task!

```java
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
    // Submit 100,000 tasks - each gets its own virtual thread!
    List<Future<String>> futures = new ArrayList<>();
    
    for (int i = 0; i < 100_000; i++) {
        futures.add(executor.submit(() -> {
            Thread.sleep(1000);  // Simulate I/O
            return "Result";
        }));
    }
    
    // Collect results
    for (Future<String> future : futures) {
        String result = future.get();
    }
}  // Executor auto-closes, waits for tasks
```

## With Structured Concurrency (Preview in Java 21)

```java
// Structured Concurrency (--enable-preview)
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> user = scope.fork(() -> fetchUser());
    Future<String> order = scope.fork(() -> fetchOrder());
    
    scope.join();           // Wait for both
    scope.throwIfFailed();  // Propagate errors
    
    return new Response(user.resultNow(), order.resultNow());
}
```

---

# 4. Platform vs Virtual Threads

## Comparison Table

| Feature | Platform Thread | Virtual Thread |
|---------|-----------------|----------------|
| **Managed by** | OS | JVM |
| **Memory** | ~1MB stack | ~few KB |
| **Creation cost** | Expensive | Cheap |
| **Max count** | Thousands | Millions |
| **Blocking cost** | High (wastes thread) | Low (unmounts) |
| **Best for** | CPU-bound | I/O-bound |
| **Scheduling** | OS scheduler | JVM scheduler |

## When Blocking Happens

```
Platform Thread:
┌──────────────────────────────────────────────────┐
│ Thread blocks on I/O                              │
│ → OS thread sits idle                            │
│ → Memory wasted                                   │
│ → OS resources consumed                          │
└──────────────────────────────────────────────────┘

Virtual Thread:
┌──────────────────────────────────────────────────┐
│ Virtual thread blocks on I/O                      │
│ → JVM unmounts from carrier thread               │
│ → Carrier thread runs other virtual threads      │
│ → Virtual thread remounts when I/O completes     │
│ → Efficient use of resources!                    │
└──────────────────────────────────────────────────┘
```

## Creating Platform Threads (For Comparison)

```java
// Platform thread builder (Java 21+)
Thread platformThread = Thread.ofPlatform()
    .name("platform-worker")
    .start(() -> cpuIntensiveWork());

// Traditional platform thread
Thread oldStyle = new Thread(() -> work());
oldStyle.start();
```

---

# 5. How Virtual Threads Work

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    JVM Virtual Thread Scheduler              │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   Virtual Threads (millions possible)                        │
│   ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐        │
│   │VT1│ │VT2│ │VT3│ │VT4│ │VT5│ │VT6│ │VT7│ │VT8│  ...   │
│   └─┬─┘ └─┬─┘ └─┬─┘ └─┬─┘ └───┘ └───┘ └───┘ └───┘        │
│     │     │     │     │      (waiting/parked)              │
│     └──┬──┴──┬──┴──┬──┘                                    │
│        │     │     │        Mount/Unmount                  │
│        ▼     ▼     ▼                                       │
│   ┌─────────────────────────────────────────┐              │
│   │    Carrier Threads (Platform Threads)    │              │
│   │   ┌────┐  ┌────┐  ┌────┐  ┌────┐       │              │
│   │   │ CT1│  │ CT2│  │ CT3│  │ CT4│       │              │
│   │   └────┘  └────┘  └────┘  └────┘       │              │
│   │        (Usually = CPU cores)            │              │
│   └─────────────────────────────────────────┘              │
│                        │                                    │
│                        ▼                                    │
│   ┌─────────────────────────────────────────┐              │
│   │           Operating System               │              │
│   └─────────────────────────────────────────┘              │
└─────────────────────────────────────────────────────────────┘
```

## Mounting and Unmounting

```java
// Virtual thread running on carrier
virtualThread.run() {
    doComputation();     // Mounted on carrier
    
    socket.read();       // Blocking I/O!
    // → JVM UNMOUNTS virtual thread from carrier
    // → Carrier is FREE to run other virtual threads
    // → When I/O completes, virtual thread REMOUNTS
    
    processData();       // Back on a carrier (maybe different one)
}
```

## ForkJoinPool as Carrier

Virtual threads use `ForkJoinPool` for carrier threads by default.

```java
// Default carrier pool size = available processors
int carriers = Runtime.getRuntime().availableProcessors();

// Can configure with system property
// -Djdk.virtualThreadScheduler.parallelism=N
// -Djdk.virtualThreadScheduler.maxPoolSize=N
```

---

# 6. Best Practices

## 1. Use Virtual Threads for I/O-Bound Tasks

```java
// ✅ Good - I/O bound (HTTP calls, database, file I/O)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (String url : urls) {
        executor.submit(() -> httpClient.get(url));
    }
}

// ❌ Not beneficial - CPU bound
// Virtual threads don't help with CPU-intensive work
executor.submit(() -> computePrimes(1_000_000));
```

## 2. Don't Pool Virtual Threads

```java
// ❌ WRONG - Don't pool virtual threads!
ExecutorService pool = Executors.newFixedThreadPool(100);

// ✅ CORRECT - Create new virtual thread per task
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
```

## 3. Use Try-with-Resources

```java
// ✅ Auto-shutdown and wait
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(task1);
    executor.submit(task2);
}  // Waits for completion, then closes

// ❌ Manual management error-prone
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
// ... might forget to shutdown
```

## 4. Prefer Thread-Per-Request Model

```java
// ✅ Simple thread-per-request with virtual threads
void handleRequest(Request request) {
    Thread.startVirtualThread(() -> {
        var user = fetchUser(request.userId());     // I/O
        var orders = fetchOrders(request.userId()); // I/O
        var response = buildResponse(user, orders);
        sendResponse(response);
    });
}
```

## 5. Keep ThreadLocal Usage Minimal

```java
// ⚠️ ThreadLocal with millions of virtual threads = memory issue!
private static final ThreadLocal<ExpensiveObject> cache = 
    ThreadLocal.withInitial(ExpensiveObject::new);

// ✅ Better: Use request-scoped objects or ScopedValue (preview)
```

---

# 7. Limitations & Pitfalls

## Pinning - When Virtual Thread Can't Unmount

```java
// ⚠️ PINNING - Virtual thread stuck on carrier!

// 1. synchronized block during blocking
synchronized (lock) {
    socket.read();  // PINNED! Can't unmount
}

// 2. Native method/JNI during blocking
nativeMethod();  // PINNED if blocks

// ✅ Solution: Use ReentrantLock instead
private final ReentrantLock lock = new ReentrantLock();

lock.lock();
try {
    socket.read();  // Can unmount!
} finally {
    lock.unlock();
}
```

## Detecting Pinning

```bash
# JVM flag to detect pinning
-Djdk.tracePinnedThreads=full

# Or
-Djdk.tracePinnedThreads=short
```

## ThreadLocal Memory

```java
// ⚠️ Problem: Million virtual threads × ThreadLocal = OutOfMemory!
private static final ThreadLocal<byte[]> buffer = 
    ThreadLocal.withInitial(() -> new byte[1024 * 1024]);

// ✅ Solution: Use ScopedValue (Java 21 preview) or pass explicitly
```

## Not for CPU-Bound Tasks

```java
// ❌ No benefit for CPU-intensive work
for (int i = 0; i < 1000; i++) {
    Thread.startVirtualThread(() -> {
        computeHash();  // CPU bound - virtual threads don't help
    });
}

// ✅ Use platform threads for CPU-bound
ExecutorService cpuPool = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors()
);
```

---

# 8. Migration Guide

## From Thread Pool to Virtual Threads

```java
// Before (platform thread pool)
ExecutorService executor = Executors.newFixedThreadPool(200);
for (Request req : requests) {
    executor.submit(() -> handleRequest(req));
}
executor.shutdown();

// After (virtual threads)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (Request req : requests) {
        executor.submit(() -> handleRequest(req));
    }
}
```

## From synchronized to ReentrantLock

```java
// Before (causes pinning)
private final Object lock = new Object();
public void process() {
    synchronized (lock) {
        blockingOperation();
    }
}

// After (no pinning)
private final ReentrantLock lock = new ReentrantLock();
public void process() {
    lock.lock();
    try {
        blockingOperation();
    } finally {
        lock.unlock();
    }
}
```

## Checking Compatibility

```java
// Check if your code is virtual-thread friendly
public boolean isVirtualThreadFriendly() {
    // 1. No synchronized blocks around I/O
    // 2. No large ThreadLocal usage
    // 3. Primarily I/O-bound, not CPU-bound
    // 4. No native/JNI blocking calls
    return true;
}
```

---

# 9. Interview Questions

## Q1: What are virtual threads and why were they introduced?

**Answer:**
Virtual threads are lightweight threads managed by the JVM (not OS). Introduced to:
- Enable millions of concurrent threads
- Make blocking code efficient
- Simplify concurrent programming (thread-per-request)
- Reduce memory footprint (~KB vs ~MB for platform threads)

---

## Q2: Difference between virtual and platform threads?

**Answer:**
| Platform | Virtual |
|----------|---------|
| OS managed | JVM managed |
| ~1MB memory | ~KB memory |
| Thousands max | Millions possible |
| Blocking wastes thread | Blocking unmounts |
| Good for CPU-bound | Good for I/O-bound |

---

## Q3: What is pinning?

**Answer:**
Pinning occurs when a virtual thread cannot unmount from its carrier thread during blocking:
- Inside `synchronized` block
- During native/JNI calls

Solution: Use `ReentrantLock` instead of `synchronized`.

---

## Q4: When should you NOT use virtual threads?

**Answer:**
- CPU-bound tasks (no benefit)
- Code with synchronized blocks around I/O
- Heavy ThreadLocal usage
- Native/JNI code that blocks

---

## Q5: Should you pool virtual threads?

**Answer:**
**No!** Virtual threads are cheap to create. Use `newVirtualThreadPerTaskExecutor()` which creates a new virtual thread per task. Pooling defeats the purpose.

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│              VIRTUAL THREADS CHEAT SHEET                    │
├─────────────────────────────────────────────────────────────┤
│ CREATE VIRTUAL THREAD                                       │
│   Thread.startVirtualThread(() -> { })                      │
│   Thread.ofVirtual().start(() -> { })                       │
│   Thread.ofVirtual().name("name").start(() -> { })          │
├─────────────────────────────────────────────────────────────┤
│ EXECUTOR                                                    │
│   Executors.newVirtualThreadPerTaskExecutor()               │
│   // Creates NEW virtual thread per task                    │
│   // Use with try-with-resources!                           │
├─────────────────────────────────────────────────────────────┤
│ CHECK IF VIRTUAL                                            │
│   Thread.currentThread().isVirtual()                        │
├─────────────────────────────────────────────────────────────┤
│ AVOID PINNING                                               │
│   ❌ synchronized (lock) { blockingIO(); }                  │
│   ✅ lock.lock(); try { blockingIO(); } finally { unlock; } │
├─────────────────────────────────────────────────────────────┤
│ BEST FOR                                                    │
│   ✅ I/O-bound tasks (HTTP, DB, files)                      │
│   ✅ High concurrency (millions of tasks)                   │
│   ❌ CPU-bound tasks (use platform threads)                 │
└─────────────────────────────────────────────────────────────┘
```
