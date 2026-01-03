package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 38: OBSERVABILITY / MICROMETER ANNOTATIONS                             ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Metrics, tracing, and logging for monitoring your application.
 * 
 * Requires: spring-boot-starter-actuator, micrometer-tracing
 */
public class _38_ObservabilityAnnotations {
    public static void main(String[] args) {
        System.out.println("=== OBSERVABILITY ANNOTATIONS ===\n");
        System.out.println("@Observed        → Auto-create metrics + traces");
        System.out.println("@Timed           → Measure method duration");
        System.out.println("@Counted         → Count method invocations");
        System.out.println("@NewSpan         → Create new trace span");
        System.out.println("@ContinueSpan    → Continue existing span");
        System.out.println("@SpanTag         → Add tag to span");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Observed (Spring 6+ / Boot 3+)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: All-in-one observability (metrics + tracing + logging)
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class ObservabilityConfig {
 *         
 *         @Bean
 *         public ObservedAspect observedAspect(ObservationRegistry registry) {
 *             return new ObservedAspect(registry);
 *         }
 *     }
 *     
 *     @Service
 *     public class OrderService {
 *         
 *         @Observed(
 *             name = "order.processing",
 *             contextualName = "process-order",
 *             lowCardinalityKeyValues = {"type", "standard"}
 *         )
 *         public Order processOrder(OrderRequest request) {
 *             // Automatically:
 *             // - Creates timer metric
 *             // - Creates trace span
 *             // - Logs start/end
 *             return orderRepository.save(new Order(request));
 *         }
 *     }
 *     
 *     // Metrics exposed:
 *     // order.processing.count
 *     // order.processing.duration
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Timed (Micrometer)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Measure method execution time
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class MetricsConfig {
 *         
 *         @Bean
 *         public TimedAspect timedAspect(MeterRegistry registry) {
 *             return new TimedAspect(registry);
 *         }
 *     }
 *     
 *     @Service
 *     public class UserService {
 *         
 *         @Timed(value = "user.fetch", description = "Time to fetch user")
 *         public User getUser(Long id) {
 *             return userRepository.findById(id).orElseThrow();
 *         }
 *         
 *         @Timed(
 *             value = "user.create",
 *             histogram = true,      // Include percentiles
 *             percentiles = {0.5, 0.95, 0.99}
 *         )
 *         public User createUser(User user) {
 *             return userRepository.save(user);
 *         }
 *         
 *         // With extra tags
 *         @Timed(value = "user.search", extraTags = {"region", "US"})
 *         public List<User> searchUsers(String query) {
 *             return userRepository.search(query);
 *         }
 *     }
 *     
 *     // Metrics at: /actuator/metrics/user.fetch
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Counted
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Count method invocations
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class MetricsConfig {
 *         
 *         @Bean
 *         public CountedAspect countedAspect(MeterRegistry registry) {
 *             return new CountedAspect(registry);
 *         }
 *     }
 *     
 *     @Service
 *     public class LoginService {
 *         
 *         @Counted(value = "login.attempts", description = "Login attempts")
 *         public boolean login(String username, String password) {
 *             return authenticate(username, password);
 *         }
 *         
 *         @Counted(
 *             value = "login.failures",
 *             recordFailuresOnly = true  // Only count exceptions
 *         )
 *         public void loginStrict(String username, String password) {
 *             if (!authenticate(username, password)) {
 *                 throw new AuthenticationException("Failed");
 *             }
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * DISTRIBUTED TRACING
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Track requests across microservices
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class OrderService {
 *         
 *         @Autowired
 *         private Tracer tracer;
 *         
 *         // Create new span
 *         @NewSpan("process-order")
 *         public Order processOrder(@SpanTag("orderId") String orderId) {
 *             // New span created for this method
 *             return doProcess(orderId);
 *         }
 *         
 *         // Continue existing span (add info to current span)
 *         @ContinueSpan
 *         public void validateOrder(@SpanTag("status") String status) {
 *             // Adds tag to existing span
 *         }
 *         
 *         // Programmatic span creation
 *         public Order manualSpan(String orderId) {
 *             Span span = tracer.nextSpan().name("custom-operation").start();
 *             try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
 *                 span.tag("orderId", orderId);
 *                 return doProcess(orderId);
 *             } finally {
 *                 span.end();
 *             }
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CUSTOM METRICS (Programmatic)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class MetricsService {
 *         
 *         private final Counter orderCounter;
 *         private final Timer orderTimer;
 *         private final Gauge activeUsers;
 *         
 *         public MetricsService(MeterRegistry registry) {
 *             // Counter - only goes up
 *             this.orderCounter = Counter.builder("orders.total")
 *                 .description("Total orders processed")
 *                 .tag("type", "all")
 *                 .register(registry);
 *             
 *             // Timer - measures duration
 *             this.orderTimer = Timer.builder("orders.processing.time")
 *                 .description("Order processing time")
 *                 .register(registry);
 *             
 *             // Gauge - can go up or down
 *             AtomicInteger activeUserCount = new AtomicInteger(0);
 *             this.activeUsers = Gauge.builder("users.active", activeUserCount, AtomicInteger::get)
 *                 .description("Currently active users")
 *                 .register(registry);
 *         }
 *         
 *         public void processOrder(Order order) {
 *             orderCounter.increment();
 *             
 *             orderTimer.record(() -> {
 *                 // Timed operation
 *                 doProcessing(order);
 *             });
 *         }
 *     }
 * 
 * METRIC TYPES:
 * --------------
 * | Type        | Use Case                    |
 * |-------------|-----------------------------| 
 * | Counter     | Total count (only up)       |
 * | Gauge       | Current value (up/down)     |
 * | Timer       | Duration + count            |
 * | Summary     | Percentiles                 |
 * | Histogram   | Distribution buckets        |
 * 
 * CONFIGURATION:
 * ---------------
 *     # application.properties
 *     management.endpoints.web.exposure.include=health,metrics,prometheus
 *     management.metrics.tags.application=my-app
 *     management.tracing.sampling.probability=1.0
 */
