/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  CORE JAVA - PART 1: DATA TYPES & VARIABLES
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME 📖
 * ─────────────
 * Imagine you have different BOXES to store different things:
 * 
 *   📦 Small box    → for small numbers (byte, short)
 *   📦 Medium box   → for regular numbers (int)
 *   📦 Large box    → for big numbers (long)
 *   📦 Special box  → for decimals (float, double)
 *   📦 Letter box   → for single letters (char)
 *   📦 Yes/No box   → for true/false (boolean)
 * 
 * Each box has a NAME (variable name) and can hold ONE type of thing!
 * 
 */

package corejava;

public class A_DataTypes_Variables {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  JAVA DATA TYPES & VARIABLES");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        primitiveTypes();
        referenceTypes();
        typeConversion();
        operators();
        controlFlow();
        varKeyword();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * PRIMITIVE DATA TYPES (8 types - stored directly in memory)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * Think of primitives like ACTUAL ITEMS in a box (not a reference to somewhere else)
     * 
     *   TYPE     │ SIZE    │ RANGE                          │ DEFAULT │ EXAMPLE
     *   ─────────┼─────────┼────────────────────────────────┼─────────┼─────────
     *   byte     │ 1 byte  │ -128 to 127                    │ 0       │ byte b = 100;
     *   short    │ 2 bytes │ -32,768 to 32,767              │ 0       │ short s = 1000;
     *   int      │ 4 bytes │ -2.1 billion to 2.1 billion    │ 0       │ int i = 100000;
     *   long     │ 8 bytes │ -9.2 quintillion to 9.2 quint  │ 0L      │ long l = 100000L;
     *   float    │ 4 bytes │ ~7 decimal digits              │ 0.0f    │ float f = 3.14f;
     *   double   │ 8 bytes │ ~15 decimal digits             │ 0.0d    │ double d = 3.14159;
     *   char     │ 2 bytes │ 0 to 65,535 (Unicode)          │ '\u0000'│ char c = 'A';
     *   boolean  │ 1 bit   │ true or false                  │ false   │ boolean b = true;
     */
    static void primitiveTypes() {
        System.out.println("1️⃣ PRIMITIVE DATA TYPES");
        System.out.println("─────────────────────────────────────────────\n");
        
        // INTEGER TYPES (whole numbers)
        byte myByte = 127;              // Tiny box: -128 to 127
        short myShort = 32000;          // Small box: -32,768 to 32,767
        int myInt = 2000000000;         // Regular box: ~2 billion
        long myLong = 9000000000L;      // Big box: ~9 quintillion (note the L!)
        
        System.out.println("Integer Types:");
        System.out.println("  byte: " + myByte + " (1 byte, -128 to 127)");
        System.out.println("  short: " + myShort + " (2 bytes)");
        System.out.println("  int: " + myInt + " (4 bytes) ← Most used!");
        System.out.println("  long: " + myLong + " (8 bytes, needs 'L' suffix)");
        
        // FLOATING POINT TYPES (decimals)
        float myFloat = 3.14f;          // Less precise (note the f!)
        double myDouble = 3.14159265359; // More precise (default for decimals)
        
        System.out.println("\nFloating Point Types:");
        System.out.println("  float: " + myFloat + " (4 bytes, needs 'f' suffix)");
        System.out.println("  double: " + myDouble + " (8 bytes) ← Most used!");
        
        // CHARACTER TYPE
        char myChar = 'A';              // Single character in single quotes
        char unicodeChar = '\u0041';    // Unicode for 'A'
        char numberChar = 65;           // ASCII value for 'A'
        
        System.out.println("\nCharacter Type:");
        System.out.println("  char: '" + myChar + "' (2 bytes, Unicode)");
        System.out.println("  Unicode '\\u0041': '" + unicodeChar + "'");
        System.out.println("  ASCII 65: '" + numberChar + "'");
        
        // BOOLEAN TYPE
        boolean isJavaFun = true;
        boolean isBoring = false;
        
        System.out.println("\nBoolean Type:");
        System.out.println("  Is Java fun? " + isJavaFun);
        System.out.println("  Is it boring? " + isBoring);
        
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * REFERENCE DATA TYPES (Objects - stored as reference/address)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * Think of reference like a REMOTE CONTROL pointing to a TV
     * The remote (variable) doesn't contain the TV, just points to it!
     * 
     *   REMOTE (reference)  →→→→→  TV (actual object in heap memory)
     */
    static void referenceTypes() {
        System.out.println("2️⃣ REFERENCE DATA TYPES");
        System.out.println("─────────────────────────────────────────────\n");
        
        // String (most common reference type)
        String name = "John";           // Points to String object
        String greeting = new String("Hello"); // Explicit object creation
        
        // Arrays
        int[] numbers = {1, 2, 3, 4, 5}; // Array of primitives
        String[] names = {"Alice", "Bob"}; // Array of objects
        
        // Any class/object
        StringBuilder sb = new StringBuilder("Hello");
        
        System.out.println("String: " + name);
        System.out.println("Array: " + java.util.Arrays.toString(numbers));
        System.out.println("StringBuilder: " + sb);
        
        // NULL - reference pointing to nothing
        String empty = null;  // Remote with no TV!
        System.out.println("Null reference: " + empty);
        
        // IMPORTANT DIFFERENCE
        System.out.println("\n📌 Primitive vs Reference:");
        System.out.println("   int a = 5;     // 'a' CONTAINS 5");
        System.out.println("   String s = \"Hi\"; // 's' POINTS TO \"Hi\" object");
        
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * TYPE CONVERSION (Casting)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * Like pouring water between different sized containers:
     * 
     *   Small → Big   = AUTOMATIC (Widening) - No data loss
     *   Big → Small   = MANUAL (Narrowing) - Might lose data!
     */
    static void typeConversion() {
        System.out.println("3️⃣ TYPE CONVERSION (Casting)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // WIDENING (Automatic) - Small to Big
        // byte → short → int → long → float → double
        int myInt = 100;
        long myLong = myInt;      // int to long (automatic)
        double myDouble = myLong; // long to double (automatic)
        
        System.out.println("Widening (Automatic):");
        System.out.println("  int " + myInt + " → long " + myLong + " → double " + myDouble);
        
        // NARROWING (Manual) - Big to Small
        double bigValue = 9.78;
        int smallValue = (int) bigValue;  // Must cast! Loses decimal
        
        System.out.println("\nNarrowing (Manual - needs cast):");
        System.out.println("  double " + bigValue + " → int " + smallValue + " (lost .78!)");
        
        // String to Number
        String numStr = "123";
        int parsed = Integer.parseInt(numStr);
        double parsedDouble = Double.parseDouble("3.14");
        
        System.out.println("\nString to Number:");
        System.out.println("  \"123\" → " + parsed);
        System.out.println("  \"3.14\" → " + parsedDouble);
        
        // Number to String
        int num = 456;
        String str1 = String.valueOf(num);
        String str2 = num + "";  // Concatenation trick
        String str3 = Integer.toString(num);
        
        System.out.println("\nNumber to String:");
        System.out.println("  456 → \"" + str1 + "\"");
        
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * OPERATORS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void operators() {
        System.out.println("4️⃣ OPERATORS");
        System.out.println("─────────────────────────────────────────────\n");
        
        int a = 10, b = 3;
        
        // ARITHMETIC
        System.out.println("Arithmetic Operators:");
        System.out.println("  " + a + " + " + b + " = " + (a + b));   // Addition
        System.out.println("  " + a + " - " + b + " = " + (a - b));   // Subtraction
        System.out.println("  " + a + " * " + b + " = " + (a * b));   // Multiplication
        System.out.println("  " + a + " / " + b + " = " + (a / b));   // Division (integer!)
        System.out.println("  " + a + " % " + b + " = " + (a % b));   // Modulus (remainder)
        
        // INCREMENT/DECREMENT
        int x = 5;
        System.out.println("\nIncrement/Decrement:");
        System.out.println("  x = " + x);
        System.out.println("  x++ = " + (x++) + " (then x becomes " + x + ")");  // Post
        System.out.println("  ++x = " + (++x) + " (x is now " + x + ")");         // Pre
        
        // COMPARISON
        System.out.println("\nComparison Operators (return boolean):");
        System.out.println("  10 == 3: " + (10 == 3));  // Equal
        System.out.println("  10 != 3: " + (10 != 3));  // Not equal
        System.out.println("  10 > 3: " + (10 > 3));    // Greater
        System.out.println("  10 < 3: " + (10 < 3));    // Less
        System.out.println("  10 >= 10: " + (10 >= 10));// Greater or equal
        
        // LOGICAL
        boolean t = true, f = false;
        System.out.println("\nLogical Operators:");
        System.out.println("  true && false: " + (t && f));  // AND
        System.out.println("  true || false: " + (t || f));  // OR
        System.out.println("  !true: " + (!t));               // NOT
        
        // TERNARY (? :)
        int age = 20;
        String status = (age >= 18) ? "Adult" : "Minor";
        System.out.println("\nTernary Operator:");
        System.out.println("  age=" + age + " → " + status);
        
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * CONTROL FLOW
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void controlFlow() {
        System.out.println("5️⃣ CONTROL FLOW");
        System.out.println("─────────────────────────────────────────────\n");
        
        // IF-ELSE
        int score = 85;
        System.out.println("If-Else (score = " + score + "):");
        if (score >= 90) {
            System.out.println("  Grade: A");
        } else if (score >= 80) {
            System.out.println("  Grade: B");
        } else if (score >= 70) {
            System.out.println("  Grade: C");
        } else {
            System.out.println("  Grade: F");
        }
        
        // SWITCH (Traditional)
        int day = 3;
        System.out.println("\nSwitch (day = " + day + "):");
        switch (day) {
            case 1:
                System.out.println("  Monday");
                break;
            case 2:
                System.out.println("  Tuesday");
                break;
            case 3:
                System.out.println("  Wednesday");
                break;
            default:
                System.out.println("  Other day");
        }
        
        // FOR LOOP
        System.out.println("\nFor Loop:");
        System.out.print("  ");
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        
        // WHILE LOOP
        System.out.println("\nWhile Loop:");
        System.out.print("  ");
        int count = 1;
        while (count <= 3) {
            System.out.print(count + " ");
            count++;
        }
        System.out.println();
        
        // DO-WHILE LOOP (executes at least once!)
        System.out.println("\nDo-While Loop (runs at least once):");
        System.out.print("  ");
        int num = 1;
        do {
            System.out.print(num + " ");
            num++;
        } while (num <= 3);
        System.out.println();
        
        // FOR-EACH (Enhanced for)
        System.out.println("\nFor-Each Loop:");
        int[] numbers = {10, 20, 30};
        System.out.print("  ");
        for (int n : numbers) {
            System.out.print(n + " ");
        }
        System.out.println();
        
        // BREAK & CONTINUE
        System.out.println("\nBreak & Continue:");
        System.out.print("  Break at 3: ");
        for (int i = 1; i <= 5; i++) {
            if (i == 3) break;
            System.out.print(i + " ");
        }
        System.out.println();
        
        System.out.print("  Skip 3 (continue): ");
        for (int i = 1; i <= 5; i++) {
            if (i == 3) continue;
            System.out.print(i + " ");
        }
        System.out.println("\n");
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * VAR KEYWORD (Java 10+)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * Let compiler figure out the type automatically!
     * Like saying "give me a box that fits this item"
     */
    static void varKeyword() {
        System.out.println("6️⃣ VAR KEYWORD (Java 10+)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Type inference with var
        var number = 10;           // Compiler knows it's int
        var name = "John";         // Compiler knows it's String
        var price = 19.99;         // Compiler knows it's double
        var list = new java.util.ArrayList<String>(); // Compiler knows the type
        
        System.out.println("var number = 10;    → type is: " + ((Object)number).getClass().getSimpleName());
        System.out.println("var name = \"John\"; → type is: " + name.getClass().getSimpleName());
        System.out.println("var price = 19.99;  → type is: " + ((Object)price).getClass().getSimpleName());
        
        System.out.println("\n📌 var Rules:");
        System.out.println("   ✅ Must be initialized: var x = 5;");
        System.out.println("   ❌ Cannot be: var x;  (no initial value)");
        System.out.println("   ❌ Cannot be: var x = null;  (can't infer type)");
        System.out.println("   ❌ Cannot use for method parameters");
        System.out.println("   ❌ Cannot use for class fields");
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Data Types & Variables");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  📌 8 PRIMITIVE TYPES:");
        System.out.println("     • Integers: byte, short, int, long");
        System.out.println("     • Decimals: float, double");
        System.out.println("     • Character: char");
        System.out.println("     • Boolean: boolean");
        System.out.println();
        System.out.println("  📌 REFERENCE TYPES:");
        System.out.println("     • String, Arrays, Classes, Interfaces");
        System.out.println("     • Can be null (primitives cannot!)");
        System.out.println();
        System.out.println("  📌 TYPE CONVERSION:");
        System.out.println("     • Widening: automatic (int → long)");
        System.out.println("     • Narrowing: manual cast needed (double → int)");
        System.out.println();
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"Primitives store actual values, references store addresses.");
        System.out.println("      int uses 4 bytes, long uses 8 bytes. Default int value is 0,");
        System.out.println("      default reference value is null.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q1: What is the difference between int and Integer?
 * A1: int is primitive (4 bytes, stores value directly, default 0)
 *     Integer is wrapper class (object, can be null, has methods)
 * 
 * Q2: Why do we need wrapper classes?
 * A2: - Collections only work with objects (ArrayList<Integer>)
 *     - To represent null values
 *     - Utility methods (Integer.parseInt())
 * 
 * Q3: What is autoboxing and unboxing?
 * A3: Autoboxing: primitive → wrapper (int → Integer) automatically
 *     Unboxing: wrapper → primitive (Integer → int) automatically
 * 
 * Q4: What is the default value of local variables?
 * A4: Local variables have NO default value! Must be initialized.
 *     (Instance variables have defaults: 0, false, null)
 * 
 * Q5: What is the range of byte?
 * A5: -128 to 127 (1 byte = 8 bits = 2^8 = 256 values)
 */
