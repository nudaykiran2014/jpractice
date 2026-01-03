# Chain of Responsibility Pattern

## Intent
Avoid coupling the sender of a request to its receiver by giving more than one object a chance to handle the request. Chain the receiving objects and pass the request along the chain until an object handles it.

## Problem
Multiple objects may handle a request, but you don't want to hardcode which object handles it. The handler should be determined dynamically.

## Solution
Create a chain of handler objects. Each handler decides whether to process the request or pass it to the next handler in the chain.

## Structure
- **Handler**: Interface for handling requests, optionally implements successor link
- **ConcreteHandler**: Handles requests it's responsible for, accesses successor
- **Client**: Initiates request to a ConcreteHandler in the chain

## When to Use
- More than one object may handle a request
- Handler should be ascertained automatically
- Request should be issued without specifying handler explicitly
- Set of objects that can handle request should be specified dynamically

## Benefits
- Reduces coupling between sender and receiver
- Adds flexibility in assigning responsibilities
- Can change chain at runtime
- Each handler focuses on single responsibility

## Drawbacks
- Request might go unhandled
- Can be hard to debug and observe runtime characteristics
- Performance concerns with long chains

## Real-World Examples
- Event handling systems (UI events)
- Logging frameworks (different log levels)
- Exception handling
- Servlet filters
- Middleware in web frameworks
- Support ticket routing systems
