# Java Streams - Complete Guide 🌊

## Kid-Friendly Explanation 🧒

**Imagine a Factory Assembly Line:**
- Raw materials (data) enter from one end
- Go through different machines (operations)
- Final product (result) comes out the other end

```
[Data Source] → [Filter] → [Transform] → [Collect] → [Result]
     📦          🔍          🔄           📥         ✅
```

---

# 1. Creating Streams

## From Collections

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

// Stream from List
Stream<String> stream1 = names.stream();

// Parallel Stream from List
Stream<String> stream2 = names.parallelStream();
```

## From Arrays

```java
String[] arr = {"A", "B", "C"};
Stream<String> stream = Arrays.stream(arr);

// Primitive arrays
int[] nums = {1, 2, 3, 4, 5};
IntStream intStream = Arrays.stream(nums);
```

## Using Stream.of()

```java
Stream<String> stream = Stream.of("A", "B", "C");
Stream<Integer> numStream = Stream.of(1, 2, 3, 4, 5);
```

## Infinite Streams

```java
// Generate infinite stream
Stream<Double> randoms = Stream.generate(Math::random);

// Iterate with seed
Stream<Integer> evenNumbers = Stream.iterate(0, n -> n + 2);

// With limit
Stream<Integer> first10Even = Stream.iterate(0, n -> n + 2).limit(10);
```

## Primitive Streams

```java
IntStream intStream = IntStream.range(1, 10);      // 1 to 9
IntStream intStream2 = IntStream.rangeClosed(1, 10); // 1 to 10
LongStream longStream = LongStream.range(1, 100);
DoubleStream doubleStream = DoubleStream.of(1.1, 2.2, 3.3);
```

---

# 2. Stream Operations Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    STREAM OPERATIONS                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  INTERMEDIATE (Lazy)         TERMINAL (Triggers execution)  │
│  ----------------------      -----------------------------  │
│  filter()                    collect()                      │
│  map()                       forEach()                      │
│  flatMap()                   reduce()                       │
│  distinct()                  count()                        │
│  sorted()                    findFirst() / findAny()        │
│  limit()                     anyMatch() / allMatch()        │
│  skip()                      min() / max()                  │
│  peek()                      toArray()                      │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

# 3. Intermediate Operations

## filter() - Keep elements matching condition

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .toList();
// [2, 4, 6, 8, 10]
```

## map() - Transform each element

```java
List<String> names = List.of("alice", "bob", "charlie");

List<String> upperNames = names.stream()
    .map(String::toUpperCase)
    .toList();
// [ALICE, BOB, CHARLIE]

// Extract specific property
List<Integer> lengths = names.stream()
    .map(String::length)
    .toList();
// [5, 3, 7]
```

## flatMap() - Flatten nested structures

```java
List<List<Integer>> nested = List.of(
    List.of(1, 2, 3),
    List.of(4, 5, 6),
    List.of(7, 8, 9)
);

List<Integer> flat = nested.stream()
    .flatMap(List::stream)
    .toList();
// [1, 2, 3, 4, 5, 6, 7, 8, 9]

// Split words from sentences
List<String> sentences = List.of("Hello World", "Java Streams");
List<String> words = sentences.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .toList();
// [Hello, World, Java, Streams]
```

## distinct() - Remove duplicates

```java
List<Integer> nums = List.of(1, 2, 2, 3, 3, 3, 4);
List<Integer> unique = nums.stream()
    .distinct()
    .toList();
// [1, 2, 3, 4]
```

## sorted() - Sort elements

```java
List<String> names = List.of("Charlie", "Alice", "Bob");

// Natural order
List<String> sorted = names.stream()
    .sorted()
    .toList();
// [Alice, Bob, Charlie]

// Custom comparator
List<String> sortedByLength = names.stream()
    .sorted(Comparator.comparing(String::length))
    .toList();
// [Bob, Alice, Charlie]

// Reverse order
List<String> reversed = names.stream()
    .sorted(Comparator.reverseOrder())
    .toList();
// [Charlie, Bob, Alice]
```

## limit() & skip()

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// First 5 elements
List<Integer> first5 = nums.stream().limit(5).toList();
// [1, 2, 3, 4, 5]

// Skip first 5
List<Integer> skip5 = nums.stream().skip(5).toList();
// [6, 7, 8, 9, 10]

// Pagination: Page 2, Size 3
List<Integer> page2 = nums.stream()
    .skip(3)    // Skip page 1
    .limit(3)   // Take 3 items
    .toList();
// [4, 5, 6]
```

## peek() - Debug/Logging

```java
List<String> result = names.stream()
    .peek(n -> System.out.println("Before filter: " + n))
    .filter(n -> n.length() > 3)
    .peek(n -> System.out.println("After filter: " + n))
    .map(String::toUpperCase)
    .peek(n -> System.out.println("After map: " + n))
    .toList();
```

---

# 4. Terminal Operations

## forEach() - Perform action on each element

```java
names.stream()
    .forEach(System.out::println);

// With index (workaround)
AtomicInteger index = new AtomicInteger(0);
names.stream()
    .forEach(n -> System.out.println(index.getAndIncrement() + ": " + n));
```

## collect() - Gather results

```java
// To List
List<String> list = stream.collect(Collectors.toList());
List<String> list2 = stream.toList();  // Java 16+

// To Set
Set<String> set = stream.collect(Collectors.toSet());

// To Map
Map<String, Integer> map = names.stream()
    .collect(Collectors.toMap(
        name -> name,           // key
        String::length          // value
    ));
// {Alice=5, Bob=3, Charlie=7}
```

## reduce() - Combine elements into single result

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5);

// Sum
int sum = nums.stream()
    .reduce(0, (a, b) -> a + b);
// 15

// Sum with method reference
int sum2 = nums.stream()
    .reduce(0, Integer::sum);

// Max
Optional<Integer> max = nums.stream()
    .reduce(Integer::max);
// Optional[5]

// Concatenate strings
String joined = names.stream()
    .reduce("", (a, b) -> a + b);
// AliceBobCharlie
```

## count(), min(), max()

```java
long count = names.stream().count();

Optional<String> min = names.stream().min(Comparator.naturalOrder());
Optional<String> max = names.stream().max(Comparator.naturalOrder());

// For primitives
int minNum = IntStream.of(3, 1, 4, 1, 5).min().orElse(0);
int maxNum = IntStream.of(3, 1, 4, 1, 5).max().orElse(0);
double avg = IntStream.of(1, 2, 3, 4, 5).average().orElse(0);
int sum = IntStream.of(1, 2, 3, 4, 5).sum();
```

## findFirst() & findAny()

```java
Optional<String> first = names.stream()
    .filter(n -> n.startsWith("A"))
    .findFirst();
// Optional[Alice]

// For parallel streams, findAny() is faster
Optional<String> any = names.parallelStream()
    .filter(n -> n.length() > 3)
    .findAny();
```

## anyMatch(), allMatch(), noneMatch()

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5);

boolean hasEven = nums.stream().anyMatch(n -> n % 2 == 0);   // true
boolean allPositive = nums.stream().allMatch(n -> n > 0);    // true
boolean noneNegative = nums.stream().noneMatch(n -> n < 0);  // true
```

## toArray()

```java
String[] array = names.stream().toArray(String[]::new);
Integer[] numArray = nums.stream().toArray(Integer[]::new);
```

---

# 5. Collectors - Complete Guide 📥

## Basic Collectors

```java
import java.util.stream.Collectors;

// To List (mutable)
List<String> list = stream.collect(Collectors.toList());

// To List (immutable) - Java 10+
List<String> immutableList = stream.collect(Collectors.toUnmodifiableList());

// To Set
Set<String> set = stream.collect(Collectors.toSet());

// To specific collection
TreeSet<String> treeSet = stream.collect(Collectors.toCollection(TreeSet::new));
LinkedList<String> linked = stream.collect(Collectors.toCollection(LinkedList::new));
```

## Collectors.toMap()

```java
// Basic: key -> value
Map<String, Integer> nameToLength = names.stream()
    .collect(Collectors.toMap(
        name -> name,        // key mapper
        String::length       // value mapper
    ));

// Handle duplicate keys
Map<Integer, String> lengthToName = names.stream()
    .collect(Collectors.toMap(
        String::length,
        name -> name,
        (existing, replacement) -> existing + ", " + replacement  // merge function
    ));

// With specific Map type
TreeMap<String, Integer> treeMap = names.stream()
    .collect(Collectors.toMap(
        name -> name,
        String::length,
        (a, b) -> a,
        TreeMap::new
    ));
```

## Collectors.groupingBy()

```java
List<String> names = List.of("Alice", "Bob", "Anna", "Charlie", "Chris");

// Group by first letter
Map<Character, List<String>> byFirstLetter = names.stream()
    .collect(Collectors.groupingBy(name -> name.charAt(0)));
// {A=[Alice, Anna], B=[Bob], C=[Charlie, Chris]}

// Group by length
Map<Integer, List<String>> byLength = names.stream()
    .collect(Collectors.groupingBy(String::length));
// {3=[Bob], 4=[Anna], 5=[Alice, Chris], 7=[Charlie]}

// Group and count
Map<Integer, Long> countByLength = names.stream()
    .collect(Collectors.groupingBy(
        String::length,
        Collectors.counting()
    ));
// {3=1, 4=1, 5=2, 7=1}

// Group and collect to Set
Map<Integer, Set<String>> byLengthSet = names.stream()
    .collect(Collectors.groupingBy(
        String::length,
        Collectors.toSet()
    ));

// Multi-level grouping
Map<Integer, Map<Character, List<String>>> multilevel = names.stream()
    .collect(Collectors.groupingBy(
        String::length,
        Collectors.groupingBy(name -> name.charAt(0))
    ));
```

## Collectors.partitioningBy()

```java
// Split into two groups based on predicate
Map<Boolean, List<String>> partitioned = names.stream()
    .collect(Collectors.partitioningBy(name -> name.length() > 4));
// {false=[Bob, Anna], true=[Alice, Charlie, Chris]}
```

## Collectors.joining()

```java
// Simple join
String joined = names.stream()
    .collect(Collectors.joining());
// "AliceBobAnnaCharlieChris"

// With delimiter
String csv = names.stream()
    .collect(Collectors.joining(", "));
// "Alice, Bob, Anna, Charlie, Chris"

// With prefix/suffix
String formatted = names.stream()
    .collect(Collectors.joining(", ", "[", "]"));
// "[Alice, Bob, Anna, Charlie, Chris]"
```

## Collectors for Statistics

```java
// Counting
long count = names.stream()
    .collect(Collectors.counting());

// Summing
int totalLength = names.stream()
    .collect(Collectors.summingInt(String::length));

// Averaging
double avgLength = names.stream()
    .collect(Collectors.averagingInt(String::length));

// Statistics (all at once)
IntSummaryStatistics stats = names.stream()
    .collect(Collectors.summarizingInt(String::length));
System.out.println("Count: " + stats.getCount());
System.out.println("Sum: " + stats.getSum());
System.out.println("Min: " + stats.getMin());
System.out.println("Max: " + stats.getMax());
System.out.println("Avg: " + stats.getAverage());
```

## Collectors.mapping() & Collectors.flatMapping()

```java
// Map before collecting
Map<Integer, List<String>> upperByLength = names.stream()
    .collect(Collectors.groupingBy(
        String::length,
        Collectors.mapping(String::toUpperCase, Collectors.toList())
    ));

// Flat map before collecting (Java 9+)
Map<Integer, Set<Character>> charsByLength = names.stream()
    .collect(Collectors.groupingBy(
        String::length,
        Collectors.flatMapping(
            name -> name.chars().mapToObj(c -> (char) c),
            Collectors.toSet()
        )
    ));
```

## Collectors.reducing()

```java
// Reduce within groups
Map<Integer, Optional<String>> longestByLength = names.stream()
    .collect(Collectors.groupingBy(
        String::length,
        Collectors.reducing((a, b) -> a.compareTo(b) > 0 ? a : b)
    ));
```

## Collectors.collectingAndThen()

```java
// Transform the final result
List<String> unmodifiable = names.stream()
    .collect(Collectors.collectingAndThen(
        Collectors.toList(),
        Collections::unmodifiableList
    ));
```

---

# 6. Parallel Streams ⚡

## Create Parallel Stream

```java
// From collection
Stream<String> parallel = names.parallelStream();

// Convert sequential to parallel
Stream<String> parallel2 = names.stream().parallel();

// Convert parallel to sequential
Stream<String> sequential = parallel.sequential();
```

## Kid-Friendly Explanation 🧒

**Sequential Stream = One Chef 👨‍🍳**
- Cooks dishes one by one
- Predictable order

**Parallel Stream = Multiple Chefs 👨‍🍳👨‍🍳👨‍🍳**
- Each chef cooks different dishes simultaneously
- Faster but order may vary

---

## When to Use Parallel Streams

### ✅ GOOD for Parallel

```java
// Large data sets with CPU-intensive operations
List<Integer> result = hugeList.parallelStream()
    .map(this::expensiveComputation)
    .toList();

// Independent operations
long sum = LongStream.range(1, 100_000_000)
    .parallel()
    .sum();
```

### ❌ BAD for Parallel

```java
// Small data sets (overhead > benefit)
List<String> small = List.of("A", "B", "C");
small.parallelStream()...  // Overhead not worth it!

// Ordered operations
list.parallelStream()
    .forEachOrdered(System.out::println);  // Defeats the purpose!

// Shared mutable state
List<Integer> results = new ArrayList<>();
numbers.parallelStream()
    .forEach(n -> results.add(n));  // ❌ Race condition!
```

---

## Parallel vs Sequential Comparison

```java
List<Integer> numbers = IntStream.range(1, 10_000_000)
    .boxed()
    .toList();

// Sequential
long start1 = System.currentTimeMillis();
long sum1 = numbers.stream()
    .mapToLong(n -> n * n)
    .sum();
long time1 = System.currentTimeMillis() - start1;

// Parallel
long start2 = System.currentTimeMillis();
long sum2 = numbers.parallelStream()
    .mapToLong(n -> n * n)
    .sum();
long time2 = System.currentTimeMillis() - start2;

System.out.println("Sequential: " + time1 + "ms");
System.out.println("Parallel: " + time2 + "ms");
// Parallel is often 3-4x faster on multi-core machines!
```

---

## Parallel Stream Gotchas ⚠️

### 1. Order Not Guaranteed

```java
// ❌ Order may vary
names.parallelStream()
    .forEach(System.out::println);

// ✅ Use forEachOrdered if order matters
names.parallelStream()
    .forEachOrdered(System.out::println);
```

### 2. Thread Safety Required

```java
// ❌ NOT thread-safe
List<Integer> results = new ArrayList<>();
numbers.parallelStream()
    .forEach(results::add);  // Race condition!

// ✅ Use thread-safe collection or collect()
List<Integer> results = numbers.parallelStream()
    .collect(Collectors.toList());
```

### 3. Avoid Stateful Operations

```java
// ❌ Stateful lambda - unpredictable
AtomicInteger counter = new AtomicInteger();
numbers.parallelStream()
    .map(n -> n + counter.incrementAndGet())  // Bad!
    .toList();

// ✅ Stateless operations
numbers.parallelStream()
    .map(n -> n * 2)  // Good!
    .toList();
```

---

## Custom Thread Pool for Parallel Streams

```java
// By default, uses ForkJoinPool.commonPool()
// To use custom pool:

ForkJoinPool customPool = new ForkJoinPool(4);  // 4 threads

List<Integer> result = customPool.submit(() ->
    numbers.parallelStream()
        .map(this::process)
        .toList()
).get();

customPool.shutdown();
```

---

# 7. Primitive Streams

```java
// IntStream, LongStream, DoubleStream - avoid boxing overhead

IntStream.range(1, 10)           // 1 to 9
    .sum();                       // 45

IntStream.rangeClosed(1, 10)     // 1 to 10
    .average()                    // OptionalDouble[5.5]
    .orElse(0);

// Convert object stream to primitive
int totalLength = names.stream()
    .mapToInt(String::length)     // IntStream
    .sum();

// Convert primitive to object
List<Integer> boxed = IntStream.range(1, 5)
    .boxed()                      // Stream<Integer>
    .toList();
```

---

# 8. Optional with Streams

```java
// findFirst returns Optional
Optional<String> first = names.stream()
    .filter(n -> n.startsWith("X"))
    .findFirst();

// Handle Optional
String result = first.orElse("Not found");
String result2 = first.orElseGet(() -> "Computed default");
String result3 = first.orElseThrow(() -> new RuntimeException("Not found"));

// Stream of Optionals
List<Optional<String>> optionals = List.of(
    Optional.of("A"),
    Optional.empty(),
    Optional.of("B")
);

// Extract present values (Java 9+)
List<String> values = optionals.stream()
    .flatMap(Optional::stream)
    .toList();
// [A, B]
```

---

# 9. Stream Pipeline Best Practices

## ✅ DO's

```java
// 1. Use method references when possible
.map(String::toUpperCase)  // ✅
.map(s -> s.toUpperCase()) // Works, but verbose

// 2. Prefer primitive streams for numbers
.mapToInt(String::length)  // ✅ No boxing
.map(s -> s.length())      // ❌ Boxing Integer

// 3. Use short-circuiting operations
.findFirst()  // Stops after finding one
.anyMatch()   // Stops on first match
.limit(10)    // Only processes 10 elements
```

## ❌ DON'Ts

```java
// 1. Don't reuse streams
Stream<String> stream = names.stream();
stream.forEach(System.out::println);
stream.count();  // ❌ IllegalStateException!

// 2. Don't modify source during stream
names.stream()
    .forEach(n -> names.add("X"));  // ❌ ConcurrentModificationException

// 3. Don't use streams for simple iterations
// ❌ Overkill
names.stream().forEach(System.out::println);
// ✅ Simple
for (String name : names) {
    System.out.println(name);
}
```

---

# 10. Stream vs Loop Comparison

| Aspect | Stream | Loop |
|--------|--------|------|
| Readability | Declarative, fluent | Imperative |
| Performance | Slight overhead | Faster for simple ops |
| Parallelism | Easy (.parallel()) | Manual threading |
| Debugging | Harder | Easier step-through |
| Use when | Complex transformations | Simple iterations |

---

# 11. Common Stream Patterns 🏦

## Banking Examples

```java
record Transaction(String id, String type, double amount, LocalDate date) {}

List<Transaction> transactions = List.of(
    new Transaction("T1", "DEPOSIT", 1000, LocalDate.of(2024, 1, 15)),
    new Transaction("T2", "WITHDRAWAL", 200, LocalDate.of(2024, 1, 16)),
    new Transaction("T3", "DEPOSIT", 500, LocalDate.of(2024, 1, 17)),
    new Transaction("T4", "WITHDRAWAL", 150, LocalDate.of(2024, 2, 1))
);

// Total deposits
double totalDeposits = transactions.stream()
    .filter(t -> t.type().equals("DEPOSIT"))
    .mapToDouble(Transaction::amount)
    .sum();
// 1500.0

// Group by type
Map<String, List<Transaction>> byType = transactions.stream()
    .collect(Collectors.groupingBy(Transaction::type));

// Sum by type
Map<String, Double> sumByType = transactions.stream()
    .collect(Collectors.groupingBy(
        Transaction::type,
        Collectors.summingDouble(Transaction::amount)
    ));
// {DEPOSIT=1500.0, WITHDRAWAL=350.0}

// Group by month
Map<Month, List<Transaction>> byMonth = transactions.stream()
    .collect(Collectors.groupingBy(t -> t.date().getMonth()));

// Top 3 largest transactions
List<Transaction> top3 = transactions.stream()
    .sorted(Comparator.comparing(Transaction::amount).reversed())
    .limit(3)
    .toList();
```

---

# 12. Quick Reference Card

```
┌─────────────────────────────────────────────────────────────┐
│                   STREAM CHEAT SHEET                        │
├─────────────────────────────────────────────────────────────┤
│ CREATE STREAM                                               │
│   list.stream() | list.parallelStream()                     │
│   Arrays.stream(arr) | Stream.of(a, b, c)                  │
│   IntStream.range(1, 10)                                   │
├─────────────────────────────────────────────────────────────┤
│ INTERMEDIATE OPERATIONS                                      │
│   filter(predicate) | map(function) | flatMap(function)    │
│   distinct() | sorted() | limit(n) | skip(n) | peek()      │
├─────────────────────────────────────────────────────────────┤
│ TERMINAL OPERATIONS                                          │
│   collect(collector) | forEach(consumer) | reduce()         │
│   count() | min() | max() | sum() | average()              │
│   findFirst() | findAny() | anyMatch() | allMatch()        │
├─────────────────────────────────────────────────────────────┤
│ COLLECTORS                                                   │
│   toList() | toSet() | toMap(k, v)                          │
│   groupingBy(classifier) | partitioningBy(predicate)        │
│   joining(delimiter) | counting() | summarizingInt()        │
├─────────────────────────────────────────────────────────────┤
│ PARALLEL STREAMS                                             │
│   parallelStream() | stream().parallel()                    │
│   ⚠️ Avoid: shared mutable state, ordered ops, small data  │
└─────────────────────────────────────────────────────────────┘
```

---

# 13. Interview Questions

**Q1: What is the difference between intermediate and terminal operations?**
> Intermediate: Returns a stream, lazy (don't execute until terminal)
> Terminal: Produces a result or side-effect, triggers execution

**Q2: When should you use parallel streams?**
> Large datasets with independent, CPU-intensive operations. Avoid for small data, I/O operations, or when order matters.

**Q3: What is the difference between map() and flatMap()?**
> map(): One-to-one transformation (element → element)
> flatMap(): One-to-many transformation (element → stream of elements), flattens nested structures

**Q4: Why can't streams be reused?**
> Streams are designed for single traversal. After a terminal operation, the stream is consumed and closed.

**Q5: What is the difference between findFirst() and findAny()?**
> findFirst(): Returns first element (in encounter order)
> findAny(): Returns any element (better for parallel streams, non-deterministic)
