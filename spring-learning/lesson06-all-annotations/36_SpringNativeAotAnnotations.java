package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 36: SPRING NATIVE / AOT ANNOTATIONS                                    ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Spring Native compiles apps to native executables using GraalVM.
 * AOT = Ahead-of-Time compilation (vs JIT = Just-in-Time)
 * 
 * Result: Faster startup (milliseconds!), lower memory usage
 */
public class _36_SpringNativeAotAnnotations {
    public static void main(String[] args) {
        System.out.println("=== SPRING NATIVE / AOT ANNOTATIONS ===\n");
        System.out.println("@NativeHint       → Hints for native compilation");
        System.out.println("@TypeHint         → Register types for reflection");
        System.out.println("@ResourceHint     → Include resources");
        System.out.println("@RegisterReflectionForBinding → JSON binding hints");
        System.out.println("@Reflective       → Mark for reflection");
        System.out.println("@ImportRuntimeHints → Runtime hints configuration");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHY NATIVE?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * JVM (Traditional):
 * - Startup: 2-10 seconds
 * - Memory: 200-500 MB
 * - Runtime optimization (gets faster over time)
 * 
 * Native (GraalVM):
 * - Startup: 50-100 milliseconds!
 * - Memory: 50-100 MB
 * - Perfect for: serverless, containers, microservices
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * THE CHALLENGE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Native compilation happens at BUILD time, not runtime.
 * These dynamic Java features need hints:
 * - Reflection
 * - Dynamic proxies
 * - Resource loading
 * - Serialization
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RegisterReflectionForBinding
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Register classes for JSON serialization/deserialization
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @RegisterReflectionForBinding({User.class, Order.class, Address.class})
 *     public class UserController {
 *         
 *         @GetMapping("/users/{id}")
 *         public User getUser(@PathVariable Long id) {
 *             return userService.findById(id);  // User needs reflection for JSON
 *         }
 *         
 *         @PostMapping("/users")
 *         public User createUser(@RequestBody User user) {  // Deserialization
 *             return userService.save(user);
 *         }
 *     }
 *     
 *     // On the DTO itself
 *     @RegisterReflectionForBinding
 *     public class UserDTO {
 *         private String name;
 *         private String email;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Reflective
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Mark methods that need reflection access
 * 
 * EXAMPLE:
 * ---------
 *     public class DynamicService {
 *         
 *         @Reflective
 *         public void processData(Object data) {
 *             // This method is called via reflection
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * RuntimeHintsRegistrar (Advanced)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Programmatic way to register hints
 * 
 * EXAMPLE:
 * ---------
 *     public class MyRuntimeHints implements RuntimeHintsRegistrar {
 *         
 *         @Override
 *         public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
 *             
 *             // Register class for reflection
 *             hints.reflection().registerType(User.class, 
 *                 MemberCategory.PUBLIC_FIELDS,
 *                 MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
 *                 MemberCategory.INVOKE_PUBLIC_METHODS);
 *             
 *             // Register resource files
 *             hints.resources().registerPattern("config/*.json");
 *             hints.resources().registerPattern("templates/*.html");
 *             
 *             // Register proxy
 *             hints.proxies().registerJdkProxy(MyInterface.class);
 *             
 *             // Register serialization
 *             hints.serialization().registerType(User.class);
 *         }
 *     }
 *     
 *     // Apply the hints
 *     @Configuration
 *     @ImportRuntimeHints(MyRuntimeHints.class)
 *     public class AppConfig { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ImportRuntimeHints
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Import RuntimeHintsRegistrar classes
 * 
 * EXAMPLE:
 * ---------
 *     @SpringBootApplication
 *     @ImportRuntimeHints({MyRuntimeHints.class, ThirdPartyHints.class})
 *     public class Application { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @AotProxyHint
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Register interfaces for dynamic proxy generation
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @AotProxyHint(targetClass = MyService.class, proxyFeatures = ProxyBits.CLASS_PROXY)
 *     public class ProxyConfig { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CONDITIONAL BEANS FOR NATIVE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class AppConfig {
 *         
 *         // Only in native image
 *         @Bean
 *         @NativeHint
 *         public NativeOptimizedService nativeService() {
 *             return new NativeOptimizedService();
 *         }
 *         
 *         // Only in JVM mode
 *         @Bean
 *         @ConditionalOnNotAot
 *         public JvmOptimizedService jvmService() {
 *             return new JvmOptimizedService();
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * BUILDING NATIVE IMAGE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Maven:
 *     mvn -Pnative native:compile
 * 
 * Gradle:
 *     ./gradlew nativeCompile
 * 
 * Result:
 *     ./target/my-app              # Native executable!
 *     ./my-app                     # Starts in ~50ms
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * COMMON ISSUES & SOLUTIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * ISSUE: Class not found at runtime
 * SOLUTION: Add @RegisterReflectionForBinding
 * 
 * ISSUE: Resource file not found
 * SOLUTION: Register in RuntimeHintsRegistrar
 *     hints.resources().registerPattern("my-file.json");
 * 
 * ISSUE: Proxy creation fails
 * SOLUTION: Register proxy hint
 *     hints.proxies().registerJdkProxy(MyInterface.class);
 * 
 * TESTING NATIVE:
 * ----------------
 *     @SpringBootTest
 *     @ImportRuntimeHints(MyRuntimeHints.class)
 *     class NativeCompatibilityTest {
 *         
 *         @Test
 *         void contextLoads() {
 *             // Test AOT-processed context
 *         }
 *     }
 * 
 * JVM vs NATIVE:
 * ---------------
 * | Aspect       | JVM              | Native           |
 * |--------------|------------------|------------------|
 * | Startup      | Seconds          | Milliseconds     |
 * | Memory       | Higher           | Much lower       |
 * | Peak perf    | Higher (JIT)     | Lower            |
 * | Build time   | Fast             | Minutes          |
 * | Debugging    | Easy             | Harder           |
 * | Best for     | Long-running     | Serverless       |
 */
