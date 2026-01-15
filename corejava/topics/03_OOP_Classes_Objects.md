# Java OOP - Complete Guide
## Part 3: Classes, Objects, Methods, Constructors

---

# 1. Classes

## 1.1 Class Declaration

```java
// Basic class structure
[modifiers] class ClassName [extends ParentClass] [implements Interface1, Interface2] {
    // Fields (variables)
    // Constructors
    // Methods
    // Nested classes
    // Initializer blocks
}

// Example
public class BankAccount {
    // Fields
    private String accountNumber;
    private double balance;
    
    // Constructor
    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0;
    }
    
    // Methods
    public void deposit(double amount) {
        balance += amount;
    }
    
    public double getBalance() {
        return balance;
    }
}
```

## 1.2 Class Modifiers

```java
// Access modifiers
public class PublicClass { }       // Accessible everywhere
class DefaultClass { }              // Package-private (default)

// Non-access modifiers
public final class FinalClass { }   // Cannot be extended
public abstract class AbstractClass { }  // Cannot be instantiated

// Strictfp - consistent floating-point across platforms
public strictfp class StrictClass { }

// Combination
public abstract strictfp class CombinedClass { }
```

## 1.3 Nested Classes

```java
public class Outer {
    private int outerField = 10;
    
    // 1. Static Nested Class
    public static class StaticNested {
        void display() {
            // Cannot access outerField directly (it's non-static)
            System.out.println("Static nested class");
        }
    }
    
    // 2. Inner Class (Non-static)
    public class Inner {
        void display() {
            // CAN access outerField
            System.out.println("Inner class: " + outerField);
        }
    }
    
    public void method() {
        // 3. Local Class (inside method)
        class LocalClass {
            void display() {
                System.out.println("Local class: " + outerField);
            }
        }
        new LocalClass().display();
        
        // 4. Anonymous Class
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous class: " + outerField);
            }
        };
        r.run();
    }
}

// Usage
Outer outer = new Outer();
Outer.StaticNested staticNested = new Outer.StaticNested();
Outer.Inner inner = outer.new Inner();
```

---

# 2. Fields (Variables)

## 2.1 Instance Fields

```java
public class Person {
    // Instance fields - each object has its own copy
    String name;        // default: null
    int age;            // default: 0
    boolean active;     // default: false
    double salary;      // default: 0.0
}

Person p1 = new Person();
Person p2 = new Person();
p1.name = "Alice";
p2.name = "Bob";  // Different values
```

## 2.2 Static Fields (Class Fields)

```java
public class Counter {
    // Static field - shared by ALL instances
    private static int count = 0;
    
    // Instance field
    private int id;
    
    public Counter() {
        count++;
        this.id = count;
    }
    
    public static int getCount() {
        return count;
    }
}

Counter c1 = new Counter();  // count = 1
Counter c2 = new Counter();  // count = 2
Counter c3 = new Counter();  // count = 3
System.out.println(Counter.getCount());  // 3
```

## 2.3 Final Fields (Constants)

```java
public class Circle {
    // Compile-time constant
    public static final double PI = 3.14159265358979;
    
    // Blank final - set in constructor
    private final double radius;
    
    public Circle(double radius) {
        this.radius = radius;  // Must be set once
    }
    
    // Cannot reassign
    // this.radius = 10;  // Compile error!
}
```

## 2.4 Field Modifiers

```java
public class FieldDemo {
    public String publicField;          // Accessible everywhere
    protected String protectedField;    // Same package + subclasses
    String defaultField;                // Same package only
    private String privateField;        // This class only
    
    static String staticField;          // Class-level
    final String finalField = "const";  // Cannot change
    transient String transientField;    // Not serialized
    volatile int volatileField;         // Thread visibility
}
```

---

# 3. Methods

## 3.1 Method Declaration

```java
[modifiers] returnType methodName([parameters]) [throws ExceptionList] {
    // method body
    [return value;]
}

// Examples
public void displayMessage() {
    System.out.println("Hello");
}

private int add(int a, int b) {
    return a + b;
}

protected static double calculateTax(double amount) throws InvalidAmountException {
    if (amount < 0) throw new InvalidAmountException();
    return amount * 0.1;
}
```

## 3.2 Method Modifiers

```java
// Access modifiers
public void publicMethod() { }
protected void protectedMethod() { }
void defaultMethod() { }       // Package-private
private void privateMethod() { }

// Non-access modifiers
static void staticMethod() { }        // Class-level
final void finalMethod() { }          // Cannot override
abstract void abstractMethod();       // No body (abstract class/interface)
synchronized void syncMethod() { }    // Thread-safe
native void nativeMethod();           // Implemented in C/C++
strictfp void strictMethod() { }      // Strict floating-point
```

## 3.3 Instance vs Static Methods

```java
public class Calculator {
    private int value;
    
    // Instance method - needs object
    public void setValue(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
    // Static method - no object needed
    public static int add(int a, int b) {
        return a + b;
    }
    
    public static int multiply(int a, int b) {
        return a * b;
    }
}

// Usage
Calculator calc = new Calculator();
calc.setValue(10);              // Instance method
int val = calc.getValue();      // Instance method

int sum = Calculator.add(5, 3);  // Static method (no object)
int prod = Calculator.multiply(4, 2);
```

## 3.4 Method Parameters

```java
public class ParameterDemo {
    // Single parameter
    public void greet(String name) {
        System.out.println("Hello, " + name);
    }
    
    // Multiple parameters
    public int add(int a, int b, int c) {
        return a + b + c;
    }
    
    // Varargs (variable arguments) - must be last
    public int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) {
            total += n;
        }
        return total;
    }
    
    // Mixed with varargs
    public void log(String message, Object... args) {
        System.out.printf(message + "%n", args);
    }
}

// Usage
demo.greet("Alice");
demo.add(1, 2, 3);
demo.sum(1, 2);           // works
demo.sum(1, 2, 3, 4, 5);  // works
demo.sum();               // works (empty array)
demo.log("Name: %s, Age: %d", "John", 30);
```

## 3.5 Method Overloading

```java
public class Printer {
    // Same name, different parameters
    public void print(int n) {
        System.out.println("Integer: " + n);
    }
    
    public void print(double d) {
        System.out.println("Double: " + d);
    }
    
    public void print(String s) {
        System.out.println("String: " + s);
    }
    
    public void print(String s, int times) {
        for (int i = 0; i < times; i++) {
            System.out.println(s);
        }
    }
}

Printer p = new Printer();
p.print(42);           // Integer: 42
p.print(3.14);         // Double: 3.14
p.print("Hello");      // String: Hello
p.print("Hi", 3);      // Hi Hi Hi
```

## 3.6 Method Overriding

```java
class Animal {
    public void makeSound() {
        System.out.println("Some sound");
    }
    
    protected String getType() {
        return "Animal";
    }
}

class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Bark!");
    }
    
    // Covariant return - can return subtype
    @Override
    public String getType() {  // Can be more accessible
        return "Dog";
    }
}

Animal animal = new Dog();
animal.makeSound();  // Bark! (runtime polymorphism)
```

## 3.7 Overriding Rules

```java
// 1. Same signature (name + parameters)
// 2. Same or covariant return type
// 3. Same or less restrictive access
// 4. Cannot throw new checked exceptions (can throw less)
// 5. Cannot override static, final, or private methods

class Parent {
    protected Object doSomething() throws IOException {
        return null;
    }
}

class Child extends Parent {
    @Override
    public String doSomething() {  // More accessible, covariant return, fewer exceptions
        return "result";
    }
}
```

---

# 4. Constructors

## 4.1 Default Constructor

```java
public class Person {
    String name;
    int age;
    
    // If no constructor defined, Java provides default:
    // public Person() { }
}

Person p = new Person();  // Uses default constructor
```

## 4.2 No-Argument Constructor

```java
public class Person {
    String name;
    int age;
    
    // Explicit no-arg constructor
    public Person() {
        this.name = "Unknown";
        this.age = 0;
    }
}
```

## 4.3 Parameterized Constructor

```java
public class Person {
    String name;
    int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

Person p = new Person("Alice", 25);
```

## 4.4 Constructor Overloading

```java
public class Person {
    String name;
    int age;
    String email;
    
    public Person() {
        this("Unknown", 0, null);
    }
    
    public Person(String name) {
        this(name, 0, null);
    }
    
    public Person(String name, int age) {
        this(name, age, null);
    }
    
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}
```

## 4.5 Constructor Chaining

```java
public class Employee {
    String name;
    int id;
    double salary;
    
    public Employee() {
        this("New Employee");  // Call another constructor
    }
    
    public Employee(String name) {
        this(name, 0);  // Chain to next
    }
    
    public Employee(String name, int id) {
        this(name, id, 50000);  // Chain to full constructor
    }
    
    public Employee(String name, int id, double salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }
}
```

## 4.6 super() Constructor Call

```java
class Animal {
    String name;
    
    public Animal(String name) {
        this.name = name;
    }
}

class Dog extends Animal {
    String breed;
    
    public Dog(String name, String breed) {
        super(name);  // MUST be first line!
        this.breed = breed;
    }
}
```

## 4.7 Copy Constructor

```java
public class Person {
    String name;
    int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Copy constructor
    public Person(Person other) {
        this.name = other.name;
        this.age = other.age;
    }
}

Person p1 = new Person("Alice", 25);
Person p2 = new Person(p1);  // Copy
```

## 4.8 Private Constructor (Singleton)

```java
public class Singleton {
    private static Singleton instance;
    
    private Singleton() {
        // Private - cannot create outside
    }
    
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}

// Usage
Singleton s1 = Singleton.getInstance();
Singleton s2 = Singleton.getInstance();
System.out.println(s1 == s2);  // true (same instance)
```

---

# 5. Initializer Blocks

## 5.1 Instance Initializer Block

```java
public class Demo {
    int value;
    
    // Instance initializer - runs before constructor
    {
        System.out.println("Instance initializer");
        value = 10;
    }
    
    public Demo() {
        System.out.println("Constructor");
    }
}

// Output when creating new Demo():
// Instance initializer
// Constructor
```

## 5.2 Static Initializer Block

```java
public class Config {
    static Map<String, String> settings;
    
    // Static initializer - runs once when class loads
    static {
        System.out.println("Static initializer");
        settings = new HashMap<>();
        settings.put("debug", "true");
        settings.put("version", "1.0");
    }
    
    public Config() {
        System.out.println("Constructor");
    }
}

// Static block runs before any constructor
```

## 5.3 Initialization Order

```java
public class InitOrder {
    static int staticVar = 1;
    int instanceVar = 2;
    
    static { System.out.println("1. Static block: " + staticVar); }
    { System.out.println("3. Instance block: " + instanceVar); }
    
    public InitOrder() {
        System.out.println("4. Constructor");
    }
    
    public static void main(String[] args) {
        System.out.println("2. Main method");
        new InitOrder();
    }
}

// Output:
// 1. Static block: 1
// 2. Main method
// 3. Instance block: 2
// 4. Constructor
```

---

# 6. this and super Keywords

## 6.1 this Keyword

```java
public class Person {
    String name;
    int age;
    
    public Person(String name, int age) {
        // Distinguish from parameters
        this.name = name;
        this.age = age;
    }
    
    public void setName(String name) {
        this.name = name;  // this.name = field, name = parameter
    }
    
    public Person copy() {
        return this;  // Return current object
    }
    
    public void display() {
        this.printDetails();  // Call another method
    }
    
    private void printDetails() {
        System.out.println(name + ", " + age);
    }
}
```

## 6.2 super Keyword

```java
class Animal {
    String name = "Animal";
    
    public void display() {
        System.out.println("Animal display");
    }
}

class Dog extends Animal {
    String name = "Dog";
    
    public void display() {
        super.display();  // Call parent method
        System.out.println("Dog display");
    }
    
    public void showNames() {
        System.out.println(name);        // Dog
        System.out.println(this.name);   // Dog
        System.out.println(super.name);  // Animal
    }
}
```

---

# 7. Object Class Methods

## 7.1 toString()

```java
public class Person {
    String name;
    int age;
    
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

Person p = new Person();
p.name = "Alice";
p.age = 25;
System.out.println(p);  // Person{name='Alice', age=25}
```

## 7.2 equals() and hashCode()

```java
public class Person {
    String name;
    int age;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

Person p1 = new Person("Alice", 25);
Person p2 = new Person("Alice", 25);
System.out.println(p1.equals(p2));  // true
System.out.println(p1.hashCode() == p2.hashCode());  // true
```

## 7.3 clone()

```java
public class Person implements Cloneable {
    String name;
    int age;
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();  // Shallow copy
    }
}

// Deep copy example
public class Employee implements Cloneable {
    String name;
    Address address;  // Reference type
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Employee cloned = (Employee) super.clone();
        cloned.address = (Address) address.clone();  // Deep copy
        return cloned;
    }
}
```

## 7.4 getClass()

```java
Person p = new Person();
Class<?> clazz = p.getClass();

System.out.println(clazz.getName());          // com.example.Person
System.out.println(clazz.getSimpleName());    // Person
System.out.println(clazz.getSuperclass());    // class java.lang.Object
```

---

# Summary

| Concept | Description |
|---------|-------------|
| Class | Blueprint for objects |
| Object | Instance of a class |
| Field | Variable in a class |
| Method | Function in a class |
| Constructor | Special method to create objects |
| this | Reference to current object |
| super | Reference to parent class |
| static | Belongs to class, not instance |
| final | Cannot be changed/overridden/extended |
