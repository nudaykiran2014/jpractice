# Iterator Pattern

## Intent
Provide a way to access the elements of an aggregate object sequentially without exposing its underlying representation.

## Problem
You need to traverse a collection of objects without exposing its internal structure. Different collections may need different traversal methods.

## Solution
Extract traversal logic into a separate iterator object. The collection provides an iterator that knows how to traverse its elements.

## Structure
- **Iterator**: Interface for accessing and traversing elements
- **ConcreteIterator**: Implements Iterator interface, keeps track of current position
- **Aggregate**: Interface for creating Iterator
- **ConcreteAggregate**: Implements Iterator creation, returns ConcreteIterator

## When to Use
- Access aggregate object's contents without exposing internal representation
- Support multiple traversals of aggregate objects
- Provide uniform interface for traversing different aggregate structures

## Benefits
- Simplifies aggregate interface
- More than one traversal can be active on an aggregate
- Supports variations in traversal
- Clean separation of concerns

## Drawbacks
- Can be overkill for simple collections
- May be less efficient than direct access

## Real-World Examples
- Java Collections Framework (Iterator interface)
- Database result sets
- File system traversal
- Tree and graph traversals
- Menu navigation
- Playlist iteration
