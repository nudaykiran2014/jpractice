# Proxy Pattern

## Intent
Provide a surrogate or placeholder for another object to control access to it.

## Problem
You need to control access to an object, add functionality when accessing it, or delay its creation until actually needed.

## Solution
Create a proxy class that implements the same interface as the real object. The proxy controls access to the real object and can add additional behavior.

## Structure
- **Subject**: Interface for RealSubject and Proxy
- **RealSubject**: Real object that proxy represents
- **Proxy**: Maintains reference to RealSubject, controls access to it
- **Client**: Works with subjects through Subject interface

## Types of Proxies
1. **Virtual Proxy**: Delays creation of expensive objects
2. **Protection Proxy**: Controls access based on permissions
3. **Remote Proxy**: Represents object in different address space
4. **Logging Proxy**: Logs requests before forwarding
5. **Caching Proxy**: Caches results of expensive operations
6. **Smart Reference**: Additional actions when object is accessed

## When to Use
- Need lazy initialization (virtual proxy)
- Access control needed (protection proxy)
- Local representation of remote object (remote proxy)
- Logging or auditing access
- Reference counting

## Benefits
- Controls access to real object
- Can add additional functionality transparently
- Lazy initialization saves resources
- Open/Closed Principle: introduce new proxies without changing service

## Drawbacks
- Increases complexity
- Response time may be slower
- Code becomes less straightforward

## Real-World Examples
- Java RMI (Remote Method Invocation)
- Lazy loading in ORMs (Hibernate)
- Spring AOP proxies
- Image loading placeholders
- Access control systems
- Network proxies
