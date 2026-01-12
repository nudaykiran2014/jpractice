/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  L - LISKOV SUBSTITUTION PRINCIPLE (LSP)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * DEFINITION:
 * ───────────
 *     "Subtypes must be substitutable for their base types."
 *     
 *     In simple terms: If B extends A, you should be able to use B wherever A is used
 *                      WITHOUT breaking anything!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * STORY TIME 📖
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Imagine you order a "Bird" from a pet store...
 * 
 * BAD Design (Violating LSP):
 * ───────────────────────────
 *     class Bird {
 *         void fly() { ... }
 *     }
 *     
 *     class Sparrow extends Bird {
 *         void fly() { System.out.println("Flying high!"); }  ✅
 *     }
 *     
 *     class Penguin extends Bird {
 *         void fly() { throw new Exception("I can't fly!"); }  ❌
 *     }
 *     
 *     Problem:
 *     ────────
 *     Bird bird = new Penguin();
 *     bird.fly();  // 💥 CRASH! Penguin can't fly!
 *     
 *     You expected a Bird that can fly, but Penguin broke that expectation!
 *     Penguin is NOT substitutable for Bird!
 * 
 * 
 * GOOD Design (Following LSP):
 * ────────────────────────────
 *     class Bird {
 *         void eat() { ... }
 *     }
 *     
 *     class FlyingBird extends Bird {
 *         void fly() { ... }
 *     }
 *     
 *     class Sparrow extends FlyingBird {
 *         void fly() { System.out.println("Flying!"); }  ✅
 *     }
 *     
 *     class Penguin extends Bird {
 *         void swim() { System.out.println("Swimming!"); }  ✅
 *     }
 *     
 *     Now: Penguin doesn't promise to fly, so no broken expectations!
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * THE CLASSIC PROBLEM: Rectangle vs Square
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     Mathematically: Square IS-A Rectangle (special case)
 *     But in code: Square should NOT extend Rectangle!
 *     
 *     Why?
 *     ────
 *     class Rectangle {
 *         int width, height;
 *         
 *         void setWidth(int w) { width = w; }
 *         void setHeight(int h) { height = h; }
 *         int getArea() { return width * height; }
 *     }
 *     
 *     class Square extends Rectangle {
 *         // Square must have equal sides!
 *         void setWidth(int w) { width = w; height = w; }  // Changes both!
 *         void setHeight(int h) { width = h; height = h; } // Changes both!
 *     }
 *     
 *     
 *     Problem:
 *     ────────
 *     Rectangle rect = new Square();
 *     rect.setWidth(5);
 *     rect.setHeight(10);
 *     
 *     // Expected: 5 * 10 = 50
 *     // Actual: 10 * 10 = 100  💥 WRONG!
 *     
 *     Square is NOT substitutable for Rectangle!
 * 
 */

package solid;

// ═══════════════════════════════════════════════════════════════════════════════
// BAD EXAMPLE - Violating LSP ❌
// ═══════════════════════════════════════════════════════════════════════════════

class BadRectangle {
    protected int width;
    protected int height;
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public int getArea() {
        return width * height;
    }
}

class BadSquare extends BadRectangle {
    // Square MUST have equal sides, so we override
    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width;  // Forces equal! ❌
    }
    
    @Override
    public void setHeight(int height) {
        this.width = height;  // Forces equal! ❌
        this.height = height;
    }
}

// This function expects Rectangle behavior
class BadShapeTest {
    public static void testRectangle(BadRectangle rect) {
        rect.setWidth(5);
        rect.setHeight(10);
        
        // Expected area: 5 * 10 = 50
        int expectedArea = 50;
        int actualArea = rect.getArea();
        
        System.out.println("Expected: " + expectedArea);
        System.out.println("Actual: " + actualArea);
        System.out.println("Test passed: " + (expectedArea == actualArea));
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// GOOD EXAMPLE - Following LSP ✅
// ═══════════════════════════════════════════════════════════════════════════════

// Solution 1: Use interface with common behavior
interface Shape {
    int getArea();
}

class Rectangle implements Shape {
    protected int width;
    protected int height;
    
    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public int getArea() {
        return width * height;
    }
}

class Square implements Shape {
    private int side;
    
    public Square(int side) {
        this.side = side;
    }
    
    @Override
    public int getArea() {
        return side * side;
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Bird Problem
// ═══════════════════════════════════════════════════════════════════════════════

// BAD: All birds can fly? ❌
class BadBird {
    public void fly() {
        System.out.println("Flying...");
    }
}

class BadPenguin extends BadBird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguins can't fly!"); // ❌
    }
}


// GOOD: Separate flying birds from non-flying ✅
interface Bird {
    void eat();
    void makeSound();
}

interface FlyingBird extends Bird {
    void fly();
}

interface SwimmingBird extends Bird {
    void swim();
}

class Sparrow implements FlyingBird {
    @Override
    public void eat() {
        System.out.println("🐦 Sparrow eating seeds");
    }
    
    @Override
    public void makeSound() {
        System.out.println("🐦 Chirp chirp!");
    }
    
    @Override
    public void fly() {
        System.out.println("🐦 Sparrow flying high!");
    }
}

class Penguin implements SwimmingBird {
    @Override
    public void eat() {
        System.out.println("🐧 Penguin eating fish");
    }
    
    @Override
    public void makeSound() {
        System.out.println("🐧 Honk honk!");
    }
    
    @Override
    public void swim() {
        System.out.println("🐧 Penguin swimming fast!");
    }
}

class Duck implements FlyingBird, SwimmingBird {
    @Override
    public void eat() {
        System.out.println("🦆 Duck eating bread");
    }
    
    @Override
    public void makeSound() {
        System.out.println("🦆 Quack quack!");
    }
    
    @Override
    public void fly() {
        System.out.println("🦆 Duck flying!");
    }
    
    @Override
    public void swim() {
        System.out.println("🦆 Duck swimming!");
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Database Connection
// ═══════════════════════════════════════════════════════════════════════════════

interface DatabaseConnection {
    void connect();
    void disconnect();
    void executeQuery(String query);
}

class MySQLConnection implements DatabaseConnection {
    @Override
    public void connect() {
        System.out.println("🔌 Connected to MySQL");
    }
    
    @Override
    public void disconnect() {
        System.out.println("🔌 Disconnected from MySQL");
    }
    
    @Override
    public void executeQuery(String query) {
        System.out.println("📝 MySQL executing: " + query);
    }
}

class PostgreSQLConnection implements DatabaseConnection {
    @Override
    public void connect() {
        System.out.println("🔌 Connected to PostgreSQL");
    }
    
    @Override
    public void disconnect() {
        System.out.println("🔌 Disconnected from PostgreSQL");
    }
    
    @Override
    public void executeQuery(String query) {
        System.out.println("📝 PostgreSQL executing: " + query);
    }
}

// Both are fully substitutable! ✅
class DatabaseService {
    public void runQuery(DatabaseConnection connection, String query) {
        connection.connect();
        connection.executeQuery(query);
        connection.disconnect();
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════

public class L_LiskovSubstitution {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  LISKOV SUBSTITUTION PRINCIPLE (LSP)");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        // BAD Example - Rectangle/Square
        System.out.println("❌ BAD EXAMPLE (Rectangle/Square Problem):");
        System.out.println("─────────────────────────────────────────────");
        System.out.println("Testing with Rectangle:");
        BadShapeTest.testRectangle(new BadRectangle());
        
        System.out.println("\nTesting with Square (substituted):");
        BadShapeTest.testRectangle(new BadSquare());  // 💥 Breaks!
        System.out.println("^ Square breaks Rectangle's contract!\n");
        
        // GOOD Example - Shapes
        System.out.println("✅ GOOD EXAMPLE (Proper Shape Design):");
        System.out.println("─────────────────────────────────────────────");
        Shape rect = new Rectangle(5, 10);
        Shape square = new Square(5);
        
        System.out.println("Rectangle area: " + rect.getArea());
        System.out.println("Square area: " + square.getArea());
        System.out.println("Both work correctly!\n");
        
        // Bird Example
        System.out.println("🐦 BIRD EXAMPLE (Proper Hierarchy):");
        System.out.println("─────────────────────────────────────────────");
        FlyingBird sparrow = new Sparrow();
        SwimmingBird penguin = new Penguin();
        Duck duck = new Duck();
        
        sparrow.fly();        // ✅ Works
        penguin.swim();       // ✅ Works
        duck.fly();           // ✅ Works
        duck.swim();          // ✅ Works
        System.out.println();
        
        // Database Example
        System.out.println("🗄️ DATABASE EXAMPLE (Substitutable Connections):");
        System.out.println("─────────────────────────────────────────────");
        DatabaseService service = new DatabaseService();
        
        service.runQuery(new MySQLConnection(), "SELECT * FROM users");
        System.out.println();
        service.runQuery(new PostgreSQLConnection(), "SELECT * FROM users");
        System.out.println("^ Both connections are fully substitutable!\n");
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Liskov Substitution Principle");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        System.out.println("  📌 RULE: Subclass should be usable in place of parent\n");
        
        System.out.println("  ❌ BAD SIGNS (Violating LSP):");
        System.out.println("     • Subclass throws exception for inherited method");
        System.out.println("     • Subclass returns null where parent returns object");
        System.out.println("     • Subclass changes behavior unexpectedly");
        System.out.println("     • instanceof checks before calling methods\n");
        
        System.out.println("  ✅ LSP RULES:");
        System.out.println("     • Preconditions can't be strengthened in subclass");
        System.out.println("     • Postconditions can't be weakened in subclass");
        System.out.println("     • Behavior must match parent's contract\n");
        
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"LSP means if I have a function that takes a Parent class,");
        System.out.println("      I should be able to pass any Child class and it should work.");
        System.out.println("      If Penguin extends Bird but can't fly, it violates LSP.");
        System.out.println("      Solution: Better hierarchy - Bird, FlyingBird, SwimmingBird.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * LSP RULES (Formal)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     1. SIGNATURE RULE:
 *        - Method parameters can be same or more general
 *        - Return type can be same or more specific
 *     
 *     2. PRECONDITIONS:
 *        - Subclass can't demand MORE than parent
 *        - If parent accepts null, subclass must too
 *     
 *     3. POSTCONDITIONS:
 *        - Subclass can't promise LESS than parent
 *        - If parent never returns null, subclass shouldn't either
 *     
 *     4. INVARIANTS:
 *        - Properties that are true for parent must be true for child
 *     
 *     5. HISTORY CONSTRAINT:
 *        - Subclass shouldn't allow state changes parent doesn't allow
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * COMMON VIOLATIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     1. Throwing exceptions in overridden methods
 *        class Child extends Parent {
 *            void doSomething() {
 *                throw new UnsupportedOperationException();  // ❌
 *            }
 *        }
 *     
 *     2. Returning null when parent returns object
 *        class Child extends Parent {
 *            Object getValue() {
 *                return null;  // Parent never returned null! ❌
 *            }
 *        }
 *     
 *     3. Changing expected behavior
 *        class Stack extends ArrayList {
 *            // Stack should be LIFO, ArrayList is not! ❌
 *        }
 * 
 * 
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * HOW TO FIX LSP VIOLATIONS?
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 *     1. USE COMPOSITION over INHERITANCE
 *        - Instead of Square extends Rectangle
 *        - Square HAS-A size, Rectangle HAS-A width, height
 *     
 *     2. CREATE BETTER HIERARCHIES
 *        - Bird → FlyingBird, SwimmingBird
 *        - Vehicle → MotorVehicle, NonMotorVehicle
 *     
 *     3. USE INTERFACES
 *        - Define contracts clearly
 *        - Implement only what makes sense
 */
