# Decorator Pattern

## Intent
Attach additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing for extending functionality.

## Problem
You need to add responsibilities to individual objects, not to an entire class. Inheritance is inflexible and adds behavior statically.

## Solution
Wrap the original object with decorator objects that add new behavior. Decorators implement the same interface as the wrapped object.

## Structure
- **Component**: Interface for objects that can have responsibilities added
- **ConcreteComponent**: Object to which additional responsibilities can be attached
- **Decorator**: Maintains reference to Component and defines interface conforming to Component
- **ConcreteDecorator**: Adds responsibilities to the component

## When to Use
- Add responsibilities to individual objects dynamically and transparently
- Responsibilities can be withdrawn
- Extension by subclassing is impractical
- Need multiple independent extensions

## Benefits
- More flexibility than static inheritance
- Avoids feature-laden classes high in hierarchy
- Responsibilities can be added/removed at runtime
- Combine several behaviors by wrapping with multiple decorators

## Drawbacks
- Lots of small objects that differ only in how they're interconnected
- Can be hard to distinguish decorated object from original
- Decorators aren't identical to their component

## Real-World Examples
- Java I/O streams (BufferedReader, InputStreamReader)
- GUI component borders and scrollbars
- Pizza/coffee ordering systems with toppings
- Text formatting (bold, italic, underline)
- Middleware in web frameworks
