# Java Concurrent Collections - Complete Guide

## Table of Contents
1. [Why Concurrent Collections?](#1-why-concurrent-collections)
2. [ConcurrentHashMap](#2-concurrenthashmap)
3. [ConcurrentSkipListMap & Set](#3-concurrentskiplistmap--set)
4. [CopyOnWriteArrayList & Set](#4-copyonwritearraylist--set)
5. [BlockingQueue](#5-blockingqueue)
6. [ConcurrentLinkedQueue & Deque](#6-concurrentlinkedqueue--deque)
7. [Comparison Table](#7-comparison-table)
8. [Interview Questions](#8-interview-questions)

---

# 1. Why Concurrent Collections?

## The Problem with Regular Collections

```java
// NOT thread-safe!
Map<String, Integer> map = new HashMap<>();
map.put("key", 1);  // Thread 1
map.get("key");     // Thread 2 - may see corrupted data!
```

## Synchronized Wrappers - Not Good Enough

```java
Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
// Problem: Locks entire map for every operation
// Problem: Compound operations still unsafe!

// NOT atomic!
if (!syncMap.containsKey("key")) {
    syncMap.put("key", 1);  // Another thread may put between check and put!
}
```

## Concurrent Collections - The Solution

| Feature | Synchronized | Concurrent |
|---------|--------------|------------|
| Lock granularity | Entire collection | Fine-grained/lock-free |
| Compound operations | Unsafe | Atomic methods provided |
| Iterator behavior | Fail-fast | Weakly consistent |
| Performance | Poor | Excellent |

---

# 2. ConcurrentHashMap

## Creating

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
ConcurrentHashMap<String, Integer> map2 = new ConcurrentHashMap<>(16);  // Initial capacity
ConcurrentHashMap<String, Integer> map3 = new ConcurrentHashMap<>(16, 0.75f, 4);  // Concurrency level
```

## Basic Operations

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

map.put("a", 1);
map.get("a");
map.remove("a");
map.containsKey("a");
map.containsValue(1);
map.size();
map.isEmpty();
```

## Atomic Compound Operations

```java
// putIfAbsent - atomic check and put
map.putIfAbsent("key", 1);  // Only puts if absent

// computeIfAbsent - atomic compute if missing
map.computeIfAbsent("key", k -> expensiveComputation(k));

// computeIfPresent - atomic update if exists
map.computeIfPresent("key", (k, v) -> v + 1);

// compute - atomic compute (insert or update)
map.compute("key", (k, v) -> v == null ? 1 : v + 1);

// merge - atomic merge
map.merge("key", 1, (oldVal, newVal) -> oldVal + newVal);

// replace - atomic replace
map.replace("key", 10);              // Replace if exists
map.replace("key", 1, 10);           // Replace if value matches

// remove - atomic remove if matches
map.remove("key", 1);                // Remove only if value is 1
```

## Bulk Operations (Java 8+)

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// forEach - parallel iteration
map.forEach(2, (k, v) -> System.out.println(k + "=" + v));  // parallelism threshold = 2

// search - parallel search, stops at first non-null result
String result = map.search(2, (k, v) -> v > 100 ? k : null);

// reduce - parallel reduction
int sum = map.reduce(2, 
    (k, v) -> v,           // Transform
    Integer::sum);         // Reduce

// reduceKeys, reduceValues
int maxValue = map.reduceValues(2, Integer::max);
```

## KeySet, Values, EntrySet Views

```java
// Key set view (backed by map)
ConcurrentHashMap.KeySetView<String, Integer> keys = map.keySet();
keys.add("newKey");  // Adds key with default value

// Key set with default value
Set<String> keySet = map.keySet(0);  // Default value for new keys
keySet.add("key");  // Adds with value 0

// Values and EntrySet
Collection<Integer> values = map.values();
Set<Map.Entry<String, Integer>> entries = map.entrySet();
```

## Example: Thread-Safe Counter

```java
ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();

public void increment(String key) {
    counters.computeIfAbsent(key, k -> new LongAdder()).increment();
}

public long getCount(String key) {
    LongAdder adder = counters.get(key);
    return adder == null ? 0 : adder.sum();
}
```

---

# 3. ConcurrentSkipListMap & Set

## What is Skip List?

Sorted, lock-free data structure with O(log n) operations.

```
Level 3: 1 ─────────────────────────────────▶ 9
Level 2: 1 ──────────▶ 4 ──────────▶ 7 ────▶ 9
Level 1: 1 ───▶ 2 ───▶ 4 ───▶ 6 ───▶ 7 ───▶ 9
Level 0: 1 → 2 → 3 → 4 → 5 → 6 → 7 → 8 → 9
```

## ConcurrentSkipListMap

```java
ConcurrentSkipListMap<String, Integer> map = new ConcurrentSkipListMap<>();

// Basic operations
map.put("b", 2);
map.put("a", 1);
map.put("c", 3);

// Sorted! Keys in order
map.keySet();  // [a, b, c]

// NavigableMap operations
map.firstKey();           // "a"
map.lastKey();            // "c"
map.lowerKey("b");        // "a" (strictly less than)
map.floorKey("b");        // "b" (less than or equal)
map.higherKey("b");       // "c" (strictly greater)
map.ceilingKey("b");      // "b" (greater than or equal)

// Submaps
map.headMap("c");         // a, b
map.tailMap("b");         // b, c
map.subMap("a", "c");     // a, b

// Descending
map.descendingMap();
map.descendingKeySet();
```

## ConcurrentSkipListSet

```java
ConcurrentSkipListSet<String> set = new ConcurrentSkipListSet<>();

set.add("b");
set.add("a");
set.add("c");

// Sorted! Elements in order
set.first();    // "a"
set.last();     // "c"
set.lower("b"); // "a"
set.higher("b"); // "c"

// Subsets
set.headSet("c");
set.tailSet("b");
set.subSet("a", "c");
```

## When to Use?

| Use ConcurrentSkipListMap/Set | Use ConcurrentHashMap |
|-------------------------------|----------------------|
| Need sorted order | Don't need order |
| Need range queries | Need fast single-key |
| NavigableMap operations | Simple get/put |

---

# 4. CopyOnWriteArrayList & Set

## How It Works

Every write creates a new copy of the array. Reads never block.

```
Write: Copy entire array → Modify copy → Replace reference
Read:  Just read current array (no locking!)
```

## CopyOnWriteArrayList

```java
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
CopyOnWriteArrayList<String> list2 = new CopyOnWriteArrayList<>(Arrays.asList("a", "b"));

// Operations
list.add("item");
list.add(0, "first");
list.get(0);
list.remove(0);
list.set(0, "new");

// Safe iteration (snapshot)
for (String s : list) {
    list.add("new");  // OK! Iterator sees snapshot
}
```

## CopyOnWriteArraySet

```java
CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();

set.add("a");
set.add("b");
set.contains("a");
set.remove("a");

// Iteration is safe
for (String s : set) {
    set.add("new");  // OK!
}
```

## When to Use?

| Use CopyOnWrite | Don't Use |
|-----------------|-----------|
| Many reads, few writes | Frequent writes |
| Small collections | Large collections |
| Need safe iteration | Need fast writes |
| Listeners, observers | General purpose |

## Example: Event Listeners

```java
class EventSource {
    private final CopyOnWriteArrayList<EventListener> listeners = 
        new CopyOnWriteArrayList<>();
    
    public void addListener(EventListener l) {
        listeners.add(l);
    }
    
    public void removeListener(EventListener l) {
        listeners.remove(l);
    }
    
    public void fireEvent(Event e) {
        // Safe to iterate even if listeners are added/removed
        for (EventListener l : listeners) {
            l.onEvent(e);
        }
    }
}
```

---

# 5. BlockingQueue

## What is BlockingQueue?

Queue that blocks when:
- Taking from empty queue (waits for element)
- Putting to full queue (waits for space)

## BlockingQueue Implementations

| Implementation | Bounded | Ordering | Notes |
|----------------|---------|----------|-------|
| ArrayBlockingQueue | Yes | FIFO | Fixed capacity |
| LinkedBlockingQueue | Optional | FIFO | Default unbounded |
| PriorityBlockingQueue | No | Priority | Sorted elements |
| DelayQueue | No | Delay | Elements available after delay |
| SynchronousQueue | 0 | N/A | Direct handoff |
| LinkedTransferQueue | No | FIFO | More efficient transfer |

## BlockingQueue Methods

| Operation | Throws | Returns Special | Blocks | Times Out |
|-----------|--------|-----------------|--------|-----------|
| Insert | add(e) | offer(e) | put(e) | offer(e, time, unit) |
| Remove | remove() | poll() | take() | poll(time, unit) |
| Examine | element() | peek() | N/A | N/A |

## ArrayBlockingQueue

```java
BlockingQueue<String> queue = new ArrayBlockingQueue<>(100);  // Capacity 100

// Blocking operations
queue.put("item");   // Blocks if full
String item = queue.take();  // Blocks if empty

// Non-blocking
queue.offer("item");  // Returns false if full
queue.poll();         // Returns null if empty

// With timeout
queue.offer("item", 1, TimeUnit.SECONDS);
queue.poll(1, TimeUnit.SECONDS);
```

## LinkedBlockingQueue

```java
BlockingQueue<String> queue = new LinkedBlockingQueue<>();      // Unbounded
BlockingQueue<String> queue2 = new LinkedBlockingQueue<>(100);  // Bounded
```

## PriorityBlockingQueue

```java
// Natural ordering
PriorityBlockingQueue<Integer> pq = new PriorityBlockingQueue<>();
pq.put(3);
pq.put(1);
pq.put(2);
pq.take();  // 1 (smallest first)

// Custom comparator
PriorityBlockingQueue<Task> taskQueue = new PriorityBlockingQueue<>(
    10, Comparator.comparing(Task::getPriority).reversed()
);
```

## DelayQueue

```java
class DelayedTask implements Delayed {
    private final long executeTime;
    private final String name;
    
    public DelayedTask(String name, long delayMs) {
        this.name = name;
        this.executeTime = System.currentTimeMillis() + delayMs;
    }
    
    @Override
    public long getDelay(TimeUnit unit) {
        long diff = executeTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.executeTime, ((DelayedTask) o).executeTime);
    }
}

DelayQueue<DelayedTask> queue = new DelayQueue<>();
queue.put(new DelayedTask("Task1", 5000));  // Available after 5 seconds
queue.put(new DelayedTask("Task2", 1000));  // Available after 1 second

DelayedTask task = queue.take();  // Blocks until first task's delay expires
```

## SynchronousQueue

No capacity - direct handoff between threads.

```java
SynchronousQueue<String> queue = new SynchronousQueue<>();

// Producer blocks until consumer takes
new Thread(() -> {
    queue.put("item");  // Blocks until taken
}).start();

// Consumer blocks until producer puts
new Thread(() -> {
    String item = queue.take();  // Blocks until put
}).start();
```

## Producer-Consumer Example

```java
class ProducerConsumer {
    private final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
    
    public void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            queue.put(value++);  // Blocks if queue full
            System.out.println("Produced: " + value);
        }
    }
    
    public void consume() throws InterruptedException {
        while (true) {
            int value = queue.take();  // Blocks if queue empty
            System.out.println("Consumed: " + value);
            Thread.sleep(1000);
        }
    }
}
```

---

# 6. ConcurrentLinkedQueue & Deque

## ConcurrentLinkedQueue

Lock-free, unbounded, FIFO queue.

```java
ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

// Add
queue.offer("a");
queue.add("b");

// Remove
String head = queue.poll();  // null if empty
String head2 = queue.remove();  // throws if empty

// Peek
String peek = queue.peek();  // null if empty

// Size (not constant time!)
int size = queue.size();  // O(n) traversal!
```

## ConcurrentLinkedDeque

Lock-free, unbounded, double-ended queue.

```java
ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();

// Add at both ends
deque.addFirst("a");
deque.addLast("b");
deque.offerFirst("c");
deque.offerLast("d");

// Remove from both ends
deque.pollFirst();
deque.pollLast();
deque.removeFirst();
deque.removeLast();

// Peek both ends
deque.peekFirst();
deque.peekLast();
```

## When to Use?

| ConcurrentLinkedQueue/Deque | BlockingQueue |
|-----------------------------|---------------|
| Non-blocking operations needed | Blocking operations needed |
| Producer-consumer different rates | Need backpressure |
| Don't need size/bounds | Need bounded capacity |

---

# 7. Comparison Table

| Collection | Thread-Safe | Ordered | Blocking | Best For |
|------------|-------------|---------|----------|----------|
| ConcurrentHashMap | Yes | No | No | General map |
| ConcurrentSkipListMap | Yes | Sorted | No | Sorted map |
| ConcurrentSkipListSet | Yes | Sorted | No | Sorted set |
| CopyOnWriteArrayList | Yes | Insertion | No | Read-heavy list |
| CopyOnWriteArraySet | Yes | Insertion | No | Read-heavy set |
| ArrayBlockingQueue | Yes | FIFO | Yes | Bounded queue |
| LinkedBlockingQueue | Yes | FIFO | Yes | General queue |
| PriorityBlockingQueue | Yes | Priority | Yes | Priority queue |
| ConcurrentLinkedQueue | Yes | FIFO | No | Non-blocking queue |

---

# 8. Interview Questions

## Q1: ConcurrentHashMap vs synchronized HashMap?

**Answer:**
| ConcurrentHashMap | synchronized HashMap |
|-------------------|---------------------|
| Segment-level/bucket-level locking | Entire map locked |
| Atomic compound operations | Compound ops unsafe |
| Better performance | Poor performance |
| Weakly consistent iterator | Fail-fast iterator |

---

## Q2: How does ConcurrentHashMap achieve thread-safety?

**Answer:**
- Java 7: Segment-based locking (16 segments by default)
- Java 8+: CAS + synchronized on individual buckets
- Lock-free reads
- Fine-grained locking for writes

---

## Q3: What is the difference between put() and putIfAbsent()?

**Answer:**
```java
// put - always replaces
map.put("key", 1);  // Replaces existing value

// putIfAbsent - only if key doesn't exist
map.putIfAbsent("key", 1);  // Does nothing if key exists
```
`putIfAbsent` is atomic - no race condition between check and put.

---

## Q4: When to use CopyOnWriteArrayList?

**Answer:**
- When reads greatly outnumber writes
- When you need safe iteration
- For small collections
- For listener/observer lists

**Don't use** for large collections or frequent writes (copies entire array).

---

## Q5: Difference between BlockingQueue methods?

**Answer:**
| | Throws | Returns null/false | Blocks | Timeout |
|---|--------|-------------------|--------|---------|
| Insert | add | offer | put | offer(e,t,u) |
| Remove | remove | poll | take | poll(t,u) |

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│           CONCURRENT COLLECTIONS CHEAT SHEET                │
├─────────────────────────────────────────────────────────────┤
│ CONCURRENTHASHMAP                                           │
│   putIfAbsent(k, v)    - Put only if absent                 │
│   computeIfAbsent(k, f) - Compute if absent                 │
│   compute(k, f)        - Compute always                     │
│   merge(k, v, f)       - Merge values                       │
├─────────────────────────────────────────────────────────────┤
│ BLOCKING QUEUE                                              │
│   put(e) / take()      - Block until success                │
│   offer(e) / poll()    - Return immediately                 │
│   offer(e,t,u) / poll(t,u) - With timeout                   │
├─────────────────────────────────────────────────────────────┤
│ IMPLEMENTATIONS                                             │
│   ArrayBlockingQueue   - Bounded FIFO                       │
│   LinkedBlockingQueue  - Optionally bounded FIFO            │
│   PriorityBlockingQueue - Unbounded priority                │
│   SynchronousQueue     - Direct handoff                     │
│   DelayQueue           - Delayed elements                   │
├─────────────────────────────────────────────────────────────┤
│ COPY-ON-WRITE                                               │
│   CopyOnWriteArrayList - Thread-safe list, read-heavy       │
│   CopyOnWriteArraySet  - Thread-safe set, read-heavy        │
└─────────────────────────────────────────────────────────────┘
```
