# Java Control Flow - Complete Guide
## Part 2: If-Else, Switch, Loops, Break, Continue

---

# 1. Conditional Statements

## 1.1 if Statement

```java
int age = 18;

// Simple if
if (age >= 18) {
    System.out.println("You are an adult");
}

// Single line (not recommended)
if (age >= 18) System.out.println("Adult");
```

## 1.2 if-else Statement

```java
int score = 75;

if (score >= 60) {
    System.out.println("PASS");
} else {
    System.out.println("FAIL");
}
```

## 1.3 if-else-if Ladder

```java
int score = 85;
String grade;

if (score >= 90) {
    grade = "A";
} else if (score >= 80) {
    grade = "B";
} else if (score >= 70) {
    grade = "C";
} else if (score >= 60) {
    grade = "D";
} else {
    grade = "F";
}

System.out.println("Grade: " + grade);  // B
```

## 1.4 Nested if

```java
int age = 25;
boolean hasLicense = true;

if (age >= 18) {
    if (hasLicense) {
        System.out.println("Can drive");
    } else {
        System.out.println("Get a license first");
    }
} else {
    System.out.println("Too young to drive");
}
```

---

# 2. Switch Statements

## 2.1 Classic Switch Statement

```java
int day = 3;
String dayName;

switch (day) {
    case 1:
        dayName = "Monday";
        break;
    case 2:
        dayName = "Tuesday";
        break;
    case 3:
        dayName = "Wednesday";
        break;
    case 4:
        dayName = "Thursday";
        break;
    case 5:
        dayName = "Friday";
        break;
    case 6:
    case 7:  // Fall-through for weekend
        dayName = "Weekend";
        break;
    default:
        dayName = "Invalid";
}

System.out.println(dayName);  // Wednesday
```

## 2.2 Switch with Strings (Java 7+)

```java
String command = "START";

switch (command) {
    case "START":
        System.out.println("Starting...");
        break;
    case "STOP":
        System.out.println("Stopping...");
        break;
    case "PAUSE":
        System.out.println("Pausing...");
        break;
    default:
        System.out.println("Unknown command");
}
```

## 2.3 Switch with Enums

```java
enum Status { ACTIVE, INACTIVE, PENDING }

Status status = Status.ACTIVE;

switch (status) {
    case ACTIVE:
        System.out.println("Account is active");
        break;
    case INACTIVE:
        System.out.println("Account is inactive");
        break;
    case PENDING:
        System.out.println("Account is pending");
        break;
}
```

## 2.4 Switch Expression with Arrow (Java 14+)

```java
int day = 3;

// Returns a value - no break needed!
String dayName = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 3 -> "Wednesday";
    case 4 -> "Thursday";
    case 5 -> "Friday";
    case 6, 7 -> "Weekend";  // Multiple labels
    default -> "Invalid";
};

System.out.println(dayName);  // Wednesday

// With blocks
String result = switch (day) {
    case 1, 2, 3, 4, 5 -> {
        System.out.println("It's a weekday");
        yield "Weekday";  // yield returns value from block
    }
    case 6, 7 -> {
        System.out.println("It's weekend!");
        yield "Weekend";
    }
    default -> "Invalid";
};
```

## 2.5 Switch with yield (Java 14+)

```java
int day = 3;

String type = switch (day) {
    case 1, 2, 3, 4, 5 -> {
        // Complex logic
        boolean isMonday = (day == 1);
        yield isMonday ? "Start of week" : "Weekday";
    }
    case 6, 7 -> "Weekend";
    default -> throw new IllegalArgumentException("Invalid day");
};
```

## 2.6 Switch Pattern Matching (Java 21)

```java
Object obj = "Hello";

String result = switch (obj) {
    case Integer i -> "Integer: " + i;
    case String s -> "String of length " + s.length();
    case Double d -> "Double: " + d;
    case null -> "Null value";
    default -> "Unknown type";
};

// With guards (when clause)
String describe = switch (obj) {
    case String s when s.isEmpty() -> "Empty string";
    case String s when s.length() > 10 -> "Long string";
    case String s -> "String: " + s;
    case Integer i when i < 0 -> "Negative integer";
    case Integer i -> "Positive integer";
    default -> "Other";
};
```

---

# 3. Loops

## 3.1 for Loop

```java
// Basic for loop
for (int i = 0; i < 5; i++) {
    System.out.println(i);  // 0, 1, 2, 3, 4
}

// Decrementing
for (int i = 5; i > 0; i--) {
    System.out.println(i);  // 5, 4, 3, 2, 1
}

// Step by 2
for (int i = 0; i <= 10; i += 2) {
    System.out.println(i);  // 0, 2, 4, 6, 8, 10
}

// Multiple variables
for (int i = 0, j = 10; i < j; i++, j--) {
    System.out.println("i=" + i + ", j=" + j);
}

// Infinite loop
// for (;;) { }

// Empty parts
int k = 0;
for (; k < 5; ) {
    System.out.println(k++);
}
```

## 3.2 Enhanced for-each Loop (Java 5+)

```java
// Array
int[] numbers = {1, 2, 3, 4, 5};
for (int num : numbers) {
    System.out.println(num);
}

// List
List<String> names = List.of("Alice", "Bob", "Charlie");
for (String name : names) {
    System.out.println(name);
}

// Map (iterate entries)
Map<String, Integer> ages = Map.of("Alice", 25, "Bob", 30);
for (Map.Entry<String, Integer> entry : ages.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// With var (Java 10+)
for (var name : names) {
    System.out.println(name);
}
```

## 3.3 while Loop

```java
int count = 0;

while (count < 5) {
    System.out.println(count);
    count++;
}

// Reading until condition
Scanner scanner = new Scanner(System.in);
String input = "";
while (!input.equals("quit")) {
    input = scanner.nextLine();
    System.out.println("You entered: " + input);
}

// Infinite loop
// while (true) { }
```

## 3.4 do-while Loop

```java
int count = 0;

// Executes at least once!
do {
    System.out.println(count);
    count++;
} while (count < 5);

// Menu example
int choice;
do {
    System.out.println("1. Option A");
    System.out.println("2. Option B");
    System.out.println("0. Exit");
    choice = scanner.nextInt();
    processChoice(choice);
} while (choice != 0);
```

## 3.5 while vs do-while

```java
int x = 10;

// while: may never execute
while (x < 5) {
    System.out.println("while: " + x);  // Never prints
}

// do-while: executes at least once
do {
    System.out.println("do-while: " + x);  // Prints "10"
} while (x < 5);
```

---

# 4. Jump Statements

## 4.1 break Statement

```java
// Exit loop
for (int i = 0; i < 10; i++) {
    if (i == 5) {
        break;  // Exit loop when i is 5
    }
    System.out.println(i);  // 0, 1, 2, 3, 4
}

// Exit switch (shown earlier)

// Find first match
String[] names = {"Alice", "Bob", "Charlie"};
for (String name : names) {
    if (name.startsWith("B")) {
        System.out.println("Found: " + name);
        break;
    }
}
```

## 4.2 Labeled break

```java
// Break out of nested loops
outer:
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 1) {
            break outer;  // Breaks both loops!
        }
        System.out.println("i=" + i + ", j=" + j);
    }
}

// Output:
// i=0, j=0
// i=0, j=1
// i=0, j=2
// i=1, j=0
// (stops at i=1, j=1)

// Search in 2D array
int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
int target = 5;
boolean found = false;

search:
for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[i].length; j++) {
        if (matrix[i][j] == target) {
            System.out.println("Found at [" + i + "][" + j + "]");
            found = true;
            break search;
        }
    }
}
```

## 4.3 continue Statement

```java
// Skip current iteration
for (int i = 0; i < 10; i++) {
    if (i % 2 == 0) {
        continue;  // Skip even numbers
    }
    System.out.println(i);  // 1, 3, 5, 7, 9
}

// Skip invalid entries
String[] names = {"Alice", "", "Bob", null, "Charlie"};
for (String name : names) {
    if (name == null || name.isEmpty()) {
        continue;
    }
    System.out.println(name);  // Alice, Bob, Charlie
}
```

## 4.4 Labeled continue

```java
// Continue outer loop
outer:
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (j == 1) {
            continue outer;  // Skip rest of inner loop
        }
        System.out.println("i=" + i + ", j=" + j);
    }
}

// Output:
// i=0, j=0
// i=1, j=0
// i=2, j=0
// (skips j=1 and j=2 for each i)
```

## 4.5 return Statement

```java
// Exit method
public void process(int value) {
    if (value < 0) {
        return;  // Early exit, no return value
    }
    System.out.println("Processing: " + value);
}

// Return with value
public int square(int n) {
    return n * n;
}

// Multiple return points
public String getGrade(int score) {
    if (score >= 90) return "A";
    if (score >= 80) return "B";
    if (score >= 70) return "C";
    if (score >= 60) return "D";
    return "F";
}
```

---

# 5. Assert Statement (Java 1.4+)

```java
// Must enable with -ea flag: java -ea MyProgram

public void withdraw(double amount) {
    assert amount > 0 : "Amount must be positive";
    assert amount <= balance : "Insufficient funds: " + amount;
    
    balance -= amount;
}

// Simple assert
int age = -5;
assert age >= 0;  // AssertionError if false

// Assert with message
assert age >= 0 : "Age cannot be negative: " + age;

// Use for:
// - Internal invariants
// - Control flow invariants
// - Preconditions (in private methods)
// - Postconditions
// - Class invariants

// DON'T use for:
// - Argument checking in public methods
// - Operations with side effects
```

---

# 6. Control Flow Examples

## 6.1 FizzBuzz

```java
for (int i = 1; i <= 100; i++) {
    if (i % 15 == 0) {
        System.out.println("FizzBuzz");
    } else if (i % 3 == 0) {
        System.out.println("Fizz");
    } else if (i % 5 == 0) {
        System.out.println("Buzz");
    } else {
        System.out.println(i);
    }
}
```

## 6.2 Prime Number Check

```java
public boolean isPrime(int n) {
    if (n <= 1) return false;
    if (n <= 3) return true;
    if (n % 2 == 0 || n % 3 == 0) return false;
    
    for (int i = 5; i * i <= n; i += 6) {
        if (n % i == 0 || n % (i + 2) == 0) {
            return false;
        }
    }
    return true;
}
```

## 6.3 Find Element in 2D Array

```java
public int[] findElement(int[][] matrix, int target) {
    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[i].length; j++) {
            if (matrix[i][j] == target) {
                return new int[]{i, j};
            }
        }
    }
    return new int[]{-1, -1};  // Not found
}
```

## 6.4 Menu-Driven Program

```java
Scanner scanner = new Scanner(System.in);
boolean running = true;

while (running) {
    System.out.println("\n=== MENU ===");
    System.out.println("1. Add");
    System.out.println("2. Remove");
    System.out.println("3. Display");
    System.out.println("0. Exit");
    System.out.print("Choice: ");
    
    int choice = scanner.nextInt();
    
    switch (choice) {
        case 1 -> add();
        case 2 -> remove();
        case 3 -> display();
        case 0 -> {
            running = false;
            System.out.println("Goodbye!");
        }
        default -> System.out.println("Invalid choice!");
    }
}
```

---

# Summary

| Statement | Purpose | Key Points |
|-----------|---------|------------|
| if/else | Conditional branching | Most flexible |
| switch | Multiple exact matches | Use arrows (Java 14+) |
| for | Known iterations | Has init, condition, update |
| for-each | Iterate collections | Read-only, simpler |
| while | Unknown iterations | Check before loop |
| do-while | At least one execution | Check after loop |
| break | Exit loop/switch | Can use labels |
| continue | Skip iteration | Can use labels |
| return | Exit method | Can return value |
| assert | Debug checks | Enable with -ea |
