# 📋 Java Atomic Topics - Complete Index
## Every Indivisible Topic in Core Java

> This is an exhaustive list of all atomic (cannot be divided further) topics in Java

---

# PART 1: LANGUAGE FUNDAMENTALS

## 1. Lexical Elements
- Unicode Character Set
- ASCII Characters
- Escape Sequences (\n, \t, \r, \\, \', \")
- Unicode Escapes (\uXXXX)
- Comments (Single-line, Multi-line, Javadoc)
- Whitespace
- Tokens
- Identifiers
- Keywords (53 reserved words)
- Literals (Integer, Floating-point, Character, String, Boolean, Null)
- Separators (; , . () [] {} @)
- Operators

## 2. Primitive Data Types
- byte (8-bit signed)
- short (16-bit signed)
- int (32-bit signed)
- long (64-bit signed)
- float (32-bit IEEE 754)
- double (64-bit IEEE 754)
- char (16-bit Unicode)
- boolean (true/false)
- Literal Suffixes (L, F, D)
- Underscores in Numeric Literals (Java 7)
- Binary Literals (0b prefix)
- Octal Literals (0 prefix)
- Hexadecimal Literals (0x prefix)

## 3. Reference Types
- Class Types
- Interface Types
- Array Types
- Enum Types
- Record Types (Java 16)
- Null Type
- Type Erasure
- Raw Types
- Parameterized Types

## 4. Variables
- Instance Variables
- Static Variables (Class Variables)
- Local Variables
- Parameter Variables
- Final Variables
- Effectively Final Variables
- Volatile Variables
- Transient Variables
- Variable Initialization
- Default Values
- Variable Scope
- Variable Shadowing
- Variable Hiding

## 5. Type Conversions
- Widening Primitive Conversion
- Narrowing Primitive Conversion
- Widening Reference Conversion
- Narrowing Reference Conversion
- Boxing Conversion
- Unboxing Conversion
- String Conversion
- Capture Conversion
- Unchecked Conversion
- Casting

## 6. Operators (All Individual)
- Arithmetic: + - * / %
- Unary: + - ++ -- !
- Relational: == != > < >= <=
- Logical: && || !
- Bitwise: & | ^ ~
- Shift: << >> >>>
- Assignment: = += -= *= /= %= &= |= ^= <<= >>= >>>=
- Ternary: ?:
- instanceof Operator
- new Operator
- cast Operator
- Arrow Operator (->)
- Method Reference Operator (::)
- Diamond Operator (<>)

## 7. Control Flow Statements
- if Statement
- if-else Statement
- if-else-if Ladder
- Nested if
- switch Statement (Classic)
- switch Expression (Java 14)
- switch with yield
- switch with Arrow
- switch with Pattern Matching (Java 21)
- for Loop
- Enhanced for-each Loop
- while Loop
- do-while Loop
- break Statement
- Labeled break
- continue Statement
- Labeled continue
- return Statement
- assert Statement

---

# PART 2: OBJECT-ORIENTED PROGRAMMING

## 8. Classes
- Class Declaration
- Class Body
- Class Modifiers (public, abstract, final, strictfp)
- Top-level Classes
- Nested Classes
- Inner Classes (Non-static)
- Static Nested Classes
- Local Classes
- Anonymous Classes
- Class Members

## 9. Fields
- Instance Fields
- Static Fields
- Final Fields
- Blank Final Fields
- Field Initialization
- Field Access
- Field Modifiers
- Field Hiding

## 10. Methods
- Method Declaration
- Method Signature
- Method Body
- Method Modifiers
- Instance Methods
- Static Methods
- Abstract Methods
- Final Methods
- Native Methods
- Synchronized Methods
- Strictfp Methods
- Default Methods (Java 8)
- Private Interface Methods (Java 9)
- Method Parameters
- Varargs (Variable Arguments)
- Method Overloading
- Method Overriding
- Covariant Return Types
- Bridge Methods

## 11. Constructors
- Constructor Declaration
- Default Constructor
- No-argument Constructor
- Parameterized Constructor
- Copy Constructor
- Constructor Overloading
- Constructor Chaining
- this() Call
- super() Call
- Constructor Access Modifiers
- Private Constructors (Singleton)
- Compact Constructors (Records)
- Canonical Constructors (Records)

## 12. Initialization
- Instance Initializer Blocks
- Static Initializer Blocks
- Initialization Order
- Class Loading Order
- Field Initialization Order
- Lazy Initialization
- Double-Checked Locking

## 13. Access Control
- private Modifier
- default (Package-Private) Access
- protected Modifier
- public Modifier
- Access to Members
- Access to Constructors
- Access to Types

## 14. Inheritance
- extends Keyword
- Single Inheritance
- Multilevel Inheritance
- Hierarchical Inheritance
- super Keyword
- super() Constructor Call
- super.method() Call
- Inheritance of Members
- Method Inheritance
- Field Inheritance
- Constructor Not Inherited

## 15. Polymorphism
- Compile-time Polymorphism
- Runtime Polymorphism
- Method Dispatch
- Virtual Method Invocation
- Dynamic Binding
- Static Binding
- Upcasting
- Downcasting
- ClassCastException

## 16. Abstraction
- Abstract Classes
- Abstract Methods
- Concrete Methods in Abstract Class
- Interfaces
- Multiple Inheritance via Interfaces

## 17. Encapsulation
- Data Hiding
- Getter Methods
- Setter Methods
- Immutability
- Defensive Copying

## 18. Interfaces (All Features)
- Interface Declaration
- Interface Methods (Abstract)
- Default Methods (Java 8)
- Static Methods (Java 8)
- Private Methods (Java 9)
- Private Static Methods (Java 9)
- Interface Constants
- Functional Interfaces
- @FunctionalInterface Annotation
- Marker Interfaces
- Tagging Interfaces
- Interface Inheritance
- Multiple Interface Implementation
- Diamond Problem Resolution

## 19. Enums
- Enum Declaration
- Enum Constants
- Enum Constructors
- Enum Methods
- Enum Fields
- Enum Implementing Interfaces
- Enum values() Method
- Enum valueOf() Method
- Enum ordinal() Method
- Enum name() Method
- EnumSet
- EnumMap
- Enum Singleton Pattern

## 20. Records (Java 16)
- Record Declaration
- Record Components
- Canonical Constructor
- Compact Constructor
- Record Methods (Automatic)
- Custom Record Methods
- Record Static Members
- Record Implementing Interfaces
- Local Records
- Nested Records
- Record Patterns (Java 21)

## 21. Sealed Classes (Java 17)
- sealed Modifier
- permits Clause
- final Subclass
- sealed Subclass
- non-sealed Subclass
- Sealed Interfaces
- Exhaustiveness in Switch

---

# PART 3: CORE JAVA CLASSES

## 22. Object Class Methods
- toString()
- equals(Object)
- hashCode()
- getClass()
- clone()
- finalize() (Deprecated)
- wait()
- wait(long)
- wait(long, int)
- notify()
- notifyAll()

## 23. String Operations
- String Creation
- String Pool
- String Interning
- String Immutability
- String Comparison (==, equals, compareTo)
- String Concatenation
- String Methods:
  - length()
  - charAt(int)
  - substring(int)
  - substring(int, int)
  - indexOf(String)
  - lastIndexOf(String)
  - contains(CharSequence)
  - startsWith(String)
  - endsWith(String)
  - replace(char, char)
  - replaceAll(String, String)
  - replaceFirst(String, String)
  - split(String)
  - join(CharSequence, CharSequence...)
  - toLowerCase()
  - toUpperCase()
  - trim()
  - strip() (Java 11)
  - stripLeading() (Java 11)
  - stripTrailing() (Java 11)
  - isBlank() (Java 11)
  - isEmpty()
  - repeat(int) (Java 11)
  - lines() (Java 11)
  - indent(int) (Java 12)
  - transform(Function) (Java 12)
  - formatted(Object...) (Java 15)
  - translateEscapes() (Java 15)

## 24. StringBuilder/StringBuffer
- StringBuilder Creation
- append() Methods
- insert() Methods
- delete(int, int)
- deleteCharAt(int)
- replace(int, int, String)
- reverse()
- setCharAt(int, char)
- setLength(int)
- capacity()
- ensureCapacity(int)
- trimToSize()

## 25. Wrapper Classes
- Byte Methods
- Short Methods
- Integer Methods
- Long Methods
- Float Methods
- Double Methods
- Character Methods
- Boolean Methods
- Autoboxing
- Unboxing
- parseXxx() Methods
- valueOf() Methods
- xxxValue() Methods
- compare() Methods
- MAX_VALUE, MIN_VALUE
- SIZE, BYTES

## 26. Math Class
- abs()
- max()
- min()
- ceil()
- floor()
- round()
- sqrt()
- cbrt()
- pow()
- exp()
- log()
- log10()
- sin(), cos(), tan()
- asin(), acos(), atan()
- sinh(), cosh(), tanh()
- toRadians()
- toDegrees()
- random()
- PI, E constants

## 27. Arrays Utility Class
- Arrays.sort()
- Arrays.parallelSort()
- Arrays.binarySearch()
- Arrays.equals()
- Arrays.deepEquals()
- Arrays.fill()
- Arrays.copyOf()
- Arrays.copyOfRange()
- Arrays.toString()
- Arrays.deepToString()
- Arrays.hashCode()
- Arrays.deepHashCode()
- Arrays.asList()
- Arrays.stream()
- Arrays.compare() (Java 9)
- Arrays.mismatch() (Java 9)

---

# PART 4: EXCEPTION HANDLING

## 28. Exception Types
- Throwable
- Error
- Exception
- RuntimeException
- Checked Exceptions
- Unchecked Exceptions

## 29. Specific Exceptions (Common)
- NullPointerException
- ArrayIndexOutOfBoundsException
- StringIndexOutOfBoundsException
- ClassCastException
- NumberFormatException
- IllegalArgumentException
- IllegalStateException
- ArithmeticException
- UnsupportedOperationException
- ConcurrentModificationException
- NoSuchElementException
- IndexOutOfBoundsException
- IOException
- FileNotFoundException
- EOFException
- SocketException
- SQLException
- ClassNotFoundException
- InterruptedException
- CloneNotSupportedException
- InstantiationException
- NoSuchMethodException
- NoSuchFieldException
- SecurityException
- OutOfMemoryError
- StackOverflowError
- NoClassDefFoundError

## 30. Exception Handling Constructs
- try Block
- catch Block
- finally Block
- throw Statement
- throws Clause
- try-with-resources (Java 7)
- Multi-catch (Java 7)
- More Precise Rethrow (Java 7)
- Suppressed Exceptions
- Exception Chaining
- getCause()
- initCause()
- getSuppressed()
- addSuppressed()
- Automatic Resource Management

---

# PART 5: GENERICS

## 31. Generic Basics
- Generic Classes
- Generic Interfaces
- Generic Methods
- Generic Constructors
- Type Parameters (T, E, K, V, N, ?)
- Type Arguments
- Diamond Operator (<>)
- Raw Types

## 32. Type Bounds
- Upper Bounded Wildcards (? extends T)
- Lower Bounded Wildcards (? super T)
- Unbounded Wildcards (?)
- Multiple Bounds (T extends A & B)
- Recursive Type Bounds

## 33. Generic Restrictions
- Type Erasure
- Cannot Instantiate Type Parameters
- Cannot Create Generic Arrays
- Cannot Use Primitives as Type Arguments
- Cannot Use instanceof with Generics
- Cannot Create Generic Exceptions
- Bridge Methods

---

# PART 6: COLLECTIONS FRAMEWORK

## 34. Collection Interfaces
- Iterable
- Collection
- List
- Set
- SortedSet
- NavigableSet
- Queue
- Deque
- Map
- SortedMap
- NavigableMap
- SequencedCollection (Java 21)
- SequencedSet (Java 21)
- SequencedMap (Java 21)

## 35. List Implementations
- ArrayList
- LinkedList
- Vector
- Stack
- CopyOnWriteArrayList

## 36. Set Implementations
- HashSet
- LinkedHashSet
- TreeSet
- EnumSet
- CopyOnWriteArraySet

## 37. Queue/Deque Implementations
- PriorityQueue
- ArrayDeque
- LinkedList (as Queue)
- ConcurrentLinkedQueue
- LinkedBlockingQueue
- ArrayBlockingQueue
- PriorityBlockingQueue
- DelayQueue
- SynchronousQueue
- LinkedTransferQueue

## 38. Map Implementations
- HashMap
- LinkedHashMap
- TreeMap
- Hashtable
- Properties
- EnumMap
- WeakHashMap
- IdentityHashMap
- ConcurrentHashMap
- ConcurrentSkipListMap

## 39. Collection Methods
- add(), addAll()
- remove(), removeAll(), removeIf()
- contains(), containsAll()
- size(), isEmpty()
- clear()
- iterator()
- toArray()
- retainAll()
- stream(), parallelStream()
- forEach()
- spliterator()

## 40. List-specific Methods
- get(int)
- set(int, E)
- add(int, E)
- remove(int)
- indexOf(), lastIndexOf()
- subList()
- listIterator()
- replaceAll()
- sort()
- reversed() (Java 21)
- getFirst(), getLast() (Java 21)

## 41. Map Methods
- put(), putAll(), putIfAbsent()
- get(), getOrDefault()
- remove()
- containsKey(), containsValue()
- keySet(), values(), entrySet()
- size(), isEmpty(), clear()
- compute(), computeIfAbsent(), computeIfPresent()
- merge()
- replace(), replaceAll()
- forEach()

## 42. Utility Methods
- Collections.sort()
- Collections.reverse()
- Collections.shuffle()
- Collections.binarySearch()
- Collections.min(), max()
- Collections.frequency()
- Collections.disjoint()
- Collections.unmodifiableXxx()
- Collections. synchronizedXxx()
- Collections.emptyXxx()
- Collections.singletonXxx()
- Collections.nCopies()
- Collections.newSetFromMap()

## 43. Factory Methods (Java 9+)
- List.of()
- Set.of()
- Map.of()
- Map.ofEntries()
- Map.entry()
- List.copyOf() (Java 10)
- Set.copyOf() (Java 10)
- Map.copyOf() (Java 10)

---

# PART 7: FUNCTIONAL PROGRAMMING

## 44. Lambda Expressions
- Lambda Syntax (Arrow)
- No Parameter Lambda
- Single Parameter Lambda
- Multiple Parameters Lambda
- Lambda with Types
- Lambda Block Body
- Lambda Return
- Effectively Final Variables
- Target Typing

## 45. Method References
- Static Method Reference (Class::staticMethod)
- Instance Method Reference (obj::method)
- Arbitrary Object Method (Class::instanceMethod)
- Constructor Reference (Class::new)
- Array Constructor Reference (Type[]::new)

## 46. Functional Interfaces
- Predicate<T>
- BiPredicate<T, U>
- Function<T, R>
- BiFunction<T, U, R>
- Consumer<T>
- BiConsumer<T, U>
- Supplier<T>
- UnaryOperator<T>
- BinaryOperator<T>
- IntPredicate, LongPredicate, DoublePredicate
- IntFunction, LongFunction, DoubleFunction
- IntConsumer, LongConsumer, DoubleConsumer
- IntSupplier, LongSupplier, DoubleSupplier
- IntUnaryOperator, LongUnaryOperator, DoubleUnaryOperator
- IntBinaryOperator, LongBinaryOperator, DoubleBinaryOperator
- ToIntFunction, ToLongFunction, ToDoubleFunction
- IntToLongFunction, IntToDoubleFunction
- LongToIntFunction, LongToDoubleFunction
- DoubleToIntFunction, DoubleToLongFunction
- ObjIntConsumer, ObjLongConsumer, ObjDoubleConsumer

## 47. Optional Class
- Optional.of()
- Optional.ofNullable()
- Optional.empty()
- isPresent()
- isEmpty() (Java 11)
- get()
- orElse()
- orElseGet()
- orElseThrow()
- orElseThrow(Supplier) (Java 10)
- ifPresent()
- ifPresentOrElse() (Java 9)
- filter()
- map()
- flatMap()
- or() (Java 9)
- stream() (Java 9)
- OptionalInt, OptionalLong, OptionalDouble

---

# PART 8: STREAM API

## 48. Stream Creation
- Collection.stream()
- Collection.parallelStream()
- Arrays.stream()
- Stream.of()
- Stream.empty()
- Stream.generate()
- Stream.iterate()
- Stream.iterate() with Predicate (Java 9)
- Stream.ofNullable() (Java 9)
- Stream.concat()
- IntStream.range()
- IntStream.rangeClosed()
- Files.lines()
- BufferedReader.lines()
- Pattern.splitAsStream()
- Random.ints(), longs(), doubles()

## 49. Intermediate Operations
- filter()
- map()
- mapToInt(), mapToLong(), mapToDouble()
- flatMap()
- flatMapToInt(), flatMapToLong(), flatMapToDouble()
- distinct()
- sorted()
- sorted(Comparator)
- peek()
- limit()
- skip()
- takeWhile() (Java 9)
- dropWhile() (Java 9)
- boxed()
- asLongStream(), asDoubleStream()
- mapMulti() (Java 16)

## 50. Terminal Operations
- forEach()
- forEachOrdered()
- toArray()
- reduce()
- collect()
- toList() (Java 16)
- min()
- max()
- count()
- sum()
- average()
- summaryStatistics()
- anyMatch()
- allMatch()
- noneMatch()
- findFirst()
- findAny()

## 51. Collectors (All)
- toList()
- toUnmodifiableList()
- toSet()
- toUnmodifiableSet()
- toCollection()
- toMap()
- toUnmodifiableMap()
- toConcurrentMap()
- joining()
- counting()
- summingInt(), summingLong(), summingDouble()
- averagingInt(), averagingLong(), averagingDouble()
- summarizingInt(), summarizingLong(), summarizingDouble()
- reducing()
- groupingBy()
- groupingByConcurrent()
- partitioningBy()
- mapping()
- flatMapping() (Java 9)
- filtering() (Java 9)
- collectingAndThen()
- minBy(), maxBy()
- teeing() (Java 12)

---

# PART 9: DATE & TIME API

## 52. Temporal Classes
- LocalDate
- LocalTime
- LocalDateTime
- ZonedDateTime
- OffsetDateTime
- OffsetTime
- Instant
- Year
- YearMonth
- MonthDay
- Month (Enum)
- DayOfWeek (Enum)

## 53. Duration & Period
- Duration
- Period
- ChronoUnit

## 54. Formatting
- DateTimeFormatter
- FormatStyle
- DateTimeFormatterBuilder
- Predefined Formatters (ISO_DATE, etc.)

## 55. Time Zones
- ZoneId
- ZoneOffset
- ZoneRules
- ZonedDateTime Conversions

## 56. Temporal Operations
- plus(), minus()
- with()
- until()
- isAfter(), isBefore()
- truncatedTo()
- atTime(), atDate(), atZone()
- toLocalDate(), toLocalTime()

---

# PART 10: MULTITHREADING

## 57. Thread Basics
- Thread Class
- Runnable Interface
- Callable Interface
- Thread States
- Thread Priority
- Thread Name
- Daemon Threads
- Thread Groups
- Thread.sleep()
- Thread.yield()
- Thread.join()
- Thread.interrupt()
- Thread.interrupted()
- Thread.isInterrupted()
- Thread.currentThread()
- Thread.isAlive()
- Thread.start() vs run()

## 58. Synchronization
- synchronized Keyword
- synchronized Methods
- synchronized Blocks
- Static synchronized
- Intrinsic Locks
- Monitor
- Mutex
- Object.wait()
- Object.notify()
- Object.notifyAll()
- Volatile Keyword
- Memory Visibility
- Happens-Before Relationship

## 59. Locks (java.util.concurrent.locks)
- Lock Interface
- ReentrantLock
- ReentrantReadWriteLock
- ReadLock
- WriteLock
- Condition
- StampedLock (Java 8)
- lock()
- tryLock()
- lockInterruptibly()
- unlock()
- newCondition()
- await()
- signal()
- signalAll()

## 60. Executors
- Executor Interface
- ExecutorService Interface
- ScheduledExecutorService
- Executors.newFixedThreadPool()
- Executors.newCachedThreadPool()
- Executors.newSingleThreadExecutor()
- Executors.newScheduledThreadPool()
- Executors.newWorkStealingPool()
- Executors.newVirtualThreadPerTaskExecutor() (Java 21)
- ThreadPoolExecutor
- ScheduledThreadPoolExecutor
- ForkJoinPool
- submit()
- execute()
- invokeAll()
- invokeAny()
- shutdown()
- shutdownNow()
- awaitTermination()

## 61. Future & CompletableFuture
- Future Interface
- Future.get()
- Future.get(timeout)
- Future.cancel()
- Future.isDone()
- Future.isCancelled()
- CompletableFuture
- supplyAsync()
- runAsync()
- thenApply()
- thenAccept()
- thenRun()
- thenCompose()
- thenCombine()
- allOf()
- anyOf()
- exceptionally()
- handle()
- whenComplete()
- complete()
- completeExceptionally()
- join()
- getNow()
- orTimeout() (Java 9)
- completeOnTimeout() (Java 9)

## 62. Atomic Classes
- AtomicInteger
- AtomicLong
- AtomicBoolean
- AtomicReference
- AtomicIntegerArray
- AtomicLongArray
- AtomicReferenceArray
- AtomicMarkableReference
- AtomicStampedReference
- LongAdder
- LongAccumulator
- DoubleAdder
- DoubleAccumulator
- get(), set()
- getAndSet()
- compareAndSet()
- incrementAndGet()
- getAndIncrement()
- decrementAndGet()
- addAndGet()
- getAndAdd()

## 63. Concurrent Collections
- ConcurrentHashMap
- ConcurrentSkipListMap
- ConcurrentSkipListSet
- CopyOnWriteArrayList
- CopyOnWriteArraySet
- ConcurrentLinkedQueue
- ConcurrentLinkedDeque
- BlockingQueue
- LinkedBlockingQueue
- ArrayBlockingQueue
- PriorityBlockingQueue
- DelayQueue
- SynchronousQueue
- LinkedTransferQueue
- BlockingDeque
- LinkedBlockingDeque

## 64. Synchronizers
- CountDownLatch
- CyclicBarrier
- Semaphore
- Exchanger
- Phaser
- await()
- countDown()
- acquire()
- release()
- exchange()
- arriveAndAwaitAdvance()
- arriveAndDeregister()

## 65. Virtual Threads (Java 21)
- Thread.startVirtualThread()
- Thread.ofVirtual()
- Thread.isVirtual()
- VirtualThreadPerTaskExecutor
- Carrier Thread
- Pinning
- Mounting/Unmounting

---

# PART 11: I/O & NIO

## 66. Byte Streams
- InputStream
- OutputStream
- FileInputStream
- FileOutputStream
- BufferedInputStream
- BufferedOutputStream
- ByteArrayInputStream
- ByteArrayOutputStream
- DataInputStream
- DataOutputStream
- ObjectInputStream
- ObjectOutputStream
- PipedInputStream
- PipedOutputStream
- FilterInputStream
- FilterOutputStream
- PushbackInputStream
- SequenceInputStream

## 67. Character Streams
- Reader
- Writer
- FileReader
- FileWriter
- BufferedReader
- BufferedWriter
- InputStreamReader
- OutputStreamWriter
- StringReader
- StringWriter
- CharArrayReader
- CharArrayWriter
- PrintWriter
- PipedReader
- PipedWriter
- FilterReader
- FilterWriter
- PushbackReader
- LineNumberReader

## 68. Serialization
- Serializable Interface
- Externalizable Interface
- serialVersionUID
- transient Keyword
- writeObject()
- readObject()
- writeReplace()
- readResolve()
- ObjectStreamField
- ObjectOutputStream
- ObjectInputStream
- NotSerializableException
- InvalidClassException

## 69. NIO Basics
- Path
- Paths
- Files
- FileSystem
- FileSystems
- FileStore
- ByteBuffer
- CharBuffer
- Channel
- FileChannel
- Selector
- SelectionKey

## 70. Files Class Methods
- Files.exists()
- Files.notExists()
- Files.isDirectory()
- Files.isRegularFile()
- Files.isReadable()
- Files.isWritable()
- Files.isExecutable()
- Files.isHidden()
- Files.isSameFile()
- Files.size()
- Files.createFile()
- Files.createDirectory()
- Files.createDirectories()
- Files.createTempFile()
- Files.createTempDirectory()
- Files.delete()
- Files.deleteIfExists()
- Files.copy()
- Files.move()
- Files.readAllBytes()
- Files.readAllLines()
- Files.readString() (Java 11)
- Files.write()
- Files.writeString() (Java 11)
- Files.lines()
- Files.list()
- Files.walk()
- Files.find()
- Files.newBufferedReader()
- Files.newBufferedWriter()
- Files.newInputStream()
- Files.newOutputStream()
- Files.getAttribute()
- Files.setAttribute()
- Files.getOwner()
- Files.setOwner()
- Files.getPosixFilePermissions()
- Files.setPosixFilePermissions()
- Files.getLastModifiedTime()
- Files.setLastModifiedTime()
- Files.probeContentType()
- Files.newDirectoryStream()
- Files.walkFileTree()
- Files.mismatch() (Java 12)

---

# PART 12: REFLECTION & ANNOTATIONS

## 71. Reflection Classes
- Class<T>
- Method
- Field
- Constructor
- Modifier
- Parameter
- AnnotatedElement
- Member
- AccessibleObject
- Array
- Proxy

## 72. Reflection Operations
- Class.forName()
- getClass()
- getName()
- getSimpleName()
- getCanonicalName()
- getTypeName()
- getPackage()
- getSuperclass()
- getInterfaces()
- getModifiers()
- isInterface()
- isArray()
- isPrimitive()
- isEnum()
- isAnnotation()
- isRecord()
- isSealed()
- getConstructors()
- getDeclaredConstructors()
- getMethods()
- getDeclaredMethods()
- getFields()
- getDeclaredFields()
- getRecordComponents()
- getPermittedSubclasses()
- newInstance()
- setAccessible()
- invoke()
- get(), set()

## 73. Annotations
- @Override
- @Deprecated
- @SuppressWarnings
- @SafeVarargs
- @FunctionalInterface
- @Native
- @Retention
- @Target
- @Documented
- @Inherited
- @Repeatable

## 74. Meta-Annotations Details
- RetentionPolicy.SOURCE
- RetentionPolicy.CLASS
- RetentionPolicy.RUNTIME
- ElementType.TYPE
- ElementType.FIELD
- ElementType.METHOD
- ElementType.PARAMETER
- ElementType.CONSTRUCTOR
- ElementType.LOCAL_VARIABLE
- ElementType.ANNOTATION_TYPE
- ElementType.PACKAGE
- ElementType.TYPE_PARAMETER
- ElementType.TYPE_USE
- ElementType.MODULE
- ElementType.RECORD_COMPONENT

---

# PART 13: MODULES (JPMS)

## 75. Module System
- module-info.java
- requires
- requires transitive
- requires static
- exports
- exports to
- opens
- opens to
- uses
- provides with
- Unnamed Module
- Automatic Module
- Named Module
- Open Module
- Module Resolution
- Module Path
- Service Provider Interface (SPI)

---

# PART 14: NETWORKING

## 76. Socket Programming
- Socket
- ServerSocket
- DatagramSocket
- DatagramPacket
- MulticastSocket
- SocketAddress
- InetAddress
- InetSocketAddress
- NetworkInterface

## 77. URL & HTTP
- URL
- URI
- URLConnection
- HttpURLConnection
- HttpClient (Java 11)
- HttpRequest
- HttpResponse
- WebSocket

## 78. HttpClient Methods (Java 11)
- HttpClient.newHttpClient()
- HttpClient.newBuilder()
- send()
- sendAsync()
- HttpRequest.newBuilder()
- uri()
- GET(), POST(), PUT(), DELETE()
- header(), headers()
- timeout()
- HttpResponse.BodyHandlers
- ofString()
- ofByteArray()
- ofInputStream()
- ofFile()
- ofLines()

---

# PART 15: PATTERN MATCHING

## 79. Pattern Matching Features
- instanceof Pattern (Java 16)
- switch Pattern (Java 21)
- Record Patterns (Java 21)
- Guarded Patterns (when clause)
- Null in Switch
- Exhaustiveness Check
- Destructuring
- Nested Patterns

---

# PART 16: MISCELLANEOUS

## 80. var Keyword (Java 10)
- Local Variable Type Inference
- var in for loops
- var in try-with-resources
- var restrictions
- var vs explicit types

## 81. Text Blocks (Java 15)
- Triple quotes (""")
- Line terminators
- Incidental whitespace
- Escape sequences in text blocks
- Trailing whitespace (\s)
- Line continuation (\)

## 82. String Templates (Java 21 Preview)
- STR template processor
- Template expressions (\{})
- RAW template processor
- FMT template processor
- Custom template processors

## 83. Regular Expressions
- Pattern
- Matcher
- Pattern.compile()
- Pattern.matches()
- Matcher.find()
- Matcher.matches()
- Matcher.group()
- Matcher.replaceAll()
- Matcher.replaceFirst()
- String.matches()
- String.split()
- String.replaceAll()

## 84. Comparator Methods (Java 8+)
- comparing()
- comparingInt(), comparingLong(), comparingDouble()
- thenComparing()
- reversed()
- nullsFirst()
- nullsLast()
- naturalOrder()
- reverseOrder()

## 85. Random Number Generation
- Random
- ThreadLocalRandom
- SecureRandom
- SplittableRandom
- RandomGenerator (Java 17)
- nextInt(), nextLong(), nextDouble()
- nextBoolean()
- nextBytes()
- ints(), longs(), doubles()

## 86. System Class Methods
- System.out, System.err, System.in
- System.exit()
- System.gc()
- System.currentTimeMillis()
- System.nanoTime()
- System.getProperty()
- System.setProperty()
- System.getenv()
- System.getProperties()
- System.lineSeparator()
- System.arraycopy()
- System.identityHashCode()
- System.console()
- System.Logger (Java 9)

## 87. Runtime Class
- Runtime.getRuntime()
- availableProcessors()
- freeMemory()
- totalMemory()
- maxMemory()
- gc()
- exec()
- exit()
- addShutdownHook()
- removeShutdownHook()
- version() (Java 9)

## 88. Process API (Java 9+)
- ProcessBuilder
- Process
- ProcessHandle
- ProcessHandle.Info
- pid()
- info()
- children()
- descendants()
- parent()
- onExit()
- isAlive()
- destroy()
- destroyForcibly()

## 89. Stack Walking API (Java 9)
- StackWalker
- StackWalker.Option
- walk()
- forEach()
- getCallerClass()
- StackFrame

## 90. Deprecation Tags (Java 9+)
- @Deprecated(since="version")
- @Deprecated(forRemoval=true)

---

# SUMMARY: Topic Count

| Part | Topics |
|------|--------|
| Language Fundamentals | 90+ |
| OOP | 150+ |
| Core Classes | 200+ |
| Exception Handling | 50+ |
| Generics | 30+ |
| Collections | 150+ |
| Functional | 80+ |
| Streams | 100+ |
| Date/Time | 60+ |
| Multithreading | 200+ |
| I/O & NIO | 150+ |
| Reflection | 80+ |
| Modules | 20+ |
| Networking | 50+ |
| Pattern Matching | 15+ |
| Miscellaneous | 100+ |
| **TOTAL** | **1500+ Atomic Topics** |

---

*This is the most comprehensive list of atomic Java topics ever compiled.*
