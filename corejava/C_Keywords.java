/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  CORE JAVA - PART 3: IMPORTANT KEYWORDS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Java has 50+ reserved keywords. Let's focus on the MOST IMPORTANT ones:
 * 
 *   static  → Belongs to CLASS, not object
 *   final   → Cannot change (constant)
 *   this    → Current object reference
 *   super   → Parent class reference
 *   abstract → Incomplete, must be implemented
 *   synchronized → Thread-safe
 *   volatile → Thread visibility
 *   transient → Skip during serialization
 * 
 */

package corejava;

public class C_Keywords {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  JAVA KEYWORDS");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        staticKeyword();
        finalKeyword();
        thisKeyword();
        superKeyword();
        accessModifiers();
        otherKeywords();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * STATIC KEYWORD
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Think of a SCHOOL 🏫
     * ──────────────────────────
     *   School name = SAME for all students (static)
     *   Student name = DIFFERENT for each student (non-static)
     *   
     *   static = Belongs to CLASS, shared by all objects
     *   non-static = Belongs to OBJECT, each has its own copy
     */
    static void staticKeyword() {
        System.out.println("1️⃣ STATIC KEYWORD");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Static variable - shared by all
        System.out.println("A) Static Variable (shared by all):");
        Student s1 = new Student("Alice");
        Student s2 = new Student("Bob");
        Student s3 = new Student("Charlie");
        
        System.out.println("   Total students: " + Student.studentCount);  // Access via class
        System.out.println("   School name: " + Student.schoolName);       // Same for all
        
        // Static method
        System.out.println("\nB) Static Method:");
        System.out.println("   Called via class: MathUtils.square(5) = " + MathUtils.square(5));
        System.out.println("   No object needed!");
        
        // Static block
        System.out.println("\nC) Static Block:");
        System.out.println("   Runs ONCE when class is loaded");
        System.out.println("   Used for initialization");
        new StaticBlockDemo();  // Static block runs first
        
        System.out.println("\n📌 Static Rules:");
        System.out.println("   • Static can access ONLY static members directly");
        System.out.println("   • Non-static can access both static and non-static");
        System.out.println("   • Static methods can't use 'this' or 'super'");
        System.out.println("   • Main method is static (no object needed to start program)\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * FINAL KEYWORD
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Think of PERMANENT MARKER ✒️
     * ───────────────────────────────────
     *   Once you write with permanent marker, you can't erase it!
     *   
     *   final variable  → Value can't change (constant)
     *   final method    → Can't be overridden
     *   final class     → Can't be extended
     */
    static void finalKeyword() {
        System.out.println("2️⃣ FINAL KEYWORD");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Final variable (constant)
        System.out.println("A) Final Variable (constant):");
        final int MAX_SIZE = 100;
        // MAX_SIZE = 200;  // ❌ ERROR! Can't change
        System.out.println("   final int MAX_SIZE = " + MAX_SIZE);
        System.out.println("   Can't change value!");
        
        // Final with reference
        System.out.println("\nB) Final Reference (tricky!):");
        final int[] arr = {1, 2, 3};
        arr[0] = 100;  // ✅ OK! Can change CONTENTS
        // arr = new int[5];  // ❌ ERROR! Can't reassign reference
        System.out.println("   final int[] arr = {1,2,3}");
        System.out.println("   arr[0] = 100 ✅ (can change contents)");
        System.out.println("   arr = new int[5] ❌ (can't reassign reference)");
        
        // Final method
        System.out.println("\nC) Final Method:");
        System.out.println("   class Parent { final void show() {...} }");
        System.out.println("   class Child extends Parent {");
        System.out.println("       void show() {...}  // ❌ ERROR! Can't override");
        System.out.println("   }");
        
        // Final class
        System.out.println("\nD) Final Class:");
        System.out.println("   final class String {...}");
        System.out.println("   class MyString extends String {...}  // ❌ ERROR!");
        System.out.println("   Examples: String, Integer, Math are final classes");
        
        System.out.println("\n📌 Final Summary:");
        System.out.println("   • final variable → Constant, can't reassign");
        System.out.println("   • final method   → Can't override in child class");
        System.out.println("   • final class    → Can't extend/inherit\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * THIS KEYWORD
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Think of pointing to YOURSELF 👆
     * ──────────────────────────────────────
     *   When someone asks "Who ate the cake?"
     *   You point to yourself: "THIS person" (meaning you)
     *   
     *   this = Reference to CURRENT object
     */
    static void thisKeyword() {
        System.out.println("3️⃣ THIS KEYWORD");
        System.out.println("─────────────────────────────────────────────\n");
        
        // this to differentiate
        System.out.println("A) Differentiate instance variable from parameter:");
        System.out.println("   class Person {");
        System.out.println("       String name;");
        System.out.println("       Person(String name) {");
        System.out.println("           this.name = name;  // this.name = instance variable");
        System.out.println("       }                      // name = parameter");
        System.out.println("   }");
        
        // this() for constructor chaining
        System.out.println("\nB) Constructor Chaining with this():");
        ThisDemo obj = new ThisDemo("John", 25);
        System.out.println("   " + obj);
        
        // this to pass current object
        System.out.println("\nC) Pass current object:");
        System.out.println("   someMethod(this);  // Pass myself to another method");
        
        // this to return current object (method chaining)
        System.out.println("\nD) Method Chaining (return this):");
        Builder builder = new Builder()
            .setName("Product")
            .setPrice(100)
            .setQuantity(5);
        System.out.println("   " + builder);
        
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * SUPER KEYWORD
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Think of calling your PARENT 📞
     * ─────────────────────────────────────
     *   When you need something from your parent:
     *   "Hey SUPER (parent), can you help?"
     *   
     *   super = Reference to PARENT class
     */
    static void superKeyword() {
        System.out.println("4️⃣ SUPER KEYWORD");
        System.out.println("─────────────────────────────────────────────\n");
        
        // super() to call parent constructor
        System.out.println("A) Call Parent Constructor:");
        ChildClass child = new ChildClass("John", 25);
        
        // super to access parent method
        System.out.println("\nB) Call Parent Method:");
        child.display();
        
        // super to access parent variable
        System.out.println("\nC) Access Parent Variable:");
        child.showValues();
        
        System.out.println("\n📌 this vs super:");
        System.out.println("   ┌────────────────────┬────────────────────┐");
        System.out.println("   │       this         │       super        │");
        System.out.println("   ├────────────────────┼────────────────────┤");
        System.out.println("   │ Current object     │ Parent class       │");
        System.out.println("   │ this() = own       │ super() = parent   │");
        System.out.println("   │ constructor        │ constructor        │");
        System.out.println("   │ this.var = own     │ super.var = parent │");
        System.out.println("   │ variable           │ variable           │");
        System.out.println("   └────────────────────┴────────────────────┘\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * ACCESS MODIFIERS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     *   public    → Accessible from ANYWHERE
     *   protected → Same package + child classes
     *   default   → Same package only (no keyword)
     *   private   → Same class only
     */
    static void accessModifiers() {
        System.out.println("5️⃣ ACCESS MODIFIERS");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   ┌─────────────┬───────┬─────────┬──────────┬───────────┐");
        System.out.println("   │  Modifier   │ Class │ Package │ Subclass │ Everywhere│");
        System.out.println("   ├─────────────┼───────┼─────────┼──────────┼───────────┤");
        System.out.println("   │  private    │  ✅   │   ❌    │    ❌    │    ❌     │");
        System.out.println("   │  default    │  ✅   │   ✅    │    ❌    │    ❌     │");
        System.out.println("   │  protected  │  ✅   │   ✅    │    ✅    │    ❌     │");
        System.out.println("   │  public     │  ✅   │   ✅    │    ✅    │    ✅     │");
        System.out.println("   └─────────────┴───────┴─────────┴──────────┴───────────┘");
        
        System.out.println("\n   Memory trick: private < default < protected < public");
        System.out.println("   (Least accessible → Most accessible)\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * OTHER IMPORTANT KEYWORDS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void otherKeywords() {
        System.out.println("6️⃣ OTHER IMPORTANT KEYWORDS");
        System.out.println("─────────────────────────────────────────────\n");
        
        // instanceof
        System.out.println("A) instanceof (check type):");
        Object obj = "Hello";
        System.out.println("   \"Hello\" instanceof String: " + (obj instanceof String));
        System.out.println("   \"Hello\" instanceof Integer: " + (obj instanceof Integer));
        
        // abstract
        System.out.println("\nB) abstract:");
        System.out.println("   • abstract class → Can't create object");
        System.out.println("   • abstract method → No body, must override");
        
        // synchronized
        System.out.println("\nC) synchronized (thread-safe):");
        System.out.println("   • Only one thread can enter at a time");
        System.out.println("   • synchronized void method() {...}");
        
        // volatile
        System.out.println("\nD) volatile (thread visibility):");
        System.out.println("   • Changes visible to all threads immediately");
        System.out.println("   • volatile boolean flag = true;");
        
        // transient
        System.out.println("\nE) transient (skip serialization):");
        System.out.println("   • transient String password;");
        System.out.println("   • Won't be saved when object is serialized");
        
        // strictfp
        System.out.println("\nF) strictfp (strict floating-point):");
        System.out.println("   • Ensures same floating-point results on all platforms");
        
        // native
        System.out.println("\nG) native:");
        System.out.println("   • Method implemented in C/C++ (JNI)");
        System.out.println("   • native void nativeMethod();");
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Java Keywords");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  📌 STATIC:  Belongs to class, shared by all objects");
        System.out.println("  📌 FINAL:   Can't change (variable), override (method), extend (class)");
        System.out.println("  📌 THIS:    Current object reference");
        System.out.println("  📌 SUPER:   Parent class reference");
        System.out.println();
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"static means class-level, no object needed. final means");
        System.out.println("      constant/immutable. this refers to current object, super");
        System.out.println("      refers to parent. Access: private < default < protected < public\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SUPPORTING CLASSES
// ═══════════════════════════════════════════════════════════════════════════════

class Student {
    static String schoolName = "ABC School";  // Shared by all
    static int studentCount = 0;              // Shared by all
    
    String name;  // Each student has different name
    
    Student(String name) {
        this.name = name;
        studentCount++;  // Increment shared counter
    }
}

class MathUtils {
    static int square(int x) {
        return x * x;
    }
    
    static int cube(int x) {
        return x * x * x;
    }
}

class StaticBlockDemo {
    static {
        System.out.println("   Static block executed! (runs once when class loads)");
    }
    
    {
        System.out.println("   Instance block executed! (runs for each object)");
    }
    
    StaticBlockDemo() {
        System.out.println("   Constructor executed!");
    }
}

class ThisDemo {
    String name;
    int age;
    
    ThisDemo() {
        this("Unknown", 0);  // Call another constructor
    }
    
    ThisDemo(String name) {
        this(name, 0);  // Call another constructor
    }
    
    ThisDemo(String name, int age) {
        this.name = name;  // this.name = instance, name = parameter
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "ThisDemo{name='" + name + "', age=" + age + "}";
    }
}

class Builder {
    String name;
    int price;
    int quantity;
    
    Builder setName(String name) {
        this.name = name;
        return this;  // Return current object for chaining
    }
    
    Builder setPrice(int price) {
        this.price = price;
        return this;
    }
    
    Builder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
    
    @Override
    public String toString() {
        return "Builder{name='" + name + "', price=" + price + ", quantity=" + quantity + "}";
    }
}

class ParentClass {
    String name = "Parent";
    int value = 100;
    
    ParentClass() {
        System.out.println("   Parent default constructor called");
    }
    
    ParentClass(String name) {
        this.name = name;
        System.out.println("   Parent parameterized constructor called: " + name);
    }
    
    void display() {
        System.out.println("   Parent's display method");
    }
}

class ChildClass extends ParentClass {
    String name = "Child";
    int value = 200;
    
    ChildClass(String name, int age) {
        super(name);  // Call parent constructor
        System.out.println("   Child constructor called, age: " + age);
    }
    
    @Override
    void display() {
        super.display();  // Call parent method
        System.out.println("   Child's display method");
    }
    
    void showValues() {
        System.out.println("   this.value (child): " + this.value);
        System.out.println("   super.value (parent): " + super.value);
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q1: Can we override static methods?
 * A1: No! Static methods belong to class, not object. They are HIDDEN, not overridden.
 * 
 * Q2: Can we have static constructor?
 * A2: No! Constructor creates objects, but static belongs to class not object.
 *     Use static block instead for static initialization.
 * 
 * Q3: What is the order of execution: static block, instance block, constructor?
 * A3: Static block (once) → Instance block → Constructor (for each object)
 * 
 * Q4: Can final method be overloaded?
 * A4: Yes! Final prevents OVERRIDING, not OVERLOADING.
 * 
 * Q5: Can we change the value of final array?
 * A5: Yes! final prevents reassigning reference, not modifying contents.
 *     final int[] arr = {1,2,3}; arr[0]=100; ✅
 *     arr = new int[5]; ❌
 * 
 * Q6: What happens if super() and this() both are used in constructor?
 * A6: Compilation error! Only one can be first statement. Can't use both.
 */
