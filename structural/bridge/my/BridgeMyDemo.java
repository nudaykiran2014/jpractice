/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * BRIDGE PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you have REMOTE CONTROLS 📺 and TVs 📺
 * 
 * WITHOUT Bridge:
 *   - SonyRemote for SonyTV
 *   - SonyRemote for SamsungTV
 *   - SamsungRemote for SonyTV
 *   - SamsungRemote for SamsungTV
 *   - 4 classes! And adding LG = 6 classes! EXPLOSION! 💥
 *   
 * WITH Bridge:
 *   - Remote (abstract) ────🌉──── TV (interface)
 *   - BasicRemote              SonyTV
 *   - AdvancedRemote           SamsungTV
 *   - Only 4 classes total, and adding LG = just 1 more class!
 * 
 * THE PATTERN:
 * ─────────────
 *     WHAT (Abstraction)    🌉 BRIDGE     HOW (Implementation)
 *     ┌─────────────┐                     ┌─────────────┐
 *     │   Remote    │ ─────────────────→  │     TV      │
 *     └─────────────┘                     └─────────────┘
 *          │                                    │
 *     ┌────┴────┐                         ┌────┴────┐
 *     Basic  Advanced                   Sony   Samsung
 *     
 *     Separate "what you want" from "how it's done"
 */

// ═══════════════════════════════════════════════════════════════════════════════
// IMPLEMENTATION (the "HOW") - Different devices
// ═══════════════════════════════════════════════════════════════════════════════
interface Device {
    void turnOn();
    void turnOff();
    void setVolume(int volume);
    int getVolume();
    void setChannel(int channel);
}

class SonyTV implements Device {
    private int volume = 50;
    private int channel = 1;
    
    public void turnOn() { System.out.println("  📺 Sony TV is ON"); }
    public void turnOff() { System.out.println("  📺 Sony TV is OFF"); }
    public void setVolume(int v) { volume = v; System.out.println("  📺 Sony volume: " + v); }
    public int getVolume() { return volume; }
    public void setChannel(int c) { channel = c; System.out.println("  📺 Sony channel: " + c); }
}

class SamsungTV implements Device {
    private int volume = 50;
    private int channel = 1;
    
    public void turnOn() { System.out.println("  📺 Samsung TV is ON"); }
    public void turnOff() { System.out.println("  📺 Samsung TV is OFF"); }
    public void setVolume(int v) { volume = v; System.out.println("  📺 Samsung volume: " + v); }
    public int getVolume() { return volume; }
    public void setChannel(int c) { channel = c; System.out.println("  📺 Samsung channel: " + c); }
}

class Radio implements Device {
    private int volume = 30;
    private int channel = 1;
    
    public void turnOn() { System.out.println("  📻 Radio is ON"); }
    public void turnOff() { System.out.println("  📻 Radio is OFF"); }
    public void setVolume(int v) { volume = v; System.out.println("  📻 Radio volume: " + v); }
    public int getVolume() { return volume; }
    public void setChannel(int c) { channel = c; System.out.println("  📻 Radio station: " + c); }
}

// ═══════════════════════════════════════════════════════════════════════════════
// ABSTRACTION (the "WHAT") - Different remotes
// ═══════════════════════════════════════════════════════════════════════════════
abstract class Remote {
    protected Device device;  // 🌉 THE BRIDGE!
    
    public Remote(Device device) {
        this.device = device;
    }
    
    public void power() {
        System.out.println("Remote: power button");
        device.turnOn();
    }
    
    public void volumeUp() {
        device.setVolume(device.getVolume() + 10);
    }
    
    public void volumeDown() {
        device.setVolume(device.getVolume() - 10);
    }
    
    public void channelUp() {
        device.setChannel(1);
    }
}

// Basic Remote - simple functions
class BasicRemote extends Remote {
    public BasicRemote(Device device) {
        super(device);
    }
    
    // Basic remote has standard functions only
}

// Advanced Remote - extra functions
class AdvancedRemote extends Remote {
    public AdvancedRemote(Device device) {
        super(device);
    }
    
    // Advanced remote has MUTE function!
    public void mute() {
        System.out.println("Remote: MUTE!");
        device.setVolume(0);
    }
    
    // Advanced remote has FAVORITE channel!
    public void setFavoriteChannel(int channel) {
        System.out.println("Remote: Setting favorite channel!");
        device.setChannel(channel);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Notification System
// ═══════════════════════════════════════════════════════════════════════════════

// Implementation - HOW to send
interface MessageSender {
    void send(String message, String recipient);
}

class EmailSender implements MessageSender {
    public void send(String message, String recipient) {
        System.out.println("  📧 Email to " + recipient + ": " + message);
    }
}

class SmsSender implements MessageSender {
    public void send(String message, String recipient) {
        System.out.println("  📱 SMS to " + recipient + ": " + message);
    }
}

class PushNotificationSender implements MessageSender {
    public void send(String message, String recipient) {
        System.out.println("  🔔 Push to " + recipient + ": " + message);
    }
}

// Abstraction - WHAT to send
abstract class Notification {
    protected MessageSender sender;  // 🌉 Bridge!
    
    public Notification(MessageSender sender) {
        this.sender = sender;
    }
    
    abstract void notifyUser(String user);
}

class AlertNotification extends Notification {
    private String alertMessage;
    
    public AlertNotification(MessageSender sender, String message) {
        super(sender);
        this.alertMessage = message;
    }
    
    void notifyUser(String user) {
        sender.send("🚨 ALERT: " + alertMessage, user);
    }
}

class ReminderNotification extends Notification {
    private String reminder;
    
    public ReminderNotification(MessageSender sender, String reminder) {
        super(sender);
        this.reminder = reminder;
    }
    
    void notifyUser(String user) {
        sender.send("⏰ Reminder: " + reminder, user);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class BridgeMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ BRIDGE PATTERN - REMOTE & TV ═══\n");
        
        // Same remote type, different devices
        System.out.println("Basic Remote with Sony TV:");
        Remote sonyRemote = new BasicRemote(new SonyTV());
        sonyRemote.power();
        sonyRemote.volumeUp();
        
        System.out.println("\nBasic Remote with Samsung TV:");
        Remote samsungRemote = new BasicRemote(new SamsungTV());
        samsungRemote.power();
        samsungRemote.volumeUp();
        
        System.out.println("\nAdvanced Remote with Radio:");
        AdvancedRemote radioRemote = new AdvancedRemote(new Radio());
        radioRemote.power();
        radioRemote.mute();  // Advanced feature!
        radioRemote.setFavoriteChannel(98);
        
        System.out.println("\n═══ BRIDGE PATTERN - NOTIFICATIONS ═══\n");
        
        // Same notification type, different senders
        System.out.println("Alert via different channels:");
        new AlertNotification(new EmailSender(), "Server down!").notifyUser("admin@company.com");
        new AlertNotification(new SmsSender(), "Server down!").notifyUser("+91-9876543210");
        new AlertNotification(new PushNotificationSender(), "Server down!").notifyUser("device-123");
        
        System.out.println("\nReminder via different channels:");
        new ReminderNotification(new EmailSender(), "Meeting at 3PM").notifyUser("john@email.com");
        new ReminderNotification(new SmsSender(), "Meeting at 3PM").notifyUser("+91-1234567890");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT BRIDGE (BAD - Class Explosion):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     // Must create class for EACH combination!
 *     class BasicSonyRemote { }
 *     class BasicSamsungRemote { }
 *     class BasicLGRemote { }
 *     class AdvancedSonyRemote { }
 *     class AdvancedSamsungRemote { }
 *     class AdvancedLGRemote { }
 *     // 2 remotes × 3 TVs = 6 classes!
 *     // Add 1 more remote type = 3 more classes!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH BRIDGE (GOOD - Separate hierarchies):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     Remote (abstract)    Device (interface)
 *        │                      │
 *     BasicRemote            SonyTV
 *     AdvancedRemote         SamsungTV
 *                            LGRemote
 *     
 *     // 2 remotes + 3 TVs = 5 classes!
 *     // Add 1 more remote type = just 1 more class!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. REMOTE + DEVICE
 *        - Any remote can control any device
 *     
 *     2. UI + PLATFORM
 *        - Window/Button (abstraction) + Windows/Linux/Mac (implementation)
 *     
 *     3. NOTIFICATION + CHANNEL
 *        - Alert/Reminder (what) + Email/SMS/Push (how)
 *     
 *     4. JDBC
 *        - JDBC API (abstraction) + MySQL/PostgreSQL/Oracle drivers (implementation)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * BRIDGE vs ADAPTER:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     ADAPTER: Makes EXISTING incompatible interfaces work together
 *              (designed after the fact)
 *     
 *     BRIDGE:  Separates abstraction from implementation UP FRONT
 *              (designed from the start)
 */
