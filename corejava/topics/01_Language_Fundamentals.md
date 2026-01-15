# Java Language Fundamentals - Complete Guide
## Part 1: Lexical Elements, Data Types, Variables & Operators

---

# 1. Lexical Elements

## 1.1 Unicode Character Set
Java uses Unicode to represent characters, supporting all world languages.

```java
// Unicode examples
char heart = '\u2665';      // ♥
char smiley = '\u263A';     // ☺
char rupee = '\u20B9';      // ₹
String japanese = "こんにちは";  // Hello in Japanese
String emoji = "😀";        // Emoji (Java 9+)

System.out.println("Heart: " + heart);      // ♥
System.out.println("Rupee: " + rupee);      // ₹
```

## 1.2 Escape Sequences
Special character sequences that represent non-printable or special characters.

```java
// All escape sequences
System.out.println("Newline:\nNext line");
System.out.println("Tab:\tIndented");
System.out.println("Carriage return:\rOverwrite");
System.out.println("Backslash: \\");
System.out.println("Single quote: \'");
System.out.println("Double quote: \"");
System.out.println("Backspace: abc\bd");        // abd
System.out.println("Form feed: page1\fpage2");
System.out.println("Unicode: \u0041");          // A

// Output:
// Newline:
// Next line
// Tab:    Indented
// Backslash: \
// Single quote: '
// Double quote: "
```

| Escape | Meaning |
|--------|---------|
| `\n` | Newline |
| `\t` | Tab |
| `\r` | Carriage return |
| `\\` | Backslash |
| `\'` | Single quote |
| `\"` | Double quote |
| `\b` | Backspace |
| `\f` | Form feed |
| `\uXXXX` | Unicode character |

## 1.3 Comments

```java
// Single-line comment - one line only

/* Multi-line comment
   Can span multiple lines
   Used for longer explanations */

/**
 * Javadoc comment
 * Used for documentation
 * @param name the name parameter
 * @return greeting message
 * @throws IllegalArgumentException if name is null
 * @since 1.0
 * @author John Doe
 * @see String
 */
public String greet(String name) {
    return "Hello, " + name;
}
```

## 1.4 Tokens
Tokens are the smallest units of a program. Java has 5 types:

```java
// 1. IDENTIFIERS - names for variables, methods, classes
int myVariable;
void calculateTotal() {}
class BankAccount {}

// 2. KEYWORDS - reserved words (53 total)
public class static void int if else

// 3. LITERALS - constant values
42          // integer literal
3.14        // floating-point literal
'A'         // character literal
"Hello"     // string literal
true        // boolean literal
null        // null literal

// 4. OPERATORS - symbols for operations
+ - * / % = == != < > && || !

// 5. SEPARATORS - punctuation
; , . () [] {} @ ::
```

## 1.5 Identifiers - Rules

```java
// ✅ VALID identifiers
int age;
int _age;
int $age;
int Age123;
int _123;
int こんにちは;   // Unicode allowed!

// ❌ INVALID identifiers
// int 123age;    // Cannot start with digit
// int my-age;    // Hyphen not allowed
// int my age;    // Space not allowed
// int class;     // Cannot use keyword
// int my#age;    // Special chars not allowed (except _ and $)
```

## 1.6 All 53 Keywords

```java
// Access modifiers (3)
public    protected    private

// Class/Interface/Method modifiers (9)
abstract    static    final    native    strictfp
synchronized    transient    volatile

// Class-related (11)
class    interface    extends    implements    new
this    super    instanceof    enum    record    sealed
non-sealed    permits

// Control flow (12)
if    else    switch    case    default    break
continue    for    while    do    return    yield

// Exception handling (5)
try    catch    finally    throw    throws

// Primitives (8)
byte    short    int    long    float    double    char    boolean

// Other (5)
void    null    true    false    var

// Package/Import (2)
package    import

// Assertion (1)
assert

// Reserved (not used) (2)
goto    const
```

## 1.7 Literals - All Types

```java
// INTEGER LITERALS
int decimal = 100;           // Decimal (base 10)
int octal = 0144;            // Octal (base 8) - prefix 0
int hex = 0x64;              // Hexadecimal (base 16) - prefix 0x
int binary = 0b1100100;      // Binary (base 2) - prefix 0b (Java 7)
long big = 100L;             // Long literal - suffix L

// Underscores in literals (Java 7)
int million = 1_000_000;
long creditCard = 1234_5678_9012_3456L;
int binary2 = 0b1010_1010_1010;

// FLOATING-POINT LITERALS
float f = 3.14f;             // Float - suffix f
double d = 3.14;             // Double (default)
double d2 = 3.14d;           // Double - suffix d
double scientific = 3.14e2;  // Scientific notation = 314.0

// CHARACTER LITERALS
char c1 = 'A';               // Character
char c2 = 65;                // ASCII value
char c3 = '\u0041';          // Unicode

// STRING LITERALS
String s = "Hello, World!";

// TEXT BLOCK (Java 15)
String json = """
    {
        "name": "John",
        "age": 30
    }
    """;

// BOOLEAN LITERALS
boolean t = true;
boolean f = false;

// NULL LITERAL
String nullStr = null;
Object obj = null;
```

---

# 2. Primitive Data Types

## 2.1 byte (8-bit signed integer)

```java
byte b = 100;               // Range: -128 to 127
byte min = Byte.MIN_VALUE;  // -128
byte max = Byte.MAX_VALUE;  // 127

// Use case: Save memory in large arrays, file I/O
byte[] buffer = new byte[1024];

// Overflow example
byte overflow = (byte) 128;  // Becomes -128!
```

## 2.2 short (16-bit signed integer)

```java
short s = 30000;              // Range: -32,768 to 32,767
short min = Short.MIN_VALUE;  // -32768
short max = Short.MAX_VALUE;  // 32767

// Use case: Memory efficiency
short[] audioSamples = new short[44100];
```

## 2.3 int (32-bit signed integer)

```java
int i = 2_147_483_647;        // Range: -2^31 to 2^31-1
int min = Integer.MIN_VALUE;  // -2147483648
int max = Integer.MAX_VALUE;  // 2147483647

// Most commonly used integer type
int count = 0;
int age = 25;
for (int j = 0; j < 10; j++) { }
```

## 2.4 long (64-bit signed integer)

```java
long l = 9_223_372_036_854_775_807L;  // Note: L suffix required
long min = Long.MIN_VALUE;
long max = Long.MAX_VALUE;

// Use cases
long timestamp = System.currentTimeMillis();
long fileSize = 10_000_000_000L;  // 10 GB
long population = 8_000_000_000L;
```

## 2.5 float (32-bit IEEE 754)

```java
float f = 3.14f;              // Note: f suffix required
float min = Float.MIN_VALUE;  // Smallest positive: 1.4E-45
float max = Float.MAX_VALUE;  // 3.4028235E38

// Special values
float posInf = Float.POSITIVE_INFINITY;
float negInf = Float.NEGATIVE_INFINITY;
float nan = Float.NaN;

// ⚠️ WARNING: Precision issues!
float sum = 0.1f + 0.2f;
System.out.println(sum);  // 0.30000001 (not 0.3!)
```

## 2.6 double (64-bit IEEE 754)

```java
double d = 3.14159265358979;  // Default for decimals
double min = Double.MIN_VALUE;
double max = Double.MAX_VALUE;

// More precision than float
double pi = Math.PI;  // 3.141592653589793

// For money, use BigDecimal instead!
// ❌ double money = 0.1 + 0.2;  // Precision issues
// ✅ BigDecimal money = new BigDecimal("0.1").add(new BigDecimal("0.2"));
```

## 2.7 char (16-bit Unicode)

```java
char c = 'A';
char unicode = '\u0041';      // 'A'
char numeric = 65;            // 'A'
char min = Character.MIN_VALUE;  // '\u0000' (0)
char max = Character.MAX_VALUE;  // '\uffff' (65535)

// Character methods
char letter = 'a';
System.out.println(Character.isLetter(letter));      // true
System.out.println(Character.isDigit('5'));          // true
System.out.println(Character.isWhitespace(' '));     // true
System.out.println(Character.toUpperCase('a'));      // 'A'
System.out.println(Character.toLowerCase('A'));      // 'a'
System.out.println(Character.isUpperCase('A'));      // true
System.out.println(Character.isLowerCase('a'));      // true
```

## 2.8 boolean (true/false)

```java
boolean isActive = true;
boolean hasPermission = false;

// Only true or false, never 0 or 1!
// ❌ boolean b = 1;     // Won't compile
// ❌ boolean b = null;  // Won't compile (primitives can't be null)

// Logical operations
boolean a = true, b = false;
System.out.println(a && b);   // false (AND)
System.out.println(a || b);   // true (OR)
System.out.println(!a);       // false (NOT)
```

## 2.9 Primitive Comparison Summary

| Type | Size | Range | Default | Wrapper |
|------|------|-------|---------|---------|
| byte | 8 bits | -128 to 127 | 0 | Byte |
| short | 16 bits | -32,768 to 32,767 | 0 | Short |
| int | 32 bits | -2.1B to 2.1B | 0 | Integer |
| long | 64 bits | -9.2E18 to 9.2E18 | 0L | Long |
| float | 32 bits | ±3.4E38 | 0.0f | Float |
| double | 64 bits | ±1.7E308 | 0.0d | Double |
| char | 16 bits | 0 to 65,535 | '\u0000' | Character |
| boolean | 1 bit | true/false | false | Boolean |

---

# 3. Variables

## 3.1 Instance Variables

```java
public class Person {
    // Instance variables - belong to each object
    String name;        // Default: null
    int age;            // Default: 0
    boolean active;     // Default: false
    
    public static void main(String[] args) {
        Person p1 = new Person();
        Person p2 = new Person();
        
        p1.name = "Alice";
        p2.name = "Bob";
        
        // Each object has its own copy
        System.out.println(p1.name);  // Alice
        System.out.println(p2.name);  // Bob
    }
}
```

## 3.2 Static Variables (Class Variables)

```java
public class BankAccount {
    // Static variable - shared by ALL instances
    static int totalAccounts = 0;
    static double interestRate = 0.05;
    
    // Instance variable
    String accountNumber;
    double balance;
    
    public BankAccount(String number) {
        this.accountNumber = number;
        totalAccounts++;  // Increment shared counter
    }
    
    public static void main(String[] args) {
        BankAccount acc1 = new BankAccount("001");
        BankAccount acc2 = new BankAccount("002");
        BankAccount acc3 = new BankAccount("003");
        
        System.out.println(BankAccount.totalAccounts);  // 3
        System.out.println(acc1.totalAccounts);         // 3 (same)
        
        // Change affects all
        BankAccount.interestRate = 0.06;
        System.out.println(acc1.interestRate);  // 0.06
        System.out.println(acc2.interestRate);  // 0.06
    }
}
```

## 3.3 Local Variables

```java
public void processData() {
    // Local variable - must be initialized before use!
    int count;
    // System.out.println(count);  // ❌ Compile error!
    
    count = 0;  // Initialize
    System.out.println(count);  // ✅ OK
    
    // Scope: only within this method
    for (int i = 0; i < 10; i++) {
        int temp = i * 2;  // Local to loop
    }
    // System.out.println(temp);  // ❌ Out of scope
}
```

## 3.4 Parameter Variables

```java
public void greet(String name, int times) {
    // 'name' and 'times' are parameter variables
    for (int i = 0; i < times; i++) {
        System.out.println("Hello, " + name);
    }
}

// Varargs (variable arguments)
public void printAll(String... messages) {
    for (String msg : messages) {
        System.out.println(msg);
    }
}

public static void main(String[] args) {
    printAll("A", "B", "C");  // Pass any number of arguments
}
```

## 3.5 Final Variables (Constants)

```java
public class Constants {
    // Final instance variable - must be set once
    final String id;
    
    // Final static variable - true constant
    static final double PI = 3.14159;
    static final String APP_NAME = "MyApp";
    
    // Blank final - set in constructor
    public Constants(String id) {
        this.id = id;  // Can only be set once!
    }
    
    public void method() {
        final int localConst = 10;
        // localConst = 20;  // ❌ Cannot reassign
        
        // Final reference - object can be modified
        final List<String> list = new ArrayList<>();
        list.add("Hello");  // ✅ OK - modifying content
        // list = new ArrayList<>();  // ❌ Cannot reassign reference
    }
}
```

## 3.6 Effectively Final Variables (Java 8)

```java
public void processList() {
    int multiplier = 2;  // Not declared final, but never modified
    
    // Can use in lambda because it's "effectively final"
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);
    numbers.stream()
        .map(n -> n * multiplier)  // ✅ OK
        .forEach(System.out::println);
    
    // multiplier = 3;  // ❌ Would make it non-effectively final
}
```

## 3.7 Volatile Variables

```java
public class SharedFlag {
    // Volatile ensures visibility across threads
    private volatile boolean running = true;
    
    public void stop() {
        running = false;  // Visible to all threads immediately
    }
    
    public void run() {
        while (running) {  // Always reads from main memory
            // Do work
        }
    }
}
```

## 3.8 Transient Variables

```java
import java.io.Serializable;

public class User implements Serializable {
    String username;
    transient String password;  // NOT serialized!
    transient int sessionId;    // NOT serialized!
    
    // When deserialized:
    // - username: restored from file
    // - password: null (default)
    // - sessionId: 0 (default)
}
```

## 3.9 Variable Scope

```java
public class ScopeDemo {
    static int classLevel = 1;       // Class scope
    int instanceLevel = 2;           // Instance scope
    
    public void method() {
        int methodLevel = 3;         // Method scope
        
        if (true) {
            int blockLevel = 4;      // Block scope
            System.out.println(blockLevel);
        }
        // blockLevel not accessible here
        
        for (int i = 0; i < 10; i++) {  // Loop scope
            int loopVar = 5;
        }
        // i and loopVar not accessible here
    }
}
```

## 3.10 Variable Shadowing

```java
public class Shadowing {
    int x = 10;  // Instance variable
    
    public void method(int x) {  // Parameter shadows instance var
        System.out.println(x);       // Parameter (local)
        System.out.println(this.x);  // Instance variable
    }
    
    public void innerClass() {
        int x = 20;  // Local variable
        
        new Runnable() {
            int x = 30;  // Inner class variable
            
            public void run() {
                int x = 40;  // Lambda local
                System.out.println(x);                    // 40
                System.out.println(this.x);               // 30
                System.out.println(Shadowing.this.x);     // 10
            }
        }.run();
    }
}
```

## 3.11 var - Local Variable Type Inference (Java 10)

```java
// var infers type from right side
var name = "John";                    // String
var age = 25;                         // int
var price = 19.99;                    // double
var list = new ArrayList<String>();  // ArrayList<String>
var map = Map.of("a", 1, "b", 2);    // Map<String,Integer>

// In for loops
for (var i = 0; i < 10; i++) { }
for (var item : list) { }

// In try-with-resources
try (var reader = new BufferedReader(new FileReader("file.txt"))) { }

// ❌ CANNOT use var:
// var x;                    // No initializer
// var y = null;             // Null type
// var z = () -> "hello";    // Lambda without target type
// var arr = {1, 2, 3};      // Array initializer
// class C { var field; }    // Instance variables
```

---

# 4. Type Conversions

## 4.1 Widening Primitive Conversion (Implicit)

```java
// Automatic - no data loss
byte b = 10;
short s = b;    // byte → short
int i = s;      // short → int
long l = i;     // int → long
float f = l;    // long → float
double d = f;   // float → double

// Widening path:
// byte → short → int → long → float → double
//        char ↗

char c = 'A';
int charAsInt = c;  // char → int (65)
```

## 4.2 Narrowing Primitive Conversion (Explicit Cast)

```java
// Must cast - potential data loss!
double d = 123.456;
float f = (float) d;    // 123.456
long l = (long) d;      // 123 (truncated)
int i = (int) l;        // 123
short s = (short) i;    // 123
byte b = (byte) s;      // 123

// ⚠️ OVERFLOW!
int big = 130;
byte small = (byte) big;  // -126 (overflow!)

// ⚠️ TRUNCATION!
double decimal = 9.99;
int truncated = (int) decimal;  // 9 (not rounded!)
```

## 4.3 Reference Type Conversion

```java
// UPCASTING - implicit (child → parent)
class Animal { }
class Dog extends Animal { }

Dog dog = new Dog();
Animal animal = dog;  // Automatic upcast

// DOWNCASTING - explicit (parent → child)
Animal animal2 = new Dog();
Dog dog2 = (Dog) animal2;  // Must cast

// ⚠️ ClassCastException if wrong type!
Animal cat = new Cat();
// Dog dog3 = (Dog) cat;  // Runtime error!

// Safe downcast with instanceof
if (animal2 instanceof Dog d) {  // Java 16+
    d.bark();
}
```

## 4.4 Boxing and Unboxing

```java
// AUTOBOXING - primitive → wrapper (automatic)
Integer obj = 100;          // int → Integer
Double d = 3.14;            // double → Double
Boolean b = true;           // boolean → Boolean

// UNBOXING - wrapper → primitive (automatic)
int primitive = obj;        // Integer → int
double prim2 = d;           // Double → double

// ⚠️ NullPointerException!
Integer nullObj = null;
// int x = nullObj;  // NPE when unboxing null!

// Manual boxing/unboxing
Integer manual = Integer.valueOf(100);
int manualPrim = manual.intValue();
```

## 4.5 String Conversion

```java
// TO String
int num = 42;
String s1 = String.valueOf(num);     // "42"
String s2 = Integer.toString(num);   // "42"
String s3 = "" + num;                // "42" (concatenation)

// FROM String
int parsed = Integer.parseInt("42");
double parsed2 = Double.parseDouble("3.14");
boolean parsed3 = Boolean.parseBoolean("true");

// ⚠️ NumberFormatException!
// int invalid = Integer.parseInt("abc");
```

---

# 5. Operators - All with Examples

## 5.1 Arithmetic Operators

```java
int a = 10, b = 3;

System.out.println(a + b);   // 13 (Addition)
System.out.println(a - b);   // 7 (Subtraction)
System.out.println(a * b);   // 30 (Multiplication)
System.out.println(a / b);   // 3 (Integer division)
System.out.println(a % b);   // 1 (Modulus/remainder)

// Division with doubles
double x = 10.0, y = 3.0;
System.out.println(x / y);   // 3.3333...

// Increment/Decrement
int c = 5;
System.out.println(c++);     // 5 (post-increment, returns old)
System.out.println(c);       // 6
System.out.println(++c);     // 7 (pre-increment, returns new)
System.out.println(c--);     // 7 (post-decrement)
System.out.println(--c);     // 5 (pre-decrement)
```

## 5.2 Relational Operators

```java
int a = 10, b = 20;

System.out.println(a == b);  // false (equal)
System.out.println(a != b);  // true (not equal)
System.out.println(a > b);   // false (greater than)
System.out.println(a < b);   // true (less than)
System.out.println(a >= b);  // false (greater or equal)
System.out.println(a <= b);  // true (less or equal)

// For objects, use .equals()
String s1 = new String("Hello");
String s2 = new String("Hello");
System.out.println(s1 == s2);      // false (different objects)
System.out.println(s1.equals(s2)); // true (same content)
```

## 5.3 Logical Operators

```java
boolean a = true, b = false;

// AND - both must be true
System.out.println(a && b);  // false
System.out.println(a && a);  // true

// OR - at least one must be true
System.out.println(a || b);  // true
System.out.println(b || b);  // false

// NOT - inverts
System.out.println(!a);      // false
System.out.println(!b);      // true

// Short-circuit evaluation
int x = 5;
boolean result = (x > 10) && (x++ > 0);  // x++ NOT evaluated!
System.out.println(x);  // Still 5

// Non-short-circuit (bitwise on booleans)
result = (x > 10) & (x++ > 0);  // x++ IS evaluated
System.out.println(x);  // 6
```

## 5.4 Bitwise Operators

```java
int a = 5;   // Binary: 0101
int b = 3;   // Binary: 0011

System.out.println(a & b);   // 1  (0001) - AND
System.out.println(a | b);   // 7  (0111) - OR
System.out.println(a ^ b);   // 6  (0110) - XOR
System.out.println(~a);      // -6 (inverts all bits)

// Practical uses
int flags = 0b1010;
int mask = 0b0010;

// Check if bit is set
boolean isSet = (flags & mask) != 0;  // true

// Set a bit
flags = flags | 0b0100;  // 0b1110

// Clear a bit
flags = flags & ~0b0010;  // 0b1100

// Toggle a bit
flags = flags ^ 0b0001;  // 0b1101
```

## 5.5 Shift Operators

```java
int a = 8;   // Binary: 1000

// Left shift - multiply by 2^n
System.out.println(a << 1);   // 16 (10000) - * 2
System.out.println(a << 2);   // 32 (100000) - * 4
System.out.println(a << 3);   // 64 - * 8

// Right shift (signed) - divide by 2^n
System.out.println(a >> 1);   // 4 - / 2
System.out.println(a >> 2);   // 2 - / 4

// Unsigned right shift
int negative = -8;
System.out.println(negative >> 1);   // -4 (preserves sign)
System.out.println(negative >>> 1);  // 2147483644 (fills with 0)

// Practical use: Fast multiply/divide by power of 2
int x = 10;
int doubled = x << 1;    // 20 (faster than x * 2)
int halved = x >> 1;     // 5 (faster than x / 2)
```

## 5.6 Assignment Operators

```java
int a = 10;        // Simple assignment

a += 5;            // a = a + 5 = 15
a -= 3;            // a = a - 3 = 12
a *= 2;            // a = a * 2 = 24
a /= 4;            // a = a / 4 = 6
a %= 4;            // a = a % 4 = 2

// Bitwise compound
a &= 1;            // a = a & 1
a |= 2;            // a = a | 2
a ^= 1;            // a = a ^ 1
a <<= 2;           // a = a << 2
a >>= 1;           // a = a >> 1
a >>>= 1;          // a = a >>> 1

// Type casting built-in!
byte b = 10;
b += 5;            // OK! Equivalent to b = (byte)(b + 5)
// b = b + 5;      // ❌ Error! int cannot be assigned to byte
```

## 5.7 Ternary Operator

```java
int a = 10, b = 20;

// condition ? valueIfTrue : valueIfFalse
int max = (a > b) ? a : b;  // 20
String result = (a % 2 == 0) ? "Even" : "Odd";  // "Even"

// Nested ternary (avoid for readability!)
String grade = (score >= 90) ? "A" :
               (score >= 80) ? "B" :
               (score >= 70) ? "C" :
               (score >= 60) ? "D" : "F";

// Null check
String name = null;
String display = (name != null) ? name : "Unknown";
// Or use: String display = Objects.requireNonNullElse(name, "Unknown");
```

## 5.8 instanceof Operator

```java
// Classic instanceof
Object obj = "Hello";
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// Pattern matching instanceof (Java 16+)
if (obj instanceof String s) {
    System.out.println(s.length());  // s already cast!
}

// With negation
if (!(obj instanceof String s)) {
    return;
}
System.out.println(s.length());  // s in scope here!

// Type hierarchy
class Animal {}
class Dog extends Animal {}

Animal animal = new Dog();
System.out.println(animal instanceof Animal);  // true
System.out.println(animal instanceof Dog);     // true
System.out.println(animal instanceof Object);  // true
```

## 5.9 Other Operators

```java
// new - create object
Object obj = new Object();
int[] arr = new int[10];

// cast - type conversion
double d = 3.14;
int i = (int) d;

// arrow -> for lambda
Runnable r = () -> System.out.println("Hello");
Function<Integer, Integer> square = x -> x * x;

// method reference ::
list.forEach(System.out::println);
Function<String, Integer> len = String::length;

// diamond operator <>
List<String> list = new ArrayList<>();  // Type inferred
Map<String, List<Integer>> map = new HashMap<>();
```

## 5.10 Operator Precedence

```java
// Highest to lowest precedence:
// 1. () [] .
// 2. ++ -- ! ~ (unary)
// 3. * / %
// 4. + -
// 5. << >> >>>
// 6. < <= > >= instanceof
// 7. == !=
// 8. &
// 9. ^
// 10. |
// 11. &&
// 12. ||
// 13. ?:
// 14. = += -= etc.

// Example
int result = 2 + 3 * 4;  // 14 (not 20!)
// Because * has higher precedence than +

// Use parentheses for clarity!
int clear = (2 + 3) * 4;  // 20
```

---

# Summary Table

| Category | Count | Examples |
|----------|-------|----------|
| Escape Sequences | 9 | \n, \t, \r, \\, \', \", \b, \f, \uXXXX |
| Keywords | 53 | public, class, static, void, if, for, etc. |
| Primitives | 8 | byte, short, int, long, float, double, char, boolean |
| Variable Types | 8 | instance, static, local, parameter, final, volatile, transient, var |
| Operators | 40+ | +, -, *, /, ==, &&, \|\|, <<, >>, ?:, instanceof, etc. |

---

*This covers Part 1 of Java Language Fundamentals. See subsequent parts for more topics.*
