# Java Exception Handling - Complete Guide
## Part 5: Exceptions, Try-Catch, Throw, Throws

---

# 1. Exception Hierarchy

```
java.lang.Throwable
├── java.lang.Error (Unrecoverable - don't catch)
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   ├── VirtualMachineError
│   └── NoClassDefFoundError
│
└── java.lang.Exception (Recoverable - can catch)
    ├── RuntimeException (Unchecked)
    │   ├── NullPointerException
    │   ├── ArrayIndexOutOfBoundsException
    │   ├── ArithmeticException
    │   ├── ClassCastException
    │   ├── IllegalArgumentException
    │   ├── IllegalStateException
    │   └── NumberFormatException
    │
    └── Checked Exceptions (Must handle)
        ├── IOException
        ├── FileNotFoundException
        ├── SQLException
        ├── ClassNotFoundException
        └── InterruptedException
```

---

# 2. Checked vs Unchecked Exceptions

## 2.1 Checked Exceptions

```java
// MUST be caught or declared with throws
public void readFile() throws IOException {
    FileReader reader = new FileReader("file.txt");  // May throw IOException
    reader.read();
}

// Or catch it
public void readFileSafe() {
    try {
        FileReader reader = new FileReader("file.txt");
        reader.read();
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }
}
```

## 2.2 Unchecked Exceptions (Runtime)

```java
// Don't need to declare - happen at runtime
public void divide(int a, int b) {
    int result = a / b;  // ArithmeticException if b is 0
}

public void accessArray(int[] arr, int index) {
    int value = arr[index];  // ArrayIndexOutOfBoundsException
}

public void useObject(String s) {
    int len = s.length();  // NullPointerException if s is null
}
```

---

# 3. Try-Catch-Finally

## 3.1 Basic Try-Catch

```java
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Cannot divide by zero!");
    System.out.println("Error: " + e.getMessage());
}
```

## 3.2 Multiple Catch Blocks

```java
public void processData(String input) {
    try {
        int number = Integer.parseInt(input);
        int[] arr = new int[5];
        arr[number] = 10;
    } catch (NumberFormatException e) {
        System.out.println("Invalid number format: " + input);
    } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("Index out of bounds");
    } catch (Exception e) {
        // Catch-all for any other exception
        System.out.println("Unexpected error: " + e.getMessage());
    }
}
// Order matters! More specific exceptions first!
```

## 3.3 Multi-Catch (Java 7+)

```java
try {
    // some code
} catch (IOException | SQLException e) {
    // Handle both the same way
    System.out.println("Error: " + e.getMessage());
}

// Cannot use if one is subclass of another:
// catch (FileNotFoundException | IOException e)  // ❌ Compile error!
```

## 3.4 Finally Block

```java
FileReader reader = null;
try {
    reader = new FileReader("file.txt");
    // Process file
} catch (IOException e) {
    System.out.println("Error: " + e.getMessage());
} finally {
    // ALWAYS executes (even if exception or return)
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## 3.5 Finally Execution Rules

```java
// Finally runs even with return
public int testFinally() {
    try {
        return 1;
    } finally {
        System.out.println("Finally runs!");  // Still prints!
    }
}

// Finally runs even with exception
public void testFinally2() {
    try {
        throw new RuntimeException();
    } finally {
        System.out.println("Finally runs!");  // Still prints!
    }
}

// Only case finally doesn't run:
// System.exit(0);  - terminates JVM immediately
```

---

# 4. Try-With-Resources (Java 7+)

## 4.1 Basic Usage

```java
// Resources auto-closed!
try (FileReader reader = new FileReader("file.txt");
     BufferedReader br = new BufferedReader(reader)) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}  // reader and br automatically closed!
// No need for finally block!
```

## 4.2 Multiple Resources

```java
try (FileInputStream fis = new FileInputStream("source.txt");
     FileOutputStream fos = new FileOutputStream("dest.txt");
     BufferedInputStream bis = new BufferedInputStream(fis);
     BufferedOutputStream bos = new BufferedOutputStream(fos)) {
    
    int data;
    while ((data = bis.read()) != -1) {
        bos.write(data);
    }
}  // All resources closed in reverse order
```

## 4.3 Custom AutoCloseable

```java
class DatabaseConnection implements AutoCloseable {
    private String name;
    
    public DatabaseConnection(String name) {
        this.name = name;
        System.out.println("Opening connection: " + name);
    }
    
    public void query(String sql) {
        System.out.println("Executing: " + sql);
    }
    
    @Override
    public void close() {
        System.out.println("Closing connection: " + name);
    }
}

// Usage
try (DatabaseConnection conn = new DatabaseConnection("mydb")) {
    conn.query("SELECT * FROM users");
}  // Auto-calls close()
```

## 4.4 Effectively Final Variables (Java 9+)

```java
// Before Java 9 - resource must be declared in try
BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
try (BufferedReader br = reader) {
    // ...
}

// Java 9+ - can use effectively final variable
BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
try (reader) {  // reader is effectively final
    // ...
}
```

---

# 5. throw Statement

## 5.1 Throwing Exceptions

```java
public void withdraw(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive");
    }
    if (amount > balance) {
        throw new IllegalStateException("Insufficient funds");
    }
    balance -= amount;
}
```

## 5.2 Re-throwing Exceptions

```java
public void process() {
    try {
        riskyOperation();
    } catch (Exception e) {
        System.out.println("Logging error: " + e.getMessage());
        throw e;  // Re-throw same exception
    }
}

// Wrap in different exception
public void processWrapped() throws ServiceException {
    try {
        riskyOperation();
    } catch (Exception e) {
        throw new ServiceException("Processing failed", e);  // Chain exceptions
    }
}
```

---

# 6. throws Declaration

## 6.1 Declaring Checked Exceptions

```java
public void readFile(String path) throws IOException, FileNotFoundException {
    FileReader reader = new FileReader(path);
    reader.read();
}

// Caller must handle or declare
public void caller() throws IOException {
    readFile("data.txt");  // Propagates exception
}

// Or catch it
public void callerSafe() {
    try {
        readFile("data.txt");
    } catch (IOException e) {
        handleError(e);
    }
}
```

## 6.2 throws vs throw

```java
// throws - DECLARES that method might throw
public void read() throws IOException {
    // ...
}

// throw - ACTUALLY throws an exception
public void validate(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }
}
```

---

# 7. Custom Exceptions

## 7.1 Custom Checked Exception

```java
public class InsufficientFundsException extends Exception {
    private double amount;
    private double balance;
    
    public InsufficientFundsException(String message) {
        super(message);
    }
    
    public InsufficientFundsException(double amount, double balance) {
        super(String.format("Cannot withdraw %.2f, balance is %.2f", amount, balance));
        this.amount = amount;
        this.balance = balance;
    }
    
    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public double getAmount() { return amount; }
    public double getBalance() { return balance; }
}

// Usage - must be caught or declared
public void withdraw(double amount) throws InsufficientFundsException {
    if (amount > balance) {
        throw new InsufficientFundsException(amount, balance);
    }
    balance -= amount;
}
```

## 7.2 Custom Unchecked Exception

```java
public class InvalidAccountException extends RuntimeException {
    private String accountNumber;
    
    public InvalidAccountException(String message) {
        super(message);
    }
    
    public InvalidAccountException(String message, String accountNumber) {
        super(message + ": " + accountNumber);
        this.accountNumber = accountNumber;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
}

// Usage - no need to declare
public void processAccount(String accountNumber) {
    if (!isValid(accountNumber)) {
        throw new InvalidAccountException("Invalid account", accountNumber);
    }
}
```

---

# 8. Exception Methods

## 8.1 Throwable Methods

```java
try {
    throw new Exception("Something went wrong");
} catch (Exception e) {
    // Get message
    String msg = e.getMessage();  // "Something went wrong"
    
    // Get full string representation
    String str = e.toString();  // java.lang.Exception: Something went wrong
    
    // Print stack trace
    e.printStackTrace();
    
    // Get stack trace as array
    StackTraceElement[] stack = e.getStackTrace();
    for (StackTraceElement element : stack) {
        System.out.println(element.getClassName() + "." + 
                          element.getMethodName() + ":" + 
                          element.getLineNumber());
    }
    
    // Get cause
    Throwable cause = e.getCause();
}
```

## 8.2 Exception Chaining

```java
public void outerMethod() throws ServiceException {
    try {
        innerMethod();
    } catch (SQLException e) {
        throw new ServiceException("Database error", e);  // Chain cause
    }
}

// Get root cause
public Throwable getRootCause(Throwable e) {
    Throwable cause = e;
    while (cause.getCause() != null) {
        cause = cause.getCause();
    }
    return cause;
}
```

## 8.3 Suppressed Exceptions

```java
try (Resource r1 = new Resource("1");
     Resource r2 = new Resource("2")) {
    // If exception here...
}
// And exception in close()...
// close() exception becomes SUPPRESSED

catch (Exception e) {
    // Get suppressed exceptions
    Throwable[] suppressed = e.getSuppressed();
    for (Throwable t : suppressed) {
        System.out.println("Suppressed: " + t.getMessage());
    }
}
```

---

# 9. Common Exceptions

## 9.1 NullPointerException

```java
String str = null;
int len = str.length();  // NullPointerException!

// Prevention
if (str != null) {
    int len = str.length();
}

// Or use Optional (Java 8+)
Optional.ofNullable(str)
    .ifPresent(s -> System.out.println(s.length()));

// Java 14+ - Helpful NPE messages
// Exception in thread "main" java.lang.NullPointerException: 
// Cannot invoke "String.length()" because "str" is null
```

## 9.2 ArrayIndexOutOfBoundsException

```java
int[] arr = {1, 2, 3};
int value = arr[5];  // ArrayIndexOutOfBoundsException!

// Prevention
if (index >= 0 && index < arr.length) {
    int value = arr[index];
}
```

## 9.3 ClassCastException

```java
Object obj = "Hello";
Integer num = (Integer) obj;  // ClassCastException!

// Prevention - use instanceof
if (obj instanceof Integer i) {
    System.out.println(i);
}
```

## 9.4 NumberFormatException

```java
String str = "abc";
int num = Integer.parseInt(str);  // NumberFormatException!

// Prevention
try {
    int num = Integer.parseInt(str);
} catch (NumberFormatException e) {
    System.out.println("Invalid number: " + str);
}
```

## 9.5 IllegalArgumentException

```java
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age: " + age);
    }
    this.age = age;
}
```

## 9.6 IllegalStateException

```java
public void withdraw(double amount) {
    if (isClosed) {
        throw new IllegalStateException("Account is closed");
    }
    // proceed with withdrawal
}
```

---

# 10. Best Practices

## 10.1 DO's ✅

```java
// 1. Catch specific exceptions
try {
    // code
} catch (FileNotFoundException e) {
    // Handle file not found
} catch (IOException e) {
    // Handle other IO errors
}

// 2. Use try-with-resources for closeable resources
try (Connection conn = getConnection()) {
    // use connection
}

// 3. Include useful information in exception messages
throw new IllegalArgumentException(
    "Customer ID must be positive, but was: " + customerId);

// 4. Log exceptions properly
catch (Exception e) {
    logger.error("Failed to process customer: " + customerId, e);
}

// 5. Clean up resources in finally or use try-with-resources
```

## 10.2 DON'Ts ❌

```java
// 1. ❌ Don't catch generic Exception/Throwable
catch (Exception e) { }  // Too broad

// 2. ❌ Don't swallow exceptions
catch (Exception e) { }  // Empty catch - bad!

// 3. ❌ Don't use exceptions for flow control
try {
    while (true) {
        array[i++] = 0;  // Using exception to end loop - bad!
    }
} catch (ArrayIndexOutOfBoundsException e) { }

// 4. ❌ Don't catch and just log without handling
catch (Exception e) {
    e.printStackTrace();  // Then what?
}

// 5. ❌ Don't throw Exception - be specific
throw new Exception("Error");  // Too generic
```

---

# Summary Table

| Exception Type | Must Handle? | Example |
|----------------|--------------|---------|
| Error | No (unrecoverable) | OutOfMemoryError |
| Checked Exception | Yes | IOException, SQLException |
| Runtime Exception | No (but should) | NullPointerException |

| Keyword | Purpose |
|---------|---------|
| try | Define code that might throw |
| catch | Handle specific exception |
| finally | Code that always runs |
| throw | Actually throw exception |
| throws | Declare method might throw |
