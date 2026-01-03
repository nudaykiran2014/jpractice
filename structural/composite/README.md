# Composite Pattern

## Intent
Compose objects into tree structures to represent part-whole hierarchies. Composite lets clients treat individual objects and compositions of objects uniformly.

## Problem
You need to represent hierarchical structures where individual objects and groups of objects should be treated the same way.

## Solution
Define classes for both primitive objects and containers. Make container classes hold collections of the base component type, which can be either primitives or other containers.

## Structure
- **Component**: Interface for objects in the composition
- **Leaf**: Primitive object with no children
- **Composite**: Component that has children, implements child-related operations
- **Client**: Manipulates objects through Component interface

## When to Use
- Represent part-whole hierarchies of objects
- Clients should ignore difference between compositions and individual objects
- Tree structure representation needed

## Benefits
- Makes client code simple
- Easy to add new component types
- Provides flexibility of structure

## Drawbacks
- Can make design overly general
- Hard to restrict component types in a composite

## Real-World Examples
- File system directories and files
- Organization hierarchies
- GUI component trees
- Graphics drawing applications
- Menu systems
- XML/HTML DOM structures
