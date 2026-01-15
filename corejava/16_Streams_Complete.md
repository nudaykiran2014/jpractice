# Java Streams API - Complete Guide

## Table of Contents
1. [What are Streams?](#1-what-are-streams)
2. [Creating Streams](#2-creating-streams)
3. [Stream Pipeline](#3-stream-pipeline)
4. [Intermediate Operations](#4-intermediate-operations)
5. [Terminal Operations](#5-terminal-operations)
6. [Primitive Streams](#6-primitive-streams)
7. [Parallel Streams](#7-parallel-streams)
8. [Optional Class](#8-optional-class)
9. [Common Patterns](#9-common-patterns)
10. [Interview Questions](#10-interview-questions)

---

# 1. What are Streams?

## 🧒 Kid-Friendly Explanation

**Stream = Assembly Line in a Factory 🏭**

```
Raw Materials → [Cut] → [Paint] → [Package] → Finished Product
     ↓            ↓         ↓          ↓            ↓
  Source    Intermediate Intermediate Terminal    Result
            Operation    Operation   Operation
```

- **Source**: Where items come from (List, Array, File)
- **Intermediate**: Transform items (filter, map, sort)
- **Terminal**: Produce final result (collect, count, forEach)

---

## Stream vs Collection

| Collection | Stream |
|------------|--------|
| Data structure (stores data) | Computation pipeline (processes data) |
| Iterate externally (for loop) | Iterate internally |
| Can be reused | Single use only! |
| Eager (all in memory) | Lazy (computes on demand) |
| Modifiable | Cannot modify source |

```java
// Collection - external iteration
List<String> names = List.of("Alice", "Bob", "Charlie");
for (String name : names) {
    if (name.startsWith("A")) {
        System.out.println(name.toUpperCase());
    }
}

// Stream - internal iteration
names.stream()
     .filter(name -> name.startsWith("A"))
     .map(String::toUpperCase)
     .forEach(System.out::println);
```

---

# 2. Creating Streams

## From Collections

```java
List<String> list = List.of("a", "b", "c");
Stream<String> stream = list.stream();
Stream<String> parallelStream = list.parallelStream();

Set<Integer> set = Set.of(1, 2, 3);
Stream<Integer> setStream = set.stream();

Map<String, Integer> map = Map.of("a", 1, "b", 2);
Stream<Map.Entry<String, Integer>> entryStream = map.entrySet().stream();
Stream<String> keyStream = map.keySet().stream();
Stream<Integer> valueStream = map.values().stream();
```

## From Arrays

```java
String[] array = {"a", "b", "c"};
Stream<String> stream1 = Arrays.stream(array);
Stream<String> stream2 = Stream.of(array);
Stream<String> stream3 = Stream.of("a", "b", "c");

// Partial array
Stream<String> partial = Arrays.stream(array, 1, 3);  // ["b", "c"]

// Primitive arrays
int[] nums = {1, 2, 3};
IntStream intStream = Arrays.stream(nums);
```

## Stream.of() and Stream.empty()

```java
Stream<String> stream = Stream.of("a", "b", "c");
Stream<Object> empty = Stream.empty();

// Single element
Stream<String> single = Stream.of("hello");

// Nullable (Java 9+)
Stream<String> nullable = Stream.ofNullable(getValue());  // Empty if null
```

## Stream.generate() - Infinite Stream

```java
// Generate infinite stream (must limit!)
Stream<Double> randoms = Stream.generate(Math::random).limit(5);

Stream<String> constants = Stream.generate(() -> "Hello").limit(3);
// ["Hello", "Hello", "Hello"]

// UUID generator
Stream<String> uuids = Stream.generate(() -> UUID.randomUUID().toString())
                             .limit(10);
```

## Stream.iterate() - Sequential Values

```java
// Old way (before Java 9) - must limit!
Stream<Integer> infinite = Stream.iterate(0, n -> n + 2).limit(5);
// [0, 2, 4, 6, 8]

// Java 9+ with predicate (stops automatically)
Stream<Integer> finite = Stream.iterate(0, n -> n < 10, n -> n + 2);
// [0, 2, 4, 6, 8]

// Fibonacci
Stream.iterate(new int[]{0, 1}, 
               arr -> new int[]{arr[1], arr[0] + arr[1]})
      .limit(10)
      .map(arr -> arr[0])
      .forEach(System.out::println);
// 0, 1, 1, 2, 3, 5, 8, 13, 21, 34
```

## Stream.builder()

```java
Stream<String> stream = Stream.<String>builder()
    .add("a")
    .add("b")
    .add("c")
    .build();
```

## From Other Sources

```java
// From String chars
IntStream chars = "Hello".chars();  // [72, 101, 108, 108, 111]

// From file lines
Stream<String> lines = Files.lines(Path.of("file.txt"));

// From BufferedReader
Stream<String> readerLines = reader.lines();

// Range of numbers
IntStream range = IntStream.range(1, 5);       // [1, 2, 3, 4]
IntStream rangeClosed = IntStream.rangeClosed(1, 5);  // [1, 2, 3, 4, 5]

// From regex pattern
Stream<String> words = Pattern.compile("\\s+")
                              .splitAsStream("Hello World Java");
// ["Hello", "World", "Java"]

// Concatenate streams
Stream<String> combined = Stream.concat(stream1, stream2);
```

---

# 3. Stream Pipeline

## Structure

```
Source → Intermediate Operations → Terminal Operation → Result
           (0 or more)              (exactly 1)
```

```java
List<String> result = names.stream()      // Source
    .filter(n -> n.length() > 3)          // Intermediate
    .map(String::toUpperCase)             // Intermediate
    .sorted()                             // Intermediate
    .collect(Collectors.toList());        // Terminal
```

## Lazy Evaluation

Intermediate operations don't execute until terminal operation is called!

```java
Stream<String> stream = names.stream()
    .filter(n -> {
        System.out.println("Filtering: " + n);
        return n.length() > 3;
    })
    .map(n -> {
        System.out.println("Mapping: " + n);
        return n.toUpperCase();
    });

System.out.println("Stream created, nothing executed yet!");

// NOW it executes:
stream.forEach(System.out::println);
```

## Short-Circuit Operations

Some operations can stop early:
- `findFirst()`, `findAny()`
- `anyMatch()`, `allMatch()`, `noneMatch()`
- `limit()`

```java
// Finds first match and stops (doesn't process all elements)
Optional<String> first = hugeList.stream()
    .filter(s -> s.startsWith("A"))
    .findFirst();
```

---

# 4. Intermediate Operations

## filter() - Keep elements matching predicate

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Keep even numbers
numbers.stream()
       .filter(n -> n % 2 == 0)
       .forEach(System.out::println);  // 2, 4, 6, 8, 10

// Multiple filters (AND logic)
numbers.stream()
       .filter(n -> n > 3)
       .filter(n -> n < 8)
       .forEach(System.out::println);  // 4, 5, 6, 7
```

## map() - Transform each element

```java
List<String> names = List.of("alice", "bob", "charlie");

// Transform to uppercase
names.stream()
     .map(String::toUpperCase)
     .forEach(System.out::println);  // ALICE, BOB, CHARLIE

// Extract property
List<Person> people = getPersonList();
people.stream()
      .map(Person::getName)
      .forEach(System.out::println);

// Calculate
numbers.stream()
       .map(n -> n * n)  // Square
       .forEach(System.out::println);
```

## flatMap() - Flatten nested structures

```java
// Problem: List of Lists
List<List<Integer>> nested = List.of(
    List.of(1, 2),
    List.of(3, 4),
    List.of(5, 6)
);

// map() gives Stream<List<Integer>> - not what we want
// flatMap() gives Stream<Integer> - flattened!
nested.stream()
      .flatMap(List::stream)
      .forEach(System.out::println);  // 1, 2, 3, 4, 5, 6

// Split words from sentences
List<String> sentences = List.of("Hello World", "Java Streams");
sentences.stream()
         .flatMap(s -> Arrays.stream(s.split(" ")))
         .forEach(System.out::println);  // Hello, World, Java, Streams

// Get all orders from all customers
customers.stream()
         .flatMap(c -> c.getOrders().stream())
         .forEach(System.out::println);
```

## distinct() - Remove duplicates

```java
List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3, 4);

numbers.stream()
       .distinct()
       .forEach(System.out::println);  // 1, 2, 3, 4

// Uses equals() for comparison
```

## sorted() - Sort elements

```java
List<String> names = List.of("Charlie", "Alice", "Bob");

// Natural order
names.stream()
     .sorted()
     .forEach(System.out::println);  // Alice, Bob, Charlie

// Custom comparator
names.stream()
     .sorted(Comparator.reverseOrder())
     .forEach(System.out::println);  // Charlie, Bob, Alice

// Sort by property
people.stream()
      .sorted(Comparator.comparing(Person::getAge))
      .forEach(System.out::println);

// Multiple sort criteria
people.stream()
      .sorted(Comparator.comparing(Person::getLastName)
                        .thenComparing(Person::getFirstName))
      .forEach(System.out::println);

// Null-safe sorting
people.stream()
      .sorted(Comparator.comparing(Person::getName, 
                                   Comparator.nullsLast(String::compareTo)))
      .forEach(System.out::println);
```

## limit() and skip()

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Take first N
numbers.stream()
       .limit(3)
       .forEach(System.out::println);  // 1, 2, 3

// Skip first N
numbers.stream()
       .skip(3)
       .forEach(System.out::println);  // 4, 5, 6, 7, 8, 9, 10

// Pagination
int page = 2;
int pageSize = 3;
numbers.stream()
       .skip((page - 1) * pageSize)
       .limit(pageSize)
       .forEach(System.out::println);  // 4, 5, 6
```

## peek() - Debug/Side effects

```java
// Useful for debugging
List<String> result = names.stream()
    .filter(n -> n.length() > 3)
    .peek(n -> System.out.println("After filter: " + n))
    .map(String::toUpperCase)
    .peek(n -> System.out.println("After map: " + n))
    .collect(Collectors.toList());

// ⚠️ Don't use for actual logic - side effects are bad practice!
```

## takeWhile() and dropWhile() (Java 9+)

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 1, 2);

// Take elements WHILE condition is true (stops at first false)
numbers.stream()
       .takeWhile(n -> n < 4)
       .forEach(System.out::println);  // 1, 2, 3

// Drop elements WHILE condition is true (keeps after first false)
numbers.stream()
       .dropWhile(n -> n < 4)
       .forEach(System.out::println);  // 4, 5, 1, 2

// Different from filter! These stop/start at first mismatch
```

## mapMulti() (Java 16+)

```java
// Alternative to flatMap for some cases
Stream.of(1, 2, 3)
      .<Integer>mapMulti((num, consumer) -> {
          consumer.accept(num);
          consumer.accept(num * 10);
      })
      .forEach(System.out::println);  // 1, 10, 2, 20, 3, 30
```

---

# 5. Terminal Operations

## forEach() and forEachOrdered()

```java
// forEach - order not guaranteed in parallel
names.parallelStream()
     .forEach(System.out::println);  // Random order

// forEachOrdered - maintains order even in parallel
names.parallelStream()
     .forEachOrdered(System.out::println);  // Original order
```

## collect() - Most powerful terminal operation

```java
// To List
List<String> list = stream.collect(Collectors.toList());

// To Set
Set<String> set = stream.collect(Collectors.toSet());

// To specific collection
TreeSet<String> treeSet = stream.collect(Collectors.toCollection(TreeSet::new));

// Join strings
String joined = names.stream()
                     .collect(Collectors.joining(", "));  // "Alice, Bob, Charlie"

// See Collectors section for more!
```

## toList() (Java 16+)

```java
// Simpler than collect(Collectors.toList())
List<String> list = names.stream()
                         .filter(n -> n.length() > 3)
                         .toList();  // Unmodifiable list!
```

## toArray()

```java
// To Object array
Object[] array = stream.toArray();

// To typed array
String[] strings = stream.toArray(String[]::new);
Integer[] integers = numbers.stream().toArray(Integer[]::new);
```

## reduce() - Combine all elements

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// Sum with identity
int sum = numbers.stream()
                 .reduce(0, (a, b) -> a + b);  // 15

// Using method reference
int sum2 = numbers.stream()
                  .reduce(0, Integer::sum);  // 15

// Without identity (returns Optional)
Optional<Integer> sum3 = numbers.stream()
                                .reduce(Integer::sum);

// Find max
Optional<Integer> max = numbers.stream()
                               .reduce(Integer::max);

// Concatenate strings
String concat = List.of("a", "b", "c").stream()
                    .reduce("", (a, b) -> a + b);  // "abc"

// Complex reduction
int sumOfSquares = numbers.stream()
                          .reduce(0, (acc, n) -> acc + n * n, Integer::sum);
```

## count()

```java
long count = names.stream()
                  .filter(n -> n.startsWith("A"))
                  .count();
```

## min() and max()

```java
// Require Comparator, return Optional
Optional<String> min = names.stream()
                            .min(Comparator.naturalOrder());

Optional<String> max = names.stream()
                            .max(Comparator.naturalOrder());

// By property
Optional<Person> youngest = people.stream()
                                  .min(Comparator.comparing(Person::getAge));

Optional<Person> oldest = people.stream()
                                .max(Comparator.comparing(Person::getAge));
```

## findFirst() and findAny()

```java
// findFirst - always returns first element
Optional<String> first = names.stream()
                              .filter(n -> n.startsWith("A"))
                              .findFirst();

// findAny - any matching element (faster in parallel)
Optional<String> any = names.parallelStream()
                            .filter(n -> n.startsWith("A"))
                            .findAny();
```

## anyMatch(), allMatch(), noneMatch()

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// Any element matches?
boolean hasEven = numbers.stream()
                         .anyMatch(n -> n % 2 == 0);  // true

// All elements match?
boolean allPositive = numbers.stream()
                             .allMatch(n -> n > 0);  // true

// No elements match?
boolean noneNegative = numbers.stream()
                              .noneMatch(n -> n < 0);  // true
```

---

# 6. Primitive Streams

## Why Primitive Streams?

Avoid boxing/unboxing overhead!

```java
// Object stream - boxing overhead
Stream<Integer> boxed = Stream.of(1, 2, 3);

// Primitive stream - no boxing!
IntStream primitive = IntStream.of(1, 2, 3);
```

## IntStream, LongStream, DoubleStream

```java
// Creating
IntStream intStream = IntStream.of(1, 2, 3);
LongStream longStream = LongStream.of(1L, 2L, 3L);
DoubleStream doubleStream = DoubleStream.of(1.0, 2.0, 3.0);

// From range
IntStream range = IntStream.range(1, 5);        // 1, 2, 3, 4
IntStream rangeClosed = IntStream.rangeClosed(1, 5);  // 1, 2, 3, 4, 5

// From array
int[] arr = {1, 2, 3};
IntStream fromArray = Arrays.stream(arr);

// From object stream
IntStream fromObjects = names.stream()
                             .mapToInt(String::length);
```

## Primitive Stream Methods

```java
IntStream numbers = IntStream.rangeClosed(1, 10);

// Aggregate operations
int sum = numbers.sum();              // 55
OptionalDouble avg = IntStream.rangeClosed(1, 10).average();  // 5.5
OptionalInt max = IntStream.rangeClosed(1, 10).max();         // 10
OptionalInt min = IntStream.rangeClosed(1, 10).min();         // 1

// Summary statistics (all at once!)
IntSummaryStatistics stats = IntStream.rangeClosed(1, 10)
                                      .summaryStatistics();
stats.getSum();      // 55
stats.getAverage();  // 5.5
stats.getMax();      // 10
stats.getMin();      // 1
stats.getCount();    // 10
```

## Converting Between Stream Types

```java
// Primitive to Object
IntStream intStream = IntStream.of(1, 2, 3);
Stream<Integer> boxed = intStream.boxed();

// Object to Primitive
Stream<String> strings = Stream.of("a", "bb", "ccc");
IntStream lengths = strings.mapToInt(String::length);

// Between primitive types
IntStream ints = IntStream.of(1, 2, 3);
LongStream longs = ints.asLongStream();
DoubleStream doubles = IntStream.of(1, 2, 3).asDoubleStream();
```

---

# 7. Parallel Streams

## Creating Parallel Streams

```java
// From collection
Stream<String> parallel1 = list.parallelStream();

// From sequential stream
Stream<String> parallel2 = list.stream().parallel();

// Back to sequential
Stream<String> sequential = parallel2.sequential();

// Check if parallel
boolean isParallel = stream.isParallel();
```

## When to Use Parallel?

| Use Parallel ✅ | Avoid Parallel ❌ |
|----------------|-------------------|
| Large datasets (10,000+) | Small datasets |
| CPU-intensive operations | I/O operations |
| Independent operations | Shared mutable state |
| ArrayList, arrays | LinkedList, iterators |
| Stateless operations | Stateful operations |

```java
// ✅ Good for parallel - independent computation
List<Integer> result = hugeList.parallelStream()
    .map(n -> expensiveComputation(n))
    .collect(Collectors.toList());

// ❌ Bad for parallel - shared state
List<String> results = new ArrayList<>();
names.parallelStream()
     .forEach(results::add);  // Race condition!
```

## Thread-Safe Collection

```java
// Use thread-safe collector
List<String> safe = names.parallelStream()
    .filter(n -> n.length() > 3)
    .collect(Collectors.toList());  // Thread-safe

// Or use concurrent collector
Map<Integer, List<String>> byLength = names.parallelStream()
    .collect(Collectors.groupingByConcurrent(String::length));
```

## Order Preservation

```java
// forEachOrdered maintains order in parallel
list.parallelStream()
    .forEachOrdered(System.out::println);  // Ordered

// Unordered can be faster
list.parallelStream()
    .unordered()
    .forEach(System.out::println);  // Faster but unordered
```

---

# 8. Optional Class

## Why Optional?

Avoid NullPointerException!

```java
// Before Optional
String name = person.getName();
if (name != null) {
    System.out.println(name.toUpperCase());
}

// With Optional
Optional<String> name = Optional.ofNullable(person.getName());
name.ifPresent(n -> System.out.println(n.toUpperCase()));
```

## Creating Optional

```java
// With value
Optional<String> opt1 = Optional.of("Hello");

// Possibly null (use this!)
Optional<String> opt2 = Optional.ofNullable(getValue());

// Empty
Optional<String> opt3 = Optional.empty();

// ⚠️ Never pass null to of()!
Optional.of(null);  // NullPointerException!
```

## Checking and Getting Value

```java
Optional<String> opt = Optional.of("Hello");

// Check if present
if (opt.isPresent()) {
    System.out.println(opt.get());
}

// Check if empty (Java 11+)
if (opt.isEmpty()) {
    System.out.println("No value");
}

// Execute if present
opt.ifPresent(System.out::println);

// Execute if present, else run alternative (Java 9+)
opt.ifPresentOrElse(
    System.out::println,
    () -> System.out.println("Empty!")
);
```

## Getting Value with Defaults

```java
Optional<String> opt = Optional.ofNullable(getValue());

// Default value
String value1 = opt.orElse("default");

// Default from supplier (lazy)
String value2 = opt.orElseGet(() -> computeDefault());

// Throw if empty
String value3 = opt.orElseThrow();  // NoSuchElementException
String value4 = opt.orElseThrow(() -> new CustomException("Not found"));
```

## Transforming Optional

```java
Optional<String> name = Optional.of("alice");

// map - transform value
Optional<String> upper = name.map(String::toUpperCase);  // Optional["ALICE"]

// flatMap - when transformation returns Optional
Optional<String> result = name.flatMap(n -> findById(n));

// filter - keep if matches
Optional<String> filtered = name.filter(n -> n.length() > 3);  // Optional["alice"]
```

## Optional Chaining

```java
// Chain operations
String result = Optional.ofNullable(user)
    .map(User::getAddress)
    .map(Address::getCity)
    .map(String::toUpperCase)
    .orElse("UNKNOWN");

// stream() method (Java 9+)
List<String> names = optionalList.stream()
    .flatMap(Optional::stream)  // Filter out empty optionals
    .collect(Collectors.toList());
```

---

# 9. Common Patterns

## Find element by condition

```java
Optional<Person> found = people.stream()
    .filter(p -> p.getName().equals("Alice"))
    .findFirst();
```

## Group and count

```java
Map<String, Long> countByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.counting()
    ));
```

## Sum property

```java
int totalAge = people.stream()
    .mapToInt(Person::getAge)
    .sum();
```

## Partition by condition

```java
Map<Boolean, List<Person>> partitioned = people.stream()
    .collect(Collectors.partitioningBy(p -> p.getAge() >= 18));

List<Person> adults = partitioned.get(true);
List<Person> minors = partitioned.get(false);
```

## Create Map from List

```java
Map<Long, Person> byId = people.stream()
    .collect(Collectors.toMap(
        Person::getId,
        Function.identity()
    ));
```

## Join to String

```java
String names = people.stream()
    .map(Person::getName)
    .collect(Collectors.joining(", ", "[", "]"));
// "[Alice, Bob, Charlie]"
```

## Find max by property

```java
Optional<Person> oldest = people.stream()
    .max(Comparator.comparing(Person::getAge));
```

## Remove duplicates by property

```java
List<Person> unique = people.stream()
    .collect(Collectors.collectingAndThen(
        Collectors.toMap(
            Person::getEmail,
            Function.identity(),
            (p1, p2) -> p1  // Keep first on duplicate
        ),
        map -> new ArrayList<>(map.values())
    ));
```

---

# 10. Interview Questions

## Q1: What is the difference between map() and flatMap()?

**Answer:**
- `map()`: One-to-one transformation. Each element → one element.
- `flatMap()`: One-to-many + flatten. Each element → stream of elements, then flattened.

```java
// map: Stream<List<String>> (nested)
// flatMap: Stream<String> (flat)
```

---

## Q2: What is lazy evaluation in streams?

**Answer:**
Intermediate operations don't execute until a terminal operation is called. This allows:
- Short-circuit optimization
- Processing only needed elements
- Memory efficiency

---

## Q3: Can streams be reused?

**Answer:**
**No!** A stream can only be consumed once. After terminal operation, it's closed.

```java
Stream<String> stream = list.stream();
stream.forEach(System.out::println);
stream.count();  // IllegalStateException!
```

---

## Q4: When should you use parallel streams?

**Answer:**
- Large datasets (>10,000 elements)
- CPU-intensive operations
- Independent, stateless operations
- Data structures with good split (ArrayList, arrays)

**Avoid:** Small data, I/O operations, shared mutable state, LinkedList.

---

## Q5: Difference between findFirst() and findAny()?

**Answer:**
- `findFirst()`: Always returns first element (ordered)
- `findAny()`: Returns any element (faster in parallel)

In sequential streams, both usually return the same. In parallel, `findAny()` is faster.

---

## Q6: What is the difference between Collection and Stream?

**Answer:**
| Collection | Stream |
|------------|--------|
| Stores data | Processes data |
| External iteration | Internal iteration |
| Reusable | Single-use |
| Modifiable | Doesn't modify source |
| Eager | Lazy |

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│                  STREAMS CHEAT SHEET                        │
├─────────────────────────────────────────────────────────────┤
│ CREATE                                                      │
│   list.stream()                                             │
│   Stream.of(a, b, c)                                        │
│   Arrays.stream(array)                                      │
│   IntStream.range(1, 10)                                    │
├─────────────────────────────────────────────────────────────┤
│ INTERMEDIATE (return Stream)                                │
│   filter(predicate)    map(function)    flatMap(function)   │
│   distinct()           sorted()         limit(n)  skip(n)   │
│   peek(consumer)       takeWhile()      dropWhile()         │
├─────────────────────────────────────────────────────────────┤
│ TERMINAL (return result)                                    │
│   collect(collector)   toList()         toArray()           │
│   forEach(consumer)    count()          reduce(accumulator) │
│   min(comparator)      max(comparator)                      │
│   findFirst()          findAny()                            │
│   anyMatch()           allMatch()       noneMatch()         │
├─────────────────────────────────────────────────────────────┤
│ PRIMITIVE STREAMS                                           │
│   mapToInt(), mapToLong(), mapToDouble()                    │
│   sum(), average(), max(), min(), summaryStatistics()       │
└─────────────────────────────────────────────────────────────┘
```
