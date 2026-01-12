/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  CORE JAVA - PART 7: MULTITHREADING & CONCURRENCY
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME 📖
 * ─────────────
 * Think of a RESTAURANT KITCHEN 👨‍🍳
 * 
 *   SINGLE THREAD = 1 chef doing everything (slow!)
 *   MULTI THREAD  = Multiple chefs working together (fast!)
 *   
 *   But multiple chefs need COORDINATION:
 *   - Don't use same knife at same time (synchronization)
 *   - Wait for ingredients to be ready (wait/notify)
 *   - Don't block the kitchen (deadlock)
 * 
 * 
 * THREAD LIFECYCLE:
 * ─────────────────
 *   NEW → RUNNABLE → RUNNING → BLOCKED/WAITING → TERMINATED
 *          ↑__________________|
 */

package corejava;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class G_Multithreading {

    public static void main(String[] args) throws Exception {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  JAVA MULTITHREADING & CONCURRENCY");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        creatingThreads();
        threadLifecycle();
        synchronizationDemo();
        volatileDemo();
        waitNotifyDemo();
        executorServiceDemo();
        callableAndFuture();
        locksDemo();
        atomicVariables();
        concurrentCollections();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * CREATING THREADS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void creatingThreads() throws InterruptedException {
        System.out.println("1️⃣ CREATING THREADS");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Method 1: Extend Thread class
        System.out.println("A) Extend Thread class:");
        Thread t1 = new MyThread();
        t1.start();  // NOT run()!
        t1.join();   // Wait for completion
        
        // Method 2: Implement Runnable interface (PREFERRED!)
        System.out.println("\nB) Implement Runnable (Preferred!):");
        Thread t2 = new Thread(new MyRunnable());
        t2.start();
        t2.join();
        
        // Method 3: Lambda (Java 8+)
        System.out.println("\nC) Lambda expression:");
        Thread t3 = new Thread(() -> {
            System.out.println("   Lambda thread: " + Thread.currentThread().getName());
        });
        t3.start();
        t3.join();
        
        // Why Runnable is better?
        System.out.println("\n📌 Why Runnable is better than Thread?");
        System.out.println("   1. Java allows single inheritance → can extend other class");
        System.out.println("   2. Separates task from thread management");
        System.out.println("   3. Can be used with ExecutorService");
        System.out.println("   4. More flexible and reusable\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * THREAD LIFECYCLE
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void threadLifecycle() throws InterruptedException {
        System.out.println("2️⃣ THREAD LIFECYCLE");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   ┌─────────┐");
        System.out.println("   │   NEW   │ ← Thread created, not started");
        System.out.println("   └────┬────┘");
        System.out.println("        │ start()");
        System.out.println("   ┌────▼────┐");
        System.out.println("   │RUNNABLE │ ← Ready to run, waiting for CPU");
        System.out.println("   └────┬────┘");
        System.out.println("        │ CPU assigned");
        System.out.println("   ┌────▼────┐");
        System.out.println("   │ RUNNING │ ← Actually executing");
        System.out.println("   └────┬────┘");
        System.out.println("        │");
        System.out.println("   ┌────┴────────────────────────────┐");
        System.out.println("   │                                 │");
        System.out.println("   ▼                                 ▼");
        System.out.println("┌──────────┐                  ┌────────────┐");
        System.out.println("│ BLOCKED/ │ sleep/wait/      │ TERMINATED │");
        System.out.println("│ WAITING  │ synchronized     │   (Dead)   │");
        System.out.println("└──────────┘                  └────────────┘");
        
        // Important methods
        System.out.println("\n📌 Important Thread Methods:");
        System.out.println("   start()  → Start the thread");
        System.out.println("   run()    → Contains the code to execute");
        System.out.println("   sleep()  → Pause for specified time");
        System.out.println("   join()   → Wait for thread to finish");
        System.out.println("   yield()  → Hint to give up CPU");
        System.out.println("   interrupt() → Interrupt sleeping/waiting thread\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * SYNCHRONIZATION
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a BATHROOM with ONE key 🔑
     * ─────────────────────────────────────
     *   Only person with key can enter
     *   Others must wait outside
     *   synchronized = the key!
     */
    static void synchronizationDemo() throws InterruptedException {
        System.out.println("3️⃣ SYNCHRONIZATION");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Problem without synchronization
        System.out.println("A) Problem WITHOUT synchronization:");
        Counter unsafeCounter = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) unsafeCounter.incrementUnsafe();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) unsafeCounter.incrementUnsafe();
        });
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("   Expected: 2000, Actual: " + unsafeCounter.getCount());
        System.out.println("   (May vary due to race condition!)\n");
        
        // Solution with synchronization
        System.out.println("B) Solution WITH synchronization:");
        Counter safeCounter = new Counter();
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) safeCounter.incrementSafe();
        });
        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) safeCounter.incrementSafe();
        });
        t3.start(); t4.start();
        t3.join(); t4.join();
        System.out.println("   Expected: 2000, Actual: " + safeCounter.getSafeCount());
        System.out.println("   ✅ Always correct!\n");
        
        // Synchronized block vs method
        System.out.println("C) synchronized method vs block:");
        System.out.println("   // Method level - locks entire method");
        System.out.println("   synchronized void method() { ... }");
        System.out.println();
        System.out.println("   // Block level - locks specific code");
        System.out.println("   void method() {");
        System.out.println("       synchronized(this) { ... }");
        System.out.println("   }");
        System.out.println("   Block is more granular and efficient!\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * VOLATILE KEYWORD
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a BULLETIN BOARD 📋
     * ────────────────────────────
     *   Without volatile: Each person has their own notebook (cache)
     *   With volatile: Everyone reads from shared bulletin board
     */
    static void volatileDemo() {
        System.out.println("4️⃣ VOLATILE KEYWORD");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   Without volatile:");
        System.out.println("   Thread 1: flag = true");
        System.out.println("   Thread 2: while(!flag) {}  // May never see change!\n");
        
        System.out.println("   With volatile:");
        System.out.println("   volatile boolean flag = false;");
        System.out.println("   Thread 1: flag = true  // Writes to main memory");
        System.out.println("   Thread 2: while(!flag) {}  // Reads from main memory ✅\n");
        
        System.out.println("📌 volatile guarantees:");
        System.out.println("   • Visibility - changes visible to all threads");
        System.out.println("   • Ordering - prevents reordering");
        System.out.println("   ❌ Does NOT guarantee atomicity!");
        System.out.println("   ❌ volatile int count; count++; // Still not thread-safe!\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * WAIT / NOTIFY
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a RESTAURANT 🍽️
     * ──────────────────────────
     *   Customer: wait() for food
     *   Chef: notify() when food ready
     */
    static void waitNotifyDemo() throws InterruptedException {
        System.out.println("5️⃣ WAIT / NOTIFY (Inter-thread Communication)");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   Producer-Consumer Pattern:");
        System.out.println("   ┌──────────────┐         ┌──────────────┐");
        System.out.println("   │   PRODUCER   │ ─────▶  │    QUEUE     │ ─────▶  │   CONSUMER   │");
        System.out.println("   │  (produces)  │ notify  │  (shared)    │  wait   │  (consumes)  │");
        System.out.println("   └──────────────┘         └──────────────┘         └──────────────┘");
        
        System.out.println("\n   synchronized(lock) {");
        System.out.println("       while (!condition) {");
        System.out.println("           lock.wait();    // Release lock, wait");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();
        System.out.println("   synchronized(lock) {");
        System.out.println("       // Change condition");
        System.out.println("       lock.notify();      // Wake up ONE waiting thread");
        System.out.println("       // or lock.notifyAll(); // Wake up ALL waiting threads");
        System.out.println("   }");
        
        System.out.println("\n📌 Important:");
        System.out.println("   • wait/notify must be called inside synchronized");
        System.out.println("   • wait() releases the lock, notify() doesn't");
        System.out.println("   • Always use while loop, not if, for wait()\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * EXECUTOR SERVICE (Thread Pool)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a TAXI COMPANY 🚕
     * ───────────────────────────
     *   Don't create new car for each ride
     *   Reuse existing cars from pool!
     */
    static void executorServiceDemo() throws Exception {
        System.out.println("6️⃣ EXECUTOR SERVICE (Thread Pool)");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("A) Types of Thread Pools:");
        System.out.println("   • newFixedThreadPool(n) - Fixed n threads");
        System.out.println("   • newCachedThreadPool() - Creates as needed, reuses");
        System.out.println("   • newSingleThreadExecutor() - Single thread");
        System.out.println("   • newScheduledThreadPool(n) - For scheduled tasks\n");
        
        // Demo
        System.out.println("B) Demo with FixedThreadPool:");
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("   Task " + taskId + " running on " + 
                    Thread.currentThread().getName());
            });
        }
        
        executor.shutdown();  // Don't accept new tasks
        executor.awaitTermination(5, TimeUnit.SECONDS);  // Wait for completion
        
        System.out.println("\n📌 Always shutdown ExecutorService!");
        System.out.println("   executor.shutdown()        - Graceful");
        System.out.println("   executor.shutdownNow()     - Forceful\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * CALLABLE AND FUTURE
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void callableAndFuture() throws Exception {
        System.out.println("7️⃣ CALLABLE & FUTURE (Return Value from Thread)");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   Runnable vs Callable:");
        System.out.println("   ┌──────────────────────┬──────────────────────┐");
        System.out.println("   │      Runnable        │       Callable       │");
        System.out.println("   ├──────────────────────┼──────────────────────┤");
        System.out.println("   │ void run()           │ V call() throws Ex   │");
        System.out.println("   │ No return value      │ Returns value        │");
        System.out.println("   │ Can't throw checked  │ Can throw exceptions │");
        System.out.println("   └──────────────────────┴──────────────────────┘\n");
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        // Submit Callable and get Future
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(100);
            return 42;  // Return value!
        });
        
        System.out.println("   Task submitted, doing other work...");
        
        // Get result (blocks until ready)
        Integer result = future.get();  // Waits for result
        System.out.println("   Result: " + result);
        
        // Future methods
        System.out.println("\n📌 Future Methods:");
        System.out.println("   get()           - Block and get result");
        System.out.println("   get(timeout)    - Block with timeout");
        System.out.println("   isDone()        - Check if complete");
        System.out.println("   cancel()        - Cancel the task");
        System.out.println("   isCancelled()   - Check if cancelled");
        
        executor.shutdown();
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * LOCKS (java.util.concurrent.locks)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void locksDemo() {
        System.out.println("8️⃣ LOCKS (More Flexible than synchronized)");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("A) ReentrantLock:");
        System.out.println("   Lock lock = new ReentrantLock();");
        System.out.println("   ");
        System.out.println("   lock.lock();");
        System.out.println("   try {");
        System.out.println("       // critical section");
        System.out.println("   } finally {");
        System.out.println("       lock.unlock();  // Always in finally!");
        System.out.println("   }");
        
        System.out.println("\nB) ReentrantLock vs synchronized:");
        System.out.println("   ┌──────────────────────┬──────────────────────┐");
        System.out.println("   │    synchronized      │    ReentrantLock     │");
        System.out.println("   ├──────────────────────┼──────────────────────┤");
        System.out.println("   │ Implicit lock/unlock │ Explicit lock/unlock │");
        System.out.println("   │ Can't try lock       │ tryLock() available  │");
        System.out.println("   │ Can't interrupt      │ lockInterruptibly()  │");
        System.out.println("   │ No fairness          │ Fair lock option     │");
        System.out.println("   │ Block-scoped         │ Method-scoped        │");
        System.out.println("   └──────────────────────┴──────────────────────┘");
        
        System.out.println("\nC) ReadWriteLock:");
        System.out.println("   • Multiple readers can read simultaneously");
        System.out.println("   • Only one writer at a time");
        System.out.println("   • Better performance for read-heavy workloads\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * ATOMIC VARIABLES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void atomicVariables() throws InterruptedException {
        System.out.println("9️⃣ ATOMIC VARIABLES (Lock-free Thread Safety)");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   Problem: count++ is NOT atomic!");
        System.out.println("   1. Read count");
        System.out.println("   2. Increment");
        System.out.println("   3. Write back");
        System.out.println("   Another thread can interfere between steps!\n");
        
        AtomicInteger atomicCount = new AtomicInteger(0);
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) atomicCount.incrementAndGet();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) atomicCount.incrementAndGet();
        });
        
        t1.start(); t2.start();
        t1.join(); t2.join();
        
        System.out.println("   AtomicInteger result: " + atomicCount.get() + " ✅ Always 2000!");
        
        System.out.println("\n📌 Atomic Classes:");
        System.out.println("   AtomicInteger, AtomicLong, AtomicBoolean");
        System.out.println("   AtomicReference<V>");
        System.out.println("   AtomicIntegerArray, AtomicLongArray");
        
        System.out.println("\n📌 Common Methods:");
        System.out.println("   get(), set(value)");
        System.out.println("   incrementAndGet(), getAndIncrement()");
        System.out.println("   compareAndSet(expected, new)  // CAS operation\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * CONCURRENT COLLECTIONS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void concurrentCollections() {
        System.out.println("🔟 CONCURRENT COLLECTIONS");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   Regular             → Concurrent Alternative");
        System.out.println("   ──────────────────────────────────────────────");
        System.out.println("   HashMap             → ConcurrentHashMap");
        System.out.println("   ArrayList           → CopyOnWriteArrayList");
        System.out.println("   HashSet             → CopyOnWriteArraySet");
        System.out.println("   TreeMap             → ConcurrentSkipListMap");
        System.out.println("   LinkedList (Queue)  → ConcurrentLinkedQueue");
        System.out.println("   PriorityQueue       → PriorityBlockingQueue");
        
        System.out.println("\n📌 ConcurrentHashMap:");
        System.out.println("   • Segment-level locking (not entire map)");
        System.out.println("   • Better performance than synchronizedMap");
        System.out.println("   • No null keys or values");
        
        System.out.println("\n📌 CopyOnWriteArrayList:");
        System.out.println("   • Creates copy on every write");
        System.out.println("   • Great for read-heavy, few writes");
        System.out.println("   • Iteration never throws ConcurrentModificationException");
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Multithreading");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  📌 Create: Runnable (preferred) or extend Thread");
        System.out.println("  📌 synchronized: Mutual exclusion, one thread at a time");
        System.out.println("  📌 volatile: Visibility across threads");
        System.out.println("  📌 ExecutorService: Thread pool, reuse threads");
        System.out.println("  📌 Callable/Future: Return value from thread");
        System.out.println("  📌 Atomic: Lock-free thread-safe operations");
        System.out.println();
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"synchronized ensures mutual exclusion, volatile ensures");
        System.out.println("      visibility. Use ExecutorService instead of creating threads");
        System.out.println("      manually. ConcurrentHashMap for thread-safe map.");
        System.out.println("      AtomicInteger for lock-free counter operations.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

// Supporting classes
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("   MyThread running: " + Thread.currentThread().getName());
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("   MyRunnable running: " + Thread.currentThread().getName());
    }
}

class Counter {
    private int count = 0;
    private int safeCount = 0;
    
    public void incrementUnsafe() {
        count++;  // Not thread-safe!
    }
    
    public synchronized void incrementSafe() {
        safeCount++;  // Thread-safe!
    }
    
    public int getCount() { return count; }
    public int getSafeCount() { return safeCount; }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q1: Thread vs Runnable?
 * A1: Runnable is preferred because:
 *     - Java allows single inheritance (can extend another class)
 *     - Separates task from thread
 *     - Works with ExecutorService
 * 
 * Q2: start() vs run()?
 * A2: start() creates new thread and calls run()
 *     run() just executes in current thread (no new thread!)
 * 
 * Q3: synchronized vs volatile?
 * A3: synchronized: Mutual exclusion + visibility (atomic operations)
 *     volatile: Only visibility (no atomicity)
 * 
 * Q4: What is deadlock?
 * A4: Two threads waiting for each other's locks forever.
 *     Thread A has Lock1, needs Lock2
 *     Thread B has Lock2, needs Lock1
 *     Both wait forever!
 * 
 * Q5: How to prevent deadlock?
 * A5: 1. Lock ordering - always acquire locks in same order
 *     2. Timeout - tryLock with timeout
 *     3. Avoid nested locks
 * 
 * Q6: wait() vs sleep()?
 * A6: wait(): Releases lock, must be in synchronized, needs notify
 *     sleep(): Keeps lock, can be anywhere, wakes after time
 */
