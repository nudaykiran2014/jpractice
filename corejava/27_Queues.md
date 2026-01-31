# 📋 Java Queues - Complete Guide

> FIFO (First-In-First-Out) data structures and their implementations

---

# 1. Queue Basics

## Kid-Friendly Explanation 🧒

**Movie Ticket Line 🎬**
- First person in line gets the ticket first
- New people join at the END of the line
- You can only leave from the FRONT
- This is **FIFO** (First In, First Out)

## Queue Interface

```java
public interface Queue<E> extends Collection<E> {
    // Throws exception if fails
    boolean add(E e);        // Add to end
    E remove();              // Remove from front
    E element();             // Peek at front
    
    // Returns special value if fails
    boolean offer(E e);      // Add to end (returns false if full)
    E poll();                // Remove from front (returns null if empty)
    E peek();                // Peek at front (returns null if empty)
}
```

## Two Flavors of Methods

| Operation | Throws Exception | Returns Special Value |
|-----------|------------------|----------------------|
| **Insert** | `add(e)` | `offer(e)` → false |
| **Remove** | `remove()` | `poll()` → null |
| **Examine** | `element()` | `peek()` → null |

> 💡 **Tip:** Use `offer/poll/peek` when failure is expected (like bounded queues).
> Use `add/remove/element` when failure is a bug.

---

# 2. Deque Interface (Double-Ended Queue)

## Kid-Friendly Explanation 🧒

**Deck of Cards 🃏**
- You can add cards to TOP or BOTTOM
- You can take cards from TOP or BOTTOM
- Works as both **Queue** AND **Stack**!

## Deque Methods

```java
public interface Deque<E> extends Queue<E> {
    // First (Head) operations
    void addFirst(E e);
    boolean offerFirst(E e);
    E removeFirst();
    E pollFirst();
    E getFirst();
    E peekFirst();
    
    // Last (Tail) operations
    void addLast(E e);
    boolean offerLast(E e);
    E removeLast();
    E pollLast();
    E getLast();
    E peekLast();
    
    // Stack operations
    void push(E e);     // Same as addFirst()
    E pop();            // Same as removeFirst()
}
```

## Deque as Queue vs Stack

| Use As | Add Method | Remove Method |
|--------|-----------|---------------|
| **Queue (FIFO)** | `addLast()` | `removeFirst()` |
| **Stack (LIFO)** | `addFirst()` / `push()` | `removeFirst()` / `pop()` |

---

# 3. Queue Implementations

## 3.1 PriorityQueue

### Kid-Friendly Explanation 🧒

**Hospital Emergency Room 🏥**
- Patients aren't served in arrival order
- Most URGENT patient goes first!
- Heart attack patient goes before someone with a cold

### Key Points

| Feature | Description |
|---------|-------------|
| **Ordering** | Natural order OR custom Comparator |
| **Null** | NOT allowed |
| **Thread-Safe** | NO |
| **Capacity** | Grows automatically |
| **Bulk Operations** | O(n log n) |
| **peek/poll** | O(log n) |

### Code Example

```java
import java.util.PriorityQueue;

public class PriorityQueueDemo {
    public static void main(String[] args) {
        // Min-Heap by default (smallest first)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.add(5);
        minHeap.add(1);
        minHeap.add(3);
        
        System.out.println(minHeap.poll());  // 1 (smallest)
        System.out.println(minHeap.poll());  // 3
        System.out.println(minHeap.poll());  // 5
        
        // Max-Heap (largest first)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
            (a, b) -> b - a  // Reverse order
        );
        maxHeap.add(5);
        maxHeap.add(1);
        maxHeap.add(3);
        
        System.out.println(maxHeap.poll());  // 5 (largest)
    }
}
```

### Custom Object Priority

```java
class Patient {
    String name;
    int urgency;  // 1 = critical, 10 = not urgent
    
    Patient(String name, int urgency) {
        this.name = name;
        this.urgency = urgency;
    }
}

PriorityQueue<Patient> emergencyRoom = new PriorityQueue<>(
    (p1, p2) -> p1.urgency - p2.urgency  // Lower number = higher priority
);

emergencyRoom.add(new Patient("Cold", 8));
emergencyRoom.add(new Patient("Heart Attack", 1));
emergencyRoom.add(new Patient("Broken Arm", 4));

// First patient: Heart Attack (urgency 1)
Patient first = emergencyRoom.poll();
```

---

## 3.2 ArrayDeque

### Kid-Friendly Explanation 🧒

**Better than LinkedList for Stack/Queue! ⚡**
- Uses resizable array internally
- Faster than `LinkedList` for most operations
- No capacity limit (grows as needed)

### Key Points

| Feature | Description |
|---------|-------------|
| **Null** | NOT allowed |
| **Thread-Safe** | NO |
| **Performance** | Faster than `LinkedList` |
| **Memory** | More efficient (no node objects) |
| **Best For** | General-purpose Stack or Queue |

### Code Example

```java
import java.util.ArrayDeque;
import java.util.Deque;

public class ArrayDequeDemo {
    public static void main(String[] args) {
        // As a Queue (FIFO)
        Deque<String> queue = new ArrayDeque<>();
        queue.addLast("First");   // or offer()
        queue.addLast("Second");
        queue.addLast("Third");
        
        System.out.println(queue.removeFirst());  // "First"
        System.out.println(queue.removeFirst());  // "Second"
        
        // As a Stack (LIFO)
        Deque<String> stack = new ArrayDeque<>();
        stack.push("Bottom");
        stack.push("Middle");
        stack.push("Top");
        
        System.out.println(stack.pop());  // "Top"
        System.out.println(stack.pop());  // "Middle"
    }
}
```

### ArrayDeque vs LinkedList

| Feature | ArrayDeque | LinkedList |
|---------|------------|------------|
| **Memory** | Less (no node overhead) | More (node objects) |
| **Cache** | Better locality | Poor locality |
| **get(i)** | Not supported | O(n) |
| **Null elements** | NO | YES |
| **Recommend** | ✅ Usually better | Only for specific needs |

---

## 3.3 LinkedList (as Queue/Deque)

### Key Points

| Feature | Description |
|---------|-------------|
| **Implements** | `List`, `Deque`, `Queue` |
| **Null** | Allowed |
| **Thread-Safe** | NO |
| **Best For** | When you need List operations too |

### Code Example

```java
import java.util.LinkedList;
import java.util.Queue;

Queue<String> queue = new LinkedList<>();
queue.offer("A");
queue.offer("B");
queue.offer("C");

while (!queue.isEmpty()) {
    System.out.println(queue.poll());  // A, B, C
}
```

---

# 4. Blocking Queues (Concurrent)

## Kid-Friendly Explanation 🧒

**Factory Assembly Line 🏭**
- Worker A creates products and puts them on conveyor belt
- Worker B takes products from conveyor belt
- If belt is FULL, Worker A **WAITS**
- If belt is EMPTY, Worker B **WAITS**

This is the **Producer-Consumer Pattern**!

## BlockingQueue Interface

```java
public interface BlockingQueue<E> extends Queue<E> {
    // Blocking operations
    void put(E e) throws InterruptedException;     // Wait if full
    E take() throws InterruptedException;          // Wait if empty
    
    // Timed operations
    boolean offer(E e, long timeout, TimeUnit unit);  // Wait up to timeout
    E poll(long timeout, TimeUnit unit);              // Wait up to timeout
    
    // Capacity
    int remainingCapacity();
    boolean contains(Object o);
    int drainTo(Collection<? super E> c);
    int drainTo(Collection<? super E> c, int maxElements);
}
```

## Blocking Queue Method Summary

| Operation | Throws Exception | Special Value | Blocks | Times Out |
|-----------|-----------------|---------------|--------|-----------|
| **Insert** | `add(e)` | `offer(e)` | `put(e)` | `offer(e, time, unit)` |
| **Remove** | `remove()` | `poll()` | `take()` | `poll(time, unit)` |
| **Examine** | `element()` | `peek()` | N/A | N/A |

---

## 4.1 ArrayBlockingQueue

### Features

| Feature | Description |
|---------|-------------|
| **Capacity** | Fixed (set at creation) |
| **Fairness** | Optional (FIFO for waiting threads) |
| **Thread-Safe** | YES |
| **Backing** | Array |

### Code Example

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);  // Capacity 5
        
        // Producer
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = "Item-" + i;
                    queue.put(item);  // Blocks if queue is full!
                    System.out.println("Produced: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Consumer
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = queue.take();  // Blocks if queue is empty!
                    System.out.println("Consumed: " + item);
                    Thread.sleep(500);  // Slow consumer
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.start();
        consumer.start();
    }
}
```

---

## 4.2 LinkedBlockingQueue

### Features

| Feature | Description |
|---------|-------------|
| **Capacity** | Optional (default: Integer.MAX_VALUE) |
| **Thread-Safe** | YES |
| **Backing** | Linked nodes |
| **Throughput** | Higher than ArrayBlockingQueue |

### Code Example

```java
// Bounded
BlockingQueue<String> bounded = new LinkedBlockingQueue<>(100);

// Unbounded (be careful - can run out of memory!)
BlockingQueue<String> unbounded = new LinkedBlockingQueue<>();
```

### ArrayBlockingQueue vs LinkedBlockingQueue

| Feature | ArrayBlockingQueue | LinkedBlockingQueue |
|---------|-------------------|---------------------|
| **Capacity** | Fixed | Optional/Unbounded |
| **Memory** | Pre-allocated | Dynamic |
| **Lock** | Single lock | Separate put/take locks |
| **Throughput** | Lower | Higher (less contention) |

---

## 4.3 PriorityBlockingQueue

### Features

| Feature | Description |
|---------|-------------|
| **Ordering** | Natural order OR Comparator |
| **Capacity** | Unbounded |
| **Thread-Safe** | YES |
| **Blocking** | Only on `take()` (never on `put()`) |

### Code Example

```java
import java.util.concurrent.PriorityBlockingQueue;

class Task implements Comparable<Task> {
    String name;
    int priority;  // Lower = more important
    
    Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
    
    @Override
    public int compareTo(Task other) {
        return this.priority - other.priority;
    }
}

PriorityBlockingQueue<Task> taskQueue = new PriorityBlockingQueue<>();
taskQueue.put(new Task("Email", 5));
taskQueue.put(new Task("Critical Bug", 1));
taskQueue.put(new Task("Meeting", 3));

// Critical Bug processed first!
Task next = taskQueue.take();
```

---

## 4.4 DelayQueue

### Kid-Friendly Explanation 🧒

**Scheduled Alarm Clock ⏰**
- You set multiple alarms for different times
- Alarms only "ring" when their time comes
- Can't take an alarm until it's ready!

### Features

| Feature | Description |
|---------|-------------|
| **Elements** | Must implement `Delayed` interface |
| **Ordering** | By delay time |
| **Thread-Safe** | YES |
| **Blocking** | Until delay expires |

### Code Example

```java
import java.util.concurrent.*;

class DelayedTask implements Delayed {
    private String name;
    private long executeAt;  // Timestamp in milliseconds
    
    DelayedTask(String name, long delaySeconds) {
        this.name = name;
        this.executeAt = System.currentTimeMillis() + (delaySeconds * 1000);
    }
    
    @Override
    public long getDelay(TimeUnit unit) {
        long remaining = executeAt - System.currentTimeMillis();
        return unit.convert(remaining, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public int compareTo(Delayed other) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS),
                           other.getDelay(TimeUnit.MILLISECONDS));
    }
    
    public String getName() { return name; }
}

public class DelayQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        DelayQueue<DelayedTask> queue = new DelayQueue<>();
        
        queue.put(new DelayedTask("Task-5sec", 5));
        queue.put(new DelayedTask("Task-2sec", 2));
        queue.put(new DelayedTask("Task-1sec", 1));
        
        System.out.println("Waiting for tasks...");
        
        // Tasks come out in order of their delay
        System.out.println(queue.take().getName());  // After 1 sec: Task-1sec
        System.out.println(queue.take().getName());  // After 2 sec: Task-2sec
        System.out.println(queue.take().getName());  // After 5 sec: Task-5sec
    }
}
```

---

## 4.5 SynchronousQueue

### Kid-Friendly Explanation 🧒

**Direct Handshake 🤝**
- No storage at all!
- Producer WAITS until Consumer arrives
- Consumer WAITS until Producer arrives
- They exchange directly, hand-to-hand

### Features

| Feature | Description |
|---------|-------------|
| **Capacity** | 0 (no storage) |
| **Handoff** | Direct producer-consumer |
| **Fairness** | Optional |
| **Use Case** | Passing work directly between threads |

### Code Example

```java
import java.util.concurrent.SynchronousQueue;

SynchronousQueue<String> queue = new SynchronousQueue<>();

// Producer (will WAIT until consumer calls take())
new Thread(() -> {
    try {
        System.out.println("Putting item...");
        queue.put("Hello");  // Blocks until consumer takes it!
        System.out.println("Item taken by consumer!");
    } catch (InterruptedException e) {}
}).start();

Thread.sleep(2000);  // Wait 2 seconds

// Consumer
String item = queue.take();  // Immediately receives "Hello"
System.out.println("Got: " + item);
```

---

## 4.6 LinkedTransferQueue

### Kid-Friendly Explanation 🧒

**Guaranteed Delivery 📬**
- Producer can WAIT until consumer receives the item
- Like certified mail with delivery confirmation!

### Features

| Feature | Description |
|---------|-------------|
| **Capacity** | Unbounded |
| **transfer()** | Wait until consumer takes item |
| **tryTransfer()** | Non-blocking transfer |
| **Use Case** | When producer needs confirmation |

### Code Example

```java
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

TransferQueue<String> queue = new LinkedTransferQueue<>();

// Producer with guaranteed delivery
new Thread(() -> {
    try {
        System.out.println("Transferring...");
        queue.transfer("Important Message");  // WAITS until consumer takes it!
        System.out.println("Consumer received the message!");
    } catch (InterruptedException e) {}
}).start();

Thread.sleep(3000);

// Consumer
String msg = queue.take();
System.out.println("Got: " + msg);
```

---

# 5. Concurrent Queues (Non-Blocking)

## 5.1 ConcurrentLinkedQueue

### Features

| Feature | Description |
|---------|-------------|
| **Thread-Safe** | YES (lock-free) |
| **Capacity** | Unbounded |
| **Blocking** | NO |
| **Algorithm** | Lock-free using CAS |
| **Best For** | High-throughput scenarios |

### Code Example

```java
import java.util.concurrent.ConcurrentLinkedQueue;

ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

// Multiple threads can add/remove safely without locks!
queue.offer("A");
queue.offer("B");

String item = queue.poll();  // Non-blocking, returns null if empty
```

---

## 5.2 ConcurrentLinkedDeque

### Features

| Feature | Description |
|---------|-------------|
| **Thread-Safe** | YES (lock-free) |
| **Double-Ended** | YES |
| **Capacity** | Unbounded |
| **Best For** | Concurrent work-stealing algorithms |

### Code Example

```java
import java.util.concurrent.ConcurrentLinkedDeque;

ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();

deque.addFirst("First");
deque.addLast("Last");

String first = deque.pollFirst();  // "First"
String last = deque.pollLast();    // "Last"
```

---

# 6. Queue Selection Guide

## Which Queue to Use?

```
Need Queue?
│
├─> Single-threaded?
│   │
│   ├─> Need priority? → PriorityQueue
│   │
│   ├─> Need Deque? → ArrayDeque ⭐
│   │
│   └─> Need List too? → LinkedList
│
└─> Multi-threaded?
    │
    ├─> Need blocking?
    │   │
    │   ├─> Fixed capacity? → ArrayBlockingQueue
    │   │
    │   ├─> Unbounded? → LinkedBlockingQueue
    │   │
    │   ├─> Priority? → PriorityBlockingQueue
    │   │
    │   ├─> Delayed items? → DelayQueue
    │   │
    │   ├─> Direct handoff? → SynchronousQueue
    │   │
    │   └─> Guaranteed delivery? → LinkedTransferQueue
    │
    └─> Non-blocking (high throughput)?
        │
        ├─> Queue? → ConcurrentLinkedQueue
        │
        └─> Deque? → ConcurrentLinkedDeque
```

---

# 7. Summary Table

| Queue | Thread-Safe | Bounded | Blocking | Priority | Use Case |
|-------|-------------|---------|----------|----------|----------|
| `PriorityQueue` | ❌ | ❌ | ❌ | ✅ | Single-thread priority |
| `ArrayDeque` | ❌ | ❌ | ❌ | ❌ | Fast stack/queue |
| `LinkedList` | ❌ | ❌ | ❌ | ❌ | When List ops needed |
| `ArrayBlockingQueue` | ✅ | ✅ | ✅ | ❌ | Bounded producer-consumer |
| `LinkedBlockingQueue` | ✅ | Optional | ✅ | ❌ | General producer-consumer |
| `PriorityBlockingQueue` | ✅ | ❌ | ✅ | ✅ | Priority thread-safe |
| `DelayQueue` | ✅ | ❌ | ✅ | ✅ | Scheduled tasks |
| `SynchronousQueue` | ✅ | ✅ (0) | ✅ | ❌ | Direct handoff |
| `LinkedTransferQueue` | ✅ | ❌ | ✅ | ❌ | Guaranteed delivery |
| `ConcurrentLinkedQueue` | ✅ | ❌ | ❌ | ❌ | High-throughput |
| `ConcurrentLinkedDeque` | ✅ | ❌ | ❌ | ❌ | Work-stealing |

---

# 8. Interview Questions

**Q1: What is the difference between Queue and Deque?**
> Queue: FIFO only (add at end, remove from front)
> Deque: Double-ended (add/remove from both ends)

**Q2: Why use ArrayDeque over LinkedList?**
> ArrayDeque is faster (better cache locality), uses less memory (no node objects), and is the preferred implementation for stacks and queues.

**Q3: What is the difference between offer() and add()?**
> Both add elements. `add()` throws exception if fails, `offer()` returns false.

**Q4: When to use blocking vs non-blocking queues?**
> Blocking: Producer-consumer pattern, when threads should wait
> Non-blocking: High-throughput, when you can handle empty/full yourself

**Q5: What makes SynchronousQueue special?**
> It has zero capacity. Insert blocks until another thread removes. Direct handoff between threads.

**Q6: When to use DelayQueue?**
> When elements should only be available after a certain delay (scheduled tasks, cache expiration, retry mechanisms).

**Q7: Why can't PriorityQueue store null?**
> It needs to compare elements for ordering. Null cannot be compared.
