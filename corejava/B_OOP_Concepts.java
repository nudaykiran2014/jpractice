/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  CORE JAVA - PART 2: OOP CONCEPTS (Object-Oriented Programming)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME 📖
 * ─────────────
 * Imagine you're building with LEGO...
 * 
 *   CLASS = Blueprint/Instructions for building a LEGO car
 *   OBJECT = The actual LEGO car you build from those instructions
 *   
 *   One blueprint → Many cars!
 *   One class → Many objects!
 * 
 * 
 * THE 4 PILLARS OF OOP:
 * ─────────────────────
 *   1. ENCAPSULATION  → Put things in a box, hide the details
 *   2. INHERITANCE    → Child gets properties from parent
 *   3. POLYMORPHISM   → Same action, different behaviors
 *   4. ABSTRACTION    → Hide complexity, show only what's needed
 * 
 */

package corejava;

public class B_OOP_Concepts {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  OOP CONCEPTS - THE 4 PILLARS");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        classAndObject();
        encapsulation();
        inheritance();
        polymorphism();
        abstraction();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * CLASS & OBJECT
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     *   CLASS = Blueprint (defines what properties and behaviors)
     *   OBJECT = Instance (actual thing created from blueprint)
     *   
     *   Think of it like:
     *   ─────────────────
     *   CLASS: Cookie Cutter 🍪
     *   OBJECT: Actual Cookies made from it
     *   
     *   One cookie cutter → Many cookies!
     */
    static void classAndObject() {
        System.out.println("1️⃣ CLASS & OBJECT");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Creating objects from Car class
        Car car1 = new Car("Toyota", "Red");
        Car car2 = new Car("Honda", "Blue");
        
        System.out.println("Created 2 cars from same Car class:");
        car1.displayInfo();
        car2.displayInfo();
        
        car1.drive();
        
        System.out.println("\n📌 Key Points:");
        System.out.println("   • Class = Blueprint (Car)");
        System.out.println("   • Object = Instance (car1, car2)");
        System.out.println("   • 'new' keyword creates objects");
        System.out.println("   • Constructor initializes the object\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * PILLAR 1: ENCAPSULATION
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Think of a TV remote 📺
     * ─────────────────────────────
     *   You press buttons (public methods)
     *   You don't see the circuits inside (private fields)
     *   
     *   ENCAPSULATION = Wrapping data + methods together
     *                   Hiding internal details
     *                   Exposing only what's necessary
     * 
     * HOW TO ACHIEVE:
     *   1. Make fields PRIVATE
     *   2. Provide PUBLIC getters/setters
     */
    static void encapsulation() {
        System.out.println("2️⃣ ENCAPSULATION (Data Hiding)");
        System.out.println("─────────────────────────────────────────────\n");
        
        BankAccount account = new BankAccount("John", 1000);
        
        // Can't access directly: account.balance = -500; ❌
        // Must use methods:
        System.out.println("Initial balance: $" + account.getBalance());
        
        account.deposit(500);
        System.out.println("After deposit $500: $" + account.getBalance());
        
        account.withdraw(200);
        System.out.println("After withdraw $200: $" + account.getBalance());
        
        // Try invalid operation
        account.withdraw(5000);  // Won't work - validation inside!
        
        System.out.println("\n📌 Benefits:");
        System.out.println("   • Data protection (can't set negative balance)");
        System.out.println("   • Controlled access through methods");
        System.out.println("   • Can add validation in setters");
        System.out.println("   • Can change internal implementation without affecting users\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * PILLAR 2: INHERITANCE
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Think of a FAMILY 👨‍👩‍👧
     * ─────────────────────────────
     *   Child inherits features from parents:
     *   - Eye color
     *   - Hair color
     *   - But child can have their own unique features too!
     * 
     * In Java:
     *   Parent class = SUPERCLASS
     *   Child class = SUBCLASS
     *   
     *   Child gets all properties/methods of parent!
     *   Child can ADD new ones or OVERRIDE existing ones.
     */
    static void inheritance() {
        System.out.println("3️⃣ INHERITANCE (IS-A relationship)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Dog IS-AN Animal
        Dog dog = new Dog("Buddy", 3);
        dog.eat();        // Inherited from Animal
        dog.sleep();      // Inherited from Animal
        dog.bark();       // Dog's own method
        
        System.out.println();
        
        // Cat IS-AN Animal
        Cat cat = new Cat("Whiskers", 2);
        cat.eat();        // Inherited from Animal
        cat.meow();       // Cat's own method
        
        System.out.println("\n📌 Inheritance Types in Java:");
        System.out.println("   ✅ Single: A → B");
        System.out.println("   ✅ Multilevel: A → B → C");
        System.out.println("   ✅ Hierarchical: A → B, A → C");
        System.out.println("   ❌ Multiple (with classes): A,B → C (NOT allowed!)");
        System.out.println("   ✅ Multiple (with interfaces): Allowed!\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * PILLAR 3: POLYMORPHISM
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * POLY = Many, MORPH = Forms
     * Same thing, different behaviors!
     * 
     * STORY: Think of a PERSON 🧑
     * ─────────────────────────────
     *   Same person behaves differently:
     *   - At work: Professional
     *   - At home: Casual
     *   - With friends: Fun
     *   
     *   Same person, different behaviors based on situation!
     * 
     * TWO TYPES:
     *   1. Compile-time (Method Overloading) - Same name, different parameters
     *   2. Runtime (Method Overriding) - Child changes parent's method
     */
    static void polymorphism() {
        System.out.println("4️⃣ POLYMORPHISM (Many Forms)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // COMPILE-TIME POLYMORPHISM (Method Overloading)
        System.out.println("A) Method OVERLOADING (Compile-time):");
        Calculator calc = new Calculator();
        System.out.println("   add(5, 3) = " + calc.add(5, 3));           // 2 ints
        System.out.println("   add(5, 3, 2) = " + calc.add(5, 3, 2));     // 3 ints
        System.out.println("   add(5.5, 3.5) = " + calc.add(5.5, 3.5));   // 2 doubles
        
        System.out.println("\n   Same method name, different parameters!");
        System.out.println("   Decided at COMPILE time.\n");
        
        // RUNTIME POLYMORPHISM (Method Overriding)
        System.out.println("B) Method OVERRIDING (Runtime):");
        Animal animal1 = new Dog("Dog", 2);  // Parent reference, Child object
        Animal animal2 = new Cat("Cat", 1);  // Parent reference, Child object
        
        System.out.print("   animal1.makeSound(): ");
        animal1.makeSound();  // Calls Dog's version
        
        System.out.print("   animal2.makeSound(): ");
        animal2.makeSound();  // Calls Cat's version
        
        System.out.println("\n   Same method call, different behavior!");
        System.out.println("   Decided at RUNTIME based on actual object.\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * PILLAR 4: ABSTRACTION
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Think of driving a CAR 🚗
     * ─────────────────────────────────
     *   You know:
     *   - Press accelerator → car moves
     *   - Press brake → car stops
     *   
     *   You DON'T need to know:
     *   - How engine works
     *   - How fuel injection happens
     *   
     *   ABSTRACTION = Showing WHAT it does, hiding HOW it does!
     * 
     * ACHIEVED BY:
     *   1. Abstract Classes (partial abstraction)
     *   2. Interfaces (full abstraction)
     */
    static void abstraction() {
        System.out.println("5️⃣ ABSTRACTION (Hide Complexity)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Using abstract class
        System.out.println("A) Abstract Class:");
        Shape circle = new Circle(5);
        Shape rectangle = new Rectangle(4, 6);
        
        System.out.println("   Circle area: " + circle.calculateArea());
        System.out.println("   Rectangle area: " + rectangle.calculateArea());
        
        // Using interface
        System.out.println("\nB) Interface:");
        Playable guitar = new Guitar();
        Playable piano = new Piano();
        
        System.out.print("   Guitar: ");
        guitar.play();
        System.out.print("   Piano: ");
        piano.play();
        
        System.out.println("\n📌 Abstract Class vs Interface:");
        System.out.println("   ┌─────────────────┬────────────────────┬────────────────────┐");
        System.out.println("   │                 │  Abstract Class    │    Interface       │");
        System.out.println("   ├─────────────────┼────────────────────┼────────────────────┤");
        System.out.println("   │ Methods         │ Abstract + Concrete│ Abstract (+ default)│");
        System.out.println("   │ Variables       │ Any type           │ public static final│");
        System.out.println("   │ Constructor     │ Yes                │ No                 │");
        System.out.println("   │ Multiple inherit│ No                 │ Yes                │");
        System.out.println("   │ Keyword         │ extends            │ implements         │");
        System.out.println("   └─────────────────┴────────────────────┴────────────────────┘");
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: 4 Pillars of OOP");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  📦 ENCAPSULATION  → Wrap data + methods, use private + getters/setters");
        System.out.println("  👨‍👩‍👧 INHERITANCE    → Child inherits from parent (extends)");
        System.out.println("  🎭 POLYMORPHISM   → Same action, different forms (overload/override)");
        System.out.println("  🎨 ABSTRACTION    → Show what, hide how (abstract class/interface)");
        System.out.println();
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"OOP has 4 pillars: Encapsulation hides data using private fields");
        System.out.println("      and public methods. Inheritance allows code reuse through extends.");
        System.out.println("      Polymorphism lets same method behave differently. Abstraction");
        System.out.println("      hides complexity using abstract classes and interfaces.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SUPPORTING CLASSES
// ═══════════════════════════════════════════════════════════════════════════════

// Simple Car class
class Car {
    String brand;
    String color;
    
    // Constructor
    Car(String brand, String color) {
        this.brand = brand;
        this.color = color;
    }
    
    void displayInfo() {
        System.out.println("   " + color + " " + brand);
    }
    
    void drive() {
        System.out.println("   " + brand + " is driving...\n");
    }
}

// Encapsulation example
class BankAccount {
    private String owner;      // Private - can't access directly
    private double balance;    // Private - can't access directly
    
    public BankAccount(String owner, double initialBalance) {
        this.owner = owner;
        this.balance = initialBalance;
    }
    
    // Getter
    public double getBalance() {
        return balance;
    }
    
    // Controlled methods with validation
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
    
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("   ❌ Invalid withdrawal!");
        }
    }
}

// Inheritance example
class Animal {
    String name;
    int age;
    
    Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    void eat() {
        System.out.println("   " + name + " is eating...");
    }
    
    void sleep() {
        System.out.println("   " + name + " is sleeping...");
    }
    
    void makeSound() {
        System.out.println("   Some sound...");
    }
}

class Dog extends Animal {
    Dog(String name, int age) {
        super(name, age);  // Call parent constructor
    }
    
    void bark() {
        System.out.println("   " + name + " says: Woof! Woof!");
    }
    
    @Override
    void makeSound() {
        System.out.println("Woof!");
    }
}

class Cat extends Animal {
    Cat(String name, int age) {
        super(name, age);
    }
    
    void meow() {
        System.out.println("   " + name + " says: Meow!");
    }
    
    @Override
    void makeSound() {
        System.out.println("Meow!");
    }
}

// Polymorphism - Overloading example
class Calculator {
    int add(int a, int b) {
        return a + b;
    }
    
    int add(int a, int b, int c) {
        return a + b + c;
    }
    
    double add(double a, double b) {
        return a + b;
    }
}

// Abstraction - Abstract class example
abstract class Shape {
    abstract double calculateArea();  // Abstract method - no body
    
    void display() {  // Concrete method - has body
        System.out.println("This is a shape");
    }
}

class Circle extends Shape {
    double radius;
    
    Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    double calculateArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    double width, height;
    
    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    double calculateArea() {
        return width * height;
    }
}

// Abstraction - Interface example
interface Playable {
    void play();  // Abstract by default
}

class Guitar implements Playable {
    @Override
    public void play() {
        System.out.println("🎸 Playing guitar strings...");
    }
}

class Piano implements Playable {
    @Override
    public void play() {
        System.out.println("🎹 Playing piano keys...");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q1: What is the difference between Overloading and Overriding?
 * A1: Overloading: Same class, same name, different parameters (compile-time)
 *     Overriding: Different class (inheritance), same signature (runtime)
 * 
 * Q2: Can we override static methods?
 * A2: No! Static methods belong to class, not object. They are hidden, not overridden.
 * 
 * Q3: Can we override private methods?
 * A3: No! Private methods are not inherited, so can't be overridden.
 * 
 * Q4: What is the difference between abstract class and interface?
 * A4: Abstract class can have constructors, concrete methods, any variables.
 *     Interface has no constructor, methods are abstract (except default), variables are final.
 *     A class can extend ONE abstract class but implement MULTIPLE interfaces.
 * 
 * Q5: What is IS-A and HAS-A relationship?
 * A5: IS-A: Inheritance (Dog IS-A Animal)
 *     HAS-A: Composition (Car HAS-A Engine)
 * 
 * Q6: Can abstract class have constructor?
 * A6: Yes! It's called when subclass object is created using super().
 */
