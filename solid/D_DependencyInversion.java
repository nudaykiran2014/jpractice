/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  D - DEPENDENCY INVERSION PRINCIPLE (DIP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * DEFINITION:
 * ───────────
 *     1. High-level modules should not depend on low-level modules. 
 *        Both should depend on ABSTRACTIONS.
 *     
 *     2. Abstractions should not depend on details.
 *        Details should depend on abstractions.
 *     
 *     In simple terms: Depend on INTERFACES, not on CONCRETE classes!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * STORY TIME 📖
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Imagine you're building a LAMP...
 * 
 * BAD Design (Violating DIP):
 * ───────────────────────────
 *     class Lamp {
 *         private PhilipsBulb bulb;  // Depends on SPECIFIC bulb! ❌
 *         
 *         public Lamp() {
 *             this.bulb = new PhilipsBulb();  // Hard-coded! ❌
 *         }
 *         
 *         void turnOn() {
 *             bulb.illuminate();
 *         }
 *     }
 *     
 *     Problem:
 *     ────────
 *     - Want to use SamsungBulb? → Change Lamp code!
 *     - Want to use LEDBulb? → Change Lamp code!
 *     - Lamp is TIGHTLY COUPLED to PhilipsBulb!
 *     - Can't test Lamp without real PhilipsBulb!
 * 
 * 
 * GOOD Design (Following DIP):
 * ────────────────────────────
 *     interface Bulb {  // Abstraction!
 *         void illuminate();
 *     }
 *     
 *     class PhilipsBulb implements Bulb { ... }
 *     class SamsungBulb implements Bulb { ... }
 *     class LEDBulb implements Bulb { ... }
 *     
 *     class Lamp {
 *         private Bulb bulb;  // Depends on INTERFACE! ✅
 *         
 *         public Lamp(Bulb bulb) {  // INJECTED! ✅
 *             this.bulb = bulb;
 *         }
 *         
 *         void turnOn() {
 *             bulb.illuminate();
 *         }
 *     }
 *     
 *     Now:
 *     ────
 *     - Use any bulb: new Lamp(new LEDBulb());
 *     - Lamp doesn't know/care which bulb!
 *     - Easy to test with mock bulb!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * THE PROBLEM (Without DIP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     HIGH-LEVEL MODULE         LOW-LEVEL MODULE
 *     ─────────────────         ────────────────
 *     OrderService      →       MySQLDatabase
 *     
 *     class OrderService {
 *         private MySQLDatabase db = new MySQLDatabase();  // ❌
 *         
 *         void saveOrder(Order order) {
 *             db.insert(order);  // Tightly coupled to MySQL!
 *         }
 *     }
 *     
 *     Problems:
 *     ─────────
 *     1. Switch to PostgreSQL? → Change OrderService!
 *     2. Can't unit test without real MySQL!
 *     3. High-level business logic depends on low-level database!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * THE SOLUTION (With DIP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     HIGH-LEVEL MODULE         ABSTRACTION          LOW-LEVEL MODULE
 *     ─────────────────         ───────────          ────────────────
 *     OrderService      →       Database      ←      MySQLDatabase
 *                                   ↑                PostgreSQLDatabase
 *                                   └────────────────MongoDatabase
 *     
 *     Both depend on ABSTRACTION (Database interface)!
 * 
 */

package solid;

import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// BAD EXAMPLE - Violating DIP ❌
// ═══════════════════════════════════════════════════════════════════════════════

// Low-level module
class MySQLDatabase {
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }
}

// High-level module depends on low-level module directly! ❌
class BadOrderService {
    private MySQLDatabase database = new MySQLDatabase();  // Hard-coded! ❌
    
    public void createOrder(String orderData) {
        // Business logic...
        database.save(orderData);  // Tightly coupled!
    }
}
// Problem: Can't change database without changing OrderService!


// ═══════════════════════════════════════════════════════════════════════════════
// GOOD EXAMPLE - Following DIP ✅
// ═══════════════════════════════════════════════════════════════════════════════

// Step 1: Create abstraction (interface)
interface Database {
    void save(String data);
    String findById(String id);
}

// Step 2: Low-level modules implement the abstraction
class MySQLDatabaseImpl implements Database {
    @Override
    public void save(String data) {
        System.out.println("💾 MySQL: Saving → " + data);
    }
    
    @Override
    public String findById(String id) {
        return "MySQL: Data for " + id;
    }
}

class PostgreSQLDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("💾 PostgreSQL: Saving → " + data);
    }
    
    @Override
    public String findById(String id) {
        return "PostgreSQL: Data for " + id;
    }
}

class MongoDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("💾 MongoDB: Saving → " + data);
    }
    
    @Override
    public String findById(String id) {
        return "MongoDB: Data for " + id;
    }
}

// For testing!
class MockDatabase implements Database {
    private List<String> savedData = new ArrayList<>();
    
    @Override
    public void save(String data) {
        savedData.add(data);
        System.out.println("🧪 Mock: Saved → " + data);
    }
    
    @Override
    public String findById(String id) {
        return "Mock: Data for " + id;
    }
    
    public List<String> getSavedData() {
        return savedData;
    }
}

// Step 3: High-level module depends on abstraction
class OrderService {
    private Database database;  // Depends on INTERFACE! ✅
    
    // Constructor Injection ✅
    public OrderService(Database database) {
        this.database = database;
    }
    
    public void createOrder(String orderData) {
        System.out.println("📦 Creating order...");
        // Business logic here...
        database.save(orderData);  // Works with ANY database!
        System.out.println("✅ Order created!\n");
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Notification System
// ═══════════════════════════════════════════════════════════════════════════════

// BAD: Direct dependency ❌
class BadNotificationService {
    private EmailSender emailSender = new EmailSender();  // Hard-coded! ❌
    
    public void notify(String message) {
        emailSender.send(message);  // Can ONLY send email!
    }
}

class EmailSender {
    public void send(String message) {
        System.out.println("📧 Sending email: " + message);
    }
}


// GOOD: Dependency on abstraction ✅
interface NotificationSender {
    void send(String message);
}

class EmailNotificationSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("📧 Email: " + message);
    }
}

class SMSNotificationSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("📱 SMS: " + message);
    }
}

class PushNotificationSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("🔔 Push: " + message);
    }
}

class SlackNotificationSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("💬 Slack: " + message);
    }
}

class NotificationService {
    private NotificationSender sender;  // Depends on interface! ✅
    
    public NotificationService(NotificationSender sender) {
        this.sender = sender;
    }
    
    public void notifyUser(String message) {
        sender.send(message);
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Payment Processing
// ═══════════════════════════════════════════════════════════════════════════════

interface PaymentGateway {
    void processPayment(double amount);
    boolean isAvailable();
}

class StripeGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("💳 Stripe: Processing $" + amount);
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}

class PayPalGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("🅿️ PayPal: Processing $" + amount);
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}

class RazorpayGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("💰 Razorpay: Processing ₹" + (amount * 83));
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}

class PaymentService {
    private PaymentGateway gateway;  // Depends on interface! ✅
    
    public PaymentService(PaymentGateway gateway) {
        this.gateway = gateway;
    }
    
    public void checkout(double amount) {
        if (gateway.isAvailable()) {
            gateway.processPayment(amount);
            System.out.println("✅ Payment successful!\n");
        } else {
            System.out.println("❌ Payment gateway unavailable!\n");
        }
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// DEPENDENCY INJECTION TYPES
// ═══════════════════════════════════════════════════════════════════════════════

interface Logger {
    void log(String message);
}

class ConsoleLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}

// 1. Constructor Injection (Recommended!) ✅
class ServiceWithConstructorInjection {
    private Logger logger;
    
    public ServiceWithConstructorInjection(Logger logger) {
        this.logger = logger;
    }
    
    public void doSomething() {
        logger.log("Doing something with constructor injection");
    }
}

// 2. Setter Injection
class ServiceWithSetterInjection {
    private Logger logger;
    
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    
    public void doSomething() {
        if (logger != null) {
            logger.log("Doing something with setter injection");
        }
    }
}

// 3. Interface Injection (Less common)
interface LoggerInjector {
    void injectLogger(Logger logger);
}

class ServiceWithInterfaceInjection implements LoggerInjector {
    private Logger logger;
    
    @Override
    public void injectLogger(Logger logger) {
        this.logger = logger;
    }
    
    public void doSomething() {
        if (logger != null) {
            logger.log("Doing something with interface injection");
        }
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════

public class D_DependencyInversion {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  DEPENDENCY INVERSION PRINCIPLE (DIP)");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        // Database Example
        System.out.println("💾 DATABASE EXAMPLE:");
        System.out.println("─────────────────────────────────────────────");
        
        // Same OrderService, different databases!
        OrderService mysqlService = new OrderService(new MySQLDatabaseImpl());
        mysqlService.createOrder("Order #1001");
        
        OrderService mongoService = new OrderService(new MongoDatabase());
        mongoService.createOrder("Order #1002");
        
        // For testing - use mock!
        OrderService testService = new OrderService(new MockDatabase());
        testService.createOrder("Test Order");
        
        // Notification Example
        System.out.println("🔔 NOTIFICATION EXAMPLE:");
        System.out.println("─────────────────────────────────────────────");
        
        // Same service, different senders!
        NotificationService emailNotifier = new NotificationService(new EmailNotificationSender());
        emailNotifier.notifyUser("Your order is shipped!");
        
        NotificationService smsNotifier = new NotificationService(new SMSNotificationSender());
        smsNotifier.notifyUser("Your OTP is 123456");
        
        NotificationService slackNotifier = new NotificationService(new SlackNotificationSender());
        slackNotifier.notifyUser("Build completed successfully!");
        
        System.out.println();
        
        // Payment Example
        System.out.println("💳 PAYMENT EXAMPLE:");
        System.out.println("─────────────────────────────────────────────");
        
        // Same service, different gateways!
        PaymentService stripePayment = new PaymentService(new StripeGateway());
        stripePayment.checkout(99.99);
        
        PaymentService razorpayPayment = new PaymentService(new RazorpayGateway());
        razorpayPayment.checkout(99.99);
        
        // Injection Types
        System.out.println("💉 INJECTION TYPES:");
        System.out.println("─────────────────────────────────────────────");
        
        Logger logger = new ConsoleLogger();
        
        ServiceWithConstructorInjection service1 = new ServiceWithConstructorInjection(logger);
        service1.doSomething();
        
        ServiceWithSetterInjection service2 = new ServiceWithSetterInjection();
        service2.setLogger(logger);
        service2.doSomething();
        
        System.out.println();
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Dependency Inversion Principle");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        System.out.println("  📌 RULE: Depend on abstractions, not concrete classes\n");
        
        System.out.println("  ❌ BAD (Violating DIP):");
        System.out.println("     private MySQLDatabase db = new MySQLDatabase();");
        System.out.println("     (Hard-coded dependency)\n");
        
        System.out.println("  ✅ GOOD (Following DIP):");
        System.out.println("     private Database db;  // Interface");
        System.out.println("     public Service(Database db) { this.db = db; }  // Injected\n");
        
        System.out.println("  💉 INJECTION TYPES:");
        System.out.println("     1. Constructor Injection (Best! - dependencies clear)");
        System.out.println("     2. Setter Injection (Optional dependencies)");
        System.out.println("     3. Interface Injection (Rare)\n");
        
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"DIP means high-level modules shouldn't depend on");
        System.out.println("      low-level modules directly. Both should depend on");
        System.out.println("      interfaces. In Spring, I use @Autowired to inject");
        System.out.println("      dependencies. This makes code testable and flexible.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * DIP IN SPRING BOOT
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     Spring Boot is BUILT on DIP!
 *     
 *     // Define interface
 *     public interface UserRepository {
 *         User findById(Long id);
 *     }
 *     
 *     // Implementation
 *     @Repository
 *     public class JpaUserRepository implements UserRepository {
 *         public User findById(Long id) { ... }
 *     }
 *     
 *     // Service depends on interface (DIP!)
 *     @Service
 *     public class UserService {
 *         
 *         private final UserRepository repository;  // Interface! ✅
 *         
 *         @Autowired  // Spring injects the implementation!
 *         public UserService(UserRepository repository) {
 *             this.repository = repository;
 *         }
 *     }
 *     
 *     Spring's IoC Container manages all dependencies!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * DIP BENEFITS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     1. TESTABILITY
 *        - Easily mock dependencies for unit tests
 *        - No need for real database/network in tests
 *     
 *     2. FLEXIBILITY
 *        - Switch implementations without changing code
 *        - MySQL to PostgreSQL? Just inject different class!
 *     
 *     3. LOOSE COUPLING
 *        - Classes don't know about each other's internals
 *        - Changes in one don't affect others
 *     
 *     4. PARALLEL DEVELOPMENT
 *        - Team A works on Service using interface
 *        - Team B works on Repository implementing interface
 *        - Both can work independently!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * DIP vs DEPENDENCY INJECTION
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     DIP = PRINCIPLE (What to do)
 *         "Depend on abstractions"
 *     
 *     Dependency Injection = TECHNIQUE (How to do it)
 *         "Pass dependencies from outside"
 *     
 *     DI is ONE WAY to achieve DIP!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * COMMON INTERVIEW QUESTION
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     Q: "What's the difference between IoC and DIP?"
 *     
 *     A: "DIP is a principle that says depend on abstractions.
 *         IoC (Inversion of Control) is a broader concept where 
 *         control of object creation is inverted - instead of 
 *         objects creating their dependencies, a framework creates 
 *         and injects them. DI (Dependency Injection) is the 
 *         implementation technique. Spring's IoC container uses DI 
 *         to achieve DIP."
 */
