# Singleton Pattern

## Intent
Ensure a class has only one instance and provide a global point of access to it.

## Problem
Sometimes we need exactly one instance of a class (e.g., database connection, configuration manager, logging service). Creating multiple instances could cause resource conflicts or inconsistent state.

## Solution
Make the class responsible for keeping track of its sole instance. The class ensures no other instance can be created by intercepting requests for creating new objects.

## Structure
- Private constructor to prevent instantiation
- Static method that returns the single instance
- Static variable to hold the single instance

## When to Use
- Exactly one instance of a class is needed
- Instance must be accessible from a well-known access point
- The sole instance should be extensible by subclassing

## Benefits
- Controlled access to sole instance
- Reduced namespace pollution
- Permits refinement of operations and representation
- Lazy initialization possible

## Drawbacks
- Difficult to test due to global state
- Violates Single Responsibility Principle
- Can hide dependencies
- Thread-safety concerns in multithreaded environments

## Real-World Examples
- Logger classes
- Configuration managers
- Database connection pools
- Thread pools
- Cache managers
- Device drivers

## Implementation Types
1. **Eager Initialization** - Instance created at class loading
2. **Lazy Initialization** - Instance created when first requested
3. **Thread-Safe** - Uses synchronization for multithreaded environments
4. **Double-Checked Locking** - Optimized thread-safe version
5. **Bill Pugh Singleton** - Uses inner static helper class
6. **Enum Singleton** - Most robust implementation
