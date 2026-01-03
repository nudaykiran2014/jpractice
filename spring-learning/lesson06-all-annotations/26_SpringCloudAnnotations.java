package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 26: SPRING CLOUD ANNOTATIONS (Microservices)                           ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Spring Cloud provides tools for building microservices.
 */
public class _26_SpringCloudAnnotations {
    public static void main(String[] args) {
        System.out.println("=== SPRING CLOUD ANNOTATIONS ===\n");
        System.out.println("SERVICE DISCOVERY:");
        System.out.println("  @EnableEurekaServer  → Run Eureka server");
        System.out.println("  @EnableEurekaClient  → Register with Eureka");
        System.out.println("  @EnableDiscoveryClient → Generic discovery");
        System.out.println("\nLOAD BALANCING:");
        System.out.println("  @LoadBalanced        → Enable client-side LB");
        System.out.println("\nCONFIG:");
        System.out.println("  @EnableConfigServer  → Config server");
        System.out.println("  @RefreshScope        → Refresh config at runtime");
        System.out.println("\nGATEWAY:");
        System.out.println("  @EnableGateway       → API Gateway");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * SERVICE DISCOVERY (Eureka)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Services register themselves and discover each other
 * 
 * EXAMPLE - EUREKA SERVER:
 * -------------------------
 *     @SpringBootApplication
 *     @EnableEurekaServer
 *     public class EurekaServerApplication {
 *         public static void main(String[] args) {
 *             SpringApplication.run(EurekaServerApplication.class, args);
 *         }
 *     }
 *     
 *     # application.yml
 *     server:
 *       port: 8761
 *     eureka:
 *       client:
 *         register-with-eureka: false
 *         fetch-registry: false
 * 
 * EXAMPLE - EUREKA CLIENT:
 * -------------------------
 *     @SpringBootApplication
 *     @EnableEurekaClient  // or @EnableDiscoveryClient
 *     public class UserServiceApplication { }
 *     
 *     # application.yml
 *     spring:
 *       application:
 *         name: user-service
 *     eureka:
 *       client:
 *         service-url:
 *           defaultZone: http://localhost:8761/eureka/
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @LoadBalanced
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Enable client-side load balancing for RestTemplate/WebClient
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class RestConfig {
 *         
 *         @Bean
 *         @LoadBalanced  // Enables load balancing
 *         public RestTemplate restTemplate() {
 *             return new RestTemplate();
 *         }
 *     }
 *     
 *     @Service
 *     public class OrderService {
 *         
 *         @Autowired
 *         private RestTemplate restTemplate;
 *         
 *         public User getUser(String userId) {
 *             // Uses service name instead of hostname!
 *             // Load balancer picks an instance of user-service
 *             return restTemplate.getForObject(
 *                 "http://user-service/users/" + userId,  // Service name
 *                 User.class
 *             );
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CONFIG SERVER
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Centralized configuration management
 * 
 * EXAMPLE - CONFIG SERVER:
 * -------------------------
 *     @SpringBootApplication
 *     @EnableConfigServer
 *     public class ConfigServerApplication { }
 *     
 *     # application.yml
 *     server:
 *       port: 8888
 *     spring:
 *       cloud:
 *         config:
 *           server:
 *             git:
 *               uri: https://github.com/myorg/config-repo
 * 
 * EXAMPLE - CONFIG CLIENT:
 * -------------------------
 *     @SpringBootApplication
 *     public class UserServiceApplication { }
 *     
 *     # application.yml
 *     spring:
 *       application:
 *         name: user-service
 *       config:
 *         import: "configserver:http://localhost:8888"
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RefreshScope
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Refresh bean's properties without restart
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @RefreshScope  // Bean will be re-created on /actuator/refresh
 *     public class ConfigController {
 *         
 *         @Value("${app.message}")
 *         private String message;
 *         
 *         @GetMapping("/message")
 *         public String getMessage() {
 *             return message;  // Returns updated value after refresh
 *         }
 *     }
 *     
 *     // Trigger refresh: POST /actuator/refresh
 *     // Or use Spring Cloud Bus for cluster-wide refresh
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * API GATEWAY (Spring Cloud Gateway)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Single entry point for all microservices
 * 
 * EXAMPLE:
 * ---------
 *     @SpringBootApplication
 *     public class GatewayApplication { }
 *     
 *     # application.yml
 *     spring:
 *       cloud:
 *         gateway:
 *           routes:
 *             - id: user-service
 *               uri: lb://user-service
 *               predicates:
 *                 - Path=/api/users/**
 *               filters:
 *                 - StripPrefix=1
 *             
 *             - id: order-service
 *               uri: lb://order-service
 *               predicates:
 *                 - Path=/api/orders/**
 *     
 *     // Custom filter
 *     @Component
 *     public class AuthFilter implements GlobalFilter {
 *         
 *         @Override
 *         public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
 *             String token = exchange.getRequest().getHeaders().getFirst("Authorization");
 *             if (token == null) {
 *                 exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
 *                 return exchange.getResponse().setComplete();
 *             }
 *             return chain.filter(exchange);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * DISTRIBUTED TRACING (Sleuth + Zipkin)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Track requests across microservices
 * 
 * EXAMPLE:
 * ---------
 *     // Just add dependency - auto-configured!
 *     // spring-cloud-starter-sleuth
 *     // spring-cloud-sleuth-zipkin
 *     
 *     # application.yml
 *     spring:
 *       zipkin:
 *         base-url: http://localhost:9411
 *       sleuth:
 *         sampler:
 *           probability: 1.0  # Sample 100% of requests
 *     
 *     // Logs will automatically include trace/span IDs:
 *     // [user-service,trace-id,span-id] INFO ...
 * 
 * MICROSERVICES ARCHITECTURE:
 * ----------------------------
 *     ┌─────────────────────────────────────────────────────────────┐
 *     │                      API Gateway                            │
 *     │                    (Spring Cloud Gateway)                   │
 *     └──────────────┬────────────────┬────────────────┬────────────┘
 *                    │                │                │
 *          ┌─────────▼───┐   ┌────────▼────┐   ┌──────▼──────┐
 *          │ User Service│   │Order Service│   │Product Svc  │
 *          └─────────────┘   └─────────────┘   └─────────────┘
 *                    │                │                │
 *          ┌─────────▼────────────────▼────────────────▼─────────┐
 *          │                  Service Discovery                   │
 *          │                     (Eureka)                         │
 *          └─────────────────────────────────────────────────────┘
 *                                    │
 *          ┌─────────────────────────▼───────────────────────────┐
 *          │                  Config Server                       │
 *          └─────────────────────────────────────────────────────┘
 */
