# Command Pattern

## Intent
Encapsulate a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations.

## Problem
You need to issue requests to objects without knowing anything about the operation being requested or the receiver of the request.

## Solution
Encapsulate requests as objects. These command objects can be stored, passed as parameters, and support operations like undo.

## Structure
- **Command**: Interface for executing an operation
- **ConcreteCommand**: Binds receiver object to an action
- **Invoker**: Asks command to carry out the request
- **Receiver**: Knows how to perform the operation
- **Client**: Creates ConcreteCommand and sets its receiver

## When to Use
- Parameterize objects with operations
- Specify, queue, and execute requests at different times
- Support undo operations
- Support logging changes for crash recovery
- Structure system around high-level operations built on primitive operations

## Benefits
- Decouples object that invokes operation from one that performs it
- Commands are first-class objects (manipulate and extend)
- Can assemble commands into composite commands
- Easy to add new commands
- Supports undo/redo
- Supports logging and transactions

## Drawbacks
- Increases number of classes
- Can be overly complex for simple operations

## Real-World Examples
- GUI buttons and menu items
- Macro recording
- Transaction systems
- Job queues
- Thread pools
- Wizard/multi-step processes
- Remote controls
