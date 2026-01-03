package spring_learning.lesson04_ioc_container;

/**
 * LESSON 4: Spring IoC Container (The Magic Behind Spring)
 * 
 * ════════════════════════════════════════════════════════════
 * WHAT IS IoC? (Inversion of Control)
 * ════════════════════════════════════════════════════════════
 * 
 * TRADITIONAL WAY (You're in control):
 * -------------------------------------
 * You: "I need a car"
 * You: *goes to factory*
 * You: *builds engine*
 * You: *builds wheels*
 * You: *assembles car*
 * You: "Finally, I have a car!"
 * 
 * IoC WAY (Framework is in control):
 * ----------------------------------
 * You: "I need a car"
 * Spring: "Here you go! 🚗"
 * You: "Wow, that was easy!"
 * 
 * The CONTROL is INVERTED:
 * - YOU don't create objects
 * - The FRAMEWORK creates them for you
 * 
 * ════════════════════════════════════════════════════════════
 * WHAT IS THE IoC CONTAINER?
 * ════════════════════════════════════════════════════════════
 * 
 * Think of it as a SMART WAREHOUSE:
 * 
 * 1. You REGISTER items in the warehouse
 *    "Hey warehouse, I have EmailSender, SmsSender, NotificationService"
 * 
 * 2. Warehouse UNDERSTANDS dependencies
 *    "NotificationService needs a MessageSender... got it!"
 * 
 * 3. When you ASK for something, warehouse BUILDS it with all parts
 *    "Give me NotificationService"
 *    → Warehouse gives you NotificationService with EmailSender inside!
 * 
 * In Spring terminology:
 * - The warehouse = IoC Container (also called ApplicationContext)
 * - Items in warehouse = Beans
 * - The process of connecting = Dependency Injection
 * 
 * ════════════════════════════════════════════════════════════
 * 
 * This demo SIMULATES what Spring's IoC Container does.
 * (Real Spring uses annotations and is more powerful)
 */
public class IoCContainerDemo {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     LESSON 4: IoC CONTAINER - SPRING'S BRAIN             ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        
        System.out.println("🏭 Creating our simple IoC Container (simulating Spring)...\n");
        
        // Our simple container that mimics Spring
        SimpleContainer container = new SimpleContainer();
        
        // ═══════════════════════════════════════════════════════════
        // STEP 1: REGISTER beans (tell container what classes exist)
        // In Spring Boot, this happens automatically via @Component!
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("📝 Step 1: Registering beans in container");
        container.register("emailSender", EmailSender.class);
        container.register("smsSender", SmsSender.class);
        container.register("userRepository", UserRepository.class);
        System.out.println("   ✓ Registered: emailSender, smsSender, userRepository\n");
        
        // ═══════════════════════════════════════════════════════════
        // STEP 2: GET beans from container
        // Container creates instances and manages them
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("📦 Step 2: Getting beans from container");
        
        MessageSender sender = (MessageSender) container.getBean("emailSender");
        sender.send("Hello from IoC Container!");
        
        System.out.println();
        
        UserRepository repo = (UserRepository) container.getBean("userRepository");
        repo.save("john_doe");
        
        // ═══════════════════════════════════════════════════════════
        // STEP 3: SINGLETON behavior (same instance returned)
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("\n📌 Step 3: Singleton behavior (default in Spring)");
        
        MessageSender sender1 = (MessageSender) container.getBean("emailSender");
        MessageSender sender2 = (MessageSender) container.getBean("emailSender");
        
        System.out.println("   sender1 == sender2 ? " + (sender1 == sender2));
        System.out.println("   → Same instance! Container reuses objects (Singleton)\n");
        
        // ═══════════════════════════════════════════════════════════
        // KEY CONCEPTS SUMMARY
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  SPRING TERMINOLOGY                                      ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  Bean        = An object managed by Spring               ║");
        System.out.println("║  IoC         = Spring controls object creation           ║");
        System.out.println("║  DI          = Spring injects dependencies               ║");
        System.out.println("║  Container   = The 'warehouse' holding all beans         ║");
        System.out.println("║  Singleton   = One instance shared (default scope)       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        System.out.println("\n🔑 In Real Spring, you use ANNOTATIONS:");
        System.out.println("   @Component  → 'Register this class as a bean'");
        System.out.println("   @Service    → 'This is a service bean'");
        System.out.println("   @Repository → 'This is a data access bean'");
        System.out.println("   @Autowired  → 'Inject a bean here'");
        
        System.out.println("\n👉 Next: Spring Boot makes all of this AUTOMATIC!");
    }
}
