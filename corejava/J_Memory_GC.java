/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  CORE JAVA - PART 10: MEMORY MANAGEMENT & GARBAGE COLLECTION
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME 📖
 * ─────────────
 * Think of JVM memory like a HOUSE:
 * 
 *   STACK   → Your desk 🗄️ (small, organized, for current work)
 *   HEAP    → Your warehouse 🏭 (big, for all your stuff/objects)
 *   
 *   Garbage Collector = Cleaning service that removes unused items from warehouse!
 * 
 */

package corejava;

import java.lang.ref.*;

public class J_Memory_GC {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  JAVA MEMORY MANAGEMENT & GARBAGE COLLECTION");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        jvmMemoryAreas();
        stackVsHeap();
        garbageCollection();
        gcAlgorithms();
        memoryLeaks();
        referenceTypes();
        jvmTuning();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * JVM MEMORY AREAS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void jvmMemoryAreas() {
        System.out.println("1️⃣ JVM MEMORY AREAS");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   ┌─────────────────────────────────────────────────────────┐");
        System.out.println("   │                    JVM MEMORY                           │");
        System.out.println("   ├─────────────────────────────────────────────────────────┤");
        System.out.println("   │                                                         │");
        System.out.println("   │  ┌─────────────────────────────────────────────────┐   │");
        System.out.println("   │  │                    HEAP                          │   │");
        System.out.println("   │  │  (Objects live here - Garbage Collected)        │   │");
        System.out.println("   │  │                                                  │   │");
        System.out.println("   │  │  ┌──────────────┐  ┌─────────────────────────┐  │   │");
        System.out.println("   │  │  │ Young Gen    │  │      Old Generation     │  │   │");
        System.out.println("   │  │  │ Eden + S0/S1 │  │   (Long-lived objects)  │  │   │");
        System.out.println("   │  │  └──────────────┘  └─────────────────────────┘  │   │");
        System.out.println("   │  └─────────────────────────────────────────────────┘   │");
        System.out.println("   │                                                         │");
        System.out.println("   │  ┌──────────────┐  ┌──────────────┐  ┌─────────────┐   │");
        System.out.println("   │  │    STACK     │  │   METASPACE  │  │ Native Mem  │   │");
        System.out.println("   │  │ (Per thread) │  │ (Class meta) │  │ (JNI, etc)  │   │");
        System.out.println("   │  └──────────────┘  └──────────────┘  └─────────────┘   │");
        System.out.println("   │                                                         │");
        System.out.println("   └─────────────────────────────────────────────────────────┘");
        
        System.out.println("\n📌 Memory Areas:");
        System.out.println("   • HEAP: Objects, arrays (GC managed)");
        System.out.println("   • STACK: Method calls, local variables (per thread)");
        System.out.println("   • METASPACE: Class metadata (replaced PermGen in Java 8)");
        System.out.println("   • Native Memory: JNI, direct buffers");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * STACK vs HEAP
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void stackVsHeap() {
        System.out.println("2️⃣ STACK vs HEAP");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   ┌────────────────────────┬────────────────────────┐");
        System.out.println("   │         STACK          │          HEAP          │");
        System.out.println("   ├────────────────────────┼────────────────────────┤");
        System.out.println("   │ Method calls           │ Objects                │");
        System.out.println("   │ Local variables        │ Instance variables     │");
        System.out.println("   │ Primitive values       │ Arrays                 │");
        System.out.println("   │ Object references      │ Static variables       │");
        System.out.println("   ├────────────────────────┼────────────────────────┤");
        System.out.println("   │ LIFO (Last In First Out)│ No order              │");
        System.out.println("   │ Auto cleaned on exit   │ Garbage collected      │");
        System.out.println("   │ Fast access            │ Slower access          │");
        System.out.println("   │ Thread-specific        │ Shared among threads   │");
        System.out.println("   │ Limited size           │ Larger size            │");
        System.out.println("   │ StackOverflowError     │ OutOfMemoryError       │");
        System.out.println("   └────────────────────────┴────────────────────────┘");
        
        // Example
        System.out.println("\nExample:");
        System.out.println("   void method() {");
        System.out.println("       int x = 10;              // x → STACK");
        System.out.println("       String s = \"Hello\";     // s (ref) → STACK, \"Hello\" → HEAP");
        System.out.println("       Person p = new Person(); // p (ref) → STACK, Object → HEAP");
        System.out.println("   }");
        
        System.out.println("\n   ┌──────────────────┐      ┌──────────────────────┐");
        System.out.println("   │     STACK        │      │        HEAP          │");
        System.out.println("   │                  │      │                      │");
        System.out.println("   │   x = 10         │      │  ┌───────────────┐   │");
        System.out.println("   │   s ────────────────────┼─▶│   \"Hello\"     │   │");
        System.out.println("   │   p ────────────────────┼─▶│  Person obj   │   │");
        System.out.println("   │                  │      │  └───────────────┘   │");
        System.out.println("   └──────────────────┘      └──────────────────────┘");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * GARBAGE COLLECTION
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like a CLEANING SERVICE 🧹
     * ─────────────────────────────────
     *   • Automatically removes objects no one is using
     *   • You don't control WHEN it runs
     *   • You can only SUGGEST with System.gc()
     */
    static void garbageCollection() {
        System.out.println("3️⃣ GARBAGE COLLECTION");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("A) What makes an object eligible for GC?");
        System.out.println("   Object has NO REFERENCES pointing to it.\n");
        
        System.out.println("   // Example 1: Nulling reference");
        System.out.println("   Person p = new Person();");
        System.out.println("   p = null;  // Object now eligible for GC");
        
        System.out.println("\n   // Example 2: Reassigning reference");
        System.out.println("   Person p = new Person(\"A\");");
        System.out.println("   p = new Person(\"B\");  // Person A eligible for GC");
        
        System.out.println("\n   // Example 3: Object created inside method");
        System.out.println("   void method() {");
        System.out.println("       Person p = new Person();");
        System.out.println("   }  // After method ends, object eligible for GC");
        
        System.out.println("\n   // Example 4: Island of isolation");
        System.out.println("   Person p1 = new Person();");
        System.out.println("   Person p2 = new Person();");
        System.out.println("   p1.friend = p2;  p2.friend = p1;");
        System.out.println("   p1 = null; p2 = null;  // Both eligible (circular ref)");
        
        System.out.println("\nB) Heap Generations:");
        System.out.println("   ┌───────────────────────────────────────────────────────┐");
        System.out.println("   │              YOUNG GENERATION                         │");
        System.out.println("   │  ┌─────────────────┬──────────┬──────────┐            │");
        System.out.println("   │  │      EDEN       │    S0    │    S1    │            │");
        System.out.println("   │  │ (New objects)   │(Survivor)│(Survivor)│            │");
        System.out.println("   │  └─────────────────┴──────────┴──────────┘            │");
        System.out.println("   │           ↓ Minor GC (frequent, fast)                 │");
        System.out.println("   ├───────────────────────────────────────────────────────┤");
        System.out.println("   │              OLD GENERATION                           │");
        System.out.println("   │  ┌─────────────────────────────────────────────────┐  │");
        System.out.println("   │  │     Long-lived objects (survived many GCs)      │  │");
        System.out.println("   │  └─────────────────────────────────────────────────┘  │");
        System.out.println("   │           ↓ Major GC (less frequent, slower)          │");
        System.out.println("   └───────────────────────────────────────────────────────┘");
        
        System.out.println("\nC) GC Process:");
        System.out.println("   1. New objects created in EDEN");
        System.out.println("   2. When Eden full → Minor GC");
        System.out.println("   3. Surviving objects move to Survivor (S0/S1)");
        System.out.println("   4. After surviving many GCs → move to OLD");
        System.out.println("   5. When OLD full → Major GC (Full GC)");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * GC ALGORITHMS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void gcAlgorithms() {
        System.out.println("4️⃣ GARBAGE COLLECTOR TYPES");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   ┌─────────────────┬────────────────────────────────────────┐");
        System.out.println("   │  GC Type        │  Description                           │");
        System.out.println("   ├─────────────────┼────────────────────────────────────────┤");
        System.out.println("   │ Serial GC       │ Single thread, small heaps             │");
        System.out.println("   │ -XX:+UseSerialGC│ Good for: Single CPU, small apps       │");
        System.out.println("   ├─────────────────┼────────────────────────────────────────┤");
        System.out.println("   │ Parallel GC     │ Multiple threads, throughput focus     │");
        System.out.println("   │ -XX:+UseParallelGC│ Good for: Batch processing           │");
        System.out.println("   ├─────────────────┼────────────────────────────────────────┤");
        System.out.println("   │ G1 GC (Default) │ Balanced throughput & latency          │");
        System.out.println("   │ -XX:+UseG1GC    │ Good for: General purpose (Java 9+)    │");
        System.out.println("   ├─────────────────┼────────────────────────────────────────┤");
        System.out.println("   │ ZGC             │ Ultra-low latency (<10ms pause)        │");
        System.out.println("   │ -XX:+UseZGC     │ Good for: Large heaps, low latency     │");
        System.out.println("   ├─────────────────┼────────────────────────────────────────┤");
        System.out.println("   │ Shenandoah      │ Low pause time                         │");
        System.out.println("   │                 │ Good for: Low latency requirements     │");
        System.out.println("   └─────────────────┴────────────────────────────────────────┘");
        
        System.out.println("\n📌 When to use what?");
        System.out.println("   • Small apps, single CPU → Serial GC");
        System.out.println("   • Batch processing, throughput → Parallel GC");
        System.out.println("   • General purpose → G1 GC (default in Java 9+)");
        System.out.println("   • Very large heaps, low latency → ZGC");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * MEMORY LEAKS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void memoryLeaks() {
        System.out.println("5️⃣ MEMORY LEAKS IN JAVA");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("Memory leak = Objects kept in memory unintentionally\n");
        
        System.out.println("A) Common Causes:");
        
        System.out.println("\n   1. Unclosed Resources:");
        System.out.println("      InputStream is = new FileInputStream(\"file\");");
        System.out.println("      // Forgot to close! Use try-with-resources!");
        
        System.out.println("\n   2. Static Collections:");
        System.out.println("      static List<Object> cache = new ArrayList<>();");
        System.out.println("      cache.add(object);  // Never removed, keeps growing!");
        
        System.out.println("\n   3. Listeners not removed:");
        System.out.println("      button.addActionListener(listener);");
        System.out.println("      // Forgot to removeActionListener!");
        
        System.out.println("\n   4. Inner class holding outer reference:");
        System.out.println("      class Outer {");
        System.out.println("          class Inner {}  // Holds implicit ref to Outer");
        System.out.println("      }");
        
        System.out.println("\n   5. ThreadLocal not cleaned:");
        System.out.println("      threadLocal.set(value);");
        System.out.println("      // Forgot threadLocal.remove()!");
        
        System.out.println("\nB) How to Detect:");
        System.out.println("   • Monitor heap usage over time");
        System.out.println("   • Use profilers: VisualVM, JProfiler, YourKit");
        System.out.println("   • Heap dumps: jmap -dump:format=b,file=heap.bin <pid>");
        System.out.println("   • Analyze with: Eclipse MAT, jhat");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * REFERENCE TYPES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void referenceTypes() {
        System.out.println("6️⃣ REFERENCE TYPES (java.lang.ref)");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("   ┌─────────────────┬─────────────────────────────────────┐");
        System.out.println("   │  Reference Type │  GC Behavior                        │");
        System.out.println("   ├─────────────────┼─────────────────────────────────────┤");
        System.out.println("   │ Strong          │ NEVER collected (normal reference)  │");
        System.out.println("   │ Object o = new..│                                     │");
        System.out.println("   ├─────────────────┼─────────────────────────────────────┤");
        System.out.println("   │ Soft            │ Collected when memory is LOW        │");
        System.out.println("   │ SoftReference<T>│ Good for: Caches                    │");
        System.out.println("   ├─────────────────┼─────────────────────────────────────┤");
        System.out.println("   │ Weak            │ Collected at NEXT GC                │");
        System.out.println("   │ WeakReference<T>│ Good for: Canonicalizing maps       │");
        System.out.println("   ├─────────────────┼─────────────────────────────────────┤");
        System.out.println("   │ Phantom         │ Collected, used for cleanup actions │");
        System.out.println("   │ PhantomRef<T>   │ Good for: Pre-mortem cleanup        │");
        System.out.println("   └─────────────────┴─────────────────────────────────────┘");
        
        System.out.println("\nExample:");
        System.out.println("   // Soft reference - good for caches");
        System.out.println("   SoftReference<byte[]> cache = new SoftReference<>(new byte[1024]);");
        System.out.println("   byte[] data = cache.get();  // May return null if GC'd");
        System.out.println("   if (data == null) {");
        System.out.println("       data = loadFromDisk();  // Reload");
        System.out.println("   }");
        
        System.out.println("\n   // Weak reference - for WeakHashMap");
        System.out.println("   WeakReference<Person> weakRef = new WeakReference<>(person);");
        System.out.println("   person = null;  // Now weakRef.get() will return null after GC");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * JVM TUNING
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void jvmTuning() {
        System.out.println("7️⃣ JVM TUNING OPTIONS");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("A) Heap Size:");
        System.out.println("   -Xms512m     Initial heap size (512 MB)");
        System.out.println("   -Xmx2g       Maximum heap size (2 GB)");
        System.out.println("   Tip: Set Xms = Xmx to avoid resizing overhead");
        
        System.out.println("\nB) Stack Size:");
        System.out.println("   -Xss1m       Thread stack size (1 MB)");
        
        System.out.println("\nC) Metaspace:");
        System.out.println("   -XX:MetaspaceSize=256m");
        System.out.println("   -XX:MaxMetaspaceSize=512m");
        
        System.out.println("\nD) GC Selection:");
        System.out.println("   -XX:+UseG1GC           G1 (default Java 9+)");
        System.out.println("   -XX:+UseZGC            ZGC (low latency)");
        System.out.println("   -XX:+UseParallelGC     Parallel (throughput)");
        
        System.out.println("\nE) GC Logging:");
        System.out.println("   -Xlog:gc*:file=gc.log");
        
        System.out.println("\nF) Useful Commands:");
        System.out.println("   jps           List Java processes");
        System.out.println("   jstat -gc pid GC statistics");
        System.out.println("   jmap -heap pid Heap info");
        System.out.println("   jstack pid   Thread dump");
        
        System.out.println("\nG) Example Production Settings:");
        System.out.println("   java -Xms4g -Xmx4g \\");
        System.out.println("        -XX:+UseG1GC \\");
        System.out.println("        -XX:MaxGCPauseMillis=200 \\");
        System.out.println("        -Xlog:gc*:file=gc.log \\");
        System.out.println("        -jar app.jar");
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Memory & Garbage Collection");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  📌 STACK: Method calls, local vars, references (thread-specific)");
        System.out.println("  📌 HEAP: Objects, arrays (shared, GC managed)");
        System.out.println("  📌 Young Gen: Eden + Survivors (Minor GC)");
        System.out.println("  📌 Old Gen: Long-lived objects (Major GC)");
        System.out.println("  📌 G1 GC: Default in Java 9+, balanced performance");
        System.out.println();
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"Stack stores method calls and local variables, per thread.");
        System.out.println("      Heap stores objects, shared among threads, GC managed.");
        System.out.println("      Objects become eligible for GC when no references exist.");
        System.out.println("      G1 is default GC from Java 9. Use -Xms/-Xmx for heap size.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q1: What is the difference between Stack and Heap?
 * A1: Stack: Thread-specific, stores method calls/local vars, LIFO, auto-cleaned
 *     Heap: Shared, stores objects, garbage collected, larger
 * 
 * Q2: When is an object eligible for Garbage Collection?
 * A2: When no live thread can reach it (no references pointing to it).
 *     Can happen by: nulling ref, reassigning, method exit, island of isolation.
 * 
 * Q3: Can we force Garbage Collection?
 * A3: No! System.gc() is only a suggestion/hint to JVM.
 *     JVM decides when to actually run GC.
 * 
 * Q4: What is the difference between Minor GC and Major GC?
 * A4: Minor GC: Cleans Young Generation (Eden + Survivors), fast, frequent
 *     Major GC: Cleans Old Generation, slow, less frequent (also called Full GC)
 * 
 * Q5: What causes OutOfMemoryError?
 * A5: - Heap space: Too many objects, memory leak
 *     - Metaspace: Too many classes loaded
 *     - GC overhead: GC running too much with little reclaim
 * 
 * Q6: What is the default GC in Java 11/17/21?
 * A6: G1 GC (Garbage First) - balanced throughput and latency.
 *     Good for most applications.
 * 
 * Q7: How to detect memory leaks?
 * A7: - Monitor heap growth over time
 *     - Use profilers (VisualVM, JProfiler)
 *     - Analyze heap dumps with Eclipse MAT
 *     - Look for growing collections, unclosed resources
 */
