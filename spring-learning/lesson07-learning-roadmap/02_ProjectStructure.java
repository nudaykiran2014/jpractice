package spring_learning.lesson07_learning_roadmap;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  SPRING BOOT PROJECT STRUCTURE - Classes & Layers Explained                  ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Understanding how to organize your Spring Boot project!
 */
public class _02_ProjectStructure {
    public static void main(String[] args) {
        System.out.println("=== SPRING BOOT PROJECT STRUCTURE ===");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * STANDARD PROJECT LAYOUT
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * my-spring-app/
 * ├── src/
 * │   ├── main/
 * │   │   ├── java/
 * │   │   │   └── com/example/myapp/
 * │   │   │       ├── MyAppApplication.java      ← Entry point
 * │   │   │       ├── controller/                ← REST Controllers
 * │   │   │       ├── service/                   ← Business Logic
 * │   │   │       ├── repository/                ← Data Access
 * │   │   │       ├── model/                     ← Entities
 * │   │   │       ├── dto/                       ← Data Transfer Objects
 * │   │   │       ├── config/                    ← Configuration classes
 * │   │   │       ├── exception/                 ← Custom exceptions
 * │   │   │       └── util/                      ← Utility classes
 * │   │   └── resources/
 * │   │       ├── application.yml                ← Configuration
 * │   │       ├── application-dev.yml            ← Dev profile
 * │   │       ├── application-prod.yml           ← Prod profile
 * │   │       └── db/migration/                  ← Flyway migrations
 * │   └── test/
 * │       └── java/                              ← Test classes
 * ├── pom.xml (or build.gradle)
 * └── README.md
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * THE LAYERED ARCHITECTURE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     ┌─────────────────────────────────────────────────────────────┐
 *     │                     CLIENT (Browser/App)                    │
 *     └─────────────────────────────┬───────────────────────────────┘
 *                                   │ HTTP Request
 *                                   ▼
 *     ┌─────────────────────────────────────────────────────────────┐
 *     │                    CONTROLLER LAYER                         │
 *     │              (Handle HTTP requests/responses)               │
 *     │                   @RestController                           │
 *     └─────────────────────────────┬───────────────────────────────┘
 *                                   │ calls
 *                                   ▼
 *     ┌─────────────────────────────────────────────────────────────┐
 *     │                     SERVICE LAYER                           │
 *     │                  (Business Logic)                           │
 *     │                      @Service                               │
 *     └─────────────────────────────┬───────────────────────────────┘
 *                                   │ calls
 *                                   ▼
 *     ┌─────────────────────────────────────────────────────────────┐
 *     │                   REPOSITORY LAYER                          │
 *     │                    (Data Access)                            │
 *     │                     @Repository                             │
 *     └─────────────────────────────┬───────────────────────────────┘
 *                                   │ queries
 *                                   ▼
 *     ┌─────────────────────────────────────────────────────────────┐
 *     │                       DATABASE                              │
 *     └─────────────────────────────────────────────────────────────┘
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. CONTROLLER LAYER (@RestController)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Handle HTTP requests, validate input, return responses
 * SHOULD:  Be thin! Just receive request → call service → return response
 * SHOULD NOT: Contain business logic or database calls
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @RequestMapping("/api/users")
 *     @RequiredArgsConstructor
 *     public class UserController {
 *         
 *         private final UserService userService;
 *         
 *         @GetMapping
 *         public List<UserDTO> getAllUsers() {
 *             return userService.findAll();
 *         }
 *         
 *         @GetMapping("/{id}")
 *         public UserDTO getUser(@PathVariable Long id) {
 *             return userService.findById(id);
 *         }
 *         
 *         @PostMapping
 *         @ResponseStatus(HttpStatus.CREATED)
 *         public UserDTO createUser(@Valid @RequestBody CreateUserRequest request) {
 *             return userService.create(request);
 *         }
 *         
 *         @PutMapping("/{id}")
 *         public UserDTO updateUser(@PathVariable Long id, 
 *                                   @Valid @RequestBody UpdateUserRequest request) {
 *             return userService.update(id, request);
 *         }
 *         
 *         @DeleteMapping("/{id}")
 *         @ResponseStatus(HttpStatus.NO_CONTENT)
 *         public void deleteUser(@PathVariable Long id) {
 *             userService.delete(id);
 *         }
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 2. SERVICE LAYER (@Service)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Business logic, validation rules, orchestration
 * SHOULD:  Contain all business rules, transaction boundaries
 * SHOULD NOT: Know about HTTP (no HttpServletRequest, no @RequestBody)
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     @RequiredArgsConstructor
 *     @Transactional(readOnly = true)
 *     public class UserService {
 *         
 *         private final UserRepository userRepository;
 *         private final UserMapper userMapper;
 *         private final PasswordEncoder passwordEncoder;
 *         
 *         public List<UserDTO> findAll() {
 *             return userRepository.findAll().stream()
 *                 .map(userMapper::toDto)
 *                 .toList();
 *         }
 *         
 *         public UserDTO findById(Long id) {
 *             User user = userRepository.findById(id)
 *                 .orElseThrow(() -> new UserNotFoundException(id));
 *             return userMapper.toDto(user);
 *         }
 *         
 *         @Transactional
 *         public UserDTO create(CreateUserRequest request) {
 *             // Business validation
 *             if (userRepository.existsByEmail(request.getEmail())) {
 *                 throw new EmailAlreadyExistsException(request.getEmail());
 *             }
 *             
 *             User user = new User();
 *             user.setName(request.getName());
 *             user.setEmail(request.getEmail());
 *             user.setPassword(passwordEncoder.encode(request.getPassword()));
 *             
 *             User saved = userRepository.save(user);
 *             return userMapper.toDto(saved);
 *         }
 *         
 *         @Transactional
 *         public UserDTO update(Long id, UpdateUserRequest request) {
 *             User user = userRepository.findById(id)
 *                 .orElseThrow(() -> new UserNotFoundException(id));
 *             
 *             user.setName(request.getName());
 *             // ... update other fields
 *             
 *             return userMapper.toDto(user);
 *         }
 *         
 *         @Transactional
 *         public void delete(Long id) {
 *             if (!userRepository.existsById(id)) {
 *                 throw new UserNotFoundException(id);
 *             }
 *             userRepository.deleteById(id);
 *         }
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 3. REPOSITORY LAYER (@Repository)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Data access, database queries
 * SHOULD:  Only contain data access logic
 * SHOULD NOT: Contain business logic
 * 
 * EXAMPLE:
 * ---------
 *     @Repository
 *     public interface UserRepository extends JpaRepository<User, Long> {
 *         
 *         // Spring Data generates implementation!
 *         Optional<User> findByEmail(String email);
 *         
 *         boolean existsByEmail(String email);
 *         
 *         List<User> findByNameContainingIgnoreCase(String name);
 *         
 *         @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
 *         List<User> findActiveUsers();
 *         
 *         @Query("SELECT u FROM User u WHERE u.createdAt > :date")
 *         List<User> findUsersCreatedAfter(@Param("date") LocalDateTime date);
 *         
 *         @Modifying
 *         @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
 *         int updateStatus(@Param("id") Long id, @Param("status") String status);
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 4. MODEL/ENTITY LAYER (@Entity)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Represent database tables
 * SHOULD:  Map to database schema
 * 
 * EXAMPLE:
 * ---------
 *     @Entity
 *     @Table(name = "users")
 *     @Getter @Setter
 *     @NoArgsConstructor
 *     public class User {
 *         
 *         @Id
 *         @GeneratedValue(strategy = GenerationType.IDENTITY)
 *         private Long id;
 *         
 *         @Column(nullable = false)
 *         private String name;
 *         
 *         @Column(unique = true, nullable = false)
 *         private String email;
 *         
 *         @Column(nullable = false)
 *         private String password;
 *         
 *         @Enumerated(EnumType.STRING)
 *         private UserStatus status = UserStatus.ACTIVE;
 *         
 *         @CreatedDate
 *         private LocalDateTime createdAt;
 *         
 *         @LastModifiedDate
 *         private LocalDateTime updatedAt;
 *         
 *         @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
 *         private List<Order> orders = new ArrayList<>();
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 5. DTO LAYER (Data Transfer Objects)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Transfer data between layers, hide entity details
 * WHY: Don't expose entities directly (security, flexibility)
 * 
 * EXAMPLE:
 * ---------
 *     // Response DTO
 *     public record UserDTO(
 *         Long id,
 *         String name,
 *         String email,
 *         LocalDateTime createdAt
 *     ) {}
 *     
 *     // Request DTO
 *     public record CreateUserRequest(
 *         @NotBlank String name,
 *         @Email @NotBlank String email,
 *         @Size(min = 8) String password
 *     ) {}
 *     
 *     public record UpdateUserRequest(
 *         @NotBlank String name
 *     ) {}
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 6. MAPPER (Entity ↔ DTO conversion)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE (MapStruct):
 * ---------------------
 *     @Mapper(componentModel = "spring")
 *     public interface UserMapper {
 *         
 *         UserDTO toDto(User user);
 *         
 *         List<UserDTO> toDtoList(List<User> users);
 *         
 *         @Mapping(target = "id", ignore = true)
 *         @Mapping(target = "createdAt", ignore = true)
 *         User toEntity(CreateUserRequest request);
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 7. EXCEPTION HANDLING
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     // Custom Exception
 *     public class UserNotFoundException extends RuntimeException {
 *         public UserNotFoundException(Long id) {
 *             super("User not found with id: " + id);
 *         }
 *     }
 *     
 *     // Global Exception Handler
 *     @RestControllerAdvice
 *     public class GlobalExceptionHandler {
 *         
 *         @ExceptionHandler(UserNotFoundException.class)
 *         @ResponseStatus(HttpStatus.NOT_FOUND)
 *         public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
 *             return new ErrorResponse("NOT_FOUND", ex.getMessage());
 *         }
 *         
 *         @ExceptionHandler(MethodArgumentNotValidException.class)
 *         @ResponseStatus(HttpStatus.BAD_REQUEST)
 *         public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
 *             List<String> errors = ex.getBindingResult().getFieldErrors().stream()
 *                 .map(e -> e.getField() + ": " + e.getDefaultMessage())
 *                 .toList();
 *             return new ErrorResponse("VALIDATION_ERROR", errors.toString());
 *         }
 *     }
 *     
 *     public record ErrorResponse(String code, String message) {}
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 8. CONFIGURATION CLASSES
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class SecurityConfig {
 *         
 *         @Bean
 *         public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 *             // Security configuration
 *         }
 *         
 *         @Bean
 *         public PasswordEncoder passwordEncoder() {
 *             return new BCryptPasswordEncoder();
 *         }
 *     }
 *     
 *     @Configuration
 *     public class WebConfig implements WebMvcConfigurer {
 *         
 *         @Override
 *         public void addCorsMappings(CorsRegistry registry) {
 *             registry.addMapping("/api/**")
 *                 .allowedOrigins("http://localhost:3000");
 *         }
 *     }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * SUMMARY: WHICH CLASS DOES WHAT?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * | Class Type   | Annotation        | Purpose                        |
 * |--------------|-------------------|--------------------------------|
 * | Controller   | @RestController   | HTTP handling                  |
 * | Service      | @Service          | Business logic                 |
 * | Repository   | @Repository       | Database access                |
 * | Entity       | @Entity           | Database table mapping         |
 * | DTO          | (plain class)     | Data transfer                  |
 * | Mapper       | @Mapper           | Entity ↔ DTO conversion        |
 * | Config       | @Configuration    | Bean definitions, settings     |
 * | Exception    | (plain class)     | Custom error types             |
 * | ExHandler    | @ControllerAdvice | Global exception handling      |
 */
