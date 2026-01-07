/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * PROXY PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you want to meet a CELEBRITY 🌟
 * 
 * You can't just walk up to them! You go through their:
 *   - BODYGUARD 🛡️ (checks if you're allowed)
 *   - MANAGER 📋 (schedules the meeting)
 *   - ASSISTANT 📱 (relays messages)
 *   
 * These are all PROXIES - they stand between you and the celebrity!
 * 
 *     You 👤 ──→ 🛡️ Proxy ──→ 🌟 Celebrity (Real Object)
 *                    │
 *            (controls access)
 *            (adds features like security, caching, logging)
 * 
 * THE PATTERN:
 * ─────────────
 *     Subject (interface)
 *          │
 *     ┌────┴────┐
 *     │         │
 *   Proxy   RealSubject
 *     │         │
 *     └────→────┘
 *     (Proxy contains RealSubject)
 * 
 * TYPES OF PROXIES:
 *   - Virtual Proxy: Lazy loading (create when needed)
 *   - Protection Proxy: Access control (check permissions)
 *   - Caching Proxy: Cache results
 *   - Logging Proxy: Log all operations
 */

import java.util.HashMap;
import java.util.Map;

// ═══════════════════════════════════════════════════════════════════════════════
// EXAMPLE 1: Virtual Proxy (Lazy Loading)
// ═══════════════════════════════════════════════════════════════════════════════

// Subject interface
interface Image {
    void display();
    String getFilename();
}

// Real Subject - Heavy object (expensive to create)
class HighResolutionImage implements Image {
    private String filename;
    private String imageData;
    
    public HighResolutionImage(String filename) {
        this.filename = filename;
        loadFromDisk();  // EXPENSIVE!
    }
    
    private void loadFromDisk() {
        System.out.println("  ⏳ Loading high-res image: " + filename + " (takes 2 seconds!)");
        try { Thread.sleep(500); } catch (Exception e) {}  // Simulate loading
        this.imageData = "<<" + filename + " data>>";
        System.out.println("  ✅ Loaded: " + filename);
    }
    
    public void display() {
        System.out.println("  🖼️ Displaying: " + filename);
    }
    
    public String getFilename() { return filename; }
}

// Virtual Proxy - Lazy loads the real image
class ImageProxy implements Image {
    private String filename;
    private HighResolutionImage realImage;  // Created only when needed!
    
    public ImageProxy(String filename) {
        this.filename = filename;
        System.out.println("  ⚡ Created proxy for: " + filename + " (instant!)");
    }
    
    public void display() {
        // Lazy loading - create real image only when display() is called
        if (realImage == null) {
            System.out.println("\n  🔄 First display - loading real image...");
            realImage = new HighResolutionImage(filename);
        }
        realImage.display();
    }
    
    public String getFilename() { return filename; }
}

// ═══════════════════════════════════════════════════════════════════════════════
// EXAMPLE 2: Protection Proxy (Access Control)
// ═══════════════════════════════════════════════════════════════════════════════

interface BankAccount {
    void withdraw(double amount);
    void deposit(double amount);
    double getBalance();
}

class RealBankAccount implements BankAccount {
    private double balance;
    private String accountNumber;
    
    public RealBankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }
    
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("  💸 Withdrew ₹" + amount + ". New balance: ₹" + balance);
        } else {
            System.out.println("  ❌ Insufficient funds!");
        }
    }
    
    public void deposit(double amount) {
        balance += amount;
        System.out.println("  💰 Deposited ₹" + amount + ". New balance: ₹" + balance);
    }
    
    public double getBalance() { return balance; }
}

// Protection Proxy - Checks authorization before operations
class SecureBankAccountProxy implements BankAccount {
    private RealBankAccount realAccount;
    private String userRole;
    private String userId;
    
    public SecureBankAccountProxy(RealBankAccount account, String userId, String role) {
        this.realAccount = account;
        this.userId = userId;
        this.userRole = role;
    }
    
    public void withdraw(double amount) {
        System.out.println("  🔐 Checking authorization for " + userId + "...");
        
        if (userRole.equals("OWNER") || userRole.equals("ADMIN")) {
            System.out.println("  ✅ Authorized!");
            logOperation("WITHDRAW", amount);
            realAccount.withdraw(amount);
        } else {
            System.out.println("  🚫 ACCESS DENIED! Only owner can withdraw.");
        }
    }
    
    public void deposit(double amount) {
        // Anyone can deposit
        logOperation("DEPOSIT", amount);
        realAccount.deposit(amount);
    }
    
    public double getBalance() {
        if (userRole.equals("OWNER") || userRole.equals("ADMIN")) {
            return realAccount.getBalance();
        }
        System.out.println("  🚫 Cannot view balance - not authorized!");
        return -1;
    }
    
    private void logOperation(String op, double amount) {
        System.out.println("  📋 LOG: " + userId + " performed " + op + " of ₹" + amount);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// EXAMPLE 3: Caching Proxy
// ═══════════════════════════════════════════════════════════════════════════════

interface DataService {
    String fetchData(String key);
}

class SlowDatabaseService implements DataService {
    public String fetchData(String key) {
        System.out.println("  🐢 Fetching from database: " + key + " (slow!)");
        try { Thread.sleep(200); } catch (Exception e) {}
        return "Data for " + key;
    }
}

// Caching Proxy - Caches results to avoid repeated slow calls
class CachingProxy implements DataService {
    private SlowDatabaseService realService;
    private Map<String, String> cache = new HashMap<>();
    
    public CachingProxy(SlowDatabaseService service) {
        this.realService = service;
    }
    
    public String fetchData(String key) {
        if (cache.containsKey(key)) {
            System.out.println("  ⚡ Cache HIT for: " + key);
            return cache.get(key);
        }
        
        System.out.println("  ❌ Cache MISS for: " + key);
        String data = realService.fetchData(key);
        cache.put(key, data);
        return data;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class ProxyMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ PROXY PATTERN - VIRTUAL PROXY (Lazy Loading) ═══\n");
        
        System.out.println("Creating image gallery (without loading actual images):\n");
        
        // Create proxies - NO actual images loaded yet!
        Image[] gallery = new Image[3];
        gallery[0] = new ImageProxy("vacation1.jpg");
        gallery[1] = new ImageProxy("vacation2.jpg");
        gallery[2] = new ImageProxy("vacation3.jpg");
        
        System.out.println("\nAll proxies created! No images loaded yet.");
        System.out.println("User clicks on first image...\n");
        
        // Only now is the first image loaded!
        gallery[0].display();
        
        System.out.println("\nUser clicks on first image again...\n");
        gallery[0].display();  // Already loaded - no delay!
        
        System.out.println("\n\n═══ PROXY PATTERN - PROTECTION PROXY (Access Control) ═══\n");
        
        // Create real account
        RealBankAccount account = new RealBankAccount("ACC-001", 10000);
        
        // Owner access
        System.out.println("👤 Owner trying to access:\n");
        BankAccount ownerAccess = new SecureBankAccountProxy(account, "John", "OWNER");
        ownerAccess.withdraw(500);
        
        System.out.println("\n👤 Guest trying to access:\n");
        BankAccount guestAccess = new SecureBankAccountProxy(account, "Stranger", "GUEST");
        guestAccess.withdraw(500);  // Should be denied!
        guestAccess.deposit(100);   // Should work
        
        System.out.println("\n\n═══ PROXY PATTERN - CACHING PROXY ═══\n");
        
        DataService service = new CachingProxy(new SlowDatabaseService());
        
        System.out.println("Fetching data multiple times:\n");
        
        // First call - cache miss, slow
        String data1 = service.fetchData("user-123");
        System.out.println("  Result: " + data1 + "\n");
        
        // Second call - cache hit, fast!
        String data2 = service.fetchData("user-123");
        System.out.println("  Result: " + data2 + "\n");
        
        // Different key - cache miss
        String data3 = service.fetchData("user-456");
        System.out.println("  Result: " + data3 + "\n");
        
        // Same as first - cache hit
        String data4 = service.fetchData("user-123");
        System.out.println("  Result: " + data4);
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * TYPES OF PROXIES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. VIRTUAL PROXY (Lazy Loading)
 *        - Creates expensive object only when needed
 *        - Example: Image gallery thumbnails
 *     
 *     2. PROTECTION PROXY (Access Control)
 *        - Checks permissions before allowing access
 *        - Example: Admin-only features
 *     
 *     3. CACHING PROXY
 *        - Caches results of expensive operations
 *        - Example: Database query cache
 *     
 *     4. LOGGING PROXY
 *        - Logs all operations
 *        - Example: Audit trail
 *     
 *     5. REMOTE PROXY
 *        - Represents object in different address space
 *        - Example: RMI stubs
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. HIBERNATE LAZY LOADING
 *        - Entity proxies load data only when accessed
 *     
 *     2. SPRING AOP
 *        - Creates proxies for @Transactional, @Cacheable
 *     
 *     3. JAVA RMI
 *        - Stubs act as proxies to remote objects
 *     
 *     4. CDN (Content Delivery Network)
 *        - Caches content, proxies to origin server
 *     
 *     5. VPN
 *        - Proxies your internet connection
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PROXY vs DECORATOR:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     PROXY:
 *        - Controls ACCESS to object
 *        - Same interface, but adds control
 *        - Client may not know it's using proxy
 *     
 *     DECORATOR:
 *        - Adds BEHAVIOR to object
 *        - Same interface, but adds features
 *        - Can be stacked (multiple decorators)
 */
