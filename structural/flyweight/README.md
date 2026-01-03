# Flyweight Pattern

## Intent
Use sharing to support large numbers of fine-grained objects efficiently by sharing common state.

## Problem
Applications need to create a large number of objects that share common data, which can consume significant memory.

## Solution
Extract shared (intrinsic) state from objects and store it in flyweight objects that can be shared. Clients pass unique (extrinsic) state when calling flyweight methods.

## Structure
- **Flyweight**: Interface through which flyweights receive and act on extrinsic state
- **ConcreteFlyweight**: Implements Flyweight interface, stores intrinsic state
- **FlyweightFactory**: Creates and manages flyweight objects, ensures sharing
- **Client**: Maintains references to flyweights, computes/stores extrinsic state

## When to Use
- Application uses large number of objects
- Storage costs are high due to quantity
- Most object state can be made extrinsic
- Many groups of objects share intrinsic state
- Application doesn't depend on object identity

## Benefits
- Reduces memory consumption
- Can support large numbers of objects
- Intrinsic state is shared efficiently

## Drawbacks
- Increases complexity
- Runtime costs for computing extrinsic state
- Code becomes less intuitive

## Real-World Examples
- Text editors (character formatting)
- Game development (particles, bullets, trees)
- String interning in Java
- Connection pools
- Cache systems
- Graphics systems (shared textures)
