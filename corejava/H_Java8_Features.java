/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  CORE JAVA - PART 8: JAVA 8 FEATURES
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Java 8 (2014) was a MAJOR release with these game-changers:
 * 
 *   1. Lambda Expressions     → Shorter code, functional style
 *   2. Functional Interfaces  → Single abstract method interfaces
 *   3. Stream API             → Process collections functionally
 *   4. Optional               → Handle null safely
 *   5. Default Methods        → Methods in interfaces
 *   6. Method References      → Even shorter lambdas
 *   7. New Date/Time API      → Better date handling
 * 
 */

package corejava;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.time.*;

public class H_Java8_Features {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  JAVA 8 FEATURES");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        lambdaExpressions();
        functionalInterfaces();
        methodReferences();
        streamAPI();
        optionalClass();
        defaultMethods();
        dateTimeAPI();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * LAMBDA EXPRESSIONS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like SHORTHAND writing ✍️
     * ────────────────────────────────
     *   Instead of writing a full letter, you write a quick note!
     *   
     *   Full letter (Anonymous class):
     *   new Runnable() { public void run() { System.out.println("Hi"); }}
     *   
     *   Quick note (Lambda):
     *   () -> System.out.println("Hi")
     */
    static void lambdaExpressions() {
        System.out.println("1️⃣ LAMBDA EXPRESSIONS");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Old way - Anonymous class
        System.out.println("A) Old Way (Anonymous Class):");
        Runnable oldWay = new Runnable() {
            @Override
            public void run() {
                System.out.println("   Hello from anonymous class!");
            }
        };
        oldWay.run();
        
        // New way - Lambda
        System.out.println("\nB) New Way (Lambda):");
        Runnable newWay = () -> System.out.println("   Hello from lambda!");
        newWay.run();
        
        // Lambda syntax variations
        System.out.println("\nC) Lambda Syntax:");
        System.out.println("   () -> expression              // No params");
        System.out.println("   x -> expression               // One param (no parentheses)");
        System.out.println("   (x, y) -> expression          // Multiple params");
        System.out.println("   (x, y) -> { return x + y; }   // Block with return");
        
        // Examples
        System.out.println("\nD) Examples:");
        
        // Comparator
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
        System.out.println("   Original: " + names);
        
        // Old way
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return a.compareTo(b);
            }
        });
        
        // Lambda way
        names = Arrays.asList("Charlie", "Alice", "Bob");
        Collections.sort(names, (a, b) -> a.compareTo(b));
        System.out.println("   Sorted: " + names);
        
        // forEach with lambda
        System.out.print("   forEach: ");
        names.forEach(name -> System.out.print(name + " "));
        System.out.println("\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * FUNCTIONAL INTERFACES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * Functional Interface = Interface with EXACTLY ONE abstract method
     * Lambda expressions work with functional interfaces!
     */
    static void functionalInterfaces() {
        System.out.println("2️⃣ FUNCTIONAL INTERFACES");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("Built-in Functional Interfaces (java.util.function):\n");
        
        // Predicate - test something, return boolean
        System.out.println("A) Predicate<T> - Test, return boolean:");
        Predicate<Integer> isEven = n -> n % 2 == 0;
        System.out.println("   isEven.test(4): " + isEven.test(4));
        System.out.println("   isEven.test(3): " + isEven.test(3));
        
        // Function - transform input to output
        System.out.println("\nB) Function<T, R> - Transform T to R:");
        Function<String, Integer> length = s -> s.length();
        System.out.println("   length.apply(\"Hello\"): " + length.apply("Hello"));
        
        // Consumer - accept input, no return
        System.out.println("\nC) Consumer<T> - Accept, no return:");
        Consumer<String> printer = s -> System.out.println("   " + s);
        printer.accept("Hello from Consumer!");
        
        // Supplier - no input, return value
        System.out.println("\nD) Supplier<T> - No input, return T:");
        Supplier<Double> random = () -> Math.random();
        System.out.println("   random.get(): " + random.get());
        
        // BiFunction - two inputs, one output
        System.out.println("\nE) BiFunction<T, U, R> - Two inputs, one output:");
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println("   add.apply(5, 3): " + add.apply(5, 3));
        
        // UnaryOperator - same type in and out
        System.out.println("\nF) UnaryOperator<T> - Same type in/out:");
        UnaryOperator<Integer> square = x -> x * x;
        System.out.println("   square.apply(5): " + square.apply(5));
        
        // Summary table
        System.out.println("\n📌 Summary:");
        System.out.println("   ┌─────────────────────┬─────────────────────┬─────────────────────┐");
        System.out.println("   │    Interface        │   Method            │   Purpose           │");
        System.out.println("   ├─────────────────────┼─────────────────────┼─────────────────────┤");
        System.out.println("   │ Predicate<T>        │ boolean test(T)     │ Test condition      │");
        System.out.println("   │ Function<T,R>       │ R apply(T)          │ Transform T → R     │");
        System.out.println("   │ Consumer<T>         │ void accept(T)      │ Use T, no return    │");
        System.out.println("   │ Supplier<T>         │ T get()             │ Produce T           │");
        System.out.println("   │ BiFunction<T,U,R>   │ R apply(T,U)        │ Two inputs          │");
        System.out.println("   │ UnaryOperator<T>    │ T apply(T)          │ Same type in/out    │");
        System.out.println("   └─────────────────────┴─────────────────────┴─────────────────────┘\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * METHOD REFERENCES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * Even shorter than lambda when calling existing methods!
     */
    static void methodReferences() {
        System.out.println("3️⃣ METHOD REFERENCES");
        System.out.println("─────────────────────────────────────────────\n");
        
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        
        // Lambda vs Method Reference
        System.out.println("A) Lambda vs Method Reference:");
        System.out.println("   Lambda:           name -> System.out.println(name)");
        System.out.println("   Method Reference: System.out::println");
        
        System.out.print("\n   Using method reference: ");
        names.forEach(System.out::print);
        System.out.println();
        
        // Types of method references
        System.out.println("\nB) Types of Method References:");
        
        // Static method reference
        System.out.println("   1. Static method: ClassName::staticMethod");
        Function<String, Integer> parse = Integer::parseInt;
        System.out.println("      Integer::parseInt → " + parse.apply("123"));
        
        // Instance method of particular object
        System.out.println("\n   2. Instance method (particular object): object::method");
        String prefix = "Hello, ";
        Function<String, String> greeter = prefix::concat;
        System.out.println("      prefix::concat → " + greeter.apply("World"));
        
        // Instance method of arbitrary object
        System.out.println("\n   3. Instance method (arbitrary object): ClassName::method");
        Function<String, String> upper = String::toUpperCase;
        System.out.println("      String::toUpperCase → " + upper.apply("hello"));
        
        // Constructor reference
        System.out.println("\n   4. Constructor: ClassName::new");
        Supplier<ArrayList<String>> listCreator = ArrayList::new;
        System.out.println("      ArrayList::new → " + listCreator.get());
        
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * STREAM API
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like an ASSEMBLY LINE in a factory 🏭
     * ──────────────────────────────────────────
     *   Data flows through different stations:
     *   Source → Filter → Transform → Collect
     */
    static void streamAPI() {
        System.out.println("4️⃣ STREAM API");
        System.out.println("─────────────────────────────────────────────\n");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Basic stream operations
        System.out.println("A) Basic Stream Operations:");
        System.out.println("   Source: " + numbers);
        
        // filter
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("   filter(even): " + evens);
        
        // map
        List<Integer> squared = numbers.stream()
            .map(n -> n * n)
            .collect(Collectors.toList());
        System.out.println("   map(square): " + squared);
        
        // reduce
        int sum = numbers.stream()
            .reduce(0, (a, b) -> a + b);
        System.out.println("   reduce(sum): " + sum);
        
        // Chaining operations
        System.out.println("\nB) Chaining (Pipeline):");
        int result = numbers.stream()
            .filter(n -> n % 2 == 0)    // Keep evens: 2,4,6,8,10
            .map(n -> n * 2)            // Double: 4,8,12,16,20
            .reduce(0, Integer::sum);   // Sum: 60
        System.out.println("   filter(even).map(*2).sum() = " + result);
        
        // Terminal operations
        System.out.println("\nC) Terminal Operations:");
        System.out.println("   count(): " + numbers.stream().count());
        System.out.println("   min(): " + numbers.stream().min(Integer::compare).get());
        System.out.println("   max(): " + numbers.stream().max(Integer::compare).get());
        System.out.println("   findFirst(): " + numbers.stream().findFirst().get());
        System.out.println("   anyMatch(>5): " + numbers.stream().anyMatch(n -> n > 5));
        System.out.println("   allMatch(>0): " + numbers.stream().allMatch(n -> n > 0));
        
        // Collectors
        System.out.println("\nD) Collectors:");
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Alice");
        
        // toList, toSet
        System.out.println("   toList(): " + names.stream().collect(Collectors.toList()));
        System.out.println("   toSet(): " + names.stream().collect(Collectors.toSet()));
        
        // joining
        String joined = names.stream().collect(Collectors.joining(", "));
        System.out.println("   joining(\", \"): " + joined);
        
        // groupingBy
        Map<Integer, List<String>> byLength = names.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("   groupingBy(length): " + byLength);
        
        // Parallel streams
        System.out.println("\nE) Parallel Streams:");
        System.out.println("   numbers.parallelStream()... // Uses multiple threads");
        long parallelSum = numbers.parallelStream()
            .mapToLong(n -> n)
            .sum();
        System.out.println("   Parallel sum: " + parallelSum);
        
        // Stream vs Collection
        System.out.println("\n📌 Stream Characteristics:");
        System.out.println("   • Streams are LAZY (process on terminal operation)");
        System.out.println("   • Streams can be used ONLY ONCE");
        System.out.println("   • Streams don't store data, they process it");
        System.out.println("   • Intermediate ops return stream, terminal ops return result\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * OPTIONAL CLASS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a GIFT BOX 🎁
     * ─────────────────────────
     *   The box might be empty or contain a gift
     *   You check before opening!
     */
    static void optionalClass() {
        System.out.println("5️⃣ OPTIONAL CLASS (Avoid NullPointerException)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Creating Optional
        System.out.println("A) Creating Optional:");
        Optional<String> present = Optional.of("Hello");        // Value present
        Optional<String> empty = Optional.empty();              // No value
        Optional<String> nullable = Optional.ofNullable(null);  // May be null
        
        System.out.println("   Optional.of(\"Hello\"): " + present);
        System.out.println("   Optional.empty(): " + empty);
        System.out.println("   Optional.ofNullable(null): " + nullable);
        
        // Checking and getting value
        System.out.println("\nB) Checking Value:");
        System.out.println("   isPresent(): " + present.isPresent());
        System.out.println("   isEmpty(): " + empty.isEmpty() + " (Java 11+)");
        
        // Getting value safely
        System.out.println("\nC) Getting Value Safely:");
        
        // get() - throws exception if empty!
        System.out.println("   get(): " + present.get() + " (risky if empty!)");
        
        // orElse - default value
        String value1 = empty.orElse("Default");
        System.out.println("   orElse(\"Default\"): " + value1);
        
        // orElseGet - lazy default
        String value2 = empty.orElseGet(() -> "Computed Default");
        System.out.println("   orElseGet(supplier): " + value2);
        
        // orElseThrow
        System.out.println("   orElseThrow(): Throws if empty");
        
        // ifPresent
        System.out.println("\nD) ifPresent:");
        present.ifPresent(v -> System.out.println("   Value is: " + v));
        
        // map and flatMap
        System.out.println("\nE) Transforming:");
        Optional<Integer> length = present.map(String::length);
        System.out.println("   map(String::length): " + length);
        
        // filter
        Optional<String> filtered = present.filter(s -> s.length() > 3);
        System.out.println("   filter(length > 3): " + filtered);
        
        // Real-world example
        System.out.println("\nF) Real-World Example:");
        System.out.println("   // Old way");
        System.out.println("   if (user != null && user.getAddress() != null) {");
        System.out.println("       return user.getAddress().getCity();");
        System.out.println("   }");
        System.out.println("\n   // Optional way");
        System.out.println("   return Optional.ofNullable(user)");
        System.out.println("       .map(User::getAddress)");
        System.out.println("       .map(Address::getCity)");
        System.out.println("       .orElse(\"Unknown\");");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * DEFAULT METHODS IN INTERFACES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void defaultMethods() {
        System.out.println("6️⃣ DEFAULT & STATIC METHODS IN INTERFACES");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("A) Default Methods:");
        System.out.println("   interface Vehicle {");
        System.out.println("       void start();  // Abstract");
        System.out.println("       ");
        System.out.println("       default void horn() {  // Has implementation!");
        System.out.println("           System.out.println(\"Beep!\");");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println("\n   • Classes get default implementation automatically");
        System.out.println("   • Can be overridden if needed");
        System.out.println("   • Added to support backward compatibility (e.g., forEach in List)");
        
        System.out.println("\nB) Static Methods in Interface:");
        System.out.println("   interface Calculator {");
        System.out.println("       static int add(int a, int b) {");
        System.out.println("           return a + b;");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println("   Calculator.add(5, 3);  // Called via interface name");
        
        System.out.println("\nC) Diamond Problem:");
        System.out.println("   If class implements 2 interfaces with same default method:");
        System.out.println("   → Must override and choose which to use");
        System.out.println("   → Or provide own implementation\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * NEW DATE/TIME API (java.time)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void dateTimeAPI() {
        System.out.println("7️⃣ NEW DATE/TIME API (java.time)");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("Old vs New:");
        System.out.println("   Old: java.util.Date, Calendar (mutable, confusing)");
        System.out.println("   New: java.time.* (immutable, clear)\n");
        
        // LocalDate
        System.out.println("A) LocalDate (date only):");
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(1990, 5, 15);
        System.out.println("   today: " + today);
        System.out.println("   birthday: " + birthday);
        System.out.println("   plusDays(7): " + today.plusDays(7));
        
        // LocalTime
        System.out.println("\nB) LocalTime (time only):");
        LocalTime now = LocalTime.now();
        LocalTime meeting = LocalTime.of(14, 30);
        System.out.println("   now: " + now);
        System.out.println("   meeting: " + meeting);
        
        // LocalDateTime
        System.out.println("\nC) LocalDateTime (date + time):");
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println("   now: " + dateTime);
        
        // Duration and Period
        System.out.println("\nD) Duration & Period:");
        Period period = Period.between(birthday, today);
        System.out.println("   Age: " + period.getYears() + " years");
        
        Duration duration = Duration.ofHours(2);
        System.out.println("   Duration: " + duration);
        
        // Formatting
        System.out.println("\nE) Formatting:");
        java.time.format.DateTimeFormatter formatter = 
            java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("   Formatted: " + today.format(formatter));
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Java 8 Features");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  📌 Lambda: (params) -> expression");
        System.out.println("  📌 Functional Interfaces: Predicate, Function, Consumer, Supplier");
        System.out.println("  📌 Method Reference: ClassName::methodName");
        System.out.println("  📌 Streams: filter, map, reduce, collect");
        System.out.println("  📌 Optional: Avoid NullPointerException");
        System.out.println("  📌 Default methods: Implementation in interfaces");
        System.out.println("  📌 java.time: LocalDate, LocalTime, LocalDateTime");
        System.out.println();
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"Java 8 introduced functional programming with lambdas and");
        System.out.println("      streams. Lambda is anonymous function syntax. Streams process");
        System.out.println("      collections functionally with lazy evaluation. Optional wraps");
        System.out.println("      nullable values safely. These features make code more concise.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q1: What is lambda expression?
 * A1: Anonymous function that can be passed as argument.
 *     Syntax: (parameters) -> expression
 *     Works with functional interfaces.
 * 
 * Q2: What is functional interface?
 * A2: Interface with exactly ONE abstract method.
 *     Can have default and static methods.
 *     @FunctionalInterface annotation (optional but recommended).
 * 
 * Q3: Difference between map() and flatMap()?
 * A3: map(): One-to-one transformation (returns Stream<T>)
 *     flatMap(): One-to-many, flattens result (returns Stream<T>)
 *     flatMap is used when mapper returns a stream.
 * 
 * Q4: What is Stream lazy evaluation?
 * A4: Intermediate operations (filter, map) don't execute immediately.
 *     They execute only when terminal operation (collect, forEach) is called.
 *     Allows optimization (e.g., short-circuit with findFirst).
 * 
 * Q5: Can we reuse a Stream?
 * A5: No! Streams can only be consumed once.
 *     After terminal operation, stream is closed.
 *     Create new stream if needed again.
 * 
 * Q6: Optional.of() vs Optional.ofNullable()?
 * A6: of(): Throws NullPointerException if value is null
 *     ofNullable(): Returns empty Optional if value is null
 */
