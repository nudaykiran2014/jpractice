package spring_learning.lesson07_learning_roadmap;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  REACTIVE PROGRAMMING - How It Works Under the Hood                          ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Reactive programming is a paradigm for handling asynchronous data streams
 * using non-blocking operations.
 * 
 * In Spring: Project Reactor (Mono, Flux) + WebFlux
 */
public class _06_ReactiveProgramming {
    public static void main(String[] args) {
        System.out.println("=== REACTIVE PROGRAMMING EXPLAINED ===");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * THE CORE PROBLEM: Blocking I/O Wastes Threads
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * TRADITIONAL (Blocking):
 * ------------------------
 *     public User getUser(Long id) {
 *         User user = database.query("SELECT...");  // Thread WAITS here
 *         return user;                               // Thread blocked 50ms
 *     }
 *     
 *     Timeline:
 *     Thread 1: [----WAITING 50ms----][return]
 *                    ↑
 *               Thread does NOTHING but wait
 * 
 * REACTIVE (Non-Blocking):
 * -------------------------
 *     public Mono<User> getUser(Long id) {
 *         return database.query("SELECT...")  // Returns IMMEDIATELY
 *             .map(user -> transform(user));  // Called when data arrives
 *     }
 *     
 *     Timeline:
 *     Thread 1: [start query][FREE]...[callback: process result]
 *                            ↑
 *                    Thread handles other requests!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * THE EVENT LOOP MODEL (How It Really Works)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Reactive uses an EVENT LOOP (like Node.js, Nginx):
 * 
 *     ┌─────────────────────────────────────────────────────────────┐
 *     │                      EVENT LOOP                             │
 *     │                   (1 thread per CPU core)                   │
 *     │                                                             │
 *     │    ┌─────────────────────────────────────┐                  │
 *     │    │         EVENT QUEUE                 │                  │
 *     │    │  [Request1] [Request2] [Callback3]  │                  │
 *     │    └──────────────┬──────────────────────┘                  │
 *     │                   │                                         │
 *     │                   ▼                                         │
 *     │    ┌─────────────────────────────────────┐                  │
 *     │    │       PROCESS EVENT                 │                  │
 *     │    │   (non-blocking, quick)             │                  │
 *     │    └──────────────┬──────────────────────┘                  │
 *     │                   │                                         │
 *     │                   ▼                                         │
 *     │         If I/O needed:                                      │
 *     │         → Register callback                                 │
 *     │         → Move to next event                                │
 *     │         → When I/O completes, callback added to queue       │
 *     └─────────────────────────────────────────────────────────────┘
 * 
 * KEY INSIGHT:
 * -------------
 * - Only a FEW threads (typically = CPU cores)
 * - Each thread processes events from a queue
 * - NEVER blocks - if I/O needed, register callback and move on
 * - When I/O completes, OS notifies → callback added to queue
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * MONO AND FLUX - The Building Blocks
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * MONO<T>: 0 or 1 element (like Optional, but async)
 * --------------------------------------------------
 *     Mono<User> user = userRepository.findById(id);
 *     
 *     // Timeline:
 *     // [created] -------- [signal: User] → [complete]
 *     //     or
 *     // [created] -------- [signal: empty] → [complete]
 *     //     or  
 *     // [created] -------- [signal: error] → [error]
 * 
 * FLUX<T>: 0 to N elements (like Stream, but async)
 * --------------------------------------------------
 *     Flux<User> users = userRepository.findAll();
 *     
 *     // Timeline:
 *     // [created] -- [User1] -- [User2] -- [User3] -- [complete]
 *     //                ↑          ↑          ↑
 *     //           Each element emitted asynchronously
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * HOW SUBSCRIPTION WORKS (The Key Concept!)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * NOTHING HAPPENS UNTIL YOU SUBSCRIBE!
 * 
 *     // This does NOTHING - just creates a blueprint
 *     Mono<User> mono = userRepository.findById(1L)
 *         .map(user -> transform(user))
 *         .flatMap(user -> sendEmail(user));
 *     
 *     // This EXECUTES the pipeline
 *     mono.subscribe(
 *         user -> System.out.println("Got: " + user),   // onNext
 *         error -> System.err.println("Error: " + error), // onError
 *         () -> System.out.println("Done!")              // onComplete
 *     );
 * 
 * SUBSCRIPTION FLOW:
 * -------------------
 *     1. subscriber.subscribe(publisher)
 *     2. publisher.onSubscribe(subscription)
 *     3. subscriber.request(n)           // "Give me n items"
 *     4. publisher.onNext(item)          // Sends item
 *     5. publisher.onNext(item)          // Sends item
 *     6. publisher.onComplete()          // Done!
 *        or publisher.onError(error)     // Failed!
 * 
 *     ┌──────────────┐                    ┌──────────────┐
 *     │  SUBSCRIBER  │                    │  PUBLISHER   │
 *     └──────┬───────┘                    └──────┬───────┘
 *            │                                   │
 *            │──── subscribe() ─────────────────>│
 *            │<─── onSubscribe(subscription) ────│
 *            │                                   │
 *            │──── request(10) ─────────────────>│
 *            │<─── onNext(item1) ────────────────│
 *            │<─── onNext(item2) ────────────────│
 *            │<─── onComplete() ─────────────────│
 *            │                                   │
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * BACKPRESSURE - Flow Control
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PROBLEM: What if producer is faster than consumer?
 * 
 *     Producer: [1][2][3][4][5][6][7][8][9]...  (1000/sec)
 *     Consumer: [1]...[2]...[3]...              (10/sec)
 *               ↑
 *         Consumer drowns in data!
 * 
 * SOLUTION: Backpressure - Consumer controls the flow
 * 
 *     subscriber.request(10);   // "I can handle 10 items"
 *     // Publisher sends 10
 *     // Consumer processes them
 *     subscriber.request(10);   // "Ready for 10 more"
 *     // Publisher sends 10 more
 * 
 * STRATEGIES:
 * ------------
 *     flux.onBackpressureBuffer()    // Buffer excess items
 *     flux.onBackpressureDrop()      // Drop excess items
 *     flux.onBackpressureLatest()    // Keep only latest
 *     flux.onBackpressureError()     // Throw error
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * SCHEDULERS - Which Thread Runs What
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * SCHEDULER TYPES:
 * -----------------
 *     Schedulers.immediate()     // Current thread (default)
 *     Schedulers.single()        // Single reusable thread
 *     Schedulers.parallel()      // Fixed pool (CPU cores)
 *     Schedulers.boundedElastic() // For blocking I/O (like virtual threads)
 * 
 * CONTROLLING EXECUTION:
 * -----------------------
 *     Mono.fromCallable(() -> blockingOperation())
 *         .subscribeOn(Schedulers.boundedElastic())  // Run subscription here
 *         .publishOn(Schedulers.parallel())          // Run downstream here
 *         .map(data -> process(data))
 *         .subscribe();
 * 
 *     subscribeOn: WHERE the source runs
 *     publishOn:   WHERE downstream operators run
 * 
 *     TIMELINE:
 *     boundedElastic: [blockingOperation()]
 *                            │
 *                            ▼
 *     parallel:       [map()][subscribe callback]
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * OPERATORS - Transforming Streams
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * COMMON OPERATORS:
 * ------------------
 *     // Transform
 *     .map(x -> transform(x))           // 1-to-1 transformation
 *     .flatMap(x -> asyncOp(x))         // 1-to-Mono/Flux (async)
 *     .filter(x -> condition(x))        // Filter items
 *     
 *     // Combine
 *     .zip(other, (a, b) -> combine)    // Combine two streams
 *     .merge(other)                     // Merge streams
 *     .concat(other)                    // Sequential concat
 *     
 *     // Error Handling
 *     .onErrorReturn(defaultValue)      // Return default on error
 *     .onErrorResume(e -> fallback())   // Switch to fallback
 *     .retry(3)                         // Retry on error
 *     
 *     // Side Effects
 *     .doOnNext(x -> log(x))            // Peek at items
 *     .doOnError(e -> logError(e))      // Peek at errors
 *     .doOnComplete(() -> cleanup())    // On completion
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL EXAMPLE: Non-Blocking API Call
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     @RestController
 *     public class UserController {
 *         
 *         @GetMapping("/users/{id}")
 *         public Mono<UserDTO> getUser(@PathVariable Long id) {
 *             return userRepository.findById(id)          // Mono<User>
 *                 .flatMap(user -> 
 *                     orderService.getOrders(user.getId()) // Mono<List<Order>>
 *                         .map(orders -> new UserDTO(user, orders))
 *                 )
 *                 .switchIfEmpty(Mono.error(new UserNotFoundException(id)));
 *         }
 *     }
 *     
 *     // What happens:
 *     // 1. Request comes in
 *     // 2. findById() starts async DB query, returns immediately
 *     // 3. Thread is FREE to handle other requests
 *     // 4. When DB responds, callback triggers
 *     // 5. getOrders() starts async call, returns immediately  
 *     // 6. Thread is FREE again
 *     // 7. When orders arrive, UserDTO created
 *     // 8. Response sent to client
 *     
 *     // Total threads used: ~1 (shared with many requests)
 *     // Traditional approach: 1 thread blocked entire time
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * UNDER THE HOOD: How flatMap Works
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     Mono.just("hello")
 *         .flatMap(s -> callExternalApi(s))  // Returns Mono<Response>
 *         .map(response -> process(response))
 *         .subscribe();
 * 
 *     INTERNALLY:
 *     -----------
 *     1. "hello" emitted
 *     2. flatMap receives "hello"
 *     3. Calls callExternalApi("hello") → returns Mono<Response>
 *     4. flatMap SUBSCRIBES to this inner Mono
 *     5. When inner Mono emits Response, flatMap emits it downstream
 *     6. map() receives Response, transforms it
 *     7. subscribe() receives final result
 * 
 *     KEY: flatMap handles the "unwrapping" of nested Monos
 *     
 *     map:     Mono<A> → Mono<B>        (sync transform)
 *     flatMap: Mono<A> → Mono<Mono<B>> → Mono<B>  (async, flattens)
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * COMPARISON: Imperative vs Reactive
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * IMPERATIVE (Traditional):
 * --------------------------
 *     User user = userRepo.findById(id);           // Block
 *     List<Order> orders = orderRepo.findByUser(user); // Block  
 *     Email email = emailService.send(user);       // Block
 *     return new Response(user, orders, email);
 *     
 *     Total time: 50ms + 30ms + 20ms = 100ms (sequential)
 *     Thread: BLOCKED entire 100ms
 * 
 * REACTIVE:
 * ----------
 *     return userRepo.findById(id)                         // Async
 *         .flatMap(user -> 
 *             Mono.zip(
 *                 orderRepo.findByUser(user),              // Async
 *                 emailService.send(user),                 // Async (parallel!)
 *                 (orders, email) -> new Response(user, orders, email)
 *             )
 *         );
 *     
 *     Total time: 50ms + max(30ms, 20ms) = 80ms (parallel where possible)
 *     Thread: NEVER blocked, handles other requests meanwhile
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * VIRTUAL THREADS vs REACTIVE - Summary
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * | Aspect           | Reactive (WebFlux)    | Virtual Threads        |
 * |------------------|----------------------|------------------------|
 * | Code style       | Functional chains    | Imperative (familiar)  |
 * | Learning curve   | Steep                | Easy                   |
 * | Debugging        | Hard                 | Easy (stack traces)    |
 * | Performance      | Excellent            | Very Good              |
 * | Memory           | Very efficient       | Efficient              |
 * | Ecosystem        | Needs reactive libs  | Works with blocking    |
 * | Backpressure     | Built-in             | Manual                 |
 * 
 * WHEN TO USE REACTIVE:
 * - Streaming data (real-time feeds)
 * - Extremely high throughput needed
 * - Already have reactive codebase
 * 
 * WHEN TO USE VIRTUAL THREADS:
 * - New projects
 * - Team unfamiliar with reactive
 * - Using blocking libraries
 * - Simpler debugging needs
 */
