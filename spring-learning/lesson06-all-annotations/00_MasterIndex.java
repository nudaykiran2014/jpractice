package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                    SPRING BOOT ANNOTATIONS - MASTER INDEX                    ║
 * ║                           250+ Annotations Covered                           ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * This is your complete reference guide to Spring Boot annotations.
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * FILE INDEX
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * FUNDAMENTALS (01-06):
 * ----------------------
 * 01_CoreAnnotations.java           @SpringBootApplication, @Configuration, @ComponentScan
 * 02_StereotypeAnnotations.java     @Component, @Service, @Repository, @Controller
 * 03_DependencyInjectionAnnotations @Autowired, @Qualifier, @Primary, @Bean, @Value
 * 04_WebRestAnnotations.java        @GetMapping, @PostMapping, @RequestBody, @PathVariable
 * 05_ValidationAnnotations.java     @Valid, @NotNull, @Size, @Email, @Pattern
 * 06_JpaDataAnnotations.java        @Entity, @Table, @Id, @OneToMany, @Transactional
 * 
 * CONFIGURATION & ERROR HANDLING (07-09):
 * ----------------------------------------
 * 07_ConfigurationAnnotations.java  @ConfigurationProperties, @Profile, @Conditional
 * 08_ExceptionHandlingAnnotations   @ControllerAdvice, @ExceptionHandler
 * 09_SecurityAnnotations.java       @EnableWebSecurity, @PreAuthorize, @Secured
 * 
 * TESTING & PERFORMANCE (10-12):
 * -------------------------------
 * 10_TestingAnnotations.java        @SpringBootTest, @WebMvcTest, @MockBean
 * 11_SchedulingAsyncAnnotations     @Scheduled, @Async, @EnableAsync
 * 12_CachingAnnotations.java        @Cacheable, @CacheEvict, @CachePut
 * 
 * ADVANCED SPRING (13-16):
 * -------------------------
 * 13_AopAnnotations.java            @Aspect, @Before, @After, @Around, @Pointcut
 * 14_EventAnnotations.java          @EventListener, @TransactionalEventListener
 * 15_JsonJacksonAnnotations.java    @JsonProperty, @JsonIgnore, @JsonFormat
 * 16_LombokAnnotations.java         @Data, @Builder, @Slf4j, @RequiredArgsConstructor
 * 
 * MESSAGING & MONITORING (17-20):
 * --------------------------------
 * 17_MessagingAnnotations.java      @KafkaListener, @RabbitListener, @JmsListener
 * 18_ActuatorAnnotations.java       @Endpoint, @ReadOperation, @Timed
 * 19_OpenApiSwaggerAnnotations      @Operation, @ApiResponse, @Schema
 * 20_MiscAnnotations.java           @Retryable, @CircuitBreaker, @FeignClient
 * 
 * REAL-TIME & BATCH (21-24):
 * ---------------------------
 * 21_WebSocketAnnotations.java      @MessageMapping, @SendTo, @SubscribeMapping
 * 22_BatchAnnotations.java          @EnableBatchProcessing, @StepScope, @BeforeStep
 * 23_MongoDbAnnotations.java        @Document, @Field, @Indexed, @DBRef
 * 24_RedisAnnotations.java          @RedisHash, @TimeToLive, RedisTemplate
 * 
 * MODERN APIS (25-27):
 * ---------------------
 * 25_GraphQLAnnotations.java        @QueryMapping, @MutationMapping, @SchemaMapping
 * 26_SpringCloudAnnotations.java    @EnableEurekaClient, @LoadBalanced, @RefreshScope
 * 27_HateoasAnnotations.java        EntityModel, CollectionModel, @Relation
 * 
 * UTILITIES (28-30):
 * -------------------
 * 28_MapStructAnnotations.java      @Mapper, @Mapping, @MappingTarget
 * 29_R2dbcReactiveAnnotations       @Table (R2DBC), Mono, Flux
 * 30_SessionMigrationAnnotations    @EnableRedisHttpSession, Flyway, Liquibase
 * 
 * SEARCH & INTEGRATION (31-33):
 * ------------------------------
 * 31_ElasticsearchAnnotations.java  @Document (ES), @Field, @Query
 * 32_QueryDslAnnotations.java       @QueryProjection, BooleanBuilder
 * 33_SpringIntegrationAnnotations   @MessagingGateway, @ServiceActivator, @Router
 * 
 * ADVANCED TOPICS (34-40):
 * -------------------------
 * 34_SpringDataRestAnnotations      @RepositoryRestResource, @Projection
 * 35_TestcontainersAnnotations      @Testcontainers, @Container, @ServiceConnection
 * 36_SpringNativeAotAnnotations     @RegisterReflectionForBinding, RuntimeHints
 * 37_JpaAuditingAnnotations.java    @CreatedDate, @LastModifiedDate, @Version
 * 38_ObservabilityAnnotations       @Observed, @Timed, @Counted, @NewSpan
 * 39_AuthorizationServerAnnotations OAuth2, @AuthenticationPrincipal, JWT
 * 40_SpringShellAnnotations.java    @ShellComponent, @ShellMethod, @ShellOption
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * QUICK REFERENCE - MOST USED ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STARTING AN APP:
 *   @SpringBootApplication          Entry point
 * 
 * CREATING BEANS:
 *   @Component                      Generic bean
 *   @Service                        Business logic
 *   @Repository                     Data access
 *   @Controller / @RestController   Web endpoints
 *   @Configuration + @Bean          Manual bean creation
 * 
 * INJECTING DEPENDENCIES:
 *   @Autowired                      Inject dependency
 *   @Qualifier("name")              Specify which bean
 *   @Value("${prop}")               Inject property
 * 
 * REST ENDPOINTS:
 *   @GetMapping("/path")            GET request
 *   @PostMapping("/path")           POST request
 *   @PathVariable                   URL path variable
 *   @RequestParam                   Query parameter
 *   @RequestBody                    JSON body
 * 
 * DATABASE:
 *   @Entity                         JPA entity
 *   @Id + @GeneratedValue           Primary key
 *   @Transactional                  Transaction boundary
 * 
 * VALIDATION:
 *   @Valid                          Trigger validation
 *   @NotNull, @NotBlank, @Size      Constraints
 * 
 * TESTING:
 *   @SpringBootTest                 Full integration test
 *   @WebMvcTest                     Controller test
 *   @MockBean                       Mock a dependency
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * LEARNING PATH RECOMMENDATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * BEGINNER:     Files 01-06 (Core, DI, Web, Validation, JPA)
 * INTERMEDIATE: Files 07-12 (Config, Security, Testing, Caching)
 * ADVANCED:     Files 13-20 (AOP, Events, Messaging, Monitoring)
 * SPECIALIST:   Files 21-40 (NoSQL, Cloud, GraphQL, Native, etc.)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 */
public class _00_MasterIndex {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║     SPRING BOOT ANNOTATIONS - COMPLETE REFERENCE GUIDE      ║");
        System.out.println("║                    40 Files • 250+ Annotations              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Browse the files in this package to learn about:");
        System.out.println("  • Core Spring Boot annotations");
        System.out.println("  • Web/REST development");
        System.out.println("  • Data access (JPA, MongoDB, Redis, Elasticsearch)");
        System.out.println("  • Security and OAuth2");
        System.out.println("  • Testing strategies");
        System.out.println("  • Microservices and Spring Cloud");
        System.out.println("  • Messaging (Kafka, RabbitMQ)");
        System.out.println("  • Modern APIs (GraphQL, WebSocket)");
        System.out.println("  • And much more!");
    }
}
