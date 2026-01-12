/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  JAVA ASYNC PROGRAMMING - Explained for Node.js Developers 🚀
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * THE BIG PICTURE: Node.js vs Java
 * ═════════════════════════════════
 * 
 *     NODE.JS:                           JAVA:
 *     ────────                           ─────
 *     Single-threaded                    Multi-threaded
 *     Event Loop handles async           Real threads run in parallel
 *     
 *     ┌─────────────┐                    ┌─────────────┐
 *     │   Thread    │                    │  Thread 1   │ → CPU Core 1
 *     │  (only 1!)  │                    ├─────────────┤
 *     │             │                    │  Thread 2   │ → CPU Core 2
 *     │ Event Loop  │                    ├─────────────┤
 *     │    ↻        │                    │  Thread 3   │ → CPU Core 3
 *     └─────────────┘                    └─────────────┘
 *     
 *     Node: "I'll do other things        Java: "I have MULTIPLE workers
 *            while waiting for I/O"             doing things AT THE SAME TIME"
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * LEVEL 1: BASIC THREADS (The Foundation) 🧵
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Think of a Thread like a WORKER in a factory.
 * 
 *     Node.js: 1 worker doing everything (but very efficiently!)
 *     Java:    Multiple workers, each doing their own task
 *     
 * Node.js equivalent: There's NO direct equivalent! Node doesn't have threads.
 * 
 * PROBLEM WITH RAW THREADS:
 *     - Creating threads is EXPENSIVE (like hiring a new employee for each task)
 *     - Managing them manually is error-prone
 *     - No easy way to get results back
 */

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

public class JavaAsyncExplained {
    
    // ═══════════════════════════════════════════════════════════════════════════
    // LEVEL 1: RAW THREADS (Don't use in production!)
    // ═══════════════════════════════════════════════════════════════════════════
    
    static void level1_RawThreads() throws InterruptedException {
        System.out.println("\n═══ LEVEL 1: RAW THREADS ═══\n");
        
        // Creating a thread - like hiring a worker
        Thread worker = new Thread(() -> {
            System.out.println("  Worker: I'm working on Thread: " + 
                Thread.currentThread().getName());
            try { Thread.sleep(1000); } catch (Exception e) {}
            System.out.println("  Worker: Done!");
        });
        
        worker.start();  // Start the worker
        worker.join();   // Wait for worker to finish (like await in Node)
        
        System.out.println("  Main: Worker finished!");
        
        /*
         * NODE.JS COMPARISON:
         * ───────────────────
         * // Node doesn't have threads, but similar concept:
         * 
         * const worker = new Promise((resolve) => {
         *     console.log("Working...");
         *     setTimeout(() => {
         *         console.log("Done!");
         *         resolve();
         *     }, 1000);
         * });
         * await worker;
         */
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // LEVEL 2: EXECUTOR FRAMEWORK (Thread Pool) 🏊‍♂️
    // ═══════════════════════════════════════════════════════════════════════════
    /*
     * PROBLEM: Creating new Thread for each task is expensive!
     * SOLUTION: Create a POOL of threads, reuse them.
     * 
     * Think of it like:
     *     RAW THREADS:  Hire a new employee for each task, fire them after
     *     THREAD POOL:  Keep 10 employees, assign tasks to whoever is free
     *     
     *     ┌─────────────────────────────────────────────┐
     *     │           THREAD POOL (10 workers)         │
     *     │  ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐           │
     *     │  │ W1│ │ W2│ │ W3│ │ W4│ │ W5│ ...       │
     *     │  └───┘ └───┘ └───┘ └───┘ └───┘           │
     *     │         ↑                                 │
     *     │    Task Queue: [Task1, Task2, Task3...]   │
     *     └─────────────────────────────────────────────┘
     *     
     * NODE.JS COMPARISON:
     *     Node's libuv has a thread pool (default 4 threads) for file I/O
     *     But you don't manage it directly
     */
    
    static void level2_ExecutorFramework() throws Exception {
        System.out.println("\n═══ LEVEL 2: EXECUTOR FRAMEWORK ═══\n");
        
        // Create a pool of 3 workers
        ExecutorService pool = Executors.newFixedThreadPool(3);
        
        System.out.println("  Submitting 5 tasks to 3 workers...\n");
        
        // Submit tasks (workers will pick them up)
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("  Task " + taskId + " running on: " + 
                    Thread.currentThread().getName());
                try { Thread.sleep(500); } catch (Exception e) {}
                System.out.println("  Task " + taskId + " done!");
            });
        }
        
        pool.shutdown();  // No new tasks accepted
        pool.awaitTermination(10, TimeUnit.SECONDS);  // Wait for all to finish
        
        System.out.println("\n  All tasks completed!");
        
        /*
         * TYPES OF EXECUTORS:
         * ───────────────────
         * newFixedThreadPool(n)    - Exactly n threads (good for CPU tasks)
         * newCachedThreadPool()    - Creates threads as needed, reuses old ones
         * newSingleThreadExecutor()- Only 1 thread (tasks run sequentially)
         * newScheduledThreadPool() - For scheduled/periodic tasks
         */
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // LEVEL 3: FUTURE (Getting results back) 📦
    // ═══════════════════════════════════════════════════════════════════════════
    /*
     * PROBLEM: How to get a RESULT from a thread?
     * SOLUTION: Future - a "promise" that will have a value later
     * 
     *     ┌─────────────────────────────────────────────┐
     *     │  Future<String> = "I'll give you a String  │
     *     │                    when I'm done"           │
     *     └─────────────────────────────────────────────┘
     *     
     * NODE.JS COMPARISON:
     *     Future = Promise (almost identical concept!)
     *     future.get() = await promise
     */
    
    static void level3_Future() throws Exception {
        System.out.println("\n═══ LEVEL 3: FUTURE ═══\n");
        
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        // Submit a task that RETURNS something
        Future<String> future = pool.submit(() -> {
            System.out.println("  Fetching data from database...");
            Thread.sleep(1000);  // Simulate slow operation
            return "User: John Doe";  // Return result
        });
        
        System.out.println("  Main thread continues doing other work...");
        System.out.println("  Is result ready? " + future.isDone());
        
        // Get the result (BLOCKS until ready - like await)
        String result = future.get();  // ← This is like "await" in Node!
        System.out.println("  Got result: " + result);
        
        pool.shutdown();
        
        /*
         * NODE.JS COMPARISON:
         * ───────────────────
         * const promise = fetchFromDB();  // Returns Promise
         * console.log("Doing other work...");
         * const result = await promise;   // Wait for result
         * console.log(result);
         * 
         * PROBLEM WITH FUTURE:
         *     future.get() BLOCKS the thread! 😫
         *     Can't chain operations nicely
         *     That's why we have CompletableFuture...
         */
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // LEVEL 4: COMPLETABLE FUTURE (Modern async) ⭐
    // ═══════════════════════════════════════════════════════════════════════════
    /*
     * CompletableFuture = Promise in Node.js! 🎉
     * 
     * Finally, Java gets something that works like Node's Promises!
     * 
     *     NODE.JS                          JAVA
     *     ────────                         ─────
     *     Promise.resolve(value)           CompletableFuture.completedFuture(value)
     *     new Promise((resolve) => ...)    CompletableFuture.supplyAsync(() -> ...)
     *     promise.then(x => ...)           future.thenApply(x -> ...)
     *     promise.catch(err => ...)        future.exceptionally(err -> ...)
     *     Promise.all([p1, p2])            CompletableFuture.allOf(f1, f2)
     *     await promise                    future.join() or future.get()
     */
    
    static void level4_CompletableFuture() throws Exception {
        System.out.println("\n═══ LEVEL 4: COMPLETABLE FUTURE ═══\n");
        
        // ─────────────────────────────────────────────────────────────
        // BASIC: Run something async
        // ─────────────────────────────────────────────────────────────
        System.out.println("1️⃣ Basic async operation:\n");
        
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // This runs on a different thread!
            sleep(500);
            return "Hello from async!";
        });
        
        System.out.println("  Main thread: I'm not blocked!");
        String result = future.join();  // Like await
        System.out.println("  Result: " + result);
        
        // ─────────────────────────────────────────────────────────────
        // CHAINING: Like .then() in JavaScript!
        // ─────────────────────────────────────────────────────────────
        System.out.println("\n2️⃣ Chaining (like .then()):\n");
        
        CompletableFuture.supplyAsync(() -> {
            System.out.println("  Step 1: Fetching user...");
            sleep(300);
            return "John";
        })
        .thenApply(name -> {
            System.out.println("  Step 2: Got " + name + ", fetching orders...");
            sleep(300);
            return name + "'s orders: [Order1, Order2]";
        })
        .thenApply(orders -> {
            System.out.println("  Step 3: Got orders, calculating total...");
            return orders + " - Total: ₹5000";
        })
        .thenAccept(finalResult -> {
            System.out.println("  Final: " + finalResult);
        })
        .join();  // Wait for completion
        
        /*
         * NODE.JS EQUIVALENT:
         * ───────────────────
         * fetchUser()
         *     .then(name => fetchOrders(name))
         *     .then(orders => calculateTotal(orders))
         *     .then(result => console.log(result));
         */
        
        // ─────────────────────────────────────────────────────────────
        // PARALLEL: Run multiple things at once
        // ─────────────────────────────────────────────────────────────
        System.out.println("\n3️⃣ Parallel execution (like Promise.all):\n");
        
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("  Task1: Fetching user...");
            sleep(500);
            return "User: John";
        });
        
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("  Task2: Fetching products...");
            sleep(300);
            return "Products: [iPhone, MacBook]";
        });
        
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("  Task3: Fetching cart...");
            sleep(400);
            return "Cart: 2 items";
        });
        
        // Wait for ALL to complete (like Promise.all)
        CompletableFuture.allOf(task1, task2, task3).join();
        
        System.out.println("\n  All done!");
        System.out.println("  " + task1.join());
        System.out.println("  " + task2.join());
        System.out.println("  " + task3.join());
        
        // ─────────────────────────────────────────────────────────────
        // ERROR HANDLING: Like .catch()
        // ─────────────────────────────────────────────────────────────
        System.out.println("\n4️⃣ Error handling (like .catch()):\n");
        
        CompletableFuture.supplyAsync(() -> {
            if (true) throw new RuntimeException("Database connection failed!");
            return "data";
        })
        .exceptionally(error -> {
            System.out.println("  Caught error: " + error.getMessage());
            return "default data";  // Fallback value
        })
        .thenAccept(data -> System.out.println("  Using: " + data))
        .join();
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // LEVEL 5: REACTIVE PROGRAMMING (WebFlux/RxJava) 🌊
    // ═══════════════════════════════════════════════════════════════════════════
    /*
     * WHEN TO USE REACTIVE?
     * ─────────────────────
     * CompletableFuture: Single value, completes once
     * Reactive:          Stream of values, over time
     * 
     *     CompletableFuture<User>     → ONE user
     *     Mono<User>                  → ONE user (reactive)
     *     Flux<User>                  → MANY users (stream)
     *     
     * Think of it like:
     *     CompletableFuture = Promise (one value)
     *     Flux/Observable = Observable stream (many values over time)
     *     
     * NODE.JS COMPARISON:
     *     Reactive is like Node.js STREAMS + RxJS combined
     *     
     *     const stream = fs.createReadStream('file.txt');
     *     stream.on('data', chunk => console.log(chunk));
     *     stream.on('end', () => console.log('done'));
     *     
     *     // Similar to:
     *     Flux.fromIterable(data)
     *         .map(x -> transform(x))
     *         .subscribe(x -> System.out.println(x));
     * 
     * 
     * WHEN TO USE WHAT?
     * ─────────────────
     * 
     *     ┌─────────────────────────────────────────────────────────────────────┐
     *     │  Scenario                    │ Use                                  │
     *     ├─────────────────────────────────────────────────────────────────────┤
     *     │  Simple async operation      │ CompletableFuture                    │
     *     │  Multiple parallel calls     │ CompletableFuture.allOf()            │
     *     │  Streaming data              │ Reactive (Flux/Flow)                 │
     *     │  High-throughput APIs        │ Reactive (WebFlux)                   │
     *     │  Real-time updates           │ Reactive (WebSocket + Flux)          │
     *     │  Traditional REST API        │ CompletableFuture is enough!         │
     *     └─────────────────────────────────────────────────────────────────────┘
     */
    
    static void level5_ReactiveBasics() {
        System.out.println("\n═══ LEVEL 5: REACTIVE CONCEPTS ═══\n");
        
        System.out.println("  Reactive programming is for STREAMS of data.\n");
        System.out.println("  Without Spring WebFlux, here's the concept:\n");
        
        /*
         * REACTIVE VOCABULARY:
         * ────────────────────
         * Mono<T>    = 0 or 1 item  (like Optional + async)
         * Flux<T>    = 0 to N items (like Stream + async)
         * 
         * KEY METHODS:
         * ────────────
         * .map()           - Transform each item
         * .flatMap()       - Transform + flatten (for async operations)
         * .filter()        - Keep only matching items
         * .subscribe()     - Start the stream (like calling .then() finally)
         * 
         * EXAMPLE (with Spring WebFlux):
         * ──────────────────────────────
         * 
         * // Return single user
         * Mono<User> getUser(Long id) {
         *     return userRepository.findById(id);
         * }
         * 
         * // Return stream of users
         * Flux<User> getAllUsers() {
         *     return userRepository.findAll();
         * }
         * 
         * // Chaining
         * userService.getUser(1)
         *     .flatMap(user -> orderService.getOrders(user.getId()))
         *     .map(orders -> calculateTotal(orders))
         *     .subscribe(total -> sendEmail(total));
         */
        
        System.out.println("  Mono<User>  = Promise<User>     (0 or 1 value)");
        System.out.println("  Flux<User>  = Observable<User>  (stream of values)");
        System.out.println("\n  For most apps, CompletableFuture is enough!");
        System.out.println("  Use Reactive for high-throughput streaming scenarios.");
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // SUMMARY: Which to Use When?
    // ═══════════════════════════════════════════════════════════════════════════
    
    static void printSummary() {
        System.out.println("\n");
        System.out.println("═══════════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: JAVA ASYNC OPTIONS");
        System.out.println("═══════════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  ┌────────────────────┬──────────────────┬─────────────────────────┐");
        System.out.println("  │ JAVA               │ NODE.JS          │ WHEN TO USE             │");
        System.out.println("  ├────────────────────┼──────────────────┼─────────────────────────┤");
        System.out.println("  │ Thread             │ (no equivalent)  │ ❌ Don't use directly   │");
        System.out.println("  │ ExecutorService    │ worker_threads   │ Thread pool management  │");
        System.out.println("  │ Future             │ (no equivalent)  │ ❌ Use CompletableFuture│");
        System.out.println("  │ CompletableFuture  │ Promise          │ ✅ MOST COMMON!         │");
        System.out.println("  │ Reactive (Flux)    │ RxJS/Streams     │ High-throughput streams │");
        System.out.println("  └────────────────────┴──────────────────┴─────────────────────────┘");
        System.out.println();
        System.out.println("  🎯 FOR YOU (Node.js background):");
        System.out.println("     Start with CompletableFuture - it's just like Promise!");
        System.out.println("     Learn Reactive only when you need streaming/high-throughput.");
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════════════════");
    }
    
    // Helper method
    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (Exception e) {}
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("═══════════════════════════════════════════════════════════════════════");
        System.out.println("  JAVA ASYNC PROGRAMMING - For Node.js Developers");
        System.out.println("═══════════════════════════════════════════════════════════════════════");
        
        level1_RawThreads();
        level2_ExecutorFramework();
        level3_Future();
        level4_CompletableFuture();
        level5_ReactiveBasics();
        printSummary();
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * CHEAT SHEET: Node.js → Java Async
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * NODE.JS                              JAVA (CompletableFuture)
 * ────────                             ────────────────────────
 * 
 * // Create promise                    // Create future
 * const promise = new Promise(         CompletableFuture.supplyAsync(() -> {
 *   (resolve, reject) => {                 return "result";
 *     resolve("result");               });
 *   }
 * );
 * 
 * // Chain with .then()                // Chain with .thenApply()
 * promise                              future
 *   .then(x => x + "!")                  .thenApply(x -> x + "!")
 *   .then(x => console.log(x));          .thenAccept(x -> System.out.println(x));
 * 
 * // Error handling                    // Error handling
 * promise.catch(err => ...)            future.exceptionally(err -> ...)
 * 
 * // Wait for result                   // Wait for result
 * const result = await promise;        String result = future.join();
 * 
 * // Parallel execution                // Parallel execution
 * Promise.all([p1, p2, p3])            CompletableFuture.allOf(f1, f2, f3)
 * 
 * // Race (first to complete)          // Race (first to complete)
 * Promise.race([p1, p2])               CompletableFuture.anyOf(f1, f2)
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW TIP:
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * "What's the difference between Future and CompletableFuture?"
 * 
 * ANSWER:
 *   Future:
 *     - get() blocks the thread
 *     - Cannot chain operations
 *     - No error handling built-in
 *   
 *   CompletableFuture:
 *     - Can chain with thenApply(), thenCompose()
 *     - Has exceptionally() for error handling
 *     - Can combine multiple futures
 *     - Non-blocking with callbacks
 * 
 * 
 * "When would you use Reactive vs CompletableFuture?"
 * 
 * ANSWER:
 *   CompletableFuture:
 *     - Single async operation
 *     - Request-response pattern
 *     - Traditional REST APIs
 *   
 *   Reactive (WebFlux):
 *     - Streaming data (WebSocket, SSE)
 *     - Very high throughput (10K+ concurrent connections)
 *     - Backpressure handling needed
 *     - Event-driven architectures
 */
