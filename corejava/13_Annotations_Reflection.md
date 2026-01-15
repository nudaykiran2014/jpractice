# Java Annotations & Reflection - Complete Guide

## Table of Contents
1. [What are Annotations?](#1-what-are-annotations)
2. [Built-in Annotations](#2-built-in-annotations)
3. [Meta-Annotations](#3-meta-annotations)
4. [Custom Annotations](#4-custom-annotations)
5. [Processing Annotations](#5-processing-annotations)
6. [What is Reflection?](#6-what-is-reflection)
7. [Class Object](#7-class-object)
8. [Inspecting Classes](#8-inspecting-classes)
9. [Working with Fields](#9-working-with-fields)
10. [Working with Methods](#10-working-with-methods)
11. [Working with Constructors](#11-working-with-constructors)
12. [Creating Objects Dynamically](#12-creating-objects-dynamically)
13. [Accessing Private Members](#13-accessing-private-members)
14. [Real-World Examples](#14-real-world-examples)
15. [Interview Questions](#15-interview-questions)

---

# 1. What are Annotations?

## Kid-Friendly Explanation 🧒

**Annotations are like Sticky Notes 📝**

Imagine you're reading a book and you put sticky notes:
- "Important! Read this again" 
- "Skip this chapter"
- "Homework due tomorrow"

Annotations are sticky notes for your code that tell:
- The compiler what to check
- Tools what to generate
- Frameworks how to behave

## Syntax

```java
@AnnotationName
@AnnotationName(value = "something")
@AnnotationName(name = "test", timeout = 1000)
```

## What Can Be Annotated?

```java
@ClassAnnotation
public class MyClass {
    
    @FieldAnnotation
    private String name;
    
    @ConstructorAnnotation
    public MyClass() { }
    
    @MethodAnnotation
    public void method(@ParameterAnnotation String param) {
        @LocalVariableAnnotation
        int x = 10;
    }
}
```

---

# 2. Built-in Annotations

## @Override

Tells compiler "I'm overriding a parent method". Compile error if not actually overriding.

```java
class Animal {
    void makeSound() { }
}

class Dog extends Animal {
    @Override
    void makeSound() {  // Compiler checks this actually overrides
        System.out.println("Bark!");
    }
    
    @Override
    void makeSond() {  // ❌ Compile ERROR! Typo detected!
        System.out.println("Bark!");
    }
}
```

## @Deprecated

Marks element as "don't use anymore, will be removed".

```java
public class OldAPI {
    
    @Deprecated
    public void oldMethod() {
        // Old implementation
    }
    
    @Deprecated(since = "2.0", forRemoval = true)  // Java 9+
    public void veryOldMethod() {
        // Will be removed soon!
    }
    
    public void newMethod() {
        // Use this instead
    }
}

// Usage generates warning:
OldAPI api = new OldAPI();
api.oldMethod();  // ⚠️ Warning: deprecated
```

## @SuppressWarnings

Tells compiler to ignore specific warnings.

```java
@SuppressWarnings("deprecation")
public void useDeprecatedStuff() {
    api.oldMethod();  // No warning now
}

@SuppressWarnings("unchecked")
public void useRawTypes() {
    List list = new ArrayList();  // No warning
    list.add("item");
}

@SuppressWarnings({"deprecation", "unchecked"})
public void multipleSuppressions() { }

// Common values:
// "deprecation"    - deprecated method/class usage
// "unchecked"      - unchecked generic operations
// "rawtypes"       - raw type usage
// "unused"         - unused variables
// "serial"         - missing serialVersionUID
```

## @FunctionalInterface

Marks interface as functional (exactly one abstract method).

```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
    
    // ❌ Adding another abstract method = Compile ERROR!
    // int anotherMethod();
    
    // ✅ Default methods are OK
    default void print() { }
    
    // ✅ Static methods are OK
    static void helper() { }
}
```

## @SafeVarargs

Suppresses heap pollution warnings for varargs with generics.

```java
@SafeVarargs
public final <T> void process(T... items) {
    for (T item : items) {
        System.out.println(item);
    }
}

// Can only be used on:
// - final methods
// - static methods
// - private methods (Java 9+)
// - constructors
```

---

# 3. Meta-Annotations

Meta-annotations are **annotations for annotations** - they define how custom annotations behave.

## @Retention - When is annotation available?

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)    // Discarded by compiler
@Retention(RetentionPolicy.CLASS)     // In .class file, not at runtime (default)
@Retention(RetentionPolicy.RUNTIME)   // Available at runtime via reflection
```

| Policy | Compiler | .class File | Runtime |
|--------|----------|-------------|---------|
| SOURCE | ✅ | ❌ | ❌ |
| CLASS | ✅ | ✅ | ❌ |
| RUNTIME | ✅ | ✅ | ✅ |

## @Target - Where can annotation be used?

```java
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(ElementType.METHOD)                    // Only on methods
@Target(ElementType.FIELD)                     // Only on fields
@Target(ElementType.TYPE)                      // Classes, interfaces, enums
@Target(ElementType.CONSTRUCTOR)               // Constructors
@Target(ElementType.PARAMETER)                 // Method parameters
@Target(ElementType.LOCAL_VARIABLE)            // Local variables
@Target(ElementType.ANNOTATION_TYPE)           // Other annotations
@Target(ElementType.PACKAGE)                   // Packages
@Target(ElementType.TYPE_PARAMETER)            // Generic type parameters (Java 8)
@Target(ElementType.TYPE_USE)                  // Any type use (Java 8)

// Multiple targets
@Target({ElementType.METHOD, ElementType.FIELD})
```

## @Documented

Include annotation in Javadoc.

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Author {
    String name();
}

@Author(name = "John Doe")  // Will appear in Javadoc
public class MyClass { }
```

## @Inherited

Annotation is inherited by subclasses.

```java
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable { }

@Auditable
public class Parent { }

public class Child extends Parent { }  // Also has @Auditable!
```

## @Repeatable (Java 8)

Allows same annotation to be used multiple times.

```java
// Container annotation
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedules {
    Schedule[] value();
}

// Repeatable annotation
@Repeatable(Schedules.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedule {
    String dayOfWeek();
    String time();
}

// Usage - same annotation multiple times!
@Schedule(dayOfWeek = "Monday", time = "09:00")
@Schedule(dayOfWeek = "Wednesday", time = "14:00")
@Schedule(dayOfWeek = "Friday", time = "09:00")
public void weeklyMeeting() { }
```

---

# 4. Custom Annotations

## Basic Custom Annotation

```java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}

// Usage
public class MyTests {
    @Test
    public void testAddition() {
        assert 2 + 2 == 4;
    }
}
```

## Annotation with Elements

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    String name() default "";           // Optional with default
    int timeout() default 1000;         // Optional with default
    String[] tags() default {};         // Array with default
    Priority priority() default Priority.MEDIUM;  // Enum
}

enum Priority { LOW, MEDIUM, HIGH }

// Usage
@Test(name = "Addition Test", timeout = 5000, priority = Priority.HIGH)
public void testAddition() { }

@Test  // Uses all defaults
public void testSubtraction() { }
```

## Special 'value' Element

If annotation has single element named `value`, you can omit the name:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Author {
    String value();
}

// Can use shorthand
@Author("John Doe")  // Instead of @Author(value = "John Doe")
public class MyClass { }
```

## Annotation Element Types (Allowed)

```java
public @interface MyAnnotation {
    int intValue();                    // Primitive
    String stringValue();              // String
    Class<?> classValue();             // Class
    MyEnum enumValue();                // Enum
    OtherAnnotation annotationValue(); // Another annotation
    int[] arrayValue();                // Array of above types
}
```

---

# 5. Processing Annotations

## Reading Annotations at Runtime

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Info {
    String author();
    String date();
}

class MyClass {
    @Info(author = "John", date = "2024-01-15")
    public void myMethod() { }
}

// Reading annotations
public class AnnotationReader {
    public static void main(String[] args) throws Exception {
        Method method = MyClass.class.getMethod("myMethod");
        
        if (method.isAnnotationPresent(Info.class)) {
            Info info = method.getAnnotation(Info.class);
            System.out.println("Author: " + info.author());
            System.out.println("Date: " + info.date());
        }
        
        // Get all annotations
        Annotation[] annotations = method.getAnnotations();
        for (Annotation a : annotations) {
            System.out.println(a);
        }
    }
}
```

## Simple Test Framework Example

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Test { }

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface BeforeEach { }

class Calculator {
    int add(int a, int b) { return a + b; }
}

class CalculatorTest {
    Calculator calc;
    
    @BeforeEach
    void setup() {
        calc = new Calculator();
    }
    
    @Test
    void testAdd() {
        assert calc.add(2, 3) == 5;
    }
    
    @Test
    void testAddNegative() {
        assert calc.add(-1, -1) == -2;
    }
}

// Test Runner
class TestRunner {
    public static void run(Class<?> testClass) throws Exception {
        Object testInstance = testClass.getDeclaredConstructor().newInstance();
        
        // Find @BeforeEach method
        Method beforeEach = null;
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(BeforeEach.class)) {
                beforeEach = m;
                break;
            }
        }
        
        // Run all @Test methods
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                System.out.print("Running " + method.getName() + "... ");
                try {
                    if (beforeEach != null) {
                        beforeEach.invoke(testInstance);
                    }
                    method.invoke(testInstance);
                    System.out.println("PASSED ✅");
                } catch (Exception e) {
                    System.out.println("FAILED ❌");
                }
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        run(CalculatorTest.class);
    }
}
```

---

# 6. What is Reflection?

## Kid-Friendly Explanation 🧒

**Reflection is like X-Ray Vision 🔍**

Normally you know what's in a box because the label tells you.
With reflection, you can:
- Open ANY box and see what's inside
- Even boxes without labels
- Even sealed boxes (private stuff!)

It lets your program examine and modify itself at runtime!

## Why Use Reflection?

| Use Case | Example |
|----------|---------|
| Frameworks | Spring creates beans dynamically |
| Testing | JUnit finds and runs @Test methods |
| Serialization | Jackson reads/writes object fields |
| IDEs | Auto-complete shows methods |
| Plugins | Load classes at runtime |

## The Cost of Reflection

⚠️ **Drawbacks:**
- **Slower** than direct access
- **No compile-time checking** (errors at runtime)
- **Security restrictions** may apply
- **Breaks encapsulation** (can access private)

---

# 7. Class Object

Every class in Java has a corresponding `Class` object.

## Getting Class Object

```java
// Method 1: .class syntax
Class<String> stringClass = String.class;
Class<int[]> intArrayClass = int[].class;

// Method 2: getClass() on instance
String str = "hello";
Class<?> clazz = str.getClass();

// Method 3: Class.forName() - by name at runtime
Class<?> clazz = Class.forName("java.util.ArrayList");

// For primitives
Class<Integer> intClass = int.class;
Class<Void> voidClass = void.class;
```

## Class Object Information

```java
Class<?> clazz = ArrayList.class;

// Names
clazz.getName();           // "java.util.ArrayList"
clazz.getSimpleName();     // "ArrayList"
clazz.getCanonicalName();  // "java.util.ArrayList"
clazz.getPackageName();    // "java.util"

// Type checks
clazz.isInterface();       // false
clazz.isArray();           // false
clazz.isPrimitive();       // false
clazz.isEnum();            // false
clazz.isAnnotation();      // false
clazz.isRecord();          // false (Java 16+)
clazz.isSealed();          // false (Java 17+)

// Hierarchy
clazz.getSuperclass();                    // AbstractList
clazz.getInterfaces();                    // [List, RandomAccess, ...]
clazz.isAssignableFrom(LinkedList.class); // false
LinkedList.class.isAssignableFrom(clazz); // false
```

---

# 8. Inspecting Classes

## Get Modifiers

```java
Class<?> clazz = ArrayList.class;
int modifiers = clazz.getModifiers();

Modifier.isPublic(modifiers);     // true
Modifier.isAbstract(modifiers);   // false
Modifier.isFinal(modifiers);      // false
Modifier.isStatic(modifiers);     // false (for nested classes)

// Convert to string
String modString = Modifier.toString(modifiers);  // "public"
```

## Get All Members

```java
Class<?> clazz = MyClass.class;

// Fields
Field[] publicFields = clazz.getFields();           // Public (including inherited)
Field[] allFields = clazz.getDeclaredFields();      // All (this class only)

// Methods
Method[] publicMethods = clazz.getMethods();        // Public (including inherited)
Method[] allMethods = clazz.getDeclaredMethods();   // All (this class only)

// Constructors
Constructor<?>[] publicConstructors = clazz.getConstructors();
Constructor<?>[] allConstructors = clazz.getDeclaredConstructors();

// Nested classes
Class<?>[] nestedClasses = clazz.getDeclaredClasses();
```

---

# 9. Working with Fields

## Getting Field Information

```java
class Person {
    public String name;
    private int age;
    protected static String species = "Human";
}

Class<?> clazz = Person.class;

// Get specific field
Field nameField = clazz.getField("name");           // Public only
Field ageField = clazz.getDeclaredField("age");     // Any visibility

// Field information
nameField.getName();                   // "name"
nameField.getType();                   // String.class
nameField.getModifiers();              // public
nameField.isAccessible();              // accessibility flag

// For generic fields
nameField.getGenericType();            // Type including generics
```

## Reading Field Values

```java
Person person = new Person();
person.name = "John";

Field nameField = Person.class.getField("name");
String value = (String) nameField.get(person);  // "John"

// Static field - pass null
Field speciesField = Person.class.getDeclaredField("species");
String species = (String) speciesField.get(null);  // "Human"
```

## Setting Field Values

```java
Person person = new Person();

Field nameField = Person.class.getField("name");
nameField.set(person, "Jane");  // person.name is now "Jane"

// Static field
Field speciesField = Person.class.getDeclaredField("species");
speciesField.set(null, "Homo Sapiens");
```

---

# 10. Working with Methods

## Getting Method Information

```java
class Calculator {
    public int add(int a, int b) { return a + b; }
    private void log(String message) { }
    public static double PI() { return 3.14159; }
}

Class<?> clazz = Calculator.class;

// Get specific method (need parameter types!)
Method addMethod = clazz.getMethod("add", int.class, int.class);
Method logMethod = clazz.getDeclaredMethod("log", String.class);

// Method information
addMethod.getName();               // "add"
addMethod.getReturnType();         // int.class
addMethod.getParameterTypes();     // [int.class, int.class]
addMethod.getParameterCount();     // 2
addMethod.getModifiers();          // public
addMethod.getExceptionTypes();     // declared exceptions
```

## Invoking Methods

```java
Calculator calc = new Calculator();

// Instance method
Method addMethod = Calculator.class.getMethod("add", int.class, int.class);
int result = (int) addMethod.invoke(calc, 5, 3);  // 8

// Static method - pass null for instance
Method piMethod = Calculator.class.getMethod("PI");
double pi = (double) piMethod.invoke(null);  // 3.14159

// Void method
Method logMethod = Calculator.class.getDeclaredMethod("log", String.class);
logMethod.setAccessible(true);  // Access private
logMethod.invoke(calc, "Hello");
```

---

# 11. Working with Constructors

## Getting Constructor Information

```java
class Person {
    public Person() { }
    public Person(String name) { }
    private Person(String name, int age) { }
}

Class<?> clazz = Person.class;

// Get specific constructor
Constructor<?> noArg = clazz.getConstructor();
Constructor<?> oneArg = clazz.getConstructor(String.class);
Constructor<?> twoArg = clazz.getDeclaredConstructor(String.class, int.class);

// Constructor information
oneArg.getParameterTypes();    // [String.class]
oneArg.getParameterCount();    // 1
oneArg.getModifiers();         // public
```

## Creating Instances

```java
Class<?> clazz = Person.class;

// Using no-arg constructor
Person p1 = (Person) clazz.getDeclaredConstructor().newInstance();

// Using parameterized constructor
Constructor<?> constructor = clazz.getConstructor(String.class);
Person p2 = (Person) constructor.newInstance("John");

// Private constructor
Constructor<?> privateConst = clazz.getDeclaredConstructor(String.class, int.class);
privateConst.setAccessible(true);
Person p3 = (Person) privateConst.newInstance("Jane", 25);
```

---

# 12. Creating Objects Dynamically

## Complete Example

```java
// Load class by name
String className = "java.util.ArrayList";
Class<?> clazz = Class.forName(className);

// Create instance
Object list = clazz.getDeclaredConstructor().newInstance();

// Call methods
Method addMethod = clazz.getMethod("add", Object.class);
addMethod.invoke(list, "Hello");
addMethod.invoke(list, "World");

Method sizeMethod = clazz.getMethod("size");
int size = (int) sizeMethod.invoke(list);  // 2

Method getMethod = clazz.getMethod("get", int.class);
String first = (String) getMethod.invoke(list, 0);  // "Hello"
```

## Factory Pattern with Reflection

```java
interface Shape {
    void draw();
}

class Circle implements Shape {
    public void draw() { System.out.println("Drawing circle"); }
}

class Rectangle implements Shape {
    public void draw() { System.out.println("Drawing rectangle"); }
}

class ShapeFactory {
    public static Shape createShape(String type) throws Exception {
        String className = "com.example.shapes." + type;
        Class<?> clazz = Class.forName(className);
        return (Shape) clazz.getDeclaredConstructor().newInstance();
    }
}

// Usage
Shape shape = ShapeFactory.createShape("Circle");
shape.draw();  // "Drawing circle"
```

---

# 13. Accessing Private Members

## setAccessible(true)

The key to accessing private members:

```java
class Secret {
    private String password = "secret123";
    
    private void hiddenMethod() {
        System.out.println("You found me!");
    }
}

Secret secret = new Secret();
Class<?> clazz = Secret.class;

// Access private field
Field passwordField = clazz.getDeclaredField("password");
passwordField.setAccessible(true);  // ✨ Magic!
String password = (String) passwordField.get(secret);  // "secret123"
passwordField.set(secret, "newPassword");

// Access private method
Method hiddenMethod = clazz.getDeclaredMethod("hiddenMethod");
hiddenMethod.setAccessible(true);  // ✨ Magic!
hiddenMethod.invoke(secret);  // "You found me!"
```

## Java 9+ Module Restrictions

In Java 9+, modules can restrict reflection:

```java
// May throw InaccessibleObjectException
field.setAccessible(true);

// JVM flag to allow:
// --add-opens java.base/java.lang=ALL-UNNAMED
```

---

# 14. Real-World Examples

## Example 1: Simple Dependency Injection

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Inject { }

class Service {
    void doSomething() {
        System.out.println("Service doing something");
    }
}

class Controller {
    @Inject
    private Service service;
    
    void handleRequest() {
        service.doSomething();
    }
}

class DIContainer {
    public static <T> T create(Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                Object dependency = field.getType()
                    .getDeclaredConstructor()
                    .newInstance();
                field.setAccessible(true);
                field.set(instance, dependency);
            }
        }
        
        return instance;
    }
}

// Usage
Controller controller = DIContainer.create(Controller.class);
controller.handleRequest();  // Works! Service was injected
```

## Example 2: Object to JSON Converter

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface JsonField {
    String value() default "";
}

class User {
    @JsonField("user_name")
    private String name;
    
    @JsonField
    private int age;
    
    private String password;  // Not serialized (no annotation)
    
    public User(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }
}

class JsonSerializer {
    public static String toJson(Object obj) throws Exception {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonField.class)) {
                field.setAccessible(true);
                
                JsonField annotation = field.getAnnotation(JsonField.class);
                String key = annotation.value().isEmpty() 
                    ? field.getName() 
                    : annotation.value();
                
                if (!first) json.append(", ");
                first = false;
                
                Object value = field.get(obj);
                if (value instanceof String) {
                    json.append("\"").append(key).append("\": \"").append(value).append("\"");
                } else {
                    json.append("\"").append(key).append("\": ").append(value);
                }
            }
        }
        
        json.append("}");
        return json.toString();
    }
}

// Usage
User user = new User("John", 30, "secret");
String json = JsonSerializer.toJson(user);
// {"user_name": "John", "age": 30}
// Note: password is NOT included!
```

## Example 3: Method Timing with Proxy

```java
import java.lang.reflect.*;

interface Calculator {
    int add(int a, int b);
    int multiply(int a, int b);
}

class CalculatorImpl implements Calculator {
    public int add(int a, int b) { return a + b; }
    public int multiply(int a, int b) { return a * b; }
}

class TimingHandler implements InvocationHandler {
    private final Object target;
    
    public TimingHandler(Object target) {
        this.target = target;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) 
            throws Throwable {
        long start = System.nanoTime();
        Object result = method.invoke(target, args);
        long end = System.nanoTime();
        
        System.out.printf("%s took %d ns%n", method.getName(), end - start);
        return result;
    }
}

// Usage
Calculator real = new CalculatorImpl();
Calculator proxy = (Calculator) Proxy.newProxyInstance(
    Calculator.class.getClassLoader(),
    new Class<?>[] { Calculator.class },
    new TimingHandler(real)
);

proxy.add(5, 3);       // "add took 1234 ns"
proxy.multiply(4, 2);  // "multiply took 567 ns"
```

---

# 15. Interview Questions

## Q1: What are annotations used for?

**Answer:**
- **Compiler instructions**: @Override, @Deprecated, @SuppressWarnings
- **Compile-time processing**: Lombok generates code
- **Runtime processing**: Spring DI, JPA, JUnit
- **Documentation**: @Author, @Since

---

## Q2: Difference between @Retention policies?

**Answer:**
| Policy | Available At | Use Case |
|--------|--------------|----------|
| SOURCE | Compile time only | @Override, Lombok |
| CLASS | In bytecode, not runtime | Default, rarely used |
| RUNTIME | Reflection at runtime | Spring, JPA, Jackson |

---

## Q3: What is reflection? Why is it slow?

**Answer:**
Reflection allows examining and modifying classes, methods, fields at runtime.

**Why slow:**
- No compile-time optimizations
- Type checking at runtime
- Security checks on every access
- Cannot be inlined by JIT

---

## Q4: How to access private fields/methods?

**Answer:**
```java
field.setAccessible(true);  // Bypass access checks
field.get(object);          // Read value
field.set(object, value);   // Write value
```

⚠️ Breaks encapsulation, use carefully!

---

## Q5: getMethod() vs getDeclaredMethod()?

**Answer:**
| getMethod() | getDeclaredMethod() |
|-------------|---------------------|
| Public methods only | All methods |
| Includes inherited | This class only |
| No private/protected | Private/protected too |

Same pattern for Fields and Constructors.

---

## Q6: How does Spring use reflection?

**Answer:**
1. **Component scanning**: Finds classes with @Component
2. **Dependency injection**: Creates instances, sets @Autowired fields
3. **AOP proxies**: Intercepts method calls
4. **Transaction management**: Wraps methods with @Transactional

---

## Q7: What is a dynamic proxy?

**Answer:**
Creates a proxy class at runtime that implements specified interfaces:

```java
Object proxy = Proxy.newProxyInstance(
    classLoader,
    interfaces,
    invocationHandler  // Handles all method calls
);
```

Used for: AOP, lazy loading, remote calls, mocking.

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│              ANNOTATIONS & REFLECTION CHEAT SHEET           │
├─────────────────────────────────────────────────────────────┤
│ BUILT-IN ANNOTATIONS                                        │
│   @Override      - Compiler checks override                 │
│   @Deprecated    - Mark as obsolete                         │
│   @SuppressWarnings - Suppress warnings                     │
│   @FunctionalInterface - Single abstract method             │
├─────────────────────────────────────────────────────────────┤
│ META-ANNOTATIONS                                            │
│   @Retention(RUNTIME/CLASS/SOURCE)                          │
│   @Target(METHOD/FIELD/TYPE/...)                            │
│   @Inherited, @Documented, @Repeatable                      │
├─────────────────────────────────────────────────────────────┤
│ GET CLASS OBJECT                                            │
│   String.class                                              │
│   obj.getClass()                                            │
│   Class.forName("java.util.List")                           │
├─────────────────────────────────────────────────────────────┤
│ REFLECTION METHODS                                          │
│   getFields() / getDeclaredFields()                         │
│   getMethods() / getDeclaredMethods()                       │
│   getConstructors() / getDeclaredConstructors()             │
├─────────────────────────────────────────────────────────────┤
│ ACCESS PRIVATE                                              │
│   field.setAccessible(true);                                │
│   field.get(obj);                                           │
│   method.invoke(obj, args);                                 │
└─────────────────────────────────────────────────────────────┘
```
