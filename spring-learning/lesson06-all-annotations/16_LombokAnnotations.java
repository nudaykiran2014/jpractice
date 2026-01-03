package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 16: LOMBOK ANNOTATIONS                                                 ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Lombok reduces boilerplate code by generating getters, setters, constructors, etc.
 * Very commonly used with Spring Boot projects.
 * 
 * Requires: lombok dependency + IDE plugin
 */
public class _16_LombokAnnotations {
    public static void main(String[] args) {
        System.out.println("=== LOMBOK ANNOTATIONS ===\n");
        System.out.println("@Getter/@Setter   → Generate getters/setters");
        System.out.println("@ToString         → Generate toString()");
        System.out.println("@EqualsAndHashCode→ Generate equals() & hashCode()");
        System.out.println("@NoArgsConstructor→ No-arg constructor");
        System.out.println("@AllArgsConstructor→ All-args constructor");
        System.out.println("@RequiredArgsConstructor → Final fields constructor");
        System.out.println("@Data             → All of the above combined");
        System.out.println("@Builder          → Builder pattern");
        System.out.println("@Slf4j            → Logger field");
        System.out.println("@Value            → Immutable class");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Getter and @Setter
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Generate getter and setter methods
 * 
 * EXAMPLE:
 * ---------
 *     // On class - applies to all fields
 *     @Getter
 *     @Setter
 *     public class User {
 *         private String name;
 *         private String email;
 *         private int age;
 *     }
 *     // Generates: getName(), setName(), getEmail(), setEmail(), getAge(), setAge()
 *     
 *     // On individual fields
 *     public class User {
 *         @Getter @Setter
 *         private String name;
 *         
 *         @Getter  // Read-only
 *         private String id;
 *         
 *         @Setter(AccessLevel.PROTECTED)  // Protected setter
 *         private String internalCode;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ToString
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Generate toString() method
 * 
 * EXAMPLE:
 * ---------
 *     @ToString
 *     public class User {
 *         private String name;
 *         private String email;
 *     }
 *     // Generates: "User(name=John, email=john@example.com)"
 *     
 *     // Exclude fields
 *     @ToString(exclude = {"password", "secretKey"})
 *     public class User {
 *         private String name;
 *         private String password;
 *     }
 *     
 *     // Include only specific fields
 *     @ToString(onlyExplicitlyIncluded = true)
 *     public class User {
 *         @ToString.Include
 *         private String name;
 *         
 *         private String password;  // Not included
 *     }
 *     
 *     // Include superclass
 *     @ToString(callSuper = true)
 *     public class Admin extends User { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @EqualsAndHashCode
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Generate equals() and hashCode() methods
 * 
 * EXAMPLE:
 * ---------
 *     @EqualsAndHashCode
 *     public class User {
 *         private Long id;
 *         private String email;
 *     }
 *     
 *     // Only use specific fields
 *     @EqualsAndHashCode(onlyExplicitlyIncluded = true)
 *     public class User {
 *         @EqualsAndHashCode.Include
 *         private Long id;
 *         
 *         private String name;  // Not used in equals/hashCode
 *     }
 *     
 *     // Exclude fields
 *     @EqualsAndHashCode(exclude = {"createdAt", "updatedAt"})
 *     public class User { }
 *     
 *     // Include superclass fields
 *     @EqualsAndHashCode(callSuper = true)
 *     public class Admin extends User { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CONSTRUCTOR ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @NoArgsConstructor  // public User() {}
 *     @AllArgsConstructor // public User(String name, String email) {}
 *     public class User {
 *         private String name;
 *         private String email;
 *     }
 *     
 *     // Required args = final fields + @NonNull fields
 *     @RequiredArgsConstructor
 *     public class UserService {
 *         private final UserRepository userRepository;  // Required
 *         private final EmailService emailService;      // Required
 *         private String tempValue;                     // Not required
 *     }
 *     // Generates: public UserService(UserRepository repo, EmailService email)
 *     
 *     // Private constructor (for static factory)
 *     @NoArgsConstructor(access = AccessLevel.PRIVATE)
 *     public class Utility { }
 *     
 *     // Force with default values
 *     @NoArgsConstructor(force = true)
 *     public class Config {
 *         private final String name;  // Will be null
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Data
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Shortcut for @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
 * 
 * EXAMPLE:
 * ---------
 *     @Data
 *     public class User {
 *         private Long id;
 *         private String name;
 *         private String email;
 *     }
 *     // Generates everything!
 *     
 *     // Common pattern for DTOs:
 *     @Data
 *     @NoArgsConstructor
 *     @AllArgsConstructor
 *     public class UserDTO {
 *         private String name;
 *         private String email;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Builder
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Generate builder pattern for object construction
 * 
 * EXAMPLE:
 * ---------
 *     @Builder
 *     public class User {
 *         private String name;
 *         private String email;
 *         private int age;
 *     }
 *     
 *     // Usage:
 *     User user = User.builder()
 *         .name("John")
 *         .email("john@example.com")
 *         .age(25)
 *         .build();
 *     
 *     // With defaults
 *     @Builder
 *     public class Config {
 *         @Builder.Default
 *         private int timeout = 30;
 *         
 *         @Builder.Default
 *         private boolean enabled = true;
 *     }
 *     
 *     // Combining with @Data
 *     @Data
 *     @Builder
 *     @NoArgsConstructor
 *     @AllArgsConstructor
 *     public class UserDTO { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Slf4j and Other Loggers
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Generate logger field
 * 
 * EXAMPLE:
 * ---------
 *     @Slf4j  // SLF4J (most common with Spring Boot)
 *     @Service
 *     public class UserService {
 *         
 *         public void createUser(String name) {
 *             log.info("Creating user: {}", name);
 *             log.debug("Debug info");
 *             log.error("Error occurred", exception);
 *         }
 *     }
 *     // Generates: private static final Logger log = LoggerFactory.getLogger(UserService.class);
 *     
 *     // Other logger annotations:
 *     @Log        // java.util.logging.Logger
 *     @Log4j      // Log4j 1.x
 *     @Log4j2     // Log4j 2.x
 *     @CommonsLog // Apache Commons Logging
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Value (Lombok's immutable version)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Create immutable class (all fields final, no setters)
 * 
 * EXAMPLE:
 * ---------
 *     @Value  // Immutable version of @Data
 *     public class Point {
 *         int x;
 *         int y;
 *     }
 *     // Generates:
 *     // - All fields final
 *     // - Only getters (no setters)
 *     // - All-args constructor
 *     // - equals, hashCode, toString
 *     
 *     Point p = new Point(10, 20);
 *     // p.setX(5);  // ERROR - no setter!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @NonNull
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Add null-check to setters and constructors
 * 
 * EXAMPLE:
 * ---------
 *     @Data
 *     public class User {
 *         @NonNull
 *         private String name;  // Throws NullPointerException if null
 *         
 *         private String email;  // Can be null
 *     }
 *     
 *     new User(null);  // Throws NullPointerException!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * SPRING BOOT + LOMBOK PATTERN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * BEST PRACTICE:
 * ---------------
 *     // Service with constructor injection
 *     @Slf4j
 *     @Service
 *     @RequiredArgsConstructor
 *     public class UserService {
 *         
 *         private final UserRepository userRepository;
 *         private final EmailService emailService;
 *         
 *         public User create(UserDTO dto) {
 *             log.info("Creating user: {}", dto.getName());
 *             // ...
 *         }
 *     }
 *     
 *     // DTO
 *     @Data
 *     @Builder
 *     @NoArgsConstructor
 *     @AllArgsConstructor
 *     public class UserDTO {
 *         private String name;
 *         private String email;
 *     }
 *     
 *     // Entity
 *     @Data
 *     @Entity
 *     @NoArgsConstructor
 *     @AllArgsConstructor
 *     public class User {
 *         @Id
 *         @GeneratedValue
 *         private Long id;
 *         private String name;
 *     }
 */
