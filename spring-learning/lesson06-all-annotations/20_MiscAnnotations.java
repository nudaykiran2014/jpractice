package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 20: MISCELLANEOUS USEFUL ANNOTATIONS                                   ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Additional useful annotations you'll encounter in Spring Boot projects.
 */
public class _20_MiscAnnotations {
    public static void main(String[] args) {
        System.out.println("=== MISCELLANEOUS ANNOTATIONS ===\n");
        System.out.println("@PostConstruct    → Run after bean creation");
        System.out.println("@PreDestroy       → Run before bean destruction");
        System.out.println("@Order            → Bean ordering");
        System.out.println("@DependsOn        → Bean dependency order");
        System.out.println("@Lookup           → Method injection");
        System.out.println("@EnableWebMvc     → Enable Spring MVC");
        System.out.println("@EnableWebFlux    → Enable reactive web");
        System.out.println("@Retryable        → Retry on failure");
        System.out.println("@CircuitBreaker   → Circuit breaker pattern");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * LIFECYCLE ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Execute code at specific points in bean lifecycle
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class CacheService {
 *         
 *         private Map<String, Object> cache;
 *         
 *         @PostConstruct  // Runs after all dependencies injected
 *         public void init() {
 *             System.out.println("Initializing cache...");
 *             this.cache = new ConcurrentHashMap<>();
 *             loadInitialData();
 *         }
 *         
 *         @PreDestroy  // Runs before bean is destroyed
 *         public void cleanup() {
 *             System.out.println("Cleaning up cache...");
 *             cache.clear();
 *             saveToFile();
 *         }
 *     }
 *     
 *     // Alternative: implement interfaces
 *     @Component
 *     public class AnotherService implements InitializingBean, DisposableBean {
 *         
 *         @Override
 *         public void afterPropertiesSet() {  // Like @PostConstruct
 *             // Initialize
 *         }
 *         
 *         @Override
 *         public void destroy() {  // Like @PreDestroy
 *             // Cleanup
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Order and @DependsOn
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Control bean initialization order
 * 
 * EXAMPLE:
 * ---------
 *     // Lower number = higher priority
 *     @Component
 *     @Order(1)
 *     public class FirstFilter implements Filter { }
 *     
 *     @Component
 *     @Order(2)
 *     public class SecondFilter implements Filter { }
 *     
 *     @Component
 *     @Order(Ordered.LOWEST_PRECEDENCE)  // Last
 *     public class LastFilter implements Filter { }
 *     
 *     // Explicit dependency
 *     @Component
 *     @DependsOn({"dataSource", "cacheManager"})
 *     public class AppService {
 *         // Created after dataSource and cacheManager
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * SPRING RETRY ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Requires: spring-retry + spring-boot-starter-aop
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableRetry
 *     public class RetryConfig { }
 *     
 *     @Service
 *     public class ExternalApiService {
 *         
 *         @Retryable(
 *             value = {IOException.class, TimeoutException.class},
 *             maxAttempts = 3,
 *             backoff = @Backoff(delay = 1000, multiplier = 2)
 *         )
 *         public String callExternalApi() {
 *             // Retries up to 3 times with exponential backoff
 *             // 1s, 2s, 4s delays
 *             return restTemplate.getForObject(url, String.class);
 *         }
 *         
 *         @Recover  // Called when all retries fail
 *         public String recover(IOException e) {
 *             return "Fallback value";
 *         }
 *         
 *         // Retryable with condition
 *         @Retryable(
 *             value = ServiceException.class,
 *             maxAttempts = 5,
 *             backoff = @Backoff(delay = 500),
 *             exceptionExpression = "#{@retryChecker.shouldRetry(#root)}"
 *         )
 *         public void conditionalRetry() { }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * RESILIENCE4J ANNOTATIONS (Circuit Breaker)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Requires: resilience4j-spring-boot2
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class OrderService {
 *         
 *         @CircuitBreaker(name = "orderService", fallbackMethod = "fallback")
 *         public Order getOrder(Long id) {
 *             return externalService.fetchOrder(id);
 *         }
 *         
 *         // Fallback when circuit is open
 *         public Order fallback(Long id, Exception ex) {
 *             return new Order(id, "UNKNOWN");
 *         }
 *         
 *         @RateLimiter(name = "orderService")
 *         @Retry(name = "orderService")
 *         @CircuitBreaker(name = "orderService")
 *         public Order getOrderWithAll(Long id) {
 *             // Rate limiting + Retry + Circuit Breaker
 *         }
 *         
 *         @Bulkhead(name = "orderService", type = Bulkhead.Type.THREADPOOL)
 *         public Order getOrderBulkhead(Long id) {
 *             // Limits concurrent calls
 *         }
 *         
 *         @TimeLimiter(name = "orderService")
 *         public CompletableFuture<Order> getOrderAsync(Long id) {
 *             // Timeout after configured duration
 *         }
 *     }
 *     
 *     // Configuration in application.yml:
 *     // resilience4j.circuitbreaker:
 *     //   instances:
 *     //     orderService:
 *     //       slidingWindowSize: 10
 *     //       failureRateThreshold: 50
 *     //       waitDurationInOpenState: 10000
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * FEIGN CLIENT ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Requires: spring-cloud-starter-openfeign
 * 
 * EXAMPLE:
 * ---------
 *     @SpringBootApplication
 *     @EnableFeignClients
 *     public class Application { }
 *     
 *     @FeignClient(
 *         name = "user-service",
 *         url = "${user.service.url}",
 *         fallback = UserClientFallback.class
 *     )
 *     public interface UserClient {
 *         
 *         @GetMapping("/users/{id}")
 *         User getUser(@PathVariable Long id);
 *         
 *         @PostMapping("/users")
 *         User createUser(@RequestBody User user);
 *         
 *         @GetMapping("/users")
 *         List<User> getUsers(@RequestParam String status);
 *     }
 *     
 *     @Component
 *     public class UserClientFallback implements UserClient {
 *         @Override
 *         public User getUser(Long id) {
 *             return new User("Unknown");
 *         }
 *         // ... other fallbacks
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CONDITIONAL BEAN REGISTRATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class ConditionalBeans {
 *         
 *         // Only if running on specific OS
 *         @Bean
 *         @ConditionalOnOs(OS.LINUX)
 *         public LinuxService linuxService() {
 *             return new LinuxService();
 *         }
 *         
 *         // Only if running in cloud
 *         @Bean
 *         @ConditionalOnCloudPlatform(CloudPlatform.KUBERNETES)
 *         public K8sService k8sService() {
 *             return new K8sService();
 *         }
 *         
 *         // Only if Java version matches
 *         @Bean
 *         @ConditionalOnJava(range = Range.EQUAL_OR_NEWER, value = JavaVersion.SEVENTEEN)
 *         public ModernService modernService() {
 *             return new ModernService();
 *         }
 *     }
 * 
 * SUMMARY - WHEN TO USE WHAT:
 * ----------------------------
 * | Need                    | Annotation                |
 * |-------------------------|---------------------------|
 * | Init after injection    | @PostConstruct            |
 * | Cleanup before destroy  | @PreDestroy               |
 * | Control bean order      | @Order, @DependsOn        |
 * | Retry failed operations | @Retryable                |
 * | Handle failures         | @CircuitBreaker           |
 * | Call external services  | @FeignClient              |
 * | Limit concurrent calls  | @Bulkhead                 |
 * | Add timeout             | @TimeLimiter              |
 */
