package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 35: TESTCONTAINERS ANNOTATIONS                                         ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Testcontainers runs real databases/services in Docker for integration tests.
 * 
 * Requires: spring-boot-testcontainers, testcontainers
 */
public class _35_TestcontainersAnnotations {
    public static void main(String[] args) {
        System.out.println("=== TESTCONTAINERS ANNOTATIONS ===\n");
        System.out.println("@Testcontainers      → Enable testcontainers");
        System.out.println("@Container           → Mark container field");
        System.out.println("@ServiceConnection   → Auto-configure connection");
        System.out.println("@DynamicPropertySource → Set dynamic properties");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHY TESTCONTAINERS?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PROBLEM: H2 in-memory DB doesn't behave like real PostgreSQL/MySQL
 * SOLUTION: Run real database in Docker container during tests!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Testcontainers + @Container
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Manage container lifecycle automatically
 * 
 * EXAMPLE:
 * ---------
 *     @SpringBootTest
 *     @Testcontainers
 *     class UserRepositoryTest {
 *         
 *         @Container
 *         static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
 *             .withDatabaseName("testdb")
 *             .withUsername("test")
 *             .withPassword("test");
 *         
 *         @DynamicPropertySource
 *         static void configureProperties(DynamicPropertyRegistry registry) {
 *             registry.add("spring.datasource.url", postgres::getJdbcUrl);
 *             registry.add("spring.datasource.username", postgres::getUsername);
 *             registry.add("spring.datasource.password", postgres::getPassword);
 *         }
 *         
 *         @Autowired
 *         private UserRepository userRepository;
 *         
 *         @Test
 *         void shouldSaveUser() {
 *             User user = new User("John", "john@example.com");
 *             User saved = userRepository.save(user);
 *             assertThat(saved.getId()).isNotNull();
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ServiceConnection (Spring Boot 3.1+)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Auto-configure connection properties (no @DynamicPropertySource needed!)
 * 
 * EXAMPLE:
 * ---------
 *     @SpringBootTest
 *     @Testcontainers
 *     class UserServiceTest {
 *         
 *         @Container
 *         @ServiceConnection  // Auto-configures datasource!
 *         static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
 *         
 *         @Autowired
 *         private UserService userService;
 *         
 *         @Test
 *         void testWithRealDatabase() {
 *             // Uses real PostgreSQL!
 *         }
 *     }
 *     
 *     // Multiple containers
 *     @SpringBootTest
 *     @Testcontainers
 *     class IntegrationTest {
 *         
 *         @Container
 *         @ServiceConnection
 *         static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
 *         
 *         @Container
 *         @ServiceConnection
 *         static RedisContainer redis = new RedisContainer("redis:7");
 *         
 *         @Container
 *         @ServiceConnection
 *         static KafkaContainer kafka = new KafkaContainer("confluentinc/cp-kafka:7.4.0");
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * COMMON CONTAINERS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     // PostgreSQL
 *     @Container
 *     @ServiceConnection
 *     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
 *     
 *     // MySQL
 *     @Container
 *     @ServiceConnection
 *     static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8");
 *     
 *     // MongoDB
 *     @Container
 *     @ServiceConnection
 *     static MongoDBContainer mongo = new MongoDBContainer("mongo:6");
 *     
 *     // Redis
 *     @Container
 *     @ServiceConnection
 *     static RedisContainer redis = new RedisContainer("redis:7");
 *     
 *     // Kafka
 *     @Container
 *     @ServiceConnection
 *     static KafkaContainer kafka = new KafkaContainer(
 *         DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));
 *     
 *     // Elasticsearch
 *     @Container
 *     @ServiceConnection
 *     static ElasticsearchContainer elastic = new ElasticsearchContainer(
 *         "docker.elastic.co/elasticsearch/elasticsearch:8.9.0");
 *     
 *     // RabbitMQ
 *     @Container
 *     @ServiceConnection
 *     static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.12");
 *     
 *     // LocalStack (AWS services)
 *     @Container
 *     static LocalStackContainer localstack = new LocalStackContainer(
 *         DockerImageName.parse("localstack/localstack:2.2"))
 *         .withServices(LocalStackContainer.Service.S3, LocalStackContainer.Service.SQS);
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * SHARED CONTAINER (Faster Tests)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Reuse container across multiple test classes
 * 
 * EXAMPLE:
 * ---------
 *     // Abstract base class
 *     @SpringBootTest
 *     @Testcontainers
 *     public abstract class AbstractIntegrationTest {
 *         
 *         @Container
 *         @ServiceConnection
 *         static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
 *             .withReuse(true);  // Reuse container!
 *     }
 *     
 *     // Test classes extend base
 *     class UserServiceTest extends AbstractIntegrationTest {
 *         @Test void test1() { }
 *     }
 *     
 *     class OrderServiceTest extends AbstractIntegrationTest {
 *         @Test void test2() { }  // Same container!
 *     }
 *     
 *     // Enable reuse in ~/.testcontainers.properties:
 *     // testcontainers.reuse.enable=true
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CUSTOM CONTAINER INITIALIZATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Container
 *     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
 *         .withDatabaseName("myapp")
 *         .withUsername("user")
 *         .withPassword("password")
 *         .withInitScript("init.sql")  // Run SQL on startup
 *         .withCopyFileToContainer(
 *             MountableFile.forClasspathResource("data.csv"),
 *             "/tmp/data.csv"
 *         )
 *         .withEnv("POSTGRES_INITDB_ARGS", "--encoding=UTF8");
 *     
 *     // Wait for container to be ready
 *     @Container
 *     static GenericContainer<?> custom = new GenericContainer<>("myimage:latest")
 *         .withExposedPorts(8080)
 *         .waitingFor(Wait.forHttp("/health").forStatusCode(200));
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @DataJpaTest with Testcontainers
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @DataJpaTest
 *     @Testcontainers
 *     @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 *     class UserRepositoryTest {
 *         
 *         @Container
 *         @ServiceConnection
 *         static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
 *         
 *         @Autowired
 *         private UserRepository userRepository;
 *         
 *         @Test
 *         void testNativeQuery() {
 *             // Real PostgreSQL-specific features work!
 *         }
 *     }
 * 
 * DEPENDENCIES:
 * --------------
 *     <dependency>
 *         <groupId>org.springframework.boot</groupId>
 *         <artifactId>spring-boot-testcontainers</artifactId>
 *         <scope>test</scope>
 *     </dependency>
 *     <dependency>
 *         <groupId>org.testcontainers</groupId>
 *         <artifactId>postgresql</artifactId>
 *         <scope>test</scope>
 *     </dependency>
 */
