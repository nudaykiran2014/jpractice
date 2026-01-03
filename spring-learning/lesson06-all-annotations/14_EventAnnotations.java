package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 14: EVENT HANDLING ANNOTATIONS                                         ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Spring's event system allows loose coupling between components.
 * Publisher doesn't know about subscribers - they communicate via events.
 */
public class _14_EventAnnotations {
    public static void main(String[] args) {
        System.out.println("=== EVENT HANDLING ANNOTATIONS ===\n");
        System.out.println("@EventListener    → Listen for application events");
        System.out.println("@TransactionalEventListener → Listen after transaction");
        System.out.println("@Async + @EventListener → Async event handling");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @EventListener
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Listen for and handle application events
 * 
 * EXAMPLE:
 * ---------
 *     // 1. Define custom event
 *     public class UserCreatedEvent {
 *         private final User user;
 *         
 *         public UserCreatedEvent(User user) {
 *             this.user = user;
 *         }
 *         
 *         public User getUser() { return user; }
 *     }
 *     
 *     // 2. Publish event
 *     @Service
 *     public class UserService {
 *         
 *         @Autowired
 *         private ApplicationEventPublisher eventPublisher;
 *         
 *         public User createUser(String name) {
 *             User user = userRepository.save(new User(name));
 *             
 *             // Publish event
 *             eventPublisher.publishEvent(new UserCreatedEvent(user));
 *             
 *             return user;
 *         }
 *     }
 *     
 *     // 3. Listen for event
 *     @Component
 *     public class UserEventListener {
 *         
 *         @EventListener
 *         public void handleUserCreated(UserCreatedEvent event) {
 *             System.out.println("User created: " + event.getUser().getName());
 *             // Send welcome email, log audit, etc.
 *         }
 *         
 *         // Multiple events
 *         @EventListener({UserCreatedEvent.class, UserUpdatedEvent.class})
 *         public void handleUserChange(Object event) {
 *             System.out.println("User changed: " + event);
 *         }
 *         
 *         // Conditional listening
 *         @EventListener(condition = "#event.user.role == 'ADMIN'")
 *         public void handleAdminCreated(UserCreatedEvent event) {
 *             System.out.println("Admin created!");
 *         }
 *         
 *         // Return event to trigger another event
 *         @EventListener
 *         public NotificationEvent handleAndTrigger(UserCreatedEvent event) {
 *             return new NotificationEvent("User " + event.getUser().getName());
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * BUILT-IN SPRING EVENTS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class AppEventListener {
 *         
 *         // Application started
 *         @EventListener
 *         public void onStartup(ApplicationReadyEvent event) {
 *             System.out.println("Application is ready!");
 *         }
 *         
 *         // Context refreshed
 *         @EventListener
 *         public void onRefresh(ContextRefreshedEvent event) {
 *             System.out.println("Context refreshed");
 *         }
 *         
 *         // Context closed
 *         @EventListener
 *         public void onShutdown(ContextClosedEvent event) {
 *             System.out.println("Shutting down...");
 *         }
 *         
 *         // Application started (before ready)
 *         @EventListener
 *         public void onStarted(ApplicationStartedEvent event) {
 *             System.out.println("Application started");
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @TransactionalEventListener
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Handle events only after transaction completes
 *          Prevents processing events from rolled-back transactions
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class TransactionalListener {
 *         
 *         // After transaction commits successfully
 *         @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
 *         public void afterCommit(UserCreatedEvent event) {
 *             // Safe to send email - user is definitely saved
 *             emailService.sendWelcome(event.getUser());
 *         }
 *         
 *         // After rollback
 *         @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
 *         public void afterRollback(UserCreatedEvent event) {
 *             log.warn("User creation rolled back");
 *         }
 *         
 *         // After completion (commit or rollback)
 *         @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
 *         public void afterCompletion(UserCreatedEvent event) {
 *             // Cleanup
 *         }
 *         
 *         // Before commit
 *         @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
 *         public void beforeCommit(UserCreatedEvent event) {
 *             // Last chance validation
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ASYNC EVENT LISTENERS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Handle events asynchronously (non-blocking)
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableAsync
 *     public class AsyncConfig { }
 *     
 *     @Component
 *     public class AsyncEventListener {
 *         
 *         @Async
 *         @EventListener
 *         public void handleAsync(UserCreatedEvent event) {
 *             // Runs in separate thread
 *             // Publisher doesn't wait
 *             sendSlowEmail(event.getUser());
 *         }
 *     }
 * 
 * TRANSACTION PHASES:
 * --------------------
 * | Phase            | When Executed                    |
 * |------------------|----------------------------------|
 * | BEFORE_COMMIT    | Before transaction commits       |
 * | AFTER_COMMIT     | After successful commit (default)|
 * | AFTER_ROLLBACK   | After rollback                   |
 * | AFTER_COMPLETION | After commit or rollback         |
 */
