# Java JDBC - Complete Guide

## Table of Contents
1. [What is JDBC?](#1-what-is-jdbc)
2. [JDBC Architecture](#2-jdbc-architecture)
3. [JDBC Drivers](#3-jdbc-drivers)
4. [Connection Steps](#4-connection-steps)
5. [Statement Types](#5-statement-types)
6. [ResultSet](#6-resultset)
7. [PreparedStatement](#7-preparedstatement)
8. [CallableStatement](#8-callablestatement)
9. [Transactions](#9-transactions)
10. [Batch Processing](#10-batch-processing)
11. [Connection Pooling](#11-connection-pooling)
12. [RowSet](#12-rowset)
13. [Best Practices](#13-best-practices)
14. [Interview Questions](#14-interview-questions)

---

# 1. What is JDBC?

## Kid-Friendly Explanation 🧒

**JDBC = Java's way to talk to databases**

Think of it like a translator:
- Your Java program speaks "Java"
- The database speaks "SQL"
- JDBC translates between them!

```
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│    JAVA      │  JDBC   │    DRIVER    │   SQL   │   DATABASE   │
│  Application │ ──────▶ │  (Translator)│ ──────▶ │  (MySQL,     │
│              │ ◀────── │              │ ◀────── │   Oracle...) │
└──────────────┘         └──────────────┘         └──────────────┘
```

## What Can JDBC Do?

1. **Connect** to a database
2. **Execute** SQL queries
3. **Retrieve** results
4. **Update** data (INSERT, UPDATE, DELETE)
5. **Manage** transactions
6. **Call** stored procedures

---

# 2. JDBC Architecture

## Two-Tier Architecture

```
┌─────────────┐                ┌─────────────┐
│   Java App  │ ◀── JDBC ───▶ │   Database  │
│  + Driver   │                │   Server    │
└─────────────┘                └─────────────┘
```

## Three-Tier Architecture

```
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│   Java App  │ ───▶ │  App Server │ ───▶ │   Database  │
│   (Client)  │      │  (Business  │      │   Server    │
│             │      │   Logic)    │      │             │
└─────────────┘      └─────────────┘      └─────────────┘
```

## Key JDBC Interfaces (java.sql)

| Interface | Purpose |
|-----------|---------|
| `Driver` | Handles communication with database |
| `DriverManager` | Manages list of database drivers |
| `Connection` | Active connection to database |
| `Statement` | Execute SQL statements |
| `PreparedStatement` | Precompiled SQL (parameterized) |
| `CallableStatement` | Execute stored procedures |
| `ResultSet` | Results of a query |
| `DataSource` | Connection factory (preferred) |

---

# 3. JDBC Drivers

## Driver Types

| Type | Name | Description |
|------|------|-------------|
| Type 1 | JDBC-ODBC Bridge | Uses ODBC driver (deprecated in Java 8) |
| Type 2 | Native-API | Uses database native libraries |
| Type 3 | Network Protocol | Middleware translates calls |
| Type 4 | Pure Java | Direct connection (most common!) |

## Common JDBC Drivers

| Database | Driver Class | Maven Dependency |
|----------|--------------|------------------|
| MySQL | `com.mysql.cj.jdbc.Driver` | `mysql-connector-java` |
| PostgreSQL | `org.postgresql.Driver` | `postgresql` |
| Oracle | `oracle.jdbc.driver.OracleDriver` | `ojdbc8` |
| SQL Server | `com.microsoft.sqlserver.jdbc.SQLServerDriver` | `mssql-jdbc` |
| H2 | `org.h2.Driver` | `h2` |
| SQLite | `org.sqlite.JDBC` | `sqlite-jdbc` |

## JDBC URL Format

```
jdbc:<subprotocol>:<subname>

Examples:
jdbc:mysql://localhost:3306/mydb
jdbc:postgresql://localhost:5432/mydb
jdbc:oracle:thin:@localhost:1521:orcl
jdbc:sqlserver://localhost:1433;databaseName=mydb
jdbc:h2:mem:testdb
jdbc:sqlite:./database.db
```

---

# 4. Connection Steps

## The 6 Steps of JDBC

```
1. Load Driver (optional in JDBC 4.0+)
           ↓
2. Establish Connection
           ↓
3. Create Statement
           ↓
4. Execute Query
           ↓
5. Process Results
           ↓
6. Close Resources
```

## Basic Example

```java
import java.sql.*;

public class JDBCExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "root";
        String password = "password";
        
        // Step 1: Load driver (optional since JDBC 4.0)
        // Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Steps 2-6: Try-with-resources handles closing
        try (
            // Step 2: Establish connection
            Connection conn = DriverManager.getConnection(url, user, password);
            
            // Step 3: Create statement
            Statement stmt = conn.createStatement();
            
            // Step 4: Execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM users")
        ) {
            // Step 5: Process results
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println(id + ": " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Step 6: Resources auto-closed by try-with-resources
    }
}
```

## Connection Properties

```java
// Method 1: URL with properties
String url = "jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC";
Connection conn = DriverManager.getConnection(url, "user", "pass");

// Method 2: Properties object
Properties props = new Properties();
props.setProperty("user", "root");
props.setProperty("password", "password");
props.setProperty("useSSL", "false");
Connection conn = DriverManager.getConnection(url, props);

// Method 3: DataSource (preferred in production)
MysqlDataSource dataSource = new MysqlDataSource();
dataSource.setServerName("localhost");
dataSource.setPort(3306);
dataSource.setDatabaseName("mydb");
dataSource.setUser("root");
dataSource.setPassword("password");
Connection conn = dataSource.getConnection();
```

---

# 5. Statement Types

## Comparison

| Type | Use Case | SQL Injection | Performance |
|------|----------|---------------|-------------|
| Statement | Static SQL, no parameters | Vulnerable! | Slow for repeated |
| PreparedStatement | Parameterized SQL | Safe! | Fast (precompiled) |
| CallableStatement | Stored procedures | Safe! | Depends on procedure |

## Statement (Avoid for user input!)

```java
try (Statement stmt = conn.createStatement()) {
    // Query
    ResultSet rs = stmt.executeQuery("SELECT * FROM users");
    
    // Update (returns affected rows)
    int rows = stmt.executeUpdate(
        "INSERT INTO users (name) VALUES ('John')");
    
    // Execute (returns true if ResultSet)
    boolean hasResultSet = stmt.execute(
        "SELECT * FROM users");
}
```

## Statement Methods

| Method | Returns | Use For |
|--------|---------|---------|
| `executeQuery(sql)` | ResultSet | SELECT |
| `executeUpdate(sql)` | int (rows affected) | INSERT, UPDATE, DELETE, DDL |
| `execute(sql)` | boolean | Any SQL |
| `executeBatch()` | int[] | Multiple statements |

---

# 6. ResultSet

## Navigating Results

```java
ResultSet rs = stmt.executeQuery("SELECT * FROM users");

// Forward navigation (default)
while (rs.next()) {
    // Process each row
}

// Check if has data
if (rs.next()) {
    // Has at least one row
}
```

## Getting Values

```java
// By column index (1-based!)
int id = rs.getInt(1);
String name = rs.getString(2);

// By column name (preferred!)
int id = rs.getInt("id");
String name = rs.getString("name");
String email = rs.getString("email");
Date created = rs.getDate("created_at");
Timestamp updated = rs.getTimestamp("updated_at");
BigDecimal price = rs.getBigDecimal("price");
byte[] data = rs.getBytes("blob_column");
boolean active = rs.getBoolean("is_active");

// Check for NULL
if (rs.wasNull()) {
    // Last value read was NULL
}

// Or check explicitly
String value = rs.getString("nullable_column");
if (value == null) {
    // Column was NULL
}
```

## ResultSet Types

```java
Statement stmt = conn.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,  // Scrolling
    ResultSet.CONCUR_READ_ONLY          // Concurrency
);
```

| Type | Description |
|------|-------------|
| `TYPE_FORWARD_ONLY` | Can only move forward (default) |
| `TYPE_SCROLL_INSENSITIVE` | Scrollable, not sensitive to changes |
| `TYPE_SCROLL_SENSITIVE` | Scrollable, reflects DB changes |

| Concurrency | Description |
|-------------|-------------|
| `CONCUR_READ_ONLY` | Cannot update ResultSet (default) |
| `CONCUR_UPDATABLE` | Can update/insert/delete through ResultSet |

## Scrollable ResultSet

```java
Statement stmt = conn.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,
    ResultSet.CONCUR_READ_ONLY
);
ResultSet rs = stmt.executeQuery("SELECT * FROM users");

rs.first();           // Move to first row
rs.last();            // Move to last row
rs.absolute(5);       // Move to row 5
rs.relative(-2);      // Move 2 rows back
rs.previous();        // Move to previous row
rs.beforeFirst();     // Move before first row
rs.afterLast();       // Move after last row

rs.isFirst();         // Is at first row?
rs.isLast();          // Is at last row?
rs.isBeforeFirst();   // Is before first?
rs.isAfterLast();     // Is after last?
rs.getRow();          // Current row number
```

## Updatable ResultSet

```java
Statement stmt = conn.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,
    ResultSet.CONCUR_UPDATABLE
);
ResultSet rs = stmt.executeQuery("SELECT * FROM users");

// Update row
rs.next();
rs.updateString("name", "Jane");
rs.updateInt("age", 30);
rs.updateRow();  // Commit changes

// Delete row
rs.next();
rs.deleteRow();

// Insert row
rs.moveToInsertRow();
rs.updateString("name", "New User");
rs.updateInt("age", 25);
rs.insertRow();
rs.moveToCurrentRow();
```

---

# 7. PreparedStatement

## Why PreparedStatement?

1. **Prevents SQL Injection** (most important!)
2. **Better performance** (precompiled)
3. **Cleaner code** (no string concatenation)

## SQL Injection Example

```java
// ❌ DANGEROUS - SQL Injection vulnerability!
String userInput = "'; DROP TABLE users; --";
String sql = "SELECT * FROM users WHERE name = '" + userInput + "'";
// Becomes: SELECT * FROM users WHERE name = ''; DROP TABLE users; --'

// ✅ SAFE - PreparedStatement escapes input
String sql = "SELECT * FROM users WHERE name = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, userInput);  // Safely escaped!
```

## Basic Usage

```java
// SELECT with parameters
String sql = "SELECT * FROM users WHERE age > ? AND city = ?";
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setInt(1, 18);           // First parameter
    pstmt.setString(2, "London");  // Second parameter
    
    try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
    }
}

// INSERT
String insertSql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
    pstmt.setString(1, "John");
    pstmt.setString(2, "john@example.com");
    pstmt.setInt(3, 25);
    int rowsAffected = pstmt.executeUpdate();
    System.out.println("Inserted: " + rowsAffected);
}

// UPDATE
String updateSql = "UPDATE users SET email = ? WHERE id = ?";
try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
    pstmt.setString(1, "new@email.com");
    pstmt.setInt(2, 123);
    int rowsAffected = pstmt.executeUpdate();
}

// DELETE
String deleteSql = "DELETE FROM users WHERE id = ?";
try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
    pstmt.setInt(1, 123);
    int rowsAffected = pstmt.executeUpdate();
}
```

## Setting Different Types

```java
PreparedStatement pstmt = conn.prepareStatement(sql);

pstmt.setInt(1, 42);
pstmt.setLong(2, 1234567890L);
pstmt.setDouble(3, 3.14159);
pstmt.setBigDecimal(4, new BigDecimal("99.99"));
pstmt.setString(5, "Hello");
pstmt.setBoolean(6, true);
pstmt.setDate(7, java.sql.Date.valueOf("2024-01-15"));
pstmt.setTimestamp(8, Timestamp.valueOf("2024-01-15 10:30:00"));
pstmt.setTime(9, Time.valueOf("10:30:00"));
pstmt.setBytes(10, byteArray);
pstmt.setNull(11, Types.VARCHAR);
pstmt.setObject(12, anyObject);
```

## Get Generated Keys

```java
String sql = "INSERT INTO users (name) VALUES (?)";
try (PreparedStatement pstmt = conn.prepareStatement(
        sql, Statement.RETURN_GENERATED_KEYS)) {
    
    pstmt.setString(1, "John");
    pstmt.executeUpdate();
    
    try (ResultSet keys = pstmt.getGeneratedKeys()) {
        if (keys.next()) {
            long generatedId = keys.getLong(1);
            System.out.println("Generated ID: " + generatedId);
        }
    }
}
```

---

# 8. CallableStatement

For calling stored procedures.

## Stored Procedure Example (MySQL)

```sql
DELIMITER //
CREATE PROCEDURE GetUserById(IN userId INT, OUT userName VARCHAR(100))
BEGIN
    SELECT name INTO userName FROM users WHERE id = userId;
END //
DELIMITER ;
```

## Calling Stored Procedures

```java
// Procedure with IN and OUT parameters
String sql = "{CALL GetUserById(?, ?)}";
try (CallableStatement cstmt = conn.prepareCall(sql)) {
    // Set IN parameter
    cstmt.setInt(1, 123);
    
    // Register OUT parameter
    cstmt.registerOutParameter(2, Types.VARCHAR);
    
    // Execute
    cstmt.execute();
    
    // Get OUT parameter
    String userName = cstmt.getString(2);
    System.out.println("User: " + userName);
}

// Procedure returning ResultSet
String sql = "{CALL GetAllUsers()}";
try (CallableStatement cstmt = conn.prepareCall(sql);
     ResultSet rs = cstmt.executeQuery()) {
    while (rs.next()) {
        System.out.println(rs.getString("name"));
    }
}

// Function returning value
String sql = "{? = CALL CalculateTotal(?)}";
try (CallableStatement cstmt = conn.prepareCall(sql)) {
    cstmt.registerOutParameter(1, Types.DECIMAL);
    cstmt.setInt(2, 123);  // Order ID
    cstmt.execute();
    BigDecimal total = cstmt.getBigDecimal(1);
}
```

---

# 9. Transactions

## Kid-Friendly Explanation 🧒

**Transaction = "All or nothing" operation**

Like a bank transfer:
1. Take money from Account A
2. Add money to Account B

If step 2 fails, you MUST undo step 1!

## ACID Properties

| Property | Meaning |
|----------|---------|
| **A**tomicity | All operations succeed or all fail |
| **C**onsistency | Database stays valid |
| **I**solation | Transactions don't interfere |
| **D**urability | Committed changes persist |

## Transaction Example

```java
Connection conn = null;
try {
    conn = DriverManager.getConnection(url, user, password);
    
    // Disable auto-commit (start transaction)
    conn.setAutoCommit(false);
    
    try (PreparedStatement debit = conn.prepareStatement(
            "UPDATE accounts SET balance = balance - ? WHERE id = ?");
         PreparedStatement credit = conn.prepareStatement(
            "UPDATE accounts SET balance = balance + ? WHERE id = ?")) {
        
        // Debit from account 1
        debit.setBigDecimal(1, new BigDecimal("100.00"));
        debit.setInt(2, 1);
        debit.executeUpdate();
        
        // Credit to account 2
        credit.setBigDecimal(1, new BigDecimal("100.00"));
        credit.setInt(2, 2);
        credit.executeUpdate();
        
        // All good - commit!
        conn.commit();
        System.out.println("Transfer successful!");
        
    } catch (SQLException e) {
        // Something failed - rollback!
        conn.rollback();
        System.err.println("Transfer failed, rolled back!");
        throw e;
    }
} finally {
    if (conn != null) {
        conn.setAutoCommit(true);  // Reset
        conn.close();
    }
}
```

## Savepoints

```java
conn.setAutoCommit(false);

try {
    // Insert order
    stmt.executeUpdate("INSERT INTO orders ...");
    
    // Create savepoint
    Savepoint sp = conn.setSavepoint("afterOrder");
    
    try {
        // Insert order items
        stmt.executeUpdate("INSERT INTO order_items ...");
    } catch (SQLException e) {
        // Rollback only the items, keep the order
        conn.rollback(sp);
    }
    
    conn.commit();
} catch (SQLException e) {
    conn.rollback();  // Rollback everything
}
```

## Isolation Levels

```java
conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
```

| Level | Dirty Read | Non-Repeatable Read | Phantom Read |
|-------|------------|---------------------|--------------|
| READ_UNCOMMITTED | Yes | Yes | Yes |
| READ_COMMITTED | No | Yes | Yes |
| REPEATABLE_READ | No | No | Yes |
| SERIALIZABLE | No | No | No |

Higher isolation = More safety, Less performance

---

# 10. Batch Processing

## Why Batch?

Instead of:
```
INSERT → wait → INSERT → wait → INSERT → wait
```

Batch:
```
INSERT + INSERT + INSERT → send all at once → wait
```

**Much faster for bulk operations!**

## Statement Batch

```java
try (Statement stmt = conn.createStatement()) {
    conn.setAutoCommit(false);
    
    stmt.addBatch("INSERT INTO users (name) VALUES ('User1')");
    stmt.addBatch("INSERT INTO users (name) VALUES ('User2')");
    stmt.addBatch("INSERT INTO users (name) VALUES ('User3')");
    
    int[] results = stmt.executeBatch();
    conn.commit();
    
    System.out.println("Inserted " + results.length + " rows");
}
```

## PreparedStatement Batch (Preferred!)

```java
String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    conn.setAutoCommit(false);
    
    List<User> users = getUsers();  // 10,000 users
    int batchSize = 1000;
    int count = 0;
    
    for (User user : users) {
        pstmt.setString(1, user.getName());
        pstmt.setString(2, user.getEmail());
        pstmt.addBatch();
        
        // Execute every 1000 rows
        if (++count % batchSize == 0) {
            pstmt.executeBatch();
        }
    }
    
    // Execute remaining
    pstmt.executeBatch();
    conn.commit();
}
```

---

# 11. Connection Pooling

## Why Pool Connections?

Creating connections is **expensive**:
1. Network handshake
2. Authentication
3. Allocate resources

**Pool = Reuse existing connections!**

```
Without Pool:                    With Pool:
Create → Use → Destroy          Create once → Reuse → Reuse → Reuse
Create → Use → Destroy              ↑                            │
Create → Use → Destroy              └────────── Return ──────────┘
```

## HikariCP (Fastest & Recommended!)

```xml
<!-- Maven dependency -->
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.0.1</version>
</dependency>
```

```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// Configure pool
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
config.setUsername("root");
config.setPassword("password");
config.setMaximumPoolSize(10);
config.setMinimumIdle(5);
config.setIdleTimeout(300000);      // 5 minutes
config.setConnectionTimeout(10000); // 10 seconds
config.setMaxLifetime(1800000);     // 30 minutes

// Create data source
HikariDataSource dataSource = new HikariDataSource(config);

// Use connections
try (Connection conn = dataSource.getConnection()) {
    // Use connection
}  // Connection returned to pool, NOT closed!

// Shutdown when app closes
dataSource.close();
```

## DataSource Interface

```java
// DataSource is the standard way to get connections
public interface DataSource {
    Connection getConnection() throws SQLException;
    Connection getConnection(String username, String password) throws SQLException;
}

// Usage (abstraction over pooling implementation)
DataSource ds = getDataSource();  // HikariCP, C3P0, DBCP, etc.
try (Connection conn = ds.getConnection()) {
    // Use connection
}
```

---

# 12. RowSet

RowSet is a JavaBeans-style wrapper around ResultSet.

## Types of RowSet

| Type | Connected | Serializable | Use Case |
|------|-----------|--------------|----------|
| JdbcRowSet | Yes | No | Same as ResultSet |
| CachedRowSet | No | Yes | Disconnected operation |
| WebRowSet | No | Yes | XML serialization |
| FilteredRowSet | No | Yes | Client-side filtering |
| JoinRowSet | No | Yes | Join multiple RowSets |

## CachedRowSet Example

```java
import javax.sql.rowset.*;

// Create and populate
CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
rowSet.setUrl("jdbc:mysql://localhost:3306/mydb");
rowSet.setUsername("root");
rowSet.setPassword("password");
rowSet.setCommand("SELECT * FROM users WHERE age > ?");
rowSet.setInt(1, 18);
rowSet.execute();  // Fetches data, disconnects

// Now disconnected! Can work offline
while (rowSet.next()) {
    System.out.println(rowSet.getString("name"));
}

// Make changes offline
rowSet.absolute(3);
rowSet.updateString("name", "New Name");
rowSet.updateRow();

// Sync changes back to database
rowSet.acceptChanges();  // Reconnects and updates
```

---

# 13. Best Practices

## 1. Always Use Try-with-Resources

```java
// ✅ Good - auto-closes
try (Connection conn = dataSource.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql);
     ResultSet rs = pstmt.executeQuery()) {
    // Process
}

// ❌ Bad - manual closing, error-prone
Connection conn = null;
try {
    conn = dataSource.getConnection();
    // ...
} finally {
    if (conn != null) conn.close();  // Might not close on exception
}
```

## 2. Always Use PreparedStatement

```java
// ✅ Safe from SQL injection
PreparedStatement pstmt = conn.prepareStatement(
    "SELECT * FROM users WHERE id = ?");
pstmt.setInt(1, userId);

// ❌ Vulnerable to SQL injection!
Statement stmt = conn.createStatement();
stmt.executeQuery("SELECT * FROM users WHERE id = " + userId);
```

## 3. Use Connection Pooling in Production

```java
// ✅ Production - use pool
DataSource ds = createHikariDataSource();
try (Connection conn = ds.getConnection()) { }

// ❌ Development only - direct connection
Connection conn = DriverManager.getConnection(url, user, pass);
```

## 4. Batch for Bulk Operations

```java
// ✅ Fast - batch insert
pstmt.addBatch();
if (count % 1000 == 0) pstmt.executeBatch();

// ❌ Slow - individual inserts
pstmt.executeUpdate();  // Called 10,000 times
```

## 5. Handle SQLException Properly

```java
try {
    // JDBC operations
} catch (SQLException e) {
    // Log the error
    logger.error("Database error: " + e.getMessage());
    logger.error("SQL State: " + e.getSQLState());
    logger.error("Error Code: " + e.getErrorCode());
    
    // Handle chained exceptions
    SQLException next = e.getNextException();
    while (next != null) {
        logger.error("Next: " + next.getMessage());
        next = next.getNextException();
    }
    
    throw new ServiceException("Database operation failed", e);
}
```

## 6. Close Resources in Correct Order

```
Open:  Connection → Statement → ResultSet
Close: ResultSet → Statement → Connection (reverse!)
```

## 7. Use Column Names, Not Indices

```java
// ✅ Clear and refactor-safe
rs.getString("name");
rs.getInt("age");

// ❌ Fragile - breaks if column order changes
rs.getString(1);
rs.getInt(2);
```

---

# 14. Interview Questions

## Q1: What is JDBC?

**Answer:**
JDBC (Java Database Connectivity) is a Java API for connecting to relational databases. It provides standard interfaces for executing SQL queries, processing results, and managing transactions.

---

## Q2: Statement vs PreparedStatement vs CallableStatement?

**Answer:**
| Type | Use Case | Precompiled | SQL Injection |
|------|----------|-------------|---------------|
| Statement | Static SQL | No | Vulnerable |
| PreparedStatement | Parameterized SQL | Yes | Protected |
| CallableStatement | Stored procedures | Yes | Protected |

Always prefer **PreparedStatement** for security and performance!

---

## Q3: How to prevent SQL Injection?

**Answer:**
Use **PreparedStatement** with parameterized queries:

```java
// Safe
PreparedStatement pstmt = conn.prepareStatement(
    "SELECT * FROM users WHERE name = ?");
pstmt.setString(1, userInput);

// Unsafe!
stmt.executeQuery("SELECT * FROM users WHERE name = '" + userInput + "'");
```

---

## Q4: Explain JDBC transaction management.

**Answer:**
```java
conn.setAutoCommit(false);  // Start transaction
try {
    // Execute multiple statements
    conn.commit();          // Success - commit
} catch (SQLException e) {
    conn.rollback();        // Failure - rollback
}
```

ACID properties: Atomicity, Consistency, Isolation, Durability.

---

## Q5: What is connection pooling? Why use it?

**Answer:**
Connection pooling maintains a cache of reusable database connections.

**Benefits:**
- Avoids expensive connection creation
- Limits maximum connections
- Better resource management
- Improved performance

Popular pools: **HikariCP**, C3P0, Apache DBCP.

---

## Q6: ResultSet types and concurrency?

**Answer:**
**Types:**
- TYPE_FORWARD_ONLY: Default, can only move forward
- TYPE_SCROLL_INSENSITIVE: Can scroll, not sensitive to DB changes
- TYPE_SCROLL_SENSITIVE: Can scroll, reflects DB changes

**Concurrency:**
- CONCUR_READ_ONLY: Cannot update through ResultSet
- CONCUR_UPDATABLE: Can update/insert/delete through ResultSet

---

## Q7: What are the JDBC driver types?

**Answer:**
- **Type 1**: JDBC-ODBC Bridge (deprecated)
- **Type 2**: Native API (uses DB native libraries)
- **Type 3**: Network Protocol (middleware)
- **Type 4**: Pure Java/Thin Driver (most common, direct connection)

---

## Q8: How to handle batch inserts efficiently?

**Answer:**
```java
conn.setAutoCommit(false);
PreparedStatement pstmt = conn.prepareStatement(sql);

for (int i = 0; i < data.size(); i++) {
    pstmt.setString(1, data.get(i));
    pstmt.addBatch();
    
    if (i % 1000 == 0) {  // Execute in batches
        pstmt.executeBatch();
    }
}
pstmt.executeBatch();  // Remaining
conn.commit();
```

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│                    JDBC CHEAT SHEET                         │
├─────────────────────────────────────────────────────────────┤
│ CONNECTION                                                  │
│   Connection conn = DriverManager.getConnection(url, u, p); │
│   Connection conn = dataSource.getConnection();             │
├─────────────────────────────────────────────────────────────┤
│ PREPARED STATEMENT (Always use!)                            │
│   PreparedStatement ps = conn.prepareStatement(sql);        │
│   ps.setString(1, value);                                   │
│   ps.setInt(2, number);                                     │
│   ResultSet rs = ps.executeQuery();  // SELECT              │
│   int rows = ps.executeUpdate();     // INSERT/UPDATE/DELETE│
├─────────────────────────────────────────────────────────────┤
│ RESULT SET                                                  │
│   while (rs.next()) {                                       │
│       String name = rs.getString("column_name");            │
│       int id = rs.getInt("id");                             │
│   }                                                         │
├─────────────────────────────────────────────────────────────┤
│ TRANSACTIONS                                                │
│   conn.setAutoCommit(false);                                │
│   // ... operations ...                                     │
│   conn.commit();   // or conn.rollback();                   │
├─────────────────────────────────────────────────────────────┤
│ BATCH                                                       │
│   ps.addBatch();                                            │
│   ps.executeBatch();                                        │
└─────────────────────────────────────────────────────────────┘
```
