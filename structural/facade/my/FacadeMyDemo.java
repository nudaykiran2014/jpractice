/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * FACADE PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you want to WATCH A MOVIE at home 🎬
 * 
 * WITHOUT Facade:
 *   1. Turn on TV
 *   2. Turn on DVD player
 *   3. Turn on sound system
 *   4. Dim the lights
 *   5. Set sound to surround mode
 *   6. Set TV to correct input
 *   7. Press play on DVD...
 *   
 *   TOO MANY STEPS! 😫
 *   
 * WITH Facade (ONE button remote):
 *   - Press "WATCH MOVIE" button
 *   - Everything happens automatically! 🎉
 *   
 *     ┌────────────────┐
 *     │ 🎬 WATCH MOVIE │  ← Simple facade
 *     └───────┬────────┘
 *             │
 *     ┌───────┴───────┐
 *     ▼       ▼       ▼
 *    📺      🔊      💡
 *    TV    Sound   Lights  ← Complex subsystem
 * 
 * THE PATTERN:
 * ─────────────
 *     Client ──→ 🏠 Facade ──→ Complex Subsystem
 *                   │
 *         (Simple interface hiding complexity)
 */

// ═══════════════════════════════════════════════════════════════════════════════
// COMPLEX SUBSYSTEM - Many classes with many methods
// ═══════════════════════════════════════════════════════════════════════════════

class Television {
    public void on() { System.out.println("  📺 TV is ON"); }
    public void off() { System.out.println("  📺 TV is OFF"); }
    public void setInput(String input) { System.out.println("  📺 TV input: " + input); }
}

class SoundSystem {
    public void on() { System.out.println("  🔊 Sound system ON"); }
    public void off() { System.out.println("  🔊 Sound system OFF"); }
    public void setVolume(int level) { System.out.println("  🔊 Volume: " + level); }
    public void setSurroundSound() { System.out.println("  🔊 Surround sound enabled"); }
}

class DVDPlayer {
    public void on() { System.out.println("  📀 DVD player ON"); }
    public void off() { System.out.println("  📀 DVD player OFF"); }
    public void play(String movie) { System.out.println("  📀 Playing: " + movie); }
    public void pause() { System.out.println("  📀 Paused"); }
    public void stop() { System.out.println("  📀 Stopped"); }
}

class Lights {
    public void on() { System.out.println("  💡 Lights ON"); }
    public void off() { System.out.println("  💡 Lights OFF"); }
    public void dim(int level) { System.out.println("  💡 Lights dimmed to " + level + "%"); }
}

class PopcornMachine {
    public void on() { System.out.println("  🍿 Popcorn machine ON"); }
    public void off() { System.out.println("  🍿 Popcorn machine OFF"); }
    public void pop() { System.out.println("  🍿 Popping corn..."); }
}

// ═══════════════════════════════════════════════════════════════════════════════
// FACADE - Simple interface to complex subsystem
// ═══════════════════════════════════════════════════════════════════════════════
class HomeTheaterFacade {
    private Television tv;
    private SoundSystem sound;
    private DVDPlayer dvd;
    private Lights lights;
    private PopcornMachine popcorn;
    
    public HomeTheaterFacade(Television tv, SoundSystem sound, 
                             DVDPlayer dvd, Lights lights, PopcornMachine popcorn) {
        this.tv = tv;
        this.sound = sound;
        this.dvd = dvd;
        this.lights = lights;
        this.popcorn = popcorn;
    }
    
    // ONE method does everything!
    public void watchMovie(String movie) {
        System.out.println("\n🎬 Getting ready to watch: " + movie);
        System.out.println("─────────────────────────────────────");
        popcorn.on();
        popcorn.pop();
        lights.dim(20);
        tv.on();
        tv.setInput("HDMI1");
        sound.on();
        sound.setSurroundSound();
        sound.setVolume(50);
        dvd.on();
        dvd.play(movie);
        System.out.println("─────────────────────────────────────");
        System.out.println("🎬 Enjoy your movie!\n");
    }
    
    public void endMovie() {
        System.out.println("🛑 Shutting down movie theater...");
        System.out.println("─────────────────────────────────────");
        dvd.stop();
        dvd.off();
        sound.off();
        tv.off();
        lights.on();
        popcorn.off();
        System.out.println("─────────────────────────────────────");
        System.out.println("✅ Theater shutdown complete\n");
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Order Processing Facade
// ═══════════════════════════════════════════════════════════════════════════════

class InventoryService {
    public boolean checkStock(String product) {
        System.out.println("  📦 Checking inventory for: " + product);
        return true;
    }
    public void reduceStock(String product) {
        System.out.println("  📦 Reducing stock for: " + product);
    }
}

class PaymentService {
    public boolean processPayment(String customerId, double amount) {
        System.out.println("  💳 Processing payment of ₹" + amount + " for customer: " + customerId);
        return true;
    }
    public void refund(String customerId, double amount) {
        System.out.println("  💳 Refunding ₹" + amount + " to: " + customerId);
    }
}

class ShippingService {
    public String createShipment(String product, String address) {
        System.out.println("  🚚 Creating shipment to: " + address);
        return "SHIP-" + System.currentTimeMillis();
    }
    public void trackShipment(String trackingId) {
        System.out.println("  🚚 Tracking: " + trackingId);
    }
}

class NotificationService {
    public void sendEmail(String email, String message) {
        System.out.println("  📧 Email to " + email + ": " + message);
    }
    public void sendSms(String phone, String message) {
        System.out.println("  📱 SMS to " + phone + ": " + message);
    }
}

// FACADE for order processing
class OrderFacade {
    private InventoryService inventory = new InventoryService();
    private PaymentService payment = new PaymentService();
    private ShippingService shipping = new ShippingService();
    private NotificationService notification = new NotificationService();
    
    // ONE simple method!
    public String placeOrder(String customerId, String product, 
                            double amount, String address, String email) {
        System.out.println("\n🛒 Processing Order...");
        System.out.println("─────────────────────────────────────");
        
        // Step 1: Check inventory
        if (!inventory.checkStock(product)) {
            notification.sendEmail(email, "Sorry, " + product + " is out of stock");
            return null;
        }
        
        // Step 2: Process payment
        if (!payment.processPayment(customerId, amount)) {
            notification.sendEmail(email, "Payment failed");
            return null;
        }
        
        // Step 3: Reduce inventory
        inventory.reduceStock(product);
        
        // Step 4: Create shipment
        String trackingId = shipping.createShipment(product, address);
        
        // Step 5: Send notifications
        notification.sendEmail(email, "Order confirmed! Tracking: " + trackingId);
        
        System.out.println("─────────────────────────────────────");
        System.out.println("✅ Order complete! Tracking: " + trackingId + "\n");
        return trackingId;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class FacadeMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ FACADE PATTERN - HOME THEATER ═══");
        
        // Create subsystem components
        Television tv = new Television();
        SoundSystem sound = new SoundSystem();
        DVDPlayer dvd = new DVDPlayer();
        Lights lights = new Lights();
        PopcornMachine popcorn = new PopcornMachine();
        
        // Create facade
        HomeTheaterFacade theater = new HomeTheaterFacade(tv, sound, dvd, lights, popcorn);
        
        // Use simple facade methods!
        theater.watchMovie("Avengers: Endgame");
        
        // Simulate watching for a bit...
        System.out.println("... enjoying the movie ...\n");
        
        theater.endMovie();
        
        System.out.println("\n═══ FACADE PATTERN - E-COMMERCE ORDER ═══");
        
        // Create facade
        OrderFacade orderFacade = new OrderFacade();
        
        // Place order with ONE method call!
        orderFacade.placeOrder(
            "CUST-123",
            "iPhone 15",
            79999.0,
            "123 Main St, Mumbai",
            "customer@email.com"
        );
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT FACADE (BAD - Client knows too much):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     // Client must know about EVERY subsystem!
 *     tv.on();
 *     tv.setInput("HDMI1");
 *     sound.on();
 *     sound.setSurroundSound();
 *     sound.setVolume(50);
 *     dvd.on();
 *     dvd.play("movie");
 *     lights.dim(20);
 *     // Forgot something? Order wrong? Client's problem!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH FACADE (GOOD - Simple interface):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     theater.watchMovie("Avengers");  // That's it!
 *     
 *     // Client doesn't know/care about subsystem details
 *     // Facade handles all the complexity
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. COMPUTER STARTUP
 *        - Press power button → CPU, RAM, BIOS, OS all start
 *     
 *     2. CAR IGNITION
 *        - Turn key → Starter, fuel pump, ignition, engine all work
 *     
 *     3. SPRING BOOT
 *        - @SpringBootApplication → Auto-configures EVERYTHING!
 *     
 *     4. JDBC
 *        - DriverManager.getConnection() → Complex connection setup
 *     
 *     5. jQuery
 *        - $(".class").hide() → Complex DOM operations
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * FACADE vs ADAPTER:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     FACADE:   Simplifies interface (many classes → one simple interface)
 *     ADAPTER:  Converts interface (one interface → another interface)
 */
