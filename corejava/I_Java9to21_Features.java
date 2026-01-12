/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  CORE JAVA - PART 9: JAVA 9 TO 21 FEATURES
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * JAVA VERSION HISTORY:
 * 
 *   Java 9 (2017)  → Modules, JShell, Private interface methods
 *   Java 10 (2018) → var keyword
 *   Java 11 (2018) → String methods, HTTP Client, Files methods (LTS)
 *   Java 12 (2019) → Switch expressions (preview)
 *   Java 13 (2019) → Text blocks (preview)
 *   Java 14 (2020) → Records (preview), Pattern matching instanceof
 *   Java 15 (2020) → Text blocks (standard), Sealed classes (preview)
 *   Java 16 (2021) → Records (standard), Pattern matching instanceof
 *   Java 17 (2021) → Sealed classes (standard) (LTS)
 *   Java 18 (2022) → Simple web server
 *   Java 19 (2022) → Virtual threads (preview)
 *   Java 20 (2023) → Scoped values (preview)
 *   Java 21 (2023) → Virtual threads (standard), Pattern matching (LTS)
 * 
 *   LTS = Long Term Support (8, 11, 17, 21)
 */

package corejava;

import java.util.*;
import java.net.http.*;
import java.net.URI;
import java.nio.file.*;

public class I_Java9to21_Features {

    public static void main(String[] args) throws Exception {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  JAVA 9 TO 21 NEW FEATURES");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        java9Features();
        java10Features();
        java11Features();
        java12to13Features();
        java14Features();
        java15to17Features();
        java21Features();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * JAVA 9 FEATURES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void java9Features() {
        System.out.println("1️⃣ JAVA 9 FEATURES");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Factory methods for collections
        System.out.println("A) Immutable Collection Factory Methods:");
        List<String> list = List.of("A", "B", "C");
        Set<String> set = Set.of("X", "Y", "Z");
        Map<String, Integer> map = Map.of("One", 1, "Two", 2);
        
        System.out.println("   List.of(\"A\",\"B\",\"C\"): " + list);
        System.out.println("   Set.of(\"X\",\"Y\",\"Z\"): " + set);
        System.out.println("   Map.of(\"One\",1,\"Two\",2): " + map);
        System.out.println("   These are IMMUTABLE! Can't modify.");
        
        // Private methods in interfaces
        System.out.println("\nB) Private Methods in Interfaces:");
        System.out.println("   interface MyInterface {");
        System.out.println("       default void publicMethod() {");
        System.out.println("           helperMethod();  // Call private method");
        System.out.println("       }");
        System.out.println("       private void helperMethod() {  // Java 9+");
        System.out.println("           // implementation");
        System.out.println("       }");
        System.out.println("   }");
        
        // Stream improvements
        System.out.println("\nC) Stream Improvements:");
        
        // takeWhile
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7);
        List<Integer> taken = nums.stream().takeWhile(n -> n < 5).toList();
        System.out.println("   takeWhile(n < 5): " + taken);
        
        // dropWhile
        List<Integer> dropped = nums.stream().dropWhile(n -> n < 5).toList();
        System.out.println("   dropWhile(n < 5): " + dropped);
        
        // ofNullable
        System.out.println("   Stream.ofNullable(null) = empty stream");
        
        // Optional improvements
        System.out.println("\nD) Optional Improvements:");
        Optional<String> opt = Optional.of("Hello");
        
        // ifPresentOrElse
        opt.ifPresentOrElse(
            v -> System.out.println("   ifPresentOrElse: " + v),
            () -> System.out.println("   ifPresentOrElse: Empty!")
        );
        
        // or
        Optional<String> result = Optional.<String>empty()
            .or(() -> Optional.of("Default"));
        System.out.println("   empty().or(() -> Optional.of(\"Default\")): " + result);
        
        // Modules
        System.out.println("\nE) Module System (JPMS):");
        System.out.println("   module-info.java defines module dependencies");
        System.out.println("   module myapp {");
        System.out.println("       requires java.sql;");
        System.out.println("       exports com.myapp.api;");
        System.out.println("   }");
        
        // JShell
        System.out.println("\nF) JShell (REPL):");
        System.out.println("   Interactive Java shell for quick testing");
        System.out.println("   $ jshell");
        System.out.println("   jshell> System.out.println(\"Hello\")");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * JAVA 10 FEATURES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void java10Features() {
        System.out.println("2️⃣ JAVA 10 FEATURES");
        System.out.println("─────────────────────────────────────────────\n");
        
        // var keyword
        System.out.println("A) var Keyword (Local Variable Type Inference):");
        var name = "John";           // String
        var age = 25;                // int
        var list = new ArrayList<String>();  // ArrayList<String>
        
        System.out.println("   var name = \"John\";     // Type: String");
        System.out.println("   var age = 25;           // Type: int");
        System.out.println("   var list = new ArrayList<String>();");
        
        System.out.println("\n   ✅ Can use var:");
        System.out.println("      • Local variables with initializer");
        System.out.println("      • For-each loop variables");
        System.out.println("      • For loop index variables");
        
        System.out.println("\n   ❌ Cannot use var:");
        System.out.println("      • Method parameters");
        System.out.println("      • Constructor parameters");
        System.out.println("      • Method return types");
        System.out.println("      • Class fields");
        System.out.println("      • var x; (no initializer)");
        System.out.println("      • var x = null; (can't infer type)");
        
        // copyOf methods
        System.out.println("\nB) Immutable Collection copyOf:");
        List<String> original = new ArrayList<>(List.of("A", "B"));
        List<String> copy = List.copyOf(original);
        System.out.println("   List.copyOf() creates immutable copy");
        
        // toUnmodifiable collectors
        System.out.println("\nC) toUnmodifiable Collectors:");
        System.out.println("   Collectors.toUnmodifiableList()");
        System.out.println("   Collectors.toUnmodifiableSet()");
        System.out.println("   Collectors.toUnmodifiableMap()");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * JAVA 11 FEATURES (LTS)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void java11Features() {
        System.out.println("3️⃣ JAVA 11 FEATURES (LTS)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // String methods
        System.out.println("A) New String Methods:");
        String text = "  Hello World  ";
        
        System.out.println("   isBlank(): " + "   ".isBlank());
        System.out.println("   strip(): \"" + text.strip() + "\"");
        System.out.println("   stripLeading(): \"" + text.stripLeading() + "\"");
        System.out.println("   stripTrailing(): \"" + text.stripTrailing() + "\"");
        System.out.println("   repeat(3): \"" + "Hi".repeat(3) + "\"");
        
        // lines()
        String multiline = "Line1\nLine2\nLine3";
        System.out.println("   lines(): " + multiline.lines().toList());
        
        // var in lambda
        System.out.println("\nB) var in Lambda Parameters:");
        System.out.println("   (var x, var y) -> x + y");
        System.out.println("   Allows annotations: (@NotNull var x) -> x.length()");
        
        // HTTP Client (standard)
        System.out.println("\nC) HTTP Client API (Standard):");
        System.out.println("   HttpClient client = HttpClient.newHttpClient();");
        System.out.println("   HttpRequest request = HttpRequest.newBuilder()");
        System.out.println("       .uri(URI.create(\"https://api.example.com\"))");
        System.out.println("       .GET()");
        System.out.println("       .build();");
        System.out.println("   HttpResponse<String> response = ");
        System.out.println("       client.send(request, BodyHandlers.ofString());");
        
        // Files methods
        System.out.println("\nD) Files Methods:");
        System.out.println("   Files.readString(path)    // Read file to String");
        System.out.println("   Files.writeString(path, content)  // Write String to file");
        
        // Optional isEmpty
        System.out.println("\nE) Optional.isEmpty():");
        Optional<String> empty = Optional.empty();
        System.out.println("   Optional.empty().isEmpty(): " + empty.isEmpty());
        
        // Running single-file programs
        System.out.println("\nF) Run Single-File Programs:");
        System.out.println("   $ java HelloWorld.java  // No compile step!");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * JAVA 12-13 FEATURES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void java12to13Features() {
        System.out.println("4️⃣ JAVA 12-13 FEATURES");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Switch expressions (Java 12 preview, 14 standard)
        System.out.println("A) Switch Expressions:");
        int day = 3;
        
        // Old switch
        System.out.println("   Old switch (statement):");
        System.out.println("   switch(day) { case 1: ... break; }");
        
        // New switch expression
        String dayName = switch (day) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6, 7 -> "Weekend";  // Multiple labels!
            default -> "Unknown";
        };
        System.out.println("\n   New switch (expression):");
        System.out.println("   String dayName = switch(day) {");
        System.out.println("       case 1 -> \"Monday\";");
        System.out.println("       case 6, 7 -> \"Weekend\";  // Multiple labels!");
        System.out.println("       default -> \"Unknown\";");
        System.out.println("   };");
        System.out.println("   Result: " + dayName);
        
        // yield keyword
        System.out.println("\nB) yield Keyword (for block in switch):");
        int value = switch (day) {
            case 1 -> 100;
            case 2 -> {
                int calculated = day * 50;
                yield calculated;  // Use yield for blocks!
            }
            default -> 0;
        };
        System.out.println("   yield returns value from block");
        
        // Text blocks (Java 13 preview, 15 standard)
        System.out.println("\nC) Text Blocks (Preview in 13, Standard in 15):");
        String json = """
                {
                    "name": "John",
                    "age": 25
                }
                """;
        System.out.println("   String json = \"\"\"");
        System.out.println("       {");
        System.out.println("           \"name\": \"John\"");
        System.out.println("       }");
        System.out.println("       \"\"\";");
        System.out.println("   No more \\n and + for multiline!");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * JAVA 14 FEATURES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void java14Features() {
        System.out.println("5️⃣ JAVA 14-16 FEATURES");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Pattern matching for instanceof
        System.out.println("A) Pattern Matching for instanceof:");
        Object obj = "Hello";
        
        // Old way
        System.out.println("   Old way:");
        System.out.println("   if (obj instanceof String) {");
        System.out.println("       String s = (String) obj;  // Manual cast");
        System.out.println("       System.out.println(s.length());");
        System.out.println("   }");
        
        // New way
        System.out.println("\n   New way (Java 16):");
        if (obj instanceof String s) {  // Auto cast to s!
            System.out.println("   if (obj instanceof String s) {");
            System.out.println("       // s is already String! No cast needed");
            System.out.println("       s.length() = " + s.length());
        }
        
        // Records (Java 14 preview, 16 standard)
        System.out.println("\nB) Records (Immutable Data Classes):");
        System.out.println("   // Old way - 50+ lines!");
        System.out.println("   class Person {");
        System.out.println("       private final String name;");
        System.out.println("       private final int age;");
        System.out.println("       // constructor, getters, equals, hashCode, toString...");
        System.out.println("   }");
        
        System.out.println("\n   // New way - 1 line!");
        System.out.println("   record Person(String name, int age) {}");
        
        // Demo record
        PersonRecord person = new PersonRecord("John", 25);
        System.out.println("\n   PersonRecord person = new PersonRecord(\"John\", 25);");
        System.out.println("   person.name(): " + person.name());
        System.out.println("   person.age(): " + person.age());
        System.out.println("   person: " + person);
        
        System.out.println("\n📌 Records automatically generate:");
        System.out.println("   • Constructor");
        System.out.println("   • Getters (name(), age() - no 'get' prefix)");
        System.out.println("   • equals() and hashCode()");
        System.out.println("   • toString()");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * JAVA 15-17 FEATURES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void java15to17Features() {
        System.out.println("6️⃣ JAVA 15-17 FEATURES (17 is LTS)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Sealed classes
        System.out.println("A) Sealed Classes (Control Inheritance):");
        System.out.println("   sealed class Shape permits Circle, Rectangle, Triangle {}");
        System.out.println("   ");
        System.out.println("   final class Circle extends Shape {}     // Can't extend further");
        System.out.println("   non-sealed class Rectangle extends Shape {} // Open");
        System.out.println("   sealed class Triangle extends Shape permits RightTriangle {}");
        System.out.println("\n   Only permitted classes can extend sealed class!");
        
        // Helpful NullPointerException
        System.out.println("\nB) Helpful NullPointerException:");
        System.out.println("   Old: NullPointerException");
        System.out.println("   New: Cannot invoke \"String.length()\" because \"name\" is null");
        System.out.println("   Shows EXACTLY which variable was null!");
        
        // Pattern matching in switch (preview in 17)
        System.out.println("\nC) Pattern Matching in Switch (Preview):");
        System.out.println("   Object obj = 42;");
        System.out.println("   String result = switch(obj) {");
        System.out.println("       case Integer i -> \"Integer: \" + i;");
        System.out.println("       case String s  -> \"String: \" + s;");
        System.out.println("       case null      -> \"Null!\";");
        System.out.println("       default        -> \"Unknown\";");
        System.out.println("   };");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * JAVA 21 FEATURES (LTS)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void java21Features() {
        System.out.println("7️⃣ JAVA 21 FEATURES (LTS)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Virtual Threads
        System.out.println("A) Virtual Threads (Project Loom):");
        System.out.println("   // Old: Platform threads (OS threads, expensive)");
        System.out.println("   Thread.ofPlatform().start(() -> {...});");
        System.out.println();
        System.out.println("   // New: Virtual threads (lightweight, millions possible!)");
        System.out.println("   Thread.ofVirtual().start(() -> {...});");
        System.out.println("   // or");
        System.out.println("   Thread.startVirtualThread(() -> {...});");
        System.out.println();
        System.out.println("   // ExecutorService with virtual threads");
        System.out.println("   try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {");
        System.out.println("       executor.submit(() -> {...});");
        System.out.println("   }");
        System.out.println("\n   🚀 Can create MILLIONS of virtual threads!");
        System.out.println("   Perfect for I/O-bound tasks (HTTP, DB calls)");
        
        // Record patterns
        System.out.println("\nB) Record Patterns:");
        System.out.println("   record Point(int x, int y) {}");
        System.out.println("   ");
        System.out.println("   Object obj = new Point(3, 4);");
        System.out.println("   if (obj instanceof Point(int x, int y)) {");
        System.out.println("       // x and y extracted directly!");
        System.out.println("   }");
        
        // Pattern matching for switch (finalized)
        System.out.println("\nC) Pattern Matching for Switch (Final):");
        System.out.println("   String describe(Object obj) {");
        System.out.println("       return switch(obj) {");
        System.out.println("           case null -> \"null\";");
        System.out.println("           case String s -> \"String: \" + s;");
        System.out.println("           case Integer i when i > 0 -> \"Positive int\";");
        System.out.println("           case Integer i -> \"Non-positive int\";");
        System.out.println("           default -> \"Something else\";");
        System.out.println("       };");
        System.out.println("   }");
        
        // Sequenced Collections
        System.out.println("\nD) Sequenced Collections:");
        System.out.println("   interface SequencedCollection<E> extends Collection<E> {");
        System.out.println("       E getFirst();");
        System.out.println("       E getLast();");
        System.out.println("       void addFirst(E e);");
        System.out.println("       void addLast(E e);");
        System.out.println("       SequencedCollection<E> reversed();");
        System.out.println("   }");
        System.out.println("   ArrayList, LinkedList now implement this!");
        
        // String templates (preview)
        System.out.println("\nE) String Templates (Preview):");
        System.out.println("   String name = \"John\";");
        System.out.println("   String msg = STR.\"Hello \\{name}!\";  // Hello John!");
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Java 9-21 Features");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  📌 Java 9:  Modules, List/Set/Map.of(), private interface methods");
        System.out.println("  📌 Java 10: var keyword");
        System.out.println("  📌 Java 11: String methods, HTTP Client, Files.readString (LTS)");
        System.out.println("  📌 Java 14: Switch expressions, Records (preview)");
        System.out.println("  📌 Java 15: Text blocks");
        System.out.println("  📌 Java 16: Pattern matching instanceof, Records (standard)");
        System.out.println("  📌 Java 17: Sealed classes (LTS)");
        System.out.println("  📌 Java 21: Virtual threads, Record patterns (LTS)");
        System.out.println();
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"Key features: var for local type inference (10), Records for");
        System.out.println("      immutable data (16), Sealed classes to control inheritance (17),");
        System.out.println("      Virtual threads for scalable concurrency (21). LTS versions");
        System.out.println("      are 8, 11, 17, 21 - companies usually use these.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

// Record example (Java 16+)
record PersonRecord(String name, int age) {}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q1: What are Records?
 * A1: Immutable data carriers with auto-generated constructor, getters,
 *     equals, hashCode, toString. Declared as: record Person(String name, int age) {}
 * 
 * Q2: What are Sealed classes?
 * A2: Classes that control which classes can extend them.
 *     sealed class Shape permits Circle, Rectangle {}
 *     Only Circle and Rectangle can extend Shape.
 * 
 * Q3: What are Virtual Threads?
 * A3: Lightweight threads managed by JVM, not OS. Can create millions
 *     without performance issues. Perfect for I/O-bound operations.
 *     Thread.startVirtualThread(() -> {...});
 * 
 * Q4: var keyword limitations?
 * A4: - Only for local variables with initializers
 *     - Can't use for method parameters, return types, fields
 *     - Can't use with null or without initializer
 * 
 * Q5: Difference between List.of() and Arrays.asList()?
 * A5: List.of() returns truly immutable list (can't add/remove/set)
 *     Arrays.asList() returns fixed-size but modifiable (can set, not add)
 * 
 * Q6: Which Java version should companies use?
 * A6: LTS versions: 8, 11, 17, or 21 (latest LTS)
 *     These have long-term support (years of updates)
 */
