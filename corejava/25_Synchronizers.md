# Java Synchronizers - Complete Guide

## Table of Contents
1. [What are Synchronizers?](#1-what-are-synchronizers)
2. [CountDownLatch](#2-countdownlatch)
3. [CyclicBarrier](#3-cyclicbarrier)
4. [Semaphore](#4-semaphore)
5. [Exchanger](#5-exchanger)
6. [Phaser](#6-phaser)
7. [Comparison](#7-comparison)
8. [Interview Questions](#8-interview-questions)

---

# 1. What are Synchronizers?

## 🧒 Kid-Friendly Explanation

**Synchronizers = Traffic controllers for threads**

- **CountDownLatch**: Wait until N things happen (one-time gate)
- **CyclicBarrier**: Wait for N friends at meeting point (reusable)
- **Semaphore**: Only N people allowed in room at once
- **Exchanger**: Two people swap items
- **Phaser**: Flexible multi-phase coordination

---

# 2. CountDownLatch

## 🧒 Kid-Friendly Explanation

Like a rocket launch countdown:
```
5... 4... 3... 2... 1... LAUNCH! 🚀
(Wait until count reaches 0)
```

## How It Works

```
┌────────────────────────────────────────────────────┐
│  CountDownLatch (count = 3)                        │
│                                                    │
│  Thread A: await() ──┐                             │
│  Thread B: await() ──┼── BLOCKED until count = 0  │
│  Thread C: await() ──┘                             │
│                                                    │
│  Worker 1: countDown() → count = 2                │
│  Worker 2: countDown() → count = 1                │
│  Worker 3: countDown() → count = 0 → RELEASE ALL! │
└────────────────────────────────────────────────────┘
```

## Creating and Using

```java
CountDownLatch latch = new CountDownLatch(3);  // Count = 3

// Waiting thread
latch.await();                    // Block until count = 0
latch.await(5, TimeUnit.SECONDS); // With timeout
long remaining = latch.getCount(); // Check count

// Working threads
latch.countDown();  // Decrement count (never throws)
```

## Example: Wait for Multiple Services

```java
public class ServiceStarter {
    public static void main(String[] args) throws InterruptedException {
        int serviceCount = 3;
        CountDownLatch latch = new CountDownLatch(serviceCount);
        
        // Start services
        new Thread(new Service("Database", latch)).start();
        new Thread(new Service("Cache", latch)).start();
        new Thread(new Service("MessageQueue", latch)).start();
        
        System.out.println("Waiting for services to start...");
        latch.await();  // Block until all services ready
        System.out.println("All services started! Application ready.");
    }
}

class Service implements Runnable {
    private final String name;
    private final CountDownLatch latch;
    
    public Service(String name, CountDownLatch latch) {
        this.name = name;
        this.latch = latch;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 3000));
            System.out.println(name + " started!");
            latch.countDown();  // Signal ready
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

## Example: Parallel Task Execution

```java
public void runParallelTasks() throws InterruptedException {
    int taskCount = 5;
    CountDownLatch startSignal = new CountDownLatch(1);  // Start together
    CountDownLatch doneSignal = new CountDownLatch(taskCount);  // Wait for all
    
    for (int i = 0; i < taskCount; i++) {
        final int taskId = i;
        new Thread(() -> {
            try {
                startSignal.await();  // Wait for start signal
                doTask(taskId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                doneSignal.countDown();  // Signal completion
            }
        }).start();
    }
    
    System.out.println("Starting all tasks...");
    startSignal.countDown();  // Let all tasks begin
    
    doneSignal.await();  // Wait for all to complete
    System.out.println("All tasks completed!");
}
```

## Key Points

- **One-time use** - cannot be reset
- **countDown() never blocks** - can be called from anywhere
- **Count can only decrease** - never increase
- **Thread-safe** - multiple threads can call countDown()

---

# 3. CyclicBarrier

## 🧒 Kid-Friendly Explanation

Like friends agreeing to meet at a restaurant:
```
"Let's all meet at 7pm, no one eats until everyone arrives!"
(Reusable - can meet again tomorrow)
```

## How It Works

```
┌────────────────────────────────────────────────────┐
│  CyclicBarrier (parties = 3)                       │
│                                                    │
│  Thread A: await() ──┐                             │
│                      │── waiting (count = 1)      │
│  Thread B: await() ──┤                             │
│                      │── waiting (count = 2)      │
│  Thread C: await() ──┘                             │
│                      └── count = 3 = parties      │
│                          BARRIER TRIPPED!          │
│                          Run barrierAction         │
│                          ALL RELEASED!             │
│                          (Reset for next use)      │
└────────────────────────────────────────────────────┘
```

## Creating and Using

```java
// Without barrier action
CyclicBarrier barrier = new CyclicBarrier(3);

// With barrier action (runs when all arrive)
CyclicBarrier barrier = new CyclicBarrier(3, () -> {
    System.out.println("All parties arrived! Barrier tripped.");
});

// Waiting
barrier.await();                      // Block until all arrive
barrier.await(5, TimeUnit.SECONDS);   // With timeout

// Query
barrier.getParties();        // Number of parties required
barrier.getNumberWaiting();  // Currently waiting
barrier.isBroken();          // Is barrier broken?
barrier.reset();             // Reset (breaks waiting threads)
```

## Example: Parallel Matrix Computation

```java
class MatrixMultiplier {
    private final int[][] result;
    private final CyclicBarrier barrier;
    
    public MatrixMultiplier(int size, int workers) {
        this.result = new int[size][size];
        this.barrier = new CyclicBarrier(workers, () -> {
            System.out.println("Phase completed, merging results...");
        });
    }
    
    public void computeRow(int row) {
        try {
            // Phase 1: Compute partial results
            computePartial(row);
            barrier.await();  // Wait for all
            
            // Phase 2: Merge results
            mergeResults(row);
            barrier.await();  // Wait for all
            
            // Phase 3: Validate
            validateRow(row);
            barrier.await();  // Wait for all
            
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

## Example: Game Simulation

```java
class GameSimulation {
    private final CyclicBarrier barrier;
    private int round = 0;
    
    public GameSimulation(int players) {
        this.barrier = new CyclicBarrier(players, () -> {
            round++;
            System.out.println("Round " + round + " completed!");
        });
    }
    
    public void playRound(String player) throws Exception {
        while (round < 10) {
            System.out.println(player + " playing round " + (round + 1));
            Thread.sleep((long) (Math.random() * 1000));
            
            barrier.await();  // Wait for all players to finish round
        }
    }
}
```

## CountDownLatch vs CyclicBarrier

| CountDownLatch | CyclicBarrier |
|----------------|---------------|
| One-time use | Reusable |
| N threads count down, M threads wait | N threads wait for each other |
| await() and countDown() different | All threads call await() |
| No barrier action | Has barrier action |
| Cannot reset | Can reset |

---

# 4. Semaphore

## 🧒 Kid-Friendly Explanation

Like a parking lot with limited spaces:
```
🅿️ Parking Lot (5 spaces)
Car arrives → acquire() → if space available, enter
Car leaves → release() → free up space
If full → wait or go away
```

## How It Works

```
┌────────────────────────────────────────────────────┐
│  Semaphore (permits = 3)                           │
│                                                    │
│  Thread A: acquire() → permit granted (2 left)    │
│  Thread B: acquire() → permit granted (1 left)    │
│  Thread C: acquire() → permit granted (0 left)    │
│  Thread D: acquire() → BLOCKED (no permits)       │
│                                                    │
│  Thread A: release() → permit returned (1 left)   │
│  Thread D: → UNBLOCKED, gets permit              │
└────────────────────────────────────────────────────┘
```

## Creating and Using

```java
// Non-fair (default)
Semaphore semaphore = new Semaphore(3);

// Fair (FIFO order)
Semaphore semaphore = new Semaphore(3, true);

// Acquire
semaphore.acquire();                  // Block until permit available
semaphore.acquire(2);                 // Acquire 2 permits
semaphore.acquireUninterruptibly();   // Cannot be interrupted
boolean got = semaphore.tryAcquire(); // Non-blocking
boolean got = semaphore.tryAcquire(5, TimeUnit.SECONDS);  // Timeout

// Release
semaphore.release();     // Release 1 permit
semaphore.release(2);    // Release 2 permits

// Query
semaphore.availablePermits();  // Available permits
semaphore.hasQueuedThreads();  // Threads waiting?
semaphore.getQueueLength();    // How many waiting
```

## Example: Connection Pool

```java
class ConnectionPool {
    private final Semaphore semaphore;
    private final BlockingQueue<Connection> pool;
    
    public ConnectionPool(int size) {
        this.semaphore = new Semaphore(size);
        this.pool = new LinkedBlockingQueue<>();
        for (int i = 0; i < size; i++) {
            pool.add(createConnection());
        }
    }
    
    public Connection acquire() throws InterruptedException {
        semaphore.acquire();  // Block if no connections available
        return pool.poll();
    }
    
    public void release(Connection conn) {
        pool.offer(conn);
        semaphore.release();  // Signal connection available
    }
}

// Usage
ConnectionPool pool = new ConnectionPool(10);
Connection conn = pool.acquire();
try {
    // Use connection
} finally {
    pool.release(conn);
}
```

## Example: Rate Limiter

```java
class RateLimiter {
    private final Semaphore semaphore;
    
    public RateLimiter(int maxRequestsPerSecond) {
        this.semaphore = new Semaphore(maxRequestsPerSecond);
        
        // Refill permits every second
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            int permitsToAdd = maxRequestsPerSecond - semaphore.availablePermits();
            if (permitsToAdd > 0) {
                semaphore.release(permitsToAdd);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
    
    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }
    
    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }
}
```

## Binary Semaphore (Mutex)

```java
Semaphore mutex = new Semaphore(1);  // Only 1 permit = mutex

mutex.acquire();
try {
    // Critical section - only one thread at a time
} finally {
    mutex.release();
}
```

---

# 5. Exchanger

## 🧒 Kid-Friendly Explanation

Like two people exchanging gifts:
```
Person A: "I have 🎁" → exchange() → receives 📦
Person B: "I have 📦" → exchange() → receives 🎁
(Both must arrive to exchange)
```

## How It Works

```
┌────────────────────────────────────────────────────┐
│  Exchanger                                         │
│                                                    │
│  Thread A: exchange("A's data") ─┐                │
│                                  │── WAIT         │
│  Thread B: exchange("B's data") ─┘                │
│                                  ↓                │
│                              EXCHANGE!            │
│  Thread A receives: "B's data"                    │
│  Thread B receives: "A's data"                    │
└────────────────────────────────────────────────────┘
```

## Creating and Using

```java
Exchanger<String> exchanger = new Exchanger<>();

// Thread A
String received = exchanger.exchange("Data from A");  // Blocks until B arrives

// Thread B  
String received = exchanger.exchange("Data from B");  // Blocks until A arrives

// With timeout
String received = exchanger.exchange("Data", 5, TimeUnit.SECONDS);
```

## Example: Double Buffering

```java
class DoubleBuffer {
    private final Exchanger<List<String>> exchanger = new Exchanger<>();
    
    // Producer fills buffer, exchanges for empty
    public void produce() throws InterruptedException {
        List<String> buffer = new ArrayList<>();
        while (true) {
            // Fill buffer
            for (int i = 0; i < 10; i++) {
                buffer.add("Item " + i);
            }
            // Exchange full buffer for empty one
            buffer = exchanger.exchange(buffer);
            buffer.clear();
        }
    }
    
    // Consumer processes buffer, exchanges empty for full
    public void consume() throws InterruptedException {
        List<String> buffer = new ArrayList<>();
        while (true) {
            // Exchange empty buffer for full one
            buffer = exchanger.exchange(buffer);
            // Process items
            for (String item : buffer) {
                process(item);
            }
        }
    }
}
```

---

# 6. Phaser

## 🧒 Kid-Friendly Explanation

Like a multi-phase relay race:
```
Phase 0: All runners start → wait for all to finish
Phase 1: All runners do next leg → wait for all
Phase 2: Continue... (dynamic participants)
```

## Advanced Features

- **Dynamic registration** - threads can join/leave
- **Multiple phases** - automatic phase advancement
- **Tiered structure** - can have parent phasers

## Creating and Using

```java
Phaser phaser = new Phaser();           // No initial parties
Phaser phaser = new Phaser(3);          // 3 parties
Phaser phaser = new Phaser(parent, 3);  // With parent phaser

// Register/Deregister
phaser.register();              // Add a party
phaser.bulkRegister(5);         // Add 5 parties
phaser.arriveAndDeregister();   // Arrive and leave

// Synchronization
phaser.arrive();                // Arrive, don't wait
phaser.arriveAndAwaitAdvance(); // Arrive and wait for others
phaser.awaitAdvance(phase);     // Wait for specific phase

// Query
phaser.getPhase();              // Current phase number
phaser.getRegisteredParties();  // Number of parties
phaser.getArrivedParties();     // Arrived at current phase
phaser.getUnarrivedParties();   // Not yet arrived
phaser.isTerminated();          // Is phaser terminated
```

## Example: Multi-Phase Task

```java
class PhasedTask implements Runnable {
    private final Phaser phaser;
    private final String name;
    
    public PhasedTask(Phaser phaser, String name) {
        this.phaser = phaser;
        this.name = name;
        phaser.register();  // Register this party
    }
    
    @Override
    public void run() {
        // Phase 0: Initialize
        System.out.println(name + " initializing...");
        phaser.arriveAndAwaitAdvance();
        
        // Phase 1: Process
        System.out.println(name + " processing...");
        phaser.arriveAndAwaitAdvance();
        
        // Phase 2: Cleanup
        System.out.println(name + " cleaning up...");
        phaser.arriveAndDeregister();  // Done, leave
    }
}

// Usage
Phaser phaser = new Phaser(1);  // Register main thread
for (int i = 0; i < 3; i++) {
    new Thread(new PhasedTask(phaser, "Task-" + i)).start();
}
phaser.arriveAndDeregister();  // Main thread done
```

## Example: Dynamic Participants

```java
Phaser phaser = new Phaser() {
    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        System.out.println("Phase " + phase + " completed with " + registeredParties + " parties");
        return registeredParties == 0;  // Terminate if no parties
    }
};

// Parties can join and leave dynamically
phaser.register();          // Join
// ... do work ...
phaser.arriveAndDeregister(); // Leave
```

---

# 7. Comparison

| Synchronizer | Parties | Reusable | Use Case |
|--------------|---------|----------|----------|
| CountDownLatch | N count down, M wait | No | Wait for N events |
| CyclicBarrier | N wait for each other | Yes | Phased computation |
| Semaphore | Limit N concurrent | Yes | Resource pool |
| Exchanger | Exactly 2 | Yes | Data exchange |
| Phaser | Dynamic | Yes | Flexible phases |

## When to Use What?

```
Need to wait for N things to happen once?
→ CountDownLatch

Need N threads to repeatedly synchronize?
→ CyclicBarrier

Need to limit concurrent access to N?
→ Semaphore

Need two threads to exchange data?
→ Exchanger

Need dynamic parties or complex phasing?
→ Phaser
```

---

# 8. Interview Questions

## Q1: Difference between CountDownLatch and CyclicBarrier?

**Answer:**
| CountDownLatch | CyclicBarrier |
|----------------|---------------|
| One-time use | Reusable |
| countDown() and await() separate | All call await() |
| N events, M waiters | N threads wait for each other |
| No barrier action | Has barrier action |

---

## Q2: How does Semaphore differ from mutex/synchronized?

**Answer:**
- **Semaphore**: Can have N permits (N threads allowed)
- **Mutex/synchronized**: Only 1 thread allowed

Semaphore with 1 permit = binary semaphore ≈ mutex

---

## Q3: When would you use Phaser over CyclicBarrier?

**Answer:**
Use Phaser when:
- Number of parties changes dynamically
- Need more than one phase
- Need hierarchical synchronization
- Want onAdvance() callback

---

## Q4: Can Semaphore permits exceed initial count?

**Answer:**
Yes! `release()` can be called without prior `acquire()`, increasing permits beyond initial count. This can be useful but also dangerous if misused.

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│              SYNCHRONIZERS CHEAT SHEET                      │
├─────────────────────────────────────────────────────────────┤
│ COUNTDOWNLATCH (one-time, N→0)                              │
│   new CountDownLatch(n)                                     │
│   latch.countDown()    // Decrement                         │
│   latch.await()        // Wait for zero                     │
├─────────────────────────────────────────────────────────────┤
│ CYCLICBARRIER (reusable, all wait)                          │
│   new CyclicBarrier(n, action)                              │
│   barrier.await()      // Wait for all parties              │
│   barrier.reset()      // Reset barrier                     │
├─────────────────────────────────────────────────────────────┤
│ SEMAPHORE (limit concurrent access)                         │
│   new Semaphore(permits, fair)                              │
│   sem.acquire()        // Get permit (blocks)               │
│   sem.tryAcquire()     // Try get permit                    │
│   sem.release()        // Return permit                     │
├─────────────────────────────────────────────────────────────┤
│ EXCHANGER (two-party exchange)                              │
│   exchanger.exchange(data) // Swap data with other party    │
├─────────────────────────────────────────────────────────────┤
│ PHASER (dynamic, multi-phase)                               │
│   phaser.register()               // Join                   │
│   phaser.arriveAndAwaitAdvance()  // Sync point             │
│   phaser.arriveAndDeregister()    // Leave                  │
└─────────────────────────────────────────────────────────────┘
```
