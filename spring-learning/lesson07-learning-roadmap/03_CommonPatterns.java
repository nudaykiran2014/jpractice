package spring_learning.lesson07_learning_roadmap;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  COMMON SPRING BOOT PATTERNS - Code You'll Write Every Day                   ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class _03_CommonPatterns {
    public static void main(String[] args) {
        System.out.println("=== COMMON SPRING BOOT PATTERNS ===");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 1: CRUD REST API
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * The most common pattern - you'll write this hundreds of times!
 * 
 *     Controller → Service → Repository → Entity
 * 
 * HTTP Methods:
 *     GET    /items      → List all
 *     GET    /items/{id} → Get one
 *     POST   /items      → Create
 *     PUT    /items/{id} → Update (full)
 *     PATCH  /items/{id} → Update (partial)
 *     DELETE /items/{id} → Delete
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 2: REQUEST → VALIDATE → PROCESS → RESPOND
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     @PostMapping
 *     public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
 *         //        ↑ VALIDATE                        ↑ REQUEST
 *         
 *         UserDTO user = userService.create(request);  // PROCESS
 *         
 *         return ResponseEntity.created(uri).body(user);  // RESPOND
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 3: FIND OR THROW
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     public User findById(Long id) {
 *         return userRepository.findById(id)
 *             .orElseThrow(() -> new UserNotFoundException(id));
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 4: BUILDER FOR COMPLEX OBJECTS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     User user = User.builder()
 *         .name("John")
 *         .email("john@example.com")
 *         .status(UserStatus.ACTIVE)
 *         .build();
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 5: CONSTRUCTOR INJECTION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     @Service
 *     @RequiredArgsConstructor  // Lombok generates constructor
 *     public class UserService {
 *         
 *         private final UserRepository userRepository;  // Injected!
 *         private final PasswordEncoder passwordEncoder;  // Injected!
 *         
 *         // No need for @Autowired
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 6: STREAM + MAP FOR TRANSFORMATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     // Entity List → DTO List
 *     List<UserDTO> dtos = users.stream()
 *         .map(userMapper::toDto)
 *         .toList();
 *     
 *     // Filter + Transform
 *     List<String> activeEmails = users.stream()
 *         .filter(u -> u.getStatus() == Status.ACTIVE)
 *         .map(User::getEmail)
 *         .toList();
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 7: PAGINATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     @GetMapping
 *     public Page<UserDTO> getUsers(
 *         @RequestParam(defaultValue = "0") int page,
 *         @RequestParam(defaultValue = "10") int size,
 *         @RequestParam(defaultValue = "id") String sortBy
 *     ) {
 *         Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
 *         return userService.findAll(pageable);
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 8: RESPONSE ENTITY FOR FULL CONTROL
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     @GetMapping("/{id}")
 *     public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
 *         return userService.findById(id)
 *             .map(ResponseEntity::ok)
 *             .orElse(ResponseEntity.notFound().build());
 *     }
 *     
 *     @PostMapping
 *     public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
 *         UserDTO created = userService.create(request);
 *         URI location = URI.create("/api/users/" + created.id());
 *         return ResponseEntity.created(location).body(created);
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 9: CONDITIONAL LOGIC WITH OPTIONAL
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     // Instead of null checks:
 *     if (user != null) {
 *         return user.getName();
 *     }
 *     
 *     // Use Optional:
 *     return userRepository.findById(id)
 *         .map(User::getName)
 *         .orElse("Unknown");
 *     
 *     // Or throw if not found:
 *     return userRepository.findById(id)
 *         .orElseThrow(() -> new NotFoundException("User not found"));
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 10: TRANSACTIONAL SERVICE METHODS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     @Service
 *     @Transactional(readOnly = true)  // Default for class
 *     public class OrderService {
 *         
 *         public List<Order> findAll() {  // Read-only
 *             return orderRepository.findAll();
 *         }
 *         
 *         @Transactional  // Write transaction
 *         public Order createOrder(OrderRequest request) {
 *             Order order = new Order();
 *             // ... 
 *             return orderRepository.save(order);
 *         }
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 11: ASYNC PROCESSING
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     @Service
 *     public class EmailService {
 *         
 *         @Async
 *         public CompletableFuture<Void> sendWelcomeEmail(User user) {
 *             // This runs in a separate thread
 *             emailClient.send(user.getEmail(), "Welcome!");
 *             return CompletableFuture.completedFuture(null);
 *         }
 *     }
 *     
 *     // Controller doesn't wait for email to send
 *     @PostMapping
 *     public UserDTO createUser(@RequestBody CreateUserRequest request) {
 *         UserDTO user = userService.create(request);
 *         emailService.sendWelcomeEmail(user);  // Fire and forget
 *         return user;
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 12: CACHING
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     @Service
 *     public class ProductService {
 *         
 *         @Cacheable(value = "products", key = "#id")
 *         public Product findById(Long id) {
 *             // Only called if not in cache
 *             return productRepository.findById(id).orElseThrow();
 *         }
 *         
 *         @CacheEvict(value = "products", key = "#product.id")
 *         public Product update(Product product) {
 *             // Removes from cache after update
 *             return productRepository.save(product);
 *         }
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 13: EVENT-DRIVEN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     // Publish event
 *     @Service
 *     public class UserService {
 *         
 *         @Autowired
 *         private ApplicationEventPublisher eventPublisher;
 *         
 *         @Transactional
 *         public User createUser(CreateUserRequest request) {
 *             User user = userRepository.save(new User(request));
 *             eventPublisher.publishEvent(new UserCreatedEvent(user));
 *             return user;
 *         }
 *     }
 *     
 *     // Listen to event
 *     @Component
 *     public class UserEventListener {
 *         
 *         @EventListener
 *         public void handleUserCreated(UserCreatedEvent event) {
 *             emailService.sendWelcome(event.getUser());
 *         }
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 14: SPECIFICATION PATTERN (Dynamic Queries)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     public interface UserRepository extends JpaRepository<User, Long>,
 *                                            JpaSpecificationExecutor<User> {}
 *     
 *     // Build dynamic queries
 *     public List<User> search(UserSearchCriteria criteria) {
 *         Specification<User> spec = Specification.where(null);
 *         
 *         if (criteria.getName() != null) {
 *             spec = spec.and((root, query, cb) -> 
 *                 cb.like(root.get("name"), "%" + criteria.getName() + "%"));
 *         }
 *         
 *         if (criteria.getStatus() != null) {
 *             spec = spec.and((root, query, cb) -> 
 *                 cb.equal(root.get("status"), criteria.getStatus()));
 *         }
 *         
 *         return userRepository.findAll(spec);
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PATTERN 15: RETRY ON FAILURE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     @Service
 *     public class PaymentService {
 *         
 *         @Retryable(
 *             value = PaymentException.class,
 *             maxAttempts = 3,
 *             backoff = @Backoff(delay = 1000)
 *         )
 *         public Payment processPayment(PaymentRequest request) {
 *             return paymentGateway.charge(request);  // Retries on failure
 *         }
 *         
 *         @Recover
 *         public Payment recoverPayment(PaymentException e, PaymentRequest request) {
 *             // Called after all retries fail
 *             return Payment.failed(request, e.getMessage());
 *         }
 *     }
 */
