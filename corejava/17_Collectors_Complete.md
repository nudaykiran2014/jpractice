# Java Collectors - Complete Guide

## Table of Contents
1. [What are Collectors?](#1-what-are-collectors)
2. [Basic Collectors](#2-basic-collectors)
3. [String Collectors](#3-string-collectors)
4. [Numeric Collectors](#4-numeric-collectors)
5. [Grouping Collectors](#5-grouping-collectors)
6. [Partitioning](#6-partitioning)
7. [Mapping & Reducing](#7-mapping--reducing)
8. [Advanced Collectors](#8-advanced-collectors)
9. [Custom Collectors](#9-custom-collectors)
10. [Interview Questions](#10-interview-questions)

---

# 1. What are Collectors?

## 🧒 Kid-Friendly Explanation

**Collector = A bucket that collects stream results**

```
Stream: [apple, banana, cherry]
              ↓
         Collector
              ↓
Result: List, Set, Map, String, Count...
```

## Basic Usage

```java
import java.util.stream.Collectors;

// Collect to List
List<String> list = stream.collect(Collectors.toList());

// Collect to Set
Set<String> set = stream.collect(Collectors.toSet());

// Collect to String
String joined = stream.collect(Collectors.joining(", "));
```

---

# 2. Basic Collectors

## toList()

```java
List<String> names = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());

// Java 16+ (shorter, returns unmodifiable list)
List<String> names = people.stream()
    .map(Person::getName)
    .toList();
```

## toUnmodifiableList() (Java 10+)

```java
List<String> immutable = names.stream()
    .collect(Collectors.toUnmodifiableList());

immutable.add("new");  // UnsupportedOperationException!
```

## toSet()

```java
Set<String> uniqueNames = people.stream()
    .map(Person::getName)
    .collect(Collectors.toSet());  // HashSet by default
```

## toUnmodifiableSet() (Java 10+)

```java
Set<String> immutable = names.stream()
    .collect(Collectors.toUnmodifiableSet());
```

## toCollection() - Specific Collection Type

```java
// To TreeSet (sorted)
TreeSet<String> sorted = names.stream()
    .collect(Collectors.toCollection(TreeSet::new));

// To LinkedList
LinkedList<String> linked = names.stream()
    .collect(Collectors.toCollection(LinkedList::new));

// To LinkedHashSet (insertion order)
LinkedHashSet<String> ordered = names.stream()
    .collect(Collectors.toCollection(LinkedHashSet::new));
```

## toMap()

```java
// Basic: key and value extractors
Map<Long, String> idToName = people.stream()
    .collect(Collectors.toMap(
        Person::getId,      // Key
        Person::getName     // Value
    ));

// With identity (object itself as value)
Map<Long, Person> byId = people.stream()
    .collect(Collectors.toMap(
        Person::getId,
        Function.identity()
    ));

// Handle duplicate keys (merge function)
Map<String, Person> byName = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Function.identity(),
        (existing, replacement) -> existing  // Keep first
    ));

// Specify Map type
TreeMap<Long, String> sorted = people.stream()
    .collect(Collectors.toMap(
        Person::getId,
        Person::getName,
        (a, b) -> a,
        TreeMap::new
    ));
```

## toUnmodifiableMap() (Java 10+)

```java
Map<Long, String> immutable = people.stream()
    .collect(Collectors.toUnmodifiableMap(
        Person::getId,
        Person::getName
    ));
```

## toConcurrentMap()

```java
// Thread-safe map for parallel streams
ConcurrentMap<Long, String> concurrent = people.parallelStream()
    .collect(Collectors.toConcurrentMap(
        Person::getId,
        Person::getName
    ));
```

---

# 3. String Collectors

## joining()

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

// Simple join
String simple = names.stream()
    .collect(Collectors.joining());
// "AliceBobCharlie"

// With delimiter
String delimited = names.stream()
    .collect(Collectors.joining(", "));
// "Alice, Bob, Charlie"

// With delimiter, prefix, suffix
String bracketed = names.stream()
    .collect(Collectors.joining(", ", "[", "]"));
// "[Alice, Bob, Charlie]"

// For empty stream
String empty = Stream.<String>empty()
    .collect(Collectors.joining(", ", "[", "]"));
// "[]"
```

## Practical Examples

```java
// CSV line
String csv = values.stream()
    .collect(Collectors.joining(","));

// SQL IN clause
String inClause = ids.stream()
    .map(String::valueOf)
    .collect(Collectors.joining(", ", "(", ")"));
// "(1, 2, 3, 4)"

// HTML list
String html = items.stream()
    .map(i -> "<li>" + i + "</li>")
    .collect(Collectors.joining("\n", "<ul>\n", "\n</ul>"));
```

---

# 4. Numeric Collectors

## counting()

```java
long count = people.stream()
    .filter(p -> p.getAge() > 18)
    .collect(Collectors.counting());

// Shorter alternative
long count = people.stream()
    .filter(p -> p.getAge() > 18)
    .count();
```

## summingInt(), summingLong(), summingDouble()

```java
// Sum of ages
int totalAge = people.stream()
    .collect(Collectors.summingInt(Person::getAge));

// Sum of salaries
long totalSalary = people.stream()
    .collect(Collectors.summingLong(Person::getSalary));

// Sum of prices
double totalPrice = products.stream()
    .collect(Collectors.summingDouble(Product::getPrice));
```

## averagingInt(), averagingLong(), averagingDouble()

```java
// Average age
double avgAge = people.stream()
    .collect(Collectors.averagingInt(Person::getAge));

// Average salary
double avgSalary = people.stream()
    .collect(Collectors.averagingDouble(Person::getSalary));
```

## summarizingInt(), summarizingLong(), summarizingDouble()

```java
// Get all stats at once!
IntSummaryStatistics stats = people.stream()
    .collect(Collectors.summarizingInt(Person::getAge));

stats.getCount();    // Number of elements
stats.getSum();      // Total sum
stats.getMin();      // Minimum value
stats.getMax();      // Maximum value
stats.getAverage();  // Average value

// Example output
// IntSummaryStatistics{count=10, sum=350, min=18, average=35.0, max=65}
```

## maxBy() and minBy()

```java
// Find oldest person
Optional<Person> oldest = people.stream()
    .collect(Collectors.maxBy(Comparator.comparing(Person::getAge)));

// Find youngest person
Optional<Person> youngest = people.stream()
    .collect(Collectors.minBy(Comparator.comparing(Person::getAge)));

// Find most expensive product
Optional<Product> mostExpensive = products.stream()
    .collect(Collectors.maxBy(Comparator.comparing(Product::getPrice)));
```

---

# 5. Grouping Collectors

## groupingBy() - Basic

```java
// Group by single property
Map<String, List<Person>> byCity = people.stream()
    .collect(Collectors.groupingBy(Person::getCity));

// Result: {"New York": [p1, p2], "London": [p3], ...}
```

## groupingBy() - With Downstream Collector

```java
// Group and count
Map<String, Long> countByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.counting()
    ));
// {"New York": 5, "London": 3, ...}

// Group and sum
Map<String, Integer> totalAgeByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.summingInt(Person::getAge)
    ));

// Group and average
Map<String, Double> avgAgeByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.averagingDouble(Person::getAge)
    ));

// Group and get names only
Map<String, List<String>> namesByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.mapping(Person::getName, Collectors.toList())
    ));

// Group and join names
Map<String, String> joinedNamesByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.mapping(
            Person::getName,
            Collectors.joining(", ")
        )
    ));
```

## groupingBy() - With Map Type

```java
// Use TreeMap for sorted keys
TreeMap<String, List<Person>> sortedByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        TreeMap::new,
        Collectors.toList()
    ));

// Use LinkedHashMap to preserve insertion order
LinkedHashMap<String, List<Person>> orderedByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        LinkedHashMap::new,
        Collectors.toList()
    ));
```

## Multi-level Grouping

```java
// Group by city, then by department
Map<String, Map<String, List<Person>>> byCity ThenDept = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.groupingBy(Person::getDepartment)
    ));

// Access: byCity ThenDept.get("New York").get("Engineering")
```

## groupingByConcurrent()

```java
// Thread-safe grouping for parallel streams
ConcurrentMap<String, List<Person>> concurrent = people.parallelStream()
    .collect(Collectors.groupingByConcurrent(Person::getCity));
```

---

# 6. Partitioning

## partitioningBy() - Split into Two Groups

```java
// Partition by boolean condition
Map<Boolean, List<Person>> partition = people.stream()
    .collect(Collectors.partitioningBy(p -> p.getAge() >= 18));

List<Person> adults = partition.get(true);
List<Person> minors = partition.get(false);
```

## partitioningBy() - With Downstream Collector

```java
// Partition and count
Map<Boolean, Long> countPartition = people.stream()
    .collect(Collectors.partitioningBy(
        p -> p.getAge() >= 18,
        Collectors.counting()
    ));
// {true: 80, false: 20}

// Partition and get names
Map<Boolean, List<String>> namePartition = people.stream()
    .collect(Collectors.partitioningBy(
        p -> p.getAge() >= 18,
        Collectors.mapping(Person::getName, Collectors.toList())
    ));

// Partition and get oldest in each group
Map<Boolean, Optional<Person>> oldestPartition = people.stream()
    .collect(Collectors.partitioningBy(
        p -> p.getAge() >= 18,
        Collectors.maxBy(Comparator.comparing(Person::getAge))
    ));
```

## Difference: groupingBy vs partitioningBy

| groupingBy | partitioningBy |
|------------|----------------|
| Any number of groups | Exactly 2 groups (true/false) |
| Key is any type | Key is always Boolean |
| Null values handled | Always has both keys |

```java
// groupingBy: May not have all keys
Map<String, List<Person>> byCity = ...;  // May have 0-N cities

// partitioningBy: Always has true and false
Map<Boolean, List<Person>> byAge = ...;  // Always has true AND false keys
```

---

# 7. Mapping & Reducing

## mapping()

Transform elements before collecting:

```java
// Get list of names (instead of persons)
Map<String, List<String>> namesByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.mapping(
            Person::getName,
            Collectors.toList()
        )
    ));

// Get set of emails
Map<String, Set<String>> emailsByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.mapping(
            Employee::getEmail,
            Collectors.toSet()
        )
    ));
```

## flatMapping() (Java 9+)

Flatten and collect:

```java
// Each person has multiple phone numbers
Map<String, Set<String>> phonesByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.flatMapping(
            p -> p.getPhoneNumbers().stream(),
            Collectors.toSet()
        )
    ));
```

## filtering() (Java 9+)

Filter before collecting:

```java
// Group by city, but only include adults
Map<String, List<Person>> adultsByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.filtering(
            p -> p.getAge() >= 18,
            Collectors.toList()
        )
    ));

// Note: Cities with no adults will have empty lists (not missing)
```

## reducing()

```java
// Sum of all ages
Optional<Integer> totalAge = people.stream()
    .map(Person::getAge)
    .collect(Collectors.reducing(Integer::sum));

// With identity
int total = people.stream()
    .map(Person::getAge)
    .collect(Collectors.reducing(0, Integer::sum));

// Full form: identity, mapper, combiner
int total = people.stream()
    .collect(Collectors.reducing(
        0,                          // Identity
        Person::getAge,             // Mapper
        Integer::sum                // Combiner
    ));

// Find longest name per city
Map<String, Optional<String>> longestNameByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.mapping(
            Person::getName,
            Collectors.reducing((a, b) -> a.length() > b.length() ? a : b)
        )
    ));
```

---

# 8. Advanced Collectors

## collectingAndThen()

Apply finishing transformation:

```java
// Collect to unmodifiable list
List<String> immutable = names.stream()
    .collect(Collectors.collectingAndThen(
        Collectors.toList(),
        Collections::unmodifiableList
    ));

// Get count as int (not long)
int count = people.stream()
    .collect(Collectors.collectingAndThen(
        Collectors.counting(),
        Long::intValue
    ));

// Get single element or throw
Person single = people.stream()
    .filter(p -> p.getId() == 123)
    .collect(Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
            if (list.size() != 1) {
                throw new IllegalStateException("Expected exactly one");
            }
            return list.get(0);
        }
    ));
```

## teeing() (Java 12+)

Combine two collectors:

```java
// Get both min and max in single pass
record MinMax(Optional<Integer> min, Optional<Integer> max) {}

MinMax result = numbers.stream()
    .collect(Collectors.teeing(
        Collectors.minBy(Comparator.naturalOrder()),
        Collectors.maxBy(Comparator.naturalOrder()),
        MinMax::new
    ));

// Get both count and sum
record CountSum(long count, int sum) {}

CountSum stats = numbers.stream()
    .collect(Collectors.teeing(
        Collectors.counting(),
        Collectors.summingInt(Integer::intValue),
        CountSum::new
    ));

// Calculate average manually
double average = numbers.stream()
    .collect(Collectors.teeing(
        Collectors.summingDouble(Integer::doubleValue),
        Collectors.counting(),
        (sum, count) -> count == 0 ? 0 : sum / count
    ));
```

## Nested Downstream Collectors

```java
// Complex: Group by city, then get average age per city, sorted
Map<String, Double> avgAgeByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        TreeMap::new,  // Sorted map
        Collectors.averagingDouble(Person::getAge)
    ));

// Group by city, count adults vs minors
Map<String, Map<Boolean, Long>> adultCountByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.partitioningBy(
            p -> p.getAge() >= 18,
            Collectors.counting()
        )
    ));
// {"New York": {true: 45, false: 12}, "London": {true: 30, false: 8}}
```

---

# 9. Custom Collectors

## Collector Interface

```java
public interface Collector<T, A, R> {
    Supplier<A> supplier();           // Create accumulator
    BiConsumer<A, T> accumulator();   // Add element
    BinaryOperator<A> combiner();     // Merge accumulators (parallel)
    Function<A, R> finisher();        // Final transformation
    Set<Characteristics> characteristics();
}
```

## Creating Custom Collector

```java
// Custom collector: Join strings with limit
public static Collector<String, ?, String> joiningWithLimit(int limit) {
    return Collector.of(
        StringBuilder::new,                      // Supplier
        (sb, s) -> {                             // Accumulator
            if (sb.length() < limit) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(s);
            }
        },
        (sb1, sb2) -> {                          // Combiner
            if (sb1.length() < limit) {
                sb1.append(sb2);
            }
            return sb1;
        },
        StringBuilder::toString                   // Finisher
    );
}

// Usage
String result = names.stream()
    .collect(joiningWithLimit(50));
```

## Using Collector.of()

```java
// Collect to ImmutableList (Guava)
Collector<String, ?, ImmutableList<String>> toImmutableList =
    Collector.of(
        ImmutableList::builder,
        ImmutableList.Builder::add,
        (b1, b2) -> b1.addAll(b2.build()),
        ImmutableList.Builder::build
    );

// Collect to comma-separated with quotes
Collector<String, StringJoiner, String> quotedCsv =
    Collector.of(
        () -> new StringJoiner(","),
        (j, s) -> j.add("\"" + s + "\""),
        StringJoiner::merge,
        StringJoiner::toString
    );

String csv = names.stream().collect(quotedCsv);
// "Alice","Bob","Charlie"
```

---

# 10. Interview Questions

## Q1: What is the difference between toList() and collect(Collectors.toList())?

**Answer:**
| `toList()` (Java 16+) | `collect(Collectors.toList())` |
|-----------------------|-------------------------------|
| Returns unmodifiable list | Returns modifiable ArrayList |
| Shorter syntax | More verbose |
| No null elements allowed | Nulls allowed |

---

## Q2: When would you use groupingBy vs partitioningBy?

**Answer:**
- **groupingBy**: Multiple groups based on any classifier
- **partitioningBy**: Exactly 2 groups (true/false) based on predicate

`partitioningBy` always returns both keys, while `groupingBy` may have missing keys.

---

## Q3: How do you handle duplicate keys in toMap()?

**Answer:**
Provide a merge function:
```java
Collectors.toMap(
    Person::getName,
    Function.identity(),
    (existing, replacement) -> existing  // or throw, or merge
)
```

---

## Q4: What is the purpose of collectingAndThen()?

**Answer:**
Applies a final transformation after collecting:
```java
Collectors.collectingAndThen(
    Collectors.toList(),
    Collections::unmodifiableList  // Final transformation
)
```

---

## Q5: What does teeing() collector do?

**Answer:**
Combines two collectors and merges their results:
```java
Collectors.teeing(
    collector1,
    collector2,
    (result1, result2) -> combine(result1, result2)
)
```
Useful for computing multiple aggregations in a single pass.

---

## Q6: How to create thread-safe collections with collectors?

**Answer:**
```java
// Use concurrent collectors
Collectors.toConcurrentMap(...)
Collectors.groupingByConcurrent(...)

// Or wrap in synchronized
Collectors.collectingAndThen(
    Collectors.toList(),
    Collections::synchronizedList
)
```

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│                  COLLECTORS CHEAT SHEET                     │
├─────────────────────────────────────────────────────────────┤
│ BASIC                                                       │
│   toList(), toSet(), toCollection(TreeSet::new)             │
│   toMap(keyFn, valueFn), toMap(keyFn, valueFn, mergeFn)     │
│   toUnmodifiableList(), toUnmodifiableSet()                 │
├─────────────────────────────────────────────────────────────┤
│ STRINGS                                                     │
│   joining()                                                 │
│   joining(delimiter)                                        │
│   joining(delimiter, prefix, suffix)                        │
├─────────────────────────────────────────────────────────────┤
│ NUMERIC                                                     │
│   counting()                                                │
│   summingInt/Long/Double(fn)                                │
│   averagingInt/Long/Double(fn)                              │
│   summarizingInt/Long/Double(fn)                            │
│   maxBy(comparator), minBy(comparator)                      │
├─────────────────────────────────────────────────────────────┤
│ GROUPING                                                    │
│   groupingBy(classifier)                                    │
│   groupingBy(classifier, downstream)                        │
│   groupingBy(classifier, mapFactory, downstream)            │
│   partitioningBy(predicate)                                 │
│   partitioningBy(predicate, downstream)                     │
├─────────────────────────────────────────────────────────────┤
│ TRANSFORMING                                                │
│   mapping(fn, downstream)                                   │
│   flatMapping(fn, downstream)                               │
│   filtering(predicate, downstream)                          │
│   reducing(identity, accumulator)                           │
│   collectingAndThen(collector, finisher)                    │
│   teeing(collector1, collector2, merger)                    │
└─────────────────────────────────────────────────────────────┘
```
