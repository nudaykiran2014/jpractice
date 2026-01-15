# Java Locks (java.util.concurrent.locks) - Complete Guide

## Table of Contents
1. [Why Explicit Locks?](#1-why-explicit-locks)
2. [Lock Interface](#2-lock-interface)
3. [ReentrantLock](#3-reentrantlock)
4. [ReentrantReadWriteLock](#4-reentrantreadwritelock)
5. [StampedLock (Java 8)](#5-stampedlock-java-8)
6. [Condition Interface](#6-condition-interface)
7. [Lock Methods](#7-lock-methods)
8. [Best Practices](#8-best-practices)
9. [Interview Questions](#9-interview-questions)

---

# 1. Why Explicit Locks?

## synchronized Limitations

| synchronized | Explicit Locks |
|--------------|----------------|
| Cannot try to acquire | `tryLock()` - non-blocking attempt |
| Cannot interrupt waiting | `lockInterruptibly()` |
| No fairness control | Fair/unfair modes |
| No timeout | `tryLock(timeout)` |
| Single condition | Multiple Conditions |
| Block-scoped only | Flexible scope |

## When to Use Explicit Locks

- Need `tryLock()` or timeout
- Need interruptible locking
- Need fairness
- Need multiple conditions
- Complex locking patterns
- Read-write separation

---

# 2. Lock Interface

```java
public interface Lock {
    void lock();                           // Acquire lock (blocking)
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();                     // Try once, return immediately
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();                         // Release lock
    Condition newCondition();              // Create Condition
}
```

## Basic Usage Pattern

```java
Lock lock = new ReentrantLock();

lock.lock();  // Acquire
try {
    // Critical section
} finally {
    lock.unlock();  // ALWAYS unlock in finally!
}
```

---

# 3. ReentrantLock

## Creating ReentrantLock

```java
// Non-fair lock (default) - better throughput
ReentrantLock lock = new ReentrantLock();

// Fair lock - threads acquire in order (FIFO)
ReentrantLock fairLock = new ReentrantLock(true);
```

## Basic Example

```java
class Counter {
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;
    
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
    
    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
```

## Reentrant = Same Thread Can Lock Multiple Times

```java
ReentrantLock lock = new ReentrantLock();

public void outer() {
    lock.lock();
    try {
        System.out.println("Outer - hold count: " + lock.getHoldCount());
        inner();  // Same thread can acquire again
    } finally {
        lock.unlock();
    }
}

public void inner() {
    lock.lock();  // OK - same thread, increments hold count
    try {
        System.out.println("Inner - hold count: " + lock.getHoldCount());
    } finally {
        lock.unlock();  // Decrements hold count
    }
}

// Output:
// Outer - hold count: 1
// Inner - hold count: 2
```

## ReentrantLock Methods

```java
ReentrantLock lock = new ReentrantLock();

lock.lock();
lock.unlock();

// Query methods
lock.isLocked();           // Is lock held by any thread?
lock.isHeldByCurrentThread(); // Is lock held by current thread?
lock.getHoldCount();       // How many times current thread holds lock
lock.isFair();             // Is this a fair lock?
lock.hasQueuedThreads();   // Are threads waiting for lock?
lock.getQueueLength();     // Approximate number waiting
lock.hasQueuedThread(t);   // Is specific thread waiting?
```

---

# 4. ReentrantReadWriteLock

## 🧒 Kid-Friendly Explanation

```
Regular Lock:  Only ONE person in the library at a time
               (whether reading or writing)

ReadWriteLock: MANY readers OR ONE writer
               - Multiple people can READ books simultaneously
               - Only ONE person can WRITE at a time
               - NO reading while writing!
```

## Why ReadWriteLock?

- **Read operations** don't modify data - safe to run concurrently
- **Write operations** modify data - need exclusive access
- Better performance for read-heavy workloads

## Creating ReadWriteLock

```java
ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
ReentrantReadWriteLock fairRwLock = new ReentrantReadWriteLock(true);

Lock readLock = rwLock.readLock();
Lock writeLock = rwLock.writeLock();
```

## Example: Thread-Safe Cache

```java
class Cache<K, V> {
    private final Map<K, V> map = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    
    public V get(K key) {
        readLock.lock();  // Multiple threads can read simultaneously
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }
    
    public void put(K key, V value) {
        writeLock.lock();  // Exclusive access for writing
        try {
            map.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }
    
    public V getOrCompute(K key, Function<K, V> compute) {
        // Try read first
        readLock.lock();
        try {
            V value = map.get(key);
            if (value != null) {
                return value;
            }
        } finally {
            readLock.unlock();
        }
        
        // Need to write - upgrade not allowed, must release read first
        writeLock.lock();
        try {
            // Double-check after acquiring write lock
            V value = map.get(key);
            if (value == null) {
                value = compute.apply(key);
                map.put(key, value);
            }
            return value;
        } finally {
            writeLock.unlock();
        }
    }
}
```

## Lock Compatibility

| | Read Lock | Write Lock |
|---|-----------|------------|
| **Read Lock** | ✅ Compatible | ❌ Blocked |
| **Write Lock** | ❌ Blocked | ❌ Blocked |

## Lock Downgrading (Allowed)

```java
// Write lock → Read lock (OK)
writeLock.lock();
try {
    // Modify data
    readLock.lock();  // Acquire read lock while holding write
} finally {
    writeLock.unlock();  // Release write lock
}
// Now holding only read lock
try {
    // Read data
} finally {
    readLock.unlock();
}
```

## Lock Upgrading (NOT Allowed!)

```java
// Read lock → Write lock (DEADLOCK!)
readLock.lock();
try {
    writeLock.lock();  // ❌ DEADLOCK! Can't upgrade
} finally {
    readLock.unlock();
}
```

---

# 5. StampedLock (Java 8)

## What's Special?

- **Optimistic reading** - even faster than read lock!
- No reentrant (same thread can't lock twice)
- Better performance than ReadWriteLock

## Three Modes

1. **Writing** - exclusive, like write lock
2. **Reading** - shared, like read lock
3. **Optimistic Reading** - no lock, just validates

## Basic Usage

```java
StampedLock lock = new StampedLock();

// Write lock
long stamp = lock.writeLock();
try {
    // Exclusive access
} finally {
    lock.unlockWrite(stamp);
}

// Read lock
long stamp = lock.readLock();
try {
    // Shared read access
} finally {
    lock.unlockRead(stamp);
}
```

## Optimistic Reading (The Magic!)

```java
class Point {
    private double x, y;
    private final StampedLock lock = new StampedLock();
    
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp);
        }
    }
    
    public double distanceFromOrigin() {
        // Optimistic read - no actual locking!
        long stamp = lock.tryOptimisticRead();
        double currentX = x;
        double currentY = y;
        
        // Check if data was modified during read
        if (!lock.validate(stamp)) {
            // Data changed, fall back to read lock
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
}
```

## Lock Conversion

```java
StampedLock lock = new StampedLock();

// Read → Write (upgrade)
long stamp = lock.readLock();
try {
    if (needToWrite) {
        long writeStamp = lock.tryConvertToWriteLock(stamp);
        if (writeStamp == 0) {
            // Conversion failed, release read and get write
            lock.unlockRead(stamp);
            writeStamp = lock.writeLock();
        }
        stamp = writeStamp;
        // Now have write lock
    }
} finally {
    lock.unlock(stamp);
}
```

## When to Use StampedLock

| Use StampedLock | Use ReentrantReadWriteLock |
|-----------------|---------------------------|
| High read contention | Need reentrant locking |
| Reads much > writes | Need Condition support |
| Performance critical | Simpler code needed |

---

# 6. Condition Interface

## What is Condition?

Like `wait()`/`notify()` but more powerful:
- Multiple conditions per lock
- Can have timeout
- Can be interrupted

## Creating Conditions

```java
ReentrantLock lock = new ReentrantLock();
Condition notEmpty = lock.newCondition();
Condition notFull = lock.newCondition();
```

## Condition Methods

```java
void await() throws InterruptedException;
void awaitUninterruptibly();
long awaitNanos(long nanosTimeout) throws InterruptedException;
boolean await(long time, TimeUnit unit) throws InterruptedException;
boolean awaitUntil(Date deadline) throws InterruptedException;
void signal();      // Like notify()
void signalAll();   // Like notifyAll()
```

## Bounded Buffer Example

```java
class BoundedBuffer<T> {
    private final Object[] items;
    private int count, putIndex, takeIndex;
    
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    
    public BoundedBuffer(int capacity) {
        items = new Object[capacity];
    }
    
    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();  // Wait until not full
            }
            items[putIndex] = item;
            putIndex = (putIndex + 1) % items.length;
            count++;
            notEmpty.signal();  // Signal that buffer is not empty
        } finally {
            lock.unlock();
        }
    }
    
    @SuppressWarnings("unchecked")
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();  // Wait until not empty
            }
            T item = (T) items[takeIndex];
            items[takeIndex] = null;
            takeIndex = (takeIndex + 1) % items.length;
            count--;
            notFull.signal();  // Signal that buffer is not full
            return item;
        } finally {
            lock.unlock();
        }
    }
}
```

## wait/notify vs Condition

| Object.wait/notify | Condition |
|--------------------|-----------|
| Single wait set | Multiple conditions |
| Used with synchronized | Used with Lock |
| No timeout variants | Rich timeout options |
| `wait()`, `notify()` | `await()`, `signal()` |

---

# 7. Lock Methods

## lock() - Blocking Acquire

```java
lock.lock();  // Blocks until lock is available
try {
    // Critical section
} finally {
    lock.unlock();
}
```

## tryLock() - Non-blocking

```java
if (lock.tryLock()) {
    try {
        // Got the lock, proceed
    } finally {
        lock.unlock();
    }
} else {
    // Couldn't get lock, do something else
}
```

## tryLock(timeout) - With Timeout

```java
try {
    if (lock.tryLock(5, TimeUnit.SECONDS)) {
        try {
            // Got the lock within 5 seconds
        } finally {
            lock.unlock();
        }
    } else {
        // Timeout - couldn't get lock in 5 seconds
    }
} catch (InterruptedException e) {
    // Thread was interrupted while waiting
    Thread.currentThread().interrupt();
}
```

## lockInterruptibly() - Interruptible

```java
try {
    lock.lockInterruptibly();  // Can be interrupted while waiting
    try {
        // Critical section
    } finally {
        lock.unlock();
    }
} catch (InterruptedException e) {
    // Thread was interrupted while waiting for lock
    Thread.currentThread().interrupt();
}
```

## Comparison

| Method | Blocks? | Interruptible? | Returns? |
|--------|---------|----------------|----------|
| `lock()` | Yes | No | void |
| `tryLock()` | No | No | boolean |
| `tryLock(timeout)` | Up to timeout | Yes | boolean |
| `lockInterruptibly()` | Yes | Yes | void |

---

# 8. Best Practices

## Always Unlock in Finally

```java
// ✅ CORRECT
lock.lock();
try {
    // Work
} finally {
    lock.unlock();  // Always executed
}

// ❌ WRONG - May not unlock on exception!
lock.lock();
// Work
lock.unlock();
```

## Prefer tryLock for Deadlock Prevention

```java
// Transfer money without deadlock
public boolean transfer(Account from, Account to, double amount) {
    while (true) {
        if (from.lock.tryLock()) {
            try {
                if (to.lock.tryLock()) {
                    try {
                        from.balance -= amount;
                        to.balance += amount;
                        return true;
                    } finally {
                        to.lock.unlock();
                    }
                }
            } finally {
                from.lock.unlock();
            }
        }
        // Couldn't get both locks, back off and retry
        Thread.sleep(random.nextInt(100));
    }
}
```

## Use Fair Locks Sparingly

```java
// Fair lock - predictable but slower
ReentrantLock fairLock = new ReentrantLock(true);

// Non-fair lock - faster but may cause starvation
ReentrantLock fastLock = new ReentrantLock(false);  // default
```

## Prefer ReadWriteLock for Read-Heavy Workloads

```java
// Many reads, few writes → Use ReadWriteLock
ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

// Balanced or write-heavy → Use ReentrantLock
ReentrantLock lock = new ReentrantLock();
```

---

# 9. Interview Questions

## Q1: Difference between synchronized and ReentrantLock?

**Answer:**
| synchronized | ReentrantLock |
|--------------|---------------|
| JVM keyword | API class |
| Auto unlock | Manual unlock |
| No tryLock | Has tryLock |
| No fairness | Fair/unfair |
| Single condition | Multiple conditions |
| Block scope | Flexible scope |

---

## Q2: What is a fair lock?

**Answer:**
A fair lock grants access in FIFO order - longest waiting thread gets lock first.
- **Fair**: Predictable, prevents starvation, but slower
- **Non-fair** (default): Faster, but may cause starvation

---

## Q3: What is the difference between ReadLock and WriteLock?

**Answer:**
- **ReadLock**: Shared - multiple threads can hold simultaneously
- **WriteLock**: Exclusive - only one thread can hold

| | Read | Write |
|---|------|-------|
| **Read** | ✅ OK | ❌ Block |
| **Write** | ❌ Block | ❌ Block |

---

## Q4: What is StampedLock's optimistic reading?

**Answer:**
Reads data WITHOUT acquiring a lock, then validates if data changed:
```java
long stamp = lock.tryOptimisticRead();
// Read data
if (!lock.validate(stamp)) {
    // Data changed, use proper read lock
}
```
Much faster when writes are rare.

---

## Q5: Why use Condition instead of wait/notify?

**Answer:**
1. **Multiple conditions** per lock (separate wait sets)
2. **Timeout variants** (`await(time, unit)`)
3. **Works with Lock** (not just synchronized)
4. **More precise signaling** (signal specific condition)

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│                    LOCKS CHEAT SHEET                        │
├─────────────────────────────────────────────────────────────┤
│ REENTRANT LOCK                                              │
│   Lock lock = new ReentrantLock();                          │
│   lock.lock(); try { } finally { lock.unlock(); }           │
│   lock.tryLock(), lock.tryLock(time, unit)                  │
│   lock.lockInterruptibly()                                  │
├─────────────────────────────────────────────────────────────┤
│ READ-WRITE LOCK                                             │
│   ReadWriteLock rwLock = new ReentrantReadWriteLock();      │
│   rwLock.readLock().lock();   // Multiple readers OK        │
│   rwLock.writeLock().lock();  // Exclusive access           │
├─────────────────────────────────────────────────────────────┤
│ STAMPED LOCK                                                │
│   StampedLock lock = new StampedLock();                     │
│   long stamp = lock.tryOptimisticRead();                    │
│   if (!lock.validate(stamp)) { stamp = lock.readLock(); }   │
├─────────────────────────────────────────────────────────────┤
│ CONDITION                                                   │
│   Condition cond = lock.newCondition();                     │
│   cond.await();     // Like wait()                          │
│   cond.signal();    // Like notify()                        │
│   cond.signalAll(); // Like notifyAll()                     │
└─────────────────────────────────────────────────────────────┘
```
