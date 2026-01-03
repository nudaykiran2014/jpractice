package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 7: CONFIGURATION / PROPERTIES ANNOTATIONS                              ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class _07_ConfigurationAnnotations {
    public static void main(String[] args) {
        System.out.println("=== CONFIGURATION ANNOTATIONS ===\n");
        System.out.println("@ConfigurationProperties → Bind properties to object");
        System.out.println("@Profile                 → Activate for specific profile");
        System.out.println("@ConditionalOnProperty   → Create bean if property exists");
        System.out.println("@ConditionalOnClass      → Create bean if class on classpath");
        System.out.println("@ConditionalOnBean       → Create bean if another bean exists");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ConfigurationProperties
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Bind external configuration to a Java object (type-safe)
 * 
 * EXAMPLE:
 * ---------
 *     # application.yml
 *     app:
 *       name: MyApp
 *       api:
 *         base-url: https://api.example.com
 *         timeout: 30
 *         retry-count: 3
 *     
 *     @Component
 *     @ConfigurationProperties(prefix = "app")
 *     public class AppProperties {
 *         private String name;
 *         private Api api = new Api();
 *         
 *         public static class Api {
 *             private String baseUrl;
 *             private int timeout;
 *             private int retryCount;
 *             // getters and setters
 *         }
 *         // getters and setters
 *     }
 *     
 *     // Usage:
 *     @Service
 *     public class ApiService {
 *         @Autowired
 *         private AppProperties props;
 *         
 *         public void call() {
 *             String url = props.getApi().getBaseUrl();
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Profile
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Activate beans only for specific profiles (dev, test, prod)
 * 
 * WHERE: On @Component classes or @Bean methods
 * 
 * EXAMPLE:
 * ---------
 *     // Active only in development
 *     @Component
 *     @Profile("dev")
 *     public class DevDataLoader implements CommandLineRunner {
 *         @Override
 *         public void run(String... args) {
 *             // Load test data
 *         }
 *     }
 *     
 *     // Active in multiple profiles
 *     @Component
 *     @Profile({"dev", "test"})
 *     public class MockEmailService implements EmailService { }
 *     
 *     // Active when NOT in production
 *     @Component
 *     @Profile("!prod")
 *     public class DebugService { }
 *     
 *     // Different beans for different profiles
 *     @Configuration
 *     public class DataSourceConfig {
 *         
 *         @Bean
 *         @Profile("dev")
 *         public DataSource devDataSource() {
 *             return new EmbeddedDatabaseBuilder()
 *                 .setType(EmbeddedDatabaseType.H2)
 *                 .build();
 *         }
 *         
 *         @Bean
 *         @Profile("prod")
 *         public DataSource prodDataSource() {
 *             return DataSourceBuilder.create()
 *                 .url("jdbc:postgresql://prod-server/db")
 *                 .build();
 *         }
 *     }
 *     
 *     // Activate profile:
 *     // application.properties: spring.profiles.active=dev
 *     // Command line: java -jar app.jar --spring.profiles.active=prod
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CONDITIONAL ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Create beans conditionally based on various factors
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class ConditionalConfig {
 *         
 *         // Only if property exists and equals true
 *         @Bean
 *         @ConditionalOnProperty(name = "feature.enabled", havingValue = "true")
 *         public FeatureService featureService() {
 *             return new FeatureService();
 *         }
 *         
 *         // Only if class is on classpath
 *         @Bean
 *         @ConditionalOnClass(name = "com.mongodb.MongoClient")
 *         public MongoService mongoService() {
 *             return new MongoService();
 *         }
 *         
 *         // Only if bean doesn't already exist
 *         @Bean
 *         @ConditionalOnMissingBean(CacheService.class)
 *         public CacheService defaultCacheService() {
 *             return new InMemoryCacheService();
 *         }
 *         
 *         // Only if bean exists
 *         @Bean
 *         @ConditionalOnBean(DataSource.class)
 *         public JdbcService jdbcService() {
 *             return new JdbcService();
 *         }
 *     }
 * 
 * CONDITIONAL ANNOTATIONS SUMMARY:
 * ---------------------------------
 * | Annotation                | Condition                    |
 * |---------------------------|------------------------------|
 * | @ConditionalOnProperty    | Property exists/matches      |
 * | @ConditionalOnClass       | Class on classpath           |
 * | @ConditionalOnMissingClass| Class NOT on classpath       |
 * | @ConditionalOnBean        | Bean exists                  |
 * | @ConditionalOnMissingBean | Bean does NOT exist          |
 * | @ConditionalOnWebApplication| Running as web app         |
 * | @ConditionalOnExpression  | SpEL expression is true      |
 */
