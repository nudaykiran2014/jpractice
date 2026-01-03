# Lesson 6: Complete Spring Boot Annotations Guide 📚

A comprehensive reference of ALL Spring Boot annotations with examples!

## Files Overview

| File | Topic |
|------|-------|
| `01_CoreAnnotations.java` | @SpringBootApplication, @Configuration, @ComponentScan |
| `02_StereotypeAnnotations.java` | @Component, @Service, @Repository, @Controller, @RestController |
| `03_DependencyInjectionAnnotations.java` | @Autowired, @Qualifier, @Primary, @Bean, @Value, @Scope |
| `04_WebRestAnnotations.java` | @GetMapping, @PostMapping, @PathVariable, @RequestBody, etc. |
| `05_ValidationAnnotations.java` | @Valid, @NotNull, @Size, @Email, @Pattern, etc. |
| `06_JpaDataAnnotations.java` | @Entity, @Id, @OneToMany, @Transactional, @Query |
| `07_ConfigurationAnnotations.java` | @ConfigurationProperties, @Profile, @Conditional* |
| `08_ExceptionHandlingAnnotations.java` | @ControllerAdvice, @ExceptionHandler |
| `09_SecurityAnnotations.java` | @PreAuthorize, @Secured, @RolesAllowed |
| `10_TestingAnnotations.java` | @SpringBootTest, @WebMvcTest, @MockBean |
| `11_SchedulingAsyncAnnotations.java` | @Scheduled, @Async, @EnableAsync |
| `12_CachingAnnotations.java` | @Cacheable, @CacheEvict, @CachePut |

## How to Use

Each file contains:
- Annotation name and purpose
- Where to use it
- Code examples
- Related annotations

Run any file to see a quick summary:
```bash
cd jpractice
javac -d out spring-learning/lesson06-all-annotations/*.java
java -cp out spring_learning.lesson06_all_annotations._01_CoreAnnotations
```

## Quick Reference Table

### Most Used Annotations

| Annotation | Purpose | Example |
|------------|---------|---------|
| `@SpringBootApplication` | Main app entry | On main class |
| `@RestController` | REST API controller | On controller class |
| `@Service` | Business logic | On service class |
| `@Repository` | Data access | On repository interface |
| `@Autowired` | Inject dependency | On constructor/field |
| `@GetMapping` | Handle GET request | On controller method |
| `@PostMapping` | Handle POST request | On controller method |
| `@PathVariable` | URL path param | Method parameter |
| `@RequestBody` | JSON body → Object | Method parameter |
| `@Valid` | Trigger validation | With @RequestBody |
| `@Transactional` | DB transaction | On service method |

### Dependency Annotations

| Annotation | When to Use |
|------------|-------------|
| `@Autowired` | Inject any bean |
| `@Qualifier("name")` | Choose specific bean |
| `@Primary` | Default bean choice |
| `@Value("${prop}")` | Inject config value |

### Validation Annotations

| Annotation | Validates |
|------------|-----------|
| `@NotNull` | Not null |
| `@NotBlank` | Not null/empty/whitespace |
| `@Size(min, max)` | Length range |
| `@Email` | Email format |
| `@Min` / `@Max` | Number range |

### JPA Annotations

| Annotation | Purpose |
|------------|---------|
| `@Entity` | Database table |
| `@Id` | Primary key |
| `@GeneratedValue` | Auto-increment |
| `@Column` | Customize column |
| `@OneToMany` | 1:N relationship |
| `@ManyToOne` | N:1 relationship |

## Learning Path

1. Start with **Core** (01) and **Stereotype** (02)
2. Master **Dependency Injection** (03)
3. Learn **Web/REST** (04) for APIs
4. Add **Validation** (05) and **JPA** (06)
5. Explore **Exception Handling** (08)
6. Advanced: Security, Testing, Scheduling, Caching

Happy Learning! 🚀
