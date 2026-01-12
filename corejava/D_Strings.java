/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  CORE JAVA - PART 4: STRINGS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME 📖
 * ─────────────
 * Think of Strings like WRITTEN TEXT:
 * 
 *   String       → Written in STONE 🪨 (can't change)
 *   StringBuilder→ Written on WHITEBOARD 📝 (can change easily, single person)
 *   StringBuffer → Written on SHARED WHITEBOARD (can change, multiple people safely)
 * 
 * 
 * KEY CONCEPT: STRING IMMUTABILITY
 * ─────────────────────────────────
 *   String s = "Hello";
 *   s = s + " World";
 *   
 *   This creates a NEW string, doesn't modify original!
 *   Original "Hello" still exists in memory (until garbage collected)
 * 
 */

package corejava;

public class D_Strings {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  JAVA STRINGS");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        stringBasics();
        stringPool();
        stringMethods();
        stringBuilderDemo();
        stringBufferDemo();
        comparison();
        stringFormatting();
        textBlocks();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * STRING BASICS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void stringBasics() {
        System.out.println("1️⃣ STRING BASICS");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Two ways to create String
        String s1 = "Hello";              // String literal (uses pool)
        String s2 = new String("Hello");  // New object (doesn't use pool)
        
        System.out.println("Creating Strings:");
        System.out.println("   String s1 = \"Hello\";           // Literal (uses pool)");
        System.out.println("   String s2 = new String(\"Hello\"); // New object");
        
        // String is IMMUTABLE
        System.out.println("\nImmutability Demo:");
        String original = "Hello";
        String modified = original.concat(" World");
        
        System.out.println("   original = \"Hello\"");
        System.out.println("   modified = original.concat(\" World\")");
        System.out.println("   original is still: \"" + original + "\"");
        System.out.println("   modified is: \"" + modified + "\"");
        System.out.println("   Original didn't change! New string created.");
        
        // Why immutable?
        System.out.println("\n📌 Why String is Immutable?");
        System.out.println("   1. String Pool - share same objects");
        System.out.println("   2. Security - can't change passwords once set");
        System.out.println("   3. Thread-safe - no synchronization needed");
        System.out.println("   4. Caching - hashCode can be cached\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * STRING POOL (String Constant Pool)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Think of a LIBRARY 📚
     * ───────────────────────────
     *   Library has ONE copy of each book (String Pool)
     *   Everyone borrows the SAME copy (same reference)
     *   
     *   String s1 = "Java";  → Get book from library
     *   String s2 = "Java";  → Same book! (same reference)
     *   String s3 = new String("Java"); → Buy NEW copy (different reference)
     */
    static void stringPool() {
        System.out.println("2️⃣ STRING POOL");
        System.out.println("─────────────────────────────────────────────\n");
        
        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");
        String s4 = new String("Hello").intern();  // Force into pool
        
        System.out.println("String Pool Visualization:");
        System.out.println("   ┌─────────────────────────────────────────┐");
        System.out.println("   │           STRING POOL (Heap)            │");
        System.out.println("   │         ┌───────────────┐               │");
        System.out.println("   │    s1 ──┤   \"Hello\"     │←── s2         │");
        System.out.println("   │         └───────────────┘               │");
        System.out.println("   └─────────────────────────────────────────┘");
        System.out.println("   ┌─────────────────────────────────────────┐");
        System.out.println("   │              HEAP                       │");
        System.out.println("   │         ┌───────────────┐               │");
        System.out.println("   │    s3 ──┤   \"Hello\"     │ (separate)    │");
        System.out.println("   │         └───────────────┘               │");
        System.out.println("   └─────────────────────────────────────────┘");
        
        System.out.println("\nComparison:");
        System.out.println("   s1 == s2: " + (s1 == s2) + " (same pool reference)");
        System.out.println("   s1 == s3: " + (s1 == s3) + " (different objects)");
        System.out.println("   s1.equals(s3): " + s1.equals(s3) + " (same content)");
        System.out.println("   s1 == s4: " + (s1 == s4) + " (intern() puts in pool)");
        
        System.out.println("\n📌 Key Points:");
        System.out.println("   • Literals go to pool automatically");
        System.out.println("   • new String() creates in heap (not pool)");
        System.out.println("   • intern() moves string to pool");
        System.out.println("   • == checks reference, equals() checks content\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * STRING METHODS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void stringMethods() {
        System.out.println("3️⃣ IMPORTANT STRING METHODS");
        System.out.println("─────────────────────────────────────────────\n");
        
        String s = "  Hello World  ";
        
        // Length and Character access
        System.out.println("String s = \"  Hello World  \"");
        System.out.println("\nA) Length & Access:");
        System.out.println("   length(): " + s.length());
        System.out.println("   charAt(2): '" + s.charAt(2) + "'");
        System.out.println("   isEmpty(): " + s.isEmpty());
        System.out.println("   isBlank(): " + s.isBlank() + " (Java 11+)");
        
        // Search
        System.out.println("\nB) Search:");
        System.out.println("   indexOf('o'): " + s.indexOf('o'));
        System.out.println("   lastIndexOf('o'): " + s.lastIndexOf('o'));
        System.out.println("   contains(\"World\"): " + s.contains("World"));
        System.out.println("   startsWith(\"  He\"): " + s.startsWith("  He"));
        System.out.println("   endsWith(\"  \"): " + s.endsWith("  "));
        
        // Extraction
        System.out.println("\nC) Extraction:");
        System.out.println("   substring(2, 7): \"" + s.substring(2, 7) + "\"");
        System.out.println("   trim(): \"" + s.trim() + "\"");
        System.out.println("   strip(): \"" + s.strip() + "\" (Java 11+, handles Unicode)");
        
        // Transformation
        System.out.println("\nD) Transformation:");
        System.out.println("   toUpperCase(): \"" + s.toUpperCase() + "\"");
        System.out.println("   toLowerCase(): \"" + s.toLowerCase() + "\"");
        System.out.println("   replace('l','L'): \"" + s.replace('l', 'L') + "\"");
        System.out.println("   replaceAll(\"\\\\s+\",\"-\"): \"" + s.replaceAll("\\s+", "-") + "\"");
        
        // Split and Join
        System.out.println("\nE) Split & Join:");
        String csv = "apple,banana,cherry";
        String[] fruits = csv.split(",");
        System.out.println("   \"apple,banana,cherry\".split(\",\"): " + java.util.Arrays.toString(fruits));
        System.out.println("   String.join(\"-\", fruits): \"" + String.join("-", fruits) + "\"");
        
        // Comparison
        System.out.println("\nF) Comparison:");
        System.out.println("   \"abc\".equals(\"ABC\"): " + "abc".equals("ABC"));
        System.out.println("   \"abc\".equalsIgnoreCase(\"ABC\"): " + "abc".equalsIgnoreCase("ABC"));
        System.out.println("   \"abc\".compareTo(\"abd\"): " + "abc".compareTo("abd") + " (negative = less)");
        
        // Java 11+ Methods
        System.out.println("\nG) Java 11+ Methods:");
        System.out.println("   \"  \".isBlank(): " + "  ".isBlank());
        System.out.println("   \"Hi\".repeat(3): \"" + "Hi".repeat(3) + "\"");
        System.out.println("   \"  Hi  \".strip(): \"" + "  Hi  ".strip() + "\"");
        
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * STRINGBUILDER (Mutable, NOT thread-safe, FASTER)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a WHITEBOARD 📝
     * ─────────────────────────
     *   You can write, erase, and rewrite easily
     *   Only ONE person using it (not thread-safe)
     *   But it's FAST!
     */
    static void stringBuilderDemo() {
        System.out.println("4️⃣ STRINGBUILDER (Mutable, Fast, Not Thread-safe)");
        System.out.println("─────────────────────────────────────────────\n");
        
        StringBuilder sb = new StringBuilder("Hello");
        
        System.out.println("StringBuilder sb = new StringBuilder(\"Hello\")");
        System.out.println("\nOperations:");
        
        sb.append(" World");
        System.out.println("   append(\" World\"): \"" + sb + "\"");
        
        sb.insert(5, ",");
        System.out.println("   insert(5, \",\"): \"" + sb + "\"");
        
        sb.replace(0, 5, "Hi");
        System.out.println("   replace(0, 5, \"Hi\"): \"" + sb + "\"");
        
        sb.delete(2, 3);
        System.out.println("   delete(2, 3): \"" + sb + "\"");
        
        sb.reverse();
        System.out.println("   reverse(): \"" + sb + "\"");
        
        sb.reverse();  // Back to normal
        System.out.println("   reverse() again: \"" + sb + "\"");
        
        // Performance comparison
        System.out.println("\n⚡ Performance Test:");
        
        long start = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < 10000; i++) {
            str += i;  // Creates new String each time! Slow!
        }
        long stringTime = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb2.append(i);  // Modifies same object. Fast!
        }
        long sbTime = System.currentTimeMillis() - start;
        
        System.out.println("   String concatenation (10000 times): " + stringTime + "ms");
        System.out.println("   StringBuilder append (10000 times): " + sbTime + "ms");
        System.out.println("   StringBuilder is MUCH faster!\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * STRINGBUFFER (Mutable, Thread-safe, SLOWER)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a SHARED WHITEBOARD with LOCK 🔒
     * ──────────────────────────────────────────
     *   Multiple people can use it
     *   But they take turns (synchronized)
     *   Slower because of waiting
     */
    static void stringBufferDemo() {
        System.out.println("5️⃣ STRINGBUFFER (Mutable, Thread-safe, Slower)");
        System.out.println("─────────────────────────────────────────────\n");
        
        StringBuffer sbuf = new StringBuffer("Hello");
        
        System.out.println("StringBuffer sbuf = new StringBuffer(\"Hello\")");
        System.out.println("Same methods as StringBuilder, but synchronized.\n");
        
        sbuf.append(" World");
        System.out.println("   append(\" World\"): \"" + sbuf + "\"");
        
        System.out.println("\n📌 When to use:");
        System.out.println("   • StringBuilder → Single thread (99% cases)");
        System.out.println("   • StringBuffer → Multiple threads need same builder\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * COMPARISON
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void comparison() {
        System.out.println("6️⃣ STRING vs STRINGBUILDER vs STRINGBUFFER");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   ┌──────────────────┬────────────────┬────────────────┬────────────────┐");
        System.out.println("   │                  │     String     │ StringBuilder  │  StringBuffer  │");
        System.out.println("   ├──────────────────┼────────────────┼────────────────┼────────────────┤");
        System.out.println("   │ Mutable          │      ❌        │       ✅       │       ✅       │");
        System.out.println("   │ Thread-safe      │      ✅        │       ❌       │       ✅       │");
        System.out.println("   │ Performance      │     Slow*      │      Fast      │     Medium     │");
        System.out.println("   │ Storage          │   String Pool  │      Heap      │      Heap      │");
        System.out.println("   │ When to use      │ Few operations │  Many changes  │  Multi-thread  │");
        System.out.println("   └──────────────────┴────────────────┴────────────────┴────────────────┘");
        System.out.println("   * Slow when concatenating in loops\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * STRING FORMATTING
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void stringFormatting() {
        System.out.println("7️⃣ STRING FORMATTING");
        System.out.println("─────────────────────────────────────────────\n");
        
        String name = "John";
        int age = 25;
        double salary = 50000.567;
        
        // printf style
        System.out.println("A) String.format() / printf:");
        System.out.printf("   Name: %s, Age: %d, Salary: %.2f%n", name, age, salary);
        String formatted = String.format("Name: %s, Age: %d", name, age);
        System.out.println("   " + formatted);
        
        // Format specifiers
        System.out.println("\nB) Format Specifiers:");
        System.out.println("   %s - String");
        System.out.println("   %d - Integer");
        System.out.println("   %f - Float/Double");
        System.out.println("   %.2f - 2 decimal places");
        System.out.println("   %n - Newline");
        System.out.println("   %10s - Right pad to 10 chars");
        System.out.println("   %-10s - Left pad to 10 chars");
        
        // Java 15+ formatted() method
        System.out.println("\nC) Java 15+ formatted() method:");
        String result = "Hello %s, you are %d years old".formatted(name, age);
        System.out.println("   " + result);
        
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * TEXT BLOCKS (Java 15+)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void textBlocks() {
        System.out.println("8️⃣ TEXT BLOCKS (Java 15+)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Old way
        String oldJson = "{\n" +
                         "  \"name\": \"John\",\n" +
                         "  \"age\": 25\n" +
                         "}";
        
        // New way with text blocks
        String newJson = """
                {
                  "name": "John",
                  "age": 25
                }
                """;
        
        System.out.println("Old way (ugly):");
        System.out.println("   String json = \"{\\n\" +");
        System.out.println("                 \"  \\\"name\\\": \\\"John\\\"\\n\" + ...");
        
        System.out.println("\nNew way (text blocks):");
        System.out.println("   String json = \"\"\"");
        System.out.println("           {");
        System.out.println("             \"name\": \"John\"");
        System.out.println("           }");
        System.out.println("           \"\"\";");
        
        System.out.println("\nOutput:");
        System.out.println(newJson);
        
        System.out.println("📌 Text Block Features:");
        System.out.println("   • Multi-line strings without \\n");
        System.out.println("   • No escape for quotes");
        System.out.println("   • Preserves formatting");
        System.out.println("   • Great for JSON, SQL, HTML");
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Java Strings");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  📌 String: Immutable, use for constants, few operations");
        System.out.println("  📌 StringBuilder: Mutable, fast, single-thread");
        System.out.println("  📌 StringBuffer: Mutable, thread-safe, multi-thread");
        System.out.println("  📌 String Pool: Reuses literals, saves memory");
        System.out.println();
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"String is immutable for security, thread-safety, and");
        System.out.println("      caching. Use StringBuilder for concatenation in loops.");
        System.out.println("      == checks reference, equals() checks content.");
        System.out.println("      String literals go to pool, new String() goes to heap.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q1: Why is String immutable in Java?
 * A1: 1. String Pool - multiple references can share same string
 *     2. Security - parameters (passwords, URLs) can't be changed
 *     3. Thread-safe - no synchronization needed
 *     4. Caching - hashCode is cached (used in HashMap keys)
 * 
 * Q2: What is String Pool?
 * A2: A special memory area where String literals are stored.
 *     When you create "Hello", JVM checks pool first.
 *     If exists, returns reference. If not, creates and stores.
 * 
 * Q3: How many objects are created: String s = new String("Hello");
 * A3: Possibly 2 objects:
 *     1. "Hello" in String Pool (if not already exists)
 *     2. New object in Heap (always created due to 'new')
 * 
 * Q4: == vs equals() for Strings?
 * A4: == compares REFERENCE (memory address)
 *     equals() compares CONTENT (characters)
 *     Always use equals() for String comparison!
 * 
 * Q5: String vs StringBuilder vs StringBuffer?
 * A5: String - immutable, slow for concatenation
 *     StringBuilder - mutable, fast, NOT thread-safe
 *     StringBuffer - mutable, slow, thread-safe (synchronized)
 * 
 * Q6: What does intern() do?
 * A6: Moves String to pool and returns pool reference.
 *     String s = new String("Hi").intern(); // Now in pool
 */
