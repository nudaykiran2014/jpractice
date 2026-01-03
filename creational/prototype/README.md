# Prototype Pattern

## Intent
Specify the kinds of objects to create using a prototypical instance, and create new objects by copying this prototype.

## Problem
Creating new objects from scratch can be expensive, especially when object initialization is costly or complex. Sometimes you need to create many similar objects with only slight variations.

## Solution
Create new objects by cloning existing objects (prototypes). The prototype acts as a template from which new objects are copied.

## Structure
- **Prototype**: Interface declaring the clone method
- **ConcretePrototype**: Implements clone operation
- **Client**: Creates new objects by asking prototype to clone itself

## When to Use
- System should be independent of how products are created
- Classes to instantiate are specified at runtime
- Avoiding building class hierarchies of factories
- Object creation is expensive or complex
- Need to create objects that are similar to existing objects

## Benefits
- Reduces need for subclassing
- Hides complexity of creating new instances
- Adds and removes products at runtime
- Specifies new objects by varying values
- Reduces subclassing

## Drawbacks
- Each subclass must implement cloning
- Cloning complex objects with circular references can be difficult
- Deep vs shallow copy considerations

## Real-World Examples
- Cell division in biology
- Copy/paste operations
- Database record cloning
- Configuration objects
- Graphic editors (clone shapes)
- Game objects (enemies, items)
