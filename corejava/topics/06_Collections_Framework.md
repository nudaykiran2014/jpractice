# Java Collections Framework - Complete Guide
## Part 6: List, Set, Map, Queue with All Methods

---

# 1. Collection Hierarchy

```
Iterable<E>
└── Collection<E>
    ├── List<E> (ordered, duplicates allowed)
    │   ├── ArrayList<E>
    │   ├── LinkedList<E>
    │   ├── Vector<E>
    │   └── Stack<E>
    │
    ├── Set<E> (no duplicates)
    │   ├── HashSet<E>
    │   ├── LinkedHashSet<E>
    │   └── TreeSet<E> (SortedSet)
    │
    └── Queue<E>
        ├── PriorityQueue<E>
        ├── ArrayDeque<E> (Deque)
        └── LinkedList<E> (Deque)

Map<K,V> (separate hierarchy)
├── HashMap<K,V>
├── LinkedHashMap<K,V>
├── TreeMap<K,V> (SortedMap)
├── Hashtable<K,V>
└── ConcurrentHashMap<K,V>
```

---

# 2. List Interface

## 2.1 ArrayList

```java
// Creation
List<String> list = new ArrayList<>();
List<String> list2 = new ArrayList<>(100);  // Initial capacity
List<String> list3 = new ArrayList<>(existingCollection);

// Add elements
list.add("Apple");                 // Add to end
list.add(0, "Banana");            // Add at index
list.addAll(Arrays.asList("C", "D"));  // Add collection

// Access elements
String first = list.get(0);        // Get by index
int size = list.size();            // Number of elements
boolean empty = list.isEmpty();    // Check if empty

// Search
int index = list.indexOf("Apple");     // First occurrence (-1 if not found)
int last = list.lastIndexOf("Apple"); // Last occurrence
boolean has = list.contains("Apple");  // Check existence

// Modify
list.set(0, "NewValue");          // Replace at index
list.remove(0);                    // Remove by index
list.remove("Apple");             // Remove by value (first occurrence)
list.removeIf(s -> s.startsWith("A")); // Remove matching (Java 8+)
list.replaceAll(String::toUpperCase);  // Transform all (Java 8+)

// Sublist
List<String> sub = list.subList(1, 3);  // [1, 3) - view, not copy!

// Sort
list.sort(Comparator.naturalOrder());
list.sort(Comparator.reverseOrder());
list.sort(Comparator.comparing(String::length));

// Convert
String[] array = list.toArray(new String[0]);
Object[] objArray = list.toArray();

// Clear
list.clear();
```

## 2.2 LinkedList

```java
LinkedList<String> linked = new LinkedList<>();

// Additional methods (Deque operations)
linked.addFirst("First");         // Add at beginning
linked.addLast("Last");           // Add at end
linked.offerFirst("First");       // Add at beginning (returns boolean)
linked.offerLast("Last");         // Add at end (returns boolean)

String first = linked.getFirst(); // Get first (throws if empty)
String last = linked.getLast();   // Get last (throws if empty)
String peekF = linked.peekFirst(); // Get first (null if empty)
String peekL = linked.peekLast();  // Get last (null if empty)

String removeF = linked.removeFirst(); // Remove first (throws if empty)
String removeL = linked.removeLast();  // Remove last (throws if empty)
String pollF = linked.pollFirst(); // Remove first (null if empty)
String pollL = linked.pollLast();  // Remove last (null if empty)

// Use as stack
linked.push("Element");           // Same as addFirst
String popped = linked.pop();     // Same as removeFirst

// Use as queue
linked.offer("Element");          // Add to end
String polled = linked.poll();    // Remove from front
```

## 2.3 ArrayList vs LinkedList

| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| get(index) | O(1) | O(n) |
| add(end) | O(1) amortized | O(1) |
| add(index) | O(n) | O(n) |
| remove(index) | O(n) | O(n) |
| Memory | Less | More (node references) |
| Best for | Random access | Frequent insert/delete |

---

# 3. Set Interface

## 3.1 HashSet

```java
Set<String> set = new HashSet<>();

// Add elements
set.add("Apple");           // Returns true if added
set.add("Apple");           // Returns false (duplicate)
set.addAll(List.of("A", "B", "C"));

// Check
boolean has = set.contains("Apple");
int size = set.size();
boolean empty = set.isEmpty();

// Remove
set.remove("Apple");
set.removeIf(s -> s.length() > 3);

// No order guarantee!
for (String s : set) {
    System.out.println(s);  // Random order
}

// Set operations
Set<String> other = Set.of("X", "Y", "Apple");
set.retainAll(other);  // Intersection (modifies set)
set.addAll(other);     // Union (modifies set)
set.removeAll(other);  // Difference (modifies set)
```

## 3.2 LinkedHashSet

```java
// Maintains insertion order
Set<String> linkedSet = new LinkedHashSet<>();
linkedSet.add("C");
linkedSet.add("A");
linkedSet.add("B");

for (String s : linkedSet) {
    System.out.println(s);  // C, A, B (insertion order)
}
```

## 3.3 TreeSet

```java
// Sorted order
Set<String> treeSet = new TreeSet<>();
treeSet.add("Banana");
treeSet.add("Apple");
treeSet.add("Cherry");

for (String s : treeSet) {
    System.out.println(s);  // Apple, Banana, Cherry (sorted)
}

// NavigableSet methods
TreeSet<Integer> nums = new TreeSet<>(List.of(1, 5, 10, 15, 20));
Integer lower = nums.lower(10);     // 5 (strictly less than)
Integer floor = nums.floor(10);     // 10 (less than or equal)
Integer higher = nums.higher(10);   // 15 (strictly greater)
Integer ceiling = nums.ceiling(10); // 10 (greater than or equal)
Integer first = nums.first();       // 1
Integer last = nums.last();         // 20

// Subsets
SortedSet<Integer> sub = nums.subSet(5, 15);  // [5, 10)
SortedSet<Integer> head = nums.headSet(10);   // [1, 5)
SortedSet<Integer> tail = nums.tailSet(10);   // [10, 15, 20]

// Custom comparator
Set<String> byLength = new TreeSet<>(Comparator.comparing(String::length));
```

## 3.4 Set Comparison

| Feature | HashSet | LinkedHashSet | TreeSet |
|---------|---------|---------------|---------|
| Order | None | Insertion | Sorted |
| Null | One allowed | One allowed | Not allowed |
| Performance | O(1) | O(1) | O(log n) |
| Implementation | HashMap | LinkedHashMap | Red-Black Tree |

---

# 4. Map Interface

## 4.1 HashMap

```java
Map<String, Integer> map = new HashMap<>();

// Add/Update
map.put("Alice", 25);          // Add entry
map.put("Alice", 26);          // Update existing
map.putIfAbsent("Bob", 30);    // Only if key absent
map.putAll(otherMap);          // Add all from another map

// Get
Integer age = map.get("Alice");              // null if not found
Integer ageDefault = map.getOrDefault("Unknown", 0);  // Default value

// Check
boolean hasKey = map.containsKey("Alice");
boolean hasValue = map.containsValue(25);
int size = map.size();
boolean empty = map.isEmpty();

// Remove
map.remove("Alice");           // Remove by key
map.remove("Alice", 25);       // Remove only if value matches

// Iteration
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
for (String key : map.keySet()) { }
for (Integer value : map.values()) { }
map.forEach((k, v) -> System.out.println(k + ": " + v));

// Compute operations (Java 8+)
map.compute("Alice", (k, v) -> v == null ? 1 : v + 1);
map.computeIfAbsent("New", k -> calculateValue(k));
map.computeIfPresent("Alice", (k, v) -> v + 1);
map.merge("Alice", 1, Integer::sum);  // Add 1 to existing, or set to 1

// Replace
map.replace("Alice", 30);              // Replace if exists
map.replace("Alice", 26, 30);          // Replace only if value matches
map.replaceAll((k, v) -> v + 10);      // Transform all values

// Views
Set<String> keys = map.keySet();
Collection<Integer> values = map.values();
Set<Map.Entry<String, Integer>> entries = map.entrySet();
```

## 4.2 LinkedHashMap

```java
// Maintains insertion order
Map<String, Integer> linked = new LinkedHashMap<>();
linked.put("C", 3);
linked.put("A", 1);
linked.put("B", 2);

for (String key : linked.keySet()) {
    System.out.println(key);  // C, A, B (insertion order)
}

// Access-order LinkedHashMap (for LRU cache)
Map<String, Integer> accessOrder = new LinkedHashMap<>(16, 0.75f, true);
```

## 4.3 TreeMap

```java
// Sorted by keys
Map<String, Integer> tree = new TreeMap<>();
tree.put("Banana", 2);
tree.put("Apple", 1);
tree.put("Cherry", 3);

for (String key : tree.keySet()) {
    System.out.println(key);  // Apple, Banana, Cherry (sorted)
}

// NavigableMap methods
TreeMap<Integer, String> nums = new TreeMap<>();
nums.put(1, "One");
nums.put(5, "Five");
nums.put(10, "Ten");

Map.Entry<Integer, Integer> lower = nums.lowerEntry(5);  // 1
Map.Entry<Integer, Integer> floor = nums.floorEntry(5);  // 5
Map.Entry<Integer, Integer> higher = nums.higherEntry(5);// 10
Map.Entry<Integer, Integer> ceiling = nums.ceilingEntry(5);// 5

Integer firstKey = nums.firstKey();
Integer lastKey = nums.lastKey();

// Submaps
SortedMap<Integer, String> sub = nums.subMap(1, 10);
SortedMap<Integer, String> head = nums.headMap(5);
SortedMap<Integer, String> tail = nums.tailMap(5);
```

## 4.4 Map Comparison

| Feature | HashMap | LinkedHashMap | TreeMap |
|---------|---------|---------------|---------|
| Order | None | Insertion | Key sorted |
| Null Keys | One | One | None |
| Null Values | Multiple | Multiple | Multiple |
| Performance | O(1) | O(1) | O(log n) |

---

# 5. Queue and Deque

## 5.1 Queue Interface

```java
Queue<String> queue = new LinkedList<>();

// Add elements
queue.add("First");     // Throws exception if full
queue.offer("Second");  // Returns false if full

// Examine head
String head = queue.element();  // Throws if empty
String peek = queue.peek();     // Returns null if empty

// Remove head
String removed = queue.remove();  // Throws if empty
String polled = queue.poll();     // Returns null if empty
```

## 5.2 PriorityQueue

```java
// Min-heap by default (smallest first)
Queue<Integer> minHeap = new PriorityQueue<>();
minHeap.add(5);
minHeap.add(1);
minHeap.add(3);
System.out.println(minHeap.poll());  // 1 (smallest)

// Max-heap
Queue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
maxHeap.add(5);
maxHeap.add(1);
maxHeap.add(3);
System.out.println(maxHeap.poll());  // 5 (largest)

// Custom comparator
Queue<String> byLength = new PriorityQueue<>(Comparator.comparing(String::length));
```

## 5.3 Deque (Double-Ended Queue)

```java
Deque<String> deque = new ArrayDeque<>();

// Add to both ends
deque.addFirst("First");       // Add to front
deque.addLast("Last");         // Add to end
deque.offerFirst("First");     // Add to front (returns boolean)
deque.offerLast("Last");       // Add to end (returns boolean)

// Examine both ends
String first = deque.getFirst();   // Throws if empty
String last = deque.getLast();     // Throws if empty
String peekF = deque.peekFirst();  // Null if empty
String peekL = deque.peekLast();   // Null if empty

// Remove from both ends
String removeF = deque.removeFirst();  // Throws if empty
String removeL = deque.removeLast();   // Throws if empty
String pollF = deque.pollFirst();      // Null if empty
String pollL = deque.pollLast();       // Null if empty

// Use as Stack
deque.push("Element");         // addFirst
String popped = deque.pop();   // removeFirst
String top = deque.peek();     // peekFirst

// Use as Queue
deque.offer("Element");        // addLast
String front = deque.poll();   // removeFirst
```

---

# 6. Iteration Methods

## 6.1 For-Each Loop

```java
List<String> list = List.of("A", "B", "C");
for (String s : list) {
    System.out.println(s);
}

Map<String, Integer> map = Map.of("A", 1, "B", 2);
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
```

## 6.2 forEach Method (Java 8+)

```java
list.forEach(System.out::println);
list.forEach(s -> System.out.println(s.toUpperCase()));

map.forEach((k, v) -> System.out.println(k + ": " + v));
```

## 6.3 Iterator

```java
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String s = iterator.next();
    if (s.equals("B")) {
        iterator.remove();  // Safe removal during iteration
    }
}

// ListIterator (bidirectional)
ListIterator<String> listIter = list.listIterator();
while (listIter.hasNext()) {
    String s = listIter.next();
    listIter.set(s.toUpperCase());  // Modify
    listIter.add("New");            // Insert
}
listIter.previous();  // Go backwards
```

---

# 7. Utility Classes

## 7.1 Collections Class

```java
List<Integer> list = new ArrayList<>(List.of(3, 1, 4, 1, 5));

// Sort
Collections.sort(list);
Collections.sort(list, Comparator.reverseOrder());

// Search (list must be sorted)
int index = Collections.binarySearch(list, 4);

// Shuffle
Collections.shuffle(list);

// Reverse
Collections.reverse(list);

// Min/Max
int min = Collections.min(list);
int max = Collections.max(list);

// Frequency
int count = Collections.frequency(list, 1);  // How many 1s?

// Replace
Collections.replaceAll(list, 1, 100);  // Replace all 1s with 100

// Rotate
Collections.rotate(list, 2);  // Rotate right by 2

// Swap
Collections.swap(list, 0, 2);  // Swap positions

// Fill
Collections.fill(list, 0);  // Fill with 0s

// Copy
Collections.copy(dest, source);  // dest must be large enough

// Unmodifiable views
List<String> unmodifiable = Collections.unmodifiableList(list);
Set<String> unmodifiableSet = Collections.unmodifiableSet(set);
Map<K, V> unmodifiableMap = Collections.unmodifiableMap(map);

// Synchronized views
List<String> syncList = Collections.synchronizedList(list);
Set<String> syncSet = Collections.synchronizedSet(set);
Map<K, V> syncMap = Collections.synchronizedMap(map);

// Empty collections
List<String> empty = Collections.emptyList();
Set<String> emptySet = Collections.emptySet();
Map<K, V> emptyMap = Collections.emptyMap();

// Singleton collections
List<String> single = Collections.singletonList("only");
Set<String> singleSet = Collections.singleton("only");
Map<K, V> singleMap = Collections.singletonMap("key", "value");
```

## 7.2 Factory Methods (Java 9+)

```java
// Immutable collections
List<String> list = List.of("A", "B", "C");
Set<String> set = Set.of("A", "B", "C");
Map<String, Integer> map = Map.of("A", 1, "B", 2, "C", 3);
Map<String, Integer> mapEntries = Map.ofEntries(
    Map.entry("A", 1),
    Map.entry("B", 2)
);

// Copy (Java 10+)
List<String> copy = List.copyOf(mutableList);
Set<String> setCopy = Set.copyOf(mutableSet);
Map<K, V> mapCopy = Map.copyOf(mutableMap);
```

---

# 8. Comparable vs Comparator

## 8.1 Comparable (Natural Ordering)

```java
class Person implements Comparable<Person> {
    String name;
    int age;
    
    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);  // Sort by name
    }
}

List<Person> people = new ArrayList<>();
Collections.sort(people);  // Uses compareTo
```

## 8.2 Comparator (Custom Ordering)

```java
// Lambda comparators
Comparator<Person> byAge = (p1, p2) -> p1.age - p2.age;
Comparator<Person> byName = (p1, p2) -> p1.name.compareTo(p2.name);

// Method reference comparators
Comparator<Person> byAge2 = Comparator.comparingInt(p -> p.age);
Comparator<Person> byName2 = Comparator.comparing(p -> p.name);

// Chaining
Comparator<Person> byNameThenAge = Comparator
    .comparing(Person::getName)
    .thenComparingInt(Person::getAge);

// Reverse
Comparator<Person> byAgeDesc = Comparator.comparingInt(Person::getAge).reversed();

// Null handling
Comparator<Person> nullsFirst = Comparator.nullsFirst(byName);
Comparator<Person> nullsLast = Comparator.nullsLast(byName);

// Use
Collections.sort(people, byAge);
people.sort(byName);
```

---

# 9. Sequenced Collections (Java 21)

```java
// New interfaces for ordered access
SequencedCollection<E>  
SequencedSet<E>
SequencedMap<K,V>

List<String> list = new ArrayList<>();
list.addFirst("First");      // Add at beginning
list.addLast("Last");        // Add at end
String first = list.getFirst();  // Get first
String last = list.getLast();    // Get last
list.removeFirst();          // Remove first
list.removeLast();           // Remove last
List<String> reversed = list.reversed();  // Reversed view

// Works with LinkedHashSet, LinkedHashMap too
SequencedMap<String, Integer> map = new LinkedHashMap<>();
map.putFirst("first", 1);
map.putLast("last", 99);
var firstEntry = map.firstEntry();
var lastEntry = map.lastEntry();
map.reversed();  // Reversed view
```

---

# Summary Table

| Collection | Ordered | Duplicates | Null | Thread-Safe |
|------------|---------|------------|------|-------------|
| ArrayList | Yes (index) | Yes | Yes | No |
| LinkedList | Yes (index) | Yes | Yes | No |
| HashSet | No | No | One | No |
| LinkedHashSet | Insertion | No | One | No |
| TreeSet | Sorted | No | No | No |
| HashMap | No | N/A | One key | No |
| LinkedHashMap | Insertion | N/A | One key | No |
| TreeMap | Sorted | N/A | No keys | No |
| PriorityQueue | Priority | Yes | No | No |
| ArrayDeque | Yes | Yes | No | No |
