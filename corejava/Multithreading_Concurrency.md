# Java Multithreading & Concurrency - Complete Guide

## Table of Contents
1. [What is a Thread?](#1-what-is-a-thread)
2. [Creating Threads](#2-creating-threads)
3. [Thread Lifecycle](#3-thread-lifecycle)
4. [Thread Methods](#4-thread-methods)
5. [synchronized Keyword](#5-synchronized-keyword)
6. [volatile Keyword](#6-volatile-keyword)
7. [wait(), notify(), notifyAll()](#7-wait-notify-notifyall)
8. [Locks](#8-locks)
   - ReentrantLock
   - ReadWriteLock
   - StampedLock
9. [Deadlock, Livelock, Starvation](#9-deadlock-livelock-starvation)
10. [Executor Service & Thread Pools](#10-executor-service--thread-pools)
11. [Callable & Future](#11-callable--future)
12. [CompletableFuture](#12-completablefuture)
13. [Atomic Variables](#13-atomic-variables)
14. [Concurrent Collections](#14-concurrent-collections)
15. [Semaphore, CountDownLatch, CyclicBarrier](#15-semaphore-countdownlatch-cyclicbarrier)
16. [Virtual Threads (Java 21)](#16-virtual-threads-java-21)
17. [Interview Questions](#17-interview-questions)

---

# 1. What is a Thread?

## Kid-Friendly Explanation 🧒

**Imagine a Restaurant Kitchen:**
- **Single Thread** = 1 chef doing everything alone
  - Takes order → Cooks → Serves → Cleans → Next order
  - SLOW! Customers wait forever!

- **Multi Thread** = Multiple chefs working together
  - Chef 1: Takes orders
  - Chef 2: Cooks appetizers
  - Chef 3: Cooks main course
  - Chef 4: Serves food
  - FAST! Multiple customers served at once!

## Technical Definition

- **Process** = Running program (e.g., Chrome browser)
- **Thread** = Lightweight sub-process within a process
- Multiple threads share same memory space
- Each thread has its own:
  - Stack (local variables)
  - Program counter (current instruction)

```
┌─────────────────────────────────────────┐
│              PROCESS                     │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐   │
│  │ Thread1 │ │ Thread2 │ │ Thread3 │   │
│  │ Stack   │ │ Stack   │ │ Stack   │   │
│  └────┬────┘ └────┬────┘ └────┬────┘   │
│       │          │          │          │
│       └──────────┴──────────┘          │
│              SHARED HEAP                │
│         (Objects, Arrays)               │
└─────────────────────────────────────────┘
```

---

# 2. Creating Threads

## Method 1: Extend Thread Class

```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running: " + Thread.currentThread().getName());
    }
}

// Usage
MyThread t = new MyThread();
t.start();  // NOT run()!
```

## Method 2: Implement Runnable Interface (PREFERRED!)

```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable running: " + Thread.currentThread().getName());
    }
}

// Usage
Thread t = new Thread(new MyRunnable());
t.start();
```

## Method 3: Lambda Expression (Java 8+)

```java
Thread t = new Thread(() -> {
    System.out.println("Lambda thread running!");
});
t.start();
```

## Method 4: Callable (Returns Value)

```java
Callable<Integer> task = () -> {
    return 42;
};
// Used with ExecutorService
```

## Why Runnable is Better than Thread?

| Thread Class | Runnable Interface |
|--------------|-------------------|
| Single inheritance (can't extend other class) | Can implement multiple interfaces |
| Tightly coupled | Loosely coupled (separation of concern) |
| Can't be used with ExecutorService easily | Works with ExecutorService |

---

# 3. Thread Lifecycle

## Kid-Friendly Explanation 🧒

Think of a thread like a **Student in School**:

1. **NEW** = Student enrolled but not in class yet
2. **RUNNABLE** = Student in class, ready to answer questions
3. **RUNNING** = Student actually answering a question
4. **BLOCKED/WAITING** = Student waiting for teacher's attention
5. **TERMINATED** = Student graduated (done)

## Thread States Diagram

```
        ┌─────────┐
        │   NEW   │  ← Thread created (new Thread())
        └────┬────┘
             │ start()
        ┌────▼────┐
        │RUNNABLE │  ← Ready to run, waiting for CPU
        └────┬────┘
             │ CPU scheduler picks
        ┌────▼────┐
        │ RUNNING │  ← Actually executing run()
        └────┬────┘
             │
    ┌────────┼────────────────────┐
    │        │                    │
    ▼        ▼                    ▼
┌───────┐ ┌─────────┐      ┌────────────┐
│BLOCKED│ │ WAITING │      │ TERMINATED │
│       │ │ TIMED_  │      │   (Dead)   │
│       │ │ WAITING │      └────────────┘
└───┬───┘ └────┬────┘
    │          │
    └──────────┘
      Back to RUNNABLE
```

## State Descriptions

| State | Description | How to Enter |
|-------|-------------|--------------|
| NEW | Thread created, not started | `new Thread()` |
| RUNNABLE | Ready to run or running | `start()` |
| BLOCKED | Waiting for monitor lock | Trying to enter `synchronized` |
| WAITING | Waiting indefinitely | `wait()`, `join()` |
| TIMED_WAITING | Waiting with timeout | `sleep(ms)`, `wait(ms)` |
| TERMINATED | Finished execution | `run()` completes |

---

# 4. Thread Methods

## Essential Methods

### start() vs run()

```java
Thread t = new Thread(() -> System.out.println("Hello"));

t.run();    // ❌ WRONG! Runs in CURRENT thread (no new thread!)
t.start();  // ✅ CORRECT! Creates NEW thread and calls run()
```

### sleep() - Pause the Thread

```java
// Kid explanation: Thread takes a nap!
try {
    Thread.sleep(1000);  // Sleep for 1 second (1000 ms)
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

### join() - Wait for Thread to Finish

```java
// Kid explanation: "Wait for your friend before going!"
Thread t = new Thread(() -> {
    // Some long task
});
t.start();
t.join();  // Main thread waits here until t finishes
System.out.println("t is done, now I continue");
```

### yield() - Hint to Give Up CPU

```java
// Kid explanation: "Let others have a turn!"
Thread.yield();  // Suggests scheduler to let other threads run
// Just a hint! No guarantee.
```

### interrupt() - Interrupt a Thread

```java
Thread t = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        // Do work
    }
    System.out.println("I was interrupted!");
});
t.start();
t.interrupt();  // Sets interrupt flag
```

### setDaemon() - Background Thread

```java
// Kid explanation: Daemon = Helper that dies when main work is done
Thread t = new Thread(() -> {
    while (true) {
        System.out.println("Background task...");
    }
});
t.setDaemon(true);  // Will stop when main thread exits
t.start();
```

---

# 5. synchronized Keyword

## Kid-Friendly Explanation 🧒

**Imagine a Bathroom with ONE Key 🔑🚽**

- Only person WITH the key can enter
- Others must WAIT outside
- When done, pass the key to next person

`synchronized` = The key!

## The Problem Without synchronized

```java
class Counter {
    int count = 0;
    
    void increment() {
        count++;  // NOT atomic! (read → add → write)
    }
}

// Two threads incrementing 1000 times each
// Expected: 2000
// Actual: Maybe 1500, 1800, varies! (RACE CONDITION)
```

## The Solution: synchronized

### Method Level

```java
class Counter {
    int count = 0;
    
    synchronized void increment() {
        count++;  // Only ONE thread at a time
    }
}
```

### Block Level (More Granular)

```java
class Counter {
    int count = 0;
    private final Object lock = new Object();
    
    void increment() {
        // Some non-critical code...
        
        synchronized (lock) {  // Only this part is locked
            count++;
        }
        
        // More non-critical code...
    }
}
```

### Static synchronized

```java
class Counter {
    static int count = 0;
    
    // Locks on Counter.class, not instance
    static synchronized void increment() {
        count++;
    }
}
```

## What Does synchronized Lock On?

| Type | Locks On |
|------|----------|
| Instance method | `this` object |
| Static method | `Class` object (e.g., `Counter.class`) |
| Block | Object specified in `synchronized(obj)` |

## Reentrant Nature

```java
synchronized void methodA() {
    methodB();  // Same thread can enter another synchronized method
}

synchronized void methodB() {
    // Works! Same thread already holds the lock
}
```

---

# 6. volatile Keyword

## Kid-Friendly Explanation 🧒

**Imagine a Bulletin Board 📋**

- Without volatile: Each person has their own notebook (CPU cache)
  - Person A writes "Meeting at 3pm" in their notebook
  - Person B doesn't see it (still has old info)

- With volatile: Everyone reads/writes on shared bulletin board
  - Changes are visible to everyone immediately!

## The Problem Without volatile

```java
class Worker {
    boolean running = true;
    
    void run() {
        while (running) {  // May never see change!
            // do work
        }
    }
    
    void stop() {
        running = false;  // Other thread may not see this!
    }
}
```

## The Solution: volatile

```java
class Worker {
    volatile boolean running = true;  // ✅ Changes visible to all threads
    
    void run() {
        while (running) {  // Will see the change!
            // do work
        }
    }
    
    void stop() {
        running = false;  // Written directly to main memory
    }
}
```

## volatile vs synchronized

| volatile | synchronized |
|----------|--------------|
| Only visibility | Visibility + Atomicity |
| No blocking | Blocks other threads |
| Only for single read/write | For compound operations |
| `count = 5` ✅ | `count++` needs synchronized |

## When volatile is NOT Enough

```java
volatile int count = 0;

void increment() {
    count++;  // ❌ Still NOT thread-safe!
    // count++ = read + add + write (3 operations!)
}
```

---

# 7. wait(), notify(), notifyAll()

## Kid-Friendly Explanation 🧒

**Imagine a Restaurant 🍽️**

- **Customer (Consumer)**: "I'll `wait()` for my food"
- **Chef (Producer)**: Makes food, then `notify()` "Food ready!"
- Customer wakes up and eats

## Rules

1. MUST be called inside `synchronized` block
2. `wait()` releases the lock and waits
3. `notify()` wakes up ONE waiting thread
4. `notifyAll()` wakes up ALL waiting threads

## Producer-Consumer Example

```java
class SharedBuffer {
    private int data;
    private boolean hasData = false;
    
    // Producer puts data
    synchronized void produce(int value) throws InterruptedException {
        while (hasData) {
            wait();  // Wait until consumer takes data
        }
        data = value;
        hasData = true;
        System.out.println("Produced: " + value);
        notify();  // Wake up consumer
    }
    
    // Consumer takes data
    synchronized int consume() throws InterruptedException {
        while (!hasData) {
            wait();  // Wait until producer adds data
        }
        hasData = false;
        System.out.println("Consumed: " + data);
        notify();  // Wake up producer
        return data;
    }
}
```

## Why while Loop Instead of if?

```java
// ❌ BAD - Spurious wakeup can cause issues
synchronized void method() {
    if (condition) {
        wait();
    }
    // May proceed even if condition is still false!
}

// ✅ GOOD - Always recheck condition
synchronized void method() {
    while (condition) {
        wait();
    }
    // Guaranteed condition is false
}
```

## wait() vs sleep()

| wait() | sleep() |
|--------|---------|
| Releases lock | Keeps lock |
| Must be in synchronized | Anywhere |
| Wakes by notify/notifyAll | Wakes after time |
| Object method | Thread method |

---

# 8. Locks

## Why Locks Over synchronized?

| synchronized | Lock |
|--------------|------|
| Can't try to acquire | `tryLock()` available |
| Can't timeout | `tryLock(time)` available |
| Can't interrupt | `lockInterruptibly()` |
| No fairness | Fair lock option |
| Auto release | Manual unlock (use finally!) |

---

## 8.1 ReentrantLock

### Kid-Friendly Explanation 🧒

Like a **Smarter Bathroom Key** with extra features:
- Can TRY to enter without waiting forever
- Can set a timeout
- Can check if someone is inside

### Basic Usage

```java
import java.util.concurrent.locks.ReentrantLock;

class Counter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();
    
    void increment() {
        lock.lock();  // Acquire lock
        try {
            count++;
        } finally {
            lock.unlock();  // ALWAYS in finally!
        }
    }
}
```

### tryLock() - Try Without Blocking

```java
if (lock.tryLock()) {
    try {
        // Got the lock, do work
    } finally {
        lock.unlock();
    }
} else {
    // Couldn't get lock, do something else
    System.out.println("Lock was busy, skipping...");
}
```

### tryLock with Timeout

```java
if (lock.tryLock(5, TimeUnit.SECONDS)) {
    try {
        // Got the lock within 5 seconds
    } finally {
        lock.unlock();
    }
} else {
    // Couldn't get lock in 5 seconds
}
```

### Fair Lock

```java
// Fair = Longest waiting thread gets lock first (FIFO)
ReentrantLock fairLock = new ReentrantLock(true);

// Unfair (default) = Any waiting thread can get lock
ReentrantLock unfairLock = new ReentrantLock(false);
```

### Reentrant Nature

```java
lock.lock();      // Hold count = 1
lock.lock();      // Hold count = 2 (same thread can lock again!)
lock.unlock();    // Hold count = 1
lock.unlock();    // Hold count = 0 (released)
```

---

## 8.2 ReadWriteLock

### Kid-Friendly Explanation 🧒

**Imagine a Library 📚**

- **Reading**: Many people can read the SAME book simultaneously
- **Writing**: Only ONE person can write, and no one else can read during that time

This is more efficient than synchronized (which blocks everyone)!

### How It Works

```
Multiple Readers  ✅  Can read at same time
Reader + Writer   ❌  Can't happen together
Multiple Writers  ❌  Only one writer at a time
```

### Code Example

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Cache {
    private Map<String, String> data = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    
    // Multiple threads can read simultaneously
    String get(String key) {
        readLock.lock();
        try {
            return data.get(key);
        } finally {
            readLock.unlock();
        }
    }
    
    // Only one thread can write at a time
    void put(String key, String value) {
        writeLock.lock();
        try {
            data.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }
}
```

### When to Use?

- **Read-heavy workloads**: Many reads, few writes → Use ReadWriteLock
- **Write-heavy workloads**: Many writes → Just use ReentrantLock

---

## 8.3 StampedLock (Java 8+)

### Kid-Friendly Explanation 🧒

Like ReadWriteLock but with a **special ticket (stamp)** for optimistic reading:
- First TRY to read without locking
- If someone wrote during read, retry with actual lock

### Code Example

```java
import java.util.concurrent.locks.StampedLock;

class Point {
    private double x, y;
    private final StampedLock lock = new StampedLock();
    
    // Optimistic Read (no actual lock!)
    double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();  // Get stamp, no lock
        double currentX = x;
        double currentY = y;
        
        if (!lock.validate(stamp)) {  // Check if someone wrote
            // Someone wrote! Get real read lock
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
    
    // Write
    void move(double newX, double newY) {
        long stamp = lock.writeLock();
        try {
            x = newX;
            y = newY;
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
```

### Lock Comparison

| Feature | synchronized | ReentrantLock | ReadWriteLock | StampedLock |
|---------|-------------|---------------|---------------|-------------|
| Fairness | No | Optional | Optional | No |
| tryLock | No | Yes | Yes | Yes |
| Read/Write separation | No | No | Yes | Yes |
| Optimistic reading | No | No | No | Yes |
| Reentrant | Yes | Yes | Yes | No |

---

# 9. Deadlock, Livelock, Starvation

## 9.1 Deadlock

### Kid-Friendly Explanation 🧒

**Two Kids with Toys:**
- Kid A has truck, wants doll
- Kid B has doll, wants truck
- Both refuse to give until they get what they want
- NOBODY MOVES! **DEADLOCK!**

### Code Example

```java
Object lock1 = new Object();
Object lock2 = new Object();

// Thread 1
synchronized (lock1) {
    Thread.sleep(100);
    synchronized (lock2) {  // Waiting for lock2
        // Never reaches here!
    }
}

// Thread 2
synchronized (lock2) {
    Thread.sleep(100);
    synchronized (lock1) {  // Waiting for lock1
        // Never reaches here!
    }
}
```

### How to Prevent Deadlock?

1. **Lock Ordering**: Always acquire locks in same order
```java
// Always lock1 first, then lock2
synchronized (lock1) {
    synchronized (lock2) {
        // safe
    }
}
```

2. **tryLock with Timeout**
```java
if (lock1.tryLock(1, TimeUnit.SECONDS)) {
    if (lock2.tryLock(1, TimeUnit.SECONDS)) {
        // Got both locks
    } else {
        lock1.unlock();  // Release and retry
    }
}
```

3. **Avoid Nested Locks** when possible

---

## 9.2 Livelock

### Kid-Friendly Explanation 🧒

**Two People in Hallway:**
- Person A moves left to let B pass
- Person B moves left to let A pass
- Both keep moving same direction!
- They're MOVING but not PROGRESSING!

### Difference from Deadlock

| Deadlock | Livelock |
|----------|----------|
| Threads blocked (not moving) | Threads active (moving) |
| Waiting for each other | Responding to each other |
| No CPU usage | High CPU usage |

---

## 9.3 Starvation

### Kid-Friendly Explanation 🧒

**Lunch Queue:**
- Big kids keep cutting in line
- Small kid never gets food
- Small kid **STARVES!**

### Cause

- Low priority threads never get CPU time
- High priority threads dominate

### Solution

- Use fair locks: `new ReentrantLock(true)`
- Avoid setting thread priorities

---

# 10. Executor Service & Thread Pools

## Kid-Friendly Explanation 🧒

**Imagine a Taxi Company 🚕**

- **Without Pool**: Buy new car for each ride, throw away after (EXPENSIVE!)
- **With Pool**: Have 10 cars, reuse them for all rides (EFFICIENT!)

Thread Pool = Reuse threads instead of creating new ones each time!

## Why Thread Pools?

| Creating Threads | Thread Pool |
|-----------------|-------------|
| Expensive (OS overhead) | Reuses existing threads |
| No limit (can crash) | Fixed limit |
| No task queuing | Queue tasks if busy |

## Types of Thread Pools

```java
// 1. Fixed Thread Pool - Fixed number of threads
ExecutorService fixed = Executors.newFixedThreadPool(5);

// 2. Cached Thread Pool - Creates threads as needed, reuses
ExecutorService cached = Executors.newCachedThreadPool();

// 3. Single Thread - Only 1 thread (tasks execute sequentially)
ExecutorService single = Executors.newSingleThreadExecutor();

// 4. Scheduled Thread Pool - For delayed/periodic tasks
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(3);
```

## Basic Usage

```java
ExecutorService executor = Executors.newFixedThreadPool(3);

// Submit tasks
executor.submit(() -> System.out.println("Task 1"));
executor.submit(() -> System.out.println("Task 2"));
executor.submit(() -> System.out.println("Task 3"));
executor.submit(() -> System.out.println("Task 4"));  // Waits in queue
executor.submit(() -> System.out.println("Task 5"));  // Waits in queue

// Shutdown (IMPORTANT!)
executor.shutdown();  // Finish pending tasks, no new tasks
// or
executor.shutdownNow();  // Stop everything immediately
```

## Scheduled Executor

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

// Run after delay
scheduler.schedule(() -> System.out.println("Delayed!"), 5, TimeUnit.SECONDS);

// Run periodically (fixed rate)
scheduler.scheduleAtFixedRate(
    () -> System.out.println("Every 2 seconds"),
    0,  // Initial delay
    2,  // Period
    TimeUnit.SECONDS
);

// Run periodically (fixed delay between end and start)
scheduler.scheduleWithFixedDelay(
    () -> System.out.println("2 seconds after previous ends"),
    0,
    2,
    TimeUnit.SECONDS
);
```

---

# 11. Callable & Future

## Kid-Friendly Explanation 🧒

**Runnable vs Callable:**
- **Runnable**: "Do this task" (no response)
- **Callable**: "Do this task and TELL ME the result"

**Future** = A promise/receipt for the result
- "Your pizza will be ready, here's your receipt"
- Check receipt later to get pizza

## Callable - Task That Returns Value

```java
Callable<Integer> task = () -> {
    Thread.sleep(1000);
    return 42;  // Returns a value!
};
```

## Future - Handle to Get Result

```java
ExecutorService executor = Executors.newSingleThreadExecutor();

Future<Integer> future = executor.submit(() -> {
    Thread.sleep(1000);
    return 42;
});

// Do other work while task runs...
System.out.println("Doing other work...");

// Get result (blocks until ready)
Integer result = future.get();  // Waits and returns 42
System.out.println("Result: " + result);

executor.shutdown();
```

## Future Methods

```java
future.get();                         // Block until result
future.get(5, TimeUnit.SECONDS);      // Block with timeout
future.isDone();                      // Check if complete
future.cancel(true);                  // Cancel the task
future.isCancelled();                 // Check if cancelled
```

## Runnable vs Callable

| Runnable | Callable |
|----------|----------|
| `void run()` | `V call() throws Exception` |
| No return value | Returns value |
| Can't throw checked exceptions | Can throw exceptions |

---

# 12. CompletableFuture

## Kid-Friendly Explanation 🧒

**Future** = "Wait here until pizza is ready" (BLOCKING)
**CompletableFuture** = "We'll CALL you when pizza is ready" (NON-BLOCKING)

You can chain actions: "When pizza ready → add toppings → deliver"

## Basic Usage

```java
// Create and run async task
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    sleep(1000);
    return "Hello";
});

// Non-blocking: attach callback
future.thenAccept(result -> System.out.println("Got: " + result));

// Or block and get result
String result = future.get();
```

## Chaining Operations

```java
CompletableFuture.supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World")        // Transform result
    .thenApply(String::toUpperCase)       // Transform again
    .thenAccept(System.out::println);     // Consume result
// Output: HELLO WORLD
```

## Combining Futures

```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");

// Combine two futures
future1.thenCombine(future2, (s1, s2) -> s1 + " " + s2)
    .thenAccept(System.out::println);  // "Hello World"

// Wait for all
CompletableFuture.allOf(future1, future2).join();

// Wait for any
CompletableFuture.anyOf(future1, future2).join();
```

## Error Handling

```java
CompletableFuture.supplyAsync(() -> {
    if (true) throw new RuntimeException("Error!");
    return "Success";
})
.exceptionally(ex -> "Failed: " + ex.getMessage())
.thenAccept(System.out::println);  // "Failed: Error!"
```

## Common Methods

| Method | Description |
|--------|-------------|
| `supplyAsync()` | Run task async, return result |
| `runAsync()` | Run task async, no result |
| `thenApply()` | Transform result |
| `thenAccept()` | Consume result |
| `thenRun()` | Run action after completion |
| `thenCompose()` | Flatten nested futures |
| `thenCombine()` | Combine two futures |
| `exceptionally()` | Handle errors |
| `handle()` | Handle result OR error |

---

# 13. Atomic Variables

## Kid-Friendly Explanation 🧒

**Problem with count++:**
```
count++ is actually 3 steps:
1. READ count (value: 5)
2. ADD 1 (value: 6)
3. WRITE back (count = 6)

Another thread can interfere between steps!
```

**Atomic** = All-or-nothing, can't be interrupted

## AtomicInteger Example

```java
import java.util.concurrent.atomic.AtomicInteger;

class Counter {
    private AtomicInteger count = new AtomicInteger(0);
    
    void increment() {
        count.incrementAndGet();  // Atomic! Thread-safe!
    }
    
    int get() {
        return count.get();
    }
}
```

## Atomic Classes

| Class | For Type |
|-------|----------|
| `AtomicInteger` | int |
| `AtomicLong` | long |
| `AtomicBoolean` | boolean |
| `AtomicReference<V>` | Object reference |
| `AtomicIntegerArray` | int[] |

## Common Methods

```java
AtomicInteger atom = new AtomicInteger(0);

atom.get();                    // Read value
atom.set(10);                  // Set value
atom.incrementAndGet();        // ++atom (returns new value)
atom.getAndIncrement();        // atom++ (returns old value)
atom.decrementAndGet();        // --atom
atom.addAndGet(5);             // atom += 5
atom.compareAndSet(10, 20);    // If value is 10, set to 20
```

## Compare-And-Set (CAS)

```java
AtomicInteger atom = new AtomicInteger(10);

// Only update if current value is 10
boolean success = atom.compareAndSet(10, 20);  // true, now 20

// This will fail because value is now 20, not 10
boolean fail = atom.compareAndSet(10, 30);  // false, still 20
```

---

# 14. Concurrent Collections

## Why Regular Collections Fail?

```java
// ❌ NOT thread-safe!
List<String> list = new ArrayList<>();
// Two threads adding simultaneously = CRASH or data loss!

// ❌ ConcurrentModificationException
for (String s : list) {
    list.remove(s);  // Modifying while iterating!
}
```

## Thread-Safe Collections

### ConcurrentHashMap

```java
// ✅ Thread-safe Map
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

map.put("A", 1);
map.putIfAbsent("B", 2);       // Only if key doesn't exist
map.computeIfAbsent("C", k -> 3);  // Compute if absent
map.merge("A", 10, Integer::sum);  // Merge values
```

**Why not Collections.synchronizedMap()?**
- synchronizedMap locks entire map
- ConcurrentHashMap uses segment locking (better performance)

### CopyOnWriteArrayList

```java
// ✅ Thread-safe for iteration
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

list.add("A");
list.add("B");

// Safe iteration (iterates over snapshot)
for (String s : list) {
    list.add("C");  // No ConcurrentModificationException!
}
```

**Best for**: Read-heavy, few writes (write creates new copy)

### BlockingQueue

```java
// Producer-Consumer pattern
BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);

// Producer
queue.put("item");     // Blocks if full

// Consumer  
String item = queue.take();  // Blocks if empty
```

## Concurrent Collection Summary

| Regular | Concurrent Alternative |
|---------|----------------------|
| HashMap | ConcurrentHashMap |
| ArrayList | CopyOnWriteArrayList |
| HashSet | CopyOnWriteArraySet |
| TreeMap | ConcurrentSkipListMap |
| LinkedList (Queue) | ConcurrentLinkedQueue |
| PriorityQueue | PriorityBlockingQueue |

---

# 15. Semaphore, CountDownLatch, CyclicBarrier

## 15.1 Semaphore

### Kid-Friendly Explanation 🧒

**Parking Lot with 3 Spaces:**
- 3 permits available
- Car enters → permit taken (2 left)
- Car leaves → permit released (3 again)
- 4th car must WAIT for a space

### Code Example

```java
import java.util.concurrent.Semaphore;

Semaphore parking = new Semaphore(3);  // 3 permits

void parkCar() throws InterruptedException {
    parking.acquire();  // Get permit (blocks if none available)
    try {
        System.out.println("Car parked");
        Thread.sleep(2000);  // Stay parked
    } finally {
        parking.release();  // Release permit
        System.out.println("Car left");
    }
}
```

---

## 15.2 CountDownLatch

### Kid-Friendly Explanation 🧒

**Race Start:**
- Count: 3
- "On your marks!" → count = 2
- "Get set!" → count = 1
- "GO!" → count = 0 → Everyone starts running!

One-time use: Once count reaches 0, can't reset.

### Code Example

```java
import java.util.concurrent.CountDownLatch;

CountDownLatch latch = new CountDownLatch(3);

// Workers
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        System.out.println("Worker done");
        latch.countDown();  // Decrease count
    }).start();
}

// Main thread waits
latch.await();  // Blocks until count = 0
System.out.println("All workers done!");
```

---

## 15.3 CyclicBarrier

### Kid-Friendly Explanation 🧒

**Friends Meeting Point:**
- 3 friends agree to meet at mall
- Each arrives at different times
- ALL must arrive before anyone enters
- Can repeat for next activity!

Unlike CountDownLatch: CyclicBarrier can be REUSED!

### Code Example

```java
import java.util.concurrent.CyclicBarrier;

CyclicBarrier barrier = new CyclicBarrier(3, () -> {
    System.out.println("All threads arrived! Let's go!");
});

for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        System.out.println(Thread.currentThread().getName() + " arrived");
        barrier.await();  // Wait for others
        System.out.println(Thread.currentThread().getName() + " proceeding");
    }).start();
}
```

## Comparison

| Feature | Semaphore | CountDownLatch | CyclicBarrier |
|---------|-----------|----------------|---------------|
| Purpose | Limit concurrent access | Wait for N events | Wait for N threads |
| Reusable | Yes | No | Yes |
| Who decrements | Any thread | Any thread | Same threads that wait |
| Typical use | Connection pool | Wait for services to start | Parallel computation phases |

---

# 16. Virtual Threads (Java 21)

## Kid-Friendly Explanation 🧒

**Traditional Threads = Cars 🚗**
- Heavy, expensive to create
- Limited parking spots (OS thread limit)
- Can have ~thousands

**Virtual Threads = Bicycles 🚲**
- Lightweight, cheap to create
- Unlimited parking
- Can have ~MILLIONS!

## Why Virtual Threads?

| Platform Thread | Virtual Thread |
|-----------------|----------------|
| Managed by OS | Managed by JVM |
| ~1MB memory each | ~few KB each |
| ~thousands possible | ~millions possible |
| Blocking = waste | Blocking = efficient |

## Creating Virtual Threads

### Method 1: Thread.startVirtualThread()

```java
Thread vThread = Thread.startVirtualThread(() -> {
    System.out.println("Hello from virtual thread!");
});
vThread.join();
```

### Method 2: Thread.ofVirtual()

```java
Thread vThread = Thread.ofVirtual()
    .name("my-virtual-thread")
    .start(() -> {
        System.out.println("Virtual thread running");
    });
```

### Method 3: ExecutorService (Recommended!)

```java
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
    // Submit 100,000 tasks - no problem!
    for (int i = 0; i < 100_000; i++) {
        executor.submit(() -> {
            Thread.sleep(1000);  // Blocking is OK!
            return "Done";
        });
    }
}  // Auto-shutdown
```

## When to Use Virtual Threads?

### ✅ GOOD for (I/O-bound):
- HTTP requests
- Database calls
- File operations
- Network operations
- Any task that WAITS a lot

### ❌ NOT GOOD for (CPU-bound):
- Heavy calculations
- Data processing
- Image processing
- Use platform threads for these

## Example: 100,000 HTTP Calls

```java
// Platform threads: Would crash or be very slow!
// Virtual threads: Easy!

try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<String>> futures = new ArrayList<>();
    
    for (int i = 0; i < 100_000; i++) {
        futures.add(executor.submit(() -> {
            // Make HTTP call (blocks, but it's OK!)
            return httpClient.send(request, BodyHandlers.ofString()).body();
        }));
    }
    
    // Collect results
    for (Future<String> f : futures) {
        String response = f.get();
    }
}
```

## Checking Thread Type

```java
Thread t = Thread.currentThread();
System.out.println("Is virtual: " + t.isVirtual());
```

## Virtual Thread Gotchas

### 1. Don't Pool Virtual Threads!
```java
// ❌ WRONG - Don't pool virtual threads
ExecutorService pool = Executors.newFixedThreadPool(100);  // Platform threads

// ✅ RIGHT - One virtual thread per task
ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
```

### 2. Avoid synchronized for Long Operations
```java
// ❌ synchronized pins virtual thread to platform thread
synchronized (lock) {
    longBlockingOperation();  // Bad!
}

// ✅ Use ReentrantLock instead
lock.lock();
try {
    longBlockingOperation();  // OK!
} finally {
    lock.unlock();
}
```

---

# 17. Interview Questions

## Basic Questions

**Q1: What is the difference between Process and Thread?**
> Process: Independent program with own memory space.
> Thread: Lightweight sub-unit of process, shares memory with other threads.

**Q2: start() vs run()?**
> `start()`: Creates new thread, then calls run()
> `run()`: Just a method call in current thread (no new thread!)

**Q3: How to create a thread?**
> 1. Extend Thread class
> 2. Implement Runnable (preferred)
> 3. Implement Callable (returns value)
> 4. Use ExecutorService

**Q4: Why is Runnable preferred over Thread?**
> - Java supports single inheritance (can extend other class)
> - Separation of task from thread management
> - Works with ExecutorService

## synchronized Questions

**Q5: What is synchronized?**
> Keyword that ensures only one thread executes a block/method at a time. Provides mutual exclusion and visibility.

**Q6: What does synchronized lock on?**
> - Instance method: `this` object
> - Static method: `Class` object
> - Block: Specified object

**Q7: Can synchronized method and non-synchronized method run simultaneously?**
> Yes! Only synchronized methods/blocks on SAME lock compete.

## volatile Questions

**Q8: What is volatile?**
> Keyword that ensures variable changes are visible to all threads (reads/writes go to main memory, not CPU cache).

**Q9: volatile vs synchronized?**
> volatile: Only visibility, no atomicity, no blocking
> synchronized: Visibility + atomicity + blocking

**Q10: Is volatile int count; count++; thread-safe?**
> NO! count++ is 3 operations (read, add, write). Use AtomicInteger instead.

## Lock Questions

**Q11: ReentrantLock vs synchronized?**
> ReentrantLock offers: tryLock(), timeout, fairness, lockInterruptibly()
> synchronized: Simpler syntax, auto-release

**Q12: What is ReadWriteLock?**
> Lock that allows multiple readers OR one writer. Better performance for read-heavy workloads.

**Q13: What is deadlock? How to prevent?**
> Deadlock: Two threads waiting for each other's locks forever.
> Prevention: Lock ordering, tryLock with timeout, avoid nested locks.

## Thread Pool Questions

**Q14: Why use thread pools?**
> - Reuse threads (avoid creation overhead)
> - Control max threads
> - Task queuing

**Q15: Types of thread pools?**
> - FixedThreadPool: Fixed number of threads
> - CachedThreadPool: Creates as needed, reuses
> - SingleThreadExecutor: One thread
> - ScheduledThreadPool: For scheduled tasks

## Advanced Questions

**Q16: What is CompletableFuture?**
> Non-blocking Future with chaining, composition, and callbacks. Enables async programming without blocking.

**Q17: What are Atomic variables?**
> Thread-safe variables using CAS (Compare-And-Set) operations. Lock-free, better performance than synchronized.

**Q18: What are Virtual Threads?**
> Lightweight threads managed by JVM (not OS). Can create millions. Perfect for I/O-bound tasks. Available in Java 21.

**Q19: wait() vs sleep()?**
> wait(): Releases lock, must be in synchronized, wakes by notify
> sleep(): Keeps lock, anywhere, wakes after time

**Q20: What is ThreadLocal?**
> Variable that each thread has its own copy of. No sharing between threads.

```java
ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);
threadLocal.set(42);      // Set for current thread
threadLocal.get();        // Get for current thread
threadLocal.remove();     // Clean up!
```

---

# 18. Unmodifiable Collections in Banking Application

## Kid-Friendly Explanation 🧒

### The Museum Exhibit Example 🏛️

**Imagine a Museum with Precious Diamonds:**
- Diamonds are displayed in a **glass case**
- Visitors can **LOOK** at diamonds (read) ✅
- Visitors **CANNOT TOUCH or TAKE** diamonds (modify) ❌
- Only the museum curator can change the exhibit

**Unmodifiable Collection** = Data behind glass - everyone can see it, but nobody can change it!

### The Bank Statement Example 🏦

**Your Bank Statement:**
- You can **VIEW** all your past transactions
- You **CANNOT DELETE** or **CHANGE** old transactions
- The bank already confirmed these transactions
- History is **READ-ONLY**!

---

## Why Unmodifiable Collections?

### Problem Without Protection

```java
// ❌ DANGEROUS: Account can be modified from outside!
public class BankAccount {
    private List<Transaction> transactions = new ArrayList<>();
    
    public List<Transaction> getTransactions() {
        return transactions;  // Returns the actual list!
    }
}

// Hacker code
BankAccount account = new BankAccount();
List<Transaction> txns = account.getTransactions();
txns.clear();  // 😱 Deleted all transactions!
txns.add(new Transaction("Fake deposit", 1000000));  // 😱 Added fake money!
```

### Solution: Unmodifiable Collections

```java
// ✅ SAFE: Returns read-only view
public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
}

// Hacker tries...
List<Transaction> txns = account.getTransactions();
txns.clear();  // 💥 UnsupportedOperationException!
txns.add(...); // 💥 UnsupportedOperationException!
```

---

## Three Ways to Create Unmodifiable Collections

### 1. Collections.unmodifiableXXX() (Classic Way)

```java
List<String> original = new ArrayList<>();
original.add("Item1");
original.add("Item2");

// Wrap with unmodifiable view
List<String> unmodifiable = Collections.unmodifiableList(original);

unmodifiable.add("Item3");  // 💥 UnsupportedOperationException

// ⚠️ WARNING: Original can still be modified!
original.add("Item3");  // Works! And unmodifiable sees it!
```

| Method | Collection Type |
|--------|-----------------|
| `Collections.unmodifiableList()` | List |
| `Collections.unmodifiableSet()` | Set |
| `Collections.unmodifiableMap()` | Map |
| `Collections.unmodifiableSortedSet()` | SortedSet |
| `Collections.unmodifiableSortedMap()` | SortedMap |

### 2. List.of(), Set.of(), Map.of() (Java 9+) ⭐ Recommended

```java
// Truly immutable - no original reference to worry about
List<String> immutableList = List.of("A", "B", "C");
Set<Integer> immutableSet = Set.of(1, 2, 3);
Map<String, Integer> immutableMap = Map.of(
    "Alice", 100,
    "Bob", 200
);

immutableList.add("D");  // 💥 UnsupportedOperationException
immutableSet.add(4);     // 💥 UnsupportedOperationException
immutableMap.put("Charlie", 300);  // 💥 UnsupportedOperationException
```

### 3. List.copyOf(), Set.copyOf(), Map.copyOf() (Java 10+)

```java
List<String> original = new ArrayList<>();
original.add("A");
original.add("B");

// Creates immutable COPY
List<String> copied = List.copyOf(original);

original.add("C");          // Original modified
System.out.println(copied); // ["A", "B"] - Copy NOT affected!
```

---

## 🏦 Complete Banking Application Example

### Transaction Class

```java
import java.time.LocalDateTime;
import java.math.BigDecimal;

public final class Transaction {
    private final String id;
    private final String type;  // "DEPOSIT", "WITHDRAWAL", "TRANSFER"
    private final BigDecimal amount;
    private final LocalDateTime timestamp;
    private final String description;

    public Transaction(String id, String type, BigDecimal amount, String description) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    // Only getters - no setters! (Immutable)
    public String getId() { return id; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%s - %s", 
            timestamp.toLocalDate(), type, amount, description);
    }
}
```

### BankAccount Class

```java
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BankAccount {
    private final String accountNumber;
    private final String accountHolder;
    private BigDecimal balance;
    private final List<Transaction> transactions;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public BankAccount(String accountNumber, String accountHolder) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = BigDecimal.ZERO;
        this.transactions = new ArrayList<>();
    }
    
    // ✅ Returns UNMODIFIABLE transaction history
    public List<Transaction> getTransactionHistory() {
        lock.readLock().lock();
        try {
            // Return unmodifiable COPY for safety
            return List.copyOf(transactions);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // ✅ Returns UNMODIFIABLE view of last N transactions
    public List<Transaction> getRecentTransactions(int count) {
        lock.readLock().lock();
        try {
            int start = Math.max(0, transactions.size() - count);
            List<Transaction> recent = transactions.subList(start, transactions.size());
            return Collections.unmodifiableList(new ArrayList<>(recent));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Deposit money
    public Transaction deposit(BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        lock.writeLock().lock();
        try {
            balance = balance.add(amount);
            Transaction txn = new Transaction(
                generateTxnId(), "DEPOSIT", amount, description
            );
            transactions.add(txn);
            return txn;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // Withdraw money
    public Transaction withdraw(BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        lock.writeLock().lock();
        try {
            if (balance.compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient funds");
            }
            balance = balance.subtract(amount);
            Transaction txn = new Transaction(
                generateTxnId(), "WITHDRAWAL", amount, description
            );
            transactions.add(txn);
            return txn;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public BigDecimal getBalance() {
        lock.readLock().lock();
        try {
            return balance;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    private String generateTxnId() {
        return "TXN-" + System.currentTimeMillis();
    }
}
```

### Bank Class with Unmodifiable Account List

```java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
    private final String bankName;
    private final Map<String, BankAccount> accounts = new ConcurrentHashMap<>();
    
    // Unmodifiable list of supported currencies
    private static final List<String> SUPPORTED_CURRENCIES = 
        List.of("USD", "EUR", "GBP", "JPY", "INR");
    
    // Unmodifiable set of account types
    private static final Set<String> ACCOUNT_TYPES = 
        Set.of("SAVINGS", "CHECKING", "BUSINESS");
    
    // Unmodifiable map of minimum balance requirements
    private static final Map<String, Integer> MIN_BALANCE = Map.of(
        "SAVINGS", 100,
        "CHECKING", 0,
        "BUSINESS", 1000
    );
    
    public Bank(String bankName) {
        this.bankName = bankName;
    }
    
    // ✅ Returns UNMODIFIABLE list of currencies
    public List<String> getSupportedCurrencies() {
        return SUPPORTED_CURRENCIES;  // Already immutable!
    }
    
    // ✅ Returns UNMODIFIABLE set of account types
    public Set<String> getAccountTypes() {
        return ACCOUNT_TYPES;  // Already immutable!
    }
    
    // ✅ Returns UNMODIFIABLE map of min balances
    public Map<String, Integer> getMinBalanceRequirements() {
        return MIN_BALANCE;  // Already immutable!
    }
    
    // ✅ Returns UNMODIFIABLE view of all accounts
    public Collection<BankAccount> getAllAccounts() {
        return Collections.unmodifiableCollection(accounts.values());
    }
    
    // ✅ Returns UNMODIFIABLE set of account numbers
    public Set<String> getAccountNumbers() {
        return Collections.unmodifiableSet(accounts.keySet());
    }
    
    public BankAccount createAccount(String accountNumber, String holderName) {
        BankAccount account = new BankAccount(accountNumber, holderName);
        accounts.put(accountNumber, account);
        return account;
    }
    
    public Optional<BankAccount> findAccount(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }
}
```

### Testing the Banking Application

```java
import java.math.BigDecimal;
import java.util.List;

public class BankingDemo {
    public static void main(String[] args) {
        // Create bank
        Bank bank = new Bank("Safe Bank");
        
        // Create account
        BankAccount account = bank.createAccount("ACC-001", "Alice");
        
        // Perform transactions
        account.deposit(new BigDecimal("1000.00"), "Initial deposit");
        account.deposit(new BigDecimal("500.50"), "Salary");
        account.withdraw(new BigDecimal("200.00"), "ATM withdrawal");
        account.deposit(new BigDecimal("75.25"), "Refund");
        
        System.out.println("Balance: $" + account.getBalance());
        
        // Get transaction history - UNMODIFIABLE!
        List<Transaction> history = account.getTransactionHistory();
        System.out.println("\n📜 Transaction History:");
        history.forEach(System.out::println);
        
        // Try to modify - WILL FAIL!
        try {
            history.clear();  // 💥
        } catch (UnsupportedOperationException e) {
            System.out.println("\n❌ Cannot clear transaction history!");
        }
        
        try {
            history.add(new Transaction("FAKE", "DEPOSIT", 
                new BigDecimal("999999"), "Fake deposit"));  // 💥
        } catch (UnsupportedOperationException e) {
            System.out.println("❌ Cannot add fake transactions!");
        }
        
        // Bank's unmodifiable data
        System.out.println("\n🏦 Supported Currencies: " + bank.getSupportedCurrencies());
        
        try {
            bank.getSupportedCurrencies().add("BITCOIN");  // 💥
        } catch (UnsupportedOperationException e) {
            System.out.println("❌ Cannot modify supported currencies!");
        }
        
        System.out.println("\n✅ All data is safe and protected!");
    }
}
```

### Expected Output

```
Balance: $1375.75

📜 Transaction History:
[2024-01-15] DEPOSIT: $1000.00 - Initial deposit
[2024-01-15] DEPOSIT: $500.50 - Salary
[2024-01-15] WITHDRAWAL: $200.00 - ATM withdrawal
[2024-01-15] DEPOSIT: $75.25 - Refund

❌ Cannot clear transaction history!
❌ Cannot add fake transactions!

🏦 Supported Currencies: [USD, EUR, GBP, JPY, INR]
❌ Cannot modify supported currencies!

✅ All data is safe and protected!
```

---

## Unmodifiable vs Immutable - What's the Difference?

### Kid-Friendly Explanation 🧒

**Unmodifiable = Locked Door 🚪**
- YOU can't open the door
- But someone with a key CAN
- The original list owner can still modify

**Immutable = Solid Wall 🧱**
- NOBODY can get through
- No key exists
- Truly unchangeable

### Code Demonstration

```java
// UNMODIFIABLE (View only - original can still change)
List<String> original = new ArrayList<>();
original.add("A");
List<String> unmodifiable = Collections.unmodifiableList(original);

original.add("B");  // Works!
System.out.println(unmodifiable);  // [A, B] - It changed!

// IMMUTABLE (True copy - completely independent)
List<String> original2 = new ArrayList<>();
original2.add("X");
List<String> immutable = List.copyOf(original2);

original2.add("Y");  // Works on original
System.out.println(immutable);  // [X] - NOT affected!
```

---

## Thread Safety with Unmodifiable Collections

### Why Unmodifiable + Multithreading Work Well Together

```java
public class ThreadSafeBankReport {
    private final List<Transaction> transactions;
    
    public ThreadSafeBankReport(List<Transaction> transactions) {
        // Create immutable copy at construction time
        this.transactions = List.copyOf(transactions);
    }
    
    // ✅ Multiple threads can safely read
    // No synchronization needed!
    public List<Transaction> getTransactions() {
        return transactions;  // Already immutable, safe to return
    }
    
    public BigDecimal calculateTotal() {
        // Multiple threads can call this safely
        return transactions.stream()
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

### Multi-threaded Example

```java
import java.util.concurrent.*;

public class MultiThreadedBankingDemo {
    public static void main(String[] args) throws Exception {
        Bank bank = new Bank("Multi-Thread Bank");
        BankAccount account = bank.createAccount("ACC-001", "Bob");
        
        // Deposit some money first
        for (int i = 0; i < 100; i++) {
            account.deposit(new BigDecimal("10"), "Deposit " + i);
        }
        
        // Multiple threads reading transaction history simultaneously
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                // ✅ SAFE: Each thread gets unmodifiable copy
                List<Transaction> history = account.getTransactionHistory();
                
                System.out.println(Thread.currentThread().getName() + 
                    " sees " + history.size() + " transactions");
                
                // ✅ CANNOT modify - throws exception
                // history.clear();  // Would throw UnsupportedOperationException
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
```

---

## Best Practices Summary

### ✅ DO's

| Practice | Example |
|----------|---------|
| Return immutable copies | `List.copyOf(internalList)` |
| Use `List.of()` for constants | `List.of("A", "B", "C")` |
| Make fields final | `private final List<T> items` |
| Create defensive copies | `this.items = List.copyOf(inputList)` |

### ❌ DON'Ts

| Anti-Pattern | Problem |
|--------------|---------|
| Return internal list directly | Caller can modify your data |
| Wrap without copying | Original can still be modified |
| Forget null checks | `List.of()` doesn't allow nulls |
| Use for mutable elements | Object inside can still change |

### Deep vs Shallow Immutability ⚠️

```java
// ⚠️ SHALLOW: List is immutable, but Transaction objects might not be
List<Transaction> transactions = List.of(txn1, txn2, txn3);

// If Transaction has setters, this could still work:
// transactions.get(0).setAmount(999999);  // 😱

// ✅ SOLUTION: Make Transaction immutable too (use final fields, no setters)
```

---

## Interview Questions

**Q1: What is the difference between unmodifiable and immutable collections?**
> Unmodifiable: A view that doesn't allow modification through that reference, but the original collection can still be modified.
> Immutable: Cannot be modified at all, by anyone.

**Q2: Why use unmodifiable collections in banking applications?**
> - Prevent unauthorized modification of transaction history
> - Thread safety (no synchronization needed for reads)
> - Audit trail integrity
> - Defense against programming errors

**Q3: How to create a truly immutable collection?**
> Use `List.of()`, `Set.of()`, `Map.of()` (Java 9+) or `List.copyOf()`, `Set.copyOf()`, `Map.copyOf()` (Java 10+)

**Q4: What exception is thrown when modifying an unmodifiable collection?**
> `UnsupportedOperationException`

**Q5: Can unmodifiable collections contain null?**
> - `Collections.unmodifiableList()`: Yes, if original has null
> - `List.of()`, `List.copyOf()`: No, throws `NullPointerException`

---

# Quick Reference Card

```
┌─────────────────────────────────────────────────────────────┐
│                 MULTITHREADING CHEAT SHEET                  │
├─────────────────────────────────────────────────────────────┤
│ CREATE THREAD                                               │
│   Thread t = new Thread(() -> {...});                       │
│   t.start();                                                │
├─────────────────────────────────────────────────────────────┤
│ SYNCHRONIZED                                                │
│   synchronized void method() {...}                          │
│   synchronized (lock) {...}                                 │
├─────────────────────────────────────────────────────────────┤
│ REENTRANT LOCK                                              │
│   lock.lock(); try {...} finally { lock.unlock(); }         │
├─────────────────────────────────────────────────────────────┤
│ EXECUTOR SERVICE                                            │
│   ExecutorService exec = Executors.newFixedThreadPool(5);   │
│   exec.submit(() -> {...});                                 │
│   exec.shutdown();                                          │
├─────────────────────────────────────────────────────────────┤
│ VIRTUAL THREADS (Java 21)                                   │
│   Executors.newVirtualThreadPerTaskExecutor();              │
│   Thread.startVirtualThread(() -> {...});                   │
├─────────────────────────────────────────────────────────────┤
│ CONCURRENT COLLECTIONS                                      │
│   ConcurrentHashMap, CopyOnWriteArrayList                   │
│   BlockingQueue, ConcurrentLinkedQueue                      │
├─────────────────────────────────────────────────────────────┤
│ ATOMIC VARIABLES                                            │
│   AtomicInteger count = new AtomicInteger(0);               │
│   count.incrementAndGet();                                  │
└─────────────────────────────────────────────────────────────┘
```

---

# 19. Static Synchronized Methods

## Kid-Friendly Explanation 🧒

### Instance Synchronized = Bathroom in Each Apartment 🏠

Imagine an **apartment building** with 3 apartments:
- Each apartment has its **OWN bathroom**
- Person in Apartment A can use bathroom A
- Person in Apartment B can use bathroom B **at the same time**
- They don't block each other!

```
Apartment A 🏠 → Bathroom A 🚽 → Person A uses it
Apartment B 🏠 → Bathroom B 🚽 → Person B uses it (same time!)
Apartment C 🏠 → Bathroom C 🚽 → Person C uses it (same time!)
```

### Static Synchronized = ONE Bathroom for Entire Building 🏢

Now imagine there's only **ONE bathroom** for the whole building:
- ALL residents must wait for the **same bathroom**
- If Person A is using it, Person B and C must WAIT
- Doesn't matter which apartment you're from!

```
Building 🏢 → ONE Bathroom 🚽 → EVERYONE waits in line!
```

---

## What Does Static Synchronized Lock On?

### Instance Method (synchronized)

```java
public synchronized void instanceMethod() {
    // Locks on: this object (current instance)
}
```

**Lock = `this` (the specific object)**

### Static Method (static synchronized)

```java
public static synchronized void staticMethod() {
    // Locks on: Class object (BankAccount.class)
}
```

**Lock = `ClassName.class` (one lock for ALL instances)**

---

## Visual Difference

```
┌─────────────────────────────────────────────────────────────┐
│           INSTANCE SYNCHRONIZED (Multiple Locks)            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   Object 1          Object 2          Object 3              │
│   ┌─────┐           ┌─────┐           ┌─────┐              │
│   │Lock1│           │Lock2│           │Lock3│              │
│   └─────┘           └─────┘           └─────┘              │
│      ↓                 ↓                 ↓                  │
│   Thread A          Thread B          Thread C              │
│   (can run)         (can run)         (can run)             │
│                                                             │
│   ✅ All can execute simultaneously on different objects    │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│           STATIC SYNCHRONIZED (Single Lock)                 │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│                    ┌───────────────┐                        │
│                    │ Class.class   │                        │
│                    │ (ONE Lock)    │                        │
│                    └───────────────┘                        │
│                          ↓                                  │
│   Thread A ────────→ RUNNING                                │
│   Thread B ────────→ WAITING ⏳                             │
│   Thread C ────────→ WAITING ⏳                             │
│                                                             │
│   ❌ Only ONE thread can execute at a time (any object)     │
└─────────────────────────────────────────────────────────────┘
```

---

## Code Example - Banking Application

### The Problem: Global Transaction Counter

```java
public class BankTransaction {
    // 🌍 GLOBAL counter shared across ALL transactions
    private static int globalTransactionCount = 0;
    
    // ❌ WRONG: Instance synchronized - each object has its own lock
    public synchronized void incrementWrong() {
        globalTransactionCount++;  // RACE CONDITION! Static variable!
    }
    
    // ✅ CORRECT: Static synchronized - one lock for the class
    public static synchronized void incrementCorrect() {
        globalTransactionCount++;  // SAFE! Class-level lock!
    }
}
```

### Complete Example: Bank with Global Statistics

```java
public class BankGlobalStats {
    
    // Static (shared) data
    private static int totalAccountsCreated = 0;
    private static int totalTransactionsProcessed = 0;
    private static double totalMoneyMoved = 0.0;
    
    // Instance data
    private String accountNumber;
    private double balance;
    
    public BankGlobalStats(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
        incrementAccountCount();  // Track globally
    }
    
    // ✅ STATIC SYNCHRONIZED - protects static variable
    public static synchronized void incrementAccountCount() {
        totalAccountsCreated++;
        System.out.println("Total accounts: " + totalAccountsCreated);
    }
    
    // ✅ STATIC SYNCHRONIZED - protects multiple static variables  
    public static synchronized void recordTransaction(double amount) {
        totalTransactionsProcessed++;
        totalMoneyMoved += amount;
    }
    
    // ✅ STATIC SYNCHRONIZED - reading static variables safely
    public static synchronized String getGlobalStats() {
        return String.format(
            "Accounts: %d, Transactions: %d, Money Moved: $%.2f",
            totalAccountsCreated,
            totalTransactionsProcessed,
            totalMoneyMoved
        );
    }
    
    // ✅ INSTANCE SYNCHRONIZED - protects instance variable
    public synchronized void deposit(double amount) {
        balance += amount;
        recordTransaction(amount);  // Also update global stats
    }
    
    // ✅ INSTANCE SYNCHRONIZED - protects instance variable
    public synchronized void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            recordTransaction(amount);
        }
    }
    
    public synchronized double getBalance() {
        return balance;
    }
}
```

### Testing with Multiple Threads

```java
public class StaticSyncDemo {
    public static void main(String[] args) throws InterruptedException {
        
        // Create threads that create accounts simultaneously
        Thread[] threads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            final int id = i;
            threads[i] = new Thread(() -> {
                // Each thread creates a new account
                BankGlobalStats account = new BankGlobalStats("ACC-" + id);
                
                // Each thread deposits money
                account.deposit(100.0);
                account.deposit(50.0);
                account.withdraw(30.0);
            });
        }
        
        // Start all threads
        for (Thread t : threads) {
            t.start();
        }
        
        // Wait for all to complete
        for (Thread t : threads) {
            t.join();
        }
        
        // Print global statistics
        System.out.println("\n📊 " + BankGlobalStats.getGlobalStats());
    }
}
```

### Expected Output (Thread-Safe!)

```
Total accounts: 1
Total accounts: 2
Total accounts: 3
...
Total accounts: 10

📊 Accounts: 10, Transactions: 30, Money Moved: $1800.00
```

---

## When to Use Which?

| Situation | Use |
|-----------|-----|
| Protecting **instance** variables | `synchronized` (instance method) |
| Protecting **static** variables | `static synchronized` |
| Counter per object | Instance synchronized |
| Counter for entire application | Static synchronized |
| Bank account balance | Instance synchronized |
| Total bank transactions | Static synchronized |

---

## ⚠️ Common Mistake: Using Instance Lock for Static Data

```java
public class WrongExample {
    private static int sharedCounter = 0;  // STATIC data
    
    // ❌ WRONG! Each object has different 'this' lock
    public synchronized void increment() {
        sharedCounter++;  // NOT PROTECTED for static variable!
    }
}

// Problem demonstration:
WrongExample obj1 = new WrongExample();
WrongExample obj2 = new WrongExample();

// Thread 1 calls obj1.increment() → locks on obj1
// Thread 2 calls obj2.increment() → locks on obj2
// BOTH run at same time! Race condition on sharedCounter!
```

### Correct Version

```java
public class CorrectExample {
    private static int sharedCounter = 0;
    
    // ✅ CORRECT! All objects share the Class lock
    public static synchronized void increment() {
        sharedCounter++;
    }
    
    // OR use explicit class lock in instance method:
    public void incrementAlternative() {
        synchronized (CorrectExample.class) {
            sharedCounter++;
        }
    }
}
```

---

## Static Synchronized with Explicit Lock

```java
public class BankService {
    private static final Object CLASS_LOCK = new Object();
    private static int globalCounter = 0;
    
    // Option 1: Static synchronized method
    public static synchronized void method1() {
        globalCounter++;
    }
    
    // Option 2: Synchronized block on Class object
    public static void method2() {
        synchronized (BankService.class) {
            globalCounter++;
        }
    }
    
    // Option 3: Synchronized block on dedicated lock object
    public static void method3() {
        synchronized (CLASS_LOCK) {
            globalCounter++;
        }
    }
}
```

| Option | Lock Object | Notes |
|--------|-------------|-------|
| Option 1 | `BankService.class` | Implicit, simple |
| Option 2 | `BankService.class` | Explicit, same as Option 1 |
| Option 3 | `CLASS_LOCK` | More control, can have multiple locks |

---

## Can Static and Instance Synchronized Run Together?

**YES!** They use **DIFFERENT locks**.

```java
public class MixedExample {
    private static int staticData = 0;
    private int instanceData = 0;
    
    // Lock: MixedExample.class
    public static synchronized void staticMethod() {
        staticData++;
    }
    
    // Lock: this
    public synchronized void instanceMethod() {
        instanceData++;
    }
}
```

```
Thread A calls staticMethod()  → acquires Class lock
Thread B calls instanceMethod() → acquires object lock

✅ Both can run at the SAME TIME! (Different locks)
```

### But Be Careful!

```java
// ❌ If instance method accesses static data, you need class lock!
public synchronized void badMethod() {
    staticData++;  // NOT SAFE! Has instance lock, not class lock
}

// ✅ Fixed version
public void goodMethod() {
    synchronized (MixedExample.class) {
        staticData++;
    }
}
```

---

## Summary Table

| Aspect | Instance Synchronized | Static Synchronized |
|--------|----------------------|---------------------|
| Lock on | `this` (object) | `ClassName.class` |
| Protects | Instance variables | Static variables |
| Multiple objects | Each has own lock | All share ONE lock |
| Blocking | Only same object | ALL objects blocked |
| Keyword | `synchronized` | `static synchronized` |
| Equivalent block | `synchronized(this)` | `synchronized(Class.class)` |

---

## Interview Question

**Q: What's the difference between synchronized and static synchronized?**

> - `synchronized` method locks on `this` (current object instance). Multiple threads calling synchronized methods on **different objects** can run simultaneously.
>
> - `static synchronized` method locks on the `Class` object. ALL threads calling static synchronized methods are blocked, regardless of which object they use.
>
> Use instance synchronized for instance data, static synchronized for static (class-level) data.

---

# 20. ForkJoinPool

## Kid-Friendly Explanation 🧒

**The Big Puzzle Project 🧩**

Imagine you have a **GIANT** 10,000 piece puzzle to solve.

- **Single Thread:** One person trying to do it alone. (Takes forever!) 😴
- **ThreadPool:** You call 4 friends, and give each 2,500 pieces. But what if one friend finishes fast and the others are slow? The fast friend sits idle. 🤷
- **ForkJoinPool:** You call 4 friends. If one finishes their pile, they **STEAL** work from the others who are still busy! Everyone keeps working until the WHOLE job is done. 🤝⚡

## Key Concept: Divide and Conquer

1.  **Split** big task into smaller tasks (**Fork**)
2.  **Do** the small tasks
3.  **Combine** results (**Join**)

This is perfect for recursive problems!

## The Magic: Work Stealing 🕵️

- Threads don't just sit idle.
- If Thread A finishes its queue, it looks at Thread B's queue.
- It steals tasks from the **end** (deque) of Thread B's queue to help out.
- **Result:** Maximum CPU utilization!

## Code Example

```java
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

// Simple task: Sum array of numbers
class SumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000; // Split if more than 1000 items
    private long[] array;
    private int start, end;

    public SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        // If task is small enough, just do it directly
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            // Task is too big! Split it!
            int mid = start + (end - start) / 2;
            
            SumTask leftTask = new SumTask(array, start, mid);
            SumTask rightTask = new SumTask(array, mid, end);
            
            // Fork: Run subtasks asynchronously
            leftTask.fork();                        // Push to queue
            long rightResult = rightTask.compute(); // Do right half immediately
            long leftResult = leftTask.join();      // Wait for left (help others while waiting!)
            
            return leftResult + rightResult;
        }
    }
}

public class ForkJoinDemo {
    public static void main(String[] args) {
        // commonPool uses threads = number of CPU cores - 1
        ForkJoinPool pool = ForkJoinPool.commonPool(); 
        
        long[] array = new long[10_000];
        for(int i=0; i<array.length; i++) array[i] = 1;
        
        SumTask mainTask = new SumTask(array, 0, array.length);
        long result = pool.invoke(mainTask);
        
        System.out.println("Result: " + result);
    }
}
```

## Difference from ExecutorService

| Feature | ExecutorService | ForkJoinPool |
|---------|-----------------|--------------|
| **Task Relationship** | Independent tasks | Recursive sub-tasks |
| **Worker Threads** | One queue per pool (usually) | One queue per Thread (Deque) |
| **Idle Threads** | Wait for work | **Steal** work from busy threads |
| **Best For** | Web server requests | Heavy computation (Matrix math, Sorting) |
| **Used By** | Manually created | `Stream.parallel()`, `CompletableFuture` |























