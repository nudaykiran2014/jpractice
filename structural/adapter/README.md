# Adapter Pattern

## Intent
Convert the interface of a class into another interface clients expect. Adapter lets classes work together that couldn't otherwise because of incompatible interfaces.

## Problem
You want to use an existing class but its interface doesn't match the one you need. You can't modify the existing class.

## Solution
Create an adapter class that wraps the incompatible object and translates calls to the interface the client expects.

## Structure
- **Target**: Interface that the client expects
- **Adapter**: Adapts the Adaptee to the Target interface
- **Adaptee**: Existing class with incompatible interface
- **Client**: Uses objects via Target interface

## When to Use
- Use an existing class with an incompatible interface
- Create reusable classes that cooperate with unrelated classes
- Need to use several existing subclasses but impractical to adapt by subclassing

## Benefits
- Single Responsibility Principle: separate interface conversion
- Open/Closed Principle: introduce new adapters without breaking existing code
- Increases reusability of existing code
- Allows incompatible interfaces to work together

## Drawbacks
- Increases overall complexity
- Sometimes simpler to just change the service class

## Real-World Examples
- Legacy code integration
- Third-party library integration
- XML to JSON converters
- Media format converters
- Database drivers
- Power adapters (real-world analogy)
