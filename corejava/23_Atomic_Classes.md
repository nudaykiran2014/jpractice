# Java Atomic Classes - Complete Guide

## Table of Contents
1. [Why Atomic Classes?](#1-why-atomic-classes)
2. [AtomicInteger](#2-atomicinteger)
3. [AtomicLong](#3-atomiclong)
4. [AtomicBoolean](#4-atomicboolean)
5. [AtomicReference](#5-atomicreference)
6. [Atomic Arrays](#6-atomic-arrays)
7. [AtomicMarkableReference & AtomicStampedReference](#7-atomicmarkablereference--atomicstampedreference)
8. [LongAdder & LongAccumulator](#8-longadder--longaccumulator)
9. [Compare-And-Swap (CAS)](#9-compare-and-swap-cas)
10. [Interview Questions](#10-interview-questions)

---

# 1. Why Atomic Classes?

## The Problem

```java
// NOT thread-safe!
int count = 0;
count++;  // This is actually 3 operations:
          // 1. Read count
          // 2. Add 1
          // 3. Write back
```

```
Thread 1: Read (0) → Add → Write (1)
Thread 2:    Read (0) → Add → Write (1)  // Lost update!
Expected: 2, Actual: 1
```

## Solutions Comparison

| Solution | Performance | Blocking | Use Case |
|----------|-------------|----------|----------|
| synchronized | Slow | Yes | Complex operations |
| volatile | Fast | No | Single read/write only |
| Atomic classes | Fast | No | Single variable operations |

## Atomic = Lock-Free Thread Safety

```java
AtomicInteger count = new AtomicInteger(0);
count.incrementAndGet();  // Thread-safe, lock-free!
```

---

# 2. AtomicInteger

## Creating

```java
AtomicInteger ai = new AtomicInteger();      // Default 0
AtomicInteger ai2 = new AtomicInteger(10);   // Initial value 10
```

## Basic Operations

```java
AtomicInteger count = new AtomicInteger(0);

// Get and Set
int value = count.get();          // Read
count.set(10);                    // Write
count.lazySet(10);                // Eventually write (weaker, faster)

// Atomic operations
int old = count.getAndSet(20);    // Get current, then set (returns old)
int newVal = count.incrementAndGet();  // ++count (returns new)
int oldVal = count.getAndIncrement();  // count++ (returns old)
count.decrementAndGet();          // --count
count.getAndDecrement();          // count--

// Add
count.addAndGet(5);               // count += 5 (returns new)
count.getAndAdd(5);               // count += 5 (returns old)

// Update with function (Java 8+)
count.updateAndGet(x -> x * 2);   // Double the value
count.getAndUpdate(x -> x * 2);   // Double, return old

// Accumulate (Java 8+)
count.accumulateAndGet(5, (current, delta) -> current + delta);
```

## Compare-And-Set (CAS)

```java
AtomicInteger count = new AtomicInteger(10);

// Only update if current value matches expected
boolean success = count.compareAndSet(10, 20);  // true, now 20
boolean fail = count.compareAndSet(10, 30);     // false, still 20

// Weak version (may spuriously fail)
count.weakCompareAndSet(20, 30);
```

## Example: Thread-Safe Counter

```java
class SafeCounter {
    private final AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        count.incrementAndGet();
    }
    
    public int get() {
        return count.get();
    }
}

// Usage
SafeCounter counter = new SafeCounter();
IntStream.range(0, 1000)
    .parallel()
    .forEach(i -> counter.increment());
System.out.println(counter.get());  // Always 1000!
```

---

# 3. AtomicLong

Same as AtomicInteger but for `long` values.

```java
AtomicLong al = new AtomicLong(0L);

al.incrementAndGet();
al.addAndGet(100L);
al.compareAndSet(100L, 200L);
al.updateAndGet(x -> x * 2);
```

## Use Case: Unique ID Generator

```java
class IdGenerator {
    private static final AtomicLong counter = new AtomicLong(0);
    
    public static long nextId() {
        return counter.incrementAndGet();
    }
}

long id1 = IdGenerator.nextId();  // 1
long id2 = IdGenerator.nextId();  // 2
```

---

# 4. AtomicBoolean

```java
AtomicBoolean flag = new AtomicBoolean(false);

// Get and Set
boolean value = flag.get();
flag.set(true);

// Atomic operations
boolean old = flag.getAndSet(true);  // Get old, set new
boolean success = flag.compareAndSet(false, true);  // CAS
```

## Use Case: One-Time Initialization

```java
class OneTimeInitializer {
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    
    public void initialize() {
        if (initialized.compareAndSet(false, true)) {
            // Only one thread executes this
            doExpensiveInit();
        }
    }
}
```

## Use Case: Shutdown Flag

```java
class Service {
    private final AtomicBoolean running = new AtomicBoolean(true);
    
    public void run() {
        while (running.get()) {
            doWork();
        }
    }
    
    public void shutdown() {
        running.set(false);
    }
}
```

---

# 5. AtomicReference

For atomic operations on object references.

```java
AtomicReference<String> ref = new AtomicReference<>("Hello");

// Basic operations
String value = ref.get();
ref.set("World");
String old = ref.getAndSet("New");
boolean success = ref.compareAndSet("New", "Newer");

// Update operations (Java 8+)
ref.updateAndGet(s -> s.toUpperCase());
ref.accumulateAndGet(" World", (current, delta) -> current + delta);
```

## Use Case: Thread-Safe Lazy Initialization

```java
class LazyValue<T> {
    private final AtomicReference<T> value = new AtomicReference<>();
    private final Supplier<T> supplier;
    
    public LazyValue(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public T get() {
        T result = value.get();
        if (result == null) {
            result = supplier.get();
            if (!value.compareAndSet(null, result)) {
                result = value.get();  // Another thread won
            }
        }
        return result;
    }
}
```

## Use Case: Immutable Object Updates

```java
class ImmutableConfig {
    private final String host;
    private final int port;
    // ... constructor, getters
}

class ConfigHolder {
    private final AtomicReference<ImmutableConfig> config = 
        new AtomicReference<>(new ImmutableConfig("localhost", 8080));
    
    public void updateHost(String newHost) {
        config.updateAndGet(c -> new ImmutableConfig(newHost, c.getPort()));
    }
    
    public ImmutableConfig getConfig() {
        return config.get();
    }
}
```

---

# 6. Atomic Arrays

## AtomicIntegerArray

```java
AtomicIntegerArray array = new AtomicIntegerArray(10);  // Size 10
AtomicIntegerArray array2 = new AtomicIntegerArray(new int[]{1, 2, 3});

// Operations on specific index
array.get(0);                    // Read element at index
array.set(0, 100);               // Write element
array.incrementAndGet(0);        // Increment element at index
array.compareAndSet(0, 100, 200);// CAS on element
array.addAndGet(0, 50);          // Add to element

int length = array.length();
```

## AtomicLongArray

```java
AtomicLongArray longArray = new AtomicLongArray(10);
longArray.incrementAndGet(0);
longArray.addAndGet(5, 100L);
```

## AtomicReferenceArray

```java
AtomicReferenceArray<String> refArray = new AtomicReferenceArray<>(10);

refArray.set(0, "Hello");
refArray.compareAndSet(0, "Hello", "World");
refArray.updateAndGet(0, s -> s.toUpperCase());
```

## Use Case: Thread-Safe Cache

```java
class SimpleCache<T> {
    private final AtomicReferenceArray<T> cache;
    
    public SimpleCache(int size) {
        this.cache = new AtomicReferenceArray<>(size);
    }
    
    public T get(int index) {
        return cache.get(index);
    }
    
    public void set(int index, T value) {
        cache.set(index, value);
    }
    
    public T computeIfAbsent(int index, Supplier<T> supplier) {
        T value = cache.get(index);
        if (value == null) {
            T newValue = supplier.get();
            if (cache.compareAndSet(index, null, newValue)) {
                return newValue;
            }
            return cache.get(index);
        }
        return value;
    }
}
```

---

# 7. AtomicMarkableReference & AtomicStampedReference

## ABA Problem

```
Thread 1: Read A, plan to CAS(A → C)
Thread 2: Changes A → B → A (back to A!)
Thread 1: CAS succeeds (A → C) but missed B!
```

## AtomicStampedReference - Solve ABA with Version

```java
AtomicStampedReference<String> ref = 
    new AtomicStampedReference<>("A", 0);  // Value and stamp

// Get value and stamp
int[] stampHolder = new int[1];
String value = ref.get(stampHolder);
int stamp = stampHolder[0];

// CAS with stamp check
boolean success = ref.compareAndSet(
    "A",     // Expected value
    "B",     // New value
    0,       // Expected stamp
    1        // New stamp
);

// Get stamp only
int currentStamp = ref.getStamp();

// Get reference only
String currentRef = ref.getReference();

// Set both
ref.set("C", 2);

// Attempt stamp update
ref.attemptStamp("C", 3);
```

## AtomicMarkableReference - Boolean Mark

```java
AtomicMarkableReference<String> ref = 
    new AtomicMarkableReference<>("A", false);  // Value and mark

boolean[] markHolder = new boolean[1];
String value = ref.get(markHolder);
boolean mark = markHolder[0];

// CAS with mark
ref.compareAndSet("A", "B", false, true);

// Check if marked
ref.isMarked();

// Attempt to mark
ref.attemptMark("B", true);
```

## Use Case: Lock-Free Linked List Node Deletion

```java
class Node<T> {
    final T value;
    final AtomicMarkableReference<Node<T>> next;
    
    Node(T value, Node<T> next) {
        this.value = value;
        this.next = new AtomicMarkableReference<>(next, false);
    }
    
    // Mark node as logically deleted before physical removal
    boolean markDeleted() {
        Node<T> nextNode = next.getReference();
        return next.compareAndSet(nextNode, nextNode, false, true);
    }
}
```

---

# 8. LongAdder & LongAccumulator

## Why LongAdder?

AtomicLong under high contention has many CAS failures. LongAdder distributes updates across multiple cells.

```
AtomicLong:           LongAdder:
    ┌───┐               ┌───┐ ┌───┐ ┌───┐ ┌───┐
    │ 5 │ ← contention  │ 2 │ │ 1 │ │ 1 │ │ 1 │
    └───┘               └───┘ └───┘ └───┘ └───┘
                        sum() = 5
```

## LongAdder

```java
LongAdder adder = new LongAdder();

// Add operations
adder.add(5);
adder.increment();
adder.decrement();

// Get sum
long sum = adder.sum();
long sumThenReset = adder.sumThenReset();

// Reset
adder.reset();
```

## DoubleAdder

```java
DoubleAdder adder = new DoubleAdder();
adder.add(3.14);
double sum = adder.sum();
```

## LongAccumulator - Custom Operation

```java
// Sum accumulator
LongAccumulator sum = new LongAccumulator(Long::sum, 0);
sum.accumulate(5);
sum.accumulate(3);
long result = sum.get();  // 8

// Max accumulator
LongAccumulator max = new LongAccumulator(Long::max, Long.MIN_VALUE);
max.accumulate(10);
max.accumulate(5);
max.accumulate(15);
long maxValue = max.get();  // 15

// Min accumulator
LongAccumulator min = new LongAccumulator(Long::min, Long.MAX_VALUE);
```

## DoubleAccumulator

```java
DoubleAccumulator sum = new DoubleAccumulator(Double::sum, 0.0);
sum.accumulate(1.5);
sum.accumulate(2.5);
double result = sum.get();  // 4.0
```

## When to Use What?

| Class | Use When |
|-------|----------|
| AtomicLong | Need get/set, CAS, single thread updates |
| LongAdder | High contention, only need sum at end |
| LongAccumulator | Custom combining function |

---

# 9. Compare-And-Swap (CAS)

## How CAS Works

```java
// Pseudocode for CAS
boolean compareAndSet(expectedValue, newValue) {
    synchronized (this) {
        if (currentValue == expectedValue) {
            currentValue = newValue;
            return true;
        }
        return false;
    }
}
```

But actually implemented at hardware level (CPU instruction) - no locking!

## CAS Loop Pattern

```java
AtomicInteger value = new AtomicInteger(0);

// Increment using CAS loop
int current, next;
do {
    current = value.get();
    next = current + 1;
} while (!value.compareAndSet(current, next));

// This is what incrementAndGet() does internally!
```

## Implementing Custom Atomic Operation

```java
public static int atomicMultiply(AtomicInteger ai, int factor) {
    int current, newValue;
    do {
        current = ai.get();
        newValue = current * factor;
    } while (!ai.compareAndSet(current, newValue));
    return newValue;
}
```

---

# 10. Interview Questions

## Q1: Why use AtomicInteger instead of volatile int?

**Answer:**
`volatile` only ensures visibility, not atomicity. `count++` on volatile is still not thread-safe (read-modify-write). AtomicInteger provides atomic compound operations.

```java
volatile int count = 0;
count++;  // NOT atomic!

AtomicInteger count = new AtomicInteger(0);
count.incrementAndGet();  // Atomic!
```

---

## Q2: What is CAS? Why is it better than locking?

**Answer:**
**Compare-And-Swap**: Atomic operation that updates value only if current value matches expected.

Better than locking because:
- No thread blocking/waiting
- No context switching overhead
- Hardware-level implementation
- Better scalability under moderate contention

---

## Q3: What is the ABA problem?

**Answer:**
When a value changes from A to B to A, CAS thinks nothing changed.

Solution: Use `AtomicStampedReference` which tracks a version stamp along with the value.

---

## Q4: When to use LongAdder vs AtomicLong?

**Answer:**
- **AtomicLong**: Need get/set, CAS, moderate contention
- **LongAdder**: High contention, only need final sum (counters, statistics)

LongAdder is faster under contention but doesn't support get/CAS operations.

---

## Q5: Is AtomicInteger.incrementAndGet() faster than synchronized?

**Answer:**
Generally yes, because:
- No lock acquisition/release
- No thread blocking
- Uses CPU CAS instruction

But under very high contention, LongAdder is even faster than AtomicInteger.

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│               ATOMIC CLASSES CHEAT SHEET                    │
├─────────────────────────────────────────────────────────────┤
│ ATOMIC INTEGER/LONG                                         │
│   get(), set(v), getAndSet(v)                               │
│   incrementAndGet(), getAndIncrement()                      │
│   decrementAndGet(), getAndDecrement()                      │
│   addAndGet(delta), getAndAdd(delta)                        │
│   compareAndSet(expect, update)                             │
│   updateAndGet(func), accumulateAndGet(x, func)             │
├─────────────────────────────────────────────────────────────┤
│ ATOMIC BOOLEAN                                              │
│   get(), set(v), getAndSet(v)                               │
│   compareAndSet(expect, update)                             │
├─────────────────────────────────────────────────────────────┤
│ ATOMIC REFERENCE                                            │
│   get(), set(v), getAndSet(v)                               │
│   compareAndSet(expect, update)                             │
│   updateAndGet(func), accumulateAndGet(x, func)             │
├─────────────────────────────────────────────────────────────┤
│ LONGADDER (High Contention)                                 │
│   add(x), increment(), decrement()                          │
│   sum(), sumThenReset(), reset()                            │
├─────────────────────────────────────────────────────────────┤
│ ATOMIC STAMPED REFERENCE (ABA Solution)                     │
│   get(stampHolder), getStamp(), getReference()              │
│   compareAndSet(eRef, nRef, eStamp, nStamp)                 │
│   set(ref, stamp), attemptStamp(ref, stamp)                 │
└─────────────────────────────────────────────────────────────┘
```
