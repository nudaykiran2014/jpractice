/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  O - OPEN/CLOSED PRINCIPLE (OCP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * DEFINITION:
 * ───────────
 *     "Software entities should be OPEN for extension, but CLOSED for modification."
 *     
 *     In simple terms: Add new features WITHOUT changing existing code.
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * STORY TIME 📖
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Imagine you have a REMOTE CONTROL for your TV...
 * 
 * BAD Design (Violating OCP):
 * ───────────────────────────
 *     Your remote has a button that works like this:
 *     
 *     pressButton() {
 *         if (device == "TV") {
 *             turnOnTV();
 *         } else if (devie == "AC") {
 *             turnOnAC();
 *         } else if (device == "Fan") {
 *             turnOnFan();
 *         }
 *         // Every new device = MODIFY this code!
 *     }
 *     
 *     Problem: 
 *     - Want to add "Light"? → Change the remote code!
 *     - Want to add "Speaker"? → Change the remote code!
 *     - Each change risks breaking existing devices!
 * 
 * 
 * GOOD Design (Following OCP):
 * ────────────────────────────
 *     Remote has a universal button that works with ANY device:
 *     
 *     interface Device {
 *         void turnOn();
 *     }
 *     
 *     class TV implements Device { turnOn() {...} }
 *     class AC implements Device { turnOn() {...} }
 *     class Light implements Device { turnOn() {...} }  // NEW! No remote change!
 *     
 *     pressButton(Device device) {
 *         device.turnOn();  // Works with ANY device!
 *     }
 *     
 *     Now:
 *     - Add new device? Just create new class!
 *     - Remote code NEVER changes!
 *     - OPEN for extension (new devices)
 *     - CLOSED for modification (remote code)
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * THE PROBLEM (Without OCP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     class DiscountCalculator {
 *         double calculate(String customerType, double amount) {
 *             if (customerType.equals("REGULAR")) {
 *                 return amount * 0.10;
 *             } else if (customerType.equals("PREMIUM")) {
 *                 return amount * 0.20;
 *             } else if (customerType.equals("VIP")) {
 *                 return amount * 0.30;
 *             }
 *             // New customer type? MODIFY this code!
 *             return 0;
 *         }
 *     }
 *     
 *     PROBLEMS:
 *     ─────────
 *     1. Adding "GOLD" customer → Change existing code
 *     2. Risk breaking existing logic
 *     3. Growing if-else chain (ugly!)
 *     4. Hard to test individual types
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * THE SOLUTION (With OCP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     interface DiscountStrategy {
 *         double calculate(double amount);
 *     }
 *     
 *     class RegularDiscount implements DiscountStrategy {
 *         double calculate(double amount) { return amount * 0.10; }
 *     }
 *     
 *     class PremiumDiscount implements DiscountStrategy {
 *         double calculate(double amount) { return amount * 0.20; }
 *     }
 *     
 *     // NEW! No change to existing classes!
 *     class GoldDiscount implements DiscountStrategy {
 *         double calculate(double amount) { return amount * 0.25; }
 *     }
 *     
 *     class DiscountCalculator {
 *         double calculate(DiscountStrategy strategy, double amount) {
 *             return strategy.calculate(amount);  // NEVER changes!
 *         }
 *     }
 * 
 */

package solid;

import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// BAD EXAMPLE - Violating OCP ❌
// ═══════════════════════════════════════════════════════════════════════════════

class BadPaymentProcessor {
    
    public void processPayment(String paymentType, double amount) {
        if (paymentType.equals("CREDIT_CARD")) {
            System.out.println("Processing credit card payment: $" + amount);
            // Credit card logic
        } else if (paymentType.equals("PAYPAL")) {
            System.out.println("Processing PayPal payment: $" + amount);
            // PayPal logic
        } else if (paymentType.equals("UPI")) {
            System.out.println("Processing UPI payment: $" + amount);
            // UPI logic
        }
        // Want to add Google Pay? → MODIFY this code! ❌
        // Want to add Apple Pay? → MODIFY this code! ❌
        // Want to add Crypto? → MODIFY this code! ❌
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// GOOD EXAMPLE - Following OCP ✅
// ═══════════════════════════════════════════════════════════════════════════════

// Step 1: Create an interface (abstraction)
interface PaymentMethod {
    void pay(double amount);
    String getName();
}

// Step 2: Implement for each payment type
class CreditCardPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("💳 Processing Credit Card payment: $" + amount);
    }
    
    @Override
    public String getName() { return "Credit Card"; }
}

class PayPalPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("🅿️ Processing PayPal payment: $" + amount);
    }
    
    @Override
    public String getName() { return "PayPal"; }
}

class UPIPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("📱 Processing UPI payment: $" + amount);
    }
    
    @Override
    public String getName() { return "UPI"; }
}

// NEW PAYMENT METHOD! No change to existing code! ✅
class CryptoPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("₿ Processing Crypto payment: $" + amount);
    }
    
    @Override
    public String getName() { return "Crypto"; }
}

// Step 3: Payment processor that NEVER needs to change
class PaymentProcessor {
    // This method is CLOSED for modification
    // But the system is OPEN for new payment methods!
    public void processPayment(PaymentMethod paymentMethod, double amount) {
        System.out.println("Processing payment via: " + paymentMethod.getName());
        paymentMethod.pay(amount);
        System.out.println("✅ Payment successful!\n");
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Shape Area Calculator
// ═══════════════════════════════════════════════════════════════════════════════

// BAD: if-else chain ❌
class BadAreaCalculator {
    public double calculateArea(Object shape) {
        if (shape instanceof BadRectangle) {
            BadRectangle r = (BadRectangle) shape;
            return r.width * r.height;
        } else if (shape instanceof BadCircle) {
            BadCircle c = (BadCircle) shape;
            return Math.PI * c.radius * c.radius;
        }
        // New shape? MODIFY this code! ❌
        return 0;
    }
}

class BadRectangle {
    double width, height;
    BadRectangle(double w, double h) { width = w; height = h; }
}

class BadCircle {
    double radius;
    BadCircle(double r) { radius = r; }
}


// GOOD: Using polymorphism ✅
interface Shape {
    double calculateArea();
    String getName();
}

class Rectangle implements Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
    
    @Override
    public String getName() { return "Rectangle"; }
}

class Circle implements Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public String getName() { return "Circle"; }
}

// NEW SHAPE! No change to existing code! ✅
class Triangle implements Shape {
    private double base, height;
    
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }
    
    @Override
    public String getName() { return "Triangle"; }
}

// Calculator that NEVER changes
class AreaCalculator {
    public void printArea(Shape shape) {
        System.out.println(shape.getName() + " area: " + 
                          String.format("%.2f", shape.calculateArea()));
    }
    
    public double totalArea(List<Shape> shapes) {
        return shapes.stream()
                     .mapToDouble(Shape::calculateArea)
                     .sum();
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Notification System
// ═══════════════════════════════════════════════════════════════════════════════

interface NotificationChannel {
    void send(String message);
}

class EmailNotification implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("📧 Email: " + message);
    }
}

class SMSNotification implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("📱 SMS: " + message);
    }
}

class PushNotification implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("🔔 Push: " + message);
    }
}

// Add Slack without changing existing code! ✅
class SlackNotification implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("💬 Slack: " + message);
    }
}

class NotificationService {
    private List<NotificationChannel> channels = new ArrayList<>();
    
    public void addChannel(NotificationChannel channel) {
        channels.add(channel);
    }
    
    // This method NEVER changes!
    public void notifyAll(String message) {
        for (NotificationChannel channel : channels) {
            channel.send(message);
        }
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════

public class O_OpenClosed {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  OPEN/CLOSED PRINCIPLE (OCP)");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        // Payment Example
        System.out.println("💰 PAYMENT EXAMPLE:");
        System.out.println("─────────────────────────────────────────────");
        PaymentProcessor processor = new PaymentProcessor();
        
        processor.processPayment(new CreditCardPayment(), 100.0);
        processor.processPayment(new PayPalPayment(), 200.0);
        processor.processPayment(new CryptoPayment(), 500.0);  // NEW! No code change!
        
        // Shape Example
        System.out.println("📐 SHAPE EXAMPLE:");
        System.out.println("─────────────────────────────────────────────");
        AreaCalculator calculator = new AreaCalculator();
        
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Rectangle(10, 5));
        shapes.add(new Circle(7));
        shapes.add(new Triangle(10, 6));  // NEW! No code change!
        
        for (Shape shape : shapes) {
            calculator.printArea(shape);
        }
        System.out.println("Total area: " + String.format("%.2f", calculator.totalArea(shapes)));
        
        System.out.println();
        
        // Notification Example
        System.out.println("🔔 NOTIFICATION EXAMPLE:");
        System.out.println("─────────────────────────────────────────────");
        NotificationService notifier = new NotificationService();
        notifier.addChannel(new EmailNotification());
        notifier.addChannel(new SMSNotification());
        notifier.addChannel(new SlackNotification());  // NEW! No code change!
        
        notifier.notifyAll("Your order has been shipped!");
        
        System.out.println();
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Open/Closed Principle");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        System.out.println("  📌 RULE: Open for extension, Closed for modification\n");
        
        System.out.println("  ❌ BAD SIGNS (Violating OCP):");
        System.out.println("     • Long if-else or switch chains");
        System.out.println("     • Adding feature = modifying existing class");
        System.out.println("     • Using 'instanceof' checks\n");
        
        System.out.println("  ✅ HOW TO ACHIEVE OCP:");
        System.out.println("     • Use interfaces/abstract classes");
        System.out.println("     • Use polymorphism");
        System.out.println("     • Strategy pattern");
        System.out.println("     • Dependency Injection\n");
        
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"OCP means I can add new features without modifying");
        System.out.println("      existing code. I use interfaces and polymorphism.");
        System.out.println("      For example, to add a new payment method, I just");
        System.out.println("      create a new class implementing PaymentMethod interface.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * HOW TO ACHIEVE OCP?
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     1. INTERFACES - Define contract, implement differently
 *     2. ABSTRACT CLASSES - Common behavior, extend for variations
 *     3. STRATEGY PATTERN - Swap algorithms at runtime
 *     4. TEMPLATE METHOD - Define skeleton, override steps
 *     5. DEPENDENCY INJECTION - Inject implementations
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * OCP IN SPRING BOOT
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     Spring naturally supports OCP:
 *     
 *     // Define interface
 *     public interface PaymentService {
 *         void process(Payment payment);
 *     }
 *     
 *     // Implementations
 *     @Service("creditCard")
 *     public class CreditCardService implements PaymentService { ... }
 *     
 *     @Service("paypal")
 *     public class PayPalService implements PaymentService { ... }
 *     
 *     // Add new without changing existing!
 *     @Service("crypto")
 *     public class CryptoService implements PaymentService { ... }
 *     
 *     // Usage - inject any implementation
 *     @Autowired
 *     @Qualifier("crypto")
 *     private PaymentService paymentService;
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * COMMON INTERVIEW QUESTION
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     Q: "How do you add a new feature without modifying existing code?"
 *     
 *     A: "I use the Open/Closed Principle. I define an interface for the behavior,
 *         then create new implementations. The existing code depends on the interface,
 *         not concrete classes. So adding a new PaymentMethod just means creating a
 *         new class - no existing code changes."
 */
