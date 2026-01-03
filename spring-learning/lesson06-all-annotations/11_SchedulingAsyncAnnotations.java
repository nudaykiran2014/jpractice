package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 11: SCHEDULING & ASYNC ANNOTATIONS                                     ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class _11_SchedulingAsyncAnnotations {
    public static void main(String[] args) {
        System.out.println("=== SCHEDULING & ASYNC ANNOTATIONS ===\n");
        System.out.println("@EnableScheduling → Enable scheduled tasks");
        System.out.println("@Scheduled        → Run method on schedule");
        System.out.println("@EnableAsync      → Enable async processing");
        System.out.println("@Async            → Run method asynchronously");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * SCHEDULING ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Run methods automatically on a schedule
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableScheduling  // Required to enable scheduling
 *     public class SchedulingConfig { }
 *     
 *     @Component
 *     public class ScheduledTasks {
 *         
 *         // Run every 5 seconds
 *         @Scheduled(fixedRate = 5000)
 *         public void runEvery5Seconds() {
 *             System.out.println("Running task at " + LocalDateTime.now());
 *         }
 *         
 *         // Run 5 seconds AFTER previous execution completes
 *         @Scheduled(fixedDelay = 5000)
 *         public void runWithDelay() {
 *             // Good for tasks that shouldn't overlap
 *         }
 *         
 *         // Initial delay before first execution
 *         @Scheduled(fixedRate = 5000, initialDelay = 10000)
 *         public void runWithInitialDelay() {
 *             // Waits 10s, then runs every 5s
 *         }
 *         
 *         // Cron expression - every day at 2:00 AM
 *         @Scheduled(cron = "0 0 2 * * ?")
 *         public void runDaily() {
 *             // Cleanup, reports, etc.
 *         }
 *         
 *         // Cron - weekdays at 9 AM
 *         @Scheduled(cron = "0 0 9 * * MON-FRI")
 *         public void runWeekdays() { }
 *         
 *         // From properties file
 *         @Scheduled(cron = "${app.cleanup.cron}")
 *         public void configuredTask() { }
 *     }
 * 
 * CRON FORMAT: second minute hour day month weekday
 * -------------------------------------------------
 * | Field    | Values         | Special           |
 * |----------|----------------|-------------------|
 * | Second   | 0-59           | , - * /           |
 * | Minute   | 0-59           | , - * /           |
 * | Hour     | 0-23           | , - * /           |
 * | Day      | 1-31           | , - * / ? L W     |
 * | Month    | 1-12 or JAN-DEC| , - * /           |
 * | Weekday  | 0-7 or SUN-SAT | , - * / ? L #     |
 * 
 * Examples:
 * "0 0 * * * *"     = every hour
 * "0 0 8-10 * * *"  = 8, 9, 10 o'clock daily
 * "0 0 0 * * SUN"   = midnight every Sunday
 * "0 0 12 1 * ?"    = noon on first of every month
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ASYNC ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Run methods in a separate thread (non-blocking)
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableAsync  // Required to enable async
 *     public class AsyncConfig {
 *         
 *         @Bean
 *         public Executor taskExecutor() {
 *             ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
 *             executor.setCorePoolSize(2);
 *             executor.setMaxPoolSize(10);
 *             executor.setQueueCapacity(500);
 *             executor.setThreadNamePrefix("Async-");
 *             executor.initialize();
 *             return executor;
 *         }
 *     }
 *     
 *     @Service
 *     public class EmailService {
 *         
 *         // Fire and forget - returns immediately
 *         @Async
 *         public void sendEmailAsync(String to, String message) {
 *             // Runs in separate thread
 *             // Caller doesn't wait
 *         }
 *         
 *         // Return a Future to get result later
 *         @Async
 *         public CompletableFuture<String> sendAndConfirm(String to) {
 *             // Send email...
 *             return CompletableFuture.completedFuture("Sent!");
 *         }
 *         
 *         // Use specific executor
 *         @Async("taskExecutor")
 *         public void sendWithExecutor(String to) { }
 *     }
 *     
 *     // Usage:
 *     @RestController
 *     public class NotificationController {
 *         
 *         @Autowired
 *         private EmailService emailService;
 *         
 *         @PostMapping("/notify")
 *         public String notify() {
 *             emailService.sendEmailAsync("user@example.com", "Hello!");
 *             return "Queued!";  // Returns immediately
 *         }
 *         
 *         @GetMapping("/send-confirm")
 *         public String sendConfirm() throws Exception {
 *             CompletableFuture<String> future = 
 *                 emailService.sendAndConfirm("user@example.com");
 *             return future.get();  // Wait for result
 *         }
 *     }
 * 
 * IMPORTANT: @Async only works when called from OUTSIDE the class!
 * Internal calls within the same class bypass the proxy.
 */
