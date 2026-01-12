/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  S - SINGLE RESPONSIBILITY PRINCIPLE (SRP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * DEFINITION:
 * ───────────
 *     "A class should have only ONE reason to change."
 *     
 *     In simple terms: Each class should do ONLY ONE thing.
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * STORY TIME 📖
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Imagine you're a CHEF at a restaurant...
 * 
 * BAD Restaurant (No SRP):
 * ─────────────────────────
 *     Chef has to:
 *     → Cook food
 *     → Take orders from customers
 *     → Clean the tables
 *     → Handle payments
 *     → Wash dishes
 *     
 *     Problem: Chef is OVERWHELMED! 
 *     - If payment system changes → Chef needs to learn it
 *     - If menu changes → Chef needs to update
 *     - If cleaning rules change → Chef needs to adapt
 *     
 *     Chef has TOO MANY reasons to change!
 * 
 * 
 * GOOD Restaurant (With SRP):
 * ───────────────────────────
 *     Chef        → Only cooks food
 *     Waiter      → Takes orders, serves food
 *     Cashier     → Handles payments
 *     Cleaner     → Cleans tables, washes dishes
 *     
 *     Now:
 *     - Payment system changes? Only Cashier affected!
 *     - Menu changes? Only Chef affected!
 *     - Each person has ONE job, ONE reason to change.
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * THE PROBLEM (Without SRP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     class Employee {
 *         // Responsibility 1: Employee data
 *         String name;
 *         double salary;
 *         
 *         // Responsibility 2: Calculate pay (Business logic)
 *         double calculatePay() { ... }
 *         
 *         // Responsibility 3: Save to database (Persistence)
 *         void saveToDatabase() { ... }
 *         
 *         // Responsibility 4: Generate report (Presentation)
 *         String generateReport() { ... }
 *     }
 *     
 *     PROBLEMS:
 *     ─────────
 *     1. Database change? → Modify Employee class
 *     2. Report format change? → Modify Employee class
 *     3. Pay calculation change? → Modify Employee class
 *     4. Hard to test - too many things mixed!
 *     5. Hard to reuse - want report logic? Get DB logic too!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * THE SOLUTION (With SRP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     class Employee {
 *         String name;
 *         double salary;
 *         // Only holds data!
 *     }
 *     
 *     class PayCalculator {
 *         double calculatePay(Employee emp) { ... }
 *         // Only calculates pay!
 *     }
 *     
 *     class EmployeeRepository {
 *         void save(Employee emp) { ... }
 *         // Only handles database!
 *     }
 *     
 *     class EmployeeReportGenerator {
 *         String generate(Employee emp) { ... }
 *         // Only generates reports!
 *     }
 *     
 *     NOW:
 *     ────
 *     - Each class has ONE reason to change
 *     - Easy to test each separately
 *     - Easy to reuse each separately
 * 
 */

package solid;

// ═══════════════════════════════════════════════════════════════════════════════
// BAD EXAMPLE - Violating SRP ❌
// ═══════════════════════════════════════════════════════════════════════════════

class BadInvoice {
    private String customerName;
    private double amount;
    
    public BadInvoice(String customerName, double amount) {
        this.customerName = customerName;
        this.amount = amount;
    }
    
    // Responsibility 1: Calculate total
    public double calculateTotal() {
        double tax = amount * 0.18;
        return amount + tax;
    }
    
    // Responsibility 2: Print invoice (Presentation) ❌
    public void printInvoice() {
        System.out.println("===== INVOICE =====");
        System.out.println("Customer: " + customerName);
        System.out.println("Amount: $" + amount);
        System.out.println("Tax: $" + (amount * 0.18));
        System.out.println("Total: $" + calculateTotal());
    }
    
    // Responsibility 3: Save to database (Persistence) ❌
    public void saveToDatabase() {
        System.out.println("Saving invoice to MySQL database...");
        // Database logic here
    }
    
    // Responsibility 4: Send email (Communication) ❌
    public void sendEmail() {
        System.out.println("Sending invoice email to " + customerName);
        // Email logic here
    }
}
// This class has 4 reasons to change! ❌


// ═══════════════════════════════════════════════════════════════════════════════
// GOOD EXAMPLE - Following SRP ✅
// ═══════════════════════════════════════════════════════════════════════════════

// Class 1: Only holds invoice data
class Invoice {
    private String customerName;
    private double amount;
    
    public Invoice(String customerName, double amount) {
        this.customerName = customerName;
        this.amount = amount;
    }
    
    public String getCustomerName() { return customerName; }
    public double getAmount() { return amount; }
    
    // Only ONE responsibility: Calculate total
    public double calculateTotal() {
        double tax = amount * 0.18;
        return amount + tax;
    }
}

// Class 2: Only prints invoices
class InvoicePrinter {
    public void print(Invoice invoice) {
        System.out.println("===== INVOICE =====");
        System.out.println("Customer: " + invoice.getCustomerName());
        System.out.println("Amount: $" + invoice.getAmount());
        System.out.println("Tax: $" + (invoice.getAmount() * 0.18));
        System.out.println("Total: $" + invoice.calculateTotal());
    }
}

// Class 3: Only saves invoices
class InvoiceRepository {
    public void save(Invoice invoice) {
        System.out.println("Saving invoice to database...");
        System.out.println("Customer: " + invoice.getCustomerName());
        // Only database logic here
    }
}

// Class 4: Only sends emails
class InvoiceEmailer {
    public void sendEmail(Invoice invoice) {
        System.out.println("Sending email to: " + invoice.getCustomerName());
        System.out.println("Subject: Your invoice for $" + invoice.calculateTotal());
        // Only email logic here
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: User Service
// ═══════════════════════════════════════════════════════════════════════════════

// BAD: UserService doing everything ❌
class BadUserService {
    public void registerUser(String name, String email) {
        // Validate
        if (email == null || !email.contains("@")) {
            throw new RuntimeException("Invalid email");
        }
        // Save to DB
        System.out.println("INSERT INTO users...");
        // Send welcome email
        System.out.println("Sending welcome email...");
        // Log the action
        System.out.println("[LOG] User registered: " + name);
    }
}

// GOOD: Separated responsibilities ✅
class User {
    private String name;
    private String email;
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public String getName() { return name; }
    public String getEmail() { return email; }
}

class UserValidator {
    public void validate(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new RuntimeException("Invalid email");
        }
        System.out.println("✅ User validated");
    }
}

class UserRepository {
    public void save(User user) {
        System.out.println("💾 Saving user to database: " + user.getName());
    }
}

class EmailService {
    public void sendWelcomeEmail(User user) {
        System.out.println("📧 Sending welcome email to: " + user.getEmail());
    }
}

class Logger {
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}

class UserService {
    private UserValidator validator;
    private UserRepository repository;
    private EmailService emailService;
    private Logger logger;
    
    public UserService() {
        this.validator = new UserValidator();
        this.repository = new UserRepository();
        this.emailService = new EmailService();
        this.logger = new Logger();
    }
    
    public void registerUser(String name, String email) {
        User user = new User(name, email);
        
        validator.validate(user);      // Validation responsibility
        repository.save(user);         // Persistence responsibility
        emailService.sendWelcomeEmail(user); // Communication responsibility
        logger.log("User registered: " + name); // Logging responsibility
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════

public class S_SingleResponsibility {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  SINGLE RESPONSIBILITY PRINCIPLE (SRP)");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        // BAD Example
        System.out.println("❌ BAD EXAMPLE (One class does everything):");
        System.out.println("─────────────────────────────────────────────");
        BadInvoice badInvoice = new BadInvoice("John", 100.0);
        badInvoice.printInvoice();
        badInvoice.saveToDatabase();
        badInvoice.sendEmail();
        
        System.out.println("\n");
        
        // GOOD Example
        System.out.println("✅ GOOD EXAMPLE (Separated responsibilities):");
        System.out.println("─────────────────────────────────────────────");
        Invoice invoice = new Invoice("Jane", 200.0);
        
        InvoicePrinter printer = new InvoicePrinter();
        InvoiceRepository repo = new InvoiceRepository();
        InvoiceEmailer emailer = new InvoiceEmailer();
        
        printer.print(invoice);
        repo.save(invoice);
        emailer.sendEmail(invoice);
        
        System.out.println("\n");
        
        // Real-world Example
        System.out.println("🌍 REAL-WORLD EXAMPLE (User Registration):");
        System.out.println("─────────────────────────────────────────────");
        UserService userService = new UserService();
        userService.registerUser("Alice", "alice@example.com");
        
        System.out.println("\n");
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Single Responsibility Principle");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        System.out.println("  📌 RULE: One class = One responsibility = One reason to change\n");
        
        System.out.println("  ❌ BAD SIGNS (Violating SRP):");
        System.out.println("     • Class has many unrelated methods");
        System.out.println("     • Class name has 'And' (UserAndEmailManager)");
        System.out.println("     • Changing one feature breaks another");
        System.out.println("     • Hard to test without mocking many things\n");
        
        System.out.println("  ✅ GOOD SIGNS (Following SRP):");
        System.out.println("     • Class name clearly describes ONE thing");
        System.out.println("     • Easy to explain what class does in one sentence");
        System.out.println("     • Changes are isolated to relevant class\n");
        
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"SRP means a class should have only one reason to change.");
        System.out.println("      For example, in a UserService, I separate validation,");
        System.out.println("      persistence, and notification into different classes.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * WHEN TO APPLY SRP?
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     Ask yourself:
 *     ─────────────
 *     1. "Does this class have more than one reason to change?"
 *     2. "Can I describe this class in ONE sentence without 'and'?"
 *     3. "Is this class doing things unrelated to its name?"
 *     
 *     If YES to #1 or #3 → Split the class!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES IN SPRING BOOT
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     Spring follows SRP naturally:
 *     
 *     @Controller  → Only handles HTTP requests/responses
 *     @Service     → Only handles business logic
 *     @Repository  → Only handles database operations
 *     @Component   → Only one specific functionality
 *     
 *     This is why Spring apps are easy to maintain!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * COMMON MISTAKES
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     1. "God Class" - One class does everything
 *        → Split into smaller classes
 *     
 *     2. "Utility Class Abuse" - Everything in Utils
 *        → Create specific classes for specific tasks
 *     
 *     3. "Over-splitting" - One method per class
 *        → SRP is about RESPONSIBILITY, not method count
 *        → Related methods can stay together
 */
