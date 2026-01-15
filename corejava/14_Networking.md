# Java Networking - Complete Guide

## Table of Contents
1. [Networking Basics](#1-networking-basics)
2. [InetAddress](#2-inetaddress)
3. [URL & URI](#3-url--uri)
4. [Sockets](#4-sockets)
5. [TCP Client-Server](#5-tcp-client-server)
6. [UDP Communication](#6-udp-communication)
7. [HTTP Client (Java 11+)](#7-http-client-java-11)
8. [URLConnection (Legacy)](#8-urlconnection-legacy)
9. [Non-blocking I/O (NIO)](#9-non-blocking-io-nio)
10. [Interview Questions](#10-interview-questions)

---

# 1. Networking Basics

## Kid-Friendly Explanation 🧒

**Think of networking like sending letters:**

- **IP Address** = House address (where to deliver)
- **Port** = Room number in the house (which application)
- **Protocol** = Language (TCP = reliable registered mail, UDP = fast postcard)
- **Socket** = Mailbox (endpoint for communication)

```
┌──────────────────┐                    ┌──────────────────┐
│     CLIENT       │                    │      SERVER      │
│                  │                    │                  │
│  Socket ─────────┼──── Internet ─────┼───── Socket      │
│  (Port 54321)    │                    │   (Port 8080)    │
│                  │                    │                  │
│ IP: 192.168.1.10 │                    │ IP: 93.184.216.34│
└──────────────────┘                    └──────────────────┘
```

## TCP vs UDP

| Feature | TCP | UDP |
|---------|-----|-----|
| Connection | Connection-oriented | Connectionless |
| Reliability | Guaranteed delivery | No guarantee |
| Order | Preserves order | May arrive out of order |
| Speed | Slower | Faster |
| Use Case | HTTP, Email, File transfer | Streaming, Gaming, DNS |
| Java Classes | Socket, ServerSocket | DatagramSocket, DatagramPacket |

## Common Port Numbers

| Port | Protocol | Use |
|------|----------|-----|
| 21 | FTP | File transfer |
| 22 | SSH | Secure shell |
| 25 | SMTP | Email sending |
| 53 | DNS | Domain resolution |
| 80 | HTTP | Web (unencrypted) |
| 443 | HTTPS | Web (encrypted) |
| 3306 | MySQL | Database |
| 5432 | PostgreSQL | Database |
| 8080 | HTTP Alt | Development servers |

---

# 2. InetAddress

Represents an IP address (IPv4 or IPv6).

## Getting InetAddress

```java
import java.net.InetAddress;

// Local host
InetAddress localhost = InetAddress.getLocalHost();
System.out.println("Local: " + localhost);  // hostname/192.168.1.10

// By hostname
InetAddress google = InetAddress.getByName("www.google.com");
System.out.println("Google: " + google);  // www.google.com/142.250.x.x

// By IP address
InetAddress byIp = InetAddress.getByName("8.8.8.8");
System.out.println("DNS: " + byIp);  // /8.8.8.8

// All addresses for a hostname (multiple IPs)
InetAddress[] allAddresses = InetAddress.getAllByName("www.google.com");
for (InetAddress addr : allAddresses) {
    System.out.println(addr);
}

// Loopback
InetAddress loopback = InetAddress.getLoopbackAddress();
System.out.println("Loopback: " + loopback);  // localhost/127.0.0.1
```

## InetAddress Methods

```java
InetAddress addr = InetAddress.getByName("www.example.com");

addr.getHostName();         // "www.example.com"
addr.getHostAddress();      // "93.184.216.34"
addr.getAddress();          // byte[] of IP
addr.isReachable(5000);     // Ping with 5s timeout
addr.isLoopbackAddress();   // Is 127.0.0.1?
addr.isAnyLocalAddress();   // Is 0.0.0.0?
addr.isSiteLocalAddress();  // Is private IP? (192.168.x.x, 10.x.x.x)
```

---

# 3. URL & URI

## URI (Uniform Resource Identifier)

Identifies a resource. May or may not be locatable.

```java
import java.net.URI;

URI uri = new URI("https://user:pass@example.com:8080/path/file?query=value#fragment");

uri.getScheme();      // "https"
uri.getUserInfo();    // "user:pass"
uri.getHost();        // "example.com"
uri.getPort();        // 8080
uri.getPath();        // "/path/file"
uri.getQuery();       // "query=value"
uri.getFragment();    // "fragment"

// Create URI
URI uri2 = URI.create("https://example.com/api");

// Resolve relative URIs
URI base = URI.create("https://example.com/api/");
URI relative = URI.create("users/123");
URI resolved = base.resolve(relative);  // https://example.com/api/users/123
```

## URL (Uniform Resource Locator)

Locatable resource. Can open connection.

```java
import java.net.URL;

URL url = new URL("https://example.com:8080/path/file?query=value");

// Same methods as URI plus:
url.getProtocol();     // "https"
url.getDefaultPort();  // 443 (for https)
url.getFile();         // "/path/file?query=value"

// Open connection
InputStream is = url.openStream();  // Read content
URLConnection conn = url.openConnection();

// Convert between URL and URI
URI uri = url.toURI();
URL url2 = uri.toURL();
```

---

# 4. Sockets

## Kid-Friendly Explanation 🧒

**Socket = Phone Call 📞**

- **ServerSocket** = Phone that can RECEIVE calls (waiting for connections)
- **Socket** = The actual phone call (active connection)

```
Server                              Client
  │                                   │
  │  ServerSocket (listening)         │
  │       ↓                           │
  │  accept() ← ────────────── ── ────Socket connects
  │       ↓                           │
  │  Socket (connected) ←─────────────Socket (connected)
  │       │                           │
  │  InputStream ←────── data ───────OutputStream
  │  OutputStream ────── data ──────→InputStream
```

## Socket Classes Overview

| Class | Description |
|-------|-------------|
| `Socket` | Client-side TCP socket |
| `ServerSocket` | Server-side TCP socket (listens for connections) |
| `DatagramSocket` | UDP socket (connectionless) |
| `DatagramPacket` | UDP packet (data container) |
| `SocketChannel` | NIO TCP channel |
| `ServerSocketChannel` | NIO server channel |
| `DatagramChannel` | NIO UDP channel |

---

# 5. TCP Client-Server

## Simple TCP Server

```java
import java.net.*;
import java.io.*;

public class SimpleServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);
            
            while (true) {
                // Accept connection (blocks until client connects)
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + 
                    clientSocket.getInetAddress());
                
                // Handle client in new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        }
    }
    
    private static void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true)
        ) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                out.println("Echo: " + message);  // Send response
                
                if ("bye".equalsIgnoreCase(message)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException e) { }
        }
    }
}
```

## Simple TCP Client

```java
import java.net.*;
import java.io.*;

public class SimpleClient {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 8080;
        
        try (
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(
                new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server");
            
            String message;
            while ((message = userInput.readLine()) != null) {
                out.println(message);  // Send to server
                String response = in.readLine();  // Read response
                System.out.println("Server: " + response);
                
                if ("bye".equalsIgnoreCase(message)) {
                    break;
                }
            }
        }
    }
}
```

## Socket Options

```java
Socket socket = new Socket();

// Connection timeout
socket.connect(new InetSocketAddress(host, port), 5000);  // 5s timeout

// Read timeout
socket.setSoTimeout(10000);  // 10s read timeout

// Keep alive
socket.setKeepAlive(true);

// Buffer sizes
socket.setReceiveBufferSize(8192);
socket.setSendBufferSize(8192);

// Reuse address (for servers)
serverSocket.setReuseAddress(true);

// TCP no delay (disable Nagle's algorithm)
socket.setTcpNoDelay(true);

// Linger on close
socket.setSoLinger(true, 10);  // Wait up to 10s for data to send
```

## Multi-threaded Server with ExecutorService

```java
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ThreadPoolServer {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;
    
    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(new ClientHandler(clientSocket));
            }
        } finally {
            executor.shutdown();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                out.println("Processed: " + line.toUpperCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

---

# 6. UDP Communication

## UDP Server

```java
import java.net.*;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        int port = 9999;
        byte[] buffer = new byte[1024];
        
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server listening on port " + port);
            
            while (true) {
                // Receive packet
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);  // Blocks until packet arrives
                
                String message = new String(
                    request.getData(), 0, request.getLength());
                System.out.println("Received: " + message);
                
                // Send response
                String response = "Echo: " + message;
                byte[] responseData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(
                    responseData,
                    responseData.length,
                    request.getAddress(),
                    request.getPort()
                );
                socket.send(responsePacket);
            }
        }
    }
}
```

## UDP Client

```java
import java.net.*;

public class UDPClient {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 9999;
        
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(host);
            
            // Send packet
            String message = "Hello UDP!";
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                sendData,
                sendData.length,
                serverAddress,
                port
            );
            socket.send(sendPacket);
            
            // Receive response
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(
                receiveData, receiveData.length);
            socket.receive(receivePacket);
            
            String response = new String(
                receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Server: " + response);
        }
    }
}
```

## Multicast (UDP Group Communication)

```java
import java.net.*;

// Receiver (joins multicast group)
public class MulticastReceiver {
    public static void main(String[] args) throws Exception {
        String group = "230.0.0.1";  // Multicast address
        int port = 4446;
        
        try (MulticastSocket socket = new MulticastSocket(port)) {
            InetAddress groupAddr = InetAddress.getByName(group);
            socket.joinGroup(groupAddr);
            
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            System.out.println("Waiting for multicast messages...");
            socket.receive(packet);
            
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received: " + message);
            
            socket.leaveGroup(groupAddr);
        }
    }
}

// Sender (sends to multicast group)
public class MulticastSender {
    public static void main(String[] args) throws Exception {
        String group = "230.0.0.1";
        int port = 4446;
        
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress groupAddr = InetAddress.getByName(group);
            
            String message = "Hello Multicast!";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(
                data, data.length, groupAddr, port);
            
            socket.send(packet);
            System.out.println("Message sent to multicast group");
        }
    }
}
```

---

# 7. HTTP Client (Java 11+)

## Kid-Friendly Explanation 🧒

The modern way to make HTTP requests in Java - like a super-powered web browser in your code!

## Creating HttpClient

```java
import java.net.http.*;
import java.net.URI;
import java.time.Duration;

// Simple client
HttpClient client = HttpClient.newHttpClient();

// Customized client
HttpClient client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .connectTimeout(Duration.ofSeconds(10))
    .followRedirects(HttpClient.Redirect.NORMAL)
    .build();
```

## GET Request

```java
HttpClient client = HttpClient.newHttpClient();

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users"))
    .GET()  // Default, can be omitted
    .header("Accept", "application/json")
    .timeout(Duration.ofSeconds(30))
    .build();

// Synchronous
HttpResponse<String> response = client.send(
    request, 
    HttpResponse.BodyHandlers.ofString()
);

System.out.println("Status: " + response.statusCode());
System.out.println("Body: " + response.body());
System.out.println("Headers: " + response.headers());
```

## POST Request

```java
// POST with JSON body
String json = """
    {
        "name": "John",
        "email": "john@example.com"
    }
    """;

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(json))
    .build();

HttpResponse<String> response = client.send(
    request, 
    HttpResponse.BodyHandlers.ofString()
);

// POST with form data
HttpRequest formRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/login"))
    .header("Content-Type", "application/x-www-form-urlencoded")
    .POST(HttpRequest.BodyPublishers.ofString(
        "username=john&password=secret"))
    .build();
```

## Other HTTP Methods

```java
// PUT
HttpRequest putRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users/123"))
    .PUT(HttpRequest.BodyPublishers.ofString(json))
    .build();

// DELETE
HttpRequest deleteRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users/123"))
    .DELETE()
    .build();

// PATCH (custom method)
HttpRequest patchRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users/123"))
    .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
    .build();
```

## Asynchronous Requests

```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users"))
    .build();

// Async - returns CompletableFuture
CompletableFuture<HttpResponse<String>> future = client.sendAsync(
    request,
    HttpResponse.BodyHandlers.ofString()
);

// Handle response when ready
future.thenApply(HttpResponse::body)
      .thenAccept(System.out::println)
      .join();  // Wait for completion

// Multiple async requests
List<URI> uris = List.of(
    URI.create("https://api.example.com/users/1"),
    URI.create("https://api.example.com/users/2"),
    URI.create("https://api.example.com/users/3")
);

List<CompletableFuture<String>> futures = uris.stream()
    .map(uri -> HttpRequest.newBuilder(uri).build())
    .map(req -> client.sendAsync(req, HttpResponse.BodyHandlers.ofString()))
    .map(cf -> cf.thenApply(HttpResponse::body))
    .toList();

// Wait for all
CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
```

## Response Body Handlers

```java
// As String
HttpResponse<String> strResponse = client.send(
    request, HttpResponse.BodyHandlers.ofString());

// As byte array
HttpResponse<byte[]> bytesResponse = client.send(
    request, HttpResponse.BodyHandlers.ofByteArray());

// As InputStream
HttpResponse<InputStream> streamResponse = client.send(
    request, HttpResponse.BodyHandlers.ofInputStream());

// Save to file
HttpResponse<Path> fileResponse = client.send(
    request, HttpResponse.BodyHandlers.ofFile(Path.of("output.txt")));

// Discard body
HttpResponse<Void> discardResponse = client.send(
    request, HttpResponse.BodyHandlers.discarding());

// As lines (Stream<String>)
HttpResponse<Stream<String>> linesResponse = client.send(
    request, HttpResponse.BodyHandlers.ofLines());
```

## WebSocket Client

```java
HttpClient client = HttpClient.newHttpClient();

WebSocket webSocket = client.newWebSocketBuilder()
    .buildAsync(
        URI.create("wss://echo.websocket.org"),
        new WebSocket.Listener() {
            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println("Connected!");
                webSocket.sendText("Hello WebSocket!", true);
                WebSocket.Listener.super.onOpen(webSocket);
            }
            
            @Override
            public CompletionStage<?> onText(WebSocket webSocket, 
                    CharSequence data, boolean last) {
                System.out.println("Received: " + data);
                return WebSocket.Listener.super.onText(webSocket, data, last);
            }
            
            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }
        })
    .join();

// Send message
webSocket.sendText("Another message", true);

// Close
webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Goodbye");
```

---

# 8. URLConnection (Legacy)

For older Java versions (pre-Java 11) or simple use cases.

## GET Request

```java
import java.net.*;
import java.io.*;

URL url = new URL("https://api.example.com/users");
HttpURLConnection conn = (HttpURLConnection) url.openConnection();

conn.setRequestMethod("GET");
conn.setRequestProperty("Accept", "application/json");
conn.setConnectTimeout(5000);
conn.setReadTimeout(10000);

int status = conn.getResponseCode();
System.out.println("Status: " + status);

// Read response
try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(conn.getInputStream()))) {
    String line;
    StringBuilder response = new StringBuilder();
    while ((line = reader.readLine()) != null) {
        response.append(line);
    }
    System.out.println("Response: " + response);
}

conn.disconnect();
```

## POST Request

```java
URL url = new URL("https://api.example.com/users");
HttpURLConnection conn = (HttpURLConnection) url.openConnection();

conn.setRequestMethod("POST");
conn.setRequestProperty("Content-Type", "application/json");
conn.setDoOutput(true);  // Enable sending body

// Write body
String json = "{\"name\": \"John\"}";
try (OutputStream os = conn.getOutputStream()) {
    os.write(json.getBytes());
    os.flush();
}

int status = conn.getResponseCode();

// Read response...
```

---

# 9. Non-blocking I/O (NIO)

## Why NIO for Networking?

- **Traditional I/O**: One thread per connection (doesn't scale)
- **NIO**: One thread handles many connections (scales well)

```
Traditional (Blocking)          NIO (Non-blocking)
┌─────────────────┐            ┌─────────────────┐
│ Thread 1 → Client 1 │        │                 │
│ Thread 2 → Client 2 │        │  1 Thread →     │
│ Thread 3 → Client 3 │        │    Selector →   │
│ Thread 4 → Client 4 │        │      Client 1   │
│ Thread 5 → Client 5 │        │      Client 2   │
│     ...             │        │      Client 3   │
└─────────────────┘            │      ...        │
                               └─────────────────┘
```

## Selector-based Server

```java
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;

public class NIOServer {
    public static void main(String[] args) throws Exception {
        // Create selector
        Selector selector = Selector.open();
        
        // Create server channel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8080));
        serverChannel.configureBlocking(false);
        
        // Register for accept events
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        System.out.println("NIO Server started on port 8080");
        
        while (true) {
            // Wait for events
            selector.select();
            
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                
                if (key.isAcceptable()) {
                    // New connection
                    handleAccept(serverChannel, selector);
                } else if (key.isReadable()) {
                    // Data available to read
                    handleRead(key);
                }
            }
        }
    }
    
    private static void handleAccept(ServerSocketChannel serverChannel,
            Selector selector) throws Exception {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("Client connected: " + clientChannel.getRemoteAddress());
    }
    
    private static void handleRead(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        int bytesRead = channel.read(buffer);
        if (bytesRead == -1) {
            channel.close();
            return;
        }
        
        buffer.flip();
        String message = new String(buffer.array(), 0, bytesRead);
        System.out.println("Received: " + message);
        
        // Echo back
        buffer.rewind();
        channel.write(buffer);
    }
}
```

## SelectionKey Operations

| Constant | Meaning |
|----------|---------|
| `OP_ACCEPT` | Ready to accept new connection |
| `OP_CONNECT` | Connection finished |
| `OP_READ` | Ready to read |
| `OP_WRITE` | Ready to write |

---

# 10. Interview Questions

## Q1: What is a Socket?

**Answer:**
A Socket is an endpoint for communication between two machines. It combines an IP address and port number to identify a specific process on a network.

- **Server Socket**: Listens for incoming connections
- **Client Socket**: Initiates connections

---

## Q2: Difference between TCP and UDP?

**Answer:**
| TCP | UDP |
|-----|-----|
| Connection-oriented | Connectionless |
| Reliable, ordered delivery | Unreliable, may lose packets |
| Slower (handshake, acknowledgments) | Faster (no overhead) |
| HTTP, HTTPS, FTP, Email | DNS, Streaming, Gaming |
| Socket/ServerSocket | DatagramSocket/DatagramPacket |

---

## Q3: How to handle multiple clients in a server?

**Answer:**
1. **Thread per client**: Simple but doesn't scale
   ```java
   new Thread(() -> handleClient(socket)).start();
   ```

2. **Thread pool**: Better resource management
   ```java
   ExecutorService executor = Executors.newFixedThreadPool(100);
   executor.submit(() -> handleClient(socket));
   ```

3. **NIO Selector**: Single thread handles many connections
   ```java
   selector.select();
   // Handle multiple channels
   ```

---

## Q4: What is HTTP/2 and how does Java support it?

**Answer:**
HTTP/2 features:
- Multiplexing (multiple requests on single connection)
- Header compression
- Server push
- Binary protocol

Java 11+ HttpClient supports it:
```java
HttpClient client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .build();
```

---

## Q5: Explain the Java NIO Selector pattern.

**Answer:**
Selector allows a single thread to monitor multiple channels:

1. Create `Selector`
2. Register channels with selector for events (ACCEPT, READ, WRITE)
3. Call `selector.select()` - blocks until event occurs
4. Get selected keys and handle each event
5. Loop

Benefits: Scalable, efficient for many connections.

---

## Q6: How to make HTTP requests in Java?

**Answer:**
**Java 11+ (Recommended):**
```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com"))
    .build();
HttpResponse<String> response = client.send(request, 
    HttpResponse.BodyHandlers.ofString());
```

**Legacy (Pre-Java 11):**
```java
URL url = new URL("https://api.example.com");
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
// Read from conn.getInputStream()
```

---

## Q7: What is the difference between synchronous and asynchronous HTTP calls?

**Answer:**
**Synchronous:** Blocks until response
```java
HttpResponse<String> response = client.send(request, handler);
// Thread waits here
```

**Asynchronous:** Returns immediately, handles response later
```java
CompletableFuture<HttpResponse<String>> future = 
    client.sendAsync(request, handler);
future.thenAccept(response -> {
    // Handle response when ready
});
// Thread continues immediately
```

---

# Quick Reference

```
┌─────────────────────────────────────────────────────────────┐
│                 NETWORKING CHEAT SHEET                      │
├─────────────────────────────────────────────────────────────┤
│ TCP CLIENT                                                  │
│   Socket socket = new Socket(host, port);                   │
│   socket.getInputStream() / getOutputStream()               │
├─────────────────────────────────────────────────────────────┤
│ TCP SERVER                                                  │
│   ServerSocket server = new ServerSocket(port);             │
│   Socket client = server.accept();  // Blocks               │
├─────────────────────────────────────────────────────────────┤
│ UDP                                                         │
│   DatagramSocket socket = new DatagramSocket();             │
│   DatagramPacket packet = new DatagramPacket(data, len);    │
│   socket.send(packet) / socket.receive(packet)              │
├─────────────────────────────────────────────────────────────┤
│ HTTP CLIENT (Java 11+)                                      │
│   HttpClient client = HttpClient.newHttpClient();           │
│   HttpRequest request = HttpRequest.newBuilder()            │
│       .uri(URI.create(url))                                 │
│       .GET() / .POST(BodyPublishers.ofString(body))         │
│       .build();                                             │
│   HttpResponse<String> response = client.send(request,      │
│       BodyHandlers.ofString());                             │
├─────────────────────────────────────────────────────────────┤
│ NIO                                                         │
│   Selector selector = Selector.open();                      │
│   channel.register(selector, SelectionKey.OP_READ);         │
│   selector.select();  // Wait for events                    │
└─────────────────────────────────────────────────────────────┘
```
