package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 13: AOP (Aspect-Oriented Programming) ANNOTATIONS                      ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * AOP allows you to add behavior to existing code without modifying it.
 * Common uses: logging, security, transactions, performance monitoring
 * 
 * Requires: spring-boot-starter-aop
 */
public class _13_AopAnnotations {
    public static void main(String[] args) {
        System.out.println("=== AOP ANNOTATIONS ===\n");
        System.out.println("@EnableAspectJAutoProxy → Enable AOP");
        System.out.println("@Aspect      → Mark class as aspect");
        System.out.println("@Before      → Run before method");
        System.out.println("@After       → Run after method (always)");
        System.out.println("@AfterReturning → Run after successful return");
        System.out.println("@AfterThrowing  → Run after exception");
        System.out.println("@Around      → Wrap method execution");
        System.out.println("@Pointcut    → Define reusable pointcut");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * AOP CONCEPTS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * TERMINOLOGY:
 * - Aspect     = A module containing cross-cutting concerns (logging, security)
 * - Advice     = Action taken (before, after, around)
 * - Pointcut   = Where to apply the advice (which methods)
 * - JoinPoint  = A point during execution (method call)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Aspect and @EnableAspectJAutoProxy
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableAspectJAutoProxy  // Enable AOP
 *     public class AopConfig { }
 *     
 *     @Aspect
 *     @Component
 *     public class LoggingAspect {
 *         // Advice methods go here
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Before
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Execute BEFORE the target method
 * 
 * EXAMPLE:
 * ---------
 *     @Aspect
 *     @Component
 *     public class LoggingAspect {
 *         
 *         // Before any method in UserService
 *         @Before("execution(* com.example.service.UserService.*(..))")
 *         public void logBefore(JoinPoint joinPoint) {
 *             System.out.println("Calling: " + joinPoint.getSignature().getName());
 *         }
 *         
 *         // Before any method with @Loggable annotation
 *         @Before("@annotation(com.example.annotation.Loggable)")
 *         public void logAnnotated(JoinPoint joinPoint) {
 *             System.out.println("Annotated method: " + joinPoint.getSignature());
 *         }
 *         
 *         // Access method arguments
 *         @Before("execution(* com.example.service.*.*(..)) && args(id,..)")
 *         public void logWithArgs(JoinPoint joinPoint, Long id) {
 *             System.out.println("Method called with id: " + id);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @After, @AfterReturning, @AfterThrowing
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE:
 *   @After          → Runs ALWAYS (like finally block)
 *   @AfterReturning → Runs only on SUCCESS
 *   @AfterThrowing  → Runs only on EXCEPTION
 * 
 * EXAMPLE:
 * ---------
 *     @Aspect
 *     @Component
 *     public class AuditAspect {
 *         
 *         // Always runs (success or failure)
 *         @After("execution(* com.example.service.*.*(..))")
 *         public void logAfter(JoinPoint joinPoint) {
 *             System.out.println("Method completed: " + joinPoint.getSignature());
 *         }
 *         
 *         // Access return value
 *         @AfterReturning(
 *             pointcut = "execution(* com.example.service.UserService.find*(..))",
 *             returning = "result"
 *         )
 *         public void logResult(JoinPoint joinPoint, Object result) {
 *             System.out.println("Method returned: " + result);
 *         }
 *         
 *         // Access exception
 *         @AfterThrowing(
 *             pointcut = "execution(* com.example.service.*.*(..))",
 *             throwing = "ex"
 *         )
 *         public void logException(JoinPoint joinPoint, Exception ex) {
 *             System.out.println("Exception in " + joinPoint.getSignature() 
 *                 + ": " + ex.getMessage());
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Around
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Most powerful - wraps the entire method execution
 *          Can modify arguments, return value, or skip execution
 * 
 * EXAMPLE:
 * ---------
 *     @Aspect
 *     @Component
 *     public class PerformanceAspect {
 *         
 *         // Measure execution time
 *         @Around("execution(* com.example.service.*.*(..))")
 *         public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
 *             long start = System.currentTimeMillis();
 *             
 *             Object result = joinPoint.proceed();  // Execute the method
 *             
 *             long duration = System.currentTimeMillis() - start;
 *             System.out.println(joinPoint.getSignature() + " took " + duration + "ms");
 *             
 *             return result;
 *         }
 *         
 *         // Caching example
 *         @Around("@annotation(com.example.annotation.Cached)")
 *         public Object cacheResult(ProceedingJoinPoint joinPoint) throws Throwable {
 *             String key = joinPoint.getSignature().toString();
 *             
 *             // Check cache
 *             Object cached = cache.get(key);
 *             if (cached != null) {
 *                 return cached;
 *             }
 *             
 *             // Execute and cache
 *             Object result = joinPoint.proceed();
 *             cache.put(key, result);
 *             return result;
 *         }
 *         
 *         // Retry on failure
 *         @Around("@annotation(com.example.annotation.Retry)")
 *         public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
 *             int attempts = 3;
 *             Exception lastException = null;
 *             
 *             for (int i = 0; i < attempts; i++) {
 *                 try {
 *                     return joinPoint.proceed();
 *                 } catch (Exception e) {
 *                     lastException = e;
 *                     Thread.sleep(1000);
 *                 }
 *             }
 *             throw lastException;
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Pointcut
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Define reusable pointcut expressions
 * 
 * EXAMPLE:
 * ---------
 *     @Aspect
 *     @Component
 *     public class CommonPointcuts {
 *         
 *         // Reusable pointcuts
 *         @Pointcut("execution(* com.example.service.*.*(..))")
 *         public void serviceLayer() {}
 *         
 *         @Pointcut("execution(* com.example.repository.*.*(..))")
 *         public void repositoryLayer() {}
 *         
 *         @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
 *         public void transactionalMethods() {}
 *         
 *         // Combine pointcuts
 *         @Pointcut("serviceLayer() || repositoryLayer()")
 *         public void dataAccessLayer() {}
 *         
 *         // Use in advice
 *         @Before("serviceLayer()")
 *         public void logService(JoinPoint jp) { }
 *         
 *         @Around("transactionalMethods()")
 *         public Object wrapTransaction(ProceedingJoinPoint pjp) throws Throwable {
 *             return pjp.proceed();
 *         }
 *     }
 * 
 * POINTCUT EXPRESSIONS:
 * ----------------------
 * | Expression              | Matches                           |
 * |-------------------------|-----------------------------------|
 * | execution(* *.*(..))    | Any method                        |
 * | execution(* set*(..))   | Methods starting with "set"       |
 * | execution(* com.ex..*.*(..)) | Any method in com.ex package |
 * | within(com.example.service.*) | Any method in service pkg   |
 * | @annotation(Loggable)   | Methods with @Loggable            |
 * | @within(Service)        | Methods in @Service classes       |
 * | args(Long,..)           | Methods with Long first param     |
 * | bean(userService)       | Methods on userService bean       |
 */
