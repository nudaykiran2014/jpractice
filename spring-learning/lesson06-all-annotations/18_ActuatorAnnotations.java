package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 18: ACTUATOR / MONITORING ANNOTATIONS                                  ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Spring Boot Actuator provides production-ready features for monitoring.
 * 
 * Requires: spring-boot-starter-actuator
 */
public class _18_ActuatorAnnotations {
    public static void main(String[] args) {
        System.out.println("=== ACTUATOR ANNOTATIONS ===\n");
        System.out.println("@Endpoint          → Custom actuator endpoint");
        System.out.println("@ReadOperation     → GET operation");
        System.out.println("@WriteOperation    → POST operation");
        System.out.println("@DeleteOperation   → DELETE operation");
        System.out.println("@Timed             → Measure method timing");
        System.out.println("@Counted           → Count method calls");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * CUSTOM ACTUATOR ENDPOINTS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Create custom monitoring/management endpoints
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     @Endpoint(id = "custom")  // /actuator/custom
 *     public class CustomEndpoint {
 *         
 *         // GET /actuator/custom
 *         @ReadOperation
 *         public Map<String, Object> info() {
 *             Map<String, Object> info = new HashMap<>();
 *             info.put("status", "running");
 *             info.put("version", "1.0.0");
 *             info.put("timestamp", LocalDateTime.now());
 *             return info;
 *         }
 *         
 *         // GET /actuator/custom/{name}
 *         @ReadOperation
 *         public String getByName(@Selector String name) {
 *             return "Hello, " + name;
 *         }
 *         
 *         // POST /actuator/custom
 *         @WriteOperation
 *         public void update(@Nullable String config) {
 *             // Update configuration
 *         }
 *         
 *         // DELETE /actuator/custom
 *         @DeleteOperation
 *         public void clear() {
 *             // Clear cache, etc.
 *         }
 *     }
 *     
 *     // Web-only endpoint (not JMX)
 *     @Component
 *     @WebEndpoint(id = "web-info")
 *     public class WebInfoEndpoint {
 *         
 *         @ReadOperation
 *         public String info() {
 *             return "Web-only endpoint";
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * HEALTH INDICATORS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Add custom health checks to /actuator/health
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class DatabaseHealthIndicator implements HealthIndicator {
 *         
 *         @Autowired
 *         private DataSource dataSource;
 *         
 *         @Override
 *         public Health health() {
 *             try {
 *                 Connection conn = dataSource.getConnection();
 *                 conn.close();
 *                 return Health.up()
 *                     .withDetail("database", "Connected")
 *                     .build();
 *             } catch (Exception e) {
 *                 return Health.down()
 *                     .withDetail("error", e.getMessage())
 *                     .build();
 *             }
 *         }
 *     }
 *     
 *     // Using reactive
 *     @Component
 *     public class ApiHealthIndicator implements ReactiveHealthIndicator {
 *         
 *         @Override
 *         public Mono<Health> health() {
 *             return webClient.get()
 *                 .uri("/health")
 *                 .retrieve()
 *                 .bodyToMono(String.class)
 *                 .map(s -> Health.up().build())
 *                 .onErrorResume(e -> Mono.just(Health.down().build()));
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * MICROMETER METRICS ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Collect metrics for monitoring (Prometheus, Grafana, etc.)
 * 
 * Requires: micrometer-core (included in actuator)
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class OrderService {
 *         
 *         // Time method execution
 *         @Timed(
 *             value = "order.create.time",
 *             description = "Time to create order",
 *             percentiles = {0.5, 0.95, 0.99}
 *         )
 *         public Order createOrder(OrderRequest request) {
 *             // ...
 *         }
 *         
 *         // Count method calls
 *         @Counted(
 *             value = "order.create.count",
 *             description = "Number of orders created"
 *         )
 *         public Order create(OrderRequest request) {
 *             // ...
 *         }
 *     }
 *     
 *     // Manual metrics
 *     @Service
 *     public class PaymentService {
 *         
 *         private final Counter paymentCounter;
 *         private final Timer paymentTimer;
 *         
 *         public PaymentService(MeterRegistry registry) {
 *             this.paymentCounter = registry.counter("payments.total");
 *             this.paymentTimer = registry.timer("payments.duration");
 *         }
 *         
 *         public void process(Payment payment) {
 *             paymentTimer.record(() -> {
 *                 // Process payment
 *                 paymentCounter.increment();
 *             });
 *         }
 *     }
 * 
 * BUILT-IN ENDPOINTS:
 * --------------------
 * | Endpoint      | Purpose                      |
 * |---------------|------------------------------|
 * | /health       | Application health           |
 * | /info         | Application info             |
 * | /metrics      | Application metrics          |
 * | /env          | Environment properties       |
 * | /beans        | All Spring beans             |
 * | /mappings     | Request mappings             |
 * | /loggers      | Logger levels                |
 * | /threaddump   | Thread dump                  |
 * | /heapdump     | Heap dump                    |
 * | /prometheus   | Prometheus format metrics    |
 * 
 * CONFIGURATION:
 * ---------------
 *     # application.properties
 *     management.endpoints.web.exposure.include=health,info,metrics
 *     management.endpoint.health.show-details=always
 */
