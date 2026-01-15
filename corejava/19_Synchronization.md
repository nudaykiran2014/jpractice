# Java Synchronization - Complete Guide

## Table of Contents
1. [Why Synchronization?](#1-why-synchronization)
2. [synchronized Keyword](#2-synchronized-keyword)
3. [Synchronized Methods](#3-synchronized-methods)
4. [Synchronized Blocks](#4-synchronized-blocks)
5. [Static Synchronization](#5-static-synchronization)
6. [Intrinsic Locks & Monitors](#6-intrinsic-locks--monitors)
7. [wait(), notify(), notifyAll()](#7-wait-notify-notifyall)
8. [volatile Keyword](#8-volatile-keyword)
9. [Memory Visibility](#9-memory-visibility)
10. [Happens-Before Relationship](#10-happens-before-relationship)
11. [Common Patterns](#11-common-patterns)
12. [Interview Questions](#12-interview-questions)

---

# 1. Why Synchronization?

## 🧒 Kid-Friendly Explanation

**Problem:** Two kids trying to write on the same paper at the same time!

```
Without Sync:                 With Sync:
Kid 1: A--                   Kid 1: ABC (waits)
Kid 2: --B--                 Kid 2: DEF (after Kid 1)
Result: AB? (mess!)          Result: ABCDEF (clean!)
```

## Race Condition Example

```java
class Counter {
    private int count = 0;
    
    public void increment() {
        count++;  // NOT ATOMIC!
    }
    // count++ is actually:
    // 1. Read count
    // 2. Add 1
    // 3. Write back
    // Another thread can interfere between steps!
}

// Without synchronization
Counter counter = new Counter();
Thread t1 = new Thread(() -> {
    for (int i = 0; i < 10000; i++) counter.increment();
});
Thread t2 = new Thread(() -> {
    for (int i = 0; i < 10000; i++) counter.increment();
});
t1.start(); t2.start();
t1.join(); t2.join();
System.out.println(counter.count);  // Less than 20000! Race condition!
```

---

# 2. synchronized Keyword

## What It Does

1. **Mutual Exclusion**: Only one thread can execute at a time
2. **Memory Visibility**: Changes are visible to other threads
3. **Reordering Prevention**: Instructions not reordered across sync

## How It Works

```
┌─────────────────────────────────────────┐
│              MONITOR (Lock)              │
│                                          │
│   Thread 1 ──▶ ┌──────────┐              │
│                │ ACQUIRE  │              │
│   Thread 2 ──▶ │  LOCK    │──▶ Execute   │
│   (waits)      └──────────┘    Critical  │
│                                Section   │
│   Thread 3 ──▶ (waits)                   │
│                                          │
│                ┌──────────┐              │
│                │ RELEASE  │◀── Done      │
│                │  LOCK    │              │
│                └──────────┘              │
│                     │                    │
│                     ▼                    │
│              Next thread enters          │
└─────────────────────────────────────────┘
```

---

# 3. Synchronized Methods

## Instance Synchronized Method

```java
class Counter {
    private int count = 0;
    
    // Lock is on 'this' object
    public synchronized void increment() {
        count++;
    }
    
    public synchronized int getCount() {
        return count;
    }
}

// Equivalent to:
public void increment() {
    synchronized (this) {
        count++;
    }
}
```

## Multiple Synchronized Methods Share Lock

```java
class BankAccount {
    private double balance;
    
    public synchronized void deposit(double amount) {
        balance += amount;
    }
    
    public synchronized void withdraw(double amount) {
        balance -= amount;
    }
    
    public synchronized double getBalance() {
        return balance;
    }
}

// All three methods use the SAME lock (this)
// Only ONE can execute at a time on same object!
```

## Different Objects = Different Locks

```java
BankAccount acc1 = new BankAccount();
BankAccount acc2 = new BankAccount();

// These can run simultaneously (different locks!)
Thread t1 = new Thread(() -> acc1.deposit(100));
Thread t2 = new Thread(() -> acc2.deposit(100));

// These CANNOT run simultaneously (same lock!)
Thread t3 = new Thread(() -> acc1.deposit(100));
Thread t4 = new Thread(() -> acc1.withdraw(50));
```

---

# 4. Synchronized Blocks

## Basic Syntax

```java
synchronized (lockObject) {
    // Critical section
    // Only one thread can be here at a time (per lock object)
}
```

## Advantages Over Synchronized Methods

1. **Finer control** - lock only what's needed
2. **Different locks** - for different resources
3. **Shorter lock duration** - better performance

```java
class BetterCounter {
    private int count1 = 0;
    private int count2 = 0;
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    public void incrementCount1() {
        synchronized (lock1) {
            count1++;
        }
    }
    
    public void incrementCount2() {
        synchronized (lock2) {
            count2++;
        }
    }
    // count1 and count2 can be incremented simultaneously!
}
```

## Lock on 'this'

```java
public void method() {
    // Some non-synchronized code
    
    synchronized (this) {
        // Only critical section is synchronized
    }
    
    // More non-synchronized code
}
```

## Lock on Class Object

```java
public void method() {
    synchronized (MyClass.class) {
        // Lock shared by ALL instances
    }
}
```

## Lock on Private Final Object (Recommended!)

```java
class SafeClass {
    private final Object lock = new Object();  // Dedicated lock
    private int data;
    
    public void modify() {
        synchronized (lock) {
            data++;
        }
    }
}
// Why private final?
// - private: external code can't use our lock
// - final: lock object never changes
```

---

# 5. Static Synchronization

## Static Synchronized Method

```java
class Counter {
    private static int count = 0;
    
    // Lock is on Counter.class (not 'this')
    public static synchronized void increment() {
        count++;
    }
}

// Equivalent to:
public static void increment() {
    synchronized (Counter.class) {
        count++;
    }
}
```

## Instance vs Static Lock

```java
class Example {
    private int instanceVar = 0;
    private static int staticVar = 0;
    
    // Lock on 'this' (instance)
    public synchronized void instanceMethod() {
        instanceVar++;
    }
    
    // Lock on Example.class (class)
    public static synchronized void staticMethod() {
        staticVar++;
    }
}

// instanceMethod and staticMethod use DIFFERENT locks!
// They can run simultaneously!
```

## Protecting Static Data

```java
class SharedResource {
    private static List<String> sharedList = new ArrayList<>();
    private static final Object listLock = new Object();
    
    public static void addItem(String item) {
        synchronized (listLock) {
            sharedList.add(item);
        }
    }
    
    public static String getItem(int index) {
        synchronized (listLock) {
            return sharedList.get(index);
        }
    }
}
```

---

# 6. Intrinsic Locks & Monitors

## What is an Intrinsic Lock?

Every Java object has a built-in lock (also called **monitor lock**).

```java
Object obj = new Object();
synchronized (obj) {  // Acquires obj's intrinsic lock
    // Critical section
}  // Releases obj's intrinsic lock
```

## Monitor Concept

```
┌────────────────── MONITOR ──────────────────┐
│                                              │
│   Entry Set          Owner          Wait Set │
│   (Blocked)                         (Waiting)│
│                                              │
│   ┌───┐                              ┌───┐  │
│   │ T2│              ┌───┐           │ T4│  │
│   │ T3│──acquire──▶  │ T1│  ◀─notify─│ T5│  │
│   └───┘              └───┘           └───┘  │
│      │                 │               ▲     │
│      │                 │               │     │
│      │                 └───wait()──────┘     │
│      │                                       │
│      └────────────────release────────────────│
└──────────────────────────────────────────────┘
```

## Reentrant Locking

Same thread can acquire the same lock multiple times:

```java
class Reentrant {
    public synchronized void outer() {
        System.out.println("Outer");
        inner();  // Same thread can acquire lock again!
    }
    
    public synchronized void inner() {
        System.out.println("Inner");
    }
}

// Without reentrancy, inner() would deadlock!
```

---

# 7. wait(), notify(), notifyAll()

## Purpose

- **wait()**: Release lock and wait for notification
- **notify()**: Wake up ONE waiting thread
- **notifyAll()**: Wake up ALL waiting threads

## Rules

1. Must be called from synchronized block/method
2. Must hold the lock on the object you call wait/notify on
3. wait() releases the lock while waiting

## Basic Pattern

```java
class MessageBox {
    private String message;
    private boolean hasMessage = false;
    
    public synchronized void put(String msg) {
        while (hasMessage) {
            wait();  // Wait until message is consumed
        }
        message = msg;
        hasMessage = true;
        notifyAll();  // Notify consumers
    }
    
    public synchronized String take() {
        while (!hasMessage) {
            wait();  // Wait until message is available
        }
        hasMessage = false;
        notifyAll();  // Notify producers
        return message;
    }
}
```

## Producer-Consumer Example

```java
class Buffer {
    private Queue<Integer> queue = new LinkedList<>();
    private int capacity = 10;
    
    public synchronized void produce(int item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();  // Buffer full, wait
        }
        queue.add(item);
        System.out.println("Produced: " + item);
        notifyAll();  // Notify consumers
    }
    
    public synchronized int consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();  // Buffer empty, wait
        }
        int item = queue.poll();
        System.out.println("Consumed: " + item);
        notifyAll();  // Notify producers
        return item;
    }
}
```

## wait() Overloads

```java
synchronized (lock) {
    lock.wait();              // Wait indefinitely
    lock.wait(1000);          // Wait max 1 second
    lock.wait(1000, 500000);  // Wait 1 sec + 500000 nanos
}
```

## notify() vs notifyAll()

| notify() | notifyAll() |
|----------|-------------|
| Wakes ONE random thread | Wakes ALL waiting threads |
| More efficient | Safer (no missed signals) |
| Risk of deadlock if wrong thread woken | All compete for lock |

**Best Practice:** Use `notifyAll()` unless you have specific reason for `notify()`.

## Spurious Wakeups - Always Use While Loop!

```java
// WRONG - may miss condition after spurious wakeup
synchronized (lock) {
    if (!condition) {
        wait();
    }
    // Proceed
}

// CORRECT - re-check condition after wakeup
synchronized (lock) {
    while (!condition) {
        wait();
    }
    // Proceed
}
```

---

# 8. volatile Keyword

## What volatile Does

1. **Visibility**: Changes immediately visible to all threads
2. **Ordering**: Prevents instruction reordering around volatile access
3. **NOT atomicity**: Does NOT make compound operations atomic!

## Memory Without volatile

```java
class Flag {
    private boolean running = true;  // May be cached!
    
    public void stop() {
        running = false;  // Other threads may not see this!
    }
    
    public void run() {
        while (running) {
            // May loop forever! Thread may use cached value
        }
    }
}
```

## Memory With volatile

```java
class Flag {
    private volatile boolean running = true;
    
    public void stop() {
        running = false;  // Writes directly to main memory
    }
    
    public void run() {
        while (running) {  // Always reads from main memory
            // Will see the change!
        }
    }
}
```

## When to Use volatile

```java
// ✅ Good use cases:
volatile boolean flag;           // Simple flag
volatile int state;              // Status indicator
volatile Object reference;       // Reference publication

// ❌ NOT suitable for:
volatile int counter;
counter++;  // NOT ATOMIC! Still has race condition!
// count++ = read + increment + write (3 operations)
```

## volatile vs synchronized

| volatile | synchronized |
|----------|--------------|
| Only visibility | Visibility + atomicity |
| No blocking | Can block |
| Single variable | Multiple variables |
| Read/write only | Complex operations |
| Faster | Slower |

---

# 9. Memory Visibility

## CPU Cache Problem

```
┌─────────────────────────────────────────────────┐
│                   MAIN MEMORY                    │
│              ┌───────────────┐                  │
│              │  shared = 0   │                  │
│              └───────────────┘                  │
│                 ▲         ▲                     │
│                 │         │                     │
│    ┌────────────┴─┐     ┌─┴────────────┐       │
│    │   CPU 1      │     │    CPU 2     │       │
│    │ ┌─────────┐  │     │ ┌─────────┐  │       │
│    │ │Cache: 0 │  │     │ │Cache: 0 │  │       │
│    │ └─────────┘  │     │ └─────────┘  │       │
│    │   Thread 1   │     │   Thread 2   │       │
│    │  shared = 1  │     │ read shared  │       │
│    │ (cache only!)│     │ (sees 0!)    │       │
│    └──────────────┘     └──────────────┘       │
└─────────────────────────────────────────────────┘
```

## Ensuring Visibility

```java
// 1. synchronized - flushes cache on unlock
synchronized (lock) {
    sharedVar = newValue;
}  // Value flushed to main memory

// 2. volatile - bypasses cache
volatile int sharedVar;
sharedVar = newValue;  // Writes directly to main memory

// 3. Final fields - safe after constructor
final int value;  // Visible to all threads after construction
```

---

# 10. Happens-Before Relationship

## What is Happens-Before?

A guarantee that memory effects of one operation are visible to another.

## Key Happens-Before Rules

```java
// 1. Program Order
int a = 1;
int b = 2;  // a=1 happens-before b=2 (in same thread)

// 2. Monitor Lock
synchronized (lock) {
    x = 1;
}  // happens-before next thread acquires same lock

// 3. volatile Write → Read
volatile int v;
v = 1;     // happens-before
int r = v; // this read (even in different thread)

// 4. Thread Start
x = 1;
thread.start();  // x=1 happens-before run() starts

// 5. Thread Join
// Everything in thread happens-before join() returns
thread.join();
int result = sharedVar;  // Sees all changes from thread

// 6. Thread Interrupt
thread.interrupt();  // happens-before interrupted thread detects it
```

## Why It Matters

```java
class Example {
    int a;
    volatile boolean ready;
    
    void writer() {
        a = 42;        // 1
        ready = true;  // 2 - volatile write
    }
    
    void reader() {
        if (ready) {         // 3 - volatile read
            System.out.println(a);  // 4 - guaranteed to see 42!
        }
    }
}
// Happens-before: 1 → 2 → 3 → 4
// So reader always sees a = 42 when ready is true
```

---

# 11. Common Patterns

## Double-Checked Locking

```java
class Singleton {
    private static volatile Singleton instance;  // Must be volatile!
    
    public static Singleton getInstance() {
        if (instance == null) {              // First check (no lock)
            synchronized (Singleton.class) {
                if (instance == null) {      // Second check (with lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

## Thread-Safe Lazy Initialization

```java
// Better than double-checked locking!
class Singleton {
    private Singleton() {}
    
    private static class Holder {
        static final Singleton INSTANCE = new Singleton();
    }
    
    public static Singleton getInstance() {
        return Holder.INSTANCE;  // Loaded on first access
    }
}
```

## Thread-Safe Counter

```java
class SafeCounter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public synchronized int get() {
        return count;
    }
}

// Or use AtomicInteger (better performance!)
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();
```

---

# 12. Interview Questions

## Q1: What is the difference between synchronized method and synchronized block?

**Answer:**
| Synchronized Method | Synchronized Block |
|--------------------|-------------------|
| Locks entire method | Locks specific code |
| Lock on `this` or `class` | Lock on any object |
| Less flexible | More flexible |
| Longer lock duration | Can minimize lock time |

---

## Q2: Can two threads execute synchronized methods on the same object simultaneously?

**Answer:**
**No**, if they're both instance synchronized methods (same lock).
**Yes**, if one is instance and one is static (different locks).
**Yes**, if they're on different objects (different locks).

---

## Q3: What is volatile? When to use it?

**Answer:**
`volatile` ensures visibility across threads. Use for:
- Simple flags (`volatile boolean stopped`)
- Single variable reads/writes
- NOT for compound operations (use synchronized/atomic)

---

## Q4: Why should wait() be called in a loop?

**Answer:**
1. **Spurious wakeups**: JVM may wake thread without notify
2. **Condition may change**: Another thread might change condition before this thread runs

```java
while (!condition) {
    wait();  // Re-check after wakeup
}
```

---

## Q5: What is happens-before relationship?

**Answer:**
A guarantee that memory writes by one operation are visible to reads by another operation. Key rules:
- Unlock happens-before subsequent lock
- Volatile write happens-before volatile read
- Thread start/join creates happens-before

---

## Q6: What is a deadlock? How to prevent it?

**Answer:**
Deadlock = Two threads waiting for each other's locks forever.

Prevention:
1. Lock ordering (always acquire in same order)
2. Lock timeout (`tryLock(timeout)`)
3. Single lock for related resources
4. Avoid nested locks

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│              SYNCHRONIZATION CHEAT SHEET                    │
├─────────────────────────────────────────────────────────────┤
│ SYNCHRONIZED                                                │
│   synchronized void method() { }    // Lock on 'this'       │
│   static synchronized void m() { }  // Lock on Class        │
│   synchronized (obj) { }            // Lock on obj          │
├─────────────────────────────────────────────────────────────┤
│ WAIT/NOTIFY (must be in synchronized block!)                │
│   obj.wait();        // Release lock, wait for notify       │
│   obj.notify();      // Wake one waiting thread             │
│   obj.notifyAll();   // Wake all waiting threads            │
├─────────────────────────────────────────────────────────────┤
│ VOLATILE                                                    │
│   volatile boolean flag;  // Visibility guarantee           │
│   // NOT for compound operations like count++               │
├─────────────────────────────────────────────────────────────┤
│ BEST PRACTICES                                              │
│   - Use private final lock objects                          │
│   - Always use while loop with wait()                       │
│   - Prefer notifyAll() over notify()                        │
│   - Keep synchronized blocks short                          │
│   - Consistent lock ordering to prevent deadlock            │
└─────────────────────────────────────────────────────────────┘
```
