/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  I - INTERFACE SEGREGATION PRINCIPLE (ISP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * DEFINITION:
 * ───────────
 *     "Clients should not be forced to depend on interfaces they do not use."
 *     
 *     In simple terms: Don't force classes to implement methods they don't need!
 *                      Many small interfaces > One big interface
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * STORY TIME 📖
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Imagine you're at a RESTAURANT...
 * 
 * BAD Design (Violating ISP):
 * ───────────────────────────
 *     interface RestaurantEmployee {
 *         void cook();
 *         void takeOrder();
 *         void cleanTables();
 *         void handlePayment();
 *         void deliverFood();
 *     }
 *     
 *     class Chef implements RestaurantEmployee {
 *         void cook() { /* Chef cooks! */ }
 *         void takeOrder() { /* Empty - Chef doesn't do this! */ }  ❌
 *         void cleanTables() { /* Empty */ }  ❌
 *         void handlePayment() { /* Empty */ }  ❌
 *         void deliverFood() { /* Empty */ }  ❌
 *     }
 *     
 *     Problem: Chef is FORCED to implement methods it doesn't use!
 * 
 * 
 * GOOD Design (Following ISP):
 * ────────────────────────────
 *     interface Cookable {
 *         void cook();
 *     }
 *     
 *     interface OrderTaker {
 *         void takeOrder();
 *     }
 *     
 *     interface Cleaner {
 *         void cleanTables();
 *     }
 *     
 *     class Chef implements Cookable {
 *         void cook() { /* Chef only cooks! */ }  ✅
 *     }
 *     
 *     class Waiter implements OrderTaker, Cleaner {
 *         void takeOrder() { /* Takes orders! */ }  ✅
 *         void cleanTables() { /* Cleans! */ }  ✅
 *     }
 *     
 *     Now: Each class implements ONLY what it needs!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * THE PROBLEM (Without ISP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     interface Machine {
 *         void print();
 *         void scan();
 *         void fax();
 *         void staple();
 *     }
 *     
 *     class AllInOnePrinter implements Machine {
 *         void print() { ... }  ✅
 *         void scan() { ... }   ✅
 *         void fax() { ... }    ✅
 *         void staple() { ... } ✅
 *     }
 *     
 *     class SimplePrinter implements Machine {
 *         void print() { ... }  ✅
 *         void scan() { throw new UnsupportedOperationException(); }  ❌
 *         void fax() { throw new UnsupportedOperationException(); }   ❌
 *         void staple() { throw new UnsupportedOperationException(); } ❌
 *     }
 *     
 *     Problem: SimplePrinter is FORCED to have methods it can't use!
 * 
 */

package solid;

// ═══════════════════════════════════════════════════════════════════════════════
// BAD EXAMPLE - Violating ISP ❌
// ═══════════════════════════════════════════════════════════════════════════════

// FAT interface - too many methods!
interface BadWorker {
    void work();
    void eat();
    void sleep();
    void code();
    void attendMeetings();
    void managePeople();
}

class BadDeveloper implements BadWorker {
    @Override
    public void work() {
        System.out.println("Developer working...");
    }
    
    @Override
    public void eat() {
        System.out.println("Developer eating...");
    }
    
    @Override
    public void sleep() {
        System.out.println("Developer sleeping...");
    }
    
    @Override
    public void code() {
        System.out.println("Developer coding...");
    }
    
    @Override
    public void attendMeetings() {
        System.out.println("Developer in meeting...");
    }
    
    @Override
    public void managePeople() {
        // Developer doesn't manage! But FORCED to implement! ❌
        throw new UnsupportedOperationException("Developer doesn't manage!");
    }
}

class BadRobot implements BadWorker {
    @Override
    public void work() {
        System.out.println("Robot working 24/7...");
    }
    
    @Override
    public void eat() {
        throw new UnsupportedOperationException("Robot doesn't eat!"); // ❌
    }
    
    @Override
    public void sleep() {
        throw new UnsupportedOperationException("Robot doesn't sleep!"); // ❌
    }
    
    @Override
    public void code() {
        throw new UnsupportedOperationException("Robot doesn't code!"); // ❌
    }
    
    @Override
    public void attendMeetings() {
        throw new UnsupportedOperationException("Robot doesn't attend meetings!"); // ❌
    }
    
    @Override
    public void managePeople() {
        throw new UnsupportedOperationException("Robot doesn't manage!"); // ❌
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// GOOD EXAMPLE - Following ISP ✅
// ═══════════════════════════════════════════════════════════════════════════════

// Small, focused interfaces
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

interface Codeable {
    void code();
}

interface Manageable {
    void managePeople();
}

// Developer implements only what it needs
class Developer implements Workable, Eatable, Sleepable, Codeable {
    @Override
    public void work() {
        System.out.println("👨‍💻 Developer working...");
    }
    
    @Override
    public void eat() {
        System.out.println("🍕 Developer eating pizza...");
    }
    
    @Override
    public void sleep() {
        System.out.println("😴 Developer sleeping (rarely)...");
    }
    
    @Override
    public void code() {
        System.out.println("⌨️ Developer coding awesome stuff!");
    }
}

// Manager implements different interfaces
class Manager implements Workable, Eatable, Sleepable, Manageable {
    @Override
    public void work() {
        System.out.println("👔 Manager working...");
    }
    
    @Override
    public void eat() {
        System.out.println("🍽️ Manager having lunch meeting...");
    }
    
    @Override
    public void sleep() {
        System.out.println("😴 Manager sleeping...");
    }
    
    @Override
    public void managePeople() {
        System.out.println("👥 Manager managing team...");
    }
}

// Robot implements ONLY what it can do
class Robot implements Workable {
    @Override
    public void work() {
        System.out.println("🤖 Robot working 24/7 without breaks!");
    }
    // No eat(), sleep(), code() - because Robot doesn't need them! ✅
}


// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Printer/Scanner
// ═══════════════════════════════════════════════════════════════════════════════

// BAD: Fat interface ❌
interface BadMachine {
    void print(String document);
    void scan(String document);
    void fax(String document);
}

// GOOD: Segregated interfaces ✅
interface Printer {
    void print(String document);
}

interface Scanner {
    void scan(String document);
}

interface Fax {
    void fax(String document);
}

// Simple printer - only prints
class SimplePrinter implements Printer {
    @Override
    public void print(String document) {
        System.out.println("🖨️ Printing: " + document);
    }
}

// All-in-one - does everything
class AllInOnePrinter implements Printer, Scanner, Fax {
    @Override
    public void print(String document) {
        System.out.println("🖨️ Printing: " + document);
    }
    
    @Override
    public void scan(String document) {
        System.out.println("📄 Scanning: " + document);
    }
    
    @Override
    public void fax(String document) {
        System.out.println("📠 Faxing: " + document);
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Vehicle Features
// ═══════════════════════════════════════════════════════════════════════════════

interface Drivable {
    void drive();
}

interface Flyable {
    void fly();
}

interface Sailable {
    void sail();
}

interface Submersible {
    void submerge();
}

class Car implements Drivable {
    @Override
    public void drive() {
        System.out.println("🚗 Car driving on road...");
    }
}

class Airplane implements Flyable {
    @Override
    public void fly() {
        System.out.println("✈️ Airplane flying in sky...");
    }
}

class Boat implements Sailable {
    @Override
    public void sail() {
        System.out.println("🚤 Boat sailing on water...");
    }
}

// Amphibious vehicle - multiple capabilities
class AmphibiousVehicle implements Drivable, Sailable {
    @Override
    public void drive() {
        System.out.println("🚙 Amphibious vehicle driving...");
    }
    
    @Override
    public void sail() {
        System.out.println("🚙 Amphibious vehicle sailing...");
    }
}

// Flying car (future!) - multiple capabilities
class FlyingCar implements Drivable, Flyable {
    @Override
    public void drive() {
        System.out.println("🚗 Flying car driving...");
    }
    
    @Override
    public void fly() {
        System.out.println("🚗 Flying car taking off!");
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Repository Pattern
// ═══════════════════════════════════════════════════════════════════════════════

// BAD: Fat repository ❌
interface BadRepository<T> {
    T findById(Long id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(Long id);
    void bulkInsert(List<T> entities);
    void generateReport();
    void sendNotification();
}

// GOOD: Segregated repository ✅
interface ReadRepository<T> {
    T findById(Long id);
    java.util.List<T> findAll();
}

interface WriteRepository<T> {
    void save(T entity);
    void update(T entity);
    void delete(Long id);
}

interface BulkRepository<T> {
    void bulkInsert(java.util.List<T> entities);
}

// Read-only service needs only ReadRepository
class ReportService {
    private ReadRepository<String> repository;
    
    public void generateReport() {
        // Only uses read operations!
    }
}

// Full service can use both
class UserService {
    private ReadRepository<String> readRepo;
    private WriteRepository<String> writeRepo;
    
    // Can read and write!
}


// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════

public class I_InterfaceSegregation {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  INTERFACE SEGREGATION PRINCIPLE (ISP)");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        // Worker Example
        System.out.println("👷 WORKER EXAMPLE:");
        System.out.println("─────────────────────────────────────────────");
        Developer dev = new Developer();
        dev.work();
        dev.code();
        dev.eat();
        
        System.out.println();
        
        Manager mgr = new Manager();
        mgr.work();
        mgr.managePeople();
        
        System.out.println();
        
        Robot robot = new Robot();
        robot.work();
        // robot.eat();  // Doesn't exist! Robot doesn't need it! ✅
        
        System.out.println();
        
        // Printer Example
        System.out.println("🖨️ PRINTER EXAMPLE:");
        System.out.println("─────────────────────────────────────────────");
        SimplePrinter simple = new SimplePrinter();
        simple.print("Hello World");
        // simple.scan();  // Doesn't exist! Simple printer doesn't scan! ✅
        
        AllInOnePrinter allInOne = new AllInOnePrinter();
        allInOne.print("Report.pdf");
        allInOne.scan("Document.pdf");
        allInOne.fax("Contract.pdf");
        
        System.out.println();
        
        // Vehicle Example
        System.out.println("🚗 VEHICLE EXAMPLE:");
        System.out.println("─────────────────────────────────────────────");
        Car car = new Car();
        car.drive();
        
        AmphibiousVehicle amphibious = new AmphibiousVehicle();
        amphibious.drive();
        amphibious.sail();
        
        FlyingCar flyingCar = new FlyingCar();
        flyingCar.drive();
        flyingCar.fly();
        
        System.out.println();
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Interface Segregation Principle");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        System.out.println("  📌 RULE: Many small interfaces > One fat interface\n");
        
        System.out.println("  ❌ BAD SIGNS (Violating ISP):");
        System.out.println("     • Interface has 10+ methods");
        System.out.println("     • Classes implement methods with empty body");
        System.out.println("     • Classes throw UnsupportedOperationException");
        System.out.println("     • Changes to interface affect unrelated classes\n");
        
        System.out.println("  ✅ GOOD SIGNS (Following ISP):");
        System.out.println("     • Small, focused interfaces (3-5 methods)");
        System.out.println("     • Classes implement ALL methods meaningfully");
        System.out.println("     • Easy to add new implementations\n");
        
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"ISP means breaking fat interfaces into smaller ones.");
        System.out.println("      Instead of one Machine interface with print, scan, fax,");
        System.out.println("      I create Printer, Scanner, Fax interfaces. This way,");
        System.out.println("      SimplePrinter only implements Printer, not forced to");
        System.out.println("      implement scan and fax it doesn't support.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * ISP IN SPRING BOOT
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     Spring Data JPA follows ISP:
 *     
 *     interface CrudRepository<T, ID> {
 *         T save(T entity);
 *         Optional<T> findById(ID id);
 *         void delete(T entity);
 *         // Basic CRUD only!
 *     }
 *     
 *     interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {
 *         Page<T> findAll(Pageable pageable);
 *         // Adds paging capabilities!
 *     }
 *     
 *     interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
 *         void flush();
 *         // Adds JPA-specific features!
 *     }
 *     
 *     You pick the interface level you need!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * WHEN TO SPLIT INTERFACES?
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     Ask yourself:
 *     ─────────────
 *     1. "Do all implementers need ALL these methods?"
 *     2. "Are some methods always implemented together?"
 *     3. "Would splitting make implementations simpler?"
 *     
 *     If some methods are often empty/throwing → SPLIT!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * RELATIONSHIP WITH OTHER PRINCIPLES
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     ISP + SRP:
 *     ──────────
 *     SRP = One class, one responsibility
 *     ISP = One interface, one purpose
 *     Both promote focused, single-purpose design!
 *     
 *     ISP + LSP:
 *     ──────────
 *     If you follow ISP, LSP becomes easier!
 *     Small interfaces = Easier to implement correctly
 */
