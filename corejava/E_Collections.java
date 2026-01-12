/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  CORE JAVA - PART 5: COLLECTIONS FRAMEWORK
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME 📖
 * ─────────────
 * Think of Collections like different CONTAINERS:
 * 
 *   LIST   → Shopping List 📝 (ordered, allows duplicates)
 *   SET    → Unique Stamps Collection 📮 (no duplicates)
 *   MAP    → Dictionary 📖 (word → meaning, key → value)
 *   QUEUE  → Queue at Bank 🏦 (first come, first serve)
 * 
 * 
 * HIERARCHY:
 * ──────────
 *                     Iterable
 *                        │
 *                    Collection
 *                        │
 *          ┌─────────────┼─────────────┐
 *          │             │             │
 *         List          Set          Queue
 *          │             │             │
 *    ┌─────┴─────┐  ┌────┴────┐   ┌────┴────┐
 * ArrayList  LinkedList  HashSet  TreeSet  PriorityQueue
 * 
 *                       Map (separate)
 *                        │
 *              ┌─────────┼─────────┐
 *           HashMap   TreeMap  LinkedHashMap
 */

package corejava;

import java.util.*;

public class E_Collections {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  JAVA COLLECTIONS FRAMEWORK");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        listDemo();
        setDemo();
        mapDemo();
        queueDemo();
        comparisons();
        iterating();
        collectionsUtility();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * LIST - Ordered, allows duplicates
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a SHOPPING LIST 📝
     * ─────────────────────────────
     *   - Items stay in order you added them
     *   - Can have same item multiple times (2x milk)
     *   - Can access by position (item #3)
     */
    static void listDemo() {
        System.out.println("1️⃣ LIST (Ordered, Duplicates OK)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // ArrayList - Dynamic array, fast random access
        System.out.println("A) ArrayList (Dynamic Array):");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Apple");
        arrayList.add("Banana");
        arrayList.add("Apple");  // Duplicate OK!
        arrayList.add(1, "Orange");  // Insert at index 1
        
        System.out.println("   " + arrayList);
        System.out.println("   get(2): " + arrayList.get(2));
        System.out.println("   contains(\"Apple\"): " + arrayList.contains("Apple"));
        System.out.println("   indexOf(\"Apple\"): " + arrayList.indexOf("Apple"));
        
        arrayList.set(0, "Grape");  // Replace
        System.out.println("   After set(0, \"Grape\"): " + arrayList);
        
        arrayList.remove("Banana");  // Remove by value
        System.out.println("   After remove(\"Banana\"): " + arrayList);
        
        // LinkedList - Doubly linked, fast insert/delete
        System.out.println("\nB) LinkedList (Doubly Linked):");
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("A");
        linkedList.add("B");
        linkedList.addFirst("First");  // Add at beginning
        linkedList.addLast("Last");    // Add at end
        
        System.out.println("   " + linkedList);
        System.out.println("   getFirst(): " + linkedList.getFirst());
        System.out.println("   getLast(): " + linkedList.getLast());
        
        // ArrayList vs LinkedList
        System.out.println("\nC) ArrayList vs LinkedList:");
        System.out.println("   ┌────────────────────┬──────────────┬──────────────┐");
        System.out.println("   │    Operation       │  ArrayList   │  LinkedList  │");
        System.out.println("   ├────────────────────┼──────────────┼──────────────┤");
        System.out.println("   │  get(index)        │    O(1) ✅   │    O(n)      │");
        System.out.println("   │  add(end)          │    O(1)      │    O(1)      │");
        System.out.println("   │  add(middle)       │    O(n)      │    O(1) ✅   │");
        System.out.println("   │  remove(middle)    │    O(n)      │    O(1) ✅   │");
        System.out.println("   │  Memory            │    Less      │    More      │");
        System.out.println("   └────────────────────┴──────────────┴──────────────┘");
        System.out.println("   Use ArrayList for most cases (90%+)\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * SET - No duplicates
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a STAMP COLLECTION 📮
     * ─────────────────────────────────
     *   - Each stamp is unique
     *   - Can't have same stamp twice
     *   - Different sets organize differently
     */
    static void setDemo() {
        System.out.println("2️⃣ SET (No Duplicates)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // HashSet - Unordered, fastest
        System.out.println("A) HashSet (Unordered, Fastest):");
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("Banana");
        hashSet.add("Apple");
        hashSet.add("Cherry");
        hashSet.add("Apple");  // Duplicate - ignored!
        
        System.out.println("   Added: Banana, Apple, Cherry, Apple");
        System.out.println("   Result: " + hashSet);
        System.out.println("   Size: " + hashSet.size() + " (Apple added only once!)");
        
        // LinkedHashSet - Maintains insertion order
        System.out.println("\nB) LinkedHashSet (Insertion Order):");
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("Banana");
        linkedHashSet.add("Apple");
        linkedHashSet.add("Cherry");
        
        System.out.println("   Added: Banana, Apple, Cherry");
        System.out.println("   Result: " + linkedHashSet + " (same order!)");
        
        // TreeSet - Sorted order
        System.out.println("\nC) TreeSet (Sorted Order):");
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("Banana");
        treeSet.add("Apple");
        treeSet.add("Cherry");
        
        System.out.println("   Added: Banana, Apple, Cherry");
        System.out.println("   Result: " + treeSet + " (alphabetically sorted!)");
        System.out.println("   first(): " + treeSet.first());
        System.out.println("   last(): " + treeSet.last());
        
        // Comparison
        System.out.println("\nD) HashSet vs LinkedHashSet vs TreeSet:");
        System.out.println("   ┌────────────────────┬──────────────┬────────────────┬──────────────┐");
        System.out.println("   │                    │   HashSet    │ LinkedHashSet  │   TreeSet    │");
        System.out.println("   ├────────────────────┼──────────────┼────────────────┼──────────────┤");
        System.out.println("   │  Order             │   None       │   Insertion    │   Sorted     │");
        System.out.println("   │  add/remove        │   O(1) ✅    │   O(1)         │   O(log n)   │");
        System.out.println("   │  null              │   1 allowed  │   1 allowed    │   ❌ Not     │");
        System.out.println("   │  Use when          │   Fast lookup│   Need order   │   Need sorted│");
        System.out.println("   └────────────────────┴──────────────┴────────────────┴──────────────┘\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * MAP - Key-Value pairs
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a DICTIONARY 📖
     * ──────────────────────────
     *   Word (Key) → Meaning (Value)
     *   "Apple" → "A fruit"
     *   Each key is unique, but values can repeat
     */
    static void mapDemo() {
        System.out.println("3️⃣ MAP (Key-Value Pairs)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // HashMap - Most used, unordered
        System.out.println("A) HashMap (Unordered, Most Used):");
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("Alice", 90);
        hashMap.put("Bob", 85);
        hashMap.put("Charlie", 95);
        hashMap.put("Alice", 92);  // Updates existing key!
        
        System.out.println("   " + hashMap);
        System.out.println("   get(\"Alice\"): " + hashMap.get("Alice"));
        System.out.println("   containsKey(\"Bob\"): " + hashMap.containsKey("Bob"));
        System.out.println("   containsValue(85): " + hashMap.containsValue(85));
        System.out.println("   keySet(): " + hashMap.keySet());
        System.out.println("   values(): " + hashMap.values());
        
        // getOrDefault
        System.out.println("   getOrDefault(\"Dave\", 0): " + hashMap.getOrDefault("Dave", 0));
        
        // putIfAbsent
        hashMap.putIfAbsent("Alice", 100);  // Won't update, key exists
        hashMap.putIfAbsent("Dave", 88);    // Will add, key doesn't exist
        System.out.println("   After putIfAbsent: " + hashMap);
        
        // LinkedHashMap - Maintains insertion order
        System.out.println("\nB) LinkedHashMap (Insertion Order):");
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("First", 1);
        linkedHashMap.put("Second", 2);
        linkedHashMap.put("Third", 3);
        System.out.println("   " + linkedHashMap + " (maintains order)");
        
        // TreeMap - Sorted by keys
        System.out.println("\nC) TreeMap (Sorted by Keys):");
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("Zebra", 3);
        treeMap.put("Apple", 1);
        treeMap.put("Mango", 2);
        System.out.println("   " + treeMap + " (sorted by keys)");
        System.out.println("   firstKey(): " + treeMap.firstKey());
        System.out.println("   lastKey(): " + treeMap.lastKey());
        
        // Comparison
        System.out.println("\nD) HashMap vs LinkedHashMap vs TreeMap:");
        System.out.println("   ┌────────────────────┬──────────────┬────────────────┬──────────────┐");
        System.out.println("   │                    │   HashMap    │ LinkedHashMap  │   TreeMap    │");
        System.out.println("   ├────────────────────┼──────────────┼────────────────┼──────────────┤");
        System.out.println("   │  Order             │   None       │   Insertion    │   Sorted     │");
        System.out.println("   │  get/put           │   O(1) ✅    │   O(1)         │   O(log n)   │");
        System.out.println("   │  null key          │   1 allowed  │   1 allowed    │   ❌ Not     │");
        System.out.println("   │  Use when          │   Fast lookup│   Need order   │   Need sorted│");
        System.out.println("   └────────────────────┴──────────────┴────────────────┴──────────────┘\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * QUEUE - FIFO (First In, First Out)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a QUEUE at Bank 🏦
     * ─────────────────────────────
     *   First person in line gets served first
     *   New people join at the end
     */
    static void queueDemo() {
        System.out.println("4️⃣ QUEUE (First In, First Out)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // LinkedList as Queue
        System.out.println("A) Queue (LinkedList implementation):");
        Queue<String> queue = new LinkedList<>();
        queue.offer("First");   // Add to end
        queue.offer("Second");
        queue.offer("Third");
        
        System.out.println("   Queue: " + queue);
        System.out.println("   peek(): " + queue.peek() + " (view front, don't remove)");
        System.out.println("   poll(): " + queue.poll() + " (remove from front)");
        System.out.println("   After poll(): " + queue);
        
        // PriorityQueue - Elements ordered by priority
        System.out.println("\nB) PriorityQueue (By Priority):");
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(30);
        pq.offer(10);
        pq.offer(20);
        
        System.out.println("   Added: 30, 10, 20");
        System.out.println("   PriorityQueue: " + pq);
        System.out.print("   Polling: ");
        while (!pq.isEmpty()) {
            System.out.print(pq.poll() + " ");  // Comes out in sorted order!
        }
        System.out.println("(smallest first)");
        
        // Deque - Double-ended queue
        System.out.println("\nC) Deque (Double-Ended Queue):");
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("First");
        deque.addLast("Last");
        deque.addFirst("New First");
        
        System.out.println("   " + deque);
        System.out.println("   Can add/remove from BOTH ends!");
        
        // Stack using Deque (modern approach)
        System.out.println("\nD) Stack using Deque (LIFO - Last In First Out):");
        Deque<String> stack = new ArrayDeque<>();
        stack.push("Bottom");
        stack.push("Middle");
        stack.push("Top");
        
        System.out.println("   Stack: " + stack);
        System.out.println("   pop(): " + stack.pop());
        System.out.println("   After pop: " + stack);
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * COMPARISONS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void comparisons() {
        System.out.println("5️⃣ WHEN TO USE WHAT?");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   Need                           → Use");
        System.out.println("   ────────────────────────────────────────────────────");
        System.out.println("   Ordered list, fast access      → ArrayList");
        System.out.println("   Frequent insert/delete middle  → LinkedList");
        System.out.println("   Unique elements, fast lookup   → HashSet");
        System.out.println("   Unique, maintain order         → LinkedHashSet");
        System.out.println("   Unique, sorted                 → TreeSet");
        System.out.println("   Key-value, fast lookup         → HashMap");
        System.out.println("   Key-value, maintain order      → LinkedHashMap");
        System.out.println("   Key-value, sorted keys         → TreeMap");
        System.out.println("   FIFO processing                → Queue/LinkedList");
        System.out.println("   Priority processing            → PriorityQueue");
        System.out.println("   LIFO (Stack)                   → Deque/ArrayDeque");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * ITERATING COLLECTIONS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void iterating() {
        System.out.println("6️⃣ WAYS TO ITERATE");
        System.out.println("─────────────────────────────────────────────\n");
        
        List<String> list = Arrays.asList("A", "B", "C");
        
        // For loop
        System.out.print("   for loop: ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();
        
        // For-each
        System.out.print("   for-each: ");
        for (String item : list) {
            System.out.print(item + " ");
        }
        System.out.println();
        
        // Iterator
        System.out.print("   Iterator: ");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();
        
        // forEach with lambda (Java 8+)
        System.out.print("   forEach lambda: ");
        list.forEach(item -> System.out.print(item + " "));
        System.out.println();
        
        // Stream (Java 8+)
        System.out.print("   Stream: ");
        list.stream().forEach(item -> System.out.print(item + " "));
        System.out.println();
        
        // Iterating Map
        System.out.println("\n   Iterating Map:");
        Map<String, Integer> map = Map.of("A", 1, "B", 2, "C", 3);
        
        System.out.print("   entrySet: ");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.print(entry.getKey() + "=" + entry.getValue() + " ");
        }
        System.out.println();
        
        System.out.print("   forEach: ");
        map.forEach((k, v) -> System.out.print(k + "=" + v + " "));
        System.out.println("\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * COLLECTIONS UTILITY CLASS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void collectionsUtility() {
        System.out.println("7️⃣ COLLECTIONS UTILITY CLASS");
        System.out.println("─────────────────────────────────────────────\n");
        
        List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6));
        
        System.out.println("   Original: " + numbers);
        
        Collections.sort(numbers);
        System.out.println("   sort(): " + numbers);
        
        Collections.reverse(numbers);
        System.out.println("   reverse(): " + numbers);
        
        Collections.shuffle(numbers);
        System.out.println("   shuffle(): " + numbers);
        
        Collections.sort(numbers);
        System.out.println("   max(): " + Collections.max(numbers));
        System.out.println("   min(): " + Collections.min(numbers));
        System.out.println("   frequency(1): " + Collections.frequency(numbers, 1));
        
        // binarySearch (must be sorted!)
        int index = Collections.binarySearch(numbers, 4);
        System.out.println("   binarySearch(4): index " + index);
        
        // Immutable collections
        System.out.println("\n   Creating Immutable Collections:");
        List<String> immutableList = List.of("A", "B", "C");
        Set<String> immutableSet = Set.of("X", "Y", "Z");
        Map<String, Integer> immutableMap = Map.of("One", 1, "Two", 2);
        
        System.out.println("   List.of(): " + immutableList);
        System.out.println("   Set.of(): " + immutableSet);
        System.out.println("   Map.of(): " + immutableMap);
        System.out.println("   These CANNOT be modified! (Java 9+)");
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Collections");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  📌 LIST: Ordered, duplicates OK → ArrayList (99%), LinkedList");
        System.out.println("  📌 SET: No duplicates → HashSet (fast), TreeSet (sorted)");
        System.out.println("  📌 MAP: Key-Value → HashMap (fast), TreeMap (sorted)");
        System.out.println("  📌 QUEUE: FIFO → LinkedList, PriorityQueue");
        System.out.println();
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"ArrayList uses dynamic array, O(1) access. LinkedList is");
        System.out.println("      doubly-linked, O(1) insert/delete. HashSet/HashMap use");
        System.out.println("      hashing for O(1) operations. TreeSet/TreeMap are sorted");
        System.out.println("      using Red-Black tree, O(log n) operations.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q1: ArrayList vs LinkedList?
 * A1: ArrayList: Dynamic array, O(1) get, O(n) insert middle, less memory
 *     LinkedList: Doubly linked, O(n) get, O(1) insert middle, more memory
 *     Use ArrayList 90% of the time!
 * 
 * Q2: HashSet vs TreeSet?
 * A2: HashSet: O(1) operations, unordered, allows 1 null
 *     TreeSet: O(log n) operations, sorted, no null
 * 
 * Q3: HashMap vs Hashtable?
 * A3: HashMap: Not synchronized, allows 1 null key, faster
 *     Hashtable: Synchronized (thread-safe), no null, legacy class
 *     Use ConcurrentHashMap for thread-safe operations!
 * 
 * Q4: How HashMap works internally?
 * A4: Uses array of buckets. hashCode() determines bucket index.
 *     Collisions handled by linked list (→ tree if > 8 elements).
 *     Load factor 0.75 triggers resize.
 * 
 * Q5: What is fail-fast iterator?
 * A5: If collection is modified while iterating (except via iterator.remove()),
 *     throws ConcurrentModificationException. ArrayList, HashMap are fail-fast.
 * 
 * Q6: How to make collection thread-safe?
 * A6: 1. Collections.synchronizedList/Set/Map()
 *     2. ConcurrentHashMap, CopyOnWriteArrayList
 *     3. Manual synchronization
 */
