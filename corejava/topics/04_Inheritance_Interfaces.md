# Java Inheritance & Interfaces - Complete Guide
## Part 4: Inheritance, Polymorphism, Abstract Classes, Interfaces

---

# 1. Inheritance

## 1.1 extends Keyword

```java
// Parent/Super/Base class
class Animal {
    String name;
    
    void eat() {
        System.out.println(name + " is eating");
    }
    
    void sleep() {
        System.out.println(name + " is sleeping");
    }
}

// Child/Sub/Derived class
class Dog extends Animal {
    String breed;
    
    void bark() {
        System.out.println(name + " is barking");
    }
}

// Usage
Dog dog = new Dog();
dog.name = "Buddy";  // Inherited from Animal
dog.breed = "Labrador";  // Own field
dog.eat();    // Inherited method
dog.bark();   // Own method
```

## 1.2 Types of Inheritance

```java
// 1. SINGLE INHERITANCE
class A { }
class B extends A { }  // B inherits from A

// 2. MULTILEVEL INHERITANCE
class Animal { }
class Mammal extends Animal { }
class Dog extends Mammal { }  // Dog → Mammal → Animal

// 3. HIERARCHICAL INHERITANCE
class Animal { }
class Dog extends Animal { }
class Cat extends Animal { }
class Bird extends Animal { }

// 4. MULTIPLE INHERITANCE (via interfaces only)
interface Flyable { void fly(); }
interface Swimmable { void swim(); }
class Duck implements Flyable, Swimmable {
    public void fly() { }
    public void swim() { }
}

// ❌ NOT ALLOWED: class extends multiple classes
// class C extends A, B { }  // Compile error!
```

## 1.3 What is Inherited?

```java
class Parent {
    public int publicField;
    protected int protectedField;
    int defaultField;           // Package-private
    private int privateField;   // NOT inherited
    
    public void publicMethod() { }
    protected void protectedMethod() { }
    void defaultMethod() { }
    private void privateMethod() { }  // NOT inherited
    
    static void staticMethod() { }     // Inherited (but not overridden)
    final void finalMethod() { }       // Inherited (cannot override)
}

class Child extends Parent {
    // Has access to: publicField, protectedField, defaultField (same package)
    // Has access to: publicMethod, protectedMethod, defaultMethod (same package)
    // Has access to: staticMethod, finalMethod
    // NO access to: privateField, privateMethod
}
```

## 1.4 Constructor Inheritance

```java
// Constructors are NOT inherited!
class Parent {
    public Parent() {
        System.out.println("Parent constructor");
    }
    
    public Parent(String name) {
        System.out.println("Parent: " + name);
    }
}

class Child extends Parent {
    public Child() {
        super();  // Implicit call to Parent()
        System.out.println("Child constructor");
    }
    
    public Child(String name) {
        super(name);  // Explicit call to Parent(String)
        System.out.println("Child: " + name);
    }
    
    // Cannot call: new Child("name", 10) - Parent doesn't have this
}
```

---

# 2. super Keyword

## 2.1 super() - Call Parent Constructor

```java
class Animal {
    String name;
    int age;
    
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class Dog extends Animal {
    String breed;
    
    public Dog(String name, int age, String breed) {
        super(name, age);  // MUST be first statement!
        this.breed = breed;
    }
}
```

## 2.2 super.method() - Call Parent Method

```java
class Animal {
    void display() {
        System.out.println("I am an animal");
    }
}

class Dog extends Animal {
    @Override
    void display() {
        super.display();  // Call parent's display()
        System.out.println("I am a dog");
    }
}

// Output:
// I am an animal
// I am a dog
```

## 2.3 super.field - Access Parent Field

```java
class Parent {
    String name = "Parent";
}

class Child extends Parent {
    String name = "Child";
    
    void showNames() {
        System.out.println(name);        // Child
        System.out.println(this.name);   // Child
        System.out.println(super.name);  // Parent
    }
}
```

---

# 3. Polymorphism

## 3.1 Compile-Time Polymorphism (Method Overloading)

```java
class Calculator {
    // Same method name, different parameters
    int add(int a, int b) {
        return a + b;
    }
    
    double add(double a, double b) {
        return a + b;
    }
    
    int add(int a, int b, int c) {
        return a + b + c;
    }
}

Calculator calc = new Calculator();
calc.add(1, 2);        // Calls int version
calc.add(1.5, 2.5);    // Calls double version
calc.add(1, 2, 3);     // Calls three-param version
// Decision made at COMPILE time
```

## 3.2 Runtime Polymorphism (Method Overriding)

```java
class Animal {
    void makeSound() {
        System.out.println("Some generic sound");
    }
}

class Dog extends Animal {
    @Override
    void makeSound() {
        System.out.println("Bark!");
    }
}

class Cat extends Animal {
    @Override
    void makeSound() {
        System.out.println("Meow!");
    }
}

// Runtime polymorphism
Animal animal1 = new Dog();
Animal animal2 = new Cat();
Animal animal3 = new Animal();

animal1.makeSound();  // Bark! (decided at RUNTIME)
animal2.makeSound();  // Meow! (decided at RUNTIME)
animal3.makeSound();  // Some generic sound
```

## 3.3 Upcasting and Downcasting

```java
class Animal { }
class Dog extends Animal {
    void bark() { System.out.println("Bark!"); }
}

// UPCASTING (implicit) - Child to Parent
Dog dog = new Dog();
Animal animal = dog;  // Automatic

// DOWNCASTING (explicit) - Parent to Child
Animal animal2 = new Dog();
Dog dog2 = (Dog) animal2;  // Must cast
dog2.bark();

// ⚠️ ClassCastException if wrong type!
Animal animal3 = new Animal();
// Dog dog3 = (Dog) animal3;  // Runtime error!

// Safe downcasting with instanceof
if (animal3 instanceof Dog d) {
    d.bark();
}
```

## 3.4 Dynamic Method Dispatch

```java
class Shape {
    void draw() { System.out.println("Drawing shape"); }
}

class Circle extends Shape {
    @Override
    void draw() { System.out.println("Drawing circle"); }
}

class Square extends Shape {
    @Override
    void draw() { System.out.println("Drawing square"); }
}

// Method is determined at runtime based on actual object type
void drawShape(Shape shape) {
    shape.draw();  // Which draw()? Depends on actual object!
}

drawShape(new Circle());  // Drawing circle
drawShape(new Square());  // Drawing square
drawShape(new Shape());   // Drawing shape
```

---

# 4. Abstract Classes

## 4.1 Abstract Class Declaration

```java
abstract class Animal {
    String name;
    
    // Abstract method - no body, MUST be overridden
    abstract void makeSound();
    
    // Concrete method - has body, can be inherited
    void sleep() {
        System.out.println(name + " is sleeping");
    }
    
    // Constructor - can have constructors!
    public Animal(String name) {
        this.name = name;
    }
}

// Cannot instantiate abstract class!
// Animal animal = new Animal();  // Compile error!

class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    void makeSound() {
        System.out.println("Bark!");
    }
}

Animal dog = new Dog("Buddy");
dog.makeSound();  // Bark!
dog.sleep();      // Buddy is sleeping
```

## 4.2 Abstract Class Rules

```java
// 1. Can have abstract and concrete methods
abstract class Example {
    abstract void abstractMethod();
    void concreteMethod() { }
}

// 2. Can have constructors
abstract class WithConstructor {
    WithConstructor(int value) { }
}

// 3. Can have fields (any type)
abstract class WithFields {
    private int privateField;
    protected String protectedField;
    public static final int CONSTANT = 100;
}

// 4. Can have static methods
abstract class WithStatic {
    static void staticMethod() { }
}

// 5. Cannot be final (contradiction!)
// abstract final class Invalid { }  // Compile error!

// 6. If child doesn't implement all abstract methods, it must also be abstract
abstract class PartialImplementation extends Animal {
    // Still abstract because makeSound() not implemented
}
```

## 4.3 When to Use Abstract Classes

```java
// Use abstract class when:
// 1. Sharing code among related classes
// 2. Common base class with some implementation
// 3. Non-static/non-final fields needed
// 4. Access modifiers other than public needed

abstract class Vehicle {
    protected String brand;
    protected int speed;
    
    public Vehicle(String brand) {
        this.brand = brand;
        this.speed = 0;
    }
    
    // Common implementation
    public void accelerate(int amount) {
        speed += amount;
        System.out.println(brand + " speed: " + speed);
    }
    
    // Force subclasses to implement
    public abstract void startEngine();
}

class Car extends Vehicle {
    public Car(String brand) {
        super(brand);
    }
    
    @Override
    public void startEngine() {
        System.out.println("Car engine started with key");
    }
}

class ElectricCar extends Vehicle {
    public ElectricCar(String brand) {
        super(brand);
    }
    
    @Override
    public void startEngine() {
        System.out.println("Electric car started silently");
    }
}
```

---

# 5. Interfaces

## 5.1 Basic Interface

```java
interface Drawable {
    // Constants (implicitly public static final)
    int DEFAULT_SIZE = 100;
    
    // Abstract methods (implicitly public abstract)
    void draw();
    void resize(int width, int height);
}

class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing circle");
    }
    
    @Override
    public void resize(int width, int height) {
        System.out.println("Resizing to " + width + "x" + height);
    }
}
```

## 5.2 Default Methods (Java 8+)

```java
interface Vehicle {
    void start();
    
    // Default method - has implementation
    default void honk() {
        System.out.println("Beep beep!");
    }
    
    default void stop() {
        System.out.println("Vehicle stopped");
    }
}

class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("Car started");
    }
    
    // Can override default method
    @Override
    public void honk() {
        System.out.println("Car horn: Honk!");
    }
    
    // stop() is inherited as-is
}

Car car = new Car();
car.start();  // Car started
car.honk();   // Car horn: Honk!
car.stop();   // Vehicle stopped
```

## 5.3 Static Methods (Java 8+)

```java
interface MathUtils {
    // Static method in interface
    static int add(int a, int b) {
        return a + b;
    }
    
    static int subtract(int a, int b) {
        return a - b;
    }
}

// Called on interface, not implementing class
int sum = MathUtils.add(5, 3);  // 8
int diff = MathUtils.subtract(10, 4);  // 6
```

## 5.4 Private Methods (Java 9+)

```java
interface Logger {
    default void logInfo(String message) {
        log("INFO", message);
    }
    
    default void logError(String message) {
        log("ERROR", message);
    }
    
    // Private method - reusable helper
    private void log(String level, String message) {
        System.out.println("[" + level + "] " + message);
    }
    
    // Private static method
    private static String format(String message) {
        return message.trim().toUpperCase();
    }
}
```

## 5.5 Multiple Interface Implementation

```java
interface Flyable {
    void fly();
    default void takeOff() { System.out.println("Taking off"); }
}

interface Swimmable {
    void swim();
    default void dive() { System.out.println("Diving"); }
}

interface Walkable {
    void walk();
}

// Implement multiple interfaces
class Duck implements Flyable, Swimmable, Walkable {
    @Override
    public void fly() { System.out.println("Duck flying"); }
    
    @Override
    public void swim() { System.out.println("Duck swimming"); }
    
    @Override
    public void walk() { System.out.println("Duck walking"); }
}

Duck duck = new Duck();
duck.fly();     // Duck flying
duck.swim();    // Duck swimming
duck.walk();    // Duck walking
duck.takeOff(); // Taking off (from Flyable)
duck.dive();    // Diving (from Swimmable)
```

## 5.6 Diamond Problem Resolution

```java
interface A {
    default void show() { System.out.println("A"); }
}

interface B {
    default void show() { System.out.println("B"); }
}

// Class must resolve conflict!
class C implements A, B {
    @Override
    public void show() {
        A.super.show();  // Call A's version
        B.super.show();  // Call B's version
        System.out.println("C");
    }
}

// Or just provide own implementation
class D implements A, B {
    @Override
    public void show() {
        System.out.println("D's own implementation");
    }
}
```

## 5.7 Interface Inheritance

```java
interface BasicCRUD {
    void create();
    void read();
    void update();
    void delete();
}

interface AdvancedCRUD extends BasicCRUD {
    void bulkCreate();
    void bulkDelete();
}

// Must implement all methods from both interfaces
class Repository implements AdvancedCRUD {
    @Override public void create() { }
    @Override public void read() { }
    @Override public void update() { }
    @Override public void delete() { }
    @Override public void bulkCreate() { }
    @Override public void bulkDelete() { }
}
```

## 5.8 Functional Interfaces (Java 8+)

```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);  // Single Abstract Method (SAM)
    
    // Can have default and static methods
    default void display() { }
    static void helper() { }
}

// Use with lambda
Calculator add = (a, b) -> a + b;
Calculator multiply = (a, b) -> a * b;

System.out.println(add.calculate(5, 3));       // 8
System.out.println(multiply.calculate(5, 3));  // 15
```

## 5.9 Marker Interfaces

```java
// Marker interface - no methods, just "marks" the class
interface Serializable { }  // Mark as serializable
interface Cloneable { }     // Mark as cloneable
interface RandomAccess { }  // Mark as random access

class Document implements Serializable, Cloneable {
    // Implementation...
}

// Check with instanceof
if (doc instanceof Serializable) {
    // Can serialize
}
```

---

# 6. Abstract Class vs Interface

| Feature | Abstract Class | Interface |
|---------|----------------|-----------|
| Methods | Abstract + Concrete | Abstract + Default + Static + Private |
| Fields | Any (instance, static) | Only public static final |
| Constructor | Yes | No |
| Multiple Inheritance | No (single extends) | Yes (multiple implements) |
| Access Modifiers | Any | Methods: public/private, Fields: public |
| When to Use | IS-A relationship with shared code | CAN-DO capability, API contract |

```java
// Abstract class: IS-A relationship
abstract class Animal {
    protected String name;
    public Animal(String name) { this.name = name; }
    abstract void makeSound();
    void sleep() { System.out.println("Sleeping"); }
}

// Interface: CAN-DO capability
interface Runnable { void run(); }
interface Flyable { void fly(); }
interface Swimmable { void swim(); }

// Combine both
class Duck extends Animal implements Flyable, Swimmable {
    public Duck(String name) { super(name); }
    void makeSound() { System.out.println("Quack"); }
    public void fly() { System.out.println("Flying"); }
    public void swim() { System.out.println("Swimming"); }
}
```

---

# 7. Sealed Classes (Java 17)

## 7.1 Sealed Class Declaration

```java
// Only specific classes can extend
public sealed class Shape 
    permits Circle, Rectangle, Triangle {
    
    public abstract double area();
}

// Must be final, sealed, or non-sealed
public final class Circle extends Shape {
    private double radius;
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

public sealed class Rectangle extends Shape 
    permits Square {
    protected double width, height;
    
    @Override
    public double area() {
        return width * height;
    }
}

public final class Square extends Rectangle {
    // Can't be extended further
}

public non-sealed class Triangle extends Shape {
    // Can be extended by anyone
    @Override
    public double area() { return 0; }
}
```

## 7.2 Sealed Interface

```java
public sealed interface Service
    permits UserService, OrderService, AdminService {
    void process();
}

public final class UserService implements Service {
    public void process() { }
}

public non-sealed class OrderService implements Service {
    public void process() { }
}

// AdminService must be defined somewhere
```

---

# Summary Table

| Concept | Keyword | Purpose |
|---------|---------|---------|
| Inheritance | `extends` | Reuse parent class code |
| Interface Implementation | `implements` | Fulfill contract |
| Abstract Class | `abstract class` | Partial implementation |
| Interface | `interface` | Define contract |
| Final Class | `final class` | Prevent inheritance |
| Sealed Class | `sealed...permits` | Control inheritance |
| Method Override | `@Override` | Replace parent method |
| Parent Reference | `super` | Access parent members |
