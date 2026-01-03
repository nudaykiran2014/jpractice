# Strategy Pattern

## Intent
Define a family of algorithms, encapsulate each one, and make them interchangeable. Strategy lets the algorithm vary independently from clients that use it.

## Problem
You have multiple algorithms for a specific task and want to switch between them at runtime. Using conditional statements creates inflexible and hard-to-maintain code.

## Solution
Extract algorithms into separate strategy classes with a common interface. The context delegates algorithm execution to a strategy object.

## Structure
- **Strategy**: Interface common to all supported algorithms
- **ConcreteStrategy**: Implements the algorithm using Strategy interface
- **Context**: Configured with a ConcreteStrategy, maintains reference to Strategy object

## When to Use
- Many related classes differ only in behavior
- Need different variants of an algorithm
- Algorithm uses data clients shouldn't know about
- Class defines many behaviors appearing as multiple conditional statements

## Benefits
- Algorithms can be switched at runtime
- Eliminates conditional statements
- Easier to add new strategies
- Promotes Open/Closed Principle
- Encapsulates algorithm implementation

## Drawbacks
- Increases number of objects
- Clients must be aware of different strategies
- Communication overhead between Strategy and Context

## Real-World Examples
- Sorting algorithms (QuickSort, MergeSort, BubbleSort)
- Payment methods (Credit Card, PayPal, Bitcoin)
- Compression algorithms (ZIP, RAR, TAR)
- Route finding (shortest, fastest, scenic)
- Validation strategies
- Authentication strategies
