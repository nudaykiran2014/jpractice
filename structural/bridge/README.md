# Bridge Pattern

## Intent
Decouple an abstraction from its implementation so that the two can vary independently.

## Problem
When an abstraction can have multiple implementations, inheritance creates a tight coupling between abstraction and implementation, making it difficult to modify them independently.

## Solution
Separate the abstraction hierarchy from the implementation hierarchy. The abstraction contains a reference to the implementation and delegates work to it.

## Structure
- **Abstraction**: Defines abstraction's interface, maintains reference to Implementor
- **RefinedAbstraction**: Extends the interface defined by Abstraction
- **Implementor**: Interface for implementation classes
- **ConcreteImplementor**: Implements the Implementor interface

## When to Use
- Avoid permanent binding between abstraction and implementation
- Both abstraction and implementation should be extensible by subclassing
- Changes in implementation shouldn't impact clients
- Share implementation among multiple objects

## Benefits
- Decouples interface from implementation
- Improves extensibility
- Hides implementation details from clients
- Reduces number of subclasses

## Drawbacks
- Increases complexity
- Requires identifying orthogonal dimensions

## Real-World Examples
- Device drivers for different OS
- Graphics systems (shape + rendering)
- Database abstraction layers
- Remote controls and devices
- GUI frameworks across platforms
