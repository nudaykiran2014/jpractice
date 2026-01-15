# Java I/O & NIO - Complete Guide

## Table of Contents
1. [Introduction to I/O](#1-introduction-to-io)
2. [Byte Streams](#2-byte-streams)
3. [Character Streams](#3-character-streams)
4. [Buffered Streams](#4-buffered-streams)
5. [Data Streams](#5-data-streams)
6. [Object Streams (Serialization)](#6-object-streams-serialization)
7. [File Class](#7-file-class)
8. [NIO - New I/O (Java 7+)](#8-nio---new-io-java-7)
9. [Path & Paths](#9-path--paths)
10. [Files Utility Class](#10-files-utility-class)
11. [Reading & Writing Files](#11-reading--writing-files)
12. [Directory Operations](#12-directory-operations)
13. [WatchService](#13-watchservice---file-system-monitoring)
14. [Channels & Buffers](#14-channels--buffers)
15. [Interview Questions](#15-interview-questions)

---

# 1. Introduction to I/O

## Kid-Friendly Explanation 🧒

**Think of I/O like a Post Office:**

- **Input** = Receiving letters (reading data INTO your program)
- **Output** = Sending letters (writing data OUT of your program)
- **Stream** = A conveyor belt that carries letters one by one

```
┌─────────────┐                    ┌─────────────┐
│   SOURCE    │  ──── Stream ────▶ │   PROGRAM   │  (INPUT)
│ (File/Net)  │                    │             │
└─────────────┘                    └─────────────┘

┌─────────────┐                    ┌─────────────┐
│   PROGRAM   │  ──── Stream ────▶ │ DESTINATION │  (OUTPUT)
│             │                    │ (File/Net)  │
└─────────────┘                    └─────────────┘
```

## Stream Types Overview

```
                    ┌─────────────────────────────────┐
                    │         JAVA I/O STREAMS        │
                    └─────────────────────────────────┘
                                    │
            ┌───────────────────────┴───────────────────────┐
            │                                               │
    ┌───────▼───────┐                               ┌───────▼───────┐
    │  BYTE STREAMS │                               │ CHAR STREAMS  │
    │   (Binary)    │                               │   (Text)      │
    │   8-bit data  │                               │  16-bit chars │
    └───────┬───────┘                               └───────┬───────┘
            │                                               │
    InputStream                                       Reader
    OutputStream                                      Writer
```

---

# 2. Byte Streams

## When to Use?
- Binary files (images, videos, PDFs)
- Network data
- Any raw byte data

## InputStream Hierarchy

```
InputStream (abstract)
├── FileInputStream        - Read from file
├── ByteArrayInputStream   - Read from byte array
├── BufferedInputStream    - Buffered reading
├── DataInputStream        - Read primitive types
├── ObjectInputStream      - Read objects
└── FilterInputStream      - Base for filter streams
```

## OutputStream Hierarchy

```
OutputStream (abstract)
├── FileOutputStream       - Write to file
├── ByteArrayOutputStream  - Write to byte array
├── BufferedOutputStream   - Buffered writing
├── DataOutputStream       - Write primitive types
├── ObjectOutputStream     - Write objects
└── FilterOutputStream     - Base for filter streams
```

## FileInputStream - Reading Bytes

```java
// Method 1: Basic (NOT recommended - no auto-close)
FileInputStream fis = new FileInputStream("image.png");
int byteData;
while ((byteData = fis.read()) != -1) {
    // Process each byte
}
fis.close();

// Method 2: Try-with-resources (RECOMMENDED!)
try (FileInputStream fis = new FileInputStream("image.png")) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = fis.read(buffer)) != -1) {
        // Process buffer[0] to buffer[bytesRead-1]
    }
}  // Auto-closed!
```

## FileOutputStream - Writing Bytes

```java
// Write bytes to file
try (FileOutputStream fos = new FileOutputStream("output.bin")) {
    byte[] data = {65, 66, 67, 68};  // ABCD
    fos.write(data);
}

// Append mode
try (FileOutputStream fos = new FileOutputStream("output.bin", true)) {
    fos.write(69);  // Append 'E'
}
```

## Copy File Using Byte Streams

```java
public void copyFile(String source, String dest) throws IOException {
    try (FileInputStream fis = new FileInputStream(source);
         FileOutputStream fos = new FileOutputStream(dest)) {
        
        byte[] buffer = new byte[8192];  // 8KB buffer
        int bytesRead;
        
        while ((bytesRead = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }
    }
}
```

---

# 3. Character Streams

## When to Use?
- Text files (.txt, .csv, .json, .xml)
- Any human-readable content
- When encoding matters (UTF-8, ASCII)

## Reader Hierarchy

```
Reader (abstract)
├── FileReader            - Read text from file
├── BufferedReader        - Buffered text reading
├── InputStreamReader     - Bridge: bytes → chars
├── StringReader          - Read from String
└── CharArrayReader       - Read from char array
```

## Writer Hierarchy

```
Writer (abstract)
├── FileWriter            - Write text to file
├── BufferedWriter        - Buffered text writing
├── OutputStreamWriter    - Bridge: chars → bytes
├── PrintWriter           - Convenient printing
├── StringWriter          - Write to String
└── CharArrayWriter       - Write to char array
```

## FileReader - Reading Text

```java
try (FileReader fr = new FileReader("story.txt")) {
    int charData;
    while ((charData = fr.read()) != -1) {
        System.out.print((char) charData);
    }
}
```

## FileWriter - Writing Text

```java
try (FileWriter fw = new FileWriter("output.txt")) {
    fw.write("Hello, World!\n");
    fw.write("This is line 2.");
}

// Append mode
try (FileWriter fw = new FileWriter("output.txt", true)) {
    fw.write("\nAppended line!");
}
```

## InputStreamReader - Specifying Encoding

```java
// Read file with specific encoding
try (InputStreamReader isr = new InputStreamReader(
        new FileInputStream("file.txt"), 
        StandardCharsets.UTF_8)) {
    // Read characters with UTF-8 encoding
}

// Write with specific encoding
try (OutputStreamWriter osw = new OutputStreamWriter(
        new FileOutputStream("file.txt"),
        StandardCharsets.UTF_8)) {
    osw.write("你好世界");  // Chinese characters
}
```

---

# 4. Buffered Streams

## Kid-Friendly Explanation 🧒

**Without Buffer:** 
- Go to fridge, get 1 apple, come back
- Go to fridge, get 1 apple, come back
- (100 trips for 100 apples!) SLOW!

**With Buffer:**
- Go to fridge, fill basket with 50 apples, come back
- Go to fridge, fill basket with 50 apples, come back
- (2 trips for 100 apples!) FAST!

## Why Use Buffers?
- Reduces disk/network access
- Dramatically improves performance
- Groups many small reads/writes into fewer large ones

## BufferedInputStream & BufferedOutputStream

```java
// Buffered byte streams
try (BufferedInputStream bis = new BufferedInputStream(
        new FileInputStream("large_file.bin"));
     BufferedOutputStream bos = new BufferedOutputStream(
        new FileOutputStream("copy.bin"))) {
    
    byte[] buffer = new byte[8192];
    int bytesRead;
    while ((bytesRead = bis.read(buffer)) != -1) {
        bos.write(buffer, 0, bytesRead);
    }
}
```

## BufferedReader - Best for Text Files!

```java
try (BufferedReader br = new BufferedReader(
        new FileReader("document.txt"))) {
    
    String line;
    while ((line = br.readLine()) != null) {  // Read whole line!
        System.out.println(line);
    }
}

// Modern way with Stream (Java 8+)
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    br.lines()  // Returns Stream<String>
      .filter(line -> !line.isEmpty())
      .forEach(System.out::println);
}
```

## BufferedWriter

```java
try (BufferedWriter bw = new BufferedWriter(
        new FileWriter("output.txt"))) {
    
    bw.write("Line 1");
    bw.newLine();  // Platform-independent line separator
    bw.write("Line 2");
    bw.newLine();
    bw.write("Line 3");
}
```

## PrintWriter - Most Convenient!

```java
try (PrintWriter pw = new PrintWriter(
        new BufferedWriter(new FileWriter("log.txt")))) {
    
    pw.println("Log entry 1");           // Auto newline
    pw.printf("Value: %d%n", 42);        // Formatted
    pw.print("No newline");
}

// Shortcut constructor (Java 5+)
try (PrintWriter pw = new PrintWriter("output.txt")) {
    pw.println("Hello!");
}
```

---

# 5. Data Streams

## Purpose
Read/write primitive types (int, double, boolean) in binary format.

```java
// Write primitive types
try (DataOutputStream dos = new DataOutputStream(
        new FileOutputStream("data.bin"))) {
    
    dos.writeInt(42);
    dos.writeDouble(3.14159);
    dos.writeBoolean(true);
    dos.writeUTF("Hello");  // String with length prefix
}

// Read primitive types (SAME ORDER!)
try (DataInputStream dis = new DataInputStream(
        new FileInputStream("data.bin"))) {
    
    int i = dis.readInt();
    double d = dis.readDouble();
    boolean b = dis.readBoolean();
    String s = dis.readUTF();
    
    System.out.println(i + ", " + d + ", " + b + ", " + s);
}
```

---

# 6. Object Streams (Serialization)

## Kid-Friendly Explanation 🧒

**Serialization** = Freeze-drying your object (convert to bytes)
**Deserialization** = Rehydrating (convert back to object)

Like saving a game - your character's state is saved to disk!

## Making a Class Serializable

```java
import java.io.Serializable;

public class Person implements Serializable {
    // Recommended: Define version for compatibility
    private static final long serialVersionUID = 1L;
    
    private String name;
    private int age;
    private transient String password;  // NOT serialized!
    
    public Person(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }
    
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
```

## Serialize (Save Object)

```java
Person person = new Person("John", 30, "secret123");

try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("person.ser"))) {
    
    oos.writeObject(person);
    System.out.println("Object saved!");
}
```

## Deserialize (Load Object)

```java
try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream("person.ser"))) {
    
    Person loaded = (Person) ois.readObject();
    System.out.println("Loaded: " + loaded);
    // password will be null (transient)
}
```

## Serialization Keywords

| Keyword | Effect |
|---------|--------|
| `transient` | Field is NOT serialized |
| `serialVersionUID` | Version control for compatibility |

## Custom Serialization

```java
public class CustomPerson implements Serializable {
    private String name;
    private transient int age;  // We'll handle this manually
    
    // Custom serialization
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();  // Write non-transient fields
        out.writeInt(age * 2);     // Custom: write encrypted age
    }
    
    // Custom deserialization
    private void readObject(ObjectInputStream in) 
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();    // Read non-transient fields
        this.age = in.readInt() / 2;  // Decrypt age
    }
}
```

---

# 7. File Class

## Legacy Way (Pre-Java 7)

```java
File file = new File("document.txt");
File dir = new File("/users/john/documents");
File fullPath = new File(dir, "report.pdf");

// Check existence
boolean exists = file.exists();
boolean isFile = file.isFile();
boolean isDir = file.isDirectory();

// Get info
String name = file.getName();
String path = file.getPath();
String absolutePath = file.getAbsolutePath();
long size = file.length();
long lastModified = file.lastModified();

// Permissions
boolean canRead = file.canRead();
boolean canWrite = file.canWrite();
boolean canExecute = file.canExecute();

// Create
boolean created = file.createNewFile();
boolean dirCreated = file.mkdir();
boolean dirsCreated = file.mkdirs();  // Creates parent dirs too

// Delete
boolean deleted = file.delete();

// Rename/Move
file.renameTo(new File("newname.txt"));

// List directory contents
String[] names = dir.list();
File[] files = dir.listFiles();

// Filter files
File[] txtFiles = dir.listFiles((d, name) -> name.endsWith(".txt"));
```

---

# 8. NIO - New I/O (Java 7+)

## Why NIO?

| Old I/O (java.io) | NIO (java.nio) |
|-------------------|----------------|
| Stream-based | Buffer-based |
| Blocking | Non-blocking possible |
| No file metadata | Rich file attributes |
| No symbolic links | Symbolic link support |
| Platform-dependent paths | Platform-independent Path |

## NIO Key Classes

```
java.nio.file
├── Path         - File/directory path
├── Paths        - Path factory (Java 7-10)
├── Files        - File operations utility
├── FileSystem   - File system access
└── FileSystems  - FileSystem factory

java.nio
├── Buffer       - Data container
├── ByteBuffer   - Byte buffer
├── CharBuffer   - Character buffer
└── Channel      - I/O channel
```

---

# 9. Path & Paths

## Creating Paths

```java
// Java 7-10
Path path1 = Paths.get("document.txt");
Path path2 = Paths.get("/users", "john", "docs", "file.txt");
Path path3 = Paths.get(URI.create("file:///users/john/file.txt"));

// Java 11+ (preferred)
Path path4 = Path.of("document.txt");
Path path5 = Path.of("/users", "john", "docs", "file.txt");
```

## Path Methods

```java
Path path = Path.of("/users/john/documents/report.pdf");

// Components
path.getFileName();      // report.pdf
path.getParent();        // /users/john/documents
path.getRoot();          // /
path.getNameCount();     // 4

// Individual components
path.getName(0);         // users
path.getName(1);         // john

// Path manipulation
path.resolve("images");           // /users/john/documents/report.pdf/images
path.resolve("../backup");        // /users/john/documents/report.pdf/../backup
path.resolveSibling("data.csv");  // /users/john/documents/data.csv
path.relativize(otherPath);       // Relative path between two paths

// Normalization
Path messy = Path.of("/users/john/../john/./documents");
messy.normalize();  // /users/john/documents

// Conversion
path.toAbsolutePath();
path.toRealPath();    // Resolves symlinks
path.toFile();        // Convert to File
path.toUri();         // Convert to URI
```

## Path Comparison

```java
Path p1 = Path.of("a/b/c");
Path p2 = Path.of("a/b/c");

p1.equals(p2);              // true
p1.compareTo(p2);           // 0
p1.startsWith("a/b");       // true
p1.endsWith("c");           // true
```

---

# 10. Files Utility Class

## Checking File Properties

```java
Path path = Path.of("file.txt");

Files.exists(path);
Files.notExists(path);
Files.isRegularFile(path);
Files.isDirectory(path);
Files.isSymbolicLink(path);
Files.isReadable(path);
Files.isWritable(path);
Files.isExecutable(path);
Files.isHidden(path);
Files.size(path);                    // Size in bytes
Files.getLastModifiedTime(path);
Files.getOwner(path);
```

## Creating Files & Directories

```java
// Create file
Files.createFile(Path.of("newfile.txt"));

// Create directory
Files.createDirectory(Path.of("newdir"));

// Create directories (including parents)
Files.createDirectories(Path.of("a/b/c/d"));

// Create temporary file
Path temp = Files.createTempFile("prefix", ".tmp");

// Create temporary directory
Path tempDir = Files.createTempDirectory("myapp");
```

## Copy, Move, Delete

```java
Path source = Path.of("source.txt");
Path target = Path.of("target.txt");

// Copy
Files.copy(source, target);
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES);

// Move
Files.move(source, target);
Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);

// Delete
Files.delete(path);              // Throws if not exists
Files.deleteIfExists(path);      // No exception if not exists
```

## File Attributes

```java
// Basic attributes
BasicFileAttributes attrs = Files.readAttributes(
    path, BasicFileAttributes.class);

attrs.creationTime();
attrs.lastModifiedTime();
attrs.lastAccessTime();
attrs.size();
attrs.isDirectory();
attrs.isRegularFile();
attrs.isSymbolicLink();

// Set attributes
Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
Files.setAttribute(path, "dos:hidden", true);  // Windows
```

---

# 11. Reading & Writing Files

## Read Entire File (Small Files)

```java
// Read all bytes
byte[] bytes = Files.readAllBytes(Path.of("image.png"));

// Read all lines as List
List<String> lines = Files.readAllLines(Path.of("file.txt"));
List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

// Read as String (Java 11+)
String content = Files.readString(Path.of("file.txt"));
String content = Files.readString(path, StandardCharsets.UTF_8);
```

## Write Entire File (Small Files)

```java
// Write bytes
byte[] data = "Hello".getBytes();
Files.write(Path.of("output.bin"), data);

// Write lines
List<String> lines = List.of("Line 1", "Line 2", "Line 3");
Files.write(Path.of("output.txt"), lines);

// Write String (Java 11+)
Files.writeString(Path.of("output.txt"), "Hello, World!");

// With options
Files.write(path, lines, 
    StandardOpenOption.CREATE,
    StandardOpenOption.APPEND);
```

## Stream-Based Reading (Large Files)

```java
// Lines as Stream (lazy, memory efficient)
try (Stream<String> lines = Files.lines(Path.of("huge_file.txt"))) {
    lines.filter(line -> line.contains("ERROR"))
         .forEach(System.out::println);
}

// BufferedReader
try (BufferedReader reader = Files.newBufferedReader(path)) {
    String line;
    while ((line = reader.readLine()) != null) {
        process(line);
    }
}

// InputStream
try (InputStream is = Files.newInputStream(path)) {
    // Read bytes
}
```

## Stream-Based Writing (Large Files)

```java
// BufferedWriter
try (BufferedWriter writer = Files.newBufferedWriter(path)) {
    writer.write("Hello");
    writer.newLine();
    writer.write("World");
}

// OutputStream
try (OutputStream os = Files.newOutputStream(path)) {
    os.write(data);
}

// With options
try (BufferedWriter writer = Files.newBufferedWriter(path,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND)) {
    writer.write("Appended text");
}
```

---

# 12. Directory Operations

## List Directory Contents

```java
// List immediate contents
try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
    for (Path entry : stream) {
        System.out.println(entry.getFileName());
    }
}

// With glob pattern filter
try (DirectoryStream<Path> stream = 
        Files.newDirectoryStream(dir, "*.{txt,pdf}")) {
    for (Path entry : stream) {
        System.out.println(entry);
    }
}

// As Stream (Java 8+)
try (Stream<Path> stream = Files.list(dir)) {
    stream.filter(Files::isRegularFile)
          .forEach(System.out::println);
}
```

## Walk Directory Tree

```java
// Walk entire tree
try (Stream<Path> stream = Files.walk(startDir)) {
    stream.filter(Files::isRegularFile)
          .filter(p -> p.toString().endsWith(".java"))
          .forEach(System.out::println);
}

// Walk with max depth
try (Stream<Path> stream = Files.walk(startDir, 2)) {
    stream.forEach(System.out::println);
}

// Find files matching criteria
try (Stream<Path> stream = Files.find(startDir, 10,
        (path, attrs) -> attrs.isRegularFile() && 
                         path.toString().endsWith(".log"))) {
    stream.forEach(System.out::println);
}
```

## FileVisitor - Advanced Tree Walking

```java
Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        System.out.println("File: " + file);
        return FileVisitResult.CONTINUE;
    }
    
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        System.out.println("Entering: " + dir);
        return FileVisitResult.CONTINUE;
    }
    
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        System.out.println("Leaving: " + dir);
        return FileVisitResult.CONTINUE;
    }
    
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println("Failed: " + file);
        return FileVisitResult.CONTINUE;
    }
});
```

## Delete Directory Recursively

```java
// Delete directory and all contents
Files.walkFileTree(dirToDelete, new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) 
            throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
    }
    
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) 
            throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
    }
});

// Java 8+ with Stream
try (Stream<Path> walk = Files.walk(dirToDelete)) {
    walk.sorted(Comparator.reverseOrder())  // Files before directories
        .forEach(p -> {
            try { Files.delete(p); } 
            catch (IOException e) { e.printStackTrace(); }
        });
}
```

---

# 13. WatchService - File System Monitoring

## Kid-Friendly Explanation 🧒

Like a security camera watching a folder:
- "Someone created a file!" 📸
- "Someone modified a file!" 📸
- "Someone deleted a file!" 📸

## Basic Usage

```java
// Create watch service
WatchService watchService = FileSystems.getDefault().newWatchService();

// Register directory to watch
Path dir = Path.of("/path/to/watch");
dir.register(watchService,
    StandardWatchEventKinds.ENTRY_CREATE,
    StandardWatchEventKinds.ENTRY_MODIFY,
    StandardWatchEventKinds.ENTRY_DELETE);

// Watch loop
while (true) {
    WatchKey key = watchService.take();  // Blocks until event
    
    for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind<?> kind = event.kind();
        
        if (kind == StandardWatchEventKinds.OVERFLOW) {
            continue;  // Events may have been lost
        }
        
        Path filename = (Path) event.context();
        System.out.println(kind.name() + ": " + filename);
    }
    
    // Reset key to receive further events
    boolean valid = key.reset();
    if (!valid) {
        break;  // Directory no longer accessible
    }
}
```

## Watch Multiple Directories

```java
WatchService watchService = FileSystems.getDefault().newWatchService();

// Register multiple directories
for (Path dir : directories) {
    dir.register(watchService,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_MODIFY,
        StandardWatchEventKinds.ENTRY_DELETE);
}

// Process events...
```

---

# 14. Channels & Buffers

## NIO Architecture

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   SOURCE    │ ──▶ │   CHANNEL   │ ──▶ │   BUFFER    │ ──▶ Program
│  (File/Net) │ ◀── │             │ ◀── │             │ ◀── 
└─────────────┘     └─────────────┘     └─────────────┘
```

## ByteBuffer Basics

```java
// Allocate buffer
ByteBuffer buffer = ByteBuffer.allocate(1024);

// Key properties
buffer.capacity();   // Total size (1024)
buffer.position();   // Current position
buffer.limit();      // Read/write limit
buffer.remaining();  // limit - position

// Writing to buffer
buffer.put((byte) 65);           // Put single byte
buffer.put(byteArray);           // Put array
buffer.putInt(42);               // Put int (4 bytes)
buffer.putDouble(3.14);          // Put double (8 bytes)

// Flip for reading (IMPORTANT!)
buffer.flip();  // position = 0, limit = old position

// Reading from buffer
byte b = buffer.get();
int i = buffer.getInt();
double d = buffer.getDouble();

// Clear for reuse
buffer.clear();  // position = 0, limit = capacity
```

## FileChannel - Reading

```java
try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    
    while (channel.read(buffer) != -1) {
        buffer.flip();  // Prepare for reading
        
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
        
        buffer.clear();  // Prepare for writing
    }
}
```

## FileChannel - Writing

```java
try (FileChannel channel = FileChannel.open(path,
        StandardOpenOption.WRITE,
        StandardOpenOption.CREATE)) {
    
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    buffer.put("Hello, NIO!".getBytes());
    buffer.flip();  // Prepare for reading (by channel)
    
    channel.write(buffer);
}
```

## Memory-Mapped Files (Large Files!)

```java
// Map file to memory - VERY fast for large files!
try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
    MappedByteBuffer buffer = channel.map(
        FileChannel.MapMode.READ_ONLY,
        0,                          // Start position
        channel.size()              // Size to map
    );
    
    while (buffer.hasRemaining()) {
        System.out.print((char) buffer.get());
    }
}
```

---

# 15. Interview Questions

## Q1: What is the difference between InputStream and Reader?

**Answer:**
| InputStream | Reader |
|-------------|--------|
| Reads bytes (8-bit) | Reads characters (16-bit) |
| For binary data | For text data |
| No encoding awareness | Handles character encoding |
| Example: FileInputStream | Example: FileReader |

---

## Q2: Why should we use BufferedReader/BufferedWriter?

**Answer:**
- Reduces disk I/O operations by buffering data
- `readLine()` method for convenient line reading
- Significantly improves performance (10-100x faster)
- Groups multiple small reads/writes into fewer large ones

---

## Q3: What is serialization? What is `serialVersionUID`?

**Answer:**
- **Serialization**: Converting object to byte stream for storage/transmission
- **Deserialization**: Converting byte stream back to object
- **serialVersionUID**: Version identifier for serialization compatibility
  - If class changes without matching UID, deserialization fails
  - Best practice: Always declare explicitly

---

## Q4: What does `transient` keyword do?

**Answer:**
- Marks field to be excluded from serialization
- Use for: sensitive data (passwords), computed fields, non-serializable objects
- After deserialization, transient fields have default values (null, 0, false)

---

## Q5: Difference between `Files.copy()` and manual copy with streams?

**Answer:**
```java
// Files.copy() - Simple, optimized
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

// Manual - More control, works with any streams
try (InputStream in = ...; OutputStream out = ...) {
    byte[] buffer = new byte[8192];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
    }
}
```

`Files.copy()` may use OS-level optimizations (like `sendfile` on Linux).

---

## Q6: How to read a large file efficiently?

**Answer:**
```java
// Use Files.lines() - lazy loading, memory efficient
try (Stream<String> lines = Files.lines(path)) {
    lines.filter(...)
         .map(...)
         .forEach(...);
}

// For binary: Memory-mapped files
MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, size);
```

Never use `Files.readAllLines()` for large files (loads everything to memory)!

---

## Q7: What is NIO vs IO?

**Answer:**
| Feature | IO | NIO |
|---------|-----|-----|
| Approach | Stream-oriented | Buffer-oriented |
| Blocking | Always blocking | Can be non-blocking |
| Selectors | No | Yes (for multiplexing) |
| File system | File class | Path, Files classes |
| Performance | Good | Better for many connections |

---

## Q8: How does WatchService work?

**Answer:**
- Monitors file system for changes (create, modify, delete)
- Uses OS-native file watching mechanisms
- Efficient - no polling needed
- Returns events: ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE, OVERFLOW

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│                    I/O CHEAT SHEET                          │
├─────────────────────────────────────────────────────────────┤
│ READ TEXT FILE                                              │
│   Files.readString(path)              // Java 11+, small    │
│   Files.readAllLines(path)            // Java 7+, small     │
│   Files.lines(path)                   // Java 8+, large     │
│   BufferedReader.readLine()           // Classic            │
├─────────────────────────────────────────────────────────────┤
│ WRITE TEXT FILE                                             │
│   Files.writeString(path, str)        // Java 11+           │
│   Files.write(path, lines)            // Java 7+            │
│   BufferedWriter + PrintWriter        // Classic            │
├─────────────────────────────────────────────────────────────┤
│ BINARY FILES                                                │
│   Files.readAllBytes(path)            // Small files        │
│   Files.write(path, bytes)            // Small files        │
│   FileChannel + ByteBuffer            // Large files        │
│   MappedByteBuffer                    // Very large files   │
├─────────────────────────────────────────────────────────────┤
│ FILE OPERATIONS                                             │
│   Files.exists(), Files.isDirectory()                       │
│   Files.copy(), Files.move(), Files.delete()                │
│   Files.createFile(), Files.createDirectories()             │
│   Files.walk(), Files.list(), Files.find()                  │
└─────────────────────────────────────────────────────────────┘
```
