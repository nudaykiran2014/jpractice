/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 *  CORE JAVA - PART 6: EXCEPTION HANDLING
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME 📖
 * ─────────────
 * Think of Exception like ACCIDENTS on a road trip 🚗
 * 
 *   TRY     → "I'll try to drive to the destination"
 *   CATCH   → "If accident happens, here's my backup plan"
 *   FINALLY → "No matter what, I'll return the rental car"
 *   THROW   → "There's a problem! Alert everyone!"
 *   THROWS  → "Warning: This road might have problems"
 * 
 * 
 * HIERARCHY:
 * ──────────
 *                      Throwable
 *                          │
 *              ┌───────────┴───────────┐
 *              │                       │
 *           Error                  Exception
 *        (Don't catch!)                │
 *     OutOfMemoryError        ┌────────┴────────┐
 *     StackOverflowError      │                 │
 *                      RuntimeException    Checked Exceptions
 *                      (Unchecked)         (Must handle!)
 *                          │                    │
 *                 NullPointerException    IOException
 *                 ArrayIndexOutOf...      SQLException
 *                 ArithmeticException     FileNotFoundException
 */

package corejava;

import java.io.*;

public class F_Exceptions {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("  JAVA EXCEPTION HANDLING");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");
        
        basicTryCatch();
        multipleCatch();
        finallyBlock();
        throwAndThrows();
        checkedVsUnchecked();
        customExceptions();
        tryWithResources();
        bestPractices();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * BASIC TRY-CATCH
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void basicTryCatch() {
        System.out.println("1️⃣ BASIC TRY-CATCH");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Without exception handling - program crashes!
        System.out.println("Without try-catch (dangerous!):");
        System.out.println("   int result = 10 / 0;  // 💥 Program crashes!\n");
        
        // With exception handling - program continues
        System.out.println("With try-catch (safe!):");
        try {
            int result = 10 / 0;  // This will cause exception
            System.out.println("Result: " + result);  // Won't execute
        } catch (ArithmeticException e) {
            System.out.println("   Caught: " + e.getMessage());
            System.out.println("   Program continues safely! ✅");
        }
        
        System.out.println("\n   try {");
        System.out.println("       // Risky code");
        System.out.println("   } catch (ExceptionType e) {");
        System.out.println("       // Handle exception");
        System.out.println("   }");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * MULTIPLE CATCH BLOCKS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void multipleCatch() {
        System.out.println("2️⃣ MULTIPLE CATCH BLOCKS");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Multiple catch blocks
        System.out.println("A) Multiple Catch Blocks:");
        try {
            int[] arr = {1, 2, 3};
            System.out.println("   Accessing arr[5]...");
            System.out.println(arr[5]);  // ArrayIndexOutOfBoundsException
        } catch (ArithmeticException e) {
            System.out.println("   Arithmetic error!");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("   Caught: Array index out of bounds!");
        } catch (Exception e) {
            System.out.println("   Some other exception!");
        }
        
        // Multi-catch (Java 7+) - Catch multiple types in one block
        System.out.println("\nB) Multi-catch (Java 7+):");
        System.out.println("   catch (IOException | SQLException e) {");
        System.out.println("       // Handle both types same way");
        System.out.println("   }");
        
        // Order matters!
        System.out.println("\nC) Order Matters!");
        System.out.println("   ❌ catch (Exception e) { }");
        System.out.println("      catch (IOException e) { }  // Compile error!");
        System.out.println("   ");
        System.out.println("   ✅ catch (IOException e) { }  // Specific first");
        System.out.println("      catch (Exception e) { }    // General last");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * FINALLY BLOCK
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like cleaning up after a party 🎉
     * ───────────────────────────────────────
     *   No matter if party was good or bad,
     *   You MUST clean up the house!
     *   
     *   finally = Code that ALWAYS runs (cleanup)
     */
    static void finallyBlock() {
        System.out.println("3️⃣ FINALLY BLOCK (Always Executes)");
        System.out.println("─────────────────────────────────────────────\n");
        
        // Example 1: Exception occurs
        System.out.println("A) When exception occurs:");
        try {
            System.out.println("   try: Risky operation...");
            int x = 10 / 0;
        } catch (Exception e) {
            System.out.println("   catch: Handling error...");
        } finally {
            System.out.println("   finally: Cleanup (ALWAYS runs!) ✅");
        }
        
        // Example 2: No exception
        System.out.println("\nB) When NO exception:");
        try {
            System.out.println("   try: Safe operation...");
            int x = 10 / 2;
        } catch (Exception e) {
            System.out.println("   catch: (not executed)");
        } finally {
            System.out.println("   finally: Still runs! ✅");
        }
        
        // Example 3: Return in try
        System.out.println("\nC) Even with return statement:");
        System.out.println("   Result: " + testFinallyWithReturn());
        
        System.out.println("\n📌 finally is used for:");
        System.out.println("   • Closing database connections");
        System.out.println("   • Closing file streams");
        System.out.println("   • Releasing resources");
        System.out.println();
    }
    
    static String testFinallyWithReturn() {
        try {
            return "   Returned from try";
        } finally {
            System.out.println("   finally: Runs even before return!");
        }
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * THROW vs THROWS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void throwAndThrows() {
        System.out.println("4️⃣ THROW vs THROWS");
        System.out.println("─────────────────────────────────────────────\n");
        
        // throw - Actually throws exception
        System.out.println("A) throw (Actually throw an exception):");
        System.out.println("   void validate(int age) {");
        System.out.println("       if (age < 0) {");
        System.out.println("           throw new IllegalArgumentException(\"Invalid age\");");
        System.out.println("       }");
        System.out.println("   }");
        
        try {
            validateAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("   Caught: " + e.getMessage());
        }
        
        // throws - Declares possible exceptions
        System.out.println("\nB) throws (Declare possible exceptions):");
        System.out.println("   void readFile() throws IOException {");
        System.out.println("       // This method MIGHT throw IOException");
        System.out.println("       // Caller must handle it!");
        System.out.println("   }");
        
        // Comparison
        System.out.println("\nC) Comparison:");
        System.out.println("   ┌────────────────────┬─────────────────────────────────────┐");
        System.out.println("   │       throw        │              throws                 │");
        System.out.println("   ├────────────────────┼─────────────────────────────────────┤");
        System.out.println("   │ Used inside method │ Used in method signature            │");
        System.out.println("   │ Throws ONE exception│ Declares POSSIBLE exceptions       │");
        System.out.println("   │ throw new E()      │ void method() throws E1, E2         │");
        System.out.println("   │ Followed by object │ Followed by class names             │");
        System.out.println("   └────────────────────┴─────────────────────────────────────┘");
        System.out.println();
    }
    
    static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative!");
        }
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * CHECKED vs UNCHECKED EXCEPTIONS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void checkedVsUnchecked() {
        System.out.println("5️⃣ CHECKED vs UNCHECKED EXCEPTIONS");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("A) CHECKED Exceptions (Compile-time):");
        System.out.println("   • MUST be handled (try-catch) or declared (throws)");
        System.out.println("   • Compiler forces you to handle them");
        System.out.println("   • Examples:");
        System.out.println("     - IOException");
        System.out.println("     - SQLException");
        System.out.println("     - FileNotFoundException");
        System.out.println("     - ClassNotFoundException");
        
        System.out.println("\nB) UNCHECKED Exceptions (Runtime):");
        System.out.println("   • NOT mandatory to handle");
        System.out.println("   • Extend RuntimeException");
        System.out.println("   • Usually programming errors");
        System.out.println("   • Examples:");
        System.out.println("     - NullPointerException");
        System.out.println("     - ArrayIndexOutOfBoundsException");
        System.out.println("     - ArithmeticException");
        System.out.println("     - IllegalArgumentException");
        
        System.out.println("\nC) Error (DON'T catch!):");
        System.out.println("   • Serious problems, can't recover");
        System.out.println("   • Examples:");
        System.out.println("     - OutOfMemoryError");
        System.out.println("     - StackOverflowError");
        
        System.out.println("\n   ┌────────────────────┬────────────────────┬────────────────────┐");
        System.out.println("   │                    │     Checked        │     Unchecked      │");
        System.out.println("   ├────────────────────┼────────────────────┼────────────────────┤");
        System.out.println("   │ Must handle?       │ YES (compile err)  │ NO (optional)      │");
        System.out.println("   │ Extends            │ Exception          │ RuntimeException   │");
        System.out.println("   │ When               │ External problems  │ Programming bugs   │");
        System.out.println("   │ Example            │ File not found     │ Null pointer       │");
        System.out.println("   └────────────────────┴────────────────────┴────────────────────┘");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * CUSTOM EXCEPTIONS
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void customExceptions() {
        System.out.println("6️⃣ CUSTOM EXCEPTIONS");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("A) Creating Custom Exception:");
        System.out.println("   // Checked exception");
        System.out.println("   class InsufficientBalanceException extends Exception {");
        System.out.println("       public InsufficientBalanceException(String msg) {");
        System.out.println("           super(msg);");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();
        System.out.println("   // Unchecked exception");
        System.out.println("   class InvalidUserException extends RuntimeException {");
        System.out.println("       public InvalidUserException(String msg) {");
        System.out.println("           super(msg);");
        System.out.println("       }");
        System.out.println("   }");
        
        // Demo
        System.out.println("\nB) Using Custom Exception:");
        BankAccountDemo account = new BankAccountDemo(1000);
        try {
            account.withdraw(1500);
        } catch (InsufficientBalanceException e) {
            System.out.println("   Caught custom exception: " + e.getMessage());
        }
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * TRY-WITH-RESOURCES (Java 7+)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * 
     * STORY: Like auto-closing doors 🚪
     * ─────────────────────────────────
     *   Automatic doors close themselves
     *   No need to manually close!
     */
    static void tryWithResources() {
        System.out.println("7️⃣ TRY-WITH-RESOURCES (Java 7+)");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("A) Old Way (Manual close):");
        System.out.println("   BufferedReader br = null;");
        System.out.println("   try {");
        System.out.println("       br = new BufferedReader(new FileReader(\"file.txt\"));");
        System.out.println("       // use br");
        System.out.println("   } finally {");
        System.out.println("       if (br != null) br.close();  // Must close manually!");
        System.out.println("   }");
        
        System.out.println("\nB) New Way (Auto-close):");
        System.out.println("   try (BufferedReader br = new BufferedReader(...)) {");
        System.out.println("       // use br");
        System.out.println("   }  // Automatically closed! ✅");
        
        // Demo with custom AutoCloseable
        System.out.println("\nC) Demo with AutoCloseable:");
        try (MyResource resource = new MyResource()) {
            resource.doSomething();
        }  // Auto-closed!
        
        System.out.println("\n📌 Requirements:");
        System.out.println("   • Resource must implement AutoCloseable");
        System.out.println("   • Resource declared in try() parentheses");
        System.out.println("   • Multiple resources: try (R1 r1 = ...; R2 r2 = ...)");
        System.out.println();
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * BEST PRACTICES
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    static void bestPractices() {
        System.out.println("8️⃣ BEST PRACTICES");
        System.out.println("─────────────────────────────────────────────\n");
        
        System.out.println("✅ DO:");
        System.out.println("   • Catch specific exceptions (not just Exception)");
        System.out.println("   • Log the exception (don't just print)");
        System.out.println("   • Use try-with-resources for closeable resources");
        System.out.println("   • Create custom exceptions for business logic");
        System.out.println("   • Include meaningful error messages");
        System.out.println("   • Clean up resources in finally block");
        
        System.out.println("\n❌ DON'T:");
        System.out.println("   • Catch Exception or Throwable (too broad)");
        System.out.println("   • Empty catch block (swallowing exceptions)");
        System.out.println("   • Use exceptions for flow control");
        System.out.println("   • Throw exceptions from finally");
        System.out.println("   • Catch and rethrow without adding info");
        
        System.out.println("\n📌 Exception Chaining:");
        System.out.println("   try {");
        System.out.println("       // code");
        System.out.println("   } catch (SQLException e) {");
        System.out.println("       throw new ServiceException(\"DB error\", e);  // Preserve cause");
        System.out.println("   }");
        
        printSummary();
    }
    
    static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("  SUMMARY: Exception Handling");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  📌 try-catch: Handle exceptions");
        System.out.println("  📌 finally: Always executes (cleanup)");
        System.out.println("  📌 throw: Actually throw an exception");
        System.out.println("  📌 throws: Declare possible exceptions");
        System.out.println("  📌 Checked: Must handle (IOException)");
        System.out.println("  📌 Unchecked: Optional (NullPointerException)");
        System.out.println();
        System.out.println("  🎯 INTERVIEW TIP:");
        System.out.println("     \"Checked exceptions must be handled or declared, they extend");
        System.out.println("      Exception. Unchecked extend RuntimeException, handling is");
        System.out.println("      optional. finally always runs for cleanup. Use try-with-");
        System.out.println("      resources for AutoCloseable objects.\"");
        System.out.println("═══════════════════════════════════════════════════════════════════");
    }
}

// Custom Exception classes
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

class BankAccountDemo {
    private double balance;
    
    public BankAccountDemo(double balance) {
        this.balance = balance;
    }
    
    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException(
                "Balance: " + balance + ", Requested: " + amount
            );
        }
        balance -= amount;
    }
}

// AutoCloseable resource demo
class MyResource implements AutoCloseable {
    public MyResource() {
        System.out.println("   Resource opened");
    }
    
    public void doSomething() {
        System.out.println("   Using resource...");
    }
    
    @Override
    public void close() {
        System.out.println("   Resource auto-closed! ✅");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * INTERVIEW QUESTIONS
 * ═══════════════════════════════════════════════════════════════════════════════════════════════
 * 
 * Q1: Checked vs Unchecked exceptions?
 * A1: Checked: Must handle, extends Exception (IOException)
 *     Unchecked: Optional handling, extends RuntimeException (NullPointerException)
 * 
 * Q2: Can finally block be skipped?
 * A2: Only in these cases:
 *     - System.exit() called
 *     - JVM crashes
 *     - Infinite loop in try/catch
 *     - Thread killed
 * 
 * Q3: throw vs throws?
 * A3: throw: Actually throws exception (inside method)
 *     throws: Declares possible exceptions (method signature)
 * 
 * Q4: Can we have try without catch?
 * A4: Yes, with finally: try { } finally { }
 *     Or with resources: try (Resource r = ...) { }
 * 
 * Q5: What happens if exception in finally?
 * A5: Original exception is suppressed, finally exception is thrown.
 *     Bad practice! Avoid throwing from finally.
 * 
 * Q6: Error vs Exception?
 * A6: Error: Serious, unrecoverable (OutOfMemoryError), don't catch
 *     Exception: Recoverable, should handle
 */
