package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 3: DEPENDENCY INJECTION ANNOTATIONS                                    ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class _03_DependencyInjectionAnnotations {
    public static void main(String[] args) {
        System.out.println("=== DEPENDENCY INJECTION ANNOTATIONS ===\n");
        System.out.println("@Autowired  → Inject a bean automatically");
        System.out.println("@Qualifier  → Choose which bean to inject");
        System.out.println("@Primary    → Default bean when multiple exist");
        System.out.println("@Bean       → Define a bean in @Configuration");
        System.out.println("@Value      → Inject property values");
        System.out.println("@Lazy       → Delay bean creation");
        System.out.println("@Scope      → Bean lifecycle (singleton/prototype)");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Autowired
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Tells Spring to automatically inject a dependency
 * 
 * WHERE: Constructors (BEST), fields, or setter methods
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class OrderService {
 *         
 *         // ✅ BEST: Constructor injection (recommended)
 *         private final UserRepository userRepository;
 *         private final PaymentService paymentService;
 *         
 *         @Autowired  // Optional on single constructor in Spring 4.3+
 *         public OrderService(UserRepository userRepository, 
 *                            PaymentService paymentService) {
 *             this.userRepository = userRepository;
 *             this.paymentService = paymentService;
 *         }
 *         
 *         // ⚠️ OK: Field injection (harder to test)
 *         @Autowired
 *         private EmailService emailService;
 *         
 *         // ⚠️ OK: Setter injection
 *         private NotificationService notificationService;
 *         
 *         @Autowired
 *         public void setNotificationService(NotificationService service) {
 *             this.notificationService = service;
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Qualifier
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: When multiple beans of same type exist, specifies WHICH one to inject
 * 
 * WHERE: With @Autowired when you have multiple implementations
 * 
 * EXAMPLE:
 * ---------
 *     // Two implementations of MessageSender:
 *     @Component("email")
 *     public class EmailSender implements MessageSender { }
 *     
 *     @Component("sms")
 *     public class SmsSender implements MessageSender { }
 *     
 *     // In the service, specify which one:
 *     @Service
 *     public class NotificationService {
 *         
 *         @Autowired
 *         @Qualifier("email")  // Inject EmailSender specifically
 *         private MessageSender messageSender;
 *         
 *         // Or with constructor:
 *         public NotificationService(@Qualifier("sms") MessageSender sender) {
 *             this.messageSender = sender;
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Primary
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Marks a bean as the DEFAULT choice when multiple beans exist
 * 
 * WHERE: On bean definition (class or @Bean method)
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     @Primary  // This will be injected by default
 *     public class EmailSender implements MessageSender { }
 *     
 *     @Component
 *     public class SmsSender implements MessageSender { }
 *     
 *     @Service
 *     public class NotificationService {
 *         @Autowired
 *         private MessageSender sender;  // EmailSender injected (it's @Primary)
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Bean
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Defines a bean explicitly in a @Configuration class
 *          Use when you need to configure third-party classes
 * 
 * WHERE: Methods inside @Configuration classes
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class AppConfig {
 *         
 *         @Bean
 *         public RestTemplate restTemplate() {
 *             return new RestTemplate();
 *         }
 *         
 *         @Bean("customMapper")  // Custom bean name
 *         public ObjectMapper objectMapper() {
 *             return new ObjectMapper()
 *                 .setSerializationInclusion(JsonInclude.Include.NON_NULL);
 *         }
 *         
 *         // Bean that depends on another bean
 *         @Bean
 *         public UserClient userClient(RestTemplate restTemplate) {
 *             return new UserClient(restTemplate, "https://api.example.com");
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Value
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Injects values from properties files or environment variables
 * 
 * WHERE: Fields, constructor parameters, or method parameters
 * 
 * EXAMPLE:
 * ---------
 *     // application.properties:
 *     // app.name=MyApplication
 *     // app.timeout=30
 *     
 *     @Service
 *     public class AppService {
 *         
 *         @Value("${app.name}")
 *         private String appName;
 *         
 *         @Value("${app.timeout:30}")  // With default value
 *         private int timeout;
 *         
 *         @Value("${DATABASE_URL:jdbc:h2:mem:test}")  // Environment variable
 *         private String databaseUrl;
 *         
 *         // In constructor
 *         public AppService(@Value("${app.name}") String appName) {
 *             this.appName = appName;
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Lazy
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Delays bean creation until it's first accessed
 * 
 * WHERE: On @Component classes or @Bean methods
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     @Lazy  // Created only when first used
 *     public class ExpensiveService {
 *         public ExpensiveService() {
 *             System.out.println("Creating ExpensiveService...");
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Scope
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Defines the lifecycle/scope of a bean
 * 
 * SCOPES:
 *   singleton (default) → One instance for entire application
 *   prototype           → New instance every time requested
 *   request             → One instance per HTTP request
 *   session             → One instance per HTTP session
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     @Scope("singleton")  // Default - one instance
 *     public class SingletonService { }
 *     
 *     @Component
 *     @Scope("prototype")  // New instance each time
 *     public class PrototypeService { }
 */
