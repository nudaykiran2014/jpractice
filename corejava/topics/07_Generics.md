# Java Generics - Complete Guide
## Part 7: Type Parameters, Wildcards, Bounds, Type Erasure

---

# 1. Introduction to Generics

## 1.1 Why Generics?

```java
// WITHOUT Generics (before Java 5)
List list = new ArrayList();
list.add("Hello");
list.add(123);              // No compile error!
String s = (String) list.get(1);  // Runtime ClassCastException!

// WITH Generics
List<String> list = new ArrayList<>();
list.add("Hello");
// list.add(123);           // Compile error! Type safety!
String s = list.get(0);     // No cast needed
```

## 1.2 Benefits

```java
// 1. Type Safety - errors caught at compile time
// 2. No Casting - compiler handles it
// 3. Code Reuse - same code works with different types
```

---

# 2. Generic Classes

## 2.1 Basic Generic Class

```java
// T is a type parameter (placeholder)
public class Box<T> {
    private T content;
    
    public void set(T content) {
        this.content = content;
    }
    
    public T get() {
        return content;
    }
}

// Usage with different types
Box<String> stringBox = new Box<>();
stringBox.set("Hello");
String str = stringBox.get();

Box<Integer> intBox = new Box<>();
intBox.set(123);
Integer num = intBox.get();

Box<List<String>> listBox = new Box<>();
listBox.set(List.of("A", "B", "C"));
```

## 2.2 Multiple Type Parameters

```java
public class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
}

Pair<String, Integer> pair = new Pair<>("Age", 25);
String key = pair.getKey();       // "Age"
Integer value = pair.getValue();  // 25

// Multiple types
public class Triple<A, B, C> {
    private A first;
    private B second;
    private C third;
    // ...
}
```

## 2.3 Common Type Parameter Names

| Parameter | Convention |
|-----------|------------|
| `T` | Type |
| `E` | Element |
| `K` | Key |
| `V` | Value |
| `N` | Number |
| `S, U, V` | 2nd, 3rd, 4th types |

---

# 3. Generic Methods

## 3.1 Basic Generic Method

```java
public class Util {
    // <T> declares the type parameter
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.println(element);
        }
    }
    
    public static <T> T getFirst(List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }
}

// Type inferred from arguments
Util.printArray(new String[]{"A", "B", "C"});
Util.printArray(new Integer[]{1, 2, 3});

String first = Util.getFirst(List.of("Hello", "World"));
```

## 3.2 Multiple Type Parameters in Methods

```java
public static <K, V> V getOrDefault(Map<K, V> map, K key, V defaultValue) {
    return map.containsKey(key) ? map.get(key) : defaultValue;
}

public static <T, R> R transform(T input, Function<T, R> transformer) {
    return transformer.apply(input);
}

// Usage
String result = transform(123, num -> "Number: " + num);
```

## 3.3 Generic Constructor

```java
public class MyClass<T> {
    private T value;
    
    // Generic constructor
    public <U> MyClass(U input, Function<U, T> converter) { 

        this.value = converter.apply(input);
    }
}

// Usage
MyClass<String> obj = new MyClass<>(123, num -> num.toString());
```

---

# 4. Bounded Type Parameters

## 4.1 Upper Bounds (extends)

```java
// T must be Number or subclass of Number
public class NumberBox<T extends Number> {
    private T value;
    
    public double getDoubleValue() {
        return value.doubleValue();  // Can call Number methods!
    }
}

NumberBox<Integer> intBox = new NumberBox<>();
NumberBox<Double> doubleBox = new NumberBox<>();
// NumberBox<String> stringBox = new NumberBox<>();  // Compile error!

// In methods
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

String maxStr = max("Apple", "Banana");  // "Banana"
Integer maxInt = max(10, 20);            // 20
```

## 4.2 Multiple Bounds

```java
// T must implement BOTH Comparable AND Serializable
public class MultiBox<T extends Comparable<T> & Serializable> {
    private T value;
}

// Class must come first, then interfaces
public <T extends Number & Comparable<T> & Serializable> void process(T item) {
    // Can use methods from Number, Comparable, and Serializable
}
```

## 4.3 Recursive Type Bounds

```java
// T must be comparable to itself
public static <T extends Comparable<T>> T findMax(List<T> list) {
    T max = list.get(0);
    for (T item : list) {
        if (item.compareTo(max) > 0) {
            max = item;
        }
    }
    return max;
}

// Enum pattern
public abstract class Enum<E extends Enum<E>> {
    public final int compareTo(E other) { ... }
}
```

---

# 5. Wildcards

## 5.1 Unbounded Wildcard (?)

```java
// Accept any type of List
public static void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

printList(List.of("A", "B", "C"));
printList(List.of(1, 2, 3));
printList(List.of(new Object(), new Object()));

// ⚠️ Can only read as Object, cannot add (except null)
List<?> unknown = new ArrayList<String>();
Object obj = unknown.get(0);  // OK - read as Object
// unknown.add("Hello");       // Compile error!
unknown.add(null);             // Only null is allowed
```

## 5.2 Upper Bounded Wildcard (? extends T) - Producer

```java
// Accept List of Number or any subclass
public static double sum(List<? extends Number> list) {
    double total = 0;
    for (Number num : list) {
        total += num.doubleValue();
    }
    return total;
}

sum(List.of(1, 2, 3));           // List<Integer>
sum(List.of(1.5, 2.5, 3.5));     // List<Double>
sum(List.of(1L, 2L, 3L));        // List<Long>

// "Producer Extends" - can READ elements
List<? extends Number> numbers = List.of(1, 2, 3);
Number n = numbers.get(0);  // OK
// numbers.add(1);           // Compile error - can't add!
```

## 5.3 Lower Bounded Wildcard (? super T) - Consumer

```java
// Accept List of Integer or any superclass
public static void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}

List<Integer> ints = new ArrayList<>();
List<Number> nums = new ArrayList<>();
List<Object> objs = new ArrayList<>();

addNumbers(ints);  // OK
addNumbers(nums);  // OK
addNumbers(objs);  // OK

// "Consumer Super" - can WRITE elements
List<? super Integer> consumer = new ArrayList<Number>();
consumer.add(1);       // OK - can add Integer
consumer.add(2);       // OK
Object o = consumer.get(0);  // Can only read as Object
```

## 5.4 PECS Principle

```java
// PECS = Producer Extends, Consumer Super

// PRODUCER - only read from it → use "extends"
public void readFrom(List<? extends Animal> animals) {
    Animal a = animals.get(0);  // Read OK
}

// CONSUMER - only write to it → use "super"
public void writeTo(List<? super Dog> dogs) {
    dogs.add(new Dog());  // Write OK
}

// Example: Collections.copy()
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (int i = 0; i < src.size(); i++) {
        dest.set(i, src.get(i));  // Read from src, write to dest
    }
}
```

---

# 6. Generic Inheritance

## 6.1 Generic Class Inheritance

```java
// Base generic class
class Box<T> {
    protected T content;
}

// Subclass with same type parameter
class ColoredBox<T> extends Box<T> {
    private String color;
}

// Subclass with concrete type
class IntBox extends Box<Integer> {
    // content is now Integer
}

// Subclass with additional type parameter
class PairBox<T, U> extends Box<T> {
    private U extra;
}
```

## 6.2 Generic Interface Implementation

```java
interface Container<T> {
    void add(T item);
    T get();
}

// Implement with same type parameter
class GenericContainer<T> implements Container<T> {
    private T item;
    public void add(T item) { this.item = item; }
    public T get() { return item; }
}

// Implement with concrete type
class StringContainer implements Container<String> {
    private String item;
    public void add(String item) { this.item = item; }
    public String get() { return item; }
}
```

## 6.3 Subtyping with Generics

```java
// NOT covariant! This is WRONG:
List<Dog> dogs = new ArrayList<>();
// List<Animal> animals = dogs;  // Compile error!

// Why? Because you could do:
// animals.add(new Cat());  // Would corrupt dogs list!

// Arrays ARE covariant (and that's a problem):
Dog[] dogArray = new Dog[10];
Animal[] animalArray = dogArray;  // Compiles!
animalArray[0] = new Cat();       // ArrayStoreException at runtime!
```

---

# 7. Type Erasure

## 7.1 What is Type Erasure?

```java
// COMPILE TIME:
List<String> strings = new ArrayList<String>();
strings.add("Hello");
String s = strings.get(0);

// RUNTIME (after erasure):
List strings = new ArrayList();
strings.add("Hello");
String s = (String) strings.get(0);  // Compiler adds cast
```

## 7.2 Erasure Rules

```java
// Type parameter T becomes Object
class Box<T> { T value; }
// → class Box { Object value; }

// Bounded type becomes bound
class Box<T extends Number> { T value; }
// → class Box { Number value; }

// Multiple bounds - first is used
class Box<T extends Comparable<T> & Serializable> { T value; }
// → class Box { Comparable value; }
```

## 7.3 Consequences of Type Erasure

```java
// 1. Cannot use instanceof with type parameter
public static <T> void check(Object obj) {
    // if (obj instanceof T) { }  // Compile error!
    // if (obj instanceof List<String>) { }  // Compile error!
    if (obj instanceof List<?>) { }  // OK with unbounded
}

// 2. Cannot create generic arrays
// T[] array = new T[10];  // Compile error!
// List<String>[] lists = new List<String>[10];  // Compile error!
List<?>[] lists = new List<?>[10];  // OK with wildcard

// 3. Cannot create instances of type parameter
// T obj = new T();  // Compile error!

// 4. Cannot use primitives as type arguments
// List<int> ints;  // Compile error!
List<Integer> ints;  // OK - use wrapper

// 5. Cannot overload methods that erase to same signature
void process(List<String> list) { }
// void process(List<Integer> list) { }  // Compile error - same erasure!
```

## 7.4 Bridge Methods

```java
class Node<T> {
    public T data;
    public void setData(T data) { this.data = data; }
}

class StringNode extends Node<String> {
    @Override
    public void setData(String data) { super.setData(data); }
}

// Compiler generates bridge method to preserve polymorphism:
// public void setData(Object data) { setData((String) data); }
```

---

# 8. Generic Restrictions

## 8.1 What You CANNOT Do

```java
public class Restrictions<T> {
    
    // 1. Cannot instantiate type parameters
    // T obj = new T();  // Error!
    
    // 2. Cannot create arrays of type parameters
    // T[] array = new T[10];  // Error!
    
    // 3. Cannot declare static fields of type parameter
    // static T value;  // Error!
    
    // 4. Cannot use instanceof with parameterized types
    // if (obj instanceof List<String>) { }  // Error!
    
    // 5. Cannot create generic exception classes
    // class MyException<T> extends Exception { }  // Error!
    
    // 6. Cannot catch type parameter
    // catch (T e) { }  // Error!
}
```

## 8.2 Workarounds

```java
// Creating instances using Class<T>
public static <T> T createInstance(Class<T> clazz) throws Exception {
    return clazz.getDeclaredConstructor().newInstance();
}

String str = createInstance(String.class);

// Creating arrays using Array.newInstance
@SuppressWarnings("unchecked")
public static <T> T[] createArray(Class<T> type, int size) {
    return (T[]) java.lang.reflect.Array.newInstance(type, size);
}

String[] strings = createArray(String.class, 10);
```

---

# 9. Best Practices

## 9.1 DO's ✅

```java
// 1. Use generics for type-safe collections
List<String> names = new ArrayList<>();

// 2. Use bounded types when needed
public <T extends Comparable<T>> T max(List<T> list) { ... }

// 3. Prefer List<T> over T[]
public <T> void process(List<T> items) { ... }

// 4. Use PECS for flexibility
public void copy(List<? super T> dest, List<? extends T> src) { ... }

// 5. Use @SuppressWarnings("unchecked") sparingly and document
@SuppressWarnings("unchecked")
public <T> T[] toArray(List<T> list, Class<T> type) {
    // Unavoidable unchecked cast - document why it's safe
    return (T[]) list.toArray();
}
```

## 9.2 DON'Ts ❌

```java
// 1. Don't use raw types
List list = new ArrayList();  // Bad!
List<Object> list = new ArrayList<>();  // Better

// 2. Don't mix raw and generic types
List<String> strings = new ArrayList();  // Warning!
List<String> strings = new ArrayList<>();  // Good

// 3. Don't ignore or suppress warnings without understanding
@SuppressWarnings("unchecked")  // Why? Document it!
void method() { ... }
```

---

# Summary Table

| Syntax | Name | Use |
|--------|------|-----|
| `<T>` | Type Parameter | Define generic class/method |
| `<T extends X>` | Upper Bound | T must be X or subclass |
| `<T super X>` | Lower Bound | T must be X or superclass |
| `<?>` | Unbounded Wildcard | Any type |
| `<? extends T>` | Upper Bounded Wildcard | Read (produce) |
| `<? super T>` | Lower Bounded Wildcard | Write (consume) |

| Term | Meaning |
|------|---------|
| Type Erasure | Generic info removed at runtime |
| Bridge Method | Compiler-generated for polymorphism |
| Raw Type | Generic type without parameters |
| Reifiable Type | Type info available at runtime |
