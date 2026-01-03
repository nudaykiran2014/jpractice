package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 1: CORE / BOOTSTRAP ANNOTATIONS                                        ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class _01_CoreAnnotations {
    public static void main(String[] args) {
        System.out.println("=== CORE SPRING BOOT ANNOTATIONS ===\n");
        System.out.println("Read the source file for detailed examples!");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @SpringBootApplication
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: The main annotation that marks the entry point of a Spring Boot app.
 *          It combines 3 annotations into one!
 * 
 * COMBINES:
 *   @Configuration           → This class can define @Bean methods
 *   @EnableAutoConfiguration → Enable Spring Boot's auto-configuration
 *   @ComponentScan           → Scan for components in this package and sub-packages
 * 
 * WHERE: Only on your main application class
 * 
 * EXAMPLE:
 * ---------
 * 
 *     @SpringBootApplication
 *     public class MyApplication {
 *         public static void main(String[] args) {
 *             SpringApplication.run(MyApplication.class, args);
 *         }
 *     }
 * 
 *     // With exclusions:
 *     @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
 *     public class MyApplication { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @EnableAutoConfiguration
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Tells Spring Boot to automatically configure beans based on:
 *          - Dependencies in classpath (e.g., spring-data-jpa → configure JPA)
 *          - Properties in application.properties
 * 
 * WHERE: Usually part of @SpringBootApplication, rarely used alone
 * 
 * EXAMPLE:
 * ---------
 * 
 *     @Configuration
 *     @EnableAutoConfiguration
 *     public class MyConfig { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ComponentScan
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Tells Spring WHERE to look for components (@Component, @Service, etc.)
 *          By default, scans the package of the annotated class and all sub-packages
 * 
 * WHERE: Configuration classes
 * 
 * EXAMPLE:
 * ---------
 * 
 *     // Default: scans com.example and all sub-packages
 *     @ComponentScan
 *     public class AppConfig { }
 *     
 *     // Custom: scan specific packages
 *     @ComponentScan(basePackages = {"com.example.services", "com.example.repos"})
 *     public class AppConfig { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Configuration
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Marks a class as a source of bean definitions (replaces XML config)
 *          Methods annotated with @Bean will create beans
 * 
 * WHERE: Classes that define application configuration
 * 
 * EXAMPLE:
 * ---------
 * 
 *     @Configuration
 *     public class AppConfig {
 *         
 *         @Bean
 *         public RestTemplate restTemplate() {
 *             return new RestTemplate();
 *         }
 *         
 *         @Bean
 *         public ObjectMapper objectMapper() {
 *             ObjectMapper mapper = new ObjectMapper();
 *             mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 *             return mapper;
 *         }
 *     }
 */
