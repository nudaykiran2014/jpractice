package spring_learning.lesson07_learning_roadmap;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  VIRTUAL THREADS (Project Loom) - Java 21+                                   ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Virtual Threads are lightweight threads that dramatically improve
 * the scalability of Java applications.
 * 
 * Available in: Java 21+ (LTS)
 * Spring Boot support: 3.2+
 */
public class _05_VirtualThreads {
    public static void main(String[] args) {
        System.out.println("=== VIRTUAL THREADS EXPLAINED ===");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * THE PROBLEM: Traditional Threads Are Expensive
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * TRADITIONAL (PLATFORM) THREADS:
 * ---------------------------------
 * - Each thread = 1 OS thread
 * - Each thread uses ~1MB of memory (stack)
 * - Creating/destroying threads is expensive
 * - Typical server can handle ~200-500 threads
 * 
 * PROBLEM SCENARIO:
 * ------------------
 *     // Traditional web server
 *     // 200 threads in pool
 *     // Each request = 1 thread
 *     // Request makes DB call (waits 100ms)
 *     
 *     Thread is BLOCKED while waiting!
 *     ↓
 *     Only 200 concurrent requests possible
 *     ↓
 *     Thread 201 must WAIT for a free thread
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * THE SOLUTION: Virtual Threads
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * VIRTUAL THREADS:
 * -----------------
 * - Lightweight threads managed by JVM (not OS)
 * - Each virtual thread uses ~1KB (vs ~1MB)
 * - Can create MILLIONS of virtual threads
 * - When blocked, the underlying OS thread is released!
 * 
 * HOW IT WORKS:
 * --------------
 *     Virtual Thread 1 ─┐
 *     Virtual Thread 2 ─┼──→ OS Thread 1
 *     Virtual Thread 3 ─┘
 *     
 *     Virtual Thread 4 ─┐
 *     Virtual Thread 5 ─┼──→ OS Thread 2
 *     Virtual Thread 6 ─┘
 *     
 *     When VT1 blocks (I/O), JVM moves VT2 to OS Thread 1
 *     VT1 resumes when I/O completes
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * COMPARISON
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * | Feature              | Platform Thread  | Virtual Thread     |
 * |----------------------|------------------|---------------------|
 * | Memory per thread    | ~1MB             | ~1KB                |
 * | Max threads          | ~500-2000        | Millions            |
 * | Creation cost        | High             | Very Low            |
 * | Blocking behavior    | Thread blocked   | Releases OS thread  |
 * | Managed by           | OS               | JVM                 |
 * | Best for             | CPU-bound tasks  | I/O-bound tasks     |
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CREATING VIRTUAL THREADS (Java 21+)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE 1: Simple Virtual Thread
 * ----------------------------------
 *     // Start a virtual thread
 *     Thread.startVirtualThread(() -> {
 *         System.out.println("Running in virtual thread!");
 *     });
 *     
 *     // Or using builder
 *     Thread vThread = Thread.ofVirtual()
 *         .name("my-virtual-thread")
 *         .start(() -> {
 *             System.out.println("Hello from virtual thread!");
 *         });
 * 
 * EXAMPLE 2: Virtual Thread Executor
 * ------------------------------------
 *     // Create executor with virtual threads
 *     try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
 *         
 *         // Submit 10,000 tasks - each gets its own virtual thread!
 *         for (int i = 0; i < 10_000; i++) {
 *             executor.submit(() -> {
 *                 // Simulate I/O (database call, HTTP request, etc.)
 *                 Thread.sleep(Duration.ofSeconds(1));
 *                 return "Done";
 *             });
 *         }
 *     }
 *     // With platform threads, this would need 10,000 threads!
 *     // With virtual threads, it uses only a few OS threads
 * 
 * EXAMPLE 3: Check if Virtual Thread
 * ------------------------------------
 *     Thread current = Thread.currentThread();
 *     
 *     if (current.isVirtual()) {
 *         System.out.println("Running on virtual thread");
 *     } else {
 *         System.out.println("Running on platform thread");
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * SPRING BOOT + VIRTUAL THREADS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * ENABLE IN SPRING BOOT 3.2+:
 * ----------------------------
 *     # application.properties
 *     spring.threads.virtual.enabled=true
 *     
 *     # That's it! All request handling uses virtual threads now.
 * 
 * WHAT CHANGES:
 * --------------
 *     BEFORE (Platform Threads):
 *     - Tomcat thread pool: 200 threads
 *     - Max concurrent requests: ~200
 *     - Blocking I/O blocks the thread
 *     
 *     AFTER (Virtual Threads):
 *     - Each request gets a virtual thread
 *     - Max concurrent requests: Millions (limited by memory)
 *     - Blocking I/O releases OS thread
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLE: API That Calls External Service
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * SCENARIO:
 * ----------
 *     // Your API calls an external service that takes 500ms
 *     
 *     @GetMapping("/data")
 *     public Data getData() {
 *         return externalService.fetchData();  // Takes 500ms
 *     }
 * 
 * WITH PLATFORM THREADS (200 thread pool):
 * -----------------------------------------
 *     Request 1 → Thread 1 (blocked 500ms)
 *     Request 2 → Thread 2 (blocked 500ms)
 *     ...
 *     Request 200 → Thread 200 (blocked 500ms)
 *     Request 201 → WAITING! (no free threads)
 *     
 *     Max throughput: 400 requests/second
 *     (200 threads × 2 requests per second each)
 * 
 * WITH VIRTUAL THREADS:
 * ----------------------
 *     Request 1 → VThread 1 (blocks, releases OS thread)
 *     Request 2 → VThread 2 (blocks, releases OS thread)
 *     ...
 *     Request 10000 → VThread 10000 (blocks, releases OS thread)
 *     
 *     All 10,000 requests handled by ~8 OS threads!
 *     Max throughput: Limited only by external service
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHEN TO USE VIRTUAL THREADS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * ✅ GOOD USE CASES (I/O-bound):
 * -------------------------------
 * - REST APIs that call databases
 * - APIs that call external services
 * - File I/O operations
 * - Any application that waits a lot
 * 
 * ❌ NOT HELPFUL FOR (CPU-bound):
 * --------------------------------
 * - Heavy calculations
 * - Image/video processing
 * - Data compression
 * - Machine learning inference
 * 
 * WHY?
 * -----
 * Virtual threads shine when threads are WAITING.
 * If your code is always computing, there's nothing to optimize.
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VIRTUAL THREADS vs REACTIVE (WebFlux)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * REACTIVE (WebFlux):
 * --------------------
 *     Mono.fromCallable(() -> fetchData())
 *         .flatMap(data -> processData(data))
 *         .flatMap(result -> saveResult(result))
 *         .subscribe();
 *     
 *     Pros: Very efficient
 *     Cons: Hard to read, different programming model
 * 
 * VIRTUAL THREADS:
 * -----------------
 *     Data data = fetchData();      // Blocking, but doesn't block OS thread
 *     Result result = process(data);
 *     saveResult(result);
 *     
 *     Pros: Simple, familiar code
 *     Cons: Slightly less efficient than reactive
 * 
 * RECOMMENDATION:
 * ----------------
 * - New projects: Use Virtual Threads (simpler)
 * - Existing WebFlux: Keep using WebFlux
 * - Extremely high scale: Consider WebFlux
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * GOTCHAS / THINGS TO KNOW
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. SYNCHRONIZED BLOCKS CAN PIN THREADS
 *    -----------------------------------
 *    // BAD - pins virtual thread to OS thread
 *    synchronized (lock) {
 *        Thread.sleep(1000);  // OS thread is blocked!
 *    }
 *    
 *    // GOOD - use ReentrantLock instead
 *    lock.lock();
 *    try {
 *        Thread.sleep(1000);  // OS thread is released
 *    } finally {
 *        lock.unlock();
 *    }
 * 
 * 2. THREAD LOCALS - USE WITH CAUTION
 *    ----------------------------------
 *    // Virtual threads make ThreadLocal less useful
 *    // Consider ScopedValue instead (Java 21+)
 * 
 * 3. DON'T POOL VIRTUAL THREADS
 *    ----------------------------
 *    // BAD - defeats the purpose
 *    ExecutorService pool = Executors.newFixedThreadPool(100);
 *    pool.submit(() -> virtualThreadTask());
 *    
 *    // GOOD - create new virtual thread per task
 *    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * SUMMARY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Virtual Threads = Write simple blocking code, get async performance
 * 
 * BEFORE:
 *     - Use thread pools (limited)
 *     - Or learn reactive programming (complex)
 * 
 * AFTER (Java 21+):
 *     - Write normal blocking code
 *     - Enable virtual threads
 *     - Get massive scalability for free!
 * 
 * ENABLE IN SPRING BOOT:
 *     spring.threads.virtual.enabled=true
 */
