# Java Executors & Thread Pools - Complete Guide

## Table of Contents
1. [Why Executors?](#1-why-executors)
2. [Executor Interface](#2-executor-interface)
3. [ExecutorService Interface](#3-executorservice-interface)
4. [Thread Pool Types](#4-thread-pool-types)
5. [ThreadPoolExecutor](#5-threadpoolexecutor)
6. [ScheduledExecutorService](#6-scheduledexecutorservice)
7. [ForkJoinPool](#7-forkjoinpool)
8. [Shutdown & Lifecycle](#8-shutdown--lifecycle)
9. [Best Practices](#9-best-practices)
10. [Interview Questions](#10-interview-questions)

---

# 1. Why Executors?

## 🧒 Kid-Friendly Explanation

**Without Thread Pool:**
```
Task 1 → Create Thread → Do Work → Destroy Thread
Task 2 → Create Thread → Do Work → Destroy Thread
Task 3 → Create Thread → Do Work → Destroy Thread
(Creating/destroying is EXPENSIVE!)
```

**With Thread Pool:**
```
                    ┌─ Worker 1 ─┐
Task 1 ─┐          │             │
Task 2 ─┼─ Queue ─▶│─ Worker 2 ─│─▶ Results
Task 3 ─┘          │             │
                    └─ Worker 3 ─┘
(Reuse workers - EFFICIENT!)
```

## Problems with Manual Thread Management

| Problem | Solution with Executors |
|---------|------------------------|
| Thread creation overhead | Reuse threads |
| Resource exhaustion | Limit pool size |
| No task queuing | Built-in queues |
| Complex scheduling | ScheduledExecutor |
| Hard to shutdown | Lifecycle management |

---

# 2. Executor Interface

```java
public interface Executor {
    void execute(Runnable command);
}
```

## Simple Usage

```java
Executor executor = Executors.newSingleThreadExecutor();

executor.execute(() -> {
    System.out.println("Task running in: " + Thread.currentThread().getName());
});
```

---

# 3. ExecutorService Interface

Extends `Executor` with lifecycle management and Future support.

```java
public interface ExecutorService extends Executor {
    void shutdown();
    List<Runnable> shutdownNow();
    boolean isShutdown();
    boolean isTerminated();
    boolean awaitTermination(long timeout, TimeUnit unit);
    
    <T> Future<T> submit(Callable<T> task);
    <T> Future<T> submit(Runnable task, T result);
    Future<?> submit(Runnable task);
    
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks);
    <T> T invokeAny(Collection<? extends Callable<T>> tasks);
}
```

## submit() vs execute()

| execute() | submit() |
|-----------|----------|
| Returns void | Returns Future |
| Runnable only | Runnable or Callable |
| Exception lost | Exception in Future |
| Fire and forget | Track completion |

```java
ExecutorService executor = Executors.newFixedThreadPool(2);

// execute - no result
executor.execute(() -> System.out.println("Task 1"));

// submit Runnable - Future with null result
Future<?> future1 = executor.submit(() -> System.out.println("Task 2"));

// submit Callable - Future with result
Future<Integer> future2 = executor.submit(() -> 42);
Integer result = future2.get();  // 42
```

---

# 4. Thread Pool Types

## newFixedThreadPool(n)

Fixed number of threads. Tasks queue when all threads busy.

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

// Always 4 threads
// Unbounded queue (can grow infinitely!)
// Good for: Known number of concurrent tasks
```

```
┌─────────────────────────────────────┐
│   Fixed Thread Pool (size=4)         │
│                                      │
│   ┌──────┐  ┌────────────────────┐  │
│   │Queue │─▶│ T1  T2  T3  T4    │  │
│   │......│  │ (always 4 threads) │  │
│   └──────┘  └────────────────────┘  │
└─────────────────────────────────────┘
```

## newCachedThreadPool()

Creates threads as needed, reuses idle threads. Threads die after 60s idle.

```java
ExecutorService executor = Executors.newCachedThreadPool();

// Threads created on demand
// Idle threads removed after 60 seconds
// Good for: Many short-lived tasks
// Warning: Can create too many threads!
```

## newSingleThreadExecutor()

Single thread, tasks execute sequentially.

```java
ExecutorService executor = Executors.newSingleThreadExecutor();

// Only 1 thread
// Tasks execute in order (FIFO)
// Good for: Sequential task execution, logging
```

## newWorkStealingPool() (Java 8)

Uses ForkJoinPool, threads steal work from each other.

```java
ExecutorService executor = Executors.newWorkStealingPool();
ExecutorService executor2 = Executors.newWorkStealingPool(4);  // 4 parallelism

// Uses all available processors by default
// Work-stealing for better CPU utilization
// Good for: CPU-intensive, recursive tasks
```

## newVirtualThreadPerTaskExecutor() (Java 21)

Creates a virtual thread for each task!

```java
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

// One virtual thread per task
// Millions of concurrent tasks possible!
// Good for: I/O-bound tasks, high concurrency
```

## Comparison

| Pool Type | Threads | Queue | Use Case |
|-----------|---------|-------|----------|
| Fixed | N (constant) | Unbounded | Known workload |
| Cached | 0 to ∞ | SynchronousQueue | Many short tasks |
| Single | 1 | Unbounded | Sequential tasks |
| WorkStealing | N (cores) | Work queues | CPU-bound |
| VirtualThread | 1 per task | N/A | I/O-bound, Java 21 |

---

# 5. ThreadPoolExecutor

## Constructor Parameters

```java
ThreadPoolExecutor(
    int corePoolSize,      // Min threads to keep alive
    int maximumPoolSize,   // Max threads allowed
    long keepAliveTime,    // Idle thread timeout
    TimeUnit unit,         // Time unit
    BlockingQueue<Runnable> workQueue,  // Task queue
    ThreadFactory threadFactory,        // Creates threads
    RejectedExecutionHandler handler    // When queue is full
)
```

## Custom Thread Pool

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    2,                      // Core: 2 threads
    10,                     // Max: 10 threads
    60, TimeUnit.SECONDS,   // Idle timeout
    new ArrayBlockingQueue<>(100),  // Bounded queue
    new CustomThreadFactory(),
    new ThreadPoolExecutor.CallerRunsPolicy()  // Rejection handler
);
```

## How It Works

```
1. Task arrives
   ├─ Core threads available? → Run immediately
   ├─ Core full, queue not full? → Add to queue
   ├─ Queue full, < max threads? → Create new thread
   └─ Queue full, max threads? → Rejection handler
```

## Queue Types

| Queue | Behavior |
|-------|----------|
| `LinkedBlockingQueue` | Unbounded, tasks always queue |
| `ArrayBlockingQueue` | Bounded, may reject tasks |
| `SynchronousQueue` | No capacity, direct handoff |
| `PriorityBlockingQueue` | Priority ordering |

## Rejection Handlers

```java
// AbortPolicy (default) - throws RejectedExecutionException
new ThreadPoolExecutor.AbortPolicy()

// CallerRunsPolicy - caller's thread runs the task
new ThreadPoolExecutor.CallerRunsPolicy()

// DiscardPolicy - silently discard task
new ThreadPoolExecutor.DiscardPolicy()

// DiscardOldestPolicy - discard oldest queued task
new ThreadPoolExecutor.DiscardOldestPolicy()

// Custom handler
RejectedExecutionHandler custom = (task, executor) -> {
    System.out.println("Task rejected: " + task);
    // Log, retry, etc.
};
```

## Monitoring

```java
ThreadPoolExecutor executor = ...;

executor.getPoolSize();          // Current threads
executor.getActiveCount();       // Threads running tasks
executor.getCompletedTaskCount();// Completed tasks
executor.getTaskCount();         // Total scheduled tasks
executor.getQueue().size();      // Queued tasks
executor.getLargestPoolSize();   // Peak threads
```

---

# 6. ScheduledExecutorService

## Creating

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
ScheduledExecutorService single = Executors.newSingleThreadScheduledExecutor();
```

## schedule() - Run Once After Delay

```java
// Run once after 5 seconds
ScheduledFuture<?> future = scheduler.schedule(
    () -> System.out.println("Delayed task"),
    5,
    TimeUnit.SECONDS
);

// With return value
ScheduledFuture<Integer> result = scheduler.schedule(
    () -> 42,
    5,
    TimeUnit.SECONDS
);
```

## scheduleAtFixedRate() - Periodic at Fixed Rate

```java
// Run every 2 seconds (regardless of task duration)
ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(
    () -> System.out.println("Tick: " + System.currentTimeMillis()),
    0,                  // Initial delay
    2,                  // Period
    TimeUnit.SECONDS
);

// Timeline: 0s → 2s → 4s → 6s (fixed intervals)
// If task takes longer than period, next runs immediately after
```

## scheduleWithFixedDelay() - Fixed Delay Between Runs

```java
// Run with 2 second gap AFTER each completion
ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(
    () -> {
        System.out.println("Task");
        Thread.sleep(1000);  // Task takes 1 second
    },
    0,                  // Initial delay
    2,                  // Delay after completion
    TimeUnit.SECONDS
);

// Timeline: 0s → (1s task) → 2s delay → 3s → (1s task) → 2s delay → 6s
```

## Fixed Rate vs Fixed Delay

| scheduleAtFixedRate | scheduleWithFixedDelay |
|---------------------|------------------------|
| Fixed start times | Fixed gaps between runs |
| May overlap if slow | Never overlaps |
| For time-critical | For sequential tasks |

## Cancel Scheduled Task

```java
ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(...);

// Cancel
future.cancel(false);  // Don't interrupt if running
future.cancel(true);   // Interrupt if running

// Check
future.isCancelled();
future.isDone();
```

---

# 7. ForkJoinPool

## 🧒 Kid-Friendly Explanation

**Divide and Conquer!**
```
Big Task
    ├── Subtask 1
    │     ├── Sub-subtask 1a
    │     └── Sub-subtask 1b
    └── Subtask 2
          ├── Sub-subtask 2a
          └── Sub-subtask 2b

Each worker can steal tasks from others!
```

## Creating ForkJoinPool

```java
// Default (uses available processors)
ForkJoinPool pool = ForkJoinPool.commonPool();

// Custom
ForkJoinPool pool = new ForkJoinPool(4);  // 4 threads
```

## RecursiveTask (Returns Result)

```java
class SumTask extends RecursiveTask<Long> {
    private final long[] array;
    private final int start, end;
    private static final int THRESHOLD = 10000;
    
    public SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected Long compute() {
        int length = end - start;
        
        if (length <= THRESHOLD) {
            // Small enough, compute directly
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }
        
        // Split into two subtasks
        int mid = start + length / 2;
        SumTask left = new SumTask(array, start, mid);
        SumTask right = new SumTask(array, mid, end);
        
        left.fork();  // Submit left to pool
        long rightResult = right.compute();  // Compute right in this thread
        long leftResult = left.join();  // Wait for left
        
        return leftResult + rightResult;
    }
}

// Usage
long[] array = new long[1000000];
ForkJoinPool pool = ForkJoinPool.commonPool();
Long sum = pool.invoke(new SumTask(array, 0, array.length));
```

## RecursiveAction (No Result)

```java
class SortTask extends RecursiveAction {
    private final int[] array;
    private final int start, end;
    
    @Override
    protected void compute() {
        if (end - start <= 1000) {
            Arrays.sort(array, start, end);
        } else {
            int mid = (start + end) / 2;
            invokeAll(
                new SortTask(array, start, mid),
                new SortTask(array, mid, end)
            );
            merge(array, start, mid, end);
        }
    }
}
```

## Work Stealing

```
┌─────────────────────────────────────────────────┐
│ Worker 1: [Task A] [Task B] [Task C]            │
│ Worker 2: [Task D] [          ]   ← idle        │
│ Worker 3: [Task E] [Task F]                     │
│                                                  │
│ Worker 2 STEALS Task C from Worker 1's queue!   │
└─────────────────────────────────────────────────┘
```

---

# 8. Shutdown & Lifecycle

## Proper Shutdown Pattern

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

// Submit tasks...

// Shutdown
executor.shutdown();  // Stop accepting new tasks

try {
    // Wait for existing tasks to complete
    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
        executor.shutdownNow();  // Force shutdown
        
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            System.err.println("Pool did not terminate");
        }
    }
} catch (InterruptedException e) {
    executor.shutdownNow();
    Thread.currentThread().interrupt();
}
```

## shutdown() vs shutdownNow()

| shutdown() | shutdownNow() |
|------------|---------------|
| Graceful | Forceful |
| Completes running + queued | Interrupts running |
| No new tasks accepted | Returns unexecuted tasks |
| Non-blocking | Non-blocking |

## Lifecycle States

```
┌─────────────────────────────────────────────────┐
│  RUNNING ──shutdown()──▶ SHUTDOWN               │
│     │                        │                  │
│     │                        │ (all tasks done) │
│     │                        ▼                  │
│     └──shutdownNow()──▶ STOP ──▶ TERMINATED     │
└─────────────────────────────────────────────────┘
```

## Try-with-Resources (Java 19+)

```java
try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
    executor.submit(() -> doWork());
    // Auto-shutdown when leaving try block
}
```

---

# 9. Best Practices

## 1. Always Shutdown Executors

```java
// ✅ CORRECT
ExecutorService executor = Executors.newFixedThreadPool(4);
try {
    // Use executor
} finally {
    executor.shutdown();
}

// ❌ WRONG - resource leak!
Executors.newFixedThreadPool(4).execute(() -> { });
// Executor never shutdown, threads keep running!
```

## 2. Name Your Threads

```java
ThreadFactory namedFactory = new ThreadFactory() {
    private final AtomicInteger counter = new AtomicInteger(1);
    
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName("MyPool-Worker-" + counter.getAndIncrement());
        return t;
    }
};

ExecutorService executor = Executors.newFixedThreadPool(4, namedFactory);
```

## 3. Use Bounded Queues

```java
// ✅ Bounded - prevents memory issues
new ThreadPoolExecutor(2, 10, 60, TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(1000));

// ❌ Unbounded - can cause OutOfMemoryError
Executors.newFixedThreadPool(4);  // Uses unbounded LinkedBlockingQueue
```

## 4. Handle Rejected Tasks

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(...);
executor.setRejectedExecutionHandler((task, pool) -> {
    log.warn("Task rejected, queue full!");
    // Option: block caller, log, throw, etc.
});
```

## 5. Size Pool Appropriately

```java
// CPU-bound tasks
int cpuBound = Runtime.getRuntime().availableProcessors();

// I/O-bound tasks (more threads for waiting)
int ioBound = Runtime.getRuntime().availableProcessors() * 2;

// Or calculate based on wait ratio
// threads = cores * (1 + waitTime/computeTime)
```

---

# 10. Interview Questions

## Q1: What is the difference between execute() and submit()?

**Answer:**
| execute() | submit() |
|-----------|----------|
| Returns void | Returns Future |
| Only Runnable | Runnable or Callable |
| Exception propagates | Exception stored in Future |

---

## Q2: What happens when you submit a task to a fixed thread pool with all threads busy?

**Answer:**
The task is added to the queue and waits until a thread becomes available. With default `LinkedBlockingQueue`, it can wait indefinitely.

---

## Q3: Difference between newFixedThreadPool and newCachedThreadPool?

**Answer:**
| Fixed | Cached |
|-------|--------|
| Fixed N threads | 0 to unlimited threads |
| Unbounded queue | SynchronousQueue (handoff) |
| Threads stay alive | Idle threads die after 60s |
| For known workload | For many short-lived tasks |

---

## Q4: How does ForkJoinPool's work-stealing work?

**Answer:**
Each worker has its own deque. When a worker's deque is empty, it steals tasks from the tail of another worker's deque. This balances load automatically and reduces contention.

---

## Q5: What is the difference between scheduleAtFixedRate and scheduleWithFixedDelay?

**Answer:**
- **scheduleAtFixedRate**: Fixed intervals between START times
- **scheduleWithFixedDelay**: Fixed delay after COMPLETION

If task takes 1s and period/delay is 2s:
- FixedRate: 0s, 2s, 4s, 6s (start times)
- FixedDelay: 0s, 3s, 6s, 9s (2s after each completion)

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│                  EXECUTORS CHEAT SHEET                      │
├─────────────────────────────────────────────────────────────┤
│ CREATE EXECUTOR                                             │
│   Executors.newFixedThreadPool(n)      // Fixed N threads   │
│   Executors.newCachedThreadPool()      // Dynamic threads   │
│   Executors.newSingleThreadExecutor()  // Single thread     │
│   Executors.newScheduledThreadPool(n)  // Scheduled tasks   │
│   Executors.newVirtualThreadPerTaskExecutor() // Java 21    │
├─────────────────────────────────────────────────────────────┤
│ SUBMIT TASKS                                                │
│   executor.execute(runnable);          // No result         │
│   Future<?> f = executor.submit(runnable);                  │
│   Future<T> f = executor.submit(callable);                  │
├─────────────────────────────────────────────────────────────┤
│ SCHEDULED                                                   │
│   scheduler.schedule(task, delay, unit);                    │
│   scheduler.scheduleAtFixedRate(task, init, period, unit);  │
│   scheduler.scheduleWithFixedDelay(task, init, delay, unit);│
├─────────────────────────────────────────────────────────────┤
│ SHUTDOWN                                                    │
│   executor.shutdown();                 // Graceful          │
│   executor.shutdownNow();              // Forceful          │
│   executor.awaitTermination(time, unit);                    │
└─────────────────────────────────────────────────────────────┘
```
