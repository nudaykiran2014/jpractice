package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 29: R2DBC & REACTIVE ANNOTATIONS                                       ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * R2DBC = Reactive Relational Database Connectivity
 * Non-blocking database access for reactive applications.
 * 
 * Requires: spring-boot-starter-data-r2dbc
 */
public class _29_R2dbcReactiveAnnotations {
    public static void main(String[] args) {
        System.out.println("=== R2DBC & REACTIVE ANNOTATIONS ===\n");
        System.out.println("@EnableR2dbcRepositories → Enable R2DBC repos");
        System.out.println("@Table            → R2DBC table mapping");
        System.out.println("@Id               → Primary key");
        System.out.println("@Column           → Column mapping");
        System.out.println("@Transient        → Don't persist");
        System.out.println("@Query            → Custom query");
        System.out.println("Mono<T>           → Single async result");
        System.out.println("Flux<T>           → Stream of async results");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * R2DBC ENTITY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * NOTE: R2DBC uses Spring Data annotations, NOT JPA annotations!
 * 
 * EXAMPLE:
 * ---------
 *     import org.springframework.data.annotation.Id;
 *     import org.springframework.data.relational.core.mapping.Table;
 *     import org.springframework.data.relational.core.mapping.Column;
 *     
 *     @Table("users")  // NOT @Entity!
 *     public class User {
 *         
 *         @Id
 *         private Long id;
 *         
 *         @Column("user_name")
 *         private String name;
 *         
 *         private String email;
 *         
 *         @Transient  // Not persisted
 *         private String tempData;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * R2DBC REPOSITORY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Non-blocking database operations returning Mono/Flux
 * 
 * EXAMPLE:
 * ---------
 *     public interface UserRepository extends ReactiveCrudRepository<User, Long> {
 *         
 *         // Returns Flux (stream of users)
 *         Flux<User> findByName(String name);
 *         
 *         // Returns Mono (single user or empty)
 *         Mono<User> findByEmail(String email);
 *         
 *         // Custom query
 *         @Query("SELECT * FROM users WHERE status = :status")
 *         Flux<User> findByStatus(String status);
 *         
 *         // Modifying query
 *         @Modifying
 *         @Query("UPDATE users SET status = :status WHERE id = :id")
 *         Mono<Integer> updateStatus(Long id, String status);
 *         
 *         // Delete
 *         Mono<Void> deleteByEmail(String email);
 *         
 *         // Count
 *         Mono<Long> countByStatus(String status);
 *         
 *         // Exists
 *         Mono<Boolean> existsByEmail(String email);
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REACTIVE SERVICE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class UserService {
 *         
 *         @Autowired
 *         private UserRepository userRepository;
 *         
 *         public Mono<User> findById(Long id) {
 *             return userRepository.findById(id);
 *         }
 *         
 *         public Flux<User> findAll() {
 *             return userRepository.findAll();
 *         }
 *         
 *         public Mono<User> save(User user) {
 *             return userRepository.save(user);
 *         }
 *         
 *         // Chain operations
 *         public Mono<User> createAndNotify(User user) {
 *             return userRepository.save(user)
 *                 .doOnSuccess(saved -> log.info("User saved: {}", saved.getId()))
 *                 .flatMap(saved -> notificationService.sendWelcome(saved)
 *                     .thenReturn(saved));
 *         }
 *         
 *         // Error handling
 *         public Mono<User> findByIdOrThrow(Long id) {
 *             return userRepository.findById(id)
 *                 .switchIfEmpty(Mono.error(new NotFoundException("User not found")));
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REACTIVE CONTROLLER (WebFlux)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @RequestMapping("/api/users")
 *     public class UserController {
 *         
 *         @Autowired
 *         private UserService userService;
 *         
 *         // Returns stream of users
 *         @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
 *         public Flux<User> streamUsers() {
 *             return userService.findAll()
 *                 .delayElements(Duration.ofMillis(100));  // Simulate streaming
 *         }
 *         
 *         // Single user
 *         @GetMapping("/{id}")
 *         public Mono<ResponseEntity<User>> getUser(@PathVariable Long id) {
 *             return userService.findById(id)
 *                 .map(ResponseEntity::ok)
 *                 .defaultIfEmpty(ResponseEntity.notFound().build());
 *         }
 *         
 *         // Create user
 *         @PostMapping
 *         @ResponseStatus(HttpStatus.CREATED)
 *         public Mono<User> createUser(@RequestBody User user) {
 *             return userService.save(user);
 *         }
 *         
 *         // Error handling
 *         @GetMapping("/search")
 *         public Flux<User> search(@RequestParam String name) {
 *             return userService.findByName(name)
 *                 .onErrorResume(e -> Flux.empty());
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Transactional (Reactive)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class OrderService {
 *         
 *         @Autowired
 *         private TransactionalOperator transactionalOperator;
 *         
 *         // Using annotation
 *         @Transactional
 *         public Mono<Order> createOrder(OrderRequest request) {
 *             return orderRepository.save(new Order(request))
 *                 .flatMap(order -> 
 *                     paymentService.processPayment(order)
 *                         .thenReturn(order)
 *                 );
 *         }
 *         
 *         // Programmatic transaction
 *         public Mono<Order> createOrderProgrammatic(OrderRequest request) {
 *             return transactionalOperator.transactional(
 *                 orderRepository.save(new Order(request))
 *                     .flatMap(order -> paymentService.processPayment(order)
 *                         .thenReturn(order))
 *             );
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * MONO vs FLUX
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * MONO<T>: 0 or 1 element
 * --------------------------
 *     Mono.just("Hello")           // Single value
 *     Mono.empty()                 // No value
 *     Mono.error(new Exception())  // Error
 *     
 *     // Operations
 *     mono.map(s -> s.toUpperCase())
 *     mono.flatMap(s -> anotherMono)
 *     mono.filter(s -> s.length() > 5)
 *     mono.switchIfEmpty(fallbackMono)
 *     mono.block()  // Block and get value (avoid in reactive code!)
 * 
 * FLUX<T>: 0 to N elements (stream)
 * -----------------------------------
 *     Flux.just("A", "B", "C")     // Multiple values
 *     Flux.fromIterable(list)      // From collection
 *     Flux.range(1, 10)            // Numbers 1-10
 *     Flux.interval(Duration.ofSeconds(1))  // Emit every second
 *     
 *     // Operations
 *     flux.map(s -> s.toUpperCase())
 *     flux.filter(s -> s.length() > 5)
 *     flux.take(5)                 // First 5 elements
 *     flux.skip(2)                 // Skip first 2
 *     flux.collectList()           // Convert to Mono<List<T>>
 * 
 * CONFIGURATION:
 * ---------------
 *     # application.properties
 *     spring.r2dbc.url=r2dbc:postgresql://localhost:5432/mydb
 *     spring.r2dbc.username=user
 *     spring.r2dbc.password=pass
 *     
 *     # Or H2
 *     spring.r2dbc.url=r2dbc:h2:mem:///testdb
 */
