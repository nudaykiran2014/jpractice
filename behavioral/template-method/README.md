# Template Method Pattern

## Intent
Define the skeleton of an algorithm in an operation, deferring some steps to subclasses. Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.

## Problem
You have an algorithm with multiple steps where some steps are common across implementations but others vary. Duplicating common code in subclasses leads to maintenance issues.

## Solution
Define a template method in an abstract class that calls both concrete and abstract methods. Subclasses override abstract methods to provide specific implementations.

## Structure
- **AbstractClass**: Defines abstract primitive operations, implements template method
- **ConcreteClass**: Implements primitive operations to carry out subclass-specific steps

## When to Use
- Implement invariant parts of an algorithm once
- Common behavior among subclasses should be factored into a common class
- Control subclass extensions with hook methods
- Avoid code duplication

## Benefits
- Code reuse through inheritance
- Controls which methods subclasses can override
- Inverts control structure (Hollywood Principle: "Don't call us, we'll call you")
- Easy to add new implementations

## Drawbacks
- Can be harder to understand
- Requires subclass for each variation
- Can violate Liskov Substitution Principle

## Real-World Examples
- Frameworks and libraries (JUnit test execution)
- Data parsers (CSV, JSON, XML)
- Game AI routines
- Database connection handling
- Document generation
- Cooking recipes
