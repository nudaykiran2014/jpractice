# Java Thread Basics - Complete Guide

## Table of Contents
1. [What is a Thread?](#1-what-is-a-thread)
2. [Creating Threads](#2-creating-threads)
3. [Thread Class](#3-thread-class)
4. [Runnable Interface](#4-runnable-interface)
5. [Callable Interface](#5-callable-interface)
6. [Thread States (Lifecycle)](#6-thread-states-lifecycle)
7. [Thread Priority](#7-thread-priority)
8. [Daemon Threads](#8-daemon-threads)
9. [Thread Methods](#9-thread-methods)
10. [Thread Groups](#10-thread-groups)
11. [Common Problems](#11-common-problems)
12. [Interview Questions](#12-interview-questions)

---

# 1. What is a Thread?

## 🧒 Kid-Friendly Explanation

**Thread = A worker doing a task**

Think of a restaurant kitchen:
- **Process** = The entire kitchen
- **Thread** = One chef
- **Multithreading** = Multiple chefs cooking simultaneously

```
┌─────────────────── PROCESS (Kitchen) ───────────────────┐
│                                                          │
│   Thread 1        Thread 2        Thread 3              │
│   (Chef 1)        (Chef 2)        (Chef 3)              │
│      │               │               │                   │
│   Cooking         Chopping        Plating               │
│   pasta           vegetables      dessert               │
│                                                          │
│   ─────────── Shared Memory (Ingredients) ───────────   │
└──────────────────────────────────────────────────────────┘
```

## Process vs Thread

| Process | Thread |
|---------|--------|
| Independent program | Lightweight sub-process |
| Own memory space | Shares memory with other threads |
| Heavy to create | Light to create |
| Isolated | Can communicate easily |
| IPC needed for communication | Shared variables |

---

# 2. Creating Threads

## Three Ways to Create Threads

```
1. Extend Thread class
2. Implement Runnable interface (preferred!)
3. Implement Callable interface (returns result)
```

## Method 1: Extending Thread Class

```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running: " + getName());
    }
}

// Usage
MyThread t = new MyThread();
t.start();  // NOT run()!
```

## Method 2: Implementing Runnable (Preferred!)

```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable running");
    }
}

// Usage
Thread t = new Thread(new MyRunnable());
t.start();

// Lambda (Java 8+)
Thread t2 = new Thread(() -> {
    System.out.println("Lambda runnable");
});
t2.start();
```

## Method 3: Implementing Callable (Returns Result)

```java
import java.util.concurrent.*;

class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 42;
    }
}

// Usage with ExecutorService
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<Integer> future = executor.submit(new MyCallable());
Integer result = future.get();  // 42
executor.shutdown();
```

## Why Runnable is Better Than Thread?

| Extending Thread | Implementing Runnable |
|------------------|----------------------|
| Can't extend another class | Can extend another class |
| Tightly coupled | Loosely coupled |
| Each object = new thread | Can reuse with thread pools |
| Less flexible | More flexible |

---

# 3. Thread Class

## Important Constructors

```java
Thread()
Thread(Runnable target)
Thread(Runnable target, String name)
Thread(String name)
Thread(ThreadGroup group, Runnable target)
Thread(ThreadGroup group, Runnable target, String name)
Thread(ThreadGroup group, Runnable target, String name, long stackSize)
```

## Creating Threads with Names

```java
// Method 1: Constructor
Thread t1 = new Thread(() -> { }, "Worker-1");

// Method 2: setName()
Thread t2 = new Thread(() -> { });
t2.setName("Worker-2");

// Get name
String name = t2.getName();
```

## Thread ID

```java
Thread t = new Thread();
long id = t.getId();  // Unique ID assigned by JVM

// Current thread ID
long currentId = Thread.currentThread().getId();
```

---

# 4. Runnable Interface

## Definition

```java
@FunctionalInterface
public interface Runnable {
    void run();
}
```

## Various Ways to Use

```java
// Anonymous class
Thread t1 = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Anonymous class");
    }
});

// Lambda expression (recommended!)
Thread t2 = new Thread(() -> System.out.println("Lambda"));

// Method reference
Thread t3 = new Thread(System.out::println);

// Separate class
Thread t4 = new Thread(new MyRunnable());
```

## Runnable with State

```java
class Counter implements Runnable {
    private int count = 0;
    
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            count++;
        }
    }
    
    public int getCount() {
        return count;
    }
}

Counter counter = new Counter();
Thread t1 = new Thread(counter);
Thread t2 = new Thread(counter);  // Same runnable!
t1.start();
t2.start();
// Warning: This has race condition!
```

---

# 5. Callable Interface

## Definition

```java
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

## Runnable vs Callable

| Runnable | Callable |
|----------|----------|
| `void run()` | `V call()` |
| No return value | Returns a value |
| Cannot throw checked exception | Can throw exception |
| Use with Thread | Use with ExecutorService |

## Usage Example

```java
import java.util.concurrent.*;

Callable<String> task = () -> {
    Thread.sleep(1000);
    return "Task completed!";
};

ExecutorService executor = Executors.newSingleThreadExecutor();
Future<String> future = executor.submit(task);

// Do other work...

String result = future.get();  // Blocks until done
System.out.println(result);    // "Task completed!"

executor.shutdown();
```

## Callable with Exception

```java
Callable<Integer> riskyTask = () -> {
    if (Math.random() > 0.5) {
        throw new Exception("Random failure!");
    }
    return 42;
};

try {
    Future<Integer> future = executor.submit(riskyTask);
    Integer result = future.get();
} catch (ExecutionException e) {
    System.out.println("Task failed: " + e.getCause().getMessage());
}
```

---

# 6. Thread States (Lifecycle)

## Thread States Diagram

```
        ┌─────────────────────────────────────────────────────────┐
        │                                                          │
        │    ┌───────┐                                             │
        │    │  NEW  │ ─────── start() ───────┐                   │
        │    └───────┘                         ▼                   │
        │                               ┌───────────┐              │
        │         ┌────────────────────│ RUNNABLE  │◄────────┐    │
        │         │                     └───────────┘         │    │
        │         │                        │    │             │    │
        │         │                        │    │             │    │
        │    sleep()              synchronized  I/O      notify()  │
        │    wait()                  lock      done     notifyAll()│
        │    join()                    │        │      interrupt() │
        │         │                    │        │             │    │
        │         ▼                    ▼        ▼             │    │
        │   ┌──────────┐         ┌─────────┐                  │    │
        │   │ TIMED_   │         │ BLOCKED │──────────────────┘    │
        │   │ WAITING  │         └─────────┘  (got lock)           │
        │   └──────────┘               │                           │
        │         │                    │                           │
        │         │              ┌─────────┐                       │
        │         └─────────────▶│ WAITING │───────────────────────┘
        │                        └─────────┘                       │
        │                                                          │
        │                        ┌────────────┐                    │
        │    run() completes ───▶│ TERMINATED │                    │
        │                        └────────────┘                    │
        └─────────────────────────────────────────────────────────┘
```

## Six Thread States

```java
public enum Thread.State {
    NEW,           // Created but not started
    RUNNABLE,      // Running or ready to run
    BLOCKED,       // Waiting for monitor lock
    WAITING,       // Waiting indefinitely
    TIMED_WAITING, // Waiting with timeout
    TERMINATED     // Completed execution
}
```

## State Transitions

| State | Entered When | Exits When |
|-------|--------------|------------|
| NEW | `new Thread()` | `start()` called |
| RUNNABLE | `start()` called | Blocked/Waiting/Terminated |
| BLOCKED | Waiting for synchronized lock | Lock acquired |
| WAITING | `wait()`, `join()`, `park()` | `notify()`, thread ends |
| TIMED_WAITING | `sleep(ms)`, `wait(ms)`, `join(ms)` | Timeout or notify |
| TERMINATED | `run()` completes or exception | Never (final state) |

## Checking Thread State

```java
Thread t = new Thread(() -> {
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) { }
});

System.out.println(t.getState());  // NEW

t.start();
System.out.println(t.getState());  // RUNNABLE

Thread.sleep(100);
System.out.println(t.getState());  // TIMED_WAITING

t.join();
System.out.println(t.getState());  // TERMINATED
```

---

# 7. Thread Priority

## Priority Range

```java
Thread.MIN_PRIORITY   // 1
Thread.NORM_PRIORITY  // 5 (default)
Thread.MAX_PRIORITY   // 10
```

## Setting Priority

```java
Thread t = new Thread(() -> { });

t.setPriority(Thread.MAX_PRIORITY);  // 10
t.setPriority(7);                    // Custom
int priority = t.getPriority();      // Get current

// Priority inherited from parent thread
Thread main = Thread.currentThread();
main.setPriority(8);
Thread child = new Thread(() -> { });
// child priority is also 8
```

## Priority is Just a Hint!

```java
// High priority doesn't guarantee execution order!
Thread high = new Thread(() -> {
    System.out.println("High priority");
});
Thread low = new Thread(() -> {
    System.out.println("Low priority");
});

high.setPriority(Thread.MAX_PRIORITY);
low.setPriority(Thread.MIN_PRIORITY);

low.start();
high.start();

// Output order is NOT guaranteed!
// OS scheduler makes final decision
```

---

# 8. Daemon Threads

## What are Daemon Threads?

**Daemon Thread** = Background service thread

- JVM exits when all **non-daemon** threads finish
- Daemon threads are terminated abruptly when JVM exits
- Used for: garbage collection, housekeeping, monitoring

```java
Thread daemon = new Thread(() -> {
    while (true) {
        System.out.println("Background task...");
        Thread.sleep(1000);
    }
});
daemon.setDaemon(true);  // Must set BEFORE start()!
daemon.start();

// Main thread exits immediately
// Daemon thread is killed when main exits
```

## User Thread vs Daemon Thread

| User Thread | Daemon Thread |
|-------------|---------------|
| JVM waits for completion | JVM doesn't wait |
| Default type | Must explicitly set |
| For main application logic | For background services |
| Clean shutdown | May be killed abruptly |

## Daemon Thread Example

```java
public class DaemonExample {
    public static void main(String[] args) {
        Thread daemon = new Thread(() -> {
            while (true) {
                System.out.println("Daemon working...");
                try { Thread.sleep(500); } catch (InterruptedException e) { break; }
            }
        });
        daemon.setDaemon(true);
        daemon.start();
        
        // Main thread does some work
        try { Thread.sleep(2000); } catch (InterruptedException e) { }
        
        System.out.println("Main thread ending");
        // Daemon thread killed automatically!
    }
}
```

## Important Rules

```java
// Must set daemon BEFORE start()
Thread t = new Thread(() -> { });
t.setDaemon(true);  // ✅ OK
t.start();

Thread t2 = new Thread(() -> { });
t2.start();
t2.setDaemon(true);  // ❌ IllegalThreadStateException!

// Check if daemon
boolean isDaemon = t.isDaemon();

// Child inherits daemon status
Thread parent = new Thread(() -> {
    Thread child = new Thread(() -> { });
    // child.isDaemon() == parent.isDaemon()
});
```

---

# 9. Thread Methods

## sleep() - Pause Current Thread

```java
try {
    Thread.sleep(1000);          // Sleep 1 second
    Thread.sleep(1000, 500000);  // 1 second + 500000 nanoseconds
} catch (InterruptedException e) {
    // Thread was interrupted during sleep
    Thread.currentThread().interrupt();  // Restore flag
}

// TimeUnit alternative (more readable)
TimeUnit.SECONDS.sleep(1);
TimeUnit.MILLISECONDS.sleep(500);
TimeUnit.MINUTES.sleep(1);
```

## yield() - Give Up CPU Time

```java
Thread.yield();  // Hint to scheduler to let other threads run

// Just a hint - may be ignored!
// Rarely used in practice
```

## join() - Wait for Thread to Complete

```java
Thread t = new Thread(() -> {
    // Long running task
    for (int i = 0; i < 5; i++) {
        System.out.println("Working... " + i);
        Thread.sleep(500);
    }
});

t.start();
System.out.println("Waiting for thread to finish...");

t.join();  // Block until t completes

System.out.println("Thread finished!");

// With timeout
t.join(2000);  // Wait max 2 seconds
t.join(2000, 500000);  // 2 seconds + 500000 nanos
```

## interrupt() - Request Thread Interruption

```java
Thread t = new Thread(() -> {
    try {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Working...");
            Thread.sleep(1000);
        }
    } catch (InterruptedException e) {
        System.out.println("Interrupted during sleep!");
    }
    System.out.println("Thread ending gracefully");
});

t.start();
Thread.sleep(3000);
t.interrupt();  // Request interruption
```

## Interrupt Methods

```java
// Request interruption
thread.interrupt();

// Check if interrupted (doesn't clear flag)
boolean interrupted = thread.isInterrupted();

// Check AND clear flag (static method - checks current thread)
boolean wasInterrupted = Thread.interrupted();  // Clears flag!

// Example of proper interrupt handling
while (!Thread.currentThread().isInterrupted()) {
    try {
        doWork();
        Thread.sleep(100);
    } catch (InterruptedException e) {
        // Sleep was interrupted - decide what to do
        Thread.currentThread().interrupt();  // Restore flag
        break;  // Exit loop
    }
}
```

## currentThread() - Get Current Thread

```java
Thread current = Thread.currentThread();

System.out.println("Name: " + current.getName());
System.out.println("ID: " + current.getId());
System.out.println("Priority: " + current.getPriority());
System.out.println("State: " + current.getState());
System.out.println("Daemon: " + current.isDaemon());
```

## isAlive() - Check if Thread is Running

```java
Thread t = new Thread(() -> {
    Thread.sleep(2000);
});

System.out.println(t.isAlive());  // false (not started)

t.start();
System.out.println(t.isAlive());  // true (running)

t.join();
System.out.println(t.isAlive());  // false (terminated)
```

## start() vs run()

```java
Thread t = new Thread(() -> {
    System.out.println("Thread: " + Thread.currentThread().getName());
});

// WRONG - runs in current thread!
t.run();  // Output: main (runs synchronously!)

// CORRECT - creates new thread!
t.start();  // Output: Thread-0 (runs in new thread!)

// Can only start once!
t.start();  // ❌ IllegalThreadStateException!
```

---

# 10. Thread Groups

## Creating Thread Groups

```java
// Create thread group
ThreadGroup group = new ThreadGroup("MyGroup");

// Create thread in group
Thread t1 = new Thread(group, () -> { }, "Thread-1");
Thread t2 = new Thread(group, () -> { }, "Thread-2");

// Subgroups
ThreadGroup subGroup = new ThreadGroup(group, "SubGroup");
```

## Thread Group Methods

```java
ThreadGroup group = new ThreadGroup("Workers");

// Count active threads
int activeCount = group.activeCount();

// Count active subgroups
int activeGroupCount = group.activeGroupCount();

// Get parent group
ThreadGroup parent = group.getParent();

// Get name
String name = group.getName();

// Interrupt all threads in group
group.interrupt();

// List all threads
Thread[] threads = new Thread[group.activeCount()];
group.enumerate(threads);
```

## Default Thread Group

```java
// main thread belongs to "main" group
Thread main = Thread.currentThread();
ThreadGroup mainGroup = main.getThreadGroup();
System.out.println(mainGroup.getName());  // "main"

// Parent of main group is "system"
System.out.println(mainGroup.getParent().getName());  // "system"
```

---

# 11. Common Problems

## Race Condition

```java
// PROBLEM: Multiple threads modifying shared variable
class Counter {
    private int count = 0;
    
    public void increment() {
        count++;  // NOT atomic! Read → Modify → Write
    }
}

Counter counter = new Counter();
// Two threads incrementing simultaneously = lost updates!
```

## Deadlock

```java
// PROBLEM: Two threads waiting for each other
Object lock1 = new Object();
Object lock2 = new Object();

Thread t1 = new Thread(() -> {
    synchronized (lock1) {
        Thread.sleep(100);
        synchronized (lock2) { }  // Waits for lock2
    }
});

Thread t2 = new Thread(() -> {
    synchronized (lock2) {
        Thread.sleep(100);
        synchronized (lock1) { }  // Waits for lock1
    }
});
// DEADLOCK! Both threads wait forever
```

## Memory Visibility

```java
// PROBLEM: Changes not visible across threads
class Flag {
    private boolean running = true;  // Not volatile!
    
    public void stop() {
        running = false;
    }
    
    public void run() {
        while (running) {  // May never see the change!
            // do work
        }
    }
}
```

---

# 12. Interview Questions

## Q1: What is the difference between start() and run()?

**Answer:**
- `start()`: Creates new thread and calls `run()` in that thread
- `run()`: Just a normal method call in current thread

```java
t.run();   // Runs in current thread (synchronous)
t.start(); // Creates new thread (asynchronous)
```

---

## Q2: Can we start a thread twice?

**Answer:**
**No!** Calling `start()` twice throws `IllegalThreadStateException`.
A thread can only be started once. Once terminated, it cannot be restarted.

---

## Q3: What is the difference between Runnable and Callable?

**Answer:**
| Runnable | Callable |
|----------|----------|
| `void run()` | `V call()` |
| No return value | Returns value |
| No checked exception | Can throw exception |
| Use with Thread | Use with ExecutorService |

---

## Q4: What is a daemon thread?

**Answer:**
Background service thread that doesn't prevent JVM from exiting.
- JVM exits when all non-daemon threads finish
- Must set daemon before `start()`
- Examples: Garbage collector, finalizer thread

---

## Q5: What are thread states in Java?

**Answer:**
1. **NEW**: Created, not started
2. **RUNNABLE**: Running or ready to run
3. **BLOCKED**: Waiting for synchronized lock
4. **WAITING**: Waiting indefinitely (wait, join)
5. **TIMED_WAITING**: Waiting with timeout (sleep, wait(ms))
6. **TERMINATED**: Completed

---

## Q6: How to handle InterruptedException?

**Answer:**
```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // Option 1: Restore flag and return
    Thread.currentThread().interrupt();
    return;
    
    // Option 2: Throw runtime exception
    throw new RuntimeException(e);
}
```

Never ignore it silently!

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│                 THREAD BASICS CHEAT SHEET                   │
├─────────────────────────────────────────────────────────────┤
│ CREATE THREAD                                               │
│   new Thread(() -> { }).start();                            │
│   new Thread(runnable).start();                             │
│   executor.submit(callable);                                │
├─────────────────────────────────────────────────────────────┤
│ THREAD METHODS                                              │
│   Thread.sleep(ms)          - Pause current thread          │
│   Thread.yield()            - Give up CPU time              │
│   thread.join()             - Wait for thread               │
│   thread.interrupt()        - Request interruption          │
│   Thread.currentThread()    - Get current thread            │
│   thread.isAlive()          - Check if running              │
├─────────────────────────────────────────────────────────────┤
│ THREAD STATES                                               │
│   NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING            │
│   → RUNNABLE → TERMINATED                                   │
├─────────────────────────────────────────────────────────────┤
│ DAEMON                                                      │
│   thread.setDaemon(true);   // Before start()!              │
│   thread.isDaemon();                                        │
├─────────────────────────────────────────────────────────────┤
│ PRIORITY                                                    │
│   Thread.MIN_PRIORITY (1)                                   │
│   Thread.NORM_PRIORITY (5) - default                        │
│   Thread.MAX_PRIORITY (10)                                  │
└─────────────────────────────────────────────────────────────┘
```
