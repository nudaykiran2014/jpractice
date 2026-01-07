
boss coder https://firebasestorage.googleapis.com/v0/b/bosscoderplatformindia.appspot.com/o/Brochure%2FCurriculum%2FEvolve%20Curriculum%20Brochure.pdf?alt=media&token=62ac6b7e-1512-45f0-b4ad-366444aeeb36

# Java Design Patterns

A comprehensive guide to all 23 Gang of Four (GoF) design patterns with Java implementations and explanations.

## Table of Contents

### Creational Patterns (5)
These patterns deal with object creation mechanisms.

1. **[Singleton](./creational/singleton/README.md)** - Ensures a class has only one instance
2. **[Factory Method](./creational/factory-method/README.md)** - Creates objects without specifying exact class
3. **[Abstract Factory](./creational/abstract-factory/README.md)** - Creates families of related objects
4. **[Builder](./creational/builder/README.md)** - Constructs complex objects step by step
5. **[Prototype](./creational/prototype/README.md)** - Creates objects by cloning existing ones

### Structural Patterns (7)
These patterns deal with object composition and relationships.

6. **[Adapter](./structural/adapter/README.md)** - Converts interface to match client expectations
7. **[Bridge](./structural/bridge/README.md)** - Separates abstraction from implementation
8. **[Composite](./structural/composite/README.md)** - Composes objects into tree structures
9. **[Decorator](./structural/decorator/README.md)** - Adds behavior to objects dynamically
10. **[Facade](./structural/facade/README.md)** - Provides simplified interface to complex system
11. **[Flyweight](./structural/flyweight/README.md)** - Shares objects to support large numbers efficiently
12. **[Proxy](./structural/proxy/README.md)** - Provides placeholder for another object

### Behavioral Patterns (11)
These patterns deal with communication between objects.

13. **[Chain of Responsibility](./behavioral/chain-of-responsibility/README.md)** - Passes request along chain of handlers
14. **[Command](./behavioral/command/README.md)** - Encapsulates request as an object
15. **[Interpreter](./behavioral/interpreter/README.md)** - Defines grammar and interprets sentences
16. **[Iterator](./behavioral/iterator/README.md)** - Accesses elements sequentially
17. **[Mediator](./behavioral/mediator/README.md)** - Defines simplified communication between classes
18. **[Memento](./behavioral/memento/README.md)** - Captures and restores object state
19. **[Observer](./behavioral/observer/README.md)** - Notifies dependents of state changes
20. **[State](./behavioral/state/README.md)** - Alters behavior when state changes
21. **[Strategy](./behavioral/strategy/README.md)** - Encapsulates algorithms as interchangeable objects
22. **[Template Method](./behavioral/template-method/README.md)** - Defines algorithm skeleton in base class
23. **[Visitor](./behavioral/visitor/README.md)** - Separates algorithm from object structure

## How to Use

Each pattern folder contains:
- `README.md` - Detailed explanation with use cases and benefits
- Java source files - Complete working implementation
- Example usage and output

## Running the Examples

```bash
# Compile a pattern example
javac creational/singleton/*.java

# Run the example
java creational.singleton.SingletonDemo
```

## Pattern Selection Guide

- **Need only one instance?** → Singleton
- **Object creation is complex?** → Factory Method, Abstract Factory, or Builder
- **Need to copy objects?** → Prototype
- **Incompatible interfaces?** → Adapter
- **Separate abstraction from implementation?** → Bridge
- **Tree structures?** → Composite
- **Add responsibilities dynamically?** → Decorator
- **Simplify complex subsystem?** → Facade
- **Memory optimization for many objects?** → Flyweight
- **Control access to object?** → Proxy
- **Request handling chain?** → Chain of Responsibility
- **Undo/Redo operations?** → Command or Memento
- **Parse language?** → Interpreter
- **Traverse collection?** → Iterator
- **Complex communication between objects?** → Mediator
- **Save/restore state?** → Memento
- **One-to-many dependency?** → Observer
- **Behavior changes with state?** → State
- **Interchangeable algorithms?** → Strategy
- **Common algorithm structure?** → Template Method
- **Operations on object structure?** → Visitor
