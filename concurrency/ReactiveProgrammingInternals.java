/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  REACTIVE PROGRAMMING - How It Works Under The Hood 🔬
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * THE CORE PROBLEM: Why Reactive Exists
 * ══════════════════════════════════════
 * 
 * TRADITIONAL (Blocking):
 * ────────────────────────
 *     Thread 1: Call DB ──────────[WAITING 100ms]──────────→ Got data → Process
 *     Thread 2: Call API ─────────[WAITING 200ms]──────────→ Got data → Process
 *     Thread 3: Call DB ──────────[WAITING 100ms]──────────→ Got data → Process
 *     
 *     Problem: 1000 requests = 1000 threads waiting! 
 *              Each thread = ~1MB memory
 *              1000 threads = 1GB RAM just for WAITING! 😱
 * 
 * REACTIVE (Non-Blocking):
 * ─────────────────────────
 *     Thread 1: Call DB → [Don't wait!] → Handle other request
 *               └── When DB responds → Continue processing
 *     
 *     Result: 1000 requests = ~10 threads (reused!)
 *             Much less memory! 🎉
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * THE EVENT LOOP MODEL (Heart of Reactive)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Similar to Node.js! You already know this! 😊
 * 
 *     ┌─────────────────────────────────────────────────────────────────┐
 *     │                      EVENT LOOP                                 │
 *     │                                                                 │
 *     │   ┌─────────────────────────────────────────────────────────┐  │
 *     │   │                    Event Queue                          │  │
 *     │   │  [Request1] [DBCallback] [Request2] [APICallback] ...   │  │
 *     │   └─────────────────────────────────────────────────────────┘  │
 *     │                           │                                    │
 *     │                           ▼                                    │
 *     │   ┌─────────────────────────────────────────────────────────┐  │
 *     │   │              Worker Threads (few)                       │  │
 *     │   │         Pick event → Process → Pick next                │  │
 *     │   │                                                         │  │
 *     │   │    Thread1: Process Request1 → Done → Pick DBCallback   │  │
 *     │   │    Thread2: Process Request2 → Done → Pick next...      │  │
 *     │   └─────────────────────────────────────────────────────────┘  │
 *     │                                                                 │
 *     └─────────────────────────────────────────────────────────────────┘
 *     
 *     KEY: Threads NEVER wait! They process, then pick next task.
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * MONO & FLUX - The Core Types
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     Mono<T>  = Publisher that emits 0 or 1 item
 *     Flux<T>  = Publisher that emits 0 to N items
 *     
 *     ┌─────────────────────────────────────────────────────────────────┐
 *     │  Mono<User>                                                     │
 *     │  ──────────                                                     │
 *     │       │                                                         │
 *     │       └──→ [User] ──→ Complete                                  │
 *     │            (0 or 1)                                             │
 *     └─────────────────────────────────────────────────────────────────┘
 *     
 *     ┌─────────────────────────────────────────────────────────────────┐
 *     │  Flux<User>                                                     │
 *     │  ─────────                                                      │
 *     │       │                                                         │
 *     │       └──→ [User1] ──→ [User2] ──→ [User3] ──→ ... ──→ Complete│
 *     │            (stream of items over time)                          │
 *     └─────────────────────────────────────────────────────────────────┘
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * THE SUBSCRIPTION MODEL (Publisher-Subscriber)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     NOTHING happens until you SUBSCRIBE!
 *     
 *     Mono.just("Hello")           // Nothing happens yet!
 *         .map(s -> s + " World")  // Still nothing!
 *         .subscribe(System.out::println);  // NOW it runs!
 *     
 *     
 *     Internal Flow:
 *     ──────────────
 *     
 *     1. You call: mono.subscribe(consumer)
 *                           │
 *                           ▼
 *     2. Mono creates a Subscription
 *                           │
 *                           ▼
 *     3. Subscriber.onSubscribe(subscription) called
 *                           │
 *                           ▼
 *     4. Subscriber requests data: subscription.request(n)
 *                           │
 *                           ▼
 *     5. Publisher sends: onNext(item), onNext(item)...
 *                           │
 *                           ▼
 *     6. Publisher signals: onComplete() or onError(e)
 *     
 *     
 *     ┌──────────────┐         ┌──────────────┐
 *     │  Publisher   │◄────────│  Subscriber  │
 *     │  (Mono/Flux) │         │  (Consumer)  │
 *     └──────────────┘         └──────────────┘
 *           │                        │
 *           │   1. subscribe()       │
 *           │◄───────────────────────│
 *           │                        │
 *           │   2. onSubscribe(sub)  │
 *           │───────────────────────►│
 *           │                        │
 *           │   3. request(n)        │
 *           │◄───────────────────────│
 *           │                        │
 *           │   4. onNext(item)      │
 *           │───────────────────────►│
 *           │   ... (n times)        │
 *           │                        │
 *           │   5. onComplete()      │
 *           │───────────────────────►│
 *           │                        │
 */

import java.util.concurrent.*;
import java.util.function.*;

public class ReactiveProgrammingInternals {

    // ═══════════════════════════════════════════════════════════════════════════
    // SIMPLIFIED REACTIVE IMPLEMENTATION (To understand internals)
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Simplified Publisher interface
    interface SimplePublisher<T> {
        void subscribe(SimpleSubscriber<T> subscriber);
    }
    
    // Simplified Subscriber interface
    interface SimpleSubscriber<T> {
        void onSubscribe(SimpleSubscription subscription);
        void onNext(T item);
        void onComplete();
        void onError(Throwable error);
    }
    
    // Simplified Subscription interface
    interface SimpleSubscription {
        void request(long n);  // "Give me n items"
        void cancel();
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // SIMPLE MONO IMPLEMENTATION (Educational)
    // ═══════════════════════════════════════════════════════════════════════════
    
    static class SimpleMono<T> implements SimplePublisher<T> {
        private final Supplier<T> supplier;
        
        private SimpleMono(Supplier<T> supplier) {
            this.supplier = supplier;
        }
        
        public static <T> SimpleMono<T> fromSupplier(Supplier<T> supplier) {
            return new SimpleMono<>(supplier);
        }
        
        public static <T> SimpleMono<T> just(T value) {
            return new SimpleMono<>(() -> value);
        }
        
        // This is where the magic happens!
        @Override
        public void subscribe(SimpleSubscriber<T> subscriber) {
            // Create subscription
            SimpleSubscription subscription = new SimpleSubscription() {
                private boolean cancelled = false;
                
                @Override
                public void request(long n) {
                    if (cancelled) return;
                    
                    try {
                        // Get the value
                        T value = supplier.get();
                        
                        // Send to subscriber
                        subscriber.onNext(value);
                        subscriber.onComplete();
                        
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
                
                @Override
                public void cancel() {
                    cancelled = true;
                }
            };
            
            // Tell subscriber about subscription
            subscriber.onSubscribe(subscription);
        }
        
        // Map operator - transforms values
        public <R> SimpleMono<R> map(Function<T, R> mapper) {
            SimpleMono<T> upstream = this;
            
            return new SimpleMono<>(() -> {
                // This is lazy - only runs on subscribe!
                return null; // Placeholder
            }) {
                @Override
                public void subscribe(SimpleSubscriber<R> subscriber) {
                    // Subscribe to upstream, transform values
                    upstream.subscribe(new SimpleSubscriber<T>() {
                        SimpleSubscription upstreamSubscription;
                        
                        @Override
                        public void onSubscribe(SimpleSubscription s) {
                            upstreamSubscription = s;
                            subscriber.onSubscribe(s);
                        }
                        
                        @Override
                        public void onNext(T item) {
                            // TRANSFORM and pass downstream
                            R transformed = mapper.apply(item);
                            subscriber.onNext(transformed);
                        }
                        
                        @Override
                        public void onComplete() {
                            subscriber.onComplete();
                        }
                        
                        @Override
                        public void onError(Throwable error) {
                            subscriber.onError(error);
                        }
                    });
                }
            };
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // DEMO: How subscription works
    // ═══════════════════════════════════════════════════════════════════════════
    
    static void demoSubscription() {
        System.out.println("═══ HOW SUBSCRIPTION WORKS ═══\n");
        
        SimpleMono<String> mono = SimpleMono.just("Hello")
            .map(s -> {
                System.out.println("  3. Map operator transforming: " + s);
                return s + " World";
            });
        
        System.out.println("  1. Mono created - NOTHING executed yet!\n");
        
        System.out.println("  2. Calling subscribe()...\n");
        
        mono.subscribe(new SimpleSubscriber<String>() {
            @Override
            public void onSubscribe(SimpleSubscription subscription) {
                System.out.println("  4. onSubscribe called - requesting data");
                subscription.request(1);
            }
            
            @Override
            public void onNext(String item) {
                System.out.println("  5. onNext received: " + item);
            }
            
            @Override
            public void onComplete() {
                System.out.println("  6. onComplete - stream finished!");
            }
            
            @Override
            public void onError(Throwable error) {
                System.out.println("  Error: " + error.getMessage());
            }
        });
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // BACKPRESSURE EXPLAINED
    // ═══════════════════════════════════════════════════════════════════════════
    /*
     * BACKPRESSURE = Consumer telling Producer "slow down!"
     * 
     * Problem without backpressure:
     * ─────────────────────────────
     *     Producer: [item][item][item][item][item][item]...  (1000/sec)
     *                              │
     *                              ▼
     *     Consumer: [process]...[process]...                 (10/sec)
     *                              │
     *                              ▼
     *                         MEMORY OVERFLOW! 💥
     *     
     * 
     * With backpressure:
     * ──────────────────
     *     Consumer: "I can only handle 10 items"
     *                              │
     *                              ▼
     *     Producer: [item][item]...[item] (sends only 10)
     *                              │
     *                              ▼
     *     Consumer: processes 10, then asks for more
     *     
     *     
     * In code:
     * ────────
     *     subscription.request(10);  // "Give me 10 items"
     *     // ... process 10 items ...
     *     subscription.request(10);  // "Give me 10 more"
     *     
     *     
     * Backpressure Strategies:
     * ─────────────────────────
     *     1. BUFFER    - Store extras in memory (dangerous!)
     *     2. DROP      - Discard if consumer slow
     *     3. LATEST    - Keep only latest, discard old
     *     4. ERROR     - Throw error if overwhelmed
     */
    
    static void explainBackpressure() {
        System.out.println("\n═══ BACKPRESSURE EXPLAINED ═══\n");
        
        System.out.println("  Problem: Producer is FASTER than Consumer\n");
        System.out.println("    Producer: 1000 items/sec");
        System.out.println("    Consumer: 10 items/sec");
        System.out.println("    Result: Memory fills up! 💥\n");
        
        System.out.println("  Solution: Consumer controls the flow\n");
        System.out.println("    subscription.request(10);  // 'Give me 10'");
        System.out.println("    // process...");
        System.out.println("    subscription.request(10);  // 'Give me 10 more'\n");
        
        System.out.println("  Strategies:");
        System.out.println("    • BUFFER - Store in memory (risky)");
        System.out.println("    • DROP   - Discard extras");
        System.out.println("    • LATEST - Keep only newest");
        System.out.println("    • ERROR  - Fail if too fast");
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // SCHEDULERS (Where code runs)
    // ═══════════════════════════════════════════════════════════════════════════
    /*
     * Schedulers = Thread pools for reactive operations
     * 
     *     Schedulers.immediate()     - Current thread (no switching)
     *     Schedulers.single()        - Single reusable thread
     *     Schedulers.parallel()      - Fixed pool (CPU cores)
     *     Schedulers.boundedElastic()- Elastic pool for blocking I/O
     *     
     *     
     * Key Methods:
     * ────────────
     *     .subscribeOn(scheduler)  - Where to START the pipeline
     *     .publishOn(scheduler)    - Where to CONTINUE from this point
     *     
     *     
     * Example:
     * ────────
     *     Flux.range(1, 100)
     *         .subscribeOn(Schedulers.parallel())      // Start on parallel pool
     *         .map(i -> heavyComputation(i))          // Runs on parallel
     *         .publishOn(Schedulers.boundedElastic()) // Switch thread!
     *         .map(i -> blockingIO(i))                // Runs on elastic
     *         .subscribe();
     *         
     *         
     *     ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
     *     │ parallel()   │───►│ Switch via   │───►│ elastic()    │
     *     │ CPU work     │    │ publishOn()  │    │ I/O work     │
     *     └──────────────┘    └──────────────┘    └──────────────┘
     */
    
    static void explainSchedulers() {
        System.out.println("\n═══ SCHEDULERS (Thread Pools) ═══\n");
        
        System.out.println("  Available Schedulers:\n");
        System.out.println("    • immediate()      - Current thread");
        System.out.println("    • single()         - One thread (sequential)");
        System.out.println("    • parallel()       - CPU cores (for computation)");
        System.out.println("    • boundedElastic() - For blocking I/O\n");
        
        System.out.println("  Key Methods:\n");
        System.out.println("    • subscribeOn() - Where pipeline STARTS");
        System.out.println("    • publishOn()   - Where to SWITCH to\n");
        
        System.out.println("  Example Flow:");
        System.out.println("    Flux.range(1,100)");
        System.out.println("        .subscribeOn(parallel())     // Start here");
        System.out.println("        .map(heavyComputation)       // On parallel");
        System.out.println("        .publishOn(boundedElastic()) // Switch!");
        System.out.println("        .map(blockingIO)             // On elastic");
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // COLD vs HOT Publishers
    // ═══════════════════════════════════════════════════════════════════════════
    /*
     * COLD Publisher (Default):
     * ─────────────────────────
     *     - Data generated PER subscriber
     *     - Each subscriber gets ALL data from start
     *     - Like watching a movie on Netflix (start from beginning)
     *     
     *     Flux.range(1, 5);  // Each subscriber gets 1,2,3,4,5
     *     
     *     
     * HOT Publisher:
     * ──────────────
     *     - Data generated regardless of subscribers
     *     - Subscribers get data from when they join
     *     - Like watching live TV (miss what happened before)
     *     
     *     Flux.create(sink -> {
     *         // Emits to all current subscribers
     *     }).share();  // Makes it HOT!
     *     
     *     
     *     COLD:                          HOT:
     *     ─────                          ────
     *     Sub1: [1][2][3][4][5]          Sub1: [3][4][5]  (joined at 3)
     *     Sub2: [1][2][3][4][5]          Sub2: [4][5]     (joined at 4)
     *           (everyone gets all)            (miss earlier items)
     */
    
    static void explainHotVsCold() {
        System.out.println("\n═══ HOT vs COLD Publishers ═══\n");
        
        System.out.println("  COLD (Default):");
        System.out.println("    • Each subscriber gets ALL data");
        System.out.println("    • Like Netflix - start from beginning");
        System.out.println("    • Subscriber1: [1][2][3][4][5]");
        System.out.println("    • Subscriber2: [1][2][3][4][5]\n");
        
        System.out.println("  HOT:");
        System.out.println("    • Subscribers get data from when they join");
        System.out.println("    • Like live TV - miss what's passed");
        System.out.println("    • Subscriber1: [3][4][5]  (joined at 3)");
        System.out.println("    • Subscriber2: [5]        (joined at 5)\n");
        
        System.out.println("  Make HOT: .share() or .publish().connect()");
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // REAL WEBFLUX EXAMPLE (How it works in Spring)
    // ═══════════════════════════════════════════════════════════════════════════
    
    static void realWorldExample() {
        System.out.println("\n═══ REAL SPRING WEBFLUX FLOW ═══\n");
        
        System.out.println("  @GetMapping('/users/{id}')");
        System.out.println("  Mono<User> getUser(@PathVariable Long id) {");
        System.out.println("      return userRepository.findById(id);");
        System.out.println("  }\n");
        
        System.out.println("  What happens internally:");
        System.out.println("  ─────────────────────────\n");
        
        System.out.println("  1. Request arrives at Netty (non-blocking server)");
        System.out.println("     │");
        System.out.println("     ▼");
        System.out.println("  2. Event loop picks up request");
        System.out.println("     │");
        System.out.println("     ▼");
        System.out.println("  3. Controller method called - returns Mono<User>");
        System.out.println("     (Mono is just a RECIPE, not executed yet!)");
        System.out.println("     │");
        System.out.println("     ▼");
        System.out.println("  4. WebFlux subscribes to Mono");
        System.out.println("     │");
        System.out.println("     ▼");
        System.out.println("  5. Mono triggers DB query (non-blocking!)");
        System.out.println("     Thread goes to handle OTHER requests! 🎉");
        System.out.println("     │");
        System.out.println("     ▼");
        System.out.println("  6. DB responds → Callback fires");
        System.out.println("     │");
        System.out.println("     ▼");
        System.out.println("  7. Event loop picks up callback");
        System.out.println("     │");
        System.out.println("     ▼");
        System.out.println("  8. Response sent to client\n");
        
        System.out.println("  KEY: Thread NEVER waited for DB!");
        System.out.println("       It handled other requests while waiting.");
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  REACTIVE PROGRAMMING INTERNALS");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        
        demoSubscription();
        explainBackpressure();
        explainSchedulers();
        explainHotVsCold();
        realWorldExample();
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Reactive Internals");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        System.out.println("  🔑 KEY CONCEPTS:");
        System.out.println("  ─────────────────");
        System.out.println("  1. LAZY        - Nothing runs until subscribe()");
        System.out.println("  2. PUSH-BASED  - Publisher pushes data to Subscriber");
        System.out.println("  3. BACKPRESSURE- Consumer controls the flow");
        System.out.println("  4. NON-BLOCKING- Threads don't wait, they do other work");
        System.out.println("  5. EVENT LOOP  - Small thread pool handles many requests\n");
        
        System.out.println("  📊 COMPARISON WITH NODE.JS:");
        System.out.println("  ─────────────────────────────");
        System.out.println("  Node.js Event Loop  ≈  Netty Event Loop (in WebFlux)");
        System.out.println("  Promise             ≈  Mono (single value)");
        System.out.println("  Stream              ≈  Flux (multiple values)");
        System.out.println("  async/await         ≈  block() [but don't use in reactive!]\n");
        
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS & ANSWERS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q: "How does Reactive programming work internally?"
 * 
 * A: "Reactive uses the Publisher-Subscriber pattern. When you create a Mono or Flux,
 *     nothing executes - it's lazy. Only when you subscribe, the chain executes.
 *     
 *     The key is NON-BLOCKING I/O. When waiting for DB or API, the thread doesn't wait.
 *     It goes to handle other requests. When the response comes, a callback fires and
 *     an event loop thread picks it up.
 *     
 *     This allows handling thousands of concurrent requests with just a few threads,
 *     unlike traditional blocking where each request needs its own thread."
 * 
 * 
 * Q: "What is backpressure?"
 * 
 * A: "Backpressure is a mechanism where the consumer tells the producer how much data
 *     it can handle. Without it, a fast producer could overwhelm a slow consumer,
 *     causing memory overflow.
 *     
 *     In Reactive Streams, the subscriber calls request(n) to ask for n items.
 *     Strategies include BUFFER (store extras), DROP (discard), LATEST (keep newest),
 *     and ERROR (fail if too fast)."
 * 
 * 
 * Q: "Difference between subscribeOn and publishOn?"
 * 
 * A: "subscribeOn() controls where the entire pipeline STARTS executing - it affects
 *     everything upstream. You typically use it once at the beginning.
 *     
 *     publishOn() changes the thread for everything DOWNSTREAM from that point.
 *     You can use it multiple times to switch threads mid-pipeline.
 *     
 *     Example: Use subscribeOn(parallel) for CPU work, then publishOn(boundedElastic)
 *     before blocking I/O operations."
 * 
 * 
 * Q: "What's the difference between Cold and Hot publishers?"
 * 
 * A: "Cold publishers generate data per subscriber - each subscriber gets all data from
 *     the start, like watching a movie on Netflix.
 *     
 *     Hot publishers emit data regardless of subscribers - subscribers only get data from
 *     when they subscribe, like live TV. You create hot publishers with .share() or
 *     .publish().connect()."
 */
