package spring_learning.lesson05_spring_boot;

/**
 * SPRING BOOT ANNOTATIONS CHEAT SHEET
 * 
 * Print this and keep it handy while learning!
 * 
 * ════════════════════════════════════════════════════════════
 * CORE ANNOTATIONS
 * ════════════════════════════════════════════════════════════
 * 
 * @SpringBootApplication
 * ├── WHERE: Main class
 * ├── WHAT: Marks the entry point of your app
 * └── COMBINES: @Configuration + @EnableAutoConfiguration + @ComponentScan
 * 
 * ════════════════════════════════════════════════════════════
 * STEREOTYPE ANNOTATIONS (Marking beans)
 * ════════════════════════════════════════════════════════════
 * 
 * @Component
 * ├── WHERE: Any class you want Spring to manage
 * └── WHAT: Generic bean marker
 * 
 * @Service
 * ├── WHERE: Business logic classes
 * └── WHAT: Same as @Component but more semantic
 * 
 * @Repository
 * ├── WHERE: Data access classes (DAO)
 * └── WHAT: Adds exception translation for database errors
 * 
 * @Controller
 * ├── WHERE: Web MVC controllers (returns views/HTML)
 * └── WHAT: Handles HTTP requests, returns view names
 * 
 * @RestController
 * ├── WHERE: REST API controllers (returns JSON/XML)
 * └── WHAT: @Controller + @ResponseBody combined
 * 
 * @Configuration
 * ├── WHERE: Classes that define beans
 * └── WHAT: Contains @Bean methods
 * 
 * ════════════════════════════════════════════════════════════
 * DEPENDENCY INJECTION ANNOTATIONS
 * ════════════════════════════════════════════════════════════
 * 
 * @Autowired
 * ├── WHERE: Fields, constructors, or setters
 * ├── WHAT: Tells Spring to inject a bean here
 * └── TIP: Constructor injection is preferred!
 * 
 * @Qualifier("beanName")
 * ├── WHERE: With @Autowired when multiple beans of same type exist
 * └── WHAT: Specifies which bean to inject
 * 
 * @Primary
 * ├── WHERE: On a bean definition
 * └── WHAT: Makes this the default bean if multiple exist
 * 
 * @Value("${property.name}")
 * ├── WHERE: Fields
 * └── WHAT: Injects values from application.properties
 * 
 * ════════════════════════════════════════════════════════════
 * WEB/REST ANNOTATIONS
 * ════════════════════════════════════════════════════════════
 * 
 * @RequestMapping("/path")
 * ├── WHERE: Class or method level
 * └── WHAT: Maps requests to path
 * 
 * @GetMapping("/path")     → Handle GET requests
 * @PostMapping("/path")    → Handle POST requests
 * @PutMapping("/path")     → Handle PUT requests
 * @DeleteMapping("/path")  → Handle DELETE requests
 * @PatchMapping("/path")   → Handle PATCH requests
 * 
 * @PathVariable
 * ├── WHERE: Method parameter
 * ├── WHAT: Extracts value from URL path
 * └── EXAMPLE: @GetMapping("/users/{id}") + @PathVariable Long id
 * 
 * @RequestParam
 * ├── WHERE: Method parameter
 * ├── WHAT: Extracts query parameter
 * └── EXAMPLE: /users?name=John + @RequestParam String name
 * 
 * @RequestBody
 * ├── WHERE: Method parameter
 * ├── WHAT: Converts JSON body to object
 * └── EXAMPLE: POST body → User object
 * 
 * @ResponseBody
 * ├── WHERE: Method or class
 * └── WHAT: Converts return value to JSON
 * 
 * @ResponseStatus(HttpStatus.CREATED)
 * ├── WHERE: Method
 * └── WHAT: Sets HTTP status code
 * 
 * ════════════════════════════════════════════════════════════
 * BEAN SCOPE ANNOTATIONS
 * ════════════════════════════════════════════════════════════
 * 
 * @Scope("singleton")   → One instance (default)
 * @Scope("prototype")   → New instance each time
 * @Scope("request")     → One per HTTP request
 * @Scope("session")     → One per HTTP session
 * 
 * ════════════════════════════════════════════════════════════
 * VALIDATION ANNOTATIONS (on DTOs/entities)
 * ════════════════════════════════════════════════════════════
 * 
 * @Valid              → Trigger validation on object
 * @NotNull            → Field cannot be null
 * @NotEmpty           → String/collection cannot be empty
 * @NotBlank           → String cannot be blank
 * @Size(min=2, max=50) → String/collection size limits
 * @Email              → Must be valid email format
 * @Min(0) / @Max(100) → Numeric range
 * @Pattern(regexp="") → Regex pattern match
 * 
 * ════════════════════════════════════════════════════════════
 * JPA/DATA ANNOTATIONS
 * ════════════════════════════════════════════════════════════
 * 
 * @Entity             → Marks class as JPA entity
 * @Table(name="...")  → Specifies table name
 * @Id                 → Marks primary key field
 * @GeneratedValue     → Auto-generate ID
 * @Column(name="...") → Specifies column name
 * @Transactional      → Wraps method in transaction
 * 
 * ════════════════════════════════════════════════════════════
 * TESTING ANNOTATIONS
 * ════════════════════════════════════════════════════════════
 * 
 * @SpringBootTest     → Full integration test
 * @WebMvcTest         → Test only web layer
 * @DataJpaTest        → Test only JPA layer
 * @MockBean           → Create mock bean
 * 
 */
public class AnnotationsCheatSheet {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     SPRING BOOT ANNOTATIONS CHEAT SHEET                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        
        System.out.println("🏷️  STEREOTYPE (Bean markers):");
        System.out.println("    @Component  → Generic bean");
        System.out.println("    @Service    → Business logic");
        System.out.println("    @Repository → Data access");
        System.out.println("    @Controller → Web MVC");
        System.out.println("    @RestController → REST API\n");
        
        System.out.println("💉 DEPENDENCY INJECTION:");
        System.out.println("    @Autowired  → Inject bean");
        System.out.println("    @Qualifier  → Choose specific bean");
        System.out.println("    @Value      → Inject config value\n");
        
        System.out.println("🌐 WEB/REST:");
        System.out.println("    @GetMapping     → GET request");
        System.out.println("    @PostMapping    → POST request");
        System.out.println("    @PutMapping     → PUT request");
        System.out.println("    @DeleteMapping  → DELETE request");
        System.out.println("    @PathVariable   → URL path param");
        System.out.println("    @RequestParam   → Query param");
        System.out.println("    @RequestBody    → JSON body\n");
        
        System.out.println("✅ VALIDATION:");
        System.out.println("    @Valid     → Trigger validation");
        System.out.println("    @NotNull   → Cannot be null");
        System.out.println("    @NotBlank  → Cannot be blank");
        System.out.println("    @Email     → Valid email");
        System.out.println("    @Size      → Length limits\n");
        
        System.out.println("💾 JPA/DATA:");
        System.out.println("    @Entity        → JPA entity");
        System.out.println("    @Id            → Primary key");
        System.out.println("    @Transactional → Transaction\n");
        
        System.out.println("📖 See the full JavaDoc comments in this file for details!");
    }
}
