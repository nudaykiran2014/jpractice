package spring_learning.lesson05_spring_boot;

/**
 * LESSON 5: Spring Boot - The Modern Way
 * 
 * ════════════════════════════════════════════════════════════
 * SPRING vs SPRING BOOT - What's the difference?
 * ════════════════════════════════════════════════════════════
 * 
 * SPRING FRAMEWORK (2004):
 * - Powerful but required lots of configuration
 * - XML files everywhere
 * - Manual server setup (Tomcat, Jetty)
 * - "I'll give you tools, you figure out how to use them"
 * 
 * SPRING BOOT (2014):
 * - "Convention over Configuration"
 * - Auto-configuration (smart defaults)
 * - Embedded server (just run!)
 * - "I'll guess what you need and set it up"
 * 
 * ════════════════════════════════════════════════════════════
 * ANALOGY: Building a House
 * ════════════════════════════════════════════════════════════
 * 
 * SPRING FRAMEWORK:
 * - Here's lumber, nails, concrete, wires, pipes
 * - Here's a manual on how to build
 * - Good luck! 🏗️
 * 
 * SPRING BOOT:
 * - What kind of house? (web app? batch job? microservice?)
 * - 3 bedrooms? Got it!
 * - Here's your ready-to-move-in house! 🏠
 * - (You can still customize everything if you want)
 * 
 * ════════════════════════════════════════════════════════════
 * KEY SPRING BOOT ANNOTATIONS (The Magic Words)
 * ════════════════════════════════════════════════════════════
 * 
 * @SpringBootApplication  → "This is my main app, start everything!"
 *    └── Combines: @Configuration + @EnableAutoConfiguration + @ComponentScan
 * 
 * @Component              → "This class is a bean, manage it!"
 * @Service                → "This is a business logic bean"
 * @Repository             → "This is a data access bean"
 * @Controller             → "This handles web requests"
 * @RestController         → "This handles REST API requests"
 * 
 * @Autowired              → "Inject a bean here automatically"
 * @Bean                   → "This method creates a bean"
 * @Value                  → "Inject a value from config"
 * 
 * @GetMapping("/path")    → "Handle GET requests to /path"
 * @PostMapping("/path")   → "Handle POST requests to /path"
 * 
 * ════════════════════════════════════════════════════════════
 */
public class SpringBootMagicDemo {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     LESSON 5: SPRING BOOT - THE MODERN WAY               ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        
        // ═══════════════════════════════════════════════════════════
        // What a REAL Spring Boot app looks like
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("📁 A Minimal Spring Boot Project Structure:\n");
        System.out.println("   my-app/");
        System.out.println("   ├── src/main/java/");
        System.out.println("   │   └── com/example/myapp/");
        System.out.println("   │       ├── MyAppApplication.java      ← Main class");
        System.out.println("   │       ├── controller/");
        System.out.println("   │       │   └── UserController.java    ← REST endpoints");
        System.out.println("   │       ├── service/");
        System.out.println("   │       │   └── UserService.java       ← Business logic");
        System.out.println("   │       └── repository/");
        System.out.println("   │           └── UserRepository.java    ← Data access");
        System.out.println("   ├── src/main/resources/");
        System.out.println("   │   └── application.properties         ← Configuration");
        System.out.println("   └── pom.xml                            ← Dependencies\n");
        
        // ═══════════════════════════════════════════════════════════
        // The Main Application Class
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("📄 MyAppApplication.java (The entry point)\n");
        System.out.println("   @SpringBootApplication");
        System.out.println("   public class MyAppApplication {");
        System.out.println("       public static void main(String[] args) {");
        System.out.println("           SpringApplication.run(MyAppApplication.class, args);");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println("\n   → That's IT! Spring Boot starts a web server automatically!\n");
        
        // ═══════════════════════════════════════════════════════════
        // A REST Controller
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("📄 UserController.java (Handles HTTP requests)\n");
        System.out.println("   @RestController");
        System.out.println("   @RequestMapping(\"/api/users\")");
        System.out.println("   public class UserController {");
        System.out.println("");
        System.out.println("       @Autowired  // ← Spring injects this automatically!");
        System.out.println("       private UserService userService;");
        System.out.println("");
        System.out.println("       @GetMapping  // ← GET /api/users");
        System.out.println("       public List<User> getAllUsers() {");
        System.out.println("           return userService.findAll();");
        System.out.println("       }");
        System.out.println("");
        System.out.println("       @GetMapping(\"/{id}\")  // ← GET /api/users/123");
        System.out.println("       public User getUser(@PathVariable Long id) {");
        System.out.println("           return userService.findById(id);");
        System.out.println("       }");
        System.out.println("");
        System.out.println("       @PostMapping  // ← POST /api/users");
        System.out.println("       public User createUser(@RequestBody User user) {");
        System.out.println("           return userService.save(user);");
        System.out.println("       }");
        System.out.println("   }\n");
        
        // ═══════════════════════════════════════════════════════════
        // A Service Class
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("📄 UserService.java (Business logic)\n");
        System.out.println("   @Service  // ← Marks this as a Spring bean");
        System.out.println("   public class UserService {");
        System.out.println("");
        System.out.println("       @Autowired");
        System.out.println("       private UserRepository userRepository;");
        System.out.println("");
        System.out.println("       public List<User> findAll() {");
        System.out.println("           return userRepository.findAll();");
        System.out.println("       }");
        System.out.println("");
        System.out.println("       public User save(User user) {");
        System.out.println("           // Business logic here (validation, etc.)");
        System.out.println("           return userRepository.save(user);");
        System.out.println("       }");
        System.out.println("   }\n");
        
        // ═══════════════════════════════════════════════════════════
        // AUTO-CONFIGURATION MAGIC
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("✨ SPRING BOOT AUTO-CONFIGURATION MAGIC:\n");
        System.out.println("   When you add a dependency, Spring Boot configures it!");
        System.out.println("");
        System.out.println("   Add spring-boot-starter-web:");
        System.out.println("   → Embedded Tomcat server configured ✓");
        System.out.println("   → JSON serialization configured ✓");
        System.out.println("   → Error handling configured ✓");
        System.out.println("");
        System.out.println("   Add spring-boot-starter-data-jpa:");
        System.out.println("   → DataSource configured ✓");
        System.out.println("   → EntityManager configured ✓");
        System.out.println("   → Transaction management configured ✓");
        System.out.println("");
        System.out.println("   You just write YOUR code, Spring Boot handles the rest!\n");
        
        // ═══════════════════════════════════════════════════════════
        // SUMMARY
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  SPRING BOOT SUMMARY                                     ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Add dependencies (starters) in pom.xml               ║");
        System.out.println("║  2. Write @SpringBootApplication main class              ║");
        System.out.println("║  3. Create @RestController for endpoints                 ║");
        System.out.println("║  4. Create @Service for business logic                   ║");
        System.out.println("║  5. Create @Repository for data access                   ║");
        System.out.println("║  6. Use @Autowired to inject dependencies                ║");
        System.out.println("║  7. Run and Spring Boot handles everything!              ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        System.out.println("\n🎯 TO START A REAL SPRING BOOT PROJECT:");
        System.out.println("   1. Go to https://start.spring.io");
        System.out.println("   2. Select dependencies (Web, JPA, etc.)");
        System.out.println("   3. Download and unzip");
        System.out.println("   4. Import into your IDE");
        System.out.println("   5. Run the main class!");
    }
}
