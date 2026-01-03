package spring_learning.lesson03_dependency_injection;

import spring_learning.lesson03_dependency_injection.solution.*;

/**
 * LESSON 3: Dependency Injection - THE CORE OF SPRING
 * 
 * ════════════════════════════════════════════════════════════
 * ANALOGY: Think of a Restaurant
 * ════════════════════════════════════════════════════════════
 * 
 * WITHOUT Dependency Injection (Bad):
 * - The chef goes to the farm to get vegetables
 * - The chef goes to the butcher to get meat
 * - The chef builds his own oven
 * - The chef makes his own plates
 * → Chef is doing EVERYTHING! Can't focus on cooking!
 * 
 * WITH Dependency Injection (Good):
 * - Vegetables are DELIVERED to the chef
 * - Meat is DELIVERED to the chef
 * - Oven is already INSTALLED in the kitchen
 * - Plates are already PROVIDED
 * → Chef just COOKS! Dependencies are "injected"!
 * 
 * ════════════════════════════════════════════════════════════
 * 
 * In Spring:
 * - Your classes are like the chef
 * - Spring is like the restaurant manager
 * - Spring "injects" all the dependencies your classes need
 * - Your classes just do their job!
 */
public class DependencyInjectionDemo {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     LESSON 3: DEPENDENCY INJECTION                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        
        // ═══════════════════════════════════════════════════════════
        // MANUAL Dependency Injection (what YOU do without Spring)
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("📌 Manual Dependency Injection:\n");
        
        // We CREATE the dependency
        MessageSender emailSender = new EmailSender();
        
        // We INJECT it into the service
        NotificationService serviceWithEmail = new NotificationService(emailSender);
        
        // Now the service uses email
        serviceWithEmail.notifyUser("user123", "Your order is confirmed!");
        
        System.out.println();
        
        // ═══════════════════════════════════════════════════════════
        // SWITCH to SMS - just inject a different implementation!
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("📌 Switching to SMS (same service, different sender):\n");
        
        MessageSender smsSender = new SmsSender();
        NotificationService serviceWithSms = new NotificationService(smsSender);
        serviceWithSms.notifyUser("user456", "Your package is out for delivery!");
        
        System.out.println();
        
        // ═══════════════════════════════════════════════════════════
        // SWITCH to Push Notifications - so flexible!
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("📌 Switching to Push Notifications:\n");
        
        MessageSender pushSender = new PushNotificationSender();
        NotificationService serviceWithPush = new NotificationService(pushSender);
        serviceWithPush.notifyUser("user789", "Flash sale starting now!");
        
        // ═══════════════════════════════════════════════════════════
        // KEY INSIGHT
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  KEY INSIGHT                                             ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  NotificationService NEVER changed!                      ║");
        System.out.println("║  We just injected different MessageSenders.              ║");
        System.out.println("║                                                          ║");
        System.out.println("║  This is the POWER of Dependency Injection:              ║");
        System.out.println("║  • Loose coupling (classes don't depend on specifics)    ║");
        System.out.println("║  • Easy testing (inject mock objects)                    ║");
        System.out.println("║  • Flexible (swap implementations without code change)   ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        System.out.println("\n🤔 BUT... creating and wiring objects manually is tedious!");
        System.out.println("   What if we have 100 services with 500 dependencies?");
        System.out.println("\n💡 SOLUTION: Let Spring do it automatically!");
        System.out.println("   → Next lesson: Spring IoC Container");
    }
}
