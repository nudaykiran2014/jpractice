# 📚 Complete Core Java Syllabus
## From Java 1.0 to Java 21+ (LTS)

> A comprehensive guide covering every topic in Core Java with version-specific features

---

# 📋 Table of Contents

1. [Java Fundamentals](#1-java-fundamentals)
2. [Object-Oriented Programming](#2-object-oriented-programming-oop)
3. [Data Types & Variables](#3-data-types--variables)
4. [Operators & Control Flow](#4-operators--control-flow)
5. [Arrays & Strings](#5-arrays--strings)
6. [Classes & Objects](#6-classes--objects)
7. [Inheritance & Polymorphism](#7-inheritance--polymorphism)
8. [Interfaces & Abstract Classes](#8-interfaces--abstract-classes)
9. [Exception Handling](#9-exception-handling)
10. [Collections Framework](#10-collections-framework)
11. [Generics](#11-generics)
12. [Multithreading & Concurrency](#12-multithreading--concurrency)
13. [Java I/O & NIO](#13-java-io--nio)
14. [Lambda Expressions & Functional Programming](#14-lambda-expressions--functional-programming-java-8)
15. [Stream API](#15-stream-api-java-8)
16. [Date & Time API](#16-date--time-api-java-8)
17. [Modules (JPMS)](#17-modules---java-platform-module-system-java-9)
18. [Records & Sealed Classes](#18-records--sealed-classes-java-14-17)
19. [Pattern Matching](#19-pattern-matching-java-16-21)
20. [Virtual Threads](#20-virtual-threads-java-21)
21. [Memory Management & JVM](#21-memory-management--jvm)
22. [Annotations & Reflection](#22-annotations--reflection)
23. [Networking](#23-networking)
24. [JDBC](#24-jdbc---database-connectivity)
25. [New Features by Version](#25-java-version-history--features)

---

# 1. Java Fundamentals

## 1.1 Introduction to Java
- History of Java (James Gosling, Sun Microsystems, 1995)
- Java Editions: Java SE, Java EE, Java ME
- JDK vs JRE vs JVM
- Platform Independence ("Write Once, Run Anywhere")
- Java Bytecode & Compilation Process

## 1.2 JVM Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                    JVM ARCHITECTURE                          │
├─────────────────────────────────────────────────────────────┤
│  .java → javac → .class (bytecode) → JVM → Machine Code    │
├─────────────────────────────────────────────────────────────┤
│  Class Loader Subsystem                                      │
│    ├── Bootstrap Class Loader                               │
│    ├── Extension Class Loader                               │
│    └── Application Class Loader                             │
├─────────────────────────────────────────────────────────────┤
│  Runtime Data Areas                                          │
│    ├── Method Area (Class data, static variables)           │
│    ├── Heap (Objects)                                       │
│    ├── Stack (Method calls, local variables)                │
│    ├── PC Register                                          │
│    └── Native Method Stack                                  │
├─────────────────────────────────────────────────────────────┤
│  Execution Engine                                            │
│    ├── Interpreter                                          │
│    ├── JIT Compiler                                         │
│    └── Garbage Collector                                    │
└─────────────────────────────────────────────────────────────┘
```

## 1.3 First Java Program
```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

## 1.4 Java Naming Conventions
| Element | Convention | Example |
|---------|------------|---------|
| Class | PascalCase | `BankAccount` |
| Interface | PascalCase | `Runnable` |
| Method | camelCase | `calculateTotal()` |
| Variable | camelCase | `firstName` |
| Constant | UPPER_SNAKE_CASE | `MAX_VALUE` |
| Package | lowercase | `com.example.banking` |

## 1.5 Keywords (Reserved Words)
```
abstract  assert    boolean   break     byte      case
catch     char      class     const     continue  default
do        double    else      enum      extends   final
finally   float     for       goto      if        implements
import    instanceof int      interface long      native
new       package   private   protected public    return
short     static    strictfp  super     switch    synchronized
this      throw     throws    transient try       void
volatile  while     var       yield     record    sealed
non-sealed permits
```

---

# 2. Object-Oriented Programming (OOP)

## 2.1 Four Pillars of OOP

### Encapsulation
- Bundling data + methods together
- Access modifiers: `private`, `default`, `protected`, `public`
- Getters and Setters

### Inheritance
- `extends` keyword
- Single inheritance (class)
- Multiple inheritance (interfaces)
- `super` keyword

### Polymorphism
- Compile-time: Method Overloading
- Runtime: Method Overriding
- Upcasting and Downcasting

### Abstraction
- Abstract classes
- Interfaces
- Hiding implementation details

## 2.2 Access Modifiers
| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `private` | ✅ | ❌ | ❌ | ❌ |
| `default` | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

## 2.3 SOLID Principles
- **S**ingle Responsibility Principle
- **O**pen/Closed Principle
- **L**iskov Substitution Principle
- **I**nterface Segregation Principle
- **D**ependency Inversion Principle

---

# 3. Data Types & Variables

## 3.1 Primitive Data Types
| Type | Size | Range | Default |
|------|------|-------|---------|
| `byte` | 8 bits | -128 to 127 | 0 |
| `short` | 16 bits | -32,768 to 32,767 | 0 |
| `int` | 32 bits | -2^31 to 2^31-1 | 0 |
| `long` | 64 bits | -2^63 to 2^63-1 | 0L |
| `float` | 32 bits | ±3.4E38 | 0.0f |
| `double` | 64 bits | ±1.7E308 | 0.0d |
| `char` | 16 bits | 0 to 65,535 | '\u0000' |
| `boolean` | 1 bit | true/false | false |

## 3.2 Reference Types
- Classes
- Interfaces
- Arrays
- Enums
- Records (Java 14+)

## 3.3 Type Casting
```java
// Widening (implicit)
int i = 100;
long l = i;

// Narrowing (explicit)
double d = 100.5;
int i = (int) d;  // 100
```

## 3.4 Wrapper Classes
| Primitive | Wrapper |
|-----------|---------|
| `byte` | `Byte` |
| `short` | `Short` |
| `int` | `Integer` |
| `long` | `Long` |
| `float` | `Float` |
| `double` | `Double` |
| `char` | `Character` |
| `boolean` | `Boolean` |

## 3.5 Autoboxing & Unboxing (Java 5)
```java
Integer obj = 100;      // Autoboxing
int primitive = obj;    // Unboxing
```

## 3.6 var - Local Variable Type Inference (Java 10)
```java
var name = "John";        // String
var numbers = List.of(1, 2, 3);  // List<Integer>
var map = new HashMap<String, Integer>();
```

---

# 4. Operators & Control Flow

## 4.1 Operators

### Arithmetic
`+`, `-`, `*`, `/`, `%`, `++`, `--`

### Relational
`==`, `!=`, `>`, `<`, `>=`, `<=`

### Logical
`&&`, `||`, `!`

### Bitwise
`&`, `|`, `^`, `~`, `<<`, `>>`, `>>>`

### Assignment
`=`, `+=`, `-=`, `*=`, `/=`, `%=`, `&=`, `|=`, `^=`

### Ternary
```java
result = condition ? value1 : value2;
```

### instanceof (Enhanced in Java 16+)
```java
// Before Java 16
if (obj instanceof String) {
    String s = (String) obj;
}

// Java 16+ Pattern Matching
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

## 4.2 Control Flow Statements

### if-else
```java
if (condition) {
    // code
} else if (condition2) {
    // code
} else {
    // code
}
```

### switch (Classic)
```java
switch (value) {
    case 1:
        // code
        break;
    case 2:
        // code
        break;
    default:
        // code
}
```

### switch Expression (Java 14+)
```java
String result = switch (day) {
    case MONDAY, FRIDAY -> "Work day";
    case SATURDAY, SUNDAY -> "Weekend";
    default -> "Unknown";
};
```

### switch with yield (Java 14+)
```java
int result = switch (input) {
    case "A" -> 1;
    case "B" -> {
        int calculated = calculate();
        yield calculated;
    }
    default -> 0;
};
```

### Loops
```java
// for loop
for (int i = 0; i < 10; i++) { }

// enhanced for-each
for (String s : list) { }

// while
while (condition) { }

// do-while
do { } while (condition);
```

### break & continue
```java
// Labeled break
outer:
for (int i = 0; i < 10; i++) {
    for (int j = 0; j < 10; j++) {
        if (condition) break outer;
    }
}
```

---

# 5. Arrays & Strings

## 5.1 Arrays
```java
// Declaration
int[] arr1 = new int[5];
int[] arr2 = {1, 2, 3, 4, 5};
int[][] matrix = new int[3][3];

// Methods
Arrays.sort(arr);
Arrays.binarySearch(arr, key);
Arrays.copyOf(arr, newLength);
Arrays.fill(arr, value);
Arrays.equals(arr1, arr2);
Arrays.toString(arr);
```

## 5.2 String
- Immutable
- String Pool
- Common methods:
```java
str.length()
str.charAt(index)
str.substring(start, end)
str.indexOf(str)
str.contains(str)
str.startsWith(prefix)
str.endsWith(suffix)
str.toLowerCase()
str.toUpperCase()
str.trim()
str.strip()           // Java 11
str.isBlank()         // Java 11
str.repeat(count)     // Java 11
str.lines()           // Java 11
str.indent(n)         // Java 12
str.transform(func)   // Java 12
```

## 5.3 StringBuilder & StringBuffer
| Feature | StringBuilder | StringBuffer |
|---------|---------------|--------------|
| Mutable | ✅ | ✅ |
| Thread-safe | ❌ | ✅ |
| Performance | Faster | Slower |

## 5.4 Text Blocks (Java 15)
```java
String json = """
    {
        "name": "John",
        "age": 30
    }
    """;
```

## 5.5 String Templates (Java 21 Preview)
```java
String name = "John";
int age = 30;
String message = STR."Hello \{name}, you are \{age} years old";
```

---

# 6. Classes & Objects

## 6.1 Class Structure
```java
public class BankAccount {
    // Fields
    private String accountNumber;
    private double balance;
    
    // Static field
    private static int accountCount = 0;
    
    // Constructor
    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0;
        accountCount++;
    }
    
    // Instance method
    public void deposit(double amount) {
        balance += amount;
    }
    
    // Static method
    public static int getAccountCount() {
        return accountCount;
    }
    
    // Getters & Setters
    public double getBalance() {
        return balance;
    }
}
```

## 6.2 Constructors
- Default constructor
- Parameterized constructor
- Copy constructor
- Constructor chaining (`this()`, `super()`)
- Constructor overloading

## 6.3 `this` Keyword
- Refers to current object
- Distinguish instance variables from parameters
- Call other constructors

## 6.4 `static` Keyword
- Static variables (class variables)
- Static methods
- Static blocks
- Static nested classes

## 6.5 `final` Keyword
- Final variables (constants)
- Final methods (cannot override)
- Final classes (cannot extend)

## 6.6 Object Class Methods
```java
toString()      // String representation
equals()        // Equality check
hashCode()      // Hash code
getClass()      // Runtime class
clone()         // Clone object
finalize()      // Before garbage collection (deprecated)
wait()          // Thread synchronization
notify()        // Thread synchronization
notifyAll()     // Thread synchronization
```

## 6.7 equals() and hashCode() Contract
- If `a.equals(b)` is true, then `a.hashCode() == b.hashCode()`
- If `hashCode()` is same, `equals()` may or may not be true

---

# 7. Inheritance & Polymorphism

## 7.1 Inheritance
```java
class Animal {
    void eat() { System.out.println("Eating"); }
}

class Dog extends Animal {
    void bark() { System.out.println("Barking"); }
}
```

## 7.2 Method Overriding
```java
class Animal {
    void sound() { System.out.println("Some sound"); }
}

class Dog extends Animal {
    @Override
    void sound() { System.out.println("Bark"); }
}
```

## 7.3 Method Overloading
```java
class Calculator {
    int add(int a, int b) { return a + b; }
    double add(double a, double b) { return a + b; }
    int add(int a, int b, int c) { return a + b + c; }
}
```

## 7.4 Covariant Return Types (Java 5)
```java
class Parent {
    Parent getInstance() { return new Parent(); }
}

class Child extends Parent {
    @Override
    Child getInstance() { return new Child(); }  // Covariant return
}
```

## 7.5 `super` Keyword
- Access parent class members
- Call parent constructor

---

# 8. Interfaces & Abstract Classes

## 8.1 Abstract Classes
```java
abstract class Shape {
    abstract double area();
    
    void display() {
        System.out.println("Shape");
    }
}
```

## 8.2 Interfaces

### Before Java 8
```java
interface Drawable {
    void draw();  // implicitly public abstract
    int SIZE = 10;  // implicitly public static final
}
```

### Java 8+ Features
```java
interface Vehicle {
    // Abstract method
    void start();
    
    // Default method
    default void honk() {
        System.out.println("Beep!");
    }
    
    // Static method
    static int getWheelCount() {
        return 4;
    }
}
```

### Java 9+ Private Methods
```java
interface Helper {
    default void method1() {
        helperMethod();
    }
    
    default void method2() {
        helperMethod();
    }
    
    private void helperMethod() {
        System.out.println("Helper");
    }
}
```

## 8.3 Functional Interfaces (Java 8)
```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);  // Single Abstract Method (SAM)
}

// Built-in functional interfaces
Predicate<T>       // T -> boolean
Function<T, R>     // T -> R
Consumer<T>        // T -> void
Supplier<T>        // () -> T
BiFunction<T,U,R>  // (T, U) -> R
UnaryOperator<T>   // T -> T
BinaryOperator<T>  // (T, T) -> T
```

## 8.4 Abstract Class vs Interface

| Feature | Abstract Class | Interface |
|---------|----------------|-----------|
| Methods | Abstract + Concrete | Abstract + Default + Static |
| Variables | Any type | public static final |
| Constructors | Yes | No |
| Multiple Inheritance | No | Yes |
| Access Modifiers | Any | public (until Java 9) |

---

# 9. Exception Handling

## 9.1 Exception Hierarchy
```
Throwable
├── Error (JVM errors, OutOfMemoryError)
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── VirtualMachineError
└── Exception
    ├── RuntimeException (Unchecked)
    │   ├── NullPointerException
    │   ├── ArrayIndexOutOfBoundsException
    │   ├── ArithmeticException
    │   ├── ClassCastException
    │   └── IllegalArgumentException
    └── Checked Exceptions
        ├── IOException
        ├── SQLException
        ├── FileNotFoundException
        └── ClassNotFoundException
```

## 9.2 try-catch-finally
```java
try {
    // risky code
} catch (SpecificException e) {
    // handle specific
} catch (Exception e) {
    // handle general
} finally {
    // always executes
}
```

## 9.3 try-with-resources (Java 7)
```java
try (FileReader fr = new FileReader("file.txt");
     BufferedReader br = new BufferedReader(fr)) {
    // use resources
}  // auto-closed
```

## 9.4 Multi-catch (Java 7)
```java
try {
    // code
} catch (IOException | SQLException e) {
    // handle both
}
```

## 9.5 throw & throws
```java
public void validate(int age) throws InvalidAgeException {
    if (age < 0) {
        throw new InvalidAgeException("Age cannot be negative");
    }
}
```

## 9.6 Custom Exceptions
```java
class InsufficientFundsException extends Exception {
    private double amount;
    
    public InsufficientFundsException(double amount) {
        super("Insufficient funds: " + amount);
        this.amount = amount;
    }
}
```

---

# 10. Collections Framework

## 10.1 Collection Hierarchy
```
Iterable
└── Collection
    ├── List (ordered, allows duplicates)
    │   ├── ArrayList
    │   ├── LinkedList
    │   ├── Vector
    │   └── Stack
    ├── Set (no duplicates)
    │   ├── HashSet
    │   ├── LinkedHashSet
    │   ├── TreeSet
    │   └── EnumSet
    └── Queue
        ├── PriorityQueue
        ├── ArrayDeque
        └── LinkedList

Map (key-value pairs)
├── HashMap
├── LinkedHashMap
├── TreeMap
├── Hashtable
├── ConcurrentHashMap
└── EnumMap
```

## 10.2 List Implementations

| Feature | ArrayList | LinkedList | Vector |
|---------|-----------|------------|--------|
| Implementation | Dynamic Array | Doubly Linked List | Dynamic Array |
| Access | O(1) | O(n) | O(1) |
| Insert/Delete | O(n) | O(1) | O(n) |
| Thread-safe | No | No | Yes |
| Growth | 50% | N/A | 100% |

## 10.3 Set Implementations

| Feature | HashSet | LinkedHashSet | TreeSet |
|---------|---------|---------------|---------|
| Order | No | Insertion | Sorted |
| Null | Yes (one) | Yes (one) | No |
| Performance | O(1) | O(1) | O(log n) |
| Implementation | HashMap | LinkedHashMap | Red-Black Tree |

## 10.4 Map Implementations

| Feature | HashMap | LinkedHashMap | TreeMap | ConcurrentHashMap |
|---------|---------|---------------|---------|-------------------|
| Order | No | Insertion | Sorted | No |
| Null Key | Yes (one) | Yes (one) | No | No |
| Null Values | Yes | Yes | No | No |
| Thread-safe | No | No | No | Yes |

## 10.5 Queue & Deque
```java
Queue<String> queue = new LinkedList<>();
queue.offer("A");    // Add
queue.poll();        // Remove
queue.peek();        // View

Deque<String> deque = new ArrayDeque<>();
deque.addFirst("A");
deque.addLast("B");
deque.pollFirst();
deque.pollLast();
```

## 10.6 Comparable vs Comparator

```java
// Comparable - natural ordering
class Person implements Comparable<Person> {
    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);
    }
}

// Comparator - custom ordering
Comparator<Person> byAge = (p1, p2) -> p1.age - p2.age;
Comparator<Person> byName = Comparator.comparing(Person::getName);
```

## 10.7 Immutable Collections (Java 9+)
```java
List<String> list = List.of("A", "B", "C");
Set<String> set = Set.of("A", "B", "C");
Map<String, Integer> map = Map.of("A", 1, "B", 2);

// Java 10+
List<String> copy = List.copyOf(mutableList);
```

## 10.8 Sequenced Collections (Java 21)
```java
SequencedCollection<E>  // ordered with defined first/last
SequencedSet<E>         // Set with sequence
SequencedMap<K,V>       // Map with sequence

list.getFirst();
list.getLast();
list.addFirst(e);
list.addLast(e);
list.reversed();  // Reversed view
```

---

# 11. Generics

## 11.1 Generic Classes
```java
public class Box<T> {
    private T content;
    
    public void set(T content) { this.content = content; }
    public T get() { return content; }
}

Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();
```

## 11.2 Generic Methods
```java
public <T> void printArray(T[] array) {
    for (T element : array) {
        System.out.println(element);
    }
}

public <T, R> R transform(T input, Function<T, R> func) {
    return func.apply(input);
}
```

## 11.3 Bounded Type Parameters
```java
// Upper bound
public <T extends Number> double sum(List<T> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

// Multiple bounds
public <T extends Comparable<T> & Serializable> void process(T item) { }
```

## 11.4 Wildcards
```java
// Unbounded
List<?> list;

// Upper bounded (read)
List<? extends Number> numbers;  // Can read as Number

// Lower bounded (write)
List<? super Integer> integers;  // Can write Integer
```

## 11.5 PECS Principle
- **P**roducer **E**xtends: Use `? extends T` when you only GET values
- **C**onsumer **S**uper: Use `? super T` when you only PUT values

## 11.6 Type Erasure
- Generic type info removed at runtime
- Replaced with Object or bound
- Can't use `instanceof` with generics
- Can't create generic arrays

---

# 12. Multithreading & Concurrency

## 12.1 Creating Threads
```java
// Extend Thread
class MyThread extends Thread {
    public void run() { }
}

// Implement Runnable
class MyRunnable implements Runnable {
    public void run() { }
}

// Lambda (Java 8)
Thread t = new Thread(() -> System.out.println("Hello"));
```

## 12.2 Thread States
```
NEW → RUNNABLE → RUNNING → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
```

## 12.3 Thread Methods
```java
start()           // Start thread
run()             // Thread's task
sleep(ms)         // Pause thread
join()            // Wait for thread to finish
yield()           // Hint to scheduler
interrupt()       // Interrupt thread
isAlive()         // Check if running
setDaemon(true)   // Set as daemon thread
```

## 12.4 Synchronization
```java
// Synchronized method
public synchronized void method() { }

// Synchronized block
synchronized (lock) { }

// Static synchronized
public static synchronized void staticMethod() { }
```

## 12.5 Locks (java.util.concurrent.locks)
```java
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}

// Try lock with timeout
if (lock.tryLock(1, TimeUnit.SECONDS)) { }

// Read-Write Lock
ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock();
rwLock.writeLock().lock();
```

## 12.6 Executor Service
```java
ExecutorService executor = Executors.newFixedThreadPool(5);
ExecutorService cached = Executors.newCachedThreadPool();
ExecutorService single = Executors.newSingleThreadExecutor();
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(3);

// Submit tasks
Future<String> future = executor.submit(() -> "Result");
String result = future.get();

executor.shutdown();
```

## 12.7 Callable & Future
```java
Callable<Integer> task = () -> {
    Thread.sleep(1000);
    return 42;
};

Future<Integer> future = executor.submit(task);
Integer result = future.get();
```

## 12.8 CompletableFuture (Java 8)
```java
CompletableFuture.supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World")
    .thenAccept(System.out::println);
```

## 12.9 Concurrent Collections
```java
ConcurrentHashMap<K, V>
CopyOnWriteArrayList<E>
CopyOnWriteArraySet<E>
BlockingQueue<E>
ConcurrentLinkedQueue<E>
```

## 12.10 Atomic Variables
```java
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();
counter.compareAndSet(expected, newValue);
```

## 12.11 Synchronizers
- `CountDownLatch` - Wait for N events
- `CyclicBarrier` - Wait for N threads
- `Semaphore` - Limit concurrent access
- `Exchanger` - Exchange data between threads
- `Phaser` - Flexible barrier

## 12.12 Virtual Threads (Java 21)
```java
Thread vThread = Thread.startVirtualThread(() -> {
    System.out.println("Virtual thread!");
});

try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> doTask());
}
```

---

# 13. Java I/O & NIO

## 13.1 I/O Streams (java.io)

### Byte Streams
```java
FileInputStream fis = new FileInputStream("file.txt");
FileOutputStream fos = new FileOutputStream("file.txt");
BufferedInputStream bis = new BufferedInputStream(fis);
BufferedOutputStream bos = new BufferedOutputStream(fos);
```

### Character Streams
```java
FileReader fr = new FileReader("file.txt");
FileWriter fw = new FileWriter("file.txt");
BufferedReader br = new BufferedReader(fr);
BufferedWriter bw = new BufferedWriter(fw);
PrintWriter pw = new PrintWriter(fw);
```

## 13.2 Serialization
```java
class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private transient String password;  // Not serialized
}

// Serialize
ObjectOutputStream oos = new ObjectOutputStream(fos);
oos.writeObject(person);

// Deserialize
ObjectInputStream ois = new ObjectInputStream(fis);
Person p = (Person) ois.readObject();
```

## 13.3 NIO (java.nio) - Java 7+

### Path & Files
```java
Path path = Path.of("file.txt");
Path path2 = Paths.get("dir", "subdir", "file.txt");

// File operations
Files.exists(path);
Files.createFile(path);
Files.createDirectories(path);
Files.copy(source, target);
Files.move(source, target);
Files.delete(path);
Files.readAllLines(path);
Files.write(path, lines);
Files.readString(path);        // Java 11
Files.writeString(path, str);  // Java 11
```

### Walking Directory Tree
```java
Files.walk(path)
    .filter(Files::isRegularFile)
    .forEach(System.out::println);

Files.find(path, maxDepth, (p, attr) -> attr.isRegularFile());
```

### WatchService
```java
WatchService watchService = FileSystems.getDefault().newWatchService();
path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
WatchKey key = watchService.take();
```

---

# 14. Lambda Expressions & Functional Programming (Java 8)

## 14.1 Lambda Syntax
```java
// No parameters
() -> System.out.println("Hello")

// One parameter
x -> x * 2

// Multiple parameters
(x, y) -> x + y

// With types
(int x, int y) -> x + y

// Block body
(x, y) -> {
    int sum = x + y;
    return sum;
}
```

## 14.2 Method References
```java
// Static method
Math::max           // (a, b) -> Math.max(a, b)

// Instance method of a particular object
str::length         // () -> str.length()

// Instance method of an arbitrary object
String::toUpperCase // s -> s.toUpperCase()

// Constructor
ArrayList::new      // () -> new ArrayList()
```

## 14.3 Functional Interfaces
```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
}

// Built-in interfaces
Predicate<T>        test(T t)          // T -> boolean
Function<T, R>      apply(T t)         // T -> R
Consumer<T>         accept(T t)        // T -> void
Supplier<T>         get()              // () -> T
BiFunction<T,U,R>   apply(T t, U u)    // (T, U) -> R
BiPredicate<T,U>    test(T t, U u)     // (T, U) -> boolean
BiConsumer<T,U>     accept(T t, U u)   // (T, U) -> void
UnaryOperator<T>    apply(T t)         // T -> T
BinaryOperator<T>   apply(T t1, T t2)  // (T, T) -> T
```

## 14.4 Closures & Effectively Final
```java
int factor = 2;  // effectively final
Function<Integer, Integer> multiplier = x -> x * factor;
// factor = 3;  // Would cause compile error
```

---

# 15. Stream API (Java 8)

## 15.1 Creating Streams
```java
list.stream()
list.parallelStream()
Arrays.stream(array)
Stream.of(elements)
Stream.generate(supplier)
Stream.iterate(seed, function)
IntStream.range(1, 10)
```

## 15.2 Intermediate Operations
```java
filter(predicate)
map(function)
flatMap(function)
distinct()
sorted()
sorted(comparator)
limit(n)
skip(n)
peek(consumer)
```

## 15.3 Terminal Operations
```java
collect(collector)
forEach(consumer)
forEachOrdered(consumer)
reduce(identity, accumulator)
count()
min(comparator)
max(comparator)
findFirst()
findAny()
anyMatch(predicate)
allMatch(predicate)
noneMatch(predicate)
toArray()
```

## 15.4 Collectors
```java
Collectors.toList()
Collectors.toSet()
Collectors.toMap(keyMapper, valueMapper)
Collectors.toCollection(supplier)
Collectors.joining(delimiter)
Collectors.groupingBy(classifier)
Collectors.partitioningBy(predicate)
Collectors.counting()
Collectors.summingInt(mapper)
Collectors.averagingDouble(mapper)
Collectors.summarizingInt(mapper)
Collectors.reducing()
Collectors.collectingAndThen()
Collectors.mapping()
Collectors.flatMapping()
Collectors.teeing()  // Java 12
```

---

# 16. Date & Time API (Java 8)

## 16.1 Main Classes (java.time)
```java
LocalDate date = LocalDate.now();
LocalTime time = LocalTime.now();
LocalDateTime dateTime = LocalDateTime.now();
ZonedDateTime zonedDateTime = ZonedDateTime.now();
Instant instant = Instant.now();
Duration duration = Duration.between(time1, time2);
Period period = Period.between(date1, date2);
```

## 16.2 Creating Date/Time
```java
LocalDate date = LocalDate.of(2024, 1, 15);
LocalTime time = LocalTime.of(10, 30, 45);
LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30);
```

## 16.3 Parsing & Formatting
```java
LocalDate parsed = LocalDate.parse("2024-01-15");
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String formatted = date.format(formatter);
```

## 16.4 Manipulation
```java
date.plusDays(7)
date.minusMonths(1)
date.withYear(2025)
time.truncatedTo(ChronoUnit.HOURS)
```

---

# 17. Modules - Java Platform Module System (Java 9)

## 17.1 Module Declaration (module-info.java)
```java
module com.example.banking {
    requires java.sql;
    requires transitive com.example.common;
    
    exports com.example.banking.api;
    exports com.example.banking.internal to com.example.admin;
    
    opens com.example.banking.model to jackson.databind;
    opens com.example.banking.model;
    
    provides BankingService with BankingServiceImpl;
    uses PaymentProcessor;
}
```

## 17.2 Module Keywords
| Keyword | Purpose |
|---------|---------|
| `requires` | Declare dependency |
| `requires transitive` | Transitive dependency |
| `exports` | Make package public |
| `exports to` | Qualified export |
| `opens` | Allow reflection |
| `opens to` | Qualified open |
| `provides with` | Service provider |
| `uses` | Service consumer |

---

# 18. Records & Sealed Classes (Java 14-17)

## 18.1 Records (Java 16)
```java
// Before: 50+ lines
// After: 1 line!
record Person(String name, int age) { }

// Compiler generates:
// - Constructor
// - Getters (name(), age())
// - equals(), hashCode(), toString()

// Custom constructor
record Person(String name, int age) {
    public Person {
        if (age < 0) throw new IllegalArgumentException();
    }
}

// Additional methods
record Point(int x, int y) {
    public double distance() {
        return Math.sqrt(x * x + y * y);
    }
}
```

## 18.2 Sealed Classes (Java 17)
```java
public sealed class Shape 
    permits Circle, Rectangle, Triangle {
}

public final class Circle extends Shape { }
public sealed class Rectangle extends Shape permits Square { }
public non-sealed class Triangle extends Shape { }
public final class Square extends Rectangle { }
```

| Modifier | Meaning |
|----------|---------|
| `sealed` | Only permitted subclasses |
| `final` | No further subclasses |
| `non-sealed` | Open for extension |

---

# 19. Pattern Matching (Java 16-21)

## 19.1 instanceof Pattern Matching (Java 16)
```java
// Before
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// After
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

## 19.2 Switch Pattern Matching (Java 21)
```java
String result = switch (obj) {
    case Integer i -> "Integer: " + i;
    case String s  -> "String: " + s;
    case null      -> "Null!";
    default        -> "Unknown";
};
```

## 19.3 Record Patterns (Java 21)
```java
record Point(int x, int y) { }

void printSum(Object obj) {
    if (obj instanceof Point(int x, int y)) {
        System.out.println(x + y);
    }
}

// In switch
String describe(Object obj) {
    return switch (obj) {
        case Point(int x, int y) when x == y -> "Diagonal point";
        case Point(int x, int y) -> "Point at " + x + ", " + y;
        default -> "Not a point";
    };
}
```

## 19.4 Guarded Patterns (Java 21)
```java
switch (obj) {
    case String s when s.length() > 5 -> "Long string";
    case String s -> "Short string";
    case Integer i when i > 0 -> "Positive";
    case Integer i -> "Non-positive";
    default -> "Unknown";
}
```

---

# 20. Virtual Threads (Java 21)

## 20.1 Creating Virtual Threads
```java
// Method 1
Thread vThread = Thread.startVirtualThread(() -> {
    System.out.println("Virtual thread!");
});

// Method 2
Thread vThread = Thread.ofVirtual()
    .name("my-virtual-thread")
    .start(() -> doWork());

// Method 3 - Executor
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 100_000; i++) {
        executor.submit(() -> doWork());
    }
}
```

## 20.2 Virtual vs Platform Threads
| Feature | Platform Thread | Virtual Thread |
|---------|-----------------|----------------|
| Managed by | OS | JVM |
| Memory | ~1MB each | ~few KB each |
| Count | ~thousands | ~millions |
| Blocking | Wastes resources | Efficient |
| Best for | CPU-bound | I/O-bound |

---

# 21. Memory Management & JVM

## 21.1 Memory Areas
- **Heap**: Objects and arrays
- **Stack**: Local variables, method calls
- **Method Area**: Class metadata, static variables
- **PC Register**: Current instruction
- **Native Method Stack**: Native method calls

## 21.2 Garbage Collection
- **Serial GC**: Single thread, small apps
- **Parallel GC**: Multiple threads, throughput
- **CMS**: Concurrent, low pause
- **G1 GC**: Region-based, balanced (default)
- **ZGC**: Ultra-low latency (Java 15+)
- **Shenandoah**: Low pause (Java 15+)

## 21.3 GC Algorithms
- Mark and Sweep
- Mark and Compact
- Copying Algorithm
- Generational Collection

## 21.4 Memory Tuning
```bash
-Xms512m        # Initial heap size
-Xmx2g          # Maximum heap size
-Xss512k        # Stack size per thread
-XX:+UseG1GC    # Use G1 garbage collector
-XX:+PrintGCDetails  # Print GC info
```

---

# 22. Annotations & Reflection

## 22.1 Built-in Annotations
```java
@Override           // Method overrides superclass
@Deprecated         // Marked for removal
@SuppressWarnings   // Suppress compiler warnings
@FunctionalInterface // SAM interface
@SafeVarargs        // Safe varargs
```

## 22.2 Meta-Annotations
```java
@Retention(RetentionPolicy.RUNTIME)  // Available at runtime
@Target(ElementType.METHOD)          // Applicable to methods
@Documented                          // Include in Javadoc
@Inherited                           // Inherited by subclasses
@Repeatable(Schedules.class)         // Can be repeated
```

## 22.3 Custom Annotations
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    String name() default "";
    int timeout() default 1000;
}
```

## 22.4 Reflection
```java
Class<?> clazz = Class.forName("com.example.MyClass");
Constructor<?> constructor = clazz.getConstructor(String.class);
Object obj = constructor.newInstance("Hello");

Method method = clazz.getMethod("sayHello");
method.invoke(obj);

Field field = clazz.getDeclaredField("name");
field.setAccessible(true);
field.set(obj, "World");
```

---

# 23. Networking

## 23.1 Sockets
```java
// Server
ServerSocket serverSocket = new ServerSocket(8080);
Socket clientSocket = serverSocket.accept();

// Client
Socket socket = new Socket("localhost", 8080);
InputStream in = socket.getInputStream();
OutputStream out = socket.getOutputStream();
```

## 23.2 HTTP Client (Java 11)
```java
HttpClient client = HttpClient.newHttpClient();

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(json))
    .build();

HttpResponse<String> response = client.send(
    request, 
    HttpResponse.BodyHandlers.ofString()
);

// Async
CompletableFuture<HttpResponse<String>> future = 
    client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
```

## 23.3 URL & URLConnection
```java
URL url = new URL("https://example.com/api");
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("GET");
BufferedReader reader = new BufferedReader(
    new InputStreamReader(conn.getInputStream())
);
```

---

# 24. JDBC - Database Connectivity

## 24.1 Basic JDBC Steps
```java
// 1. Load driver (optional in JDBC 4.0+)
Class.forName("com.mysql.cj.jdbc.Driver");

// 2. Establish connection
Connection conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/mydb", "user", "password"
);

// 3. Create statement
Statement stmt = conn.createStatement();
PreparedStatement pstmt = conn.prepareStatement(
    "SELECT * FROM users WHERE id = ?"
);

// 4. Execute query
ResultSet rs = stmt.executeQuery("SELECT * FROM users");
pstmt.setInt(1, userId);
ResultSet rs2 = pstmt.executeQuery();

// 5. Process results
while (rs.next()) {
    String name = rs.getString("name");
    int age = rs.getInt("age");
}

// 6. Close resources
rs.close();
stmt.close();
conn.close();
```

## 24.2 Transactions
```java
conn.setAutoCommit(false);
try {
    // Execute statements
    conn.commit();
} catch (SQLException e) {
    conn.rollback();
}
```

## 24.3 Connection Pooling
```java
// Using HikariCP or similar
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
config.setUsername("user");
config.setPassword("password");
DataSource ds = new HikariDataSource(config);
```

---

# 25. Java Version History & Features

## Major Features by Version

| Version | Year | Key Features |
|---------|------|--------------|
| **Java 1.0** | 1996 | Initial release |
| **Java 1.2** | 1998 | Collections, Swing |
| **Java 1.4** | 2002 | assert, NIO |
| **Java 5** | 2004 | Generics, Enums, Annotations, Autoboxing, For-each |
| **Java 6** | 2006 | Performance, Scripting |
| **Java 7** | 2011 | Diamond operator, Try-with-resources, Switch on String |
| **Java 8** | 2014 | Lambda, Stream API, Optional, Date/Time API, Default methods |
| **Java 9** | 2017 | Modules (JPMS), JShell, Factory methods for collections |
| **Java 10** | 2018 | `var` keyword, Unmodifiable collections |
| **Java 11** | 2018 | HTTP Client, String methods, Single-file execution (LTS) |
| **Java 12** | 2019 | Switch expressions (preview) |
| **Java 13** | 2019 | Text blocks (preview) |
| **Java 14** | 2020 | Records (preview), Pattern matching instanceof (preview) |
| **Java 15** | 2020 | Text blocks, Sealed classes (preview), ZGC |
| **Java 16** | 2021 | Records, Pattern matching instanceof |
| **Java 17** | 2021 | Sealed classes, Pattern matching switch (preview) (LTS) |
| **Java 18** | 2022 | UTF-8 default, Simple web server |
| **Java 19** | 2022 | Virtual threads (preview), Record patterns (preview) |
| **Java 20** | 2023 | Scoped values (preview) |
| **Java 21** | 2023 | Virtual threads, Pattern matching, Sequenced collections (LTS) |

## LTS (Long Term Support) Versions
- Java 8 (March 2014)
- Java 11 (September 2018)
- Java 17 (September 2021)
- Java 21 (September 2023)

---

# 📚 Study Roadmap

## Beginner (1-2 months)
1. Java Fundamentals
2. Data Types & Variables
3. Operators & Control Flow
4. Arrays & Strings
5. OOP Basics
6. Exception Handling

## Intermediate (2-3 months)
7. Collections Framework
8. Generics
9. I/O & NIO
10. Lambda & Streams
11. Date/Time API
12. Basic Multithreading

## Advanced (3-4 months)
13. Advanced Multithreading
14. Concurrency Utilities
15. JVM Internals
16. Memory Management
17. Networking & JDBC
18. Modern Java (Records, Sealed Classes, Pattern Matching)
19. Virtual Threads

---

# ✅ Interview Preparation Checklist

- [ ] OOP concepts with examples
- [ ] Collections internals (HashMap, ArrayList)
- [ ] Generics & type erasure
- [ ] Exception handling best practices
- [ ] Multithreading & synchronization
- [ ] Stream API operations
- [ ] Java 8+ features
- [ ] JVM memory model
- [ ] Garbage collection
- [ ] SOLID principles
- [ ] Design patterns basics
- [ ] Common coding problems with streams

---

*📖 This syllabus covers Java SE from version 1.0 to Java 21 (LTS). Updated for 2024.*
